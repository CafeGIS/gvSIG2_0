package com.iver.utiles.swing.wizard;

/**
 * Listener de los eventos del asistente
 *
 * @author Fernando Gonz�lez Cort�s
 */
public interface WizardListener {
	/**
	 * Evento disparado cuando se cancela el asistente
	 *
	 * @param w objeto con la informaci�n del evento
	 */
	public void cancel(WizardEvent w);

	/**
	 * Evento disparado cuando se finaliza el asistente
	 *
	 * @param w objeto con la informaci�n del evento
	 */
	public void finished(WizardEvent w);

	/**
	 * Evento disparado cuando se avanza un paso el asistente
	 *
	 * @param w objeto con la informaci�n del evento
	 */
	public void next(WizardEvent w);

	/**
	 * Evento disparado cuando se da un paso atr�s en el asistente
	 *
	 * @param w objeto con la informaci�n del evento
	 */
	public void back(WizardEvent w);
}
