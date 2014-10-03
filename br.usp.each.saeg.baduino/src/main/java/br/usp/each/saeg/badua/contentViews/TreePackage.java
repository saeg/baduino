package br.usp.each.saeg.badua.contentViews;

import java.util.ArrayList;
import java.util.List;

public class TreePackage {
	private String name;
	private List<TreeClass> Classes = new ArrayList<TreeClass>();
	private int covered = 0;
	private int total = 0;

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
		if(covered == 0 && total == 0){
			getCoverageRecursive();
		}

		if(total != 0){
			return "("+covered+"/"+total+") "+String.format("%.2f", (double)covered/(double)total*100)+"%";
		}else{
			return "No Def-Use Associations";
		}
	}

	public int[] getCoverageRecursive() {
		if(covered == 0 && total == 0){
			if(Classes.size() != 0){
				for(TreeClass classes: Classes){
					int[] cover = classes.getCoverageRecursive();
					if((cover[0] != -1) && (cover[1] != -1)){
						covered += cover[0];
						total += cover[1];
					}
				}
			}
		}
		return new int[]{covered,total};
	}

}
