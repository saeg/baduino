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
		
		final Class<?>[] testClasses = model.getClasses()
				.stream()
				.filter(clazz -> clazz.endsWith("Test"))
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

        // Run Tests
        JUnitCore junit = new JUnitCore();
        Result result = junit.run(testClasses);
        
        String str = String.format("Executed %d tests in %d miliseconds. %d tests failed.", result.getRunCount(), result.getRunTime(), result.getFailureCount());
        logger.info(str);
        
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
