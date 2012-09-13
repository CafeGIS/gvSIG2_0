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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.gui.beans.buttonspanel.ButtonsPanel;
import org.gvsig.gui.beans.buttonspanel.ButtonsPanelEvent;
import org.gvsig.gui.beans.buttonspanel.ButtonsPanelListener;
import org.gvsig.gui.beans.defaultbuttonspanel.DefaultButtonsPanel;
import org.gvsig.gui.beans.swing.JFileChooser;
import org.gvsig.raster.IProcessActions;
import org.gvsig.raster.RasterLibrary;
import org.gvsig.raster.dataset.GeoRasterWriter;
import org.gvsig.raster.grid.Grid;
import org.gvsig.raster.grid.GridException;
import org.gvsig.raster.util.ExtendedFileFilter;
import org.gvsig.raster.util.PropertyEvent;
import org.gvsig.raster.util.PropertyListener;
import org.gvsig.remotesensing.principalcomponents.PCImageProcess;
import org.gvsig.remotesensing.principalcomponents.PCStatistics;
import org.gvsig.remotesensing.principalcomponents.PCStatisticsProcess;

import Jama.Matrix;

import com.iver.andami.PluginServices;
import com.iver.andami.Utilities;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.IWindowListener;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.iver.cit.gvsig.project.documents.view.gui.View;


/**
 * Clase que define la interfaz para la selección de componentes que interviene la construcción de la
 * imagen resultante del analisis de componentes principales.
 *
 * @see PCStatisticsProcess
 * @author Alejandro Muñoz Sanchez (alejandro.munoz@uclm.es)
 * @author Diego Guerrero Sevilla (diego.guerrero@uclm.es)
 * @version 22/10/2007
 */

public class PrincipalComponentCalculusPanel extends DefaultButtonsPanel implements PropertyListener, IWindow, IWindowListener,
ButtonsPanelListener,ActionListener,FocusListener, IProcessActions {

	private static final long serialVersionUID = 1L;

	private JPanel 					statisticsPanel;
	private JPanel 					panelTodos;
	private JPanel 					nombreCapaPanel;
	private JButton 				jbExportar;
	private JButton 				jexportVarMatrix;
	private JButton 				jexportAutovectorMatrix;
	private ComponentTableFormat 	tableFormatComponent;
	private JScrollPane 			scrollComponents;
	private JTable 					tableComponent;
	private JTextField 				jTextNombreCapa;
	private JButton 				jbTodos;
	private JButton 				jbNinguno;
	private int 					wPanel=420;
	private int						hPanel=410;
	private JPanel 					panelSuperior;
	private JRadioButton 			rButtonFile 				= null;
	private JRadioButton 			rButtonMemory 				= null;
	private JCheckBox 				bandasStatistics;
	private JCheckBox 				varianceStatistics;
	private JCheckBox 				autovectorStatistics;

	private PCStatisticsProcess 	pc							= null;
	private FLyrRasterSE 			resultLayer 				= null;
	private View 					view 						= null;
	private String 					filename 					= null;
	private boolean 				selectedBands[] 			= null;
	private PCStatistics			pcStatistics				= null;


	/**
	 * Constructor
	 * @param vista vista de la aplicacion
	 * @param statistics Estadísticas necesarias para crear el raster transformado.
	 * @param pC proceso de estadisticas
	 * @param selectecBands bandas de raster de entrada que intervienen en la transformación
	 */
	public PrincipalComponentCalculusPanel(View vista,PCStatistics pcStatistics, PCStatisticsProcess pC, boolean[] selectecBands){
		pc=pC;
		view= vista;
		this.selectedBands = selectecBands;
		this.pcStatistics = pcStatistics;
		this.addButtonPressedListener(this);
		Inicializar();
	}

	/**
	 * @see com.iver.mdiApp.ui.MDIManager.View#getViewInfo()
	 */
	public WindowInfo getWindowInfo() {
		WindowInfo m_viewinfo = new WindowInfo(WindowInfo.MODALDIALOG);
		//Establecer el tamaño del formulario
		m_viewinfo.setWidth(wPanel);
		m_viewinfo.setHeight(hPanel);
		//Establecer el título del formulario
		m_viewinfo.setTitle(PluginServices.getText(this,"principal_components_calculus"));
		//punto X de la pantalla donde se situa
		m_viewinfo.setX(300);
		return m_viewinfo;
	}


	/**
	 * @return panel con las opciones de exportar estadisticas
	 */
	 public JPanel getStatisticsPanel(){

		 if(statisticsPanel==null){
			 statisticsPanel=new JPanel();
			 TableLayout thisLayout = new TableLayout(new double[][] {
						{20,245,100},
						{TableLayout.PREFERRED}});

			 statisticsPanel.setLayout(thisLayout);
			 JPanel p= new JPanel();
			 TitledBorder topBorder = BorderFactory.createTitledBorder((PluginServices.getText(this,"estadisticas")));
			 topBorder.setTitlePosition(TitledBorder.TOP);
			 statisticsPanel.setBorder(new CompoundBorder(topBorder,new EmptyBorder(5,5,5,5)));
			 BorderLayout bd=new BorderLayout();
			 p.setPreferredSize(new Dimension(160,70));
			 bd.setVgap(5);
			 bd.setHgap(5);
			 p.setLayout(bd);
			 p.add(getBandasStatistics(),BorderLayout.NORTH);
			 p.add(getVarianceStatistics(),BorderLayout.CENTER);
			 p.add(getAutovectorStatistics(),BorderLayout.SOUTH);
			 TableLayout thisLayout2 = new TableLayout(new double[][] {
						{5 ,65},
						{40,TableLayout.PREFERRED}});
			 JPanel panelButton= new JPanel();
			 panelButton.setLayout(thisLayout2);
			 panelButton.add(getExportar(),"1,1");
			 statisticsPanel.add(p,"1,0");
			 statisticsPanel.add(panelButton,"2,0");
		 }
		 return statisticsPanel;
	 }


	 /**
	  * inicialización del diálogo
	  */
	 private void Inicializar(){
			BorderLayout bd=new BorderLayout();
			this.setLayout(bd);
			TitledBorder topBorder = BorderFactory.createTitledBorder(" ");
			topBorder.setTitlePosition(TitledBorder.TOP);
			this.setBorder(new CompoundBorder(topBorder,new EmptyBorder(5,10,10,10)));
			this.add(getPanelSuperior(),BorderLayout.NORTH);
		    this.add(getStatisticsPanel(),BorderLayout.CENTER);
			completarTabla();
	 }


	 /**
	  * @return panel con opciones de salida, tabla de componentes y botones de selección todos/ninguno
	  */
	 private JPanel getPanelSuperior(){
		 if (panelSuperior==null){
			 panelSuperior = new JPanel();
			 TitledBorder topBorder = BorderFactory.createTitledBorder((PluginServices.getText(this,"salida_raster")));
			 topBorder.setTitlePosition(TitledBorder.TOP);
			 panelSuperior.setBorder(new CompoundBorder(topBorder,new EmptyBorder(5,5,5,5)));
			 JPanel p=new JPanel();
			 TableLayout thisLayout = new TableLayout(new double[][] {
					 {350},
					{5,135,40}});
				thisLayout.setHGap(5);
				thisLayout.setVGap(3);
				p.setLayout(thisLayout);
			//	p.add(getNombreCapaPanel(),"0,0");
				p.add(getScrollComponents(),"0,1");
				p.add(getPanelTodos(),"0,2");
			    panelSuperior.add(p);
		 }
		 return panelSuperior;
	 }


	 /**
	 * Añade las filas a la tabla de selección de componentes
	 */
	public void completarTabla(){
		double acumulado=0;
		for (int i=0;i<pcStatistics.getAutovalues().length;i++)
			acumulado+=pcStatistics.getAutovalues()[i];
		int autova[]=new int[ pcStatistics.getAutovalues().length];
		int cont= pcStatistics.getAutovalues().length-1;
		for (int i=0; i<pcStatistics.getAutovalues().length;i++){
			autova[i]=cont;
			cont--;
		}

		for (int i=pcStatistics.getAutovalues().length-1;i>=0;i--)
			getTableFormatComponent().addRow(autova[i],pcStatistics.getAutovalues()[i],pcStatistics.getAutovalues()[i]/acumulado,true);
	 }


	/**
	 * @return JButton jbExportar
	 */
	public JButton getExportar() {
		if (jbExportar==null){
			jbExportar=new JButton((PluginServices.getText(this,"exportar")));
			jbExportar.addActionListener(this);
		}
		return jbExportar;
	}


	/**
	 * @return JButton jbNinguno
	 */
	public JButton getNinguno() {
		if (jbNinguno==null){
			ImageIcon icono = new ImageIcon(PrincipalComponentCalculusPanel.class.getClassLoader().getResource("images/table_delete.png"));
			jbNinguno = new JButton(icono);
			jbNinguno.addActionListener(this);
		}
		return jbNinguno;
	}


	/**
	 * @return JButton jbTodos
	 */
	public JButton getTodos() {
		if (jbTodos==null){
			ImageIcon icono = new ImageIcon(PrincipalComponentCalculusPanel.class.getClassLoader().getResource("images/table.png"));
			jbTodos = new JButton(icono);
			jbTodos.addActionListener(this);
		}
		return jbTodos;
	}


	/**
	 * @return JButton jexportVarMatrix
	 */
	public JButton getJexportVarMatrix() {
		if(jexportVarMatrix==null){
			jexportVarMatrix=new JButton((PluginServices.getText(this,"var_matrix")));
			jexportVarMatrix.addActionListener(this);
		}
		return jexportVarMatrix;
	}


	/**
	 * @return JButton jexportAutovectorMatrix
	 */
	public JButton getJexportAutovectorMatrix() {
		if (jexportAutovectorMatrix==null){
			jexportAutovectorMatrix=new JButton((PluginServices.getText(this,"autovector_matrix")));
			jexportAutovectorMatrix.addActionListener(this);
		}
		return jexportAutovectorMatrix;
	}


	/**
	 * @see ComponentTableFormat
	 * @return modelo de tabla de componentes
	 */
	public ComponentTableFormat getTableFormatComponent() {
		if(tableFormatComponent==null){
			tableFormatComponent=new ComponentTableFormat();
		}
		return tableFormatComponent;
	}


	/**
	 * @return tabla de componentes
	 */
	public JTable getTableComponents(){
		if (tableComponent==null){
			tableComponent=new JTable(getTableFormatComponent());
		}
		return tableComponent;
	}


	/**
	 * @return scroll con la tabla de componentes
	 */
	public JScrollPane getScrollComponents() {

		if(scrollComponents==null){
			scrollComponents=new JScrollPane();
		}
		 getTableComponents().getColumn((PluginServices.getText(this,"componente"))).setPreferredWidth(110);
		 getTableComponents().getColumn((PluginServices.getText(this,"autovalor"))).setPreferredWidth(80);
		 getTableComponents().getColumn((PluginServices.getText(this,"%"))).setPreferredWidth(80);

		 scrollComponents.setViewportView(getTableComponents());
		 scrollComponents.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		return scrollComponents;
	}


	/**
	 * @return panel con los botones de selección todo/ninguno
	 */
	public JPanel getPanelTodos(){
		if (panelTodos==null){
			panelTodos= new JPanel();
			JPanel p = new JPanel();
			p.setLayout(new FlowLayout(FlowLayout.RIGHT,1,1));
			p.add(getTodos());
			p.add(getNinguno());
			p.setPreferredSize(new Dimension(320,30));
			panelTodos.add(p);
		 }
		 return panelTodos;
	}


	/**
	* @return panel que incluye el nombre de la capa y las opciones de almacenamieto de la capa de salida
	*/
	public JPanel getNombreCapaPanel() {

		if (nombreCapaPanel==null){
			nombreCapaPanel=new JPanel();
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
			nombreCapaPanel.setLayout(new GridBagLayout());

			gridBagConstraints = new java.awt.GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.anchor = GridBagConstraints.WEST;
			gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
			nombreCapaPanel.add(new JLabel( (PluginServices.getText(this,"nombre_capa")),SwingConstants.RIGHT ),gridBagConstraints);

			gridBagConstraints = new java.awt.GridBagConstraints();
			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.anchor = GridBagConstraints.WEST;
			gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
			nombreCapaPanel.add(getJTextNombreCapa(),gridBagConstraints);

			gridBagConstraints = new java.awt.GridBagConstraints();
			gridBagConstraints.gridx = 2;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.anchor = GridBagConstraints.WEST;
			gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
			nombreCapaPanel.add(radioPanel,gridBagConstraints);
		}
		return nombreCapaPanel;
	}

	/**
	 * @return javax.swing.JTextField
	 */
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


	/**
	 * Especificar el nombre de la nueva capa para el recuadro de texto asignándo
	 * en cada llamada un nombre consecutivo.
	 */
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
	 * Método para exportar las estadisticas seleccionadas a un fichero de texto
	 */
	public void exportStatistics(){

		JFileChooser dlgExportar = new JFileChooser("PRINCIPAL_COMPONENT_CALCULUS_PANEL_STATISTICS", JFileChooser.getLastPath("PRINCIPAL_COMPONENT_CALCULUS_PANEL_STATISTICS", null));
		dlgExportar.setDialogTitle(PluginServices.getText(this, "seleccionar_fichero"));
		String[] extList = new String[]{"txt"};
		for(int i=0;i<extList.length;i++)
			dlgExportar.addChoosableFileFilter(new ExtendedFileFilter(extList[i]));
		int op= dlgExportar.showSaveDialog(this);
		    // Se comprueban las estadisticas a exportar
			if (op == JFileChooser.APPROVE_OPTION){
				File outFile = new File(dlgExportar.getSelectedFile().getAbsolutePath());
				JFileChooser.setLastPath("PRINCIPAL_COMPONENT_CALCULUS_PANEL_STATISTICS", outFile);
				OutputStream outputStream=null;
				try {
					 outputStream = new FileOutputStream(outFile);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}

				if(getBandasStatistics().isSelected()){
					ExportGridStatistic(pc.getInputGrid(), outputStream);
				}
				if(getVarianceStatistics().isSelected()){
					ExportarMatriz(pcStatistics.getCoVarMatrix(),true,outputStream);
				}

				if (getAutovectorStatistics().isSelected()){
					ExportarMatriz(pcStatistics.getAutoVectorsMatrix(),true,outputStream);
				}

				try {
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}


	/**
	 * Método para exportar a disco las estadisticas del grid que se pasa como argumento.
	 * @param grid	grid con las estadisticas a exportar
	 * @param descriptor de  fichero salida
	 */
	private void ExportGridStatistic(Grid grid,OutputStream outputStream){

		try {
			outputStream.write("\t\t".getBytes());
			outputStream.write("Max Value\t".getBytes());
			outputStream.write("Min Value\t".getBytes());
			outputStream.write("Mean Value\t".getBytes());
			outputStream.write("\n".getBytes());
			String aux="";

			for(int i=0; i<grid.getBandCount();i++)
				{
					grid.setBandToOperate(i);
					outputStream.write("\n".getBytes());
					outputStream.write(("Band"+(i+1)).getBytes());
					// Se escribe los valores con un numero de caracteres inferior a 8.
					outputStream.write(("\t\t").getBytes());
					Double var= new Double(grid.getMaxValue());
					if (var.toString().getBytes().length>7)
						outputStream.write(var.toString().getBytes(),0,7);// Elementos de la matriz;
					else{
						aux = var.toString();
						for (int k=var.toString().getBytes().length; k<8; k++)
							aux=aux+" ";
						outputStream.write(aux.getBytes(),0,7);
					}
					outputStream.write(("\t").getBytes());
					var= new Double(grid.getMinValue());
					if (var.toString().getBytes().length>7)
						outputStream.write(var.toString().getBytes(),0,7);// Elementos de la matriz;
					else{
						aux = var.toString();
						for (int k=var.toString().getBytes().length; k<8; k++)
							aux=aux+" ";
						outputStream.write(aux.getBytes(),0,7);
					}
					outputStream.write(("\t").getBytes());
					var= new Double(grid.getMeanValue());
					if (var.toString().getBytes().length>7)
						outputStream.write(var.toString().getBytes(),0,7);// Elementos de la matriz;
					else{
						aux = var.toString();
						for (int k=var.toString().getBytes().length; k<8; k++)
							aux=aux+" ";
						outputStream.write(aux.getBytes(),0,7);
					}

				}
			outputStream.write("\n\n".getBytes());

			} catch (IOException e) {
				e.printStackTrace();
			} catch (GridException e) {

			}


	}

	/**
	 * Método para exportar a disco la matriz que se le pasa por argumento.
	 * Si es una matriz de autovectores ordena las columnas en orden descendente segun autovalores.
	 * @param matriz matriz a exportar
	 * @param isAutovectorMatrix true si es una matrix de autovectores
	 * @param descriptor de  fichero de salida
	 */
	private void ExportarMatriz(Matrix matriz, boolean isAutovectorMatrix,OutputStream outputStream ){

		// Orden correcto de las columnas de autovectores
		int resultOrden[]= new int[matriz.getRowDimension()];
		int cont = matriz.getRowDimension()-1;
		for(int i=0;i<matriz.getRowDimension();i++){
				if (isAutovectorMatrix)
					resultOrden[i]=cont;
				else
					resultOrden[i]=i;

				cont--;
		}
		String aux=null;
		try{
			outputStream.write("\t\t\t".getBytes());
					for (int j=0; j<matriz.getColumnDimension();j++)
						{
							outputStream.write(("Band"+(j+1)).getBytes());
							outputStream.write("\t\t\t".getBytes());
						}
					outputStream.write("\n".getBytes());
					// Volcado de la Matriz a disco
					for (int i=0 ; i<matriz.getRowDimension(); i++){
						outputStream.write(("Band"+(i+1)).getBytes());
						outputStream.write("\t\t".getBytes());
						for (int j=0; j<matriz.getColumnDimension();j++){
							Double var =new Double(matriz.get(i,resultOrden[j]));
							if (var.toString().getBytes().length>15)
								outputStream.write(var.toString().getBytes(),0,15);// Elementos de la matriz;
							else{
								aux = var.toString();
								for (int k=var.toString().getBytes().length; k<16; k++)
									aux=aux+" ";
								outputStream.write(aux.getBytes(),0,15);
							}
							outputStream.write("\t".getBytes());
						}
						outputStream.write("\n".getBytes());
					}
					outputStream.write("\n".getBytes());
			}catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	}

	/**
	 * @return checkbox autovectores
	 */
	public JCheckBox getAutovectorStatistics() {
		if(autovectorStatistics==null){
			autovectorStatistics=new JCheckBox(PluginServices.getText(this, "autovector_statistics"),true);
		}
		return autovectorStatistics;
	}


	/**
	 * @return checkbox estadisticas por banda
	 */
	public JCheckBox getBandasStatistics() {
		if(bandasStatistics==null){
			bandasStatistics=new JCheckBox(PluginServices.getText(this, "bandas_statistics"),true);
		}
		return bandasStatistics;
	}


	/**
	 * @return checkbox matriz varianza-covarianza
	 */
	public JCheckBox getVarianceStatistics() {
		if(varianceStatistics==null){
			varianceStatistics=new JCheckBox(PluginServices.getText(this, "variance_statistics"),true);
		}
		return varianceStatistics;
	}


	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.buttonspanel.ButtonsPanelListener#actionButtonPressed(org.gvsig.gui.beans.buttonspanel.ButtonsPanelEvent)
	 */
	public void actionButtonPressed(ButtonsPanelEvent e) {
//		 Botón de Aceptar
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


	/**
	 * acciones al aplicar
	 */
	public void aplicar(){

		if(getTableFormatComponent().getNumSelected()>0){
			PCImageProcess process = new PCImageProcess();
			process.addParam("inputRasterLayer",pc.getRasterLayer());
			process.addParam("statistics",pcStatistics);
			process.addParam("selectedBands", selectedBands);
			process.addParam("selectedComponents",getTableFormatComponent().getSeleccionadas());
			process.addParam("outputPath",filename);
			process.setActions(this);
			process.start();


		}
		else{
			JOptionPane.showMessageDialog(null,
			PluginServices.getText(this,"no_components"), PluginServices.getText(this,"principal_components"),
			JOptionPane.WARNING_MESSAGE);
		}
	}

	/**
	 * acciones al cerrar
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
	 * Devuelve la ruta del fichero donde se va a guardar, en caso de guardarse
	 * en memoria, calcula el nombre sin preguntar y devuelve la ruta.
	 * @return string con la ruta de salida
	 */
	public String getFileSelected() {
		String path = "";
		if (getRadioFile().isSelected()) {
			JFileChooser chooser = new JFileChooser("PRINCIPAL_COMPONENT_CALCULUS_PANEL_FILE_SELECTED",JFileChooser.getLastPath("PRINCIPAL_COMPONENT_CALCULUS_PANEL_FILE_SELECTED", null));
			chooser.setDialogTitle(PluginServices.getText(this, "seleccionar_fichero"));
			//Añadimos las extensiones que hayan sido registradas en el driver
			String[] extList = GeoRasterWriter.getDriversExtensions();
			for(int i=0;i<extList.length;i++)
				chooser.addChoosableFileFilter(new ExtendedFileFilter(extList[i]));

			if (chooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION)
				return null;
			JFileChooser.setLastPath("PRINCIPAL_COMPONENT_CALCULUS_PANEL_FILE_SELECTED", chooser.getSelectedFile());
//			FileOpenWizard.setLastPath(chooser.getSelectedFile().getPath().substring(0, chooser.getSelectedFile().getPath().lastIndexOf(File.separator)));
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

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==getExportar()){
			exportStatistics();
		}
		if (e.getSource()==getTodos()){
			getTableFormatComponent().seleccionarTodas();
			updateUI();
		}
		if (e.getSource()==getNinguno()){
			getTableFormatComponent().seleccionarNinguna();
			updateUI();
		}
	}


	public void setFilename(String fileName){
		filename=fileName;
	}

	public void focusGained(FocusEvent arg0) {
		// TODO Auto-generated method stub
	}

	public void focusLost(FocusEvent arg0) {
		// TODO Auto-generated method stub
	}



	public void windowActivated() {
		// TODO Auto-generated method stub

	}

	public void windowClosed() {
		// TODO Auto-generated method stub

	}

	public void end(Object param) {
		resultLayer = (FLyrRasterSE)param;
		loadLayer();
	}

	private void loadLayer() {
		view.getMapControl().getMapContext().getLayers().addLayer(resultLayer);
		view.getMapControl().getMapContext().endAtomicEvent();
		view.getMapControl().getMapContext().invalidate();
	}

	public void interrupted() {
		// TODO Auto-generated method stub
	}

	public Object getWindowProfile() {
		return WindowInfo.DIALOG_PROFILE;
	}
}





