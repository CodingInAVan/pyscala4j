package com.pyscala4j.transpiler.ir;

import java.util.Optional;

public enum UnaryOp {
	Not("!", "not"),
	Minus("-"),
	Plus("+"),
	BitNot("~")
	;

	private final String symbol;
	private final String pythonSymbol;

	UnaryOp(String symbol, String pythonSymbol) {
		this.symbol = symbol;
		this.pythonSymbol = pythonSymbol;
	}

	UnaryOp(String symbol) {
		this(symbol, symbol);
	}

	public String getSymbol() {
		return this.symbol;
	}

	public String getPythonSymbol() {
		return this.pythonSymbol;
	}

	public Optional<UnaryOp> fromPython(String pythonOp) {
		switch (pythonOp) {
			case "not" -> {
				return Optional.of(Not);
			}
			case "-" -> {
				return Optional.of(Minus);
			}
			case "+" -> {
				return Optional.of(Plus);
			}
			case "~" -> {
				return Optional.of(BitNot);
			}
			default -> {
				return Optional.empty();
			}
		}
	}
}
