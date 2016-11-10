package br.usp.each.saeg.baduino.tree;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 
 * @author Mario Concilio
 *
 */
public class TreeProject extends TreeElement {
	
	private String name;
	private List<TreePackage> packages = new ArrayList<>();
	
	public String getName() {
		return "Project";
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public List<TreePackage> getPackages() {
		return packages;
	}
	
	public void addPackage(TreePackage pkg) {
		// verify if this project already contains package pkg
		if (!packages.contains(pkg)) {
			packages.add(pkg);
		}
	}

	public void setPackages(List<TreePackage> packages) {
		this.packages = packages
        		.stream()
        		.distinct()
        		.collect(Collectors.toList());
	}
	
	@Override
    public String toString() {
    	final StringBuilder str = new StringBuilder("project counters -> ");
    	final List<TreeCounter> counters = getCounters();
    	counters.forEach(counter -> {
    		str.append(counter)
    		.append(" | ");
    	});
    	
    	return str.toString();
    }

}
