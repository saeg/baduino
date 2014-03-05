package br.usp.each.saeg.badua.contentViews;
import java.util.ArrayList;
import java.util.List;

public class TreeMethod {
	private String name;
	private List<TreeDUA> Duas = new ArrayList<TreeDUA>();
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
		return Duas;
	}

	public String getCoverage(){
		if(covered == 0 && total == 0){
			getCoverageRecursive();
		}

		if(covered != 0 && total != 0){
			return "("+covered+"/"+total+") "+String.format("%.2f", (double)covered/(double)total*100)+"%";
		}else{
			return "No Def-Use Associations";
		}
	}

	public int[] getCoverageRecursive() {
		if(covered == 0 && total == 0){
			if(Duas.size() != 0){
				for(TreeDUA duas: Duas){
					if(duas.getCovered().equals(String.valueOf(true))) {
						covered++;
					}
				}
			}
			total = Duas.size();
		}
		return new int[]{covered,total};
	}
} 