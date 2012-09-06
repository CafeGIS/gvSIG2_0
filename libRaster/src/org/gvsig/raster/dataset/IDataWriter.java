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


/**
 * Este interfaz debe ser implementado por las clases que vayan a
 * servir datos a los drivers de escritura.
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public interface IDataWriter {
	/**
	 * Para el tipo de datos ARGB (32 bits en un solo entero) obtiene un array
	 * que representa los datos leídos. Este array se obtendrá de la fuente de
	 * datos asociada al renderizador y sera de ancho sizeX y alto sizeY. Es el
	 * propio renderizador el encargado de llevar la posición de la última línea
	 * leída para la siguiente petición ofrecer datos a partir de donde se
	 * quedo.
	 * 
	 * @param sizeX
	 *            Ancho del bloque de datos
	 * @param sizeY
	 *            Alto del bloque de datos
	 * @return Array con los datos leidos
	 * @throws OutOfMemoryError
	 * @throws InterruptedException
	 */
	public int[] readARGBData(int sizeX, int sizeY, int nBand)
			throws InterruptedException, OutOfMemoryError;

	/**
	 * Para el tipo de datos byte obtiene un array bidimensional donde la primera dimensión
	 * son las bandas y la segunda los datos. Este array se obtendra de la fuente de datos
	 * asociada al renderizador y sera de ancho sizeX y alto sizeY. Es el propio renderizador
	 * el encargado de llevar la posición de la última línea leída para la siguiente petición
	 * ofrecer datos a partir de donde se quedo.
	 * @param sizeX Ancho del bloque de datos
	 * @param sizeY Alto del bloque de datos
	 * @return Array bidimensionar de numero de bandas por datos leidos
	 */
    public byte[][] readByteData(int sizeX, int sizeY);
	/**
	 * Para el tipo de datos short obtiene un array bidimensional donde la primera dimensión
	 * son las bandas y la segunda los datos. Este array se obtendra de la fuente de datos
	 * asociada al renderizador y sera de ancho sizeX y alto sizeY. Es el propio renderizador
	 * el encargado de llevar la posición de la última línea leída para la siguiente petición
	 * ofrecer datos a partir de donde se quedo.
	 * @param sizeX Ancho del bloque de datos
	 * @param sizeY Alto del bloque de datos
	 * @return Array bidimensionar de numero de bandas por datos leidos
	 */
    public short[][] readShortData(int sizeX, int sizeY);
	/**
	 * Para el tipo de datos int obtiene un array bidimensional donde la primera dimensión
	 * son las bandas y la segunda los datos. Este array se obtendra de la fuente de datos
	 * asociada al renderizador y sera de ancho sizeX y alto sizeY. Es el propio renderizador
	 * el encargado de llevar la posición de la última línea leída para la siguiente petición
	 * ofrecer datos a partir de donde se quedo.
	 * @param sizeX Ancho del bloque de datos
	 * @param sizeY Alto del bloque de datos
	 * @return Array bidimensionar de numero de bandas por datos leidos
	 */
    public int[][] readIntData(int sizeX, int sizeY);
	/**
	 * Para el tipo de datos float obtiene un array bidimensional donde la primera dimensión
	 * son las bandas y la segunda los datos. Este array se obtendra de la fuente de datos
	 * asociada al renderizador y sera de ancho sizeX y alto sizeY. Es el propio renderizador
	 * el encargado de llevar la posición de la última línea leída para la siguiente petición
	 * ofrecer datos a partir de donde se quedo.
	 * @param sizeX Ancho del bloque de datos
	 * @param sizeY Alto del bloque de datos
	 * @return Array bidimensionar de numero de bandas por datos leidos
	 */
    public float[][] readFloatData(int sizeX, int sizeY);
	/**
	 * Para el tipo de datos double obtiene un array bidimensional donde la primera dimensión
	 * son las bandas y la segunda los datos. Este array se obtendra de la fuente de datos
	 * asociada al renderizador y sera de ancho sizeX y alto sizeY. Es el propio renderizador
	 * el encargado de llevar la posición de la última línea leída para la siguiente petición
	 * ofrecer datos a partir de donde se quedo.
	 * @param sizeX Ancho del bloque de datos
	 * @param sizeY Alto del bloque de datos
	 * @return Array bidimensionar de numero de bandas por datos leidos
	 */
    public double[][] readDoubleData(int sizeX, int sizeY);
}
