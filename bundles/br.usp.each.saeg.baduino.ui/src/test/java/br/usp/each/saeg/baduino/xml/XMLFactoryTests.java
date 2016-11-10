package br.usp.each.saeg.baduino.xml;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.io.File;
import java.net.URL;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class XMLFactoryTests {
	
	private static XMLProject project;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		final URL url = XMLFactoryTests.class.getResource("/xml/coverage.xml");
		final File xml = new File(url.toURI());
		project = XMLFactory.getInstance(xml);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		project = null;
	}
	
	@Test
	public final void testClasses() {
		final List<XMLClass> classes = project.getClasses();
		classes.forEach(System.out::println);
		assertThat(classes, hasSize(6));
	}
	
	@Test
	public final void testMethods() {
//		final List<XMLClass> classes = project.getClasses();
//		classes.forEach(clazz -> {
//			System.out.println("-" + clazz);
//			
//			clazz.getMethods().forEach(System.out::println);
//		});
	}

}
