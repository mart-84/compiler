/**
 * 2024 - Martin Bonnefoy
 */
package fr.martinbonnefoy.compiler;

import static fr.martinbonnefoy.compiler.assertions.FilesAssertions.assertFilesContentEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import fr.martinbonnefoy.compiler.exception.CompilerException;
import fr.martinbonnefoy.compiler.parameterized.CustomerParameterResolver;
import fr.martinbonnefoy.compiler.util.CompilationTestStep;

/**
 *
 */
@ExtendWith(CustomerParameterResolver.class)
class CompilationTest {

	private static final String SOURCE_DIR = "src/test/resources/sources".replace("/", File.separator);
	private static final String TARGET_DIR = "target/compile-test".replace("/", File.separator);

	/**
	 * The path of the original C file
	 */
	private String sourceFilePath;

	/**
	 * The directory where to put all files generated during the test
	 */
	private String targetDirectory;

	/**
	 * The name of the test
	 */
	private String testName;

	@BeforeAll
	static void setUpBeforeClass() throws IOException {
		FileUtils.deleteDirectory(new File(TARGET_DIR));
		new File(TARGET_DIR).mkdirs();
	}

	@BeforeEach
	void setUp(File sourceFile) {
		this.testName = getTestName(sourceFile);
		this.sourceFilePath = sourceFile.getAbsolutePath();
		this.targetDirectory = TARGET_DIR + File.separator + testName + File.separator;
		new File(this.targetDirectory).mkdirs(); // Create the directory if it does not exist
	}

	@ParameterizedTest
	@MethodSource("provideSourceFiles")
	void testCompilation(File sourceFile) throws IOException, InterruptedException {
		// Compile with GCC
		boolean canGccCompile = true;
		try {
			CompilationTestStep.compileWithGcc(sourceFilePath, targetDirectory + "asm-gcc.s",
					targetDirectory + "gcc-compile.txt");
		} catch (CompilerException e) {
			canGccCompile = false;
		}

		// Compile with Custom compiler
		boolean canCustomCompile = true;
		try {
			CompilationTestStep.compileWithCustomCompiler(sourceFilePath, targetDirectory + "asm-custom.s",
					targetDirectory + "custom-compile.txt");
		} catch (CompilerException e) {
			canCustomCompile = false;
		}
		assertEquals(canGccCompile, canCustomCompile, "Compilation not compliant with GCC");
		if (!canGccCompile) {
			return;
		}

		// Link gcc
		boolean canGccLink = true;
		try {
			CompilationTestStep.linkWithGcc(targetDirectory + "asm-gcc.s", targetDirectory + "executable-gcc.exe",
					targetDirectory + "gcc-link.txt");
		} catch (CompilerException e) {
			canGccLink = false;
		}

		// Link custom
		boolean canCustomLink = true;
		try {
			CompilationTestStep.linkWithGcc(targetDirectory + "asm-custom.s", targetDirectory + "executable-custom.exe",
					targetDirectory + "custom-link.txt");
		} catch (CompilerException e) {
			canCustomLink = false;
		}
		assertEquals(canGccLink, canCustomLink, "Linking not compliant with GCC");
		if (!canGccLink) {
			return;
		}

		// Execute gcc executable
		int gccExitCode = CompilationTestStep.runExecutable(targetDirectory + "executable-gcc.exe",
				targetDirectory + "gcc-execution.txt");
		// Execute custom executable
		int customExitCode = CompilationTestStep.runExecutable(targetDirectory + "executable-custom.exe",
				targetDirectory + "custom-execution.txt");
		assertEquals(gccExitCode, customExitCode, "Execution exit code not compliant with GCC");
		assertFilesContentEquals(targetDirectory + "gcc-execution.txt", targetDirectory + "custom-execution.txt",
				"Execution output stream not compliant with GCC");
	}

	/**
	 * Generate the name of the test based on the file path
	 *
	 * @param sourceFile
	 * @return
	 */
	private static String getTestName(File sourceFile) {
		String absolutePath = sourceFile.getAbsolutePath();
		String testPath = absolutePath.substring(absolutePath.lastIndexOf(SOURCE_DIR) + SOURCE_DIR.length() + 1);
		String testName = testPath.substring(0, testPath.lastIndexOf("."));
		testName = testName.replaceAll("[/\\\\]", "_");
		return testName;
	}

	/**
	 * Source of the parameters for the parameterized tests
	 *
	 * @return
	 * @throws IOException
	 */
	private static List<File> provideSourceFiles() throws IOException {
		Path startDir = Paths.get(SOURCE_DIR);
		try (Stream<Path> stream = Files.walk(startDir)) {
			return stream.filter(Files::isRegularFile).map(Path::toFile).toList();
		}
	}

}
