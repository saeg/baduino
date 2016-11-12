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
	
	private static final Logger logger = Logger.getLogger(ProjectModel.class);
	
	public static ProjectModel buildModel(IJavaProject javaProject) throws JavaModelException {
		final ProjectModel model = new ProjectModel();
		
		final IPath locationPath = javaProject.getProject().getLocation();
		final IPath output = javaProject.getOutputLocation();
		
		model.setJavaProject(javaProject);
		model.setName(javaProject.getProject().getName());
		model.setLocation(locationPath.toOSString());
		model.setClassesPath(locationPath.removeLastSegments(1).append(output).toOSString());
		model.setBaduinoPath(locationPath.append(".baduino").toOSString());
		model.setClasses(searchClasses(javaProject));
		model.setCoverageBinPath(locationPath.append(".baduino").append("coverage.ser").toOSString());
		model.setCoverageXmlPath(locationPath.append(".baduino").append("coverage.xml").toOSString());
		model.setJsonPath(locationPath.append(".baduino").append(javaProject.getProject().getName() + ".json").toOSString());
		
		return model;
	}
	
	private static List<String> searchClasses(IJavaProject javaProject) throws JavaModelException {
		return getClassesFiltering(javaProject, ".class");
	}

	private static List<String> searchTestClasses(IJavaProject javaProject) throws JavaModelException {
		return getClassesFiltering(javaProject, "Test.class");
	}
	
	private static List<String> getClassesFiltering(IJavaProject javaProject, String filter) throws JavaModelException {
		final IPath locationPath = javaProject.getProject().getLocation();
		final IPath output = javaProject.getOutputLocation();
		
		// create a list of output IPaths from project classpath
		Stream<IPath> streamPath = Stream.of(javaProject.readRawClasspath())
				.map(entry -> entry.getOutputLocation())
				.filter(path -> path != null);
		
		// add the first output path and make all IPaths absolute
		streamPath = Stream.concat(streamPath, Stream.of(output))
				.map(path -> locationPath.removeLastSegments(1).append(path));
		
		// filter all output paths containing Test.class files
		List<IPath> testPaths = streamPath.filter(path -> {
			try (Stream<Path> files = Files.walk(Paths.get(path.toOSString()))) {
				List<String> filenames = files.map(String::valueOf)
						.filter(name -> name.endsWith(filter))
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
		
		List<String> testClasses = testPaths.stream()
				.map(ipath -> {
					final Path absolute = Paths.get(ipath.toOSString());

					try (Stream<Path> files = Files.walk(absolute)) {
						List<String> filenames = files
								.map(path -> absolute.relativize(path)) // get the package, ignoring absolute path
								.map(String::valueOf)
								.filter(file -> file.endsWith(filter))
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
		
		return testClasses;
	}
	
}
