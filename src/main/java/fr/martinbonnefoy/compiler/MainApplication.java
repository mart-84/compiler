/**
 * 2024 - Martin Bonnefoy
 */
package fr.martinbonnefoy.compiler;

import java.io.FileInputStream;
import java.io.IOException;

import fr.martinbonnefoy.compiler.exception.CompilerException;

/**
 *
 */
public class MainApplication {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Handle args : source file, target file, optimisations

		Compiler comp = new Compiler();
		try (FileInputStream fis = new FileInputStream("test.c")) {
			comp.compile(fis);
		} catch (IOException | CompilerException e) {
			e.printStackTrace();
		}
	}

}
