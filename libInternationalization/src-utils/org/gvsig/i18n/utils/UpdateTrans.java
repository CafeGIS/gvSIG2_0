/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
*
* Copyright (C) 2006 IVER T.I. and Generalitat Valenciana.
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
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,USA.
*
* For more information, contact:
*
*  Generalitat Valenciana
*   Conselleria d'Infraestructures i Transport
*   Av. Blasco Ibáñez, 50
*   46010 VALENCIA
*   SPAIN
*
*      +34 963862235
*   gvsig@gva.es
*      www.gvsig.gva.es
*
*    or
*
*   IVER T.I. S.A
*   Salamanca 50
*   46005 Valencia
*   Spain
*
*   +34 963163400
*   dac@iver.es
*/


/**
 * 
 */
package org.gvsig.i18n.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;

/**
 * @author cesar
 *
 */
public class UpdateTrans {
	// The filename which stores the configuration (may be overriden by the command line parameter)
	private String configFileName = "config.xml";
	
	// Object to load and store the config options
	private ConfigOptions config;
	
	private TranslationDatabase database;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		UpdateTrans process = new UpdateTrans();
		
		// load command line parameters
		if (!process.readParameters(args)) {
			usage();
			System.exit(-1);
		}
		
		// transfer control to the program's main loop
		process.start();
	}
	
	private void start() {
		// load config options from the config file
		if (!loadConfig()) {
			System.out.println("Error leyendo el fichero de configuración.");
			usage();
			System.exit(-1);
		}
		
		loadKeys();
		loadDataBase();
		
		updateDB();
	}
	
	private void updateDB(){
		String lang, auxLang;
		String key, value, dbValue;
		
		HashMap newKeys = detectNewKeys();
		TreeMap newKeysDict;
		HashMap removedKeys = new HashMap();
		
		ArrayList removedKeysDict;

		/**
		 * Process the new or changed keys
		 */
		for (int i=0; i<config.languages.length; i++) {
			lang = config.languages[i];
			File langDir = new File(config.outputDir+File.separator+lang);
			langDir.mkdirs();
			
			// process the keys
			newKeysDict = (TreeMap) newKeys.get(lang);
			removedKeysDict = new ArrayList();
			removedKeys.put(lang, removedKeysDict);
			Iterator newKeysIterator = newKeysDict.keySet().iterator();
			while (newKeysIterator.hasNext()) {
				int numValues=0;
				value = null;
				key = (String) newKeysIterator.next();
				
				dbValue = database.getTranslation(lang, key);
				ArrayList newKeyList = (ArrayList) newKeysDict.get(key);
				String[] newKey;
				boolean equal=true;
				for (int j=0; j<newKeyList.size(); j++) {
					newKey = (String[]) newKeyList.get(j);
					if (!newKey[0].equals("")) {
						if (dbValue!=null && !dbValue.equals(newKey[0]))
							equal = false;
						if (numValues==0) { //if there are several non-empty values, take the first one
							value = newKey[0];
						}
						else if (!value.equals(newKey[0])) {
							equal=false;
						}
						numValues++;	
					}
				}
				if (equal==false) {
					System.err.println("\nAtención -- La clave '"+key+"' tiene diferentes valores para el idioma "  + lang + ".");
					System.err.println("Valor en base de datos: "+key+"="+dbValue);
					for (int j=0; j<newKeyList.size(); j++) {
						newKey = (String[]) newKeyList.get(j);
						System.err.println(newKey[1] + " -- " + key + "=" + newKey[0]);
					}
				}
				if (value!=null && !value.equals("")) { // the translation has a value
					if (dbValue==null) {
						// new translation
						database.setTranslation(lang, key, value);
						// it has been added to database, it isn't new anymore
						// we add the key to the list of keys to remove. We don't remove it now because then there is troubles with the iterator
						removedKeysDict.add(key);
					}
					else if (!dbValue.equals("")) {
						// if dbValue contains a translation, it isn't a new translation
						removedKeysDict.add(key);
					}
					/*
					 * else { // if dbValue.equals(""), it means that the key has been changed, and it must be sent for translation
					 *       //It should not be added to the database with the value from the property file, as it is not valid anymore.
					 * 
					 * }
					 */
				}
			}
		}
		
		// remove 
		for (int i=0; i<config.languages.length; i++) {
			lang = config.languages[i];
			removedKeysDict = (ArrayList) removedKeys.get(lang);
			newKeysDict = (TreeMap) newKeys.get(lang);
			Iterator removeIterator = removedKeysDict.iterator();
			while (removeIterator.hasNext()) {
				key = (String) removeIterator.next();
				newKeysDict.remove(key);
			}
		}
		
		removedKeys = new HashMap();
		
		// we already added all the new keys with value to the database
		// now we try to find a translation for the keys without translation
		for (int i=0; i<config.languages.length; i++) {
			lang = config.languages[i];
			File langDir = new File(config.outputDir+File.separator+lang);
			langDir.mkdirs();
			
			// process the keys
			newKeysDict = (TreeMap) newKeys.get(lang);
			removedKeysDict = new ArrayList();
			removedKeys.put(lang, removedKeysDict);
			Iterator newKeysIterator = newKeysDict.keySet().iterator();
			while (newKeysIterator.hasNext()) {
				key = (String) newKeysIterator.next();
				value = "";
				String auxValue;
				for (int currentAuxLang=0; currentAuxLang<config.languages.length && (value==null || value.equals("")); currentAuxLang++) {
					auxLang = config.languages[currentAuxLang];
					auxValue = database.getTranslation(auxLang, key);
					if (auxValue!=null && !auxValue.equals("")) {
						ArrayList keyList = database.getAssociatedKeys(auxLang, value);
						if (keyList!=null) {
							for (int j=0; j<keyList.size() && (value==null || !value.equals("")); j++) {
								value = database.getTranslation(lang, (String)keyList.get(j));
							}
						}
					}
				}
				if (value!=null && !value.equals("")) { // the translation has a value
					dbValue = database.getTranslation(lang, key);
					if (dbValue==null || !dbValue.equals("")) {
						/* if dbValue == "" means that the key has been changed and should be sent for translation.
						 * It should not be added to the database with the value from the property file, as it is not valid anymore.
						 */
											
						database.setTranslation(lang, key, value);
						// it has been added to database, it isn't new anymore
						// we add the key to the list of keys to remove. We don't remove it now because then there is troubles with the iterator
						removedKeysDict.add(key);
					}
				}
			}
		}
		
		// remove 
		for (int i=0; i<config.languages.length; i++) {
			lang = config.languages[i];
			removedKeysDict = (ArrayList) removedKeys.get(lang);
			newKeysDict = (TreeMap) newKeys.get(lang);
			Iterator removeIterator = removedKeysDict.iterator();
			while (removeIterator.hasNext()) {
				key = (String) removeIterator.next();
				newKeysDict.remove(key);
			}
		}
		
		// output the keys to be translated
		outputNewKeys(newKeys);
		
		// update the values of each project's property files and store to disk
		saveKeys();
		
		// store datase to disk
		database.save();
	}
	
	private void outputNewKeys(HashMap newKeys) {
		String lang, auxLang;
		/**
		 * Process the new or changed keys
		 */
		for (int i=0; i<config.languages.length; i++) {
			lang = config.languages[i];
			File langDir = new File(config.outputDir+File.separator+lang);
			langDir.mkdirs();
			HashMap outputFiles = new HashMap();
			HashMap outputPropertiesStream = new HashMap();
			HashMap outputProperties = new HashMap();
			
			// open the output files, one for each defined language
			for (int j=0; j<config.outputLanguages.length; j++) {
				auxLang = config.outputLanguages[j];
				FileOutputStream fos, fosProp;
				OrderedProperties prop;
				try {
					fos = new FileOutputStream(langDir.getPath()+File.separator+config.defaultBaseName+"_"+auxLang+".properties-"+config.outputEncoding);
					fosProp = new FileOutputStream(langDir.getPath()+File.separator+config.defaultBaseName+"_"+auxLang+".properties");
					prop = new OrderedProperties();
					outputPropertiesStream.put(auxLang, fosProp);
					outputProperties.put(auxLang, prop);
					try {
						outputFiles.put(auxLang, new BufferedWriter(new OutputStreamWriter(fos, config.outputEncoding)));
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						System.err.println(e.getLocalizedMessage());
						System.exit(-1);
					}
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					System.err.println(e.getLocalizedMessage());
					System.exit(-1);
				}
			}
			
			// also open the file for language we're processing currently
			if (!outputFiles.containsKey(lang)) {
				FileOutputStream fos, fosProp;
				OrderedProperties prop;
				try {
					fos = new FileOutputStream(langDir.getPath()+File.separator+config.defaultBaseName+"_"+lang+".properties-"+config.outputEncoding);
					fosProp = new FileOutputStream(langDir.getPath()+File.separator+config.defaultBaseName+"_"+lang+".properties");
					prop = new OrderedProperties();
					outputPropertiesStream.put(lang, fosProp);
					outputProperties.put(lang, prop);
					try {
						outputFiles.put(lang, new BufferedWriter(new OutputStreamWriter(fos, config.outputEncoding)));
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						System.err.println(e.getLocalizedMessage());
						System.exit(-1);
					}
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					System.err.println(e.getLocalizedMessage());
					System.exit(-1);
				}
			}
			
			TreeMap dict = (TreeMap) newKeys.get(lang);
			Iterator keyIterator = dict.keySet().iterator();
			String key, value;
			while (keyIterator.hasNext()) {
				key = (String) keyIterator.next();

				Iterator files = outputFiles.keySet().iterator();
				BufferedWriter writer;
				while (files.hasNext()) {
					// we output the pair key-value for the defined output languages (they're used for reference by translators)							
					auxLang = (String) files.next();
					writer = (BufferedWriter) outputFiles.get(auxLang);
					value = database.getTranslation(auxLang, key);
					try {
						if (value!=null)
							writer.write(key+"="+value+"\n");
						else
							writer.write(key+"=\n");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				Iterator props = outputProperties.keySet().iterator();
				OrderedProperties prop;
				while (props.hasNext()) {
					// we output the pair key-value for the defined output languages (they're used for reference by translators)							
					auxLang = (String) props.next();
					prop = (OrderedProperties) outputProperties.get(auxLang);
					value = database.getTranslation(auxLang, key);
					if (value!=null)
						prop.put(key, value);
					else
						prop.put(key, "");
				}
			}
			
			Iterator props = outputProperties.keySet().iterator();
			OrderedProperties prop;
			FileOutputStream fos;
			while (props.hasNext()) {
				auxLang = (String) props.next();
				fos = (FileOutputStream) outputPropertiesStream.get(auxLang);
				prop = (OrderedProperties) outputProperties.get(auxLang);
				try {
					prop.store(fos, "Translations for language ["+auxLang+"]");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			// close the files now
			Iterator files = outputFiles.keySet().iterator();
			while (files.hasNext()) {							
				auxLang = (String) files.next();
				BufferedWriter writer = (BufferedWriter) outputFiles.get(auxLang);
				try {
					writer.close();
				} catch (IOException e) {
					// do nothing here
				}
			}
		}		
	}
		
	private HashMap detectNewKeys() {
		Project currentProject;
		String lang;
		OrderedProperties dict;
		TreeMap auxDict;
		Iterator keys;
		String key, value, dbValue;
		HashMap newKeys = new HashMap();
		for (int i=0; i<config.languages.length; i++) {
			lang = config.languages[i];
			newKeys.put(lang, new TreeMap());
		}
	
		/**
		 * Detect the new or changed keys
		 * We just make a list, we will check the list later (to detect conflicting changes)
		 */
		for (int i=0; i<config.projects.size(); i++) {
			currentProject = (Project) config.projects.get(i);
			for (int j=0; j<config.languages.length; j++) {
				lang = config.languages[j];
				dict = (OrderedProperties) currentProject.dictionaries.get(lang);
				keys = dict.keySet().iterator();
				while (keys.hasNext()) {
					key = (String) keys.next();
					value = dict.getProperty(key);
					dbValue = database.getTranslation(lang, key);
					if (dbValue==null || dbValue.equals("") || (!value.equals("") && !dbValue.equals(value))) {
						String []newKey = new String[2];
						newKey[0]=value;
						newKey[1]= currentProject.dir;
						auxDict = (TreeMap) newKeys.get(lang);
						ArrayList keyList = (ArrayList) auxDict.get(key);
						if (keyList==null) {
							keyList = new ArrayList();
						}
						keyList.add(newKey);
						auxDict.put(key, keyList);
					}
				}
			}
		}
		return newKeys;
	}
	
	private static void usage() {
		System.out.println("Uso: UpdateTrans [OPCION]");
		System.out.println("\t-c\t--config=configFile");
	}
	
	/**
	 *  Reads the command line parameters */
	private boolean readParameters(String[] args) {
		String configPair[];

		for (int i=0; i<args.length; i++) {
			configPair = args[i].split("=",2);
			if ( (configPair[0].equals("-c") || configPair[0].equals("--config"))
					&& configPair.length==2) {
				configFileName = configPair[1];
			}
			else {
				return false;
			}
		}
		return true;
	}
	
	private boolean loadConfig() {
		config = new ConfigOptions(configFileName);
		config.load();
		return true;
	}
	
	/**
	 * Reads the translation keys from the projects speficied in
	 * the config file. The keys are stored for each project in
	 * 'config.projects'. 
	 * 
	 * @return
	 */
	private void loadKeys() {
		Keys keys = new Keys(config);
		keys.load();
	}
	
	private void saveKeys() {
		Project project;
		OrderedProperties dict;
		String lang;
		
		for (int currentProject=0; currentProject<config.projects.size(); currentProject++) {
			project = ((Project)config.projects.get(currentProject));
			
			for (int currentLang=0; currentLang<config.languages.length; currentLang++) {
				lang = (String) config.languages[currentLang];
				dict = (OrderedProperties) project.dictionaries.get(lang);
				String dbValue, key;

				Iterator keysIterator = dict.keySet().iterator();
				while (keysIterator.hasNext()) {
					key = (String) keysIterator.next();
					dbValue = database.getTranslation(lang, key);
					if (dbValue!=null) {
						dict.setProperty(key, dbValue);
					}
				}
			}
		}
		
		Keys keys = new Keys(config);
		keys.save();
	}
	
	private void loadDataBase() {
		database = new TranslationDatabase(config);
		database.load();
	}

}
