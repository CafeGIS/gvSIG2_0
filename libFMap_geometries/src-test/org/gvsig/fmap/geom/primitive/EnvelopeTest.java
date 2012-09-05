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

import junit.framework.Assert;
import junit.framework.TestCase;

import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.fmap.geom.Geometry.SUBTYPES;
import org.gvsig.fmap.geom.Geometry.TYPES;
import org.gvsig.fmap.geom.exception.CreateEnvelopeException;
import org.gvsig.fmap.geom.exception.CreateGeometryException;
import org.gvsig.fmap.geom.impl.DefaultGeometryLibrary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="mailto:jpiera@gvsig.org">Jorge Piera</a>
 */
public class EnvelopeTest extends TestCase {
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

	public void testEnvelope2D() throws InstantiationException,
			IllegalAccessException, CreateEnvelopeException, CreateGeometryException {

		// Create
		Envelope env = createEnvelope2D(0, 1, 2, 3);
		checkEnvelope2D(env, 0, 1, 2, 3, "Create");

		Envelope env2 = createEnvelope2D(1, 2, 3, 4);
		checkEnvelope2D(env2, 1, 2, 3, 4, "Create 2");

		// Add
		env.add(env2);
		checkEnvelope2D(env, 0, 1, 3, 4, "Add");

		// Contains
		assertTrue("Contains", env.contains(env2));
		env2 = createEnvelope2D(1, 1, 2, 2);
		assertTrue("Contains 2", env.contains(env2));
		env2 = createEnvelope2D(1, 1, 6, 6);
		assertFalse("Contains 3", env.contains(env2));
		env2 = createEnvelope2D(-1, -1, -6, -6);
		assertFalse("Contains 4", env.contains(env2));

		// Intersets
		env2 = createEnvelope2D(1, 1, 2, 2);
		assertTrue("Intersets", env.intersects(env2));
		env2 = createEnvelope2D(-1, -1, 2, 2);
		assertTrue("Intersets 2", env.intersects(env2));
		env2 = createEnvelope2D(1, 1, 6, 6);
		assertTrue("Intersets 3", env.intersects(env2));
		env2 = createEnvelope2D(-5, -5, 6, 6);
		assertTrue("Intersets 3", env.intersects(env2));
		env2 = createEnvelope2D(-1, -1, -6, -6);
		assertFalse("Intersets 4", env.intersects(env2));
		env2 = createEnvelope2D(5, 5, 9, 9);
		assertFalse("Intersets 4", env.intersects(env2));



	}

	private Envelope createEnvelope2D(double minx, double miny, double maxx,
			double maxy) throws InstantiationException, IllegalAccessException, CreateEnvelopeException, CreateGeometryException {
		Envelope envelope = manager.createEnvelope(SUBTYPES.GEOM2D);
		Point min = (Point) manager.create(TYPES.POINT,SUBTYPES.GEOM2D);
		Point max = (Point) manager.create(TYPES.POINT,SUBTYPES.GEOM2D);
		min.setX(minx);
		min.setY(miny);

		max.setX(maxx);
		max.setY(maxy);
		envelope.setLowerCorner(min);
		envelope.setUpperCorner(max);

		return envelope;
	}

	private void checkEnvelope2D(Envelope env, double minx, double miny,
			double maxx, double maxy, String info) {

		Assert.assertTrue(info + ": minx : expected " + minx + " but has "
				+ env.getMinimum(0), env.getMinimum(0) - minx < 0.000000001);
		Assert.assertTrue(info + ": miny : expected " + miny + " but has "
				+ env.getMinimum(1), env.getMinimum(1) - miny < 0.000000001);
		Assert.assertTrue(info + ": maxx : expected " + maxx + " but has "
				+ env.getMaximum(0), env.getMaximum(0) - maxx < 0.000000001);
		Assert.assertTrue(info + ": maxy : expected " + maxy + " but has "
				+ env.getMaximum(1), env.getMaximum(1) - maxy < 0.000000001);

		Assert.assertTrue(info + ": lower conrner x : expected " + minx
				+ "but has " + env.getLowerCorner().getX(),
				env.getLowerCorner().getX() - minx < 0.000000001);
		Assert.assertTrue(info + ": lower conrner y : expected " + miny
				+ "but has " + env.getLowerCorner().getY(),
				env.getLowerCorner().getY() - miny < 0.000000001);
		Assert.assertTrue(info + ": upper conrner x : expected " + maxx
				+ "but has " + env.getUpperCorner().getX(),
				env.getUpperCorner().getX() - maxx < 0.000000001);
		Assert.assertTrue(info + ": upper conrner y : expected " + maxy
				+ "but has " + env.getUpperCorner().getY(),
				env.getUpperCorner().getY() - maxy < 0.000000001);

		double lengthx = 0, lengthy = 0;
		double midx = 0, midy = 0;
		try {
			lengthx = maxx - minx;
			midx = ( lengthx/ 2) + minx;
		} catch (Exception e) {
			// Nonthing to do
		}
		try {
			lengthy = maxy - miny;
			midy = (lengthy / 2) + miny;
		} catch (Exception e) {
			// Nonthing to do
		}

		Assert.assertTrue(info + ": center x : expected " + midx + "but has "
				+ env.getCenter(0),
				env.getCenter(0) - midx < 0.000000001);
		Assert.assertTrue(info + ": center y : expected " + midy + "but has "
				+ env.getCenter(1), env.getCenter(1) - midy < 0.000000001);

		Assert.assertTrue(info + ": length x : expected " + lengthx
				+ "but has " + env.getLength(0),
				env.getLength(0) - lengthx < 0.000000001);
		Assert.assertTrue(info + ": length y : expected " + lengthy
				+ "but has " + env.getLength(1),
				env.getLength(1) - lengthy < 0.000000001);



	}


}


