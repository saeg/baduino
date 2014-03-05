package br.usp.each.saeg.badua.contentViews;

import java.util.ArrayList;
import java.util.List;

public class TreeFolder {
	private String name;
	private List<TreePackage> Packages = new ArrayList<TreePackage>();
	private int covered = 0;
	private int total = 0;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public List<TreePackage> getPackages() {
		return Packages;
	}

	public String getCoverage() {
		if(covered == 0 && total == 0){
			getCoverageRecursive();
		}

		if(covered != 0 && total != 0){
			return "("+covered+"/"+total+") "+String.format("%.2f", (double)covered/(double)total*100)+"%";
		}else{
			return "No Def-Use Associations";
		}
	}

	public int[] getCoverageRecursive(){
		if(covered == 0 && total == 0){
			if(Packages.size() != 0){
				for(TreePackage packages: Packages){
					int[] cover = packages.getCoverageRecursive();
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
