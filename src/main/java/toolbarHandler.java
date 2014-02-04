import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IClassFile;
import org.eclipse.jdt.internal.core.CompilationUnit;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeSelection;

import br.com.ooboo.asm.defuse.viz.Project;
import br.com.ooboo.asm.defuse.viz.SourceViz;
import br.com.ooboo.asm.defuse.viz.swing.DisplayUtils;

import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

@SuppressWarnings("restriction")
public class toolbarHandler extends AbstractHandler {

	public Object execute(ExecutionEvent event) throws ExecutionException {
//		IWorkbenchWindow window =
//				PlatformUI.getWorkbench().getActiveWorkbenchWindow();
//		ISelection selection = window.getSelectionService().getSelection("org.eclipse.jdt.ui.PackageExplorer");
//
//		
//		//selection.getClass().getMethods();
//		//System.out.println(selection.getClass().getName());
//		IStructuredSelection st = (IStructuredSelection) selection; 
////		System.out.println(st.getFirstElement().getClass().getName());;
////		CompilationUnit file = (CompilationUnit) st.getFirstElement();
////		IClassFile a = file.getClassFile();
////		IPath b = a.getPath();
////		System.out.println(b.toPortableString());
//		Method[] methods = selection.getClass().getMethods();
////		Method[] methods = a.getClass().getDeclaredMethods();
//		for(int i = 0; i< methods.length;i++){
////			System.out.println(methods[i]);
//		}


		final Path root = Paths.get("/home/louiz/coverage/TestInSS");
		final List<Path> classPaths = Collections.singletonList(Paths.get("/home/louiz/coverage/TestInSS/build/java"));
		final List<Path> sourcePaths = Collections.singletonList(Paths.get("/home/louiz/coverage/TestInSS/src/java"));
		final Project project = new Project(root, classPaths, sourcePaths);

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					final SourceViz frame = new SourceViz(project);
					frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					frame.setSize(DisplayUtils.getProportionalDimension());
					frame.setLocationRelativeTo(null);
					frame.setVisible(true);
				} catch (final IOException e) {
					e.printStackTrace();
				}
			}
		});
		return null;
	}

}
