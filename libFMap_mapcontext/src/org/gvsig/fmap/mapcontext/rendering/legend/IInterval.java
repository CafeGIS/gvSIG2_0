package org.gvsig.fmap.mapcontext.rendering.legend;

public interface IInterval {

	/**
	 * Devuelve "true" si el double que se pasa como parámetro esta dentro del
	 * intervalo.
	 *
	 * @param check dou¬l¬»a comprobar.
	 *
	Œ* @return "true" si está dentro del intervalo.
	 */
	public abstract boolean isInInterval(Object v);

	/**
	 * Devuelve un string con la información de inicio y final.
	 *
	 * @return String.
	 */
	public abstract String toString();

}