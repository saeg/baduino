package br.usp.each.saeg.baduino.handlers;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;

import br.usp.each.saeg.baduino.core.launching.Launcher;
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
			
			final Launcher launcher = new Launcher(model);
			launcher.addListener(new VMListener() {
				@Override
				public void terminated() {
					try {
						final BaduinoReport report = new BaduinoReport(model);
						report.reportAll();
						report.writeXML();
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
