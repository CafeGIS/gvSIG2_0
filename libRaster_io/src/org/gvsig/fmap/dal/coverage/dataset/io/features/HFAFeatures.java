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
import org.gvsig.raster.dataset.Params;
import org.gvsig.raster.dataset.WriteFileFormatFeatures;
/**
 * Caracteristicas del formato HFA (.img) de Erdas para escritura.
 * Soporta enteros de 8, 16 y 32 bits y datos en coma flotante de 32 y 64 bits
 * con cualquier número de bandas.
 * 
 * @version 04/06/2007
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
 //TODO: Mirar si se pueden generar con piramides
public class HFAFeatures extends WriteFileFormatFeatures {
	
	public HFAFeatures() {
		super(GdalDriver.FORMAT_HFA, "img", new int[] { -1 }, new int[] { 0, 1, 2, 3, 4, 5 }, GdalWriter.class);
	}
	
	/**
	 * Carga los parámetros de este driver.
	 */
	public void loadParams() {
		super.loadParams();

		driverParams.setParam("rrd", 
				new Integer(1), 
				Params.CHOICE, 
				new String[]{ "YES", "NO"});

		driverParams.setParam("compress", 
				new Integer(1), 
				Params.CHOICE, 
				new String[]{"YES", "NO"});
	}
}
