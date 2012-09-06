
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

import java.io.FileNotFoundException;
import java.io.IOException;

import org.gvsig.raster.dataset.NotSupportedExtensionException;
import org.gvsig.raster.dataset.RasterDataset;
import org.gvsig.raster.dataset.RasterDriverException;
import org.gvsig.raster.datastruct.Extent;


/**
 * Esta clase representa una p�gina de disco. Una p�gina de disco tendr� un n�mero de
 * p�gina asociado y un servidor de datos de disco por cada banda. 
 * 
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class HddPage {
	/**
	 * Objeto que guarda y recupera trozos de cache de disco.
	 */
	private ICacheDataSource[] dataSource = null;
	//private int nPag = 0;
	
	/**
	 * Constructor: crea un objeto dataSource.
	 * @param nPag N�mero de p�gina
	 * @param nBands N�mero de bandas del dataset
	 * @throws RasterDriverException 
	 * @throws NotSupportedExtensionException 
	 * @throws FileNotFoundException 
	 */
	public HddPage(int nPag, RasterDataset dataset, Extent extent) throws FileNotFoundException, NotSupportedExtensionException, RasterDriverException{
		//this.nPag = nPag;
		dataSource = new CacheDataFromDriverServer[1];
		dataSource[0] = new CacheDataFromDriverServer(dataset, nPag, extent);	
	}
	
	/**
	 * Limpia los trozos de cach� en disco. Despu�s del llamar a este 
	 * m�todo no puede volver a usarse esta cach�.
	 * @throws IOException 
	 */
	public void deletePage(int nBands) throws IOException {
		for (int i = 0; i < nBands; i++)
			deleteBand(i);
	}
		
	/**
	 * Constructor: crea un objeto dataSource para cada banda. Para todos los datasource de un 
	 * mismo HddPage se usa el mismo identificador.
	 * @param nPag N�mero de p�gina
	 * @param nBands N�mero de bandas del dataset
	 */
	public HddPage(int nPag, int nBands){
		//this.nPag = nPag;
		dataSource = new CacheDataServer[nBands];
		for (int iBand = 0; iBand < nBands; iBand++)
			dataSource[iBand] = new CacheDataServer(Long.toString(System.currentTimeMillis()), iBand, nPag);	
	}
	
	/**
	 * Obtiene el servidor de datos de disco para una banda de la p�gina de
	 * disco actual
	 * @param nBand N�mero de banda
	 * @return Servidor de datos de disco
	 */
	public ICacheDataSource getDataServer(int nBand){
		if(nBand >= 0 && nBand < dataSource.length)
			return dataSource[nBand];
		return null;
	}
	
	/**
	 * Elimina una banda de la cach�. Antes salva a disco todos los trozos cacheados e inicializa
	 * las estructuras de control de cach�. 
	 * @param nBand N�mero de banda a eliminar
	 * @throws IOException 
	 */
	public void deleteBand(int nBand) throws IOException{
		if(nBand >= 0 && nBand < dataSource.length)
			dataSource[nBand].delete();
	}
	
	/**
	 * Asigna una banda de disco a todas las p�ginas.
	 * @param cacheDataSource Fuente de las p�ginas. Es la referencia a disco de ellas.
	 */
	public void assignBand(int nBand, ICacheDataSource cacheDataSource){
		if(nBand >= 0 && nBand < dataSource.length)
			dataSource[nBand] = cacheDataSource;
	}
	
	/**
	 * Obtiene la fuente de datos que corresponde a una banda.
	 * @param nBand Banda a recuperar
	 * @return ICacheDataSource 
	 */
	public ICacheDataSource getBandDataSource(int nBand){
		return dataSource[nBand];
	}
}
