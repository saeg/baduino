package br.usp.each.saeg.baduino.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Mario Concilio
 *
 */
public class TreeClass extends TreeElement {
	
	private String name;
	private List<TreeMethod> methods = new ArrayList<>();
	
	public TreeClass() {
		
	}
	
	public TreeClass(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getSimpleName() {
		final int index = name.lastIndexOf(".");
		final String simpleName = name.substring(index + 1);
		
		return simpleName;
	}
	
	public List<TreeMethod> getMethods() {
		return methods;
	}
	
	public void setMethods(List<TreeMethod> methods) {
		this.methods = methods;
	}
	
	@Override
    public String toString() {
    	return name;
    }

}
