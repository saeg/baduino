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
			if(Methods.size() != 0){
				for(TreeMethod methods: Methods){
					int[] cover = methods.getCoverageRecursive();
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
