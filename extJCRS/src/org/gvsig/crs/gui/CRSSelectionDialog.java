/* gvSIG. Sistema de Informacin Geogrfica de la Generalitat Valenciana
 *
 * Copyright (C) 2006 Instituto de Desarrollo Regional and Generalitat Valenciana.
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
 *   Av. Blasco Ibez, 50
 *   46010 VALENCIA
 *   SPAIN
 *
 *      +34 963862235
 *   gvsig@gva.es
 *      www.gvsig.gva.es
 *
 *    or
 *
 *   Instituto de Desarrollo Regional (Universidad de Castilla La-Mancha)
 *   Campus Universitario s/n
 *   02071 Alabacete
 *   Spain
 *
 *   +34 967 599 200
 */

package org.gvsig.crs.gui;

import javax.swing.JPanel;
import javax.swing.ListSelectionModel;

import org.cresques.cts.IProjection;
import org.gvsig.crs.ICrs;
import org.gvsig.crs.gui.listeners.CRSSelectionDialogListener;
import org.gvsig.crs.persistence.CrsData;
import org.gvsig.crs.persistence.RecentCRSsPersistence;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.iver.cit.gvsig.gui.panels.crs.ISelectCrsPanel;

/**
 * Dilogo contenedor del panel para la seleccin de CRS. 
 * (para el CRS de la vista)
 * 
 * @author Jos Luis Gmez Martnez (jolugomar@gmail.com)
 * @author Diego Guerrero Sevilla (diego.guerrero@uclm.es)
 */
public class CRSSelectionDialog extends JPanel
	implements  IWindow, ISelectCrsPanel{
	
	private static final long serialVersionUID = 1L;	
	
	JPanel contentPane = null;
	private CRSMainPanel crsMainPanel = null;
	private IProjection lastProj = null;
	int code = 0;
	String dataSource = "";
	
	private boolean okPressed = false;
		
	public CRSSelectionDialog(ICrs curProj){
		super();
		lastProj = curProj;
		initialize();
	}
	
	public void initialize(){
		crsMainPanel = new CRSMainPanel((ICrs)lastProj);
		this.add(getContentPanel(), null);
		setListeners();	
		
	}
	
	public void initRecents(ICrs proj) {
		CrsData crsData = new CrsData(proj.getCrsWkt().getAuthority()[0], proj.getCode(),proj.getCrsWkt().getName());
		RecentCRSsPersistence persistence = new RecentCRSsPersistence();
		persistence.addCrsData(crsData);
		
		crsMainPanel.getRecentsPanel().loadRecents();
	}
	
	public boolean isOkPressed() { return okPressed; }
			
    public CRSMainPanel getProjPanel() {
        return (CRSMainPanel) getContentPanel();
    }

	public JPanel getContentPanel() {
	    if (contentPane == null) {
        	contentPane = crsMainPanel;
        	
       }
      return contentPane;
    }	
	
	public void setListeners(){
		
		CRSSelectionDialogListener listener = new CRSSelectionDialogListener(this); 
		
		ListSelectionModel rowSM = crsMainPanel.getEpsgPanel().getJTable().getSelectionModel();
		rowSM.addListSelectionListener(listener);
		
		ListSelectionModel rowSMiau = crsMainPanel.getIauPanel().getJTable().getSelectionModel();
		rowSMiau.addListSelectionListener(listener);
		
		ListSelectionModel rowSMrecents = crsMainPanel.getRecentsPanel().getJTable().getSelectionModel();
		rowSMrecents.addListSelectionListener(listener);
		
		ListSelectionModel rowSMesri = crsMainPanel.getEsriPanel().getJTable().getSelectionModel();
		rowSMesri.addListSelectionListener(listener);
		
		ListSelectionModel rowSMusr = crsMainPanel.getNewCrsPanel().getJTable().getSelectionModel();
		rowSMusr.addListSelectionListener(listener);
		
		crsMainPanel.getJComboOptions().addItemListener(listener);
		crsMainPanel.getJButtonAccept().addActionListener(listener);
        crsMainPanel.getJButtonCancel().addActionListener(listener);
        crsMainPanel.getEsriPanel().getJTable().addMouseListener(listener);
        crsMainPanel.getEpsgPanel().getJTable().addMouseListener(listener);
        crsMainPanel.getRecentsPanel().getJTable().addMouseListener(listener);
        crsMainPanel.getIauPanel().getJTable().addMouseListener(listener);
        crsMainPanel.getNewCrsPanel().getJTable().addMouseListener(listener);
	}

	public void setCode(int cod){
		code = cod;
	}
	
	public int getCode(){
		return code;
	}
	
	public IProjection getProjection() {
		return (IProjection) getProjPanel().getProjection();
	}
	/**
	 * @param proj
	 */
	public void setProjection(IProjection proj) {
		lastProj = proj;
		getProjPanel().setProjection(proj);
	}
	
	public String getProjectionAbrev(){
		return (String) getProjPanel().getProjection().getAbrev();
	}

	
	
	public void setDataSource(String sour){
		dataSource = sour;
	}
	
	public String getDataSource(){
		return dataSource;
	}
	
	public WindowInfo getWindowInfo() {
		WindowInfo m_viewinfo=new WindowInfo(WindowInfo.MODALDIALOG);
   		m_viewinfo.setTitle(PluginServices.getText(this, "nuevo_crs"));
		return m_viewinfo;
	}

	public IProjection getLastProj() {
		return lastProj;
	}

	public void setLastProj(IProjection lastProj) {
		this.lastProj = lastProj;
	}

	public void setOkPressed(boolean okPressed) {
		this.okPressed = okPressed;
	}

	public CRSMainPanel getCrsMainPanel() {
		return crsMainPanel;
	}

	public Object getWindowProfile() {
		return WindowInfo.DIALOG_PROFILE;
	}
}
