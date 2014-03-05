package br.usp.each.saeg.badua.handlers;

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

import br.usp.each.saeg.badua.views.*;


public class DataflowHandler extends AbstractHandler {

	private static Object type;

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		try {
			//get selection type
			ISelection sel = HandlerUtil.getActiveMenuSelection(event);
			IStructuredSelection selection = (IStructuredSelection) sel;

			if(selection.getFirstElement() instanceof IJavaProject){
				setType((IJavaProject) selection.getFirstElement());
			}else if(selection.getFirstElement() instanceof IPackageFragmentRoot){
				setType((IPackageFragmentRoot) selection.getFirstElement());
			}else if(selection.getFirstElement() instanceof IPackageFragment){
				setType((IPackageFragment) selection.getFirstElement());
			}else if(selection.getFirstElement() instanceof ICompilationUnit){
				setType((ICompilationUnit) selection.getFirstElement()); 
			}

			openView();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private void openView() throws PartInitException {
		DataFlowMethodView.closeViews(); // close view if it was already open
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(DataFlowMethodView.ID);//open view DataFlowMethodView.createPartControl()
	}

	public static Object getType() {
		return type;
	}

	public static void setType(Object type) {
		DataflowHandler.type = type;
	}


}


