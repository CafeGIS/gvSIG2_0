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

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.mapcontext.rendering.symbols.ISymbol;
import org.gvsig.fmap.mapcontext.rendering.symbols.SimpleMarkerSymbol;
import org.gvsig.fmap.mapcontext.rendering.symbols.SymbologyFactory;
import org.gvsig.geocoding.address.Address;
import org.gvsig.geocoding.result.GeocodingResult;
import org.gvsig.geocoding.result.ScoredFeature;


//
/**
 * Implementation with the results of geocoding process. It has the geometry of the
 * geocoding position and the features related. This objects are stored in a
 * TreeSet the best to worst quality.
 * 
 * @author <a href="mailto:jsanz@prodevelop.es"> Jorge Gaspar Sanz Salinas</a>
 * @author <a href="mailto:vsanjaime@prodevelop.es"> Vicente Sanjaime Calvet</a>
 */

public class GeomMatchResult implements GeocodingResult,
		Comparable<GeomMatchResult> {

	private List<List<ScoredFeature>> sources = new ArrayList<List<ScoredFeature>>();	private Geometry geom = null;
	private Address address;

	/**
	 * Get address
	 * 
	 * @return address
	 */
	public Address getAddress() {
		return this.address;
	}

	/**
	 * Get geometry
	 * 
	 * @return geom
	 */
	public Geometry getGeometry() {
		return geom;
	}

	/**
	 * Get the score. return the first score of the main list of scored feature
	 * 
	 * @return score
	 */
	public double getScore() {
		return sources.get(0).get(0).getScore();
	}

	/**
	 * Get list of the scored features related with this result
	 * 
	 * @return
	 */
	public List<List<ScoredFeature>> getSources() {
		return sources;
	}

	/**
	 * Get main source list
	 * 
	 * @return source
	 */
	public List<ScoredFeature> getMainSources() {
		return sources.get(0);
	}

	/**
	 * get second source list
	 * 
	 * @return source
	 */
	public List<ScoredFeature> getSecondSources() {
		if (sources.size() == 2) {
			return sources.get(1);
		}
		return null;
	}

	/**
	 * get third source list
	 * 
	 * @return source
	 */
	public List<ScoredFeature> getThirdSources() {
		if (sources.size() == 3) {
			return sources.get(2);
		}
		return null;
	}

	/**
	 * Set the list of list with scored features
	 * 
	 * @param sources
	 */
	public void setSources(List<List<ScoredFeature>> sources) {
		this.sources = sources;
	}

	/**
	 * Set main source list
	 * 
	 * @param source
	 */
	public void setMainSources(List<ScoredFeature> source) {
		this.sources.add(0, source);
	}

	/**
	 * Set second source list
	 * 
	 * @param source
	 */
	public void setSecondSources(List<ScoredFeature> source) {
		this.sources.add(1, source);
	}

	/**
	 * Set third source list
	 * 
	 * @param source
	 */
	public void setThirdSources(List<ScoredFeature> source) {
		this.sources.add(2, source);
	}

	/**
	 * Get geometry
	 * 
	 * @return
	 */
	public Geometry getGeom() {
		return geom;
	}

	/**
	 * Set geometry (point) of the result
	 * 
	 * @param geom
	 */
	public void setGeom(Geometry geom) {
		this.geom = geom;
	}

	/**
	 * Compare to other result
	 */
	public int compareTo(GeomMatchResult gmres) {

		double score = gmres.getScore();
		if (this.getScore() > score) {
			return -1;
		} else if (this.getScore() < score) {
			return 1;
		} else {
			Address adr = this.getAddress();
			if (adr != null) {
				return adr.compareTo(gmres.getAddress());
			} else {
				return 1;
			}
		}
	}

	/**
	 * Set address
	 * 
	 * @param address
	 */
	public void setAddress(Address address) {
		this.address = address;
	}

	/**
	 * Get shape to draw result position on gvSIG view
	 * 
	 * @return
	 */
	public ISymbol getSymbolPosition() {

		SimpleMarkerSymbol symbol = (SimpleMarkerSymbol) SymbologyFactory
				.createDefaultMarkerSymbol();
		symbol.setStyle(SimpleMarkerSymbol.CIRCLE_STYLE);
		symbol.setSize(4);
		symbol.setColor(Color.RED);
		return symbol;
	}

}
