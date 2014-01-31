package br.com.ooboo.asm.defuse.viz.swing;

import java.nio.file.Path;

import javax.swing.tree.DefaultMutableTreeNode;

public class ExplorerTreeNode extends DefaultMutableTreeNode {

	private static final long serialVersionUID = 1L;

	private final NodeType type;

	private Path source;

	public ExplorerTreeNode(final Object obj, final NodeType type) {
		super(obj);
		this.type = type;
	}

	public NodeType getType() {
		return type;
	}

	public void setSource(final Path source) {
		this.source = source;
	}

	public Path getSource() {
		return source;
	}

}
