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

/**
 * 
 * 
 * @author <a href="mailto:jsanz@prodevelop.es"> Jorge Gaspar Sanz Salinas</a>
 * @author <a href="mailto:vsanjaime@prodevelop.es"> Vicente Sanjaime Calvet</a>
 */

package org.gvsig.normalization.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.gvsig.fmap.dal.DataTypes;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.EditableFeature;
import org.gvsig.fmap.dal.feature.EditableFeatureType;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.normalization.Normalizer;
import org.gvsig.normalization.algorithm.NormalizationAlgorithm;
import org.gvsig.normalization.pattern.Datevalue;
import org.gvsig.normalization.pattern.Decimalvalue;
import org.gvsig.normalization.pattern.Element;
import org.gvsig.normalization.pattern.Fieldtype;
import org.gvsig.normalization.pattern.Integervalue;
import org.gvsig.normalization.pattern.Patternnormalization;
import org.gvsig.normalization.pattern.Stringvalue;
import org.gvsig.tools.persistence.PersistenceException;
import org.gvsig.tools.persistence.PersistentState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NormalizationTransform extends Normalizer {

	private static final Logger log = LoggerFactory
			.getLogger(NormalizationTransform.class);

	private Patternnormalization pattern = null;
	private int positionFieldSelected;
	private NormalizationAlgorithm algorithm = null;
	private Map<Integer, Integer> newFields = new HashMap<Integer, Integer>();
	private FeatureType originalFeatureType = null;

	/**
	 * Constructor
	 */
	public NormalizationTransform() {
		super();
	}

	/**
	 * Initialize transform
	 * 
	 * @param store
	 * @param pat
	 * @param position
	 * @param algo
	 * @throws DataException
	 */
	public void initialize(FeatureStore store, Patternnormalization pat,
			int position, NormalizationAlgorithm algo) throws DataException {
		setFeatureStore(store);
		this.pattern = pat;
		this.algorithm = algo;
		this.positionFieldSelected = position;
		this.newFields.clear();
		this.originalFeatureType = this.getFeatureStore()
				.getDefaultFeatureType();
		// modify feature type
		this.transformFeatureType();
	}

	/**
	 * Apply transform
	 */
	public void applyTransform(Feature source, EditableFeature target)
			throws DataException {

		target.copyFrom(source);

		String chain = source.get(this.positionFieldSelected).toString();

		List<String> chains = this.splitChain(chain);

		Iterator<Entry<Integer, Integer>> it = newFields.entrySet().iterator();
		int contador = 0;
		while (it.hasNext()) {
			Map.Entry<Integer, Integer> entry = (Map.Entry<Integer, Integer>) it
					.next();
			int fieldPosition = entry.getKey();
			int fieldType = entry.getValue();
			String value = chains.get(contador);
			switch (fieldType) {
			case DataTypes.STRING:
				target.setString(fieldPosition, value);
				break;
			case DataTypes.DATE:
				String datePat = getDateFormat(contador);
				Date dateValue = null;
				try {
					dateValue = formatDates(value, datePat);
				} catch (ParseException e) {
					log.error("Error parsing the date", e);
				}
				target.setDate(fieldPosition, dateValue);
				break;
			case DataTypes.INT:
				int intValue = new Integer(value);
				target.setInt(fieldPosition, intValue);
				break;
			case DataTypes.DOUBLE:
				double douValue = new Double(value);
				target.setDouble(fieldPosition, douValue);
				break;
			}
			contador++;
		}

	}

	/**
	 * 
	 */
	public FeatureType getSourceFeatureTypeFrom(FeatureType targetFeatureType) {
		return this.originalFeatureType;
	}

	/**
	 * 
	 */
	public boolean isTransformsOriginalValues() {
		return false;
	}

	/**
	 * 
	 */
	public void loadFromState(PersistentState state)
			throws PersistenceException {
	}

	/**
	 * 
	 */
	public void saveToState(PersistentState state) throws PersistenceException {

	}

	/**
	 * Transform Default Feature Type from pattern
	 * 
	 * @throws DataException
	 */
	private void transformFeatureType() throws DataException {

		FeatureType type = this.getFeatureStore().getDefaultFeatureType();
		EditableFeatureType eType = type.getEditable();

		Iterator<Element> eles = this.pattern.getElements().iterator();
		while (eles.hasNext()) {
			Element ele = eles.next();
			String fieldName = ele.getFieldname();
			Fieldtype fieldType = ele.getFieldtype();
			// Field type (STRING)
			if (((Stringvalue) fieldType.getStringvalue()) != null) {
				Stringvalue val = fieldType.getStringvalue();
				int siz = val.getStringvaluewidth();
				eType.add(fieldName, DataTypes.STRING, siz);
				int position = eType.size() - 1;
				newFields.put(position, DataTypes.STRING);
			}
			// Field type (DATE)
			else if (((Datevalue) fieldType.getDatevalue()) != null) {
				eType.add(fieldName, DataTypes.DATE);
				int position = eType.size() - 1;
				newFields.put(position, DataTypes.DATE);
			}
			// Field type (INTEGER)
			else if (((Integervalue) fieldType.getIntegervalue()) != null) {
				Integervalue val = fieldType.getIntegervalue();
				int siz = val.getIntegervaluewidth();
				eType.add(fieldName, DataTypes.INT, siz);
				int position = eType.size() - 1;
				newFields.put(position, DataTypes.INT);
			}
			// Field type (DOUBLE)
			else if (((Decimalvalue) fieldType.getDecimalvalue()) != null) {
				Decimalvalue val = fieldType.getDecimalvalue();
				int sizeint = val.getDecimalvalueint();
				int sizedec = val.getDecimalvaluedec();
				eType.add(fieldName, DataTypes.DOUBLE, sizeint + 1 + sizedec);
				int position = eType.size() - 1;
				newFields.put(position, DataTypes.DOUBLE);
			}
		}
		FeatureType type2 = eType.getNotEditableCopy();
		List<FeatureType> allTypes = new ArrayList<FeatureType>();
		allTypes.add(type2);
		this.setFeatureTypes(allTypes, type2);
		System.out.println("");
	}

	/**
	 * Split chain
	 * 
	 * @param chain
	 * @return
	 */
	private List<String> splitChain(String chain) {
		List<String> chains = this.algorithm.splitChain(chain);
		List<String> filteredchains = this.algorithm.filterSplitChains(chains);
		return filteredchains;
	}

	/**
	 * This method gets the Date format of a field from pattern
	 * 
	 * @param posi
	 * @return date formatted
	 */
	private String getDateFormat(int posi) {
		String format = "";
		Element adres = (Element) pattern.getElements().get(posi);
		format = adres.getFieldtype().getDatevalue().getDatevalueformat();
		return format;
	}

	/**
	 * This method formats the date from a date pattern
	 * 
	 * @see http 
	 *      ://java.sun.com/j2se/1.5.0/docs/api/java/text/SimpleDateFormat.html
	 * 
	 * @param date
	 * @param pattern
	 * @return Value
	 * @throws ParseException
	 */
	private Date formatDates(String date, String pattern) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		Date fecha = sdf.parse(date);
		return fecha;
	}

}
