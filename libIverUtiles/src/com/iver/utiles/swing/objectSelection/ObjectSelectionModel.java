package com.iver.utiles.swing.objectSelection;

/**
 * Modelo del control ObjectSelection
 *
 * @author Fernando González Cortés
 */
public interface ObjectSelectionModel {
	/**
	 * Obtiene las referencias a los objetos que aparecerán
	 * seleccionables en el combo
	 *
	 * @return array de objetos
	 *
	 * @throws SelectionException Si se produce algún error
	 */
	public Object[] getObjects() throws SelectionException;

	/**
	 * Obtiene el mensaje que se mostrará al usuario
	 *
	 * @return String
	 *
	 */
	public String getMsg();
}
