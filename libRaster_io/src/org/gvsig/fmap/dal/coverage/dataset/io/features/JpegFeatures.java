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
package org.gvsig.fmap.dal.coverage.dataset.io.features;

import org.gvsig.fmap.dal.coverage.dataset.io.JpegWriter;
import org.gvsig.raster.dataset.Params;
import org.gvsig.raster.dataset.WriteFileFormatFeatures;
/**
 * Caracteristicas del formato Jpeg para escritura.
 * Soporta escritura de imagenes 1 banda (monocromo) o 3 (RGB) en 8 bits. 
 * La georreferenciación puede hacerse mediante un fichero .wld.
 * 
 * @version 04/06/2007
 * @author Nacho Brodin (nachobrodin@gmail.com)
 *
 */
public class JpegFeatures extends WriteFileFormatFeatures {
	
	public JpegFeatures() {
		super("Jpeg", "jpg", new int[]{3}, null, JpegWriter.class);
	}
	
	/**
	 * Carga los parámetros de este driver.
	 */
	public void loadParams() {
		super.loadParams();
		
		driverParams.setParam("quality",
				new Integer(16),
				Params.SLIDER,
				new String[]{ "10", "100", "16", "5", "30"}); //min, max, valor defecto, intervalo pequeño, intervalo grande;

		driverParams.setParam("progressive", 
				new Boolean("false"), 
				Params.CHECK, 
				null);
	}
}
