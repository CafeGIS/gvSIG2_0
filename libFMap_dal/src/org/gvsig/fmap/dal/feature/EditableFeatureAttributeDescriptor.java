package org.gvsig.fmap.dal.feature;

import org.cresques.cts.IProjection;
import org.gvsig.fmap.dal.DataTypes;
import org.gvsig.fmap.dal.feature.exception.AttributeFeatureTypeIntegrityException;
import org.gvsig.tools.evaluator.Evaluator;

/**
 * This interface represents a FeatureAttributeDescriptor in editable state.
 * To edit a FeatureAttributeDescriptor you have to obtain its instance of
 * EditableFeatureAttributeDescriptor and then perform editing operations on it.
 *
 * Once you have completed the editing you can save the changes to the original
 * FeatureAttributeDescriptor. This is the only way to edit a FeatureAttributeDescriptor.
 */
public interface EditableFeatureAttributeDescriptor extends
		FeatureAttributeDescriptor {

	/**
	 * Checks attribute integrity
	 */
	void checkIntegrity() throws AttributeFeatureTypeIntegrityException;

	/**
	 * Sets the name
	 * @param
	 * 		name to set
	 * @return this
	 */
	public EditableFeatureAttributeDescriptor setName(String name);

	/**
	 * Sets the data type
	 * @param
	 * 		type one of the constants defined in {@link DataTypes}
	 * @return this
	 */
	public EditableFeatureAttributeDescriptor setDataType(int type);

	/**
	 * Sets the size
	 * @param size
	 * 			a size of type int
	 * @return this
	 *
	 */
	public EditableFeatureAttributeDescriptor setSize(int size);

	/**
	 * Sets the precision
	 *
	 * @param
	 * 		precision of type int
	 *
	 * @return this
	 */
	public EditableFeatureAttributeDescriptor setPrecision(int precision);

	/**
	 * Sets the Class to which the related FeatureAttribute can be cast
	 *
	 * @param theClass
	 * 				Class to which the related FeatureAttribute can be cast
	 * @return this
	 */
	public EditableFeatureAttributeDescriptor setObjectClass(Class theClass);

	/**
	 * Sets the number of minimum occurrences
	 *
	 * @param minimumOccurrences
	 *
	 * @return this
	 */
	public EditableFeatureAttributeDescriptor setMinimumOccurrences(
			int minimumOccurrences);

	/**
	 * Sets the maximum number of occurrences
	 *
	 * @param maximumOccurrences
	 *
	 * @return
	 */
	public EditableFeatureAttributeDescriptor setMaximumOccurrences(
			int maximumOccurrences);

	/**
	 * Sets whether the related FeatureAttribute is part of the FeatureType's primary key
	 *
	 * @param isPrimaryKey
	 * 				true if is part of the primary key
	 * @return this
	 */
	public EditableFeatureAttributeDescriptor setIsPrimaryKey(
			boolean isPrimaryKey);

	/**
	 * Sets the expression evaluator that the FeatureAttribute will use
	 * @param evaluator
	 * 				an implementation of DAL's Evaluator interface
	 * @return this
	 */
	public EditableFeatureAttributeDescriptor setEvaluator(Evaluator evaluator);

	/**
	 * Sets whether the related FeatureAttribute is read only
	 *
	 * @param isReadOnly
	 *
	 * @return this
	 */
	public EditableFeatureAttributeDescriptor setIsReadOnly(boolean isReadOnly);

	/**
	 * Sets whether the related FeatureAttribute can have a null value
	 *
	 * @param allowNull
	 * 				a boolean value determining whether the FeatureAttribute can be null
	 *
	 * @return this
	 */
	public EditableFeatureAttributeDescriptor setAllowNull(boolean allowNull);

	/**
	 * Sets the SRS.
	 *
	 * @param SRS
	 *
	 * @return
	 */
	public EditableFeatureAttributeDescriptor setSRS(IProjection SRS);

	/**
	 * Sets the geometry type
	 *
	 * @param geometryType
	 *
	 * @return this
	 */
	public EditableFeatureAttributeDescriptor setGeometryType(int geometryType);


	/**
	 * Sets the geometry subtype
	 * 
	 * @param geometrySubType
	 * 
	 * @return this
	 */
	public EditableFeatureAttributeDescriptor setGeometrySubType(
			int geometrySubType);

	/**
	 * Sets the default value
	 *
	 * @param defaultValue
	 *
	 * @return this
	 */
	public EditableFeatureAttributeDescriptor setDefaultValue(
			Object defaultValue);

	/**
	 * Sets additional information of the attribute
	 * @return TODO
	 *
	 *
	 */
	public EditableFeatureAttributeDescriptor setAdditionalInfo(String infoName, Object value);

	/**
	 * Sets whether the related FeatureAttribute is part of the FeatureType's
	 * primary key
	 *
	 * @param isPrimaryKey
	 *            true if is part of the primary key
	 * @return this
	 */
	public EditableFeatureAttributeDescriptor setIsAutomatic(
			boolean isAutomatic);

}
