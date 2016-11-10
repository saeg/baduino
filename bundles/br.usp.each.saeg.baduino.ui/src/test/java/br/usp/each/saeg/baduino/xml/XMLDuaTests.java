package br.usp.each.saeg.baduino.xml;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class XMLDuaTests {
	
	private static XMLDua dua;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		dua = new XMLDua();
		dua.setCovered(true);
		dua.setDef(9);
		dua.setUse(10);
		dua.setVar("nIdade");
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		dua = null;
	}
	
	@Test
	public final void testProperties() {
		assertThat(dua, hasProperty("covered"));
		assertThat(dua, hasProperty("def"));
		assertThat(dua, hasProperty("target"));
		assertThat(dua, hasProperty("use"));
		assertThat(dua, hasProperty("var"));
	}

	@Test
	public final void testToString() {
		dua.setTarget(11);
		assertThat(dua.toString(), is("(9, (10, 11), nIdade)"));
		
		dua.setTarget(0);
		assertThat(dua.toString(), is("(9, 10, nIdade)"));
	}

}
