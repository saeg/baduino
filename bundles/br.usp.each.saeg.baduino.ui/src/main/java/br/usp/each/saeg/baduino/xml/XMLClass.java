package br.usp.each.saeg.baduino.xml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 * 
 * @author Mario Concilio
 *
 */
public class XMLClass {
	
    private String name;
    private List<XMLMethod> methods = new ArrayList<>();
    private List<XMLCounter> counters = new ArrayList<>();
	
	@XmlElement(name="counter")
    public List<XMLCounter> getCounters() {
		return counters;
	}

	public void setCounters(List<XMLCounter> counters) {
		this.counters = counters;
	}

	@XmlAttribute(name="name")
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name.replaceAll("/", ".");
    }

    @XmlElement(name="method")
    public List<XMLMethod> getMethods() {
        return methods;
    }
    
    public void setMethods(List<XMLMethod> methods) {
        this.methods = methods;
    }
    
    public void addMethod(XMLMethod method) {
        if (method != null) {
            methods.add(method);
        }
    }
    
    @Override
    public String toString() {
    	return name;
    }

}
