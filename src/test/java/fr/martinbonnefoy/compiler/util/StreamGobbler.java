/**
 * 2024 - Martin Bonnefoy
 */
package fr.martinbonnefoy.compiler.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.function.Consumer;

/**
 * Consume an InputStream
 */
public class StreamGobbler {
	private InputStream inputStream;
	private Consumer<String> consumer;

	/**
	 * Constructor
	 *
	 * @param inputStream the input stream
	 * @param consumer    the consumer
	 */
	public StreamGobbler(InputStream inputStream, Consumer<String> consumer) {
		this.inputStream = inputStream;
		this.consumer = consumer;
	}

	/**
	 * Start the StreamGobbler
	 */
	public void start() {
		new BufferedReader(new InputStreamReader(inputStream)).lines().forEach(consumer);
	}
}