package br.usp.each.saeg.baduino.xml;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.Arrays;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class XMLMethodTests {
	
	private static XMLMethod method;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		method = new XMLMethod();
		method.setName("ApuraIMC");
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		method = null;
	}
	
	@Test
	public final void testProperties() {
		assertThat(method, hasProperty("name"));
		assertThat(method, hasProperty("duas"));
	}

	@Test
	public final void testGetDuas() {
		final XMLDua dua1 = new XMLDua();
		final XMLDua dua2 = new XMLDua();
		final XMLDua dua3 = new XMLDua();
		
		final List<XMLDua> duas = Arrays.asList(dua1, dua2, dua3);
		method.setDuas(duas);
		
		assertThat(method.getDuas(), hasSize(3));
		assertThat(method.getDuas(), containsInAnyOrder(dua1, dua2, dua3));
	}
	
	@Test
	public final void testToString() {
		assertThat(method.toString(), is("ApuraIMC"));
	}

}
