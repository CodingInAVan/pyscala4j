package com.pyscala4j.transpiler.ir.expr;

public record IRBinaryOp(IRExpr left, BinaryOp op, IRExpr right) implements IRExpr {}
