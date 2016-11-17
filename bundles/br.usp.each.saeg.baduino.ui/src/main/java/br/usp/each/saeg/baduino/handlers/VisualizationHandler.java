package br.usp.each.saeg.baduino.handlers;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import br.usp.each.saeg.baduino.core.logger.LoggerManager;
import br.usp.each.saeg.baduino.core.model.ProjectModel;
import br.usp.each.saeg.baduino.core.model.ProjectModelBuilder;
import br.usp.each.saeg.baduino.util.ProjectUtils;
import br.usp.each.saeg.baduino.view.DataFlowMethodView;


public class VisualizationHandler extends AbstractHandler {
	
	private static Object type;

	//method called when we want open the plugin visualization
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		LoggerManager.setupLogger();
		
		try {
			//get selection type
			ISelection sel = HandlerUtil.getActiveMenuSelection(event);
			IStructuredSelection selection = (IStructuredSelection) sel;

			if (selection.getFirstElement() instanceof IJavaProject) {
				setType((IJavaProject) selection.getFirstElement());
			}
			else if (selection.getFirstElement() instanceof IPackageFragmentRoot) {
				setType((IPackageFragmentRoot) selection.getFirstElement());
			}
			else if (selection.getFirstElement() instanceof IPackageFragment) {
				setType((IPackageFragment) selection.getFirstElement());
			}
			else if (selection.getFirstElement() instanceof ICompilationUnit) {
				setType((ICompilationUnit) selection.getFirstElement()); 
			}

			openView();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	@Override
	public boolean isEnabled() {
		try {
			final IJavaProject javaProject = ProjectUtils.getCurrentSelectedJavaProject();
			final ProjectModel model = ProjectModelBuilder.buildModel(javaProject);
			final Path path = Paths.get(model.getCoverageXmlPath());
			
			return Files.exists(path);
		}
		catch (Exception ex) {
			return false;
		}
	}

	public static void openView() throws PartInitException {
		DataFlowMethodView.closeViews(); // close view if it was already open
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(DataFlowMethodView.ID);//open view DataFlowMethodView.createPartControl()
	}

	public static Object getType() {
		return type;
	}

	public static void setType(Object type) {
		VisualizationHandler.type = type;
	}
	
}


