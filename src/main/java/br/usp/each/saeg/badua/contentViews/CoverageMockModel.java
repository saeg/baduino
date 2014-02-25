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
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IRegion;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.LineNumberNode;
import org.objectweb.asm.tree.LocalVariableNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.analysis.AnalyzerException;

import br.com.ooboo.asm.defuse.DefUseAnalyzer;
import br.com.ooboo.asm.defuse.DefUseChain;
import br.com.ooboo.asm.defuse.DepthFirstDefUseChainSearch;
import br.com.ooboo.asm.defuse.Field;
import br.com.ooboo.asm.defuse.Local;
import br.com.ooboo.asm.defuse.Variable;
import br.usp.each.saeg.badua.handlers.DataflowHandler;

public class CoverageMockModel  {
	private DefUseAnalyzer analyzer = new DefUseAnalyzer();
	private DefUseChain[] duas;
	private Variable[] vars;
	private final DepthFirstDefUseChainSearch dfducs = new DepthFirstDefUseChainSearch();
	private IMethod[] methodList;

	List<TreeClass> Class = new ArrayList<TreeClass>();
	List<TreePackage> Package = new ArrayList<TreePackage>();




	public List<?> getTree() {
		if(DataflowHandler.getType() instanceof IPackageFragment){
			return getPackageNode((IPackageFragment) DataflowHandler.getType());
		}
		if(DataflowHandler.getType() instanceof ICompilationUnit){
			return getClassNode((ICompilationUnit) DataflowHandler.getType(),null);
		}
		return null;
	}

	public List<TreePackage> getPackageNode(IPackageFragment pf) {


		TreePackage newPackage = new TreePackage();
		newPackage.setName(pf.getElementName());

		try {
			IJavaElement[] children = pf.getChildren();
			List<TreeClass> Classes;
			for (int i = 0; i < children.length; i++) {
				ICompilationUnit child = (ICompilationUnit) children[i]; //tratar o caso se nao for ICompilationUnit
				Classes = getClassNode(child, newPackage);
				newPackage.getClasses().addAll(Classes);
				Classes.clear();
			}

		} catch (JavaModelException e) {
			e.printStackTrace();
		}
		Package.add(newPackage);
		return Package;
	}

	public List<TreeClass> getClassNode(ICompilationUnit cu,TreePackage newPackage){


		//get class bytecode
		ClassNode classNode = new ClassNode(Opcodes.ASM4);
		ClassReader classReader = null;

		try {
			methodList = cu.getAllTypes()[0].getMethods();
			classReader = new ClassReader(Files.readAllBytes(getClassPaths(cu)));
		} catch (JavaModelException | IOException e) {
			e.printStackTrace();
		}



		classReader.accept(classNode, ClassReader.SKIP_FRAMES);

		TreeClass newClass = new TreeClass();
		newClass.setName(classNode.name);


		Class.add(newClass);

		// for each method in the class
		for (int posMethod = 0; posMethod < classNode.methods.size(); posMethod++) {
			methodDuas(classNode, posMethod,newClass,cu);
		}

		return Class;
	}

	private void methodDuas(ClassNode classNode, int posMethod, TreeClass newClass,ICompilationUnit cu) {

		MethodNode methodNode = classNode.methods.get(posMethod);

		TreeMethod newMethod = new TreeMethod(); // cria um novo method


		//System.out.println(methodList.length);
		if(!methodNode.name.equals("<init>") && !methodNode.name.equals("<clinit>")){
			for (int i = 0; i < methodList.length; i++) {
				try {
					if(methodNode.name.equals(methodList[i].getElementName()) && methodNode.desc.equals(methodList[i].getSignature())){
						newMethod.setName(formatName(methodList[i].toString()));
						newMethod.setSignature(methodNode.desc);
						break;
					}
					else{
						newMethod.setName(methodNode.name);
						//System.out.println(methodNode.desc + " " + methodList[i].getSignature());
					}
				} catch (JavaModelException e) {
					e.printStackTrace();
				}
			}
		}
		else{
			newMethod.setName(methodNode.name);		
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

			//DELETAR - GERAR DADOS ALEATORIOS PARA COBERTURA
			if(Math.random() > 0.5){
				newDua.setCovered(true);
			}else newDua.setCovered(false);

			newMethod.getDUAS().add(newDua); //adiciona nas duas existentes

		}

	}

	//usado para o type.getMethods
	private String formatName(String method){
		int finish = method.indexOf(")");
		method =  (String) method.subSequence(0, finish+1);
		return method;

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


	private Path getClassPaths(ICompilationUnit cu) throws JavaModelException, IOException {

		IRegion region = JavaCore.newRegion();
		//		for(ICompilationUnit compilationUnit : cu){
		//			region.add(compilationUnit.getPrimaryElement());
		//		}
		region.add(cu.getPrimaryElement());
		IResource[] r = JavaCore.getGeneratedResources(region, false);


		//		IFile file = (IFile) r[0];
		//		IClassFile b = (IClassFile) JavaCore.createClassFileFrom(file);
		//		System.out.println(b.getSource()); 		//null
		//		IMethod[] met = b.getType().getMethods();
		//		System.out.println(met.length);			//Java Model Status [bin/source [in Max] is not on its project's build path]




		IPath ipath = r[0].getLocation();
		//		IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(ipath);
		//		IClassFile f = JavaCore.createClassFileFrom(file);


		//		
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

		//byte[] buffer2 = Files.readAllBytes(getPath());

	}
} 