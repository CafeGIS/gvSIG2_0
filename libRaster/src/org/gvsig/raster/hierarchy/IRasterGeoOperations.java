package org.gvsig.raster.hierarchy;

import java.awt.geom.AffineTransform;

import org.cresques.cts.IProjection;
import org.gvsig.raster.dataset.RasterDriverException;
import org.gvsig.raster.datastruct.Extent;

/**
 * Interfaz que representa operaciones de imagenes con georreferenciaci�n.
 * 
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public interface IRasterGeoOperations extends IRasterOperations{
	/**
	 * Valor en coordenadas reales de la posici�n m�xima en X
	 * @return double
	 */
	public double getMaxX();
	/**
	 * Valor en coordenadas reales de la posici�n m�xima en Y
	 * @return double
	 */
	public double getMaxY();
	/**
	 * Valor en coordenadas reales de la posici�n m�nima en X
	 * @return double
	 */
	public double getMinX();
	/**
	 * Valor en coordenadas reales de la posici�n m�nima en Y
	 * @return double
	 */
	public double getMinY();
	/**
	 * Ancho del raster en coordenadas del mundo real.
	 * @return
	 */
	public double getWCWidth();
	/**
	 * Alto del raster en coordenadas del mundo real.
	 * @return
	 */
	public double getWCHeight();
	/**
	 * Dice si el raster est� georreferenciado o no.
	 * @return true si est� georreferenciado y false si no lo est�.
	 */
	public boolean isGeoreferenced();
	/**
	 * Obtiene el tama�o de pixel en X por fichero en el dataset
	 * @return double que representa el tama�o de pixel en X
	 */
	//public double getPixelSizeX();
	/**
	 * Obtiene el tama�o de pixel en Y por fichero en el dataset
	 * @return double que representa el tama�o de pixel en Y
	 */
	//public double getPixelSizeY();
	/**
	 * Obtiene el extent completo del raster
	 * @return Extent
	 */
	public Extent getFullRasterExtent();
	/**
	 * Obtiene la proyecci�n en la que est� el raster
	 * @return IProjection
	 */
	public IProjection getProjection();
	
	/**
	 * Obtiene la proyecci�n asociada al dataset.
	 * 
	 * @return Proyecci�n en formato wkt
	 */
	public String getWktProjection() throws RasterDriverException;
	
	/**
	 * Obtiene la matriz de transformaci�n de una banda determinada que transforma puntos den coordenadas reales
	 * en puntos en coordenadas pixel.
	 * @param band
	 * @return AffineTransform
	 */
	public AffineTransform getAffineTransform(int band);
	
	/**
	 * Obtiene la matriz de transformaci�n de una banda determinada que transforma puntos den coordenadas reales
	 * en puntos en coordenadas pixel.
	 * @return AffineTransform
	 */
	public AffineTransform getAffineTransform();
}