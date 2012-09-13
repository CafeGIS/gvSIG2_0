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
* 2008 Geographic Information research group: http://www.geoinfo.uji.es
* Departamento de Lenguajes y Sistemas Informáticos (LSI)
* Universitat Jaume I   
* {{Task}}
*/



package org.gvsig.metadata.extended.manager;

import org.gvsig.metadata.Metadata;
import org.gvsig.metadata.extended.ExtendedMDManager;
import org.gvsig.metadata.extended.exchanger.MDExchanger;
import org.gvsig.metadata.extended.exchanger.XSLTExchanger;
import org.gvsig.metadata.extended.persistence.H2Persistence;
import org.gvsig.metadata.extended.persistence.MDPersistence;


public class ExtendedMDManagerImpl implements ExtendedMDManager {
	
	MDExchanger mde = null;
	MDPersistence mdp = null;
	
	public ExtendedMDManagerImpl() {
		mde = new XSLTExchanger();
		mdp = new H2Persistence();
	}


	public String exportMD(Metadata md, String format) {
        // TODO Implement
        throw new UnsupportedOperationException("Not implemented");
	}
	
	public Metadata importMD(Metadata md, String filePath) {
        // TODO Implement
        throw new UnsupportedOperationException("Not implemented");
	}

    public void loadMD(Metadata metadata) {
        // TODO Implement
        throw new UnsupportedOperationException("Not implemented");
    }

    public void storeMD(Metadata metadata, boolean storeChildren) {
        // TODO Implement
        throw new UnsupportedOperationException("Not implemented");
    }
}