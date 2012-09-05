
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
package org.gvsig.gazetteer.ui.search;
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

import org.gvsig.catalog.ui.search.SearchButtonPanel;
import org.gvsig.catalog.utils.CatalogConstants;
import org.gvsig.gazetteer.GazetteerClient;
import org.gvsig.gazetteer.querys.Feature;
import org.gvsig.gazetteer.querys.FeatureType;
import org.gvsig.gazetteer.querys.FeatureTypeAttribute;
import org.gvsig.gazetteer.querys.GazetteerQuery;
import org.gvsig.gazetteer.ui.showresults.ShowResultsDialog;
import org.gvsig.i18n.Messages;


/**
 * @author Jorge Piera Llodra (piera_jor@gva.es)
 */
public class SearchDialogPanel extends JPanel implements ActionListener {
	private JFrame parent;
	private GazetteerQuery query = null;
	protected Object serverConnectFrame;
	protected SearchUpperPanel upperPanel = null;
	protected SearchLowerPanel lowerPanel = null;
	protected SearchButtonPanel buttonsPanel = null;
	protected GazetteerClient client = null;
	protected boolean isMinimized = true;
	protected Feature[] features = null;
	private Collection searchThreads = null;
	private String currentServer = null;
	private String featureAttribute = null;

	/**
	 * This method initializes
	 * 
	 * 
	 * @param client 
	 * @param translator 
	 */
	public  SearchDialogPanel(GazetteerClient client, Object serverConnectFrame) {        
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
			FeatureType[] types = null;			
			try {
				types = ((GazetteerClient)client).getFeatureTypes();
			} catch (Exception e) {
				//The thesaurus will not loaded
			}			
			lowerPanel = new SearchLowerPanel(types,
					client.getAditionalSearchPanel());
			lowerPanel.addResultsByPageNumber(10);
			lowerPanel.addResultsByPageNumber(25);
			lowerPanel.addResultsByPageNumber(50);
			lowerPanel.addCoordinatesRelationship(
					Messages.getText("coordinatesContains"));
			lowerPanel.addCoordinatesRelationship(
					Messages.getText("coordinatesFullyOutsideOf"));
		}
		return lowerPanel;
	} 

	/**
	 * Set the gazetteer client
	 * @param 
	 * Gazetteer client to set
	 */
	public void setGazetteerClient(GazetteerClient gazetteerClient) {        
		this.client = gazetteerClient;
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

	/**
	 * @return 
	 */
	public FeatureType getFeatureSelected() {        
		return lowerPanel.getType();
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
			parent.setSize(parent.getWidth(),450);
			parent.doLayout();
			getLowerPanel().setVisible(true);
			getUpperPanel().setIcon(getUpIcon());
		}else{
			parent.setSize(parent.getWidth(),115);
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
		FeatureType featureType = lowerPanel.getType();
		if (client.isDescribeFeatureTypeNeeded()){
			try {
				FeatureTypeAttribute atribute = getAttribute(featureType);
				if (atribute == null){
					return;
				}else{
					featureAttribute = atribute.getName();
				}
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
		}	
		searchThread st =  new searchThread();
		searchThreads.add(st);   
		setCursor(new Cursor(Cursor.WAIT_CURSOR));       
	} 

	/**
	 * This method open a Jdialog panel and shows a list of
	 * attributes to choose one of them.
	 * @param featureType
	 * Feature 
	 * @return
	 * The selected attribute
	 * @throws Exception
	 */
	private FeatureTypeAttribute getAttribute(FeatureType featureType) throws Exception{
		if ((featureType == null) || 
				(lowerPanel.getType().getName().equals(Messages.getText("ThesaurusRoot")))){
			JOptionPane.showMessageDialog(
					this,
					Messages.getText("errorNotThesaurusSelected"),
					"WFS",
					JOptionPane.ERROR_MESSAGE
			);
			return null;
		}	
		FeatureTypeAttribute[] atributes = 
			client.describeFeatureType(featureType.getName());
		FeatureTypeAttribute attribute = (FeatureTypeAttribute)JOptionPane.showInputDialog(
				this,
				Messages.getText("chooseAttribute"),
				null,
				JOptionPane.PLAIN_MESSAGE,
				null,
				atributes,
				featureType.getName());
		if (attribute == null) {
			JOptionPane.showMessageDialog(
					this,
					Messages.getText("chooseAttribute"),
					"WFS",
					JOptionPane.ERROR_MESSAGE
			);
		}
		return attribute;		
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
			searchThread st = (searchThread)searchThreads.toArray()[i];
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
	protected GazetteerQuery doQuery() {        
		query = client.createNewQuery();
		query.setName(upperPanel.getName());
		query.setNameFilter(lowerPanel.getConcordancia());
		query.setFieldAttribute(featureAttribute);
		query.setFeatures(lowerPanel.getAllTypes());
		query.setRecsByPage(lowerPanel.getResultsByPage());
		query.setCoordinates(lowerPanel.getCoordinates());
		query.setCoordinatesFilter(lowerPanel.getCoordinatesOption());
		query.setCoordinatesClicked(upperPanel.isRestrictAreaClicked());
		query.getOptions().getAspect().setGoTo(lowerPanel.isGoToClicked());
		query.getOptions().getAspect().setKeepOld(lowerPanel.isKeepOldClicked());
		query.getOptions().getAspect().setPaintCurrent(lowerPanel.isMarkedPlaceClicked());
		query.getOptions().getSearch().setWithAccents(lowerPanel.isAccentsSearchEnabled());
		query.setProperties(lowerPanel.getProperties());
		return query;
	} 
	
	/**
	 * It returns the query that the user has selected
	 * @throws Exception 
	 * 
	 */
	private void doSearch() throws Exception {        
		features = client.getFeature(doQuery());
		if (features == null) {
			JOptionPane.showMessageDialog(this,
					Messages.getText("errorGetRecords"),
					"Error",
					JOptionPane.ERROR_MESSAGE);
		}
	} 

	/**
	 * Show the results 
	 */
	private void showResults() {        
		if (features.length == 0){
			JOptionPane.showMessageDialog(this,
					Messages.getText("anyResult"),
					Messages.getText("gazetteer_search"),
					JOptionPane.INFORMATION_MESSAGE);
		}else{
			showResultsActionPerformed(features);
		}
	} 

	/**
	 * @param features 
	 */
	protected void showResultsActionPerformed(Feature[] features) {        
		new ShowResultsDialog(client,
				features,
				lowerPanel.getResultsByPage(),
				doQuery());
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
	private class searchThread implements Runnable {
		volatile Thread myThread = null;

		public  searchThread() {        
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
			if ((features != null) && (features.length >= 0)) {
				showResults();
			}
			searchThreads.remove(this);
			if (searchThreads.size() == 0){
				setCursor(new Cursor(Cursor.DEFAULT_CURSOR));       
			}
		} 
	}

	public GazetteerQuery getQuery() {
		return query;
	}

	public String getCurrentServer() {
		return currentServer;
	}

	public void setCurrentServer(String currentServer) {
		this.currentServer = currentServer;
	}
}
