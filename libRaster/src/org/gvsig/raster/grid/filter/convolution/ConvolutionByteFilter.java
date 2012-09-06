/* gvSIG. Sistema de Información Geogrfica de la Generalitat Valenciana
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

package org.gvsig.raster.grid.filter.convolution;

import org.gvsig.raster.buffer.RasterBuffer;
import org.gvsig.raster.dataset.IBuffer;

/**
 * Filtro de convolucion para Buffer de tipo Byte
 * @author Alejandro Muñoz        <alejandro.munoz@uclm.es>
 * @author Diego Guerrero Sevilla  <diego.guerrero@uclm.es>
 * */
public class ConvolutionByteFilter extends ConvolutionFilter {

	public ConvolutionByteFilter(){
		super();
	}

	/**
	 * @param Kernel a aplicar. En caso de que no se trate de un kernel definido en ConvolutionFilter, se puede pasar como
	 * parametro el kernel se pretende aplicar.
	 * **/
	public ConvolutionByteFilter(Kernel k){
		super();
		super.kernel=k;
	}


	public void pre(){
		super.pre();
		rasterResult = RasterBuffer.getBuffer(IBuffer.TYPE_BYTE, raster.getWidth(), raster.getHeight(), raster.getBandCount(), true);
	}

	/** Aplicacion del filtro para el pixel de la posicion line, col */
	public void process(int col, int line) {
		int lado = kernel.getLado();
		int semiLado = (lado - 1) >> 1;
		double ventana[][] = new double[lado][lado];
		double resultConvolution = 0;
		for (int band = 0; band < raster.getBandCount(); band++) {
/*
			TODO: Mirar de cambiar el sistema de recorrido de pixels, para tener
			metodos circulares, repetitivos o de descarte.

			int posx = 0;
			int posy = 0;
			for (int j = -semiLado; j <= semiLado; j++) {
				for (int i = -semiLado; i <= semiLado; i++) {
					posx = col + i;
					posy = line + j;
					if (posx < 0) posx=0;
					if (posy < 0) posy=0;
					if (posx >= width) posx=width - 1;
					if (posy >= height) posy=height - 1;

					ventana[i + semiLado][j + semiLado] = raster.getElemByte(posy, posx, band) & 0xff;
				}
			}
*/

			if ((col - semiLado >= 0) && (line - semiLado >= 0) && (col + semiLado < width) && (line + semiLado < height)) {
				for (int j = -semiLado; j <= semiLado; j++)
					for (int i = -semiLado; i <= semiLado; i++)
						ventana[i + semiLado][j + semiLado] = raster.getElemByte(line + j, col + i, band) & 0xff;
				Kernel Kventana = new Kernel(ventana);

				resultConvolution = kernel.convolution(Kventana);
				if (resultConvolution > 255)
					resultConvolution = 255;
				else
					if (resultConvolution < 0)
						resultConvolution = 0;

				rasterResult.setElem(line, col, band, (byte) resultConvolution);
			} else
				rasterResult.setElem(line, col, band, (byte) raster.getElemByte(line, col, band));
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
			return this.rasterResult;
		return null;
	}
}