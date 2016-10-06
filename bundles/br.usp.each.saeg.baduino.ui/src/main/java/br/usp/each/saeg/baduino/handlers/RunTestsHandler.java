package br.usp.each.saeg.baduino.handlers;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.junit.runner.JUnitCore;

import br.usp.each.saeg.badua.cli.Instrument;
import br.usp.each.saeg.baduino.utils.ProjectUtils;
import static br.usp.each.saeg.baduino.utils.LambdaExceptionUtils.*;

public class RunTestsHandler extends AbstractHandler implements IJavaLaunchConfigurationConstants {
	
	@Override
	public Object execute(ExecutionEvent arg) throws ExecutionException {

		final IProject project = ProjectUtils.getCurrentSelectedProject();
		final IJavaProject javaProject = ProjectUtils.getCurrentSelectedJavaProject();
		final String projectName = project.getName();
		final IPath location = project.getLocation();
		
		System.out.println("[INFO] Building project " + projectName);
		System.out.println("[INFO] Project location: " + location.toOSString());

		try {
			final IPath output = javaProject.getOutputLocation();
			final IPath src = location.removeLastSegments(1).append(output);
			final IPath dest = location.append(".baduino").append("classes");
			
			// clean and build the project
			project.build(IncrementalProjectBuilder.CLEAN_BUILD, new NullProgressMonitor());
			project.build(IncrementalProjectBuilder.FULL_BUILD, new NullProgressMonitor());
			
			// create a list of output IPaths from project classpath
			Stream<IPath> streamPath = Stream.of(javaProject.readRawClasspath())
					.map(entry -> entry.getOutputLocation())
					.filter(path -> path != null);
			
			// add the first output path and make all IPaths absolute
			streamPath = Stream.concat(streamPath, Stream.of(output))
					.map(path -> location.removeLastSegments(1).append(path));
	
			// filter all output paths containing Test.class files
			final List<IPath> paths = streamPath.filter(path -> {
				try (Stream<Path> files = Files.walk(Paths.get(path.toOSString()))) {
					List<String> filenames = files.map(String::valueOf)
							.filter(name -> name.endsWith("Test.class"))
							.collect(Collectors.toList());

					return !filenames.isEmpty();
				} catch (IOException e) {
					System.out.println("[ERROR] File not found: " + path.toOSString());
					return false;
				}

			}).collect(Collectors.toList());
			
			paths.forEach(path -> System.out.println("[DEBUG] Outuput Paths: " + path.toOSString()));	
			
			System.out.println("[DEBUG] src: " + src.toOSString());
			System.out.println("[DEBUG] dest: " + dest.toOSString());

			// ba-dua instrumenting
			Instrument.main(new String[] {"-src", src.toString(), "-dest", dest.toString()});
			
			// start testing
			// URLs for new Class Loader
			URL[] urls = paths.stream()
					.map(rethrowFunction(path -> path.toFile().toURI().toURL()))
					.toArray(URL[]::new);

			URLClassLoader classLoader = new URLClassLoader(urls);
			List<String> testClasses = paths.stream()
					.flatMap(ipath -> {
						final Path absolute = Paths.get(ipath.toOSString());
						
						try (Stream<Path> files = Files.walk(absolute)) {
							Stream<String> filenames = files
									.map(path -> absolute.relativize(path))
									.map(String::valueOf)
									.filter(file -> file.endsWith("Test.class"))
									// transforming dir notation to package notation and removing .class
									.map(file -> file.replace(File.separator, ".").substring(0, file.length() - 6));
							
							return filenames;
						} catch (IOException e) {
							return null;
						}
					})
					.collect(Collectors.toList());
//					.map(rethrowFunction(file -> {
//						System.out.println("[DEBUG] File name: " + file);
//						return Class.forName(file);
//					}))
//					.toArray(Class<?>[]::new);
			
//			for (Class<?> clazz : testClasses) {
//				System.out.println("[DEBUG] Class name: " + clazz.getName());
//			}
			
			testClasses.forEach(System.out::println);
			
		} catch (CoreException e) {
			e.printStackTrace();
		}
					
		try (Stream<Path> paths = Files.walk(Paths.get(location.toOSString()))) {
			URL[] testsURL = paths
					.map(String::valueOf)
					.filter(path -> path.endsWith("Test.class"))
					.map(path -> new File(path))
					.map(file -> {
						URL url = null;
						
						try {
							url = file.toURI().toURL();
						} catch (MalformedURLException e) {
							System.out.println("[ERROR] Malformed URL: " + e.getMessage() + " from path: " + file);
						}
						
						return url;
					})
					.toArray(URL[]::new);
			
			File dir = new File("/home/mario/develop/eclipse/ach2026/target/test-classes/");
			
//			URL url = new URL("file://home/mario/develop/eclipse/ach2026/target/test-classes/br/usp/each/ach2026/");
			URL url = dir.toURI().toURL();
			
//			URL[] urls = {dir.toURI().toURL()};
//			System.out.println("[INFO] Tests URL: " + urls[0]);
			
//			for (URL url : testsURL) 
//				System.out.println("[DEBUG] Test URL: " + url);
			
			URLClassLoader loader = URLClassLoader.newInstance(new URL[] {url});
			
			for (URL u : loader.getURLs())
				System.out.println("[DEBUG] Test URL: " + u);
			
			Class<?> testClazz = loader.loadClass("br.usp.each.ach2026.PropertiesManagerTest");
//			loader.close();
			
			/*
			Class<?>[] tests = paths
					.map(String::valueOf)
					.filter(path -> path.endsWith("Test.class"))
					.map(path -> {
						Class<?> clazz = null;
						
						try {
							clazz = Class.forName(path);
						} catch (ClassNotFoundException e) {
							System.out.println("[ERROR] Class not found: " + path);
						}
						
						return clazz;
					})
					.toArray(Class[]::new);
					*/
			
//			for (Class clazz : tests) 
//				System.out.println(clazz);
			
			JUnitCore.runClasses(testClazz);
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		/*
		// replace classes on .baduino folder to original classes folder
		try {
			Files.move(Paths.get(dest.toString()), and
					Paths.get(src.toString()), 
					StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
		*/
		
		/*
		// run junit
		JUnitLaunchShortcut junit = new JUnitLaunchShortcut();
		junit.launch(new StructuredSelection(javaProject),"run");
		*/
		
		return null;
	}

	@Override
	public boolean isEnabled() {
		final IProject project = ProjectUtils.getCurrentSelectedProject();
		return (project != null);
	}
}
