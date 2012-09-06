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
 * Esta clase gestiona una lista de bandas que puede pertenecer
 * a un fichero (la tiene un Dataset) o puede ser una lista 
 * de bandas de multiples ficheros (la tiene un MultiRasterDataset). Las bandas son
 * almacenadas en un array donde la posición en este es el número de banda.  
 * @author Nacho Brodin (nachobrodin@gmail.com)
 *
 */
public class BandList {
	//Band array
	private ArrayList bands = new ArrayList();
	private int[]	drawableBands = null;
		
	/*
	 *  (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public Object clone() {
		BandList result = new BandList();
		
		ArrayList b = new ArrayList();
		for (int i = 0; i < bands.size(); i++) 
			b.add(((Band)bands.get(i)).clone());
		result.setBandArray(bands);
		
		if(drawableBands != null) {
			int[] drawBands = new int[drawableBands.length];
			for (int i = 0; i < drawableBands.length; i++) 
				drawBands[i] = drawableBands[i];
			result.setDrawableBands(drawableBands);
		}
		
		return result;
	}
	
	/**
	 * Asigna el último array de bandas a escribir solicitadas.
	 * @param drawableBands
	 */
	public void setDrawableBands(int[] drawableBands) {
		this.drawableBands = drawableBands;
	}
	
	/**
	 * Asigna el array de bandas
	 * @param bands
	 */
	public void setBandArray(ArrayList bands) {
		this.bands = bands;
	}
	
	/**
	 * Obtiene el último array de bandas a escribir solicitadas.
	 * @return
	 */
	public int[] getDrawableBands() {	
		return drawableBands;
	}
	
	/**
	 * Encuentra una banda en la lista.
	 * @param file Fichero al que pertenece la banda buscada.
	 * @param pos Posición que ocupa en el fichero.
	 * @return true si se ha hallado la banda y false si no se 
	 * ha encontrado
	 */
	public boolean findBand(Band band) {
		for(int i = 0; i < bands.size(); i++) {
			Band b = (Band)bands.get(i); 
			if(	b.getFileName().equals(band.getFileName()) &&
				b.getPosition() == band.getPosition())
				return true;
		}
		return false;
	}
		
	/**
	 * Obtiene la lista de bandas a pintar sobre el buffer de 
	 * salida en forma de array. Cada elemento del array es una banda
	 * del RasterBuf de salida que ha de dibujarse y el valor que contiene
	 * ese elemento es la banda de la imagen que va pintada en esa banda 
	 * del RasterBuf.
	 * <table border=1>
	 * <tr><td>Elem</td><td>Valor</td></tr>
	 * <tr><td>0</td><td>1</td></tr>
	 * <tr><td>1</td><td>0</td></tr>
	 * <tr><td>2</td><td>-1</td></tr>
	 * <tr><td>3</td><td>0</td></tr>
	 * <tr><td>4</td><td>2</td></tr>
	 * </table>
	 * El RasterBuf tendra en la banda 0 dibujada la banda 1 de este GeoRasterFile, 
	 * en la banda 1 la 0 del dataset, en la banda 2 no habrá ninguna de este dataset,
	 * en la 3 la 0 y en la 4 la 2 de este GeoRasterFile.
	 * @return lista de bandas a dibujar o un array de un elemento con valor -1.
	 */
	public int[] bandsToDrawList(int nBandsDataImage){
		
		int[] bandsToRead = new int[nBandsDataImage];
		for(int dataImageBand = 0;dataImageBand < nBandsDataImage;dataImageBand++) {
			bandsToRead[dataImageBand] = -1;
			for(int band = 0;band < bands.size();band++) {
				if(	((Band)bands.get(band)) != null &&
					((Band)bands.get(band)).getBufferBandListToDraw() != null) {
					for(int dest=0;dest<((Band)bands.get(band)).getBufferBandListToDraw().length;dest++){
						if(dataImageBand == ((Band)bands.get(band)).getBufferBandListToDraw()[dest])
							bandsToRead[dataImageBand] = band;
					}
				}
			}
		}
	
		return bandsToRead;
	}
	
	/**
	 * Checkea si alguna banda de la lista se estça dibujando en el buffer de salida.
	 * @return	true si alguna banda se está dibujando y false si no
	 */
	public boolean isDrawingAnyBand(){
		for(int band = 0;band < bands.size();band++){
			if(((Band)bands.get(band)).isDrawing())
				return true;
		}
		return false;
	}
	
	//******************************
	//Setters and Getters
	//******************************
	
	/**
	 * Añade una banda a la lista.
	 * @param b banda a añadir.
	 */
	public void addBand(Band b, int pos)throws BandNotFoundInListException{
		bands.add(b);
	}
	
	/**
	 * Añade la lista de bandas pasada por parámetro a la lista
	 * actual. Si alguna banda ya existe no la añade y continua.
	 * @param bl Lista de bandas
	 */
	public void addBandList(BandList bl){
		for(int i = 0; i < bl.getBandCount(); i++){
			if(!findBand(bl.getBand(i)))
				bands.add(bl.getBand(i));
		}
	}
	
	/**
	 * ELimina todas las bandas que tienen un nombre determinado.
	 * @param name Nombre de las bandas a eliminar
	 */
	public void removeBands(String name){
		for(int i = 0; i < getBandCount(); i++){
			Band band = getBand(i);
			if(band.getFileName().equals(name))
				bands.remove(i);
		}
	}
	
	/**
	 * Resetea la asignación de dibujado de las bandas de la imagen
	 * sobre el DataImage cuando se hace un update para esta banda.
	 */
	public void clearDrawableBands(){
		drawableBands = null;
		for(int i = 0; i < getBandCount(); i++)
			((Band)bands.get(i)).clearDrawableBands();
	}
	
	/**
	 * Para este GeoRasterFile asigna que bandas se pintaran
	 * sobre el RasterBuf cuando se haga un update. Especificamos a 
	 * través de los parámetros para que posición del RasterBuf irá 
	 * dibujada con que banda del fichero de imagen.
	 * @param posRasterBuf	Posición del RasterBuf que queremos pintar.
	 * @param imageBand	Banda de la imagen que se pintará
	 */
	public void addDrawableBand(int posRasterBuf, int imageBand){
		try{
			((Band)bands.get(imageBand)).setPositionToDrawInBuffer(posRasterBuf);
		}catch(IndexOutOfBoundsException exc){
			//No hacemos nada simplemente no inserta la banda.
		}
	}
	
	/**
	 * Obtiene el número de bandas de un RasterBuf sobre las que se pintara
	 * alguna banda de este fichero cuando se llama a un updateBuffer. 
	 * @return Número de bandas. Cero en caso de no tener ninguna asignada. 
	 */
	public int getDrawableBandsCount(){
		int nbands = 0;
		for(int i = 0;i < bands.size();i++) {
			Band b = (Band)bands.get(i);
			if(b.getBufferBandListToDraw() != null)
				nbands += b.getBufferBandListToDraw().length;
		}
		return nbands;
	}
	
	/**
	 * Obtiene la banda de la posición i.
	 * @param i Posición de la banda a obtener.
	 * @return Banda.
	 */
	public Band getBand(int i){
		if(i < 0 || i >= bands.size())
			return null;
		return (Band)bands.get(i);
	}
	
	/**
	 * Obtiene el número de bandas.
	 * @return entero con el número de bandas.
	 */
	public int getBandCount(){
		return bands.size();
	}
	
	/**
	 * A partir de un nombre de fichero y un número de banda obtiene la banda o bandas del buffer de salida 
	 * donde se pinta.
	 * @param fileName Nombre de fichero
	 * @param band Número de banda de la imagen
	 * @return banda o bandas del buffer de salida donde se dibuja.
	 */
	public int[] getBufferBandToDraw(String fileName, int band){
		for(int i = 0; i < bands.size(); i++){
			if(((Band)bands.get(i)).getFileName().equals(fileName))
				if(((Band)bands.get(i)).getPosition() == band)
					return ((Band)bands.get(i)).getBufferBandListToDraw();
		}
		int[] r = {-1};
		return r;
	}
	/**
	 * Obtiene en un array de String la lista de nombres de ficheros
	 * @return lista de nombres de los ficheros del GeoRasterMultiFile
	 */
	public String[] getBandStringList(){
		String[] list = new String[bands.size()];
		for(int i = 0; i < bands.size(); i++)
			list[i] = ((Band)bands.get(i)).getFileName();
		return list;
	}
	
	/**
	 * Obtiene en un array de enteros con la lista de la posición de bandas
	 * @return lista de la posición de bandas
	 */
	public int[] getBandPositionList(){
		int[] list = new int[bands.size()];
		for(int i = 0; i < bands.size(); i++)
			list[i] = ((Band)bands.get(i)).getPosition();
		return list;
	}
	
	/**
	 * Obtiene el tipo de dato de las bandas. Esta llamada supone que todos los tipos de dato
	 * de las bandas son igual por lo que devolverá el primero. 
	 * @return Entero que representa el tipo de datos de las bandas
	 */
	public int getBandsDataType(){
		if(bands.size() <= 0)
			return IBuffer.TYPE_UNDEFINED;
		return ((Band)bands.get(0)).getDataType();
	}
	
	/**
	 * Obtiene la posición del fichero en la lista a partir del nombre 
	 * @param fileName Nombre del fichero
	 * @return Posición del fichero o -1 si no existe. 
	 */
	public int getFileNumber(String fileName){
		String aux = "";
		int count = -1;
		for(int i = 0; i < bands.size(); i++){
			if(((Band)bands.get(i)).getFileName().indexOf(aux) != 0){
				count ++;
				aux = ((Band)bands.get(i)).getFileName();
			}
			if(((Band)bands.get(i)).getFileName().indexOf(fileName) == 0)
				return count;
		}
		return -1;
	}
	
	/**
	 * Limpia la lista de bandas
	 */
	public void clear() {
		bands.clear();
		drawableBands = null;
	}
	
	/**
	 *Muestra la lista de bandas en modo texto
	 */
	public void show() {
		System.out.println("BandCount: " + getBandCount());
		System.out.println("DrawableBandsCount: " + getDrawableBandsCount());
		for (int i = 0; i < getBandCount(); i++) {
			System.out.println("");
			System.out.println("***********************");
			System.out.println("Band: " + i);
			System.out.println("DataType: " + ((Band)getBand(i)).getDataType());
			System.out.println("FileName: " + ((Band)getBand(i)).getFileName());
			System.out.println("Position: " + ((Band)getBand(i)).getPosition());
			if(((Band)getBand(i)).getBufferBandListToDraw() != null) {
				System.out.print("Band Dst: ");
				for (int j = 0; j < ((Band)getBand(i)).getBufferBandListToDraw().length; j++)
					System.out.print(((Band)getBand(i)).getBufferBandListToDraw()[j] + " ");
			}
			System.out.println();
		}
		
	}

}
