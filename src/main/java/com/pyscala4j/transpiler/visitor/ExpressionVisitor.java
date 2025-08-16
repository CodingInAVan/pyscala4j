package com.pyscala4j.transpiler.visitor;

import com.pyscala4j.antlr.generated.Python3Parser;
import com.pyscala4j.antlr.generated.Python3ParserBaseVisitor;
import com.pyscala4j.transpiler.ir.expr.*;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExpressionVisitor extends Python3ParserBaseVisitor<IRExpr> {

	@Override
	public IRExpr visitExpr(Python3Parser.ExprContext ctx) {
		if(ctx.expr().size() == 2) {
			IRExpr left = visit(ctx.expr(0));
			IRExpr right = visit(ctx.expr(1));

			if (ctx.ADD() != null) {
				return new IRBinaryOp(left, BinaryOp.ADD, right);
			}
			if (ctx.MINUS() != null) {
				return new IRBinaryOp(left, BinaryOp.SUB, right);
			}
		}
		if(ctx.expr().size() == 1 && ctx.MINUS() != null) {
			IRExpr operand = visit(ctx.expr(0));
			return new IRUnaryOp(UnaryOp.Minus, operand);
		}

		return super.visitExpr(ctx);
	}

	/**
	 * This method handles the 'atom' part of an expression.
	 * It's responsible for identifying the most basic language constructs.
	 */
	@Override
	public IRExpr visitAtom(Python3Parser.AtomContext ctx) {

		if (ctx.name() != null) {
			return IRExpr.identifier(ctx.name().getText());
		}
		if (ctx.NUMBER() != null) {
			String text = ctx.NUMBER().getText();
			if (text.matches("[+-]?\\d+\\.\\d+")) {
				return IRExpr.literal(text, LiteralType.Float);
			}
			return IRExpr.literal(text, LiteralType.Integer);
		}
		if (!ctx.STRING().isEmpty()) {
			String fullString = unwrapString(ctx.getText()); // Assuming you have a helper
			return IRExpr.literal(fullString, LiteralType.String);
		}
		if (ctx.TRUE() != null) {
			return IRExpr.literal("true", LiteralType.Boolean);
		}
		if (ctx.FALSE() != null) {
			return IRExpr.literal("false", LiteralType.Boolean);
		}
		if (ctx.NONE() != null) {
			return IRExpr.literal("null", LiteralType.Null);
		}

		return super.visitAtom(ctx);
	}

	@Override
	public IRExpr visitAtom_expr(Python3Parser.Atom_exprContext ctx) {
		IRExpr result = visit(ctx.atom());
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
				result = handleSubscription(result, trailerCtx.subscriptlist());

			}
		}

		return result;
	}

	private IRExpr visitIfPresent(ParserRuleContext ctx) {
		return (ctx != null) ? visit(ctx) : null;
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
			if(!subscriptCtx.test().isEmpty()) {
				Python3Parser.TestContext firstExpr = subscriptCtx.test(0);
				int exprTokenIndex = firstExpr.getStart().getTokenIndex();
				int colonTokenIndex = subscriptCtx.COLON().getSymbol().getTokenIndex();

				if (exprTokenIndex < colonTokenIndex) {
					start = Optional.of(visit(subscriptCtx.test(0)));

					if (subscriptCtx.test().size() > 1) {
						stop = Optional.of(visit(subscriptCtx.test(1)));
					}
				} else {
					stop = Optional.of(visit(firstExpr));
				}
			}

			if (subscriptCtx.sliceop() != null && subscriptCtx.sliceop().test() != null) {
				step = Optional.of(visit(subscriptCtx.sliceop().test()));
			}
		}
		return new IRSubscription(owner, start, stop, step);
	}

	private String unwrapString(String text) {
		// Basic implementation, can be made more robust for escaped quotes
		return text.substring(1, text.length() - 1);
	}
}
