/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
*
* Copyright (C) 2007 IVER T.I. and Generalitat Valenciana.
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
*/
package org.gvsig.rastertools.saveraster.operations;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.fmap.geom.Geometry.SUBTYPES;
import org.gvsig.fmap.geom.exception.CreateEnvelopeException;
import org.gvsig.fmap.geom.primitive.Envelope;
import org.gvsig.fmap.mapcontext.ViewPort;
import org.gvsig.fmap.mapcontext.impl.DefaultMapContextDrawer;
import org.gvsig.fmap.mapcontext.layers.FLayers;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.gui.beans.incrementabletask.IIncrementable;
import org.gvsig.raster.dataset.IBuffer;
import org.gvsig.raster.dataset.IDataWriter;
import org.gvsig.raster.util.RasterToolsUtil;
import org.gvsig.tools.task.Cancellable;

import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
/**
 * Sirve datos solicitados por los drivers que salvan a raster. Hereda de
 * Rasterizer y reescribe el método readData que es el que será llamado desde el
 * driver cada vez que vacie el buffer y necesite más datos.
 *
 * @version 04/06/2007
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class RasterizerLayer implements IDataWriter, IIncrementable {
	private static final GeometryManager 	geomManager		= GeometryLocator.getGeometryManager();
	private ViewPort						viewPort 		= null;
	private ViewPort						viewPortBlock	= null;
	private FLayers							flayers 		= null;
	private Color							backgroundColor = null;
	private boolean							firstRead 		= true;
	private int 							nBlocks 		= 0;
//	private double 							percentMax 		= 100.0D;
	protected double 						wcIntervalo 	= 0;
	protected Dimension 					dimension 		= null;
	protected int 							blockSize 		= 0;
	protected double 						wcAlto 			= 0;
	protected int 							lastBlock 		= 0;
	protected BufferedImage 				image 			= null;
	protected int[] 						rasterData 		= null;
	protected int 							contBlocks 		= 1;
	protected int							percent 		= 0;
	protected int							imgHeight 		= 0;

	/**
	 * Calculo del viewPort
	 * @param vp
	 */
	private void calcViewPort(ViewPort vp) {
		Rectangle2D ext = null;

		if (viewPortBlock == null)
			ext = new Rectangle2D.Double(	vp.getExtent().getMinX(),
											vp.getExtent().getMaxY() - wcIntervalo,
											vp.getExtent().getWidth(),
											wcIntervalo
										);
		else
			ext = new Rectangle2D.Double(	viewPortBlock.getExtent().getMinX(),
											viewPortBlock.getExtent().getMinY() - wcIntervalo,
											viewPortBlock.getExtent().getWidth(),
											wcIntervalo
										);

		viewPortBlock = new ViewPort(vp.getProjection());
		Envelope env;
		try {
			env = geomManager.createEnvelope(ext.getMinX(), ext.getMinY(), ext.getMaxX(), ext.getMaxY(), SUBTYPES.GEOM2D);
			viewPortBlock.setEnvelope(env);
		} catch (CreateEnvelopeException e) {
			RasterToolsUtil.debug("Error creating the envelope", null, e);
		}		
		viewPortBlock.setImageSize(dimension);
		viewPortBlock.refreshExtent();
	}

	/**
	 * Constructor
	 * @param flyrs capas
	 * @param vp viewport
	 * @param blockSize altura del bloque que se lee de una vez en la imagen de entrada
	 * @param mapCtrl Mapcontrol
	 */
	public RasterizerLayer(FLayers flyrs, ViewPort vp, int blockSize) {
		this.blockSize = blockSize;
		backgroundColor = vp.getBackColor();
		viewPort = new ViewPort(vp.getProjection());
		viewPort.setImageSize(vp.getImageSize());
		Rectangle2D ex = vp.getExtent();
		Envelope env = null;
		try {
			env = geomManager.createEnvelope(ex.getMinX(), ex.getMinY(), ex.getMaxX(), ex.getMaxY(), SUBTYPES.GEOM2D);
			//viewPortBlock = new ViewPort(vp.getProjection());
			//viewPortBlock.setEnvelope(env);
		} catch (CreateEnvelopeException e) {
			RasterToolsUtil.debug("Error creating the envelope", null, e);
		}
		

		// Calculo del viewPort del primer bloque
		viewPort.setEnvelope(vp.getAdjustedExtent());
		wcAlto = viewPort.getExtent().getMaxY() - viewPort.getExtent().getMinY();
		wcIntervalo = (blockSize * wcAlto) / viewPort.getImageHeight();
		dimension = new Dimension(viewPort.getImageWidth(), blockSize);

		imgHeight = vp.getImageHeight();
		nBlocks = (vp.getImageHeight() / blockSize);

		// Tamaño de último bloque en pixeles
		lastBlock = vp.getImageHeight() - (nBlocks * blockSize);

		calcViewPort(viewPort);

		this.flayers = flyrs;
	}

	/**
	 * Compatibilidad con el piloto de raster
	 * @see readData
	 */
	public int[] readARGBData(int sX, int sY, int nBand) throws InterruptedException, OutOfMemoryError {
		return readData( sX, sY, nBand);
	}

	public int[] readData(int sX, int sY, int nBand) throws InterruptedException, OutOfMemoryError {
		if (nBand == 0) { // Con nBand==0 se devuelven las 3 bandas
			nBlocks = (int) Math.ceil(imgHeight / (double) blockSize);
			image = new BufferedImage(sX, sY, BufferedImage.TYPE_INT_RGB);
			Graphics2D g = (Graphics2D) image.getGraphics();
			g.setColor(backgroundColor);
			g.fillRect(0, 0, viewPortBlock.getImageWidth(), viewPortBlock.getImageHeight());
			try {
				// TODO: FUNCIONALIDAD: Salvar los máximos y mínimos para salvar 16 bits

				// Si es la primera lectura salvamos los valores de máximo y mínimo para la aplicación
				// de realce si la imagen es de 16 bits.
				if (firstRead) {
					for (int i = 0; i < flayers.getLayersCount(); i++)
						if (flayers.getLayer(i) instanceof FLyrRasterSE) {
							FLyrRasterSE raster = (FLyrRasterSE) flayers.getLayer(i);
							if (raster.getDataType()[0] == IBuffer.TYPE_SHORT || raster.getDataType()[0] == IBuffer.TYPE_USHORT) {
								//Statistic stats = raster.getSource().getFilterStack().getStats();
								//stats.history.add(stats.new History(raster.getName(), stats.minBandValue, stats.maxBandValue, stats.secondMinBandValue, stats.secondMaxBandValue));
							}
						}
					firstRead = false;
				}

				DefaultMapContextDrawer mapContextDrawer = new DefaultMapContextDrawer();
				mapContextDrawer.setMapContext(flayers.getMapContext());
				mapContextDrawer.setViewPort(viewPortBlock);
				mapContextDrawer.draw(flayers, image, g, new Cancellable(){
					public boolean isCanceled() {
						return false;
					}

					public void setCanceled(boolean canceled) {
					}
				}, flayers.getMapContext().getScaleView());

				// Si es el último bloque vaciamos el historial de máximos y mínimos
				if ((contBlocks + 1) == nBlocks)
					for (int i = 0; i < flayers.getLayersCount(); i++)
						if (flayers.getLayer(i) instanceof FLyrRasterSE) {
							FLyrRasterSE raster = (FLyrRasterSE) flayers.getLayer(i);
							if (raster.getDataType()[0] == IBuffer.TYPE_SHORT || raster.getDataType()[0] == IBuffer.TYPE_USHORT) {
								//raster.getDatasource().getFilterStack().getStats().history.clear();
								//Statistic stats = raster.getSource().getFilterStack().getStats();
							}
						}

			} catch (ReadException e) {
				NotificationManager.addError("Error en el draw de capa", e);
			}
			rasterData = image.getRGB(0, 0, sX, sY, rasterData, 0, sX);

			// Calculamos el viewPort del sgte bloque

			if (((contBlocks + 1) * blockSize) <= viewPort.getImageHeight())
				dimension = new Dimension(sX, sY);
			else { // Calculo de la altura del último bloque
				dimension = new Dimension(sX, (viewPort.getImageHeight() - (contBlocks * blockSize)));
				wcIntervalo = (lastBlock * wcAlto) / viewPort.getImageHeight();
			}

			calcViewPort(viewPortBlock);

			percent = ((100 * (contBlocks)) / nBlocks);
			contBlocks++;

			return rasterData;
		}

		return null;
	}

	/**
	 * Asigna el ancho del bloque
	 * @param sizeBlock Ancho del bloque en pixeles
	 */
	public void setBlockSize(int blockSize) {
		this.blockSize = blockSize;
	}

	/**
	 * No tiene uso en RasterizerLayer
	 */
	public byte[][] readByteData(int sizeX, int sizeY) {
		return null;
	}

	/**
	 * No tiene uso en RasterizerLayer
	 */
	public double[][] readDoubleData(int sizeX, int sizeY) {
		return null;
	}

	/**
	 * No tiene uso en RasterizerLayer
	 */
	public float[][] readFloatData(int sizeX, int sizeY) {
		return null;
	}

	/**
	 * No tiene uso en RasterizerLayer
	 */
	public int[][] readIntData(int sizeX, int sizeY) {
		return null;
	}

	/**
	 * No tiene uso en RasterizerLayer
	 */
	public short[][] readShortData(int sizeX, int sizeY) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.incrementabletask.IIncrementable#getTitle()
	 */
	public String getTitle() {
		return PluginServices.getText(this, "salvando_raster");
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.incrementabletask.IIncrementable#getLog()
	 */
	public String getLog() {
		return PluginServices.getText(this, "salvando_bloque") + " " + Math.min(nBlocks, contBlocks) + " " + PluginServices.getText(this, "de") + " " + nBlocks;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.incrementabletask.IIncrementable#getLabel()
	 */
	public String getLabel() {
		return PluginServices.getText(this, "rasterizando") + "...";
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.incrementabletask.IIncrementable#getPercent()
	 */
	public int getPercent() {
		return percent;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.incrementabletask.IIncrementable#isCancelable()
	 */
	public boolean isCancelable() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.incrementabletask.IIncrementable#isPausable()
	 */
	public boolean isPausable() {
		return false;
	}
}