package com.pyscala4j.transpiler.visitor;

import com.pyscala4j.antlr.generated.Python3Lexer;
import com.pyscala4j.antlr.generated.Python3Parser;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ExpressionVisitorTest {
	private ExpressionVisitor expressionVisitor;

	@BeforeEach
	void setUp() {
		this.expressionVisitor = new ExpressionVisitor();
	}

	/**
	 * Helper function to parse a Python expression snippet.
	 * It handles all the ANTLR boilerplate
	 * @param pythonCode A string containing the Python expression to parse.
	 * @return The ANTLR context object for the atom_expr rule.
	 */
	private Python3Parser.Atom_exprContext parseAtomExpr(String pythonCode) {
		Python3Lexer lexer = new Python3Lexer(CharStreams.fromString(pythonCode));
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		Python3Parser parser = new Python3Parser(tokens);
		return parser.atom_expr();
	}

	@Test
	void testSimpleFunctionCall() {
		String code = "my_function(1,2,3)";

		Python3Parser.Atom_exprContext atom_expr = parseAtomExpr(code);
		System.out.println(expressionVisitor.visitAtom_expr(atom_expr));

	}
}
