package br.usp.each.saeg.badua.handlers;

import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import br.usp.each.saeg.badua.views.DataFlowMethodView;
import br.usp.each.saeg.badua.xml.XmlClass;
import br.usp.each.saeg.badua.xml.XmlInput;
import br.usp.each.saeg.badua.xml.XmlMethod;
import br.usp.each.saeg.badua.xml.XmlObject;
import br.usp.each.saeg.badua.xml.XmlPackage;
import br.usp.each.saeg.badua.xml.XmlStatement;


public class VisualizationHandler extends AbstractHandler {

	private static Object type;

	//method called when we want open the plugin visualization
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

//		XmlInput xmlInput = XmlObject.getInstance();
//		List<XmlPackage> listPackage = xmlInput.getPackages();
//		for(XmlPackage l:listPackage ){
//			System.out.println(l.getName());
//			List<XmlClass>listClass = l.getClasses();
//			for(XmlClass l2:listClass){
//				System.out.println("\t"+l2.getName());
//				List<XmlMethod>listMethod = l2.getMethods();
//				for(XmlMethod l3:listMethod){
//					System.out.println("\t"+"\t"+l3.getName());
//					List<XmlStatement>listStatement = l3.getStatements();
//					for(XmlStatement l4:listStatement){
//						System.out.println(l4.getDef()+" "+l4.getUse()+" "+l4.getTarget()+" "+l4.getVar()+" "+l4.getCovered());
//					}
//				}
//			}
//		}
		
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
		VisualizationHandler.type = type;
	}
	
}

