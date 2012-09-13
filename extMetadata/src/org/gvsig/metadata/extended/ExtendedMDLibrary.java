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

package org.gvsig.metadata.extended;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.gvsig.metadata.MDLocator;
import org.gvsig.metadata.MDManager;
import org.gvsig.metadata.extended.manager.ExtendedMDManagerImpl;
import org.gvsig.metadata.extended.registry.MDRegistry;
import org.gvsig.metadata.extended.registry.MDRegistryImpl;
import org.gvsig.metadata.extended.registry.objects.MDDefinition;
import org.gvsig.metadata.extended.registry.objects.MDElementDefinition;
import org.gvsig.personaldb.PersonalDBLocator;
import org.gvsig.tools.locator.BaseLibrary;
import org.gvsig.tools.locator.ReferenceNotRegisteredException;

public class ExtendedMDLibrary extends BaseLibrary {

	public void initialize() {
        super.initialize();
        MDLocator.registerMDManager(ExtendedMDManagerImpl.class);
    }
	
	public void postInitialize() {
        super.postInitialize();
        // CHECK if extended implementation was installed.
		MDManager manager = MDLocator.getMDManager();
        if ( !(manager instanceof ExtendedMDManagerImpl) ) {
        	System.out.println("Extended Metadata Implementation installation FAILED.");
        	throw new ReferenceNotRegisteredException(MDLocator.METADATA_MANAGER_NAME, MDLocator.getInstance());
        }
        
        // INITIALIZE Data Base
        dataBaseInit();
	}
	
	
	// Posibilidad de hacerlo desde ANT:
	// Creating Tables Using ANT (http://java.sun.com/docs/books/tutorial/jdbc/basics/tables.html)
	public void dataBaseInit() {
		Connection conn = PersonalDBLocator.getInstance().getPersonalDBManager().getConnection();
		String sql = "CREATE TABLE IF NOT EXISTS MDDEFINITIONS(ID INT AUTO_INCREMENT(1) PRIMARY KEY, NAME VARCHAR2, DESCRIPTION LONGVARCHAR, FATHER INT DEFAULT 0, EXPORT_NAME VARCHAR2 DEFAULT '');";
		String sql2 = "CREATE TABLE IF NOT EXISTS MDELEMENTDEFINITIONS(ID INT AUTO_INCREMENT(1) PRIMARY KEY, NAME VARCHAR2, DESCRIPTION LONGVARCHAR, REQUIRED BOOLEAN DEFAULT true, FATHER INT DEFAULT 0, EXPORT_NAME VARCHAR2 DEFAULT '', TIPO VARCHAR2 DEFAULT 'String', DEFAULT_VALUE VARCHAR2 DEFAULT '');";
		PreparedStatement prep;
		try {
			prep = conn.prepareStatement(sql);
			prep.execute();
			prep = conn.prepareStatement(sql2);
			prep.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// PROVISIONAL (llenado de tablas) ----------------------------------------------------------
		// PROBLEMA: se insertan cada vez como nuevas.
		MDDefinition mdd = new MDDefinition("FLayer", "Testing");
		MDElementDefinition mded1 = new MDElementDefinition("NombreCapa", "El Nombre de la capa", true, mdd, "String", "Capa1");
		mdd.addMDElementDefinition(mded1);
		MDElementDefinition mded2 = new MDElementDefinition("Autor", "El Nombre del Autor", true, mdd, "String", "Arturo");
		mdd.addMDElementDefinition(mded2);
		MDElementDefinition mded3 = new MDElementDefinition("Numero", "Un número", true, mdd, "Double", "3.1415");
		mdd.addMDElementDefinition(mded3);
		
		MDRegistry reg = new MDRegistryImpl();
		reg.setMDDefinition(mdd);
	}
	
}
