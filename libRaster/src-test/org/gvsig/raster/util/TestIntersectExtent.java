/* gvSIG. Sistema de Informaci�n Geogr�fica de la Generalitat Valenciana
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

import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;

import junit.framework.TestCase;

import org.gvsig.raster.RasterLibrary;
import org.gvsig.raster.datastruct.Extent;

/**
 * Comprueba la llamada de RasterUtilities intersects con un raster rotado. Esta llamada comprueba
 * si dos extents intersectan.
 * 
 * @author Nacho Brodin (nachobrodin@gmail.com)
 *
 */
public class TestIntersectExtent extends TestCase {

	static {
		RasterLibrary.wakeUp();
	}
	
	public void start() {
		this.setUp();
		this.testStack();
	}
	
	public void setUp() {
		System.err.println("TestIntersectsExtent running...");
	}
	
	public void testStack(){
		AffineTransform at = new AffineTransform(2.0707369692354263, -1.2132800188916328, -1.2132800188916328, -2.0707369692354263, 645519.8062266004, 4925635.747389836);
		Point2D ul = new Point2D.Double(645519.8062266004, 4925635.747389836);
		Point2D ll = new Point2D.Double(644464.2526101647, 4923834.206226601);
		Point2D ur = new Point2D.Double(647321.3473898353, 4924580.193773401);
		Point2D lr = new Point2D.Double(646265.7937733995, 4922778.652610166);

		Point2D ulA = new Point2D.Double(645021.0, 4925601.0);
		Point2D lrA = new Point2D.Double(645973.0, 4924920.0);
		
		Point2D ulB = new Point2D.Double(646008.0, 4923286.0);
		Point2D lrB = new Point2D.Double(646614.0, 4922830.0);
		
		Point2D ulC = new Point2D.Double(646475.0, 4925468.0);
		Point2D lrC = new Point2D.Double(646822.0, 4925156.0);
		
		Extent e1 = new Extent(ul, lr, ur, ll);
		Extent e2 = new Extent(ulA, lrA);
		Extent e3 = new Extent(ulB, lrB);
		Extent e4 = new Extent(ulC, lrC);
		
		try {
			assertEquals(RasterUtilities.intersects(e1, e2, at), true);
			assertEquals(RasterUtilities.intersects(e1, e3, at), true);
			assertEquals(RasterUtilities.intersects(e1, e4, at), false);
		} catch (NoninvertibleTransformException e) {
			e.printStackTrace();
		}
	}

}
