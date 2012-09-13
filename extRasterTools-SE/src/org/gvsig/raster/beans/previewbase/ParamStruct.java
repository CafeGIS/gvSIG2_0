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
package org.gvsig.raster.beans.previewbase;

import org.gvsig.raster.dataset.Params;

/**
 * Estructura para los parámetros de PreviewFiltering. Contiene el nombre del 
 * filtro, el parámetro y la clase de este.
 * 
 */
public class ParamStruct {
	String filterName = null;
	Params filterParam = null;
	Class filterClass = null;

	/**
	 * @return the filterName
	 */
	public String getFilterName() {
		return filterName;
	}

	/**
	 * @param filterName the filterName to set
	 */
	public void setFilterName(String filterName) {
		this.filterName = filterName;
	}

	/**
	 * @return the filterParam
	 */
	public Params getFilterParam() {
		return filterParam;
	}

	/**
	 * @param filterParam the filterParam to set
	 */
	public void setFilterParam(Params filterParam) {
		this.filterParam = filterParam;
	}

	/**
	 * @return the filterClass
	 */
	public Class getFilterClass() {
		return filterClass;
	}

	/**
	 * @param filterClass the filterClass to set
	 */
	public void setFilterClass(Class filterClass) {
		this.filterClass = filterClass;
	}
}
