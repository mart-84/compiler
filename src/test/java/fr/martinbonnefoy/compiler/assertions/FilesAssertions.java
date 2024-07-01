/**
 * 2024 - Martin Bonnefoy
 */
package fr.martinbonnefoy.compiler.assertions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 *
 */
public class FilesAssertions {

	public static void assertFilesContentEquals(String expected, String actual, String message) throws IOException {
		String expectedContent = Files.readString(Paths.get(expected));
		String actualContent = Files.readString(Paths.get(actual));
		assertEquals(expectedContent, actualContent, message);
	}

	public static void assertFilesContentEquals(String expected, String actual) throws IOException {
		assertFilesContentEquals(expected, actual, null);
	}

}
