package com.pyscala4j.transpiler.ir.analyzer;

public class VariableInfo {
	private final String name;
	private InferredType inferredType;
	private ScopeType scope;
	private boolean isDefinedOnAllPaths = true;
	
	public VariableInfo(String name, InferredType inferredType, ScopeType scope) {
		this.name = name;
		this.inferredType = inferredType;
		this.scope = scope;
	}

	/**
	 * Updates the type of the variable. If the new type is different
	 * it promotes the type to ANY.
	 */
	public void updateType(InferredType newType) {
		if (this.inferredType != newType) {
			this.inferredType = newType;
		} else {
			this.inferredType = InferredType.ANY;
		}
	}

	public String getName() {
		return name;
	}

	public InferredType getInferredType() {
		return inferredType;
	}

	public ScopeType getScope() {
		return scope;
	}

	public boolean isDefinedOnAllPaths() {
		return isDefinedOnAllPaths;
	}
}