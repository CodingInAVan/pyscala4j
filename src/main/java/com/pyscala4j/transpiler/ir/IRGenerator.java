package com.pyscala4j.transpiler.ir;


import com.pyscala4j.transpiler.ir.exception.IRConvertException;
import com.pyscala4j.transpiler.ir.expr.*;

public class IRGenerator {
	public static IRExpr unwrapString(String text) {
		if(text == null || text.length() <= 2) return new IRLiteral("", LiteralType.String);
		return new IRLiteral(text.substring(1, text.length() - 1), LiteralType.String);
	}
	public static IRExpr convertTextToExpr(String text) throws IRConvertException {
		String trimmed = text.trim();

		// 1. Logical OR
		if (trimmed.contains(" or ")) {
			String[] parts = trimmed.split(" or ", 2);
			return IRExpr.binaryOp(convertTextToExpr(parts[0]), BinaryOp.OR, convertTextToExpr(parts[1]));
		}
		// 2. Logical AND
		if (trimmed.contains(" and ")) {
			String[] parts = trimmed.split(" and ", 2);
			return IRExpr.binaryOp(convertTextToExpr(parts[0]), BinaryOp.AND, convertTextToExpr(parts[1]));
		}
		// 3. Comparisons (all have similar precedence)
		if (trimmed.contains("==")) {
			String[] parts = trimmed.split("==", 2);
			return IRExpr.binaryOp(convertTextToExpr(parts[0]), BinaryOp.EQ, convertTextToExpr(parts[1]));
		}
		if (trimmed.contains("!=")) {
			String[] parts = trimmed.split("!=", 2);
			return IRExpr.binaryOp(convertTextToExpr(parts[0]), BinaryOp.NE, convertTextToExpr(parts[1]));
		}
		if (trimmed.contains(">=")) {
			String[] parts = trimmed.split(">=", 2);
			return IRExpr.binaryOp(convertTextToExpr(parts[0]), BinaryOp.GE, convertTextToExpr(parts[1]));
		}
		if (trimmed.contains("<=")) {
			String[] parts = trimmed.split("<=", 2);
			return IRExpr.binaryOp(convertTextToExpr(parts[0]), BinaryOp.LE, convertTextToExpr(parts[1]));
		}
		if (trimmed.contains(">")) {
			String[] parts = trimmed.split(">", 2);
			return IRExpr.binaryOp(convertTextToExpr(parts[0]), BinaryOp.GT, convertTextToExpr(parts[1]));
		}
		if (trimmed.contains("<")) {
			String[] parts = trimmed.split("<", 2);
			return IRExpr.binaryOp(convertTextToExpr(parts[0]), BinaryOp.LT, convertTextToExpr(parts[1]));
		}
		// 4. Addition/Subtraction
		if (trimmed.contains("+")) {
			String[] parts = trimmed.split("\\+", 2);
			return IRExpr.binaryOp(convertTextToExpr(parts[0]), BinaryOp.ADD, convertTextToExpr(parts[1]));
		}
		if (trimmed.contains("-")) {
			String[] parts = trimmed.split("-", 2);
			return IRExpr.binaryOp(convertTextToExpr(parts[0]), BinaryOp.SUB, convertTextToExpr(parts[1]));
		}
		// 5. Multiplication/Division/Modulus
		if (trimmed.contains("*")) {
			String[] parts = trimmed.split("\\*", 2);
			return IRExpr.binaryOp(convertTextToExpr(parts[0]), BinaryOp.MUL, convertTextToExpr(parts[1]));
		}
		if (trimmed.contains("/")) {
			String[] parts = trimmed.split("/", 2);
			return IRExpr.binaryOp(convertTextToExpr(parts[0]), BinaryOp.DIV, convertTextToExpr(parts[1]));
		}
		if (trimmed.contains("%")) {
			String[] parts = trimmed.split("%", 2);
			return IRExpr.binaryOp(convertTextToExpr(parts[0]), BinaryOp.MOD, convertTextToExpr(parts[1]));
		}

		// 6. Unary Not
		if (trimmed.startsWith("not ")) {
			return IRExpr.unaryOp(UnaryOp.Not, convertTextToExpr(trimmed.substring(4)));
		}

		// 7. Literals and Identifiers (leaf nodes)
		if (trimmed.equals("True")) {
			return IRExpr.literal("true", LiteralType.Boolean);
		}
		if (trimmed.equals("False")) {
			return IRExpr.literal("false", LiteralType.Boolean);
		}
		if (trimmed.equals("None")) {
			return IRExpr.literal("null", LiteralType.Null);
		}
		if (trimmed.matches("[+-]?\\d+\\.\\d+")) {
			return IRExpr.literal(trimmed, LiteralType.Float);
		}
		if (trimmed.matches("[+-]?\\d+")) {
			return IRExpr.literal(trimmed, LiteralType.Integer);
		}
		if (trimmed.startsWith("'") && trimmed.endsWith("'")) {
			return unwrapString(trimmed);
		}
		if (trimmed.startsWith("\"") && trimmed.endsWith("\"")) {
			return unwrapString(trimmed);
		}
		if (trimmed.matches("[a-zA-Z_][a-zA-Z0-9_]*")) {
			return IRExpr.identifier(trimmed);
		}

		throw new IRConvertException("Failed to convert [" + trimmed + "] to IRExpr");
	}
}
