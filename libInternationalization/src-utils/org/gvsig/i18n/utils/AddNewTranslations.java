/**
 * 
 */
package org.gvsig.i18n.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Properties;

/**
 * @author cesar
 *
 */
public class AddNewTranslations {
	// The filename which stores the configuration (may be overriden by the command line parameter)
	private String configFileName = "config.xml";
	
	// Object to load and store the config options
	private ConfigOptions config;
	
	private TranslationDatabase database;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		AddNewTranslations process = new AddNewTranslations();
		
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
		
		loadDataBase();
		
		readNewTranslations();
		
		database.save();
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
	
	private static void usage() {
		System.out.println("Uso: AddNewTranslations [OPCION]");
		System.out.println("\t-c\t--config=configFile");
	}
	
	private void loadDataBase() {
		database = new TranslationDatabase(config);
		database.load();
	}
	
	private void readNewTranslations() {
		String lang, key, value, oldValue;
		
		for (int i=0; i<config.languages.length; i++) {
			lang = config.languages[i];
			try {
				FileInputStream fisProp = new FileInputStream(config.inputDir+File.separator+config.defaultBaseName+"_"+lang+".properties");
				Properties prop = new Properties();
				try {
					prop.load(fisProp);
					Enumeration keysEnum  = prop.keys();
					while (keysEnum.hasMoreElements()){
						key = (String) keysEnum.nextElement();
						value = prop.getProperty(key);
						if (value!=null && !value.equals("")) {
							if (!database.containsKey(lang, key)) {
								System.out.println("["+lang+"] Traducción añadida -- "+key+"="+value);
								database.setTranslation(lang, key, value);
							}
							else {
								oldValue = database.setTranslation(lang, key, value);
								if (!oldValue.equals(value)) {
									System.out.println("["+lang+"] Traducción actualizada -- "+key+"="+value);
									System.out.println("Valor anterior: "+database.getTranslation(lang, key));
								}
							}
						}
					}
				} catch (IOException e) {
					System.err.println("Error leyendo traducciones para idioma ["+lang+"]. "+e.getLocalizedMessage());
				}
				
			} catch (FileNotFoundException e) {
				try {
					FileInputStream fis = new FileInputStream(config.inputDir+File.separator+config.defaultBaseName+"_"+lang+".properties-"+config.outputEncoding);
					
					BufferedReader currentFile=null;
					try {
						currentFile = new BufferedReader(new InputStreamReader(fis, config.outputEncoding));
					} catch (UnsupportedEncodingException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
				    String line = null;
				    try {
						while((line = currentFile.readLine()) != null) {
							String[] parts = line.split("=");
							if (parts.length == 2) {
								key = parts[0];
								value = parts[1];
								if (value!=null && !value.equals("")) {
									if (!database.containsKey(lang, key)) {
										System.out.println("["+lang+"] Traducción añadida -- "+key+"="+value);
										database.setTranslation(lang, key, value);
									}
									else {
										oldValue = database.setTranslation(lang, key, value);
										if (!oldValue.equals(value)) {
											System.out.println("["+lang+"] Traducción actualizada -- "+key+"="+value);
											System.out.println("Valor anterior: "+database.getTranslation(lang, key));
										}
									}
								}
							}
							else {
								System.err.println("Error leyendo traducciones para idioma ["+lang+"].");
								System.err.println("Línea: "+line);
							}
						}
					    currentFile.close();
					} catch (IOException ex) {
						System.err.println("Error leyendo traducciones para idioma ["+lang+"]. "+ex.getLocalizedMessage());
					}
					
				} catch (FileNotFoundException e1) {
					System.out.println("Aviso -- no se encontraron nuevas traducciones para el idioma ["+lang+"].");
				}
			}
			
		}
		
	}
	
}
