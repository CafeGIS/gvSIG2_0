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

package org.gvsig.remotesensing.profiles.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.gvsig.fmap.mapcontrol.MapControl;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.gui.beans.buttonspanel.ButtonsPanel;
import org.gvsig.gui.beans.defaultbuttonspanel.DefaultButtonsPanel;
import org.gvsig.gui.beans.graphic.GraphicChartPanel;
import org.gvsig.gui.beans.table.exceptions.NotInitializeException;
import org.gvsig.raster.util.RasterToolsUtil;

import com.iver.andami.PluginServices;

/**
 * Clase que define el panel donde aparece el grafico  del perfil espectral de los puntos que seleccione el
 * usuario.
 *
 * @author aMuÑoz (alejandro.munoz@uclm.es)
 * @version 11/3/2008
 */

public class ProfilePanel  extends DefaultButtonsPanel implements ChangeListener{

	private static final long 			serialVersionUID = 1L;
	private GraphicChartPanel			jPanelChart = null;
	private MapControl 					mapControl = null;;
	private JPanel 						southPanel = null;
	private ZProfileOptionsPanel  		pointOptionsPanel= null;
	private LineProfileOptionsPanel  	lineOptionsPanel= null;
	private JTabbedPane 				tabbedPane  = null;
	public static int 					PANELZPROFILE = 1;
	public static int 					PANELLINEPROFILE =0;
	private int 						nextActiveChart = 0;
	private FLyrRasterSE				fLayer = null;
	/**  Constructor del panel.
	 *   @param flayer capa de la que se van a tomar los puntos
	 **/
	public ProfilePanel(ProfileDialog dialog){
		super(ButtonsPanel.BUTTONS_ACCEPTCANCEL);
		fLayer= dialog.getFlayer();
		this.mapControl= dialog.getMapControl();
		initialize();
	}


	/**
	 *  Inicializacion del panel que contiene el grafico
	 * */
	private void initialize() {
		this.setLayout(new BorderLayout());
		this.add(getChart(), BorderLayout.CENTER);
		this.add(getSouthPanel(), BorderLayout.SOUTH);
	}

	/**
	 * 	@return grafico actual
	 * */
	public GraphicChartPanel getChart(){
		if(jPanelChart == null){
			jPanelChart = new GraphicChartPanel();
			 this.jPanelChart.getChart().getChart().getPlot().setBackgroundImage(null);
			 //jPanelChart.getChart().setDomainZoomable(false);
			// jPanelChart.getChart().setRangeZoomable(false);
			 jPanelChart.getChart().setDisplayToolTips(true);
		}
		return 	jPanelChart;
	}


	/**
	 * @return mapControl
	 * */
	public MapControl getMapControl() {
		return mapControl ;
	}


	/**
	 * @return JPanel con los tab.
	 * */
	public JPanel getSouthPanel(){
		if(southPanel== null){
			southPanel= new JPanel();
			BorderLayout bd= new BorderLayout();
			southPanel.setLayout(bd);
			southPanel.setPreferredSize(new Dimension(150,110));
			lineOptionsPanel=new LineProfileOptionsPanel(this);
			lineOptionsPanel.setJPanelChart(getChart());
		 	getTabbedPane().addTab(PluginServices.getText(this, "lineProfile"),lineOptionsPanel );
		 	pointOptionsPanel= new ZProfileOptionsPanel(this);
		 	pointOptionsPanel.setJPanelChart(getChart());
			getTabbedPane().addTab(PluginServices.getText(this, "zProfile"),pointOptionsPanel);
			southPanel.add(getTabbedPane(),BorderLayout.CENTER);
		}
		return southPanel;
	}


	/**
	 * @return panel con las opciones zProfiles
	 * */
	public ZProfileOptionsPanel getPointOptionsPanel(){
		if(pointOptionsPanel==null){
			pointOptionsPanel= new ZProfileOptionsPanel(this);
			pointOptionsPanel.setJPanelChart(getChart());
		}
		return pointOptionsPanel;
	}

	/**
	 *  @return panel con las opciones lineProfiles
	 *  */
	public LineProfileOptionsPanel getLineOptionsPanel(){
		if(lineOptionsPanel==null){
			lineOptionsPanel= new LineProfileOptionsPanel(this);
			lineOptionsPanel.setJPanelChart(getChart());
		}
		return lineOptionsPanel;
	}


	public  JTabbedPane getTabbedPane(){
		if(tabbedPane==null){
			tabbedPane= new JTabbedPane();
			tabbedPane.addChangeListener(this);
		}
		return tabbedPane;
	}


	/**
	 * Acciones a llevar a cabo cuando se cambia de pestaña
	 * */
	public void stateChanged(ChangeEvent e) {

		if(e.getSource().equals(getTabbedPane())){

			if(nextActiveChart==PANELZPROFILE){
				nextActiveChart = PANELLINEPROFILE;
				getLineOptionsPanel().getNewButton().setSelected(false);
				// Se actualizan las propiedades del gráfico
				getPointOptionsPanel().getJPanelChart();
				getPointOptionsPanel().getListener().drawChartAllPointsRois();
				getPointOptionsPanel().UpdateChart();
			}
			else if(nextActiveChart == PANELLINEPROFILE){

				nextActiveChart= PANELZPROFILE;
				getPointOptionsPanel().getNewButton().setSelected(false);
				try {

					int selectedRow = getLineOptionsPanel().getTable().getSelectedRow();
					getLineOptionsPanel().getJPanelChart();
					if(selectedRow==-1)
						return;
				} catch (NotInitializeException e1) {
					RasterToolsUtil.messageBoxError("tabla_no_inicializada", this, e1);
				}
			}
		}
	}


	/**
	 * @return entero que identifica el panel activo
	 * */
	public int getActivePanel(){
		return nextActiveChart;
	}

	/**@return layer asociado al panel*/
	public FLyrRasterSE getFlayer(){
		return fLayer;
	}

}
