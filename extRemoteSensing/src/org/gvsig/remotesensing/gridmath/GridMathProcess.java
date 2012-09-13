/* gvSIG. Sistema de Informaci�n Geogr�fica de la Generalitat Valenciana
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
 *   Av. Blasco Iba�ez, 50
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

package org.gvsig.remotesensing.gridmath;

import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import org.gvsig.fmap.mapcontext.MapContext;
import org.gvsig.fmap.mapcontext.exceptions.LoadLayerException;
import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.mapcontext.layers.FLayers;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.gui.beans.incrementabletask.IncrementableEvent;
import org.gvsig.raster.RasterProcess;
import org.gvsig.raster.buffer.BufferFactory;
import org.gvsig.raster.buffer.RasterBuffer;
import org.gvsig.raster.buffer.WriterBufferServer;
import org.gvsig.raster.dataset.GeoRasterWriter;
import org.gvsig.raster.dataset.IBuffer;
import org.gvsig.raster.dataset.IRasterDataSource;
import org.gvsig.raster.dataset.InvalidSetViewException;
import org.gvsig.raster.dataset.NotSupportedExtensionException;
import org.gvsig.raster.dataset.RasterDriverException;
import org.gvsig.raster.grid.GridExtent;
import org.gvsig.raster.process.CancelEvent;
import org.gvsig.raster.util.RasterToolsUtil;
import org.gvsig.remotesensing.RemoteSensingUtils;
import org.gvsig.remotesensing.gridmath.gui.GridMathPanel;
import org.nfunk.jep.JEP;
import org.nfunk.jep.Variable;

import com.iver.andami.PluginServices;

/**
 * GridMathProcess implenta la funcionalidad para el c�lculo de operaciones entre grids.
 * El proceso permite hacer operaciones matem�ticas entre los valores de las bandas de la misma imagen o entre
 * diferentes im�genes bajo ciertas restricciones espaciales (siempre sobre los datos originales).
 *
 * @author Alejandro Mu�oz Sanchez (alejandro.munoz@uclm.es)
 * @author Diego Guerrero Sevilla (diego.guerrero@uclm.es)
 * @version 19/10/2007
 *
 */

public class GridMathProcess extends RasterProcess{

	HashMap 						params 				= null;   // Asignacion de variables a grids.
	JEP 							parser 				= null;
	String 							expression 			= null;
	GridExtent 						resultExtent 		= null;
	private WriterBufferServer 		writerBufferServer	= null;
	private int 					percent 		  	= 0;
	private boolean 				cancel 				= false;
	private MapContext				mapContext			= null;
	private String					filePath			= null;
	private AffineTransform			aTransform			= null;
	private RasterBuffer 			rasterResult 		= null;
	private GridMathPanel			gridMathPanel		= null;
	private boolean					loadEnd= false;
	int loadBuffers					=0;
	int numVar						=1;
	/**
	 * Constructor
	 */
	public GridMathProcess(){
		parser = new JEP();
		parser.setAllowUndeclared(true);
		parser.addStandardFunctions();
	}


	/**
	 * @return HashMap con las variables asociadas a sus grid correspondientes
	 * */
	public HashMap getParams() {
		return params;
	}


	/**
	 * Asigna el params con las variables
	 * @param params
	 */
	public void setParams(HashMap params) {
		this.params = params;
	}


	/**
	 * Establece la expresi�n en el parser
	 * @param expression expresion
	 */
	public void setExpression(String expression){
		this.expression = expression;
		parser.getSymbolTable().clear();
		parser.parseExpression(expression);
	}

	/**
	 * @return expresi�n a evaluar
	 */
	public String getExpression() {
		return expression;
	}


	/**
	 * @return true si en el parseo de la expresion hay error
	 */
	public boolean hasError(){
		if (parser!=null)
			return parser.hasError();
		else
			return true;
	}



	/**
	*  Escritura del resultado en disco.
	*/
	private void writeToFile(){

		if(filePath==null)
			return;
		try {
			writerBufferServer = new WriterBufferServer(rasterResult);
			aTransform = new AffineTransform(resultExtent.getCellSizeX(), 0.0, 0.0, -resultExtent.getCellSizeY(), resultExtent.getMin().getX(), resultExtent.getMax().getY());
			GeoRasterWriter grw = GeoRasterWriter.getWriter(writerBufferServer, filePath, rasterResult.getBandCount(), aTransform, resultExtent.getNX(), resultExtent.getNY(), rasterResult.getDataType(), GeoRasterWriter.getWriter(filePath).getParams(), mapContext.getProjection());
			grw.dataWrite();
			grw.writeClose();
		} catch (NotSupportedExtensionException e) {
			RasterToolsUtil.messageBoxError(PluginServices.getText(this, "error_writer_notsupportedextension"), this, e);
		} catch (IOException e) {
			RasterToolsUtil.messageBoxError(PluginServices.getText(this, "error_writer"), this, e);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		} catch (RasterDriverException e) {
			RasterToolsUtil.messageBoxError(PluginServices.getText(this, "raster_buffer_invalid_extension"), this, e);
		}

		mapContext.beginAtomicEvent();
		FLayer lyr = null;

		int endIndex = filePath.lastIndexOf(".");
		if (endIndex < 0)
			endIndex = filePath.length();

		try {
			 lyr = FLyrRasterSE.createLayer(
					 filePath.substring(filePath.lastIndexOf(File.separator) + 1, endIndex),
					 new File(filePath),
					 mapContext.getProjection());
		} catch (LoadLayerException e) {
			RasterToolsUtil.messageBoxError("error_cargar_capa", this, e);
		}

		mapContext.getLayers().addLayer(lyr);
		mapContext.endAtomicEvent();
		mapContext.invalidate();
		mapContext.endAtomicEvent();
	}

	/**
	 * @return extent del resultadoroot
	 */
	public GridExtent getResultExtent() {
		return resultExtent;
	}

	/**
	 * Asignaci�n del extent
	 * @param resultExtent
	 */
	public void setResultExtent(GridExtent resultExtent) {
		this.resultExtent = resultExtent;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.incrementabletask.IIncrementable#getLabel()
	 */
	public String getLabel() {
		return  PluginServices.getText(this,"procesando");
	}


	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.incrementabletask.IIncrementable#getTitle()
	 */
	public String getTitle() {
		return PluginServices.getText(this,"band_math");
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.incrementabletask.IIncrementable#actionCanceled
	 */
	public void actionCanceled(IncrementableEvent e) {
		if(writerBufferServer != null)
			rasterTask.setEvent(new CancelEvent(this));
		cancel = true;
	}


	public void init() {
		gridMathPanel= (GridMathPanel)getParam("panel");
		expression= getStringParam("expresion");
		setExpression(expression);
		params= (HashMap )getParam("params");
		resultExtent= (GridExtent) getParam("extent");
		mapContext= (MapContext)getParam("mapcontext");
		filePath= getStringParam("filepath");
		loadBuffers();
	}


	public void process() throws InterruptedException {
		RasterBuffer inputBuffer=null;
	
		// Construccion del rasterbuffer que recoge el resultado del calculo 
		rasterResult = RasterBuffer.getBuffer(RasterBuffer.TYPE_DOUBLE, resultExtent.getNX() ,resultExtent.getNY(), 1, true);

			
		// Calculo de grid resultante
		int iNX = resultExtent.getNX();
		int iNY = resultExtent.getNY();
	
		// Calculo de grid resultante
		for (int x=0;x<iNX;x++){
			if (cancel) return;  //Proceso cancelado 
			for(int y=0;y<iNY;y++){
				int i=0;
				for (Iterator iter = params.keySet().iterator(); iter.hasNext();) {
					String varName = (String)iter.next();
					Object data[]= (Object[])params.get(varName);
					inputBuffer= (RasterBuffer)data[0];
				
					int dataType= ((Integer)data[1]).intValue();
					double value=0;
				
					if(dataType==IBuffer.TYPE_BYTE){
						value =(byte) RemoteSensingUtils.getCellValueInLayerCoords(inputBuffer, x, y, 0);			
						if(value!=inputBuffer.getNoDataValue()){	
							parser.setVarValue(varName,new Double(value));
							i++;					
						}else{					
							rasterResult.setElem(y, x, 0, rasterResult.noDataValue);
							break;	
						}	
					}else{
							value =RemoteSensingUtils.getCellValueInLayerCoords(inputBuffer, x, y, 0);			
							if(value!=inputBuffer.getNoDataValue()){	
								parser.setVarValue(varName,new Double(value));
								i++;					
							}else{					
								rasterResult.setElem(y, x, 0, rasterResult.noDataValue);
								break;	
							}	
					}
				}
				// Evaluacion de la expresion en el x,y.
				if (i == params.size()){
					rasterResult.setElem(y, x, 0, (double)parser.getValue());
					percent = x*100/rasterResult.getWidth();
				}	
			}		
	
		}
		// Escritura de los datos a disco
		writeToFile();
		
	}

	void loadBuffers(){

		int nBand;
		String layerBand;
		String layerName;
		FLyrRasterSE rasterLayer;
		FLayers layers = gridMathPanel.getView().getModel().getMapContext().getLayers();

		RasterBuffer valor=null;
		int numVar= gridMathPanel.getCalculatorPanel().getJTableVariables().getTableFormat().getRowCount();
		for (int i=0;i<numVar;i++){

			layerBand= gridMathPanel.getCalculatorPanel().getJTableVariables().getTableFormat().getValueAt(i,1).toString();
			layerName = layerBand.substring(0,layerBand.indexOf("["));
			nBand = Integer.valueOf(layerBand.substring(layerBand.lastIndexOf("Band")+4,layerBand.lastIndexOf("]"))).intValue();
			rasterLayer = (FLyrRasterSE)layers.getLayer(layerName);

			double minX=0,minY=0,maxX=0,maxY=0;
			minX= gridMathPanel.getOutputExtent().getMin().getX();
			minY= gridMathPanel.getOutputExtent().getMin().getY();
			maxX= gridMathPanel.getOutputExtent().getMax().getX();
			maxY =gridMathPanel.getOutputExtent().getMax().getY();

			try {

				IRasterDataSource dsetCopy = null;
				dsetCopy = rasterLayer.getDataSource().newDataset();
				BufferFactory bufferFactory = new BufferFactory(dsetCopy);
				if (!RasterBuffer.loadInMemory(dsetCopy))
					bufferFactory.setReadOnly(true);
				bufferFactory.setAdjustToExtent(false);
				bufferFactory.setDrawableBands(new int[]{nBand-1});
				bufferFactory.setAreaOfInterest(minX,minY,maxX,maxY,gridMathPanel.getOutputExtent().getNX(),gridMathPanel.getOutputExtent().getNY());
				valor=(RasterBuffer) bufferFactory.getRasterBuf();
				loadBuffers+=1;

			} catch (ArrayIndexOutOfBoundsException e) {
				RasterToolsUtil.messageBoxError(PluginServices.getText(this, "error_writer"), this, e);
			} catch (InvalidSetViewException e) {
					e.printStackTrace();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			} catch (RasterDriverException e) {
				e.printStackTrace();
			}

			gridMathPanel.getCalculatorPanel().getQWindowsHash().put(gridMathPanel.getCalculatorPanel().getJTableVariables().getTableFormat().getValueAt(i,0).toString(),new Object[]{valor,new Integer(valor.getDataType())});

		}
		gridMathPanel.getGridMath().setParams(gridMathPanel.getCalculatorPanel().getQWindowsHash());


		JEP parser = new JEP();
		parser.setAllowUndeclared(true);
		parser.addStandardFunctions();
		parser.parseExpression(gridMathPanel.getCalculatorPanel().getJTextExpression().getText());

		for (Iterator iter = parser.getSymbolTable().values().iterator(); iter.hasNext();) {
			Variable variable = (Variable) iter.next();
			if (!gridMathPanel.getGridMath().getParams().containsKey(variable.getName())){
					RasterToolsUtil.messageBoxError(PluginServices.getText(this,"variables_sin_asignar"),this);
					return;
				}
		}
		params= gridMathPanel.getGridMath().getParams();
		loadEnd= true;
	}


	public int getPercent(){

		/*if(!loadEnd)
			return (int)((loadBuffers-1)/numVar)*100;
		else */if(writerBufferServer!=null)
			return writerBufferServer.getPercent();
		else
			return percent;
	}


	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.incrementabletask.IIncrementable#getLog()
	 */
	public String getLog() {

/*		if(!loadEnd)
			return PluginServices.getText(this,"cargando datos")+"...";*/
		/*else*/ if (writerBufferServer!=null)
			return PluginServices.getText(this,"escribiendo_resultado")+"...";
		else
			return PluginServices.getText(this,"calculando_imagen")+"...";
	}


	public Object getResult(){
		return rasterResult;
	}
}
