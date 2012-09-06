
/* gvSIG. Sistema de Informaci�n Geogr�fica de la Generalitat Valenciana
 *
 * Copyright (C) 2006 IVER T.I. and Generalitat Valenciana.
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
package org.gvsig.raster.buffer.cache;

import java.io.IOException;

import org.gvsig.raster.dataset.Band;
import org.gvsig.raster.dataset.BandList;
import org.gvsig.raster.dataset.BandNotFoundInListException;
import org.gvsig.raster.dataset.NotSupportedExtensionException;
import org.gvsig.raster.dataset.RasterDataset;
import org.gvsig.raster.dataset.RasterDriverException;
import org.gvsig.raster.datastruct.Extent;


/** 
 * Servidor de datos de cach�. Esta clase es la encargada de recuperar las p�ginas
 * cuando le son solicitadas. Implementar� el interfaz ICacheDataSource, aunque esta
 * clase se encargar� de servir p�ginas de solo lectura. Las p�ginas son servidas directamente desde
 * un RasterDataset y un objeto de este tipo representa a una sola p�gina, por lo que tendr� asociado
 * el extent correspondiente al trozo de p�gina que sirve.
 * 
 * @author Nacho Brodin (nachobrodin@gmail.com)
 *
 */
public class CacheDataFromDriverServer implements ICacheDataSource{
	
	private RasterDataset			dataset = null;
	private Extent					pageExtent = null; 
	
	/**
	 * Constructor. 
	 * Crea el identificador para todos los trozos de cach� que se guardar�n en disco. 
	 * @param id Identificador de fichero. Si este es null se calcula uno autom�ticamente
	 * @param numBand N�mero de banda
	 * @param numPag N�mero de p�gina
	 * @throws RasterDriverException 
	 * @throws NotSupportedExtensionException 
	 */
	public CacheDataFromDriverServer(RasterDataset dataset, int numPag, Extent extent) {
		this.dataset = dataset;
		pageExtent = extent;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dataaccess.cache.ICacheDataSource#loadPage(int, org.gvsig.fmap.dataaccess.cache.PageBuffer)
	 */
	public void loadPage(PageBandBuffer pageBuffer) throws InterruptedException {
		//Creamos un BandList con todas las bandas del fichero
		BandList bandList = new BandList();
		for(int i = 0; i < dataset.getBandCount();i++) {
			try {
				Band band = new Band(dataset.getFName(), i, dataset.getDataType()[i]);
				bandList.addBand(band, i);
			} catch(BandNotFoundInListException ex) {
				//No a�adimos la banda
			}
		}
		try {
			dataset.getWindowRaster(pageExtent.getMin().getX(), pageExtent.getMax().getY(), pageExtent.width(), pageExtent.height(), bandList, pageBuffer);
		} catch (RasterDriverException e) {
		}
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dataaccess.cache.ICacheDataSource#savePage(int, org.gvsig.fmap.dataaccess.cache.PageBuffer)
	 */
	public void savePage(PageBandBuffer pageBuffer) throws IOException {
		//Read Only		
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataaccess.cache.ICacheDataSource#delete()
	 */
	public void delete() {
		//Read Only
	}
	
	/*
	 *  (non-Javadoc)
	 * @see org.gvsig.raster.dataaccess.cache.ICacheDataSource#getPath()
	 */
	public String getPath() {
		if(dataset != null)
			return dataset.getFName();
		return null;
	}
}

