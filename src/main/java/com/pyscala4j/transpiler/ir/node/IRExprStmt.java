package com.pyscala4j.transpiler.ir.node;

import com.pyscala4j.transpiler.ir.expr.IRExpr;

public record IRExprStmt(IRExpr expr) implements IRNode {}
