package br.usp.each.saeg.baduino.contentViews;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
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
import br.usp.each.saeg.asm.defuse.DefUseFrame;
import br.usp.each.saeg.asm.defuse.DepthFirstDefUseChainSearch;
import br.usp.each.saeg.asm.defuse.Field;
import br.usp.each.saeg.asm.defuse.Local;
import br.usp.each.saeg.asm.defuse.Variable;
import br.usp.each.saeg.baduino.handlers.VisualizationHandler;
import br.usp.each.saeg.baduino.xml.XmlClass;
import br.usp.each.saeg.baduino.xml.XmlInput;
import br.usp.each.saeg.baduino.xml.XmlMethod;
import br.usp.each.saeg.baduino.xml.XmlObject;
import br.usp.each.saeg.baduino.xml.XmlDua;

//Structure layers  
//layer 0: IJavaProject(Project)|
//layer 1:						|->IPackageFragmentRoot(Folders)|
//layer 2: 														|->IPackageFragment(Packages)|
//layer 3: 																					 |->ICompilationUnit(Classes)

public class CoverageMockModel  {
	
	private static final Logger logger = Logger.getLogger(CoverageMockModel.class);

	private Variable[] variables;
	private int[][] basicBlocks;
	private int[] leaders;
	private String className;
	private XmlInput information;

	//	private IMethod[] methodList;

	public List<?> getTree() throws JavaModelException {
		information = XmlObject.getInstance();

		final List<XmlClass> classes = information.getClasses();
		for (XmlClass clazz : classes) {
			logger.debug("class: " + clazz);
			
			final List<XmlMethod> methods = clazz.getMethods();
			for (XmlMethod method : methods) {
				logger.debug("method: " + method);
				
				final List<XmlDua> statements = method.getDuas();
				for (XmlDua statement : statements) {
					logger.debug("result: " + statement);			
				}
			}
		}
		
		logger.debug(information);
		
		//use the selection type to show the type level and below 
		if (VisualizationHandler.getType() instanceof IJavaProject){
			return getProjectNode((IJavaProject) VisualizationHandler.getType());
		}

		if (VisualizationHandler.getType() instanceof IPackageFragmentRoot){
			return getFolderNode((IPackageFragmentRoot) VisualizationHandler.getType());
		}

		if (VisualizationHandler.getType() instanceof IPackageFragment){
			return getPackageNode((IPackageFragment) VisualizationHandler.getType());
		}

		if (VisualizationHandler.getType() instanceof ICompilationUnit){
			return getClassNode((ICompilationUnit) VisualizationHandler.getType());
		}
		
		return null;
	}

	//get coverage from Project Layer and below
	private List<TreeProject> getProjectNode(final IJavaProject jp) throws JavaModelException {
		final List<TreeProject> project = new ArrayList<>();
		final TreeProject newProject = new TreeProject();
		newProject.setName(jp.getElementName());

		final IJavaElement[] children = jp.getChildren();

		List<TreeFolder> folders;
		for (int i = 0; i < children.length; i++) {
			if(children[i] instanceof IPackageFragmentRoot){
				IPackageFragmentRoot child = (IPackageFragmentRoot) children[i];
				folders = getFolderNode(child);
				newProject.getFolders().addAll(folders);
			}
		}

		project.add(newProject);
		return project;
	}

	//get coverage from Folder Layer
	private List<TreeFolder> getFolderNode(final IPackageFragmentRoot pfr) throws JavaModelException {
		final List<TreeFolder> folder = new ArrayList<>();
		final TreeFolder newFolder = new TreeFolder();
		newFolder.setName(pfr.getElementName());

		if (!pfr.isArchive()) { //ignore external folders like .jar
			final IJavaElement[] children = pfr.getChildren();
			if (children.length == 0) {
				return folder;
			}

			List<TreePackage> packages;
			for (int i = 0; i < children.length; i++) {
				if(children[i] instanceof IPackageFragment){
					IPackageFragment child = (IPackageFragment) children[i];
					packages = getPackageNode(child);
					newFolder.getPackages().addAll(packages);
				}
			}

			folder.add(newFolder);
		}

		return folder;
	}

	//get coverage from Package Layer
	public List<TreePackage> getPackageNode(final IPackageFragment pf) throws JavaModelException {
		final List<TreePackage> pkg = new ArrayList<>();
		final TreePackage newPackage = new TreePackage();
		
		if (pf.isDefaultPackage()){
			newPackage.setName("(Default Package)");
		}
		else {
			newPackage.setName(pf.getElementName());
		}

		final IJavaElement[] children = pf.getChildren();
		if(children.length == 0){
			return pkg;
		}
		
		List<TreeClass> classes;
		for (int i = 0; i < children.length; i++) {
			if(children[i] instanceof ICompilationUnit){
				ICompilationUnit child = (ICompilationUnit) children[i];
				classes = getClassNode(child);
				newPackage.getClasses().addAll(classes);
			}
		}

		pkg.add(newPackage);
		return pkg;
	}

	//get coverage from Class Layer
	public List<TreeClass> getClassNode(final ICompilationUnit cu){
		final List<TreeClass> listClass = new ArrayList<>(); //create a list
		final LinkedList<ClassNode> classNodes = getASMClassNode(cu);
		
		for (ClassNode classNode : classNodes) {
			final TreeClass newClass = new TreeClass();
			final String[] name = classNode.name.split("/");
			newClass.setName(name[name.length-1] + ".java");

			XmlClass clazz = null;
			if (information != null) {
				final String clazzName = classNode.name.replace('/', '.');
				final List<XmlClass> xmlClasses = information.getClasses();
				
				for (XmlClass c : xmlClasses) {
					if (clazzName.equals(c.getName())) {
						clazz = c;
						break;
					}
				}
				
				/*
				List<XmlPackage> listPackage = information.getPackages();
				XmlPackage Package = null;
				for(XmlPackage l:listPackage ){
					try {
						if(cu.getPackageDeclarations().length > 0){
							String packageName = extractPackageName(cu.getPackageDeclarations()[0].toString());
							if(packageName.equals(l.getName())){
								Package = l;
								break;
							}
						}else {
							if(newClass.getName().equals(l.getName())){
								Package = l;
								break;
							}
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
							clazz = classes;
							break;
						}
					}
				}
				*/
			}

			className = classNode.name;
			
			// for each method in the class
			for (int posMethod = 0; posMethod < classNode.methods.size(); posMethod++) {
				methodDuas(classNode, posMethod, newClass, cu, clazz);
			}
			
			listClass.add(newClass);
		}
		
		return listClass;
	}

	private String extractPackageName(String Package) {
		return Package.substring(8, Package.indexOf(" ["));
	}

	//transform class bytes in ClassNode form from ASM
	private LinkedList<ClassNode> getASMClassNode(final ICompilationUnit cu) {
		final LinkedList<ClassNode> classNodes = new LinkedList<>();
		
		try {
			final LinkedList<Path> paths = getClasspath(cu); // path for the inner class and mainly class
			
			for (Path path : paths) {
				final byte[] bytes = Files.readAllBytes(path);
				final ClassNode classNode = new ClassNode();
				final ClassReader classReader = new ClassReader(bytes);
				classReader.accept(classNode, ClassReader.EXPAND_FRAMES);
				
				//do not analyze interfaces
				if ((classNode.access & Opcodes.ACC_INTERFACE) == 0) {
					classNodes.add(classNode);
				}
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return classNodes;
	}

	private LinkedList<Path> getClasspath(final ICompilationUnit cu) throws JavaModelException, IOException {
		final IRegion region = JavaCore.newRegion();
		region.add(cu);
		
		final IResource[] resources = JavaCore.getGeneratedResources(region, false);
		final LinkedList<Path> path = new LinkedList<Path>();
		for (IResource resource : resources) {
			path.add(Paths.get(resource.getLocation().toFile().toURI()));
		}
		
		return path;
	}

	private void methodDuas(final ClassNode classNode, final int posMethod, final TreeClass newClass, final ICompilationUnit cu, final XmlClass clazz) {
		final MethodNode methodNode = classNode.methods.get(posMethod);
		final TreeMethod newMethod = new TreeMethod(); //create new method

		// Does not instrument:
		// 1. Abstract methods
		if ((methodNode.access & Opcodes.ACC_ABSTRACT) != 0) {
			return;
		}
		// 2. Interfaces
		if ((methodNode.access & Opcodes.ACC_INTERFACE) != 0){
			return;
		}
		// 3. Synthetic methods
		if ((methodNode.access & Opcodes.ACC_SYNTHETIC) != 0){
			return;
		}
		// 4. Static class initialization
		if (methodNode.name.equals("<clinit>")) {
			return;
		}

		final String[] fullClassName = classNode.name.split("/");
		final String className = fullClassName[fullClassName.length - 1];
		//System.out.println(methodNode.name + " " + methodNode.desc);
		if (methodNode.name.equals("<clinit>")) {
			final String name = Signature.toString(methodNode.desc, className, null, false, false);
			newMethod.setName("static " + name);
			//System.out.println("<clinit> vira: static "+name);
		}
		else if (methodNode.name.equals("<init>")) {
			final String name = Signature.toString(methodNode.desc, className, null, false, false);
			newMethod.setName(name);
			//System.out.println("<init> vira: "+name);
		}
		else {
			final String name = Signature.toString(methodNode.desc, methodNode.name, null, false,true);
			
			if ((methodNode.access & Opcodes.ACC_STATIC) != 0) {
				newMethod.setName("static " + name);
				//	System.out.println("com flag ACC_STATIC vira: static "+name);
			}
			else {
				newMethod.setName(name);
				//	System.out.println("Default: "+name);
			}
		}

		XmlMethod xmlMethod = null;
		if (clazz != null) {
			final List<XmlMethod> xmlMethods = clazz.getMethods();
			
			for (XmlMethod m : xmlMethods) {
				if (newMethod.getName().equals(m.getName())) {
					xmlMethod = m;
					break;
				}
			}
		}

		if ((methodNode.access & Opcodes.ACC_PUBLIC) != 0){
			newMethod.setAccess(Opcodes.ACC_PUBLIC);
		}
		else if ((methodNode.access & Opcodes.ACC_PROTECTED) != 0){
			newMethod.setAccess(Opcodes.ACC_PROTECTED);
		}
		else if ((methodNode.access & Opcodes.ACC_PRIVATE) != 0){
			newMethod.setAccess(Opcodes.ACC_PRIVATE);
		}
		else {
			newMethod.setAccess(-1); //default methods;
		}

		newClass.getMethods().add(newMethod);//adiciona aos metodos existentes

		List<XmlDua> xmlDuas = null;
		if (xmlMethod != null) {
//			duasXML =  (ArrayList<XmlDua>) xmlMethod.getStatements().clone();
			xmlDuas = xmlMethod.getDuas();
		}

		final int[] lines = getLines(methodNode);
		final DefUseChain[] duaI = transform(methodNode);
		
		for (DefUseChain defUseChain : duaI) {
			DefUseChain bbchain = toBB(defUseChain);
			
			if(bbchain != null){
				int defLine = lines[defUseChain.def];
				int useLine = lines[defUseChain.use];
				int targetLine = -1;
				if (defUseChain.target != -1){
					targetLine = lines[defUseChain.target];
				}
				
				final String varName = getName(defUseChain,methodNode);
				if (varName != null) {
					final TreeDUA newDua = new TreeDUA(defLine, useLine, targetLine, varName, cu); //cria uma dua
					
					if (xmlDuas != null) {
						for (XmlDua dua : xmlDuas) {
							if (
									dua.getDef() == defLine &&
									dua.getUse() == useLine &&
									dua.getTarget() == targetLine &&
									dua.getVar().equals(varName)
									) {
								
								boolean covered = dua.getCovered();
								logger.debug("dua covered: " + dua.getCovered());
								newDua.setCovered(covered);
								break;
							}
						}
						
//						for(int i = 0; i< xmlDuas.size(); i++){
//							if (xmlDuas.get(i).getDef() == defLine && 
//									xmlDuas.get(i).getUse() == useLine && 
//									xmlDuas.get(i).getTarget() == targetLine && 
//									xmlDuas.get(i).getVar().equals(varName)) {
//								
//								logger.debug("dua covered: " + xmlDuas.get(i).getCovered());
//								newDua.setCovered(xmlDuas.get(i).getCovered());
//								xmlDuas.remove(i);
//								break;
//							}
//						}

					}
					
					newMethod.getDUAS().add(newDua); //adiciona nas duas existentes
				}
			}
		}
	}

	private DefUseChain toBB(DefUseChain c) {
		if (DefUseChain.isGlobal(c, leaders, basicBlocks)) {
			return new DefUseChain(leaders[c.def], leaders[c.use], c.target == -1 ? -1 : leaders[c.target], c.var);
		}
		
		return null;
	}

	private String getName(final DefUseChain dua, MethodNode methodNode) {
		final Variable var = variables[dua.var];
		String name;
		if (var instanceof Field) {
			name = ((Field) var).name;
		} else {
			try {
				name = varName(dua.use, ((Local) var).var, methodNode);
			} catch (final Exception e) {
				name = null;
			}
		}

		return name;
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


	private DefUseChain[] transform(final MethodNode methodNode) {
		final DefUseAnalyzer analyzer = new DefUseAnalyzer();
		try {
			analyzer.analyze(className, methodNode);
		} catch (final AnalyzerException e) {
			throw new RuntimeException(e);
		}
		
		final DefUseFrame[] frames = analyzer.getDefUseFrames();
		variables = analyzer.getVariables();
		final int[][] successors = analyzer.getSuccessors();
		final int[][] predecessors = analyzer.getPredecessors();
		basicBlocks = analyzer.getBasicBlocks();
		leaders = analyzer.getLeaders();
		//defuse with instructions
		return new DepthFirstDefUseChainSearch().search(frames, variables, successors, predecessors);
	}


	private int[] getLines(MethodNode methodNode) {
		final int[] lines = new int[methodNode.instructions.size()];
		for (int i = 0; i < lines.length; i++) {
			if (methodNode.instructions.get(i) instanceof LineNumberNode) {
				final LineNumberNode insn = (LineNumberNode) methodNode.instructions.get(i);
				lines[methodNode.instructions.indexOf(insn.start)] = insn.line;
			}
		}

		int line = 1;
		for (int i = 0; i < lines.length; i++) {
			if (lines[i] == 0) {
				lines[i] = line;
			} else {
				line = lines[i];
			}
		}
		return lines;
	}


} 