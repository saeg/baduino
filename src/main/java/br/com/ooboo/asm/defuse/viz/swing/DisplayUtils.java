package br.com.ooboo.asm.defuse.viz.swing;

import java.awt.Dimension;
import java.awt.Toolkit;

public class DisplayUtils {

	public final static float SIZE_PROPORTION = .85f;

	public static Dimension getScreenResolution() {
		return Toolkit.getDefaultToolkit().getScreenSize();
	}

	public static Dimension getProportionalDimension() {
		return new Dimension(
				(int) (getScreenResolution().getWidth() * SIZE_PROPORTION),
				(int) (getScreenResolution().getHeight() * SIZE_PROPORTION));
	}

}
