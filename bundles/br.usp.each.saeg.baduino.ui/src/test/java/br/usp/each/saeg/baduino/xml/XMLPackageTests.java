package br.usp.each.saeg.baduino.xml;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.Arrays;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class XMLPackageTests {

	private static XMLPackage pkg;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		pkg = new XMLPackage();
		pkg.setName("imc/usp/IMC");
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
		assertThat(pkg.getName(), is("imc.usp"));
	}

	@Test
	public final void testGetClasses() {
		final XMLClass clazz1 = new XMLClass();
		clazz1.setName("imc/usp/IMC1");
		
		final XMLClass clazz2 = new XMLClass();
		clazz2.setName("imc/usp/IMC2");
		
		final XMLClass clazz3 = new XMLClass();
		clazz3.setName("imc/usp/calc/Calc");
		
		final List<XMLClass> classes = Arrays.asList(clazz1, clazz2, clazz3);
		pkg.setClasses(classes);
		
		assertThat(pkg.getClasses(), hasSize(3));
		assertThat(pkg.getClasses(), containsInAnyOrder(clazz1, clazz2, clazz3));
	}

	@Test
	public final void testToString() {
		assertThat(pkg.toString(), is("imc.usp"));
	}

}
