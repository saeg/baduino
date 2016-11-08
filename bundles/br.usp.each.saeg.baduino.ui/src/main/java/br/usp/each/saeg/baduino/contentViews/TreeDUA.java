package br.usp.each.saeg.baduino.contentViews;

import org.eclipse.jdt.core.ICompilationUnit;

public class TreeDUA {

	private final int def;
	private final int use;
	private final int target;
	private final String var;
	private boolean covered = false;
	private final ICompilationUnit cu;

	public TreeDUA(final int def, final int use, final int target, final String var, ICompilationUnit cu) {
		this.def = def;
		this.use = use;
		this.target = target;
		this.var = var;
		this.cu = cu;
	}
	
	public int getDef() {
		return def;
	}

	public int getUse() {
		return use;
	}

	public int getTarget() {
		return target;
	}

	public String getVar() {
		return var;
	}
	
//	public String getCovered() {
//		return String.valueOf(covered);
//	}
	
	public boolean isCovered() {
		return covered;
	}
	
	public void setCovered(boolean covered) {
		this.covered = covered;
	}

	@Override
	public String toString() {
		if(target == -1) {
			return String.format("(%d , %d, %s)", def, use, var);
		}
		else {
			return String.format("(%d , (%d , %d) , %s)", def, use, target, var);
		}
	}

	public ICompilationUnit getCu() {
		return cu;
	}

}