package br.usp.each.saeg.baduino.xml;

import java.io.File;

import javax.xml.bind.JAXBContext;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaProject;

import br.usp.each.saeg.baduino.util.ProjectUtils;

/**
 * 
 * @author Mario Concilio
 *
 */
public class XMLFactory {
	
	private static final Logger logger = Logger.getLogger(XMLFactory.class);

	private static XMLProject instance = null;
	private static long fileLastModified;
	
	public static XMLProject getInstance(File file) {
		if (!file.exists()) {
			logger.debug("is project open? " + ProjectUtils.getCurrentSelectedProject().isOpen());
			return null;
		}

		if (instance == null || fileRefreshed(file)) {
			logger.debug("getting instance from xml file");
			instance = unmarshal(file);
			updateLastModified(file);
		}

		return instance;
	}

	public static XMLProject getInstance() {
		refresh();
		
		final IJavaProject javaProject = ProjectUtils.getCurrentSelectedJavaProject();
		File file = javaProject.getProject().getLocation().append(".baduino").append("coverage.xml").toFile();
		
		return getInstance(file);
	}
	
	private static XMLProject unmarshal(File reportLocation) {
        try {
            JAXBContext context = JAXBContext.newInstance(XMLProject.class);
            return (XMLProject) context.createUnmarshaller().unmarshal(reportLocation);
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
	private static boolean fileRefreshed(File file) {
		long lastModified = file.lastModified();
		long resp = Long.compare(fileLastModified, lastModified);
		
		return resp != 0;
	}

	private static void updateLastModified(File file) {
//		fileLastModified = resource.getLocation().toFile().lastModified();
		fileLastModified = file.lastModified();
	}
}
