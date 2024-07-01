/**
 * 2024 - Martin Bonnefoy
 */
package fr.martinbonnefoy.compiler.visitor;

import java.io.PrintStream;

import fr.martinbonnefoy.compiler.antlr4.CBaseVisitor;
import fr.martinbonnefoy.compiler.antlr4.CParser.FunctionContext;
import fr.martinbonnefoy.compiler.antlr4.CParser.ProgContext;
import fr.martinbonnefoy.compiler.antlr4.CParser.ReturnInstructionContext;

/**
 *
 */
public class CodeGeneratorVisitor extends CBaseVisitor<Object> {

	private PrintStream output;

	/**
	 * Constructor
	 *
	 * @param output the output stream where to write the generated ASM code
	 */
	public CodeGeneratorVisitor(PrintStream output) {
		this.output = output;
	}

	@Override
	public Object visitProg(ProgContext ctx) {
		visit(ctx.function());

		return null;
	}

	@Override
	public Object visitFunction(FunctionContext ctx) {
		output.println(".globl _main");
		output.println("_main:");
		output.println("pushl %ebp");
		output.println("movl %esp, %ebp");

		visit(ctx.returnInstruction());

		output.println("popl %ebp");
		output.println("ret");

		return null;
	}

	@Override
	public Object visitReturnInstruction(ReturnInstructionContext ctx) {
		output.println("movl $" + ctx.NUMBER().getText() + ", %eax");

		return null;
	}

}
