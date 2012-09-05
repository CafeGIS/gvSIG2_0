package org.gvsig.fmap.dal;

import org.gvsig.tools.visitor.Visitable;

/**
 * <p> Interface that represents a generic set of data. It may proceed either from a data 
 * query, a user selection or a collection of locked elements.</p>
 */
public interface DataSet extends Visitable {
	
	/**
	 * Frees this DataSet resources
	 */
	public void dispose();
	
	/**
	 * Indicates whether this DataSet belongs to a specific store
	 * @param store 
	 * 			a DataStore   
	 * @return true if this belongs to the given DataStore, false if not.
	 */
	public boolean isFromStore(DataStore store);

}
