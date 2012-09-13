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

package org.gvsig.remotesensing.tasseledcap.gui;
import info.clearthought.layout.TableLayout;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
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
import org.gvsig.raster.buffer.WriterBufferServer;
import org.gvsig.raster.dataset.GeoRasterWriter;
import org.gvsig.raster.util.ExtendedFileFilter;
import org.gvsig.raster.util.PropertyEvent;
import org.gvsig.raster.util.PropertyListener;
import org.gvsig.remotesensing.tasseledcap.TasseledCapProcess;

import com.iver.andami.PluginServices;
import com.iver.andami.Utilities;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.IWindowListener;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.iver.cit.gvsig.project.documents.view.gui.View;
import com.iver.utiles.swing.JComboBox;

/**
 * Clase que define la interfaz para el proceso de transformación Tasseled Cap de una imagen.
 * Recoge la imagen a clasificar  y el tipo de transformación.Posibilita configurar las opciones
 * de salida.
 *
 * @author Alejandro Muñoz Sanchez (alejandro.munoz@uclm.es)
 * @author Diego Guerrero Sevilla (diego.guerrero@uclm.es)
 * @version 19/10/2007
 */

public class TasseledCapPanel  extends DefaultButtonsPanel implements PropertyListener, IWindow, IWindowListener,
	ButtonsPanelListener,ActionListener,FocusListener {

	private static final long serialVersionUID = 1L;
	private JPanel centerPanel;
	private JPanel panelTipo;
	private MapContext m_MapContext;
	private int wPanel=422;
	private int hPanel=265;
	private JComboBox comboCapas;
	private JComboBox comboTipos;
	private FLayers layers;
	private JPanel  panelImagen;
	private final int TIPOLANDSAT_ETM= 2;
	private final int TIPOLANDSAT_TM=1;
	private final int TIPOLANDSAT_MS=0;
	private DefaultComboBoxModel  jComboBoxLayersModel;
	private JTextField jTextNombreCapa;
	private JPanel nombreCapa;
	private JRadioButton rButtonFile = null;
	private JRadioButton rButtonMemory = null;
	private View view=null;

	/**
	 * @param vista vista actual
	 */
	public TasseledCapPanel(View vista) {
		super();
	    view= vista;
		m_MapContext = vista.getModel().getMapContext();
		layers = m_MapContext.getLayers();
		this.addButtonPressedListener(this);
		Inicializar();
	}


	/**
	 * @see com.iver.mdiApp.ui.MDIManager.View#getViewInfo()
	 */
	public WindowInfo getWindowInfo() {
		WindowInfo m_viewinfo = new WindowInfo(WindowInfo.PALETTE);
		m_viewinfo.setWidth(wPanel);
		m_viewinfo.setHeight(hPanel);
		m_viewinfo.setTitle(PluginServices.getText(this,"tasseled_cap"));
		m_viewinfo.setX(300);
		return m_viewinfo;
	}


	/**
	 * inicialización del cuadro de diálogo
	 */
	private void Inicializar(){
		BorderLayout bd=new BorderLayout();
		this.setLayout(bd);
		TitledBorder topBorder = BorderFactory.createTitledBorder((PluginServices.getText(this,"")));
		topBorder.setTitlePosition(TitledBorder.TOP);
		this.setBorder(new CompoundBorder(topBorder,new EmptyBorder(5,5,5,5)));
		this.add(getCenterPanel(), BorderLayout.CENTER);
		actualizarCombo(getComboTipos().getSelectedIndex());
		updateUI();
	 }


	/*
	 * (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource()==getComboTipos()){
			actualizarCombo(getComboTipos().getSelectedIndex());
		}
	}

	 /**
	 * @return panel con los combos para le seleccion de transformacion e imagen
	 */
	public JPanel getCenterPanel(){
		 if (centerPanel==null){
			 centerPanel=new JPanel();
			 centerPanel.setLayout(new GridBagLayout());
			 TitledBorder topBorder = BorderFactory.createTitledBorder(PluginServices.getText(this,"salida_raster"));;
			 topBorder.setTitlePosition(TitledBorder.TOP);
			 centerPanel.setBorder(topBorder);
			 GridBagConstraints c = new GridBagConstraints();
			 c.fill = GridBagConstraints.BOTH;
			 c.insets=new Insets(8,8,0,8);
			 c.weightx = 1;
			 c.gridx = 0;
			 c.gridy = 0;
			 centerPanel.add(getNombreCapa(), c);
			 c.insets=new Insets(8,8,8,8);
			 c.weightx = 1.0;
			 c.gridx = 0;
			 c.gridy = 1;
 //			 Agregar panel que contiene los paneles con los combos
			 TableLayout thisLayout2 = new TableLayout(new double[][] {
					{50 ,TableLayout.PREFERRED,50},
					{TableLayout.PREFERRED, TableLayout.PREFERRED}});

			 JPanel p2=new JPanel();
			 p2.setLayout(thisLayout2);
			 TitledBorder topBorder2 = BorderFactory.createTitledBorder(PluginServices.getText(this,"imagen_transformar"));;
			 topBorder2.setTitlePosition(TitledBorder.TOP);
			 p2.setBorder(new CompoundBorder(topBorder2,new EmptyBorder(5,5,5,5)));
			 p2.add(getPanelTipo(),"1,0");
			 p2.add(getPanelImagen(),"1,1");
			 centerPanel.add(p2, c);
		 }
		 return centerPanel;
	 }


	 /**
	 * @return panel con el combo de imagenes
	 */
	public JPanel getPanelImagen() {
			if (panelImagen==null){
				panelImagen=new JPanel();

				    // Nuevo panel al que se aaden los elementos
					JPanel p=new JPanel();
					TableLayout thisLayout = new TableLayout(new double[][] {
						{ 5, 50,150},
						{TableLayout.PREFERRED}});
						//Establece la separacion entre los elementos
						thisLayout.setHGap(5);
						thisLayout.setVGap(5);
						p.setLayout(thisLayout);
						p.add(getComboCapas(),"2,0");
				        p.add(new JLabel((PluginServices.getText(this,"imagen"))),"1,0");

				panelImagen.add(p);
			}
			return panelImagen;
		}


	/**
	 * @return panel con el combo de tipos
	 */
	 public JPanel getPanelTipo() {
		 if (panelTipo==null){
			 panelTipo=new JPanel();
			 JPanel p=new JPanel();
			 TableLayout thisLayout = new TableLayout(new double[][] {
				{ 5.0, 50,150},
				{TableLayout.PREFERRED}});
			thisLayout.setHGap(5);
			thisLayout.setVGap(5);
			p.setLayout(thisLayout);
			p.add(getComboTipos(),"2,0");
			p.add(new JLabel((PluginServices.getText(this,"tipo"))),"1,0");
			panelTipo.add(p);
		 }
		 return panelTipo;
		}


	 /**
	  * @return modelo de Combo
	  */
	 public DefaultComboBoxModel getComboModel(){
		 if(jComboBoxLayersModel==null){
			 jComboBoxLayersModel=new DefaultComboBoxModel(getLayerNames(TIPOLANDSAT_MS));
		 }
		 return jComboBoxLayersModel;
	 }


	 /**
	 * @return panel que incluye el nombre de la capa y las opciones de almacenamieto de la capa de salida
	 */
	 public JPanel getNombreCapa() {
		 if (nombreCapa==null){

			nombreCapa=new JPanel();
			nombreCapa.setPreferredSize(new Dimension(300,200));
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
			nombreCapa.add(new JLabel( (PluginServices.getText(this,"nombre_capa")),SwingConstants.RIGHT ),gridBagConstraints);

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
	  * @return javax.swing.JTextField
	  */
	 public JTextField getJTextNombreCapa() {
		 if (jTextNombreCapa==null){
			 jTextNombreCapa=new JTextField(10);
			 jTextNombreCapa.setText(RasterLibrary.getOnlyLayerName());
			 jTextNombreCapa.addFocusListener(this);
			 RasterLibrary.addOnlyLayerNameListener(this);
		 }

		 return jTextNombreCapa;
	 }



	 public void updateNewLayerText() {
		 getJTextNombreCapa().setText(RasterLibrary.getOnlyLayerName());
 	}


	 /**
	 * Cuando alguien ha cambiado la propiedad del nombre de la
	 * capa se actualiza autom?ticamente
	 */

	 public void actionValueChanged(PropertyEvent e) {
		 updateNewLayerText();
	 }


	/**
	* @return combo con el nombre de las imágenes cargadas
	*/
	public JComboBox getComboCapas() {
		 if (comboCapas==null){
			 comboCapas = new JComboBox();
			 comboCapas.setModel(getComboModel());
			 comboCapas.setEnabled(true);
			 comboCapas.addActionListener(this);
			}
		 return comboCapas;
	 }


	/**
	 * actualiza el combo de imágenes con las que se ajustan al tipo pasado como parametro
	 */
	 private void actualizarCombo(int tipo){
		 getComboModel().removeAllElements();
		 String []a = getLayerNames(tipo);
		 for (int i=0;i<a.length;i++)
			 getComboModel().addElement(new String(a[i]));

		 if(getComboCapas().getSelectedIndex()==-1){
			 	getButtonsPanel().getButton(1).setEnabled(false);
		 		getButtonsPanel().getButton(3).setEnabled(false);
		 		}
		 else{
			 	getButtonsPanel().getButton(1).setEnabled(true);
			 	getButtonsPanel().getButton(3).setEnabled(true);
		 	 }
		 updateUI();
	 }

	 /**
	 * @param tipo de transformacion
	 * @return nombres de las capas que se ajustan al tipo de entrada
	 */
	 private String[] getLayerNames(int tipo) {

		 	getComboCapas().removeAllItems();
		    int i,j=0;
		    ArrayList sNames= new ArrayList();

		    for (i = 0; i < layers.getLayersCount(); i++) {
		    	if (layers.getLayer(i)instanceof FLyrRasterSE){
		    		FLyrRasterSE rasterLayer = (FLyrRasterSE)layers.getLayer(i);
		    		int bandCount = rasterLayer.getBandCount();

		    		if(tipo==TIPOLANDSAT_MS){
		    			if(bandCount==4){
		    				sNames.add(new String((layers.getLayer(i)).getName()));
		    				j++;
		    			}
		    		}

		    		if (tipo==TIPOLANDSAT_ETM){
		    			if(bandCount==6 || bandCount==7){
		    				sNames.add(new String((layers.getLayer(i)).getName()));
		    				j++;
		    			}
		    		}

		    		if(tipo==TIPOLANDSAT_TM){
		    			if(bandCount==6 || bandCount==7 ){
		    				sNames.add(new String((layers.getLayer(i)).getName()));
		    				j++;
		    			}
		    		}
		    	}
		    }

		    String[] sCapas = new String[sNames.size()];
		    for(int k=0; k<sNames.size();k++)
		    	sCapas[k]=(String)sNames.get(k);

		    return sCapas;
		}


	/**
	* @return JComboBox con tipos
	*/
	public JComboBox getComboTipos() {

		if(comboTipos==null){
			String a[]= {"LandSat MS","LandSat TM","LandaSat ETM+"};
			ComboBoxModel jComboBoxLayersModel = new  DefaultComboBoxModel(a);
			comboTipos = new JComboBox();
			comboTipos.setModel(jComboBoxLayersModel);
			comboTipos.setEnabled(true);
			comboTipos.addActionListener(this);
		}
		return comboTipos;
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

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.buttonspanel.ButtonsPanelListener#actionButtonPressed(org.gvsig.gui.beans.buttonspanel.ButtonsPanelEvent)
	 */
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


	/** Se construye el proceso y se lanza a ejecución*/
	public void aplicar(){
		FLyrRasterSE rasterLayer= (FLyrRasterSE)layers.getLayer((String)getComboCapas().getSelectedItem());
		String filename= getFileSelected();
		if(filename!=null){
			TasseledCapProcess process = new TasseledCapProcess();
			process.addParam("filename",filename);
			process.addParam("datawriter",new WriterBufferServer());
			process.addParam("viewName", view.getName());
			process.addParam("type",new Integer(getComboTipos().getSelectedIndex()));
			process.addParam("layer",rasterLayer);
			process.start();
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


	/**
	 * Devuelve la ruta del fichero donde se va a guardar, en caso de guardarse
	 * en memoria, calcula el nombre sin preguntar y devuelve la ruta.
	 * @return string con la ruta de salida
	 */
	public String getFileSelected() {
		String path = "";
		if (getRadioFile().isSelected()) {
			JFileChooser chooser = new JFileChooser("TASSELED_CAP_PANEL",JFileChooser.getLastPath("TASSELED_CAP_PANEL", null));
			chooser.setDialogTitle(PluginServices.getText(this, "seleccionar_fichero"));
			//Añadimos las extensiones que hayan sido registradas en el driver
			String[] extList = GeoRasterWriter.getDriversExtensions();
			for(int i=0;i<extList.length;i++)
				chooser.addChoosableFileFilter(new ExtendedFileFilter(extList[i]));

			if (chooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION)
				return null;
			JFileChooser.setLastPath("TASSELED_CAP_PANEL", chooser.getSelectedFile());
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


	public void focusGained(FocusEvent arg0) {}

	public void focusLost(FocusEvent arg0) {}



	public void windowActivated() {
		// TODO Auto-generated method stub

	}


	public void windowClosed() {
		// TODO Auto-generated method stub

	}


	public Object getWindowProfile() {
		return null;
//		return WindowInfo.PROPERTIES_PROFILE;
	}


}


