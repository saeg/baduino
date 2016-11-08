package br.usp.each.saeg.baduino.xml;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Danilo Mutti (dmutti@gmail.com)
 */
//@XmlRootElement(name="FaultClassification")
@XmlRootElement(name="report")
public class XmlInput {

    private static final Random random = new Random();
    
    private String name;
    private List<XmlClass> classes = new ArrayList<>();
    private List<XmlCounter> counters = new ArrayList<>();
    
    @XmlAttribute(name="name")
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    @XmlElement(name="class")
    public List<XmlClass> getClasses() {
        return classes;
    }
    
    public void setClasses(List<XmlClass> classes) {
        this.classes = classes;
    }
    
    @XmlElement(name="counter")
    public List<XmlCounter> getCounters() {
		return counters;
	}

	public void setCounters(List<XmlCounter> counters) {
		this.counters = counters;
	}
    
    /*
    private List<XmlPackage> packages = new ArrayList<XmlPackage>();
    private Map<String, XmlPackage> namePackage = new HashMap<String, XmlPackage>();

    @XmlElementWrapper(name="test-criteria") 
    @XmlElement(name="package")
    public List<XmlPackage> getPackages() {
        return packages;
    }
    
    public void setPackages(List<XmlPackage> packages) {
        this.packages = packages;
    }

    public XmlPackage byName(String name) {
        if (namePackage.isEmpty()) {
            for (XmlPackage pkg : packages) {
                namePackage.put(pkg.getName(), pkg);
            }
        }
        
        return namePackage.get(name);
    }
    */

    public static float nextScore() {
        return random.nextFloat();
    }
    
    @Override
    public String toString() {
    	final StringBuilder str = new StringBuilder("input counters -> ");
    	final List<XmlCounter> counters = getCounters();
    	counters.forEach(counter -> {
    		str.append(counter)
    		.append(" | ");
    	});
    	
    	return str.toString();
    }
  
}
