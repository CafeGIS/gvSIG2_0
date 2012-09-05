package org.gvsig.fmap.dal.feature.impl;

import java.util.Date;
import java.util.Iterator;

import org.gvsig.fmap.dal.feature.EditableFeature;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.FeatureAttributeDescriptor;
import org.gvsig.fmap.dal.feature.spi.FeatureProvider;
import org.gvsig.fmap.geom.Geometry;

public class DefaultEditableFeature extends DefaultFeature implements
EditableFeature {
	private DefaultFeature source;

	protected DefaultEditableFeature(DefaultFeature feature) {
		super(feature);
		this.source = feature;
	}

	protected DefaultEditableFeature(DefaultEditableFeature feature) {
		super(feature);
		this.source = (DefaultFeature) feature.getSource();
	}

	public DefaultEditableFeature(DefaultFeatureStore store, FeatureProvider data) {
		// Se trata de un editable feature sobre una ya existente
		super(store, data);
		this.source = null;
	}

	public Feature getSource() {
		return this.source;
	}

	public EditableFeature getEditable() {
		return this;
	}

	public Feature getCopy() {
		return new DefaultEditableFeature(this);
	}


	public Feature getNotEditableCopy() {
		return new DefaultFeature(this);
	}

	public void setDefaultGeometry(Geometry geometry) {
		FeatureAttributeDescriptor attribute = this.getType()
				.getAttributeDescriptor(
						this.getType().getDefaultGeometryAttributeIndex());
		this.set(attribute, geometry);
	}

	public void set(String name, Object value) {
		FeatureAttributeDescriptor attribute = this.getType()
		.getAttributeDescriptor(name);
		this.set(attribute, value);
	}

	public void set(int index, Object value) {
		FeatureAttributeDescriptor attribute = this.getType()
		.getAttributeDescriptor(index);
		this.set(attribute, value);
	}

	public void setArray(String name, Object[] value) {
		FeatureAttributeDescriptor attribute = this.getType()
				.getAttributeDescriptor(name);
		this.set(attribute, value);
	}

	public void setArray(int index, Object[] value) {
		FeatureAttributeDescriptor attribute = this.getType()
				.getAttributeDescriptor(index);
		this.set(attribute, value);
	}

	public void setBoolean(String name, boolean value) {
		FeatureAttributeDescriptor attribute = this.getType()
		.getAttributeDescriptor(name);
		this.set(attribute, Boolean.valueOf(value));
	}

	public void setBoolean(int index, boolean value) {
		FeatureAttributeDescriptor attribute = this.getType()
		.getAttributeDescriptor(index);
		this.set(attribute, Boolean.valueOf(value));
	}

	public void setByte(String name, byte value) {
		FeatureAttributeDescriptor attribute = this.getType()
		.getAttributeDescriptor(name);
		this.set(attribute, Byte.valueOf(value));
	}

	public void setByte(int index, byte value) {
		FeatureAttributeDescriptor attribute = this.getType()
		.getAttributeDescriptor(index);
		this.set(attribute, Byte.valueOf(value));
	}

	public void setDate(String name, Date value) {
		FeatureAttributeDescriptor attribute = this.getType()
		.getAttributeDescriptor(name);
		this.set(attribute, value);
	}

	public void setDate(int index, Date value) {
		FeatureAttributeDescriptor attribute = this.getType()
		.getAttributeDescriptor(index);
		this.set(attribute, value);
	}

	public void setDouble(String name, double value) {
		FeatureAttributeDescriptor attribute = this.getType()
		.getAttributeDescriptor(name);
		this.set(attribute, Double.valueOf(value));
	}

	public void setDouble(int index, double value) {
		FeatureAttributeDescriptor attribute = this.getType()
		.getAttributeDescriptor(index);
		this.set(attribute, Double.valueOf(value));
	}

	public void setFeature(String name, Feature value) {
		FeatureAttributeDescriptor attribute = this.getType()
		.getAttributeDescriptor(name);
		this.set(attribute, value);
	}

	public void setFeature(int index, Feature value) {
		FeatureAttributeDescriptor attribute = this.getType()
		.getAttributeDescriptor(index);
		this.set(attribute, value);
	}

	public void setFloat(String name, float value) {
		FeatureAttributeDescriptor attribute = this.getType()
		.getAttributeDescriptor(name);
		this.set(attribute, Float.valueOf(value));
	}

	public void setFloat(int index, float value) {
		FeatureAttributeDescriptor attribute = this.getType()
		.getAttributeDescriptor(index);
		this.set(attribute, Float.valueOf(value));
	}

	public void setGeometry(String name, Geometry value) {
		FeatureAttributeDescriptor attribute = this.getType()
		.getAttributeDescriptor(name);
		this.set(attribute, value);
	}

	public void setGeometry(int index, Geometry value) {
		FeatureAttributeDescriptor attribute = this.getType()
		.getAttributeDescriptor(index);
		this.set(attribute, value);
	}

	public void setInt(String name, int value) {
		FeatureAttributeDescriptor attribute = this.getType()
		.getAttributeDescriptor(name);
		this.set(attribute, Integer.valueOf(value));
	}

	public void setInt(int index, int value) {
		FeatureAttributeDescriptor attribute = this.getType()
		.getAttributeDescriptor(index);
		this.set(attribute, Integer.valueOf(value));
	}

	public void setLong(String name, long value) {
		FeatureAttributeDescriptor attribute = this.getType()
		.getAttributeDescriptor(name);
		this.set(attribute, Long.valueOf(value));
	}

	public void setLong(int index, long value) {
		FeatureAttributeDescriptor attribute = this.getType()
		.getAttributeDescriptor(index);
		this.set(attribute, Long.valueOf(value));
	}

	public void setString(String name, String value) {
		FeatureAttributeDescriptor attribute = this.getType()
		.getAttributeDescriptor(name);
		this.set(attribute, value);
	}

	public void setString(int index, String value) {
		FeatureAttributeDescriptor attribute = this.getType()
		.getAttributeDescriptor(index);
		this.set(attribute, value);
	}

	public void copyFrom(Feature source) {
		// iterate over the attributes and copy one by one
		Iterator it = this.getType().iterator();
		while( it.hasNext() ) {
			FeatureAttributeDescriptor attr = (FeatureAttributeDescriptor) it.next();
			set(attr.getIndex(), source.get(attr.getIndex()));
		}
	}


}
