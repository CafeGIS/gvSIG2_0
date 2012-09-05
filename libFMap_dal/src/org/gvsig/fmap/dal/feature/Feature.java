package org.gvsig.fmap.dal.feature;

import java.util.Date;
import java.util.List;

import org.cresques.cts.IProjection;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.primitive.Envelope;

/**
 * <p>Represents the basic data unit of a tabular structure, equivalent 
 * to a record in a data base table. In SIG domain, a Feature is a compound 
 * data structure that may contain a geographic component. The conventional term 
 * Feature comes from the term cartographic feature and both represent the same 
 * concept.
 * </p>
 * <p>
 * A Feature may contain more than one geometry attribute. In the case there is not any geometry data, 
 * the <code>getDefaultGeometry()</code> will return <code>null</code>. 
 * </p>
 * <p>
 * Features are not editable as such. To edit a Feature you have to obtain an 
 * editable instance <code>EditableFeature</code> using the method <code>getEditable()</code>.
 * Modify that editable instance and then apply the changes to the Feature. This
 * mechanism is to avoid ambiguity and loosing track on the Feature internal state.
 * </p>
 * <br>
 * <p>The Feature:
 *   <ul>
 *     <li>Has an unique identifier <code>FeatureReference</code>
 *     to recognize our Feature from each other from the same data store</li>
 *     <li>Has a <code>FeatureType</code> that describes the Feature characteristics (attributes,
 *     data types, default geometry, validation rules).</li>
 *     <li>Can obtain a copy of itself.</li>
 *     <li>Can obtain its default geometry attribute and also a list with all the geometry attributes.</li>
 *     <li>Can obtain its default Spatial Reference System and also a list with all the SRSs used in the geometry attributes.</li>
 *     <li>Can obtain the envelope (extent) of the default geometry attribute.
 *     <li>Can obtain the editable instance of the Feature.</li>
 *     <li>Has a set of utility methods to read attributes when their type is known, by both index and name.</li>
 *   </ul>
 * </p>
 *
 */
public interface Feature {

	/**
	 * Returns a unique identifier for this Feature in the associated store. 
	 * 
	 * @return
	 * 		a unique FeatureReference in the associated store
	 */
	public FeatureReference getReference();
	
	/**
	 * Returns the FeatureType that describes the structure of this Feature.
	 *
	 * @return
	 * 		a FeatureType describing this Feature structure.
	 */
	public FeatureType getType();
	
	/**
	 * Creates and returns a copy of this
	 * 
	 * @return
	 * 		a new instance of Feature which is equal to this
	 */
	public Feature getCopy();
	
	/** Mode that indicates the validation of all FeatureRules */
	static final int ALL = 0;
	
	/** Mode that indicates the validation of the update FeatureRules */
	static final int UPDATE = 1;
	
	/** Mode that indicates the validation of the finish editing FeatureRules */
	static final int FINISH_EDITING = 2;
	
	/**
	 * Validates this Feature by applying the <code>FeatureRules</code>
	 * corresponding to the given mode. 
	 * 
	 * @param mode
	 * 			one of the constants {ALL, UPDATE, FINISH_EDITING}
	 */
	public void validate(int mode);

	/**
	 * Returns the editable instance of this Feature.
	 * EditableFeature offers methods for Feature editing.
	 * 
	 * @return
	 * 		EditableFeature of this
	 */
	public EditableFeature getEditable();

	/**
	 * Returns the value of an attribute given its name.
	 * 
	 * @param name
	 * 			a string containing the name of the attribute
	 * @return
	 * 		value of the specified attribute
	 */
	public Object   get(String name);
	
	/**
	 * Returns the value of an attribute given its position.
	 * 
	 * @param index
	 * 			position of the attribute
	 * @return
	 * 		value of the specified attribute
	 */
	public Object   get(int index);

	/**
	 * Returns the int value of an attribute given its name.
	 * 
	 * @param name
	 * 			a string containing the name of the attribute
	 * @return
	 * 		value of the specified attribute
	 */	
	public int      getInt(String name);
	
	/**
	 * Returns the int value of an attribute given its position.
	 * 
	 * @param index
	 * 			position of the attribute
	 * @return
	 * 		value of the specified attribute
	 */	
	public int      getInt(int index);
	
	/**
	 * Returns the Boolean value of an attribute given its name.
	 * 
	 * @param name
	 * 			name of the attribute
	 * @return
	 * 		value of the specified attribute
	 */
	public boolean  getBoolean(String name);
	
	/**
	 * Returns the Boolean value of an attribute given its position.
	 * 
	 * @param index
	 * 			position of the attribute
	 * @return
	 * 		value of the specified attribute
	 */	
	public boolean  getBoolean(int index);

	/**
	 * Returns the long value of an attribute given its name.
	 * 
	 * @param name
	 * 			name of the attribute
	 * @return
	 * 		value of the specified attribute
	 */	
	public long     getLong(String name);
	
	/**
	 * Returns the long value of an attribute given its position.
	 * 
	 * @param index
	 * 			position of the attribute
	 * @return
	 * 		value of the specified attribute
	 */	
	public long     getLong(int index);

	/**
	 * Returns the float value of an attribute given its name.
	 * 
	 * @param name
	 * 			name of the attribute
	 * @return
	 * 		value of the specified attribute
	 */	
	public float   getFloat(String name);
	
	/**
	 * Returns the float value of an attribute given its position.
	 * 
	 * @param index
	 * 			position of the attribute
	 * @return
	 * 		value of the specified attribute
	 */	
	public float    getFloat(int index);

	/**
	 * Returns the double value of an attribute given its name.
	 * 
	 * @param name
	 * 			name of the attribute
	 * @return
	 * 		value of the specified attribute
	 */	
	public double  getDouble(String name);
	
	/**
	 * Returns the double value of an attribute given its position.
	 * 
	 * @param index
	 * 			position of the attribute
	 * @return
	 * 		value of the specified attribute
	 */	
	public double   getDouble(int index);

	/**
	 * Returns the Date value of an attribute given its name.
	 * 
	 * @param name
	 * 			name of the attribute
	 * @return
	 * 		value of the specified attribute
	 */	
	public Date    getDate(String name);
	
	/**
	 * Returns the Date value of an attribute given its position.
	 * 
	 * @param index
	 * 			position of the attribute
	 * @return
	 * 		value of the specified attribute
	 */	
	public Date     getDate(int index);

	/**
	 * Returns the String value of an attribute given its name.
	 * 
	 * @param name
	 * 			name of the attribute
	 * @return
	 * 		value of the specified attribute
	 */	
	public String  getString(String name);
	
	/**
	 * Returns the String value of an attribute given its position.
	 * 
	 * @param index
	 * 			position of the attribute
	 * @return
	 * 		value of the specified attribute
	 */	
	public String   getString(int index);

	/**
	 * Returns the byte value of an attribute given its name.
	 * 
	 * @param name
	 * 			name of the attribute
	 * @return
	 * 		value of the specified attribute
	 */	
	public byte    getByte(String name);
	
	/**
	 * Returns the byte value of an attribute given its position.
	 * 
	 * @param index
	 * 			position of the attribute
	 * @return
	 * 		value of the specified attribute
	 */	
	public byte     getByte(int index);

	/**
	 * Returns the Geometry value of an attribute given its name.
	 * 
	 * @param name
	 * 			name of the attribute
	 * @return
	 * 		value of the specified attribute
	 */
	public Geometry getGeometry(String name);
	
	/**
	 * Returns the Geometry value of an attribute given its position.
	 * 
	 * @param index
	 * 			position of the attribute
	 * @return
	 * 		value of the specified attribute
	 */	
	public Geometry getGeometry(int index);

	/**
	 * Returns the array value of an attribute given its name.
	 * 
	 * @param name
	 * 			name of the attribute
	 * @return
	 * 		value of the specified attribute
	 */
	public Object[] getArray(String name);
	
	/**
	 * Returns the array value of an attribute given its position.
	 * 
	 * @param index
	 * 			position of the attribute
	 * @return
	 * 		value of the specified attribute
	 */	
	public Object[] getArray(int index);

	/**
	 * Returns the Feature value of an attribute given its name.
	 * 
	 * @param name
	 * 			name of the attribute
	 * @return
	 * 		value of the specified attribute
	 */		
	public Feature  getFeature(String name);
	
	/**
	 * Returns the Feature value of an attribute given its position.
	 * 
	 * @param index
	 * 			position of the attribute
	 * @return
	 * 		value of the specified attribute
	 */	
	public Feature  getFeature(int index);


	/**
	 * Envelope (AKA extent or bounding box) of the default 
	 * geometry attribute.
	 *
	 * @return Envelope
	 * 				of the default geometry attribute
	 */
	public Envelope getDefaultEnvelope();

	/**
	 * Returns the value of the default geometry attribute,
	 * which is a {@link Geometry}.
	 * 
	 * @return
	 * 		value of the default geometry attribute
	 */
	public Geometry getDefaultGeometry();
	
	/**
	 * Returns a list with the values of this Feature's 
	 * geometry attributes.
	 * 
	 * @return
	 * 		a list with the values of this Feature's geometry attributes
	 */
	public List getGeometries();

	/**
	 * Returns the Spatial Reference System in which is 
	 * expressed the default geometry attribute.
	 * 
	 * @return
	 * 		A string containing the default geometry attribute SRS.
	 */
	public IProjection getDefaultSRS();
	
	/**
	 * Returns a list with the Spatial Reference Systems in which
	 * are expressed this Feature's geometry attributes.
	 * 
	 * @return
	 * 		a list with the Spatial Reference Systems.
	 */
	public List getSRSs();

}
