package org.gvsig.fmap.dal.store.dbf;

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.exception.ReadRuntimeException;
import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.fmap.dal.feature.spi.DefaultFeatureProvider;
import org.gvsig.fmap.dal.feature.spi.FeatureProvider;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.primitive.Envelope;

public class DBFFeatureProvider extends DefaultFeatureProvider {
	protected DBFStoreProvider store;
	protected boolean loading;
	protected boolean loaded;

	public DBFFeatureProvider(DBFStoreProvider store, FeatureType type) {
		super(type);
		this.store = store;
		loading = false;
		loaded = false;
	}

	protected void load() {
		if (loading || loaded || this.isNew()) {
			return;
		}
		loading = true;
		try {
			this.store.loadFeatureProviderByIndex(this);
		} catch (DataException e) {
			throw new ReadRuntimeException("DBFFeatureProvider.load", e);
		} finally {
			loading = false;
			loaded = true;
		}
	}

	public void set(int i, Object value) {
		this.load();
		super.set(i, value);
	}

	public void set(String name, Object value) {
		this.load();
		super.set(featureType.getIndex(name), value);
	}

	public Object get(int i) {
		this.load();
		return super.get(i);
	}

	public Object get(String name) {
		this.load();
		return super.get(name);
	}

	public Geometry getDefaultGeometry() {
		return null;
	}

	public Envelope getDefaultEnvelope() {
		return null;
	}

	public void setOID(Object oid) {
		this.loaded = false;
		super.setOID(oid);
	}

	public FeatureProvider getCopy() {
		this.load();
		return super.getCopy();
	}




}
