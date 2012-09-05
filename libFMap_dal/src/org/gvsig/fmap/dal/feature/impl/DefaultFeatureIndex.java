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


package org.gvsig.fmap.dal.feature.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.gvsig.fmap.dal.DataTypes;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.exception.InitializeException;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.FeatureAttributeDescriptor;
import org.gvsig.fmap.dal.feature.FeatureReference;
import org.gvsig.fmap.dal.feature.FeatureSet;
import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.fmap.dal.feature.exception.FeatureIndexException;
import org.gvsig.fmap.dal.feature.spi.DefaultLongList;
import org.gvsig.fmap.dal.feature.spi.FeatureReferenceProviderServices;
import org.gvsig.fmap.dal.feature.spi.FeatureStoreProviderServices;
import org.gvsig.fmap.dal.feature.spi.index.FeatureIndexProvider;
import org.gvsig.fmap.dal.feature.spi.index.FeatureIndexProviderServices;

/**
 * Default feature index provider services.
 *
 * @author jyarza
 *
 */
public class DefaultFeatureIndex implements FeatureIndexProviderServices {

	private final FeatureStoreProviderServices featureStore;
	private final FeatureType featureType;
	private final String attributeName;
	private final String indexName;
	private final int dataType;
	private final FeatureIndexProvider indexProvider;
	private List attributeNames;

	public DefaultFeatureIndex(FeatureStoreProviderServices featureStore, FeatureType featureType, FeatureIndexProvider indexProvider, String attributeName, String indexName) {
		if (featureStore == null) {
			throw new IllegalArgumentException("featureStore cannot be null.");
		}
		if (featureType == null) {
			throw new IllegalArgumentException("featureType cannot be null.");
		}
		if (attributeName == null) {
			throw new IllegalArgumentException("attributeName cannot be null.");
		}
		if (indexName == null) {
			throw new IllegalArgumentException("indexName cannot be null.");
		}

		// FIXME Esto debe ir al provider
		if (featureStore.getProvider().getOIDType() != DataTypes.INT &&
				featureStore.getProvider().getOIDType() != DataTypes.LONG) {
			throw new IllegalArgumentException();
		}

		FeatureAttributeDescriptor attr = featureType.getAttributeDescriptor(attributeName);
		if (attr == null) {
			throw new IllegalArgumentException("Attribute " + attributeName +" not found in FeatureType " + featureType.toString());
		}

		this.featureStore = featureStore;
		this.featureType = featureType;
		this.attributeName = attributeName;
		this.indexName = indexName;
		this.dataType = attr.getDataType();
		this.indexProvider = indexProvider;

		attributeNames = new ArrayList();
		attributeNames.add(attributeName);
	}

	public final FeatureAttributeDescriptor getFeatureAttributeDescriptor() {
		return featureType.getAttributeDescriptor(attributeName);
	}

	public final FeatureStoreProviderServices getFeatureStoreProviderServices() {
		return featureStore;
	}

	public final FeatureType getFeatureType() {
		return featureType;
	}

	public final String getName() {
		return indexName;
	}

	public final String getAttributeName() {
		return attributeName;
	}

	public final int getDataType() {
		return dataType;
	}

	/**
	 * Fills this index with all the data in its FeatureStore
	 */
	public final void fill() throws FeatureIndexException {
		try {
			insert(getFeatureStoreProviderServices().getFeatureStore()
					.getFeatureSet());
		} catch (DataException e) {
			throw new FeatureIndexException(e);
		}
	}

	public final void insert(FeatureSet data) throws DataException {
		Iterator it = data.iterator();
		while (it.hasNext()) {
			Feature feat = (Feature) it.next();
			insert(feat);
		}
	}

	public void insert(Feature feat) {
		try {
			indexProvider.insert(feat.get(attributeName),
					(FeatureReferenceProviderServices) feat.getReference());
		} catch (NullPointerException e) {
			throw new IllegalArgumentException("Feature does not contain a column with name " + attributeName);
		} catch (ClassCastException e) {
			throw new IllegalArgumentException("Attribute data type is not valid.");
		}
	}

	public FeatureSet getMatchFeatureSet(Object value)
			throws FeatureIndexException {
		return new IndexFeatureSet(this, new DefaultLongList(indexProvider
				.match(value)));
	}

	public FeatureSet getRangeFeatureSet(Object value1, Object value2)
			throws FeatureIndexException {
		return new IndexFeatureSet(this, new DefaultLongList(indexProvider
				.range(value1, value2)));
	}

	public FeatureSet getNearestFeatureSet(int count, Object value)
			throws FeatureIndexException {
		return new IndexFeatureSet(this, new DefaultLongList(indexProvider
				.nearest(count, value)));
	}

	public FeatureSet getNearestFeatureSet(int count, Object value,
			Object tolerance) throws FeatureIndexException {
		return new IndexFeatureSet(this, new DefaultLongList(indexProvider
				.nearest(count, value, tolerance)));
	}

	public void initialize() throws InitializeException {
		indexProvider.setFeatureIndexProviderServices(this);
		indexProvider.initialize();
	}

	public void delete(Object value, FeatureReference fref) {
		indexProvider.delete(value, (FeatureReferenceProviderServices) fref);
	}

	public void delete(Feature feat) {
		indexProvider.delete(feat.get(this.attributeName),
				(FeatureReferenceProviderServices) feat.getReference());
	}

	public void delete(FeatureSet data) throws FeatureIndexException {
		try {
			Iterator it = data.iterator();
			while (it.hasNext()) {
				Feature feat = (Feature) it.next();
				delete(feat);
			}
		} catch (DataException e) {
			throw new FeatureIndexException(e);
		}
	}

	public List getAttributeNames() {
		return attributeNames;
	}

	public String getNewFileName(String prefix, String sufix) {
		int n=0;
		File file = new File(prefix + getName(), sufix);
		while (file.exists()) {
			n++;
			file = new File(prefix + getName() + n, sufix);
		}
		return file.getAbsolutePath();
	}

	public String getFileName() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getTemporaryFileName() {
		// TODO Auto-generated method stub
		return null;
	}

	public FeatureIndexProvider getFeatureIndexProvider() {
		return this.indexProvider;
	}

}

