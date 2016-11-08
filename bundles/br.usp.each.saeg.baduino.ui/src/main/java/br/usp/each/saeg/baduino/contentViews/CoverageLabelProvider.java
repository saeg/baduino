package br.usp.each.saeg.baduino.contentViews;

import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.ui.ISharedImages;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;
import org.objectweb.asm.Opcodes;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;


public class CoverageLabelProvider implements ITableLabelProvider {

	private static final Image COVERED 		= getImage("check.png");
	private static final Image UNCOVERED 	= getImage("uncheck.png");
	private static final Image PERCENT 		= getImage("percent.png");
	private static final Image PROJECT 		= getImage("projects.gif");
	private static final Image FOLDER 		= getImage("folder.gif");
	private static final Image PACKAGE 		= getImage("package.gif");
	private static final Image CLASS 		= getImage("classes.gif");
	private static final Image DUA 			= getImage("dua.png");

	// Helper Method to load the images
	public static Image getImage(final String file) {
		final Bundle bundle = FrameworkUtil.getBundle(CoverageLabelProvider.class);
		final URL url = FileLocator.find(bundle, new Path("src/main/resources/icons/" + file), null);
		final ImageDescriptor image = ImageDescriptor.createFromURL(url);
		
		return image.createImage();
	}

	@Override
	public void addListener(ILabelProviderListener listener) {}

	@Override
	public void dispose() {}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {}

	@Override
	public Image getColumnImage(final Object element, final int columnIndex) {
		switch(columnIndex) {
		
		case 0://first column
			if (element instanceof TreeProject) {
				return PROJECT;
			}
			else if (element instanceof TreeFolder) {
				return FOLDER;
			}
			else if (element instanceof TreePackage) {
				return PACKAGE;
			}
			else if (element instanceof TreeClass) {
				return CLASS;
			}
			else if (element instanceof TreeMethod) {
				final ISharedImages images = JavaUI.getSharedImages();
				final TreeMethod method = (TreeMethod) element;
				Image img = null;
				
				switch (method.getAccess()) {
					case Opcodes.ACC_PUBLIC:
						img = images.getImage(ISharedImages.IMG_OBJS_PUBLIC);
						break;
					
					case Opcodes.ACC_PROTECTED:
						img = images.getImage(ISharedImages.IMG_OBJS_PROTECTED);
						break;
					
					case Opcodes.ACC_PRIVATE:
						img = images.getImage(ISharedImages.IMG_OBJS_PRIVATE);
						break;
					
					default:
						img = images.getImage(ISharedImages.IMG_OBJS_DEFAULT);
				}
				
				return img;
				
//				if(method.getAccess() == Opcodes.ACC_PUBLIC) {
//					img = images.getImage(ISharedImages.IMG_OBJS_PUBLIC);
//				}
//				if (method.getAccess() == Opcodes.ACC_PROTECTED) {
//					return images.getImage(ISharedImages.IMG_OBJS_PROTECTED);
//				}
//				if (method.getAccess() == Opcodes.ACC_PRIVATE) {
//					return images.getImage(ISharedImages.IMG_OBJS_PRIVATE);
//				}
//					return images.getImage(ISharedImages.IMG_OBJS_DEFAULT);
					
			}
			else if (element instanceof TreeDUA){
				return DUA;
			}
			
			return null;
			
		case 1://second column
			if (element instanceof TreeProject) {
				return PERCENT;
			}
			else if (element instanceof TreeFolder) {
				return PERCENT;
			}
			else if (element instanceof TreePackage) {
				return PERCENT;
			}
			else if (element instanceof TreeClass) {
				return PERCENT;
			}
			else if (element instanceof TreeMethod) {
				return PERCENT;
			}
			else {
				final TreeDUA dua = (TreeDUA) element;
				return dua.isCovered() ? COVERED : UNCOVERED;
						
//				if (dua.isCovered()){
//					return COVERED;
//				}
//				else {
//					return UNCOVERED;
//				}
			}
		}
		
		return null;
	}

	@Override
	public String getColumnText(final Object element, final int columnIndex) {
		switch(columnIndex) {

		case 0://first column
			if(element instanceof TreeProject) {
				final TreeProject project = (TreeProject) element;
				return project.getName();
			}
			else if (element instanceof TreeFolder) {
				final TreeFolder folder = (TreeFolder) element;
				return folder.getName();
			}
			else if (element instanceof TreePackage) {
				final TreePackage pkg = (TreePackage) element;
				return pkg.getName();
			}
			else if (element instanceof TreeClass) {
				final TreeClass clazz = (TreeClass) element;
				return clazz.getName();

			}
			else if (element instanceof TreeMethod) {
				final TreeMethod method = (TreeMethod) element;
				return method.getName();
			}
			else {
				final TreeDUA dua = (TreeDUA) element;
				return dua.toString();
			}

		case 1://second column
			if (element instanceof TreeProject) {
				final TreeProject project = (TreeProject) element;
				return project.getCoverage();	
			}
			else if (element instanceof TreeFolder) {
				final TreeFolder folder = (TreeFolder) element;
				return folder.getCoverage();	
			}
			else if (element instanceof TreePackage) {
				final TreePackage pkg = (TreePackage) element;
				return pkg.getCoverage();
			}
			else if (element instanceof TreeClass) {
				final TreeClass clazz = (TreeClass) element;
				return clazz.getCoverage();
			}
			else if (element instanceof TreeMethod) {
				final TreeMethod method = (TreeMethod) element;
				return method.getCoverage();
			}
			else {
				final TreeDUA dua = (TreeDUA) element;
				return dua.isCovered()? "true" : "false";
			}
		}

		return null;
	} 
} 