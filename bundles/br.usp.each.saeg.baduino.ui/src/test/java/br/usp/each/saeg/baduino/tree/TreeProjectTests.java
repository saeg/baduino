package br.usp.each.saeg.baduino.tree;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.Arrays;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class TreeProjectTests {
	
	private static TreeProject project;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		project = new TreeProject();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		project = null;
	}
	
	@Test
	public final void testProperties() {
		assertThat(project, hasProperty("packages"));
		assertThat(project, hasProperty("counters"));
	}

	@Test
	public final void testGetPackages() {
		final TreePackage pkg1 = new TreePackage("br.usp.each.ach2026");
		final TreePackage pkg2 = new TreePackage("br.usp.each.ach2026.response");
		final TreePackage pkg3 = new TreePackage("br.usp.each.ach2026");
		
		final List<TreePackage> packages = Arrays.asList(pkg1, pkg2, pkg3);
		project.setPackages(packages);
		
		assertThat(project.getPackages(), hasSize(2));
		assertThat(project.getPackages(), containsInAnyOrder(pkg1, pkg2));
	}

}
