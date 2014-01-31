package br.com.ooboo.asm.defuse.viz.commons;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

public class MatcherFileVisitor implements FileVisitor<Path> {

	private final PathMatcherChain include;

	private final PathMatcherChain exclude;

	private final FileVisitor<Path> visitor;

	public MatcherFileVisitor(
			final PathMatcherChain include, 
			final PathMatcherChain exclude,
			final FileVisitor<Path> visitor) {

		if (include == null) {
			this.include = new PathMatcherChain(PathMatchers.ACCEPT_ALL);
		} else {
			this.include = include;
		}

		if (exclude == null) {
			this.exclude = new PathMatcherChain();
		} else {
			this.exclude = exclude;
		}

		this.visitor = visitor;
	}

	@Override
	public FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes attrs)
			throws IOException {

		final Path name = dir.getFileName();

		if (exclude.matches(name)) {
			return FileVisitResult.SKIP_SUBTREE;
		}

		return visitor.preVisitDirectory(dir, attrs);
	}

	@Override
	public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs)
			throws IOException {

		final Path name = file.getFileName();

		if (include.matches(name) && !exclude.matches(name)) {
			return visitor.visitFile(file, attrs);
		}

		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult visitFileFailed(final Path file, final IOException exc)
			throws IOException {
		return visitor.visitFileFailed(file, exc);
	}

	@Override
	public FileVisitResult postVisitDirectory(final Path dir, final IOException exc)
			throws IOException {
		return visitor.postVisitDirectory(dir, exc);
	}

}
