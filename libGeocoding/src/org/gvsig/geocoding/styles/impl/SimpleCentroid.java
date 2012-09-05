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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.primitive.Point;
import org.gvsig.fmap.geom.util.Converter;
import org.gvsig.geocoding.address.Address;
import org.gvsig.geocoding.address.Literal;
import org.gvsig.geocoding.address.RelationsComponent;
import org.gvsig.geocoding.address.impl.DefaultAddress;
import org.gvsig.geocoding.address.impl.DefaultAddressComponent;
import org.gvsig.geocoding.address.impl.DefaultLiteral;
import org.gvsig.geocoding.geommatches.MatcherUtils;
import org.gvsig.geocoding.result.DissolveResult;
import org.gvsig.geocoding.result.GeocodingResult;
import org.gvsig.geocoding.result.ScoredFeature;
import org.gvsig.geocoding.result.impl.GeomMatchResult;
import org.gvsig.geocoding.styles.AbstractGeocodingStyle;
import org.gvsig.tools.persistence.PersistenceException;
import org.gvsig.tools.persistence.PersistentState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implemented Geocoding style with search by centroid. Search of the centroid of points,
 * lines or polygons geometries.
 * 
 * @author <a href="mailto:jsanz@prodevelop.es"> Jorge Gaspar Sanz Salinas</a>
 * @author <a href="mailto:vsanjaime@prodevelop.es"> Vicente Sanjaime Calvet</a>
 */

public class SimpleCentroid extends AbstractGeocodingStyle {

	private static final Logger log = LoggerFactory
			.getLogger(SimpleCentroid.class);

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

		// catch the first element of the relations Literal to get the field
		// descriptor
		// to make the dissolve process
		RelationsComponent rComp0 = (RelationsComponent) this
				.getRelationsLiteral().get(0);
		String descriptDissol = rComp0.getValue();

		// Get the first list
		List<ScoredFeature> firstListFeatures = inLists.get(0);
		if (firstListFeatures.size() > 0) {
			try {
				Geometry geometry = firstListFeatures.get(0).getFeature()
						.getDefaultGeometry();
				int dim = MatcherUtils.getGeometryDimension(geometry);

				// POINTs FEATURES
				if (dim == 0) {
					results = createPointsFeaturesResult(firstListFeatures);
				}
				// OTHER GEOMETRIES TYPES
				else {
					// dissolve features selected
					List<DissolveResult> disRes = this.dissolveScoredFeatures(
							descriptDissol, firstListFeatures);

					for (DissolveResult dissol : disRes) {
						GeomMatchResult matchres = new GeomMatchResult();
						Point position = null;

						Geometry geom = dissol.getGeom();
						com.vividsolutions.jts.geom.Geometry gemJTS = Converter
								.geometryToJts(geom);
						// Lines
						if (gemJTS.getDimension() == 1) {
							com.vividsolutions.jts.geom.Geometry geoJTS = Converter
									.geometryToJts(geom);
							position = MatcherUtils
									.getLinePositionFromRelativeDistance(
											geoJTS, 50);
						}
						// Polys
						if (gemJTS.getDimension() == 2) {
							position = MatcherUtils.internalPointGeometry(geom);
						}
						matchres.setGeom(position);
						matchres.setMainSources(dissol.getScoredFeatures());
						matchres.setAddress(getResultAddress(dissol
								.getScoredFeatures()));
						results.add(matchres);
						// log.debug("Insert result");
					}
				}
			} catch (DataException e) {
				log.error("Error getting the features", e);
			}
		}
		log.debug("Results:" + results.size());
		return results;
	}

	/**
	 * Dissolve features geometries behind one field
	 * 
	 * @param field
	 * @param features
	 * @return list with dissolved features
	 * @throws DataException
	 */
	private List<DissolveResult> dissolveScoredFeatures(String field,
			List<ScoredFeature> features) throws DataException {

		List<DissolveResult> listResults = new ArrayList<DissolveResult>();

		// Group geometries by main attribute
		HashMap<String, List<ScoredFeature>> dissolAttributes = MatcherUtils
				.groupScoredFeaturesByAttribute(field, features);

		// For each group of features, to do geometries dissolve process getting
		// at the end one o more geometries
		for (Iterator<Map.Entry<String, List<ScoredFeature>>> iterator = dissolAttributes
				.entrySet().iterator(); iterator.hasNext();) {
			Map.Entry<String, List<ScoredFeature>> e = iterator.next();
			List<ScoredFeature> feats = e.getValue();
			Geometry geom = feats.get(0).getFeature().getDefaultGeometry();
			com.vividsolutions.jts.geom.Geometry geoJTS = Converter
					.geometryToJts(geom);
			int dim = geoJTS.getDimension();

			List<DissolveResult> geomsDissol = new ArrayList<DissolveResult>();

			// dissolve geoms
			if (dim == 1) {
				// log.debug("DISSOLVE LINES");
				geomsDissol = MatcherUtils.dissolveGeomsJTS(feats,
						MatcherUtils.LINES);
			}
			if (dim == 2) {
				// log.debug("DISSOLVE POLYS");
				geomsDissol = MatcherUtils.dissolveGeomsJTS(feats,
						MatcherUtils.POLYS);
			}

			// insert each dissolve result in the general array
			for (DissolveResult dissolveResult : geomsDissol) {
				listResults.add(dissolveResult);
			}
		}
		return listResults;
	}

	/**
	 * Get result address
	 * 
	 * @return
	 */
	private Address getResultAddress(List<ScoredFeature> sources) {

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
		Address address = new DefaultAddress();
		address.setMainLiteral(literal);

		return address;
	}

	/**
	 * This method creates the output from points features. Because this
	 * elements can't dissolve themself.
	 * 
	 * @param sFeats
	 * @return
	 * @throws DataException
	 */
	private Set<GeocodingResult> createPointsFeaturesResult(
			List<ScoredFeature> sFeats) {

		Set<GeocodingResult> results = new TreeSet<GeocodingResult>();

		for (ScoredFeature sFeat : sFeats) {
			GeomMatchResult result = new GeomMatchResult();
			try {
				result.setGeom(sFeat.getFeature().getDefaultGeometry());
				List<ScoredFeature> sources = new ArrayList<ScoredFeature>();
				sources.add(sFeat);
				result.setMainSources(sources);
				result.setAddress(getResultAddress(sources));
				results.add(result);
			} catch (DataException e) {
				log.error("Error getting the feature", e);
			}
		}
		return results;
	}

	/**
	 * Saves the internal state of the object on the provided PersistentState
	 * object.
	 * 
	 * @param state
	 */
	public void saveToState(PersistentState state) throws PersistenceException {
		super.saveToState(state);
	}

	/**
	 * Set the state of the object from the state passed as parameter.
	 * 
	 * @param state
	 */
	public void loadFromState(PersistentState state) throws PersistenceException {
		super.setState(state);
	}
}
