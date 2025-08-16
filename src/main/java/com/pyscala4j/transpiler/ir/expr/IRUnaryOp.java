package com.pyscala4j.transpiler.ir.expr;

public record IRUnaryOp(UnaryOp op, IRExpr expr) implements IRExpr {}
