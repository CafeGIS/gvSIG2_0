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
package org.gvsig.raster.dataset.properties;

import java.util.ArrayList;
/**
 * Clase que contiene la interpretación de color por banda. Inicialmente
 * es inicializada con los valores contenidos en el raster si los tiene. Después 
 * estos valores pueden ser modificados.
 * 
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class DatasetColorInterpretation {
	// Identificadores de color interpretation
	public static final String RED_BAND            = "Red";
	public static final String GREEN_BAND          = "Green";
	public static final String BLUE_BAND           = "Blue";
	public static final String ALPHA_BAND          = "Alpha";
	public static final String GRAY_BAND           = "Gray";
	public static final String PAL_BAND            = "Palette";
	public static final String UNDEF_BAND          = "Undefined";

	/**
	 * Interpretación de color para cada banda
	 */
	private String[]           colorInterpretation = null;
	/**
	 * true si la imagen tiene una banda con el identificador de interpretación de
	 * color a Alpha
	 */
	private boolean            isAlphaBand         = false;

	/**
	 * Constructor vacio. 
	 */
	public DatasetColorInterpretation() {
		this.colorInterpretation = new String[0];
	}
	
	/**
	 * Constructor que asigna los valores de interpretación de color 
	 */
	public DatasetColorInterpretation(String[] colorInterp) {
		this.colorInterpretation = colorInterp;
	}

	/**
	 * Obtiene una interpretación de color GRAY
	 * @return DatasetColorInterpretation
	 */
	public static DatasetColorInterpretation createGrayInterpretation() {
		return new DatasetColorInterpretation(new String[]{GRAY_BAND});
	}
	
	/**
	 * Obtiene una interpretación de color RGB
	 * @return DatasetColorInterpretation
	 */
	public static DatasetColorInterpretation createARGBInterpretation() {
		return new DatasetColorInterpretation(new String[]{ALPHA_BAND, RED_BAND, GREEN_BAND, BLUE_BAND});
	}
	
	/**
	 * Obtiene una interpretación de color RGB
	 * @return DatasetColorInterpretation
	 */
	public static DatasetColorInterpretation createRGBInterpretation() {
		return new DatasetColorInterpretation(new String[]{RED_BAND, GREEN_BAND, BLUE_BAND});
	}
	
	/**
	 * Constructor que inicializa el número de valores de la interpretación de 
	 * color. Implica asignar posteriormente los valores a las bandas.
	 */
	public DatasetColorInterpretation(int values) {
		colorInterpretation = new String[values];
	}
	
	/**
	 * Inicializa el vector de cadenas que contendrán el nombre de la interpretación 
	 * de color asignada a cada banda. Este valor es el devuelto por la imagen.
	 * @param values Número de valores
	 */
	public void initColorInterpretation(int values){
		colorInterpretation = new String[values];
	}
	
	/**
	 * Obtiene los valores de la interpretación de color
	 * @return String[]
	 */
	public String[] getValues() {
		return colorInterpretation;
	}
	
	/**
	 * Asigna los valores de la interpretación de color
	 * @return String[]
	 */
	public void setValues(String[] colorInterp) {
		colorInterpretation = colorInterp;
	}
	
	/**
	 * Asigna un valor para la interpretación de color de una banda
	 * @param band Banda 
	 * @param value valor
	 */
	public void setColorInterpValue(int band, String value){
		try{
			colorInterpretation[band] = value;
			if(value.equals("Alpha"))
				isAlphaBand = true;
		}catch(ArrayIndexOutOfBoundsException ex){
			//No asignamos el elemento
		}
	}
	
	/**
	 * Obtiene la posición de la banda que contiene el identificador pasado por parámetro 
	 * o -1 si no tiene dicho identificador.
	 * @return Posición de la banda que contiene el identificador o -1 si no lo tiene.
	 */
	public int getBand(String id){
		if(colorInterpretation != null){
			for(int i = 0; i < colorInterpretation.length; i++)
				if(colorInterpretation[i] != null && colorInterpretation[i].equals(id))
					return i;
		}
		return -1;
	}
	
	/**
	 * Obtiene la posición de las bandas que contienen el identificador pasado por parámetro 
	 * o null si no tiene dicho identificador.
	 * @return Lista con las posiciones de las bandas que contienen el identificador o null si no lo tiene.
	 */
	public int[] getBands(String id){
		if(colorInterpretation != null){
			ArrayList array = new ArrayList();
			for(int i = 0; i < colorInterpretation.length; i++)
				if(colorInterpretation[i].equals(id))
					array.add(new Integer(i));
			int[] list = new int[array.size()];
			for (int i = 0; i < list.length; i++) 
				list[i] = ((Integer)array.get(i)).intValue();
			return list;
		}
		return null;
	}

	/**
	 * Obtiene true si existe una banda de alpha
	 * @return
	 */
	public boolean isAlphaBand() {
		return isAlphaBand;
	}	
	
	/**
	 * Obtiene el número de entradas de interpretación de color por
	 * banda en la lista.
	 * @return
	 */
	public int length(){
		return colorInterpretation.length;
	}
	
	/**
	 * Obtiene el valor de interpretación de color de la entrada i. 
	 * @param i Número de entrada
	 * @return interpretación de color para la entrada solicitada
	 */
	public String get(int i){
		if (i >= colorInterpretation.length)
			return null;
		return colorInterpretation[i];
	}
	
	/**
	 * Añade un objeto DatasetColorInterpretation al actual. El resultado es la suma 
	 * de ambos.
	 * @param ci
	 */
	public void addColorInterpretation(DatasetColorInterpretation ci) {
		String[] newCI = new String[colorInterpretation.length + ci.length()];
		for (int i = 0; i < colorInterpretation.length; i++)
			newCI[i] = colorInterpretation[i];
		for (int i = 0; i < ci.length(); i++) {
			newCI[colorInterpretation.length + i] = ci.get(i);
			if(newCI[colorInterpretation.length + i] != null && newCI[colorInterpretation.length + i].equals("Alpha"))
				isAlphaBand = true;
		}
		this.colorInterpretation = newCI;
	}
	
	/**
	 * Consulta si la interpretación de color está por definir en la imagen.
	 * @return true si no hay interpretación de color definida y false si la hay
	 */
	public boolean isUndefined() {
		for (int i = 0; i < colorInterpretation.length; i++) {
			if (colorInterpretation[i] != null) {
				if (!colorInterpretation[i].equals(UNDEF_BAND) &&
						!colorInterpretation[i].equals(ALPHA_BAND))
					return false;
			}
		}
		return true;
	}
}