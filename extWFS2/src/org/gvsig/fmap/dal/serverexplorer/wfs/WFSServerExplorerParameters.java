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

package org.gvsig.fmap.dal.serverexplorer.wfs;

import org.gvsig.fmap.dal.DataServerExplorerParameters;
import org.gvsig.fmap.dal.DataTypes;
import org.gvsig.fmap.dal.spi.AbstractDataParameters;
import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.dynobject.DelegatedDynObject;
import org.gvsig.tools.dynobject.DynClass;
import org.gvsig.tools.dynobject.DynField;
import org.gvsig.tools.dynobject.DynObjectManager;

/**
 * @author <a href="mailto:jpiera@gvsig.org">Jorge Piera</a>
 */
public class WFSServerExplorerParameters extends AbstractDataParameters
implements DataServerExplorerParameters {
	public static final String DYNCLASS_NAME = "WFSServerExplorerParameters";
	public static final String DYNFIELDNAME_URL = "url";
	public static final String DYNFIELDNAME_VERSION = "version";

	public WFSServerExplorerParameters() {
		super();
		this.delegatedDynObject = (DelegatedDynObject) ToolsLocator
		.getDynObjectManager()
		.createDynObject(this.registerDynClass());
	}
	
	private DynClass registerDynClass() {
	   	DynObjectManager dynman = ToolsLocator.getDynObjectManager();
    	DynClass dynClass = dynman.get(DYNCLASS_NAME);
    	DynField field;
    	if (dynClass == null) {
    		dynClass = dynman.add(DYNCLASS_NAME);

    		field = dynClass.addDynField(DYNFIELDNAME_URL);
    		field.setType(DataTypes.STRING);
    		field.setDescription("Path of the remote service");
    		field.setTheTypeOfAvailableValues(DynField.SINGLE);
    		field.setMandatory(true);
    		
    		field = dynClass.addDynField(DYNFIELDNAME_VERSION);
    		field.setType(DataTypes.STRING);
    		field.setDescription("Version of the remote service");
    		field.setTheTypeOfAvailableValues(DynField.SINGLE);
    		field.setMandatory(false);
    	}
    	return dynClass;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.DataServerExplorerParameters#getExplorerName()
	 */
	public String getExplorerName() {
		return WFSServerExplorer.NAME;
	}

	public String getUrl(){
		return (String) this.getDynValue(DYNFIELDNAME_URL);
	}

	public void setUrl(String url){
		this.setDynValue(DYNFIELDNAME_URL, url);
	}
	
	public String getVersion(){
		return (String) this.getDynValue(DYNFIELDNAME_VERSION);
	}

	public void setVersion(String version){
		this.setDynValue(DYNFIELDNAME_VERSION, version);
	}

}

