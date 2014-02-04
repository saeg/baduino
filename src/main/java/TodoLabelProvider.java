import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;


public class TodoLabelProvider extends LabelProvider {
  
  private static final Image FOLDER = getImage("folder.gif");
  private static final Image FILE = getImage("file.gif");
  private static final Image PUBLIC_CO = getImage("public_co.gif");
  
  
  @Override
  public String getText(Object element) {
    if (element instanceof Category) {
      Category category = (Category) element;
      return category.getName();
    }
    return ((Todo) element).getSummary();
  }

  @Override
  public Image getImage(Object element) {
    if (element instanceof Category) {
      return PUBLIC_CO;
    }
    return FILE;
  }

  // Helper Method to load the images
  private static Image getImage(String file) {
    Bundle bundle = FrameworkUtil.getBundle(TodoLabelProvider.class);
    URL url = FileLocator.find(bundle, new Path("icons/" + file), null);
    ImageDescriptor image = ImageDescriptor.createFromURL(url);
    return image.createImage();

  } 
} 