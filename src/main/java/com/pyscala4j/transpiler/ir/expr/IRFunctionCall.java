package com.pyscala4j.transpiler.ir.expr;

import java.util.List;

public record IRFunctionCall(IRExpr func, List<IRExpr> args) implements IRExpr {}
