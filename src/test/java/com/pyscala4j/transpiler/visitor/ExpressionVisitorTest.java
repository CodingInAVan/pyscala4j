package com.pyscala4j.transpiler.visitor;

import com.pyscala4j.antlr.generated.Python3Lexer;
import com.pyscala4j.antlr.generated.Python3Parser;
import com.pyscala4j.transpiler.ir.expr.LiteralType;
import com.pyscala4j.transpiler.ir.expr.*;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

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
		IRExpr expr = expressionVisitor.visitAtom_expr(atom_expr);
		List<IRExpr> expectedParams = List.of(
			new IRLiteral("1", LiteralType.Integer),
			new IRLiteral("2", LiteralType.Integer),
			new IRLiteral("3", LiteralType.Integer)
		);
		assertThat(expr)
			.isInstanceOf(IRFunctionCall.class)
			.asInstanceOf(InstanceOfAssertFactories.type(IRFunctionCall.class))
			.satisfies(call -> {
				assertThat(call.func()).isInstanceOf(IRIdentifier.class);
				assertThat(call.args()).hasSize(3);
				assertThat(call.args()).isEqualTo(expectedParams);

			});

	}

	@Test
	void testSimpleArrayExpr() {
		String code = "test[0]";

		Python3Parser.Atom_exprContext atom_expr = parseAtomExpr(code);
		IRExpr expr = expressionVisitor.visitAtom_expr(atom_expr);
		assertThat(expr)
			.isInstanceOf(IRSubscription.class)
			.asInstanceOf(InstanceOfAssertFactories.type(IRSubscription.class))
			.satisfies(subscription -> {
				assertThat(subscription.owner()).isInstanceOf(IRIdentifier.class)
					.asInstanceOf(InstanceOfAssertFactories.type(IRIdentifier.class))
					.satisfies(id -> {
						assertThat(id.name()).isEqualTo("test");
					});

				assertThat(subscription.start())
					.isNotEmpty()
					.hasValueSatisfying(start -> {
						assertThat(start)
							.isInstanceOf(IRLiteral.class)
							.asInstanceOf(InstanceOfAssertFactories.type(IRLiteral.class))
							.satisfies(literal -> {
								assertThat(literal.value()).isEqualTo("0");
							});
					});

				assertThat(subscription.stop())
					.isEmpty();

				assertThat(subscription.step())
					.isEmpty();
			});

	}

	@Test
	void testSliceWithStartOnly() {
		String code = "test[42:]";

		Python3Parser.Atom_exprContext atom_expr = parseAtomExpr(code);
		IRExpr expr = expressionVisitor.visitAtom_expr(atom_expr);

		assertThat(expr)
			.isInstanceOf(IRSubscription.class)
			.asInstanceOf(InstanceOfAssertFactories.type(IRSubscription.class))
			.satisfies(subscription -> {
				assertThat(subscription.owner()).extracting("name").isEqualTo("test");

				assertThat(subscription.start())
					.isNotEmpty()
					.hasValueSatisfying(start -> assertThat(start)
						.extracting("value", "literalType")
						.containsExactly("42", LiteralType.Integer));

				assertThat(subscription.stop()).isEmpty();
				assertThat(subscription.step()).isEmpty();
			});
	}

	@Test
	void testSliceWithStopOnly() {
		String code = "test[:100]";

		Python3Parser.Atom_exprContext atom_expr = parseAtomExpr(code);
		IRExpr expr = expressionVisitor.visitAtom_expr(atom_expr);
		System.out.println(expr);

		assertThat(expr)
			.isInstanceOf(IRSubscription.class)
			.asInstanceOf(InstanceOfAssertFactories.type(IRSubscription.class))
			.satisfies(subscription -> {
				assertThat(subscription.owner()).extracting("name").isEqualTo("test");

				assertThat(subscription.start()).isEmpty();

				assertThat(subscription.stop())
					.isNotEmpty()
					.hasValueSatisfying(stop -> assertThat(stop)
						.extracting("value", "literalType")
						.containsExactly("100", LiteralType.Integer));

				assertThat(subscription.step()).isEmpty();
			});
	}

	@Test
	void testFullSliceWithStep() {
		String code = "test[1:10:2]";

		Python3Parser.Atom_exprContext atom_expr = parseAtomExpr(code);
		IRExpr expr = expressionVisitor.visitAtom_expr(atom_expr);

		assertThat(expr)
			.isInstanceOf(IRSubscription.class)
			.asInstanceOf(InstanceOfAssertFactories.type(IRSubscription.class))
			.satisfies(subscription -> {
				assertThat(subscription.owner()).extracting("name").isEqualTo("test");

				assertThat(subscription.start())
					.isNotEmpty()
					.hasValueSatisfying(start -> assertThat(start)
						.extracting("value").isEqualTo("1"));

				assertThat(subscription.stop())
					.isNotEmpty()
					.hasValueSatisfying(stop -> assertThat(stop)
						.extracting("value").isEqualTo("10"));

				assertThat(subscription.step())
					.isNotEmpty()
					.hasValueSatisfying(step -> assertThat(step)
						.extracting("value").isEqualTo("2"));
			});
	}

	@Test
	void testReverseSlice() {
		String code = "test[::-1]";

		Python3Parser.Atom_exprContext atom_expr = parseAtomExpr(code);
		IRExpr expr = expressionVisitor.visitAtom_expr(atom_expr);
		assertThat(expr)
			.isInstanceOf(IRSubscription.class)
			.asInstanceOf(InstanceOfAssertFactories.type(IRSubscription.class))
			.satisfies(subscription -> {
				assertThat(subscription.owner()).extracting("name").isEqualTo("test");

				assertThat(subscription.start()).isEmpty();
				assertThat(subscription.stop()).isEmpty();

				assertThat(subscription.step())
					.isNotEmpty()
					.hasValueSatisfying(step -> assertThat(step)
						.isInstanceOf(IRUnaryOp.class)
						.asInstanceOf(InstanceOfAssertFactories.type(IRUnaryOp.class))
						.satisfies(unaryOp -> {
							IRLiteral expected = new IRLiteral("1", LiteralType.Integer);
							assertThat(unaryOp.expr())
								.isEqualTo(expected);
						}));
			});
	}
}
