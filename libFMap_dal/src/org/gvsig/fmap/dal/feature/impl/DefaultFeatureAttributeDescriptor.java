package org.gvsig.fmap.dal.feature.impl;

import java.io.File;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.cresques.cts.IProjection;
import org.gvsig.fmap.dal.DataTypes;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.FeatureAttributeDescriptor;
import org.gvsig.fmap.dal.feature.exception.UnsupportedDataTypeException;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.tools.evaluator.Evaluator;

public class DefaultFeatureAttributeDescriptor implements
		FeatureAttributeDescriptor {

	protected boolean allowNull;
	protected int dataType;
	protected DateFormat dateFormat;
	protected Object defaultValue;
	protected int index;
	protected int maximumOccurrences;
	protected int minimumOccurrences;
	protected int size;
	protected String name;
	protected Class objectClass;
	protected int precision;
	protected Evaluator evaluator;
	protected boolean primaryKey;
	protected boolean readOnly;
	protected IProjection SRS;
	protected int geometryType;
	protected int geometrySubType;
	protected Map additionalInfo;
	protected boolean isAutomatic;


    private static final Class TYPE_CLASS[] = new Class[] {
    	null,
    	Boolean.class,
    	Byte.class, // keep for formatting
        Character.class,
        Integer.class,
        Long.class,
        Float.class,
        Double.class,
        String.class,
        Date.class,
        Date.class,
        Date.class,
        Geometry.class,
        Object.class, //
        Feature.class,
        IProjection.class,
        File.class,
        new byte[] {}.getClass()

    };

	protected DefaultFeatureAttributeDescriptor() {
		this.allowNull = true;
		this.dataType = DataTypes.UNKNOWN;
		this.dateFormat = null;
		this.defaultValue = null;
		this.index = -1;
		this.maximumOccurrences = 0;
		this.minimumOccurrences = 0;
		this.size = 0;
		this.name = null;
		this.objectClass = null;
		this.precision = 0;
		this.evaluator = null;
		this.primaryKey = false;
		this.readOnly = false;
		this.SRS = null;
		this.geometryType = Geometry.TYPES.NULL;
		this.geometrySubType = Geometry.SUBTYPES.UNKNOWN;
		this.additionalInfo = null;
		this.isAutomatic = false;
	}

	protected DefaultFeatureAttributeDescriptor(
			DefaultFeatureAttributeDescriptor other) {
		this.allowNull = other.allowNull;
		this.dataType = other.dataType;
		this.dateFormat = other.dateFormat;
		this.defaultValue = other.defaultValue;
		this.index = other.index;
		this.maximumOccurrences = other.maximumOccurrences;
		this.minimumOccurrences = other.minimumOccurrences;
		this.size = other.size;
		this.name = other.name;
		this.objectClass = other.objectClass;
		this.precision = other.precision;
		this.evaluator = other.evaluator;
		this.primaryKey = other.primaryKey;
		this.readOnly = other.readOnly;
		this.SRS = other.SRS;
		this.geometryType = other.geometryType;
		this.geometrySubType = other.geometrySubType;
		if (other.additionalInfo != null) {
			Iterator iter = other.additionalInfo.entrySet().iterator();
			Map.Entry entry;
			this.additionalInfo = new HashMap();
			while (iter.hasNext()) {
				entry = (Entry) iter.next();
				this.additionalInfo.put(entry.getKey(), entry.getValue());
			}
		} else {
			this.additionalInfo = null;
		}
		this.isAutomatic = other.isAutomatic;
	}

	public String getDataTypeName() {
		return DataTypes.TYPE_NAMES[this.getDataType()];
	}

	public FeatureAttributeDescriptor getCopy() {
		return new DefaultFeatureAttributeDescriptor(this);
	}

	public boolean allowNull() {
		return allowNull;
	}

	public int getDataType() {
		return this.dataType;
	}

	public DateFormat getDateFormat() {
		return this.dateFormat;
	}

	public Object getDefaultValue() {
		return this.defaultValue;
	}

	public Evaluator getEvaluator() {
		return this.evaluator;
	}

	public int getGeometryType() {
		return this.geometryType;
	}

	public int getGeometrySubType() {
		return this.geometrySubType;
	}

	public int getIndex() {
		return this.index;
	}

	protected FeatureAttributeDescriptor setIndex(int index) {
		this.index = index;
		return this;
	}

	public int getMaximumOccurrences() {
		return this.maximumOccurrences;
	}

	public int getMinimumOccurrences() {
		return this.minimumOccurrences;
	}

	public String getName() {
		return this.name;
	}

	public Class getObjectClass() {
		if (this.dataType > TYPE_CLASS.length || this.dataType < 0) {
			throw new UnsupportedDataTypeException(this.name, this.dataType);
		}
		return TYPE_CLASS[this.dataType];
	}

	public int getPrecision() {
		return this.precision;
	}

	public IProjection getSRS() {
		return this.SRS;
	}

	public int getSize() {
		return this.size;
	}

	public boolean isPrimaryKey() {
		return this.primaryKey;
	}

	public boolean isReadOnly() {
		return this.readOnly;
	}

	public Object getAdditionalInfo(String infoName) {
		if (this.additionalInfo == null) {
			return null;
		}
		return this.additionalInfo.get(infoName);
	}

	public boolean isAutomatic() {
		return this.isAutomatic;
	}

	private boolean compareObject(Object a, Object b) {
		if (a != b) {
			if (a != null) {
				return false;
			}
			return a.equals(b);
		}
		return true;

	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof DefaultFeatureAttributeDescriptor)) {
			return false;
		}
		DefaultFeatureAttributeDescriptor other = (DefaultFeatureAttributeDescriptor) obj;

		if (this.allowNull != other.allowNull) {
			return false;
		}

		if (this.index != other.index) {
			return false;
		}

		if (!compareObject(this.name, other.name)) {
				return false;
		}

		if (this.dataType != other.dataType) {
			return false;
		}

		if (this.size != other.size) {
			return false;
		}

		if (!compareObject(this.defaultValue, other.defaultValue)) {
			return false;
		}

		if (!compareObject(this.defaultValue, other.defaultValue)) {
			return false;
		}

		if (this.primaryKey != other.primaryKey) {
			return false;
		}

		if (this.isAutomatic != other.isAutomatic) {
			return false;
		}

		if (this.readOnly != other.readOnly) {
			return false;
		}

		if (this.precision != other.precision) {
			return false;
		}

		if (this.maximumOccurrences != other.maximumOccurrences) {
			return false;
		}

		if (this.minimumOccurrences != other.minimumOccurrences) {
			return false;
		}
		if (this.geometryType != other.geometryType) {
			return false;
		}

		if (this.geometrySubType != other.geometrySubType) {
			return false;
		}

		if (!compareObject(this.evaluator, other.evaluator)) {
			return false;
		}

		if (!compareObject(this.SRS, other.SRS)) {
			return false;
		}

		if (!compareObject(this.dateFormat, other.dateFormat)) {
			return false;
		}

		if (!compareObject(this.objectClass, other.objectClass)) {
			return false;
		}

		return true;
	}
}
