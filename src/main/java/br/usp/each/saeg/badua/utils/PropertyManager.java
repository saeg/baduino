package br.usp.each.saeg.badua.utils;

import java.io.FileInputStream;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyManager {

	private static final String FOLDER_SEPARATOR = System.getProperty("file.separator");
	private static final String PROJECT_DIR = ProjectUtils.getCurrentSelectedProject().getLocation().toString();
	private static final String COMPILED_CLASSES_DIR = ProjectUtils.getCurrentSelectedProject().getLocation() + FOLDER_SEPARATOR + "target" + FOLDER_SEPARATOR + "classes";
	private static final String COMPILED_TESTS_DIR = ProjectUtils.getCurrentSelectedProject().getLocation() + FOLDER_SEPARATOR + "target" + FOLDER_SEPARATOR + "test-classes";
	private static final String JAGUAR_JAR = ProjectUtils.getCurrentSelectedProject().getLocation() + FOLDER_SEPARATOR + "jaguar.jar";
	private static final String JACOCO_AGENT_JAR = ProjectUtils.getCurrentSelectedProject().getLocation() + FOLDER_SEPARATOR + "jacocoagent.jar";
	private static final String HEURISTIC = "Tarantula";
	private static final String CONFIG_FILE = "codeforest.properties";
	
	private String jacocoAgentJar;
	private String jaguarJar;
	private String compiledTestsDir;
	private String compiledClassesDir;
	private String projectDir;
	private String heuristic;

	public PropertyManager() {
		super();
		loadProperties();
	}

	private void loadProperties() {
		Properties prop = getPropertyFile(ProjectUtils.getCurrentSelectedProject().getLocation() + FOLDER_SEPARATOR + CONFIG_FILE);
		if (prop == null) {
			System.out.println("Using default properties!");
			setJacocoAgentJar(JACOCO_AGENT_JAR);
			setJaguarJar(JAGUAR_JAR);
			setCompiledTestsDir(COMPILED_TESTS_DIR);
			setCompiledClassesDir(COMPILED_CLASSES_DIR);
			setProjectDir(PROJECT_DIR);
			setHeuristic(HEURISTIC);
			return;
		}
		
		setJacocoAgentJar(prop.getProperty("jacoco.agent.jar", JACOCO_AGENT_JAR));
		setJaguarJar(prop.getProperty("jaguar.jar", JAGUAR_JAR));
		setCompiledTestsDir(prop.getProperty("compiled.tests.dir", COMPILED_TESTS_DIR));
		setCompiledClassesDir(prop.getProperty("compiled.classes.dir", COMPILED_CLASSES_DIR));
		setProjectDir(prop.getProperty("project.dir", PROJECT_DIR));
		setHeuristic(prop.getProperty("heuristic", HEURISTIC));
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

	public String getHeuristic() {
		return heuristic;
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
	
	public void setHeuristic(String heuristic) {
		this.heuristic = heuristic;
		System.out.println("heuristic = " + heuristic);
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