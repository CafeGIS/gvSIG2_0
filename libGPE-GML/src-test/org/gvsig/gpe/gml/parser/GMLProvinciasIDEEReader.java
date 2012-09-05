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

import org.gvsig.gpe.containers.Layer;

public class GMLProvinciasIDEEReader extends GMLReaderBaseTest {
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.readers.GPEReaderBaseTest#getFile()
	 */
	public String getFile() {
		return "testdata/ProvinciasIDEE.gml";
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.gml.readers.GMLReaderBaseTest#hasSchema()
	 */
	public boolean hasSchema() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.readers.GPEReaderBaseTest#makeAsserts()
	 */
	public void makeAsserts() {
		Layer[] layers = getLayers();
		assertEquals(layers.length, 1);
		Layer layer = layers[0];
		assertEquals(52, layer.getFeatures().size());
		
	}
	
}

