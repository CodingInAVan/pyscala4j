package com.pyscala4j.transpiler.ir.analyzer;

public record VariableInfo(String name, InferredType inferredType, ScopeType scope) {}
