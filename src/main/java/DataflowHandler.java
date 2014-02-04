import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourceAttributes;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IClassFile;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.core.CompilationUnit;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;


public class DataflowHandler extends AbstractHandler {
	static IMethod[] methods;
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		try {
			Shell shell = HandlerUtil.getActiveShell(event);
			ISelection sel = HandlerUtil.getActiveMenuSelection(event);
			IStructuredSelection selection = (IStructuredSelection) sel;
			ICompilationUnit cu = (ICompilationUnit) selection.getFirstElement();

			//			IType bo = cu.getType(cu.getCorrespondingResource().get);
			//			System.out.println(bo.getFullyQualifiedName());
			//			IMethod[] methods = bo.getMethods();
			//			for(int i = 0; i< methods.length;i++){
			//				System.out.println(methods[i]);
			//			}


			IType[] classes = cu.getTypes();
			methods = classes[0].getMethods(); // get only the fist selected class
			for(int j = 0; j< methods.length;j++){
				//System.out.println(methods[j]);
			}
			
//			for(IType i:a){
//				System.out.println(i.getFullyQualifiedName());
//				System.out.println(i.getSource());
//				IMethod[] methods = i.getMethods();
//				for(int j = 0; j< methods.length;j++){
//					System.out.println(methods[j]);
//				}
//			}

			//	        	IResource a = cu.getCorrespondingResource();
			//	        	String b = JavaCore.create(a).getPrimaryElement().;
			//	        	System.out.println(b);

			//Class a = Class.forName("source.Max");

			//	    		ISelection selection = HandlerUtil.getCurrentSelection(event);
			//	    		IStructuredSelection st = (IStructuredSelection) selection;
			//	    		CompilationUnit comp = (CompilationUnit) st.getFirstElement();
			//	    		System.out.println(comp.getJavaProject().;

			//System.out.println(a.getName());
			
			IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			page.showView(DataFlowMethodView.ID);
			
			DataFlowMethodView.methods = methods;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}



}
