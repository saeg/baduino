package br.usp.each.saeg.baduino.xml;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.Arrays;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class XMLProjectTests {
	
	private static XMLProject project;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		project = new XMLProject();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		project = null;
	}
	
	@Test
	public final void testProperties() {
		assertThat(project, hasProperty("classes"));
		assertThat(project, hasProperty("counters"));
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
		project.setClasses(classes);
		
		assertThat(project.getClasses(), hasSize(3));
		assertThat(project.getClasses(), containsInAnyOrder(clazz1, clazz2, clazz3));
	}
	
//	@Test
//	public final void testGetPackages() {
//		final XMLPackage pkg1 = new XMLPackage();
//		pkg1.setName("imc/usp/IMC1");
//		
//		final XMLPackage pkg2 = new XMLPackage();
//		pkg2.setName("imc/usp/IMC2");
//		
//		final XMLPackage pkg3 = new XMLPackage();
//		pkg3.setName("imc/usp/calc/Calc");
//		
//		final List<XMLPackage> packages = Arrays.asList(pkg1, pkg2, pkg3);
//		project.setPackages(packages);
//		
//		assertThat(project.getPackages(), hasSize(3));
//		assertThat(project.getPackages(), containsInAnyOrder(pkg1, pkg2, pkg3));
//	}

}
