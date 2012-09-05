package com.iver.utiles.swing.wizard;

/**
 * Evento con la informaci�n del estado del asistente
 *
 * @author Fernando Gonz�lez Cort�s
 */
public class WizardEvent {
	public WizardControl wizard;
	public int currentStep;

	/**
	 * Crea un nuevo WizardEvent.
	 *
	 * @param w referencia al asistente donde se gener� el evento
	 * @param currentStep paso actual del asistente
	 */
	public WizardEvent(Wizard w, int currentStep) {
		this.wizard = w;
		this.currentStep = currentStep;
	}
}
