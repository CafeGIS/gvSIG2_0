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

import java.util.*;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;

import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.metadata.extended.registry.MDRegistry;
import org.gvsig.metadata.extended.registry.MDRegistryImpl;
import org.gvsig.metadata.extended.registry.objects.MDDefinition;
import org.gvsig.metadata.extended.registry.objects.MDElementDefinition;

import com.iver.andami.PluginServices;



public class MDBasicGUIPanel extends JPanel{

	private static final long serialVersionUID = -2005251151677378280L; 
	private FLayer layer;
	private List<JComponent> uiElements;
	
	
	public MDBasicGUIPanel(FLayer fl) {
		this.layer = fl;
		this.uiElements = new ArrayList<JComponent>();
		
		System.out.println("Metadata for: " + layer.getName());
		
		MDRegistry reg = new MDRegistryImpl();
		MDDefinition mdd = reg.getMDDefinition("FLayer");
		
		setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),
				PluginServices.getText(this, mdd.getName()),
				TitledBorder.DEFAULT_JUSTIFICATION,
				TitledBorder.DEFAULT_POSITION, null, null)
			);
		setLayout(new MigLayout());
		
		Iterator it = mdd.iterator();
		while (it.hasNext()) {
			Map.Entry e = (Map.Entry)it.next();
			add(new JLabel(((MDElementDefinition)e.getValue()).getName() + ": "));
			JTextField auxJTF = new JTextField(20);
			auxJTF.setEnabled(false);
			
			// Provisional, el valor se deberia leer del respectivo atributo de la capa
			auxJTF.setText(((MDElementDefinition)e.getValue()).getDefaultValue().toString());
			
			add(auxJTF, "wrap");
			uiElements.add(auxJTF);
		}
		
	}		


	public void allowEdition() {
		Iterator it = uiElements.iterator();
		while (it.hasNext()) {
			JComponent jc = (JComponent)it.next();
			jc.setEnabled(true);
		}
	}

	public void closeEdition() {
		Iterator it = uiElements.iterator();
		while (it.hasNext()) {
			JComponent jc = (JComponent)it.next();
			jc.setEnabled(false);
		}
	}

	public void saveChanges() {
		// TODO Auto-generated method stub
		
	}
	
}
