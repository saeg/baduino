package br.usp.each.saeg.baduino.xml;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * 
 * @author Mario Concilio
 *
 */
public class XMLCounter {
	
	public enum XMLCounterType {
		DU("DU"),
		METHOD("METHOD"),
		CLASS("CLASS");
		
		private String type;
		
		XMLCounterType(String type) {
			this.type = type;
		}
		
		@Override
		public String toString() {
			return this.type;
		}
	}
	
	private int covered;
	private int missed;
	private XMLCounterType type;
	
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
	public XMLCounterType getType() {
		return type;
	}
	
	public void setType(XMLCounterType type) {
		this.type = type;
	}
	
	public int getTotal() {
		return covered + missed;
	}
	
	public double getPercentage() {
		return ((double) covered / (double) getTotal()) * 100.0; 
	}
	
	@Override
	public String toString() {
		return String.format("%s covered=%d, missed=%d", 
				getType(),
				getCovered(), 
				getMissed());
	}

}
