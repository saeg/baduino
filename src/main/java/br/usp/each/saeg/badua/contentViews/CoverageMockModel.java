package br.usp.each.saeg.badua.contentViews;
import java.io.IOException;
import java.io.ObjectInputStream.GetField;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.IClassFile;
import org.eclipse.jdt.core.IMethod;
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
	private List<Methods> methods = new ArrayList<Methods>();
	private final DepthFirstDefUseChainSearch dfducs = new DepthFirstDefUseChainSearch();
	private IMethod[] methodList;

	public List<Methods> getMethods(){
		//get class bytecode
		ClassNode classNode = new ClassNode(Opcodes.ASM4);
		ClassReader classReader = null;

		try {
			methodList = DataflowHandler.getCu().getAllTypes()[0].getMethods();
			classReader = new ClassReader(Files.readAllBytes(DataflowHandler.getPath()));
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
		
		classReader.accept(classNode, ClassReader.SKIP_FRAMES);
		
		// for each method in the class
		for (int posMethod = 0; posMethod < classNode.methods.size(); posMethod++) {
			methodDuas(classNode, posMethod);
		}

		return methods;
	}

//usado para o type.getMethods
	private String formatName(String method){
		int finish = method.indexOf(")");
		method =  (String) method.subSequence(0, finish+1);
		return method;

	}
	
	
	private void methodDuas(ClassNode classNode, int posMethod) {

		MethodNode methodNode = classNode.methods.get(posMethod);
		Methods newMethod = new Methods();
		
	
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
		
		methods.add(newMethod);
		
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
			
			DUA newDua = new DUA(def, use, target, name);
			
			//DELETAR - GERAR DADOS ALEATORIOS PARA COBERTURA
			if(Math.random() > 0.5){
				newDua.setCovered(true);
			}else newDua.setCovered(false);

			newMethod.getDUAS().add(newDua);

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