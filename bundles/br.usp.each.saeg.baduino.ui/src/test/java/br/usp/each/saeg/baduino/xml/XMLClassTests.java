package br.usp.each.saeg.baduino.xml;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.Arrays;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class XMLClassTests {
	
	private static XMLClass clazz;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		clazz = new XMLClass();
		clazz.setName("imc/usp/IMC");
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		clazz = null;
	}
	
	@Test
	public final void testProperties() {
		assertThat(clazz, hasProperty("name"));
		assertThat(clazz, hasProperty("methods"));
	}

	@Test
	public final void testGetName() {
		assertThat(clazz.getName(), is("imc.usp.IMC"));
	}

	@Test
	public final void testGetMethods() {
		final XMLMethod method1 = new XMLMethod();
		final XMLMethod method2 = new XMLMethod();
		final XMLMethod method3 = new XMLMethod();
		
		List<XMLMethod> methods = Arrays.asList(method1, method2, method3);
		clazz.setMethods(methods);
		
		assertThat(clazz.getMethods(), hasSize(3));
		assertThat(clazz.getMethods(), containsInAnyOrder(method1, method2, method3));
	}

}
