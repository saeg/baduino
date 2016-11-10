package br.usp.each.saeg.baduino.xml;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * @author Mario Concilio
 *
 */
@XmlRootElement(name="class")
public class XMLPackage {

    private String name;
    private List<XMLClass> classes = new ArrayList<XMLClass>();

    @XmlAttribute(name="name")
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
    	final String[] str = name.split("/");
    	this.name = String.join(".", Arrays.copyOf(str, str.length-1));
    }

    @XmlElement(name="class")
    public List<XMLClass> getClasses() {
        return classes;
    }
    
    public void setClasses(List<XMLClass> classes) {
        this.classes = classes;
    }
    
    @Override
    public String toString() {
    	return name;
    }

}
