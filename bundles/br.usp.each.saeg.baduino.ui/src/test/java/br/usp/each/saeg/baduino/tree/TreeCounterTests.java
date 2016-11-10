package br.usp.each.saeg.baduino.tree;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class TreeCounterTests {
	
	private static TreeCounter counter;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		counter = new TreeCounter();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		counter = null;
	}

	@Test
	public final void testGetTotalAndPercentage() {
		counter.setCovered(70);
		counter.setMissed(34);
		assertThat(counter.getTotal(), is(104));
		assertThat(counter.getPercentage(), closeTo(67.30, 0.01));
		
		counter.setCovered(4);
		counter.setMissed(3);
		assertThat(counter.getTotal(), is(7));
		assertThat(counter.getPercentage(), closeTo(57.14, 0.01));
		
		counter.setCovered(3);
		counter.setMissed(3);
		assertThat(counter.getTotal(), is(6));
		assertThat(counter.getPercentage(), closeTo(50.0, 0.01));
	}

}
