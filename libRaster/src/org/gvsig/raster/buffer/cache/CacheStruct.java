
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
package org.gvsig.raster.buffer.cache;

import org.gvsig.raster.RasterLibrary;
import org.gvsig.raster.dataset.IBuffer;

/**
 * Esta clase representa la estructura en memoria de la cache. Contiene el número de páginas 
 * así como los subconjuntos en las que se divide. Tendrá también datos como en ancho y alto de
 * cada página y tamaño en memoria de esta. 
 * 
 * @author Nacho Brodin (nachobrodin@gmail.com)
 *
 */
public class CacheStruct {
	//******************* PARAMETERS ***************************
	/**
	 * Tamaño aproximado de caché en Megas. Si este valor es alto cabrán muchas páginas en memoria 
	 * a la vez y si es bajo cabrán pocas. Hay que tener en cuenta que al instanciar se convertira en bytes
	 * para su mejor tratamiento. Al llamar al constructor esta variable contendrá el tamaño exacto
	 * de la cache en bytes. El tamaño aquí especificado es aproximado. Este variará dependiendo de los
	 * parámetros del raster a cachear ya que las páginas deben tener una altura potencia de 2.
	 */
	private long cacheSize = RasterLibrary.cacheSize;
	/**
	 * Tamaño máximo de la página en Megas. Hay que tener en cuenta que al instanciar se convertira en bytes
	 * para su mejor tratamiento. Al llamar al constructor esta variable contendrá el tamaño exacto
	 * de la página en bytes
	 */
	private double pageSize = RasterLibrary.pageSize;
	/**
	 * Número de páginas que tiene cada conjunto
	 */
	private int pagsPerGroup = RasterLibrary.pagsPerGroup;
	//****************** END PARAMETERS ************************
		
	private int[] possibleHeights = {2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048, 4096, 8192, 16384, 32768, 65536};
		
	/**
	 * Altura de la página de cada banda en líneas.
	 */
	private int hPag = 1;
	/**
	 * Para extraer el desplazamiento de una dirección (línea de raster) hay que hacer una operación And con 
	 * con la altura de la página -1. Por ejemplo, una página de 16 líneas de altura el desplazamiento será
	 * 16 - 1 = 15 porque 15 en binario es 1111.
	 * 
	 * Si queremos acceder a la linea del raster número 83 (1010011) realizando la operación And con el valor del
	 * desplazamiento obtenemos (0001111 & 1010011 = 0000011), es decir el valor 3 en decimal. Esto quiere decir
	 * que la línea 83 del raster es la 3 de su página. 
	 */
	private int offset = 1;
	/**
	 * Número de páginas en caché
	 */
	private int nPags = 0;
	/**
	 * Número de estructuras PageBandBuffer que tendrá el objeto PageBuffer. Cada una de estas
	 * corresponde a una banda del buffer cacheada.
	 */
	private int nPageBandBuffers = 0;
	/**
	 * Número de grupos
	 */
	private int nGroups = 0;
	/**
	 * Número de bits para el desplazamiento de una página. Por ejemplo, una página de 64 líneas
	 * tendrá un bitsPag = 6
	 */
	private int bitsPag = 0;
	/**
	 * Número total de páginas en las que se divide el raster
	 */
	private int nTotalPags = 0;
	/**
	 * Tamaño de cache por banda del raster (cacheSize / NBandas)
	 */
	private long cacheSizePerBand = 0;
	/**
	 * Tamaño de página por banda del raster (pagSize / NBandas)
	 */
	private long pageSizePerBand = 0;
	/**
	 * Tipo de dato de la caché
	 */
	private int	dataType = 0;
	
	/**
	 * Calcula los parámetros del tamaño de caché y de página, asi como la altura de las mismas y el número
	 * de grupos que salen en caché.
	 * @param nBands Número de bandas del raster
	 * @param dataType Tipo de dato de la imagen
	 * @param dataSourceWidth ancho de cada línea completa del raster.
	 */
	public CacheStruct(int nBands, int dataType, int dataSourceWidth, int dataSourceHeight){
		init(nBands, dataType, dataSourceWidth, dataSourceHeight);
	}
	
	/**
	 * Constructor de una sola banda y sin el calculo de parámetros. Estos deben ser asignados
	 * posteriorimente. Esta llamada es usada en casos especiales de creación de buffers cacheados.
	 */
	public CacheStruct() {
	}
	
	/**
	 * Constructor solo para test. No utilizar con un uso normal de la librería
	 * Calcula los parámetros del tamaño de caché y de página, asi como la altura de las mismas y el número
	 * de grupos que salen en caché.
	 * @param nBands Número de bandas del raster
	 * @param dataType Tipo de dato de la imagen
	 * @param dataSourceWidth ancho de cada línea completa del raster.
	 */
	public CacheStruct(int nBands, int dataType, int dataSourceWidth, int dataSourceHeight, long cacheSize, long pageSize){
		this.cacheSize = cacheSize;
		this.pageSize = pageSize;
		init(nBands, dataType, dataSourceWidth, dataSourceHeight);
	}
	
	private void init(int nBands, int dataType, int dataSourceWidth, int dataSourceHeight){
		this.nPageBandBuffers = nBands;
		this.dataType = dataType;
		
		//Pasamos los megas a bytes
		cacheSize = cacheSize * 1048576;
		pageSize = pageSize * 1048576;
		
		cacheSizePerBand = (long)(cacheSize / nBands);
		pageSizePerBand = (long)(pageSize / nBands);
				
		//int dataSize = 0;
		int dataSizePerBand = 0;
		if (dataType == IBuffer.TYPE_BYTE){
			dataSizePerBand = 1;
		} else if ((dataType == IBuffer.TYPE_SHORT) | (dataType == IBuffer.TYPE_USHORT)) {
			dataSizePerBand = 2;
        } else if (dataType == IBuffer.TYPE_INT) {
        	dataSizePerBand = 4;
        } else if (dataType == IBuffer.TYPE_FLOAT) {
        	dataSizePerBand = 4;
        } else if (dataType == IBuffer.TYPE_DOUBLE) {
        	dataSizePerBand = 8;
        }
		//dataSize = dataSizePerBand * nBands;
				
		//La altura de la página depende del ancho de esta y del tipo de dato
		for(int i = (possibleHeights.length - 1); i >= 0; i --){
			long size = (long)dataSourceWidth * (long)possibleHeights[i] * (long)dataSizePerBand; 
			if(size <= pageSizePerBand){
				hPag = possibleHeights[i];
				break;
			}
		}
		//Calculamos el tamaño de página en bytes 
		pageSizePerBand = dataSourceWidth * hPag * dataSizePerBand;
		
		//Calculamos el número de páginas que tendrá el buffer completo
		nPags = (int)(cacheSizePerBand / pageSizePerBand);
				
		while((nPags % pagsPerGroup) != 0)
			nPags ++;	
		
		//Recalculamos el tamaño de la caché
		cacheSizePerBand = (long)(pageSizePerBand * nPags);
		
		//Calculamos el número de grupos de caché
		nGroups = (int)(nPags / pagsPerGroup);
		
		int h = hPag; 
		while(h > 1){
			h >>= 1;
			bitsPag ++;
		}
		
		//Calculamos el número total de páginas en caché
		nTotalPags = (int)(dataSourceHeight / hPag);
		if((dataSourceHeight % hPag) != 0)
			nTotalPags ++;
		
		offset = hPag - 1;
		
		cacheSize = cacheSizePerBand * nBands;
		pageSize = pageSizePerBand * nBands;
	}

	/**
	 * Obtiene el tamaño de caché en Bytes
	 * @return Tamaño de caché en Bytes
	 */
	public long getCacheSize() {
		return cacheSize;
	}

	/**
	 * Obtiene la altura de la página de cache en líneas. 
	 * @return Número de líneas de altura de página.
	 */
	public int getHPag() {
		return hPag;
	}
	
	/**
	 * Asigna la altura de la página de cache en líneas. 
	 * @param Número de líneas de altura de página.
	 */
	public void setHPag(int hPag){
		this.hPag = hPag;
	}

	/**
	 * Obtiene el número de bandas
	 * @return Número de bandas
	 */
	public int getNBands() {
		return nPageBandBuffers;
	}
	
	/**
	 * Asigna el número de bandas
	 * @param Número de bandas
	 */
	public void setNBands(int nBands){
		this.nPageBandBuffers = nBands;
	}

	/**
	 * Obtiene el número de páginas de la caché
	 * @return Número total de páginas de la caché
	 */
	public int getNPags() {
		return nPags;
	}
	
	/**
	 * Asigna el número de páginas de la caché
	 * @param Número total de páginas de la caché
	 */
	public void setNPags(int nPags) {
		this.nPags = nPags;
	}

	/**
	 * Obtiene el tamaño de página en bytes
	 * @return Tamaño de página en bytes
	 */
	public long getPagSize() {
		return (long)pageSize;
	}
		
	/**
	 * Obtiene el número de páginas que contiene cada grupo.
	 * @return Número de páginas de un grupo.
	 */
	public int getPagsPerGroup() {
		return pagsPerGroup;
	}

	/**
	 * Obtiene el número de grupos de caché
	 * @return Número de grupos
	 */
	public int getNGroups() {
		return nGroups;
	}
	
	/**
	 * Asigna el número de grupos de caché
	 * @param nGroups Número de grupos
	 */
	public void setNGroups(int nGroups){
		this.nGroups = nGroups;
	}
	
	/**
	 * Obtiene el número total de páginas del raster
	 * @return Número total de páginas
	 */
	public int getNTotalPags() {
		return nTotalPags;
	}
	
	/**
	 * Asigna el número total de páginas del raster
	 * @param Número total de páginas
	 */
	public void setNTotalPags(int nPags){
		this.nTotalPags = nPags;
	}
	
	/**
	 * Obtiene el número de bits por página para poder calcular el desplazamiento binario
	 * de la dirección de acceso de la petición. Es decir si se solicita un dato en la línea
	 * 36 (en decimal) del raster 100100(en binario) y el desplazamiento es 4 bits el número de 
	 * página resultante será 10(en binario) 2 (en decimal)
	 * @return Número de bits por página.
	 */
	public int getBitsPag() {
		return bitsPag;
	}
	
	/**
	 * Asigna el número de bits por página para poder calcular el desplazamiento binario
	 * de la dirección de acceso de la petición. Es decir si se solicita un dato en la línea
	 * 36 (en decimal) del raster 100100(en binario) y el desplazamiento es 4 bits el número de 
	 * página resultante será 10(en binario) 2 (en decimal)
	 * @param bitsPag
	 */
	public void setBitsPag(int bitsPag){
		this.bitsPag = bitsPag;
	}

	/**
	 * Obtiene el valor del desplazamiento
	 * Para extraer el desplazamiento de una dirección (línea de raster) hay que hacer una operación And con 
	 * con la altura de la página -1. Por ejemplo, una página de 16 líneas de altura el desplazamiento será
	 * 16 - 1 = 15 porque 15 en binario es 1111.
	 * 
	 * Si queremos acceder a la linea del raster número 83 (1010011) realizando la operación And con el valor del
	 * desplazamiento obtenemos (0001111 & 1010011 = 0000011), es decir el valor 3 en decimal. Esto quiere decir
	 * que la línea 83 del raster es la 3 de su página. 
	 * @return valor del desplazamiento
	 */
	public int getOffset() {
		return offset;
	}

	/**
	 * Asigna el valor del desplazamiento.
	 * Para extraer el desplazamiento de una dirección (línea de raster) hay que hacer una operación And con 
	 * con la altura de la página -1. Por ejemplo, una página de 16 líneas de altura el desplazamiento será
	 * 16 - 1 = 15 porque 15 en binario es 1111.
	 * 
	 * Si queremos acceder a la linea del raster número 83 (1010011) realizando la operación And con el valor del
	 * desplazamiento obtenemos (0001111 & 1010011 = 0000011), es decir el valor 3 en decimal. Esto quiere decir
	 * que la línea 83 del raster es la 3 de su página. 
	 * @param offset valor del desplazamiento
	 */
	public void setOffset(int offset){
		this.offset = offset;
	}

	/**
	 * Obtiene el tamaño de la caché por cada banda del raster. Hay que tener en
	 * cuenta que cada banda se trata como una estructura de datos en memoria distinta
	 * para poder hacer switch con las bandas de una forma sencilla
	 * @return long con el tamaño de cache por banda
	 */
	public long getCacheSizePerBand() {
		return cacheSizePerBand;
	}

	/**
	 * Asigna el tamaño de la caché por cada banda del raster. Hay que tener en
	 * cuenta que cada banda se trata como una estructura de datos en memoria distinta
	 * para poder hacer switch con las bandas de una forma sencilla
	 * @param long con el tamaño de cache por banda
	 */
	public void setCacheSizePerBand(long cacheSizePerBand) {
		this.cacheSizePerBand = cacheSizePerBand;
	}

	/**
	 * Obtiene el tamaño de una página de caché correspondiente a una banda
	 * @return tamaño de una página de caché correspondiente a una banda
	 */
	public long getPagSizePerBand() {
		return pageSizePerBand;
	}

	/**
	 * Asigna el tamaño de una página de caché correspondiente a una banda
	 * @param tamaño de una página de caché correspondiente a una banda
	 */
	public void setPagSizePerBand(long pagSizePerBand) {
		this.pageSizePerBand = pagSizePerBand;
	}
	
	/**
	 * Imprime la información de estructura de caché
	 */
	public void show(){
		System.out.println("Cache (total size):" + cacheSize);
		System.out.println("Page (total size):" + pageSize);
		System.out.println("Cache (size per band):" + cacheSizePerBand);
		System.out.println("Page (size per band):" + pageSizePerBand);
		
		System.out.println("Number of Pags del raster:" + nTotalPags);
		System.out.println("Number of Pags de cache:" + nPags);
		System.out.println("Number of Bands:" + nPageBandBuffers);
		System.out.println("Number of Groups:" + nGroups);
		System.out.println("Pages per group:" + pagsPerGroup);
		System.out.println("bits per pag:" + bitsPag);
		System.out.println("Page Height (in lines):" + hPag);
	}

	/**
	 * Obtiene el tipo de dato
	 * @return Tipo de dato
	 */
	public int getDataType() {
		return dataType;
	}

	/**
	 * Asigna el tipo de dato
	 * @param dataType
	 */
	public void setDataType(int dataType) {
		this.dataType = dataType;
	}

}
