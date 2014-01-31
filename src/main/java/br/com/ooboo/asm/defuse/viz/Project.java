package br.com.ooboo.asm.defuse.viz;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Project {

	private final Path root;

	private final List<Path> classes;

	private final List<Path> sources;

	public Project(final Path root, final List<Path> classes, final List<Path> sources) {

		Objects.requireNonNull(root);
		Objects.requireNonNull(classes);
		Objects.requireNonNull(sources);

		if (!root.isAbsolute()) {
			throw new IllegalArgumentException(String.format(
					"'%s' should be an absolute directory", root));
		}

		if (!Files.isDirectory(root)) {
			throw new IllegalArgumentException(String.format("'%s' should be a directory", root));
		}

		this.root = root;
		this.classes = absolutefy(classes);
		this.sources = absolutefy(sources);
	}

	private List<Path> absolutefy(final List<Path> paths) {
		final List<Path> result = new ArrayList<Path>(paths.size());
		for (Path path : paths) {
			if (path.isAbsolute()) {
				if (!path.startsWith(root)) {
					throw new IllegalArgumentException(String.format(
							"'%s' should be relative to '%s'", path, root));
				}
			} else {
				path = root.resolve(path);
				if (!Files.exists(path)) {
					throw new IllegalArgumentException(String.format(
							"'%s' should be a relative to '%s'", path, root));
				}
			}
			if (!Files.isDirectory(path)) {
				throw new IllegalArgumentException(String.format(
						"'%s' should be a valid directory", path));
			}
			result.add(path);
		}
		return result;
	}

	public Path getRootPath() {
		return root;
	}

	public List<Path> getClassPaths() {
		return Collections.unmodifiableList(classes);
	}

	public List<Path> getSourcePaths() {
		return Collections.unmodifiableList(sources);
	}

}
