package br.usp.each.saeg.baduino.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Mario Concilio
 *
 */
public class TreeMethod extends TreeElement {

	private String name;
    private int access;
    private List<TreeDua> duas = new ArrayList<>();
    
    public TreeMethod() {
    	
    }
    
    public TreeMethod(String name) {
    	this.name = name;
    }
    
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getAccess() {
		return access;
	}
	
	public void setAccess(int access) {
		this.access = access;
	}
	
	public List<TreeDua> getDuas() {
		return duas;
	}
	
	public void setDuas(List<TreeDua> duas) {
		this.duas = duas;
	}
	
	@Override
    public String toString() {
    	return name;
    }
	
}
