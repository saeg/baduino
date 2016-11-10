package br.usp.each.saeg.baduino.xml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * @author Mario Concilio
 *
 */
@XmlRootElement(name="report")
public class XMLProject {
    	
    private List<XMLClass> classes = new ArrayList<>();
    private List<XMLCounter> counters = new ArrayList<>();
	
	@XmlElement(name="counter")
    public List<XMLCounter> getCounters() {
		return counters;
	}

	public void setCounters(List<XMLCounter> counters) {
		this.counters = counters;
	}

    @XmlElement(name="class")
    public List<XMLClass> getClasses() {
        return this.classes;
    }
    
    public void setClasses(List<XMLClass> classes) {
        this.classes = classes;
    }
    
    /*
    private List<XMLPackage> packages = new ArrayList<XMLPackage>();
    private Map<String, XMLPackage> namePackage = new HashMap<String, XMLPackage>();

    @XmlElementWrapper(name="test-criteria") 
    @XmlElement(name="package")
    public List<XMLPackage> getPackages() {
        return packages;
    }
    
    public void setPackages(List<XMLPackage> packages) {
        this.packages = packages;
    }

    public XMLPackage byName(String name) {
        if (namePackage.isEmpty()) {
            for (XMLPackage pkg : packages) {
                namePackage.put(pkg.getName(), pkg);
            }
        }
        
        return namePackage.get(name);
    }
    */
    
    @Override
    public String toString() {
    	final StringBuilder str = new StringBuilder("project counters -> ");
    	final List<XMLCounter> counters = getCounters();
    	counters.forEach(counter -> {
    		str.append(counter)
    		.append(" | ");
    	});
    	
    	return str.toString();
    }
  
}
