package br.com.ooboo.asm.defuse.viz.swing;

import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;

public enum NodeType {

	PROJECT("project.png"),
	SRC_FOLDER("src_folder.png"),
	PACKAGE("package.png"),
	EMPTY_PACKAGE("empty_package.png"),
	JAVA_FILE("java.png"),
	CLASS("class.png"),
	METHOD_DEFAULT("method_default.png"),
	METHOD_PRIVATE("method_private.png"),
	METHOD_PROTECTED("method_protected.png"),
	METHOD_PUBLIC("method_public.png"),
	DEFUSE("defuse.png");

	private String resourceURL;

	private Icon icon;

	private NodeType(final String resourceURL) {
		this.resourceURL = resourceURL;
	}

	public URL getResource() {
		return getClass().getResource(resourceURL);
	}

	public Icon getIcon() {
		if (icon == null) {
			icon = new ImageIcon(getResource());
		}
		return icon;
	}

}
