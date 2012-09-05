package org.gvsig.fmap.dal.feature;

import org.gvsig.fmap.dal.exception.DataException;

/**
 * Represents a Feature validation rule. These rules are used to 
 * check Feature state integrity in editing mode.
 *
 */
public interface FeatureRule {
	
	/**
	 * Returns the rule name
	 * 
	 * @return
	 * 		the rule name
	 */
	public String getName();
	
	/**
	 * Returns the rule description
	 * 
	 * @return
	 * 		the rule description
	 */
	public String getDescription();
	
	/**
	 * This is the method that applies this rule to the {@link Feature}, given also its associated {@link FeatureStore}.
	 * @param feature
	 * 			Feature to which apply the rule
	 * @param featureStore
	 * 			FeatureStore to which the Feature belongs
	 * @throws DataException
	 * 			if an error occurs during validation
	 */
	public void validate(Feature feature, FeatureStore featureStore)
			throws DataException;
	
	/**
	 * Indicates whether this rule should be checked at update.
	 * 
	 * @return
	 * 		true if this rule should be checked at update, otherwise false.
	 */
	public boolean checkAtUpdate();
	
	/**
	 * Returns true if this rule should be applied just when editing is being finished.
	 *
	 * @return	
	 * 		true if this rule should be applied when editing is being finished, otherwise false.
	 */
	public boolean checkAtFinishEditing();
}
