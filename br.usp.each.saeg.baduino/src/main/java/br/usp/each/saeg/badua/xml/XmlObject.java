package br.usp.each.saeg.badua.xml;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;

import br.usp.each.saeg.badua.utils.ProjectUtils;

public class XmlObject {

	private static XmlInput instance;
	private static long fileLastModified;
	private static IResource resource;

	private XmlObject() {

	}

	public static XmlInput getInstance() {
		refresh();
		resource = ProjectUtils.getCurrentSelectedProject().getFile(
				"baduino.xml");
		if (!resource.exists()) {
			System.out.println("isOpen? "+ProjectUtils.getCurrentSelectedProject().isOpen());
			return null;
		}

		if (instance == null || fileRefreshed()) {
			System.out.println("Criando uma instance do xml");
			instance = XmlInput.unmarshal(resource.getLocation().toFile());
			updateLastModified();
		}

		return instance;
	}

	private static void refresh() {
		try {
			ProjectUtils.getCurrentSelectedProject().refreshLocal(
					IResource.DEPTH_ONE, null);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	// verify if the XML was updated and the instance needs to be refreshed
	private static boolean fileRefreshed() {
		long lastModified = resource.getLocation().toFile().lastModified();
		long resp = Long.compare(fileLastModified, lastModified);
		return resp != 0;
	}

	private static void updateLastModified() {
		fileLastModified = resource.getLocation().toFile().lastModified();
	}
}
