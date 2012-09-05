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

import org.gvsig.fmap.dal.exception.InitializeException;
import org.gvsig.fmap.dal.feature.FeatureAttributeDescriptor;
import org.gvsig.fmap.dal.feature.FeatureIndex;
import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.fmap.dal.feature.exception.FeatureIndexException;
import org.gvsig.fmap.dal.feature.spi.FeatureStoreProviderServices;


/**
 * SPI for all index implementations
 * @author jyarza
 *
 */
public interface FeatureIndexProviderServices extends FeatureIndex {
	
	/** Initializes this provider */
	public void initialize() throws InitializeException;
	
	/** Column to which belongs this index */
	public FeatureAttributeDescriptor getFeatureAttributeDescriptor();
	
	/** FeatureType to which belongs this index */
	public FeatureType getFeatureType();
	
	/** FeatureStore to which belongs this index */
	public FeatureStoreProviderServices getFeatureStoreProviderServices();
	
	/** Returns the absolute path (directory + filename) where this index is or will be stored */
	public String getFileName();
	
	/** Returns a temporary absolute path (directory + filename) according to the system environment */
	public String getTemporaryFileName();
	
	/** Calculates and returns a new filename for an index, using the given prefix and suffix */
	public String getNewFileName(String prefix, String sufix);
	
	/** Fills this index with the store's data */
	public void fill() throws FeatureIndexException;	
}

