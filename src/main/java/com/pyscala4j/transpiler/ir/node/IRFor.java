package com.pyscala4j.transpiler.ir.node;

import com.pyscala4j.transpiler.ir.expr.IRExpr;

public record IRFor(IRExpr stmt, IRBlock body) implements IRNode {}
