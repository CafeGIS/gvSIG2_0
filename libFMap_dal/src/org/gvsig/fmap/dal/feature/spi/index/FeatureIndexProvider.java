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
* 2008 {{Company}}   {{Task}}
*/
 

package org.gvsig.fmap.dal.feature.spi.index;

import java.util.List;

import org.gvsig.fmap.dal.exception.InitializeException;
import org.gvsig.fmap.dal.feature.exception.FeatureIndexException;
import org.gvsig.fmap.dal.feature.spi.FeatureReferenceProviderServices;


public interface FeatureIndexProvider {
	
	/** Sets the IndexProviderServices that will provide application services to this index provider */
	public void setFeatureIndexProviderServices(FeatureIndexProviderServices services);
	
	/** Initializes this provider */
	public void initialize() throws InitializeException;

	/** Inserts a value into the index */
	public void insert(Object value, FeatureReferenceProviderServices fref);

	/** Deletes a value from the index, given its reference */
	public void delete(Object value, FeatureReferenceProviderServices fref);
	
	/** Performs a search in the index and returns a list with all the values that match the given value 
	 * @throws FeatureIndexException TODO*/
	public List match(Object value) throws FeatureIndexException;
	
	/** Performs a search in the index and returns a list with the values that intersect with the given interval 
	 * @throws FeatureIndexException TODO*/
	public List range(Object value1, Object value2) throws FeatureIndexException;
	
	/** Performs a search in the index and returns the list of up to n values which are nearest to the given value. */
	public List nearest(int count, Object value) throws FeatureIndexException;	

	/** Performs a search in the index and returns the list of up to n values which are nearest to the given value and within the distance specified by tolerance. */
	public List nearest(int count, Object value, Object tolerance) throws FeatureIndexException;	
	
	
	/** Returns true if the provider supports the match function */
	public boolean isMatchSupported();
	/** Returns true if the provider supports the range function */
	public boolean isRangeSupported();
	/** Returns true if the provider supports the nearest function */
	public boolean isNearestSupported();
	/** Returns true if the provider supports the nearest with tolerance function */
	public boolean isNearestToleranceSupported();

}

