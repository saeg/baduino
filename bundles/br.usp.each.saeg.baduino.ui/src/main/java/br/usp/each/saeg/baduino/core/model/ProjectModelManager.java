package br.usp.each.saeg.baduino.core.model;

import java.io.File;
import java.io.FileReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 * 
 * @author Mario Concilio
 *
 */
public class ProjectModelManager {

	/**
	 * Writes the ProjectModel to disk as xml file.
	 * @param model
	 * @throws Exception
	 */
	public static void writeToDisk(final ProjectModel model) throws Exception {		
		final JAXBContext context = JAXBContext.newInstance(ProjectModel.class);
        final Marshaller m = context.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        m.marshal(model, new File(model.getXmlPath()));
	}
	
	/**
	 * Reads the ProjectModel from disk from a given path.
	 * @param path
	 * @return
	 * @throws Exception
	 */
	public static ProjectModel readFromDisk(final String path) throws Exception {
		final JAXBContext context = JAXBContext.newInstance(ProjectModel.class);
		final Unmarshaller um = context.createUnmarshaller();
        final ProjectModel model = (ProjectModel) um.unmarshal(new FileReader(path));
		
		return model;
	}

}
