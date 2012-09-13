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
import java.util.List;
import java.util.Set;

import org.gvsig.fmap.dal.feature.FeatureAttributeDescriptor;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.mapcontext.layers.vectorial.FLyrVect;
import org.gvsig.geocoding.pattern.Patterngeocoding;
import org.gvsig.geocoding.result.GeocodingResult;

/**
 * interface of the extensión model
 * 
 * @author <a href="mailto:jsanz@prodevelop.es"> Jorge Gaspar Sanz Salinas</a>
 * @author <a href="mailto:vsanjaime@prodevelop.es"> Vicente Sanjaime Calvet</a>
 */
public interface IGeocodingModel {

	/**
	 * Default parameters of search
	 */

	public static final int DEFMAX = 5;
	public static final int DEFQUALI = 80;

	/**
	 * This method gets the geocoding pattern from the model
	 * 
	 * @return
	 */
	public Patterngeocoding getPattern();

	/**
	 * This method sets the Geocoding Pattern
	 * 
	 * @param pat
	 */
	public void setPattern(Patterngeocoding pat);

	/**
	 * This method gets the layer source
	 * 
	 * @return
	 */
	public FLyrVect getLyr();

	/**
	 * This method sets the layer source
	 * 
	 * @param lyr
	 */
	public void setLyr(FLyrVect lyr);

	/**
	 * Save the pattern file
	 * 
	 * @param file
	 */
	public void setPatternFile(File file);

	/**
	 * Get the pattern file
	 * 
	 * @return
	 */
	public File getPatternFile();

	/**
	 * @return the one results
	 */
	public Set<GeocodingResult> getOneGroupResults(int index);

	/**
	 * @return the results list
	 */
	public List<Set<GeocodingResult>> getAllResults();

	/**
	 * @param one
	 *            group of results the results to set
	 */
	public void setOneGroupResults(Set<GeocodingResult> results);

	/**
	 * @param one
	 *            group of results the results to set
	 */
	public void setAllResults(List<Set<GeocodingResult>> results);

	/**
	 * @return the simple
	 */
	public boolean isSimple();

	/**
	 * @param simple
	 *            the simple to set
	 */
	public void setSimple(boolean simple);

	/**
	 * set list of field descriptors of selected table
	 * 
	 * @param descs
	 */
	public void setListDescriptorSelectedTable(
			List<String> descs);

	/**
	 * get list fields descriptor selected table
	 * 
	 * @return
	 */
	public List<String> getListDescriptorSelectedTable();

	/**
	 * get store of selected table
	 * 
	 * @return
	 */
	public FeatureStore getSelectedTableStore();

	/**
	 * set store of selected table
	 * 
	 * @param selectedTableStore
	 */
	public void setSelectedTableStore(FeatureStore selectedTableStore);

	/**
	 * Add one result to list of results
	 * 
	 * @param result
	 */
	public void addResult(Set<GeocodingResult> result);

	/**
	 * Clear list of geocoding results
	 */
	public void clearResults();

	/**
	 * set number of result of results list showed
	 * 
	 * @param i
	 */
	public void setNumResultShowed(int i);

	/**
	 * get number of result of the results list showed
	 * 
	 * @return
	 */
	public int getNumResultShowed();

	/**
	 * get selected results export elements
	 * 
	 * @return
	 */
	public Integer[] getExportElements();

	/**
	 * set selected results export elements
	 * 
	 * @param exportElements
	 */
	public void setExportElements(Integer[] exportElements);

	/**
	 * set descriptor id field of massive table
	 * @param descriptor
	 */
	public void setIdMasiveTable(String descriptor);
	
	/**
	 * get descriptor id field of massive table
	 * @return
	 */
	public String getIdMasiveTable();

	

}
