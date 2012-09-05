package com.iver.utiles.swing.objectSelection;

/**
 * Modelo del control ObjectSelection
 *
 * @author Fernando Gonz�lez Cort�s
 */
public interface ObjectSelectionModel {
	/**
	 * Obtiene las referencias a los objetos que aparecer�n
	 * seleccionables en el combo
	 *
	 * @return array de objetos
	 *
	 * @throws SelectionException Si se produce alg�n error
	 */
	public Object[] getObjects() throws SelectionException;

	/**
	 * Obtiene el mensaje que se mostrar� al usuario
	 *
	 * @return String
	 *
	 */
	public String getMsg();
}
