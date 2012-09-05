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
 * 2008 Prodevelop S.L. main development
 */

package org.gvsig.geocoding.result.impl;

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.FeatureReference;
import org.gvsig.geocoding.result.ScoredFeature;

/**
 * Feature with a new attribute named score. the score define the score of the
 * feature in the search proces.
 * 
 * @author <a href="mailto:jsanz@prodevelop.es"> Jorge Gaspar Sanz Salinas</a>
 * @author <a href="mailto:vsanjaime@prodevelop.es"> Vicente Sanjaime Calvet</a>
 */

public class DefaultScoredFeature implements ScoredFeature {

	private double score;
	private FeatureReference reference;

	/**
	 * Get score of search
	 * 
	 * @return
	 */
	public double getScore() {
		return score;
	}

	/**
	 * Set score of search
	 * 
	 * @param score
	 */
	public void setScore(double score) {
		this.score = score;
	}

	/**
	 * Get Feature reference
	 * 
	 * @return
	 */
	public FeatureReference getReference() {
		return reference;
	}

	/**
	 * Set feature reference
	 * 
	 * @param reference
	 */
	public void setReference(FeatureReference reference) {
		this.reference = reference;
	}

	/**
	 * get feature
	 * 
	 * @return
	 * @throws DataException
	 */
	public Feature getFeature() throws DataException {
		return this.getReference().getFeature();
	}

	/**
	 * Compare with other ScoredFeature
	 * 
	 * @param feat
	 */
	public int compareTo(ScoredFeature feat) {

		double score = feat.getScore();
		if (this.getScore() > score) {
			return -1;
		} else if (this.getScore() < score) {
			return 1;
		} else
			return 0;
	}

	/**
	 * Set feature and store reference
	 * 
	 * @param fea
	 */
	public void setFeature(Feature fea) {
		this.setReference(fea.getReference());

	}

}
