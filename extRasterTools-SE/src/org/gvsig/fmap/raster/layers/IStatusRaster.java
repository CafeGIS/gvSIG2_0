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
package org.gvsig.fmap.raster.layers;

import java.util.ArrayList;

import org.gvsig.raster.dataset.NotSupportedExtensionException;
import org.gvsig.raster.dataset.RasterDriverException;
import org.gvsig.raster.datastruct.ColorTable;
import org.gvsig.raster.grid.GridTransparency;
import org.gvsig.raster.grid.filter.FilterTypeException;
import org.gvsig.raster.grid.filter.RasterFilterList;
import org.gvsig.raster.hierarchy.IRasterProperties;

import com.iver.utiles.XMLEntity;
import com.iver.utiles.XMLException;

public interface IStatusRaster{
	
	/**
	 * Recupera en fichero XML algunas propiedades y filtros del raster
	 * @param xml
	 * @throws XMLException
	 */
	public void setXMLEntity(XMLEntity xml, IRasterProperties layer)throws XMLException;
	
	/**
	 * Salva en fichero XML algunas propiedades y filtros de raster
	 * @param xml
	 * @throws XMLException
	 */
	public void getXMLEntity(XMLEntity xml, boolean loadClass, IRasterProperties layer)throws XMLException;
	
	/**
	 * Aplica el estado almacenado al FLyrRaster pasado por parámetro
	 * @param adapter
	 * @throws NotSupportedExtensionException Cuando la extensión no está soportada
	 * @throws RasterDriverException Cuando se produce un error en la lectura del fichero
	 */
	public void applyStatus(FLyrRasterSE layer) throws NotSupportedExtensionException, RasterDriverException, FilterTypeException ;
	
	/**
	 * Obtiene el listado de filtros para poder montar una pila
	 * después de la recuperación desde el XML
	 * @return
	 */
	public ArrayList getFilters();
	
	/**
	 * Obtiene el orden de renderizado de bandas cargado desde el proyecto
	 * después de hacer un setXMLEntity
	 * @return Lista de enteros con el orden de renderizado
	 */
	public int[] getRenderBands();
	
	/**
	 * Obtiene la lista de filtros cargada desde el proyecto después de hacer un 
	 * setXMLEntity
	 * @return RasterFilterList
	 * @throws FilterTypeException 
	 */
	public RasterFilterList getFilterList() throws FilterTypeException;
	
	/**
	 * Obtiene el objeto de transparencia cargado desde el proyecto después de hacer un 
	 * setXMLEntity
	 * @return la lista de filtros
	 */
	public GridTransparency getTransparency();
	
	/**
	 * Obtiene la lista de filtros cargada desde el proyecto después de hacer un 
	 * setXMLEntity. No construye los filtros sino que devuelve una lista de parámetros
	 * @return RasterFilterList
	 */
	public ArrayList getFilterArguments();
	
	/**
	 * Obtiene el ColorTable si hay un filtro de tabla de color en la lista
	 * @return ColorTable
	 * @throws FilterTypeException 
	 */
	public ColorTable getColorTable() throws FilterTypeException;
}
