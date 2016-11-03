package br.usp.each.saeg.baduino.listener;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.junit.runner.*;
import org.junit.runner.notification.*;

public class TestRunnerListener extends RunListener {
	
	private static final Logger logger = Logger.getLogger(TestRunnerListener.class);
	
	public TestRunnerListener() {
		final String pattern = "[%-5p] %d{dd-MM-yyyy HH:mm:ss} %c{1}:%L - %m%n";
		final ConsoleAppender console = new ConsoleAppender();
		console.setLayout(new PatternLayout(pattern));
		console.setThreshold(Level.DEBUG);
		console.activateOptions();
		
		Logger.getRootLogger().getLoggerRepository().resetConfiguration();
		Logger.getRootLogger().addAppender(console);
	}
	
	@Override
	public void testRunStarted(Description description)	throws Exception {
		logger.info("Number of test cases to execute: " + description.testCount());
	}
	
	@Override
	public void testRunFinished(Result result) throws Exception {
		logger.info("Number of test cases executed: " + result.getRunCount());
	}

	@Override
	public void testStarted(Description description) throws Exception {
		logger.info("Starting execution of test case: " + description.getDisplayName());
	}

	@Override
	public void testFinished(Description description) throws Exception {
		logger.info("Finished execution of test case: " + description.getDisplayName());
	}

	@Override
	public void testFailure(Failure failure) throws Exception {
		logger.error("Execution of test case: [" + failure.getTestHeader() + "] failed.", failure.getException());
	}

	@Override
	public void testIgnored(Description description) throws Exception {
		logger.info("Execution of test case ignored: " + description.getDisplayName());
	}

}
