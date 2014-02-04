
import java.lang.reflect.Method;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.part.*;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.jface.action.*;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.*;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.SWT;


/**
 * This sample class demonstrates how to plug-in a new
 * workbench view. The view shows data obtained from the
 * model. The sample creates a dummy model on the fly,
 * but a real implementation would connect to the model
 * available either in this or another plug-in (e.g. the workspace).
 * The view is connected to the model using a content provider.
 * <p>
 * The view uses a label provider to define how model
 * objects should be presented in the view. Each
 * view can present the same model objects using
 * different labels and icons, if needed. Alternatively,
 * a single label provider can be shared between views
 * in order to ensure that objects of the same type are
 * presented in the same way everywhere.
 * <p>
 */

public class DataFlowMethodView extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "br.usp.each.saeg.badua.DataflowView";
	public static IMethod[] methods;
	private TreeViewer viewer;
	private Label label;


	/**
	 * The constructor.
	 */
	public DataFlowMethodView() {
		super();
		methods=DataflowHandler.methods;
	}

//	public void init(IViewSite site,IMemento memento) throws PartInitException{
//		
//	}

	/**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 */
	public void createPartControl(Composite parent) {
		viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		viewer.setContentProvider(new TodoContentProvider());
		viewer.setLabelProvider(new TodoLabelProvider());
		// Expand the tree
		// viewer.setAutoExpandLevel(2);
		// provide the input to the ContentProvider
		viewer.setInput(new TodoMockModel());
		viewer.refresh();
		

		//	    adicionar uma nova categoria
		//	    Category c = new Category();
		//	    c.setName("nova");
		//	    Todo t = new Todo("moe");
		//	    viewer.add(viewer.getInput(), t);
		// viewer.setInput(new TodoMockModel());
		// add a doubleclicklistener
		viewer.addDoubleClickListener(new IDoubleClickListener() {

			@Override
			public void doubleClick(DoubleClickEvent event) {

				IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
				Category a = (Category) selection.getFirstElement();


				TreeViewer viewer = (TreeViewer) event.getViewer();
				IStructuredSelection thisSelection = (IStructuredSelection) event
						.getSelection();
				Object selectedNode = thisSelection.getFirstElement();
				viewer.setExpandedState(selectedNode,
						!viewer.getExpandedState(selectedNode));
			}
		});

		viewer.addTreeListener(new ITreeViewerListener() {

			@Override
			public void treeExpanded(TreeExpansionEvent event) {
				//implementar as duas

			}

			@Override
			public void treeCollapsed(TreeExpansionEvent event) {
				// TODO Auto-generated method stub

			}
		});


		viewer.getTree().addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(final KeyEvent e) {
				if (e.keyCode == SWT.DEL) {
					final IStructuredSelection selection = (IStructuredSelection) viewer
							.getSelection();
					if (selection.getFirstElement() instanceof Todo) {
						Todo o = (Todo) selection.getFirstElement();
						// TODO Delete the selected element from the model
					}

				}
			}
		});



		//		ISelection serv = getSite().getWorkbenchWindow().getSelectionService().getSelection("org.eclipse.jdt.ui.PackageExplorer");

		//ISelection a = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService().getSelection("org.eclipse.jdt.ui.PackageExplorer");
		//System.out.println(serv.toString());
		//		IStructuredSelection st = (IStructuredSelection) serv; 
		//		IFile file = (IFile) st.getFirstElement();
		//		IPath path = file.getLocation();
		//		System.out.println(path.toPortableString());
		//		if(!a.isEmpty()){
		//			Class classe = st.getClass();
		//			Method[] methods = classe.getDeclaredMethods();
		//			for(int i = 0; i< methods.length;i++){
		//				System.out.println(methods[i]);
		//			}
		//		}
		//		label = new Label(parent, 0);
		//		label.setText("Hello world");

		//		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		//		viewer.setContentProvider(new ViewContentProvider());
		//		viewer.setLabelProvider(new ViewLabelProvider());
		//		viewer.setSorter(new NameSorter());
		//		viewer.setInput(getViewSite());
		//
	
	}
	
	//close the view, when workbench is closed, and changed
	static void closeViews() {
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		if (page != null) {
			IViewReference[] viewReferences = page.getViewReferences();
			for (IViewReference ivr : viewReferences) {
				if (ivr.getId().startsWith("br.usp.each.saeg.badua")) {
					page.hideView(ivr);
				}
			}
		}
	}
	@Override
	public void saveState(IMemento memento){
		closeViews();
	}
	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		//label.setFocus();
		viewer.getControl().setFocus();
	}
	
	public void dispose(){
		super.dispose();
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		if (page != null) {
			IViewReference[] viewReferences = page.getViewReferences();
			for (IViewReference ivr : viewReferences) {
				if (ivr.getId().startsWith("br.usp.each.saeg.badua")) {
					page.hideView(ivr);
				}
			}
		}
	}
}