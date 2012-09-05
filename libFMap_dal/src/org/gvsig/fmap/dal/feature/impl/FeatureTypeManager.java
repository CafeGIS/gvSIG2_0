/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2005 IVER T.I. and Generalitat Valenciana.
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
 *
 * For more information, contact:
 *
 *  Generalitat Valenciana
 *   Conselleria d'Infraestructures i Transport
 *   Av. Blasco Ibáñez, 50
 *   46010 VALENCIA
 *   SPAIN
 *
 *      +34 963862235
 *   gvsig@gva.es
 *      www.gvsig.gva.es
 *
 *    or
 *
 *   IVER T.I. S.A
 *   Salamanca 50
 *   46005 Valencia
 *   Spain
 *
 *   +34 963163400
 *   dac@iver.es
 */

package org.gvsig.fmap.dal.feature.impl;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.EditableFeature;
import org.gvsig.fmap.dal.feature.EditableFeatureType;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.FeatureAttributeDescriptor;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.dal.feature.FeatureStoreTransform;
import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.fmap.dal.feature.impl.expansionadapter.ExpansionAdapter;
import org.gvsig.fmap.dal.feature.spi.FeatureStoreProvider.FeatureTypeChanged;
import org.gvsig.tools.persistence.PersistenceException;
import org.gvsig.tools.persistence.PersistentState;

/**
 * DOCUMENT ME!
 *
 * @author Vicente Caballero Navarro
 */
public class FeatureTypeManager {
	private ExpansionAdapter expansionAdapter;
	private ArrayList deleted = new ArrayList();// <FeatureID>
	private int deltaSize = 0;
	private HashMap added = new HashMap();
	private HashMap modifiedFromOriginal = new HashMap();
	private FeatureType originalType = null;
	private boolean first = true;
	private FeatureTypeManagerFeatureStoreTransforms transforms;
	private FeatureStore store;

	public FeatureTypeManager(FeatureStore store,
			ExpansionAdapter expansionAdapter) {
		this.expansionAdapter = expansionAdapter;
		this.store = store;
		this.transforms = new FeatureTypeManagerFeatureStoreTransforms();
		this.transforms.setFeatureStore(store);
	}

	public void dispose() {
		this.expansionAdapter.close();
		this.expansionAdapter = null;
		this.deleted.clear();
		this.deleted = null;
		this.transforms.clear();
	}

	// public FeatureType delete(String id) {
	// deleted.add(id);
	// FeatureType type=(FeatureType)added.remove(id);
	// if (type==null) {
	// type=(FeatureType)modifiedFromOriginal.remove(id);
	// }
	// deltaSize--;
	// return type;
	// }

	/**
	 * DOCUMENT ME!
	 *
	 * @param feature
	 *            DOCUMENT ME!
	 */
	// public void add(FeatureType type) {
	// int pos = expansionAdapter.addObject(type);
	// added.put(type.getId(),new Integer(pos));
	// deltaSize++;
	// }
	/**
	 * DOCUMENT ME!
	 *
	 * @param id
	 *            DOCUMENT ME!
	 */
	// public void deleteLastFeatureType() {
	// expansionAdapter.deleteLastObject();
	// FeatureType
	// type=(FeatureType)expansionAdapter.getObject(expansionAdapter.getSize()-1);
	// added.remove(type.getId());
	// modifiedFromOriginal.remove(type.getId());
	// deltaSize--;
	// }
	/**
	 * DOCUMENT ME!
	 *
	 * @param id
	 *            DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 * @throws IsNotFeatureSettingException
	 */
	public FeatureType getType(String id) throws DataException {
		Integer intNum = ((Integer) added.get(id));
		if (intNum == null) {
			intNum = ((Integer) modifiedFromOriginal.get(id));
			if (intNum == null) {
				return null;
			}
		}
		int num = intNum.intValue();

		FeatureType type = (FeatureType) expansionAdapter.getObject(num);
		return type;
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param feature
	 *            DOCUMENT ME!
	 * @param oldFeature
	 *            DOCUMENT ME!
	 * @throws DataException
	 */
	public int update(FeatureType type, FeatureType oldType) {
		// deleted.add(oldType.getId());
		if (first) {
			originalType = oldType;
			first = false;
		}
		int oldNum = -1;
		int num = expansionAdapter.addObject(type);
		String id = type.getId();

		if (added.containsKey(id)) {
			oldNum = ((Integer) added.get(id)).intValue();
			added.put(id, new Integer(num));
		} else {
			if (modifiedFromOriginal.get(id) != null) {
				oldNum = ((Integer) modifiedFromOriginal.get(id)).intValue();
			}
			modifiedFromOriginal.put(id, new Integer(num));
		}

		try {
			this.transforms.add(new UpdateFeatureTypeTransform(this.store,
					type, oldType));
		} catch (DataException e) {
			throw new RuntimeException(); // FIXME (pero esto no deberia de
											// pasar nunca)
		}
		return oldNum;
	}

	private class UpdateFeatureTypeTransform implements FeatureStoreTransform {
		private FeatureType ftSource;
		private FeatureType ftTarget;
		private WeakReference wkRefStore;
		private List ftypes = null;
		private List attrToUse;

		UpdateFeatureTypeTransform(FeatureStore featureStore,
				FeatureType ftSource, FeatureType ftTarget) {
			this.ftSource = ftSource;
			this.ftTarget = ftTarget;
			this.wkRefStore = new WeakReference(featureStore);
			this.initializeAttributesToUse();
		}

		private void initializeAttributesToUse() {
			attrToUse = new ArrayList(ftTarget.size());
			Iterator iter = ftTarget.iterator();
			FeatureAttributeDescriptor tAttr, sAttr;
			while (iter.hasNext()) {
				tAttr = (FeatureAttributeDescriptor) iter.next();
				sAttr = this.ftSource.getAttributeDescriptor(tAttr.getName());
				if (sAttr == null) {
					continue;
				}
				if (tAttr.getDataType() != sAttr.getDataType()) {
					continue;
				}
				attrToUse.add(tAttr.getName());

			}

		}

		public void applyTransform(Feature source, EditableFeature target)
				throws DataException {
			Iterator iter = target.getType().iterator();
			FeatureAttributeDescriptor tAttr;
			String name;
			while (iter.hasNext()) {
				tAttr = (FeatureAttributeDescriptor) iter.next();
				name = tAttr.getName();
				if (this.attrToUse.contains(name)) {
					target.set(name, source.get(name));
				} else {
					target.set(name, tAttr.getDefaultValue());
				}
			}
		}

		public FeatureType getDefaultFeatureType() throws DataException {
			return this.ftTarget;
		}

		public FeatureStore getFeatureStore() {
			return (FeatureStore) this.wkRefStore.get();
		}

		public List getFeatureTypes() throws DataException {
			if (this.ftypes == null) {
				this.ftypes = Arrays
						.asList(new FeatureType[] { this.ftTarget });
			}
			return this.ftypes;
		}

		public FeatureType getSourceFeatureTypeFrom(
				FeatureType targetFeatureType) {
			EditableFeatureType orgType = ftSource.getEditable();
			Iterator iter = orgType.iterator();
			FeatureAttributeDescriptor attr;

			while (iter.hasNext()) {
				attr = (FeatureAttributeDescriptor) iter.next();
				if (!attrToUse.contains(attr.getName())) {
					iter.remove();
				}
			}

			return orgType.getNotEditableCopy();
		}

		public void setFeatureStore(FeatureStore featureStore) {
			this.wkRefStore = new WeakReference(featureStore);
		}

		public PersistentState getState() throws PersistenceException {
			// FIXME
			throw new UnsupportedOperationException();
		}

		public void saveToState(PersistentState state)
				throws PersistenceException {
			// FIXME
			throw new UnsupportedOperationException();
		}

		public void loadFromState(PersistentState state) throws PersistenceException {
			// FIXME
			throw new UnsupportedOperationException();
		}

		public boolean isTransformsOriginalValues() {
			return false;
		}

	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param id
	 *            DOCUMENT ME!
	 */
	public void restore(String id) {
		deleted.remove(id);
		deltaSize++;
	}

	public void restore(String id, int num) {
		if (added.containsKey(id)) {
			added.put(id, new Integer(num));
		} else {
			modifiedFromOriginal.put(id, new Integer(num));
		}
	}

	public boolean isDeleted(FeatureType type) {
		return deleted.contains(type.getId());
	}

	public boolean isDeleted(String id) {
		return deleted.contains(id);
	}

	public void clear() {
		added.clear();
		modifiedFromOriginal.clear();
		expansionAdapter.close();
		deleted.clear();// <FeatureID>
		deltaSize = 0;
	}

	public boolean hasChanges() {
		return added.size() > 0 || modifiedFromOriginal.size() > 0
				|| deleted.size() > 0;
	}

	public Iterator newsIterator() {
		return added.values().iterator();
	}

	public boolean hasNews() {
		return !added.isEmpty();
	}

	public long getDeltaSize() {
		return deltaSize;
	}

	public FeatureType getOriginalFeatureType() {
		return originalType;
	}

	public DefaultFeatureStoreTransforms getTransforms() {
		return this.transforms;
	}

	public class FeatureTypeManagerFeatureStoreTransforms extends
			DefaultFeatureStoreTransforms {

		private FeatureTypeManagerFeatureStoreTransforms() {

		}

		protected void checkEditingMode() {
			return;
		}

		protected void notifyChangeToStore() {
			return;
		}

		public PersistentState getState() throws PersistenceException {
			// FIXME
			throw new UnsupportedOperationException();
		}

		public void loadState(PersistentState state)
				throws PersistenceException {
			// FIXME
			throw new UnsupportedOperationException();
		}

		public void loadFromState(PersistentState state) throws PersistenceException {
			// FIXME
			throw new UnsupportedOperationException();
		}

		public FeatureStoreTransform add(FeatureStoreTransform transform)
				throws DataException {
			if (!(transform instanceof UpdateFeatureTypeTransform)) {
				// FIXME
				throw new IllegalArgumentException();
			}
			return super.add(transform);
		}

	}

	public class FeatureTypesChangedItem implements FeatureTypeChanged {

		private FeatureType source;
		private FeatureType target;

		public FeatureTypesChangedItem(FeatureType source, FeatureType target) {
			this.source = source;
			this.target = target;
		}

		public FeatureType getSource() {
			return source;
		}

		public FeatureType getTarget() {
			return target;
		}

	}

	public Iterator getFeatureTypesChanged() throws DataException {
		// FIXME this don't work for Store.fType.size() > 1
		List list = new ArrayList();
		if (modifiedFromOriginal.size() > 0) {
			FeatureType src = this.getOriginalFeatureType();
			list.add(new FeatureTypesChangedItem(src, this.store
					.getFeatureType(src.getId())));
		}
		return list.iterator();
	}

}
