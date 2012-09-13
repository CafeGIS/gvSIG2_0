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
package org.gvsig.crs.gui.panels;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;

import org.gvsig.crs.CrsFactory;
import org.gvsig.crs.ICrs;
import org.gvsig.crs.gui.CRSMainTrPanel;
import org.gvsig.crs.gui.listeners.TransformationCompuestaPanelListener;
import org.gvsig.crs.persistence.CompTrData;
import org.gvsig.crs.persistence.RecentTrsPersistence;
import org.gvsig.crs.persistence.TrData;

import com.iver.andami.PluginServices;

/**
 *  Clase que define la transformacion de la vista en una transformacion compuesta
 *
 * @author Jose Luis Gomez Martinez (JoseLuis.Gomez@uclm.es)
 * @author Luisa Marina Fernandez (luisam.fernandez@uclm.es)
 *
 */
public class TransformationVistaPanel extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	private JLabel lblTransVista=null;
	private JComboBox jComboOptions=null;
	private JPanel pNorth=null;
	private JPanel pCenter=null;
	private TransformationManualPanel manualTrPanel = null;
	private TransformationEpsgPanel epsgTrPanel = null;
	private TransformationNadgridsPanel nadsTrPanel = null;
	private TransformationRecentsPanel recentsTrPanel = null;
	private JLabel lblCrsView = null;
	private JLabel lblCrsViewCode = null;
	
	String crs_target = null;
	String crs_source = null;
	String sourceAbrev = "";
	String sourceTransformation = null;
	String targetTransformation = null;
	CRSMainTrPanel crsMainTrPanel = null;
	
	boolean recentCompTransformation = false;
	
	/**
	 * Inicializa this
	 *
	 */
	public TransformationVistaPanel(String target, CRSMainTrPanel p) {
		super();
		crsMainTrPanel = p;
		setCrs_target(target);
		manualTrPanel = new TransformationManualPanel();
		epsgTrPanel = new TransformationEpsgPanel(getCrs_target());
		nadsTrPanel = new TransformationNadgridsPanel(false);
		recentsTrPanel = new TransformationRecentsPanel();
						
		this.setLayout(new BorderLayout());
		//Aadir al norte el panel que contiene las diferentes opciones de transformacin
		this.add(getPNorth(),BorderLayout.NORTH);
		this.add(getPCenter(),BorderLayout.CENTER);
		
		setListeners();
		
	}
	
	public JPanel getPCenter() {
		if (pCenter == null) {
			pCenter = new JPanel();
			pCenter.setLayout(new CardLayout());		
			
			pCenter.add("recents", recentsTrPanel);
			pCenter.add("manual", manualTrPanel);
			pCenter.add("epsg", epsgTrPanel);
			pCenter.add("nad", nadsTrPanel);
		}
		
		return pCenter;
	}

	public JLabel getLblTransVista() {
		if(lblTransVista==null){
		lblTransVista=new JLabel(PluginServices.getText(this,"transformacion_vista")+":");
		}
		return lblTransVista;
	}
	
	public JLabel getLblCrsVista() {
		if(lblCrsView==null){
			lblCrsView=new JLabel(PluginServices.getText(this,"crs_view")+":");
		}
		return lblCrsView;
	}
	
	public JLabel getLblCrsVistaCode() {
		if(lblCrsViewCode==null){
			lblCrsViewCode=new JLabel();
		}
		return lblCrsViewCode;
	}
	/**
	 * Definicion del panel que contiene el Label y el Combo de 
	 * las transformaciones de la vista
	 * @return
	 */
	public JPanel getPNorth() {
		if (pNorth==null){
			pNorth=new JPanel();
			pNorth.setLayout(new GridLayout(2,1));
			JPanel p1=new JPanel(new FlowLayout(FlowLayout.LEFT,10,3));
			p1.add(getLblCrsVista());
			p1.add(getLblCrsVistaCode());
			JPanel p2=new JPanel(new FlowLayout(FlowLayout.LEFT,10,3));
			p2.add(getLblTransVista());
			p2.add(getJComboOptions());
			
			pNorth.add(p1);
			pNorth.add(p2);
		}
		return pNorth;
	}
	/**
	 * Definicin del ComboBox que contiene las diferentes transformaciones 
	 * aplicables a la vista
	 * @return
	 */

	public JComboBox getJComboOptions() {
		if (jComboOptions == null){
		String[] selection = {PluginServices.getText(this, "recents_transformation"),
				PluginServices.getText(this, "transformacion_epsg"),
					PluginServices.getText(this, "transformacion_manual"), 
					PluginServices.getText(this, "nadgrids")}; 
			jComboOptions = new JComboBox(selection);
			//jComboOptions=new JComboBox();
			jComboOptions.setPreferredSize(new Dimension(180,25));	
			jComboOptions.setSelectedIndex(0);
			jComboOptions.addActionListener(this);
		}
		
		return jComboOptions;
	}
	
	/**
	 * Mtodo para aplicarle los eventos necesarios en el panel actual. La
	 * definicin de estos listeners estarn en la clase CRSMainTrPanelListener
	 *
	 */
	private void setListeners(){

		TransformationCompuestaPanelListener listener = new TransformationCompuestaPanelListener(this, crsMainTrPanel);
		
		getJComboOptions().addActionListener(listener);
		getRecentsTrPanel().getJButtonInfo().addActionListener(listener);
				 
        ListSelectionModel rowSMEpsgTr = getEpsgTrPanel().getJTable().getSelectionModel();
		rowSMEpsgTr.addListSelectionListener(listener);
		
		ListSelectionModel rowSMRecentsTr = getRecentsTrPanel().getJTable().getSelectionModel();
		rowSMRecentsTr.addListSelectionListener(listener);
		
		getNadsTrPanel().getJComboNadFile().addItemListener(listener);
		
		getManualTrPanel().getTx_Translation().addKeyListener(listener);
		getManualTrPanel().getTy_Translation().addKeyListener(listener);
		getManualTrPanel().getTz_Translation().addKeyListener(listener);
		getManualTrPanel().getTx_Rotation().addKeyListener(listener);
		getManualTrPanel().getTy_Rotation().addKeyListener(listener);
		getManualTrPanel().getTz_Rotation().addKeyListener(listener);
		getManualTrPanel().getTscale().addKeyListener(listener);
	}

		
	public void actionPerformed(ActionEvent e) {
		//Al cambiar el item seleccionado del combo
		if(e.getSource().equals(getJComboOptions())){
			CardLayout cl = (CardLayout)(pCenter.getLayout());
			if(getJComboOptions().getSelectedItem().equals(PluginServices.getText(this,"recents_transformation"))){
				//Mostrar el panel de transformacin reciente
				 cl.show(pCenter, "recents");
			}
			else if(getJComboOptions().getSelectedItem().equals(PluginServices.getText(this,"transformacion_epsg"))){
		
				//mostrar el panel de transformacin epsg
				 cl.show(pCenter, "epsg");
			}
			else if(getJComboOptions().getSelectedItem().equals(PluginServices.getText(this,"transformacion_manual"))){
		
				
				//mostrar el panel de transformacin manual
				 cl.show(pCenter, "manual");
			}
			else if(getJComboOptions().getSelectedItem().equals(PluginServices.getText(this,"nadgrids"))){
			//mostrar el panel de nadgrids
				 cl.show(pCenter, "nad");
			}
		}
		
	}
	
	public TransformationEpsgPanel getEpsgTrPanel() {
		return epsgTrPanel;
	}

	public TransformationManualPanel getManualTrPanel() {
		return manualTrPanel;
	}

	public TransformationNadgridsPanel getNadsTrPanel() {
		return nadsTrPanel;
	}
	
	public TransformationRecentsPanel getRecentsTrPanel() {
		return recentsTrPanel;
	}
	
	public void setCrs_target(String authTarget) {
		crs_target = authTarget;
		getLblCrsVistaCode().setText(authTarget);
	}
	
	public String getCrs_target() {
		return crs_target;
	}
	
	public void setCrs_source(String authority) {
		crs_source = authority;
		setSourceAbrev(authority);
	}
	
	public String getCrs_source() {
		return crs_source;
	}
	
	public void fillData() {
		String[] source = getCrs_source().split(":");
		String[] target = getCrs_target().split(":");
		recentsTrPanel.loadRecentsCompuesta(getCrs_target());
		epsgTrPanel.setSourceCompuesta(getCrs_target());		
		nadsTrPanel.setCode(Integer.parseInt(source[1]));
		nadsTrPanel.setSourceAbrev(source[0], source[1]);
		nadsTrPanel.setTargetAbrev(target[0], target[1]);
		nadsTrPanel.getJComboNadFile().setSelectedIndex(0);
		manualTrPanel.setCode(Integer.parseInt(source[1]));
		manualTrPanel.setSourceAbrev(source[0], source[1]);
		manualTrPanel.setTargetAbrev(target[0], target[1]);
		getJComboOptions().setSelectedIndex(0);
		/*
		 * Se dice que no es una transformacion cogida de las recientes compuestas
		 * para un correcto funcionamiento de los botones.
		 */
		setRecentCompTransformation(false);
	}
	
	public void setSourceAbrev(String fuente){
		sourceAbrev = fuente;		
	}
	
	public String getSourceAbrev(){
		return sourceAbrev;
	}
	
	public ICrs getProjection() {
		ICrs crs;		
		try {
			crs = new CrsFactory().getCRS(getSourceAbrev());
			crs.setTransformationParams(getSourceTransformation(),getTargetTransformation());//nadFile);
			return crs;
		} catch (org.gvsig.crs.CrsException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void setSourceTransformation(String trans) {
		sourceTransformation = trans;
	}
	
	public String getSourceTransformation() {
		return sourceTransformation;
	}
	
	public void setTargetTransformation(String trans) {
		targetTransformation = trans;
	}
	
	public String getTargetTransformation() {
		return targetTransformation;
	}
	
	public String getParamsEpsg(String [] values) {
		String params = "+towgs84=";
		params += values[0];
		for(int i = 1; i < values.length; i++)
			params +=","+values[i];
		return params;
	}
		
	public String getManualParams() {
		if(getManualTrPanel().getTx_Translation().getText().equals("")){
			getManualTrPanel().getTx_Translation().setText("0");
		}
		else if (getManualTrPanel().getTy_Translation().getText().equals("")){
			getManualTrPanel().getTy_Translation().setText("0");
		}
		else if (getManualTrPanel().getTz_Translation().getText().equals("")){
			getManualTrPanel().getTz_Translation().setText("0");
		}
		else if (getManualTrPanel().getTx_Rotation().getText().equals("")){
			getManualTrPanel().getTx_Rotation().setText("0");
		}
		else if (getManualTrPanel().getTy_Rotation().getText().equals("")){
			getManualTrPanel().getTy_Rotation().setText("0");
		}
		else if (getManualTrPanel().getTz_Rotation().getText().equals("")){
			getManualTrPanel().getTz_Rotation().setText("0");
		}
		else if (getManualTrPanel().getTscale().getText().equals("")){
			getManualTrPanel().getTscale().setText("0");
		}		
		String param = "+towgs84="+ getManualTrPanel().getTx_Translation().getText()+","+
						getManualTrPanel().getTy_Translation().getText()+","+
						getManualTrPanel().getTz_Translation().getText()+","+
						getManualTrPanel().getTx_Rotation().getText()+","+
						getManualTrPanel().getTy_Rotation().getText()+","+
						getManualTrPanel().getTz_Rotation().getText()+","+
						getManualTrPanel().getTscale().getText() + " ";
		return param;		
	}
	
	public String getNadsParams(String info) {
		String[] partes = info.split("\\(");
		String nadFile = partes[0];		
		return "+nadgrids="+nadFile;
	}
	
	/**
	 * cuando utilizamos crs+transformacion, cargamos los paneles para que
	 * el usuario pueda consultar la transformacion utilizada...
	 * @param details
	 */
	public void fillData(String details) {
		RecentTrsPersistence trPersistence = new RecentTrsPersistence();
		TrData crsTrDataArray[] = trPersistence.getArrayOfTrData();
		CompTrData comp = null;
		
		for (int iRow = crsTrDataArray.length-1; iRow >= 0; iRow--) {
			if (details.equals(crsTrDataArray[iRow].getAuthority()+":"+crsTrDataArray[iRow].getCode()+" <--> "+crsTrDataArray[iRow].getDetails()) && crsTrDataArray[iRow].getAuthority().equals(PluginServices.getText(this, "COMP"))) {
				comp = (CompTrData) crsTrDataArray[iRow];				
				break;
			}
		}
		if (comp.getSecondTr().getAuthority().equals(PluginServices.getText(this, "EPSG"))) {
			getJComboOptions().setSelectedIndex(1);
			int cod =Integer.parseInt(comp.getFirstTr().getCrsSource().split(":")[1]);
			String code = String.valueOf(comp.getSecondTr().getCode());
			//getEpsgTrPanel().setSource(PluginServices.getText(this, "EPSG"), cod);
			for (int i=0; i< getEpsgTrPanel().getJTable().getRowCount(); i++) {
				if (code.equals((String)getEpsgTrPanel().getJTable().getValueAt(i, 0))) {
					//seleccionarlo						
					getEpsgTrPanel().getJTable().setRowSelectionInterval(i,i);
					break;						
				}
			}
		} else if (comp.getSecondTr().getAuthority().equals(PluginServices.getText(this, "USR"))) {
			getJComboOptions().setSelectedIndex(2);
			String data = comp.getSecondTr().getDetails();
			data = data.trim().substring(1, data.length()-1);
			String values[] = data.split(",");
			getManualTrPanel().getTx_Translation().setText(values[0]);
			getManualTrPanel().getTy_Translation().setText(values[1]);
			getManualTrPanel().getTz_Translation().setText(values[2]);
			getManualTrPanel().getTx_Rotation().setText(values[3]);
			getManualTrPanel().getTy_Rotation().setText(values[4]);
			getManualTrPanel().getTz_Rotation().setText(values[5]);
			getManualTrPanel().getTscale().setText(values[6]);
			
		} else if (comp.getSecondTr().getAuthority().equals(PluginServices.getText(this, "NADGR"))) {
			getJComboOptions().setSelectedIndex(3);
			String data[] = comp.getSecondTr().getDetails().split(" ");
			String fichero = data[0];
			String[] authority = data[1].substring(1,data[1].length()-1).split(":");
			//Mirar como indicarle el CRS al que se le aplica...			
			getNadsTrPanel().setSourceAbrev(authority[0], authority[1]);
			
			for (int i = 0; i< getNadsTrPanel().getJComboNadFile().getItemCount(); i++) {
				if (fichero.equals((String)getNadsTrPanel().getJComboNadFile().getItemAt(i))) {
					getNadsTrPanel().getJComboNadFile().setSelectedIndex(i);
					break;
				}
			}			
		}
		/*
		 * Por aquí solo pasa cuando se ha seleccionado una transformación compuesta de recientes.
		 * Pondremos el valor de que es un crs compuesto reciente a true para que el botón de siguiente
		 * salga habilitado.
		 */
		setRecentCompTransformation(true);
	}
	
	public void setRecentCompTransformation(boolean state) {
		recentCompTransformation = state;
	}
	
	public boolean getRecentCompTransformation() {
		return recentCompTransformation;
	}
	
	public void resetData() {
		fillData();
	}
	
	public boolean isSthSelected() {
		if (getRecentsTrPanel().selectedRowTable == -1)
			return false;
		return true;
	}
	
}
