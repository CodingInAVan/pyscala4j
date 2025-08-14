package com.pyscala4j.transpiler.visitor;

import com.pyscala4j.antlr.generated.Python3Parser;
import com.pyscala4j.antlr.generated.Python3ParserBaseVisitor;
import com.pyscala4j.transpiler.ir.IRExpr;
import com.pyscala4j.transpiler.ir.IRSubscription;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExpressionVisitor extends Python3ParserBaseVisitor<IRExpr> {
	@Override
	public IRExpr visitAtom_expr(Python3Parser.Atom_exprContext ctx) {
		IRExpr result = visit(ctx);

		if (ctx.trailer() == null || ctx.trailer().isEmpty()) {
			return result;
		}
		for(Python3Parser.TrailerContext trailerCtx : ctx.trailer()) {
			if (trailerCtx.OPEN_PAREN() != null && trailerCtx.CLOSE_PAREN() != null) {
				List<IRExpr> args = new ArrayList<>();
				Python3Parser.ArglistContext arglistCtx = trailerCtx.arglist();

				if (arglistCtx != null) {
					for (Python3Parser.ArgumentContext argCtx : arglistCtx.argument()) {
						IRExpr argExpr = visit(argCtx.test(0));
						args.add(argExpr);
					}
				}

				result = IRExpr.functionCall(result, args);
			} else if (trailerCtx.DOT() != null) {
				String attributeName = trailerCtx.name().getText();
				result = IRExpr.attributeAccess(result, attributeName);
			} else if (trailerCtx.OPEN_BRACK() != null) {
				Python3Parser.Subscript_Context subscript = trailerCtx.subscriptlist().subscript_(0);
				IRExpr indexExpr = visit(subscript.test(0));
				result = handleSubscription(result, trailerCtx.subscriptlist());

			}
		}

		return result;
	}

	private IRExpr handleSubscription(IRExpr owner, Python3Parser.SubscriptlistContext subscriptListCtx) {
		Python3Parser.Subscript_Context subscriptCtx = subscriptListCtx.subscript_(0);
		Optional<IRExpr> start = Optional.empty();
		Optional<IRExpr> stop = Optional.empty();
		Optional<IRExpr> step = Optional.empty();

		if(subscriptCtx.COLON() == null) {
			// Simple Index
			start = Optional.of(visit(subscriptCtx.test(0)));
		} else {
			// --- Case 2: Slicing ---
			if(subscriptCtx.test(0) != null) {
				start = Optional.of(visit(subscriptCtx.test(0)));
			}

			int stopIndex = (subscriptCtx.test(0) != null) ? 1 : 0;
			if (subscriptCtx.test().size() > stopIndex) {
				stop = Optional.of(visit(subscriptCtx.test(stopIndex)));
			}

			if(subscriptCtx.sliceop() != null && subscriptCtx.sliceop().test() != null) {
				step = Optional.of(visit(subscriptCtx.sliceop().test()));
			}
		}

		return new IRExpr.subscription()
	}
}
