package br.usp.each.saeg.badua.contentViews;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class CoverageContentProvider implements ITreeContentProvider {

	private CoverageMockModel model;

	@Override
	public void dispose() {
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		this.model = (CoverageMockModel) newInput;
	}

	@Override
	public Object[] getElements(Object inputElement) {
		return model.getTree().toArray();
	}

	@Override
	public Object[] getChildren(Object parentElement) {

		if(parentElement instanceof TreeProject){
			TreeProject Project = (TreeProject) parentElement;
			return Project.getFolders().toArray();

		}else if(parentElement instanceof TreeFolder){
			TreeFolder Folder = (TreeFolder) parentElement;
			return Folder.getPackages().toArray();

		}else if(parentElement instanceof TreePackage){
			TreePackage Package = (TreePackage) parentElement;
			return Package.getClasses().toArray();

		}else if(parentElement instanceof TreeClass){
			TreeClass Class = (TreeClass) parentElement;
			return Class.getMethods().toArray();

		}else if (parentElement instanceof TreeMethod) {
			TreeMethod category = (TreeMethod) parentElement;
			return category.getDUAS().toArray();
		}
		return null;
	}

	@Override
	public Object getParent(Object element) {
		return null;	
	}

	@Override
	public boolean hasChildren(Object element) {
		if(element instanceof TreeProject){
			return true;
			
		}else if(element instanceof TreeFolder){
			return true;
			
		}else if(element instanceof TreePackage){
			return true;
			
		}else if(element instanceof TreeClass){
			return true;
			
		}else if (element instanceof TreeMethod) {
			return true;
			
		}else{
			return false;	
		}

	}

} 