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
package org.gvsig.raster.util;

import java.awt.Dimension;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;

import junit.framework.TestCase;

import org.gvsig.raster.RasterLibrary;
import org.gvsig.raster.datastruct.Extent;
/**
 * Comprueba la llamada de RasterUtilities calculateAdjustedView con un raster
 * rotado. Le pasará un extent y este tiene que ser devuelto ajustado a la
 * transformación indicada. Como comprobación se convertirá el resultado a
 * valores pixel y se mirará que esten entre el rango 0-width, 0-height
 * 
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class TestAdjustExtentToRotateRaster extends TestCase {
	
	private int w = 870, h = 870;
	static {
		RasterLibrary.wakeUp();
	}

	public void start() {
		this.setUp();
		this.testStack();
	}

	public void setUp() {
		System.err.println("TestAdjustExtentToRotateRaster running...");
	}

	public void testStack() {
		AffineTransform at = new AffineTransform(2.4, 0.2, 0.2, -2.4, 644850.0, 4925250.0);
		Extent ext = new Extent(644823.3, 4925240.5, 644930.5, 4925123.6);
		Extent e = RasterUtilities.calculateAdjustedView(ext, at, new Dimension(w, h));
//		System.out.println("-UL=" + e.minX() + " " + e.maxY());
//		System.out.println("-LR=" + e.maxX() + " " + e.minY());
		
		Point2D ul = new Point2D.Double(e.minX(), e.maxY());
		Point2D lr = new Point2D.Double(e.maxX(), e.minY());

		try {
			at.inverseTransform(ul, ul);
			at.inverseTransform(lr, lr);
		} catch (NoninvertibleTransformException exc) {
			exc.printStackTrace();
		}
		
//		System.out.println("*UL=" + ul.getX() + " " + ul.getY());
//		System.out.println("*LR=" + lr.getX() + " " + lr.getY());

		if (ul.getX() < 0 || ul.getY() < 0 || lr.getX() < 0 || lr.getY() < 0)
			assertEquals(0, 1);

		if (ul.getX() >= w || ul.getY() >= h || lr.getX() >= w || lr.getY() >= h)
			assertEquals(0, 1);
		
//		System.out.println("-UL=" + e.minX() + " " + e.maxY());
//		System.out.println("-LR=" + e.maxX() + " " + e.minY());
	}
}