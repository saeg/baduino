package br.usp.each.saeg.baduino.xml;

import java.io.File;

import javax.xml.bind.JAXBContext;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaProject;

import br.usp.each.saeg.baduino.util.ProjectUtils;

public class XmlObject {

	private static XmlInput instance = null;
	private static long fileLastModified;
//	private static IResource resource;
	private static File xmlFile;

	private XmlObject() {

	}

	public static XmlInput getInstance() {
		refresh();
//		resource = ProjectUtils.getCurrentSelectedProject().getFile(".baduino/coverage.xml");
		
		final IJavaProject javaProject = ProjectUtils.getCurrentSelectedJavaProject();
		xmlFile = javaProject.getProject().getLocation().append(".baduino").append("coverage.xml").toFile();
		
//		if (!resource.exists()) {
		if (!xmlFile.exists()) {
			System.out.println("isOpen? " + ProjectUtils.getCurrentSelectedProject().isOpen());
			return null;
		}

		if (instance == null || fileRefreshed()) {
			System.out.println("Criando uma instance do xml");
//			instance = unmarshal(resource.getLocation().toFile());
			instance = unmarshal(xmlFile);
			updateLastModified();
		}

		return instance;
	}
	
	private static XmlInput unmarshal(File reportLocation) {
        try {
            JAXBContext context = JAXBContext.newInstance(XmlInput.class);
            return (XmlInput) context.createUnmarshaller().unmarshal(reportLocation);
        } 
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

	private static void refresh() {
		try {
			ProjectUtils.getCurrentSelectedProject().refreshLocal(IResource.DEPTH_ONE, null);
		} 
		catch (CoreException e) {
			e.printStackTrace();
		}
	}

	// verify if the XML was updated and the instance needs to be refreshed
	private static boolean fileRefreshed() {
//		long lastModified = resource.getLocation().toFile().lastModified();
		long lastModified = xmlFile.lastModified();
		long resp = Long.compare(fileLastModified, lastModified);
		
		return resp != 0;
	}

	private static void updateLastModified() {
//		fileLastModified = resource.getLocation().toFile().lastModified();
		fileLastModified = xmlFile.lastModified();
	}
}
