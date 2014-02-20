package br.usp.each.saeg.badua.contentViews;
public class DUA {

	private final int def;
	private final int use;
	private final int target;
	private final String var;
	private boolean covered = false; 


	public DUA(final int def, final int use, final int target, final String var) {
		this.def = def;
		this.use = use;
		this.target = target;
		this.var = var;
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
	
	public String getCovered(){
		return String.valueOf(covered);
	}
	
	public void setCovered(boolean covered){
		this.covered = covered;
	}

	@Override
	public String toString() {
		if(target == -1){
			return String.format("(%d , %d, %s)", def, use, var);
		}else{
			return String.format("(%d , (%d , %d) , %s)", def, use, target, var);
		}
		
	}

}