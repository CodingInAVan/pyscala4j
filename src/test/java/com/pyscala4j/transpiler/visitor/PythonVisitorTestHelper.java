package com.pyscala4j.transpiler.visitor;

import com.pyscala4j.antlr.generated.Python3Lexer;
import com.pyscala4j.antlr.generated.Python3Parser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

public class PythonVisitorTestHelper {

	public static Python3Parser createParser(String pythonCode) {
		Python3Lexer lexer = new Python3Lexer(CharStreams.fromString(pythonCode));
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		return new Python3Parser(tokens);
	}
}
