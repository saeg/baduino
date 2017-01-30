package br.usp.each.saeg.baduino.handlers;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

import org.apache.log4j.Logger;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;

import br.usp.each.saeg.baduino.core.launching.VMLauncher;
import br.usp.each.saeg.baduino.core.launching.VMListener;
import br.usp.each.saeg.baduino.core.logger.LoggerManager;
import br.usp.each.saeg.baduino.core.model.ProjectModel;
import br.usp.each.saeg.baduino.core.model.ProjectModelBuilder;
import br.usp.each.saeg.baduino.core.model.ProjectModelManager;
import br.usp.each.saeg.baduino.core.report.BaduinoReport;
import br.usp.each.saeg.baduino.util.ProjectUtils;

public class RunTestsHandler extends AbstractHandler implements IJavaLaunchConfigurationConstants {
	
	private static final Logger logger = Logger.getLogger(RunTestsHandler.class);
	
	@Override
	public Object execute(ExecutionEvent arg) throws ExecutionException {
		final Instant start = Instant.now();
		LoggerManager.setupLogger();
		
		try {
			final IJavaProject javaProject = ProjectUtils.getCurrentSelectedJavaProject();
			final ProjectModel model = ProjectModelBuilder.buildModel(javaProject);
			
			logger.info("Building project: " + model.getName());
			logger.info("Project location: " + model.getLocation());
			logger.info("src: " + model.getClassesPath());
			logger.info("dest: " + model.getBaduinoPath());
			
			model.build();
			ProjectModelManager.writeToDisk(model);
			
			/*
			VMListener reportListener = new VMListener() {
				@Override
				public void terminated() {
					try {
						VisualizationHandler.openView();
					}
					catch (PartInitException e) {
						e.printStackTrace();
					}
				}
			};
			*/
			
			final VMLauncher launcher = new VMLauncher(model);
			launcher.addListener(new VMListener() {
				@Override
				public void terminated() {
					try {
						final BaduinoReport report = new BaduinoReport(model);
//						report.addListener(reportListener);
						report.reportAll();
						report.writeXML();
						
						final Instant end = Instant.now();
						Duration duration = Duration.between(start, end);
						double total = duration.toMillis()/1000.0;
						logger.info(String.format("Baduino took %.3f seconds to execute.", total));
					} 
					catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
			
			launcher.launch();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public boolean isEnabled() {
		final IProject project = ProjectUtils.getCurrentSelectedProject();
		return (project != null);
	}
	
}
