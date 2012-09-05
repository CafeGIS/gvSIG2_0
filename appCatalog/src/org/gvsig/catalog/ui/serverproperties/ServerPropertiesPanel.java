package org.gvsig.catalog.ui.serverproperties;

import java.awt.event.ActionListener;
import java.util.Properties;

import javax.swing.JPanel;

import org.gvsig.catalog.drivers.profiles.IProfile;
import org.gvsig.catalog.utils.CatalogConstants;
import org.gvsig.i18n.Messages;

import com.iver.utiles.swing.jcomboServer.ServerData;


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
public class ServerPropertiesPanel extends JPanel{
	private ServerData serverData = null;
	private IProfile profile = null;

	private javax.swing.JLabel abstractLabel;
	private javax.swing.JTextField abstractText;
	private javax.swing.JPanel buttonsPanel;
	private javax.swing.JButton cancelButton;
	private javax.swing.JLabel cathegoryLabel;
	private javax.swing.JTextField cathegoryText;
	private javax.swing.JButton closeButton;
	private javax.swing.JPanel componetsPanel;
	private javax.swing.JLabel coordinatesLabel;
	private javax.swing.JTextField coordinatesText;
	private javax.swing.JLabel dateFromLabel;
	private javax.swing.JTextField dateFromText;
	private javax.swing.JLabel dateToLabel;
	private javax.swing.JTextField dateToText;
	private javax.swing.JLabel elementLabelText;
	private javax.swing.JTextField elementNameText;
	private javax.swing.JLabel keywordLabel;
	private javax.swing.JTextField keywordsText;
	private javax.swing.JPanel labelsPanel;
	private javax.swing.JLabel providerLabel;
	private javax.swing.JTextField providerText;
	private javax.swing.JLabel scaleLabel;
	private javax.swing.JTextField scaleText;
	private javax.swing.JPanel textsPanel;
	private javax.swing.JLabel titleLabel;
	private javax.swing.JTextField titleText;

	public ServerPropertiesPanel(ServerData serverData, IProfile profile){
		this.serverData = serverData; 
		this.profile = profile;
		initializeComponents();
		initializeLabels();
		initServerFields();
		initButtonSize();
		completeFieldsWithProfile();		
	}

	/**
	 * It initializes the graphical components
	 */
	private void initializeComponents(){
		java.awt.GridBagConstraints gridBagConstraints;

		componetsPanel = new javax.swing.JPanel();
		labelsPanel = new javax.swing.JPanel();
		titleText = new javax.swing.JTextField();
		titleLabel = new javax.swing.JLabel();
		abstractLabel = new javax.swing.JLabel();
		keywordLabel = new javax.swing.JLabel();
		cathegoryLabel = new javax.swing.JLabel();
		scaleLabel = new javax.swing.JLabel();
		providerLabel = new javax.swing.JLabel();
		coordinatesLabel = new javax.swing.JLabel();
		dateFromLabel = new javax.swing.JLabel();
		dateToLabel = new javax.swing.JLabel();
		elementLabelText = new javax.swing.JLabel();
		abstractText = new javax.swing.JTextField();
		keywordsText = new javax.swing.JTextField();
		cathegoryText = new javax.swing.JTextField();
		scaleText = new javax.swing.JTextField();
		providerText = new javax.swing.JTextField();
		coordinatesText = new javax.swing.JTextField();
		dateFromText = new javax.swing.JTextField();
		dateToText = new javax.swing.JTextField();
		elementNameText = new javax.swing.JTextField();
		buttonsPanel = new javax.swing.JPanel();
		cancelButton = new javax.swing.JButton();
		closeButton = new javax.swing.JButton();

		setLayout(new java.awt.BorderLayout());

		componetsPanel.setLayout(new java.awt.GridBagLayout());

		componetsPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
		labelsPanel.setLayout(new java.awt.GridBagLayout());

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(3, 2, 3, 2);
		labelsPanel.add(titleText, gridBagConstraints);

		titleLabel.setText("jLabel1");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		gridBagConstraints.insets = new java.awt.Insets(5, 2, 5, 2);
		labelsPanel.add(titleLabel, gridBagConstraints);

		abstractLabel.setText("jLabel2");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		gridBagConstraints.insets = new java.awt.Insets(5, 2, 5, 2);
		labelsPanel.add(abstractLabel, gridBagConstraints);

		keywordLabel.setText("jLabel3");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		gridBagConstraints.insets = new java.awt.Insets(5, 2, 5, 2);
		labelsPanel.add(keywordLabel, gridBagConstraints);

		cathegoryLabel.setText("jLabel4");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		gridBagConstraints.insets = new java.awt.Insets(5, 2, 5, 2);
		labelsPanel.add(cathegoryLabel, gridBagConstraints);

		scaleLabel.setText("jLabel5");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		gridBagConstraints.insets = new java.awt.Insets(5, 2, 5, 2);
		labelsPanel.add(scaleLabel, gridBagConstraints);

		providerLabel.setText("jLabel6");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		gridBagConstraints.insets = new java.awt.Insets(5, 2, 5, 1);
		labelsPanel.add(providerLabel, gridBagConstraints);

		coordinatesLabel.setText("jLabel7");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 6;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		gridBagConstraints.insets = new java.awt.Insets(5, 2, 5, 2);
		labelsPanel.add(coordinatesLabel, gridBagConstraints);

		dateFromLabel.setText("jLabel8");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 7;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		gridBagConstraints.insets = new java.awt.Insets(5, 2, 5, 2);
		labelsPanel.add(dateFromLabel, gridBagConstraints);

		dateToLabel.setText("jLabel9");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 8;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		gridBagConstraints.insets = new java.awt.Insets(5, 2, 5, 2);
		labelsPanel.add(dateToLabel, gridBagConstraints);

		elementLabelText.setText("jLabel10");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 9;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		gridBagConstraints.insets = new java.awt.Insets(5, 2, 5, 2);
		labelsPanel.add(elementLabelText, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(3, 2, 3, 2);
		labelsPanel.add(abstractText, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(3, 2, 3, 2);
		labelsPanel.add(keywordsText, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(3, 2, 3, 2);
		labelsPanel.add(cathegoryText, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(3, 2, 3, 2);
		labelsPanel.add(scaleText, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(3, 2, 3, 2);
		labelsPanel.add(providerText, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 6;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(3, 2, 3, 2);
		labelsPanel.add(coordinatesText, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 7;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(3, 2, 3, 2);
		labelsPanel.add(dateFromText, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 8;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(3, 2, 3, 2);
		labelsPanel.add(dateToText, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 9;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(3, 2, 3, 2);
		labelsPanel.add(elementNameText, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		componetsPanel.add(labelsPanel, gridBagConstraints);

		add(componetsPanel, java.awt.BorderLayout.NORTH);

		buttonsPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

		buttonsPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 5, 5, 5));
		cancelButton.setText("cancel");
		buttonsPanel.add(cancelButton);

		closeButton.setText("close");
		buttonsPanel.add(closeButton);

		add(buttonsPanel, java.awt.BorderLayout.SOUTH);
	}

	/**
	 * Initializes the labels
	 */
	private void initializeLabels() {
		titleLabel.setText(Messages.getText("title") + ":");
		abstractLabel.setText(Messages.getText("abstract") + ":");
		keywordLabel.setText(Messages.getText("keyWords") + ":");
		cathegoryLabel.setText(Messages.getText("cathegory") + ":");
		scaleLabel.setText(Messages.getText("scale") + ":");
		providerLabel.setText(Messages.getText("provider") + ":");
		coordinatesLabel.setText(Messages.getText("coordinates") + ":");
		dateFromLabel.setText(Messages.getText("from"));
		dateToLabel.setText(Messages.getText("to"));	
		elementLabelText.setText(Messages.getText("elementName") + ":");
		closeButton.setText(Messages.getText("aceptar"));
		cancelButton.setText(Messages.getText("cancel"));
	}

	/**
	 * Initializes the text boxes with the server values
	 */
	private void initServerFields() {
		titleText.setText(serverData.getProperty(IProfile.TITLE));
		abstractText.setText(serverData.getProperty(IProfile.ABSTRACT));
		keywordsText.setText(serverData.getProperty(IProfile.KEYWORDS));
		cathegoryText.setText(serverData.getProperty(IProfile.CATHEGORY));
		scaleText.setText(serverData.getProperty(IProfile.SCALE));
		providerText.setText(serverData.getProperty(IProfile.PROVIDER));
		coordinatesText.setText(serverData.getProperty(IProfile.COORDINATES));
		dateFromText.setText(serverData.getProperty(IProfile.DATEFROM));
		dateToText.setText(serverData.getProperty(IProfile.DATETO));
		elementNameText.setText(serverData.getProperty(IProfile.ELEMENT_NAME));
	}

	/**
	 * Complete the blank fields with the profile
	 */
	private void completeFieldsWithProfile(){
		if (profile != null){
			if ((titleText.getText() == null) || (titleText.getText().compareTo("") == 0)){
				titleText.setText(profile.getTitleProperty());
			}
			if ((abstractText.getText() == null) || (abstractText.getText().compareTo("") == 0)){
				abstractText.setText(profile.getAbstractProperty());
			}
			if ((keywordsText.getText() == null) || (keywordsText.getText().compareTo("") == 0)){
				keywordsText.setText(profile.getKeywordsProperty());
			}
			if ((cathegoryText.getText() == null) || (cathegoryText.getText().compareTo("") == 0)){
				cathegoryText.setText(profile.getTopicProperty());
			}
			if ((scaleText.getText() == null) || (scaleText.getText().compareTo("") == 0)){
				scaleText.setText(profile.getScaleProperty());
			}
			if ((providerText.getText() == null) || (providerText.getText().compareTo("") == 0)){
				providerText.setText(profile.getProviderProperty());
			}
			if ((coordinatesText.getText() == null) || (coordinatesText.getText().compareTo("") == 0)){
				coordinatesText.setText(profile.getCoordinatesProperty());
			}
			if ((dateFromText.getText() == null) || (dateFromText.getText().compareTo("") == 0)){
				dateFromText.setText(profile.getDateFromProperty());
			}
			if ((dateToText.getText() == null) || (dateToText.getText().compareTo("") == 0)){
				dateToText.setText(profile.getDateToProperty());
			}		
			if ((elementNameText.getText() == null) || (elementNameText.getText().compareTo("") == 0)){
				elementNameText.setText(profile.getElementNameProperty());
			}		
		}
	}

	/**
	 * Initialize the buttons size
	 */
	private void initButtonSize(){
		cancelButton.setPreferredSize(CatalogConstants.BUTTON_SIZE);
		closeButton.setPreferredSize(CatalogConstants.BUTTON_SIZE);
	}

	/**
	 * @return the serverData
	 */
	protected ServerData updateServerData() {
		Properties properties = serverData.getProperies();
		if (properties == null){
			properties = new Properties();
		}
		updateProperty(titleText.getText(),profile.getTitleProperty(),IProfile.TITLE,properties);
		updateProperty(abstractText.getText(),profile.getAbstractProperty(),IProfile.ABSTRACT,properties);
		updateProperty(keywordsText.getText(),profile.getKeywordsProperty(),IProfile.KEYWORDS,properties);
		updateProperty(cathegoryText.getText(),profile.getTopicProperty(),IProfile.CATHEGORY,properties);
		updateProperty(providerText.getText(),profile.getProviderProperty(),IProfile.PROVIDER,properties);
		updateProperty(scaleText.getText(),profile.getScaleProperty(),IProfile.SCALE,properties);
		updateProperty(coordinatesText.getText(),profile.getCoordinatesProperty(),IProfile.COORDINATES,properties);
		updateProperty(dateFromText.getText(),profile.getDateFromProperty(),IProfile.DATEFROM,properties);
		updateProperty(dateToText.getText(),profile.getDateToProperty(),IProfile.DATETO,properties);
		updateProperty(elementNameText.getText(), profile.getElementNameProperty(), IProfile.ELEMENT_NAME, properties);
		serverData.setProperies(properties);
		return serverData;
	}	

	/**
	 * Updates a property
	 * @param text
	 * @param profileValue
	 * @param key
	 * @param properties
	 */
	private void updateProperty(String text, String profileValue, String key, Properties properties){
		if ((text != null) && (!text.equals(""))){
			if (profileValue == null){
				properties.setProperty(key,text);
			}else if (!(text.compareTo(profileValue) == 0)){
				properties.setProperty(key,text);
			}
		}
	}

	/**
	 * Set the listener for the buttons
	 * @param listener
	 */
	public void addActionListener(ActionListener listener){
		closeButton.setActionCommand(CatalogConstants.CLOSE_BUTTON_ACTION_COMMAND);
		closeButton.addActionListener(listener);
		cancelButton.setActionCommand(CatalogConstants.CANCEL_BUTTON_ACTION_COMMAND);
		cancelButton.addActionListener(listener);
	}


}
