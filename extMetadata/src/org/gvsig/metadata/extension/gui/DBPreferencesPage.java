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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import net.miginfocom.swing.MigLayout;

import com.iver.andami.PluginServices;
import com.iver.andami.preferences.AbstractPreferencePage;
import com.iver.andami.preferences.StoreException;

public class DBPreferencesPage extends AbstractPreferencePage implements ActionListener {
	
	private static final long serialVersionUID = -1100950358368412672L;
	private final ImageIcon	icon;
	public static String	id;
	
	private JRadioButton personalButton;
	private JRadioButton corporativeButton;
	
	
	public DBPreferencesPage () {
		super();
		this.setParentID(MDPreferencesPage.id);
		icon = new ImageIcon(this.getClass().getClassLoader().getResource("images/dataBase.png"));
		id = getID();
		
		// tratar como un panel
		setLayout(new MigLayout());
		
		personalButton = new JRadioButton(PluginServices.getText(this, "Personal_DB_Button"));
		personalButton.setActionCommand("personal");
		personalButton.addActionListener(this);
		personalButton.setSelected(true);
		
		corporativeButton = new JRadioButton(PluginServices.getText(this, "Corporative_DB_Button"));
		corporativeButton.setActionCommand("corporative");
		corporativeButton.addActionListener(this);
		corporativeButton.setEnabled(false);
		
		ButtonGroup group = new ButtonGroup();
		group.add(personalButton);
		group.add(corporativeButton);

		add(personalButton, "wrap");
		add(corporativeButton, "wrap");
	}
	
	public String getID () {
		return this.getClass().getName();
	}
	
	public String getTitle () {
		return PluginServices.getText(this, "Base_Datos");
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

	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		// e.getActionCommand() nos devuelve "personal" o "corporative" dependiendo del boton seleccionado
	}
}
