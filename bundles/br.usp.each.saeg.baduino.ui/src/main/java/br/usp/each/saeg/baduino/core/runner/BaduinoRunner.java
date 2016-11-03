package br.usp.each.saeg.baduino.core.runner;

import java.io.File;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;

import br.usp.each.saeg.baduino.core.model.ProjectModel;
import br.usp.each.saeg.baduino.core.model.ProjectModelManager;

/**
 * Main class to be run on the new VM. It will setup the Logger, run the tests using JUnit and create the coverage files.
 * @author Mario Concilio
 *
 */
public class BaduinoRunner {
	
	private final ProjectModel model;
	
	public BaduinoRunner(final ProjectModel model) {
		this.model = model;
	}
	
	public void setupLogger() {
		final ConsoleAppender console = new ConsoleAppender();
		final String PATTERN = "[%-5p] %d{dd-MM-yyyy HH:mm:ss} %c{1}:%L - %m%n";
		console.setLayout(new PatternLayout(PATTERN)); 
		console.setThreshold(Level.DEBUG);
		console.activateOptions();
		
		final FileAppender fileAppender = new RollingFileAppender();
		fileAppender.setLayout(new PatternLayout(PATTERN));
		fileAppender.setThreshold(Level.DEBUG);
		fileAppender.setFile(model.getBaduinoPath() + File.separator + "baduino.log");
		fileAppender.activateOptions();
		
		final Logger logger = Logger.getRootLogger();
		logger.getLoggerRepository().resetConfiguration();
		logger.addAppender(console);
		logger.addAppender(fileAppender);
	}
	
	public void run() throws Exception {
        final TestsRunner tests = new TestsRunner(model);
        tests.run();
    }
	
	public static void main(String[] args) throws Exception {
		final String path = args[0];
		final ProjectModel model = ProjectModelManager.readFromDisk(path);
		
		// setting up coverage.ser location
		System.setProperty("output.file", model.getCoverageBinPath());
		
		final BaduinoRunner runner = new BaduinoRunner(model);
		runner.setupLogger();
		runner.run();
		
		System.exit(0);
	}

}
