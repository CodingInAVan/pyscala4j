package com.pyscala4j.transpiler.visitor;

import com.pyscala4j.antlr.generated.Python3Parser;
import com.pyscala4j.antlr.generated.Python3ParserBaseVisitor;
import com.pyscala4j.transpiler.ir.expr.IRExpr;
import com.pyscala4j.transpiler.ir.node.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class PythonToIRNodeVisitor extends Python3ParserBaseVisitor<IRNode> {

	private final ExpressionVisitor expressionVisitor;
	private static final Logger logger = LoggerFactory.getLogger(PythonToIRNodeVisitor.class);

	public PythonToIRNodeVisitor(ExpressionVisitor expressionVisitor) {
		this.expressionVisitor = expressionVisitor;
	}

	@Override
	public IRNode visitFile_input(Python3Parser.File_inputContext ctx) {
		logger.info("Starting IR generation for file.");
		List<IRNode> statements = new ArrayList<>();

		for (Python3Parser.StmtContext stmtCtx : ctx.stmt()) {
			IRNode statementNode = visit(stmtCtx);
			if (statementNode != null) {
				statements.add(statementNode);
			}
		}

		logger.info("Finished IR generation for file.");
		return new IRBlock(statements);
	}

	@Override
	public IRNode visitStmt(Python3Parser.StmtContext ctx) {
		if (ctx.simple_stmts() != null) {
			return visit(ctx.simple_stmts());
		}
		if (ctx.compound_stmt() != null) {
			return visit(ctx.compound_stmt());
		}
		logger.warn("Encountered an unknown statement type.");
		return new InvalidIRNode("Unknown statement type",
			ctx.getText(),
			ctx.getStart().getLine(),
			ctx.getStart().getCharPositionInLine());
	}

	@Override
	public IRNode visitExpr_stmt(Python3Parser.Expr_stmtContext ctx) {
		logger.debug("Visiting expr_stmt: {}", ctx.getText());

		if(ctx.ASSIGN(0) != null) {
			String variableName = ctx.testlist_star_expr(0).getText();
			Python3Parser.Testlist_star_exprContext valueContext = ctx.testlist_star_expr(1);
			IRExpr expr = expressionVisitor.visit(valueContext);

			if(valueContext != null) {
				return new IRAssignment(variableName, expr);
			}

		} else {
			Python3Parser.Testlist_star_exprContext expressionTree = ctx.testlist_star_expr(0);
			IRExpr expr = expressionVisitor.visit(expressionTree);

			if (expr != null) {
				return new IRExprStmt(expr);
			}
		}



		return new InvalidIRNode("Invalid expression in statement",
			ctx.getText(),
			ctx.getStart().getLine(),
			ctx.getStart().getCharPositionInLine()
			);
	}
}
