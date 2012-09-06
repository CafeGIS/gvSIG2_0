package org.gvsig.raster.hierarchy;

/**
 * Interfaz de operaciones sobre fichero.
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public interface IRasterFile{
	/**
	 * Obtiene el Tama�o de cada fichero de que consta el raster en bytes. 
	 * @return long que representa el tama�o
	 */
	public long[] getFileSize();
	/**
	 * Obtiene el nombre de de cada fichero de que consta el raster. En caso de estar en disco
	 * debe ofrecerse con la ruta completa.
	 * @return Cadena con el nombre del fichero.
	 */
	public String[] getFileName();
	/**
	 * Obtiene el n�mero de ficheros de que consta el raster.
	 * @return N�mero de ficheros.
	 */
	public int getFileCount();
	/**
	 * Formato del fichero raster. Puede devolver la extensi�n correspondiente al
	 * fichero
	 * @return String que representa el formato
	 */
	public String getFileFormat();
}