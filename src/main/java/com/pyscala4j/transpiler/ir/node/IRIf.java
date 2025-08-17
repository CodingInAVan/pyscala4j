package com.pyscala4j.transpiler.ir.node;

import com.pyscala4j.transpiler.ir.expr.IRExpr;

import java.util.Optional;

public record IRIf(IRExpr cond, IRBlock thenBranch, Optional<IRElseBranchNode> elseBranch) implements IRNode, IRElseBranchNode {}
