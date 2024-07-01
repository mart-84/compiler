/**
 * 2024 - Martin Bonnefoy
 */
package fr.martinbonnefoy.compiler;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;

import fr.martinbonnefoy.compiler.antlr4.CLexer;
import fr.martinbonnefoy.compiler.antlr4.CParser;
import fr.martinbonnefoy.compiler.exception.CompilerException;
import fr.martinbonnefoy.compiler.visitor.CodeGeneratorVisitor;

/**
 *
 */
public class Compiler {

	private PrintStream output;

	/**
	 *
	 */
	public Compiler() {
		this(System.out);
	}

	/**
	 * @param output
	 */
	public Compiler(PrintStream output) {
		this.output = output;
	}

	// Goals :
	// evaluate/propagate expression
	// AST to IR
	// optimization 1
	// IR to ASM
	// optimization 2

	public void compile(InputStream input) throws IOException, CompilerException {
		CharStream charStream = CharStreams.fromStream(input);
		CLexer lexer = new CLexer(charStream);
		TokenStream tokens = new CommonTokenStream(lexer);
		CParser parser = new CParser(tokens);

		CodeGeneratorVisitor visitor = new CodeGeneratorVisitor(output);
		visitor.visit(parser.prog());

	}
}
