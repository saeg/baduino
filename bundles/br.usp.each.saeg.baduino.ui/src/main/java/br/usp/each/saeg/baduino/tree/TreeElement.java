package br.usp.each.saeg.baduino.tree;

import java.util.ArrayList;
import java.util.List;

import br.usp.each.saeg.baduino.xml.XMLCounter.XMLCounterType;

/**
 * 
 * @author Mario Concilio
 *
 */
public class TreeElement {
	
	protected List<TreeCounter> counters = new ArrayList<>();
	
	public String getCoverage() {
		final TreeCounter counter = this.counters
    			.stream()
    			.filter(c -> c.getType().equals(XMLCounterType.DU))
    			.findFirst()
    			.get();
		
		String coverage;
    	final int total = counter.getTotal();
    	if (total > 0) {
    		final int covered = counter.getCovered();
    		final double percentage = counter.getPercentage();
    		coverage = String.format("(%d/%d) (%.2f%%)", covered, total, percentage);
    	}
    	else {
    		coverage = "No Def-Use Associations";
    	}
    	
    	return coverage;
	}

	public List<TreeCounter> getCounters() {
		return counters;
	}

	public void setCounters(List<TreeCounter> counters) {
		this.counters = counters;
	}

}
