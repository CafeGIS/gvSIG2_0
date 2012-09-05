package com.iver.cit.gvsig.project.documents.gui;

import com.iver.utiles.swing.objectSelection.ObjectSelection;
import com.iver.utiles.swing.wizard.Step;
import com.iver.utiles.swing.wizard.WizardControl;


/**
 * Control ObjectSelection como paso de un asistente
 *
 * @author Fernando González Cortés
 */
public class ObjectSelectionStep extends ObjectSelection implements Step {

    private WizardControl w;

	/**
	 * @see com.iver.utiles.swing.wizard.Step#init(com.iver.utiles.swing.wizard.Wizard)
	 */
	public void init(WizardControl w) {
		this.w = w;
	}

	/**
	 * @see com.iver.utiles.swing.objectSelection.ObjectSelection#seleccion()
	 */
	protected void seleccion() {
		if (w != null) {
			w.enableNext(getCombo().getSelectedIndex() != -1);
		}
	}

	/**
	 * Obtiene el elemento seleccionado
	 *
	 * @return
	 */
	public Object getSelectedItem() {
		return getCombo().getSelectedItem();
	}
}
