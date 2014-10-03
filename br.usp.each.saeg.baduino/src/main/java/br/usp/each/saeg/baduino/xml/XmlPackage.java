package br.usp.each.saeg.baduino.xml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Danilo Mutti (dmutti@gmail.com)
 */
public class XmlPackage {

    private String name;
    private List<XmlClass> classes = new ArrayList<XmlClass>();

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
    public void addClass(XmlClass arg) {
        if (arg != null) {
            classes.add(arg);
        }
    }

    public XmlClass byName(String name) {
        for (XmlClass clz : classes) {
            if (StringUtils.equals(name, clz.getName())) {
                return clz;
            }
        }
        return null;
    }
}
