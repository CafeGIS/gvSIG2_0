package com.iver.utiles.swing.wizard;

/**
 * Evento con la información del estado del asistente
 *
 * @author Fernando González Cortés
 */
public class WizardEvent {
	public WizardControl wizard;
	public int currentStep;

	/**
	 * Crea un nuevo WizardEvent.
	 *
	 * @param w referencia al asistente donde se generó el evento
	 * @param currentStep paso actual del asistente
	 */
	public WizardEvent(Wizard w, int currentStep) {
		this.wizard = w;
		this.currentStep = currentStep;
	}
}
