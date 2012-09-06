package org.gvsig.raster.hierarchy;

import org.gvsig.raster.dataset.IRasterDataSource;
import org.gvsig.raster.dataset.NotSupportedExtensionException;
import org.gvsig.raster.dataset.RasterDriverException;

/**
 * Interfaz que ofrece información sobre un dataset raster formado por uno o 
 * multiples ficheros a través de objetos  
 * 
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public interface IRasterDataset extends IRasterFile{		
	/**
	 * Obtiene el dataset que corresponde al raster.
	 * @return MultiRasterDataset
	 */
	public IRasterDataSource getDataSource();
	/**
	 * Añade un dataset al dataset multiple
	 * @param dataset
	 * @throws NotSupportedExtensionException Cuando la extensión no está soportada
	 * @throws RasterDriverException Cuando se produce un error en la lectura del fichero
	 */
	public void addFile(String pathName) throws NotSupportedExtensionException, RasterDriverException;
	/**
	 * Elimina un dataset de la lista
	 * @param pathName
	 */
	public void delFile(String pathName);
	/**
	 * Obtiene información registrada en el dataset a partir de una clave.
	 * Esta llamada es util para devolver información que no es contenida en la 
	 * librería de raster y que puede ser registrada por la aplicación que hace uso 
	 * de esta.
	 * @param key Nombre del parámetro registrado
	 * @return Objeto registrado con la clave proporcionada
	 */
	public Object getInfo(String key);
}