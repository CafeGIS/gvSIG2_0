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
package org.gvsig.raster.datastruct;

import org.gvsig.raster.RasterLibrary;
import org.gvsig.raster.dataset.IBuffer;
/**
 * Clase NoData que contiene el valor noData, el tipo de datos en el que se maneja
 * y si esta asignado por el usuario, es de la capa o esta desactivado.
 * 
 * @version 11/07/2008
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
public class NoData {
	double value = 0;
	int type = -1;
	
	/**
	 * Constructor vacio
	 */
	public NoData() {
	}
	
	/**
	 * Constructor con todos los parametros posibles
	 * 
	 * @param noData
	 * @param type
	 * @param dataType
	 */
	public NoData(double noData, int type, int dataType) {
		switch (dataType) {
			case IBuffer.TYPE_BYTE:
				setValue((byte) noData);
				break;
			case IBuffer.TYPE_FLOAT:
				setValue((float) noData);
				break;
			case IBuffer.TYPE_USHORT:
			case IBuffer.TYPE_INT:
				setValue((int) noData);
				break;
			case IBuffer.TYPE_SHORT:
				setValue((short) noData);
				break;
			default:
				setValue(noData);
				break;
		}
		setType(type);
		if (noData != getValue())
			setType(RasterLibrary.NODATATYPE_DISABLED);
	}
	
	/**
	 * Constructor con el valor noData y si esta activo, suponiendo que es double. 
	 * 
	 * @param noData
	 * @param type
	 */
	public NoData(double noData, int type) {
		this(noData, type, IBuffer.TYPE_DOUBLE);
	}
	
	/**
	 * @return the noData
	 */
	public double getValue() {
		return value;
	}

	/**
	 * @param noData the noData to set
	 */
	public void setValue(double noData) {
		this.value = noData;
	}

	/**
	 * @return the enabled
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param enabled the enabled to set
	 */
	public void setType(int type) {
		this.type = type;
	}
}