package br.usp.each.saeg.badua.handlers;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IRegion;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Initializer;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import br.usp.each.saeg.badua.views.*;


public class DataflowHandler extends AbstractHandler {
//	private static LinkedList<ICompilationUnit> cu = new LinkedList<ICompilationUnit>();
	
	
	private static Object type;
	private static IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		try {

			ISelection sel = HandlerUtil.getActiveMenuSelection(event);
			IStructuredSelection selection = (IStructuredSelection) sel;
			
			if(selection.getFirstElement() instanceof IJavaProject){
				setType((IJavaProject) selection.getFirstElement());
				System.out.println("Project selected");
			}else if(selection.getFirstElement() instanceof IPackageFragmentRoot){
				setType((IPackageFragmentRoot) selection.getFirstElement());
				System.out.println("Folder selected");
			}else if(selection.getFirstElement() instanceof IPackageFragment){
				setType((IPackageFragment) selection.getFirstElement());
				System.out.println("Package selected");
			}else if(selection.getFirstElement() instanceof ICompilationUnit){
				setType((ICompilationUnit) selection.getFirstElement()); 
				System.out.println("Class selected");
			}
			
			openView();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static IWorkbenchPage getPage() {
		return page;
	}

	private void openView() throws PartInitException {
		DataFlowMethodView.closeViews();
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(DataFlowMethodView.ID);
	}

	public static Object getType() {
		return type;
	}

	public static void setType(Object type) {
		DataflowHandler.type = type;
	}


}


