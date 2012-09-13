package org.gvsig.remotesensing.scatterplot.chart;

import java.awt.Color;

import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.raster.RasterProcess;
import org.gvsig.raster.buffer.BufferFactory;
import org.gvsig.raster.buffer.RasterBuffer;
import org.gvsig.raster.buffer.RasterBufferInvalidException;
import org.gvsig.raster.dataset.IBuffer;
import org.gvsig.raster.dataset.IRasterDataSource;
import org.gvsig.raster.grid.Grid;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;

import com.iver.andami.PluginServices;

public class ScatterPlotProcess extends RasterProcess {

	private static final long 		serialVersionUID 	= 1L;
	private JFreeChart 				chart				= null;
	private  ScatterPlotChart		plot 				= null;
	private int 					bandaX				= 0;
	private int 					bandaY				= 1;
	private Grid 					grid				= null;
	private float 					data[][]			= null;
	private FLayer 					fLayer				= null;
	private int 					percent				= 0;
	private Color 					colorExternal		= null;
	private Color 					colorBackground		= null;
	private Color 					colorChart			= null;

	public void init() {
		fLayer = (FLayer)getParam("layer");
		colorBackground = (Color) getParam("backgroundColor");
		colorExternal = (Color) getParam("externalColor");
		colorChart = (Color) getParam("chartColor");
		bandaX = getIntParam("bandX");
		bandaY = getIntParam("bandY");
		percent = 0;
	}

	public void process() throws InterruptedException {
		createChart(colorChart, colorBackground, colorExternal);
		if (externalActions != null)
			externalActions.end(chart);
	}

	public int getPercent() {
		return percent;
	}

	public String getTitle() {
		return PluginServices.getText(this, "generando_scatterplot");
	}

	public Object getResult() {
		return chart;
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
	     setDataChart();
	     plot = new ScatterPlotChart(this.data, domainAxis, rangeAxis);
	     chart = new JFreeChart(PluginServices.getText(this,"diagrama_dispersion"), plot);
	     data= null;
		 chart.getRenderingHints().clear();
		 chart.setBackgroundPaint(colorExternal);
		 plot.setPaint(colorChart);
		 plot.setBackgroundPaint(colorBackground);
	}


	/**
	 * 	Metodo que establece los datos a representar.
	 * 	El vector data[][] se rellena con los valores de cada punto para las bandas
	 *  bandX y bandY.
	 * */
	public void setDataChart(){

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
				percent = j * 100 /height;
			}
		}

		if (dataGrid.getDataType()== IBuffer.TYPE_SHORT){
			for(int j=0; j<height; j++){
				for(int i=0; i<width;i++){
					data[0][indice]= dataGrid.getRasterBuf().getElemShort(j, i, 0);
					data[1][indice]= dataGrid.getRasterBuf().getElemShort(j, i, 1);
					indice++;
				}
				percent = j * 100 /height;
			}
		}

		if (dataGrid.getDataType()== IBuffer.TYPE_INT){
			for(int j=0; j<height; j++){
				for(int i=0; i<width;i++){
					data[0][indice]= dataGrid.getRasterBuf().getElemInt(j, i, 0);
					data[1][indice]= dataGrid.getRasterBuf().getElemInt(j, i, 1);
					indice++;
				}
				percent = j * 100 /height;
			}
		}

		if (dataGrid.getDataType()== IBuffer.TYPE_FLOAT){
			for(int j=0; j<height; j++){
				for(int i=0; i<width;i++){
					data[0][indice]= dataGrid.getRasterBuf().getElemFloat(j, i, 0);
					data[1][indice]= dataGrid.getRasterBuf().getElemFloat(j, i, 1);
					indice++;
				}
				percent = j * 100 /height;
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
				percent = j * 100 /height;
			}
		}

	}
}
