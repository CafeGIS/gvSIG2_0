
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
package org.gvsig.gazetteer.ui.showresults;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import org.gvsig.gazetteer.GazetteerClient;
import org.gvsig.gazetteer.querys.Feature;
import org.gvsig.gazetteer.querys.GazetteerQuery;


/**
 * 
 * 
 * 
 * @author Jorge Piera Llodra (piera_jor@gva.es)
 */
public class ShowResultsDialogPanel extends JPanel implements ActionListener {
	private JDialog parent;
	protected ShowResultsPanel ppalPanel = null;
	protected GazetteerClient client = null;
	private int currentPage = 0;
	private int recordsByPage = 0;
	private JButton nextButton = null;
	private JButton lastButton = null;
	private JButton mapButton = null;
	private JButton closeButton = null;
	private int featuresLength = 0;
	protected GazetteerQuery query = null;

	/**
	 * @param client 
	 * @param features 
	 * @param recordsByPage 
	 * @param translator 
	 */
	public ShowResultsDialogPanel(GazetteerClient client, Feature[] features, int recordsByPage, GazetteerQuery query) {        
		this.client = client;
		this.currentPage = 1;
		this.recordsByPage = recordsByPage;
		this.query = query;
		initialize(client,features,recordsByPage);     
	} 

	/**
	 * 
	 * 
	 * 
	 * @param client 
	 * @param features 
	 * @param recordsByPage 
	 */
	public void initialize(GazetteerClient client, Feature[] features, int recordsByPage) {        
		this.featuresLength = features.length;
		this.currentPage = 1;
		this.recordsByPage = recordsByPage;
		ppalPanel = new ShowResultsPanel(features,getNumPages(),recordsByPage);
		setDefaultButtonListeners();

		add(ppalPanel);
	} 

	/**
	 * 
	 * 
	 */
	private void setDefaultButtonListeners() {        
		nextButton = ppalPanel.getNextButton();
		lastButton = ppalPanel.getLastButton();
		mapButton = ppalPanel.getMapButton();
		closeButton = ppalPanel.getCloseButton();

		nextButton.addActionListener(this);
		lastButton.addActionListener(this);
		mapButton.addActionListener(this);
		closeButton.addActionListener(this);
	} 

	/**
	 * 
	 * 
	 * 
	 * @param e 
	 */
	public void actionPerformed(ActionEvent e) {        
		if (e.getActionCommand().equals("next")) {
			nextButtonActionPerformed();
		} 
		if (e.getActionCommand().equals("last")) {
			lastButtonActionPerformed();
		} 
		if (e.getActionCommand().equals("localize")) {
			loadButtonActionPerformed();
		} 
		if (e.getActionCommand().equals("close")) {
			closeButtonActionPerformed();
		}

	} 

	/**
	 * 
	 * 
	 * 
	 * @return 
	 */
	private int getNumPages() {        
		if ((featuresLength % recordsByPage) == 0)
			return featuresLength/recordsByPage;
		return (featuresLength/recordsByPage) + 1;
	} 

	/**
	 * 
	 * 
	 */
	public void nextButtonActionPerformed() {        
		this.currentPage = this.currentPage + 1;

		if (this.currentPage == getNumPages()) {
			nextButton.setEnabled(false);
		} else {
			nextButton.setEnabled(true);
		}
		lastButton.setEnabled(true);
		ppalPanel.actualizaLabel(currentPage,recordsByPage);


	} 

	/**
	 * 
	 * 
	 */
	public void lastButtonActionPerformed() {        
		this.currentPage = this.currentPage - 1;
		if (this.currentPage == 1) {
			lastButton.setEnabled(false);
		} else {
			lastButton.setEnabled(true);
		}
		nextButton.setEnabled(true);

		ppalPanel.actualizaLabel(currentPage,recordsByPage);


	} 

	/**
	 * 
	 * 
	 */
	public void loadButtonActionPerformed() {        
		Feature feature = ppalPanel.getFeature();

		if (feature != null){
			System.out.println("ID: " + feature.getId());
			System.out.println("NAME: " + feature.getName());
			System.out.println("DESCRIPTION: " + feature.getDescription());
			System.out.println("COORDINATES: X=" + feature.getCoordinates().getX() + " Y=" + feature.getCoordinates().getY());
		}
	} 

	/**
	 * 
	 * 
	 */
	public void closeButtonActionPerformed() {        
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
