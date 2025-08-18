package com.pyscala4j.transpiler.ir.analyzer;

import com.pyscala4j.transpiler.ir.expr.*;
import com.pyscala4j.transpiler.ir.node.IRAssignment;

public final class TypeInference {
	public static InferredType inferTypeFromAssignment(IRAssignment assignment, SymbolTable symbolTable) {
		return inferExprType(assignment.value(), symbolTable);
	}

	public static InferredType inferExprType(IRExpr expr, SymbolTable symbolTable) {
		return switch (expr) {
			case IRLiteral literal -> switch (literal.literalType()) {
				case Integer -> InferredType.INTEGER;
				case Float -> InferredType.FLOAT;
				case String -> InferredType.STRING;
				case Boolean -> InferredType.BOOLEAN;
				case Null -> InferredType.NULL;
			};
			case IRIdentifier identifier -> symbolTable.findVariable(identifier.name())
				.map(VariableInfo::inferredType)
				.orElse(InferredType.ANY);

			case IRBinaryOp binaryOp -> {
				InferredType leftType = inferExprType(binaryOp.left(), symbolTable);
				InferredType rightType = inferExprType(binaryOp.right(), symbolTable);

				if (leftType == InferredType.DOUBLE || rightType == InferredType.DOUBLE) {
					yield InferredType.DOUBLE;
				}
				if (leftType == InferredType.FLOAT || rightType == InferredType.FLOAT) {
					yield InferredType.FLOAT;
				}
				yield leftType;
			}
			case IRFunctionCall call -> InferredType.ANY;
			case IRSubscription sub -> InferredType.ANY;
			case IRAttributeAccess acc -> InferredType.ANY;
			default -> InferredType.UNKNOWN;
		};
	}
}
