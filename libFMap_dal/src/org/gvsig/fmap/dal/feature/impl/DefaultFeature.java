package org.gvsig.fmap.dal.feature.impl;

import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.cresques.cts.IProjection;
import org.gvsig.fmap.dal.DataTypes;
import org.gvsig.fmap.dal.exception.DataEvaluatorRuntimeException;
import org.gvsig.fmap.dal.feature.EditableFeature;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.FeatureAttributeDescriptor;
import org.gvsig.fmap.dal.feature.FeatureReference;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.fmap.dal.feature.exception.IllegalValueException;
import org.gvsig.fmap.dal.feature.exception.SetReadOnlyAttributeException;
import org.gvsig.fmap.dal.feature.spi.FeatureProvider;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.primitive.Envelope;
import org.gvsig.tools.evaluator.Evaluator;
import org.gvsig.tools.evaluator.EvaluatorData;
import org.gvsig.tools.evaluator.EvaluatorException;

public class DefaultFeature implements Feature, EvaluatorData {

	protected FeatureProvider data;
	protected FeatureReference reference;
	private WeakReference storeRef;

	/*
	 * Usar con mucha precaucion o mejor no usar. Lo precisa el
	 * DefaultFeatureSet en la ordenacion.
	 */
	public DefaultFeature(FeatureStore store) {
		this.storeRef = new WeakReference(store);
		this.reference = null;
	}

	public DefaultFeature(FeatureStore store, FeatureProvider data) {
		this.data = data;
		this.storeRef = new WeakReference(store);
		this.reference = null;
	}

	DefaultFeature(DefaultFeature feature) {
		this.data = feature.data.getCopy();
		this.storeRef = feature.storeRef;
		this.reference = feature.reference;
	}

	public void setData(FeatureProvider data) {
		this.data = data;
		this.reference = null;
	}

	public FeatureProvider getData() {
		return this.data;
	}

	void set(FeatureAttributeDescriptor attribute, Object value) {
		int i = attribute.getIndex();

		if (attribute.isReadOnly()) {
			throw new SetReadOnlyAttributeException();
		}

		if (attribute.getEvaluator() != null) {
			throw new SetReadOnlyAttributeException();
		}

		if (value == null) {
			if (!attribute.allowNull()) {
				if (!attribute.isAutomatic()) {
					throw new IllegalValueException(attribute, value);
				}
			}
			this.data.set(i, null);
			return;

		}

		if (attribute.getObjectClass().isInstance(value)) {
			this.data.set(i, value);
			return;
		}

		if ((Number.class.isAssignableFrom(attribute.getObjectClass()) && Number.class.isAssignableFrom(value.getClass()))
				|| (attribute.getDataType()==DataTypes.STRING && Number.class.isAssignableFrom(value.getClass()))) {
			Object number=getNumberByType((Number)value, attribute.getDataType());
			this.data.set(i, number);
			return;
		}

		if (!(value instanceof String)) {
			throw new IllegalValueException(attribute, value);
		}
		int dataType = attribute.getDataType();

		switch (dataType) {
		case DataTypes.BYTE:
			this.data.set(i, Byte.valueOf((String) value));
			break;

		case DataTypes.DATE:
			try {
				this.data.set(i, attribute.getDateFormat()
						.parse((String) value));
			} catch (ParseException e) {
				throw new IllegalValueException(attribute, value, e);
			}
			break;

		case DataTypes.DOUBLE:
			this.data.set(i, Double.valueOf((String) value));
			break;

		case DataTypes.FLOAT:
			this.data.set(i, Float.valueOf((String) value));
			break;

		case DataTypes.INT:
			this.data.set(i, Integer.valueOf((String) value));
			break;

		case DataTypes.LONG:
			this.data.set(i, Long.valueOf((String) value));
			break;

		default:
			throw new IllegalValueException(attribute, value);
		}
	}

	private Object getNumberByType(Number value, int type) {
		if (type==DataTypes.DOUBLE){
			return new Double(value.doubleValue());
		}else if (type==DataTypes.FLOAT){
			return new Float(value.floatValue());
		}else if (type==DataTypes.LONG){
			return new Long(value.longValue());
		}else if (type==DataTypes.INT){
			return new Integer(value.intValue());
		}else if (type==DataTypes.STRING){
			return value.toString();
		}
		return value;
	}

	public void initializeValues() {
		FeatureType type = this.getType();
		Iterator iterator = type.iterator();

		while (iterator.hasNext()) {
			FeatureAttributeDescriptor attribute = (FeatureAttributeDescriptor) iterator
			.next();
			if (attribute.isAutomatic() || attribute.isReadOnly()
					|| attribute.getEvaluator() != null) {
				continue;
			}
			if (attribute.getDefaultValue() == null && !attribute.allowNull()) {
				continue;
			}
			this.set(attribute, attribute.getDefaultValue());
		}
	}

	public void initializeValues(Feature feature) {
		FeatureType myType=this.getType();
		FeatureType type =feature.getType();
		Iterator iterator = type.iterator();

		while (iterator.hasNext()) {
			FeatureAttributeDescriptor attribute = (FeatureAttributeDescriptor) iterator
			.next();
			FeatureAttributeDescriptor myAttribute=myType.getAttributeDescriptor(attribute.getName());
			if (myAttribute != null) {
				this.set(myAttribute, feature.get(attribute.getIndex()));
			}
		}
	}

	public FeatureStore getStore() {
		return (FeatureStore) this.storeRef.get();
	}

	public FeatureType getType() {
		return this.data.getType();
	}

	public EditableFeature getEditable() {
		return new DefaultEditableFeature(this);
	}

	public Feature getCopy() {
		return new DefaultFeature(this);
	}

	public FeatureReference getReference() {
		if (this.reference == null) {
			this.reference = new DefaultFeatureReference(this);
		}
		return this.reference;
	}

	public void validate(int mode) {
		((DefaultFeatureType) this.data.getType()).validateFeature(this, mode);
	}

	public List getSRSs() {
		// TODO Auto-generated method stub
		return null;
	}

	public Envelope getDefaultEnvelope() {
		return this.data.getDefaultEnvelope();
	}

	public Geometry getDefaultGeometry() {
		return this.data.getDefaultGeometry();
	}

	public IProjection getDefaultSRS() {
		return this.data.getType().getDefaultSRS();
	}

	public List getGeometries() {
		// TODO Auto-generated method stub
		return null;
	}

	public Object get(String name) {
		return this.get(this.data.getType().getIndex(name));
	}

	public Object get(int index) {
		if (!this.data.getType().hasEvaluators()) {
			return this.data.get(index);
		}
		FeatureAttributeDescriptor attribute = this.data.getType()
		.getAttributeDescriptor(index);
		Evaluator eval = attribute.getEvaluator();
		if (eval == null) {
			return this.data.get(index);
		} else {
			Object value = this.data.get(index);
			if (value != null) { // FIXME: para comprobar si esta calculado usar
									// un array
									// especifico.
				return this.data.get(index);
			}
			try {
				value = eval.evaluate(this);
			} catch (EvaluatorException e) {
				throw new DataEvaluatorRuntimeException(e);
			}
			this.data.set(index, value);
			return value;
		}
	}

	public Object[] getArray(String name) {
		return this.getArray(this.data.getType().getIndex(name));
	}

	public Object[] getArray(int index) {
		return (Object[]) this.get(index);
	}

	public boolean getBoolean(String name) {
		return this.getBoolean(this.data.getType().getIndex(name));
	}

	public boolean getBoolean(int index) {
		Boolean value = (Boolean) this.get(index);
		if (value == null) {
			return false;
		}
		return value.booleanValue();
	}

	public byte getByte(String name) {
		return this.getByte(this.data.getType().getIndex(name));
	}

	public byte getByte(int index) {
		Byte value = (Byte) this.get(index);
		if (value == null) {
			return 0;
		}
		return value.byteValue();
	}

	public Date getDate(String name) {
		return this.getDate(this.data.getType().getIndex(name));
	}

	public Date getDate(int index) {
		return (Date) this.get(index);
	}

	public double getDouble(String name) {
		return this.getDouble(this.data.getType().getIndex(name));
	}

	public double getDouble(int index) {
		Double value = (Double) this.get(index);
		if (value == null) {
			return 0;
		}
		return value.doubleValue();
	}

	public Feature getFeature(String name) {
		return this.getFeature(this.data.getType().getIndex(name));
	}

	public Feature getFeature(int index) {
		return (Feature) this.get(index);
	}

	public float getFloat(String name) {
		return this.getFloat(this.data.getType().getIndex(name));
	}

	public float getFloat(int index) {
		Float value = (Float) this.get(index);
		if (value == null) {
			return 0;
		}
		return value.floatValue();
	}

	public Geometry getGeometry(String name) {
		return this.getGeometry(this.data.getType().getIndex(name));
	}

	public Geometry getGeometry(int index) {
		return (Geometry) this.get(index);
	}

	public int getInt(String name) {
		return this.getInt(this.data.getType().getIndex(name));
	}

	public int getInt(int index) {
		Integer value = (Integer) this.get(index);
		if (value == null) {
			return 0;
		}
		return value.intValue();
	}

	public long getLong(String name) {
		return this.getLong(this.data.getType().getIndex(name));
	}

	public long getLong(int index) {
		Long value = (Long) this.get(index);
		if (value == null) {
			return 0;
		}
		return value.longValue();
	}

	public String getString(String name) {
		return this.getString(this.data.getType().getIndex(name));
	}

	public String getString(int index) {
		return (String) this.get(index);
	}

	public Object getContextValue(String name) {
		name = name.toLowerCase();
		if (name.equals("store")) {
			return this.getStore();
		}

		if (name.equals("featuretype")) {
			return this.data.getType();
		}

		if (name.equals("feature")) {
			return this;
		}

		throw new IllegalArgumentException(name);
	}

	public Iterator getDataNames() {
		class DataNamesIterator implements Iterator {
			Iterator attributeIteraror;

			DataNamesIterator(DefaultFeature feature) {
				this.attributeIteraror = feature.getType().iterator();
			}

			public boolean hasNext() {
				return this.attributeIteraror.hasNext();
			}

			public Object next() {
				return ((FeatureAttributeDescriptor) this.attributeIteraror
						.next()).getName();
			}

			public void remove() {
				throw new UnsupportedOperationException();
			}

		}
		return new DataNamesIterator(this);
	}

	public Object getDataValue(String name) {
		name = name.toLowerCase();
		return get(name);
	}

	public Iterator getDataValues() {
		class DataValuesIterator implements Iterator {
			DefaultFeature feature;
			int current = 0;

			DataValuesIterator(DefaultFeature feature) {
				this.feature = feature;
			}

			public boolean hasNext() {
				return current < feature.getType().size() - 1;
			}

			public Object next() {
				return feature.get(current++);
			}

			public void remove() {
				throw new UnsupportedOperationException();
			}

		}
		return new DataValuesIterator(this);
	}

	public boolean hasContextValue(String name) {
		name = name.toLowerCase();
		if (name.equals("store")) {
			return true;
		}

		if (name.equals("featuretype")) {
			return true;
		}

		if (name.equals("feature")) {
			return true;
		}
		return false;
	}

	public boolean hasDataValue(String name) {
		name = name.toLowerCase();
		return this.data.getType().getIndex(name) >= 0;
	}

}
