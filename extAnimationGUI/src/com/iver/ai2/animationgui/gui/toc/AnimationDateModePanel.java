/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
*
* Copyright (C) 2005 IVER T.I. and Generalitat Valenciana.
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
*/

package com.iver.ai2.animationgui.gui.toc;

import java.awt.Choice;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import org.gvsig.gui.beans.swing.GridBagLayoutPanel;
import org.gvsig.gui.beans.swing.JButton;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.SingletonWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.iver.cit.gvsig.animation.IAnimationType;

/**
 * Dialogo donde se muestran las propiedades de una vista
 * 
 * @author Fernando González Cortés
 */
public class AnimationDateModePanel extends GridBagLayoutPanel implements
		SingletonWindow, ItemListener{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int width = 250;

	private int height = 100;

	private GridBagLayoutPanel okCancelPanel;

	private JButton okButton;
	
	private Choice choice = null;

	private int animationMode;

	private int operation;

	private IAnimationType animationDay;

	/**
	 * This is the default constructor
	 * @param animationType 
	 * 
	 */
	public AnimationDateModePanel(IAnimationType animationType) {

		animationDay = animationType;
		initialize();

	}

	/**
	 * This method initializes the panel
	 */
	private void initialize() {
		
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.fill = GridBagConstraints.VERTICAL;
		gridBagConstraints1.gridy = 2;
		gridBagConstraints1.weightx = 1.0;
		gridBagConstraints1.gridx = 1;
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridheight = 2;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.insets = new Insets(15, 0, 0, 0);
		gridBagConstraints.gridy = 3;
		
		this.add(getChoiceValue(), gridBagConstraints1);
		this.add(getOkCancelPanel(),gridBagConstraints);
		
		setName(PluginServices.getText(this, "Transparency"));
		this.setPreferredSize(new Dimension(width, height));
		this.setSize(new Dimension(width, height));

	}

	
	private Choice getChoiceValue() {
		// TODO Auto-generated method stub
		if (choice == null) {
			choice = new Choice();
			// choice.addItem( textopru );
			choice.addItem("Intervalo");
			choice.addItem("Incremental");
			choice.addItem("Decremental");
			// choice.addItem( "modo3" );
			// choice.addItem( "modo4" );
			choice.addItemListener(this);
		}
		return choice;
	}


	private GridBagLayoutPanel getOkCancelPanel() {
		if (okCancelPanel == null) {
			ActionListener okAction;
			okAction = new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					
//					animationDay.setFilterMode(animationMode);
					PluginServices.getMDIManager().closeWindow(
							AnimationDateModePanel.this);
				}
			};
			okCancelPanel = new GridBagLayoutPanel();
			okCancelPanel.setAlignmentX(GridBagLayoutPanel.RIGHT_ALIGNMENT);
			okButton = new JButton();
			okButton.setText("Aceptar");
			okButton.addActionListener(okAction);

			okCancelPanel.addComponent(okButton);
		}
		return okCancelPanel;
	}

	
	/**
	 * 
	 * @see com.iver.andami.ui.mdiManager.IWindow#getWindowInfo()
	 * @return WindowInfo: window parameters
	 */

	public WindowInfo getWindowInfo() {
		WindowInfo m_viewinfo = new WindowInfo();
		m_viewinfo.setTitle(PluginServices.getText(this, "Transparency"));
		m_viewinfo.setHeight(height);
		m_viewinfo.setWidth(width);
		return m_viewinfo;
	}

	public Object getWindowModel() {
		return null;
	}

	public void itemStateChanged(ItemEvent event) {
		Object obj = event.getSource();
		if (obj == choice) {
			operation = choice.getSelectedIndex(); // get select item.
			getMensaje(operation);
		} 
		
	}
	private void getMensaje(int option) {
		String mode = choice.getItem(option);
//		if (mode.equals("Intervalo")) {
//			animationMode = DateFilter.BOTH;
//		} else if (mode.equals("Incremental")) {
//			animationMode = DateFilter.BEFORE_BEGIN;
//		} else if (mode.equals("Decremental")) {
//			animationMode = DateFilter.AFTER_END;
//		}
		System.out.println("opcion: " + mode);
	}

	public Object getWindowProfile() {
		// TODO Auto-generated method stub
		return WindowInfo.TOOL_PROFILE;
	}


}
