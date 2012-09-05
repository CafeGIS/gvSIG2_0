package org.gvsig.fmap.dal.feature;

import java.util.Date;

import org.gvsig.fmap.geom.Geometry;

/**
 * This interface represents a Feature in editable state. To edit a Feature
 * you have to obtain its instance of EditableFeature and then perform editing
 * operations on it. Once you have completed the editing you can save the changes
 * to the original Feature. This is the only way to edit a Feature.
 */
public interface EditableFeature extends Feature {

	/**
	 * Sets the value of an attribute given its name
	 * @param name
	 * 			attribute's name
	 * @param value
	 *  		value to set
	 */
	public void set(String name, Object value);

	/**
	 * Sets the value of an attribute given its index
	 * @param index
	 * 			attribute's index
	 * @param value
	 *  		value to set
	 */
	public void set(int index, Object value);

	/**
	 * Sets the value of an attribute of type integer, given its name
	 * @param name
	 * 			attribute's name
	 * @param value
	 *  		value to set
	 */
	public void setInt(String name, int value);

	/**
	 * Sets the value of an attribute of type integer, given its index
	 * @param index
	 * 			attribute's index
	 * @param value
	 *  		value to set
	 */
	public void setInt(int index, int value);

	/**
	 * Sets the value of an attribute of type boolean, given its name
	 * @param name
	 * 			attribute's name
	 * @param value
	 *  		value to set
	 */
	public void setBoolean(String name, boolean value);

	/**
	 * Sets the value of an attribute of type boolean, given its index
	 * @param index
	 * 			attribute's index
	 * @param value
	 *  		value to set
	 */
	public void setBoolean(int index, boolean value);

	/**
	 * Sets the value of an attribute of type long, given its name
	 * @param name
	 * 			attribute's name
	 * @param value
	 *  		value to set
	 */
	public void setLong(String name, long value);

	/**
	 * Sets the value of an attribute of type long, given its index
	 * @param index
	 * 			attribute's index
	 * @param value
	 *  		value to set
	 */
	public void setLong(int index, long value);

	/**
	 * Sets the value of an attribute of type float, given its name
	 * @param name
	 * 			attribute's name
	 * @param value
	 *  		value to set
	 */
	public void setFloat(String name, float value);

	/**
	 * Sets the value of an attribute of type float, given its index
	 * @param index
	 * 			attribute's index
	 * @param value
	 *  		value to set
	 */
	public void setFloat(int index, float value);

	/**
	 * Sets the value of an attribute of type double, given its name
	 * @param name
	 * 			attribute's name
	 * @param value
	 *  		value to set
	 */
	public void setDouble(String name, double value);

	/**
	 * Sets the value of an attribute of type double, given its index
	 * @param index
	 * 			attribute's index
	 * @param value
	 *  		value to set
	 */
	public void setDouble(int index, double value);

	/**
	 * Sets the value of an attribute of type date, given its name
	 * @param name
	 * 			attribute's name
	 * @param value
	 *  		value to set
	 */
	public void setDate(String name, Date value);

	/**
	 * Sets the value of an attribute of type date, given its index
	 * @param index
	 * 			attribute's index
	 * @param value
	 *  		value to set
	 */
	public void setDate(int index, Date value);

	/**
	 * Sets the value of an attribute of type string, given its name
	 * @param name
	 * 			attribute's name
	 * @param value
	 *  		value to set
	 */
	public void setString(String name, String value);

	/**
	 * Sets the value of an attribute of type string, given its index
	 * @param index
	 * 			attribute's index
	 * @param value
	 *  		value to set
	 */
	public void setString(int index, String value);

	/**
	 * Sets the value of an attribute of type byte, given its name
	 * @param name
	 * 			attribute's name
	 * @param value
	 *  		value to set
	 */
	public void setByte(String name, byte value);

	/**
	 * Sets the value of an attribute of type byte, given its index
	 * @param index
	 * 			attribute's index
	 * @param value
	 *  		value to set
	 */
	public void setByte(int index, byte value);

	/**
	 * Sets the value of an attribute of type geometry, given its name
	 * @param name
	 * 			attribute's name
	 * @param value
	 *  		value to set
	 */
	public void setGeometry(String name, Geometry value);

	/**
	 * Sets the value of an attribute of type geometry, given its index
	 * @param index
	 * 			attribute's index
	 * @param value
	 *  		value to set
	 */
	public void setGeometry(int index, Geometry value);

	/**
	 * Sets the value of an attribute of type array, given its name
	 * @param name
	 * 			attribute's name
	 * @param value
	 *  		value to set
	 */
	public void setArray(String name, Object[] value);

	/**
	 * Sets the value of an attribute of type array, given its index
	 * @param index
	 * 			attribute's index
	 * @param value
	 *  		value to set
	 */
	public void setArray(int index, Object[] value);

	/**
	 * Sets the value of an attribute of type feature, given its name
	 * @param name
	 * 			attribute's name
	 * @param value
	 *  		value to set
	 */
	public void setFeature(String name, Feature value);

	/**
	 * Sets the value of an attribute of type feature, given its index
	 * @param index
	 * 			attribute's index
	 * @param value
	 *  		value to set
	 */
	public void setFeature(int index, Feature value);

	/**
	 * Returns the Feature from which this EditableFeature was created
	 *
	 * @return Feature from which this EditableFeature was created
	 */
	public Feature getSource();

	/**
	 * Returns a non editable copy of the Feature.
	 *
	 * @return non editable copy of the Feature.
	 */
	public Feature getNotEditableCopy();

	/**
	 * Sets de value of the default geometry attribute.
	 *
	 * @param value geometry to set.
	 */
	public void setDefaultGeometry(Geometry value);
	
	/**
	 * Copies the values of all attributes from the source feature to this feature
	 * 
	 * @param source
	 * 			source feature from which the values will be copied.
	 */
	public void copyFrom(Feature source);	

}
