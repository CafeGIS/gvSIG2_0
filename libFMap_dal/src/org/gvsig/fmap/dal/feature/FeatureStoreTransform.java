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
* 2008 IVER T.I. S.A.   {{Task}}
*/

package org.gvsig.fmap.dal.feature;

import java.util.List;

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.tools.persistence.Persistent;

/**
 * A FeatureStoreTransform provides a mechanism for mapping a source FeatureType
 * to a target FeatureType, allowing to build different views (as in database
 * view) over different feature types even from different stores.
 *
 * @author jmvivo
 *
 */
public interface FeatureStoreTransform extends Persistent {

	/**
	 * Returns the default {@link FeatureType}.
	 *
	 * @return default {@link FeatureType}
	 *
	 * @throws DataException
	 */
	public FeatureType getDefaultFeatureType() throws DataException;

	/**
	 * Returns this FeatureStoreTransform's FeatureType(s)
	 * @return
	 * @throws DataException
	 */
	public List getFeatureTypes() throws DataException;

	/**
	 * Returns the original store {@link FeatureType} that replaces
	 * targetFeatureType of this FeatureStoreTransform's
	 *
	 * @return source {@link FeatureType}
	 * @throws DataException
	 */
	public FeatureType getSourceFeatureTypeFrom(FeatureType targetFeatureType);

	/**
	 * Applies this transform between two features, copying the source data to
	 * the target feature.
	 *
	 * @param source
	 *            feature whose data will be used as source
	 *
	 * @param target
	 *            feature in which the source data will be copied
	 *
	 * @throws DataException
	 */
	public void applyTransform(Feature source, EditableFeature target)
			throws DataException;

	/**
	 * Sets the FeatureStore to which this transform is applied.
	 *
	 * @param featureStore
	 *            FeatureStore to which this transform is applied.
	 */
	public void setFeatureStore(FeatureStore featureStore);

	/**
	 * Returns the FeatureStore to which this transform belongs.
	 *
	 * @return FeatureStore to which this transform belongs.
	 */
	public FeatureStore getFeatureStore();

	/**
	 * Retruns true if this make changes of any attributes values or false if
	 * only {@link FeatureType} definitions is changed.
	 *
	 * @see {@link FeatureStoreTransforms#isTransformsOriginalValues()}
	 * 
	 * @return
	 */
	public boolean isTransformsOriginalValues();
}
