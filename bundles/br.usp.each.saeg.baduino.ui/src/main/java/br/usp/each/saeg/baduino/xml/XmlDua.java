package br.usp.each.saeg.baduino.xml;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * @author Danilo Mutti (dmutti@gmail.com)
 */
public class XmlDua implements Cloneable {
    
    private boolean covered;
    private int def;
	private int use;
	private int target;
	private String var;
    
    @XmlAttribute(name="covered")
    public boolean getCovered(){
    	return covered;
    }
    
    public void setCovered(boolean covered){
    	this.covered=covered;
    }
    
    @XmlAttribute(name="def")
    public int getDef(){
    	return def;
    }
    
    public void setDef(int def){
    	this.def=def;
    }
    
    @XmlAttribute(name="use")
    public int getUse(){
    	return use;
    }
    
    public void setUse(int use){
    	this.use=use;
    }
    
    @XmlAttribute(name="target")
    public int getTarget(){
    	return target;
    }
    
    public void setTarget(int target){
    	this.target=target;
    }
    
    @XmlAttribute(name="var")
    public String getVar(){
    	return var;
    }
    
    public void setVar(String var){
    	this.var=var;
    }
    
    @Override
    public String toString() {
    	return String.format("covered=%s, var=%s, def=%d, use=%d, target=%s",
    			getCovered(),
    			getVar(),
    			getDef(),
    			getUse(),
    			getTarget());
    }
    
}
