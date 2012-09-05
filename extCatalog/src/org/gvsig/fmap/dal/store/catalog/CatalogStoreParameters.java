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
 
package org.gvsig.fmap.dal.store.catalog;

import org.gvsig.fmap.dal.DataStoreParameters;
import org.gvsig.fmap.dal.spi.AbstractDataParameters;
import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.dynobject.DelegatedDynObject;
import org.gvsig.tools.dynobject.DynClass;
import org.gvsig.tools.dynobject.DynField;
import org.gvsig.tools.dynobject.DynObjectManager;

/**
 * @author <a href="mailto:jpiera@gvsig.org">Jorge Piera</a>
 */
public class CatalogStoreParameters extends AbstractDataParameters implements
DataStoreParameters{
	public static final String DYNCLASS_NAME = "CatalogStoreParameters";
	public static String NAME = "CatalogStoreParameters";
	public static String DESCRIPTION = "Strore to load catalog layers";
	protected static DynClass DYNCLASS = null;
	
	public CatalogStoreParameters() {
		super();
		this.delegatedDynObject = new CatalogDynObject(DYNCLASS);
		//(DelegatedDynObject) ToolsLocator		
		//.getDynObjectManager().createDynObject(
		//		CatalogStoreParameters.DYNCLASS);
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.DataStoreParameters#getDataStoreName()
	 */
	public String getDataStoreName() {
		return NAME;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.DataStoreParameters#getDescription()
	 */
	public String getDescription() {		
		return DESCRIPTION;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.DataStoreParameters#isValid()
	 */
	public boolean isValid() {
		// TODO Auto-generated method stub
		return false;
	}
	
	protected static void registerDynClass() {
		DynObjectManager dynman = ToolsLocator.getDynObjectManager();
		DynClass dynClass;
		DynField field;
		if (DYNCLASS == null) {
			dynClass = dynman.add(DYNCLASS_NAME);
			DYNCLASS = dynClass;
		}
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.spi.AbstractDataParameters#setDynValue(java.lang.String, java.lang.Object)
	 */	
	public void setDynValue(String name, Object value) {
		this.delegatedDynObject.setDynValue(name, value);
	}
	
	
}

