/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2005 IVER T.I. and Generalitat Valenciana.
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
package org.gvsig.raster.datastruct.io.formats;

import java.io.File;
import java.io.IOException;

import org.gvsig.raster.dataset.RasterDataset;
import org.gvsig.raster.dataset.serializer.RmfSerializerException;
import org.gvsig.raster.datastruct.ColorTable;
import org.gvsig.raster.datastruct.io.RasterLegendIO;
/**
 * Clase LegendgvSIG para la exportacion e importacion de ficheros rmf.
 * Permite coger cualquier tipo de rmf generado con gvSIG y cargar de
 * ellos solo la tabla de color o sustituir su tabla de color sin
 * perder el rmf.
 *
 * @version 20/11/2007
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
public class LegendgvSIG extends RasterLegendIO {

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.datastruct.io.RasterLegendIO#read(java.io.File)
	 */
	public ColorTable read(File input) throws IOException {
		try {
			return (ColorTable) RasterDataset.loadObjectFromRmfFile(input.toString(), ColorTable.class, null);
		} catch (RmfSerializerException e) {
			throw new IOException("No se ha podido leer del RMF la tabla de color"); 
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.datastruct.io.RasterLegendIO#write(org.gvsig.raster.datastruct.ColorTable, java.io.File)
	 */
	public void write(ColorTable colorTable, File output) throws IOException {
		try {
			RasterDataset.saveObjectToRmfFile(output.toString(), ColorTable.class, colorTable);
		} catch (RmfSerializerException e) {
			throw new IOException("No se ha podido guardar en el RMF la tabla de color"); 
		}
	}
}