package br.usp.each.saeg.baduino.contentViews;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
import org.objectweb.asm.tree.MethodNode;

import br.usp.each.saeg.baduino.handlers.VisualizationHandler;
import br.usp.each.saeg.baduino.tree.TreeBuilder;
import br.usp.each.saeg.baduino.tree.TreeClass;
import br.usp.each.saeg.baduino.tree.TreeMethod;
import br.usp.each.saeg.baduino.tree.TreePackage;
import br.usp.each.saeg.baduino.tree.TreeProject;
import br.usp.each.saeg.baduino.xml.XMLFactory;
import br.usp.each.saeg.baduino.xml.XMLProject;

/**
 * 
 * @author Mario Concilio
 *
 */
public class CoverageInput {
	
	private static final Logger logger = Logger.getLogger(CoverageInput.class);

	private TreeProject project;
	
	public CoverageInput() {
		XMLProject xml = XMLFactory.getInstance();
		this.project = TreeBuilder.build(xml);
		
		/*
		final List<TreePackage> packages = this.project.getPackages();
		for (final TreePackage pkg : packages) {
			final List<TreeClass> classes = pkg.getClasses();
			logger.debug("tree package: " + pkg);
			
			
			for (final TreeClass clazz : classes) {
				logger.debug("tree class: " + clazz);
				
				final List<TreeMethod> methods = clazz.getMethods();
				for (final TreeMethod method : methods) {
					logger.debug("tree method: " + method);
					
					final List<TreeDua> duas = method.getDuas();
					for (final TreeDua dua :  duas) {
						logger.debug("tree dua: " + dua);
					}
				}
			}
		}
		*/
	}
	
	public List<?> getTree() throws JavaModelException, IOException {
		if (VisualizationHandler.getType() instanceof IJavaProject) {
			logger.debug("type IJavaProject");
			return getProjectNode((IJavaProject) VisualizationHandler.getType());
		}
		
		if (VisualizationHandler.getType() instanceof IPackageFragment) {
			logger.debug("type IPackageFragment");
			return getPackageNode((IPackageFragment) VisualizationHandler.getType());
		}
		
		if (VisualizationHandler.getType() instanceof ICompilationUnit) {
			logger.debug("type ICompilationUnit");
			return getClassNode((ICompilationUnit) VisualizationHandler.getType());
		}

		return null;
	}
	
	public List<TreeProject> getProjectNode(final IJavaProject javaProject) throws JavaModelException, IOException {
		this.project.setName(javaProject.getElementName());
		logger.debug("project: " + javaProject.getElementName());
		
		final IJavaElement[] children = javaProject.getChildren();
		for (IJavaElement element : children) {
			if (element instanceof IPackageFragmentRoot) {
				final IPackageFragmentRoot folder = (IPackageFragmentRoot) element;
				
				if (!folder.isArchive()) {
					final IJavaElement[] folderChildren = folder.getChildren();
					
					for (final IJavaElement javaElement : folderChildren) {
						if (javaElement instanceof IPackageFragment) {
							final IPackageFragment pkg = (IPackageFragment) javaElement;
							getPackageNode(pkg, this.project);
						}
					}
				}
			}
		}

		return Arrays.asList(project);
	}
	
	public List<TreePackage> getPackageNode(final IPackageFragment pkg) throws JavaModelException, IOException {
		return getPackageNode(pkg, this.project);
	}
	
	public List<TreePackage> getPackageNode(final IPackageFragment pkg, final TreeProject project) throws JavaModelException, IOException {
		final String packageName = pkg.getElementName();
		
		// find the correct package in TreeProject
		final TreePackage treePackage = project.getPackages()
				.stream()
				.filter(p -> p.getName().equals(packageName))
				.findFirst()
				.orElse(null);
		
		if (treePackage != null) {
			logger.debug("package fragment: " + packageName + ", tree package: " + treePackage);

			final IJavaElement[] children = pkg.getChildren();
			if (children.length > 0) {
				for (final IJavaElement child : children) {
					if (child instanceof ICompilationUnit) {
						final ICompilationUnit unit = (ICompilationUnit) child;
						getClassNode(unit, treePackage);
					}
				}
			}
		}
		
		return project.getPackages();
	}
	
	public List<TreeClass> getClassNode(final ICompilationUnit unit) throws JavaModelException, IOException {
		//TODO: fetch na primeira class, pegar seu nome e procurar qual pacote
		final LinkedList<ClassNode> classNodes = getASMClassNode(unit);
		final int index = classNodes.getFirst().name.lastIndexOf("/");
		final String name = classNodes.getFirst().name.substring(0, index).replaceAll("/", ".");
		
		
		return getClassNode(unit, null);
	}
	
	public List<TreeClass> getClassNode(final ICompilationUnit unit, final TreePackage pkg) throws JavaModelException, IOException {
		final LinkedList<ClassNode> classNodes = getASMClassNode(unit);
		for (final ClassNode classNode : classNodes) {
			final String className = classNode.name.replaceAll("/", ".");
			final TreeClass treeClass = pkg.getClasses()
					.stream()
					.filter(clazz -> clazz.getName().equals(className))
					.findFirst()
					.orElse(null);
			
			if (treeClass != null && !className.contains("Test")) {
				logger.debug("class node: " + className + ", tree class: " + treeClass);
				classMethods(classNode, treeClass, unit);
			}
		}
		
		return pkg.getClasses();
	}
	
	private void classMethods(final ClassNode classNode, final TreeClass treeClass, final ICompilationUnit unit) {
		final List<MethodNode> methodNodes = classNode.methods;
		
		for (final MethodNode methodNode : methodNodes) {
			// Does not instrument:
			// 1. Abstract methods
			if ((methodNode.access & Opcodes.ACC_ABSTRACT) != 0)
				break;
			
			// 2. Interfaces
			if((methodNode.access & Opcodes.ACC_INTERFACE) != 0)
				break;
			
			// 3. Synthetic methods
			if((methodNode.access & Opcodes.ACC_SYNTHETIC) != 0)
				break;
			
			// 4. Static class initialization
			if (methodNode.name.equals("<clinit>"))
				break;
			
			final String methodName = methodNode.name;
			
			// find the correct method in TreeClass
			final TreeMethod treeMethod = treeClass.getMethods()
					.stream()
					.filter(m -> m.getName().equals(methodName))
					.findFirst()
					.orElse(null);
			
			logger.debug("method node: " + methodName + ", tree method: " + treeMethod);
			
			if (treeMethod != null) {
				treeMethod.setName(getMethodName(methodNode, treeClass));
				treeMethod.setAccess(getMethodAccess(methodNode));
				treeMethod.getDuas().forEach(dua -> dua.setCompilationUnit(unit));
			}
		}
	}
	
	private String getMethodName(final MethodNode methodNode, final TreeClass treeClass) {
		final String className = treeClass.getName();
		String name = null;
		
		if (methodNode.name.equals("<clinit>")) {
			name = Signature.toString(methodNode.desc, className, null, false, false);
		}
		else if (methodNode.name.equals("<init>")) {
			name = Signature.toString(methodNode.desc, className, null, false, false);
		}
		else {
			name = Signature.toString(methodNode.desc, methodNode.name, null, false, true);
			if ((methodNode.access & Opcodes.ACC_STATIC) != 0) {
				name = "static " + name;
			}
		}
		
		return name;
	}
	
	private int getMethodAccess(final MethodNode methodNode) {
		int access = -1;
		
		if ((methodNode.access & Opcodes.ACC_PUBLIC) != 0) {
			access = Opcodes.ACC_PUBLIC;
		}
		else if ((methodNode.access & Opcodes.ACC_PROTECTED) != 0) {
			access = Opcodes.ACC_PROTECTED;
		}
		else if ((methodNode.access & Opcodes.ACC_PRIVATE) != 0) {
			access = Opcodes.ACC_PRIVATE;
		}
		
		return access;
	}
	
	/**
	 * Transform class bytes in ClassNode form from ASM
	 * @param unit
	 * @return
	 * @throws JavaModelException
	 * @throws IOException
	 */
	private LinkedList<ClassNode> getASMClassNode(final ICompilationUnit unit) throws JavaModelException, IOException {
		final LinkedList<ClassNode> classNodes = new LinkedList<>();
		final LinkedList<Path> paths = getClasspath(unit); // path for the inner class and mainly class

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
		
		return classNodes;
	} 
	
	private LinkedList<Path> getClasspath(final ICompilationUnit unit) throws JavaModelException, IOException {
		final IRegion region = JavaCore.newRegion();
		region.add(unit);
		
		final IResource[] resources = JavaCore.getGeneratedResources(region, false);
		final LinkedList<Path> path = new LinkedList<Path>();
		for (final IResource resource : resources) {
			path.add(Paths.get(resource.getLocation().toFile().toURI()));
		}
		
		return path;
	}

}

