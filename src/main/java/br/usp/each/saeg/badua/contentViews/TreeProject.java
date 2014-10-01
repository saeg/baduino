package br.usp.each.saeg.badua.contentViews;

import java.util.ArrayList;
import java.util.List;

public class TreeProject {
	private String name;
	private List<TreeFolder> Folders = new ArrayList<TreeFolder>();
	int covered = 0;
	int total = 0;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<TreeFolder> getFolders() {
		return Folders;
	}

	public String getCoverage() {
		if(Folders.size() != 0){
			for(TreeFolder folders: Folders){
				int[] cover = folders.getCoverageRecursive();
				covered += cover[0];
				total += cover[1];
			}
			if(total != 0){
				return "("+covered+"/"+total+") "+String.format("%.2f", (double)covered/(double)total*100)+"%";

			}
			return "No Def-Use Associations";
		}
		return "No Def-Use Associations";
	}

}
