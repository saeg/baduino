package br.usp.each.saeg.badua.contentViews;

import java.util.ArrayList;
import java.util.List;

public class TreeFolder {
	private String name;
	private List<TreePackage> Packages = new ArrayList<TreePackage>();

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
		return null;
	}

}
