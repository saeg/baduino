package br.usp.each.saeg.baduino.tree;

import org.eclipse.jdt.core.ICompilationUnit;

/**
 * 
 * @author Mario Concilio
 *
 */
public class TreeDua {
	
	private boolean covered;
    private int def;
	private int use;
	private int target;
	private String var;
	private ICompilationUnit compilationUnit;
	
	public boolean isCovered() {
		return covered;
	}
	
	public void setCovered(boolean covered) {
		this.covered = covered;
	}
	
	public int getDef() {
		return def;
	}
	
	public void setDef(int def) {
		this.def = def;
	}
	
	public int getUse() {
		return use;
	}
	
	public void setUse(int use) {
		this.use = use;
	}
	
	public int getTarget() {
		return target;
	}
	
	public void setTarget(int target) {
		this.target = target;
	}
	
	public String getVar() {
		return var;
	}
	
	public void setVar(String var) {
		this.var = var;
	}
	
	public ICompilationUnit getCompilationUnit() {
		return compilationUnit;
	}

	public void setCompilationUnit(ICompilationUnit compilationUnit) {
		this.compilationUnit = compilationUnit;
	}
	
	@Override
    public String toString() {
    	String str;
    	
    	if (target == 0) {
    		str = String.format("(%d, %d, %s)", def, use, var);
    	}
    	else {
    		str = String.format("(%d, (%d, %d), %s)", def, use, target, var);
    	}
    	
    	return str;
    }

}
