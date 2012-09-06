/* gvSIG. Sistema de Informaci�n Geogr�fica de la Generalitat Valenciana
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
package org.gvsig.raster.dataset;


/**
 * Caracteristicas de un formato de fichero utilizado para escritura. El contenido 
 * de esta clase es utilizado por el driver para informar de las caracteristicas de cada
 * formato. 
 *
 * 02-jun-2007
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class WriteFileFormatFeatures {
	private String		driverName = null;
	private String		ext = null;
	private int[]		nBandsSupported = new int[]{-1};
	private int[]		dataTypesSupported = new int[]{IBuffer.TYPE_BYTE};
	private Class		driver = null;
	protected Params	driverParams = new Params();
	
	/**
	 * Constructor.
	 * Asigna las caracteristicas del formato de escritura. El tipo de dato se inicializa por
	 * defecto a solo byte. Si recibe como parametro un null el formato acepta por defecto el tipo
	 * byte.
	 * @param name Nombre del driver. En el caso de gdal debe coincidir con el nombre de driver de gdal
	 * @param ext Extensi�n del fichero
	 * @param bands N�mero de bandas m�ximas soportadas en la escritura. Si el valor es menor que 1 soporta cualquier n�mero.
	 * @param dataTypes Array con el tipo de datos que soporta. Cada posici�n del array contiene un entero 
	 * que corresponde con un tipo de dato soportado. Los tipo de datos posibles est�n listados como constante
	 * en la clase IBuffer
	 * @param driver Clase con el driver de la libreria de raster que gestiona el formato.
	 */
	public WriteFileFormatFeatures(String name, String ext, int[] bands, int[] dataTypes, Class driver) {
		this.driverName = name;
		this.ext = ext;
		this.nBandsSupported = bands;
		if(dataTypes != null)
			this.dataTypesSupported = dataTypes;
		this.driver = driver;
	}
	
	public WriteFileFormatFeatures() {
		
	}
	
	 /**
     * Carga los par�metros comunes a todos los drivers en el objeto WriterParams.
     */
    public void loadParams() {
    	driverParams.clear();
    	//Aqu� se definen los par�metros globales a todos los drivers.
    }
    
    /**
     * Obtiene los par�metros del driver.
     * @return WriterParams
     */
    public Params getParams() {
		return driverParams;
	}
    
    /**
     * Asigna los par�metros del driver modificados por el cliente.
     * @param Params
     */
    public void setParams(Params params) {
		this.driverParams = params;
	}
    
	/**
	 * Obtiene la clase del driver que gestiona la escritura de ese tipo de formato
	 * @return Clase del driver
	 */
	public Class getDriver() {
		return driver;
	}
	
	/**
	 * Asigna la clase del driver que gestiona la escritura de ese tipo de formato
	 * @param Clase del driver
	 */
	public void setDriver(Class driver) {
		this.driver = driver;
	}
	
	/**
	 * Obtiene una lista de los tipos de datos soportados. Estos est�n definidos como
	 * constantes en IBuffer.
	 * @return Lista con los tipos de datos soportados
	 */
	public int[] getDataTypesSupported() {
		return dataTypesSupported;
	}
	
	/**
	 * Asigna una lista de los tipos de datos soportados. Estos est�n definidos como
	 * constantes en IBuffer.
	 * @param Lista con los tipos de datos soportados
	 */
	public void setDataTypesSupported(int[] dataTypesSupported) {
		this.dataTypesSupported = dataTypesSupported;
	}
	
	/**
	 * Obtiene el nombre del driver. En el caso de gdal debe coincidir con el nombre de driver de gdal.
	 * @return Nombre del driver
	 */
	public String getDriverName() {
		return driverName;
	}
	
	/**
	 * Asigna el nombre del driver. En el caso de gdal debe coincidir con el nombre de driver de gdal.
	 * @return Nombre del driver
	 */
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}
	
	/**
	 * Obtiene la extensi�n del formato
	 * @return Cadena con la extensi�n del formato
	 */
	public String getExt() {
		return ext;
	}
	
	/**
	 * Asigna la extensi�n del formato
	 * @param ext Cadena con la extensi�n del formato
	 */
	public void setExt(String ext) {
		this.ext = ext;
	}
	
	/**
	 * Obtiene el n�mero de bandas soportadas o -1 si es cualquier n�mero de ellas
	 * @return N�mero de bandas soportadas
	 */
	public int[] getNBandsSupported() {
		return nBandsSupported;
	}
	
	/**
	 * Asigna el n�mero de bandas soportadas o -1 si es cualquier n�mero de ellas
	 * @param N�mero de bandas soportadas
	 */
	public void setNBandsSupported(int[] bandsSupported) {
		nBandsSupported = bandsSupported;
	}
}
