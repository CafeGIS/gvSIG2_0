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

package org.gvsig.remotesensing.scatterplot.listener;

import java.util.ArrayList;
import java.util.Iterator;

import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.fmap.geom.Geometry.SUBTYPES;
import org.gvsig.fmap.geom.exception.CreateGeometryException;
import org.gvsig.fmap.geom.util.UtilFunctions;
import org.gvsig.fmap.mapcontext.layers.GraphicLayer;
import org.gvsig.fmap.mapcontext.rendering.legend.FGraphic;
import org.gvsig.fmap.mapcontext.rendering.symbols.IMarkerSymbol;
import org.gvsig.fmap.mapcontext.rendering.symbols.ISymbol;
import org.gvsig.fmap.mapcontext.rendering.symbols.SymbologyFactory;
import org.gvsig.fmap.mapcontrol.MapControl;
import org.gvsig.fmap.raster.grid.roi.VectorialROI;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.raster.RasterProcess;
import org.gvsig.raster.buffer.BufferFactory;
import org.gvsig.raster.buffer.RasterBuffer;
import org.gvsig.raster.buffer.RasterBufferInvalidException;
import org.gvsig.raster.dataset.IBuffer;
import org.gvsig.raster.dataset.IRasterDataSource;
import org.gvsig.raster.grid.Grid;
import org.gvsig.raster.grid.GridException;
import org.gvsig.raster.grid.roi.ROI;
import org.gvsig.rastertools.rasterresolution.ZoomPixelCursorListener;
import org.gvsig.remotesensing.scatterplot.chart.ROIChart;
import org.jfree.data.Range;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.project.documents.view.gui.View;

/**
 * Clase que implementa el proceso de construcción de una roi a partir de una
 * ROIChart definida en el gráfico de dispersion.
 *
 * @author Alejandro Muñoz Sanchez (alejandro.munoz@uclm.es)
 * @version 11/12/2007
 */
public class RoiFromChartProcess extends RasterProcess {
	private static final GeometryManager geomManager = GeometryLocator.getGeometryManager();
	private static final Logger logger = LoggerFactory.getLogger(RoiFromChartProcess.class);

	private Grid grid=null;
	private ROIChart roiToExport= null;
	private FLyrRasterSE rasterSE =null;
	private int percent =0;

	/**
	 *  Constructor
	 *  @param roiToExport roiChart  que va a ser a exportar
	 *  @grid	grid asociado (unicamente dispone de las dos bandas seleccionadas en el grafico)
	 *
	 **/

	/*public RoiFromChartProcess(ROIChart roiToExport, FLyrRasterSE raster){
		this.roiToExport= roiToExport;
		this.rasterSE= raster;
	}*/



	public ROI createRasterRoi(){
		// creaccion de la roiRaster
		return null;
	}



	/***
	 *  Metodo que determina si el valor pasado esta dentro del intervalo
	 *  definido en rango.
	 * @param valor valor a comprobar
	 * @rango rango intervalo de tipo double
	 *
	 */
	public boolean isInside(double valor, Range rango){

		double minValue= rango.getLowerBound();
		double maxValue= rango.getUpperBound();
		if( ( minValue<= valor) && (maxValue>=valor)) {
			return true;
		} else {
			return false;
		}

	}


	/**Esto tiene que cambiar!!!!!!!! Actualmente solo pinta los pixeles que pertenecen a la roi
	 * Dibujado de la roi
	 * */
	void drawRoi(VectorialROI roi){

		VectorialROI vectorialROI = roi;
		ISymbol symbol = null;
		FGraphic fGraphic = null;
		View view = (View) PluginServices.getMDIManager().getActiveWindow();
		MapControl mapControl = view.getMapControl();
		GraphicLayer graphicLayer =mapControl.getMapContext().getGraphicsLayer();

		for (Iterator iterator = vectorialROI.getGeometries()
				.iterator(); iterator.hasNext();) {
			Geometry geometry = (Geometry) iterator.next();
			switch (geometry.getType()) {
			case Geometry.TYPES.POINT:
				symbol = SymbologyFactory.createDefaultMarkerSymbol();
				((IMarkerSymbol) symbol).setColor(roi.getColor());
				break;
			}
			fGraphic = new FGraphic(geometry, graphicLayer.addSymbol(symbol));
			graphicLayer.addGraphic(fGraphic);
			mapControl.drawGraphics();
		}

	}

	public void init() {
		this.roiToExport= (ROIChart)getParam("roi");
		this.rasterSE= (FLyrRasterSE)getParam("raster");
	}

	/**
	 *  Proceso que construye la ROI Vectorial añadiendo un punto por
	 *  cada pixel que se encuentra en el conjunto de rangos especificados.
	 *  Para terminar se pinta la roi en la vista
	 * */
	public void process() throws InterruptedException {

		IRasterDataSource dsetCopy = null;
		dsetCopy = rasterSE.getDataSource().newDataset();
		BufferFactory bufferFactory = new BufferFactory(dsetCopy);
		if (!RasterBuffer.loadInMemory(dsetCopy)) {
			bufferFactory.setReadOnly(true);
		}

		try {
			grid=new Grid(bufferFactory);
		} catch (RasterBufferInvalidException e1) {
			e1.printStackTrace();
		}

		VectorialROI newRoi = new VectorialROI(grid);
		newRoi.setColor(roiToExport.getColor());
		newRoi.setName(roiToExport.getName());

		double valorBandX= 0;
		double valorBandY=0;
		Range rango[] = null;
		int nX = grid.getRasterBuf().getWidth();
		int nY = grid.getRasterBuf().getHeight();
		IBuffer buffer = grid.getRasterBuf();
		ArrayList rangos= roiToExport.getRanges();
		double mapX=0;
		double mapY=0;

		// Caso de buffer tipo BYTE
		if(buffer.getDataType() == RasterBuffer.TYPE_BYTE)
		{
				for (int j=0; j<nY; j++){

					for(int i=0; i<nX; i++)
					{
						try {
							grid.setBandToOperate(roiToExport.getBandaX());
							valorBandX= grid.getCellValueAsByte(i,j)&0xff;
							grid.setBandToOperate(roiToExport.getBandaY());
							valorBandY=	grid.getCellValueAsByte(i,j)&0xff;
						} catch (GridException e) {
							e.printStackTrace();
						}

						Iterator iterator =rangos.iterator();
						while(iterator.hasNext()){
							rango=(Range[]) iterator.next();
							if(isInside(valorBandX,rango[0]) && isInside(valorBandY,rango[1])){
								 mapX = grid.getGridExtent().getMin().getX()+i*grid.getCellSize();
								 mapY = grid.getGridExtent().getMax().getY()-j*grid.getCellSize();
								Geometry geometry;
								try {
									geometry = geomManager.createPoint(mapX,
										mapY, SUBTYPES.GEOM2D);
									newRoi.addGeometry(geometry); // Geometria
								} catch (CreateGeometryException e) {
									logger.error("Error creating a point", e);
								}
								
							}
						}
					}
					percent=j*100/nY;
				}
		}

		// Caso de buffer tipo SHORT
		if(buffer.getDataType() == RasterBuffer.TYPE_SHORT)
		{
			for (int j=0; j<nY; j++){
				for(int i=0; i<nX; i++)
					{
					try {
					grid.setBandToOperate(roiToExport.getBandaX());
					valorBandX= grid.getCellValueAsShort(i,j);
					grid.setBandToOperate(roiToExport.getBandaY());
					valorBandY=	grid.getCellValueAsShort(i,j);

					} catch (GridException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
						Iterator iterator =rangos.iterator();
						while(iterator.hasNext()){
							rango=(Range[]) iterator.next();
							if(isInside(valorBandX,rango[0]) && isInside(valorBandY,rango[1])){
								 mapX = grid.getGridExtent().getMin().getX()+i*grid.getCellSize();
								 mapY = grid.getGridExtent().getMax().getY()-j*grid.getCellSize();
								Geometry geometry;
								try {
									geometry = geomManager.createPoint(mapX,
										mapY, SUBTYPES.GEOM2D);
									newRoi.addGeometry(geometry);
								} catch (CreateGeometryException e) {
									logger.error("Error creating a point", e);
								}								
							}
						}
					}
				}
		}

		drawRoi(newRoi);
		externalActions.end(newRoi);
	}

	public String getTitle() {
		// TODO Auto-generated method stub
		return PluginServices.getText(this,"generando_roi");
	}

	public int getPercent() {
		// TODO Auto-generated method stub
		return percent;
	}

}
