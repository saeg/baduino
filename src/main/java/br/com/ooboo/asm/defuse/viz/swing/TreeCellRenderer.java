package br.com.ooboo.asm.defuse.viz.swing;

import java.awt.Component;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

public class TreeCellRenderer extends DefaultTreeCellRenderer {

	private static final long serialVersionUID = 1L;

	@Override
	public Component getTreeCellRendererComponent(final JTree tree, final Object value,
			final boolean sel, final boolean expanded, final boolean leaf, final int row,
			final boolean hasFocus) {

		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
		setIcon(((ExplorerTreeNode) value).getType().getIcon());
		return this;
	}

}
