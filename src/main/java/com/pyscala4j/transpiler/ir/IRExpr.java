package com.pyscala4j.transpiler.ir;

import java.util.List;
import java.util.Optional;

public sealed interface IRExpr permits IRAttributeAccess, IRBinaryOp, IRCall, IRIdentifier, IRLiteral, IRSubscription, IRUnaryOp {

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
		return new IRCall(function, args);
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

	static IRExpr subscription(IRExpr owner) {
		return new IRSubscription(owner, Optional.empty(), Optional.empty(), Optional.empty());
	}

	static IRExpr subscription(IRExpr owner, IRExpr start) {
		return new IRSubscription(owner, Optional.of(start), Optional.empty(), Optional.empty());
	}

	static IRExpr subscription(IRExpr owner, IRExpr start, IRExpr stop) {
		return new IRSubscription(owner, Optional.of(start), Optional.of(stop), Optional.empty());
	}

	static IRExpr subscription(IRExpr owner, IRExpr start, IRExpr stop, IRExpr step) {
		return new IRSubscription(owner, Optional.of(start), Optional.of(stop), Optional.of(step));
	}
}

record IRIdentifier(String name) implements IRExpr {}
record IRCall(IRExpr func, List<IRExpr> args) implements IRExpr {}
record IRBinaryOp(IRExpr left, BinaryOp op, IRExpr right) implements IRExpr {}
record IRLiteral(String value, LiteralType literalType) implements IRExpr {}
record IRUnaryOp(UnaryOp op, IRExpr expr) implements IRExpr {}
record IRAttributeAccess(IRExpr owner, String attributeName) implements IRExpr {}

/**
 * Represents a subscription or slicing operation, e.g., `obj[index]` or `obj[start:stop:step]`.
 *
 * @param owner The expression being sliced or indexed (e.g., the 'obj').
 * @param start The optional starting index of the slice.
 * @param stop The optional stopping index of the slice.
 * @param step The optional step value of the slice.
 */
record IRSubscription(IRExpr owner, Optional<IRExpr> start, Optional<IRExpr> stop, Optional<IRExpr> step) implements IRExpr {}