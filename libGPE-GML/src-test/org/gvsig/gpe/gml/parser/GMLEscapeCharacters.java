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
* 2009 Iver T.I.  {{Task}}
*/
 
package org.gvsig.gpe.gml.parser;

import java.util.ArrayList;

import org.gvsig.gpe.containers.Layer;

public class GMLEscapeCharacters extends GMLReaderBaseTest {
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.parser.GPEReaderBaseTest#getFile()
	 */
	public String getFile() {
		return "testdata/escape-characters.gml";
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.gml.parser.GMLReaderBaseTest#hasSchema()
	 */
	public boolean hasSchema() {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.parser.GPEReaderBaseTest#makeAsserts()
	 */
	public void makeAsserts() {
		assertEquals(1, getLayers().length);
		Layer layer = getLayers()[0];
		ArrayList features = layer.getFeatures();
		assertEquals(85, features.size());
		
	}
}

