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
 * 2008 Prodevelop S.L. main developer
 */

package org.gvsig.geocoding.gui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.gvsig.fmap.dal.feature.FeatureAttributeDescriptor;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.mapcontext.layers.vectorial.FLyrVect;
import org.gvsig.geocoding.pattern.Patterngeocoding;
import org.gvsig.geocoding.result.GeocodingResult;

/**
 * Model of geocoding extension
 * 
 * @author <a href="mailto:jsanz@prodevelop.es"> Jorge Gaspar Sanz Salinas</a>
 * @author <a href="mailto:vsanjaime@prodevelop.es"> Vicente Sanjaime Calvet</a>
 */

public class GeocodingModel implements IGeocodingModel {

	private Patterngeocoding pattern = null;

	private File patternFile = null;

	private FLyrVect lyr = null;

	private List<Set<GeocodingResult>> listResults;

	private boolean simple = true;

	private List<String> selDescs = null;

	private FeatureStore selectedTableStore = null;

	private int numResultShowed;

	private Integer[] exportElements = null;

	private String idFieldMassiveTable = null;

	/**
	 * Constructor
	 * 
	 */
	public GeocodingModel() {
		listResults = new ArrayList<Set<GeocodingResult>>();
	}

	/**
	 * This method gets the Geocoding Pattern
	 * 
	 * @return
	 */
	public Patterngeocoding getPattern() {
		return pattern;
	}

	/**
	 * This method sets the Geocoding Pattern
	 * 
	 * @param pat
	 */
	public void setPattern(Patterngeocoding pat) {
		pattern = pat;
	}

	/**
	 * This method gets the layer source
	 * 
	 * @return
	 */
	public FLyrVect getLyr() {
		return lyr;
	}

	/**
	 * This method sets the layer source
	 * 
	 * @param lyr
	 */
	public void setLyr(FLyrVect lyr) {
		this.lyr = lyr;
	}

	/**
	 * Get the pattern file
	 * 
	 * @return
	 */
	public File getPatternFile() {
		return patternFile;
	}

	/**
	 * Save the pattern file
	 * 
	 * @param file
	 */
	public void setPatternFile(File file) {
		this.patternFile = file;
	}

	/**
	 * @return the one results
	 */
	public Set<GeocodingResult> getOneGroupResults(int index) {
		return listResults.get(index);
	}

	/**
	 * @return the results list
	 */
	public List<Set<GeocodingResult>> getAllResults() {
		return listResults;
	}

	/**
	 * @param one
	 *            group of results the results to set
	 */
	public void setOneGroupResults(Set<GeocodingResult> result) {
		this.listResults.add(result);
	}

	/**
	 * @param one
	 *            group of results the results to set
	 */
	public void setAllResults(List<Set<GeocodingResult>> results) {
		this.listResults.clear();
		this.listResults = results;
	}

	/**
	 * @return the simple
	 */
	public boolean isSimple() {
		return simple;
	}

	/**
	 * @param simple
	 *            the simple to set
	 */
	public void setSimple(boolean simple) {
		this.simple = simple;
	}

	/**
	 * get list fields descriptor selected table
	 * 
	 * @return
	 */
	public List<String> getListDescriptorSelectedTable() {
		return selDescs;
	}

	/**
	 * set list of field descriptors of selected table
	 * 
	 * @param descs
	 */
	public void setListDescriptorSelectedTable(
			List<String> descs) {
		selDescs = descs;

	}

	/**
	 * get table store of selected table (massive geocoding process)
	 * 
	 * @return
	 */
	public FeatureStore getSelectedTableStore() {
		return selectedTableStore;
	}

	/**
	 * set table store of selected table (massive geocoding process)
	 */
	public void setSelectedTableStore(FeatureStore selectedTableStore) {
		this.selectedTableStore = selectedTableStore;
	}

	/**
	 * Add one result to list of results
	 * 
	 * @param result
	 */
	public void addResult(Set<GeocodingResult> result) {
		this.listResults.add(result);
	}

	/**
	 * Clear list of geocoding results
	 */
	public void clearResults() {
		this.listResults.clear();
	}

	/**
	 * get number of result of the results list showed
	 */
	public int getNumResultShowed() {
		return numResultShowed;
	}

	/**
	 * set number of result of results list showed
	 * 
	 * @param
	 */
	public void setNumResultShowed(int i) {
		this.numResultShowed = i;

	}

	/**
	 * get results export elements
	 * 
	 * @return
	 */
	public Integer[] getExportElements() {
		return exportElements;
	}

	/**
	 * set results export elemnts
	 * 
	 * @param exportElements
	 */
	public void setExportElements(Integer[] exportElements) {
		this.exportElements = exportElements;
	}

	/**
	 * get descriptor id field of massive table
	 * 
	 * @return
	 */
	public String getIdMasiveTable() {
		return idFieldMassiveTable;
	}

	/**
	 * set descriptor id field of massive table
	 * 
	 * @param descriptor
	 */
	public void setIdMasiveTable(String descriptor) {
		this.idFieldMassiveTable = descriptor;
	}

}
