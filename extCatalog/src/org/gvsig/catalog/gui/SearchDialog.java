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
package org.gvsig.catalog.gui;

import java.awt.Frame;
import java.awt.geom.Rectangle2D;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JDialog;

import org.gvsig.catalog.CatalogClient;
import org.gvsig.catalog.drivers.GetRecordsReply;
import org.gvsig.catalog.querys.Coordinates;
import org.gvsig.catalog.ui.search.SearchDialogPanel;
import org.gvsig.catalog.utils.Frames;
import org.gvsig.fmap.mapcontext.events.ColorEvent;
import org.gvsig.fmap.mapcontext.events.ExtentEvent;
import org.gvsig.fmap.mapcontext.events.ProjectionEvent;
import org.gvsig.fmap.mapcontext.events.listeners.ViewPortListener;
import org.gvsig.i18n.Messages;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.iver.cit.gvsig.project.documents.view.gui.BaseView;

/**
 * This class is used to search a record using the catalog client
 * @author luisw
 * @modified by Jorge Piera
 */
public class SearchDialog extends SearchDialogPanel implements
IWindow,ViewPortListener {    
	public WindowInfo m_windowInfo = null;
	public ConnectDialog parentDialog = null;
	public JDialog frame = null;

	public SearchDialog(CatalogClient client, Object serverConnectFrame) {
		super(client,serverConnectFrame);
		parentDialog = (ConnectDialog)serverConnectFrame;
		setViewChangeListener();		
		loadViewPortCoordinates();
	}

	/* (non-Javadoc)
	 * @see com.iver.andami.ui.mdiManager.View#getViewInfo()
	 */
	public WindowInfo getWindowInfo() {
		if (m_windowInfo == null){
			m_windowInfo = new WindowInfo(WindowInfo.PALETTE);
			m_windowInfo.setTitle(Messages.getText("gazetteer_search") + " [" +
					getCurrentServer() + "]");		
			m_windowInfo.setHeight(80);
			m_windowInfo.setWidth(525);
		}
		return m_windowInfo;

	}
	public Object getWindowProfile(){
		return WindowInfo.TOOL_PROFILE;
	}

	protected void showResultsActionPerformed(GetRecordsReply recordsReply) {
		JDialog panel = new JDialog((Frame) PluginServices.getMainFrame(), false);
		Frames.centerFrame(panel,620,420);
		panel.setTitle(Messages.getText( "search_results")); 
		panel.setResizable(false);
		ShowResultsDialog dialog = new ShowResultsDialog(panel,
				client,
				recordsReply,
				1);
		panel.getContentPane().add(dialog);
		panel.setVisible(true); 
	}

	protected void closeButtonActionPerformed() {
		closeJDialog();
	}

	/**
	 * Size button action performed
	 */	 
	protected void resizeButtonActionPerformed(){
		if (isMinimized){
			frame.setSize(frame.getWidth(),495);
			getLowerPanel().setVisible(true);
			getUpperPanel().setIcon(getUpIcon());
		}else{
			frame.setSize(frame.getWidth(),165);
			getLowerPanel().setVisible(false);	 			
			getUpperPanel().setIcon(getDownIcon());
		}
		isMinimized = !isMinimized;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.catalog.ui.search.SearchDialogPanel#getUpIcon()
	 */
	protected Icon getUpIcon(){
		return PluginServices.getIconTheme().get("catalog-up");
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.catalog.ui.search.SearchDialogPanel#getDownIcon()
	 */
	protected Icon getDownIcon(){
		return PluginServices.getIconTheme().get("catalog-down");
	}
	
	/**
	 * Return button action
	 */
	protected void lastButtonActionPerformed() {  
		closeJDialog();
		ConnectDialog serverConnect = (ConnectDialog)serverConnectFrame;
		serverConnect.setVisible(true);
		serverConnect.getControlsPanel().enableSearchButton(false);
		PluginServices.getMDIManager().addWindow(serverConnect);
	} 

	public void closeJDialog() {
		frame.setVisible(false);
	}

	/**
	 * This method loads the view coordinates to the catalog search dialog
	 *
	 */
	private void loadViewPortCoordinates(){
		BaseView activeView = 
			(BaseView) PluginServices.getMDIManager().getActiveWindow();

		Rectangle2D r2d= activeView.getMapControl().getViewPort().getExtent();

		try{
			getLowerPanel().setCoordinates(new Coordinates(r2d.getMinX(),
					r2d.getMaxY(),
					r2d.getMaxX(),
					r2d.getMinY()));
		}catch(NullPointerException E){
			//We cant retrieve the coordinates if it doesn't 
			//exist a loaded layer
		}
	}
	/*
	 * This method joins the viewPort event to the listener
	 */
	private void setViewChangeListener(){
		BaseView activeView = 
			(BaseView) PluginServices.getMDIManager().getActiveWindow();

		activeView.getMapControl().getViewPort().addViewPortListener(this);

	}

	public void extentChanged(ExtentEvent e) {
		loadViewPortCoordinates();   

	}

	public void backColorChanged(ColorEvent e) {
		// TODO Auto-generated method stub

	}

	public void projectionChanged(ProjectionEvent e) {
		loadViewPortCoordinates();		
	}

	public void setFrame(JDialog frame) {
		this.frame = frame;
	} 	 	    
}
