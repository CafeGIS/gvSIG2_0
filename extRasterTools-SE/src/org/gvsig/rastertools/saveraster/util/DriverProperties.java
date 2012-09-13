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
package org.gvsig.rastertools.saveraster.util;

import org.gvsig.raster.dataset.GeoRasterWriter;

/**
 * Clase que representa a las propiedades que se seleccionan de cada driver.
 * Inicialmente se resetea el array driversProps a null. Cada elemento de este
 * array representa una extensión de fichero soportada. Cuando se modifica
 * las propiedades para una determinada extensión de un driver estas se asignan
 * a su entrada del vector driversExtensionProps y desde aquí son leidas para
 * ser restauradas en caso de volver el cuadro de dialogo de propiedades.
 *  
 * @author Nacho Brodin (nachobrodin@gmail.com)
 *
 */
public class DriverProperties{

	private String[][]				driversExtensionProps = null;
	private String[]				driversExtension = null;

	public DriverProperties(){

		driversExtensionProps = new String[GeoRasterWriter.getNDrivers()][];
		for(int i = 0; i < GeoRasterWriter.getNDrivers(); i++)
			driversExtensionProps[i] = null;
		driversExtension = GeoRasterWriter.getDriversExtensions();

	}

	/**
	 * Asigna un vector de propiedades de driver de escritura.
	 * @param ext	extensión del driver.
	 * @param props	propiedades.
	 */
	public void setProperties(String ext, String[] props){
		for(int i = 0; i < driversExtension.length; i++){
			if(ext.equals(driversExtension[i])){
				driversExtensionProps[i] = props;
			}
		}
	}

	/**
	 * Obtiene un vector de propiedades de driver de escritura.
	 * @param ext	extensión del driver.
	 * @return	propiedades.
	 */
	public String[] getProperties(String ext){
		for(int i = 0; i < driversExtension.length; i++){
			if(ext.equals(driversExtension[i])){
				return driversExtensionProps[i];
			}
		}
		return null;
	}

	/**
	 * Para una extensión de fichero determinada obtiene el valor de una propiedad si existe este.
	 * La extensión solicitadad deberá estar registrada
	 * @param extension Extensión del fichero.
	 * @param property Propiedad a recuperar.
	 * @return Valor de la propiedad o null en caso de que la extensión de fichero no exista o la propiedad
	 * no se encuentre.
	 */
	public String getValue(String extension, String property) {
		String[] props = getProperties(extension);
		if(props != null){
			for (int i = 0; i < props.length; i++) {
				if(props[i].startsWith(property + "="))
					return props[i].substring(props[i].lastIndexOf("=") + 1, props[i].length());
			}
		}
		return null;
	}
}