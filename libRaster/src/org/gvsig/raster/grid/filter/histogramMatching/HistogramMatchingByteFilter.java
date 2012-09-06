/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
	 *
	 * Copyright (C) 2006 Instituto de Desarrollo Regional and Generalitat Valenciana.
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
	 *
	 * For more information, contact:
	 *
	 *  Generalitat Valenciana
	 *   Conselleria d'Infraestructures i Transport
	 *   Av. Blasco Ibañez, 50
	 *   46010 VALENCIA
	 *   SPAIN
	 *
	 *      +34 963862235
	 *   gvsig@gva.es
	 *      www.gvsig.gva.es
	 *
	 *    or
	 *
	 *   Instituto de Desarrollo Regional (Universidad de Castilla La-Mancha)
	 *   Campus Universitario s/n
	 *   02071 Alabacete
	 *   Spain
	 *
	 *   +34 967 599 200
	 */
package org.gvsig.raster.grid.filter.histogramMatching;


import org.gvsig.raster.buffer.RasterBuffer;

/**
 * Filtro para la aplicación de HistogramMatching a un raster de tipo byte.
 * 
 * @author aMuÑoz (alejandro.munoz@uclm.es)
 * @version 27-5-2008
 * 
 * */

public class HistogramMatchingByteFilter extends  HistogramMatchingFilter {
	
	/** 
	 * Filtro de Histogram Matching par imagenes de tipo byte
	 * */	
	public HistogramMatchingByteFilter (){
		super();	
	}

	/**
	 * Ejecucion del filtro tipo byte
	 * */
	public void process(int x, int y) {
		int index=0;
		for(int band=0; band<rasterResult.getBandCount();band++){
			int data= (int) (raster.getElemByte(y,x,band));
			index = data & 0xff;
			rasterResult.setElem(y,x,band,(byte)tableAsign[band][index]);
		}
	}

	/**
	 * @return  tipo de dato del buffer de entrada
	 * */

	public int getInRasterDataType() {
		return RasterBuffer.TYPE_BYTE;
	}


	/**
	 * @return tipo de dato del buffer de salida
	 * */
	public int getOutRasterDataType() {
		return RasterBuffer.TYPE_BYTE;
	}

	
	/**
	 * @return  buffer resultante tras aplicar el filtro
	 * */
	public Object getResult(String name) {
		if (name.equals("raster"))
			return rasterResult;
		return null;
	}
	
} // Fin de la clase