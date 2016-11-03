package br.usp.each.saeg.baduino.core.launching;

import static org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants.ATTR_CLASSPATH;
import static org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants.ATTR_DEFAULT_CLASSPATH;
import static org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME;
import static org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants.ATTR_PROGRAM_ARGUMENTS;
import static org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants.ATTR_VM_ARGUMENTS;
import static org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants.ID_JAVA_APPLICATION;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.core.ILaunchesListener2;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.launching.IRuntimeClasspathEntry;
import org.eclipse.jdt.launching.JavaRuntime;
import org.hamcrest.SelfDescribing;
import org.junit.runner.JUnitCore;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.Versioned;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.usp.each.saeg.badua.agent.rt.internal.PreMain;
import br.usp.each.saeg.badua.commons.time.TimeWatch;
import br.usp.each.saeg.baduino.core.model.ProjectModel;
import br.usp.each.saeg.baduino.core.runner.BaduinoRunner;

/**
 * This class creates and launches a new VM.
 * @author Mario Concilio
 *
 */
public class Launcher {
	
	private static final Logger logger = Logger.getLogger(Launcher.class);

	private final IJavaProject javaProject;
	private final ILaunchManager manager;
	private final ILaunchConfigurationWorkingCopy workingCopy;
	private final List<String> classpath;
	
	public Launcher(final ProjectModel model) throws CoreException {
		this.javaProject = model.getJavaProject();
		this.classpath = new ArrayList<String>();
		this.manager = DebugPlugin.getDefault().getLaunchManager();
		
		final ILaunchConfigurationType type = manager.getLaunchConfigurationType(ID_JAVA_APPLICATION);
		workingCopy = type.newInstance(null, "Baduino");
		configClasspath(model);
	}
	
	public void launch(Consumer<Void> consumer) throws CoreException {
		logger.debug("Launching new VM");
		manager.addLaunchListener(new ILaunchesListener2() {
			@Override
			public void launchesRemoved(ILaunch[] launches) {}

			@Override
			public void launchesAdded(ILaunch[] launches) {}

			@Override
			public void launchesChanged(ILaunch[] launches) {}

			@Override
			public void launchesTerminated(ILaunch[] launches) {
				try {
					Thread.sleep(1000);
					consumer.accept(null);
				} 
				catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
		});
		
		final ILaunchConfiguration configuration = workingCopy.doSave();
//		configuration.launch(ILaunchManager.RUN_MODE, null);
		DebugUITools.launch(configuration, ILaunchManager.RUN_MODE);
	}
	
	private void configClasspath(final ProjectModel model) throws CoreException {
		logger.debug("Configuring Classpath for new VM");
		
		// adding selected project
		final IRuntimeClasspathEntry projectCp = JavaRuntime.newDefaultProjectClasspathEntry(javaProject);
		projectCp.setClasspathProperty(IRuntimeClasspathEntry.PROJECT);
		classpath.add(projectCp.getMemento());
		
		// external libs
		final IClasspathEntry[] entries = javaProject.getResolvedClasspath(true);
		for (IClasspathEntry entry : entries) {
			if (entry != null) {
				IPath srcPath = entry.getSourceAttachmentPath();
				IRuntimeClasspathEntry dependenciesEntry = JavaRuntime.newArchiveRuntimeClasspathEntry(entry.getPath());
				dependenciesEntry.setSourceAttachmentPath(srcPath);
				dependenciesEntry.setSourceAttachmentRootPath(entry.getSourceAttachmentRootPath());
				dependenciesEntry.setClasspathProperty(IRuntimeClasspathEntry.ARCHIVE);
				classpath.add(dependenciesEntry.getMemento());
			}
		}
		
		// tests 
//		List<String> testsPath = this.model.getTestClasses();
//		for (String p : testsPath) {
//			IPath path = new Path(p);
//			IRuntimeClasspathEntry entry = JavaRuntime.newArchiveRuntimeClasspathEntry(path);
//			entry.setClasspathProperty(IRuntimeClasspathEntry.USER_CLASSES);
//			classpath.add(entry.getMemento());
//		}
		
		// junit
		final String junit = mementoForClass(JUnitCore.class);
		classpath.add(junit);

		// hamcrest
		final String hamcrest = mementoForClass(SelfDescribing.class);
		classpath.add(hamcrest);
		
		// log4j
		final String log4j = mementoForClass(Logger.class);
		classpath.add(log4j);
		
		// jackson databind
		final String databind = mementoForClass(ObjectMapper.class);
		classpath.add(databind);
		
		final String core = mementoForClass(Versioned.class);
		classpath.add(core);
		
		// annotation
		final String ann = mementoForClass(JsonIgnore.class);
		classpath.add(ann);

		// eclipse core
		final String eclipseCore = mementoForClass(IPath.class);
		classpath.add(eclipseCore);
		
		// eclipse jdt
		final String jdt = mementoForClass(IJavaProject.class);
		classpath.add(jdt);

		// ba-dua
		final String badua = mementoForClass(PreMain.class);
		classpath.add(badua);

		// baduino
//		final String path = "/Users/666mario/Documents/develop/each/baduino/bundles/br.usp.each.saeg.baduino.ui/target/br.usp.each.saeg.baduino.ui-0.1.26.jar";
//		final IPath ipath = new Path(path);
//		final IRuntimeClasspathEntry entry = JavaRuntime.newArchiveRuntimeClasspathEntry(ipath);
//		entry.setClasspathProperty(IRuntimeClasspathEntry.USER_CLASSES);
//		String baduino = entry.getMemento();

		final String baduino = mementoForClass(BaduinoRunner.class);
		classpath.add(baduino);
		
		// saeg commons
		final String saeg = mementoForClass(TimeWatch.class);
		classpath.add(saeg);
		
		// setting classpath
		workingCopy.setAttribute(ATTR_CLASSPATH, classpath);
		workingCopy.setAttribute(ATTR_DEFAULT_CLASSPATH, false);
		
		// main class for new VM to execute
		workingCopy.setAttribute(ATTR_MAIN_TYPE_NAME, BaduinoRunner.class.getName());
		
		// setting up java agent from ba-dua
		workingCopy.setAttribute(ATTR_VM_ARGUMENTS, "-javaagent:" + pathForClass(PreMain.class));
		
		// passing json path as parameter
		// so the new VM knows where to look for the json file
		workingCopy.setAttribute(ATTR_PROGRAM_ARGUMENTS, model.getJsonPath());
	}
	
	private String mementoForClass(Class<?> clazz) throws CoreException {
		final String path = pathForClass(clazz);
		final IPath ipath = new Path(path);
		final IRuntimeClasspathEntry entry = JavaRuntime.newArchiveRuntimeClasspathEntry(ipath);
		entry.setClasspathProperty(IRuntimeClasspathEntry.USER_CLASSES);
		
		return entry.getMemento();
	}
	
	private String pathForClass(Class<?> clazz) {
		return clazz.getProtectionDomain().getCodeSource().getLocation().getPath();
	}
	
}
