package com.pyscala4j.transpiler.ir.analyzer;

import com.pyscala4j.transpiler.ir.expr.*;
import com.pyscala4j.transpiler.ir.node.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IRTreeAnalyzer {
	private static final Logger logger = LoggerFactory.getLogger(IRTreeAnalyzer.class);
	private final SymbolTable symbolTable = new SymbolTable();

	public IRTreeAnalyzer() {
	}

	private void visit(IRNode node, IRNode parent) {
		switch(node) {
			case IRExprStmt expr -> analyzeExpression(expr.expr());
			case IRAssignment assignment -> analyzeAssignment(assignment, parent);
			case IRIf ifStmt -> analyzeIfStmt(ifStmt, parent);
			case IRWhile whileStmt -> analyzeWhileStmt(whileStmt, parent);
			default -> {}
		}
	}

	private void analyzeAssignment(IRAssignment assignment, IRNode parent) {
		InferredType type = TypeInference.inferTypeFromAssignment(assignment, symbolTable);
		symbolTable.addVariable(assignment.name(), type);

		logger.debug("Assigned variable '{}' with inferred type '{}' in scope '{}'",
			assignment.name(), type, symbolTable.getCurrentScope().getType());
	}

	private void analyzeIfStmt(IRIf ifStmt, IRNode parent) {

	}

	private void analyzeWhileStmt(IRWhile whileStmt, IRNode parent) {

	}

	private void analyzeExpression(IRExpr expr) {
		if (expr == null) {
			return;
		}

		switch (expr) {
			// For a binary operation, recursively analyze the left and right sides.
			case IRBinaryOp binaryOp -> {
				analyzeExpression(binaryOp.left());
				analyzeExpression(binaryOp.right());
			}

			// For a function call, analyze the function being called and each argument.
			case IRFunctionCall call -> {
				analyzeExpression(call.func());
				for (IRExpr arg: call.args()) {
					analyzeExpression(arg);
				}
			}

			case IRIdentifier identifier -> {
				if (symbolTable.findVariable(identifier.name()).isEmpty()) {
					logger.error("Error: Variable '{}' used before assignment", identifier.name());
				}
			}
			// For a subscription, analyze the object and the index/slice expressions.
			case IRSubscription subscription -> {
				analyzeExpression(subscription.owner());
				subscription.start().ifPresent(this::analyzeExpression);
				subscription.stop().ifPresent(this::analyzeExpression);
				subscription.step().ifPresent(this::analyzeExpression);
			}

			case IRLiteral literal -> {}
			default -> logger.warn("Unhandled expression type: {}", expr.getClass().getSimpleName());

		}
	}
}
