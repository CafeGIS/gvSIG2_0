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

package org.gvsig.normalization.gui;

import java.awt.Component;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.FeatureSet;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.normalization.algorithm.NormalizationAlgorithm;
import org.gvsig.normalization.algorithm.impl.DefaultNormalizationAlgorithm;
import org.gvsig.normalization.pattern.Element;
import org.gvsig.normalization.pattern.Fieldseparator;
import org.gvsig.normalization.pattern.Fieldtype;
import org.gvsig.normalization.pattern.Infieldseparators;
import org.gvsig.normalization.pattern.Patternnormalization;
import org.gvsig.normalization.pattern.Stringvalue;
import org.gvsig.normalization.pattern.impl.DefaultElement;
import org.gvsig.normalization.pattern.impl.DefaultFieldseparator;
import org.gvsig.normalization.pattern.impl.DefaultFieldtype;
import org.gvsig.normalization.pattern.impl.DefaultInfieldseparators;
import org.gvsig.normalization.pattern.impl.DefaultPatternnormalization;
import org.gvsig.normalization.pattern.impl.DefaultStringvalue;
import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.persistence.PersistenceException;
import org.gvsig.tools.persistence.PersistenceManager;
import org.gvsig.tools.persistence.PersistentState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sun.text.Normalizer;

import com.iver.andami.PluginServices;
import com.iver.utiles.GenericFileFilter;
import com.iver.utiles.XMLEntity;

/**
 * Controller of the Normalization panel
 * 
 * @author <a href="mailto:jsanz@prodevelop.es"> Jorge Gaspar Sanz Salinas</a>
 * @author <a href="mailto:vsanjaime@prodevelop.es"> Vicente Sanjaime Calvet</a>
 */

public class NormalizationPanelController {

	private static final Logger log = LoggerFactory
			.getLogger(NormalizationPanelController.class);

	public static final int SAMPLES = 3;

	private int selectedField;
	private Patternnormalization pattern;
	private FeatureStore store = null;
	private int contador = 1;
	private String[] samples = new String[SAMPLES];

	/* SINGLETON DEFINITION */
	private volatile static NormalizationPanelController uniqueInstance;

	/**
	 * Get the instance of panel controller
	 * 
	 * @return panel model
	 */
	public static NormalizationPanelController getInstance() {
		if (uniqueInstance == null) {
			synchronized (NormalizationPanelController.class) {
				if (uniqueInstance == null) {
					uniqueInstance = new NormalizationPanelController();
				}
			}
		}
		return uniqueInstance;
	}

	/**
	 * Private constructor
	 */
	private NormalizationPanelController() {
		this.setPattern(initializeDefaultPattern());
	}

	/**
	 * This method fills the GUI sample table with the samples
	 * 
	 * @return Object[][]
	 */
	protected Object[][] normalizeOutSamples() {

		int numFields = pattern.getElements().size();
		Object[][] results = new Object[SAMPLES][numFields];

		NormalizationAlgorithm na = new DefaultNormalizationAlgorithm(
				this.pattern);
		List<String> chains = null;
		for (int i = 0; i < SAMPLES; i++) {
			chains = na.splitChain(samples[i]);
			numFields = chains.size();
			for (int j = 0; j < numFields; j++) {
				results[i][j] = chains.get(j);
			}
		}
		return results;
	}

	/**
	 * This method up the selected element one position in the list of Elements
	 * 
	 * @param pos
	 */
	protected void fieldUp(int pos) {

		int nu = pattern.getElements().size();

		if (pos > 0 && nu > 1) {
			int newpos = pos - 1;
			Element[] ad = pattern.getArrayElements();
			Element ele21 = ad[pos];
			Element ele12 = ad[newpos];

			ad[newpos] = ele21;
			ad[pos] = ele12;
			List<Element> elems = new ArrayList<Element>();
			for (int i = 0; i < ad.length; i++) {
				elems.add(ad[i]);
			}
			pattern.setElements(elems);
		}
	}

	/**
	 * This method down the selected element one position in the list of
	 * Elements
	 * 
	 * @param pos
	 */
	protected void fieldDown(int pos) {
		int nu = pattern.getElements().size();

		if (pos != (nu - 1) && nu > 1) {
			int newpos = pos + 1;
			Element[] ad = pattern.getArrayElements();
			Element ele21 = ad[pos];
			Element ele12 = ad[newpos];

			ad[newpos] = ele21;
			ad[pos] = ele12;
			List<Element> elems = new ArrayList<Element>();
			for (int i = 0; i < ad.length; i++) {
				elems.add(ad[i]);
			}
			pattern.setElements(elems);
		}
	}

	/**
	 * This method adds a new element to the pattern
	 */

	protected void addField() {

		contador++;
		int tam = pattern.getElements().size();
		Element eleme = new DefaultElement();

		Fieldseparator fsep = new DefaultFieldseparator();
		fsep.setSemicolonsep(true);
		fsep.setJoinsep(false);
		fsep.setColonsep(false);
		fsep.setSpacesep(false);
		fsep.setTabsep(false);

		String nam = "";
		boolean isOkName = true;
		do {
			isOkName = true;
			nam = "NewField" + contador;
			for (int i = 0; i < tam; i++) {
				String napat = ((Element) pattern.getElements().get(i))
						.getFieldname();
				if (napat.compareToIgnoreCase(nam) == 0) {
					isOkName = false;
					break;
				}
			}
			if (!isOkName) {
				contador++;
			}

		} while (!isOkName);

		// validate the new field name
		String vname = validateFieldName(nam);
		eleme.setFieldname(vname);
		eleme.setFieldseparator(fsep);
		eleme.setInfieldseparators(getDefaultInfieldseparators());
		eleme.setFieldtype(getDefaultNewFieldType());
		eleme.setFieldwidth(0);
		eleme.setImportfield(true);

		List<Element> elems = pattern.getElements();
		elems.add(tam, eleme);
	}

	/**
	 * This method erases a selected element to the list
	 * 
	 * @param pos
	 *            position
	 */
	protected void deleteField(int pos) {
		int conta = pattern.getElements().size();
		if (conta > 1) {
			pattern.getElements().remove(pos);
		}
	}

	/**
	 * This method saves a Normalization pattern to XML file *
	 */
	protected void savePatternXML() {

		JFileChooser jfc = new JFileChooser();
		jfc.setDialogTitle(PluginServices.getText(this, "save_norm_pattern"));
		String[] extensions = { "xml" };
		jfc.setCurrentDirectory(new File(getFolderPattern()));
		jfc.addChoosableFileFilter(new GenericFileFilter(extensions,
				PluginServices.getText(this, "pattern_norm_file")));
		int returnval = jfc.showSaveDialog((Component) PluginServices
				.getMainFrame());

		if (returnval == JFileChooser.APPROVE_OPTION) {
			File thefile = jfc.getSelectedFile();
			// Check if the file has extension .xml
			if (!(thefile.getPath().toLowerCase().endsWith(".xml"))) {
				thefile = new File(thefile.getPath() + ".xml");
			}
			try {
				// the file exists
				if (thefile.exists()) {

					int n = JOptionPane.showConfirmDialog(null, PluginServices
							.getText(null, "file_exists"), PluginServices
							.getText(null, "save_norm_pattern"),
							JOptionPane.YES_NO_OPTION);
					if (n == JOptionPane.YES_OPTION) {
						writeSaveFile(thefile);
					}
				}
				// the file not exists
				else {
					writeSaveFile(thefile);
				}
			} catch (Exception e) {
				log.error("Error saving the pattern", e);
			}
		}
	}

	/**
	 * This method loads a Normalization pattern from a XML file and return the
	 * pattern and the String info
	 * 
	 * @return pattern
	 */
	protected Patternnormalization loadPatternXML() {

		PersistenceManager manager = ToolsLocator.getPersistenceManager();

		Patternnormalization pat = new DefaultPatternnormalization();
		File thefile = null;

		// Show the FileChooser to select a pattern
		JFileChooser jfc = new JFileChooser();
		jfc.setCurrentDirectory(new File(getFolderPattern()));
		jfc.setDialogTitle(PluginServices.getText(this, "load_norm_pattern"));
		String[] extensions = { "xml" };
		jfc.addChoosableFileFilter(new GenericFileFilter(extensions,
				PluginServices.getText(this, "pattern_norm_file")));

		int returnval = jfc.showOpenDialog((Component) PluginServices
				.getMainFrame());

		if (returnval == JFileChooser.APPROVE_OPTION) {
			thefile = jfc.getSelectedFile();
		} else {
			return null;
		}

		FileReader reader = null;
		PersistentState state = null;
		try {
			reader = new FileReader(thefile);
			state = manager.loadState(reader);
			pat.loadFromState(state);
			reader.close();
		} catch (Exception e) {
			log.error("Error loading the pattern", e);
			return null;
		}

		return pat;
	}

	/**
	 * This method returns a Element of one position
	 * 
	 * @param index
	 * @return
	 */
	protected Element getElement(int index) {
		Element[] adrs = pattern.getArrayElements();
		return adrs[index];
	}

	/**
	 * This method generates and returns field separators
	 * 
	 * @return default separators between fields
	 */
	protected Fieldseparator getDefaultFieldseparators() {

		Fieldseparator filsep = new DefaultFieldseparator();
		filsep.setSemicolonsep(true);
		filsep.setJoinsep(false);
		filsep.setColonsep(false);
		filsep.setSpacesep(false);
		filsep.setTabsep(false);

		return filsep;
	}

	/**
	 * @return the pattern
	 */
	protected Patternnormalization getPattern() {
		return pattern;
	}

	/**
	 * This method sets the pattern
	 * 
	 * @pat pattern
	 */
	protected void setPattern(Patternnormalization pat) {
		pattern = pat;
	}

	/**
	 * 
	 * @return
	 */
	protected int getSelectedField() {
		return selectedField;
	}

	/**
	 * 
	 * @param selectedField
	 */
	protected void setSelectedField(int selectedField) {
		this.selectedField = selectedField;
	}

	/**
	 * 
	 * @return
	 */
	protected FeatureStore getStore() {
		return store;
	}

	/**
	 * 
	 * @param store
	 */
	protected void setStore(FeatureStore store) {
		this.store = store;
	}

	/**
	 * set the number of first rows that not normalize
	 * 
	 * @param first
	 *            select the first row
	 */
	protected void setFirstRows(int first) {
		pattern.setNofirstrows(first);

	}

	/**
	 * get Normalize the first row
	 * 
	 * @return normalize first row
	 */
	protected int getFirstRows() {
		return pattern.getNofirstrows();

	}

	/**
	 * This method creates the default Normalization pattern
	 */
	private Patternnormalization initializeDefaultPattern() {

		Patternnormalization pat = new DefaultPatternnormalization();

		pat.setPatternname("defaultPattern");
		pat.setNofirstrows(0);

		/* Create the first Address Element */
		Element elem = new DefaultElement();

		elem.setFieldname("NewField");
		elem.setFieldseparator(getDefaultFieldseparators());
		elem.setInfieldseparators(getDefaultInfieldseparators());
		elem.setFieldtype(getDefaultNewFieldType());
		elem.setFieldwidth(0);
		elem.setImportfield(true);

		List<Element> elems = new ArrayList<Element>();
		elems.add(elem);

		pat.setElements(elems);

		return pat;
	}

	/**
	 * This method validates the name of a new field
	 * 
	 * @param text
	 * @return field name formatted
	 */
	private String validateFieldName(String text) {

		String temp = Normalizer.normalize(text, Normalizer.DECOMP, 0);
		temp = temp.replaceAll("[^\\p{ASCII}]", "");
		temp = temp.replaceAll("[\\s]+", "_");
		temp = temp.toUpperCase();

		return temp;
	}

	/**
	 * This method generates and returns in field separators
	 * 
	 * @return special characters within one string
	 */

	private Infieldseparators getDefaultInfieldseparators() {
		/* create the default in-field separators */
		Locale loc = Locale.getDefault();
		DecimalFormatSymbols dfs = new DecimalFormatSymbols(loc);
		Infieldseparators infilsep = new DefaultInfieldseparators();
		infilsep.setThousandseparator(Character.toString(dfs
				.getGroupingSeparator()));
		infilsep.setDecimalseparator(Character.toString(dfs
				.getDecimalSeparator()));
		infilsep.setTextseparator("\"");
		return infilsep;
	}

	/**
	 * This method generates and returns a new field type of type Stringvalue
	 * 
	 * @return field type
	 */
	private Fieldtype getDefaultNewFieldType() {
		Fieldtype newtype = new DefaultFieldtype();
		Stringvalue strval = new DefaultStringvalue();
		strval.setStringvaluewidth(50);
		newtype.setStringvalue(strval);
		return newtype;
	}

	/**
	 * This method write the save xml file
	 * 
	 * @param file
	 * @throws IOException
	 * @throws PersistenceException
	 */
	private void writeSaveFile(File thefile) throws IOException,
			PersistenceException {

		PersistenceManager manager = ToolsLocator.getPersistenceManager();

		pattern.setPatternname(thefile.getName());

		Writer writer = new FileWriter(thefile);
		PersistentState state = manager.getState(pattern);
		state.save(writer);
		writer.close();
	}

	/**
	 * This method return the folder where gvSIG stores the patterns
	 * 
	 * @return
	 */
	private String getFolderPattern() {
		XMLEntity xml = PluginServices.getPluginServices(this)
				.getPersistentXML();
		String pathFolder = String.valueOf(xml
				.getStringProperty("Normalization_pattern_folder"));
		return pathFolder;
	}

	/**
	 * Gets the strings sample for the table of samples
	 * 
	 * @return table model
	 * @throws DataException 
	 */
	protected DefaultTableModel getSamplesDataStore(int noRows) throws DataException {

		FeatureSet features = (FeatureSet) store.getDataSet();
		Iterator<Feature> it = features.iterator(noRows);
		int contador = 0;
		while (it.hasNext()) {
			Feature feature = (Feature) it.next();
			if (contador < SAMPLES) {
				String sample = feature.get(selectedField).toString();
				samples[contador] = sample;
			}
		}
		// String samples
		Object[][] data = new Object[samples.length][1];
		for (int i = 0; i < samples.length; i++) {
			data[i][0] = samples[i];
		}
		// table field name
		String fieldname = store.getDefaultFeatureType().getAttributeDescriptor(selectedField).getName();
		String[] names = { fieldname };
		DefaultTableModel tablemodel = new DefaultTableModel(data, names) {

			private static final long serialVersionUID = -7429493540158414622L;

			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return false;
			}
		};

		return tablemodel;

	}

	

}
