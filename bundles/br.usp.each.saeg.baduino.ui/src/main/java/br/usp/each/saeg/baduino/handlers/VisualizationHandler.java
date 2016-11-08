package br.usp.each.saeg.baduino.handlers;

import java.util.List;

import org.apache.log4j.Logger;
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

import br.usp.each.saeg.baduino.views.DataFlowMethodView;
import br.usp.each.saeg.baduino.xml.XmlClass;
import br.usp.each.saeg.baduino.xml.XmlInput;
import br.usp.each.saeg.baduino.xml.XmlMethod;
import br.usp.each.saeg.baduino.xml.XmlObject;
import br.usp.each.saeg.baduino.xml.XmlPackage;
import br.usp.each.saeg.baduino.xml.XmlDua;


public class VisualizationHandler extends AbstractHandler {

	private static final Logger logger = Logger.getLogger(VisualizationHandler.class);
	
	private static Object type;

	//method called when we want open the plugin visualization
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
/*
		XmlInput xmlInput = XmlObject.getInstance();
		int coveredAll = 0;
		int totalAll = 0;
		
		List<XmlPackage> listPackage = xmlInput.getPackages();
		for(XmlPackage l : listPackage ){
			//System.out.println(l.getName());
			int coveredClass=0,totalClass=0;
			List<XmlClass>listClass = l.getClasses();
			
			for(XmlClass l2 : listClass){
			//	System.out.println("\t"+l2.getName());
				int coveredMETHOD=0,totalMETHOD=0;
				List<XmlMethod>listMethod = l2.getMethods();
				
				for(XmlMethod l3 : listMethod){
				//	System.out.println("\t"+"\t"+l3.getName());
					int coveredDUA=0,totalDUA=0;
					List<XmlDua>listStatement = l3.getStatements();
					
					for(XmlDua l4:listStatement){
						//System.out.println(l4.getDef()+" "+l4.getUse()+" "+l4.getTarget()+" "+l4.getVar()+" "+l4.getCovered());
						if(l4.getCovered()){
							coveredDUA++;
						}
						
						totalDUA++;
					}
					
					System.out.println("\t\t\t"+l3.getName()+" "+coveredDUA+"/"+totalDUA);
					coveredMETHOD+=coveredDUA;totalMETHOD+=totalDUA;
					coveredDUA=0;totalDUA=0;
				}
				
				System.out.println("\t\t"+l2.getName()+" "+coveredMETHOD+"/"+totalMETHOD);
				coveredClass+=coveredMETHOD;totalClass+= totalMETHOD;
				coveredMETHOD=0;totalMETHOD=0;
			}
			
			System.out.println("\t"+l.getName()+" "+coveredClass+"/"+totalClass);
			coveredAll+= coveredClass;totalAll+=totalClass;
			coveredClass=0;totalClass=0;
		}
		
		System.out.println("CoveredALL: "+coveredAll+"/"+totalAll);
		*/
		
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


