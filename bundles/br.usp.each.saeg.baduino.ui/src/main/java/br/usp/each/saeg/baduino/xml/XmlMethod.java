package br.usp.each.saeg.baduino.xml;

import java.util.ArrayList;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author Danilo Mutti (dmutti@gmail.com)
 */
public class XmlMethod {

    private String name;
    private ArrayList<XmlDua> duas = new ArrayList<>();

//    @XmlElement(name="requirement")
    @XmlElement(name="du")
    public ArrayList<XmlDua> getDuas() {
        return duas;
    }
    
    public void setDuas(ArrayList<XmlDua> duas) {
//    	Collections.sort(duas, new Comparator<XmlDua>() {
//            @Override
//            public int compare(XmlDua o1, XmlDua o2) {
//          	  int resp = Integer.compare(o1.getDef(), o2.getDef());
//          	  if(resp != 0) return resp;
//          	  resp = Integer.compare(o1.getUse(), o2.getUse());
//          	  if(resp != 0) return resp;
//          	  resp = Integer.compare(o1.getTarget(), o2.getTarget());
//          	  if(resp != 0) return resp;
//          	  return o1.getVar().compareTo(o2.getVar());
//
//            }
//        });
    	
        this.duas = duas;
    }
    public void addStatements(XmlDua statement) {
        if (statement != null) {
            duas.add(statement);
        }
    }

    @XmlAttribute(name="name")
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    @Override
    public String toString() {
    	return getName();
    }
    
}
