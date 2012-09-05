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
 * with even numbers on one side of the street and odd numbers on other side.
 * 
 * @author <a href="mailto:jsanz@prodevelop.es"> Jorge Gaspar Sanz Salinas</a>
 * @author <a href="mailto:vsanjaime@prodevelop.es"> Vicente Sanjaime Calvet</a>
 */

public class DoubleRange extends AbstractRange {

	private static final Logger log = LoggerFactory
			.getLogger(DoubleRange.class);

	private static final String FIRSTLEFTNUMBER = "firstleftnumber";
	private static final String FIRSTRIGHTNUMBER = "firstrightnumber";
	private static final String LASTLEFTNUMBER = "lastleftnumber";
	private static final String LASTRIGHTNUMBER = "firstrightnumber";

	private RelationsComponent firstLeftNumber;
	private RelationsComponent firstRightNumber;
	private RelationsComponent lastLeftNumber;
	private RelationsComponent lastRightNumber;

	/**
	 * Get relation component that define the field of data source that it has
	 * the first left number of the feature
	 * 
	 * @return
	 */
	public RelationsComponent getFirstLeftNumber() {
		return firstLeftNumber;
	}

	/**
	 * Set relation component that define the field of data source that it has
	 * the first left number of the feature
	 * 
	 * @param firstLeftNumber
	 */
	public void setFirstLeftNumber(RelationsComponent firstLeftNumber) {
		this.firstLeftNumber = firstLeftNumber;
	}

	/**
	 * Get relation component that define the field of data source that it has
	 * the first right number of the feature
	 * 
	 * @return
	 */
	public RelationsComponent getFirstRightNumber() {
		return firstRightNumber;
	}

	/**
	 * Set relation component that define the field of data source that it has
	 * the first right number of the feature
	 * 
	 * @param firstRightNumber
	 */
	public void setFirstRightNumber(RelationsComponent firstRightNumber) {
		this.firstRightNumber = firstRightNumber;
	}

	/**
	 * Get relation component that define the field of data source that it has
	 * the last left number of the feature
	 * 
	 * @return
	 */
	public RelationsComponent getLastLeftNumber() {
		return lastLeftNumber;
	}

	/**
	 * Set relation component that define the field of data source that it has
	 * the last left number of the feature
	 * 
	 * @param lastLeftNumber
	 */
	public void setLastLeftNumber(RelationsComponent lastLeftNumber) {
		this.lastLeftNumber = lastLeftNumber;
	}

	/**
	 * Get relation component that define the field of data source that it has
	 * the last right number of the feature
	 * 
	 * @return
	 */
	public RelationsComponent getLastRightNumber() {
		return lastRightNumber;
	}

	/**
	 * Set relation component that define the field of data source that it has
	 * the last right number of the feature
	 * 
	 * @param lastRightNumber
	 */
	public void setLastRightNumber(RelationsComponent lastRightNumber) {
		this.lastRightNumber = lastRightNumber;
	}

	/**
	 * Spatial search over the geometries of the selected features
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

		String firstLeftNumberDesc = this.getFirstLeftNumber().getValue();
		String lastLeftNumberDesc = this.getLastLeftNumber().getValue();
		String firstRightNumberDesc = this.getFirstRightNumber().getValue();
		String lastRightNumberDesc = this.getLastRightNumber().getValue();

		// get the list
		List<ScoredFeature> sFeats = inLists.get(0);
		if (sFeats.size() > 0) {
			int number = ((NumberAddress) address).getNumber();

			try {
				for (ScoredFeature sFeat : sFeats) {

					Feature fea = sFeat.getFeature();

					int[] range = getNumberRange(fea, firstLeftNumberDesc,
							lastLeftNumberDesc, firstRightNumberDesc,
							lastRightNumberDesc);
					int firstL = range[0];
					int lastL = range[1];
					int firstR = range[2];
					int lastR = range[3];

					// Number even (Par)
					if (number % 2 == 0) {
						// Left numbers - EVEN // right numbers - ODD
						if (firstL % 2 == 0) {
							if (number >= firstL && number <= lastL) {
								// create result
								GeomMatchResult res = calculatePosition(sFeat,
										number, firstL, lastL);
								// add result to the list
								results.add(res);
							}
						}
						// Left numbers - ODD // right numbers - EVEN
						else {
							if (number >= firstR && number <= lastR) {
								// create result
								GeomMatchResult res = calculatePosition(sFeat,
										number, firstR, lastR);
								// add result to the list
								results.add(res);
							}
						}
					}
					// number odd (Impar)
					else {
						// Left numbers - EVEN // right numbers - ODD
						if (firstL % 2 == 0) {
							if (number >= firstR && number <= lastR) {
								// create result
								GeomMatchResult res = calculatePosition(sFeat,
										number, firstR, lastR);
								// add result to the list
								results.add(res);
							}
						}
						// Left numbers - ODD // right numbers - EVEN
						else {
							if (number >= firstL && number <= lastL) {
								// create result
								GeomMatchResult res = calculatePosition(sFeat,
										number, firstL, lastL);
								// add result to the list
								results.add(res);
							}
						}
					}
				}
			} catch (DataException e) {
				log.error("Error getting the features", e);
			}
		}
		return results;
	}

	/**
	 * Calculate the relative distance in line
	 * 
	 * @param number
	 * @param first
	 * @param last
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
	 * Calculate the position
	 * 
	 * @param feat
	 * @param number
	 * @param first
	 * @param last
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
	 * Calculate the position with a offset positive or negative
	 * 
	 * @param feat
	 * @param number
	 * @param first
	 * @param last
	 * @param offset
	 * @return
	 * @throws DataException
	 */
	private GeomMatchResult calculateOffsetPosition(ScoredFeature feat,
			int number, int first, int last, int offset) throws DataException {

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
	 * 
	 * @param feat
	 * @return
	 */
	private int[] getNumberRange(Feature feat, String firstRightNumberDesc,
			String lastRightNumberDesc, String firstLeftNumberDesc,
			String lastLeftNumberDesc) {

		int[] range = new int[4];

		Object oRFirst = feat.get(firstRightNumberDesc);
		Object oRLast = feat.get(lastRightNumberDesc);
		Object oLFirst = feat.get(firstLeftNumberDesc);
		Object oLLast = feat.get(lastLeftNumberDesc);

		// RIGHT FIRST
		if (oRFirst instanceof Double) {
			range[0] = ((Double) oRFirst).intValue();
		}
		if (oRFirst instanceof Integer) {
			range[0] = ((Integer) oRFirst).intValue();
		}
		if (oRFirst instanceof String) {
			range[0] = Integer.parseInt(((String) oRFirst));
		}
		// RIGHT LAST
		if (oRLast instanceof Double) {
			range[1] = ((Double) oRLast).intValue();
		}
		if (oRLast instanceof Integer) {
			range[1] = ((Integer) oRLast).intValue();
		}
		if (oRLast instanceof String) {
			range[1] = Integer.parseInt(((String) oRLast));
		}
		// LEFT FIRST
		if (oLFirst instanceof Double) {
			range[2] = ((Double) oLFirst).intValue();
		}
		if (oLFirst instanceof Integer) {
			range[2] = ((Integer) oLFirst).intValue();
		}
		if (oLFirst instanceof String) {
			range[2] = Integer.parseInt(((String) oLFirst));
		}
		// LEFT LAST
		if (oLLast instanceof Double) {
			range[3] = ((Double) oLLast).intValue();
		}
		if (oLLast instanceof Integer) {
			range[3] = ((Integer) oLLast).intValue();
		}
		if (oLLast instanceof String) {
			range[3] = Integer.parseInt(((String) oLLast));
		}

		return range;
	}

	/**
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
		state.set(FIRSTLEFTNUMBER, this.firstLeftNumber);
		state.set(FIRSTRIGHTNUMBER, this.firstRightNumber);
		state.set(LASTLEFTNUMBER, this.lastLeftNumber);
	}

	/**
	 * Set the state of the object from the state passed as parameter.
	 * 
	 * @param state
	 */
	public void loadFromState(PersistentState state) throws PersistenceException {
		super.setState(state);
		this.firstLeftNumber = (RelationsComponent) state.get(FIRSTLEFTNUMBER);
		this.firstRightNumber = (RelationsComponent) state
				.get(FIRSTRIGHTNUMBER);
		this.lastLeftNumber = (RelationsComponent) state.get(LASTLEFTNUMBER);
		this.lastRightNumber = (RelationsComponent) state.get(LASTRIGHTNUMBER);
	}

}
