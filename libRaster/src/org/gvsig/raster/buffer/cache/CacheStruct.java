
/* gvSIG. Sistema de Informaci�n Geogr�fica de la Generalitat Valenciana
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
 * Esta clase representa la estructura en memoria de la cache. Contiene el n�mero de p�ginas 
 * as� como los subconjuntos en las que se divide. Tendr� tambi�n datos como en ancho y alto de
 * cada p�gina y tama�o en memoria de esta. 
 * 
 * @author Nacho Brodin (nachobrodin@gmail.com)
 *
 */
public class CacheStruct {
	//******************* PARAMETERS ***************************
	/**
	 * Tama�o aproximado de cach� en Megas. Si este valor es alto cabr�n muchas p�ginas en memoria 
	 * a la vez y si es bajo cabr�n pocas. Hay que tener en cuenta que al instanciar se convertira en bytes
	 * para su mejor tratamiento. Al llamar al constructor esta variable contendr� el tama�o exacto
	 * de la cache en bytes. El tama�o aqu� especificado es aproximado. Este variar� dependiendo de los
	 * par�metros del raster a cachear ya que las p�ginas deben tener una altura potencia de 2.
	 */
	private long cacheSize = RasterLibrary.cacheSize;
	/**
	 * Tama�o m�ximo de la p�gina en Megas. Hay que tener en cuenta que al instanciar se convertira en bytes
	 * para su mejor tratamiento. Al llamar al constructor esta variable contendr� el tama�o exacto
	 * de la p�gina en bytes
	 */
	private double pageSize = RasterLibrary.pageSize;
	/**
	 * N�mero de p�ginas que tiene cada conjunto
	 */
	private int pagsPerGroup = RasterLibrary.pagsPerGroup;
	//****************** END PARAMETERS ************************
		
	private int[] possibleHeights = {2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048, 4096, 8192, 16384, 32768, 65536};
		
	/**
	 * Altura de la p�gina de cada banda en l�neas.
	 */
	private int hPag = 1;
	/**
	 * Para extraer el desplazamiento de una direcci�n (l�nea de raster) hay que hacer una operaci�n And con 
	 * con la altura de la p�gina -1. Por ejemplo, una p�gina de 16 l�neas de altura el desplazamiento ser�
	 * 16 - 1 = 15 porque 15 en binario es 1111.
	 * 
	 * Si queremos acceder a la linea del raster n�mero 83 (1010011) realizando la operaci�n And con el valor del
	 * desplazamiento obtenemos (0001111 & 1010011 = 0000011), es decir el valor 3 en decimal. Esto quiere decir
	 * que la l�nea 83 del raster es la 3 de su p�gina. 
	 */
	private int offset = 1;
	/**
	 * N�mero de p�ginas en cach�
	 */
	private int nPags = 0;
	/**
	 * N�mero de estructuras PageBandBuffer que tendr� el objeto PageBuffer. Cada una de estas
	 * corresponde a una banda del buffer cacheada.
	 */
	private int nPageBandBuffers = 0;
	/**
	 * N�mero de grupos
	 */
	private int nGroups = 0;
	/**
	 * N�mero de bits para el desplazamiento de una p�gina. Por ejemplo, una p�gina de 64 l�neas
	 * tendr� un bitsPag = 6
	 */
	private int bitsPag = 0;
	/**
	 * N�mero total de p�ginas en las que se divide el raster
	 */
	private int nTotalPags = 0;
	/**
	 * Tama�o de cache por banda del raster (cacheSize / NBandas)
	 */
	private long cacheSizePerBand = 0;
	/**
	 * Tama�o de p�gina por banda del raster (pagSize / NBandas)
	 */
	private long pageSizePerBand = 0;
	/**
	 * Tipo de dato de la cach�
	 */
	private int	dataType = 0;
	
	/**
	 * Calcula los par�metros del tama�o de cach� y de p�gina, asi como la altura de las mismas y el n�mero
	 * de grupos que salen en cach�.
	 * @param nBands N�mero de bandas del raster
	 * @param dataType Tipo de dato de la imagen
	 * @param dataSourceWidth ancho de cada l�nea completa del raster.
	 */
	public CacheStruct(int nBands, int dataType, int dataSourceWidth, int dataSourceHeight){
		init(nBands, dataType, dataSourceWidth, dataSourceHeight);
	}
	
	/**
	 * Constructor de una sola banda y sin el calculo de par�metros. Estos deben ser asignados
	 * posteriorimente. Esta llamada es usada en casos especiales de creaci�n de buffers cacheados.
	 */
	public CacheStruct() {
	}
	
	/**
	 * Constructor solo para test. No utilizar con un uso normal de la librer�a
	 * Calcula los par�metros del tama�o de cach� y de p�gina, asi como la altura de las mismas y el n�mero
	 * de grupos que salen en cach�.
	 * @param nBands N�mero de bandas del raster
	 * @param dataType Tipo de dato de la imagen
	 * @param dataSourceWidth ancho de cada l�nea completa del raster.
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
				
		//La altura de la p�gina depende del ancho de esta y del tipo de dato
		for(int i = (possibleHeights.length - 1); i >= 0; i --){
			long size = (long)dataSourceWidth * (long)possibleHeights[i] * (long)dataSizePerBand; 
			if(size <= pageSizePerBand){
				hPag = possibleHeights[i];
				break;
			}
		}
		//Calculamos el tama�o de p�gina en bytes 
		pageSizePerBand = dataSourceWidth * hPag * dataSizePerBand;
		
		//Calculamos el n�mero de p�ginas que tendr� el buffer completo
		nPags = (int)(cacheSizePerBand / pageSizePerBand);
				
		while((nPags % pagsPerGroup) != 0)
			nPags ++;	
		
		//Recalculamos el tama�o de la cach�
		cacheSizePerBand = (long)(pageSizePerBand * nPags);
		
		//Calculamos el n�mero de grupos de cach�
		nGroups = (int)(nPags / pagsPerGroup);
		
		int h = hPag; 
		while(h > 1){
			h >>= 1;
			bitsPag ++;
		}
		
		//Calculamos el n�mero total de p�ginas en cach�
		nTotalPags = (int)(dataSourceHeight / hPag);
		if((dataSourceHeight % hPag) != 0)
			nTotalPags ++;
		
		offset = hPag - 1;
		
		cacheSize = cacheSizePerBand * nBands;
		pageSize = pageSizePerBand * nBands;
	}

	/**
	 * Obtiene el tama�o de cach� en Bytes
	 * @return Tama�o de cach� en Bytes
	 */
	public long getCacheSize() {
		return cacheSize;
	}

	/**
	 * Obtiene la altura de la p�gina de cache en l�neas. 
	 * @return N�mero de l�neas de altura de p�gina.
	 */
	public int getHPag() {
		return hPag;
	}
	
	/**
	 * Asigna la altura de la p�gina de cache en l�neas. 
	 * @param N�mero de l�neas de altura de p�gina.
	 */
	public void setHPag(int hPag){
		this.hPag = hPag;
	}

	/**
	 * Obtiene el n�mero de bandas
	 * @return N�mero de bandas
	 */
	public int getNBands() {
		return nPageBandBuffers;
	}
	
	/**
	 * Asigna el n�mero de bandas
	 * @param N�mero de bandas
	 */
	public void setNBands(int nBands){
		this.nPageBandBuffers = nBands;
	}

	/**
	 * Obtiene el n�mero de p�ginas de la cach�
	 * @return N�mero total de p�ginas de la cach�
	 */
	public int getNPags() {
		return nPags;
	}
	
	/**
	 * Asigna el n�mero de p�ginas de la cach�
	 * @param N�mero total de p�ginas de la cach�
	 */
	public void setNPags(int nPags) {
		this.nPags = nPags;
	}

	/**
	 * Obtiene el tama�o de p�gina en bytes
	 * @return Tama�o de p�gina en bytes
	 */
	public long getPagSize() {
		return (long)pageSize;
	}
		
	/**
	 * Obtiene el n�mero de p�ginas que contiene cada grupo.
	 * @return N�mero de p�ginas de un grupo.
	 */
	public int getPagsPerGroup() {
		return pagsPerGroup;
	}

	/**
	 * Obtiene el n�mero de grupos de cach�
	 * @return N�mero de grupos
	 */
	public int getNGroups() {
		return nGroups;
	}
	
	/**
	 * Asigna el n�mero de grupos de cach�
	 * @param nGroups N�mero de grupos
	 */
	public void setNGroups(int nGroups){
		this.nGroups = nGroups;
	}
	
	/**
	 * Obtiene el n�mero total de p�ginas del raster
	 * @return N�mero total de p�ginas
	 */
	public int getNTotalPags() {
		return nTotalPags;
	}
	
	/**
	 * Asigna el n�mero total de p�ginas del raster
	 * @param N�mero total de p�ginas
	 */
	public void setNTotalPags(int nPags){
		this.nTotalPags = nPags;
	}
	
	/**
	 * Obtiene el n�mero de bits por p�gina para poder calcular el desplazamiento binario
	 * de la direcci�n de acceso de la petici�n. Es decir si se solicita un dato en la l�nea
	 * 36 (en decimal) del raster 100100(en binario) y el desplazamiento es 4 bits el n�mero de 
	 * p�gina resultante ser� 10(en binario) 2 (en decimal)
	 * @return N�mero de bits por p�gina.
	 */
	public int getBitsPag() {
		return bitsPag;
	}
	
	/**
	 * Asigna el n�mero de bits por p�gina para poder calcular el desplazamiento binario
	 * de la direcci�n de acceso de la petici�n. Es decir si se solicita un dato en la l�nea
	 * 36 (en decimal) del raster 100100(en binario) y el desplazamiento es 4 bits el n�mero de 
	 * p�gina resultante ser� 10(en binario) 2 (en decimal)
	 * @param bitsPag
	 */
	public void setBitsPag(int bitsPag){
		this.bitsPag = bitsPag;
	}

	/**
	 * Obtiene el valor del desplazamiento
	 * Para extraer el desplazamiento de una direcci�n (l�nea de raster) hay que hacer una operaci�n And con 
	 * con la altura de la p�gina -1. Por ejemplo, una p�gina de 16 l�neas de altura el desplazamiento ser�
	 * 16 - 1 = 15 porque 15 en binario es 1111.
	 * 
	 * Si queremos acceder a la linea del raster n�mero 83 (1010011) realizando la operaci�n And con el valor del
	 * desplazamiento obtenemos (0001111 & 1010011 = 0000011), es decir el valor 3 en decimal. Esto quiere decir
	 * que la l�nea 83 del raster es la 3 de su p�gina. 
	 * @return valor del desplazamiento
	 */
	public int getOffset() {
		return offset;
	}

	/**
	 * Asigna el valor del desplazamiento.
	 * Para extraer el desplazamiento de una direcci�n (l�nea de raster) hay que hacer una operaci�n And con 
	 * con la altura de la p�gina -1. Por ejemplo, una p�gina de 16 l�neas de altura el desplazamiento ser�
	 * 16 - 1 = 15 porque 15 en binario es 1111.
	 * 
	 * Si queremos acceder a la linea del raster n�mero 83 (1010011) realizando la operaci�n And con el valor del
	 * desplazamiento obtenemos (0001111 & 1010011 = 0000011), es decir el valor 3 en decimal. Esto quiere decir
	 * que la l�nea 83 del raster es la 3 de su p�gina. 
	 * @param offset valor del desplazamiento
	 */
	public void setOffset(int offset){
		this.offset = offset;
	}

	/**
	 * Obtiene el tama�o de la cach� por cada banda del raster. Hay que tener en
	 * cuenta que cada banda se trata como una estructura de datos en memoria distinta
	 * para poder hacer switch con las bandas de una forma sencilla
	 * @return long con el tama�o de cache por banda
	 */
	public long getCacheSizePerBand() {
		return cacheSizePerBand;
	}

	/**
	 * Asigna el tama�o de la cach� por cada banda del raster. Hay que tener en
	 * cuenta que cada banda se trata como una estructura de datos en memoria distinta
	 * para poder hacer switch con las bandas de una forma sencilla
	 * @param long con el tama�o de cache por banda
	 */
	public void setCacheSizePerBand(long cacheSizePerBand) {
		this.cacheSizePerBand = cacheSizePerBand;
	}

	/**
	 * Obtiene el tama�o de una p�gina de cach� correspondiente a una banda
	 * @return tama�o de una p�gina de cach� correspondiente a una banda
	 */
	public long getPagSizePerBand() {
		return pageSizePerBand;
	}

	/**
	 * Asigna el tama�o de una p�gina de cach� correspondiente a una banda
	 * @param tama�o de una p�gina de cach� correspondiente a una banda
	 */
	public void setPagSizePerBand(long pagSizePerBand) {
		this.pageSizePerBand = pagSizePerBand;
	}
	
	/**
	 * Imprime la informaci�n de estructura de cach�
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
