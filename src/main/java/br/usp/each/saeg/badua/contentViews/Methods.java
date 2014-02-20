package br.usp.each.saeg.badua.contentViews;
import java.util.ArrayList;
import java.util.List;

public class Methods {
	private String name;
	private List<DUA> duas = new ArrayList<DUA>();
	private String signature;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<DUA> getDUAS() {
		return duas;
	}

	public String getCoverage(){
		if(duas.size() != 0){
			int covered = 0;
			for (DUA d : duas) {
				if(d.getCovered().equals(String.valueOf(true))) {
					covered++;
				}
				System.out.println("tem dua "+d.getCovered());
			}
			return String.format("%.2f", (double)covered/(double)duas.size()*100)+"%";
		}else return "No Def-Use Associations";
		
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

} 