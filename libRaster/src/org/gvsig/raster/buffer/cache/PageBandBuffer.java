
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

import org.gvsig.raster.buffer.RasterMemoryBuffer;

/**
 * Esta clase representa una p�gina de cache. Una p�gina no es m�s que un buffer de datos
 * con todas sus funcionalidades con un uso especifico, el de ser una p�gina. 
 * 
 * @author Nacho Brodin (nachobrodin@gmail.com)
 *
 */
public class PageBandBuffer extends RasterMemoryBuffer{

	/**
	 * N�mero de banda que corresponde a este PageBandBuffer
	 */
	private int numBand = 0;
	private HddPage[] hddPages = null;
	
	/**
	 * Constructor.
	 * @param dataType Tipo de dato
	 * @param width Ancho de p�gina
	 * @param height Alto de p�gina
	 * @param bandNr N�mero de bandas
	 * @param malloc true para reservar de memoria en el buffer 
	 */
	public PageBandBuffer(int dataType, int width, int height, int bandNr, boolean malloc, int numBand){
		super(dataType, width, height, bandNr, malloc);
		this.numBand = numBand;
	}
	
	/**
	 * Asigna la lista de paginas de disco
	 * @param hddList Lista de p�ginas de disco
	 */
	public void setHddPages(HddPage[] hddList){
		this.hddPages = hddList;
	}
	
	/**
	 * Carga una p�gina especificada en el par�metro nPag con los datos necesarios.
	 * Para esto utiliza el dataSource.
	 *   
	 * @param nPag N�mero de p�gina a cargar
	 */
	public void loadPage(int nPag) throws InterruptedException{
		hddPages[nPag].getDataServer(numBand).loadPage(this);
	}
	
	/**
	 * Salva una p�gina especificada en el par�metro nPag a disco. 
	 * Para esto utiliza el dataSource
	 *   
	 * @param nPag N�mero de p�gina a salvar
	 * @throws IOException
	 */
	public void savePage(int nPag) throws IOException{
		hddPages[nPag].getDataServer(numBand).savePage(this);
	}

}
