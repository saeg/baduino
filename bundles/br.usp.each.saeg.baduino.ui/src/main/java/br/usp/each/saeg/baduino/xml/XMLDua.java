package br.usp.each.saeg.baduino.xml;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * 
 * @author Mario Concilio
 *
 */
public class XMLDua {
    
    private boolean covered;
    private int def;
	private int use;
	private int target;
	private String var;
    
	@XmlAttribute(name="covered")
    public boolean isCovered(){
    	return covered;
    }
    
    public void setCovered(boolean covered){
    	this.covered = covered;
    }
    
    @XmlAttribute(name="def")
    public int getDef(){
    	return def;
    }
    
    public void setDef(int def){
    	this.def = def;
    }
    
    @XmlAttribute(name="use")
    public int getUse(){
    	return use;
    }
    
    public void setUse(int use){
    	this.use = use;
    }
    
    @XmlAttribute(name="target")
    public int getTarget(){
    	return target;
    }
    
    public void setTarget(int target){
    	this.target = target;
    }
    
    @XmlAttribute(name="var")
    public String getVar(){
    	return var;
    }
    
    public void setVar(String var){
    	this.var = var;
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
