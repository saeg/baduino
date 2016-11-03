package br.usp.each.saeg.baduino.core.report;

import java.util.ArrayList;
import java.util.List;

import br.usp.each.saeg.badua.core.analysis.ClassCoverage;
import br.usp.each.saeg.badua.core.analysis.ICoverageVisitor;

public class CoverageVisitor implements ICoverageVisitor {
	
	private final List<ClassCoverage> classes = new ArrayList<ClassCoverage>();
    private final ICoverageVisitor next;

	public CoverageVisitor(final ICoverageVisitor next) {
        this.next = next;
    }

    @Override
    public void visitCoverage(final ClassCoverage coverage) {
        classes.add(coverage);
        if (next != null) {
            next.visitCoverage(coverage);
        }
    }
    
    public final List<ClassCoverage> getClasses() {
    	return classes;
    }

}
