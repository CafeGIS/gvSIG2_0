package org.gvsig.gui.beans.imagenavigator;

import java.awt.Graphics2D;
/**
 * Interfaz que debe implementar quien quiera dibujar en ImageNavigator
 * @version 12/06/2008
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
public interface IClientImageNavigator {

	/**
	 * Metodo que es invocado cuando ImageNavigator va a pintar la imagen.
	 * Pasa un Graphics para hacer el dibujado, y luego todas las posibles
	 * posiciones para hacer el pintado.
	 * 
	 * @param g
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @param zoom
	 * @param width
	 * @param height
	 * @throws ImageUnavailableException
	 */
	public void drawImage(Graphics2D g, double x1, double y1, double x2, double y2, double zoom, int width, int height) throws ImageUnavailableException;
}