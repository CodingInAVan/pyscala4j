package com.pyscala4j.transpiler.ir.node;

import java.util.List;

public record IRBlock(List<IRNode> statements) implements IRNode, IRElseBranchNode {}
