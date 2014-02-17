package br.usp.each.saeg.badua.contentViews;
public class DUA {

	public final int def;
	public final int use;
	public final String var;

	public DUA(final int def, final int use, final String var) {
		this.def = def;
		this.use = use;
		this.var = var;
	}
	
	public int getDef() {
		return def;
	}

	public int getUse() {
		return use;
	}

	public String getVar() {
		return var;
	}

	@Override
	public String toString() {
		return String.format("(%d , %d, %s)", def, use, var);
	}

}