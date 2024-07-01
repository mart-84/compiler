/**
 * 2024 - Martin Bonnefoy
 */
package fr.martinbonnefoy.compiler.exception;

/**
 *
 */
public class CompilerException extends Exception {

	/**
	 * Serial UID
	 */
	private static final long serialVersionUID = 9188102398672295084L;

	/**
	 *
	 */
	public CompilerException() {
	}

	/**
	 * @param message
	 * @param cause
	 */
	public CompilerException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public CompilerException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public CompilerException(Throwable cause) {
		super(cause);
	}

}
