/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
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
	 *   Av. Blasco Ibañez, 50
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
package org.gvsig.remotesensing.tasseledcap;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import org.gvsig.fmap.mapcontext.MapContext;
import org.gvsig.fmap.mapcontext.layers.FLayers;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.gui.beans.buttonspanel.ButtonsPanel;
import org.gvsig.gui.beans.buttonspanel.ButtonsPanelEvent;
import org.gvsig.gui.beans.buttonspanel.ButtonsPanelListener;
import org.gvsig.gui.beans.defaultbuttonspanel.DefaultButtonsPanel;
import org.gvsig.gui.beans.swing.JFileChooser;
import org.gvsig.raster.IProcessActions;
import org.gvsig.raster.RasterLibrary;
import org.gvsig.raster.buffer.WriterBufferServer;
import org.gvsig.raster.dataset.GeoRasterWriter;
import org.gvsig.raster.util.ExtendedFileFilter;
import org.gvsig.raster.util.PropertyEvent;
import org.gvsig.raster.util.PropertyListener;
import org.gvsig.raster.util.RasterToolsUtil;
import org.gvsig.remotesensing.principalcomponents.PCStatistics;
import org.gvsig.remotesensing.principalcomponents.PCStatisticsProcess;
import org.gvsig.remotesensing.principalcomponents.gui.BandTableFormat;
import org.gvsig.remotesensing.principalcomponents.gui.PrincipalComponentCalculusPanel;

import com.iver.andami.PluginServices;
import com.iver.andami.Utilities;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.IWindowListener;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.iver.cit.gvsig.project.documents.view.gui.View;
import com.iver.utiles.swing.JComboBox;

/**
 * Clase que implementa la interfaz para las transformaciones multiespectrales de Componentes
 * Principales y TasseledCap.
 * @author aMuÑoz (alejandro.munoz@uclm.es)
 * @version 30/4/2008
 * */

public class TransformationPanel extends DefaultButtonsPanel implements PropertyListener, IWindow, IWindowListener,
ButtonsPanelListener,ActionListener,FocusListener, IProcessActions{

	private static final long serialVersionUID = 1L;
	private int 					width=380;
	private int 					height=450;
	private MapContext 				m_MapContext=null;
	private JScrollPane				scrollBandas=null;
	private BandTableFormat 		mModeloTabla=null;
	private JTable 					jTableBandas=null;
	private FLayers 				layers=null;
	private JComboBox				comboLayers= null;
	private int[] 					bands= null;
	private JPanel					panelSeleccion=null;
	private JRadioButton 			rButtonFile=null;
	private JRadioButton 			rButtonMemory =null;
	private JPanel 					nombreCapa=null;
	private JTextField 				jTextNombreCapa=null;
	private JPanel					panelCentral= null;
	private JRadioButton			rTasseledCapOption=null;
	private JComboBox				typeTasseledCap=null;
	private JRadioButton			rPrincipalComponentOption=null;
	private View view=null;
	private PCStatisticsProcess 	statisticsProcess= null;
	private boolean selectedBands[]= null;

	public void actionValueChanged(PropertyEvent e) {

	}

	public WindowInfo getWindowInfo() {
		WindowInfo m_viewinfo = new WindowInfo(WindowInfo.MODELESSDIALOG | WindowInfo.RESIZABLE | WindowInfo.MAXIMIZABLE);
		m_viewinfo.setWidth(width);
		m_viewinfo.setHeight(height);
		m_viewinfo.setTitle(PluginServices.getText(this,"transformacion_title"));
		m_viewinfo.setX(300);
		return m_viewinfo;
	}


	/**
	 * @param view vista de la aplicacion
	 */
	public TransformationPanel(View view) {
		m_MapContext = view.getModel().getMapContext();
		this.view=view;
		layers = m_MapContext.getLayers();
		this.setPreferredSize(new Dimension(width, height));
		this.setSize(width, height);
		this.setLayout(new BorderLayout(3, 3));
		this.addButtonPressedListener(this);
		initialize();
	}


	/**
	 * Inicialización del cuadro de dialogo
	 */
	private void initialize(){
		BorderLayout bd=new BorderLayout();
		this.setLayout(bd);
		TitledBorder topBorder = BorderFactory.createTitledBorder(" ");
		topBorder.setTitlePosition(TitledBorder.TOP);
		this.setBorder(new CompoundBorder(topBorder,new EmptyBorder(0,5,5,5)));
		this.setLayout(new BorderLayout());
		this.add(getSelectionImagePanel(), BorderLayout.NORTH);
		JPanel aux= new JPanel(); aux.setBackground(Color.RED);
		this.add(getPanelCentral(), BorderLayout.CENTER);
		this.add(getOutputOptionsPanel(), BorderLayout.SOUTH);
		getBandas();
	}


	/**
	 * Toma las Bandas de la imagen seleccionada en el combo Imagen
	 */
	public void getBandas(){

		// Antes de insertar se borra lo anterior
		getModeloTabla().LimpiarLista();

		FLyrRasterSE rasterLayer= (FLyrRasterSE)layers.getLayer((String)getComboCapas().getSelectedItem());
		String bandas[]=new String[rasterLayer.getBandCount()];
		bands= new int[rasterLayer.getBandCount()];
		if (layers.getLayer((String)getComboCapas().getSelectedItem()) instanceof  FLyrRasterSE ){
			for (int j=0; j<rasterLayer.getBandCount(); j++){
					String s=(PluginServices.getText(this,"banda"))+(j+1);
					bandas[j]=s;
					bands[j]=j;
				}
			for (int i=0;i<bandas.length;i++)
				getModeloTabla().addRow(bandas[i],true);
			getTableBands().updateUI();
		}
	}

	/**
	 * @return modelo de tabla
	 * @see BandTableFormat
	 */
	public BandTableFormat getModeloTabla() {
		if(mModeloTabla==null){
			mModeloTabla=new BandTableFormat();
		}
		return mModeloTabla;
	}

	/**
	 * @return scroll con bandas del ráster
	 */
	public JScrollPane getScrollBands() {
		if (scrollBandas==null){
			scrollBandas=new JScrollPane();
		}
		getTableBands().getColumn("").setPreferredWidth(30);
		scrollBandas.setPreferredSize(new Dimension(250,80));
		scrollBandas.setViewportView(getTableBands());
		scrollBandas.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		return scrollBandas;
	}

	/**
	 * @return tabla conlas bandas de la imagen
	 */
	private JTable getTableBands(){
		if (jTableBandas==null){
			jTableBandas=new JTable(getModeloTabla());
		}
		return jTableBandas;
	}


	/**
	 * @return JComboBox con los ráster cargados en la vista.
	 */
	 public JComboBox getComboCapas() {
		if (comboLayers==null){
			ComboBoxModel jComboBoxLayersModel = new
	        DefaultComboBoxModel(getLayerNames());
			comboLayers = new JComboBox();
			comboLayers.setModel(jComboBoxLayersModel);
			comboLayers.setEnabled(true);
			comboLayers.addActionListener(this);
			comboLayers.setSelectedIndex(0);
		}
		return comboLayers;
	}



	 /**
	  * @return array de string con los nombre de las capas raster
	  */
	private String[] getLayerNames() {
		int i, capasraster=0;
		for (i = 0; i < layers.getLayersCount(); i++) {
			if (layers.getLayer(i)instanceof  FLyrRasterSE)
				capasraster++;
		}
		// Solo se toman las capas raster.
		String[] sNames = new String[capasraster];
		capasraster=0;

		for (i = 0; i < layers.getLayersCount(); i++) {
			if (layers.getLayer(i)instanceof FLyrRasterSE){
				sNames[capasraster] =layers.getLayer(i).getName();
				capasraster++;
			}
		}
		return sNames;
	}


	public JPanel getSelectionImagePanel(){
		if(panelSeleccion==null){
			panelSeleccion= new JPanel();
			TitledBorder topBorder = BorderFactory.createTitledBorder(PluginServices.getText(this,"imagen"));
			topBorder.setTitlePosition(TitledBorder.TOP);
			panelSeleccion.setBorder(topBorder);
			JPanel aux= new JPanel();
			aux.setPreferredSize(new Dimension(300,110));
			BorderLayout bd=new BorderLayout();
			aux.setLayout(bd);
			aux.add(getComboCapas(),BorderLayout.NORTH);
			aux.add(getScrollBands(),BorderLayout.SOUTH);
			panelSeleccion.add(aux);
		}
		return panelSeleccion;
	}

	/**
	  *Actualización de las bandas cuando cambia la imagen seleccionada
	  */
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getSource()==getComboCapas()){
			getBandas();
		}
		if(arg0.getSource()==getRTasseledCapOption()){
			getTypeTasseledCap().setEnabled(getRTasseledCapOption().isSelected());
		}
		if(arg0.getSource()==getRPrincipalComponentOption()){
			getTypeTasseledCap().setEnabled(getRTasseledCapOption().isSelected());
		}
	}


	public void windowActivated() {
		// TODO Auto-generated method stub

	}

	public void windowClosed() {
		// TODO Auto-generated method stub

	}

	public void actionButtonPressed(ButtonsPanelEvent e) {
		// Botón de Aceptar
		if (e.getButton() == ButtonsPanel.BUTTON_ACCEPT) {
			aplicar();
			close();
		}

		// Botón de Aplicar
		if (e.getButton() == ButtonsPanel.BUTTON_APPLY) {
			aplicar();
		}

		// Botón de Cerrar
		if (e.getButton() == ButtonsPanel.BUTTON_CANCEL) {
			close();
		}

	}

	public void focusGained(FocusEvent e) {
		// TODO Auto-generated method stub

	}

	public void focusLost(FocusEvent e) {
		// TODO Auto-generated method stub

	}


	/**
	 * @return panel con las opciones de salida.
	 */
	public JPanel getOutputOptionsPanel() {
		if (nombreCapa==null){
			nombreCapa=new JPanel();
			nombreCapa.setPreferredSize(new Dimension(400,130));
			TitledBorder topBorder = BorderFactory.createTitledBorder(PluginServices.getText(this, "salida"));
			topBorder.setTitlePosition(TitledBorder.TOP);
			nombreCapa.setBorder(topBorder);
			GridBagConstraints gridBagConstraints;
			JPanel radioPanel = new JPanel();
			radioPanel.setLayout(new GridBagLayout());
			radioPanel.setBorder(BorderFactory.createTitledBorder(""));
			ButtonGroup buttonGroup = new ButtonGroup();
			buttonGroup.add(getRadioMemory());
			gridBagConstraints = new java.awt.GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 1;
			gridBagConstraints.anchor = GridBagConstraints.WEST;
			gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
			radioPanel.add(getRadioMemory(),gridBagConstraints);
			buttonGroup.add(getRadioFile());
			gridBagConstraints = new java.awt.GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 2;
			gridBagConstraints.anchor = GridBagConstraints.WEST;
			gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
			radioPanel.add(getRadioFile(),gridBagConstraints);
			//Establece la separacion entre los elementos
			nombreCapa.setLayout(new GridBagLayout());
			gridBagConstraints = new java.awt.GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.anchor = GridBagConstraints.WEST;
			gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
			nombreCapa.add(new JLabel(("Nombre:"),SwingConstants.RIGHT ),gridBagConstraints);

			gridBagConstraints = new java.awt.GridBagConstraints();
			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.anchor = GridBagConstraints.WEST;
			gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
			nombreCapa.add(getJTextNombreCapa(),gridBagConstraints);

			gridBagConstraints = new java.awt.GridBagConstraints();
			gridBagConstraints.gridx = 2;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.anchor = GridBagConstraints.WEST;
			gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
			nombreCapa.add(radioPanel,gridBagConstraints);
		}
		return nombreCapa;
	}


	/**
	 * @return JRadioButton de generar fichero
	 */
	public JRadioButton getRadioFile(){
		if (rButtonFile == null){
			rButtonFile = new JRadioButton(PluginServices.getText(this,"a_fichero"));
			rButtonFile.addActionListener(this);
		}
		return rButtonFile;
	}

	/**
	 * @return JRadioButton de generar en memoria
	 */
	public JRadioButton getRadioMemory(){
		if (rButtonMemory == null){
			rButtonMemory = new JRadioButton(PluginServices.getText(this,"a_memoria"),true);
			rButtonMemory.addActionListener(this);
		}
		return rButtonMemory;
	}
	 /**
	  * @return javax.swing.JTextField
	  */
	 public JTextField getJTextNombreCapa() {
		if (jTextNombreCapa==null) {
			jTextNombreCapa=new JTextField(10);
			jTextNombreCapa.setText(RasterLibrary.getOnlyLayerName());
			jTextNombreCapa.addFocusListener(this);
			RasterLibrary.addOnlyLayerNameListener(this);
		}
		return jTextNombreCapa;
	}

	 public JPanel getPanelCentral(){
		 if(panelCentral==null){
			panelCentral= new JPanel();
			panelCentral.setLayout(new BorderLayout());
			//panelCentral.setBorder(new EmptyBorder(10,10,10,10));
			TitledBorder topBorder = BorderFactory.createTitledBorder(PluginServices.getText(this,"transformacion"));
			topBorder.setTitlePosition(TitledBorder.TOP);
			panelCentral.setBorder(topBorder);
			panelCentral.setEnabled(false);
			// Añadir aqui si el panel tiene que contener algún parámetro
			JPanel panel = new JPanel();
			ButtonGroup btgr= new ButtonGroup();
			btgr.add(getRPrincipalComponentOption());
			btgr.add(getRTasseledCapOption());
			panel.setPreferredSize(new Dimension(200,10));
			panel.setBorder(new EmptyBorder(5,10,10,10));
			BorderLayout bd= new BorderLayout();
			panel.setLayout(bd);
			panel.add(getRPrincipalComponentOption(),BorderLayout.NORTH);
			panel.add(getRTasseledCapOption(),BorderLayout.CENTER);
			panel.add(getTypeTasseledCap(),BorderLayout.SOUTH);
			panelCentral.add(panel,BorderLayout.CENTER);
		 }
		return panelCentral;
	 }

	public JRadioButton getRPrincipalComponentOption() {
		if(rPrincipalComponentOption==null){
			rPrincipalComponentOption= new JRadioButton(PluginServices.getText(this,"principal_component"));
			rPrincipalComponentOption.setSelected(true);
			rPrincipalComponentOption.addActionListener(this);
		}
		return rPrincipalComponentOption;
	}

	public JRadioButton getRTasseledCapOption() {
		if(rTasseledCapOption==null){
		   rTasseledCapOption= new JRadioButton(PluginServices.getText(this,"tasseledcap"));
		   rTasseledCapOption.addActionListener(this);
		}
		return rTasseledCapOption;
	}

	public JComboBox getTypeTasseledCap() {
		if(typeTasseledCap==null){
			   typeTasseledCap= new JComboBox();
			   typeTasseledCap.setEnabled(false);
			   typeTasseledCap.addItem(new String(PluginServices.getText(this,"landsatMS")));
			   typeTasseledCap.addItem(new String(PluginServices.getText(this,"landsatTM")));
			   typeTasseledCap.addItem(new String(PluginServices.getText(this,"landsatETM")));

		}
		return typeTasseledCap;
	}


	/**
	 * Devuelve la ruta del fichero donde se va a guardar, en caso de guardarse
	 * en memoria, calcula el nombre sin preguntar y devuelve la ruta.
	 * @return string con la ruta de salida
	 */
	public String getFileSelected() {
		String path = "";
		if (getRadioFile().isSelected()) {
			JFileChooser chooser = new JFileChooser("TRANSFORMATION_PANEL",JFileChooser.getLastPath("TRANSFORMATION_PANEL", null));
			chooser.setDialogTitle(PluginServices.getText(this, "seleccionar_fichero"));
			//Añadimos las extensiones que hayan sido registradas en el driver
			String[] extList = GeoRasterWriter.getDriversExtensions();
			for(int i=0;i<extList.length;i++)
				chooser.addChoosableFileFilter(new ExtendedFileFilter(extList[i]));

			if (chooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION)
				return null;
			JFileChooser.setLastPath("TRANSFORMATION_PANEL", chooser.getSelectedFile());
			ExtendedFileFilter fileFilter = (ExtendedFileFilter) chooser.getFileFilter();
			path = fileFilter.getNormalizedFilename(chooser.getSelectedFile());
		} else {
			String file = getJTextNombreCapa().getText();
			path = Utilities.createTempDirectory() + File.separator + getJTextNombreCapa().getText() + ".tif";
			if(file.compareTo(RasterLibrary.getOnlyLayerName()) == 0)
				RasterLibrary.usesOnlyLayerName();
			updateNewLayerText();
		}
		return path;
	}

	 public void updateNewLayerText() {
		 getJTextNombreCapa().setText(RasterLibrary.getOnlyLayerName());
 	}


	/** Se construye el proceso y se lanza a ejecución*/
	public void aplicar(){
		FLyrRasterSE rasterLayer= (FLyrRasterSE)layers.getLayer((String)getComboCapas().getSelectedItem());
		String filename= getFileSelected();

		boolean bandsSelected[] = getModeloTabla().getSeleccionadas();
		int bandCount = 0;
		for (int i= 0; i<bandsSelected.length;i++)
			if(bandsSelected[i]==true)
				bandCount++;
		int bandList[] = new int[bandCount];
		int index = 0;
		for (int i = 0; i < bandsSelected.length; i++){
			if (bandsSelected[i])
				{	bandList[index]=i;
					index++;
				}
		}

		if(filename!=null){

			if(getRTasseledCapOption().isSelected()){
				// Comprobaciones
				if(bandList.length!=4&& getTypeTasseledCap().getSelectedIndex()==0)
				{
					RasterToolsUtil.messageBoxInfo(PluginServices.getText(this,"transformMSS_error"),"aviso" );
					return;
				}
				else if(bandList.length!=6 && getTypeTasseledCap().getSelectedIndex()==1)
				{
					RasterToolsUtil.messageBoxInfo(PluginServices.getText(this,"transformTM_error"),"aviso" );
					return;
				}
				else if(bandList.length!=6 && getTypeTasseledCap().getSelectedIndex()==2){
					RasterToolsUtil.messageBoxInfo(PluginServices.getText(this,"transformETM_error"),"aviso" );
					return;
				}

				else{
					TasseledCapProcess process = new TasseledCapProcess();
					process.addParam("filename",filename);
					process.addParam("bands",bandList);
					process.addParam("datawriter",new WriterBufferServer());
					process.addParam("viewName", view.getName());
					process.addParam("type",new Integer(getTypeTasseledCap().getSelectedIndex()));
					process.addParam("layer",rasterLayer);
					process.start();
				}
			}

			if(getRPrincipalComponentOption().isSelected()){

				statisticsProcess = new PCStatisticsProcess();
				selectedBands= getModeloTabla().getSeleccionadas();
				statisticsProcess.addParam("selectedBands",selectedBands);
				statisticsProcess.addParam("inputRasterLayer",rasterLayer);
				statisticsProcess.setActions(this);
				statisticsProcess.start();
			}
		}
	}

	/**Cierra ventana actual*/
	public void close(){
		try {
			RasterLibrary.removeOnlyLayerNameListener(this);
			PluginServices.getMDIManager().closeWindow(this);
		} catch (ArrayIndexOutOfBoundsException ex) {
			// Si la ventana no se puede eliminar no hacemos nada
		}
	}

	public void interrupted() {
		// TODO Auto-generated method stub

	}

	public void end(Object param) {
		String fileName = Utilities.createTempDirectory() + File.separator;
		fileName = fileName + RasterLibrary.getOnlyLayerName() + ".tif";
		RasterLibrary.usesOnlyLayerName();

		PrincipalComponentCalculusPanel altPrincipalComponentCalculusPanel = new PrincipalComponentCalculusPanel(view,(PCStatistics)param,statisticsProcess,selectedBands);
		altPrincipalComponentCalculusPanel.setFilename(fileName);
		PluginServices.getMDIManager().addWindow(altPrincipalComponentCalculusPanel);
	}

	public Object getWindowProfile() {
		return WindowInfo.PROPERTIES_PROFILE;
	}

}
