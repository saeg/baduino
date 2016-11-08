package br.usp.each.saeg.baduino.xml;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * 
 * @author Mario Concilio
 *
 */
public class XmlCounter {
	
	private int covered;
	private int missed;
	private String type;
	
	@XmlAttribute(name="covered")
	public int getCovered() {
		return covered;
	}
	
	public void setCovered(int covered) {
		this.covered = covered;
	}
	
	@XmlAttribute(name="missed")
	public int getMissed() {
		return missed;
	}
	
	public void setMissed(int missed) {
		this.missed = missed;
	}
	
	@XmlAttribute(name="type")
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	@Override
	public String toString() {
		return String.format("%s covered=%d, missed=%d", 
				getType(),
				getCovered(), 
				getMissed());
	}

}
