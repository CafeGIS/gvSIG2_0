package com.iver.utiles.swing.wizard;

/**
 * Interfaz a implementar por todas las páginas del wizard.
 *
 * @author Fernando González Cortés
 */
public interface Step {
	/**
	 * Recibe la referencia al Wizard del que forman parte
	 *
	 * @param w Referencia al wizard
	 */
	public void init(WizardControl w);
}
