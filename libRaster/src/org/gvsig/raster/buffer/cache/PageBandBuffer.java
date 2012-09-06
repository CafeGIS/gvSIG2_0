
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
package org.gvsig.raster.buffer.cache;

import java.io.IOException;

import org.gvsig.raster.buffer.RasterMemoryBuffer;

/**
 * Esta clase representa una página de cache. Una página no es más que un buffer de datos
 * con todas sus funcionalidades con un uso especifico, el de ser una página. 
 * 
 * @author Nacho Brodin (nachobrodin@gmail.com)
 *
 */
public class PageBandBuffer extends RasterMemoryBuffer{

	/**
	 * Número de banda que corresponde a este PageBandBuffer
	 */
	private int numBand = 0;
	private HddPage[] hddPages = null;
	
	/**
	 * Constructor.
	 * @param dataType Tipo de dato
	 * @param width Ancho de página
	 * @param height Alto de página
	 * @param bandNr Número de bandas
	 * @param malloc true para reservar de memoria en el buffer 
	 */
	public PageBandBuffer(int dataType, int width, int height, int bandNr, boolean malloc, int numBand){
		super(dataType, width, height, bandNr, malloc);
		this.numBand = numBand;
	}
	
	/**
	 * Asigna la lista de paginas de disco
	 * @param hddList Lista de páginas de disco
	 */
	public void setHddPages(HddPage[] hddList){
		this.hddPages = hddList;
	}
	
	/**
	 * Carga una página especificada en el parámetro nPag con los datos necesarios.
	 * Para esto utiliza el dataSource.
	 *   
	 * @param nPag Número de página a cargar
	 */
	public void loadPage(int nPag) throws InterruptedException{
		hddPages[nPag].getDataServer(numBand).loadPage(this);
	}
	
	/**
	 * Salva una página especificada en el parámetro nPag a disco. 
	 * Para esto utiliza el dataSource
	 *   
	 * @param nPag Número de página a salvar
	 * @throws IOException
	 */
	public void savePage(int nPag) throws IOException{
		hddPages[nPag].getDataServer(numBand).savePage(this);
	}

}
