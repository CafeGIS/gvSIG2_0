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
package org.gvsig.raster.dataset.rmf;

import java.io.IOException;

/**
 * Interfaz que deben implementar las clases que escriben bloques XML en ficheros RMF.
 * Contiene métodos para escribir y leer un bloque de información del fichero RMF. La clase
 * que implementa este interfaz es la encargada de serializar un objeto en cadenas XML y 
 * crear este desde cadenas XML. Esto lo hará a través de los métodos de este interfaz para
 * que el gestor de RMF pueda administrar la escritura y lectura del fichero.
 * 
 * 21-abr-2007
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public interface IRmfBlock {
	
	/**
	 * Lee el bloque XML pasado por parámetro y carga el objeto correspondiente
	 * con los datos.
	 * @param xml. Texto XML que representa el objeto.
	 */
	public void read(String xml) throws ParsingException;
	
	/**
	 * Obtiene un bloque XML que representa a las propiedades del objeto a 
	 * serializar.
	 * @return Texto XML que representa el objeto.
	 */
	public String write() throws IOException;
	
	/**
	 * Obtiene el tag principal del bloque
	 * @return Tag principal
	 */
	public String getMainTag();
	
	/**
	 * Obtiene el objeto resultante de haber aplicado un read.
	 * @return Object del tipo correspondiente al bloque implementado.
	 */
	public Object getResult();
}
