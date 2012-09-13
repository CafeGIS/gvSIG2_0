/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
*
* Copyright (C) 2007 Instituto de Desarrollo Regional and Generalitat Valenciana.
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
*   Av. Blasco Ibáñez, 50
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

package org.gvsig.remotesensing.scatterplot.gui;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.mapcontrol.MapControl;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.gui.beans.buttonspanel.ButtonsPanel;
import org.gvsig.gui.beans.buttonspanel.ButtonsPanelEvent;
import org.gvsig.gui.beans.colorbutton.ColorButton;
import org.gvsig.gui.beans.defaultbuttonspanel.DefaultButtonsPanel;
import org.gvsig.raster.buffer.BufferFactory;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.project.documents.view.gui.View;
import com.iver.utiles.swing.JComboBox;

/**
 * Clase que define la interfaz que gestiona el grafico de dispersion.
 * Permite la seleccion de las bandas por parte del usuario y el acceso
 * al gestor de ROis del grafico.
 *
 * @author Alejandro Muñoz Sanchez (alejandro.munoz@uclm.es)
 * @see org.gvsig.remotesensing.scatterplot.gui.ChartScaterPlotPanel
 * @version 4/12/2007
 */

public class ScatterPlotPanel extends DefaultButtonsPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	private MapControl mapControl = null;
	private FLayer fLayer = null;
	private View view = null;
	private JComboBox comboX= null;
	private JComboBox comboY= null;
	private ChartScaterPlotPanel panelGrafico = null;
	private JPanel selectionBandPanel= null;
	private JButton jbuttomGestor = null;
	private JPanel southPanel = null;
	private JPanel optionsPanel = null;
	private JPanel panelRoiButton=null;
	private ColorButton colorChart = null;
	private ColorButton colorBackground = null;
	private ColorButton colorExternal = null;
	/**
	 * 	Constructor del panel
	 **/
	public ScatterPlotPanel(ScatterPlotDialog scatterDialog) {
		super(ButtonsPanel.BUTTONS_ACCEPTCANCEL );
		initialize();
	}


	/**
	 * 	Acciones de inicializacion
	 **/
	private void initialize() {
		setLayout(new BorderLayout(5,5));
		setBorder(new EmptyBorder(11,15,11,15));
	}


	/**
	 * 	Asignacion del layer al que esta asociado el diagrama
	 * */
	public void setFLayer(FLayer layer) {

		fLayer = layer;
		if (view==null){
			view = (View) PluginServices.getMDIManager().getActiveWindow();
			mapControl = view.getMapControl();
			BorderLayout bd= new BorderLayout();
		    bd.setVgap(8);
			this.setLayout(bd);
			//  Quitar la contruccion del panel de aqui
			add(getPanelGrafico(),BorderLayout.CENTER);
			add(getSouthPanel(), BorderLayout.SOUTH);
		}

	}



	/**
	 * @return Panel con los combos de seleccion de bandas y y botton de
	 * acceso al gestor de ChartROIs
	 * */
	public JPanel getBandPanel(){

		if(selectionBandPanel==null){
			selectionBandPanel = new JPanel();
		TitledBorder topBorder = BorderFactory.createTitledBorder(PluginServices.getText(this,"bandas"));
			topBorder.setTitlePosition(TitledBorder.TOP);
			selectionBandPanel.setBorder(topBorder);
			JPanel panelX= new JPanel();
			BorderLayout bd= new BorderLayout();
			panelX.setLayout(bd);
			bd.setHgap(3);
			panelX.add(new JLabel(PluginServices.getText(this,"ejex")),BorderLayout.CENTER);
			panelX.add(getComboX(),BorderLayout.EAST);
			selectionBandPanel.add(panelX);

			JPanel panelY= new JPanel();
			BorderLayout bd2= new BorderLayout();
			panelY.setLayout(bd2);
			panelY.add(new JLabel(PluginServices.getText(this,"ejey")),BorderLayout.CENTER);
			panelY.add(getComboY(),BorderLayout.EAST);
			selectionBandPanel.add(panelY);


		}
		return selectionBandPanel;

	}


	public JPanel getSouthPanel(){
		if(southPanel==null){
			southPanel = new JPanel();
			BorderLayout bd= new BorderLayout();
			southPanel.setLayout(bd);
			southPanel.add(getBandPanel(),BorderLayout.WEST);
			southPanel.add(getOptionsPanel(),BorderLayout.CENTER);
			southPanel.add(getPanelRoiButton(),BorderLayout.EAST);
		}

		return southPanel;
	}


	public JPanel getPanelRoiButton(){

		if(panelRoiButton==null){
			panelRoiButton = new JPanel();
			TitledBorder topBorder = BorderFactory.createTitledBorder(PluginServices.getText(this,"rois"));
			topBorder.setTitlePosition(TitledBorder.TOP);
			panelRoiButton.setBorder(topBorder);
			panelRoiButton.add(getJbuttomGestor());


		}

		return panelRoiButton;
	}


	public MapControl getMapControl() {
		return mapControl;
	}



	public FLayer getLayer(){
		return fLayer;
	}


	public void aplicar(){
		mapControl.getMapContext().getGraphicsLayer().clearAllGraphics();

		// acciones al aplicar
	}

	public void actionButtonPressed(ButtonsPanelEvent e) {
//		 Botón de Aceptar
		if (e.getButton() == ButtonsPanel.BUTTON_ACCEPT) {
			aplicar();
		}

		// Botón de Aplicar
		if (e.getButton() == ButtonsPanel.BUTTON_APPLY) {
			aplicar();
		}

		// Botón de Cerrar
		if (e.getButton() == ButtonsPanel.BUTTON_CANCEL) {
		}

	}

	/**
	 *  Combo banda X
	 * */
	public JComboBox getComboX() {
		if(comboX==null){
			DefaultComboBoxModel defComboBand=new DefaultComboBoxModel(getBand());
			comboX= new JComboBox(defComboBand);
			comboX.setSelectedIndex(0);
			comboX.addActionListener(this);

		}
		return comboX;
	}



	/**
	 *  Combo banda Y
	 * */
	public JComboBox getComboY() {

		if(comboY==null){
			DefaultComboBoxModel defComboBand=new DefaultComboBoxModel(getBand());
			comboY= new JComboBox(defComboBand);
			FLyrRasterSE raster = (FLyrRasterSE) fLayer;
			BufferFactory buffer= new BufferFactory(raster.getDataSource());
			if(buffer.getBandCount()>1)
				comboY.setSelectedIndex(1);
			else
				comboY.setSelectedIndex(0);
			comboY.addActionListener(this);

		}
		return comboY;
	}


	/** @return panel grafico que contiene el diagrama */
	public ChartScaterPlotPanel getPanelGrafico() {
		if(panelGrafico==null){
			FLyrRasterSE raster = (FLyrRasterSE) fLayer;
			BufferFactory buffer= new BufferFactory(raster.getDataSource());
			if(buffer.getBandCount()>1)
				// por defecto se construye el grafico con las bandas 0 y 1
				panelGrafico= new ChartScaterPlotPanel(fLayer, 0,1);
			else
				// si solo hay una banda se construye el grafico con la misma banda en x y en y
				panelGrafico = new ChartScaterPlotPanel(fLayer,0,0);
		}

		return panelGrafico;
	}


	/**
	 * 	@return array con los identificativos de las capas
	 * */
	public String[] getBand(){

		FLyrRasterSE raster = (FLyrRasterSE) fLayer;
		String bands[]= new String[raster.getBandCount()];
		for (int i= 0; i< raster.getBandCount(); i++)
			bands[i]= PluginServices.getText(this,"banda")+(i+1);
		return bands;

	}


	public void actionPerformed(ActionEvent e) {
		if(e.getSource()== getComboX()){
			panelGrafico.setBandX(getComboX().getSelectedIndex());
			panelGrafico.updateChart(getColorChart().getColor(),getColorBackground().getColor(),getColorExternal().getColor());
			panelGrafico.updateUI();
			panelGrafico.repaint();

		}
		if(e.getSource()== getComboY()){
			panelGrafico.setBandY(getComboY().getSelectedIndex());
			panelGrafico.updateChart(getColorChart().getColor(),getColorBackground().getColor(), getColorExternal().getColor());
			panelGrafico.updateUI();
			panelGrafico.repaint();
		}

		if(e.getSource()== getJbuttomGestor()){

			// Añade el panel  gestor de rois a la vista
			ManagerROIChartPanel panel =new ManagerROIChartPanel(panelGrafico);
			panelGrafico.getChart().setManagerROi(panel);
			PluginServices.getMDIManager().addWindow(panel);

		}

	}


	public JButton getJbuttomGestor() {

		if(jbuttomGestor==null){
			jbuttomGestor= new JButton(PluginServices.getText(this,"gestor"));
			jbuttomGestor.addActionListener(this);
		}
		return jbuttomGestor;
	}




	public JPanel getOptionsPanel() {
		if (optionsPanel==null){
			optionsPanel= new JPanel();
			TitledBorder topBorder = BorderFactory.createTitledBorder(PluginServices.getText(this,"colors"));
			topBorder.setTitlePosition(TitledBorder.TOP);
			optionsPanel.setBorder(topBorder);
			GridBagLayout gb= new GridBagLayout();
			GridBagConstraints constrains= new GridBagConstraints();
			// pimer boton color
			constrains.insets = new java.awt.Insets(3, 3, 3, 3);
			optionsPanel.setLayout(gb);
			constrains.gridx =0;
			constrains.gridy =0;
			optionsPanel.add(getColorChart(),constrains);
			constrains.gridx =1;
			constrains.gridy =0;
			optionsPanel.add(new JLabel(PluginServices.getText(this,"grafico")),constrains);
			constrains.gridx =0;
			constrains.gridy =1;
			optionsPanel.add(getColorBackground(),constrains);
			constrains.gridx =1;
			constrains.gridy =1;
			optionsPanel.add(new JLabel(PluginServices.getText(this,"fondo")),constrains);

			constrains.insets = new java.awt.Insets(3, 3, 3, 3);
			constrains.gridx =2;
			constrains.gridy =0;
			optionsPanel.add(getColorExternal(),constrains);
			constrains.insets = new java.awt.Insets(1, 1, 1, 1);
			constrains.gridx =3;
			constrains.gridy =0;
			optionsPanel.add(new JLabel(PluginServices.getText(this,"exterior")),constrains);

		}
		return optionsPanel;
	}


	public ColorButton getColorChart(){
		if(colorChart ==null)
		{
			colorChart = new ColorButton();
			colorChart.setPreferredSize(new Dimension(15,8));
			colorChart.setColor(Color.BLACK);
		}
		return colorChart;
	}


	public ColorButton getColorBackground(){
		if(colorBackground ==null)
		{
			colorBackground = new ColorButton();
			colorBackground.setPreferredSize(new Dimension(15,8));
			colorBackground.setColor(Color.WHITE);
		}
		return colorBackground;
	}


	public ColorButton getColorExternal(){
		if(colorExternal ==null)
		{
			colorExternal = new ColorButton();
			colorExternal.setPreferredSize(new Dimension(15,8));
			colorExternal.setColor(Color.WHITE);
		}
		return colorExternal;
	}

 }















