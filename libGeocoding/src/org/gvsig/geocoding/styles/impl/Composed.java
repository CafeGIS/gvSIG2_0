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
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.geom.primitive.Point;
import org.gvsig.fmap.geom.util.Converter;
import org.gvsig.geocoding.address.Address;
import org.gvsig.geocoding.address.ComposedAddress;
import org.gvsig.geocoding.address.Literal;
import org.gvsig.geocoding.address.RelationsComponent;
import org.gvsig.geocoding.address.impl.DefaultAddressComponent;
import org.gvsig.geocoding.address.impl.DefaultComposedAddress;
import org.gvsig.geocoding.address.impl.DefaultLiteral;
import org.gvsig.geocoding.geommatches.MatcherUtils;
import org.gvsig.geocoding.result.GeocodingResult;
import org.gvsig.geocoding.result.ScoredFeature;
import org.gvsig.geocoding.result.impl.GeomMatchResult;
import org.gvsig.geocoding.styles.AbstractGeocodingStyle;
import org.gvsig.tools.persistence.PersistenceException;
import org.gvsig.tools.persistence.PersistentState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implemented Geocoding style with a address composed. This style can geocode a
 * cross of streets and the street result of the intersection between two
 * parallel streets.
 * 
 * @author <a href="mailto:jsanz@prodevelop.es"> Jorge Gaspar Sanz Salinas</a>
 * @author <a href="mailto:vsanjaime@prodevelop.es"> Vicente Sanjaime Calvet</a>
 */

public class Composed extends AbstractGeocodingStyle {

	private static final Logger log = LoggerFactory.getLogger(Composed.class);

	private static final String INTERSECTLITERALS = "intersectliterals";

	private List<Literal> intersectsLiterals;

	/**
	 * Get list of the secondaries literals
	 * 
	 * @return
	 */
	public List<Literal> getIntersectsLiterals() {
		return intersectsLiterals;
	}

	/**
	 * Set list of the secondaries literals
	 * 
	 * @param intersectName
	 */
	public void setIntersectsLiterals(List<Literal> intersects) {
		this.intersectsLiterals = intersects;
	}

	/**
	 * spatial search over the geometries
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

		// cross
		if (inLists.size() == 2) {

			List<ScoredFeature> mainList = inLists.get(0);
			List<ScoredFeature> secondList = inLists.get(1);

			// this list store elements of the first intersection (cross)
			if (mainList.size() > 0 && secondList.size() > 0) {
				try {
					for (ScoredFeature mainFeat : mainList) {
						com.vividsolutions.jts.geom.Geometry mainGeomJTS = Converter
								.geometryToJts(mainFeat.getFeature()
										.getDefaultGeometry());
						for (ScoredFeature secFeat : secondList) {
							com.vividsolutions.jts.geom.Geometry secGeomJTS = Converter
									.geometryToJts(secFeat.getFeature()
											.getDefaultGeometry());
							if (mainGeomJTS.touches(secGeomJTS)) {
								Point pto = MatcherUtils.intersectTwoLinesJTS(
										mainGeomJTS, secGeomJTS);
								GeomMatchResult res = new GeomMatchResult();
								// geom
								res.setGeom(pto);
								// features scored
								List<ScoredFeature> mainSources = new ArrayList<ScoredFeature>();
								mainSources.add(mainFeat);
								res.setMainSources(mainSources);
								List<ScoredFeature> secSources = new ArrayList<ScoredFeature>();
								secSources.add(secFeat);
								res.setSecondSources(secSources);
								// address
								res.setAddress(getResultAddress(mainSources,
										secSources, null));
								results.add(res);
							}
						}
					}
				} catch (Exception e) {
					log.error("Error getting the features", e);
				}
			}
		}
		// between
		if (inLists.size() == 3) {

			List<ScoredFeature> mainList = inLists.get(0);
			List<ScoredFeature> secondList = inLists.get(1);
			List<ScoredFeature> thirdList = inLists.get(2);

			// this list store elements of the first intersection (cross)
			if (mainList.size() > 0 && secondList.size() > 0) {
				try {
					for (ScoredFeature mainFeat : mainList) {
						com.vividsolutions.jts.geom.Geometry mainGeomJTS = Converter
								.geometryToJts(mainFeat.getFeature()
										.getDefaultGeometry());
						for (ScoredFeature secFeat : secondList) {
							com.vividsolutions.jts.geom.Geometry secGeomJTS = Converter
									.geometryToJts(secFeat.getFeature()
											.getDefaultGeometry());
							if (mainGeomJTS.touches(secGeomJTS)) {
								for (ScoredFeature thiFeat : thirdList) {
									com.vividsolutions.jts.geom.Geometry thiGeomJTS = Converter
											.geometryToJts(thiFeat.getFeature()
													.getDefaultGeometry());
									if (mainGeomJTS.touches(thiGeomJTS)) {
										Point pto = MatcherUtils
												.getLinePositionFromRelativeDistance(
														mainGeomJTS, 50);
										GeomMatchResult res = new GeomMatchResult();
										// geom
										res.setGeom(pto);
										// features scored
										List<ScoredFeature> mainSources = new ArrayList<ScoredFeature>();
										mainSources.add(mainFeat);
										res.setMainSources(mainSources);
										List<ScoredFeature> secSources = new ArrayList<ScoredFeature>();
										secSources.add(secFeat);
										res.setSecondSources(secSources);
										List<ScoredFeature> thiSources = new ArrayList<ScoredFeature>();
										thiSources.add(thiFeat);
										res.setThirdSources(thiSources);
										// address
										res.setAddress(getResultAddress(
												mainSources, secSources,
												thiSources));
										results.add(res);
									}
								}
							}
						}
					}
				}

				catch (DataException e) {
					log.error("Error getting the features", e);
				}
			}
		}
		return results;
	}

	/**
	 * 
	 * @return
	 */
	private Address getResultAddress(List<ScoredFeature> mainSources,
			List<ScoredFeature> secSources, List<ScoredFeature> thiSources) {

		// get the first element
		ScoredFeature mainfeat = mainSources.get(0);
		ScoredFeature secfeat = secSources.get(0);

		Literal relLiteral = this.getRelationsLiteral();
		Literal mainliteral = new DefaultLiteral();
		Literal secliteral = new DefaultLiteral();
		Literal thiliteral = new DefaultLiteral();
		// main literal
		for (Object obj : relLiteral) {
			RelationsComponent rel = (RelationsComponent) obj;
			String key = rel.getKeyElement();
			String fieldName = rel.getValue();
			Object object = null;
			String value = "";
			try {
				object = mainfeat.getFeature().get(fieldName);
				value = object.toString();
			} catch (DataException e) {
				log.error("Getting attributes ot the feature", e);
			}
			mainliteral.add(new DefaultAddressComponent(key, value));
		}
		// second literal
		for (Object obj : relLiteral) {
			RelationsComponent rel = (RelationsComponent) obj;
			String key = rel.getKeyElement();
			String fieldName = rel.getValue();
			Object object = null;
			String value = "";
			try {
				object = secfeat.getFeature().get(fieldName);
				value = object.toString();
			} catch (DataException e) {
				log.error("Getting attributes ot the feature", e);
			}
			secliteral.add(new DefaultAddressComponent(key, value));
		}

		ComposedAddress address = (ComposedAddress) new DefaultComposedAddress();
		address.setMainLiteral(mainliteral);
		List<Literal> intersect = new ArrayList<Literal>();
		intersect.add(secliteral);

		if (thiSources != null) {
			ScoredFeature thifeat = thiSources.get(0);
			// third literal
			for (Object obj : relLiteral) {
				RelationsComponent rel = (RelationsComponent) obj;
				String key = rel.getKeyElement();
				String fieldName = rel.getValue();
				Object object = null;
				String value = "";
				try {
					object = thifeat.getFeature().get(fieldName);
					value = object.toString();
				} catch (DataException e) {
					log.error("Getting attributes ot the feature", e);
				}
				thiliteral.add(new DefaultAddressComponent(key, value));
			}
			intersect.add(thiliteral);
		}

		address.setIntersectionLiterals(intersect);
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
		state.set(INTERSECTLITERALS, this.intersectsLiterals.iterator());
	}

	/**
	 * Set the state of the object from the state passed as parameter.
	 * 
	 * @param state
	 */
	public void loadFromState(PersistentState state) throws PersistenceException {
		super.setState(state);
		this.intersectsLiterals.clear();
		Iterator<Literal> it = state.getIterator(INTERSECTLITERALS);		
		while (it.hasNext()) {
			Literal lit = it.next();
			this.intersectsLiterals.add(lit);
		}		
	}

}
