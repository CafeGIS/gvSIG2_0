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

package org.gvsig.metadata.extension.gui;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import com.iver.andami.PluginServices;
import com.iver.andami.preferences.AbstractPreferencePage;
import com.iver.andami.preferences.StoreException;

public class MDPreferencesPage extends AbstractPreferencePage {
	
	private static final long serialVersionUID = 2640567471202937755L;
	private final ImageIcon	icon;
	public static String	id;
	
	public MDPreferencesPage () {
		super();
		icon = new ImageIcon(this.getClass().getClassLoader().getResource("images/metadata-emblem.png"));
		id = getID();
	}
	
	public String getID () {
		return this.getClass().getName();
	}
	
	public String getTitle () {
		return PluginServices.getText(this, "Metadata");
	}
	
	public JPanel getPanel () {
		return this;
	}
	
	public ImageIcon getIcon () {
		return icon;
	}
	
	public void setChangesApplied () {
		// TODO Auto-generated method stub
		
	}
	
	public void storeValues () throws StoreException {
		// TODO Auto-generated method stub
		
	}
	
	public void initializeDefaults () {
		// TODO Auto-generated method stub
		
	}
	
	public void initializeValues () {
		// TODO Auto-generated method stub
		
	}
	
	public boolean isValueChanged () {
		// TODO Auto-generated method stub
		return false;
	}

}
