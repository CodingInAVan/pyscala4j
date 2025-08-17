package com.pyscala4j.transpiler.visitor;

import com.pyscala4j.antlr.generated.Python3Parser;
import com.pyscala4j.transpiler.ir.expr.IRFunctionCall;
import com.pyscala4j.transpiler.ir.expr.IRLiteral;
import com.pyscala4j.transpiler.ir.expr.LiteralType;
import com.pyscala4j.transpiler.ir.node.IRAssignment;
import com.pyscala4j.transpiler.ir.node.IRBlock;
import com.pyscala4j.transpiler.ir.node.IRExprStmt;
import com.pyscala4j.transpiler.ir.node.IRNode;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.pyscala4j.transpiler.visitor.PythonVisitorTestHelper.createParser;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class PythonToIRNodeVisitorTest {
	private PythonToIRNodeVisitor irNodeVisitor;
	private ExpressionVisitor expressionVisitor;

	@BeforeEach
	public void setUp() {
		expressionVisitor = new ExpressionVisitor();
		irNodeVisitor = new PythonToIRNodeVisitor(expressionVisitor);
	}

	private Python3Parser.File_inputContext parseFile(String pythonCode) {
		Python3Parser parser = createParser(pythonCode);
		return parser.file_input();
	}

	@Test
	void testEmptyFileProducesEmptyBlock() {
		String code = "";
		Python3Parser.File_inputContext context = parseFile(code);

		IRNode result = irNodeVisitor.visitFile_input(context);

		assertThat(result)
			.isInstanceOf(IRBlock.class)
			.asInstanceOf(InstanceOfAssertFactories.type(IRBlock.class))
			.extracting("statements") // Assumes IRBlock has a 'statements' field
			.asInstanceOf(InstanceOfAssertFactories.LIST)
			.isEmpty();
	}

	@Test
	void testSingleExpressionStatement() {
		String code = "my_func()";
		Python3Parser.File_inputContext context = parseFile(code);

		IRNode result = irNodeVisitor.visitFile_input(context);

		assertThat(result)
			.isInstanceOf(IRBlock.class)
			.asInstanceOf(InstanceOfAssertFactories.type(IRBlock.class))
			.extracting("statements")
			.asInstanceOf(InstanceOfAssertFactories.LIST)
			.hasSize(1)
			.element(0) // Get the first statement
			.isInstanceOf(IRExprStmt.class)
			.asInstanceOf(InstanceOfAssertFactories.type(IRExprStmt.class))
			.extracting("expr") // Assumes IRExprStmt has an 'expr' field
			.isInstanceOf(IRFunctionCall.class);
	}

	@Test
	void testSimpleAssignmentStatement() {
		String code = "x = 42";
		Python3Parser.File_inputContext context = parseFile(code);

		IRNode result = irNodeVisitor.visitFile_input(context);
		System.out.println(result);

		assertThat(result)
			.isInstanceOf(IRBlock.class)
			.asInstanceOf(InstanceOfAssertFactories.type(IRBlock.class))
			.satisfies(block -> {
				assertThat(block.statements().size()).isEqualTo(1);

				// Assert that the first statement is an IRAssignment
				assertThat(block.statements().getFirst())
					.isInstanceOf(IRAssignment.class)
					.asInstanceOf(InstanceOfAssertFactories.type(IRAssignment.class))
					.satisfies(assignment -> {
						// Check the variable name and the assigned value
						assertThat(assignment.name()).isEqualTo("x");
						assertThat(assignment.value())
							.isEqualTo(new IRLiteral("42", LiteralType.Integer));
					});
			});
	}
}
