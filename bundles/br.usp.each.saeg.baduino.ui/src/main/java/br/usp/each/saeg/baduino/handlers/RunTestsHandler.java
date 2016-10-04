package br.usp.each.saeg.baduino.handlers;

import java.io.File;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.core.JavaProject;
import org.eclipse.jdt.junit.launcher.JUnitLaunchShortcut;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.jface.viewers.StructuredSelection;

import br.usp.each.saeg.badua.cli.Instrument;
import br.usp.each.saeg.baduino.utils.ProjectUtils;

public class RunTestsHandler extends AbstractHandler implements IJavaLaunchConfigurationConstants {

//	JaguarRunnable jaguar;
	
	@Override
	public Object execute(ExecutionEvent arg) throws ExecutionException {
		/*
		final IProject project = ProjectUtils.getCurrentSelectedProject();
		if (!project.isOpen()) {
			return null;
		}

		jaguar = new JaguarRunnable(new JaguarLaunchesListener2(project));
		try {
			jaguar.run();
		} catch (CoreException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		*/

		final IProject project = ProjectUtils.getCurrentSelectedProject();
		final IJavaProject javaProject = JavaCore.create(project);
		
		final String projectName = project.getName();
		final String location = project.getLocation().toOSString();
		
		System.out.println("Building project " + projectName);
		System.out.println("Project location: " + location);
		
		final StringBuilder src = new StringBuilder();
		final StringBuilder dest = new StringBuilder();

		try {
			String output = javaProject.getOutputLocation().toOSString();
			
			src.append(output)
			.delete(0, projectName.length() + 1)
			.insert(0, location);
			
			dest.append(location)
			.append(File.separator)
			.append(".baduino")
			.append(File.separator)
			.append("classes");
			
			project.build(IncrementalProjectBuilder.CLEAN_BUILD, new NullProgressMonitor());
			project.build(IncrementalProjectBuilder.FULL_BUILD, new NullProgressMonitor());
		} catch (CoreException e) {
			e.printStackTrace();
		}
		
		System.out.println("src: " + src.toString());
		System.out.println("dest: " + dest.toString());

		// ba-dua instrumenting
		Instrument.main(new String[] {"-src", src.toString(), "-dest", dest.toString()});
		
		/*
		// replace classes on .baduino folder to original classes folder
		try {
			Files.move(Paths.get(dest.toString()), 
					Paths.get(src.toString()), 
					StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
		*/
		
		// run junit
		JUnitLaunchShortcut junit = new JUnitLaunchShortcut();
		junit.launch(new StructuredSelection(javaProject),"run");
		
		return null;
	}

	@Override
	public boolean isEnabled() {
		final IProject project = ProjectUtils.getCurrentSelectedProject();
		return (project != null);
	}
}
