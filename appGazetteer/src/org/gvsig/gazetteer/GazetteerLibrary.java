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
 
package org.gvsig.gazetteer;

import org.gvsig.tools.locator.BaseLibrary;
import org.gvsig.tools.locator.ReferenceNotRegisteredException;

/**
 * @author <a href="mailto:jpiera@gvsig.org">Jorge Piera</a>
 */
public class GazetteerLibrary extends BaseLibrary  {
		
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.tools.locator.BaseLibrary#initialize()
	 */
	public void initialize() throws ReferenceNotRegisteredException {
		super.initialize();				
	}	

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.tools.locator.BaseLibrary#postInitialize()
	 */
	public void postInitialize() {
		super.postInitialize();		
		
		// Validate there is any implementation registered of CatalogManager.
		GazetteerManager catalogManager = GazetteerLocator.getGazetteerManager();
		if (catalogManager == null) {
			throw new ReferenceNotRegisteredException(
					GazetteerLocator.GAZETTEER_MANAGER_NAME, 
					GazetteerLocator.getInstance());
		}		
	}
}
