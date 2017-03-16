package br.usp.each.saeg.baduino.tree;

import java.util.List;
import java.util.stream.Collectors;

import br.usp.each.saeg.baduino.xml.XMLClass;
import br.usp.each.saeg.baduino.xml.XMLCounter;
import br.usp.each.saeg.baduino.xml.XMLDua;
import br.usp.each.saeg.baduino.xml.XMLMethod;
import br.usp.each.saeg.baduino.xml.XMLProject;

/**
 * 
 * @author Mario Concilio
 *
 */
public class TreeBuilder {
	
	public static TreeProject build(XMLProject xmlProject) {
		final List<TreePackage> packages = xmlProject.getClasses()
				.stream()
				.map(TreeBuilder::buildPackage)
				.collect(Collectors.toList());
		
		final List<TreeClass> classes = xmlProject.getClasses()
				.stream()
				.map(TreeBuilder::buildClass)
				.collect(Collectors.toList());
		
		packages.forEach(pkg -> pkg.setClasses(classes));
		
		final List<TreeCounter> counters = xmlProject.getCounters()
				.stream()
				.map(TreeBuilder::buildCounter)
				.collect(Collectors.toList());
		
		final TreeProject project = new TreeProject();
		project.setCounters(counters);
		project.setPackages(packages);
		
		return project;
	}
	
	public static TreePackage buildPackage(XMLClass xmlClass) {
		int index = xmlClass.getName().lastIndexOf(".");
		index = (index < 0)? 0 : index;
		final String name = xmlClass.getName().substring(0, index);
		
		final TreePackage pkg = new TreePackage();
		pkg.setName(name);
		
		return pkg;
	}
	
	public static TreeClass buildClass(XMLClass xmlClass) {
		final List<TreeMethod> methods = xmlClass.getMethods()
				.stream()
				.map(TreeBuilder::buildMethod)
				.collect(Collectors.toList());
		
		final List<TreeCounter> counters = xmlClass.getCounters()
				.stream()
				.map(TreeBuilder::buildCounter)
				.collect(Collectors.toList());
		
		final TreeClass clazz = new TreeClass();
		clazz.setName(xmlClass.getName());
		clazz.setCounters(counters);
		clazz.setMethods(methods);
		
		return clazz;
	}
	
	public static TreeMethod buildMethod(XMLMethod xmlMethod) {
		final List<TreeCounter> counters = xmlMethod.getCounters()
				.stream()
				.map(TreeBuilder::buildCounter)
				.collect(Collectors.toList());
		
		final List<TreeDua> duas = xmlMethod.getDuas()
				.stream()
				.map(TreeBuilder::buildDua)
				.collect(Collectors.toList());
		
		final TreeMethod method = new TreeMethod();
		method.setName(xmlMethod.getName());
		method.setCounters(counters);
		method.setDuas(duas);
		
		return method;
	}
	
	public static TreeDua buildDua(XMLDua xmlDua) {
		final TreeDua dua = new TreeDua();
		dua.setCovered(xmlDua.isCovered());
		dua.setDef(xmlDua.getDef());
		dua.setTarget(xmlDua.getTarget());
		dua.setUse(xmlDua.getUse());
		dua.setVar(xmlDua.getVar());
		
		return dua;
	}
	
	public static TreeCounter buildCounter(XMLCounter xmlCounter) {
		final TreeCounter counter = new TreeCounter();
		counter.setCovered(xmlCounter.getCovered());
		counter.setMissed(xmlCounter.getMissed());
		counter.setType(xmlCounter.getType());
		
		return counter;
	}

}
