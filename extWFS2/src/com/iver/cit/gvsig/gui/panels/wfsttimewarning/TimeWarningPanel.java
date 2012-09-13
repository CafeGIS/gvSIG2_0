package com.iver.cit.gvsig.gui.panels.wfsttimewarning;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.gvsig.gui.beans.swing.JButton;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.gui.panels.wfstclock.ClockText;

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
/* CVS MESSAGES:
 *
 * $Id$
 * $Log$
 *
 */
/**
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public class TimeWarningPanel extends JPanel {
	public static final String ACCEPTBUTTON_ACTIONCOMMAND = "ac";
	public static final String CANCELBUTTON_ACTIONCOMMAND = "an";
	private JPanel buttonsPanel;
	private JLabel expiryLabel;
	private ClockText expiryText;
	private JPanel expiryTimePanel;
	private JButton acceptButton;
	private JButton cancelButton;
	private JPanel queryPanel;
	private JTextArea messageArea;
	private JScrollPane messagePanel;
	private JPanel srsBasedOnXMLPanel;
	private JCheckBox srsBasedOnXMLCheck;
	private JPanel lockGeometriesPanel;
	private JCheckBox lockGeometriesCheck;
	
	public TimeWarningPanel(){
		initialize();
	}

	/**
	 * Initialize the components
	 */
	private void initialize(){
		GridBagConstraints gridBagConstraints;
		setLayout(new java.awt.GridBagLayout());

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(5, 2, 5, 2);
		add(getMessagePanel(), gridBagConstraints);		

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(5, 2, 5, 2);
		add(getExpiryTimePanel(), gridBagConstraints);	
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(5, 2, 5, 2);
		add(getLockGeometriesPanel(), gridBagConstraints);	
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(5, 2, 5, 2);
		add(getUpdateGeometriesPanel(), gridBagConstraints);	
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(5, 2, 5, 2);
		gridBagConstraints.anchor = gridBagConstraints.EAST;
		add(getButtonsPanel(), gridBagConstraints);
	}

	/**
	 * @return the buttonsPanel
	 */
	private JPanel getLockGeometriesPanel() {
		if (lockGeometriesPanel == null){
			lockGeometriesPanel = new JPanel();
			lockGeometriesPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 3, 0));
			lockGeometriesPanel.add(getLockGeometriesCheck());			
		}
		return lockGeometriesPanel;
	}	

	/**
	 * @return the updateGeometriesCheck
	 */
	private JCheckBox getLockGeometriesCheck() {
		if (lockGeometriesCheck == null){
			lockGeometriesCheck = new JCheckBox();
			lockGeometriesCheck.setSelected(true);
			lockGeometriesCheck.setText(PluginServices.getText(this, "wfst_support_lockfeature"));
		}
		return lockGeometriesCheck;
	}	
	
	/**
	 * @return the buttonsPanel
	 */
	private JPanel getUpdateGeometriesPanel() {
		if (srsBasedOnXMLPanel == null){
			srsBasedOnXMLPanel = new JPanel();
			srsBasedOnXMLPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 3, 0));
			srsBasedOnXMLPanel.add(getSrsBasedOnXMLCheck());			
		}
		return srsBasedOnXMLPanel;
	}
	

	/**
	 * @return the srsBasedOnXMLCheck
	 */
	private JCheckBox getSrsBasedOnXMLCheck() {
		if (srsBasedOnXMLCheck == null){
			srsBasedOnXMLCheck = new JCheckBox();
			srsBasedOnXMLCheck.setSelected(true);
			srsBasedOnXMLCheck.setText(PluginServices.getText(this, "wfst_srs_based_on_XML"));
		}
		return srsBasedOnXMLCheck;
	}
	
	/**
	 * @return the buttonsPanel
	 */
	private JPanel getButtonsPanel() {
		if (buttonsPanel == null){
			buttonsPanel = new JPanel();
			buttonsPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 3, 0));
			buttonsPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 4));
			buttonsPanel.add(getAcceptButton());
			buttonsPanel.add(getCancelButton());
		}
		return buttonsPanel;
	}

	/**
	 * @return the expiryLabel
	 */
	private JLabel getExpiryLabel() {
		if (expiryLabel == null){
			expiryLabel = new JLabel();
			expiryLabel.setText(PluginServices.getText(this, "wfst_minutes_to_expiry") + ": ");			
		}
		return expiryLabel;
	}

	/**
	 * @return the expiryText
	 */
	private ClockText getExpiryText() {
		if (expiryText == null){
			expiryText = new ClockText();
			expiryText.setPreferredSize(new Dimension(50, 19));
		
		}
		return expiryText;
	}

	/**
	 * @return the expiryTimePanel
	 */
	private JPanel getExpiryTimePanel() {
		if (expiryTimePanel == null){
			expiryTimePanel = new JPanel();
			expiryTimePanel.setLayout(new java.awt.BorderLayout());
			expiryTimePanel.add(getQueryPanel(), java.awt.BorderLayout.WEST);
		}
		return expiryTimePanel;
	}

	/**
	 * @return the jButton1
	 */
	private JButton getAcceptButton() {
		if (acceptButton == null){
			acceptButton = new JButton();
			acceptButton.setText(PluginServices.getText(this, "aceptar"));
			acceptButton.setActionCommand(ACCEPTBUTTON_ACTIONCOMMAND);
		}
		return acceptButton;
	}
	
	/**
	 * @return the cancelButton
	 */
	protected JButton getCancelButton() {
		if (cancelButton == null){
			cancelButton = new JButton();
			cancelButton.setText(PluginServices.getText(this, "cancelar"));
			cancelButton.setActionCommand(CANCELBUTTON_ACTIONCOMMAND);
		}
		return cancelButton;
	}
	
	/**
	 * Add a listener for the buttons
	 * @param listener
	 */
	protected void addActionListener(ActionListener listener){
		getAcceptButton().addActionListener(listener);
		getCancelButton().addActionListener(listener);
	}

	/**
	 * @return the jPanel1
	 */
	private JPanel getQueryPanel() {
		if (queryPanel == null){
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints2.gridx = 1;
			gridBagConstraints2.gridy = 0;
			gridBagConstraints2.weightx = 1.0;
			gridBagConstraints2.insets = new Insets(5, 3, 5, 5);
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.insets = new Insets(7, 5, 7, 2);
			gridBagConstraints1.gridy = 0;
			gridBagConstraints1.gridx = 0;
			queryPanel = new JPanel();
			queryPanel.setLayout(new GridBagLayout());
			queryPanel.add(getExpiryLabel(), gridBagConstraints1);
			queryPanel.add(getExpiryText(), gridBagConstraints2);
		}
		return queryPanel;
	}

	/**
	 * @return the messageArea
	 */
	private JTextArea getMessageArea() {
		if (messageArea == null){
			messageArea = new JTextArea();
			messageArea.setEditable(false);
			messageArea.setText(PluginServices.getText(this, "wfst_start_editing_warning"));
			messageArea.setLineWrap(true);
			messageArea.setBackground(null);
		}
		return messageArea;
	}

	/**
	 * @return the messagePanel
	 */
	private JScrollPane getMessagePanel() {
		if (messagePanel == null){
			messagePanel = new JScrollPane();
			messagePanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			messagePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
			messagePanel.setViewportView(getMessageArea());
		}
		return messagePanel;
	}
	
	/**
	 * @return the expiry time
	 */
	public int getExpiryTime(){
		return getExpiryText().getExpiryTime();
	}
	
	/**
	 * @return if the srs is based on XML
	 */
	public boolean isSrsBasedOnXML(){
		return getSrsBasedOnXMLCheck().isSelected();
	}
	
	/**
	 * Select or not select the srs geometries check
	 * @param isUpdated
	 */
	public void setSrsBasedOnXML(boolean isUpdated){
		getSrsBasedOnXMLCheck().setSelected(isUpdated);
	}
	
	/**
	 * @return if the geometries can be locked
	 */
	public boolean isLockGeometries(){
		return getLockGeometriesCheck().isSelected();
	}
	
	/**
	 * Select or not select the lock geometries check
	 * @param isUpdated
	 */
	public void setLockFeatures(boolean isLocked){
		getLockGeometriesCheck().setSelected(isLocked);
	}
}
