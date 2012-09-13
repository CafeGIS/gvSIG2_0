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

import javax.swing.JPanel;

import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.raster.IProcessActions;
import org.gvsig.raster.RasterProcess;
import org.gvsig.raster.buffer.BufferFactory;
import org.gvsig.raster.buffer.RasterBuffer;
import org.gvsig.raster.buffer.RasterBufferInvalidException;
import org.gvsig.raster.dataset.IBuffer;
import org.gvsig.raster.dataset.IRasterDataSource;
import org.gvsig.raster.grid.Grid;
import org.gvsig.remotesensing.scatterplot.chart.ScatterPlotChart;
import org.gvsig.remotesensing.scatterplot.chart.ScatterPlotDiagram;
import org.gvsig.remotesensing.scatterplot.chart.ScatterPlotProcess;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;

import com.iver.andami.PluginServices;

/**
 * Clase que define el panel donde aparece el grafico de dispersion para dos bandas, determinadas por
 * las variables bandaX y bandaY. El grafico es un FastScatterPlot de la libreria jfreeChart (personalizada para este
 * apartado).
 *
 * @author Alejandro Muñoz Sanchez (alejandro.munoz@uclm.es)
 * @version 11/12/2007
 */

public class ChartScaterPlotPanel extends JPanel implements IProcessActions{

	private static final long 		serialVersionUID 	= 1L;
	private JFreeChart 				chart				= null;;
	private ScatterPlotDiagram 		jPanelChart 		= null;
	private  ScatterPlotChart		plot 				= null;
	private int 					bandaX				= 0;
	private int 					bandaY				= 1;
	private Grid 					grid				= null;
	private float 					data[][]			= null;
	FLayer 							fLayer				= null;
	RasterProcess 					scatterPlotProcess 	= null;
	int 							initialRenderBand[] = null;

	/**
	 *   Constructor
	 *   @param flayer capa
	 *   @param band1	banda que se representa en el eje x.
	 *   @param band2	banda que se representa en el eje y.
	 * */
	public ChartScaterPlotPanel(FLayer fLayer, int band1, int band2){
		this.fLayer= fLayer;
		initialRenderBand = ((FLyrRasterSE) fLayer).getRenderBands();
		bandaX= band1;
		bandaY= band2;

		scatterPlotProcess = new ScatterPlotProcess();
		scatterPlotProcess.addParam("layer", fLayer);
		scatterPlotProcess.addParam("bandY", new Integer(bandaY));
		scatterPlotProcess.addParam("bandX", new Integer(bandaX));
		scatterPlotProcess.addParam("backgroundColor", Color.WHITE);
		scatterPlotProcess.addParam("externalColor", Color.WHITE);
		scatterPlotProcess.addParam("chartColor", Color.BLACK);
		scatterPlotProcess.setActions(this);
		scatterPlotProcess.start();

		//createChart(Color.BLACK,Color.WHITE,Color.WHITE);
		initialize();
	}


	/**
	 *  Inicializacion del panel que contiene el grafico
	 * */
	private void initialize() {
		this.setLayout(new BorderLayout());
		this.add(getChart(), BorderLayout.CENTER);
	}


	/**
	 * 	@return grafico actual
	 * */
	public ScatterPlotDiagram getChart(){
		if(jPanelChart == null){
			jPanelChart = new ScatterPlotDiagram(chart,(FLyrRasterSE)fLayer, new int[]{bandaX,bandaY});
			chart=null;
			jPanelChart.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.gray,1));
		}
		return 	jPanelChart;
	}



	/**
	 * 	Metodo que construye el grafico inicialmente. Antes de construirlo, llama a
	 * 	setDataChart() para cargar los datos a representar
	 *
	 * */
	private void createChart(Color colorChart, Color  colorBackground, Color colorExternal) {

		 NumberAxis domainAxis = new NumberAxis(PluginServices.getText(this,"banda")+" "+(bandaX+1));
	     domainAxis.setAutoRangeIncludesZero(false);
	     NumberAxis rangeAxis = new NumberAxis(PluginServices.getText(this,"banda")+" "+(bandaY+1));
	     rangeAxis.setAutoRangeIncludesZero(false);

	     // Se cargan los datos antes de construir el grafico
	     setDataChart(fLayer);
	     plot = new ScatterPlotChart(this.data, domainAxis, rangeAxis);
	     chart = new JFreeChart(PluginServices.getText(this,"diagrama_dispersion"), plot);
	     data= null;
		 chart.getRenderingHints().clear();
		 chart.setBackgroundPaint(colorExternal);
		 plot.setPaint(colorChart);
		 plot.setBackgroundPaint(colorBackground);
	}


	/**
	 * 	Actualizacion del panel que contiene el grafico
	 * */
	public void updateChartPanel(Color colorChart, Color  colorbackground, Color colorExternal){
		/*chart.setBackgroundPaint(colorExternal);
		plot.setPaint(colorChart);
		plot.setBackgroundPaint(colorbackground);*/
		jPanelChart.setChart(chart);
		jPanelChart.updateBands(new int[]{bandaX,bandaY});
		// hace un clear cuando se cambian las bandas
		jPanelChart.getROIChartList().getListRois().clear();
		jPanelChart.repaint();
		updateUI();
	}


	/**
	 * 	Actualizacion del grafico. Esta actualizacion se producira cuando haya variacion en la
	 *  seleccion de las bandas por parte del usuario.
	 *
	 * */
	public void updateChart(Color colorChart, Color  colorBackground, Color colorExternal) {

		 NumberAxis domainAxis = new NumberAxis(PluginServices.getText(this,"banda")+(bandaX+1));
	     domainAxis.setAutoRangeIncludesZero(false);
	     NumberAxis rangeAxis = new NumberAxis(PluginServices.getText(this,"banda")+(bandaY+1));
	     rangeAxis.setAutoRangeIncludesZero(false);
	     chart=null;
	     plot=null;

	    scatterPlotProcess = new ScatterPlotProcess();
		scatterPlotProcess.addParam("layer", fLayer);
		scatterPlotProcess.addParam("bandY", new Integer(bandaY));
		scatterPlotProcess.addParam("bandX", new Integer(bandaX));
		scatterPlotProcess.addParam("backgroundColor", Color.WHITE);
		scatterPlotProcess.addParam("externalColor", Color.WHITE);
		scatterPlotProcess.addParam("chartColor", Color.BLACK);
		scatterPlotProcess.setActions(this);
		scatterPlotProcess.start();

	     // Se cargan los datos correspondiesntes a las bandas seleccionadas
	    /* setDataChart(fLayer);
	     plot = new ScatterPlotChart(this.data, domainAxis, rangeAxis);

		 chart = new JFreeChart(PluginServices.getText(this,"diagrama_dispersion"), plot);
		 chart.getRenderingHints().clear();
		 data=null;*/

		 updateChartPanel(colorChart,colorBackground,colorExternal);

		}



	/**
	 * 	Metodo que establece los datos a representar.
	 * 	El vector data[][] se rellena con los valores de cada punto para las bandas
	 *  bandX y bandY.
	 * */
	public void setDataChart(FLayer fLayer){

		try {
			this.fLayer = fLayer;
		} catch (Exception e) {
			e.printStackTrace();
		}
		FLyrRasterSE rasterLayer = (FLyrRasterSE) fLayer;

		IRasterDataSource dsetCopy = null;
		dsetCopy = rasterLayer.getDataSource().newDataset();
		BufferFactory bufferFactory = new BufferFactory(dsetCopy);
		if (!RasterBuffer.loadInMemory(dsetCopy))
			bufferFactory.setReadOnly(true);

		// grid renderizado
		grid=rasterLayer.getRender().getGrid();
		Grid dataGrid= null;
		try {
			 dataGrid= new Grid(bufferFactory,new int[]{bandaX,bandaY});
		} catch (RasterBufferInvalidException e) {
			e.printStackTrace();
		}
		int width= grid.getRasterBuf().getWidth();
		int height= grid.getRasterBuf().getHeight();
		int indice=0;

		data= new float[2][width*height];

		if (dataGrid.getDataType()== IBuffer.TYPE_BYTE){
			for(int j=0; j<height; j++){
				for(int i=0; i<width;i++){
					data[0][indice]= dataGrid.getRasterBuf().getElemByte(j, i, 0) & 0xff;
					data[1][indice]= dataGrid.getRasterBuf().getElemByte(j, i, 1) & 0xff;
					indice++;
				}
			}
		}

		if (dataGrid.getDataType()== IBuffer.TYPE_SHORT){
			for(int j=0; j<height; j++){
				for(int i=0; i<width;i++){
					data[0][indice]= dataGrid.getRasterBuf().getElemShort(j, i, 0);
					data[1][indice]= dataGrid.getRasterBuf().getElemShort(j, i, 1);
					indice++;
				}
			}
		}

		if (dataGrid.getDataType()== IBuffer.TYPE_INT){
			for(int j=0; j<height; j++){
				for(int i=0; i<width;i++){
					data[0][indice]= dataGrid.getRasterBuf().getElemInt(j, i, 0);
					data[1][indice]= dataGrid.getRasterBuf().getElemInt(j, i, 1);
					indice++;
				}
			}
		}

		if (dataGrid.getDataType()== IBuffer.TYPE_FLOAT){
			for(int j=0; j<height; j++){
				for(int i=0; i<width;i++){
					data[0][indice]= dataGrid.getRasterBuf().getElemFloat(j, i, 0);
					data[1][indice]= dataGrid.getRasterBuf().getElemFloat(j, i, 1);
					indice++;
				}
			}
		}
		// OJO AL CASTINTG DE DOUBLE A FLOAT
		if (dataGrid.getDataType()== IBuffer.TYPE_DOUBLE){
			for(int j=0; j<height; j++){
				for(int i=0; i<width;i++){
					data[0][indice]= (float)dataGrid.getRasterBuf().getElemDouble(j, i, 0);
					data[1][indice]= (float)dataGrid.getRasterBuf().getElemDouble(j, i, 1);
					indice++;
				}
			}
		}

	}


	/**
	 * Asignacion de la banda X
	 * */
	public void setBandX(int band){
		bandaX= band;
	}




	/**
	 * Asignacion de la banda Y
	 * */
	public void setBandY(int band){
		bandaY= band;
	}


	/**
	 * @return banda X
	 * */
	public int getBandX(){
		return bandaX;
	}


	/**
	 * @return banda Y
	 * */
	public int getBandY(){
		return bandaY;
	}


	/**
	 *  @return grid asociado
	 *
	 * */
	public Grid getGrid(){
		return grid;
	}


	/**
	 * @return capa asociada al panel
	 *
	 * */

	public FLayer getFLayer(){
		return fLayer;
	}


	public void end(Object param) {
		chart = (JFreeChart)scatterPlotProcess.getResult();
		//initialize();
		updateChartPanel(Color.BLACK, Color.WHITE, Color.WHITE);
	}


	public void interrupted() {
		// TODO Auto-generated method stub

	}
}



