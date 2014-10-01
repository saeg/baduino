package br.usp.each.saeg.badua.handlers;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.core.ILaunchesListener2;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.jdt.launching.IRuntimeClasspathEntry;
import org.eclipse.jdt.launching.JavaRuntime;

import br.usp.each.saeg.badua.utils.PropertyManager;

public class JaguarRunnable implements IJavaLaunchConfigurationConstants {

	PropertyManager properties;
	ILaunchesListener2 launchesListener;

	public JaguarRunnable() {
		super();
	}

	public JaguarRunnable(ILaunchesListener2 launchesListener) {
		super();
		this.launchesListener = launchesListener;
	}

	public void run() {
		properties = new PropertyManager();
		ILaunchManager manager = DebugPlugin.getDefault().getLaunchManager();
		ILaunchConfigurationType type = manager.getLaunchConfigurationType(ID_JAVA_APPLICATION);
		if (launchesListener != null) {
			manager.addLaunchListener(launchesListener);
		}

		ILaunchConfigurationWorkingCopy workingCopy = null;
		try {
			workingCopy = type.newInstance(null, "Launch Jaguar");
		} catch (CoreException e) {
			e.printStackTrace();
			return;
		}
		workingCopy.setAttribute(ATTR_VM_ARGUMENTS, "-javaagent:" + properties.getJacocoAgentJar() + "=output=tcpserver,dataflow=true");
		List<String> classpath = buildClassPath();

		workingCopy.setAttribute(ATTR_CLASSPATH, classpath);
		workingCopy.setAttribute(ATTR_DEFAULT_CLASSPATH, false);

		workingCopy.setAttribute(ATTR_MAIN_TYPE_NAME, "br.usp.each.saeg.jaguar.runner.BaduinoRunner");

		workingCopy.setAttribute(ATTR_PROGRAM_ARGUMENTS,properties.getProjectDir() + " "
				+ properties.getCompiledClassesDir() + " " + properties.getCompiledTestsDir() + " ");

		ILaunchConfiguration configuration = null;
		try {
			configuration = workingCopy.doSave();
			configuration.launch(ILaunchManager.RUN_MODE, null);
		} catch (CoreException e) {
			e.printStackTrace();
			return;
		}

	}

	private List<String> buildClassPath() {
		List<String> classpath = new ArrayList<String>();
		try {

			IPath jaguarPath = new Path(properties.getJaguarJar());
			IRuntimeClasspathEntry jaguarEntry = JavaRuntime.newArchiveRuntimeClasspathEntry(jaguarPath);
			jaguarEntry.setClasspathProperty(IRuntimeClasspathEntry.USER_CLASSES);
			classpath.add(jaguarEntry.getMemento());

			IPath testPath = new Path(properties.getCompiledTestsDir());
			IRuntimeClasspathEntry testEntry = JavaRuntime.newArchiveRuntimeClasspathEntry(testPath);
			testEntry.setClasspathProperty(IRuntimeClasspathEntry.USER_CLASSES);
			classpath.add(testEntry.getMemento());

			IPath classesPath = new Path(properties.getCompiledClassesDir());
			IRuntimeClasspathEntry classesEntry = JavaRuntime.newArchiveRuntimeClasspathEntry(classesPath);
			classesEntry.setClasspathProperty(IRuntimeClasspathEntry.USER_CLASSES);
			classpath.add(classesEntry.getMemento());

		} catch (CoreException e) {
			e.printStackTrace();
		}
		return classpath;
	}

}
