/* gvSIG. Sistema de Informaciï¿½n Geogrï¿½fica de la Generalitat Valenciana
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

package com.iver.ai2.animationgui.gui.document;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.gvsig.gui.beans.swing.GridBagLayoutPanel;
import org.gvsig.gui.beans.swing.JButton;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.SingletonWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.iver.cit.gvsig.project.documents.ProjectDocument;

/**
 * Dialog where are all the view properties
 *  
 * @author Ángel Fraile Griñán  e-mail: angel.fraile@iver.es
 * @since 1.1
 * 
 */
public class AnimationProperties extends GridBagLayoutPanel implements
		SingletonWindow {

	
	private static final long serialVersionUID = 1L;

	private JTextField txtName;

	private JTextField txtDate;

	private JTextField txtOwner;

	private int width = 390;

	private int height = 250;

	private JScrollPane jScrollPane;

	private JTextArea txtComments;
	
	private GridBagLayoutPanel okCancelPanel;
	
	private boolean editable;

	private JButton okButton;

	private JButton cancelButton;

	private ProjectDocument projectDocument;

	/**
	 * Default constructor
	 * 
	 * @param ProjectDocument: Actual project document
	 * @param boolean:
	 */
	public AnimationProperties(ProjectDocument p, boolean edit) {
		projectDocument = p;
		setEditable(edit);
		initialize();
	}

	/**
	 * This method initializes this
	 */
	private void initialize() {
		// Inicialize component
		setName("Animation properties");
		// Introducing the margin 
		setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
		// Dimension of the panel
//		setSize(new Dimension(width, height));
		this.setPreferredSize(new Dimension(width, height));
		
		// ADDING COMPONENTS
		
		// Name Component
		this.addComponent(PluginServices.getText(this, "Name"), getTxtName(), new Insets(0,10,2,10));
		// Date component
		this.addComponent(PluginServices.getText(this, "Creation_Date"), getTxtDate(), new Insets(2,10,2,10));
		// Owner component
		this.addComponent(PluginServices.getText(this, "Owner"), getTxtOwner(), new Insets(2,10,2,10));
		// Description component
		GridBagLayoutPanel aux = new GridBagLayoutPanel();
		aux.add(getJScrollPane());
		addComponent(new JLabel(PluginServices.getText(this, "Commentaries")),new Insets(2,6,2,6));
		addComponent(aux,new Insets(2,6,2,6));
		
		
		// Accept buton
		this.addComponent(getOkCancelPanel(),new Insets(2,150,2,0));
		
		// Inicialicing default values
		this.txtName.setText(PluginServices.getText(this, "Creation_Date"));
		txtName.setText(projectDocument.getName());
		txtDate.setText(projectDocument.getCreationDate());
		txtOwner.setText(projectDocument.getOwner());
		txtComments.setText(projectDocument.getComment());
		
	}
	
	
	private javax.swing.JTextField getTxtName() {
		if (txtName == null) {
			txtName = new javax.swing.JTextField();
			txtName.setPreferredSize(new java.awt.Dimension(150, 23));
		}

		return txtName;
	}

	private javax.swing.JTextField getTxtDate() {
		if (txtDate == null) {
			txtDate = new javax.swing.JTextField();
			txtDate.setPreferredSize(new java.awt.Dimension(150, 23));
			txtDate.setEditable(false);
			txtDate.setBackground(java.awt.Color.white);
		}

		return txtDate;
	}

	private javax.swing.JTextField getTxtOwner() {
		if (txtOwner == null) {
			txtOwner = new javax.swing.JTextField();
			txtOwner.setPreferredSize(new java.awt.Dimension(150, 23));
		}

		return txtOwner;
	}

	
		
	private javax.swing.JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new javax.swing.JScrollPane();
			//jScrollPane.setSize(new java.awt.Dimension(340, 70));
			jScrollPane.setViewportView(getTxtComments());
			jScrollPane.setPreferredSize(new java.awt.Dimension(340, 70));
		}

		return jScrollPane;
	}
	
	private javax.swing.JTextArea getTxtComments() {
		if (txtComments == null) {
			txtComments = new javax.swing.JTextArea();
			txtComments.setRows(1);
			txtComments.setColumns(28);
		}

		return txtComments;
	}
	
	
	private GridBagLayoutPanel getOkCancelPanel() {
		if (okCancelPanel == null) {
			ActionListener okAction, cancelAction;
			okAction = new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					projectDocument.setName(txtName.getText());
					projectDocument.setCreationDate(txtDate.getText());
					projectDocument.setOwner(txtOwner.getText());
					projectDocument.setComment(txtComments.getText());
					PluginServices.getMDIManager().closeWindow(AnimationProperties.this);
				}
			};
			cancelAction = new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					PluginServices.getMDIManager().closeWindow(AnimationProperties.this);
				}
			};
			
			okCancelPanel = new GridBagLayoutPanel();
			okCancelPanel.setAlignmentX(GridBagLayoutPanel.RIGHT_ALIGNMENT);
			okButton = new JButton();
			okButton.setText(PluginServices.getText(this, "Accept"));
			okButton.addActionListener(okAction);
			cancelButton = new JButton();
			cancelButton.setText(PluginServices.getText(this, "Cancel"));
			cancelButton.addActionListener(cancelAction);
			
			okCancelPanel.addComponent(okButton,cancelButton);
		}
		return okCancelPanel;
	}
	
	
	public Object getWindowModel() {
		return projectDocument;
	}

	/**
	 * 
	 * @see com.iver.andami.ui.mdiManager.IWindow#getWindowInfo()
	 * @return WindowInfo: window parameters
	 */
	public WindowInfo getWindowInfo() {
		WindowInfo m_viewinfo = new WindowInfo();
		m_viewinfo.setTitle("Animation Properties");
		m_viewinfo.setHeight(height);
		m_viewinfo.setWidth(width);
		return m_viewinfo;
	}
	
	public Object getWindowProfile() {
		return WindowInfo.TOOL_PROFILE;
	}

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}
}