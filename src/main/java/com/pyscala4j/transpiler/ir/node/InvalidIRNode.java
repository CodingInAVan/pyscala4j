package com.pyscala4j.transpiler.ir.node;

public record InvalidIRNode(
	String message,
	String originalText,
	int line,
	int column
) implements IRNode {}
