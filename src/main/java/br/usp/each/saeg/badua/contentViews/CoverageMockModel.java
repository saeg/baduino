package br.usp.each.saeg.badua.contentViews;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
//import org.eclipse.jdt.core.IMethod;
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
import br.usp.each.saeg.badua.handlers.VisualizationHandler;
import br.usp.each.saeg.badua.xml.XmlClass;
import br.usp.each.saeg.badua.xml.XmlInput;
import br.usp.each.saeg.badua.xml.XmlMethod;
import br.usp.each.saeg.badua.xml.XmlObject;
import br.usp.each.saeg.badua.xml.XmlPackage;
import br.usp.each.saeg.badua.xml.XmlStatement;

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
		TreeClass newClass = new TreeClass();
		newClass.setName(cu.getElementName());

		List<XmlPackage> listPackage = information.getPackages();
		XmlPackage Package = null;
		for(XmlPackage l:listPackage ){
			try {
				if((cu.getPackageDeclarations()[0].toString().contains(l.getName()))){
					//package name
					Package = l;
					break;
				}
			} catch (JavaModelException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
		ClassNode classNode = null;
		classNode = getASMClassNode(classNode,cu);
		
		
		XmlClass Clazz = null;
		if(Package != null){
			String clazzName = classNode.name.replace('/', '.');
			for (XmlClass classes : Package.getClasses()) {
				System.out.println(clazzName +" "+classes.getName());
				if(clazzName.equals(classes.getName())){
					Clazz = classes;
					break;
				}
			}
		}

		
		// for each method in the class
		for (int posMethod = 0; posMethod < classNode.methods.size(); posMethod++) {
			methodDuas(classNode, posMethod, newClass, cu, Clazz);
		}

		Class.add(newClass);

		return Class;
	}

	//transform class bytes in ClassNode form from ASM
	private ClassNode getASMClassNode(ClassNode classNode, ICompilationUnit cu) {

		classNode = new ClassNode(Opcodes.ASM4);
		ClassReader classReader = null;

		try {
			//	methodList = cu.getAllTypes()[0].getMethods();
			classReader = new ClassReader(Files.readAllBytes(getClassPath(cu)));
		} catch (JavaModelException | IOException e) {
			e.printStackTrace();
		}

		classReader.accept(classNode, ClassReader.SKIP_FRAMES);

		return classNode;
	}



	private void methodDuas(ClassNode classNode, int posMethod, TreeClass newClass,ICompilationUnit cu, XmlClass clazz) {

		MethodNode methodNode = classNode.methods.get(posMethod);

		TreeMethod newMethod = new TreeMethod(); //create new method

		if((methodNode.access & Opcodes.ACC_SYNTHETIC) != 0){
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
		//System.out.println()
		
		XmlMethod Methods = null;
		if(clazz!= null){
			for (XmlMethod m: clazz.getMethods()) {
				System.out.println(m.getName()+" "+ newMethod.getName());
				if(newMethod.getName().contains(m.getName())){
					System.out.println("entrooo: "+m.getName()+" "+ newMethod.getName());
					Methods = m;
					break;
				}
				System.out.println("vai dar null com:"+m.getName());
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
		//		System.out.println("methodNode: "+methodNode.name + " " + methodNode.desc);
		//		
		//		String s = Signature.toString(methodNode.desc);
		//		String b = Signature.toString(methodNode.desc, methodNode.name, null, false, true);
		//		if((methodNode.access & Opcodes.ACC_STATIC) != 0){
		//		}

		//		System.out.println("ATRAVEZ: "+s+ "     "+ b);
		//		
		//		for (IMethod methods:methodList) {
		//			System.out.println(methods.toString());
		//			try {
		//				if((methods.getElementName().equals(className) && methods.getSignature().equals("()V"))){
		//					String[] temp = classNode.name.split("/");
		//					System.out.println("className: "+temp[temp.length-1]);
		//					
		//				}
		//				
		//				
		//				if(methodNode.name.equals(methods.getElementName()) && methodNode.desc.equals(methods.getSignature())){
		//					newMethod.setName(formatName(methods.toString()));
		//					newMethod.setSignature(methodNode.desc);
		//					break;
		//				}
		//				else{
		//					newMethod.setName(methodNode.name);
		//				}
		//				
		//			} catch (JavaModelException e) {e.printStackTrace();}
		//		}
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
					name = var.toString();
				}
			}

			TreeDUA newDua = new TreeDUA(def, use, target, name, cu); //cria uma dua
			if(Methods != null){
				for (XmlStatement duas : Methods.getStatements()) {
					System.out.println(duas.getDef()+" "+ duas.getUse()+" "+ duas.getTarget()+" "+ duas.getVar()+"    "+def+" "+use+" "+target+" "+name);
					if(duas.getDef() == def && duas.getUse() == use && duas.getTarget() == target && duas.getVar().equals(name)){
						System.out.println("sao iguais");
						newDua.setCovered(duas.getCovered());
						break;
					}
				}
			}

//			//DELETAR - GERAR DADOS ALEATORIOS PARA COBERTURA
//			if(Math.random() > 0.5){
//				newDua.setCovered(true);
//			}else newDua.setCovered(false);

			newMethod.getDUAS().add(newDua); //adiciona nas duas existentes

		}

	}

	//usado para o type.getMethods
	//	private String formatName(String method){
	//		int finish = method.indexOf(")");
	//		method =  (String) method.subSequence(0, finish+1);
	//		return method;
	//
	//	}

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


	private Path getClassPath(ICompilationUnit cu) throws JavaModelException, IOException {

		//		System.out.println("getClassPath "+cu.toString());		//nao pode ser not open nem working copy
		//		cu.open(null);
		//		System.out.println("getClassPath "+cu.toString());
		//		System.out.println("primariElement: "+cu.getPrimary());
		IRegion region = JavaCore.newRegion();
		region.add(cu);
		IResource[] r = JavaCore.getGeneratedResources(region, false);
		//		IFile file = (IFile) r[0];
		//		IClassFile b = (IClassFile) JavaCore.createClassFileFrom(file);
		//		System.out.println(b.getSource()); 		//null
		//		IMethod[] met = b.getType().getMethods();
		//		System.out.println(met.length);			//Java Model Status [bin/source [in Max] is not on its project's build path]
		if(r.length == 0){
			System.exit(0);
		}
		IPath ipath = r[0].getLocation();

		//		IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(ipath);
		//		IClassFile f = JavaCore.createClassFileFrom(file);

		//		ASTParser p = ASTParser.newParser(AST.JLS4);
		//		p.setSource(cu);
		//		p.setKind(ASTParser.K_COMPILATION_UNIT);
		//
		//		CompilationUnit cu = (CompilationUnit)p.createAST(null);
		//		cu.accept(new ASTVisitor() {
		//			public boolean visit(MethodDeclaration node){
		//				SimpleName n = node.getName();
		//				System.out.println(n.getFullyQualifiedName());
		//				return false;
		//
		//			}
		//			public boolean visit(Initializer node){
		//				int modifiers = node.getModifiers();
		//				if(Modifier.isStatic(modifiers)){
		//					System.out.println("tem modifier static");
		//				}else{
		//					System.out.println("tem modifier");
		//				}
		//
		//				return false;
		//
		//			}
		//		});
		return Paths.get(ipath.toFile().toURI());
	}
} 