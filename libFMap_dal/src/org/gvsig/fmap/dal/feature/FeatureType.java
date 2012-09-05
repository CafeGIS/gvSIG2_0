package org.gvsig.fmap.dal.feature;

import java.util.Iterator;
import java.util.List;

import org.cresques.cts.IProjection;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.tools.evaluator.Evaluator;

/**
 * <p>
 * This interface provides all the information that describes the structure of
 * a type of feature, methods for managing it and also offers a variety of utility
 * methods for simplicity's sake.
 * </p>
 *
 * <p>
 * The relevant information that compounds a FeatureType includes:
 * </p>
 *
 *  <ul>
 *  <li> {@link FeatureAttributeDescriptor}(s)
 *  <li> {@link FeatureRule}(s)
 *  <li> Its size
 *  <li> Its SRS(s)
 *  <li> Its identifier
 *  <li> Whether features of this type have an OID or not (identifier assigned by the store).
 * </ul>
 *
 * <p>
 * Methods for management include:
 * </p>
 *
 *  <ul>
 *  <li>Obtaining its editable instance.
 *  <li>Obtaining an iterator over its attributes.
 *  <li>Knowing whether this FeatureType has any associated evaluator for calculated attributes.
 *  </ul>
 *
 * <p>
 * Utility methods include:
 * </p>
 *
 * <ul>
 * <li>Getting a copy of the FeatureType.
 * <li>Getting the default geometry attribute.
 * <li>Getting the default spatial reference system.
 * <ul/>
 */
public interface FeatureType {

	/**
	 * Returns a new copy of this FeatureType
	 *
	 * @return
	 * 		a new copy of this FeatureType
	 */
	public FeatureType getCopy();

	/**
	 * Returns a {@link FeatureRules} containing
	 * all rules applicable to features of this type.
	 *
	 * @return
	 * 		a {@link FeatureRules} containing all rules
	 * 		applicable to features of this type.
	 */
	public FeatureRules getRules();

	/**
	 * Returns an editable instance of this FeatureType.
	 * Any modifications on a FeatureType must be done
	 * through its editable instance.
	 *
	 * @return
	 * 		the editable instance of this FeatureType.
	 *
	 * @see EditableFeatureType
	 */
	public EditableFeatureType getEditable();

	/**
	 * Given the name of an attribute, this method returns
	 * its position in this FeatureType.
	 *
	 * @param name
	 * 			of the attribute
	 * @return
	 * 		position of the attribute
	 */
	public int getIndex(String name);

	/**
	 * Returns an attribute descriptor given its name.
	 *
	 * @param name
	 * 			of the attribute
	 * @return
	 * 		descriptor of the attribute, a {@link FeatureAttributeDescriptor}.
	 */
	public Object get(String name);

	/**
	 * Returns an attribute descriptor given its index
	 *
	 * @param index
	 * 			of the attribute
	 *
	 * @return
	 * 		descriptor of the attribute, a {@link FeatureAttributeDescriptor}
	 */
	public Object get(int index);

	/**
	 * Returns a {@link FeatureAttributeDescriptor} given the attribute name.
	 *
	 * @param name
	 * 			of the attribute
	 *
	 * @return
	 * 		a {@link FeatureAttributeDescriptor}
	 */
	public FeatureAttributeDescriptor getAttributeDescriptor(String name);

	/**
	 * Returns a {@link FeatureAttributeDescriptor} given the attribute index.
	 *
	 * @param index
	 * 			of the attribute
	 *
	 * @return
	 * 		a {@link FeatureAttributeDescriptor}
	 */
	public FeatureAttributeDescriptor getAttributeDescriptor(int index);

	/**
	 * Returns an iterator over this FeatureType's attributes. Elements
	 * returned by this iterator are of type {@link FeatureAttributeDescriptor}.
	 *
	 * @return
	 * 		An iterator over this FeatureType's {@link FeatureAttributeDescriptor}s.
	 */
	public Iterator iterator();

	/**
	 * Returns this FeatureType size. The size of a FeatureType is determined
	 * by its number of attributes.
	 *
	 * @return
	 * 		this FeatureType size, defined as the number of attributes it is composed of.
	 *
	 */
	public int size();

	/**
	 * Returns this FeatureType identifier. This identifier must always be equal
	 * to a store.
	 *
	 * @return the identifier.
	 */
	public String getId();

	/**
	 * Returns the name of the attribute that will be used as default
	 * geometry attribute for those processes that require a geometry (for
	 * instance rendering).
	 *
	 * @return
	 * 		name of the default geometry attribute.
	 */
	public String getDefaultGeometryAttributeName();

	/**
	 * Returns the index of the attribute that will be used as default
	 * geometry attribute.
	 *
	 * @return
	 * 		index of the default geometry attribute.
	 */
	public int getDefaultGeometryAttributeIndex();

	/**
	 * Returns a list with the SRSs in which this FeatureType geometries are
	 * expressed. Normally there may be one SRS for each attribute of type
	 * {@link Geometry}.
	 *
	 * @return
	 * 		a list with the SRS in which this FeatureType geometries are expressed.
	 */
	public List getSRSs();

	/**
	 * Returns the SRS in which the default geometry attribute is expressed.
	 *
	 * @return
	 * 		the SRS in which the default geometry attribute is expressed.
	 */
	public IProjection getDefaultSRS();

	/**
	 * Indicates whether this FeatureType has any assigned {@link Evaluator}(s).
	 * Evaluators are used to obtain the values for calculated
	 * attributes.
	 *
	 * @return
	 * 		true if this FeatureType has any assigned {@link Evaluator}(s).
	 */
	public boolean hasEvaluators(); // FIXME: Quitar del interface y dejar en DefaultFeatureType

	/**
	 * Indicates whether {@link Feature}(s) of this FeatureType have an OID defined.
	 * An OID is the Feature unique identifier.
	 *
	 * Some stores provide their own OIDs which are always unique
	 * (such as Postgre) while others don't support this concept and
	 * then it is the library who creates runtime ad-hoc OIDs as
	 * it see fits, but then integrity of this OIDs among different
	 * work sessions cannot be guaranteed (this is the case for shape
	 * files).
	 *
	 * @return
	 * 		true if this FeatureType has an OID defined, false otherwise.
	 *
	 */
	public boolean hasOID();

	/**
	 * Incicates if attibutes with automatic values are allowed in the source
	 *
	 * @return true if source supports this feature, false otherwise
	 */
	public boolean allowAutomaticValues();

	/**
	 * Returns an Array of the FeatureAttributeDescriptor
	 *
	 * @return
	 */
	public FeatureAttributeDescriptor[] getAttributeDescriptors();

	/**
	 * Returns an Array of the FeatureAttributeDescriptor that compounds the
	 * primary key
	 *
	 * @return
	 */
	public FeatureAttributeDescriptor[] getPrimaryKey();

	/**
	 * Returns the default geometry FeatureAttributeDescriptor. Return null if
	 * it's not set
	 *
	 * @return
	 */
	public FeatureAttributeDescriptor getDefaultGeometryAttribute();
}
