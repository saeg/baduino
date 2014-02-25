package br.usp.each.saeg.badua.handlers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFileState;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IPathVariableManager;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceProxy;
import org.eclipse.core.resources.IResourceProxyVisitor;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourceAttributes;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.core.runtime.content.IContentDescription;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.jdt.core.IClassFile;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IRegion;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.SourceRange;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Initializer;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.internal.core.PackageFragment;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import br.usp.each.saeg.badua.views.*;


public class DataflowHandler extends AbstractHandler {
	private static Path path;
	private static ICompilationUnit cu;
	private static IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		try {

			ISelection sel = HandlerUtil.getActiveMenuSelection(event);
			IStructuredSelection selection = (IStructuredSelection) sel;

			cu = (ICompilationUnit) selection.getFirstElement();
			IJavaElement Package = cu.getParent();
			IJavaElement Dir = Package.getParent();
			IJavaElement Project = Dir.getParent();
			System.out.println(Project.getElementName() + " " + Dir.getElementName() + " " + Package.getElementName());

			getClassPath();

			openView();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("null")
	private void getClassPath() throws JavaModelException, IOException {
		IRegion region = JavaCore.newRegion();
		region.add(cu.getPrimary());
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
				ASTParser p = ASTParser.newParser(AST.JLS4);
				p.setSource(cu);
				p.setKind(ASTParser.K_COMPILATION_UNIT);
				
				CompilationUnit cu = (CompilationUnit)p.createAST(null);
				cu.accept(new ASTVisitor() {
					public boolean visit(MethodDeclaration node){
						SimpleName n = node.getName();
						System.out.println(n.getFullyQualifiedName());
						return false;
						
					}
					public boolean visit(Initializer node){
						int modifiers = node.getModifiers();
						if(Modifier.isStatic(modifiers)){
							System.out.println("tem modifier static");
						}else{
							System.out.println("tem modifier");
						}
						
						return false;
						
					}
				});




		path =  Paths.get(ipath.toFile().toURI());

		//byte[] buffer2 = Files.readAllBytes(getPath());

	}

	public static Path getPath() {
		return path;
	}

	public static ICompilationUnit getCu() {
		return cu;
	}


	public static IWorkbenchPage getPage() {
		return page;
	}

	private void openView() throws PartInitException {
		DataFlowMethodView.closeViews();
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(DataFlowMethodView.ID);
	}


}


