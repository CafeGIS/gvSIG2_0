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
import java.awt.image.DataBuffer;
import java.io.IOException;

import org.gvsig.fmap.geom.primitive.Envelope;
import org.gvsig.fmap.mapcontext.exceptions.LoadLayerException;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.raster.RasterProcess;
import org.gvsig.raster.buffer.BufferFactory;
import org.gvsig.raster.buffer.BufferInterpolation;
import org.gvsig.raster.buffer.RasterBuffer;
import org.gvsig.raster.buffer.RasterBufferInvalidException;
import org.gvsig.raster.buffer.WriterBufferServer;
import org.gvsig.raster.dataset.FileNotOpenException;
import org.gvsig.raster.dataset.GeoRasterWriter;
import org.gvsig.raster.dataset.IBuffer;
import org.gvsig.raster.dataset.IRasterDataSource;
import org.gvsig.raster.dataset.InvalidSetViewException;
import org.gvsig.raster.dataset.NotSupportedExtensionException;
import org.gvsig.raster.dataset.RasterDriverException;
import org.gvsig.raster.datastruct.Extent;
import org.gvsig.raster.grid.Grid;
import org.gvsig.raster.grid.GridExtent;
import org.gvsig.raster.grid.OutOfGridException;
import org.gvsig.raster.grid.filter.RasterFilter;
import org.gvsig.raster.grid.filter.enhancement.EnhancementStretchListManager;
import org.gvsig.raster.grid.filter.enhancement.LinearStretchParams;
import org.gvsig.raster.util.RasterToolsUtil;
import org.gvsig.remotesensing.RemoteSensingUtils;

import com.iver.andami.PluginServices;

/**
 * Proceso para la obtención de una imagen mosaico a partir de un conjunto de
 * imágenes por el metodo de degradado (Feathering).
 *
 * @params
 * <LI>FLyrRasterSE[] "inputRasterLayers": Capas raster de entrada</LI>
 * <LI>String "outputPath": Ruta completa al fichero de salida del proceso</LI>
 *
 * @result
 * <LI>FLyrRasterSE: Capa raster con el resultado del mosaico</LI>
 *
 * @author Diego Guerrero Sevilla (diego.guerrero@uclm.es)
 *
 */
public class FeatherProcessBuff extends RasterProcess {

	private FLyrRasterSE inputRasterLayers[] = null;
	private IBuffer inputBuffers[] = null;

	private FLyrRasterSE outputRasterLayer = null;

	private Line2D borders[][] = null;

	private Rectangle2D inputExtents[] = null;
	private Rectangle2D intersection = null;

	private Grid resultGrid = null;
	private GridExtent resultGridExtent = null;
	private int resultPxWidth, resultPxHeight= 0;
	private int resultbandCount = 0;

	private WriterBufferServer writerBufferServer = null;
	private String fileName = null;
	int percent = 0;

	//Variables auxiliares:
	private double distances[] = null;
	private byte values[] = null;
	double value = 0;
	private double totalD = 0;
	private int bandCount = 0;
	private double noDataValue = 0;
	private double result[] = null;

	private GeoRasterWriter grw;

	public void init() {

		insertLineLog(PluginServices.getText(this, "preparando_entrada")+"...");
		// Obtener las capas que intervienen en el mosaico.
		inputRasterLayers = (FLyrRasterSE[]) getParam("inputRasterLayers");

		inputExtents = new Rectangle2D.Double[inputRasterLayers.length];
		for (int i = 0; i < inputRasterLayers.length; i++){
			Envelope env=inputRasterLayers[i].getFullEnvelope();
			inputExtents[i] = new Rectangle2D.Double(env.getMinimum(0), env.getMinimum(1), env.getLength(0), env.getLength(1));
		}
		intersection = new Rectangle2D.Double();
		Rectangle2D.intersect(inputExtents[0], inputExtents[1], intersection);

		Rectangle2D resultExtent = new Rectangle2D.Double();
		Rectangle2D.union(inputExtents[0], inputExtents[1], resultExtent);

		/*
		// TODO: Contemplar el caso de capas con distinto tamaño de celda.
		double cellSizeX = inputRasterLayers[0].getAffineTransform()
				.getScaleX();
		double cellSizeY = inputRasterLayers[0].getAffineTransform()
				.getScaleY();
		*/
		double cellSizeX = getMinCellSize()[0];
		double cellSizeY = getMinCellSize()[1];

		resultGridExtent = new GridExtent(new Extent(resultExtent), cellSizeX,
				cellSizeY);

		resultPxWidth = resultGridExtent.getNX();
		resultPxHeight = resultGridExtent.getNY();

		// Grid Resultante
		try {
			resultGrid = new Grid(resultGridExtent, resultGridExtent,
					IBuffer.TYPE_BYTE, new int[] { 0, 1, 2 });
			//resultGrid.setNoDataValue(0);
			resultbandCount = resultGrid.getBandCount();
		} catch (RasterBufferInvalidException e) {
			RasterToolsUtil.messageBoxError("error_datos_entrada", this, e);
		}
		fileName = getStringParam("outputPath");
		values = new byte[resultbandCount];
		result = new double[resultbandCount];

	}

	private double[] getMinCellSize() {
		double minCellSize[] = new double[2];
		minCellSize[0]=Double.MAX_VALUE;
		minCellSize[1]=Double.MAX_VALUE;
		for (int i = 0; i< inputRasterLayers.length; i++) {
			if(Math.abs(inputRasterLayers[i].getAffineTransform().getScaleX())<Math.abs(minCellSize[0]))
				minCellSize[0]=inputRasterLayers[i].getAffineTransform().getScaleX();
			if(Math.abs(inputRasterLayers[i].getAffineTransform().getScaleY())<Math.abs(minCellSize[1]))
				minCellSize[1]=inputRasterLayers[i].getAffineTransform().getScaleY();
		}
		return minCellSize;
	}

	public void process() throws InterruptedException {
		// Calcular las lineas de borde (entre 0, 3 o 4 lineas)
		calculateBorders();
		distances = new double[borders.length];

		// Construccion de los buffers de entrada:
		IRasterDataSource dsetCopy = null;

		inputBuffers = new IBuffer[inputRasterLayers.length];
		for (int l = 0; l < inputRasterLayers.length; l++) {

			dsetCopy = inputRasterLayers[l].getDataSource().newDataset();
			BufferFactory bufferFactory = new BufferFactory(dsetCopy);
			GridExtent gridExtent = new GridExtent(resultGridExtent.getMin().getX(),
					resultGridExtent.getMin().getY(),
					resultGridExtent.getMax().getX(),
					resultGridExtent.getMax().getY(),
					 inputRasterLayers[l].getAffineTransform().getScaleX(),
					 inputRasterLayers[l].getAffineTransform().getScaleY());
			try {
				bufferFactory.setAdjustToExtent(false);
				//bufferFactory.setNoDataToFill(resultGrid.getNoDataValue());
				bufferFactory.setNoDataToFill(inputRasterLayers[l].getNoDataValue());
				bufferFactory.setReadOnly(true);
				bufferFactory.setDrawableBands(inputRasterLayers[l].getRenderBands());
				/*bufferFactory.setAreaOfInterest(resultGridExtent.getMin().getX(), resultGridExtent.getMax().getY(),
						resultGridExtent.getMax().getX(), resultGridExtent.getMin().getY(), resultGridExtent.getNX(),resultGridExtent.getNY());*/
				bufferFactory.setAreaOfInterest(gridExtent.getMin().getX(), gridExtent.getMax().getY(),
						gridExtent.getMax().getX(), gridExtent.getMin().getY(), gridExtent.getNX(),gridExtent.getNY());

			} catch (InvalidSetViewException e) {
				RasterToolsUtil.messageBoxError(PluginServices.getText(this, "error_datos_entrada"), this, e);
			} catch (RasterDriverException e) {
				RasterToolsUtil.messageBoxError(PluginServices.getText(this, "error_datos_entrada"), this, e);
			}

			//Obtenere el burffer interpolado:
			inputBuffers[l] = bufferFactory.getRasterBuf();
			inputBuffers[l] = ((RasterBuffer)inputBuffers[l]).getAdjustedWindow(resultGridExtent.getNX(), resultGridExtent.getNY(), BufferInterpolation.INTERPOLATION_Bilinear);

			//Aplicar filtro de realce si es necesario:

			if(inputBuffers[l].getDataType()!=DataBuffer.TYPE_BYTE){
				LinearStretchParams leParams = null;
				try {
					leParams = LinearStretchParams.createStandardParam(inputRasterLayers[l].getRenderBands(), 0.0, bufferFactory.getDataSource().getStatistics(), false);
				} catch (FileNotOpenException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (RasterDriverException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				RasterFilter linearStretchEnhancementFilter = EnhancementStretchListManager.createEnhancedFilter(leParams, bufferFactory.getDataSource().getStatistics(),
						inputRasterLayers[l].getRenderBands(), false);
				linearStretchEnhancementFilter.addParam("raster", inputBuffers[l]);
				linearStretchEnhancementFilter.execute();

				inputBuffers[l] = (IBuffer)linearStretchEnhancementFilter.getResult("raster");
			}
		}

		// Recorrido del grid resultante
		insertLineLog(PluginServices.getText(this, "realizando_degradado")+"...");
		for (int col = 0; col < resultPxWidth; col++) {
			percent = col*100/resultPxWidth;
			for (int row = 0; row < resultPxHeight; row++) {
				try {
					setValue(col, row);
				} catch (OutOfGridException e) {
					RasterToolsUtil.messageBoxError(PluginServices.getText(
							this, "bad_access_grid"), this, e);
				}
			}
		}

		// Se liberan los buffers
		for(int i=0; i<inputRasterLayers.length;i++)
			inputBuffers[i].free();

		createLayer();
		if (externalActions != null)
			externalActions.end(outputRasterLayer);
	}

	private void createLayer() {
		insertLineLog(PluginServices.getText(this, "escribiendo_resultado")+"...");

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
							.getParams(), inputRasterLayers[0].getProjection());
			grw.dataWrite();
			grw.writeClose();
			resultGrid.getRasterBuf().free();
			outputRasterLayer = FLyrRasterSE.createLayer("outputLayer",
					fileName, inputRasterLayers[0].getProjection());

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
		// ¿Está el punto en la intersección?
		Point2D worldCoords = rasterToWorld(col, row);
		if (intersection.contains(worldCoords)) {
			setFeatherValue(col, row);
			return;
		}
		byte value = 0;
		for (int g = 0; g < inputBuffers.length; g++) {
			if (inputExtents[g].contains(worldCoords)){
				for (int band = 0; band < inputBuffers[g].getBandCount(); band++) {
					value = (byte)RemoteSensingUtils.getCellValueInLayerCoords(inputBuffers[g], col, row, band);
					resultGrid.setBandToOperate(band);
					if ((byte)inputBuffers[g].getNoDataValue() != value) {
						resultGrid.setCellValue(col, row, value);
					} else {
						resultGrid.setCellValue(col, row, (byte) resultGrid
								.getNoDataValue());
					}
				}
				return;
			}
		}
	}

	private void setFeatherValue(int col, int row) throws OutOfGridException {
		Point2D worldPoint = rasterToWorld(col, row);

		totalD = 0;

		for (int i = 0; i < borders.length; i++) {

			// Cálculo de la distancia mínima a los bordes de cada imagen
			distances[i] = Double.POSITIVE_INFINITY;
			for (int j = 0; j < borders[0].length; j++)
				if (borders[i][j] != null) {
					double newDistance = borders[i][j].ptLineDist(worldPoint);
					if (distances[i] > newDistance)
						distances[i] = newDistance;
				}
			totalD = totalD + distances[i];

			value = 0;
			bandCount = inputBuffers[i].getBandCount();
			noDataValue = (byte)inputBuffers[i].getNoDataValue();
			for (int band = 0; band < resultbandCount; band++) {
					if (band < bandCount){
						value = RemoteSensingUtils.getCellValueInLayerCoords(inputBuffers[i], col,row, band);
						if (noDataValue!=value)
							result[band] = result[band] + value * distances[i];
					}
			}
		}

		// Escribir el resultado de cada banda.
		for (int band = 0; band < resultbandCount; band++) {
			result[band] = result[band] / totalD;
			resultGrid.setBandToOperate(band);
			resultGrid.setCellValue(col, row, (byte) result[band]);
			result[band] = 0;
		}
	}

	public Object getResult() {
		return outputRasterLayer;
	}

	public int getPercent() {
		if (grw != null)
			return grw.getPercent();

		return percent;
	}

	public String getTitle() {
		return PluginServices.getText(this, "calculando_feather");
	}

	/**
	 * Calcula los bordes que interesan para la obtención de distancias en el
	 * proceso.
	 */
	private void calculateBorders() {

		borders = new Line2D.Double[2][4];

		Line2D.Double border = new Line2D.Double(inputExtents[0].getMinX(),
				inputExtents[0].getMaxY(), inputExtents[0].getMaxX(),
				inputExtents[0].getMaxY());
		if (inputExtents[1].intersectsLine(border))
			borders[0][0] = border;
		else
			borders[0][0] = null;

		border = new Line2D.Double(inputExtents[0].getMaxX(), inputExtents[0]
				.getMaxY(), inputExtents[0].getMaxX(), inputExtents[0]
				.getMinY());
		if (inputExtents[1].intersectsLine(border))
			borders[0][1] = border;
		else
			borders[0][1] = null;

		border = new Line2D.Double(inputExtents[0].getMaxX(), inputExtents[0]
				.getMinY(), inputExtents[0].getMinX(), inputExtents[0]
				.getMinY());
		if (inputExtents[1].intersectsLine(border))
			borders[0][2] = border;
		else
			borders[0][2] = null;

		border = new Line2D.Double(inputExtents[0].getMinX(), inputExtents[0]
				.getMinY(), inputExtents[0].getMinX(), inputExtents[0]
				.getMaxY());
		if (inputExtents[1].intersectsLine(border))
			borders[0][3] = border;
		else
			borders[0][3] = null;

		border = new Line2D.Double(inputExtents[1].getMinX(), inputExtents[1]
				.getMaxY(), inputExtents[1].getMaxX(), inputExtents[1]
				.getMinY());
		if (inputExtents[0].intersectsLine(border))
			borders[1][0] = border;
		else
			borders[1][0] = null;

		border = new Line2D.Double(inputExtents[1].getMaxX(), inputExtents[1]
				.getMaxY(), inputExtents[1].getMaxX(), inputExtents[1]
				.getMinY());
		if (inputExtents[0].intersectsLine(border))
			borders[1][1] = border;
		else
			borders[1][1] = null;

		border = new Line2D.Double(inputExtents[1].getMaxX(), inputExtents[1]
				.getMinY(), inputExtents[1].getMinX(), inputExtents[1]
				.getMinY());
		if (inputExtents[0].intersectsLine(border))
			borders[1][2] = border;
		else
			borders[1][2] = null;

		border = new Line2D.Double(inputExtents[1].getMinX(), inputExtents[1]
				.getMinY(), inputExtents[1].getMinX(), inputExtents[1]
				.getMaxY());
		if (inputExtents[0].intersectsLine(border))
			borders[1][3] = border;
		else
			borders[1][3] = null;
	}

	private Point2D rasterToWorld(int col, int row) {
		double worldX = resultGridExtent.getMin().getX() + (col + 0.5)
				* resultGridExtent.getCellSizeX();
		double worldY = resultGridExtent.getMax().getY() + (row + 0.5)
				* resultGridExtent.getCellSizeY();
		return new Point2D.Double(worldX, worldY);
	}
}
