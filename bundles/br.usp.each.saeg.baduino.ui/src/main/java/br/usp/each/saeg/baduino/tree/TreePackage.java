package br.usp.each.saeg.baduino.tree;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import br.usp.each.saeg.baduino.xml.XMLCounter.XMLCounterType;

public class TreePackage {
	
	private String name;
	private List<TreeClass> classes = new ArrayList<>();
	
	public TreePackage() {
		
	}
	
	public TreePackage(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<TreeClass> getClasses() {
		return classes;
	}
	
	public void addClass(TreeClass clazz) {
		final int index = clazz.getName().lastIndexOf(".");
		final String str = clazz.getName().substring(0, index);
		
		// verify if class belongs to this package
		if (str.equals(name)) {
			classes.add(clazz);
		}
	}

	public void setClasses(List<TreeClass> classes) {
		this.classes = classes
        		.stream()
        		.filter(clazz -> {
        			final int index = clazz.getName().lastIndexOf(".");
        			final String str = clazz.getName().substring(0, index);
        			
        			return str.equals(name);
        		})
        		.collect(Collectors.toList());
	}
	
	public int getTotal() {
		final int total = classes
				.stream()
				.map(clazz -> clazz.getCounters())
				.flatMap(x -> x.stream())
				.filter(counter -> counter.getType().equals(XMLCounterType.DU))
				.mapToInt(counter -> counter.getTotal())
				.sum();
		
		return total;
	}
	
	public int getCovered() {
		final int covered = classes
				.stream()
				.map(clazz -> clazz.getCounters())
				.flatMap(x -> x.stream())
				.filter(counter -> counter.getType().equals(XMLCounterType.DU))
				.mapToInt(counter -> counter.getCovered())
				.sum();
		
		return covered;
	}
	
	public double getPercentage() {
		return ((double) getCovered() / (double) getTotal()) * 100.0;
	}
	
	public String getCoverage() {
		String coverage;
    	final int total = getTotal();
    	if (total > 0) {
    		final int covered = getCovered();
    		final double percentage = getPercentage();
    		coverage = String.format("(%d/%d) (%.2f%%)", covered, total, percentage);
    	}
    	else {
    		coverage = "No Def-Use Associations";
    	}
    	
    	return coverage;
	}
	
	@Override
    public boolean equals(Object obj) {
    	if (!(obj instanceof TreePackage))
    		return false;
    	
    	if (obj == this)
    		return true;
    	
    	final TreePackage pkg = (TreePackage) obj;
    	return name.equals(pkg.getName());
    }
    
    @Override
    public int hashCode() {
    	return name.hashCode();
    }
    
    @Override
    public String toString() {
    	return name;
    }

}
