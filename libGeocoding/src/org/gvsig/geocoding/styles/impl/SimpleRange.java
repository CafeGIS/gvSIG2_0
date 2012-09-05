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

package org.gvsig.geocoding.styles.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.primitive.Point;
import org.gvsig.fmap.geom.util.Converter;
import org.gvsig.geocoding.address.Address;
import org.gvsig.geocoding.address.Literal;
import org.gvsig.geocoding.address.NumberAddress;
import org.gvsig.geocoding.address.RelationsComponent;
import org.gvsig.geocoding.address.impl.DefaultAddressComponent;
import org.gvsig.geocoding.address.impl.DefaultLiteral;
import org.gvsig.geocoding.address.impl.DefaultNumberAddress;
import org.gvsig.geocoding.geommatches.MatcherUtils;
import org.gvsig.geocoding.result.GeocodingResult;
import org.gvsig.geocoding.result.ScoredFeature;
import org.gvsig.geocoding.result.impl.GeomMatchResult;
import org.gvsig.tools.persistence.PersistenceException;
import org.gvsig.tools.persistence.PersistentState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implemented Geocoding style with search by numerical range. This style geocodes streets
 * with even numbers and odd numbers followed.
 * 
 * @author <a href="mailto:jsanz@prodevelop.es"> Jorge Gaspar Sanz Salinas</a>
 * @author <a href="mailto:vsanjaime@prodevelop.es"> Vicente Sanjaime Calvet</a>
 */

public class SimpleRange extends AbstractRange {

	private static final Logger log = LoggerFactory
			.getLogger(SimpleRange.class);

	private static final String FIRSTNUMBER = "firstnumber";
	private static final String LASTNUMBER = "lastnumber";

	private RelationsComponent firstNumber;
	private RelationsComponent lastNumber;

	/**
	 * Set relation component that define the field of data source that it has
	 * the first number of the feature
	 * 
	 * @return
	 */
	public RelationsComponent getFirstNumber() {
		return firstNumber;
	}

	/**
	 * Set relation component that define the field of data source that it has
	 * the first number of the feature
	 * 
	 * @param firstNumber
	 */
	public void setFirstNumber(RelationsComponent firstNumber) {
		this.firstNumber = firstNumber;
	}

	/**
	 * get relation component that define the field of data source that it has
	 * the last number of the feature
	 * 
	 * @return
	 */
	public RelationsComponent getLastNumber() {
		return lastNumber;
	}

	/**
	 * Set relation component that define the field of data source that it has
	 * the last number of the feature
	 * 
	 * @param lastNumber
	 */
	public void setLastNumber(RelationsComponent lastNumber) {
		this.lastNumber = lastNumber;
	}

	/**
	 * Spatial search over the geometries of the features selected
	 * 
	 * @param lists
	 *            list of lists with ScoredFeatures
	 * @param address
	 * @return
	 */
	@Override
	public Set<GeocodingResult> match(List<List<ScoredFeature>> inLists,
			Address address) {
		Set<GeocodingResult> results = new TreeSet<GeocodingResult>();

		String firstNumberDesc = this.getFirstNumber().getValue();
		String lastNumberDesc = this.getLastNumber().getValue();

		// Get the list
		List<ScoredFeature> sFeats = inLists.get(0);
		if (sFeats.size() > 0) {
			try {
				for (ScoredFeature sFeat : sFeats) {

					int number = ((NumberAddress) address).getNumber();

					Feature fea = sFeat.getFeature();

					int[] range = getNumberRange(fea, firstNumberDesc,
							lastNumberDesc);
					int first = range[0];
					int last = range[1];

					if (number >= first && number <= last) {
						// create result
						GeomMatchResult res = calculatePosition(sFeat, number,
								first, last);
						// add result to the list
						results.add(res);
					}
				}
			} catch (DataException e) {
				log.error("Error Getting the features", e);
			}
		}
		return results;
	}

	/**
	 * Calculate point over the geometry (line)
	 * 
	 * @param feat
	 *            feature
	 * @param number
	 *            of address
	 * @param first
	 *            number of feature geometry
	 * @param last
	 *            number of feature geometry
	 * @return
	 * @throws DataException
	 */
	private GeomMatchResult calculatePosition(ScoredFeature feat, int number,
			int first, int last) throws DataException {

		Geometry geom = feat.getReference().getFeature().getDefaultGeometry();
		com.vividsolutions.jts.geom.Geometry geJTS = Converter
				.geometryToJts(geom);
		int relative = calculateRelativeDistance(number, first, last);
		Point position = MatcherUtils.getLinePositionFromRelativeDistance(
				geJTS, relative);
		// create result
		GeomMatchResult res = new GeomMatchResult();
		List<ScoredFeature> list = new ArrayList<ScoredFeature>();
		list.add(feat);
		res.setGeom(position);
		res.setMainSources(list);
		res.setAddress(getResultAddress(list, number));
		return res;
	}

	/**
	 * calculate relative position over line with number of address from initial
	 * number and last number of the geom
	 * 
	 * @param number
	 *            of address
	 * @param first
	 *            number of geometry
	 * @param last
	 *            number of geometry
	 * @return
	 */
	private int calculateRelativeDistance(int number, int first, int last) {
		if (first == last) {
			return 50;
		}
		int total = Math.abs(last - first);
		int ini = Math.abs(number - first);
		return (ini * 100) / total;
	}

	/**
	 * Get range of the numbers (ood side or even side)
	 * 
	 * @param feat
	 * @return
	 */
	private int[] getNumberRange(Feature feat, String firstNumberDesc,
			String lastNumberDesc) {

		int[] range = new int[2];

		Object oFirst = feat.get(firstNumberDesc);
		Object oLast = feat.get(lastNumberDesc);

		// FIRST
		if (oFirst instanceof Double) {
			range[0] = ((Double) oFirst).intValue();
		}
		if (oFirst instanceof Integer) {
			range[0] = ((Integer) oFirst).intValue();
		}
		if (oFirst instanceof String) {
			range[0] = Integer.parseInt(((String) oFirst));
		}
		// LAST
		if (oLast instanceof Double) {
			range[1] = ((Double) oLast).intValue();
		}
		if (oLast instanceof Integer) {
			range[1] = ((Integer) oLast).intValue();
		}
		if (oLast instanceof String) {
			range[1] = Integer.parseInt(((String) oLast));
		}

		return range;
	}

	/**
	 * Get result address
	 * 
	 * @return
	 */
	private Address getResultAddress(List<ScoredFeature> sources, int number) {

		// get the first element
		ScoredFeature feat = sources.get(0);

		Literal relLiteral = this.getRelationsLiteral();
		Literal literal = new DefaultLiteral();
		for (Object obj : relLiteral) {
			RelationsComponent rel = (RelationsComponent) obj;
			String key = rel.getKeyElement();
			String fieldName = rel.getValue();
			Object object = null;
			String value = "";
			try {
				object = feat.getFeature().get(fieldName);
				value = object.toString();
			} catch (DataException e) {
				log.error("Getting attributes ot the feature", e);
			}
			literal.add(new DefaultAddressComponent(key, value));
		}

		NumberAddress address = new DefaultNumberAddress();
		address.setMainLiteral(literal);
		address.setNumber(number);
		return address;
	}

	/**
	 * Saves the internal state of the object on the provided PersistentState
	 * object.
	 * 
	 * @param state
	 */
	public void saveToState(PersistentState state) throws PersistenceException {
		super.saveToState(state);
		state.set(FIRSTNUMBER, this.firstNumber);
		state.set(LASTNUMBER, this.lastNumber);
	}

	/**
	 * Set the state of the object from the state passed as parameter.
	 * 
	 * @param state
	 */
	public void loadFromState(PersistentState state) throws PersistenceException {
		super.setState(state);
		this.firstNumber = (RelationsComponent) state.get(FIRSTNUMBER);
		this.lastNumber = (RelationsComponent) state.get(LASTNUMBER);
	}

}
