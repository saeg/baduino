package br.usp.each.saeg.baduino.xml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Danilo Mutti (dmutti@gmail.com)
 */
public class XmlClass {

    private String name;
    private List<XmlMethod> methods = new ArrayList<XmlMethod>();


    @XmlAttribute(name="name")
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }


    @XmlElement(name="method")
    public List<XmlMethod> getMethods() {
        return methods;
    }
    public void setMethods(List<XmlMethod> methods) {
        this.methods = methods;
    }
    public void addMethod(XmlMethod method) {
        if (method != null) {
            methods.add(method);
        }
    }

    public XmlMethod byName(String name) {
        for (int i = 0, j = methods.size(); i < j; i++) {
            XmlMethod method = methods.get(i);
            if (StringUtils.equals(name, method.getName())) {
                return method;
            }
        }
        return null;
    }
}
