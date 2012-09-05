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
 * 2008 Prodevelop S.L  main development
 */

package org.gvsig.geocoding.distance;

import junit.framework.TestCase;

import org.gvsig.geocoding.distance.impl.LevenshteinDistance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test distance between strings
 * 
 * @author <a href="mailto:jsanz@prodevelop.es"> Jorge Gaspar Sanz Salinas</a>
 * @author <a href="mailto:vsanjaime@prodevelop.es"> Vicente Sanjaime Calvet</a>
 */

public class TestStringDistance extends TestCase {

	private static final Logger log = LoggerFactory
			.getLogger(TestStringDistance.class);

	private String str1 = "Col&on";
	private String str2 = "COL7ON";

	/**
	 * setUP
	 */
	public void setUp() {

	}

	/**
	 * tearDown
	 */
	public void tearDown() {

	}

	/**
	 * Test distance 1
	 */	public void testDistance() {


		int dist = LevenshteinDistance.computeLevenshteinDistance(str1, str2);
		log.info("Distance1: " + dist);

	}
	
	/**
	 * Test distance 2
	 */
	public void testDistanceUpper() {

		String str11 = str1.toUpperCase();
		String str22 = str2.toUpperCase();

		int dist = LevenshteinDistance.computeLevenshteinDistance(str11, str22);
		log.info("Distance2: " + dist);

	}

}
