package br.com.ooboo.asm.defuse.viz.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.ScrollPaneConstants;
import javax.swing.text.BadLocationException;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

public class SourcePanel extends JPanel {

	private static final long serialVersionUID = 1L;

	public static final Color ORANGE_DARK = new Color(250, 100, 30);

	public static final Color ORANGE = new Color(250, 165, 30);

	public static final Color YELLOW = new Color(250, 245, 30);

	private final List<Object> highlights = new LinkedList<>();

	private final RSyntaxTextArea textArea;

	private final RTextScrollPane scrollPane;

	public SourcePanel() {
		textArea = new RSyntaxTextArea();
		textArea.setEditable(false);
		textArea.setHighlightCurrentLine(false);
		textArea.setAutoscrolls(true);
		textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);

		scrollPane = new RTextScrollPane(textArea, true);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setIconRowHeaderEnabled(true);

		setLayout(new BorderLayout());
		add(scrollPane);
	}

	public void setText(final String string) {
		textArea.setText(string);
		textArea.setCaretPosition(0);
	}

	public void highlightLine(final int line, final Color color) throws BadLocationException {
		highlights.add(textArea.addLineHighlight(line - 1, color));
	}

	public void clearHighlights() {
		for (final Object o : highlights) {
			textArea.removeLineHighlight(o);
		}
		highlights.clear();
	}

}
