package com.pyscala4j.transpiler.ir.analyzer;

import com.pyscala4j.transpiler.ir.node.IRAssignment;
import com.pyscala4j.transpiler.ir.node.IRIf;
import com.pyscala4j.transpiler.ir.node.IRNode;

public class IRTreeAnalyzer {
	private final SymbolTable symbolTable = new SymbolTable();

	public IRTreeAnalyzer() {
	}

	private void visit(IRNode node, IRNode parent) {
		switch(node) {
			case IRAssignment assignment -> analyzeAssignment(assignment, parent);
			case IRIf ifStmt -> analyzeIfStmt(ifStmt, parent);
			default -> {}
		}
	}

	private void analyzeAssignment(IRAssignment assignment, IRNode parent) {

		symbolTable.addVariable(assignment.name(), TypeInference.inferTypeFromAssignment(assignment, symbolTable));
	}

	private void analyzeIfStmt(IRIf ifStmt, IRNode parent) {

	}
}
