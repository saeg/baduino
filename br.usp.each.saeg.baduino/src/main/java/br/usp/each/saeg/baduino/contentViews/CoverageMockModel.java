package br.usp.each.saeg.baduino.contentViews;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IRegion;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.LineNumberNode;
import org.objectweb.asm.tree.LocalVariableNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.analysis.AnalyzerException;

import br.usp.each.saeg.asm.defuse.DefUseAnalyzer;
import br.usp.each.saeg.asm.defuse.DefUseChain;
import br.usp.each.saeg.asm.defuse.DepthFirstDefUseChainSearch;
import br.usp.each.saeg.asm.defuse.Field;
import br.usp.each.saeg.asm.defuse.Local;
import br.usp.each.saeg.asm.defuse.Variable;
import br.usp.each.saeg.baduino.handlers.VisualizationHandler;
import br.usp.each.saeg.baduino.xml.XmlClass;
import br.usp.each.saeg.baduino.xml.XmlInput;
import br.usp.each.saeg.baduino.xml.XmlMethod;
import br.usp.each.saeg.baduino.xml.XmlObject;
import br.usp.each.saeg.baduino.xml.XmlPackage;
import br.usp.each.saeg.baduino.xml.XmlStatement;

//Structure layers  
//layer 0: IJavaProject(Project)|
//layer 1:						|->IPackageFragmentRoot(Folders)|
//layer 2: 														|->IPackageFragment(Packages)|
//layer 3: 																					 |->ICompilationUnit(Classes)

public class CoverageMockModel  {

	private DefUseAnalyzer analyzer = new DefUseAnalyzer();
	private DefUseChain[] duas;
	private Variable[] vars;
	private final DepthFirstDefUseChainSearch dfducs = new DepthFirstDefUseChainSearch();
	private static XmlInput information;

	//	private IMethod[] methodList;

	public List<?> getTree() {
		information = XmlObject.getInstance();
		//use the selection type to show the type level and below 
		if(VisualizationHandler.getType() instanceof IJavaProject){
			return getProjectNode((IJavaProject) VisualizationHandler.getType());
		}

		if(VisualizationHandler.getType() instanceof IPackageFragmentRoot){
			return getFolderNode((IPackageFragmentRoot) VisualizationHandler.getType());
		}

		if(VisualizationHandler.getType() instanceof IPackageFragment){
			return getPackageNode((IPackageFragment) VisualizationHandler.getType());
		}

		if(VisualizationHandler.getType() instanceof ICompilationUnit){
			return getClassNode((ICompilationUnit) VisualizationHandler.getType());
		}
		return null;
	}


	//get coverage from Project Layer and below
	private List<TreeProject> getProjectNode(IJavaProject jp) {
		List<TreeProject> Project = new ArrayList<TreeProject>();
		TreeProject newProject = new TreeProject();
		newProject.setName(jp.getElementName());

		try{
			IJavaElement[] children = jp.getChildren();
			List<TreeFolder> Folders;
			for (int i = 0; i < children.length; i++) {
				if(children[i] instanceof IPackageFragmentRoot){
					IPackageFragmentRoot child = (IPackageFragmentRoot) children[i];
					Folders = getFolderNode(child);
					newProject.getFolders().addAll(Folders);
				}
			}

		} catch (JavaModelException e) {e.printStackTrace();}

		Project.add(newProject);

		return Project;
	}


	//get coverage from Folder Layer
	private List<TreeFolder> getFolderNode(IPackageFragmentRoot pfr) {
		List<TreeFolder> Folder = new ArrayList<TreeFolder>();
		TreeFolder newFolder = new TreeFolder();
		newFolder.setName(pfr.getElementName());

		if(!pfr.isArchive()){ //ignore external folders like .jar

			try {
				IJavaElement[] children = pfr.getChildren();
				if(children.length == 0){
					return Folder;
				}
				List<TreePackage> Packages;
				for (int i = 0; i < children.length; i++) {
					if(children[i] instanceof IPackageFragment){
						IPackageFragment child = (IPackageFragment) children[i];
						Packages = getPackageNode(child);
						newFolder.getPackages().addAll(Packages);
					}
				}

			} catch (JavaModelException e) {e.printStackTrace();}

			Folder.add(newFolder);
		}

		return Folder;
	}


	//get coverage from Package Layer
	public List<TreePackage> getPackageNode(IPackageFragment pf) {
		List<TreePackage> Package = new ArrayList<TreePackage>();
		TreePackage newPackage = new TreePackage();
		if(pf.isDefaultPackage()){
			newPackage.setName("(Default Package)");
		}else{
			newPackage.setName(pf.getElementName());
		}
		try {
			IJavaElement[] children = pf.getChildren();
			if(children.length == 0){
				return Package;
			}
			List<TreeClass> Classes;
			for (int i = 0; i < children.length; i++) {
				if(children[i] instanceof ICompilationUnit){
					ICompilationUnit child = (ICompilationUnit) children[i];
					Classes = getClassNode(child);
					newPackage.getClasses().addAll(Classes);
				}
			}

		} catch (JavaModelException e) {e.printStackTrace();}

		Package.add(newPackage);
		return Package;
	}


	//get coverage from Class Layer
	public List<TreeClass> getClassNode(ICompilationUnit cu){
		List<TreeClass> Class = new ArrayList<TreeClass>(); //create a list
		LinkedList<ClassNode> classNodes = getASMClassNode(cu);
		for (ClassNode classNode : classNodes) {
			TreeClass newClass = new TreeClass();

			String[] name = classNode.name.split("/");
			newClass.setName(name[name.length-1]+".java");
			
			XmlClass Clazz = null;
			if(information != null){
				List<XmlPackage> listPackage = information.getPackages();
				XmlPackage Package = null;
				for(XmlPackage l:listPackage ){
					try {
						String packageName = extractPackageName(cu.getPackageDeclarations()[0].toString());
						if(packageName.equals(l.getName())){
							Package = l;
							break;
						}
					} catch (JavaModelException e) {
						e.printStackTrace();
					}
				}
				
				if(Package != null){
					String clazzName = classNode.name.replace('/', '.');
					for (XmlClass classes : Package.getClasses()) {
						//System.out.println(clazzName +" "+classes.getName());
						if(clazzName.equals(classes.getName())){
							Clazz = classes;
							break;
						}
					}
				}
			}
		

			// for each method in the class
			for (int posMethod = 0; posMethod < classNode.methods.size(); posMethod++) {
				methodDuas(classNode, posMethod, newClass, cu, Clazz);
			}
			Class.add(newClass);
		}
		return Class;
	}

	private String extractPackageName(String Package) {
		return Package.substring(8, Package.indexOf(" ["));
	}


	//transform class bytes in ClassNode form from ASM
	private LinkedList<ClassNode> getASMClassNode(ICompilationUnit cu) {
		LinkedList<ClassNode> classNodes = new LinkedList<ClassNode>();
		try {
			LinkedList<Path> paths = getClasspath(cu); // path for the inner class and mainly class
			for (Path path : paths) {
				ClassNode classNode = new ClassNode(Opcodes.ASM4);
				ClassReader classReader = new ClassReader(Files.readAllBytes(path));
				classReader.accept(classNode, ClassReader.EXPAND_FRAMES);
				//do not analyze interfaces
				if ((classNode.access & Opcodes.ACC_INTERFACE) == 0) {
					classNodes.add(classNode);
				}
			}
		} catch (JavaModelException |IOException e) {
			e.printStackTrace();
		}
		return classNodes;
	}

	private LinkedList<Path> getClasspath(ICompilationUnit cu) throws JavaModelException, IOException {
		IRegion region = JavaCore.newRegion();
		region.add(cu);
		IResource[] r = JavaCore.getGeneratedResources(region, false);
		LinkedList<Path> path = new LinkedList<Path>();
		for (IResource iResource : r) {
			path.add(Paths.get(iResource.getLocation().toFile().toURI()));
		}
		return path;
	}


	@SuppressWarnings("unchecked")
	private void methodDuas(ClassNode classNode, int posMethod, TreeClass newClass,ICompilationUnit cu, XmlClass clazz) {

		MethodNode methodNode = classNode.methods.get(posMethod);

		TreeMethod newMethod = new TreeMethod(); //create new method

		// Does not instrument:
		// 1. Abstract methods
		if ((methodNode.access & Opcodes.ACC_ABSTRACT) != 0) {
			return;
		}
		// 2. Interfaces
		if((methodNode.access & Opcodes.ACC_INTERFACE) != 0){
			return;
		}
		// 3. Synthetic methods
		if((methodNode.access & Opcodes.ACC_SYNTHETIC) != 0){
			return;
		}
		// 4. Static class initialization
		if (methodNode.name.equals("<clinit>")) {
			return;
		}

		String[] fullClassName = classNode.name.split("/");
		String className = fullClassName[fullClassName.length-1];
		//System.out.println(methodNode.name + " " + methodNode.desc);
		if(methodNode.name.equals("<clinit>")){
			String name = Signature.toString(methodNode.desc, className, null, false, false);
			newMethod.setName("static "+name);
			//System.out.println("<clinit> vira: static "+name);
		}else if(methodNode.name.equals("<init>")){
			String name = Signature.toString(methodNode.desc, className, null, false, false);
			newMethod.setName(name);
			//System.out.println("<init> vira: "+name);
		}else{
			String name = Signature.toString(methodNode.desc, methodNode.name, null, false,true);
			if((methodNode.access & Opcodes.ACC_STATIC) != 0){
				newMethod.setName("static " + name);
				//	System.out.println("com flag ACC_STATIC vira: static "+name);
			}else{
				newMethod.setName(name);
				//	System.out.println("Default: "+name);
			}
		}


		XmlMethod Methods = null;
		if(clazz!= null){
			for (XmlMethod m: clazz.getMethods()) {
				if(newMethod.getName().equals(m.getName())){
					Methods = m;
					break;
				}
			}
		}


		if((methodNode.access & Opcodes.ACC_PUBLIC) != 0){
			newMethod.setAccess(Opcodes.ACC_PUBLIC);
		}else if((methodNode.access & Opcodes.ACC_PROTECTED) != 0){
			newMethod.setAccess(Opcodes.ACC_PROTECTED);
		}else if((methodNode.access & Opcodes.ACC_PRIVATE) != 0){
			newMethod.setAccess(Opcodes.ACC_PRIVATE);
		}else{
			newMethod.setAccess(-1);//default methods;
		}

		newClass.getMethods().add(newMethod);//adiciona aos metodos existentes

		int[] lines = new int[methodNode.instructions.size()];
		for (int i = 0; i < lines.length; i++) {
			if (methodNode.instructions.get(i) instanceof LineNumberNode) {
				final LineNumberNode insn = (LineNumberNode) methodNode.instructions.get(i);
				lines[methodNode.instructions.indexOf(insn.start)] = insn.line;
			}
		}
		int line = 1;
		for (int i = 0; i < lines.length; i++) {
			if (lines[i] == 0)
				lines[i] = line;
			else
				line = lines[i];
		}


		try {
			analyzer.analyze(classNode.name,methodNode); // right ?

			// find all definition-use chains
			duas = dfducs.search(analyzer.getDefUseFrames(), analyzer.getVariables(),
					analyzer.getSuccessors(), analyzer.getPredecessors());

			// only global definition-use chains
			duas = DefUseChain.globals(duas, analyzer.getLeaders(), analyzer.getBasicBlocks());

			vars = analyzer.getVariables();

		} catch (final AnalyzerException ignore) {
			duas = new DefUseChain[0];
			vars = new Variable[0];
		}
		
		
		List<LocalVariableNode> localVariables = methodNode.localVariables;
		HashMap<Integer,String> variables = new HashMap<Integer,String>();
		for(LocalVariableNode var: localVariables) variables.put(var.index,var.name);
		ArrayList<XmlStatement> duasXML=null;
		if(Methods != null){
			
			duasXML =  (ArrayList<XmlStatement>) Methods.getStatements().clone();
			
		}
		
		for (final DefUseChain dua : duas) {
			final Variable var = vars[dua.var];
			final int def = lines[dua.def];
			final int use = lines[dua.use];
			int target = dua.target;
			if(target != -1){
				target = lines[dua.target];
			}
			String name;
			if (var instanceof Field) {
				name = ((Field) var).name;
			} else {
				try {
					name = varName(dua.def, ((Local) var).var,methodNode);
				} catch (Exception e) {
					//					name = var.toString();
					name = null;
				}
			}
			if(name != null){
				//name = getVariableName(name, variables);
				TreeDUA newDua = new TreeDUA(def, use, target, name, cu); //cria uma dua
				if(duasXML != null){
					
					for(int i = 0; i< duasXML.size();i++){
//					for (XmlStatement duas : Methods.getStatements()) {
						//System.out.println(duas.getDef()+" "+ duas.getUse()+" "+ duas.getTarget()+" "+ duas.getVar()+"    "+def+" "+use+" "+target+" "+name);
						if(duasXML.get(i).getDef() == def && duasXML.get(i).getUse() == use && duasXML.get(i).getTarget() == target && duasXML.get(i).getVar().equals(name)){
							//System.out.println("sao iguais");
							newDua.setCovered(duasXML.get(i).getCovered());
							duasXML.remove(i);
							break;
						}
					}
					
				}
				newMethod.getDUAS().add(newDua); //adiciona nas duas existentes
			}
		}

	}

	private String varName(final int insn, final int index, MethodNode methodNode) {
		for (final LocalVariableNode local : methodNode.localVariables) {
			if (local.index == index) {
				final int start = methodNode.instructions.indexOf(local.start);
				final int end = methodNode.instructions.indexOf(local.end);
				if (insn + 1 >= start && insn + 1 <= end) {
					return local.name;
				}
			}
		}
		throw new RuntimeException("Variable not found");
	}
} 