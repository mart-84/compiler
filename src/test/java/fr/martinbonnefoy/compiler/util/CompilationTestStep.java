/**
 * 2024 - Martin Bonnefoy
 */
package fr.martinbonnefoy.compiler.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.martinbonnefoy.compiler.Compiler;
import fr.martinbonnefoy.compiler.exception.CompilerException;

public final class CompilationTestStep {

	/**
	 * Standard output of the current process.<br/>
	 * Allow redirection of standard output to another stream for a limited
	 * time/method execution/...
	 */
	private static final PrintStream originalOut = System.out;

	private static final Logger logger = LoggerFactory.getLogger(CompilationTestStep.class);

	/**
	 * Compile to ASM using GCC
	 */
	public static void compileWithGcc(String sourceFile, String targetFile, String executionOutputFile)
			throws InterruptedException, IOException, CompilerException {
		int exitCode = executeProcess(List.of("gcc", "-S", sourceFile, "-O0", "-o", targetFile), executionOutputFile);
		if (exitCode != 0) {
			throw new CompilerException("GCC Compiler returned a non-zero exit code: " + exitCode);
		}
	}

	/**
	 * Compile to ASM using the custom compiler
	 */
	public static void compileWithCustomCompiler(String sourceFile, String targetFile, String executionOutputFile)
			throws IOException, CompilerException {
		logger.debug("Custom compiling");
		try (FileOutputStream targetFileStream = new FileOutputStream(targetFile);
				PrintStream targetFilePrintStream = new PrintStream(targetFileStream);
				FileOutputStream stdOut = new FileOutputStream(executionOutputFile);
				PrintStream outFile = new PrintStream(stdOut)) {
			System.setOut(outFile); // If a logger is used in the compiler, change ?
			Compiler compiler = new Compiler(targetFilePrintStream);
			compiler.compile(new FileInputStream(sourceFile));
			System.setOut(originalOut);
		}
	}

	/**
	 * Link ASM to executable using GCC
	 */
	public static void linkWithGcc(String sourceFile, String targetFile, String executionOutputFile)
			throws IOException, InterruptedException, CompilerException {
		int exitCode = executeProcess(List.of("gcc", sourceFile, "-o", targetFile), executionOutputFile);
		if (exitCode != 0) {
			throw new CompilerException("GCC Linker returned a non-zero exit code: " + exitCode);
		}
	}

	/**
	 * Run the executable and return its exit code
	 */
	public static int runExecutable(String executable, String executionOutputFile)
			throws IOException, InterruptedException {
		return executeProcess(executable, executionOutputFile);
	}

	/**
	 * Execute a process and redirect its output to the named file
	 */
	private static int executeProcess(String command, String executionOutputFile)
			throws IOException, InterruptedException {
		return executeProcess(Collections.singletonList(command), executionOutputFile);
	}

	/**
	 * Execute a process and redirect its output to the named file
	 */
	private static int executeProcess(List<String> command, String executionOutputFile)
			throws IOException, InterruptedException {
		logger.debug("Running command : {}", String.join(" ", command));
		ProcessBuilder builder = new ProcessBuilder(command);
		builder.redirectErrorStream(true);
		Process process = builder.start();

		try (FileOutputStream stdOut = new FileOutputStream(executionOutputFile);
				PrintWriter writer = new PrintWriter(stdOut)) {
			StreamGobbler streamGobbler = new StreamGobbler(process.getInputStream(), writer::println);
			streamGobbler.start();
			return process.waitFor();
		}
	}

}
