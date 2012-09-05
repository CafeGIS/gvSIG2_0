/* gvSIG. Geographic Information System of the Valencian Government
 *
 * Copyright (C) 2007-2008 Infrastructures and Transports Department
 * of the Valencian Government (CIT)
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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, 
 * MA  02110-1301, USA.
 * 
 */

/*
 * AUTHORS (In addition to CIT):
 * 2008 PRODEVELOP		Main development
 */

package org.gvsig.geocoding;

import org.gvsig.tools.locator.AbstractLocator;
import org.gvsig.tools.locator.LocatorException;

/**
 * Geocoding locator
 * 
 * @author <a href="mailto:jsanz@prodevelop.es"> Jorge Gaspar Sanz Salinas</a>
 * @author <a href="mailto:vsanjaime@prodevelop.es"> Vicente Sanjaime Calvet</a>
 * 
 */
public class GeocodingLocator extends AbstractLocator {

	private static final String LOCATOR_NAME = "GeocodingLocator";
	public static final String GEOCODER_NAME = "Geocoder"; 	

	/**
	 * Unique instance.
	 */
	private static GeocodingLocator instance;
	
	/**
	 * Private constructor following the Singleton pattern
	 */
	private GeocodingLocator(){
	}

	/**
	 * Return the singleton instance.
	 * 
	 * @return the singleton instance
	 */
	public static GeocodingLocator getInstance() {
		synchronized (GeocodingLocator.class){
			if (instance==null)
				instance = new GeocodingLocator();	
		}
		
		return instance;
	}

	/**
	 * Returns the Locator name.
	 * 
	 * @return String containing the locator name.
	 */
	public String getLocatorName() {
		return LOCATOR_NAME;
	}
	
	/**	
	 * Registers the Class implementing the Geocoder interface
	 * @param clazz
	 */
	@SuppressWarnings("unchecked")
	public static void registerGeocoder (Class clazz){
		getInstance().register(GEOCODER_NAME, clazz);
	}
	
	/**
	 * Return a refrence to Geocoder 
	 * @return a reference to Geocoder
	 * @throws LocatorException
	 */
	public static Geocoder getGeocoder() throws LocatorException{
		GeocodingLocator loc = GeocodingLocator.getInstance();
		Object geoco = loc.get(GEOCODER_NAME);
		return (Geocoder) geoco;
	}

}