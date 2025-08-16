package com.pyscala4j.transpiler.ir.expr;

public record IRAttributeAccess(IRExpr owner, String attributeName) implements IRExpr {}
