package org.gvsig.fmap.dal.feature;

import java.util.Iterator;

/**
 * This is a container for FeatureRules.
 * Besides getting a rule by index, this structure allows 
 * adding a rule, removing a rule, iterating over the rules and copying 
 * the whole structure.
 *
 */
public interface FeatureRules {

	/**
	 * Returns an object given its index.
	 * 
	 * @param index
	 * 			a position in this <code>FeatureRules</code>
	 * @return
	 * 		the object found in the given index
	 */
	public Object get(int index);

	/**
	 * Returns a {@link FeatureRule} given its index.
	 * 
	 * @param index
	 * 			a position in this <code>FeatureRules</code>
	 * @return
	 * 		the {@link FeatureRule} found in the given index
	 */
	
	public FeatureRule getRule(int index);

	/**
	 * Adds a new rule to this FeatureRules.
	 * 
	 * @param rule
	 * 			the new rule to add.
	 * @return
	 * 		the added rule
	 */
	public FeatureRule add(FeatureRule rule);

	/**
	 * Returns the number of rules contained in this FeatureRules.
	 * 
	 * @return
	 * 		number of rules in this FeatureRules
	 */
	public int size();

	/**
	 * Clears this FeatureRules from any rules.
	 */
	public void clear();

	/**
	 * Removes the rule stored in the given index.
	 * 
	 * @param index
	 * 			index of the rule to remove.
	 * @return
	 * 		returns the removed rule
	 */
	public Object remove(int index);

	/**
	 * Removes the given rule from this FeatureRules.
	 * 
	 * @param rule
	 * 			FeatureRule to remove
	 * @return
	 * 		true indicates success, false indicates that it was not found.
	 */
	public boolean remove(FeatureRule rule);

	/**
	 * Returns an iterator over the available {@link FeatureRule}(s)
	 * 
	 * @return
	 * 		an iterator over the available {@link FeatureRule}(s)
	 */
	public Iterator iterator();

	/**
	 * Returns a new copy of this FeatureRules.
	 * 
	 * @return
	 * 		a new copy of this FeatureRules.
	 */
	public FeatureRules getCopy();

}
