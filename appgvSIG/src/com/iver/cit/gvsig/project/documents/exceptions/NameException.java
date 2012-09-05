package com.iver.cit.gvsig.project.documents.exceptions;

/**
 * Excepción que indica un conflicto de nombres en los elementos del 
 * proyecto
 *
 * @author Fernando González Cortés
 */
public class NameException extends RuntimeException {
	/**
	 *
	 */
	public NameException() {
		super();

		// TODO Auto-generated constructor stub
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param message
	 */
	public NameException(String message) {
		super(message);

		// TODO Auto-generated constructor stub
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param message
	 * @param cause
	 */
	public NameException(String message, Throwable cause) {
		super(message, cause);

		// TODO Auto-generated constructor stub
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param cause
	 */
	public NameException(Throwable cause) {
		super(cause);

		// TODO Auto-generated constructor stub
	}
}
