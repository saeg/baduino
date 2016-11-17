package br.usp.each.saeg.baduino.core.runner;

import java.io.File;

import br.usp.each.saeg.baduino.core.logger.LoggerManager;
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
	
	public void run() throws Exception {
        final TestsRunner tests = new TestsRunner(model);
        tests.run();
    }
	
	public static void main(String[] args) throws Exception {
		final String path = args[0];
		final ProjectModel model = ProjectModelManager.readFromDisk(path);
		
		// setting up coverage.ser location
		System.setProperty("output.file", model.getCoverageBinPath());
		
		// setting up log4j
		LoggerManager.setupLogger(model.getBaduinoPath() + File.separator + "baduino.log");
		
		final BaduinoRunner runner = new BaduinoRunner(model);
		runner.run();
		
		System.exit(0);
	}

}
