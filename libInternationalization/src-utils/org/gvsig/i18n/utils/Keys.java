/**
 * 
 */
package org.gvsig.i18n.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParserException;

/**
 * @author cesar
 *
 */
public class Keys {
	private ConfigOptions config;
	
	public Keys(ConfigOptions config) {
		this.config = config;
	}
	
	public void load() {
		Project project;
		for (int currentProject=0; currentProject<config.projects.size(); currentProject++) {
			project = ((Project)config.projects.get(currentProject));
			/**
			 * There is two options, "properties" and "sources". "sources" is the default, so
			 * if there was something different to "properties", we assume "sources".
			 */
			if (!project.sourceKeys.equals("properties")) {
				project.dictionaries = loadProjectFromSources(project);
			}
			else {
				// load the keys for each language from the property file
			 	project.dictionaries = loadProjectFromProperties(project);
			 	// add missing keys tp each language
			 	completeKeys(project);
			 }
		}
	}
	
	public void save() {
		Project project;
		OrderedProperties dict;
		FileOutputStream stream=null;
		String lang;
		
		for (int currentProject=0; currentProject<config.projects.size(); currentProject++) {
			project = ((Project)config.projects.get(currentProject));
			
			for (int currentLang=0; currentLang<config.languages.length; currentLang++) {
				lang = (String) config.languages[currentLang];
				dict = (OrderedProperties) project.dictionaries.get(lang);
				
				if (dict.size()>0) {
					// ensure the directory exists
					File propertyDir = new File(project.propertyDir);
					if (propertyDir.mkdirs())
						System.out.println("Aviso -- directorio creado: "+project.propertyDir);
					
					try {
						// different for spanish...
						if (lang.equals("es")) {
							stream = new FileOutputStream(project.propertyDir+File.separator+project.basename+".properties");
						}
						else {
							stream = new FileOutputStream(project.propertyDir+File.separator+project.basename+"_"+lang+".properties");
						}
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	
					try {
						dict.store(stream, "Translations for language ["+lang+"]");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}	
	}
	
	private HashMap loadProjectFromSources(Project project) {
		// always start with an empty HashMap when loading
		HashMap dictionaries = new HashMap();		
		String lang;
		
		/**
		 * The keys obtained from the sources and the config.xml files of the
		 * plugins.
		 */
		HashSet keys = new HashSet();
		/**
		 * The translations loaded from the property files of the project.
		 */
		
		dictionaries =  loadProjectFromProperties(project);
		
		for (int i=0; i<project.srcDirs.length; i++) {
			try {
				keys = loadKeysFromSources(ConfigOptions.getAbsolutePath(File.separator, project.dir+File.separator+project.srcDirs[i]), keys);
			}
			catch (IOException ex) {
				// It there was an error reading the directory, just warn and skip the dir
				System.err.println(project.dir +" -- Aviso: no se pudo leer el directorio "+project.dir+File.separator+project.srcDirs[i]);
			}
		}
		
		for (int currentLang=0; currentLang<config.languages.length; currentLang++) {
			lang = config.languages[currentLang];

			OrderedProperties currentDict = (OrderedProperties) dictionaries.get(lang);
			Iterator keysIterator = keys.iterator();
			String key;
			// add missing keys
			while (keysIterator.hasNext()) {
				key = (String) keysIterator.next();
				if (!currentDict.containsKey(key)) {
					currentDict.put(key, "");
					System.out.println(project.dir+" -- Aviso -- clave añadida: "+key);
				}
			}
			
			// remove extra keys
			// first make a list of keys to remove, because it's not possible to access the iterator and the TreeMap at the same time
		/*	HashSet removedKeys = new HashSet();
			Iterator dictKey = currentDict.keySet().iterator();
			while (dictKey.hasNext()) {
				key = (String) dictKey.next();
				if (!keys.contains(key)) {
					removedKeys.add(key);
					System.out.println(project.dir+" -- Aviso -- clave eliminada: "+key);
				}
			}
			// now we really remove the keys
			Iterator removedKeysIt = removedKeys.iterator();
			while (removedKeysIt.hasNext()) {
				key = (String) removedKeysIt.next();
				currentDict.remove(key);
			}*/	
		}

		return dictionaries;
	}
	
	/**
	 * Reads the keys from all the languages, to make a unique list containing
	 * all the keys. 
	 */
	public void completeKeys(Project project) {
		/* The list of all the keys */
		HashSet keys = new HashSet();
		// always start with an empty HashMap when loading
		//HashMap dictionaries = new HashMap();		
		String lang;
		
		// calculate all the keys
		for (int currentLang=0; currentLang<config.languages.length; currentLang++) {
			lang = config.languages[currentLang];
			
			OrderedProperties currentDict = (OrderedProperties) project.dictionaries.get(lang);
			if (currentDict==null) {
				currentDict = new OrderedProperties();
				project.dictionaries.put(lang, currentDict);
			}
			else {
				Iterator keysIterator = currentDict.keySet().iterator();
				String key;
				// add missing keys
				while (keysIterator.hasNext()) {
					key = (String) keysIterator.next();
					keys.add(key);
				}	
			}
		}
		
		// add missing keys to each language
		for (int currentLang=0; currentLang<config.languages.length; currentLang++) {
			lang = config.languages[currentLang];
			
			OrderedProperties currentDict = (OrderedProperties) project.dictionaries.get(lang);
			Iterator keysIterator = keys.iterator();
			String key;
			// add missing keys
			while (keysIterator.hasNext()) {
				key = (String) keysIterator.next();
				if (!currentDict.containsKey(key)) {
					currentDict.put(key, "");
					System.out.println(project.dir+" -- Aviso -- clave añadida: "+key);
				}
			}			
		}
	}
	
	
	private HashSet loadKeysFromSources(String directory, HashSet keys) {
		String key;
		File dir = new File(directory);
		File files[] = dir.listFiles();
		final int BLOCKSIZE = 8192;
		char[] partialBuffer = new char[BLOCKSIZE+1];
		String text;
		StringBuffer buffer;
		
		// stores the position of the newlines
		//ArrayList newLines = new ArrayList();
		//	int lineNumber;
		
		if (files!=null) {
			//Pattern keyPattern = Pattern.compile("(PluginServices|Messages)\\.(getText|getString|get)\\([^\"\\)]*\"([^\"]*)\"[^\\)]*\\)");
			Pattern keyPattern = Pattern.compile(
					"(PluginServices|Messages)\\p{Space}*\\.\\p{Space}*(getText|getString|get)\\p{Space}*\\([^\"\\)]*\"([^\"]*)\"[^\\+\\)]*(\\+[^\\)]*)*\\)");
			Matcher keyMatcher = keyPattern.matcher("");
					//"(PluginServices|Messages)\\.(getText|getString|get)\\p{Space}*\\([^\"\\)]*\"([^\"]*)\"[^\\+\\)]*(\\+[^\"]*\"([^\"]*)\"[^\\+\\)]*)*\\)");
			//Pattern newLinePattern = Pattern.compile("\n");
			//Matcher newLineMatcher = newLinePattern.matcher("");
			
			for (int i=0; i<files.length; i++) {
				if (files[i].isDirectory()) {
					keys = loadKeysFromSources(files[i].toString(), keys);
					continue;
				}
				else if (files[i].getName().toLowerCase().equals("PluginServices.java")) {
					
					//[Messages.]getText(...)
					Pattern PsPattern = Pattern.compile("(Messages\\.)*getText\\([^\"\\)]*\"([^\"]*)\"[^\\)]*\\)");
					Matcher PsMatcher = PsPattern.matcher("");
					
					FileInputStream fis=null;
					try {
						fis = new FileInputStream(files[i]);
						
						buffer = new StringBuffer();
						BufferedReader currentFile=null;
						
						currentFile = new BufferedReader(new InputStreamReader(fis, config.sourcesEncoding));
						while (currentFile.read(partialBuffer, 0, BLOCKSIZE)!=-1) {
							buffer.append(partialBuffer);
						}
						text = buffer.toString();
						
						
						PsMatcher.reset(text);
						
						while (PsMatcher.find()) {
							key = PsMatcher.group(2);
							if (!key.equals(""))
								keys.add(key);
						}
						currentFile.close();
					} catch (UnsupportedEncodingException e1) {
						System.err.println(e1.getLocalizedMessage());
						continue;
					}
					catch (IOException e1) {
						System.err.println(e1.getLocalizedMessage());
						continue;
					}
				}
				else if (files[i].getName().toLowerCase().endsWith(".java")) {
					FileInputStream fis=null;
					try {
						fis = new FileInputStream(files[i]);
						BufferedReader currentFile=null;
						
						currentFile = new BufferedReader(new InputStreamReader(fis, config.sourcesEncoding));
						buffer = new StringBuffer();
						
						int readChars; // number of characters which were read
						while ( (readChars = currentFile.read(partialBuffer, 0, BLOCKSIZE)) != -1) {
							buffer.append(partialBuffer, 0, readChars);
						}
						text = buffer.toString();
						
						/*newLineMatcher.reset(text);
						 while (newLineMatcher.find()) {
						 newLines.add(new Integer(newLineMatcher.end()-1));
						 }*/
						// lineNumber=1;
						
						keyMatcher.reset(text);
						
						while (keyMatcher.find()) {
							
							// find out in which line number we are
							/*while (keyMatcher.start() > ((Integer)newLines.get(lineNumber)).intValue() ) lineNumber++;
							 System.out.println("FileName: "+files[i].getCanonicalPath()+"; lineNumber: "+lineNumber);*/
							
//							StringBuffer keyBuffer = new StringBuffer();
							
//							for (int ii=0; ii<=keyMatcher.groupCount(); ii++) {
//							System.out.println("group: "+ ii+ "; " + keyMatcher.group(ii));
//							}
//							for (int groupNumb=3; groupNumb<=keyMatcher.groupCount(); groupNumb+=2) {
//							if (keyMatcher.group(groupNumb)!=null)
//							keyBuffer.append(keyMatcher.group(groupNumb));
//							}
							
							if (keyMatcher.group(4)!=null) {
								System.err.println("\nError: clave repartida en varias líneas");
								System.err.println("Fichero: "+files[i].getCanonicalPath());
								System.err.println("Código: ");
								System.err.println(keyMatcher.group());
							}
							else {
								key = keyMatcher.group(3);
								if (!key.equals(""))
									keys.add(key);
							}
						}
						currentFile.close();
					} catch (UnsupportedEncodingException e1) {
						System.err.println(e1.getLocalizedMessage());
						continue;
					}
					catch (IOException e1) {
						System.err.println(e1.getLocalizedMessage());
						continue;
					}
				}
				else if (files[i].getName().equalsIgnoreCase("config.xml")) {
					keys = loadKeysFromXml(files[i], keys);
				}
			}
		}
		
		return keys;
	}
	
	
	private HashSet loadKeysFromXml(File fileName, HashSet keys) {
		KXmlParser parser = new KXmlParser();
		String tagname, attribute;
		
		// we use null encoding, in this way kxml2 tries to detect the encoding
		try {
			parser.setInput(new FileInputStream(fileName), null);
		} catch (FileNotFoundException e1) {
			System.err.println(e1.getLocalizedMessage());
			return keys;
		} catch (XmlPullParserException e1) {
			// No podemos leer el fichero de configuración. Usamos valores por defecto
			System.err.println("Aviso: error al cargar el fichero "+fileName);
			return keys;
		}
		
		try {
			for (parser.next(); parser.getEventType()!=KXmlParser.END_DOCUMENT; parser.next()) {
				// este bucle externo recorre las etiquetas de primer y segundo nivel
				if (parser.getEventType()==KXmlParser.START_TAG) {
					tagname = parser.getName();
					if (tagname.equals("menu") || tagname.equals("action-tool") || tagname.equals("selectable-tool") || tagname.equals("entry")) {
						attribute = parser.getAttributeValue(null, "text");
						if (attribute!=null) {
							String menuParts[] = attribute.split("/");
							for (int i=0; i<menuParts.length; i++) {
								if (!menuParts[i].equals(""))
									keys.add(menuParts[i]);
							}
						}
						
						attribute = parser.getAttributeValue(null, "tooltip");
						if (attribute!=null && !attribute.equals("")) {
							keys.add(attribute);
						}
						
						
					}
					else if (tagname.equals("combo-scale")) {
						attribute = parser.getAttributeValue(null, "label");
						if (attribute!=null && !attribute.equals("")) {
							keys.add(attribute);
						}
					}
					else if (tagname.equals("tool-bar")) {
						attribute = parser.getAttributeValue(null, "name");
						if (attribute!=null && !attribute.equals("")) {
							keys.add(attribute);
						}
					}

				}
			}	
		} catch (XmlPullParserException e1) {
			e1.getLocalizedMessage();
		} catch (IOException e1) {
			e1.getLocalizedMessage();
		}
		return keys;
	}
	
	private HashMap loadProjectFromProperties(Project project) {
		// always start with an empty HashMap when loading
		HashMap dictionaries = new HashMap();
		String lang;
		OrderedProperties dictionary;
		
		FileInputStream stream=null;
		
		for (int currentLang=0; currentLang<config.languages.length; currentLang++) {
			lang = config.languages[currentLang];
			dictionary = new OrderedProperties();
			try {
				// different for spanish...
				if (lang.equals("es")) {
					stream = new FileInputStream(project.propertyDir+File.separator+project.basename+".properties");
				}
				else {
					stream = new FileInputStream(project.propertyDir+File.separator+project.basename+"_"+lang+".properties");
				}
				try {
					dictionary.load(stream);
				} catch (IOException e) {
					System.err.println("Error cargando la base de datos para el idioma: ["+lang+"]. "+e.getLocalizedMessage());
				}
			} catch (FileNotFoundException e) {
				System.err.println(project.dir + " -- Error cargando la base de datos para el idioma: ["+lang+"]. "+e.getLocalizedMessage());
			}
			dictionaries.put(lang, dictionary);
		}
		return dictionaries;
	}
}
