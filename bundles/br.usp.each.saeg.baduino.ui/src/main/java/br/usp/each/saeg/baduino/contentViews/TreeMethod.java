package br.usp.each.saeg.baduino.contentViews;
import java.util.ArrayList;
import java.util.List;

public class TreeMethod {
	private String name;
	private List<TreeDUA> duas = new ArrayList<TreeDUA>();
	private int access;

	private int covered = 0;
	private int total = 0;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAccess() {
		return access;
	}

	public void setAccess(int accPublic) {
		this.access = accPublic;
	}

	public List<TreeDUA> getDUAS() {
		return duas;
	}

	public String getCoverage(){
		if(covered == 0 && total == 0) {
			getCoverageRecursive();
		}

		if(total != 0){
			return "("+covered+"/"+total+") "+String.format("%.2f", (double)covered/(double)total*100)+"%";
		}
		
		return "No Def-Use Associations";
	}

	public int[] getCoverageRecursive() {
		if (covered == 0 && total == 0) {
			if (duas.size() != 0){
				for (TreeDUA duas: duas) {
					if(duas.isCovered()) {
						covered++;
					}
				}
			}
			
			total = duas.size();
		}
		
		return new int[]{covered,total};
	}
} 