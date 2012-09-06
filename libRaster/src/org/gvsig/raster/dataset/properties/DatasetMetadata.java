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
package org.gvsig.raster.dataset.properties;

import java.util.ArrayList;

import org.gvsig.raster.datastruct.TransparencyRange;



/**
 * Guarda en un Array los metadatos de los distintos tipos de imagenes.
 *
 *  NODATA_VALUES=255 255 255 1
 *  AREA_OR_POINT=Point 4
 *  TIFFTAG_XRESOLUTION=72 4
 *  TIFFTAG_YRESOLUTION=72 4
 *  TIFFTAG_RESOLUTIONUNIT=2 (pixels/inch) 4
 *
 *  STATISTICS_MINIMUM=0
 *  STATISTICS_MAXIMUM=221
 *  STATISTICS_MEAN=35.910196078431
 *  STATISTICS_MEDIAN=30
 *  STATISTICS_MODE=0
 *  STATISTICS_STDDEV=29.609849452294
 *  STATISTICS_HISTONUMBINS=256
 *  STATISTICS_HISTOMIN=0
 *  STATISTICS_HISTOMAX=255
 *  LAYER_TYPE=athematic
 *  STATISTICS_HISTOBINVALUES=30285675|0|0|22050| ...
 *
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class DatasetMetadata{
	/**
	 * Valores para los identificadores de los metadatos
	 */
	private ArrayList 				metadata = new ArrayList();
	/**
	 * Valores de los metadatos
	 */
	private ArrayList 				values = new ArrayList();
	/**
	 * Valores no data para cada banda de la imagen (si los tiene)
	 */
	private double[]				noDataByBand = null;
	/**
	 * Valor para saber si el NoData esta activo
	 */
	private boolean         noDataEnabled = false;
	/**
	 * Metadatos
	 */
	private String[] 				metadataString = null;
	private DatasetColorInterpretation 	colorInterpr = null;

	public DatasetMetadata(String[] metadata, DatasetColorInterpretation colorInterpr){
		this.colorInterpr = colorInterpr;
		if(metadata == null)
			return;
		metadataString = metadata;
		for(int i = 0;i<metadata.length;i++){
			String[] value = metadata[i].split("=");
			if(value.length >= 2){
				this.metadata.add(value[0]);
				this.values.add(value[1]);
			}
		}
	}

	/**
	 * Crea un objeto TransparencyRange a partir de los rangos de transparencia
	 * @return
	 */
	public TransparencyRange parserNodataByBand(){
		int bandR = colorInterpr.getBand("Red");
		int bandG = colorInterpr.getBand("Green");
		int bandB = colorInterpr.getBand("Blue");

		if(bandR < 0 && bandG < 0 && bandB < 0)
			return null;

		//Esta comprobación es para los raster con paleta (gif). Cuando se trate la paleta y no se usen como
		//imagenes de una banda habrá que cambiar esto
		if((colorInterpr.length() == 1) && colorInterpr.get(0).equals("Palette"))
			return null;

		if(noDataByBand == null)
			return null;

		//Si todos los valores nodata por banda son -1 es que no hay ninguno asignado
		int count =0;
		for(int i = 0; i < noDataByBand.length; i++)
			if(noDataByBand[i] < 0)
				count ++;

		if(count == noDataByBand.length)
			return null;

		TransparencyRange tr = new TransparencyRange();
		int[] red = new int[2];
		int[] green = new int[2];
		int[] blue = new int[2];

		if(bandR >= 0){
			red[0] = red[1] = (int)noDataByBand[bandR];
			tr.setRed(red);
		}
		if(bandG >= 0){
			green[0] = green[1] = (int)noDataByBand[bandG];
			tr.setGreen(green);
		}
		if(bandB >= 0){
			blue[0] = blue[1] = (int)noDataByBand[bandB];
			tr.setBlue(blue);
		}

		tr.setAnd(true);
		tr.loadStrEntryFromValues();

		return tr;
	}

	/**
	 * Parsea los metadatos NODATA_VALUES si existe alguno y devuelve un objeto TransparencyRange.
	 * @param nodata
	 * @return Vector de enteros con los valores RGBA o null si no tiene este metadato o no es parseable
	 * en este formato.
	 */
	public TransparencyRange[] parserNodataInMetadata(){
		//Esta comprobación es para los raster con paleta (gif). Cuando se trate la paleta y no se usen como
		//imagenes de una banda habrá que cambiar esto
		if((colorInterpr.length() == 1) && colorInterpr.get(0).equals("Palette"))
			return null;

		int count = 0;
		for(int i = 0; i < metadata.size(); i++){
			if(((String)metadata.get(i)).equals("NODATA_VALUES"))
				count ++;
		}
		if(count == 0)
			return null;

		TransparencyRange[] trList = new TransparencyRange[count];

		count = 0;
		for(int i = 0; i < metadata.size(); i++){
			TransparencyRange tr = new TransparencyRange();
			int[] red = new int[2];
			int[] green = new int[2];
			int[] blue = new int[2];

			if(((String)metadata.get(i)).equals("NODATA_VALUES")){
				String data = (String)values.get(i);
				String[] dataValues = data.split(" ");
				//int[] values = new int[dataValues.length];
				try{
					red[0] = red[1] = Integer.parseInt(dataValues[0]);
					green[0] = green[1] = Integer.parseInt(dataValues[1]);
					blue[0] = blue[1] = Integer.parseInt(dataValues[2]);
				}catch(NumberFormatException exc){
					return null;
				}
			}
			tr.setAnd(true);
			tr.setRed(red);
			tr.setGreen(green);
			tr.setBlue(blue);
			tr.loadStrEntryFromValues();
			trList[count] = tr;
			count ++;
		}

		return trList;
	}

	/**
	 * Inicializa los valores no data;
	 * @param values
	 */
	public void initNoDataByBand(int values){
		noDataByBand = new double[values];
		for(int i = 0; i < values; i++)
			noDataByBand[i] = -1;
	}

	/**
	 * Asigna un valor para el valor noData por banda
	 * @param band Banda
	 * @param value valor
	 */
	public void setNoDataValue(int band, double value){
		try {
			noDataByBand[band] = value;
		} catch(ArrayIndexOutOfBoundsException ex) {
			//No asignamos el elemento
		}
	}

	/**
	 * Devuelve el valor NoData para una banda en concreto.
	 * @param band
	 * @return
	 */
	public double getNoDataValue(int band) {
		return noDataByBand[band];
	}

	/**
	 * Devuelve el valor NoData en forma de array.
	 * @return
	 */
	public double[] getNoDataValue() {
		return noDataByBand;
	}

	/**
	 * Obtiene los metadatos en forma de vector de cadenas
	 * @return Vector de cadenas en el que cada cadena es atributo=valor
	 */
	public String[] getMetadataString() {
		return metadataString;
	}

	/**
	 * Devuelve si el valor NoData esta activo
	 * @return
	 */
	public boolean isNoDataEnabled() {
		return noDataEnabled;
	}

	/**
	 * Establece si el valor NoData esta activo
	 * @param noDataEnabled
	 */
	public void setNoDataEnabled(boolean noDataEnabled) {
		this.noDataEnabled = noDataEnabled;
	}
}