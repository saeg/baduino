package br.com.ooboo.asm.defuse.viz.commons;

import java.nio.file.Path;
import java.nio.file.PathMatcher;

public class PathMatcherChain implements PathMatcher {

	private final PathMatcher[] matchers;

	public PathMatcherChain(final PathMatcher... matchers) {
		this.matchers = matchers;
	}

	@Override
	public boolean matches(final Path path) {
		for (final PathMatcher matcher : matchers) {
			if (matcher.matches(path)) {
				return true;
			}
		}
		return false;
	}

}
