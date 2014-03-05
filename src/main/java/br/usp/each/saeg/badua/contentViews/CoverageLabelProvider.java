package br.usp.each.saeg.badua.contentViews;

import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;
import org.objectweb.asm.Opcodes;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.eclipse.jdt.ui.ISharedImages;
import org.eclipse.jdt.ui.JavaUI;


public class CoverageLabelProvider implements ITableLabelProvider {

	private static final Image COVERED = getImage("check.png");
	private static final Image UNCOVERED = getImage("uncheck.png");
	private static final Image PERCENT = getImage("percent.png");

	private static final Image PROJECT = getImage("projects.gif");
	private static final Image FOLDER = getImage("folder.gif");
	private static final Image PACKAGE = getImage("package.gif");
	private static final Image CLASS = getImage("classes.gif");

	// Helper Method to load the images
	private static Image getImage(String file) {
		Bundle bundle = FrameworkUtil.getBundle(CoverageLabelProvider.class);
		URL url = FileLocator.find(bundle, new Path("icons/" + file), null);
		ImageDescriptor image = ImageDescriptor.createFromURL(url);
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
	public Image getColumnImage(Object element, int columnIndex) {
		switch(columnIndex){
		
		case 0://first column
			if(element instanceof TreeProject){
				return PROJECT;
				
			}else if(element instanceof TreeFolder){
				return FOLDER;
				
			}else if(element instanceof TreePackage){
				return PACKAGE;
				
			}else if(element instanceof TreeClass){
				return CLASS;
				
			}else  if (element instanceof TreeMethod) {
				ISharedImages images = JavaUI.getSharedImages();
				TreeMethod method = (TreeMethod)element;
				if(method.getAccess() == Opcodes.ACC_PUBLIC){
					return images.getImage(ISharedImages.IMG_OBJS_PUBLIC);
				}else if(method.getAccess() == Opcodes.ACC_PROTECTED){
					return images.getImage(ISharedImages.IMG_OBJS_PROTECTED);
				}else if(method.getAccess() == Opcodes.ACC_PRIVATE){
					return images.getImage(ISharedImages.IMG_OBJS_PRIVATE);
				}else{
					return images.getImage(ISharedImages.IMG_OBJS_DEFAULT);
				}
			}
			return null;
			
		case 1://second column
			
			if(element instanceof TreeProject){
				return PERCENT;
				
			}else if(element instanceof TreeFolder){
				return PERCENT;
				
			}else if(element instanceof TreePackage){
				return PERCENT;
				
			}else if(element instanceof TreeClass){
				return PERCENT;
				
			}else if (element instanceof TreeMethod) {
				return PERCENT;
				
			}else{
				TreeDUA dua = (TreeDUA) element;
				if(dua.getCovered().equals("true")){
					return COVERED;
				}else {
					return UNCOVERED;
				}
			}
		}
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		switch(columnIndex){
		
		case 0://first column
			
			if(element instanceof TreeProject){
				TreeProject Project = (TreeProject) element;
				return Project.getName();
				
			}else if(element instanceof TreeFolder){
				TreeFolder Folder = (TreeFolder) element;
				return Folder.getName();
				
			}else if(element instanceof TreePackage){
				TreePackage Package = (TreePackage) element;
				return Package.getName();
				
			}else if(element instanceof TreeClass){
				TreeClass Class = (TreeClass) element;
				return Class.getName();
				
			}else if (element instanceof TreeMethod) {
				TreeMethod Method = (TreeMethod) element;
				return Method.getName();
				
			}else {
				return ((TreeDUA) element).toString();
			}
			
		case 1://second column
			if(element instanceof TreeProject){
				TreeProject Project = (TreeProject) element;
				return Project.getCoverage();
				
			}else if(element instanceof TreeFolder){
				TreeFolder Folder = (TreeFolder) element;
				return Folder.getCoverage();
				
			}else if(element instanceof TreePackage){
				TreePackage Package = (TreePackage) element;
				return Package.getCoverage();
				
			}else if(element instanceof TreeClass){
				TreeClass Class = (TreeClass) element;
				return Class.getCoverage();
				
			}else if (element instanceof TreeMethod) {
				TreeMethod method = (TreeMethod) element;
				return method.getCoverage();
				
			}else{
				TreeDUA dua = (TreeDUA)element;
				return dua.getCovered();
			}
		}
		return null;
	} 
} 