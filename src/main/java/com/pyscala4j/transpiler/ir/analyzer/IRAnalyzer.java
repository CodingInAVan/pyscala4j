package com.pyscala4j.transpiler.ir.analyzer;

import com.pyscala4j.transpiler.ir.node.IRNode;

import java.util.Map;

public interface IRAnalyzer<N extends IRNode, T> {
	void analyze(N irNode, IRNode parent);
	Map<N, T> getSymbolTable();
}
