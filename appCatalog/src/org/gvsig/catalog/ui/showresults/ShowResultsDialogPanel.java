
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
package org.gvsig.catalog.ui.showresults;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import org.gvsig.catalog.CatalogClient;
import org.gvsig.catalog.drivers.GetRecordsReply;
import org.gvsig.catalog.schemas.Record;
import org.gvsig.catalog.schemas.Resource;
import org.gvsig.catalog.ui.chooseresource.ChooseResourceDialog;
import org.gvsig.catalog.ui.showtree.ShowTreeDialog;


/**
 * 
 * 
 * 
 * @author Jorge Piera Llodra (piera_jor@gva.es)
 */
public class ShowResultsDialogPanel extends JPanel implements ActionListener {
	private JDialog parent;
	protected ShowResultsPanel controlsPanel = null;
	private CatalogClient client = null;
	private int currentRecord = 0;
	private JButton nextButton = null;
	private JButton lastButton = null;
	private JButton descriptionButton = null;
	private JButton mapButton = null;
	private JButton closeButton = null;
	protected GetRecordsReply recordsReply = null;

	/**
	 * @param client 
	 * @param nodes 
	 * @param currentRecord 
	 * @param translator 
	 */
	public  ShowResultsDialogPanel(CatalogClient client, GetRecordsReply recordsReply, int currentRecord) {        
		this.recordsReply = recordsReply;
		this.client = client;
		this.currentRecord = currentRecord;
		controlsPanel = new ShowResultsPanel(client,
				recordsReply.getRecordsNumber());  
		controlsPanel.loadTextNewRecord(recordsReply.getRecordAt(currentRecord - 1));
		setDefaultButtonListeners();
		enableLoadResourcesButton();
		add(controlsPanel);
	} 

	/**
	 *  Enable the button to load online resources
	 */
	public void enableLoadResourcesButton() {        
		getMapButton().setEnabled(false);
		if (controlsPanel.getRecord() != null){
			Resource[] resources = controlsPanel.getRecord().getResources();
			if (resources == null)
				return;
			for (int i = 0; i < resources.length; i++) {
				String protocol = resources[i].getProtocol();

				if (protocol != null){        	
					if ((protocol.toUpperCase().indexOf(Resource.WMS) >= 0) ||
							(protocol.toUpperCase().indexOf(Resource.WFS) >=0) ||
							(protocol.toUpperCase().indexOf(Resource.WCS) >=0) ||
							(protocol.toUpperCase().indexOf(Resource.POSTGIS) >=0) ||
							(protocol.toUpperCase().indexOf(Resource.WEBSITE) >=0) ||
							(protocol.toUpperCase().indexOf(Resource.ARCIMS_IMAGE) >=0) ||
							(protocol.toUpperCase().indexOf(Resource.ARCIMS_VECTORIAL) >=0) ||
							(protocol.toUpperCase().indexOf(Resource.DOWNLOAD) >=0)){
						getMapButton().setEnabled(true);
						return;
					}
				}
			}
		}

	} 

	/**
	 * 
	 * 
	 */
	public void setDefaultButtonListeners() {        
		nextButton = controlsPanel.getNextButton();
		lastButton = controlsPanel.getLastButton();
		descriptionButton = controlsPanel.getDescriptionButton();
		mapButton = controlsPanel.getMapButton();
		closeButton = controlsPanel.getCloseButton();
		nextButton.addActionListener(this);
		lastButton.addActionListener(this);
		descriptionButton.addActionListener(this);
		mapButton.addActionListener(this);
		closeButton.addActionListener(this);
	} 

	/**
	 * 
	 * 
	 * 
	 * @return 
	 */
	public JButton getDescriptionButton() {        
		return descriptionButton;
	} 

	/**
	 * 
	 * 
	 * 
	 * @return 
	 */
	public JButton getMapButton() {        
		return mapButton;
	} 

	/**
	 * 
	 * 
	 * 
	 * @return 
	 */
	public JButton getCloseButton() {        
		return closeButton;
	} 

	/**
	 * 
	 * 
	 * 
	 * @param firstRecord 
	 */
	public void createNewSearch(int firstRecord) {        
		recordsReply = client.getRecords(null, firstRecord);
	} 

	/**
	 * @return 
	 */
	public int getCurrentNode() {        
		return currentRecord - 1;
	} 

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {        
		if (e.getActionCommand().equals("next")) {
			nextButtonActionPerformed();
		} 
		if (e.getActionCommand().equals("last")) {
			lastButtonActionPerformed();
		} 
		if (e.getActionCommand().equals("description")) {
			descriptionButtonActionPerformed();
		} 
		if (e.getActionCommand().equals("layer")) {
			mapButtonActionPerformed();
		} 
		if (e.getActionCommand().equals("close")) {
			closeButtonActionPerformed();
		}

	} 

	protected void nextButtonActionPerformed() {        
		this.currentRecord = this.currentRecord + 1;
		if (this.currentRecord == recordsReply.getRecordsNumber()){
			nextButton.setEnabled(false);
		} else {
			nextButton.setEnabled(true);
		}
		lastButton.setEnabled(true);
		if ((this.currentRecord % 10) == 1) {
			if (recordsReply.getRetrievedRecordsNumber() + 1 == this.currentRecord){
				createNewSearch(currentRecord);
			}
		}
		controlsPanel.loadTextNewRecord(recordsReply.getRecordAt(getCurrentNode()));
		controlsPanel.actualizaLabel(currentRecord);

		enableLoadResourcesButton();
	} 

	protected void lastButtonActionPerformed() {        
		this.currentRecord = this.currentRecord - 1;
		if (this.currentRecord == 1) {
			lastButton.setEnabled(false);
		} else {
			lastButton.setEnabled(true);
		}
		nextButton.setEnabled(true);
		controlsPanel.loadTextNewRecord(recordsReply.getRecordAt(getCurrentNode()));
		controlsPanel.actualizaLabel(currentRecord);

		enableLoadResourcesButton();
	} 

	protected void descriptionButtonActionPerformed() {        
		Record record = recordsReply.getRecordAt(getCurrentNode());
		if (record == null){
			//Impossible to parse
		}else{
			new ShowTreeDialog(record.getNode());
		}
	} 

	protected void mapButtonActionPerformed() {        
		Resource[] resources = controlsPanel.getRecord().getResources();
		Collection col = new java.util.ArrayList();
		for (int i=0 ; i<resources.length ; i++){
			col.add(resources[i]);
		}
		new ChooseResourceDialog(col);

	} 

	protected void closeButtonActionPerformed() {        
		parent.setVisible(false);
	} 

	/**
	 * 
	 * 
	 * 
	 * @param parent The parent to set.
	 */
	public void setParent(JDialog parent) {        
		this.parent = parent;
	} 
}
