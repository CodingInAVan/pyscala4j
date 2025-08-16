package com.pyscala4j.transpiler.ir;

import com.pyscala4j.transpiler.ir.expr.BinaryOp;
import com.pyscala4j.transpiler.ir.expr.IRExpr;

import java.util.Optional;

public sealed interface IRNode
	permits IRAssignment, IRAugAssignment, IRBlock, IRElseBranchNode,
	IRExprStmt, IRFor, IRIf, IRReturn, IRWhile {

	static IRNode irIfElse(IRExpr cond, IRBlock thenBranch, IRElseBranchNode elseBranch) {
		return new IRIf(cond, thenBranch, Optional.of(elseBranch));
	}
	static IRNode irIf(IRExpr cond, IRBlock thenBranch) {
		return new IRIf(cond, thenBranch, Optional.empty());
	}

	static IRNode irAugAssignment(String name, BinaryOp op, IRExpr value) {
		return new IRAugAssignment(name, op, value);
	}

	static IRNode irAssignment(String name, IRExpr value) {
		return new IRAssignment(name, value);
	}

	static IRNode irFor(IRExpr stmt, IRBlock body) {
		return new IRFor(stmt, body);
	}

	static IRNode irReturn(IRExpr value) {
		return new IRReturn(value);
	}

	static IRNode irWhile(IRExpr cond, IRBlock body) {
		return new IRWhile(cond, body);
	}

	static IRNode IRExprStmt(IRExpr expr) {
		return new IRExprStmt(expr);
	}
}

record IRIf(IRExpr cond, IRBlock thenBranch, Optional<IRElseBranchNode> elseBranch) implements IRNode, IRElseBranchNode {}
record IRAugAssignment(String name, BinaryOp op, IRExpr value) implements IRNode {}
record IRAssignment(String name, IRExpr value) implements IRNode {}
record IRFor(IRExpr stmt, IRBlock body) implements IRNode {}
record IRReturn(IRExpr value) implements IRNode {}
record IRWhile(IRExpr cond, IRBlock body) implements IRNode {}

record IRExprStmt(IRExpr expr) implements IRNode {}

