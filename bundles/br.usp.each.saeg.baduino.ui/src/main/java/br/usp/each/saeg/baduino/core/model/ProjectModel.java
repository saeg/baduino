package br.usp.each.saeg.baduino.core.model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IJavaProject;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 
 * @author Mario Concilio
 *
 */
public class ProjectModel {
	
	private String name;
	private String classesPath;
	private String baduinoPath;
	private String location;
	private String coverageBinPath;
	private String coverageXmlPath;
	private String jsonPath;
	
	private List<String> classes;
	
	@JsonIgnore
	private IJavaProject javaProject;
	
	public ProjectModel() {
		
	}
		
	public void build() throws CoreException, IOException {
		// clean and build the project
		final IProject project = javaProject.getProject();

		project.build(IncrementalProjectBuilder.CLEAN_BUILD, new NullProgressMonitor());
		project.build(IncrementalProjectBuilder.FULL_BUILD, new NullProgressMonitor());
		
		// creates baduino folder
		Path path = Paths.get(baduinoPath);
		if (!Files.exists(path)) {
			Files.createDirectory(path);
		}
	}
	
	public List<String> getClasses() {
		return classes;
	}

	public void setClasses(List<String> classes) {
		this.classes = classes;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getClassesPath() {
		return classesPath;
	}

	public void setClassesPath(String path) {
		this.classesPath = path;
	}

	public String getBaduinoPath() {
		return baduinoPath;
	}

	public void setBaduinoPath(String path) {
		this.baduinoPath = path;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public IJavaProject getJavaProject() {
		return javaProject;
	}

	public void setJavaProject(IJavaProject javaProject) {
		this.javaProject = javaProject;
	}

	public String getCoverageBinPath() {
		return coverageBinPath;
	}

	public void setCoverageBinPath(String path) {
		this.coverageBinPath = path;
	}

	public String getCoverageXmlPath() {
		return coverageXmlPath;
	}

	public void setCoverageXmlPath(String path) {
		this.coverageXmlPath = path;
	}

	public String getJsonPath() {
		return jsonPath;
	}

	public void setJsonPath(String jsonPath) {
		this.jsonPath = jsonPath;
	}

}
