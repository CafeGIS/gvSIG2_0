/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
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
package org.gvsig.raster.hierarchy;

import org.gvsig.raster.datastruct.Histogram;
import org.gvsig.raster.datastruct.HistogramException;


/**
 * Interfaz que deben implementar todas las clases capaces de servir histogramas
 * @version 27/03/2007
 * @author Nacho Brodin (nachobrodin@gmail.com)
 *
 */
public interface IHistogramable {
	
	/**
	 * Obtiene un histograma.
	 * @return Histograma
	 */
	public Histogram getHistogram() throws HistogramException, InterruptedException;
	
	/**
	 * Inicializa el porcentaje a 0 de un histograma.
	 */
	public void resetPercent();
	
	/**
	 * Obtiene el porcentaje procesado de un histograma.
	 * @return int
	 */
	public int getPercent();

}
