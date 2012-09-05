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
 * 2008 PRODEVELOP		Main development
 */

package org.gvsig.geocoding.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.gvsig.fmap.dal.DALLocator;
import org.gvsig.fmap.dal.DataManager;
import org.gvsig.fmap.dal.DataStore;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.FeatureQuery;
import org.gvsig.fmap.dal.feature.FeatureSelection;
import org.gvsig.fmap.dal.feature.FeatureSet;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.geocoding.DataGeocoder;
import org.gvsig.geocoding.address.Address;
import org.gvsig.geocoding.address.AddressComponent;
import org.gvsig.geocoding.address.ComposedAddress;
import org.gvsig.geocoding.address.Literal;
import org.gvsig.geocoding.address.NumberAddress;
import org.gvsig.geocoding.address.RelationsComponent;
import org.gvsig.geocoding.pattern.Patterngeocoding;
import org.gvsig.geocoding.result.GeocodingResult;
import org.gvsig.geocoding.result.ScoredFeature;
import org.gvsig.geocoding.result.impl.DefaultScoredFeature;
import org.gvsig.geocoding.styles.AbstractGeocodingStyle;
import org.gvsig.geocoding.styles.impl.Composed;
import org.gvsig.geocoding.styles.impl.DoubleRange;
import org.gvsig.geocoding.styles.impl.SimpleCentroid;
import org.gvsig.geocoding.styles.impl.SimpleRange;
import org.gvsig.tools.evaluator.Evaluator;
import org.gvsig.tools.locator.LocatorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Data geocoder implementation
 * 
 * @author <a href="mailto:jsanz@prodevelop.es"> Jorge Gaspar Sanz Salinas</a>
 * @author <a href="mailto:vsanjaime@prodevelop.es"> Vicente Sanjaime Calvet</a>
 */

public class DataGeocoderImpl implements DataGeocoder {

	private Logger log = LoggerFactory.getLogger(DataGeocoderImpl.class);
	private Patterngeocoding pattern = null;
	private DataManager manager = null;

	/**
	 * Constructor with one pattern
	 * 
	 * @param pattern
	 */
	public DataGeocoderImpl() {
		manager = DALLocator.getDataManager();
	}

	/**
	 * Constructor with one pattern
	 * 
	 * @param pattern
	 */
	public DataGeocoderImpl(Patterngeocoding pattern) {
		this.pattern = pattern;
		manager = DALLocator.getDataManager();
	}

	/**
	 * set the pattern
	 * 
	 * @param pattern
	 */
	public void setPattern(Patterngeocoding pattern) {
		this.pattern = pattern;

	}

	/**
	 * Get the pattern
	 * 
	 * @return pattern
	 */
	public Patterngeocoding getPattern() {
		return this.pattern;

	}

	/**
	 * Geocoding process
	 * 
	 * @param address
	 * @return results
	 */
	public Set<GeocodingResult> geocode(Address address)
			throws LocatorException, DataException {

		Set<GeocodingResult> results = new TreeSet<GeocodingResult>();
		// get the FeatureStore registred in the pattern
		DataStore store = getPattern().getSource().getLayerSource();
		// get the style
		AbstractGeocodingStyle astyle = getPattern().getSource().getStyle();
		// get literal with relations with the store
		Literal relationsLiteral = astyle.getRelationsLiteral();
		// list of list with one or more literal results
		List<List<ScoredFeature>> lists = new ArrayList<List<ScoredFeature>>();

		// SIMPLE CENTROID
		if (astyle instanceof SimpleCentroid) {
			SimpleCentroid style = (SimpleCentroid) astyle;
			Literal addressLiteral = address.getMainLiteral();
			// literal search
			List<ScoredFeature> literalResults = literalSearch(
					relationsLiteral, addressLiteral, store);
			lists.add(literalResults);
			// Geom search
			results = style.match(lists, address);
		}

		// SIMPLE RANGE
		else if (astyle instanceof SimpleRange) {
			SimpleRange style = (SimpleRange) astyle;
			Literal addressLiteral = ((NumberAddress) address).getMainLiteral();
			// literal search
			List<ScoredFeature> literalResults = literalSearch(
					relationsLiteral, addressLiteral, store);
			lists.add(literalResults);
			// geom search
			results = style.match(lists, address);
		}

		// DOUBLE RANGE
		else if (astyle instanceof DoubleRange) {
			DoubleRange style = (DoubleRange) astyle;
			Literal addressLiteral = ((NumberAddress) address).getMainLiteral();
			// literal search
			List<ScoredFeature> literalResults = literalSearch(
					relationsLiteral, addressLiteral, store);
			lists.add(literalResults);
			// number search
			results = style.match(lists, address);
		}

		// STYLE COMPOSED
		else if (astyle instanceof Composed) {
			Composed style = (Composed) astyle;
			ComposedAddress cAddress = (ComposedAddress) address;

			// main literal search
			Literal mainAddressLiteral = cAddress.getMainLiteral();
			List<ScoredFeature> literalResults = literalSearch(
					relationsLiteral, mainAddressLiteral, store);
			lists.add(literalResults);
			// search in others literals
			List<Literal> intersectslist = cAddress.getIntersectionLiterals();
			for (Literal addrLiteral : intersectslist) {
				// literal search
				List<ScoredFeature> secList = literalSearch(relationsLiteral,
						addrLiteral, store);
				lists.add(secList);
			}
			// Match
			results = style.match(lists, address);
		}
		return results;
	}

	/**
	 * First scouting process, search literal elements in data store and return
	 * a list with ok scored features
	 * 
	 * @param relationsLiteral
	 * @param addressLiteral
	 * @param store
	 * @return
	 * @throws DataException
	 */
	private List<ScoredFeature> literalSearch(Literal relationsLiteral,
			Literal addressLiteral, DataStore store) throws DataException {

		List<FeatureSelection> sels = new ArrayList<FeatureSelection>();
		FeatureSet features = null;

		String exp = "";

		int i = 0;
		// search features related with the parameters
		for (Object obj : relationsLiteral) {
			RelationsComponent lit = (RelationsComponent) obj;
			FeatureSelection selection = ((FeatureStore) store)
					.createFeatureSelection();
			FeatureQuery query = ((FeatureStore) store).createFeatureQuery();
			String key = lit.getKeyElement().trim();
			String chain = getValueAddress(key, addressLiteral);

			String fname = lit.getValue();
			if (i == 0) {
				exp = fname + " like '%" + chain + "%'";
			} else {
				exp = exp + " AND " + fname + " like '%" + chain + "%'";
			}

			Evaluator eval = manager.createExpresion(exp);

			query.setFilter(eval);
			features = ((FeatureStore) store).getFeatureSet(query);
			selection.select(features);
			sels.add(selection);
			log.debug("Iteration " + i + " - Selection : "
					+ selection.getSelectedCount());
			i++;
		}
		// Put scores
		List<ScoredFeature> scores = createLiteralScores(store, sels);

		return scores;
	}

	/**
	 * Create score of the feature to first literal search
	 * 
	 * @param store
	 * @param sels
	 * @return
	 * @throws DataException
	 */
	@SuppressWarnings("unchecked")
	private List<ScoredFeature> createLiteralScores(DataStore store,
			List<FeatureSelection> sels) throws DataException {

		List<ScoredFeature> scores = new ArrayList<ScoredFeature>();

		FeatureSet features = ((FeatureStore) store).getFeatureSet();

		for (Iterator<Feature> it = features.iterator(); it.hasNext();) {
			Feature feature = it.next();
			double num = 0;
			for (int i = 0; i < sels.size(); i++) {
				FeatureSelection sel = sels.get(i);
				if (sel.isSelected(feature)) {
					num = num + scorePonderated(i, sels.size());
				}
			}
			if (num > 0.0) {
				ScoredFeature scoFeat = new DefaultScoredFeature();
				scoFeat.setReference(feature.getReference());
				scoFeat.setScore(num);
				scores.add(scoFeat);
			}
		}
		return scores;
	}

	/**
	 * Get the score value ponderated
	 * 
	 * @param i
	 *            position
	 * @param total
	 * @return score
	 */
	private double scorePonderated(int i, int total) {
		if (total == 1) {
			return 100.0;
		}
		double score = 100.0 / (2.0 * (i + 1));
		return score;
	}

	/**
	 * Get address value from key
	 * 
	 * @param key
	 * @param addressLiteral
	 * @return
	 */
	private String getValueAddress(String key, Literal addressLiteral) {
		String value = "";
		for (Object obj : addressLiteral) {
			AddressComponent aComp = (AddressComponent) obj;
			String key2 = aComp.getKeyElement();
			if (key.compareTo(key2) == 0) {
				value = aComp.getValue();
				break;
			}
		}
		return value;
	}

}
