/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
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
package org.gvsig.raster.dataset;

import java.util.ArrayList;


/**
 * Clase que representa a una banda de un fichero.
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class Band {
	//Nombre del fichero al que pertenece la banda
	private String 		fileName = "";
	//Lista de nombre de otros ficheros que forman la banda. Esto es util para CompositeDataset que está compuesto por un mosaico de ficheros.
	private ArrayList	additionalName = new ArrayList();
	//Posición en el fichero de la  banda
	private int 		position = -1;
	//Tipo de dato de la banda
	private int 		dataType = IBuffer.TYPE_BYTE;
	//lista de bandas del buffer donde se dibuja esta banda de imagen. Cada elemento
	//del vector es un número que corresponde con el número de banda del buffer donde se escribe esta
	private int[]		rasterBufBandToDrawList = null;
	
	public Band(String f, int p, int dt){
		setFileName(f);
		setPosition(p);
		setDataType(dt);
	}
	
	/*
	 *  (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public Object clone() {
		Band result = new Band(fileName, position, dataType);
		if(rasterBufBandToDrawList != null) {
			int[] drawBands = new int[rasterBufBandToDrawList.length];
			for (int i = 0; i < rasterBufBandToDrawList.length; i++) 
				drawBands[i] = rasterBufBandToDrawList[i];
		}
		return result;
	}
	
	/**
	 * Resetea la asignación de dibujado de las bandas de la imagen
	 * sobre el DataImage cuando se hace un update para esta banda.
	 */
	public void clearDrawableBands(){
		rasterBufBandToDrawList = null;
	}

	//******************************
	//Setters and Getters
	//******************************
	
	/**
	 * Obtiene las banda del RasterBuf sobre la que se pinta
	 * este objeto banda
	 * @return bandas del RasterBuf
	 */
	public int[] getBufferBandListToDraw() {
		return rasterBufBandToDrawList;
	}

	
	
	/**
	 * Dice si la banda se está dibujando en el buffers de salida.
	 * @return true si la banda se está dibujando y false si no se está haciendo
	 */
	public boolean isDrawing(){
		if(this.rasterBufBandToDrawList == null)
			return false;
		return true;
	}
		
	/**
	 * Asigna una banda del RasterBuf sobre la que se pinta
	 * este objeto banda
	 * @param rasterBufBandToDraw banda del RasterBuf
	 */
	public void setPositionToDrawInBuffer(int rasterBufBandToDraw) {
		if (rasterBufBandToDrawList == null) {
			rasterBufBandToDrawList = new int[1];
			rasterBufBandToDrawList[0] = rasterBufBandToDraw;
		} else {
			int[] auxList = new int[rasterBufBandToDrawList.length + 1];
			for (int i = 0; i < rasterBufBandToDrawList.length; i++)
				auxList[i] = rasterBufBandToDrawList[i];
			auxList[rasterBufBandToDrawList.length] = rasterBufBandToDraw;
			rasterBufBandToDrawList = auxList;
		}
	}
	
	/**
	 * Obtiene el nombre del fichero al que pertenece la banda
	 * @return String con el nombre del fichero al 
	 * que pertenece la banda
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * Asigna el nombre del fichero al que pertenece la banda
	 * @param fileName String con el nombre del fichero al 
	 * que pertenece la banda
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * Obtiene la posición de la banda en el fichero
	 * @return entero con la posición de la banda en el fichero
	 */
	public int getPosition() {
		return position;
	}

	/**
	 * Asigna la posición de la banda en el fichero
	 * @param position Posición de la banda en el fichero
	 */
	public void setPosition(int position) {
		this.position = position;
	}

	/**
	 * Obtiene el tipo de dato de la banda
	 * @return entero con el tipo de dato 
	 */
	public int getDataType() {
		return dataType;
	}

	/**
	 * Asigna el tipo de dato de la banda
	 * @param datatype entero con el tipo de dato de la banda
	 */
	public void setDataType(int dataType) {
		this.dataType = dataType;
	}
	
	/**
	 * Asigna un nombre de fichero adicional al principal. Esto es util para
	 * mosaicos de raster donde una banda está compuesta por multiples ficheros.
	 */
	public void setAdditionalName(String fileName) {
		additionalName.add(fileName);
	}
	
	/**
	 * Obtiene la lista de nombres de fichero adicionales.
	 * @return String[]
	 */
	public String[] getAdditionalName() {
		return (String[]) additionalName.toArray();
	}
}
