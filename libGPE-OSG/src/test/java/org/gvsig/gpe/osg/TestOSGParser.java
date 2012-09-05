/* gvSIG. Geographic Information System of the Valencian Government
*  osgVP. OSG Virtual Planets.
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
* 2008 Instituto de Automática e Informática Industrial, UPV.
*/


package org.gvsig.gpe.osg;

import java.io.FileNotFoundException;
import java.net.URI;
import java.net.URISyntaxException;

import junit.framework.TestCase;

import org.gvsig.driver.OSGDriver;

import org.gvsig.osgvp.util.Util;

public class TestOSGParser extends TestCase {

	public void testParseURI() {
		//fail("Not yet implemented");
	}

	public void testParseStream() {
		// TODO: This is the real work.
		OSGDriver osgDriver = new OSGDriver();
		OSGParser parser = new OSGParser("OSG","OSG File Formats Parser");
		try {
			parser.parse(osgDriver, null, Util.extractResource("/cessna.osg").toURI());
		} catch (FileNotFoundException e) {
			fail("Resource not found");
		}
//		osgDriver.getRootFeature();
	}

	public void testOSGParser() {
		// TODO: This is the constructor, test only for creation
		// or something similar.
//		fail("Not yet implemented");
	}

	public void testAcceptURI() {
		OSGParser parser = new OSGParser("OSG","OSG File Formats Parser");
		URI uri;
		try {
			uri = Util.extractResource("/cessna.osg").toURI();
			assertEquals(parser.accept(uri),true);
			uri = new URI("pepe.com");
			assertEquals(parser.accept(uri),false);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			fail();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			fail();
		}
		
	}

	public void testGetFormats() {
		String[] formats = new String[4];
		OSGParser parser = new OSGParser("OSG","OSG File Formats Parser");
		formats[0] = "OSG";
		formats[1] = "3DS";
		formats[2] = "OBJ";
		formats[3] = "IVE";
		
		for(int i=0; i<formats.length; i++){
			
			assertEquals(formats[i].equals(parser.getFormats()[i]),true);

			
		}
	}

	public void testGetVersions() {
		String[] versions = new String[1];
		versions[0] = "All";
		OSGParser parser = new OSGParser("OSG","OSG File Formats Parser");

		for(int i=0; i<versions.length; i++){
			
			assertEquals(versions[i].equals(parser.getVersions()[i]),true);

			
		}
	}

}
