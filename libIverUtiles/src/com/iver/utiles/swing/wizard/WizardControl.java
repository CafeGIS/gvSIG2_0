package com.iver.utiles.swing.wizard;

/**
 * Visi�n que tienen los escuchadores de eventos sobre el Wizard
 *
 * @author Fernando Gonz�lez Cort�s
 */
public interface WizardControl {
	/**
	 * Activa el paso al siguiente paso del asistente
	 *
	 * @param enabled si se habilita o no
	 */
	public abstract void enableNext(boolean enabled);

	/**
	 * Activa el paso al paso anterior del asistente
	 *
	 * @param enabled si se habilita o no
	 */
	public abstract void enableBack(boolean enabled);

	/**
	 * Muestra el panel del siguiente paso del asistente
	 */
	public abstract void nextStep();

	/**
	 * Muestra el panel del paso anterior del asistente
	 */
	public abstract void backStep();

	/**
	 * Se cancela el asistente. Esta operaci�n no tiene ning�n efecto, salvo
	 * que se disparar� el evento de cancelado. El resultado de esto depender�
	 * de las implementaciones que haya escuchando el evento. Generalmente
	 * deber� haber un objeto que al escuchar este evento cerrar� el
	 * asistente.
	 */
	public abstract void cancel();

	/**
	 * Se finaliza el asistente. Esta operaci�n no tiene ning�n efecto, salvo
	 * que se disparar� el evento de finalizaci�n. El resultado de esto
	 * depender� de las implementaciones que haya escuchando el evento.
	 * Generalmente deber� haber un objeto que al escuchar este evento cerrar�
	 * el asistente.
	 */
	public abstract void finish();

	/**
	 * Obtiene el paso actual del asistente
	 *
	 * @return Paso actual del asistente
	 */
	public abstract Step getCurrentStep();
}
