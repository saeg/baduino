package br.usp.each.saeg.baduino.core.model;

import java.io.File;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @author Mario Concilio
 *
 */
public class ProjectModelManager {

	/**
	 * Writes the ProjectModel to disk as json file.
	 * @param model
	 * @throws Exception
	 */
	public static void writeToDisk(final ProjectModel model) throws Exception {
		final File jsonFile = new File(model.getJsonPath());
		final ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(jsonFile, model);
	}
	
	/**
	 * Reads the ProjectModel from disk from a given path.
	 * @param path
	 * @return
	 * @throws Exception
	 */
	public static ProjectModel readFromDisk(final String path) throws Exception {
		final File jsonFile = new File(path);
		final ObjectMapper mapper = new ObjectMapper();
		ProjectModel model = mapper.readValue(jsonFile, ProjectModel.class);
		
		return model;
	}

}
