package com.pyscala4j.transpiler.ir.analyzer;

public enum InferredType {
	INTEGER("Int"),
	FLOAT("Float"),
	DOUBLE("Double"),
	STRING("String"),
	BOOLEAN("Boolean"),
	LIST("List"),
	DICTIONARY("Map"),
	NULL("Null"),
	ANY("Any"),
	UNKNOWN("Unknown");

	private final String displayName;

	InferredType(String displayName) {
		this.displayName = displayName;
	}

	public String getDisplayName() {
		return displayName;
	}

	@Override
	public String toString() {
		return displayName;
	}
}
