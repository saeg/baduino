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
public class XMLMethod {

    private String name;
    private List<XMLDua> duas = new ArrayList<>();
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
        this.name = name;
    }

    @XmlElement(name="du")
    public List<XMLDua> getDuas() {
        return duas;
    }
    
    public void setDuas(List<XMLDua> duas) {    	
        this.duas = duas;
    }
    
    @Override
    public String toString() {
    	return name;
    }
    
}
