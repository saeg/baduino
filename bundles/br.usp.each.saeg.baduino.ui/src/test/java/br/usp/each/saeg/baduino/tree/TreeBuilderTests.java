package br.usp.each.saeg.baduino.tree;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.io.File;
import java.net.URL;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import br.usp.each.saeg.baduino.xml.XMLClass;
import br.usp.each.saeg.baduino.xml.XMLFactory;
import br.usp.each.saeg.baduino.xml.XMLFactoryTests;
import br.usp.each.saeg.baduino.xml.XMLProject;

/**
 * 
 * @author Mario Concilio
 *
 */
public class TreeBuilderTests {
	
	private static XMLProject xmlProject;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		final URL url = XMLFactoryTests.class.getResource("/xml/coverage.xml");
		final File xml = new File(url.toURI());
		xmlProject = XMLFactory.getInstance(xml);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		xmlProject = null;
	}

	@Test
	public final void testBuild() {
		final TreeProject project = TreeBuilder.build(xmlProject);
		assertThat(project, is(notNullValue()));
		
		final TreePackage defaultPackage = new TreePackage("br.usp.each.ach2026");
		final TreePackage responsePackage = new TreePackage("br.usp.each.ach2026.response");
		
		final List<TreePackage> packages = project.getPackages();
		assertThat(packages, is(notNullValue()));
		assertThat(packages, hasSize(2));
		assertThat(packages, containsInAnyOrder(defaultPackage, responsePackage));
		
		for (final TreePackage pkg : packages) {
			assertThat(pkg, is(notNullValue()));
			System.out.println(pkg + " - covered: " + pkg.getCovered() + " total: " + pkg.getTotal() + " percentage: " + pkg.getPercentage());
			
			final List<TreeClass> classes = pkg.getClasses();
			assertThat(classes, is(notNullValue()));
			assertThat(classes, hasSize(greaterThan(0)));
			
			for (final TreeClass clazz : classes) {
				System.out.println("\t" + clazz.getSimpleName());
				
				final List<TreeMethod> methods = clazz.getMethods();
				for (final TreeMethod method : methods) {
					System.out.println("\t\t" + method);
					
					final List<TreeDua> duas = method.getDuas();
					for (final TreeDua dua : duas) {
						System.out.println("\t\t\t" + dua);
					}
					
					method.getCounters().forEach(counter -> System.out.println("\t\t" + counter));
					System.out.println();
				}
				
				clazz.getCounters().forEach(counter -> System.out.println("\t" + counter));
				System.out.println();
			}
		}
	}
	
	@Test
	public final void testBuildPackage() {
		final List<XMLClass> clazz = xmlProject.getClasses();
//		final TreePackage defaultPackage = TreeBuilder.buildPackage(xmlClass);
	}

}
