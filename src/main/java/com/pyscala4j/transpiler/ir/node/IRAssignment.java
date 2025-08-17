package com.pyscala4j.transpiler.ir.node;

import com.pyscala4j.transpiler.ir.expr.IRExpr;

public record IRAssignment(String name, IRExpr value) implements IRNode {}
