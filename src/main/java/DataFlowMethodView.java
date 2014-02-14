
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.nio.file.Path;
import java.util.List;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeViewerListener;
import org.eclipse.jface.viewers.TreeExpansionEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.ViewPart;

import br.usp.each.saeg.badua.markers.CodeMarkerFactory;


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
	public static Path classFile;
	private static ICompilationUnit cu;

	private TreeViewer viewer;


	/**
	 * The constructor.
	 */
	public DataFlowMethodView() {
		super();

		methods=DataflowHandler.methods;
		classFile=DataflowHandler.path;
		cu=DataflowHandler.cu;


	}

	//	public void init(IViewSite site,IMemento memento) throws PartInitException{
	//		
	//	}

	/**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 */
	public void createPartControl(Composite parent) {
		Tree tree = new Tree(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		tree.setHeaderVisible(true);
		tree.setLinesVisible(true);
		viewer = new TreeViewer(tree);
		TreeColumn column1 = new TreeColumn(tree, SWT.LEFT);
		column1.setText("Methods/Duas");
		column1.setWidth(250);

		//viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		//		TreeViewerColumn column1 = new TreeViewerColumn(viewer, SWT.LEFT);
		//		column1.setLabelProvider(new CellLabelProvider() {
		//			private final ILabelProvider delegate = new WorkbenchLabelProvider();
		//			@Override
		//			public void update(ViewerCell cell) {
		//				cell.setText(getElementName(cell.getElement()));
		//		        cell.setImage(delegate.getImage(cell.getElement()));
		//		        System.out.println(cell.getText());
		//			}
		//			
		//		});

		TreeColumn column2 = new TreeColumn(tree, SWT.LEFT);
				column2.setText("Coverage");
				column2.setWidth(50);

		viewer.setContentProvider(new CoverageContentProvider());
		viewer.setLabelProvider(new CoverageLabelProvider());
		// Expand the tree
		// viewer.setAutoExpandLevel(2);
		// provide the input to the ContentProvider
		viewer.setInput(new CoverageMockModel());



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
				TreeViewer viewer = (TreeViewer) event.getViewer();
				IStructuredSelection thisSelection = (IStructuredSelection) event
						.getSelection();
				Object selectedNode = thisSelection.getFirstElement();
				if(selectedNode instanceof Methods){
					viewer.setExpandedState(selectedNode,
							!viewer.getExpandedState(selectedNode));
				}else{
					String dua = (String)selectedNode.toString();
					int defLine = Integer.parseInt(getDef(dua));
					int useLine = Integer.parseInt(getUse(dua));
					int[] defOffset = new int[2];
					int[] useOffset = new int[2];
					

					try {
						defOffset = parserLine(cu.getSource(),defLine,defOffset);
						useOffset = parserLine(cu.getSource(), useLine, useOffset);
						List<IMarker> toDelete = CodeMarkerFactory.findMarkers(cu.getUnderlyingResource());
						CodeMarkerFactory.removeMarkers(toDelete);
					//	CodeMarkerFactory.scheduleMarkerCreation(cu.getUnderlyingResource(),defOffset,useOffset);
						CodeMarkerFactory.mark(cu.getUnderlyingResource(),defOffset,useOffset);
					} catch (JavaModelException e1) {
						e1.printStackTrace();
					} catch (PartInitException e) {
						e.printStackTrace();
					}

				}
				
			}

			private String getDef(String dua) {
				dua = dua.substring(1, dua.length()-1);
				dua = dua.replace(" ", "");
				return dua.split(",")[0];
			}
			
			private String getUse(String dua) {
				dua = dua.substring(1, dua.length()-1);
				dua = dua.replace(" ", "");
				return dua.split(",")[1];
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


//		viewer.getTree().addKeyListener(new KeyAdapter() {
//			@Override
//			public void keyReleased(final KeyEvent e) {
//				if (e.keyCode == SWT.DEL) {
//					final IStructuredSelection selection = (IStructuredSelection) viewer
//							.getSelection();
//					if (selection.getFirstElement() instanceof DUA) {
//						DUA o = (DUA) selection.getFirstElement();
//						// TODO Delete the selected element from the model
//					}
//
//				}
//			}
//		});

	}
	//procura as linhas para pintar 
	protected int[] parserLine(String source, int Line, int[] Offset) {
		String[] Source = source.split("\n");
		int actualLine = 1;
		int counterChar = 0;
		for(String src:Source){
			if(actualLine == Line){
				Offset[0] = counterChar;
				Offset[1] = counterChar+src.length()+1;
				return Offset;
			}
			counterChar+=src.length()+1;
			actualLine++;
		}
		return null;
	}

	//close the view, when workbench is closed, and changed
	public static void closeViews() {
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
		viewer.getControl().setFocus();
	}

//	public void dispose(){
//		super.dispose();
//		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
//		if (page != null) {
//			IViewReference[] viewReferences = page.getViewReferences();
//			for (IViewReference ivr : viewReferences) {
//				if (ivr.getId().startsWith("br.usp.each.saeg.badua")) {
//					page.hideView(ivr);
//				}
//			}
//		}
//	}


	//	ILabelProvider workbenchLabelProvider;
	//	
	//	String getElementName(Object element) {
	//	    String text = getSimpleTextForJavaElement(element);
	//	    return text;
	//	  }
	//
	//	  private String getSimpleTextForJavaElement(Object element) {
	//	    if (element instanceof IPackageFragmentRoot) {
	//	      final IPackageFragmentRoot root = (IPackageFragmentRoot) element;
	//	      // tweak label if the package fragment root is the project itself:
	//	      if (root.getElementName().length() == 0) {
	//	        element = root.getJavaProject();
	//	      }
	//	      // shorten JAR references
	//	      try {
	//	        if (root.getKind() == IPackageFragmentRoot.K_BINARY) {
	//	          return root.getPath().lastSegment();
	//	        }
	//	      } catch (JavaModelException e) {
	//	        e.printStackTrace();
	//	      }
	//	    }
	//	    return workbenchLabelProvider.getText(element);
	//	  }

}