package com.iver.utiles.swing.objectSelection;

/**
 * Excepci�n que indica que el modelo del control ObjectSelection no puede
 * proporcionar la informaci�n requerida.
 *
 * @author Fernando Gonz�lez Cort�s
 */
public class SelectionException extends Exception {
	/**
	 *
	 */
	public SelectionException() {
		super();

		// TODO Auto-generated constructor stub
	}

	/**
	 * constructor
	 *
	 * @param message
	 */
	public SelectionException(String message) {
		super(message);

		// TODO Auto-generated constructor stub
	}

	/**
	 * constructor
	 *
	 * @param message
	 * @param cause
	 */
	public SelectionException(String message, Throwable cause) {
		super(message, cause);

		// TODO Auto-generated constructor stub
	}

	/**
	 * constructor
	 *
	 * @param cause
	 */
	public SelectionException(Throwable cause) {
		super(cause);

		// TODO Auto-generated constructor stub
	}
}
