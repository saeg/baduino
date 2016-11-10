package br.usp.each.saeg.baduino.contentViews;
import java.util.List;

import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import br.usp.each.saeg.baduino.tree.TreeClass;
import br.usp.each.saeg.baduino.tree.TreeMethod;
import br.usp.each.saeg.baduino.tree.TreePackage;
import br.usp.each.saeg.baduino.tree.TreeProject;
import br.usp.each.saeg.baduino.views.DataFlowMethodView;

/**
 * 
 * @author Mario Concilio
 *
 */
public class CoverageContentProvider implements ITreeContentProvider {

	private CoverageMockModel model;

	@Override
	public void dispose() {}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		this.model = (CoverageMockModel) newInput;
	}

	@Override
	public Object[] getElements(Object inputElement) {
		Object[] array = null;
		
		try {
			final List<?> tree = model.getTree();
			
			if (tree != null) {
				array = tree.toArray();
			}
			else {
				//gerando varias exception pois ele nao termina o programa.
				//correto seria fechar a view e nao fazer mais nada.
				DataFlowMethodView.closeViews();
				throw new IllegalStateException("open an Dataflow empty view");
			}
		}
		catch (JavaModelException e) {
			e.printStackTrace();
		}
		
		return array;
	}

	@Override
	public Object[] getChildren(final Object parentElement) {
		Object[] children = null;
		
		if (parentElement instanceof TreeProject) {
			final TreeProject project = (TreeProject) parentElement;
			children = project.getPackages().toArray();
		}
//		else if (parentElement instanceof TreeFolder) {
//			final TreeFolder folder = (TreeFolder) parentElement;
//			children = folder.getPackages().toArray();
//		}
		else if (parentElement instanceof TreePackage) {
			final TreePackage pkg = (TreePackage) parentElement;
			children = pkg.getClasses().toArray();
		}
		else if (parentElement instanceof TreeClass) {
			final TreeClass clazz = (TreeClass) parentElement;
			children = clazz.getMethods().toArray();
		}
		else if (parentElement instanceof TreeMethod) {
			final TreeMethod method = (TreeMethod) parentElement;
			children = method.getDuas().toArray();
		}
		
		return children;
	}

	@Override
	public Object getParent(final Object element) {
		return null;	
	}

	@Override
	public boolean hasChildren(final Object element) {
		return element instanceof TreeProject ||
//				element instanceof TreeFolder ||
				element instanceof TreePackage ||
				element instanceof TreeClass ||
				element instanceof TreeMethod;
	}

} 