package br.usp.each.saeg.badua.contentViews;

import java.util.ArrayList;
import java.util.List;

public class TreeClass {
	private String name;
	private List<TreeMethod> Methods = new ArrayList<TreeMethod>();
	private int covered = 0;
	private int total = 0;

	public List<TreeMethod> getMethods() {
		return Methods;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCoverage(){
		covered = 0;
		total = 0;
		if(Methods.size() != 0){
			for (TreeMethod method : Methods) {
				covered += method.getCoveredDuasCounter();
				total += method.getTotalDuas();
			}
			if(total != 0){
				return "("+covered+"/"+total+") "+String.format("%.2f", (double)covered/(double)total*100)+"%";
			}else{
				return "No Def-Use Associations";
			}

		}else return "No Def-Use Associations";

	}

	public int getCoveredDuasCounter() {
		
		return covered;
	}

	public int getTotalDuas() {
		return total;
	}

}
