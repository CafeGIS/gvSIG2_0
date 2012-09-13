/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2007 Instituto de Desarrollo Regional and Generalitat Valenciana.
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
 *
 * For more information, contact:
 *
 *  Generalitat Valenciana
 *   Conselleria d'Infraestructures i Transport
 *   Av. Blasco Ibáñez, 50
 *   46010 VALENCIA
 *   SPAIN
 *
 *      +34 963862235
 *   gvsig@gva.es
 *      www.gvsig.gva.es
 *
 *    or
 *
 *   Instituto de Desarrollo Regional (Universidad de Castilla La-Mancha)
 *   Campus Universitario s/n
 *   02071 Alabacete
 *   Spain
 *
 *   +34 967 599 200
 */
package org.gvsig.fmap.raster.layers;

import java.awt.Color;

import org.gvsig.raster.grid.roi.ROI;

/**
 * Clase que representa una región de interés tal y como se asocia a una 
 * capa raster, es decir, incluyendo un nombre y el color con el que se 
 * representa.
 * 
 * @author Diego Guerrero Sevilla (diego.guerrero@uclm.es)
 *
 */
public class ROILayerRaster {
	
	private ROI roi = null;
	private Color color = null;
	private String name = null;
	
	/**
	 * Constructor.
	 * 
	 * @param roi
	 * @param color
	 * @param name
	 */
	public ROILayerRaster(ROI roi, Color color, String name) {
		this.roi = roi;
		this.color = color;
		this.name = name;
	}

	/**
	 * 
	 * @return Color con el que la ROI se representa en la vista.
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * 
	 * @param color Color con el que la ROI se representa en la vista.
	 */
	public void setColor(Color color) {
		this.color = color;
	}

	/**
	 * 
	 * @return Nombre asociado a la ROI.
	 */
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @param name Nombre asociado a la ROI.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 
	 * @return Región de Interés.
	 */
	public ROI getRoi() {
		return roi;
	}

	/**
	 * 
	 * @param roi Región de interés.
	 */
	public void setRoi(ROI roi) {
		this.roi = roi;
	}
}
