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
package org.gvsig.fmap.raster.layers;
/**
 * DefaultLayerConfiguration sirve para aquellos objetos que no puedan usar la
 * configuracion de PluginServices, devolviendo siempre el valor por defecto.
 * 
 * @version 14/01/2008
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
public class DefaultLayerConfiguration implements IConfiguration {

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.fmap.raster.conf.IConfiguration#getValueBoolean(java.lang.String, java.lang.Boolean)
	 */
	public Boolean getValueBoolean(String name, Boolean defaultValue) {
		return defaultValue;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.fmap.raster.conf.IConfiguration#getValueDouble(java.lang.String, java.lang.Double)
	 */
	public Double getValueDouble(String name, Double defaultValue) {
		return defaultValue;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.fmap.raster.conf.IConfiguration#getValueFloat(java.lang.String, java.lang.Float)
	 */
	public Float getValueFloat(String name, Float defaultValue) {
		return defaultValue;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.fmap.raster.conf.IConfiguration#getValueInteger(java.lang.String, java.lang.Integer)
	 */
	public Integer getValueInteger(String name, Integer defaultValue) {
		return defaultValue;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.fmap.raster.conf.IConfiguration#getValueLong(java.lang.String, java.lang.Long)
	 */
	public Long getValueLong(String name, Long defaultValue) {
		return defaultValue;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.fmap.raster.conf.IConfiguration#getValueString(java.lang.String, java.lang.String)
	 */
	public String getValueString(String name, String defaultValue) {
		return defaultValue;
	}
}