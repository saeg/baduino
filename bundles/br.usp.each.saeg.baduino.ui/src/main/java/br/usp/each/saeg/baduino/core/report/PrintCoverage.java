package br.usp.each.saeg.baduino.core.report;

import java.util.Collection;

import org.apache.log4j.Logger;

import br.usp.each.saeg.badua.core.analysis.ClassCoverage;
import br.usp.each.saeg.badua.core.analysis.ICoverageVisitor;
import br.usp.each.saeg.badua.core.analysis.MethodCoverage;

public class PrintCoverage implements ICoverageVisitor {
	
	private static final Logger logger = Logger.getLogger(PrintCoverage.class);

    private final boolean showClasses;
    private final boolean showMethods;

    public PrintCoverage(final boolean showClasses, final boolean showMethods) {
        this.showClasses = showClasses;
        this.showMethods = showMethods;
    }

    @Override
    public void visitCoverage(final ClassCoverage coverage) {
        if (showMethods) {
        	final Collection<MethodCoverage> methods = coverage.getMethods();
            for (final MethodCoverage methodCoverage : methods) {
                print(coverage.getName(), methodCoverage);
            }
        }
        
        if (showClasses) {
            print(coverage);
        }
    }

    private void print(final String className, final MethodCoverage coverage) {
    	logger.trace(String.format("%s.%s%s\t(%d/%d)", 
    			className,
                coverage.getName(), coverage.getDesc(),
                coverage.getDUCounter().getCoveredCount(),
                coverage.getDUCounter().getTotalCount()));
    }

    private void print(final ClassCoverage coverage) {
    	logger.trace(String.format("%s\t(%d/%d)", 
    			coverage.getName(),
                coverage.getDUCounter().getCoveredCount(),
                coverage.getDUCounter().getTotalCount()));
    }

}
