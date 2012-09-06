package org.gvsig.gui.beans.comboboxconfigurablelookup.usertests;

import java.io.Serializable;

import javax.swing.JFrame;

import org.gvsig.gui.beans.Messages;
import org.gvsig.gui.beans.comboboxconfigurablelookup.JComboBoxConfigurableLookUp;


/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
*
* Copyright (C) 2004 IVER T.I. and Generalitat Valenciana.
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
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,USA.
*
* For more information, contact:
*
*  Generalitat Valenciana
*   Conselleria d'Infraestructures i Transport
*   Av. Blasco Ibáñez, 50
*   46010 VALENCIA
*   SPAIN
*
*      +34 963862235
*   gvsig@gva.es
*      www.gvsig.gva.es
*
*    or
*
*   IVER T.I. S.A
*   Salamanca 50
*   46005 Valencia
*   Spain
*
*   +34 963163400
*   dac@iver.es
*/

/**
 * <p>Creates and launches an application for testing {@link JComboBoxConfigurableLookUp JComboBoxConfigurableLookUp}.</p>
 * 
 * @author Pablo Piqueras Bartolomé (pablo.piqueras@iver.es)
 * @version 07/02/2008
 */
public class JFrameUserTestOfJComboBoxConfigurableLookUp implements Serializable{
	private static final long serialVersionUID = 5760643373469048220L;

	private JPanelUserTestOfJComboBoxConfigurableLookUp mainPanel = null;
	private JFrame frame;

	/**
	 * <p>Default constructor.</p>
	 */
	public JFrameUserTestOfJComboBoxConfigurableLookUp() {
		createFrame();
	}

	/**
	 * <p>Creates the main <code>JFrame</code> for execute the application, and adds to it the <code>JPanel</code> test application.</p> 
	 */
	private void createFrame() {
		// Create: Configuration Frame
		frame = new JFrame();
		frame.setResizable(false);
		frame.setTitle(Messages.getText("jComboBoxConfigurableLookUpByTheUser_frameTitle"));
		frame.getContentPane().add(getJPanelUserTestOfJComboBoxConfigurableLookUp());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();

		// Centers the frame in the middle of the screen
		frame.setLocationRelativeTo(null);
	}
	
	/**
	 * <p>Initializes <code>mainPanel</code>.</p>
	 * 
	 * @return JPanelUserTestOfJComboBoxConfigurableLookUp
	 */
	private JPanelUserTestOfJComboBoxConfigurableLookUp getJPanelUserTestOfJComboBoxConfigurableLookUp() {
		if (mainPanel == null) {
			mainPanel = new JPanelUserTestOfJComboBoxConfigurableLookUp();
		}
		
		return mainPanel;
	}

	/**
	 * <p>Method for launch the application.</p>
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		JFrameUserTestOfJComboBoxConfigurableLookUp app = new JFrameUserTestOfJComboBoxConfigurableLookUp();
		app.setVisible(true);
	}

	/**
	 * <p>Sets visible the frame.</p>
	 * 
	 * @param visible True or false
	 */
	public void setVisible(boolean visible) {
		frame.setVisible(visible);
	}	
}
