/* gvSIG. Geographic Information System of the Valencian Government
*
* Copyright (C) 2007-2008 Infrastructures and Transports Department
* of the Valencian Government (CIT)
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
* Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, 
* MA  02110-1301, USA.
* 
*/

/*
* AUTHORS (In addition to CIT):
* 2009 {Iver T.I.}   {Task}
*/
 
package org.gvsig.fmap.geom.primitive;

import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.fmap.geom.Geometry.SUBTYPES;
import org.gvsig.fmap.geom.Geometry.TYPES;
import org.gvsig.fmap.geom.exception.CreateGeometryException;
import org.gvsig.fmap.geom.impl.DefaultGeometryLibrary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="mailto:jpiera@gvsig.org">Jorge Piera</a>
 */
public class SurfaceTest extends TestCase {
	private GeometryManager manager;
	
	final static private Logger logger = LoggerFactory.getLogger("org.gvsig");

	static {
		//logger.addAppender(new ConsoleAppender(new SimpleLayout()));
	}

	public void setUp() throws Exception {
		super.setUp();

		DefaultGeometryLibrary lib = new DefaultGeometryLibrary();		
		lib.initialize();
		lib.postInitialize();
		
		manager = GeometryLocator.getGeometryManager();
	}
	
	public void testCreateSurface2D() throws InstantiationException, IllegalAccessException, CreateGeometryException{
		Surface surface = (Surface)manager.create(TYPES.SURFACE, SUBTYPES.GEOM2D);
		GeneralPathX generalPathX = new GeneralPathX();
		generalPathX.moveTo(0, 0);
		generalPathX.lineTo(1, 1);
		generalPathX.lineTo(2, 1);
		surface.setGeneralPath(generalPathX);	
		
		Assert.assertEquals(0.0, surface.getCoordinateAt(0,0),0);
		Assert.assertEquals(0.0, surface.getCoordinateAt(0,1),0);
		Assert.assertEquals(1.0, surface.getCoordinateAt(1,0),0);
		Assert.assertEquals(1.0, surface.getCoordinateAt(1,1),0);
		Assert.assertEquals(2.0, surface.getCoordinateAt(2,0),0);
		Assert.assertEquals(1.0, surface.getCoordinateAt(2,1),0);
		
		AffineTransform at = new AffineTransform();
	    at.translate(1, 1);
	    surface.transform(at);
		
		Assert.assertEquals(1.0, surface.getCoordinateAt(0,0),0);
		Assert.assertEquals(1.0, surface.getCoordinateAt(0,1),0);
		Assert.assertEquals(2.0, surface.getCoordinateAt(1,0),0);
		Assert.assertEquals(2.0, surface.getCoordinateAt(1,1),0);
		Assert.assertEquals(3.0, surface.getCoordinateAt(2,0),0);
		Assert.assertEquals(2.0, surface.getCoordinateAt(2,1),0);
				
		PathIterator iterator = surface.getPathIterator(at);
		double[] coords = new double[2];
		int i=0;
		while (!iterator.isDone()){
			i++;
			iterator.next();
			iterator.currentSegment(coords);			
		}
		Assert.assertEquals(3, i);		

	}
}



