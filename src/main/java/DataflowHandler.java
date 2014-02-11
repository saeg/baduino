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
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;


public class DataflowHandler extends AbstractHandler {
	static IMethod[] methods;
	static Path path;

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		try {
			ISelection sel = HandlerUtil.getActiveMenuSelection(event);
			IStructuredSelection selection = (IStructuredSelection) sel;
			ICompilationUnit cu = (ICompilationUnit) selection.getFirstElement();

			//			ITextEditor a = (ITextEditor) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
			//			ITextSelection selec = (ITextSelection) a.getSelectionProvider().getSelection();
			//			IEditorInput editorInput = a.getEditorInput();
			//			IJavaElement ele = JavaUI.getEditorInputJavaElement(editorInput);
			//			ICompilationUnit unit = (ICompilationUnit) ele;
			//			IJavaElement se = unit.getElementAt(selec.getOffset());
			//			System.out.println(selec+"  "+selec.getClass());
			
			//get the .class path
			IRegion region = JavaCore.newRegion();
			region.add(cu.getPrimaryElement());
			IResource[] r = JavaCore.getGeneratedResources(region, false);
			IPath ipath = r[0].getLocation();
			path = Paths.get(ipath.toFile().toURI());
			
			
			





			//			IType bo = cu.getType(cu.getCorrespondingResource().get);
			//			System.out.println(bo.getFullyQualifiedName());
			//			IMethod[] methods = bo.getMethods();
			//			for(int i = 0; i< methods.length;i++){
			//				System.out.println(methods[i]);
			//			}

			//nao usado no momento
			IType[] classes = cu.getTypes();
			methods = classes[0].getMethods(); // get only the fist selected class
			for(int j = 0; j< methods.length;j++){
				
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

			//show view
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(DataFlowMethodView.ID);
			


		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


}


