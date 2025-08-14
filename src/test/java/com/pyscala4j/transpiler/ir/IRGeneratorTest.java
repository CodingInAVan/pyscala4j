package com.pyscala4j.transpiler.ir;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class IRGeneratorTest {
	@Test
	public void unwrapStringTest() {
		String text = "abc";
		IRExpr expected = new IRLiteral("b", LiteralType.String);
		assertEquals(expected, IRGenerator.unwrapString(text));
	}

	@Test
	public void unwrapStringTest_happyCase() {
		String text = "\"b\"";
		IRExpr expected = new IRLiteral("b", LiteralType.String);
		assertEquals(expected, IRGenerator.unwrapString(text));
	}

	@Test
	public void unwrapStringTest_empty() {
		String text = "";
		IRExpr expected = new IRLiteral("", LiteralType.String);
		assertEquals(expected, IRGenerator.unwrapString(text));
	}
}
