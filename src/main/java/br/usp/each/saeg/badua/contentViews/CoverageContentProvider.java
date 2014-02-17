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
    return model.getMethods().toArray();
  }

  @Override
  public Object[] getChildren(Object parentElement) {
    if (parentElement instanceof Methods) {
      Methods category = (Methods) parentElement;
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
    if (element instanceof Methods) {
      return true;
    }
    return false;
  }

} 