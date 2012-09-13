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

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.cresques.cts.IProjection;
import org.gvsig.crs.ICrs;
import org.gvsig.crs.gui.panels.CrsAndTransformationRecentsPanel;
import org.gvsig.crs.gui.panels.CrsRecentsPanel;
import org.gvsig.crs.gui.panels.EPSGpanel;
import org.gvsig.crs.gui.panels.ESRIpanel;
import org.gvsig.crs.gui.panels.IAU2000panel;
import org.gvsig.crs.gui.panels.NewCRSPanel;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;

/**
 * Clase que genera el panel principal para la seleccin de CRS 
 * 
 * @author Jos Luis Gmez Martnez (jolugomar@gmail.com)
 * @author Luisa Marina Fernandez (luisam.fernandez@uclm.es)
 *
 */
public class CRSMainPanel extends JPanel implements IWindow {
		
	private static final long serialVersionUID = 1L;
	
	private JComboBox jComboOptions = null;	
	private JPanel Combopanel = null;	
	private JPanel USGSpanel = null;	
	
	final String recientes = PluginServices.getText(this,"recientes");
	final String epsg = PluginServices.getText(this,"EPSG"); 
	final String usgs = PluginServices.getText(this,"USGS");
	final String esri = PluginServices.getText(this,"ESRI");
	final String iau2000 = PluginServices.getText(this,"IAU2000");
	final String newCRS = PluginServices.getText(this,"newCRS");
	
	//String[] selection = {recientes, epsg, usgs, esri, iau2000, newCRS};
	String[] selection = {recientes, epsg, esri, iau2000, newCRS};//, esri};
	
	boolean inAnApplet = true;	
	public CrsRecentsPanel crsRecentsPanel = null;
	public CrsAndTransformationRecentsPanel crsAndTransformationRecentsPanel = null;
	public EPSGpanel epsgPanel = null;
	public ESRIpanel esriPanel = null;
	public IAU2000panel iauPanel = null;
	public NewCRSPanel newCrsPanel=null;
	
	private JPanel jPanelMain = null;	
	
	private JPanel jPanelButtons;
	private JButton jButtonAccept;
	private JButton jButonCancel;
	
	private ICrs viewCrs;
	
	String dataSource = "";

	public CRSMainPanel(ICrs crs) {
		viewCrs = crs;
		crsRecentsPanel = new CrsRecentsPanel();
		epsgPanel = new EPSGpanel();
		esriPanel = new ESRIpanel();
		iauPanel = new IAU2000panel();
		newCrsPanel=new NewCRSPanel(viewCrs);
		
		this.add(vista(), BorderLayout.NORTH);		
		this.add(getJPanelButtons(), BorderLayout.SOUTH);
		
	    setDataSource(selection[0]);
	}
	
	public CRSMainPanel(int target, ICrs crs) {
		viewCrs = crs;
		crsAndTransformationRecentsPanel = new CrsAndTransformationRecentsPanel();
		epsgPanel = new EPSGpanel();
		esriPanel = new ESRIpanel();
		iauPanel = new IAU2000panel();
		newCrsPanel=new NewCRSPanel(viewCrs);
		
		setDataSource(selection[0]);
	}
	
	/**
	 * Panel con los controles necesarios para visualizarlo en el panel de 
	 * seleccin de CRS y transformacin de la capa a aadir
	 * @return
	 */
	public JPanel capa(){
		JPanel p = new JPanel();
		//**p.setPreferredSize(new Dimension(550, 320));
		//**p.setLayout(new GridLayout(0,1));
		//p.setLayout(new FlowLayout(FlowLayout.LEFT,10,10));
		p.setLayout(new BorderLayout());
		//p.setBorder(BorderFactory.createTitledBorder(PluginServices.getText(this,"seleccione_crs_capa")));//,
								/*BorderFactory.createEmptyBorder(1,1,1,1)),
								p.getBorder()));*/
		p.add(getCombopanel(),BorderLayout.NORTH);
		p.add(getJPanelLayerMain(),BorderLayout.CENTER);
		return p;
	}
	
	public JPanel vista(){
		JPanel p = new JPanel();
		//p.setPreferredSize(new Dimension(550, 320));
		//p.setLayout(new GridLayout(0,1));
		//p.setLayout(new FlowLayout(FlowLayout.LEFT,10,10));
		/*p.setBorder(
			    BorderFactory.createCompoundBorder(
						BorderFactory.createCompoundBorder(
								BorderFactory.createTitledBorder(PluginServices.getText(this,"seleccione_crs_vista")),
								BorderFactory.createEmptyBorder(2,2,2,2)),
								p.getBorder()));*/
		p.setLayout(new BorderLayout());
		p.add(getCombopanel(),BorderLayout.NORTH);
		p.add(getJPanelMain(),BorderLayout.CENTER);
		return p;
	}
	
	public JPanel getJPanelMain(){
		if (jPanelMain == null){
			jPanelMain = new JPanel();
			jPanelMain.setLayout(new CardLayout());
			jPanelMain.setPreferredSize(new Dimension(525, 230));	
			jPanelMain.add(recientes, crsRecentsPanel);
			jPanelMain.add(epsg, epsgPanel);
			jPanelMain.add(newCRS,newCrsPanel);
			jPanelMain.add(esri, esriPanel);
			jPanelMain.add(usgs, getJPanelUSGS());
			jPanelMain.add(iau2000, iauPanel);
		}
		
		return jPanelMain;		
	}
	
	public JPanel getJPanelLayerMain(){
		if (jPanelMain == null){
			jPanelMain = new JPanel();
			jPanelMain.setLayout(new CardLayout());
			jPanelMain.setPreferredSize(new Dimension(525, 230));	
			jPanelMain.add(recientes, crsAndTransformationRecentsPanel);
			jPanelMain.add(epsg, epsgPanel);
			jPanelMain.add(newCRS,newCrsPanel);
			jPanelMain.add(esri, esriPanel);
			jPanelMain.add(usgs, getJPanelUSGS());
			jPanelMain.add(iau2000, iauPanel);
		}
		
		return jPanelMain;		
	}
	
	public JPanel getCombopanel(){
		if (Combopanel == null){
			Combopanel = new JPanel();
			Combopanel.setLayout(new FlowLayout(FlowLayout.CENTER,10,5));
			Combopanel.add(getJLabelTipo());
			Combopanel.add(getJComboOptions());
		}
		
		return Combopanel;
	}
	
	private JLabel getJLabelTipo(){
		JLabel jLabelTipo = new JLabel();
		jLabelTipo.setPreferredSize(new Dimension(50,25));
		jLabelTipo.setText(PluginServices.getText(this,"tipo")+":");
		return jLabelTipo;
	}
	
	public JComboBox getJComboOptions(){
		if (jComboOptions == null){
			jComboOptions = new JComboBox(selection);
			jComboOptions.setPreferredSize(new Dimension(100,25));			
			jComboOptions.setEditable(false);
			jComboOptions.setSelectedIndex(0);					
		}
		return jComboOptions;
	}
			
	public JPanel getJPanelUSGS() {	
		if (USGSpanel == null){
			USGSpanel = new JPanel();
			USGSpanel.setLayout(new GridLayout(3,4));
			USGSpanel.setLayout(new FlowLayout(FlowLayout.LEFT,10,10));
			USGSpanel.setPreferredSize(new Dimension(525, 400));			
		}	
		return USGSpanel;
	}			
	
	private JPanel getJPanelButtons() {
		if(jPanelButtons == null) {
			jPanelButtons = new JPanel();
			jPanelButtons.setLayout(new FlowLayout(FlowLayout.RIGHT));
			//jPanelButtons.setPreferredSize(new Dimension(525,50));
			jPanelButtons.add(getJButtonCancel(),null);
			jPanelButtons.add(getJButtonAccept(),null);			
		}
		return jPanelButtons;
	}
	
	public JButton getJButtonCancel() {
		if(jButonCancel == null) {
			jButonCancel = new JButton();
			jButonCancel.setText(PluginServices.getText(this,"cancel"));
			jButonCancel.setPreferredSize(new Dimension(100,25));
			jButonCancel.setMnemonic('C');
			jButonCancel.setToolTipText("Cancel");			
		}
		return jButonCancel;
	}
	
	public void cancelButton_actionPerformed(ActionEvent e) {		
		 PluginServices.getMDIManager().closeWindow(this);
	}
	
	public JButton getJButtonAccept() {
		if(jButtonAccept == null) {
			jButtonAccept = new JButton();
			jButtonAccept.setText(PluginServices.getText(this,"ok"));
			jButtonAccept.setPreferredSize(new Dimension(100,25));
			jButtonAccept.setEnabled(false);
			jButtonAccept.setMnemonic('A');
			jButtonAccept.setToolTipText(PluginServices.getText(this,"ok"));			
		}
		return jButtonAccept;
	}
	
	public ICrs getProjection() {
		if (getDataSource().equals(PluginServices.getText(this,"EPSG"))){
			return epsgPanel.getProjection();
		}
		else if (getDataSource().equals(PluginServices.getText(this,"IAU2000"))) {
			return iauPanel.getProjection();
		}
		else if (getDataSource().equals(PluginServices.getText(this,"recientes"))) {
			return crsRecentsPanel.getProjection();
		}
		else if (getDataSource().equals(PluginServices.getText(this,"ESRI"))) {
			return esriPanel.getProjection();
		}
		else if (getDataSource().equals(PluginServices.getText(this,"newCRS"))) {
			return newCrsPanel.getProjection();
		}
		return null;
	}
	
	public void setProjection(IProjection crs) {
		//setCrs((ICrs) crs);
	}
	
	public void setDataSource(String sour){
		dataSource = sour;
	}
	
	public String getDataSource(){
		return dataSource;
	}

	public WindowInfo getWindowInfo() {
		WindowInfo m_viewinfo=new WindowInfo(WindowInfo.MODALDIALOG);
   		m_viewinfo.setTitle(PluginServices.getText(this, "seleccionar_crs"));
		return m_viewinfo;
	}

	public EPSGpanel getEpsgPanel() {
		return epsgPanel;
	}

	

	public ESRIpanel getEsriPanel() {
		return esriPanel;
	}
	public IAU2000panel getIauPanel() {
		return iauPanel;
	}

	public CrsRecentsPanel getRecentsPanel() {
		return crsRecentsPanel;
	}
	
	public CrsAndTransformationRecentsPanel getCrsAndTransformationRecentsPanel() {
		return crsAndTransformationRecentsPanel;
	}

	public NewCRSPanel getNewCrsPanel() {
		return newCrsPanel;
	}

	public Object getWindowProfile() {
		return WindowInfo.DIALOG_PROFILE;
	}
}
