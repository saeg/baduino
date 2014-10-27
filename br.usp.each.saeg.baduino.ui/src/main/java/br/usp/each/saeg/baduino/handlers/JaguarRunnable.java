package br.usp.each.saeg.baduino.handlers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.core.ILaunchesListener2;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.jdt.launching.IRuntimeClasspathEntry;
import org.eclipse.jdt.launching.JavaRuntime;

import br.usp.each.saeg.baduino.utils.ProjectUtils;
import br.usp.each.saeg.baduino.utils.PropertyManager;
import br.usp.each.saeg.jaguar.resource.JaguarJar;

public class JaguarRunnable implements IJavaLaunchConfigurationConstants {

	PropertyManager properties;
	ILaunchesListener2 launchesListener;
	private JacocoAgentJar jacocoJar = new JacocoAgentJar();

	public JaguarRunnable() {
		super();
	}

	public JaguarRunnable(ILaunchesListener2 launchesListener) {
		super();
		this.launchesListener = launchesListener;
	}

	public void run() throws CoreException, IOException {
		properties = new PropertyManager(ProjectUtils.getCurrentSelectedProject().getLocation().toString());
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
		
		workingCopy.setAttribute(ATTR_VM_ARGUMENTS, jacocoJar.getQuotedVmArguments(properties.getIncludes()));
		
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

	private List<String> buildClassPath() throws IOException {
		List<String> classpath = new ArrayList<String>();
		try {

			IPath jaguarPath = new Path(FileLocator.toFileURL(JaguarJar.getResource()).getPath());
			checkPath(jaguarPath);
			IRuntimeClasspathEntry jaguarEntry = JavaRuntime.newArchiveRuntimeClasspathEntry(jaguarPath);
			jaguarEntry.setClasspathProperty(IRuntimeClasspathEntry.USER_CLASSES);
			classpath.add(jaguarEntry.getMemento());

			IPath testPath = new Path(properties.getCompiledTestsDir());
			checkPath(testPath);
			IRuntimeClasspathEntry testEntry = JavaRuntime.newArchiveRuntimeClasspathEntry(testPath);
			testEntry.setClasspathProperty(IRuntimeClasspathEntry.USER_CLASSES);
			classpath.add(testEntry.getMemento());

			IPath classesPath = new Path(properties.getCompiledClassesDir());
			checkPath(classesPath);
			IRuntimeClasspathEntry classesEntry = JavaRuntime.newArchiveRuntimeClasspathEntry(classesPath);
			classesEntry.setClasspathProperty(IRuntimeClasspathEntry.USER_CLASSES);
			classpath.add(classesEntry.getMemento());
			
			IClasspathEntry[] classpathEntries = ProjectUtils.getCurrentSelectedJavaProject().getResolvedClasspath(true);
			for (IClasspathEntry iClasspathEntry : classpathEntries) {
				if (iClasspathEntry != null) {
					IPath srcPath = iClasspathEntry.getSourceAttachmentPath();
					IRuntimeClasspathEntry dependenciesEntry = JavaRuntime.newArchiveRuntimeClasspathEntry(iClasspathEntry.getPath());
					dependenciesEntry.setSourceAttachmentPath(srcPath);
					dependenciesEntry.setSourceAttachmentRootPath(iClasspathEntry.getSourceAttachmentRootPath());
					dependenciesEntry.setClasspathProperty(IRuntimeClasspathEntry.ARCHIVE);
					classpath.add(dependenciesEntry.getMemento());
				}
			}
		
		} catch (CoreException e) {
			e.printStackTrace();
		}
		return classpath;
	}
	
	private void checkPath(IPath path) throws FileNotFoundException{
		File test = new File(path.toString());
		if(!test.exists()){
			JOptionPane.showMessageDialog(null, "Invalid Classpath:"+path.toString(), "Invalid Classpath", JOptionPane.DEFAULT_OPTION);
			throw new FileNotFoundException();
		}
	}

}
