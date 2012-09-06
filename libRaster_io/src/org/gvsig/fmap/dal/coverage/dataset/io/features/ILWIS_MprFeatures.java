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
 * Caracteristicas del formato ILWIS para escritura.
 * Soporta tipos de datos enteros en 8, 16 y 32 bits y coma flotante en 32 y 64 bits
 * Genera un fichero .mpl con la información de contenido (ficheros), un fichero .grf con la
 * georreferenciación, y dos ficheros por banda. Un .mpr con la información referente a los
 * datos de la banda y un .mp# con los datos de la banda
 * 
 * @version 04/06/2007
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class ILWIS_MprFeatures extends WriteFileFormatFeatures {

	public ILWIS_MprFeatures() {
		super(GdalDriver.FORMAT_ILWIS, "mpl", new int[] { -1 }, new int[] { 0, 1, 2, 3, 4, 5 }, GdalWriter.class);
	}

	/**
	 * Carga los parámetros de este driver.
	 */
	public void loadParams() {
		super.loadParams();
	}
}
