package org.gvsig.fmap.dal.feature.impl;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.FeatureAttributeDescriptor;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.fmap.dal.feature.spi.FeatureProvider;
import org.gvsig.fmap.dal.feature.spi.FeatureReferenceProviderServices;
import org.gvsig.tools.persistence.PersistenceException;
import org.gvsig.tools.persistence.Persistent;
import org.gvsig.tools.persistence.PersistentState;

public class DefaultFeatureReference implements
		FeatureReferenceProviderServices, Persistent {

	private Object oid;
	private Integer myHashCode = null;
	private Object[] pk;
	private String[] pkNames;
	private WeakReference storeRef;
	private boolean isNewFeature;
	private String featureTypeId;

	public DefaultFeatureReference(DefaultFeature feature) {
		this(feature.getStore(), feature.getData());
	}

	public DefaultFeatureReference(FeatureStore store,
			FeatureProvider fdata) {
		this.isNewFeature = fdata.isNew();
		this.oid = null;
		this.pk = null;
		this.storeRef = new WeakReference(store);
		this.featureTypeId = fdata.getType().getId();

		if (fdata.getType().hasOID() || isNewFeature) {
			this.oid = fdata.getOID();
			if (this.oid == null) {
				// FIXME Exception
				throw new RuntimeException("Missing OID");
			}
		} else {
			this.calculatePK(fdata);
			if (this.pk == null) {
				// FIXME Exception
				throw new RuntimeException("Missing pk attributes");
			}
		}

	}

	/*
	 * Use only for Persistent.setState
	 */
	public DefaultFeatureReference(FeatureStore store) {
		this.isNewFeature = false;
		this.oid = null;
		this.pk = null;
		this.storeRef = new WeakReference(store);
	}

	public DefaultFeatureReference(FeatureStore store, Object oid) {
		// FIXME featureTypeId is needed !!!
		this.isNewFeature = false;
		this.oid = oid;
		this.pk = null;
		this.storeRef = new WeakReference(store);
	}

	private DefaultFeatureStore getStore() {
		return (DefaultFeatureStore) this.storeRef.get();
	}

	private void calculatePK(FeatureProvider fdata) {
		ArrayList keys = new ArrayList();
		ArrayList keyNames = new ArrayList();
		FeatureType type = fdata.getType();
		Iterator it = type.iterator();
		while (it.hasNext()) {
			FeatureAttributeDescriptor attr = (FeatureAttributeDescriptor) it
					.next();
			if (attr.isPrimaryKey()) {
				keys.add(fdata.get(attr.getIndex()));
				keyNames.add(attr.getName());
			}
		}
		if (keys.size() < 1) {
			pk = null;
			pkNames = null;
		} else {
			pk = keys.toArray();
			pkNames = (String[]) keyNames.toArray(new String[0]);
		}
	}

	public Feature getFeature() throws DataException {
		return this.getStore().getFeatureByReference(this);
	}

	public Feature getFeature(FeatureType featureType) throws DataException {
		return this.getStore().getFeatureByReference(this, featureType);
	}

	public Object getOID() {
		return this.oid;
	}

	public boolean isNewFeature() {
		return this.isNewFeature;
	}

	public void saveToState(PersistentState state) throws PersistenceException {
		state.set("oid", oid);
		state.set("isNewFeature", isNewFeature);
		if (pk !=null){
			state.set("pk.elements", pk.length);
			for (int i = 0; i < pk.length; i++) {
				state.set("pk.item" + i, pk[i]);
			}
			for (int i = 0; i < pk.length; i++) {
				state.set("pkName.item" + i, pkNames[i]);
			}

		}

	}

	public void loadFromState(PersistentState state) throws PersistenceException {
		this.oid = state.get("oid");
		this.isNewFeature = state.getBoolean("isNewFeature");
		if (state.get("pk.elements")!=null){
			this.pk = new Object[state.getInt("pk.elements")];
			this.pkNames = new String[state.getInt("pk.elements")];
			for (int i = 0; i < pk.length; i++) {
				this.pk[i] = state.get("pk.item" + i);
			}
			for (int i = 0; i < pk.length; i++) {
				this.pkNames[i] = (String) state.get("pkName.item" + i);
			}
		}
	}

	public boolean equals(Object obj) {
		if (!(obj instanceof DefaultFeatureReference)) {
			return false;
		}
		DefaultFeatureReference other = (DefaultFeatureReference) obj;

		FeatureStore otherStore = (FeatureStore) other.storeRef.get();
		FeatureStore myrStore = (FeatureStore) this.storeRef.get();
		if (otherStore == null || myrStore == null) {
			return false;
		}
		if (!myrStore.equals(otherStore)) {
			return false;
		}
		if (myHashCode != null && other.myHashCode != null) {
			return myHashCode.equals(other.myHashCode);
		}
		if (this.oid != null) {
			return this.oid.equals(other.oid);
		}
		for (int i = 0; i < this.pk.length; i++) {
			if (!this.pk[i].equals(other.pk[i])) {
				return false;
			}
		}
		return true;
	}

	public int hashCode() {
		if (this.oid != null) {
			return this.oid.hashCode();
		}
		if (myHashCode == null) {
			StringBuffer buff = new StringBuffer();

			for (int i = 0; i < this.pk.length; i++) {
				buff.append(this.pk[i].hashCode());
				buff.append("##");
			}
			myHashCode = new Integer(buff.toString().hashCode());
		}
		return myHashCode.intValue();
	}

	public String[] getKeyNames() {
		return pkNames;
	}

	public Object getKeyValue(String name) {
		for (int i = 0; i < pkNames.length; i++) {
			if (pkNames[i].equalsIgnoreCase(name)) {
				return pk[i];
			}
		}
		// FIXME exception????
		return null;
	}

	public String getFeatureTypeId() {
		return featureTypeId;
	}


}
