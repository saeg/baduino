import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IRegion;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.ide.IDE;

import br.usp.each.saeg.badua.markers.CodeMarkerFactory;


public class DataflowHandler extends AbstractHandler {
	static IMethod[] methods;
	static Path path;
	static ICompilationUnit cu;
	public static IWorkbenchPage page;

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		try {
			ISelection sel = HandlerUtil.getActiveMenuSelection(event);
			IStructuredSelection selection = (IStructuredSelection) sel;
			cu = (ICompilationUnit) selection.getFirstElement();

			//			ITextEditor a = (ITextEditor) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
			//			ITextSelection selec = (ITextSelection) a.getSelectionProvider().getSelection();
			//			IEditorInput editorInput = a.getEditorInput();
			//			IJavaElement ele = JavaUI.getEditorInputJavaElement(editorInput);
			//			ICompilationUnit unit = (ICompilationUnit) ele;
			//			IJavaElement se = unit.getElementAt(selec.getOffset());
			//			System.out.println(selec+"  "+selec.getClass());
			
			//get the .class path
			//programa precisa ser compilado para funcionar..tratar isso
			IRegion region = JavaCore.newRegion();
			region.add(cu.getPrimaryElement());
			IResource[] r = JavaCore.getGeneratedResources(region, false);
			IPath ipath = r[0].getLocation();
			path = Paths.get(ipath.toFile().toURI());
			
			
			
			IResource resource = cu.getUnderlyingResource();
		
//			String a = cu.getSource();
//			String[] x = a.split("\n");
//			for(String b:x) System.out.println("NEW LINE: "+b+" "+b.length());;
//			
//			IMarker mark = resource.createMarker(CodeMarkerFactory.DEFINITION_MARKER);
//			mark.setAttribute(IMarker.CHAR_START, 13);
//			mark.setAttribute(IMarker.CHAR_END, 32);
//			
//			IMarker mark2 = resource.createMarker(CodeMarkerFactory.DEFINITION_MARKER);
//			mark2.setAttribute(IMarker.CHAR_START, 45);
//			mark2.setAttribute(IMarker.CHAR_END, 70);
//			
//			IMarker mark3 = resource.createMarker(CodeMarkerFactory.DEFINITION_MARKER);
//			mark2.setAttribute(IMarker.LINE_NUMBER, 20);
			
			page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			//IDE.openEditor(page, mark);
			





			//			IType bo = cu.getType(cu.getCorrespondingResource().get);
			//			System.out.println(bo.getFullyQualifiedName());
			//			IMethod[] methods = bo.getMethods();
			//			for(int i = 0; i< methods.length;i++){
			//				System.out.println(methods[i]);
			//			}

			//nao usado no momento
			IType[] classes = cu.getTypes();
			methods = classes[0].getMethods(); // get only the fist selected class

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
			DataFlowMethodView.closeViews();
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(DataFlowMethodView.ID);
			//IFile file = (IFile) Platform.getAdapterManager().getAdapter(selection.getFirstElement(), IFile.class);
			//IDE.openEditor(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage(),(IFile) path.toFile());
			//IResource resource = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getSelection();
			
			


		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


}


