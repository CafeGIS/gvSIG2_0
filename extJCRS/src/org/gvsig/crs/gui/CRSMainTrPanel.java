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

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;

import org.cresques.cts.IProjection;
import org.gvsig.crs.CrsException;
import org.gvsig.crs.CrsFactory;
import org.gvsig.crs.CrsWkt;
import org.gvsig.crs.ICrs;
import org.gvsig.crs.gui.listeners.CRSMainTrPanelListener;
import org.gvsig.crs.gui.panels.TransformationCapaPanel;
import org.gvsig.crs.gui.panels.TransformationEpsgPanel;
import org.gvsig.crs.gui.panels.TransformationManualPanel;
import org.gvsig.crs.gui.panels.TransformationNadgridsPanel;
import org.gvsig.crs.gui.panels.TransformationRecentsPanel;
import org.gvsig.crs.gui.panels.TransformationVistaPanel;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;

/**
 * Clase que genera el panel de transformacin, en el que se incluye la
 * eleccin del CRS de la capa, as como el de la posibilidad de poder
 * asignarle una transformacin.
 * 
 * @author Jos Luis Gmez Martnez (jolugomar@gmail.com)
 * @author Luisa Marina Fernandez (luisam.fernandez@uclm.es)
 *
 */

public class CRSMainTrPanel extends JPanel implements IWindow{

	private static final long serialVersionUID = 1L;
	
	/*
	 * Para mostrar los parametros si tenemos seleccionada transformacion
	 */
	boolean tra = false; 
	boolean inAnApplet = true;
	private boolean cancelYes = false;
	private JButton jButtonCancel = null;
	private JButton jButtonNext = null;
	private JButton jButtonAccept = null;
	private JButton jButtonBefore = null;
	private JPanel jPanelButtons = null;
	private JLabel jLabelTrans=null;
	
	boolean targetNad = false;	
	
	String crs_target = null;
	CrsWkt crsWkt_target = null;
		
	int transformation_code = 0;	
	
	//**private JPanel panel = null;
	CRSMainPanel viewPan = null;
	
	private JPanel jPanelMain = null;
	private TransformationManualPanel manualTrPanel = null;
	private boolean manual_tr = false;
	private TransformationEpsgPanel epsgTrPanel = null;
	private boolean epsg_tr = false;
	private TransformationNadgridsPanel nadsTrPanel = null;
	private boolean nads_tr = false;
	private TransformationRecentsPanel recentsTrPanel = null;
	private boolean recents_tr = false;
	private TransformationVistaPanel vistaTrPanel=null;
	private boolean vista_tr=false;
	private TransformationCapaPanel capaTrPanel=null;
	private boolean capa_tr=false;
	private boolean compuesta_tr=false;
	
	
	private boolean sin_tr = false;
	private JPanel jPanelCombo = null;
	private JComboBox jComboOptions = null;
	
	private IProjection crsfirst;
	
	private CRSMainPanel crsMainPanel;
	private String newSelection;
	String dataSource = "";
	ICrs curProj = null;
	
	public CRSMainTrPanel(int target, CrsWkt crsWkttarget, ICrs proj) {			
		curProj = proj;
		crsMainPanel = new CRSMainPanel(target, curProj);		
		viewPan = crsMainPanel;
		
		crs_target = crsWkttarget.getAuthority()[0]+":"+crsWkttarget.getAuthority()[1];
		setCrsWkt_target(crsWkttarget);			
		//Construir todos los paneles de transformaciones
		manualTrPanel = new TransformationManualPanel();
		epsgTrPanel = new TransformationEpsgPanel(crs_target);
		nadsTrPanel = new TransformationNadgridsPanel(true);
		recentsTrPanel = new TransformationRecentsPanel();
		capaTrPanel=new TransformationCapaPanel(crs_target, this);
		vistaTrPanel=new TransformationVistaPanel(crs_target, this);
		
		
		/*
		 * esta variable contendr tanto el panel utilizado en CRSViewPanel, como los paneles
		 * que seran necesarios para realizar las transformaciones
		 */
		jPanelMain = new JPanel();
		jPanelMain.setLayout(new CardLayout());		
		jPanelMain.add("primero", viewPanel());
		jPanelMain.add("manual", manualTrPanel);
		jPanelMain.add("epsg", epsgTrPanel);
		jPanelMain.add("nad", nadsTrPanel);
		jPanelMain.add("recents", recentsTrPanel);
		jPanelMain.add("vista",vistaTrPanel);
		jPanelMain.add("capa",capaTrPanel);
		
		this.setLayout(new BorderLayout());	
		this.add(jPanelMain, BorderLayout.CENTER);
		this.add(getButtons(), BorderLayout.SOUTH);
		
		setListeners();
		
		setDataSource(crsMainPanel.getDataSource());
		crsMainPanel.getCrsAndTransformationRecentsPanel().loadRecents(crsWkttarget, proj);
	}
	
	/**
	 * Este mtodo cargar todo el panel utilizado en dicho panel el
	 *  CRSViewPanel y el combobox de las transformaciones
	 */
	private JPanel viewPanel(){
		JPanel integro = new JPanel();
		integro.setLayout(new BorderLayout());
		integro.setBorder(BorderFactory.createTitledBorder(PluginServices.getText(this,"crsAndTransformation")));
	
		integro.add(viewPan.capa(),BorderLayout.CENTER);
		//**integro.add(getJPanel(), null);
		integro.add(getCombopanel(),BorderLayout.SOUTH);
		return integro;
	}
	
	/**
	 * Define el panel en el que se integran el label y el combobox 
	 * de transformaciones
	 */
	public JPanel getCombopanel(){
		if (jPanelCombo == null){
			jPanelCombo = new JPanel();
			jPanelCombo.setLayout(new FlowLayout(FlowLayout.LEFT,10,10));
			jPanelCombo.add(getJLabelTrans());
			jPanelCombo.add(getJComboOptions());
		}
		
		return jPanelCombo;
	}
	/**
	 * Inicializa la etiqueta 'Seleccione Transformacion'
	 * @return
	 */
	private JLabel getJLabelTrans(){
		if (jLabelTrans==null){
			jLabelTrans = new JLabel();
			jLabelTrans.setPreferredSize(new Dimension(180, 25));
			jLabelTrans.setText(PluginServices.getText(this, "seleccione_transformacion")+":");
		}
		return jLabelTrans;
	}
	/**
	 * Inicializa el comboBox de Tansformaciones
	 * @return
	 */
	public JComboBox getJComboOptions(){
		if (jComboOptions == null){
			String[] selection = {PluginServices.getText(this, "sin_transformacion"),
					PluginServices.getText(this, "recents_transformation"),
					PluginServices.getText(this, "transformacion_epsg"),
					PluginServices.getText(this, "transformacion_manual"),
					PluginServices.getText(this, "transformacion_compuesta"),
					PluginServices.getText(this, "nadgrids")}; 
			jComboOptions = new JComboBox(selection);
			jComboOptions.setPreferredSize(new Dimension(180,25));	
			jComboOptions.setEnabled(false);
			jComboOptions.setEditable(false);			
			jComboOptions.setSelectedIndex(0);
			newSelection = (String) jComboOptions.getSelectedItem();			
		}
		return jComboOptions;
	}
	/**
	 * Agrega los botones: Aceptar, cancelar, siguiente y finalizar
	 * @return
	 */
	private JPanel getButtons() {
		if(jPanelButtons == null) {
			jPanelButtons = new JPanel();
			jPanelButtons.setLayout(new FlowLayout(FlowLayout.RIGHT,10,10));
			jPanelButtons.add(getJButtonCancel());
			jPanelButtons.add(getJButtonBefore());
			jPanelButtons.add(getJButtonNext());
			jPanelButtons.add(getJButtonAccept());
		}
		return jPanelButtons;
	}
	/**
	 * Inicializa el botn 'Anterior'
	 * @return
	 */
	public JButton getJButtonBefore(){
		if(jButtonBefore == null) {
			jButtonBefore = new JButton();
			jButtonBefore.setText(PluginServices.getText(this,"anterior"));
			jButtonBefore.setMnemonic('B');			
			jButtonBefore.setPreferredSize(new Dimension(100,25));			
			jButtonBefore.setEnabled(false);
		}
		return jButtonBefore;
	}
	/**
	 * Inicializa el botn 'Finalizar'
	 * @return
	 */
	public JButton getJButtonAccept() {
		if(jButtonAccept == null) {
			jButtonAccept = new JButton();
			jButtonAccept.setText(PluginServices.getText(this,"finalizar"));
			jButtonAccept.setMnemonic('O');
			jButtonAccept.setVisible(true);
			jButtonAccept.setEnabled(false);
			jButtonAccept.setPreferredSize(new Dimension(100,25));
		}
		return jButtonAccept;
	}
	/**
	 * Inicializa el botn 'Siguiente'
	 * @return
	 */
	public JButton getJButtonNext() {
		if(jButtonNext == null) {
			jButtonNext = new JButton();
			jButtonNext.setText(PluginServices.getText(this,"siguiente"));
			jButtonNext.setMnemonic('S');
			jButtonNext.setVisible(true);
			jButtonNext.setPreferredSize(new Dimension(100,25));
		}
		return jButtonNext;
	}
	/**
	 * Inicializa el botn 'Cancelar'
	 * @return
	 */
	public JButton getJButtonCancel() {
		if(jButtonCancel == null) {
			jButtonCancel = new JButton();
			jButtonCancel.setText(PluginServices.getText(this,"cancel"));
			jButtonCancel.setMnemonic('C');
			jButtonCancel.setPreferredSize(new Dimension(100,25));
		}
		return jButtonCancel;
	}
	
	public void setTargetNad(boolean tarNad){
		targetNad = tarNad;
	}
	
	public boolean getTargetNad(){
		return targetNad;
	}
	
	/*
	 * revisar esta solucion
	 */
	public ICrs getProjection() {
		if (sin_tr) {
			ICrs crs;
			try {
				if (getDataSource().equals(PluginServices.getText(this,"EPSG"))){
					//crs = new CrsFactory().getCRS(crsMainPanel.getEpsgPanel().getCodeCRS(),crsMainPanel.getEpsgPanel().getWKT());
					crs = new CrsFactory().getCRS("EPSG:"+crsMainPanel.getEpsgPanel().getCodeCRS());
				}
				else if (getDataSource().equals(PluginServices.getText(this,"IAU2000"))){
					//crs = new CrsFactory().getCRS(crsMainPanel.getIauPanel().getCodeCRS(),crsMainPanel.getIauPanel().getWKT());
					crs = new CrsFactory().getCRS("IAU2000:"+crsMainPanel.getIauPanel().getCodeCRS());
				} 
				else if (getDataSource().equals(PluginServices.getText(this,"recientes"))){
					//crs = new CrsFactory().getCRS(crsMainPanel.getRecentsPanel().getCodeCRS(),crsMainPanel.getRecentsPanel().getCrs().getWKT());
					crs = new CrsFactory().getCRS(crsMainPanel.getCrsAndTransformationRecentsPanel().getAuthority());
				} 
				else if (getDataSource().equals(PluginServices.getText(this,"ESRI"))){
					//crs = new CrsFactory().getCRS(crsMainPanel.getEsriPanel().getCodeCRS(),crsMainPanel.getEsriPanel().getWKT());
					crs = new CrsFactory().getCRS("ESRI:"+crsMainPanel.getEsriPanel().getCodeCRS());
				} 
				else if (getDataSource().equals(PluginServices.getText(this,"newCRS"))){
					crs = new CrsFactory().getCRS("USR:"+crsMainPanel.getNewCrsPanel().getCodeCRS());
				}
				else {
					sin_tr = false;
					return null;
				}
				sin_tr = false;
				return crs;
			} catch (CrsException e) {
				e.printStackTrace();
			}
			return null;
		}		
		return (ICrs) crsfirst;		
	}
	
	public void setProjection(IProjection proj) {
		crsfirst = proj;
	}
	
	/**
	 * Mtodo para aplicarle los eventos necesarios en el panel actual. La
	 * definicin de estos listeners estarn en la clase CRSMainTrPanelListener
	 *
	 */
	private void setListeners(){

		CRSMainTrPanelListener listener = new CRSMainTrPanelListener(this);
		
		jButtonAccept.addActionListener(listener);
		jButtonBefore.addActionListener(listener);
		jButtonCancel.addActionListener(listener);
		jButtonNext.addActionListener(listener);
		crsMainPanel.getJComboOptions().addItemListener(listener);
		getJComboOptions().addActionListener(listener);
		getRecentsTrPanel().getJButtonInfo().addActionListener(listener);
		
		ListSelectionModel rowSM = crsMainPanel.getEpsgPanel().getJTable().getSelectionModel();
		rowSM.addListSelectionListener(listener);
		
		ListSelectionModel rowSMiau = crsMainPanel.getIauPanel().getJTable().getSelectionModel();
		rowSMiau.addListSelectionListener(listener);
		
		ListSelectionModel rowSMrecents = crsMainPanel.getCrsAndTransformationRecentsPanel().getJTable().getSelectionModel();
		rowSMrecents.addListSelectionListener(listener);
		
		ListSelectionModel rowSMesri = crsMainPanel.getEsriPanel().getJTable().getSelectionModel();
		rowSMesri.addListSelectionListener(listener);
		
		ListSelectionModel rowSMusr = crsMainPanel.getNewCrsPanel().getJTable().getSelectionModel();
		rowSMusr.addListSelectionListener(listener);
		
		crsMainPanel.getEsriPanel().getJTable().addMouseListener(listener);
		crsMainPanel.getEpsgPanel().getJTable().addMouseListener(listener);
        crsMainPanel.getCrsAndTransformationRecentsPanel().getJTable().addMouseListener(listener);
        crsMainPanel.getIauPanel().getJTable().addMouseListener(listener);
        crsMainPanel.getNewCrsPanel().getJTable().addMouseListener(listener);
        getEpsgTrPanel().getJTable().addMouseListener(listener);
        getRecentsTrPanel().getJTable().addMouseListener(listener);
        
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
		
	public void setDataSource(String sour){
		dataSource = sour;
	}
	
	public String getDataSource(){
		return crsMainPanel.getDataSource();
	}
	
	public WindowInfo getWindowInfo() {
		WindowInfo m_viewinfo=new WindowInfo(WindowInfo.MODALDIALOG);
   		m_viewinfo.setTitle(PluginServices.getText(this,"crsview"));
		return m_viewinfo;
	}

	public CRSMainPanel getCrsMainPanel() {
		return crsMainPanel;
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

	public boolean isEpsg_tr() {
		return epsg_tr;
	}

	public boolean isManual_tr() {
		return manual_tr;
	}

	public boolean isNads_tr() {
		return nads_tr;
	}
	
	public boolean isRecents_tr() {
		return recents_tr;
	}

	public boolean isSin_tr() {
		return sin_tr;
	}

	public void setEpsg_tr(boolean epsg_tr) {
		this.epsg_tr = epsg_tr;
	}

	public void setManual_tr(boolean manual_tr) {
		this.manual_tr = manual_tr;
	}

	public void setNads_tr(boolean nads_tr) {
		this.nads_tr = nads_tr;
	}
	
	public void setRecents_tr(boolean recents_tr) {
		this.recents_tr = recents_tr;
	}

	public void setSin_tr(boolean sin_tr) {
		this.sin_tr = sin_tr;
	}

	public boolean isCancelYes() {
		return cancelYes;
	}

	public void setCancelYes(boolean cancelYes) {
		this.cancelYes = cancelYes;
	}

	public IProjection getCrsfirst() {
		return crsfirst;
	}

	public JPanel getJPanelMain() {
		return jPanelMain;
	}

	public String getNewSelection() {
		return newSelection;
	}

	public void setNewSelection(String newSelection) {
		this.newSelection = newSelection;
	}
	
	public CrsWkt getCrsWkt_target() {
		return crsWkt_target;
	}
	
	public void setCrsWkt_target(CrsWkt crsWkt_target) {
		this.crsWkt_target = crsWkt_target;
	}

	public TransformationVistaPanel getVistaTrPanel() {
		return vistaTrPanel;
	}

	public TransformationCapaPanel getCapaTrPanel() {
		return capaTrPanel;
	}

	public boolean isVista_tr() {
		return vista_tr;
	}

	public boolean isCapa_tr() {
		return capa_tr;
	}

	public boolean isCompuesta_tr() {
		return compuesta_tr;
	}

	public void setCapa_tr(boolean capa_tr) {
		this.capa_tr = capa_tr;
	}

	public void setCompuesta_tr(boolean compuesta_tr) {
		this.compuesta_tr = compuesta_tr;
	}

	public void setVista_tr(boolean vista_tr) {
		this.vista_tr = vista_tr;
	}

	public Object getWindowProfile() {
		return WindowInfo.DIALOG_PROFILE;
	}
	/*public EpsgConnection getConnection() {
		return connection;
	}

	public void setConnection(EpsgConnection connect) {
		this.connection = connect;
	}*/
}
