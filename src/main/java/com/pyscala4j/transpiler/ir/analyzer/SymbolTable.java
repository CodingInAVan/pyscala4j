package com.pyscala4j.transpiler.ir.analyzer;

import java.util.Optional;
import java.util.Stack;

public class SymbolTable {
	private final Stack<Scope> scopes = new Stack<>();

	public SymbolTable() {
		enterScope(ScopeType.Global);
	}

	public void enterScope(ScopeType type) {
		scopes.push(new Scope(type));
	}

	public void exitScope() {
		if (!scopes.isEmpty()) {
			scopes.pop();
		}
	}

	public void addVariable(String name, InferredType type) {
		Scope currentScope = scopes.peek();
		VariableInfo info = new VariableInfo(
			name,
			type,
			currentScope.getType()
		);
		currentScope.getVariables().put(name, info);
	}

	public Optional<VariableInfo> findVariable(String name) {
		for (int i = scopes.size() - 1; i >= 0; i--) {
			Scope scope = scopes.get(i);
			if (scope.getVariables().containsKey(name)) {
				return Optional.of(scope.getVariables().get(name));
			}
		}
		return Optional.empty();
	}
}
