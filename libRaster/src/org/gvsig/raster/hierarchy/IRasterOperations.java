package org.gvsig.raster.hierarchy;

import org.gvsig.raster.dataset.properties.DatasetMetadata;

/**
 * Interfaz de operaciones sobre una capa raster.
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public interface IRasterOperations {
	/**
	 * Obtiene el Ancho en pixels
	 * @return double que representa el ancho
	 */
	public double getPxWidth();
	/**
	 * Obtiene el Alto en pixels
	 * @return double que representa el alto
	 */
	public double getPxHeight();
	/**
	 * Obtiene el número de bandas del raster.
	 * @return Entero que informa de la suma del número de bandas de todos
	 * los ficheros que componen el raster.
	 */
	public int getBandCount();
	/**
	 * Obtiene una lista con el número de bandas del raster para todos los datasets.
	 * @return Lista de enteros que informa de las bandas de todos los ficheros
	 * que componen un raster.
	 */
	public int[] getBandCountFromDataset();
	/**
	 * Obtiene el tipo de dato por banda.
	 * @return Array de enteros que representa el tipo de dato por banda.
	 */
	public int[] getDataType();
	/**
	 * Obtiene el objeto que contiene los metadatos. Este método debe ser redefinido por los
	 * drivers si necesitan devolver metadatos. 
	 * @return
	 */
	public DatasetMetadata[] getMetadata();
	/**
	* Interpretación de color asociada a una banda de un dataset.
	* @return Cadena que representa la interpretación de color
	*/
	public String getColorInterpretation(int band, int dataset);
}