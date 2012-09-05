package org.gvsig.fmap.dal.feature;

import java.text.DateFormat;

import org.cresques.cts.IProjection;
import org.gvsig.fmap.dal.DataTypes;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.tools.evaluator.Evaluator;




/**
 * A feature attribute descriptor contains information about
 * one of the attributes in a feature, such as its name, data type
 * or precision.
 *
 *
 */
public interface FeatureAttributeDescriptor {

	/**
	 * Returns a clone of this attribute descriptor
	 *
	 * @return FeatureAttributeDescriptor
	 * 						A new copy of this
	 */
	public FeatureAttributeDescriptor getCopy();


	/**
	 * Returns the name of this attribute.  This is the name that can be used to
	 * retrieve the value of an attribute and usually maps to either an XML
	 * element name or a column name in a relational database.
	 */
	public String getName();

	/**
	 * Returns a constant from {@link DataTypes}. The return
	 * value of this method indicates how the return values of {@link #getSize},
	 * {@link #getPrecision}, and {@link #getObjectClass} should be interpreted.
	 * For attributes whose maximum cardinality is greater than one, this should
	 * return the data type of the individual elements of the collection.
	 *
	 * @return
	 * 		an <code>int</code> indicating the data type as defined in {@link DataTypes}.
	 */
	public int getDataType();

	/**
	 * Returns the name of this attribute's data type.
	 *
	 * @return
	 * 		a string containing the name of this attribute's data type.
	 */
	public String getDataTypeName();

	/**
	 * Returns a number that indicates the size of this attribute. See the
	 * documentation for the various constants of {@link DataTypes}
	 * for how to interpret this value. As an example, when the data type is
	 * {@link DataTypes#STRING}, this value indicates the maximum length of the string.
	 *
	 * @return
	 * 		an <code>int</code> indicating the size of the attribute.
	 */
	public int getSize();

	/**
	 * For attributes of type {@link DataTypes#DOUBLE} and {@link DataTypes#FLOAT}
	 * , this returns the maximum number of places after the decimal point. For
	 * other types, this must always return zero.
	 */
	public int getPrecision();

	/**
	 * For attributes of type {@link DataTypes#OBJECT},
	 * this returns the Java {@link Class} object that class or interface that
	 * all values of this attribute can be cast to.
	 */
	public Class getObjectClass();

	/**
	 * Returns the minimum number of occurrences of this attribute on a given
	 * feature.  The vast majority of data sources and data consumers will only
	 * function with this value being zero or one.  If the minimum number of
	 * occurrences is zero, this is equivalent, in SQL terms, to the attribute
	 * being nillable.
	 */
	public int getMinimumOccurrences();

	/**
	 * Returns the maximum number of occurrences of this attribute on a given
	 * feature.  The vast majority of data sources and data consumers will only
	 * function with this value being one.  A value of {@link Integer#MAX_VALUE}
	 * indicates that the maximum number of occurrences is unbounded.
	 */
	public int getMaximumOccurrences();

	/**
	 * Returns {@code true} if this attribute forms all or part of the unique identifying
	 * value for the feature it is contained by.  The primary key attributes uniquely
	 * identify this feature from other features of the same type.  This is different
	 * from the {@linkplain Feature#getReference()}, which must uniquely identify
	 * the {@link Feature} among all feature types.
	 */
	public boolean isPrimaryKey();

	/**
	 * Indicates whether this attribute accepts null values.
	 *
	 * @return
	 * 		true if this attribute can be null, false if not.
	 */
	public boolean allowNull();

	/**
	 * Returns an evaluator that will be used to calculate
	 * the value of this attribute
	 */
	public Evaluator getEvaluator();

	/**
	 * Indicates whether this attribute is read only or not.
	 *
	 * @return
	 * 		true if this attribute is read only, false if not.
	 */
	public boolean isReadOnly();

	/**
	 * If this attribute is a {@link Geometry}, this method returns its
	 * Spatial Reference System.
	 *
	 * @return
	 * 		the SRS if this attribute is a {@link Geometry}, otherwise this method returns null.
	 */
	public IProjection getSRS();

	/**
	 * If this attribute is a {@link Geometry}, this method returns the specific geometry type,
	 * as defined in {@link Geometry.TYPES}.
	 *
	 * @return
	 * 		One of {@link Geometry.TYPES}
	 */
	public int getGeometryType();

	/**
	 * If this attribute is a {@link Geometry}, this method returns the specific geometry subtype,
	 * as defined in {@link Geometry.SUBTYPES}.
	 *
	 * @return
	 * 		One of {@link Geometry.SUBTYPES}
	 */
	public int getGeometrySubType();

	/**
	 * Returns this attribute default value, or null if no
	 * default value has been defined.
	 *
	 * @return
	 * 		this attribute default value, or null if no default
	 * value has been defined.
	 */
	public Object getDefaultValue();

	/**
	 * If this attribute is of type Date, then this method returns
	 * the date format set by the data store.
	 *
	 * @return
	 * 		a date format
	 */
	public DateFormat getDateFormat();

	/**
	 * Returns this attribute relative position within the {@link Feature}.
	 *
	 * @return
	 * 		an index
	 */
	public int getIndex();

	/**
	 * Returns additional information of the attribute
	 *
	 * @return info
	 *
	 */
	public Object getAdditionalInfo(String infoName);

	/**
	 * Reaturns if value is created automaticaly by the source
	 */
	public boolean isAutomatic();

}
