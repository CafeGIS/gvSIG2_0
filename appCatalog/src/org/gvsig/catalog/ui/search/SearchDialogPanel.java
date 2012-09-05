
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
package org.gvsig.catalog.ui.search;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.gvsig.catalog.CatalogClient;
import org.gvsig.catalog.drivers.GetRecordsReply;
import org.gvsig.catalog.querys.CatalogQuery;
import org.gvsig.catalog.querys.MetadataSearch;
import org.gvsig.catalog.ui.showresults.ShowResultsDialog;
import org.gvsig.catalog.utils.CatalogConstants;
import org.gvsig.i18n.Messages;


/**
 * @author Jorge Piera Llodra (piera_jor@gva.es)
 */
public class SearchDialogPanel extends JPanel implements ActionListener {
	private JFrame parent;
	private CatalogQuery query = null;
	protected Object serverConnectFrame;
	protected SearchUpperPanel upperPanel = null;
	protected SearchLowerPanel lowerPanel = null;
	protected SearchButtonPanel buttonsPanel = null;
	protected CatalogClient client = null;
	protected boolean isMinimized = true;
	private Collection searchThreads = null;
	private String currentServer = null;
	private GetRecordsReply recordsReply = null;
	
	/**
	 * This method initializes
	 * @param client 
	 * @param translator 
	 */
	public  SearchDialogPanel(CatalogClient client, Object serverConnectFrame) {        
		super();
		searchThreads = new java.util.ArrayList();
		this.client = client;
		this.isMinimized = true;
		this.serverConnectFrame = serverConnectFrame;
		initialize();
		getUpperPanel().setIcon(getDownIcon());
	} 

	/**
	 * This method initializes this
	 */
	private void initialize() {        
		setLayout(new BorderLayout());
		add(getUpperPanel(),BorderLayout.NORTH);
		add(getLowerPanel(),BorderLayout.CENTER);
		add(getButtonPanel(),BorderLayout.SOUTH);
		getLowerPanel().setVisible(false);
	} 

	/**
	 * It Gets the upperPanel
	 * @return 
	 */
	public SearchUpperPanel getUpperPanel() {        
		if (upperPanel == null){
			upperPanel = new SearchUpperPanel(); 
			upperPanel.addActionListener(this);
		}
		return upperPanel;

	} 

	/**
	 * It Gets the lowePanel
	 * @return 
	 */
	public SearchLowerPanel getLowerPanel() {        
		if (lowerPanel == null){
			lowerPanel = new SearchLowerPanel(client.getAditionalSearchPanel());
			lowerPanel.addCoordinatesRelationship(
					Messages.getText("coordinatesContains"));
			lowerPanel.addCoordinatesRelationship(
					Messages.getText("coordinatesFullyOutsideOf"));
			addServices();
			addCathegories();
			addScales();
		}
		return lowerPanel;
	} 

	/**
	 * Add the services
	 */
	private void addServices(){
	      upperPanel.addService(new MetadataSearch(Messages.getText("data")));
	      //upperPanel.addService(new ServicesSearch(Messages.getText("services")));
	}
	
	/**
	 * Adds the default cathegories
	 */
	private void addCathegories(){
		lowerPanel.addCathegory(Messages.getText("cathegoryAny"));
		lowerPanel.addCathegory(Messages.getText("cathegoryBiota"));
		lowerPanel.addCathegory(Messages.getText("cathegoryBoundaries"));
		lowerPanel.addCathegory(Messages.getText("cathegoryClimatologyMeteorologyAtmosphere"));
		lowerPanel.addCathegory(Messages.getText("cathegoryEconomy"));
		lowerPanel.addCathegory(Messages.getText("cathegoryElevation"));
		lowerPanel.addCathegory(Messages.getText("cathegoryEnvironment"));
		lowerPanel.addCathegory(Messages.getText("cathegoryFarming"));
		lowerPanel.addCathegory(Messages.getText("cathegoryGeoscientificInformation"));
		lowerPanel.addCathegory(Messages.getText("cathegoryHealth"));
		lowerPanel.addCathegory(Messages.getText("cathegoryImageryBaseMapsEarthCover"));
		lowerPanel.addCathegory(Messages.getText("cathegoryInlandWaters"));
		lowerPanel.addCathegory(Messages.getText("cathegoryIntelligenceMilitary"));
		lowerPanel.addCathegory(Messages.getText("cathegoryLocation"));
		lowerPanel.addCathegory(Messages.getText("cathegoryOceans"));
		lowerPanel.addCathegory(Messages.getText("cathegoryPlanningCadastre"));
		lowerPanel.addCathegory(Messages.getText("cathegorySociety"));
		lowerPanel.addCathegory(Messages.getText("cathegoryStructure"));
		lowerPanel.addCathegory(Messages.getText("cathegoryTransportation"));
		lowerPanel.addCathegory(Messages.getText("cathegoryUtilitiesCommunication"));
	}

	/**
	 * Adds the default scales
	 */
	private void addScales(){
		lowerPanel.addScale(Messages.getText("scaleAny"));
		lowerPanel.addScale(Messages.getText("scaleI"));
		lowerPanel.addScale(Messages.getText("scaleII"));
		lowerPanel.addScale(Messages.getText("scaleIII"));
		lowerPanel.addScale(Messages.getText("scaleIV"));
		lowerPanel.addScale(Messages.getText("scaleV"));
		lowerPanel.addScale(Messages.getText("scaleVI"));
	}

	/**
	 * @return the buttons panel
	 */
	public JPanel getButtonPanel() {        
		if (buttonsPanel == null) {
			buttonsPanel = new SearchButtonPanel();
			buttonsPanel.addActionListener(this);			
		}
		return buttonsPanel;
	} 	

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */	
	public void actionPerformed(ActionEvent e) {        
		if (e.getActionCommand().compareTo(CatalogConstants.SEARCH_BUTTON_ACTION_COMMAND)==0) {
			searchButtonActionPerformed();
		}else if (e.getActionCommand().compareTo(CatalogConstants.CLOSE_BUTTON_ACTION_COMMAND)==0){
			closeButtonActionPerformed();
		}else if(e.getActionCommand().compareTo(CatalogConstants.RESIZE_BUTTON_ACTION_COMMAND)==0){
			resizeButtonActionPerformed();
		}else if (e.getActionCommand().compareTo(CatalogConstants.CANCEL_BUTTON_ACTION_COMMAND)==0){
			cancelSearchesButtonActionPerformed();
		}else if (e.getActionCommand().compareTo(CatalogConstants.LAST_BUTTON_ACTION_COMMAND)==0){
			lastButtonActionPerformed();
		}
	} 

	/**
	 * thrown when the resize button is clicked
	 */
	protected void resizeButtonActionPerformed() {        
		if (isMinimized){
			parent.setSize(parent.getWidth(),487);
			getLowerPanel().setVisible(true);
			getUpperPanel().setIcon(getUpIcon());
		}else{
			parent.setSize(parent.getWidth(),165);
			getLowerPanel().setVisible(false);			
			getUpperPanel().setIcon(getDownIcon());
		}
		isMinimized = !isMinimized;
	} 
	
	protected Icon getUpIcon(){
		return new ImageIcon("./gvSIG/extensiones/org.gvsig.catalog/images/up.png");
	}
	
	protected Icon getDownIcon(){
		return new ImageIcon("./gvSIG/extensiones/org.gvsig.catalog/images/down.png");
	}

	/**
	 * thrown when the search button is clicked
	 * @throws Exception 
	 */
	protected void searchButtonActionPerformed() {        
		SearchThread st =  new SearchThread();
		searchThreads.add(st);   
		setCursor(new Cursor(Cursor.WAIT_CURSOR));       
	} 
	

	/**
	 * thrown when the last button is clicked
	 */
	protected void lastButtonActionPerformed() {        
		((JFrame)serverConnectFrame).setVisible(true);
		parent.setVisible(false);
	} 	

	/**
	 * thrown when the cancel button is clicked
	 */
	protected void cancelSearchesButtonActionPerformed() {        
		for (int i=0 ; i<searchThreads.size() ; i++){
			SearchThread st = (SearchThread)searchThreads.toArray()[i];
			st.stop();            
		}     
		searchThreads.clear();
		setCursor(new Cursor(Cursor.DEFAULT_CURSOR));        
	} 

	/**
	 * thrown when the close button is clicked
	 */
	protected void closeButtonActionPerformed() {        
		parent.setVisible(false);
	} 

	/**
	 * @return the selected query
	 */
	protected CatalogQuery doQuery() {        
		CatalogQuery query = client.createNewQuery();
		query.setService(upperPanel.getService());
		query.setTitle(upperPanel.getTitle());
		query.setTitleFilter(lowerPanel.getTitleOption());
		query.setAbstract(lowerPanel.getAbstract());
		query.setThemeKey(lowerPanel.getKeys());
		query.setTopic(lowerPanel.getCathegory());
		query.setScale(lowerPanel.getScale());
		query.setProvider(lowerPanel.getProvider());
		query.setDateFrom(lowerPanel.getDateFrom());
		query.setDateTo(lowerPanel.getDateTo());
		query.setCoordenates(lowerPanel.getCoordinates());
		query.setCoordenatesFilter(lowerPanel.getCoordinatesOption());
		query.setMinimized(isMinimized);
		query.setCoordinatesClicked(upperPanel.getRestrictAreaClicked());
		return query;
	} 

	/**
	 * It returns the query that the user has selected
	 * @throws Exception 
	 * 
	 */
	private void doSearch() throws Exception {        
		recordsReply = client.getRecords(doQuery(), 1);
		if (recordsReply == null) {
			JOptionPane.showMessageDialog(this,
					Messages.getText("errorGetRecords"),
					"Error",
					JOptionPane.ERROR_MESSAGE);
		} else if (recordsReply.getRecordsNumber() == 0) {
			JOptionPane.showMessageDialog(this,
					Messages.getText("anyResult"),
					Messages.getText("catalog_search"),
					JOptionPane.INFORMATION_MESSAGE);
		}
	} 

	/**
	 * @param features 
	 */
	protected void showResultsActionPerformed(GetRecordsReply recordsReply) {        
		new ShowResultsDialog(client, recordsReply, 1);			
	} 

	/**
	 * 
	 * @param parent The parent to set.
	 */
	public void setParent(JFrame parent) {        
		this.parent = parent;
	} 

	/**
	 * This class is used to manage the searches.
	 * It contains method to start and to stop a thread. It is
	 * necessary to create because "stop" method (for the Thread class)
	 * is deprecated.
	 * 
	 * 
	 * @author Jorge Piera Llodra (piera_jor@gva.es)
	 */
	private class SearchThread implements Runnable {
		volatile Thread myThread = null;

		public  SearchThread() {        
			myThread = new Thread(this);
			myThread.start();
		} 

		public void stop() {        
			myThread.stop();
		} 

		/*
		 * (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		public void run() {        
			try {
				doSearch();
			} catch (Exception e) {
				e.printStackTrace();
			}
			if ((recordsReply != null) && (recordsReply.getRecordsNumber() > 0)) {
				showResultsActionPerformed(recordsReply);
			}
			searchThreads.remove(this);
			if (searchThreads.size() == 0){
				setCursor(new Cursor(Cursor.DEFAULT_CURSOR));       
			}
		} 
	}

	public CatalogQuery getQuery() {
		return query;
	}

	public String getCurrentServer() {
		return currentServer;
	}

	public void setCurrentServer(String currentServer) {
		this.currentServer = currentServer;
	}
}
