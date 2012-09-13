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
package org.gvsig.raster.beans.canvas.layers.functions;

import java.awt.Color;
import java.awt.event.MouseEvent;
/**
 * Representa una línea escalonada para el realce de density slicing.
 * Esta hereda de StraightLine porque es básicamente la misma pero con una
 * forma determinada.
 * 
 * 11/03/2008
 * @author Nacho Brodin nachobrodin@gmail.com
 */
public class DensitySlicingLine extends StraightLine {
	public static final int DEFAULT_LEVELS = 6;

	private int             levels         = DEFAULT_LEVELS;
	
	/**
	 * Constructor. Asigna el valor de color a la línea
	 * @param c Color
	 */
	public DensitySlicingLine(Color c) {
		this(c, DEFAULT_LEVELS);
	}
	
	/**
	 * Constructor. Asigna el valor de color a la línea
	 * @param c Color
	 */
	public DensitySlicingLine(Color c, int levels) {
		super(c);
		setShowSquares(false);
		setShape(levels);
	}
	
	/**
	 * Pone la forma a la línea escalonada dependiendo del número de 
	 * escalones existentes.
	 * 
	 * @param levels
	 */
	public void setShape(int levels) {
		this.levels = levels;
		listSquare.clear();

		if (levels <= 1)
			return;

		int nSquares = (levels * 2);

		double posX = 0.0D;
		double posY = 0.0D;
		double contX = 0.0D;
		double contY = 0.0D;
		for (int i = 0; i < nSquares; i++) {
			posX = contX / (double) levels;
			posY = contY / (double) (levels - 1.0D);
			listSquare.add(new Square(posX, posY));
			if ((i % 2) != 0)
				contY++;
			else
				contX++;
		}
	}

	/**
	 * @return the levels
	 */
	public int getLevels() {
		return levels;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.beans.canvas.layers.functions.StraightLine#getFunctionType()
	 */
	public int getFunctionType() {
		return 3;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.beans.canvas.layers.functions.StraightLine#getValueFunction()
	 */
	public double getValueFunction() {
		return levels;
	}
	
	/* (non-Javadoc)
	 * @see org.gvsig.raster.beans.canvas.layers.functions.StraightLine#mouseDragged(java.awt.event.MouseEvent)
	 */
	public boolean mouseDragged(MouseEvent e) {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.raster.beans.canvas.layers.functions.StraightLine#mouseMoved(java.awt.event.MouseEvent)
	 */
	public boolean mouseMoved(MouseEvent e) {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.raster.beans.canvas.layers.functions.StraightLine#mousePressed(java.awt.event.MouseEvent)
	 */
	public boolean mousePressed(MouseEvent e) {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.raster.beans.canvas.layers.functions.StraightLine#mouseReleased(java.awt.event.MouseEvent)
	 */
	public boolean mouseReleased(MouseEvent e) {
		return true;
	}

	public void firstDrawActions() {}
}