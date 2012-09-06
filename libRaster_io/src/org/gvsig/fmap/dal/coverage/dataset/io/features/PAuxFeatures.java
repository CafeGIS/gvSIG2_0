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

import org.gvsig.fmap.dal.coverage.dataset.io.GdalDriver;
import org.gvsig.fmap.dal.coverage.dataset.io.GdalWriter;
import org.gvsig.raster.dataset.WriteFileFormatFeatures;
/**
 * Caracteristicas del formato PAux para escritura.
 * Soporta tipo de datos enteros de 8 y 16 bits y reales de 32 bits.
 * La georreferenciación y la proyección son ignoradas.
 * 
 * @version 04/06/2007
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class PAuxFeatures extends WriteFileFormatFeatures {

	public PAuxFeatures() {
		super(GdalDriver.FORMAT_PAUX, "aux", new int[] { 3 }, null, GdalWriter.class);
	}

	/**
	 * Carga los parámetros de este driver.
	 */
	public void loadParams() {
		super.loadParams();
	}
}
