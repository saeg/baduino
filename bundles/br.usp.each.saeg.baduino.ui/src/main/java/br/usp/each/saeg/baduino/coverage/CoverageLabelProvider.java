package br.usp.each.saeg.baduino.coverage;

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

import br.usp.each.saeg.baduino.tree.TreeClass;
import br.usp.each.saeg.baduino.tree.TreeDua;
import br.usp.each.saeg.baduino.tree.TreeMethod;
import br.usp.each.saeg.baduino.tree.TreePackage;
import br.usp.each.saeg.baduino.tree.TreeProject;

/**
 * 
 * @author Mario Concilio
 *
 */
public class CoverageLabelProvider implements ITableLabelProvider {

	private static final Image COVERED 		= getImage("check.png");
	private static final Image UNCOVERED 	= getImage("uncheck.png");
	private static final Image PERCENT 		= getImage("percent.png");
	private static final Image PROJECT 		= getImage("projects.gif");
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
	public void addListener(final ILabelProviderListener listener) {}
	
	@Override
	public void removeListener(final ILabelProviderListener listener) {}

	@Override
	public void dispose() {}

	@Override
	public boolean isLabelProperty(final Object element, final String property) {
		return false;
	}

	@Override
	public Image getColumnImage(final Object element, final int columnIndex) {
		Image image = null;
		
		switch(columnIndex) {
		
		// first column
		case 0:
			if (element instanceof TreeProject) {
				image = PROJECT;
			}
			else if (element instanceof TreePackage) {
				image = PACKAGE;
			}
			else if (element instanceof TreeClass) {
				image = CLASS;
			}
			else if (element instanceof TreeMethod) {
				final ISharedImages images = JavaUI.getSharedImages();
				final TreeMethod method = (TreeMethod) element;
				
				switch (method.getAccess()) {
					case Opcodes.ACC_PUBLIC:
						image = images.getImage(ISharedImages.IMG_OBJS_PUBLIC);
						break;
					
					case Opcodes.ACC_PROTECTED:
						image = images.getImage(ISharedImages.IMG_OBJS_PROTECTED);
						break;
					
					case Opcodes.ACC_PRIVATE:
						image = images.getImage(ISharedImages.IMG_OBJS_PRIVATE);
						break;
					
					default:
						image = images.getImage(ISharedImages.IMG_OBJS_DEFAULT);
				}				
			}
			else if (element instanceof TreeDua){
				image = DUA;
			}
			
			break;
			
		//second column	
		case 1: 
			if (element instanceof TreeDua) {
				final TreeDua dua = (TreeDua) element;
				image = dua.isCovered()? COVERED : UNCOVERED;
			}
			else {
				image = PERCENT;
			}
			
			break;
		}
		
		return image;
	}

	@Override
	public String getColumnText(final Object element, final int columnIndex) {
		String text = null;
		
		switch(columnIndex) {

		//first column
		case 0:
			if (element instanceof TreeProject) {
				final TreeProject project = (TreeProject) element;
				text = project.getName();
			}
			else if (element instanceof TreePackage) {
				final TreePackage pkg = (TreePackage) element;
				text = pkg.getName();
			}
			else if (element instanceof TreeClass) {
				final TreeClass clazz = (TreeClass) element;
				text = clazz.getName();

			}
			else if (element instanceof TreeMethod) {
				final TreeMethod method = (TreeMethod) element;
				text = method.getName();
			}
			else {
				final TreeDua dua = (TreeDua) element;
				text = dua.toString();
			}
			
			break;

		//second column
		case 1:
			if (element instanceof TreeProject) {
				final TreeProject project = (TreeProject) element;
				text = project.getCoverage();	
			}
			else if (element instanceof TreePackage) {
				final TreePackage pkg = (TreePackage) element;
				text = pkg.getCoverage();
			}
			else if (element instanceof TreeClass) {
				final TreeClass clazz = (TreeClass) element;
				text = clazz.getCoverage();
			}
			else if (element instanceof TreeMethod) {
				final TreeMethod method = (TreeMethod) element;
				text = method.getCoverage();
			}
			else {
				final TreeDua dua = (TreeDua) element;
				text = dua.isCovered()? "true" : "false";
			}
			
			break;
		}

		return text;
	} 
} 