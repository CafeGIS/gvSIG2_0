package org.gvsig.fmap.dal.feature;

import org.gvsig.fmap.dal.DataTypes;
import org.gvsig.tools.evaluator.Evaluator;

/**
 * This interface represents a FeatureType in editable state. 
 * To edit a FeatureType you have to obtain its instance of 
 * EditableFeatureType and then perform editing operations on it.
 * 
 * Once you have completed the editing you can save the changes to the original 
 * FeatureType. This is the only way to edit a FeatureType.
 */
public interface EditableFeatureType extends FeatureType {

	/**
	 * Sets the default geometry attribute name
	 * 
	 * @param name
	 * 			string containing the default geometry attribute name
	 */
	public void setDefaultGeometryAttributeName(String name);

	/**
	 * Removes an attribute from this EditableFeatureType, given
	 * a reference to its descriptor.
	 * 
	 * @param attribute
	 * 				descriptor of the attribute to remove
	 * 
	 * @return
	 * 		true if the attribute was removed successfully, false if not.
	 */
	public boolean remove(EditableFeatureAttributeDescriptor attribute);

	/**
	 * Removes an attribute given its name
	 * 
	 * @param name
	 * 			string containing the name of the attribute to be removed
	 * @return
	 * 		
	 */
	public Object remove(String name);

	/**
	 * Removes an attribute given its index
	 * 
	 * @param index
	 * 			position of the attribute to be removed
	 * 
	 * @return
	 * 		
	 */
	public Object remove(int index);

	/**
	 * Adds an attribute to this EditableFeatureType. 
	 * @param name
	 * 			string containing the name of the attribute
	 * @param type
	 * 			data type of the attribute (one from {@link DataTypes})
	 * 
	 * @return a new EditableFeatureAttributeDescriptor
	 */
	public EditableFeatureAttributeDescriptor add(String name, int type);

	/**
	 * Adds an attribute to this EditableFeatureType.
	 *  
	 * @param name
	 * 			string containing the name of the attribute
	 * 
	 * @param type
	 * 			data type of the attribute (one from {@link DataTypes})
	 * 
	 * @param size
	 * 			size of the attribute.
	 * 
	 * @return a new EditableFeatureAttributeDescriptor
	 */
	public EditableFeatureAttributeDescriptor add(String name, int type,
			int size);

	/**
	 * Adds a calculated attribute to this EditableFeatureType.
	 *  
	 * @param name
	 * 			string containing the name of the attribute
	 * 
	 * @param type
	 * 			data type of the attribute (one from {@link DataTypes})
	 * 
	 * @param evaluator
	 * 			an evaluator containing the desired expression
	 * 
	 * @return a new EditableFeatureAttributeDescriptor
	 */	
	public EditableFeatureAttributeDescriptor add(String name, int type,
			Evaluator evaluator);

	/**
	 * Returns the associated FeatureType.
	 *  
	 * @return the associated FeatureType
	 */
	public FeatureType getSource();

	/**
	 * Returns a copy of the associated FeatureType.
	 * 
	 * @return associated FeatureType
	 */
	public FeatureType getNotEditableCopy();

	/**
	 * Sets whether this EditableFeatureType has an OID.
	 * An OID is a unique serializable reference to a feature
	 * (a primary key of sorts that may or may not be defined 
	 * by the store). If the store does not define this OID then
	 * it will be generated by the feature reference itself.
	 * 
	 * Its main use is to provide a way to persist data associated 
	 * to a feature by this OID.
	 * 
	 * @param hasOID true if it has an OID, or false if not.
	 */
	public void setHasOID(boolean hasOID);

}
