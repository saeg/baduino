package br.usp.each.saeg.baduino.core.report;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.apache.log4j.Logger;

import br.usp.each.saeg.badua.cli.XMLCoverageWriter;
import br.usp.each.saeg.badua.core.analysis.Analyzer;
import br.usp.each.saeg.badua.core.data.ExecutionDataReader;
import br.usp.each.saeg.badua.core.data.ExecutionDataStore;
import br.usp.each.saeg.baduino.core.launching.VMListener;
import br.usp.each.saeg.baduino.core.model.ProjectModel;
import br.usp.each.saeg.baduino.core.runnable.WatchFolder;

/**
 * 
 * @author Mario Concilio
 *
 */
public class BaduinoReport {
	
	private static final Logger logger = Logger.getLogger(BaduinoReport.class);
	
	private final CoverageVisitor visitor;
	private final Analyzer analyzer;
	private final File classes;
	private final File xml;
	private final ProjectModel model;
	private final List<VMListener> listeners;
	
	public BaduinoReport(final ProjectModel model) throws IOException {
		this.classes = new File(model.getClassesPath());
		this.xml = new File(model.getCoverageXmlPath());
		this.model = model;
		this.listeners = new ArrayList<>();
		
		final File coverage = new File(model.getCoverageBinPath());
		final PrintCoverage printer = new PrintCoverage(false, true);
		visitor = new CoverageVisitor(printer);
		analyzer = new Analyzer(readExecutionData(coverage), visitor);
	}
	
	public void reportAll() throws IOException {
		logger.debug("Reporting");
		
		try (final Stream<Path> paths = Files.walk(Paths.get(classes.toURI()))) {
			paths.filter(path -> Files.isRegularFile(path))
			.map(path -> path.toFile())
			.forEach(file -> analyze(file));
		}
	}
	
	public void writeXML() throws IOException {
		logger.debug("Writing XML");
		
		final File file = new File(model.getCoverageXmlPath());
		new WatchFolder(file, listeners).start();
		
		try (final FileOutputStream output = new FileOutputStream(xml)) {
			XMLCoverageWriter.write(visitor.getClasses(), output);
		}
	}
	
	public void addListener(VMListener listener) {
		this.listeners.add(listener);
	}
	
	private void analyze(File file) {
		try (final InputStream input = new FileInputStream(file)) {
			analyzer.analyzeAll(input, file.getPath());
		}
		catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	private static ExecutionDataStore readExecutionData(final File inputFile) throws IOException {
        final ExecutionDataStore store = new ExecutionDataStore();
        final FileInputStream input = new FileInputStream(inputFile);        
        
        try {
            final ExecutionDataReader reader = new ExecutionDataReader(input);
            reader.setExecutionDataVisitor(store);
            reader.read();
        } 
        finally {
            input.close();
        }
        
        return store;
    }

}
