import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.LineNumberNode;
import org.objectweb.asm.tree.LocalVariableNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.analysis.AnalyzerException;

import br.com.ooboo.asm.defuse.DefUseAnalyzer;
import br.com.ooboo.asm.defuse.DefUseChain;
import br.com.ooboo.asm.defuse.Field;
import br.com.ooboo.asm.defuse.Local;
import br.com.ooboo.asm.defuse.Variable;
import br.com.ooboo.asm.defuse.viz.swing.NodeType;

public class CoverageMockModel  {
	private DefUseAnalyzer analyzer = new DefUseAnalyzer();
	private DefUseChain[] duas;
	private Variable[] vars;
	private List<Methods> methods = new ArrayList<Methods>();

	public List<Methods> getMethods(){
		//get class bytecode
		ClassNode classNode = new ClassNode(Opcodes.ASM4);
		ClassReader classReader = null;
		try {
			classReader = new ClassReader(Files.readAllBytes(DataflowHandler.path));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		classReader.accept(classNode, ClassReader.SKIP_FRAMES);
		
		// for each method in the class
		for (int posMethod = 0; posMethod < classNode.methods.size(); posMethod++) {
			methodDuas(classNode, posMethod);
		}

//		IMethod[] methodList = DataFlowMethodView.methods;
//		
//		Methods category = null;
//
//		for(IMethod method:methodList){
//			category = new Methods();
//			category.setName(formatName(method.toString()));
//			methods .add(category);
//			
//		}
		
		//  
		//   	category = new Category();
		//    category.setName("Programming");
		//    categories.add(category);
		//    Todo todo = new Todo("Write more about e4");
		//    category.getTodos().add(todo);
		//    todo = new Todo("Android", "Write a widget.");
		//    category.getTodos().add(todo);
		//    
		//    category = new Category();
		//    category.setName("Leasure");
		//    categories.add(category);
		//    todo = new Todo("Skiing");
		//    category.getTodos().add(todo);
		//    
		//    category = new Category();
		//    category.setName("method_1");
		//    categories.add(category);
		return methods;
	}

//	private String formatName(String method){
//		int finish = method.indexOf(")");
//		method =  (String) method.subSequence(0, finish+1);
//		return method;
//
//	}
	
	
	

	private void methodDuas(ClassNode classNode, int posMethod) {
		
		MethodNode methodNode = classNode.methods.get(posMethod);
		Methods newMethod = new Methods();
		newMethod.setName(methodNode.name);
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
			duas = analyzer.getDefUseChains();
			vars = analyzer.getVariables();
		} catch (final AnalyzerException ignore) {
			duas = new DefUseChain[0];
			vars = new Variable[0];
		}

		for (final DefUseChain dua : duas) {
			final Variable var = vars[dua.var];
			final int def = lines[dua.def];
			final int use = lines[dua.use];
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
			DUA newDua = new DUA(def, use, name);
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