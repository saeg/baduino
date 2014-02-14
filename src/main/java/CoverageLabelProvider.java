import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;


public class CoverageLabelProvider implements ITableLabelProvider {

	private static final Image FOLDER = getImage("folder.gif");
	private static final Image FILE = getImage("file.gif");
	private static final Image PUBLIC_CO = getImage("public_co.gif");


	//  @Override
	//  public String getText(Object element) {
	//    if (element instanceof Methods) {
	//      Methods method = (Methods) element;
	//      return method.getName();
	//    }
	//    return ((DUA) element).toString();
	//  }
	//
	//  @Override
	//  public Image getImage(Object element) {
	//    if (element instanceof Methods) {
	//      return PUBLIC_CO;
	//    }
	//    return FILE;
	//  }
	//
	  // Helper Method to load the images
	  private static Image getImage(String file) {
	    Bundle bundle = FrameworkUtil.getBundle(CoverageLabelProvider.class);
	    URL url = FileLocator.find(bundle, new Path("icons/" + file), null);
	    ImageDescriptor image = ImageDescriptor.createFromURL(url);
	    return image.createImage();
	
	  }

	@Override
	public void addListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		switch(columnIndex){
		case 0: 
			if (element instanceof Methods) {
				return PUBLIC_CO;
			}
			return FILE;
		}
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		switch(columnIndex){
		case 0: 
			if (element instanceof Methods) {
				Methods method = (Methods) element;
				return method.getName();
			}
			return ((DUA) element).toString();
		}
		return null;
	} 
} 