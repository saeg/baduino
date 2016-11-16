package br.usp.each.saeg.baduino.core.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class ProjectModelManagerTests {
	
	private static ProjectModel model;
	private static File xml;
	
	@ClassRule
	public static TemporaryFolder folder = new TemporaryFolder();

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		xml = folder.newFile("ach2026.xml");
		
		model = new ProjectModel();
		model.setName("ach2026");
		model.setLocation("/Users/666mario/Documents/workspace/ach2026");
		model.setClassesPath("/Users/666mario/Documents/workspace/ach2026/target/classes");
		model.setBaduinoPath("/Users/666mario/Documents/workspace/ach2026/.baduino");
		model.setClasses(Arrays.asList("br.usp.each.ach2026.AuthManager", "br.usp.each.ach2026.HtmlGenerator", "br.usp.each.ach2026.response.HttpResponse"));
		model.setCoverageBinPath("/Users/666mario/Documents/workspace/ach2026/.baduino/coverage.ser");
		model.setCoverageXmlPath("/Users/666mario/Documents/workspace/ach2026/.baduino/coverage.xml");
		model.setXmlPath(xml.getAbsolutePath());
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		model = null;
	}

	@Test
	public final void testWriteAndRead() throws Exception {
		ProjectModelManager.writeToDisk(model);
		final ProjectModel model2 = ProjectModelManager.readFromDisk(model.getXmlPath());
		assertThat(model2, is(model));
		assertThat(model2.getName(), is("ach2026"));
		assertThat(model2.getClassesPath(), is("/Users/666mario/Documents/workspace/ach2026/target/classes"));
		assertThat(model2.getBaduinoPath(), is("/Users/666mario/Documents/workspace/ach2026/.baduino"));
		assertThat(model2.getCoverageBinPath(), is("/Users/666mario/Documents/workspace/ach2026/.baduino/coverage.ser"));
		assertThat(model2.getCoverageXmlPath(), is("/Users/666mario/Documents/workspace/ach2026/.baduino/coverage.xml"));
		assertThat(model2.getXmlPath(), is(xml.getAbsolutePath()));
		
		final List<String> classes = model2.getClasses();
		assertThat(classes, is(notNullValue()));
		assertThat(classes, hasSize(3));
		assertThat(classes, containsInAnyOrder("br.usp.each.ach2026.AuthManager", "br.usp.each.ach2026.HtmlGenerator", "br.usp.each.ach2026.response.HttpResponse"));
	}

}
