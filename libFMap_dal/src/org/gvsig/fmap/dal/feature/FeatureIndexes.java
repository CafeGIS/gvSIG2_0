/* gvSIG. Geographic Information System of the Valencian Government
 *
 * Copyright (C) 2007-2008 Infrastructures and Transports Department
 * of the Valencian Government (CIT)
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 *
 */

/*
 * AUTHORS (In addition to CIT):
 * 2008 {{Company}}   {{Task}}
 */

package org.gvsig.fmap.dal.feature;

import java.util.Iterator;

import org.gvsig.fmap.dal.feature.exception.FeatureIndexException;
import org.gvsig.tools.evaluator.Evaluator;

/**
 * This interface gives access to a store's local indexes
 * and also provides a delegated method for obtaining a
 * resulting {@link FeatureSet} using the appropriate
 * index given an {@link Evaluator}.
 *
 *
 * @author jyarza
 *
 */
public interface FeatureIndexes {


	/**
	 * Returns a FeatureIndex given the name with which it was created.
	 *
	 * @param name FeatureIndex name
	 *
	 * @return FeatureIndex or null if it is not found.
	 */
	public FeatureIndex getFeatureIndex(String name);

	/**
	 * Using the given evaluator attributes, choose and use an appropriate index
	 * to obtain a FeatureSet. If no index can be applied, then this method
	 * returns null
	 *
	 * @param evaluator
	 * @return FeatureSet or null if could not find any appropriate index.
	 * @throws FeatureIndexException
	 *
	 */
	public FeatureSet getFeatureSet(Evaluator evaluator)
			throws FeatureIndexException;

	/**
	 * Returns an iterator over the indexes. Elements are of type FeatureIndex.
	 *
	 * @return Iterator over the FeatureIndex(es).
	 */
	public Iterator iterator();
}
