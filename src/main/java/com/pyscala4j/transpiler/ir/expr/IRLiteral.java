package com.pyscala4j.transpiler.ir.expr;

public record IRLiteral(String value, LiteralType literalType) implements IRExpr {}
