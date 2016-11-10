package br.usp.each.saeg.baduino.tree;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.Arrays;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class TreePackageTests {
	
	private static TreePackage pkg;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		pkg = new TreePackage("br.usp.each.ach2026");
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		pkg = null;
	}
	
	@Test
	public final void testProperties() {
		assertThat(pkg, hasProperty("name"));
		assertThat(pkg, hasProperty("classes"));
	}

	@Test
	public final void testGetName() {
		assertThat(pkg.getName(), is("br.usp.each.ach2026"));
	}

	@Test
	public final void testGetClasses() {
		final TreeClass clazz1 = new TreeClass("br.usp.each.ach2026.AuthManager");
		final TreeClass clazz2 = new TreeClass("br.usp.each.ach2026.PropertiesManager");
		final TreeClass clazz3 = new TreeClass("br.usp.each.ach2026.response.HttpResponse");
		
		final List<TreeClass> classes = Arrays.asList(clazz1, clazz2, clazz3);
		pkg.setClasses(classes);
		
		assertThat(pkg.getClasses(), hasSize(2));
		assertThat(pkg.getClasses(), containsInAnyOrder(clazz1, clazz2));
	}

	@Test
	public final void testEqualsObject() {
		final TreePackage pkg2 = new TreePackage("br.usp.each.ach2026");
		final TreePackage pkg3 = new TreePackage("br.usp.each.ach2026.response");
		
		assertThat(pkg, is(pkg2));
		assertThat(pkg, is(not(pkg3)));
	}

	@Test
	public final void testToString() {
		assertThat(pkg.toString(), is("br.usp.each.ach2026"));
	}

}
