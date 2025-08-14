package com.pyscala4j.transpiler.ir;

import java.util.List;

public record IRBlock(List<IRNode> statements) implements IRNode, IRElseBranchNode {}
