package org.gvsig.fmap.dal.feature.spi;

import org.gvsig.fmap.dal.feature.EditableFeatureType;
import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.fmap.dal.feature.impl.DefaultFeatureType;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.primitive.Envelope;

public class DefaultFeatureProvider implements FeatureProvider {

	protected FeatureType featureType;
	protected boolean[] nulls;
	protected Object[] values;
	protected Geometry defaultGeometry;
	protected Envelope envelope;
	private Object oid;
	private boolean isNew=false;

	public DefaultFeatureProvider(FeatureType type) {
		if (type instanceof EditableFeatureType) {
			throw new IllegalArgumentException("type can't by editable.");
		}
		this.featureType = (DefaultFeatureType) type;
		this.values = new Object[featureType.size()];
		this.nulls = new boolean[featureType.size()];

		this.envelope = null;
		this.defaultGeometry = null;
		this.oid = null;
	}

	public DefaultFeatureProvider(FeatureType type, Object oid) {
		this(type);
		this.oid = oid;
	}
	public void set(int i, Object value) {
		if (featureType.getDefaultGeometryAttributeIndex() == i) {
			defaultGeometry = (Geometry) value;
			envelope = null;
		}
		if (value == null) {
			nulls[i] = true;
			values[i] = featureType.getAttributeDescriptor(i).getDefaultValue();
		} else {
			values[i] = value;
			nulls[i] = false;
		}
	}

	public void set(String name, Object value) {
		set(featureType.getIndex(name), value);
	}

	public Object get(int i) {
		return values[i];
	}

	public Object get(String name) {
		int i = featureType.getIndex(name);
		return values[i];
	}

	public FeatureType getType() {
		return featureType;
	}

	public FeatureProvider getCopy() {
		DefaultFeatureProvider data = new DefaultFeatureProvider(
				this.getType());
		return getCopy(data);
	}

	protected FeatureProvider getCopy(DefaultFeatureProvider data) {
		data.oid = this.oid;
		System.arraycopy(this.values, 0, data.values, 0, this.values.length);
		data.defaultGeometry = this.defaultGeometry;
		data.envelope = this.envelope;
		data.isNew=this.isNew;
		return data;
	}

	public Envelope getDefaultEnvelope() {
		if (envelope == null && defaultGeometry != null) {
			envelope = defaultGeometry.getEnvelope();
		}
		return envelope;
	}

	public Geometry getDefaultGeometry() {
		return this.defaultGeometry;
	}

	public void setDefaultEnvelope(Envelope envelope) {
		this.envelope = envelope;
	}

	public void setDefaultGeometry(Geometry geom) {
		int i = featureType.getDefaultGeometryAttributeIndex();
		defaultGeometry = geom;
		envelope = null;
		values[i] = geom;
	}

	public boolean isNull(int i) {
		return nulls[i];
	}

	public boolean isNull(String name) {
		int i = featureType.getIndex(name);
		return isNull(i);
	}

	public Object getOID() {
		return this.oid;
	}

	public void setOID(Object oid) {
		this.oid = oid;
	}
	public boolean isNew(){
		return isNew;
	}
	public void setNew(boolean isNew){
		this.isNew=isNew;
	}
}
