import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;

import br.com.ooboo.asm.defuse.viz.Project;
import br.com.ooboo.asm.defuse.viz.SourceViz;
import br.com.ooboo.asm.defuse.viz.swing.DisplayUtils;


public class toolbarHandler extends AbstractHandler {

	public Object execute(ExecutionEvent event) throws ExecutionException {
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
