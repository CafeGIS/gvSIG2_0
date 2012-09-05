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
 

package org.gvsig.fmap.dal.feature;

import java.util.List;

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.exception.FeatureIndexException;


/**
 * This interface represents a local index on feature based data.
 * 
 * All indexes are stored in local files. Creating server 
 * side indexes is not supported.
 * 
 * 
 * @author jyarza
 *
 */
public interface FeatureIndex {
	
	/** Index name */
	public String getName();
	
	/** Attribute names */
	public List getAttributeNames();
	
	/** Data type */
	public int getDataType();
	
	/**
	 * Inserts a Feature in the index.
	 * The Feature must contain a column that matches this index's column (name and data type)
	 * @param feat
	 */
	public void insert(Feature feat);

	/**
	 * Inserts a FeatureSet into this index
	 * FeatureType is not checked so it will accept any FeatureType
	 * as long as exists a column with a valid name
	 */	
	public void insert(FeatureSet data) throws DataException;
	
	/**
	 * Deletes a Feature in the index.
	 * The Feature must contain a column that matches this index's column (name and data type)
	 * @param feat
	 */
	public void delete(Feature feat);

	/**
	 * Deletes a FeatureSet from this index
	 * FeatureType is not checked so it will accept any FeatureType
	 * as long as exists a column with a valid name
	 */	
	public void delete(FeatureSet data) throws FeatureIndexException;	
	
	/**
	 * Returns a FeatureSet with the set of values that match the given value. 
	 * 
	 * @param value 
	 * 			is the value to match.
	 * @return
	 * 		a FeatureSet containing the values in the index that match the given value.
	 * 
	 * @throws FeatureIndexException
	 */
	public FeatureSet getMatchFeatureSet(Object value)
			throws FeatureIndexException;

	/**
	 * Returns a FeatureSet with the set of values that belong to the range defined by value1 and value2.
	 * 
	 * @param value1
	 * 			range lower limit.
	 * 
	 * @param value2
	 * 			range higher limit.
	 * 
	 * @return
	 * 		a FeatureSet with the set of values that belong to the range defined by value1 and value2.
	 * 
	 * @throws FeatureIndexException
	 */
	public FeatureSet getRangeFeatureSet(Object value1, Object value2)
			throws FeatureIndexException;

	/**
	 * Returns a FeatureSet with the set of up to <code>count</code> values that are nearest to the given value.
	 * 
	 * @param count
	 * 			maximum number of values that their resulting FeatureSet will return
	 * 
	 * @param value
	 * 			the value around which the nearest <code>count</code> will be looked up.
	 * 
	 * @return
	 * 		a FeatureSet with the set of up to <code>count</code> values that are nearest to the given value.
	 * 
	 * @throws FeatureIndexException
	 */
	public FeatureSet getNearestFeatureSet(int count, Object value)
			throws FeatureIndexException;

	/**
	 * Returns a FeatureSet with the set of up to <code>count</code> values whose distance to the given value
	 * is not greater than <code>tolerance</code>
	 * 
	 * @param count
	 * 			maximum number of values that their resulting FeatureSet will return
	 * 
	 * @param value
	 * 			the value around which the nearest <code>count</code> will be looked up.
	 * 
	 * @param tolerance
	 * 			maximum distance from the given value.
	 * 
	 * @return
	 * 		a FeatureSet with the set of up to <code>count</code> values that are nearest to the given value.
	 * 
	 * @throws FeatureIndexException
	 */
	public FeatureSet getNearestFeatureSet(int count, Object value, 
			Object tolerance) throws FeatureIndexException;

	
}
