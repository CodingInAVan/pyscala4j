package com.pyscala4j.transpiler.ir.expr;

import java.util.Optional;

public record IRSubscription(IRExpr owner, Optional<IRExpr> start, Optional<IRExpr> stop, Optional<IRExpr> step) implements IRExpr {}
