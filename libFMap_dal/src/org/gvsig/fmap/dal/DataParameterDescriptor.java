package org.gvsig.fmap.dal;

/**
 * This interface describes the properties of a parameter. It is useful 
 * for any component that needs to know how to edit or visualize it.
 *
 */
public interface DataParameterDescriptor {
	
	/** Accepts a single value */
	public static final int SINGLE = 1;
	/** Accepts a single value from a value set */
	public static final int CHOICE = 2;
	/** Accepts a range of values defined with a minimum and a maximum value */
	public static final int RANGE = 3;

	/**
	 * Returns the parameter's name
	 * 
	 * @return String containing the parameter's name
	 */
	public String getName();
	
	/**
	 * Returns the parameter's description
	 * 
	 * @return String containing the parameter's description.
	 */
	public String getDescription();

	/**
	 * Returns the parameter's data type.
	 * 
	 * @return parameter's data type.
	 * 
	 * @see DataTypes
	 */
	public int getDataType();

	/**
	 * Returns the parameter's default value.
	 * 
	 * @return an Object containing the default value. Use the data type to cast.
	 */
	public Object getDefaultValue();
	
	/**
	 * Returns one of the available values type.
	 * 
	 * @return an <code>int</code> with one of the available values type.
	 * 
	 * @see #SINGLE
	 * @see #CHOICE
	 * @see #RANGE
	 */
	public int getAvailableValuesType();

	/**
	 * Returns an array containing the available values accepted by the parameter.
	 * 
	 * @return array of Object containing the available values accepted by the parameter. Use the data type to cast.
	 */
	public Object[] getAvailableValues();

	/**
	 * Returns the minimum value when the parameter accepts a range of values.
	 * 
	 * @return range's minimum value
	 */
	public Object getMinValue();

	/**
	 * Returns the maximum value when the parameter accepts a range of values.
	 * 
	 * @return range's maximum value.
	 */
	public Object getMaxValue();
}
