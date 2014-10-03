package br.usp.each.saeg.baduino.xml;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author Danilo Mutti (dmutti@gmail.com)
 */
public class XmlMethod {

    private String name;
    private ArrayList<XmlStatement> statements = new ArrayList<XmlStatement>();


    @XmlElement(name="requirement")
    public ArrayList<XmlStatement> getStatements() {
        return statements;
    }
    
    public void setStatements(ArrayList<XmlStatement> statements) {
//    	Collections.sort(statements, new Comparator<XmlStatement>() {
//            @Override
//            public int compare(XmlStatement o1, XmlStatement o2) {
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
        this.statements = statements;
    }
    public void addStatements(XmlStatement statement) {
        if (statement != null) {
            statements.add(statement);
        }
    }

    @XmlAttribute(name="name")
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
