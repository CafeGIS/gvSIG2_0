/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
*
* Copyright (C) 2005 IVER T.I. and Generalitat Valenciana.
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
package org.gvsig.raster.util.process;

import java.io.IOException;
import java.util.ArrayList;

import javax.swing.SwingUtilities;

import org.gvsig.raster.Configuration;
import org.gvsig.raster.RasterProcess;
import org.gvsig.raster.beans.previewbase.ParamStruct;
import org.gvsig.raster.buffer.BufferFactory;
import org.gvsig.raster.buffer.WriterBufferServer;
import org.gvsig.raster.buffer.cache.RasterReadOnlyBuffer;
import org.gvsig.raster.dataset.GeoRasterWriter;
import org.gvsig.raster.dataset.IBuffer;
import org.gvsig.raster.dataset.IRasterDataSource;
import org.gvsig.raster.dataset.NotSupportedExtensionException;
import org.gvsig.raster.dataset.RasterDataset;
import org.gvsig.raster.dataset.RasterDriverException;
import org.gvsig.raster.dataset.properties.DatasetColorInterpretation;
import org.gvsig.raster.dataset.serializer.RmfSerializerException;
import org.gvsig.raster.datastruct.NoData;
import org.gvsig.raster.datastruct.Transparency;
import org.gvsig.raster.grid.Grid;
import org.gvsig.raster.grid.filter.FilterTypeException;
import org.gvsig.raster.grid.filter.IRasterFilterListManager;
import org.gvsig.raster.grid.filter.RasterFilterList;
import org.gvsig.raster.grid.filter.RasterFilterListManager;
import org.gvsig.raster.hierarchy.IRasterRendering;
import org.gvsig.raster.util.RasterToolsUtil;

import com.iver.andami.PluginServices;
/**
 * Clase donde se hara todo el proceso de aplicar una lista de filtros a una
 * capa. Muestra una ventana de dialogo de incremento informativa.
 *
 * @version 24/05/2007
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
public class FilterProcess extends RasterProcess {
	private String            filename         = "";
	private IRasterDataSource rasterDataSource = null;
	private ArrayList         listFilterUsed   = null;

	private RasterFilterList  rasterFilterList = null;
	private GeoRasterWriter   geoRasterWriter  = null;
	private IRasterRendering  rendering        = null;

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.rastertools.RasterProcess#init()
	 */
	public void init() {
		rendering = (IRasterRendering) getParam("rendering");
		filename = getStringParam("filename");
		rasterDataSource = (IRasterDataSource) getParam("rasterdatasource");
		listFilterUsed = (ArrayList) getParam("listfilterused");
	}

	/**
	 * Devuelve una interpretacion de color. Es usado cuando no conseguimos un
	 * rendering y no podemos saber que interpretacion lógica tenía antes.
	 * @param geoRasterWriter
	 */
	private DatasetColorInterpretation getColorIntepretation(IBuffer buffer, Grid grid) {
		DatasetColorInterpretation colorInterpretation = null;

		do {
			// Si tiene una tabla de color asignamos las tres bandas
			if (grid.getFilterList().isActive("colortable")) {
				colorInterpretation = new DatasetColorInterpretation(new String[] { DatasetColorInterpretation.RED_BAND, DatasetColorInterpretation.GREEN_BAND, DatasetColorInterpretation.BLUE_BAND });
				break;
			}

			// Si el numero de bandas coincide asignamos la misma interpretacion que tenia antes
			if ((rendering != null) && (buffer.getBandCount() == rasterDataSource.getBandCount())) {
				colorInterpretation = rasterDataSource.getColorInterpretation();
				break;
			}

			String[] colorInterp = new String[rasterDataSource.getColorInterpretation().getValues().length];

			for (int i = 0; i < rasterDataSource.getColorInterpretation().getValues().length; i++) {
				if (rasterDataSource.getColorInterpretation().getValues()[i].equals(DatasetColorInterpretation.UNDEF_BAND)) {
					colorInterp[i] = DatasetColorInterpretation.GRAY_BAND;
					continue;
				}
				colorInterp[i] = rasterDataSource.getColorInterpretation().getValues()[i];
			}
			colorInterpretation = new DatasetColorInterpretation(colorInterp);
		} while (false);

		return colorInterpretation;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.rastertools.RasterProcess#process()
	 */
	public void process() throws InterruptedException {
		WriterBufferServer writerBufferServer = null;

		IRasterDataSource dsetCopy = null;
		BufferFactory bufferFactory = null;
		IBuffer buffer = null;
		
		try {
			insertLineLog(RasterToolsUtil.getText(this, "leyendo_raster"));
			
			//Creación del BufferFactory
			dsetCopy = rasterDataSource.newDataset();
			bufferFactory = new BufferFactory(dsetCopy);
			//if (!RasterBuffer.loadInMemory(dsetCopy))
				bufferFactory.setReadOnly(true);

			//Asignación de bandas
			int[] renderBands = rendering.getRenderBands();
			if (rendering != null) {
				// Si es gris, se reduce a una sola banda
				if ((renderBands.length == 3) && (renderBands[0] == renderBands[1]) && (renderBands[1] == renderBands[2])) 
					renderBands = new int[] { renderBands[0] };
				bufferFactory.setDrawableBands(renderBands);
			} else
				bufferFactory.setAllDrawableBands();

			bufferFactory.setAreaOfInterest();
			
			IBuffer buff = bufferFactory.getRasterBuf();
			if(buff instanceof RasterReadOnlyBuffer) 
				((RasterReadOnlyBuffer) buff).addDrawableBands(renderBands);
			
			Grid grid = new Grid(bufferFactory, true);

			//Obtenemos la lista de filtros
			rasterFilterList = grid.getFilterList();
			
			//Aplicamos los filtros usados en el panel
			addSelectedFilters(rasterFilterList, listFilterUsed);
			
			insertLineLog(RasterToolsUtil.getText(this, "aplicando_filtros"));

			grid.setNoDataValue(rasterDataSource.getNoDataValue());
			grid.applyFilters();
			
			insertLineLog(RasterToolsUtil.getText(this, "guardando_capa"));

			buffer = grid.getRasterBuf();

			writerBufferServer = new WriterBufferServer();
			writerBufferServer.setBuffer(buffer, -1);
			// TODO: FUNCIONALIDAD: Poner los getWriter con la proyección del fichero fuente

			//Calculo de la interpretación de color
			DatasetColorInterpretation colorInterpretation = null;
			if((rendering != null && rendering.existColorTable()) || buffer.getBandCount() > 1)
				colorInterpretation = new DatasetColorInterpretation(new String[] { DatasetColorInterpretation.RED_BAND, DatasetColorInterpretation.GREEN_BAND, DatasetColorInterpretation.BLUE_BAND });
			else if(rendering != null && buffer.getBandCount() == 2) {
				renderBands = rendering.getRenderBands();
				String[] ci = new String[renderBands.length];
				for (int i = 0; i < renderBands.length; i++) {
					switch (renderBands[i]) {
					case 0:ci[i] = DatasetColorInterpretation.RED_BAND; break;
					case 1:ci[i] = DatasetColorInterpretation.GREEN_BAND; break;
					case 2:ci[i] = DatasetColorInterpretation.BLUE_BAND; break;
					default: ci[i] = DatasetColorInterpretation.UNDEF_BAND; 
					}
				}
				colorInterpretation = new DatasetColorInterpretation(ci);
			} else
				colorInterpretation = new DatasetColorInterpretation(new String[] { DatasetColorInterpretation.GRAY_BAND });
			
			//Si la imagen original tenía una banda de transparencia se asignará esta. Si los filtros generados
			//crean una banda se mezclará con la original. 
			int nbands = buffer.getBandCount();
			if(rasterDataSource.getTransparencyFilesStatus().getAlphaBandNumber() >= 0) {
				bufferFactory.setDrawableBands(new int[]{rasterDataSource.getTransparencyFilesStatus().getAlphaBandNumber()});
				bufferFactory.setAreaOfInterest();
				IBuffer alpha = bufferFactory.getRasterBuf();
				if(grid.getFilterList().getAlphaBand() != null)
					alpha = Transparency.merge(alpha, grid.getFilterList().getAlphaBand());
				writerBufferServer.setAlphaBuffer(alpha);
				//Asignamos la interpretación de color de la banda alpha				
				DatasetColorInterpretation alphaI = new DatasetColorInterpretation(new String[] { DatasetColorInterpretation.ALPHA_BAND });
				colorInterpretation.addColorInterpretation(alphaI);
				nbands ++;
			} else if(grid.getFilterList().getAlphaBand() != null) {
				writerBufferServer.setAlphaBuffer(grid.getFilterList().getAlphaBand());
				//Asignamos la interpretación de color de la banda alpha
				DatasetColorInterpretation alphaI = new DatasetColorInterpretation(new String[] { DatasetColorInterpretation.ALPHA_BAND });
				colorInterpretation.addColorInterpretation(alphaI);
				nbands ++;
			}
			
			geoRasterWriter = GeoRasterWriter.getWriter(writerBufferServer, filename, nbands, rasterDataSource.getAffineTransform(0), buffer.getWidth(), buffer.getHeight(), buffer.getDataType(), GeoRasterWriter.getWriter(filename).getParams(), null);
			
			if (rendering == null)
				colorInterpretation = getColorIntepretation(buffer, grid);

			geoRasterWriter.setColorBandsInterpretation(colorInterpretation.getValues());

			geoRasterWriter.dataWrite();
			geoRasterWriter.writeClose();
			geoRasterWriter = null;
			
			// Guardamos en el RMF del fichero el valor NoData
			if (Configuration.getValue("nodata_transparency_enabled", Boolean.FALSE).booleanValue()) {
				try {
					RasterDataset.saveObjectToRmfFile(filename, NoData.class, new NoData(rasterDataSource.getNoDataValue(), rasterDataSource.isNoDataEnabled()?2:0, rasterDataSource.getDataType()[0]));
				} catch (RmfSerializerException e) {
					RasterToolsUtil.messageBoxError(PluginServices.getText(this,"error_salvando_rmf"), this, e);
				}
			}
			
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					if (externalActions != null)
						externalActions.end(filename);
				}
			});
		} catch (NotSupportedExtensionException e) {
			RasterToolsUtil.messageBoxError("error_writer_notsupportedextension", this, e);
		} catch (RasterDriverException e) {
			RasterToolsUtil.messageBoxError("error_writer", this, e);
		} catch (IOException e) {
			RasterToolsUtil.messageBoxError("error_writer", this, e);
		} catch (FilterTypeException e) {
			RasterToolsUtil.messageBoxError("error_adding_filters", this, e);
		} finally {
			rasterDataSource = null;
			if(bufferFactory != null)
				bufferFactory.free();
			if(buffer != null)
				buffer.free();
		}
	}

	/**
	 * Sustituye la lista de filtros de filterList por la que le pasamos en forma
	 * de ArrayList
	 * @param filterList
	 * @param listFilterUsed
	 * @throws FilterTypeException 
	 */
	public static void addSelectedFilters(RasterFilterList filterList, ArrayList listFilterUsed) throws FilterTypeException {
		filterList.clear();
		RasterFilterListManager stackManager = new RasterFilterListManager(filterList);

		for (int i = 0; i < listFilterUsed.size(); i++) {
			ParamStruct aux = (ParamStruct) listFilterUsed.get(i);
			IRasterFilterListManager filterManager = stackManager.getManagerByFilterClass(aux.getFilterClass());
			filterManager.addFilter(aux.getFilterClass(), aux.getFilterParam());
		}

		filterList.resetPercent();
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.incrementabletask.IIncrementable#getPercent()
	 */
	public int getPercent() {
		if (rasterFilterList == null)
			return 0;

		if (rasterFilterList.getPercent() < 100)
			return rasterFilterList.getPercent();

		if (geoRasterWriter == null)
			return 0;

		return geoRasterWriter.getPercent();
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.incrementabletask.IIncrementable#getTitle()
	 */
	public String getTitle() {
		return PluginServices.getText(this, "aplicando_filtros");
	}
}