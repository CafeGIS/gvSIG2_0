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

package org.gvsig.crs.gui.dialog;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;

import org.gvsig.crs.gui.listeners.ImportNewCrsDialogListener;
import org.gvsig.crs.gui.panels.EPSGpanel;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;

/**
 *  Clase que sirve para ir importando partes de un CRS en la creacion de
 *  un crs definido por el usuario
 *  
 * @author Jos Luis Gmez Martnez (jolugomar@gmail.com)
 *
 */

public class ImportNewCrsDialog extends JPanel implements IWindow {

	final String epsg = PluginServices.getText(this,"EPSG");	
	private EPSGpanel epsgPanel = null;
	private int crsCode = -1;
	
	private JPanel jPanelMain = null;	
		
	private JPanel jPanelButtons;
	private JButton jButtonAccept;
	private JButton jButonCancel;
	
	private String option = "";
	
	public ImportNewCrsDialog(String opt) {
		epsgPanel = new EPSGpanel();
		epsgPanel.connection();
		setOption(opt);
		initialize();
		setListeners();
	}
	
	public void initialize() {
		this.setLayout(new GridLayout(0,1));
		this.add(getJPanelMain(), BorderLayout.CENTER);
		this.add(getJPanelButtons(), BorderLayout.SOUTH);
	}
	
	public JPanel getJPanelMain(){
		if (jPanelMain == null){
			jPanelMain = new JPanel();
			jPanelMain.setLayout(new CardLayout());
			jPanelMain.setPreferredSize(new Dimension(525, 230));			
			jPanelMain.add(epsg, epsgPanel);			
		}
		
		return jPanelMain;		
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
	
	private void setListeners() {
		ImportNewCrsDialogListener listener = new ImportNewCrsDialogListener(this);
		
		getJButtonCancel().addActionListener(listener);
		getJButtonAccept().addActionListener(listener);
		
		ListSelectionModel rowSM = getEpsgPanel().getJTable().getSelectionModel();
		rowSM.addListSelectionListener(listener);
		
		getEpsgPanel().getJTable().addMouseListener(listener);
        
	}
	
	public void setOption(String opt){
		this.option = opt;
	}
	
	public String getOption() {
		return option;
	}
	
	public EPSGpanel getEpsgPanel() {
		return epsgPanel;
	}
	
	public void setCode(int code) {
		this.crsCode = code;
	}
	
	public int getCode() {
		return this.crsCode;
	}
		
	public WindowInfo getWindowInfo() {
		WindowInfo m_viewinfo=new WindowInfo(WindowInfo.MODALDIALOG);
   		m_viewinfo.setTitle(PluginServices.getText(this, "Importar")+" "+option);
   		m_viewinfo.setWidth(525);
   		m_viewinfo.setHeight(320);
   		return m_viewinfo;
	}

	public Object getWindowProfile() {
		return WindowInfo.DIALOG_PROFILE;
	}
}
