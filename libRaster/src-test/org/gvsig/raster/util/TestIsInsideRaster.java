/* gvSIG. Sistema de Informaciï¿½n Geogrï¿½fica de la Generalitat Valenciana
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
import java.awt.geom.Point2D;

import junit.framework.TestCase;

import org.gvsig.raster.RasterLibrary;
import org.gvsig.raster.datastruct.Extent;

/**
 * Comprueba la llamada de RasterUtilities isInside con un raster rotado. Esta llamada cmprueba
 * si un punto se encuentra dentro de un Extent o fuera. Para esto convierte ambos a coordenadas
 * pixel y comprueba si el punto está entre 0 y maxX y 0 y maxY. 
 * 
 * @author Nacho Brodin (nachobrodin@gmail.com)
 *
 */
public class TestIsInsideRaster extends TestCase {

	static {
		RasterLibrary.wakeUp();
	}
	
	public void start() {
		this.setUp();
		this.testStack();
	}
	
	public void setUp() {
		System.err.println("TestIsInsideRaster running...");
		
	}
	
	public void testStack(){
		AffineTransform at = new AffineTransform(2.0707369692354263, -1.2132800188916328, -1.2132800188916328, -2.0707369692354263, 645519.8062266004, 4925635.747389836);
		Point2D ul = new Point2D.Double(645519.8062266004, 4925635.747389836);
		Point2D ll = new Point2D.Double(644464.2526101647, 4923834.206226601);
		Point2D ur = new Point2D.Double(647321.3473898353, 4924580.193773401);
		Point2D lr = new Point2D.Double(646265.7937733995, 4922778.652610166);
		Extent e = new Extent(ul, lr, ur, ll);
		
		assertEquals(RasterUtilities.isInside(new Point2D.Double(645915.55, 4924461.97), e, at), true);
		assertEquals(RasterUtilities.isInside(new Point2D.Double(646161.22, 4925326.38), e, at), false);
	}

}
