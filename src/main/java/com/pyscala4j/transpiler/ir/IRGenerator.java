package com.pyscala4j.transpiler.ir;


import com.pyscala4j.transpiler.ir.exception.IRConvertException;

public class IRGenerator {
	public static IRExpr unwrapString(String text) {
		if(text == null || text.length() <= 2) return new IRLiteral("", LiteralType.String);
		return new IRLiteral(text.substring(1, text.length() - 1), LiteralType.String);
	}
	public static IRExpr convertTextToExpr(String text) throws IRConvertException {
		String trimmed = text.trim();

		if (text.equals("True")) {
			return new IRLiteral("true", LiteralType.Boolean);
		}

		if (text.equals("False")) {
			return new IRLiteral("false", LiteralType.Boolean);
		}

		if (text.equals("None")) {
			return new IRLiteral("null", LiteralType.Null);
		}

		if (text.matches("\\d+")) {
			return new IRLiteral(text, LiteralType.Integer);
		}

		if (text.matches("\\d+\\.\\d+")) {
			return new IRLiteral(text, LiteralType.Float);
		}

		if (text.startsWith("\"") && text.endsWith("\"")) {
			return unwrapString(text);
		}

		if (text.startsWith("'") && text.endsWith("'")) {
			return unwrapString(text);
		}

		if (text.matches("[a-zA-Z_][a-zA-Z0-9_]*")) {
			return new IRIdentifier(text);
		}

		if (trimmed.contains(" and ")) {
			String[] parts = trimmed.split(" and ");
			return new IRBinaryOp(
				convertTextToExpr(parts[0]), BinaryOp.AND, convertTextToExpr(parts[1])
			);
		}

		if (trimmed.contains(" or ")) {
			String[] parts = trimmed.split(" or ");
			return new IRBinaryOp(
				convertTextToExpr(parts[0]), BinaryOp.OR, convertTextToExpr(parts[1])
			);
		}

		if (trimmed.contains("==")) {
			String[] parts = trimmed.split("==");
			return new IRBinaryOp(
				convertTextToExpr(parts[0]), BinaryOp.EQ, convertTextToExpr(parts[1])
			);
		}

		if (trimmed.contains("!=")) {
			String[] parts = trimmed.split("!=", 2);
			return new IRBinaryOp(
				convertTextToExpr(parts[0]), BinaryOp.NE, convertTextToExpr(parts[1])
			);
		}

		if (trimmed.contains(">=")) {
			String[] parts = trimmed.split(">", 2);
			return new IRBinaryOp(
				convertTextToExpr(parts[0]), BinaryOp.GE, convertTextToExpr(parts[1])
			);
		}

		if (trimmed.contains("<=")) {
			String[] parts = trimmed.split("<", 2);
			return new IRBinaryOp(
				convertTextToExpr(parts[0]), BinaryOp.LE, convertTextToExpr(parts[1])
			);
		}

		if (trimmed.contains(">")) {
			String[] parts = trimmed.split(">", 2);
			return new IRBinaryOp(
				convertTextToExpr(parts[0]), BinaryOp.GT, convertTextToExpr(parts[1])
			);
		}

		if (trimmed.contains("<")) {
			String[] parts = trimmed.split("<", 2);
			return new IRBinaryOp(
				convertTextToExpr(parts[0]), BinaryOp.LT, convertTextToExpr(parts[1])
			);
		}

		if (trimmed.contains("=")) {
			String[] parts = trimmed.split("=", 2);
			return new IRBinaryOp(
				convertTextToExpr(parts[0]), BinaryOp.EQ, convertTextToExpr(parts[1])
			);
		}

		if (trimmed.contains("+")) {
			String[] parts = trimmed.split("\\+", 2);
			return new IRBinaryOp(
				convertTextToExpr(parts[0]), BinaryOp.ADD, convertTextToExpr(parts[1])
			);
		}

		if (trimmed.contains("-")) {
			String[] parts = trimmed.split("-", 2);
			return new IRBinaryOp(
				convertTextToExpr(parts[0]), BinaryOp.SUB, convertTextToExpr(parts[1])
			);
		}

		if (trimmed.contains("*")) {
			String[] parts = trimmed.split("\\*", 2);
			return new IRBinaryOp(
				convertTextToExpr(parts[0]), BinaryOp.MUL, convertTextToExpr(parts[1])
			);
		}

		if (trimmed.contains("/")) {
			String[] parts = trimmed.split("/", 2);
			return new IRBinaryOp(
				convertTextToExpr(parts[0]), BinaryOp.DIV, convertTextToExpr(parts[1])
			);
		}

		if (trimmed.contains("%")) {
			String[] parts = trimmed.split("%", 2);
			return new IRBinaryOp(
				convertTextToExpr(parts[0]), BinaryOp.MOD, convertTextToExpr(parts[1])
			);
		}

		if (trimmed.contains("not ")) {
			return new IRUnaryOp(UnaryOp.Not, convertTextToExpr(trimmed.substring(4)));
		}

		throw new IRConvertException("Failed to convert [" + trimmed + "] to IRExpr");
	}
}
