package br.usp.each.saeg.baduino.core.logger;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;
import org.eclipse.core.resources.ResourcesPlugin;

public class LoggerManager {
	
	private static final String PATTERN = "[%-5p] %d{dd-MM-yyyy HH:mm:ss} %c{1}:%L - %m%n";
	private static boolean isDefined = false;
	
	public static void setupLogger() {
//		final String path = ResourcesPlugin.getWorkspace().getRoot().getLocation().toOSString() + "/.metadata/";
		final String path = ResourcesPlugin.getWorkspace().getRoot().getLocation().append(".metadata").append("baduino.log").toOSString();
		setupLogger(path);
	}
	
	public static void setupLogger(String path) {
		// check if the logger is already defined
		if (isDefined)
			return;
		
		final Logger logger = Logger.getRootLogger();
		logger.getLoggerRepository().resetConfiguration();
		
		final ConsoleAppender console = new ConsoleAppender();
		console.setLayout(new PatternLayout(PATTERN)); 
		console.setThreshold(Level.DEBUG);
		console.activateOptions();
		logger.addAppender(console);
		
		if (path != null) {
			final FileAppender fileAppender = new RollingFileAppender();
			fileAppender.setLayout(new PatternLayout(PATTERN));
			fileAppender.setThreshold(Level.DEBUG);
			fileAppender.setFile(path);
			fileAppender.activateOptions();
			logger.addAppender(fileAppender);
		}
	
		isDefined = true;
	}

}
