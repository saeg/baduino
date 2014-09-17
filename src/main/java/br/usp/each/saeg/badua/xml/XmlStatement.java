package br.usp.each.saeg.badua.xml;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

/**
 * @author Danilo Mutti (dmutti@gmail.com)
 */
public class XmlStatement {

    private int loc;
    private BigDecimal score = new BigDecimal(XmlInput.nextScore());
    private int start;
    private int end;
    private String content;
    
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
    
    
//    @XmlAttribute(name="location")
//    public int getLoc() {
//        return loc;
//    }
//    public void setLoc(int loc) {
//        this.loc = loc;
//    }

//    @XmlAttribute(name="suspicious-value")
//    public BigDecimal getScore() {
//        return score;
//    }
//    public void setScore(BigDecimal score) {
//        if (null != score) {
//            this.score = score;
//        }
//    }

//    @XmlTransient
//    public int getStart() {
//        return start;
//    }
//    public void setStart(int start) {
//        this.start = start;
//        this.loc = start;
//    }

//    @XmlTransient
//    public int getEnd() {
//        return end;
//    }
//    public void setEnd(int end) {
//        this.end = end;
//    }
//
//    @XmlTransient
//    public String getContent() {
//        return content;
//    }
//    public void setContent(String content) {
//        this.content = content;
//    }
}
