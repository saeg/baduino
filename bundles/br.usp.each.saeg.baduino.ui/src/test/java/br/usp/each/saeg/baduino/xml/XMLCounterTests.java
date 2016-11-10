package br.usp.each.saeg.baduino.xml;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class XMLCounterTests {
	
	private static XMLCounter counter;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		counter = new XMLCounter();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		counter = null;
	}
	
	@Test
	public final void testProperties() {
		assertThat(counter, hasProperty("covered"));
		assertThat(counter, hasProperty("missed"));
		assertThat(counter, hasProperty("type"));
	}

	@Test
	public final void testGetTotal() {
		counter.setCovered(3);
		counter.setMissed(5);
		assertThat(counter.getTotal(), is(8));
		
		counter.setCovered(0);
		counter.setMissed(16);
		assertThat(counter.getTotal(), is(16));
		
		counter.setCovered(32);
		counter.setMissed(0);
		assertThat(counter.getTotal(), is(32));
	}

	@Test
	public final void testGetPercentage() {
		counter.setCovered(3);
		counter.setMissed(5);
		assertThat(counter.getPercentage(), closeTo(37.5, 0.01));
		
		counter.setCovered(0);
		counter.setMissed(16);
		assertThat(counter.getPercentage(), closeTo(0.0, 0.01));
		
		counter.setCovered(32);
		counter.setMissed(0);
		assertThat(counter.getPercentage(), closeTo(100.0, 0.01));
	}

}
