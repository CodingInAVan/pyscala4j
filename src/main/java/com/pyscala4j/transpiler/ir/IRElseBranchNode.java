package com.pyscala4j.transpiler.ir;

public sealed interface IRElseBranchNode extends IRNode permits IRIf, IRBlock {
}
