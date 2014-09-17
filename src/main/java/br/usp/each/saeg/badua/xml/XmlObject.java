package br.usp.each.saeg.badua.xml;


import org.eclipse.core.resources.IResource;
import br.usp.each.saeg.badua.utils.ProjectUtils;

public class XmlObject {

	private static XmlInput instance;
	private static long fileLastModified;
	private static IResource resource;

	private XmlObject(){

	}

	public static XmlInput getInstance(){
		resource = ProjectUtils.getCurrentSelectedProject().getFile("baduino.xml");
		if(instance == null | fileRefreshed()){
			instance = XmlInput.unmarshal(resource.getLocation().toFile());
		}
		return instance;
	}

	//verify if the XML was updated and the instance needs to be refreshed
	private static boolean fileRefreshed() {
		long lastModified = resource.getLocation().toFile().lastModified();
		long resp = Long.compare(fileLastModified, lastModified);
		if(resp == 0) return false;
		fileLastModified = lastModified;
		return true;
	}
}
