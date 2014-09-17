package br.usp.each.saeg.badua.handlers;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.debug.core.ILaunchesListener2;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;

import br.usp.each.saeg.badua.utils.ProjectUtils;


public class RunTestsHandler extends AbstractHandler implements IJavaLaunchConfigurationConstants {

	JaguarRunnable jaguar;
	
	public RunTestsHandler() {
		super();
		jaguar = new JaguarRunnable();
	}
	
	public RunTestsHandler(ILaunchesListener2 listener) {
		super();
		this.jaguar = new JaguarRunnable(listener);
	}
	@Override
	public Object execute(ExecutionEvent arg) throws ExecutionException {
		final IProject project = ProjectUtils.getCurrentSelectedProject();
		if (!project.isOpen()) {
			return null;
		}

		jaguar.run();
		
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
