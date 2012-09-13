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


package org.gvsig.metadata.extended.persistence;


import java.sql.*;
import java.util.Iterator;
import java.util.Map;

import org.gvsig.metadata.Metadata;
import org.gvsig.metadata.extended.registry.objects.MDDefinition;
import org.gvsig.metadata.extended.registry.objects.MDElementDefinition;
import org.gvsig.personaldb.PersonalDBLocator;


public class H2Persistence implements MDPersistence{
	
	private Connection conn = null;
	
	public H2Persistence() {
		conn = PersonalDBLocator.getInstance().getPersonalDBManager().getConnection();
	}

	public boolean recoverMD(Metadata md) {
		return false;
	}
	
	public boolean queryMD(String s) {
		return false;
	}

	public boolean storeMD(Metadata md) {
		return false;
	}

	public MDDefinition getMDDefinition(String name) {
		
		MDDefinition mdd = null;
		
		String sql = "SELECT * FROM MDDEFINITIONS WHERE NAME=?";
		PreparedStatement prep;
		try {
			prep = conn.prepareStatement(sql);
			prep.setString(1, name);
			ResultSet rs = prep.executeQuery();
			rs.next();
			mdd = new MDDefinition(rs.getInt("ID"), rs.getString("NAME"), rs.getString("DESCRIPTION"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		mdd.setChanged(false);
		mdd.setPersisted(true);
		
		sql = "SELECT * FROM MDELEMENTDEFINITIONS WHERE FATHER=?";
		try {
			prep = conn.prepareStatement(sql);
			prep.setInt(1, mdd.getId());
			ResultSet rs = prep.executeQuery();
			while(rs.next()) {
				MDElementDefinition mded = new MDElementDefinition(rs.getInt("ID"), rs.getString("NAME"), rs.getString("DESCRIPTION"), rs.getBoolean("REQUIRED"), mdd, rs.getString("TIPO"), rs.getString("DEFAULT_VALUE"));
				mded.setChanged(false);
				mded.setPersisted(true);
				mdd.addMDElementDefinition(mded);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return mdd;
	}

	public boolean setMDDefinition(MDDefinition mdd) {
		if(mdd.isChanged()) {
			String sql;
			if(mdd.isPersisted())
				sql = "UPDATE MDDEFINITIONS SET NAME=?, DESCRIPTION=? WHERE ID=?";
			else
				sql = "INSERT INTO MDDEFINITIONS ( NAME, DESCRIPTION ) VALUES (?, ?)";
			
			PreparedStatement prep;
			try {
				prep = conn.prepareStatement(sql);
				prep.setString(1, mdd.getName());
				prep.setString(2, mdd.getDescription());
				if(mdd.isPersisted()) 
					prep.setInt(3, mdd.getId());
				prep.executeUpdate();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			// Obtain new mdd id
			if(mdd.getId() == 0) {
				sql = "SELECT * FROM MDDEFINITIONS WHERE NAME=?";
				try {
					prep = conn.prepareStatement(sql);
					prep.setString(1, mdd.getName());
					ResultSet rs = prep.executeQuery();
					rs.next();
					mdd.setId(rs.getInt("ID"));
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			// Persist its elements
			Iterator it = mdd.iterator();
			while (it.hasNext()) {
				Map.Entry e = (Map.Entry)it.next();
				setMDElementDefinition((MDElementDefinition)e.getValue());
			}
			return true;
		} else
			return false;
	}

	public boolean setMDElementDefinition(MDElementDefinition mded) {
		if(mded.isChanged()) {
			String sql;
			if(mded.isPersisted())
				sql = "UPDATE MDELEMENTDEFINITIONS SET NAME=?, DESCRIPTION=?, REQUIRED=?, FATHER=?, TIPO=?, DEFAULT_VALUE=? WHERE ID=?";
			else
				sql = "INSERT INTO MDELEMENTDEFINITIONS (NAME, DESCRIPTION, REQUIRED, FATHER, TIPO, DEFAULT_VALUE) VALUES (?, ?, ?, ?, ?, ?)";

			PreparedStatement prep;
			try {
				prep = conn.prepareStatement(sql);
				prep.setString(1, mded.getName());
				prep.setString(2, mded.getDescription());
				prep.setBoolean(3, mded.isRequired());
				prep.setInt(4, mded.getFather().getId());
				prep.setString(5, mded.getType());
				prep.setString(6, mded.getDefaultValue().toString());
				if(mded.isPersisted()) 
					prep.setInt(7, mded.getId());
				prep.executeUpdate();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;
		} else
			return false;
	}

}