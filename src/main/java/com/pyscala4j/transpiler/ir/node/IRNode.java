package com.pyscala4j.transpiler.ir.node;

import com.pyscala4j.transpiler.ir.expr.BinaryOp;
import com.pyscala4j.transpiler.ir.expr.IRExpr;

import java.util.Optional;

public sealed interface IRNode
	permits IRAssignment, IRAugAssignment, IRBlock, IRElseBranchNode, IRExprStmt, IRFor, IRIf, IRReturn, IRWhile, InvalidIRNode {}

