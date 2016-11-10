package br.usp.each.saeg.baduino.tree;

import br.usp.each.saeg.baduino.xml.XMLCounter.XMLCounterType;

public class TreeCounter {
	
	private int covered;
	private int missed;
	private XMLCounterType type;
	
	public int getCovered() {
		return covered;
	}
	
	public void setCovered(int covered) {
		this.covered = covered;
	}
	
	public int getMissed() {
		return missed;
	}
	
	public void setMissed(int missed) {
		this.missed = missed;
	}
	
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
		return String.format("covered=%d, missed=%d, type=%s", covered, missed, type);
	}

}
