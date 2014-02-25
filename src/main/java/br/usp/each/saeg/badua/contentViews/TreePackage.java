package br.usp.each.saeg.badua.contentViews;

import java.util.ArrayList;
import java.util.List;

public class TreePackage {
	private String name;
	private List<TreeClass> Classes = new ArrayList<TreeClass>();
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public List<TreeClass> getClasses() {
		return Classes;
	}
	public String getCoverage(){
		if(Classes.size() != 0){
			int covered = 0;
			int total = 0;
			for (TreeClass method : Classes) {
				method.getCoverage();//nao esta muito eficiente pois esta tendo q calcular a cobertura 2x
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

}
