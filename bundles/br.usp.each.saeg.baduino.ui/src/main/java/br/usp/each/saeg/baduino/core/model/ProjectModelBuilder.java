package br.usp.each.saeg.baduino.core.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;

/**
 * This class builds a new ProjectModel from a IJavaProject.
 * @author Mario Concilio
 *
 */
public class ProjectModelBuilder {
	
	private static final Logger logger = Logger.getLogger(ProjectModelBuilder.class);
	
	private IJavaProject javaProject;
	private IPath locationPath;
	private IPath outputPath;
	
	public ProjectModelBuilder(IJavaProject javaProject) throws JavaModelException {
		this.javaProject = javaProject;
		this.locationPath = javaProject.getProject().getLocation();
		this.outputPath = javaProject.getOutputLocation().removeFirstSegments(1);
	}
	
	public ProjectModel build() throws JavaModelException {
		final ProjectModel model = new ProjectModel();
		final String name = javaProject.getProject().getName();
		
		model.setJavaProject(javaProject);
		model.setName(name);
		model.setLocation(locationPath.toOSString());
		model.setClassesPath(locationPath.append(outputPath).toOSString());
		model.setBaduinoPath(locationPath.append(".baduino").toOSString());
		model.setClasses(getClasses());
		model.setCoverageBinPath(locationPath.append(".baduino").append("coverage.ser").toOSString());
		model.setCoverageXmlPath(locationPath.append(".baduino").append("coverage.xml").toOSString());
		model.setXmlPath(locationPath.append(".baduino").append(name + ".xml").toOSString());
		
		return model;
	}
	
	private List<String> getClasses() throws JavaModelException {
		// create a list of output IPaths from project classpath
		Stream<IPath> streamPath = Stream.of(javaProject.readRawClasspath())
				.map(entry -> entry.getOutputLocation())
				.filter(path -> path != null)
				.map(entry -> entry.removeFirstSegments(1))
				.distinct();
		
		// add the first output path and make all IPaths absolute
		streamPath = Stream.concat(streamPath, Stream.of(outputPath))
				.map(path -> locationPath.append(path));
		
		// filter all output paths containing files
		List<IPath> paths = streamPath.filter(path -> {
			try (Stream<Path> files = Files.walk(Paths.get(path.toOSString()))) {
				List<String> filenames = files.map(String::valueOf)
						.filter(name -> name.endsWith(".class"))
						.collect(Collectors.toList());

				return !filenames.isEmpty();
			} 
			catch (IOException e) {
				logger.error("File not found: " + path.toOSString());
				return false;
			}

		})
				.distinct()
				.collect(Collectors.toList());
		
		List<String> classes = paths.stream()
				.map(ipath -> {
					final Path absolute = Paths.get(ipath.toOSString());

					try (Stream<Path> files = Files.walk(absolute)) {
						List<String> filenames = files
								.map(path -> absolute.relativize(path)) // get the package, ignoring absolute path
								.map(String::valueOf)
								.filter(file -> file.endsWith(".class"))
								.map(file -> file.replace(File.separator, ".").substring(0, file.length() - 6)) // transforming dir notation to package notation and removing '.class'
								.collect(Collectors.toList());

						return filenames;
					} 
					catch (IOException e) {
						logger.error("File not found", e);
						return null;
					}
				})
				.flatMap(x -> x.stream())
				.collect(Collectors.toList());
		
		return classes;
	}
	
	public static ProjectModel buildModel(IJavaProject javaProject) throws JavaModelException {
		final ProjectModelBuilder builder = new ProjectModelBuilder(javaProject);
		return builder.build();
	}
	
}
