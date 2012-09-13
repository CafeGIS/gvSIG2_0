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
package org.gvsig.raster.beans.canvas.layers.functions;

import java.awt.Graphics;
import java.util.ArrayList;

import org.gvsig.raster.beans.canvas.DrawableElement;
import org.gvsig.raster.beans.canvas.layers.MinMaxLines;
/**
 * Todo algoritmo que queramos que implemente un realce, debe heredar de esta base
 * para poder distinguir los drawables genericos de los algoritmos.
 * 
 * @version 03/03/2008
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
public abstract class BaseFunction extends DrawableElement {
	protected double minx = 0.0D;
	protected double maxx = 1.0D;

	/**
	 * Especifica el limite minimo del algoritmo en forma de porcentaje
	 * @param minx
	 */
	public void setMinX(double minx) {
		this.minx = minx;
	}

	/**
	 * Especifica el limite maximo del algoritmo en forma de porcentaje
	 * @param maxx
	 */
	public void setMaxX(double maxx) {
		this.maxx = maxx;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.beans.canvas.DrawableElement#paint(java.awt.Graphics)
	 */
	protected void paint(Graphics g) {
		ArrayList elements = canvas.getDrawableElements(MinMaxLines.class);
		if (elements.size() > 0) {
			MinMaxLines minMaxLines = (MinMaxLines) elements.get(0);
			setMinX(minMaxLines.getMinDistance());
			setMaxX(minMaxLines.getMaxDistance());
		}
	}
}