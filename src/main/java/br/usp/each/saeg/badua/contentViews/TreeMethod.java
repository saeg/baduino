package br.usp.each.saeg.badua.contentViews;
import java.util.ArrayList;
import java.util.List;

public class TreeMethod {
	private String name;
	private List<TreeDUA> Duas = new ArrayList<TreeDUA>();
	private String signature;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<TreeDUA> getDUAS() {
		return Duas;
	}

	public String getCoverage(){
		if(Duas.size() != 0){
			int covered = 0;
			for (TreeDUA d : Duas) {
				if(d.getCovered().equals(String.valueOf(true))) {
					covered++;
				}
			}
			return "("+covered+"/"+Duas.size()+") "+String.format("%.2f", (double)covered/(double)Duas.size()*100)+"%";
		}else return "No Def-Use Associations";
		
	}
	
	
	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public int getCoveredDuasCounter() {
		int covered = 0;
		for (TreeDUA d : Duas) {
			if(d.getCovered().equals(String.valueOf(true))) {
				covered++;
			}
		}
		return covered;
	}

	public int getTotalDuas() {
		return Duas.size();
	}

} 