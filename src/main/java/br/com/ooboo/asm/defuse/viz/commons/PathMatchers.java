package br.com.ooboo.asm.defuse.viz.commons;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.PathMatcher;

public class PathMatchers {

	public static final PathMatcher ACCEPT_ALL = new PathMatcher() {
		@Override
		public boolean matches(final Path path) {
			return true;
		}
	};

	public static PathMatcher get(final String syntax) {
		return FileSystems.getDefault().getPathMatcher(syntax);
	}

}
