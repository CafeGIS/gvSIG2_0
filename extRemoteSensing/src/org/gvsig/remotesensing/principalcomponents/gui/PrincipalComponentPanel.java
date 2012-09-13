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

package org.gvsig.remotesensing.principalcomponents.gui;

import info.clearthought.layout.TableLayout;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
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
import org.gvsig.raster.IProcessActions;
import org.gvsig.raster.RasterLibrary;
import org.gvsig.remotesensing.principalcomponents.PCStatistics;
import org.gvsig.remotesensing.principalcomponents.PCStatisticsProcess;

import com.iver.andami.PluginServices;
import com.iver.andami.Utilities;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.iver.cit.gvsig.project.documents.view.gui.View;
import com.iver.utiles.swing.JComboBox;

/**
 * Clase que define la interfaz para  la selección de parámetros para el análisis de
 * componentes principales. Para la imagen seleccionada se toman las bandas marcadas para el
 * cálculo de estadísticas como matriz de varianza-covarianza, autovalores y autovectores.
 *
 * @author Alejandro Muñoz Sanchez (alejandro.munoz@uclm.es)
 * @author Diego Guerrero Sevilla (diego.guerrero@uclm.es)
 * @version 22/10/2007
 */

public class PrincipalComponentPanel   extends DefaultButtonsPanel implements IWindow,ActionListener,FocusListener,ButtonsPanelListener, IProcessActions {

	private static final long serialVersionUID = 1L;
	private JPanel southPanel;
	private JPanel centerPanel;
	private BandTableFormat mModeloTabla;
	private JPanel panelTodos;
	private JButton todos;
	private JButton ninguno;
	private int wPanel=325;
	private int hPanel=350;
	private JComboBox comboCapas;
	private JPanel  panelImagen;
	private JTable jTableBandas;
	private JScrollPane scrollbandas;

	private MapContext m_MapContext;
	private FLayers layers;
	private View view;
	private String fileName = null;
	private PCStatisticsProcess statisticsProcess = null;
	private boolean selectecBands[] = null;


	/**
	 * Constructor
	 * @param vista vista actual de la aplicacion.
	 */
	public PrincipalComponentPanel(View vista) {
		super(ButtonsPanel.BUTTONS_ACCEPTCANCEL   );
	    view = vista;
		m_MapContext = vista.getModel().getMapContext();
		layers = m_MapContext.getLayers();
		Inicializar();
	}


	/**
	 * @see com.iver.mdiApp.ui.MDIManager.View#getViewInfo()
	 */
	public WindowInfo getWindowInfo() {
		WindowInfo m_viewinfo = new WindowInfo(WindowInfo.PALETTE | WindowInfo.RESIZABLE);
		//Establecer el tamao del formulario
		m_viewinfo.setWidth(wPanel);
		m_viewinfo.setHeight(hPanel);
		//Establecer el ttulo del formulario
		m_viewinfo.setTitle(PluginServices.getText(this,"principal_components"));
		//punto X de la pantalla donde se situa
		m_viewinfo.setX(300);
		return m_viewinfo;
	}

	/**
	 * Inicialización del cuadro de dialogo.
	 */
	 private void Inicializar(){

		BorderLayout bd=new BorderLayout();
		this.setLayout(bd);
		TitledBorder topBorder = BorderFactory.createTitledBorder((PluginServices.getText(this,"")));
		topBorder.setTitlePosition(TitledBorder.TOP);
		this.setBorder(new CompoundBorder(topBorder,new EmptyBorder(10,10,10,10)));
		this.add(getCenterPanel(), BorderLayout.CENTER);
		this.add(getSouthPanel(),BorderLayout.SOUTH);
		getBandas();
		this.addButtonPressedListener(this);

		}


	 /**
	  * @return panel con los botones aceptar y cancelar
	  */
	 public JPanel getSouthPanel() {
		if (southPanel==null){
			southPanel=new JPanel();
			FlowLayout f= new FlowLayout();
			southPanel.setLayout(f);
		}
        return southPanel;
	 }


	/**
	* @return panel con  panel imagen, tabla de bandas y botones de selección
	*/
	public JPanel getCenterPanel() {

		if (centerPanel==null){
			centerPanel=new JPanel();
			TitledBorder topBorder = BorderFactory.createTitledBorder((PluginServices.getText(this,"seleccion_banda")));
			topBorder.setTitlePosition(TitledBorder.TOP);

			BorderLayout bd=new BorderLayout();
			JPanel p=new JPanel();
			p.setPreferredSize(new Dimension(275,200));
			p.setLayout(bd);
			p.setBorder(new CompoundBorder(topBorder,new EmptyBorder(5,5,5,5)));
			p.add(getScrollVariables(),BorderLayout.CENTER);
			p.add(getPanelTodos(),BorderLayout.SOUTH);
			centerPanel.add(getPanelImagen());
			centerPanel.add(p);

		}
		return centerPanel;
	}

	 /**
	 * @return panel con el combo de selección de imagen.
	 */
	public JPanel getPanelImagen() {

		if (panelImagen==null){
			panelImagen=new JPanel();
			TitledBorder topBorder = BorderFactory.createTitledBorder((PluginServices.getText(this,"imagen")));
			topBorder.setTitlePosition(TitledBorder.TOP);
			panelImagen.setBorder(new CompoundBorder(topBorder,new EmptyBorder(2,2,2,2)));
			JPanel p= new JPanel();

			TableLayout thisLayout = new TableLayout(new double[][] {
					{ 250},
					{TableLayout.PREFERRED}});

			p.setLayout(thisLayout);
			p.setPreferredSize(new Dimension(250,30));
			p.add(getComboCapas(),"0,0");
			panelImagen.add(p);
		}
		return panelImagen;
	}


	 /**
	 * @return panel con  botones de seleccion todos/ninguno
	 */
	 public JPanel getPanelTodos(){

		 if (panelTodos==null){
			 panelTodos= new JPanel();
			 JPanel p = new JPanel();
			 p.setLayout(new FlowLayout(FlowLayout.RIGHT,1,1));
			 p.add(getTodos());
			 p.add(getNinguno());
			 p.setPreferredSize(new Dimension(240,30));
			 panelTodos.add(p);
		 }
		 return panelTodos;

		}


	 /**
	  * @return combo con el nombre de las imágenes cargadas
	  */
	public JComboBox getComboCapas() {

		if (comboCapas==null){
			ComboBoxModel jComboBoxLayersModel = new
	        DefaultComboBoxModel(getLayerNames());
			comboCapas = new JComboBox();
			comboCapas.setModel(jComboBoxLayersModel);
			comboCapas.setEnabled(true);
			comboCapas.addActionListener(this);
			}
		return comboCapas;
	}





	 /**
	 * @return scroll con tabla de bandas de imagen seleccionada
	 */
	public JScrollPane getScrollVariables() {

		if (scrollbandas==null){
			scrollbandas=new JScrollPane();
		}
		getTableBands().getColumn("").setPreferredWidth(30);
		scrollbandas.setViewportView(getTableBands());
		scrollbandas.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		return scrollbandas;
	}


	 /**
	 * @return array de String con los nombre de las capas cargadas
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
	  * @return JTable con bandas de la imagen
	  */
	 private JTable getTableBands(){
		 if (jTableBandas==null){
			 jTableBandas=new JTable(getModeloTabla());
		 }
		 return jTableBandas;
	 }


	 /**
	 * @see BandTableFormat
	 * @return modelo de tabla BandTableFormat
	 */
	public BandTableFormat getModeloTabla() {
		 if(mModeloTabla==null){
			 mModeloTabla=new BandTableFormat();
		 }
		 return mModeloTabla;
	}


	/**
	 * Toma las Bandas de la imagen seleccionada en el combo Imagen
	 */
	public void getBandas(){

	     // Antes de insertar se borra lo anterior
		 getModeloTabla().LimpiarLista();
		 FLyrRasterSE rasterLayer= (FLyrRasterSE)layers.getLayer((String)getComboCapas().getSelectedItem());
		 String bandas[]=new String[rasterLayer.getBandCount()];
		 if (layers.getLayer((String)getComboCapas().getSelectedItem()) instanceof  FLyrRasterSE ){
				for (int j=0; j<rasterLayer.getBandCount(); j++){
					String s=(PluginServices.getText(this,"banda"))+(j+1);
					bandas[j]=s;
				}
		 }
		// Insertar las bandas el la tabla
		for (int i=0;i<bandas.length;i++)
			getModeloTabla().addRow(bandas[i],true);
		getTableBands().updateUI();

	 }


	public void focusGained(FocusEvent arg0) {
			// TODO Auto-generated method stub
	}


	public void focusLost(FocusEvent arg0) {
			// TODO Auto-generated method stub
	}


	/**
	 * @return JButton seleccion ninguna banda
	 */
	public JButton getNinguno() {
		if (ninguno==null){
				ImageIcon icono = new ImageIcon(PrincipalComponentCalculusPanel.class.getClassLoader().getResource("images/table_delete.png"));
				ninguno = new JButton(icono);
				ninguno.addActionListener(this);
			}
		return ninguno;
	}


	/**
	 * @return JButton seleccion todas las bandas
	 */
	public JButton getTodos() {
		if (todos==null){
			ImageIcon icono = new ImageIcon(PrincipalComponentCalculusPanel.class.getClassLoader().getResource("images/table.png"));
			todos = new JButton(icono);
			todos.addActionListener(this);
		}
		return todos;
	}



	public void accept(){

		if (getModeloTabla().getNumSelected()>0){
			FLyrRasterSE rasterLayer= (FLyrRasterSE)layers.getLayer((String)getComboCapas().getSelectedItem());
			PluginServices.getMDIManager().closeWindow(this);
			statisticsProcess = new PCStatisticsProcess();
			statisticsProcess.addParam("inputRasterLayer",rasterLayer);
			selectecBands = getModeloTabla().getSeleccionadas();
			statisticsProcess.addParam("selectedBands",selectecBands);

			statisticsProcess.setActions(this);
			statisticsProcess.start();
		}
		else{

			JOptionPane.showMessageDialog(null,
			PluginServices.getText(this,"no_bandas"), PluginServices.getText(this,"principal_components"),
			JOptionPane.WARNING_MESSAGE);
		}

	}

	/**
	 * acciones al cerrar
	 */
	public void close(){
		try {
			PluginServices.getMDIManager().closeWindow(this);
		} catch (ArrayIndexOutOfBoundsException ex) {
		// Si la ventana no se puede eliminar no hacemos nada
		}
	}


	/*
	 * (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {


			// Check Seleccionar Todo
			if(e.getSource()==getTodos()){
					getModeloTabla().seleccionarTodas();
					updateUI();
			}
			if(e.getSource()==getNinguno()){

					getModeloTabla().seleccionarNinguna();
					updateUI();
			}

			if (e.getSource()==getComboCapas()){
				// Actualizar tabla de bandas
				getBandas();
			}
	}


	public void actionButtonPressed(ButtonsPanelEvent e) {
//		 Botón de Aceptar
		if (e.getButton() == ButtonsPanel.BUTTON_ACCEPT) {
			accept();
			close();
		}

		// Botón de Aplicar
		if (e.getButton() == ButtonsPanel.BUTTON_APPLY) {
			accept();
		}

		// Botón de Cerrar
		if (e.getButton() == ButtonsPanel.BUTTON_CANCEL) {
			close();
		}
	}


	public void end(Object param) {
		fileName = Utilities.createTempDirectory() + File.separator;
		fileName = fileName + RasterLibrary.getOnlyLayerName() + ".tif";
		RasterLibrary.usesOnlyLayerName();

		PrincipalComponentCalculusPanel altPrincipalComponentCalculusPanel = new PrincipalComponentCalculusPanel(view,(PCStatistics)param,statisticsProcess,selectecBands);
		altPrincipalComponentCalculusPanel.setFilename(fileName);
		PluginServices.getMDIManager().addWindow(altPrincipalComponentCalculusPanel);

	}


	public void interrupted() {
		// TODO Auto-generated method stub

	}


	public Object getWindowProfile() {
		return WindowInfo.PROPERTIES_PROFILE;
	}

}

