package br.usp.each.saeg.baduino.core.runner;

import java.util.List;

import org.apache.log4j.Logger;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import br.usp.each.saeg.baduino.core.model.ProjectModel;

/**
 * 
 * @author Mario Concilio
 *
 */
public class TestsRunner {
	
	private static final Logger logger = Logger.getLogger(TestsRunner.class);
	
	private final ProjectModel model;
	
	public TestsRunner(final ProjectModel model) {
		this.model = model;
	}
	
	public void run() {
		logger.debug("Loading classes");
		
		// load all classes
		model.getClasses()
		.stream()
		.filter(clazz -> !clazz.endsWith("Test"))
		.forEach(clazz -> {
			try {
				Class.forName(clazz);
			} 
			catch (ClassNotFoundException e) {
				logger.error("Unable to find class file for " + clazz);
			}
		});
		logger.debug("Finished loading classes");
		
		final Class<?>[] testClasses = model.getClasses()
				.stream()
				.filter(clazz -> clazz.endsWith("Test") || clazz.endsWith("Tests"))
				.map(name -> {
					Class<?> clazz = null;
					try {
						clazz = Class.forName(name);
					} 
					catch (ClassNotFoundException ex) {
						logger.error("Unable to find class file for test " + name);
					}
					
					return clazz;
				})
				.toArray(Class[]::new);
		logger.debug("Finished loading test classes");
		logger.debug("Testing with JUnit");
		
        // Run Tests
        Result result = JUnitCore.runClasses(testClasses);
        logger.info(String.format("Executed %d tests in %.3f seconds. %d tests failed.", result.getRunCount(), result.getRunTime()/1000.0, result.getFailureCount()));
        
        if (result.wasSuccessful()) {
        	logger.info("All tests passed.");
        }
        else {
        	final List<Failure> failures = result.getFailures();
        	failures.forEach(failure -> {
        		String s = String.format("%s: %s", failure.getTestHeader(), failure.getMessage());
        		logger.info(s);
        	});
        }
    }

}
