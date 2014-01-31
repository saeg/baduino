package br.com.ooboo.asm.defuse.viz;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.swing.JFrame;
import javax.swing.JSplitPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.text.BadLocationException;

import br.com.ooboo.asm.defuse.viz.TreeBuilder.DUA;
import br.com.ooboo.asm.defuse.viz.commons.MatcherFileVisitor;
import br.com.ooboo.asm.defuse.viz.commons.PathMatcherChain;
import br.com.ooboo.asm.defuse.viz.commons.PathMatchers;
import br.com.ooboo.asm.defuse.viz.swing.ExplorerPanel;
import br.com.ooboo.asm.defuse.viz.swing.ExplorerTreeNode;
import br.com.ooboo.asm.defuse.viz.swing.NodeType;
import br.com.ooboo.asm.defuse.viz.swing.SourcePanel;

public class SourceViz extends JFrame implements TreeSelectionListener {

	private static final long serialVersionUID = 1L;

	private final PathMatcherChain include = new PathMatcherChain(PathMatchers.get("glob:*.class"));

	private final ExplorerPanel pkgPanel;

	private final SourcePanel srcPanel;

	private Path current;

	public SourceViz(final Project project) throws IOException {
		super("Source-Viz");

		final Path rootPath = project.getRootPath().getFileName();
		final ExplorerTreeNode node = new ExplorerTreeNode(rootPath, NodeType.PROJECT);
		final TreeBuilder visitor = new TreeBuilder(node, project);

		for (final Path path : project.getClassPaths()) {
			Files.walkFileTree(path, new MatcherFileVisitor(include, null, visitor));
		}

		// Setting up package explorer panel
		pkgPanel = new ExplorerPanel(node);
		pkgPanel.addTreeSelectionListener(this);

		// Setting up source panel
		srcPanel = new SourcePanel();

		add(new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, pkgPanel, srcPanel));
	}

	// -----------------------------
	// TreeSelectionListener methods

	@Override
	public void valueChanged(final TreeSelectionEvent e) {
		final ExplorerTreeNode node = pkgPanel.getLastSelectedPathComponent();
		final Path src = node.getSource();
		if (src != null && !src.equals(current)) {
			try {
				srcPanel.setText(new String(Files.readAllBytes(src)));
			} catch (final IOException ignore) {
			}
			current = src;
		}
		srcPanel.clearHighlights();
		if (node.getType() == NodeType.DEFUSE) {
			final DUA dua = (DUA) node.getUserObject();
			try {
				if (dua.def == dua.use) {
					srcPanel.highlightLine(dua.def, SourcePanel.ORANGE_DARK);
				} else {
					srcPanel.highlightLine(dua.def, SourcePanel.ORANGE);
					srcPanel.highlightLine(dua.use, SourcePanel.YELLOW);
				}
			} catch (final BadLocationException ignore) {
			}
		}
	}

}