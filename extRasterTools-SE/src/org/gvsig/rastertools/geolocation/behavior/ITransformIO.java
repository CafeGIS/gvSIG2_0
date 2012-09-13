/* gvSIG. Sistema de Informaci�n Geogr�fica de la Generalitat Valenciana
 *
 * Copyright (C) 2007 IVER T.I. and Generalitat Valenciana.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,USA.
 */
package org.gvsig.rastertools.geolocation.behavior;

import java.awt.geom.AffineTransform;

/**
 * Interfaz para entrada y salida de datos que tienen que ver con transformaciones
 * de georreferenciaci�n. 
 * 
 * @version 30/07/2007
 * @author Nacho Brodin (nachobrodin@gmail.com)
 *
 */
public interface ITransformIO {
	/**
	 * Carga los par�metros de una transformaci�n af�n
	 * @param transform
	 */
	public void loadTransform(AffineTransform transform);
	
	/**
	 * M�todo llamado cuando se realiza la acci�n de aplicar una transformaci�n
	 */
	public void applyTransformation();
}
