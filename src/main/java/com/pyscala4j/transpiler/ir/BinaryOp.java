package com.pyscala4j.transpiler.ir;

public enum BinaryOp {
	ADD("+"),
	SUB("-"),
	MUL("*"),
	DIV("/"),
	MOD("%"),
	INT_DIV("/", "//"),

	EQ("=="),
	NE("!="),
	LT("<"),
	LE("<="),
	GT(">"),
	GE(">="),

	AND("&&", "and"),
	OR("||", "or"),

	BIT_AND("&"),
	BIT_OR("|"),
	BIT_XOR("^");

	private final String symbol;
	private final String pythonSymbol;

	BinaryOp(String symbol, String pythonSymbol) {
		this.symbol = symbol;
		this.pythonSymbol = pythonSymbol;
	}

	BinaryOp(String symbol) {
		this(symbol, symbol);
	}

	public String getSymbol() {
		return this.symbol;
	}

	public String getPythonSymbol() {
		return this.pythonSymbol;
	}
}
