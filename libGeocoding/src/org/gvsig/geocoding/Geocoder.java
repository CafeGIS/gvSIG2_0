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

import java.util.Set;

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.exception.InitializeException;
import org.gvsig.geocoding.address.Address;
import org.gvsig.geocoding.result.GeocodingResult;
import org.gvsig.tools.locator.LocatorException;

/**
 * Main interface to geocoding service
 *
 * @author <a href="mailto:jsanz@prodevelop.es"> Jorge Gaspar Sanz Salinas</a>
 * @author <a href="mailto:vsanjaime@prodevelop.es"> Vicente Sanjaime Calvet</a>
 * 
 */
public interface Geocoder {

	/**
	 * Geocoding process
	 * @param address
	 * @return
	 * @throws InitializeException
	 * @throws LocatorException
	 * @throws DataException
	 */
	public Set<GeocodingResult> geocode(Address address)
			throws InitializeException, LocatorException, DataException;

}
