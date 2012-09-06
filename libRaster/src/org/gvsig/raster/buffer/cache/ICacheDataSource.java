
/* gvSIG. Sistema de Informaci�n Geogr�fica de la Generalitat Valenciana
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
package org.gvsig.raster.buffer.cache;

import java.io.IOException;

/**
 * Interfaz que debe implementar una fuente de datos de un cache. Las fuentes
 * de datos deben interpretar la estructura de la cache para que cuando se les
 * solicite una p�gina de la cach� sepan que extent de la fuente de datos deben
 * cargar en el buffer. 
 * 
 * @author Nacho Brodin (nachobrodin@gmail.com)
 *
 */
public interface ICacheDataSource{	
	/**
	 * Carga una p�gina especificada en el par�metro nPag con los datos necesarios. La
	 * petici�n que ha de hacerse se calcula previamente con la estructura de la cach�.
	 *   
	 * @param nPag N�mero de p�gina a cargar
	 * @param pageBuffer Buffer de datos a cargar.
	 */
	public void loadPage(PageBandBuffer pageBuffer) throws InterruptedException;
	
	/**
	 * Salva una p�gina especificada en el par�metro nPag a disco. La
	 * petici�n que ha de hacerse se calcula previamente con la estructura de la cach�.
	 *   
	 * @param nPag N�mero de p�gina a salvar
	 * @param pageBuffer Buffer de datos a salvar.
	 * @throws IOException
	 */
	public void savePage(PageBandBuffer pageBuffer)throws IOException;
	
	/**
	 * Elimina la fuente de datos de disco
	 * @throws IOException
	 */
	public void delete()throws IOException;
	
	/**
	 * Obtiene la ruta a la fuente de datos
	 * @return Cadena con la ruta a la fuente de datos
	 */
	public String getPath();
}
