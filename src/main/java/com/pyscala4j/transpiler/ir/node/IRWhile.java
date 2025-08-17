package com.pyscala4j.transpiler.ir.node;

import com.pyscala4j.transpiler.ir.expr.IRExpr;

public record IRWhile(IRExpr cond, IRBlock body) implements IRNode {}
