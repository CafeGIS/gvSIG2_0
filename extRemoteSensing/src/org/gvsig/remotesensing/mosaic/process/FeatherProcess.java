/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2008 Instituto de Desarrollo Regional and Generalitat Valenciana.
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

package org.gvsig.remotesensing.mosaic.process;

import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;

import org.gvsig.fmap.geom.primitive.Envelope;
import org.gvsig.fmap.mapcontext.exceptions.LoadLayerException;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.raster.RasterProcess;
import org.gvsig.raster.buffer.RasterBufferInvalidException;
import org.gvsig.raster.buffer.WriterBufferServer;
import org.gvsig.raster.dataset.GeoRasterWriter;
import org.gvsig.raster.dataset.IBuffer;
import org.gvsig.raster.dataset.NotSupportedExtensionException;
import org.gvsig.raster.dataset.RasterDriverException;
import org.gvsig.raster.datastruct.Extent;
import org.gvsig.raster.grid.Grid;
import org.gvsig.raster.grid.GridException;
import org.gvsig.raster.grid.GridExtent;
import org.gvsig.raster.grid.GridInterpolated;
import org.gvsig.raster.grid.OutOfGridException;
import org.gvsig.raster.util.RasterToolsUtil;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.project.documents.view.gui.View;

/**
 * Proceso para la obtención de una imagen mosaico a partir de un conjunto de
 * imágenes por el metodo de degradado (Feathering).
 *
 * @author Diego Guerrero Sevilla (diego.guerrero@uclm.es)
 *
 */
public class FeatherProcess extends RasterProcess {

	private FLyrRasterSE inputRasterLayers[] = null;
	private Grid inputGrids[] = null;
	private FLyrRasterSE outputRasterLayer = null;

	private Line2D borders[][] = null;

	private Rectangle2D inputExtents[] = null;
	private Rectangle2D intersection = null;

	private Grid resultGrid = null;
	private GridExtent resultGridExtent = null;
	private int resultPxWidth, resultPxHeight= 0;
	private int resultbandCount = 0;
	private int resultNodataValue = 0;

	private WriterBufferServer writerBufferServer = null;
	private String fileName = null;
	private View view		= null;

	int percent = 0;

	//Variables auxiliares:
	private double distances[] = null;
	private double cellValue = 0;
	private double totalDistance = 0;
	private double resultCellVale[] =null;
	private GeoRasterWriter grw = null;;


	public void init() {

		// Obtener las capas que intervienen en el mosaico.
		inputRasterLayers = (FLyrRasterSE[]) getParam("inputRasterLayers");
		view = (View) getParam("view");
		inputExtents = new Rectangle2D.Double[inputRasterLayers.length];
		for (int i = 0; i<inputRasterLayers.length;i++){
			Envelope env=inputRasterLayers[i].getFullEnvelope();
			inputExtents[i] = new Rectangle2D.Double(env.getMinimum(0), env.getMinimum(1), env.getLength(0), env.getLength(1));
		}

		intersection = new Rectangle2D.Double();
		Rectangle2D.intersect(inputExtents[0], inputExtents[1], intersection);

		Rectangle2D resultExtent = new Rectangle2D.Double();
		Rectangle2D.union(inputExtents[0], inputExtents[1], resultExtent);

		double cellSizeX = inputRasterLayers[0].getAffineTransform().getScaleX();
		double cellSizeY = inputRasterLayers[0].getAffineTransform().getScaleY();

		// TODO: Contemplar el caso de capas con distinto tamaño de celda.
		for (int i = 1; i< inputRasterLayers.length; i++)
		{
			if (Math.abs(cellSizeX) > Math.abs(inputRasterLayers[i].getAffineTransform().getScaleX()))
				cellSizeX = inputRasterLayers[i].getAffineTransform().getScaleX();
			if (Math.abs(cellSizeY) > Math.abs(inputRasterLayers[i].getAffineTransform().getScaleY()))
				cellSizeY = inputRasterLayers[i].getAffineTransform().getScaleY();
		}

		resultGridExtent = new GridExtent(new Extent(resultExtent), cellSizeX,
				cellSizeY);
		resultPxWidth = resultGridExtent.getNX();
		resultPxHeight = resultGridExtent.getNY();

		// Grid Resultante
		try {
			// TODO: Determiar el tipo de dato y número de bandas.
			resultGrid = new Grid(resultGridExtent, resultGridExtent,
					IBuffer.TYPE_BYTE, new int[] { 0, 1, 2 });
			resultGrid.setNoDataValue(0);

			resultbandCount = resultGrid.getBandCount();
			resultCellVale = new double[resultbandCount];
		} catch (RasterBufferInvalidException e) {
			RasterToolsUtil.messageBoxError("buffer_incorrecto", this, e);
		}
		fileName = getStringParam("filename");

	}

	public void process() throws InterruptedException {
		// Calculara las lineas de borde (entre 0, 3 o 4 lineas)
		calculateBorders();
		distances = new double[borders.length];

		// Construccion de los grid de entrada:
		inputGrids = new Grid[inputRasterLayers.length];
		for (int l = 0; l < inputRasterLayers.length; l++) {
			try {
				inputGrids[l] = new Grid(inputRasterLayers[l].getDataSource(), new int[] { 0, 1, 2 }, resultGridExtent);
				inputGrids[l].setInterpolationMethod(GridInterpolated.INTERPOLATION_NearestNeighbour);
			} catch (RasterBufferInvalidException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// Recorrido del grid resultante
		for (int col = 0; col < resultPxWidth; col++){
			percent = col*100/resultPxWidth;
			System.out.println("------------- "+percent+" %"); /////////DEBUG***********
			for (int row = 0; row < resultPxHeight;row++) {
				try {
					setValue(col, row);
				} catch (OutOfGridException e) {
					RasterToolsUtil.messageBoxError(PluginServices.getText(
							this, "bad_access_grid"), this, e);
				}
			}
		}
		createLayer();
		if (externalActions != null)
			externalActions.end(outputRasterLayer);
	}

	private void createLayer() {
		writerBufferServer = new WriterBufferServer(resultGrid.getRasterBuf());
		AffineTransform aTransform = new AffineTransform(resultGridExtent
				.getCellSizeX(), 0.0, 0.0, resultGridExtent.getCellSizeY(),
				resultGridExtent.getMin().getX(), resultGridExtent.getMax()
						.getY());
		try {
			grw = GeoRasterWriter.getWriter(writerBufferServer, fileName,
					resultGrid.getBandCount(), aTransform, resultGridExtent
							.getNX(), resultGridExtent.getNY(), resultGrid
							.getDataType(), GeoRasterWriter.getWriter(fileName)
							.getParams(), null);
			grw.dataWrite();
			grw.getPercent();
			grw.writeClose();

			int endIndex = fileName.lastIndexOf(".");
			if (endIndex < 0)
				endIndex = fileName.length();
			outputRasterLayer = FLyrRasterSE.createLayer(fileName.substring(fileName.lastIndexOf(File.separator) + 1, endIndex),
					fileName, null);
			if(view==null)
				return;
			view.getModel().getMapContext().getLayers().addLayer(outputRasterLayer);
		} catch (NotSupportedExtensionException e) {
			RasterToolsUtil.messageBoxError(PluginServices.getText(this,
					"error_writer_notsupportedextension"), this, e);
		} catch (RasterDriverException e) {
			RasterToolsUtil.messageBoxError(PluginServices.getText(this,
					"raster_buffer_invalid_extension"), this, e);
		} catch (IOException e) {
			RasterToolsUtil.messageBoxError(PluginServices.getText(this,
					"error_writer"), this, e);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		} catch (LoadLayerException e) {
			RasterToolsUtil.messageBoxError("error_cargar_capa", this, e);
		}
	}


	private void setValue(int col, int row) throws OutOfGridException {
		//¿Está el punto en la intersección?
		Point2D  worldCoords = rasterToWorld(col, row);
		if (intersection.contains(worldCoords)){   //////**************************************************
			setFeatherValue(col, row);
			return;
		}

		for (int g = 0; g < inputGrids.length; g++) {
			if (inputExtents[g].contains(worldCoords)){ //////**************************************************
				try {

					for (int band = 0; band < resultbandCount; band++){
						inputGrids[g].setBandToOperate(band);
						resultGrid.setBandToOperate(band);
						cellValue = inputGrids[g].getCellValueAsByte(col, row) & 0xff;
						if(!inputGrids[g].isNoDataValue(cellValue)){
							resultGrid.setCellValue(col, row, (byte)cellValue);
						}
						else{
							resultGrid.setCellValue(col, row,(byte) resultNodataValue);
						}
					}
				} catch (GridException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return;
			}
		}
	}

	private void setFeatherValue(int col, int row) throws OutOfGridException {
		Point2D worldPoint = rasterToWorld(col, row);

		totalDistance = 0;
		double newDistance = 0;

		try {
			for (int i = 0; i<borders.length;i++){

				//Cálculo de la distancia mínima a los bordes de cada imagen
				distances[i]= Double.POSITIVE_INFINITY;
				for(int j = 0; j<borders[0].length;j++)
					if (borders[i][j] != null){
						newDistance = borders[i][j].ptLineDist(worldPoint); ///////*****************************
						if(distances[i]>newDistance)
							distances[i]=newDistance;
					}
				totalDistance = totalDistance + distances[i];

				for (int band = 0; band<resultbandCount;band++){
					inputGrids[i].setBandToOperate(band);
					cellValue = inputGrids[i].getCellValueAsByte(col, row) & 0xff;
					resultCellVale[band] = resultCellVale[band] + cellValue*distances[i];
				}
			}

			//Escribir el resultado de cada banda.
			for (int band = 0; band<resultbandCount;band++){
				resultCellVale[band] = resultCellVale[band]/totalDistance;
				resultGrid.setBandToOperate(band);
				resultGrid.setCellValue(col, row, (byte)resultCellVale[band]);
				resultCellVale[band]=0;
			}
		} catch (GridException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int getPercent() {
		if (grw != null)
			return grw.getPercent();

		return percent;
	}

	public String getTitle() {
		if (percent == 0)
			return PluginServices.getText(this, "iniciando_proceso");

		if (grw != null)
			return PluginServices.getText(this, "escribiendo_resultado");

		return PluginServices.getText(this, "calculando_feather");
	}

	/**
	 * Calcula los bordes que interesan para la obtención de distancias en el
	 * proceso.
	 */
	private void calculateBorders() {

		borders = new Line2D.Double[2][4];

		Line2D.Double border = new Line2D.Double(inputExtents[0].getMinX(), inputExtents[0]
				.getMaxY(), inputExtents[0].getMaxX(), inputExtents[0].getMaxY());
		if (inputExtents[1].intersectsLine(border))
			borders[0][0] = border;
		else
			borders[0][0] = null;

		border = new Line2D.Double(inputExtents[0].getMaxX(), inputExtents[0].getMaxY(),
				inputExtents[0].getMaxX(), inputExtents[0].getMinY());
		if (inputExtents[1].intersectsLine(border))
			borders[0][1] = border;
		else
			borders[0][1] = null;

		border = new Line2D.Double(inputExtents[0].getMaxX(), inputExtents[0].getMinY(),
				inputExtents[0].getMinX(), inputExtents[0].getMinY());
		if (inputExtents[1].intersectsLine(border))
			borders[0][2] = border;
		else
			borders[0][2] = null;

		border = new Line2D.Double(inputExtents[0].getMinX(), inputExtents[0].getMinY(),
				inputExtents[0].getMinX(), inputExtents[0].getMaxY());
		if (inputExtents[1].intersectsLine(border))
			borders[0][3] = border;
		else
			borders[0][3] = null;

		border = new Line2D.Double(inputExtents[1].getMinX(), inputExtents[1].getMaxY(),
				inputExtents[1].getMaxX(), inputExtents[1].getMinY());
		if (inputExtents[0].intersectsLine(border))
			borders[1][0] = border;
		else
			borders[1][0] = null;

		border = new Line2D.Double(inputExtents[1].getMaxX(), inputExtents[1].getMaxY(),
				inputExtents[1].getMaxX(), inputExtents[1].getMinY());
		if (inputExtents[0].intersectsLine(border))
			borders[1][1] = border;
		else
			borders[1][1] = null;

		border = new Line2D.Double(inputExtents[1].getMaxX(), inputExtents[1].getMinY(),
				inputExtents[1].getMinX(), inputExtents[1].getMinY());
		if (inputExtents[0].intersectsLine(border))
			borders[1][2] = border;
		else
			borders[1][2] = null;

		border = new Line2D.Double(inputExtents[1].getMinX(), inputExtents[1].getMinY(),
				inputExtents[1].getMinX(), inputExtents[1].getMaxY());
		if (inputExtents[0].intersectsLine(border))
			borders[1][3] = border;
		else
			borders[1][3] = null;
	}

	private Point2D rasterToWorld (int col, int row){
		double worldX = resultGridExtent.getMin().getX()+(col+0.5)*resultGridExtent.getCellSizeX();
		double worldY = resultGridExtent.getMax().getY()+(row+0.5)*resultGridExtent.getCellSizeY();
		return new Point2D.Double(worldX,worldY);
	}
}
