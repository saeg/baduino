package br.usp.each.saeg.baduino.core.model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IJavaProject;

/**
 * 
 * @author Mario Concilio
 *
 */
@XmlRootElement(name="model")
public class ProjectModel {
	
	private String name;
	private String classesPath;
	private String baduinoPath;
	private String location;
	private String coverageBinPath;
	private String coverageXmlPath;
	private String xmlPath;
	private List<String> classes;
	private IJavaProject javaProject;
		
	public void build() throws CoreException, IOException {
		// clean and build the project
		final IProject project = javaProject.getProject();

		project.build(IncrementalProjectBuilder.CLEAN_BUILD, new NullProgressMonitor());
		project.build(IncrementalProjectBuilder.FULL_BUILD, new NullProgressMonitor());
		
		// create baduino folder
		Path path = Paths.get(getBaduinoPath());
		if (!Files.exists(path)) {
			Files.createDirectory(path);
		}
	}
	
	@XmlAttribute(name="name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@XmlElementWrapper(name="classes")
	@XmlElement(name="class")
	public List<String> getClasses() {
		return classes;
	}

	public void setClasses(List<String> classes) {
		this.classes = classes;
	}

	@XmlElement(name="classes-path")
	public String getClassesPath() {
		return classesPath;
	}

	public void setClassesPath(String path) {
		this.classesPath = path;
	}

	@XmlElement(name="baduino")
	public String getBaduinoPath() {
		return baduinoPath;
	}

	public void setBaduinoPath(String path) {
		this.baduinoPath = path;
	}

	@XmlElement(name="location")
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	
	public String getCoverageBinPath() {
		return coverageBinPath;
	}

	@XmlElement(name="coverage-bin")
	public void setCoverageBinPath(String path) {
		this.coverageBinPath = path;
	}

	
	public String getCoverageXmlPath() {
		return coverageXmlPath;
	}

	@XmlElement(name="coverage-xml")
	public void setCoverageXmlPath(String path) {
		this.coverageXmlPath = path;
	}

	@XmlElement(name="path")
	public String getXmlPath() {
		return xmlPath;
	}

	public void setXmlPath(String xmlPath) {
		this.xmlPath = xmlPath;
	}
	
	@XmlTransient
	public IJavaProject getJavaProject() {
		return javaProject;
	}

	public void setJavaProject(IJavaProject javaProject) {
		this.javaProject = javaProject;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof ProjectModel))
			return false;
		
		if (obj == this)
			return true;
		
		final ProjectModel model = (ProjectModel) obj;
		return location.equals(model.getLocation());
	}
	
	@Override
	public int hashCode() {
		return location.hashCode();
	}

}
