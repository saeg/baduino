package br.com.ooboo.asm.defuse.viz.swing;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeSelectionModel;

public class ExplorerPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private final JTree tree;

	private final ExplorerTreeNode root;

	public ExplorerPanel(final ExplorerTreeNode root) {
		this.root = root;

		tree = new JTree(root);
		tree.setRootVisible(true);
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.setCellRenderer(new TreeCellRenderer());

		setLayout(new BorderLayout());
		add(new JScrollPane(tree));
	}

	public void addTreeSelectionListener(final TreeSelectionListener tsl) {
		tree.addTreeSelectionListener(tsl);
	}

	public ExplorerTreeNode getLastSelectedPathComponent() {
		return (ExplorerTreeNode) tree.getLastSelectedPathComponent();
	}

	public ExplorerTreeNode getRoot() {
		return root;
	}

}
