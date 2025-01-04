package org.tron.trident.core.exceptions;

public class IllegalException extends Exception {
	private static final long serialVersionUID = -239904158268711522L;

	public IllegalException() {
		super("Query failed. Please check the parameters.");
	}

	public IllegalException(String message) {
		super(message);
	}
}
