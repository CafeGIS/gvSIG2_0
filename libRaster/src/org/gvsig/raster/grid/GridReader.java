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
package org.gvsig.raster.grid;

import org.gvsig.raster.RasterLibrary;
import org.gvsig.raster.buffer.BufferFactory;
import org.gvsig.raster.buffer.RasterBuffer;
import org.gvsig.raster.buffer.RasterBufferInvalidAccessException;
import org.gvsig.raster.buffer.RasterBufferInvalidException;
import org.gvsig.raster.dataset.IBuffer;

/**
 * Clase abstracta para lectura de datos de un grid. Esta puede ser creada a partir de
 * una fuente de datos (BufferFactory) o a partir de un buffer de datos (RasterBuffer).
 * Tiene las operaciones básicas de consulta.
 * 
 * @author Nacho Brodin (nachobrodin@gmail.com)
 *
 */
public abstract class GridReader {
	
	protected BufferFactory 	bufferFactory  = null;
	protected RasterBuffer 		rasterBuf      = null;
	protected int 				dataType       = IBuffer.TYPE_UNDEFINED;
	protected int[]				bands          = null;
	protected int				bandToOperate  = 0;
	protected double            defaultNoData  = RasterLibrary.defaultNoDataValue;
	
	/**
	 * Extent de la ventana que corresponde al Grid 
	 */
	GridExtent windowExtent;
	/**
	 * Extent completo de la capa
	 */
	GridExtent layerExtent;
	
	/**
	 * Crea un objeto lector a partir de un buffer de datos y el extent.
	 * @param rb Buffer de datos
	 * @param layerExtent extent de la capa completa
	 * @param windowExtent Extent
	 * @param bands Número de bandas del origen
	 */
	public GridReader(	RasterBuffer rb, 
						GridExtent layerExtent,
						GridExtent windowExtent,
						int[] bands){
		rasterBuf = rb;
		dataType = rb.getDataType();
		this.bands = bands;
		this.windowExtent = windowExtent;
		this.layerExtent = layerExtent;
	}
	
	/**
	 * Crea un objeto lector a partir de una fuente de datos y el extent.
	 * @param rb Buffer de datos
	 * @param layerExtent extent de la capa completa
	 * @param windowExtent Extent
	 * @param bands Número de bandas del origen
	 */
	public GridReader(	BufferFactory bf,
						GridExtent layerExtent,
					   	GridExtent windowExtent,
						int[] bands){
		
		bufferFactory = bf;
		rasterBuf = (RasterBuffer)bufferFactory.getRasterBuf();
		if(bufferFactory.getDataSource() != null && bufferFactory.getDataSource().getDataType() != null)
			dataType = bufferFactory.getDataSource().getDataType()[0];
		this.bands = bands;
		this.windowExtent = windowExtent;
		this.layerExtent = layerExtent;
	}
	
	/**
	 * Obtiene el valor de tipo byte de una celda. La posición x e y de la celda a 
	 * recuperar está definida en los parámetros
	 * @param x Posición X del valor que queremos recuperar
	 * @param y Posición Y del valor que queremos recuperar
	 * @return Valor de tipo byte contenido en la posición especificada
	 */
	public abstract byte getCellValueAsByte(int x, int y) throws RasterBufferInvalidAccessException, RasterBufferInvalidException ;
	
	/**
	 * Obtiene el valor de tipo short de una celda. La posición x e y de la celda a 
	 * recuperar está definida en los parámetros
	 * @param x Posición X del valor que queremos recuperar
	 * @param y Posición Y del valor que queremos recuperar
	 * @return Valor de tipo short contenido en la posición especificada
	 */
	public abstract short getCellValueAsShort(int x, int y) throws RasterBufferInvalidAccessException, RasterBufferInvalidException ;
	
	/**
	 * Obtiene el valor de tipo int de una celda. La posición x e y de la celda a 
	 * recuperar está definida en los parámetros
	 * @param x Posición X del valor que queremos recuperar
	 * @param y Posición Y del valor que queremos recuperar
	 * @return Valor de tipo int contenido en la posición especificada
	 */
	public abstract int getCellValueAsInt(int x, int y) throws RasterBufferInvalidAccessException, RasterBufferInvalidException ;
	
	/**
	 * Obtiene el valor de tipo float de una celda. La posición x e y de la celda a 
	 * recuperar está definida en los parámetros
	 * @param x Posición X del valor que queremos recuperar
	 * @param y Posición Y del valor que queremos recuperar
	 * @return Valor de tipo float contenido en la posición especificada
	 */
	public abstract float getCellValueAsFloat(int x, int y) throws RasterBufferInvalidAccessException, RasterBufferInvalidException ;
	
	/**
	 * Obtiene el valor de tipo double de una celda. La posición x e y de la celda a 
	 * recuperar está definida en los parámetros
	 * @param x Posición X del valor que queremos recuperar
	 * @param y Posición Y del valor que queremos recuperar
	 * @return Valor de tipo double contenido en la posición especificada
	 */
	public abstract double getCellValueAsDouble(int x, int y) throws RasterBufferInvalidAccessException, RasterBufferInvalidException ;
	
	/**
	 * Obtiene el valor de tipo double de una celda. La posición x e y de la celda a 
	 * recuperar está definida en los parámetros
	 * @param x Posición X del valor que queremos recuperar
	 * @param y Posición Y del valor que queremos recuperar
	 * @return Valor de tipo double contenido en la posición especificada
	 */
	public abstract double getCellValue(int x, int y) throws RasterBufferInvalidAccessException, RasterBufferInvalidException ;
	
	/**
	 * Obtiene el valor de tipo byte de una celda en todas las bandas. 
	 * La posición x e y de la celda a recuperar está definida en los parámetros. 
	 * @param x Posición X del valor que queremos recuperar
	 * @param y Posición Y del valor que queremos recuperar
	 * @return Array de tipo byte con el contenido de las bandas en la posición especificada
	 */
	public abstract byte[] getBandsValuesAsByte(int x, int y) throws RasterBufferInvalidAccessException, RasterBufferInvalidException;
	
	/**
	 * Obtiene el valor de tipo short de una celda en todas las bandas. 
	 * La posición x e y de la celda a recuperar está definida en los parámetros
	 * @param x Posición X del valor que queremos recuperar
	 * @param y Posición Y del valor que queremos recuperar
	 * @return Array de tipo short con el contenido de las bandas en la posición especificada
	 */
	public abstract short[] getBandsValuesAsShort(int x, int y) throws RasterBufferInvalidAccessException, RasterBufferInvalidException ;
	
	/**
	 * Obtiene el valor de tipo int de una celda en todas las bandas. 
	 * La posición x e y de la celda a recuperar está definida en los parámetros
	 * @param x Posición X del valor que queremos recuperar
	 * @param y Posición Y del valor que queremos recuperar
	 * @return Array de tipo int con el contenido de las bandas en la posición especificada
	 */
	public abstract int[] getBandsValuesAsInt(int x, int y) throws RasterBufferInvalidAccessException, RasterBufferInvalidException ;
	
	/**
	 * Obtiene el valor de tipo float de una celda en todas las bandas. 
	 * La posición x e y de la celda a recuperar está definida en los parámetros
	 * @param x Posición X del valor que queremos recuperar
	 * @param y Posición Y del valor que queremos recuperar
	 * @return Array de tipo float con el contenido de las bandas en la posición especificada
	 */
	public abstract float[] getBandsValuesAsFloat(int x, int y) throws RasterBufferInvalidAccessException, RasterBufferInvalidException ;
	
	/**
	 * Obtiene el valor de tipo double de una celda en todas las bandas. 
	 * La posición x e y de la celda a recuperar está definida en los parámetros
	 * @param x Posición X del valor que queremos recuperar
	 * @param y Posición Y del valor que queremos recuperar
	 * @return Array de tipo double con el contenido de las bandas en la posición especificada
	 */
	public abstract double[] getBandsValuesAsDouble(int x, int y)  throws RasterBufferInvalidAccessException, RasterBufferInvalidException ;
	
	public boolean isNoDataValue (double dValue){
		if(rasterBuf != null)
			return (dValue == rasterBuf.getNoDataValue());
		else
			return (dValue == defaultNoData);
	}
	
	public double getNoDataValue(){
		if(rasterBuf != null)
			return rasterBuf.getNoDataValue();
		else 
			return defaultNoData;
	}
	
	public int getNY() {
		return windowExtent.getNY();
	}
	
	public int getNX() {
		return windowExtent.getNX();
	}
	
	public double getCellSize(){
		return windowExtent.getCellSize();
	}
	
	public GridExtent getGridExtent(){
		return windowExtent;
	}
	
	public boolean isCellInGrid(int iX, int iY){
		return (iX >= 0 && iX < getNX() && iY >= 0 && iY < getNY());
	}
	
	/**
	 * Asigna la banda sobre la que se realizan las operaciones. Por defecto es la banda 0
	 * con lo que para el uso de MDTs no habrá que modificar este valor.
	 * @param band Banda sobre la que se realizan las operaciones.
	 */
	public void setBandToOperate(int band){
		this.bandToOperate = band;
	}
}
