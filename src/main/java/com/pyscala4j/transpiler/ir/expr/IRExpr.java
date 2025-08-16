package com.pyscala4j.transpiler.ir.expr;

import java.util.List;

public sealed interface IRExpr permits IRAttributeAccess, IRBinaryOp, IRFunctionCall, IRIdentifier, IRLiteral, IRSubscription, IRUnaryOp {

	/**
	 * Public factory to create an instance of the non-public IRIdentifier.
	 */
	static IRExpr identifier(String name) {
		return new IRIdentifier(name);
	}

	static IRExpr literal(String value, LiteralType literalType) {
		return new IRLiteral(value, literalType);
	}

	static IRExpr functionCall(IRExpr function, List<IRExpr> args) {
		return new IRFunctionCall(function, args);
	}

	static IRExpr binaryOp(IRExpr left, BinaryOp op, IRExpr right) {
		return new IRBinaryOp(left, op, right);
	}

	static IRExpr unaryOp(UnaryOp op, IRExpr right) {
		return new IRUnaryOp(op, right);
	}

	static IRExpr attributeAccess(IRExpr expr, String attributeName) {
		return new IRAttributeAccess(expr, attributeName);
	}
}

