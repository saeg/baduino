package br.usp.each.saeg.baduino.util;

import java.io.FileInputStream;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyManager {

	private static final String FOLDER_SEPARATOR = System.getProperty("file.separator");
	private static final String CONFIG_FILE = "baduino.properties";
	private static final String INCLUDES = "*";
	
	private final String projectLocation;
	
	private String jacocoAgentJar;
	private String jaguarJar;
	private String compiledTestsDir;
	private String compiledClassesDir;
	private String projectDir;
	private String includes;

	public PropertyManager(String projectLocation) {
		super();
		this.projectLocation = projectLocation;
		loadProperties();
	}

	public void loadProperties() {
		Properties prop = getPropertyFile(ProjectUtils.getCurrentSelectedProject().getLocation() + FOLDER_SEPARATOR + CONFIG_FILE);
		if (prop == null) {
			System.out.println("Using default properties!");
			setProjectDir(projectLocation);
			setJaguarJar(projectLocation + FOLDER_SEPARATOR + "jaguar.jar");
			setCompiledTestsDir(projectLocation + FOLDER_SEPARATOR + "target" + FOLDER_SEPARATOR + "test-classes");
			setCompiledClassesDir(projectLocation + FOLDER_SEPARATOR + "target" + FOLDER_SEPARATOR + "classes");
			setIncludes(INCLUDES);
			return;
		}
		setProjectDir(prop.getProperty("project.dir", projectLocation));
		setJaguarJar(prop.getProperty("jaguar.jar", projectLocation + FOLDER_SEPARATOR + "jaguar.jar"));
		setCompiledTestsDir(prop.getProperty("compiled.tests.dir", projectLocation + FOLDER_SEPARATOR + "target" + FOLDER_SEPARATOR + "test-classes"));
		setCompiledClassesDir(prop.getProperty("compiled.classes.dir", projectLocation + FOLDER_SEPARATOR + "target" + FOLDER_SEPARATOR + "classes"));
		setIncludes(prop.getProperty("includes", INCLUDES));
	}

	public String getJacocoAgentJar() {
		return jacocoAgentJar;
	}

	public String getJaguarJar() {
		return jaguarJar;
	}

	public String getCompiledTestsDir() {
		return compiledTestsDir;
	}

	public String getCompiledClassesDir() {
		return compiledClassesDir;
	}

	public String getProjectDir() {
		return projectDir;
	}
	
	public String getIncludes() {
		return includes;
	}

	public void setJacocoAgentJar(String jacocoAgentJar) {
		this.jacocoAgentJar = jacocoAgentJar;
		System.out.println("jacocoAgentJar = " + jacocoAgentJar);
	}

	public void setJaguarJar(String jaguarJar) {
		this.jaguarJar = jaguarJar;
		System.out.println("jaguarJar = " + jaguarJar);
	}

	public void setCompiledTestsDir(String compiledTestsDir) {
		this.compiledTestsDir = compiledTestsDir;
		System.out.println("compiledTestsDir = " + compiledTestsDir);
	}

	public void setCompiledClassesDir(String compiledClassesDir) {
		this.compiledClassesDir = compiledClassesDir;
		System.out.println("compiledClassesDir = " + compiledClassesDir);
	}

	public void setProjectDir(String projectDir) {
		this.projectDir = projectDir;
		System.out.println("projectDir = " + projectDir);
	}
	
	public void setIncludes(String includes) {
		this.includes = includes;
		System.out.println("includes = " + includes);
	}
	
	public String getProperty(String propertyName) {
		return getProperty(propertyName, ProjectUtils.getCurrentSelectedProject().getLocation() + FOLDER_SEPARATOR + CONFIG_FILE);
	}

	public String getProperty(String propertyName, String fileName) {
		Properties prop = getPropertyFile(fileName);
		return prop.getProperty(propertyName);
	}

	public Properties getPropertyFile(String fileName) {
		Properties prop = new Properties();
		InputStream input = null;

		try {
			input = new FileInputStream(fileName);
			prop.load(input);
		} catch (IOException ex) {
			System.out.println("File not found: " + fileName);
			return null;
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return prop;
	}
	
}