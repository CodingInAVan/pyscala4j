package com.pyscala4j.transpiler.ir.analyzer;

import java.util.HashMap;
import java.util.Map;

public class Scope {
	private final Map<String, VariableInfo> variables = new HashMap<>();
	private final ScopeType type;

	public Scope(ScopeType type) {
		this.type = type;
	}

	public ScopeType getType() {
		return type;
	}

	public Map<String, VariableInfo> getVariables() {
		return variables;
	}
}
