package br.usp.each.saeg.badua.handlers;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IRegion;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import br.usp.each.saeg.badua.views.*;


public class DataflowHandler extends AbstractHandler {
	public static IMethod[] methods;
	public static Path path;
	public static ICompilationUnit cu;
	public static IWorkbenchPage page;

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		try {
			ISelection sel = HandlerUtil.getActiveMenuSelection(event);
			IStructuredSelection selection = (IStructuredSelection) sel;
			cu = (ICompilationUnit) selection.getFirstElement();
			page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();

			//get the .class path
			//programa precisa ser compilado para funcionar
			IRegion region = JavaCore.newRegion();
			region.add(cu.getPrimaryElement());
			IResource[] r = JavaCore.getGeneratedResources(region, false);
			IPath ipath = r[0].getLocation();
			path = Paths.get(ipath.toFile().toURI());

			openView();

			// Pegar os nomes dos metodos
			//IType[] classes = cu.getTypes();
			//methods = classes[0].getMethods(); // get only the fist selected class
			//			for(IType i:a){
			//				System.out.println(i.getFullyQualifiedName());
			//				System.out.println(i.getSource());
			//				IMethod[] methods = i.getMethods();
			//				for(int j = 0; j< methods.length;j++){
			//					System.out.println(methods[j]);
			//				}
			//			}



		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private void openView() throws PartInitException {
		DataFlowMethodView.closeViews();
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(DataFlowMethodView.ID);
	}


}


