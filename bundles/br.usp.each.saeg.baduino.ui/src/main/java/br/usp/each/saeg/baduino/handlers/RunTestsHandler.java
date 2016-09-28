package br.usp.each.saeg.baduino.handlers;

import java.io.IOException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.core.JavaProject;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;

import br.usp.each.saeg.badua.cli.Instrument;
import br.usp.each.saeg.baduino.utils.ProjectUtils;

public class RunTestsHandler extends AbstractHandler implements IJavaLaunchConfigurationConstants {

	JaguarRunnable jaguar;
	
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

		IProject project = ProjectUtils.getCurrentSelectedProject();
		IJavaProject javaProject = JavaCore.create(project);
		
		System.out.println("Building project " + project.getName());
		
		try {
			String srcFolder = javaProject.getOutputLocation().toOSString();
			System.out.println("output folder: " + srcFolder);
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
		
		try {
			project.build(IncrementalProjectBuilder.INCREMENTAL_BUILD, null);
		} catch (CoreException e) {
			e.printStackTrace();
		}
		
//		Instrument.main("");
		
		return null;
	}

	@Override
	public boolean isEnabled() {
		IProject project = ProjectUtils.getCurrentSelectedProject();
		return (project != null);
	}
}
