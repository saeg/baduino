package br.usp.each.saeg.baduino.tree;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.Arrays;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import br.usp.each.saeg.baduino.xml.XMLCounter.XMLCounterType;

public class TreeElementTests {

	private static TreeElement element;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		element = new TreeElement();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		element = null;
	}
	
	@Test
	public final void testProperties() {
		assertThat(element, hasProperty("counters"));
	}

	@Test
	public final void testGetCoverage() {
		final TreeCounter counter1 = new TreeCounter();
		counter1.setType(XMLCounterType.DU);
		counter1.setCovered(70);
		counter1.setMissed(34);
		
		final TreeCounter counter2 = new TreeCounter();
		counter2.setType(XMLCounterType.METHOD);
		counter2.setCovered(4);
		counter2.setMissed(3);
		
		final TreeCounter counter3 = new TreeCounter();
		counter3.setType(XMLCounterType.CLASS);
		counter3.setCovered(3);
		counter3.setMissed(3);
		
		final List<TreeCounter> counters = Arrays.asList(counter1, counter2, counter3);
		element.setCounters(counters);
		assertThat(element.getCoverage(), is("(70/104) (67.31%)"));
		
		counter1.setCovered(0);
		counter1.setMissed(0);
		assertThat(element.getCoverage(), is("No Def-Use Associations"));
	}

}
