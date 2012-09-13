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
package org.gvsig.remotesensing.classification.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
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
import org.gvsig.raster.RasterLibrary;
import org.gvsig.raster.dataset.GeoRasterWriter;
import org.gvsig.raster.grid.GridException;
import org.gvsig.raster.util.ExtendedFileFilter;
import org.gvsig.raster.util.PropertyEvent;
import org.gvsig.raster.util.PropertyListener;
import org.gvsig.raster.util.RasterToolsUtil;
import org.gvsig.rastertools.roi.ui.ROIsTablePanel;
import org.gvsig.remotesensing.classification.ClassificationGeneralProcess;
import org.gvsig.remotesensing.classification.ClassificationMaximumLikelihoodProcess;
import org.gvsig.remotesensing.classification.ClassificationMinimumDistanceProcess;
import org.gvsig.remotesensing.classification.ClassificationParallelepipedProcess;
import org.gvsig.remotesensing.classification.NoSupervisedClassificationProcess;
import org.gvsig.remotesensing.principalcomponents.gui.BandTableFormat;

import com.iver.andami.PluginServices;
import com.iver.andami.Utilities;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.IWindowListener;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.iver.cit.gvsig.project.documents.view.gui.View;
import com.iver.utiles.swing.JComboBox;

/**
 * Clase que define la interfaz del proceso de clasificacion de una imagen. Recoge la
 * imagen a clasificar con la selección de bandas correspondiente. Posibilita la elección del
 * método empleado y da la posibilidad de manejar la lista de clases que intervienen en el proceso.
 * Permite ajustar algunos parametros del proceso y configurar las opciones de salida.
 *
 * @author Alejandro Muñoz Sanchez (alejandro.munoz@uclm.es)
 * @author Diego Guerrero Sevilla (diego.guerrero@uclm.es)
 * @version 19/10/2007
 */
public class ClassificationPanel extends DefaultButtonsPanel implements PropertyListener, IWindow, IWindowListener,
	ButtonsPanelListener,ActionListener,FocusListener{

	private static final long 		serialVersionUID = 1L;
	private View 					view = null;
	private int 					width=400;
	private int 					height=555;
	private MapContext 				m_MapContext=null;
	private FLayers 				layers=null;
	private int nMethods			= 4;
	private JRadioButton 			rButtonFile=null;
	private JRadioButton 			rButtonMemory =null;
	private JTextField 				jTextNombreCapa=null;
	private JPanel 					panelTab1=null;
	private JPanel 					panelTab2=null;
	private JScrollPane				scrollBandas=null;
	private JScrollPane				scrollTable=null;
	private JComboBox				comboLayers= null;
	private JComboBox				comboMethods= null;
	private ROIsTablePanel 			roisTablePanel= null;
	private BandTableFormat 		mModeloTabla=null;
	private JTable 					jTableBandas=null;
	private JPanel 					nombreCapa=null;
	private int[] 					bands= null;
	private JPanel					panelParametros=null;
	private JTextField				stevField =  null;
	private JPanel					panelParametros2=null;
	private JTextField				numClases =  null;
	/**
	 * @see com.iver.mdiApp.ui.MDIManager.View#getViewInfo()
	 */
	public WindowInfo getWindowInfo(){
		WindowInfo m_viewinfo = new WindowInfo(WindowInfo.MODELESSDIALOG | WindowInfo.RESIZABLE | WindowInfo.MAXIMIZABLE);
		m_viewinfo.setWidth(width);
		m_viewinfo.setHeight(height);
		m_viewinfo.setTitle(PluginServices.getText(this,"classification"));
		m_viewinfo.setX(300);
		return m_viewinfo;
	}

	/**
	 * @param view vista de la aplicacion
	 */
	public ClassificationPanel(View view) {
		this.view = view;
		m_MapContext = view.getModel().getMapContext();
		layers = m_MapContext.getLayers();
		this.setPreferredSize(new Dimension(width, height));
		this.setSize(width, height);
		this.setLayout(new BorderLayout(1, 1));
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
		JTabbedPane tabbedPane = new JTabbedPane();
	 	tabbedPane.addTab(PluginServices.getText(this, "operacion"), getPanelTab1());
		tabbedPane.addTab(PluginServices.getText(this, "opciones"), getPanelTab2());
		this.setLayout(new BorderLayout());
		this.add(tabbedPane, BorderLayout.CENTER);
		getBandas();
	}

	/**
	 * @return panel para el tab operacion.
	 */
	 private JPanel getPanelTab1(){
		if (panelTab1==null){
			panelTab1 = new JPanel();
			panelTab1.setLayout(new GridBagLayout());

			// Panel superior opcion Selecion de imagen y bandas
			JPanel selectionImagenPanel= new JPanel();
			TitledBorder topBorder = BorderFactory.createTitledBorder(" Imagen");
			topBorder.setTitlePosition(TitledBorder.TOP);
			JPanel aux= new JPanel();
			aux.setPreferredSize(new Dimension(300,110));

			BorderLayout bd=new BorderLayout();
			aux.setLayout(bd);

			aux.add(getComboCapas(),BorderLayout.NORTH);
			aux.add(getScrollBands(),BorderLayout.SOUTH);
			selectionImagenPanel.add(aux);

			selectionImagenPanel.setBorder(topBorder);
			GridBagConstraints c = new GridBagConstraints();
			c.fill = GridBagConstraints.BOTH;
			c.insets=new Insets(2,2,2,2);
			c.weightx = 1;
			c.weighty = 0.5;
			c.gridx = 0;
			c.gridy = 0;
			panelTab1.add(selectionImagenPanel, c);


			// Panel seleccion metodo
			JPanel methodPanel= new JPanel();
			TitledBorder topBorderMethod = BorderFactory.createTitledBorder("Metodo");
			topBorder.setTitlePosition(TitledBorder.TOP);
			methodPanel.setBorder(topBorderMethod);
			methodPanel.add(new JLabel(PluginServices.getText(this, "metodo")));
			methodPanel.add(getComboMethods());
			c.fill = GridBagConstraints.BOTH;
			c.insets=new Insets(2,2,2,2);
			c.weightx = 1;
			c.weighty = 0.1;
			c.gridx = 0;
			c.gridy = 1;
			panelTab1.add(methodPanel, c);

			// Insercion del panel de las clases
			c.fill = GridBagConstraints.BOTH;
			c.insets=new Insets(2,2,2,2);
			c.weightx = 1;
			c.weighty = 1;
			c.gridx = 0;
			c.gridy = 2;
			panelTab1.add(getRoisTablePanel(), c);
		 }
		 return panelTab1;
	 }

	 /**
	  * @return Panel para el tab de opciones.
	  */
	 private JPanel getPanelTab2(){
		if (panelTab2==null){
			panelTab2 = new JPanel();
			panelTab2.setLayout(new GridBagLayout());

			JPanel parameterPanel= new JPanel();
			TitledBorder topBorder = BorderFactory.createTitledBorder(PluginServices.getText(this, "parametros"));
			topBorder.setTitlePosition(TitledBorder.TOP);
			JPanel aux= new JPanel();
			aux.setPreferredSize(new Dimension(300,110));

			BorderLayout bd=new BorderLayout();
			aux.setLayout(bd);

			//aux.add(new JButton(),BorderLayout.NORTH);
			parameterPanel.add(aux);

			parameterPanel.setBorder(topBorder);
			BorderLayout bordlayout= new BorderLayout();
			parameterPanel.setLayout(bordlayout);
			parameterPanel.add(getPanelParametros(),BorderLayout.NORTH);
			parameterPanel.add(getPanelParametros2(),BorderLayout.CENTER);
			GridBagConstraints c = new GridBagConstraints();
			c.fill = GridBagConstraints.BOTH;
			c.insets=new Insets(2,2,2,2);
			c.weightx = 1;
			c.weighty = 1;
			c.gridx = 0;
			c.gridy = 0;
			panelTab2.add(parameterPanel, c);

			// Insercion del panel de las clases
			c.fill = GridBagConstraints.BOTH;
			c.insets=new Insets(2,2,2,2);
			c.weightx = 0.05;
			c.weighty = 0.05;
			c.gridx = 0;
			c.gridy = 1;

			panelTab2.add(getOutputOptionsPanel(), c);

		}
		return panelTab2;
	 }


	 // Paneel de parametros

	 JPanel getPanelParametros(){
		 if (panelParametros==null){

			 panelParametros= new JPanel();
			 panelParametros.add(new JLabel(PluginServices.getText(this,"max_stev_interval_multiplo")));
			 panelParametros.add(getStevField());
			 panelParametros.setVisible(false);

		 }
		 return panelParametros;
	 }


	 JPanel getPanelParametros2(){
		 if (panelParametros2==null){
			 BorderLayout bd= new BorderLayout();
			 panelParametros2= new JPanel();
			 panelParametros2.add(new JLabel(PluginServices.getText(this,"numclases")),BorderLayout.NORTH);
			 panelParametros2.add(getNumClases());
			 panelParametros2.setVisible(false);
		 }
		 return panelParametros2;
	 }


	 /**
	 * @return  Scroll con la tabla de clases intervienen en el proceso de clasificación.
	 */
	 public JScrollPane getScrollTable() {

		if(scrollTable==null){
			scrollTable=new JScrollPane();
			TitledBorder topBorder = BorderFactory.createTitledBorder((PluginServices.getText(this,"clases")));
		    topBorder.setTitlePosition(TitledBorder.TOP);
		    scrollTable.setBorder(new CompoundBorder(topBorder,new EmptyBorder(2,2,2,2)));
			scrollTable.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			scrollTable.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		}
		return scrollTable;
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


	/**
	 * Especificar el nombre de la nueva capa para el recuadro de texto asignándo
	 * en cada llamada un nombre consecutivo.
	 */
	 public void updateNewLayerText() {
		 	getJTextNombreCapa().setText(RasterLibrary.getOnlyLayerName());
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
	 * Acciones a ejecuar al cerrar el diálogo.
	 */
	public void close(){
		try {
			RasterLibrary.removeOnlyLayerNameListener(this);
			PluginServices.getMDIManager().closeWindow(this);
		} catch (ArrayIndexOutOfBoundsException ex) {
			// Si la ventana no se puede eliminar no hacemos nada
		}
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


	/**
	 * Devuelve la ruta del fichero donde se va a guardar, en caso de guardarse
	 * en memoria, calcula el nombre sin preguntar y devuelve la ruta.
	 * @return string con la ruta de salida
	 */
	public String getFileSelected() {
		String path = "";
		if (getRadioFile().isSelected()) {
			JFileChooser chooser = new JFileChooser("CLASSIFICATION_PANEL",JFileChooser.getLastPath("CLASSIFICATION_PANEL", null));
			chooser.setDialogTitle(PluginServices.getText(this, "seleccionar_fichero"));
			//Añadimos las extensiones que hayan sido registradas en el driver
			String[] extList = GeoRasterWriter.getDriversExtensions();
			for(int i=0;i<extList.length;i++)
				chooser.addChoosableFileFilter(new ExtendedFileFilter(extList[i]));

			if (chooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION)
				return null;

			JFileChooser.setLastPath("CLASSIFICATION_PANEL", chooser.getSelectedFile());
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

	/**
	 * Acciones al aceptar
	 */
	public void accept(){

		// Imagen que va a ser clasificada
		FLyrRasterSE rasterLayer= ((FLyrRasterSE)layers.getLayer((String) getComboCapas().getSelectedItem()));
		/*
		 * Bandas que intervienen en la clasificación.
		 */
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

		ClassificationGeneralProcess process=null;
		if(getComboMethods().getSelectedIndex()==0)
			process = new ClassificationMaximumLikelihoodProcess();
		if(getComboMethods().getSelectedIndex()==1)
			process =  new ClassificationMinimumDistanceProcess();
		if(getComboMethods().getSelectedIndex()==2){
			process= new ClassificationParallelepipedProcess();
			process.addParam("dev", new Double(Double.parseDouble(getStevField().getText())));
		}
		if(getComboMethods().getSelectedIndex()==3){
			process= new NoSupervisedClassificationProcess();
			process.addParam("numClases",new Integer(Integer.parseInt(getNumClases().getText())));
		}
		process.addParam("layer",rasterLayer);
		process.addParam("rois",getRoisTablePanel().getROIs());
		process.addParam("bandList",bandList);
		process.addParam("filename",getFileSelected());
		process.addParam("view", view);
		process.start();
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.buttonspanel.ButtonsPanelListener#actionButtonPressed(org.gvsig.gui.beans.buttonspanel.ButtonsPanelEvent)
	 */
	public void actionButtonPressed(ButtonsPanelEvent e) {
			// Botón de Aceptar
		if (e.getButton() == ButtonsPanel.BUTTON_ACCEPT) {
			if(!(getRoisTablePanel().getROIs().isEmpty() && getComboMethods().getSelectedIndex()!=3))
				{accept(); close();}
			else
				JOptionPane.showMessageDialog(null,	PluginServices.getText(this,"no_clases"), PluginServices.getText(this,"no_clases"),JOptionPane.WARNING_MESSAGE);
		}

		// Botón de Aplicar
		if (e.getButton() == ButtonsPanel.BUTTON_APPLY) {
			if(!(getRoisTablePanel().getROIs().isEmpty()&& getComboMethods().getSelectedIndex()!=3))
				accept();
			else
				JOptionPane.showMessageDialog(null,	PluginServices.getText(this,"no_clases"), PluginServices.getText(this,"clasificacion"),JOptionPane.WARNING_MESSAGE);
		}

		// Botón de Cerrar
		if (e.getButton() == ButtonsPanel.BUTTON_CANCEL) {
			close();
		}
	 }

	/**
	  *Actualización de las bandas cuando cambia la imagen seleccionada
	  */
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getSource()==getComboCapas()){
			getBandas();
		}
		if(arg0.getSource()==getComboMethods()){
			if(getComboMethods().getSelectedIndex()==2){
				getPanelParametros().setVisible(true);
				getPanelParametros2().setVisible(false);
				//getScrollTable().setEnabled(true);
				setEnabled(getRoisTablePanel(), true);
			}
			else if(getComboMethods().getSelectedIndex()==3){
				getPanelParametros().setVisible(false);
				getPanelParametros2().setVisible(true);
				 //getScrollTable().setEnabled(false);
				setEnabled(getRoisTablePanel(), false);
			}
			else{
				getPanelParametros().setVisible(false);
				getPanelParametros2().setVisible(false);
				//getScrollTable().setEnabled(true);
				setEnabled(getRoisTablePanel(), true);
			}
		updateUI();
		}
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
	 * @return
	 * */
	public JTextField getStevField(){
		if (stevField==null){
			stevField= new JTextField(5);
			stevField.setText("3.0");
		}
		return stevField;
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
	 * @return panel de gestión de ROIs
	 */
	public ROIsTablePanel getRoisTablePanel() {
		if (roisTablePanel==null){
			roisTablePanel= new ROIsTablePanel(null);
		}
		return roisTablePanel;
	}


	/**
	 * @return JComboBox con los métodos de clasificación
	 */
	public JComboBox getComboMethods() {
		if (comboMethods==null){
			comboMethods= new JComboBox();
			String[] sNames = new String[nMethods];
			sNames[0]= new String(PluginServices.getText(this, "metodo_maxima_probabilidad"));
			sNames[1]= new String(PluginServices.getText(this, "metodo_minima_diatancia"));
			sNames[2]= new String(PluginServices.getText(this, "metodo_paralelepipedos"));
			sNames[3]= new String(PluginServices.getText(this, "metodo_noSupervisado"));
			ComboBoxModel jComboBoxLayersModel = new  DefaultComboBoxModel(sNames);
			comboMethods.setModel(jComboBoxLayersModel);
			comboMethods.setEnabled(true);
			comboMethods.addActionListener(this);
		}
		return comboMethods;
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
			try {
				getRoisTablePanel().setFLayer((FLyrRasterSE)layers.getLayer((String)getComboCapas().getSelectedItem()));
			} catch (GridException e) {
				RasterToolsUtil.messageBoxError(PluginServices.getText(this, "grid_error"), this, e);
			}
		}
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
	  * Acciones al cerrar.
	  */
	public void windowClosed() {
		/*
		 * Limpiar los gráficos de la vista:
		 */
		getRoisTablePanel().clearRoiGraphics();
		getRoisTablePanel().getMapControl().rePaintDirtyLayers();
		getRoisTablePanel().setPreviousTool();
	}

	public void focusGained(FocusEvent arg0) {

	}

	public void focusLost(FocusEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void windowActivated() {}

	public void actionValueChanged(PropertyEvent e) {
			updateNewLayerText();
	}

	public JTextField getNumClases() {
		if (numClases==null){
			numClases= new JTextField(5);
			numClases.setText("5");
		}
		return numClases;
	}

	public Object getWindowProfile() {
		return WindowInfo.PROPERTIES_PROFILE;
	}

	private void setEnabled(Container c, boolean enabled) {
        Component[] components = c.getComponents();
        for(int i=0; i<components.length; i++) {
            components[i].setEnabled(enabled);
            if(components[i] instanceof Container) {
            	setEnabled((Container)components[i], enabled);
            }
        }
    }
}













