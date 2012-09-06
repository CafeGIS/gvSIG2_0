package org.gvsig.raster.hierarchy;

/**
 * Interfaz de operaciones sobre fichero.
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public interface IRasterFile{
	/**
	 * Obtiene el Tamaño de cada fichero de que consta el raster en bytes. 
	 * @return long que representa el tamaño
	 */
	public long[] getFileSize();
	/**
	 * Obtiene el nombre de de cada fichero de que consta el raster. En caso de estar en disco
	 * debe ofrecerse con la ruta completa.
	 * @return Cadena con el nombre del fichero.
	 */
	public String[] getFileName();
	/**
	 * Obtiene el número de ficheros de que consta el raster.
	 * @return Número de ficheros.
	 */
	public int getFileCount();
	/**
	 * Formato del fichero raster. Puede devolver la extensión correspondiente al
	 * fichero
	 * @return String que representa el formato
	 */
	public String getFileFormat();
}