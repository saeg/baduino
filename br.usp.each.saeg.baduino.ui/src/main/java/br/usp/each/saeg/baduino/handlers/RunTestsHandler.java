package br.usp.each.saeg.baduino.handlers;
import java.io.IOException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;

import br.usp.each.saeg.baduino.utils.ProjectUtils;


public class RunTestsHandler extends AbstractHandler implements IJavaLaunchConfigurationConstants {

	JaguarRunnable jaguar;
	
	@Override
	public Object execute(ExecutionEvent arg) throws ExecutionException {
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
		
		return null;
	}

	@Override
	public boolean isEnabled() {
		IProject project = ProjectUtils.getCurrentSelectedProject();
		if (project == null) {
			return false;
		}
		
		return true;
	}
}
