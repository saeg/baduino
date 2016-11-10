package br.usp.each.saeg.baduino.tree;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class TreeDuaTests {
	
	private static TreeDua dua;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		dua = new TreeDua();
		dua.setCovered(true);
		dua.setDef(12);
		dua.setTarget(26);
		dua.setUse(23);
		dua.setVar("filename");
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
		assertThat(dua.toString(), is("(12, (23, 26), filename)"));
		
		dua.setTarget(0);
		assertThat(dua.toString(), is("(12, 23, filename)"));
	}

}
