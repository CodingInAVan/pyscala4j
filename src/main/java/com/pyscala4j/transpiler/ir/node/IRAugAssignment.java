package com.pyscala4j.transpiler.ir.node;

import com.pyscala4j.transpiler.ir.expr.BinaryOp;
import com.pyscala4j.transpiler.ir.expr.IRExpr;

public record IRAugAssignment(String name, BinaryOp op, IRExpr value) implements IRNode {}
