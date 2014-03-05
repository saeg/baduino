package br.usp.each.saeg.badua.views;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import br.usp.each.saeg.badua.contentViews.CoverageContentProvider;
import br.usp.each.saeg.badua.contentViews.CoverageLabelProvider;
import br.usp.each.saeg.badua.contentViews.CoverageMockModel;
import br.usp.each.saeg.badua.contentViews.TreeClass;
import br.usp.each.saeg.badua.contentViews.TreeDUA;
import br.usp.each.saeg.badua.contentViews.TreeFolder;
import br.usp.each.saeg.badua.contentViews.TreeMethod;
import br.usp.each.saeg.badua.contentViews.TreePackage;
import br.usp.each.saeg.badua.contentViews.TreeProject;
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

	private TreeViewer viewer;


	/**
	 * The constructor.
	 */
	public DataFlowMethodView() {
		super();
	}

	/**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 */
	public void createPartControl(Composite parent) {
		Tree tree = new Tree(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL); //create a tree 
		tree.setHeaderVisible(true);//show header in interface
		tree.setLinesVisible(true); //show lines in interface
		viewer = new TreeViewer(tree);

		//first column
		TreeColumn column1 = new TreeColumn(tree, SWT.LEFT);
		column1.setText("Methods -> Def-Use Assicioations");
		column1.setWidth(500);

		//second column
		TreeColumn column2 = new TreeColumn(tree, SWT.LEFT);
		column2.setText("Coverage");
		column2.setWidth(200);

		long start = System.currentTimeMillis();
		
		viewer.setContentProvider(new CoverageContentProvider());
		viewer.setLabelProvider(new CoverageLabelProvider());
		
		// provide the input to the ContentProvider
		viewer.setInput(new CoverageMockModel());
		System.out.println("Time to show view: "+(System.currentTimeMillis()-start)/1000.0);
		
		//change selection event
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection thisSelection = (IStructuredSelection) event
						.getSelection();
				Object selectedNode = thisSelection.getFirstElement();

				if(selectedNode instanceof TreeDUA){
					ICompilationUnit cu = ((TreeDUA) selectedNode).getCu();
					try {
						//get the first and last char of definition line to draw
						int[] defOffset = parserLine(cu.getSource(),((TreeDUA) selectedNode).getDef(),new int[2]);
						//get the first and last char of c-use line to draw
						int[] useOffset = parserLine(cu.getSource(), ((TreeDUA) selectedNode).getUse(), new int[2]);
						//get the first and last char of target line to draw
						int[] targetOffset = null;
						if(((TreeDUA) selectedNode).getTarget() != 1){
							targetOffset = parserLine(cu.getSource(),((TreeDUA) selectedNode).getTarget(),new int[2]);
						}
						//remove old markers
						CodeMarkerFactory.removeMarkers(CodeMarkerFactory.findMarkers(cu.getUnderlyingResource()));
						//create new markers based on selected DUA
						CodeMarkerFactory.mark(cu.getUnderlyingResource(),defOffset,useOffset,targetOffset);
						setFocus();
					} catch (JavaModelException e1) {
						e1.printStackTrace();
					} catch (PartInitException e) {
						e.printStackTrace();
					}
				}

			}
		});

		
		//double click event to expand state
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent event) {
				TreeViewer viewer = (TreeViewer) event.getViewer();
				IStructuredSelection thisSelection = (IStructuredSelection) event
						.getSelection();
				Object selectedNode = thisSelection.getFirstElement();

				if(selectedNode instanceof TreeMethod 
						|| selectedNode instanceof TreeClass 
						|| selectedNode instanceof TreePackage 
						|| selectedNode instanceof TreeFolder 
						|| selectedNode instanceof TreeProject){
					
					viewer.setExpandedState(selectedNode,
							!viewer.getExpandedState(selectedNode));
				}

			}

		});
		
		//Listener to closing view in workbench shutdown
		PlatformUI.getWorkbench().addWorkbenchListener(new IWorkbenchListener() {

			@Override
			public boolean preShutdown(IWorkbench workbench, boolean forced) {
				closeViews();
				return true;
			}

			@Override
			public void postShutdown(IWorkbench workbench) {
			}
		});

	}

	//search for the line , and get the position of the first char and last char
	protected int[] parserLine(String source, int Line, int[] Offset) {
		String[] Source = source.split("\n");
		int actualLine = 1;
		int counterChar = 0;
		for(String src:Source){
			if(actualLine == Line){ // desired line
				Offset[0] = counterChar; //first char
				Offset[1] = counterChar+src.length()+1; //last char
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


	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
	}
}