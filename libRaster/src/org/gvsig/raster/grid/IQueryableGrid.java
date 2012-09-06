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
package org.gvsig.raster.grid;

import org.gvsig.raster.buffer.RasterBufferInvalidAccessException;

/**
 * Interfaz que deben implementar los grid en los que se puede consultar sus datos.
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public interface IQueryableGrid {	  

	/**
	 * Obtiene el valor m�ximo del grid
	 * @return Valor m�nimo
	 */
	public double getMinValue() throws GridException;
	
	/**
	 * Obtiene el valor m�ximo del grid
	 * @return Valor m�ximo
	 */
	public double getMaxValue() throws GridException;
	
	/**
	 * Obtiene el valor m�dio del grid
	 * @return Valor medio
	 */
	public double getMeanValue() throws GridException;
	
	/**
	 * Obtiene la varianza
	 * @return Varianza
	 */
	public double getVariance() throws GridException;
	
	/**
	 * Obtiene el valor de una celda de tipo byte. Si el punto excede los l�mites
	 * del grid devuelve un valor NoData. La excepci�n ser� lanzada si intentamos 
	 * acceder a un tipo de dato erroneo.
	 * @param x Posici�n X a recuperar
	 * @param y Posici�n Y a recuperar
	 * @throws RasterBufferInvalidAccessException
	 * @return Valor byte
	 */
	public byte getCellValueAsByte(int x, int y)throws GridException;
	
	/**
	 * Obtiene el valor de una celda de tipo short. Si el punto excede los l�mites
	 * del grid devuelve un valor NoData. La excepci�n ser� lanzada si intentamos 
	 * acceder a un tipo de dato erroneo.
	 * @param x Posici�n X a recuperar
	 * @param y Posici�n Y a recuperar
	 * @throws RasterBufferInvalidAccessException
	 * @return Valor short
	 */
	public short getCellValueAsShort(int x, int y)throws GridException;
	
	/**
	 * Obtiene el valor de una celda de tipo int. Si el punto excede los l�mites
	 * del grid devuelve un valor NoData. La excepci�n ser� lanzada si intentamos 
	 * acceder a un tipo de dato erroneo.
	 * @param x Posici�n X a recuperar
	 * @param y Posici�n Y a recuperar
	 * @throws RasterBufferInvalidAccessException
	 * @return Valor int
	 */
	public int getCellValueAsInt(int x, int y)throws GridException;

	/**
	 * Obtiene el valor de una celda de tipo float. Si el punto excede los l�mites
	 * del grid devuelve un valor NoData. La excepci�n ser� lanzada si intentamos 
	 * acceder a un tipo de dato erroneo.
	 * @param x Posici�n X a recuperar
	 * @param y Posici�n Y a recuperar
	 * @throws RasterBufferInvalidAccessException
	 * @return Valor float
	 */
	public float getCellValueAsFloat(int x, int y)throws GridException;
	
	/**
	 * Obtiene el valor de una celda de tipo double. Si el punto excede los l�mites
	 * del grid devuelve un valor NoData. La excepci�n ser� lanzada si intentamos 
	 * acceder a un tipo de dato erroneo.
	 * @param x Posici�n X a recuperar
	 * @param y Posici�n Y a recuperar
	 * @throws RasterBufferInvalidAccessException
	 * @return Valor double
	 */
	public double getCellValueAsDouble(int x, int y)throws GridException;
	
	/**
	 * Asigna el m�todo de interpolaci�n. Si el lector no es interpolado se instanciar� 
	 * como interpolado sin necesidad de llamar a switchToInterpolationMethod. Los m�todos
	 * de interpolaci�n soportados est�n definidos en la clase GridInterpolated como variables
	 * est�ticas.
	 * 
	 * @param iMethod
	 */
	public void setInterpolationMethod(int iMethod);
	
	/**
	 * Consulta al grid si el valor pasado por par�metro coincide con el valor NoData del 
	 * buffer. 
	 * @param dValue valor para comparar con el NoData del buffer
	 * @return true si el valor pasado es NoData y false si no lo es
	 */
	public boolean isNoDataValue(double dValue);
	
	/**
	 * Obtiene la extensi�n de la ventana del raster accedida por el grid 
	 * @return Objeto GridExtent con la extensi�n.
	 */
	public GridExtent getGridExtent();
	
	/**
	 * Consulta si un punto est� dentro del grid o fuera de el
	 * @param x Coordenada X del punto a consultar
	 * @param y Coordenada Y del punto a consultar 
	 * @return true si el punto est� dentro del grid y false si est� fuera de �l
	 */
	public boolean isInGrid(int x, int y);
		
	/**
	 * Obtiene el ancho en p�xeles del grid 
	 * @return entero que representa el ancho en p�xeles
	 */
	public int getNX();
	
	/**
	 * Obtiene el alto en p�xeles del grid 
	 * @return entero que representa el alto en p�xeles
	 */
	public int getNY();
	
	/**
	 * Obtiene el ancho en p�xeles del dataset completo
	 * @return entero que representa el ancho en p�xeles del dataset completo
	 */
	public int getLayerNX();
	
	/**
	 * Obtiene el alto en p�xeles del dataset completo
	 * @return entero que representa el alto en p�xeles del dataset completo
	 */
	public int getLayerNY();
	
	/**
	 * Tama�o de celda
	 * @return
	 */
	public double getCellSize();
	
	/**
	 * Valor NODATa asociado al grid
	 * @return
	 */
	public double getNoDataValue();
	
	/**
	 * Obtiene la pendiente de un �rea de 3x3 p�xeles que tiene como 
	 * centro las coordenadas que se le indican
	 * @param x Coordenada pixel X
	 * @param y Coordenada pixel Y
	 * @return 
	 * @throws GridException
	 */
	public double getSlope(int x, int y)throws GridException;
			
	public double getAspect(int x, int y)throws GridException;
	
	public double getDistToNeighborInDir(int iDir);
	
	public int getDirToNextDownslopeCell(int x, int y)throws GridException;
	
	public int getDirToNextDownslopeCell(int x, int y, boolean bForceDirToNoDataCell)throws GridException;
	
	public GridCell[] getSortedArrayOfCells()throws GridException;	
}
