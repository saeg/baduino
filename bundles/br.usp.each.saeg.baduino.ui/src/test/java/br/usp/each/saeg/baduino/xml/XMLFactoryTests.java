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
		final URL url = XMLFactoryTests.class.getResource("/xml/coverage_test.xml");
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
		assertThat(classes, is(notNullValue()));
		assertThat(classes, hasSize(7));
		assertThat(classes, containsInAnyOrder(
				new XMLClass("br.usp.each.ach2026.AuthManager"),
				new XMLClass("br.usp.each.ach2026.HtmlGenerator"),
				new XMLClass("br.usp.each.ach2026.PropertiesManager"),
				new XMLClass("br.usp.each.ach2026.response.HttpResponse"),
				new XMLClass("br.usp.each.ach2026.response.HttpResponseFactory"),
				new XMLClass("br.usp.each.ach2026.response.HttpResponseOkFile"),
				new XMLClass("br.usp.each.ach2026.WebServer")
				));
	}
	
	@Test
	public final void testMethods() {
		final List<XMLClass> classes = project.getClasses();
		for (final XMLClass clazz : classes) {
			final List<XMLMethod> methods = clazz.getMethods();
			assertThat(methods, is(notNullValue()));
			assertThat(methods, hasSize(greaterThan(0)));
			
			if (clazz.getName().equals("br.usp.each.ach2026.AuthManager")) {
				assertThat(methods, hasSize(1));
				assertThat(methods.get(0).getName(), is("isLogged"));
			}
			else if (clazz.getName().equals("br.usp.each.ach2026.HtmlGenerator")) {
				assertThat(methods, hasSize(2));
				for (final XMLMethod method : methods) {
					assertThat(method.getName(), isOneOf("readableFileSize", "lambda$0"));
				}
			}
			else if (clazz.getName().equals("br.usp.each.ach2026.PropertiesManager")) {
				assertThat(methods, hasSize(1));
				assertThat(methods.get(0).getName(), is("<init>"));
			}
			else if (clazz.getName().equals("br.usp.each.ach2026.response.HttpResponse")) {
				assertThat(methods, hasSize(1));
				assertThat(methods.get(0).getName(), is("contentType"));
			}
			else if (clazz.getName().equals("br.usp.each.ach2026.response.HttpResponseFactory")) {
				assertThat(methods, hasSize(2));
				for (final XMLMethod method : methods) {
					assertThat(method.getName(), isOneOf("getResponse", "$SWITCH_TABLE$br$usp$each$ach2026$PropertiesManager$ListingDirectories"));
				}
			}
			else if (clazz.getName().equals("br.usp.each.ach2026.response.HttpResponseOkFile")) {
				assertThat(methods, hasSize(1));
				assertThat(methods.get(0).getName(), is("writeBody"));
			}
			else if (clazz.getName().equals("br.usp.each.ach2026.WebServer")) {
				assertThat(methods, hasSize(1));
				assertThat(methods.get(0).getName(), is("main"));
			}
		}

	}

}
