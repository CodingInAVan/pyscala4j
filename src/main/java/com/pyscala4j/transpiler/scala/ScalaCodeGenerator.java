package com.pyscala4j.transpiler.scala;

import com.pyscala4j.transpiler.ir.analyzer.IRAnalyzer;

public class ScalaCodeGenerator {
	private final IRAnalyzer analyzer;
	private final StringBuilder output;
	private int indentationLevel;

	public ScalaCodeGenerator(IRAnalyzer analyzer) {
		this.analyzer = analyzer;
		this.output = new StringBuilder();
		this.indentationLevel = 0;
	}
}
