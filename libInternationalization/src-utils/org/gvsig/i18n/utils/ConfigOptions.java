package org.gvsig.i18n.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParserException;

public class ConfigOptions {
	// Default values
	public String defaultBaseName = "text";
	public String defaultBaseDir = ".";
	public String databaseDir = "database";
	private String configFileName = "config.xml";
	public String[] languages;
	public ArrayList projects = new ArrayList();
	private String defaultLangList="ca;cs;de;en;es;eu;fr;gl;it;pt";
	public String sourceKeys = "sources";
	private String defaultPropertyDir = "config";
	public String[] outputLanguages={"en", "es"};
	public String outputDir="output";
	public String inputDir="input";
	public String[] defaultSrcDirs={"src"};
	
	
	/**
	 * The character encoding of the Java source files, used to search keys in the sources.
	 */
	public String sourcesEncoding = "ISO8859_1";
	/**
	 * The character encoding of the generated output files for missing keys.
	 */
	public String outputEncoding = "UTF8";
	
	/**
	 * Creates a new ConfigOptions object.
	 */
	public ConfigOptions() {
		// Now we transform all directories to absolute canonical paths, so
		// that the are easier to manage afterwards.
		// It's also done at the end of parseVars method, but we must also do
		// it here, because parseVars() might not get executed.
		try {
			this.defaultBaseDir = getAbsolutePath(".", this.defaultBaseDir);
			this.databaseDir = getAbsolutePath(this.defaultBaseDir, this.databaseDir);
			this.outputDir = getAbsolutePath(this.defaultBaseDir, this.outputDir);
			this.inputDir = getAbsolutePath(this.defaultBaseDir, this.inputDir);
			
			/*
			 * 
			 File baseDirFile = new File(this.defaultBaseDir);
			this.defaultBaseDir = baseDirFile.getCanonicalPath();
			File databaseDirFile = new File(this.databaseDir);
			if (databaseDirFile.isAbsolute()) {
				this.databaseDir = databaseDirFile.getCanonicalPath();
			}
			else {
				this.databaseDir = (new File(this.defaultBaseDir+File.separator+this.databaseDir)).getCanonicalPath();
			}
			File outputDirFile = new File(this.outputDir);
			if (outputDirFile.isAbsolute()) {
				this.outputDir = outputDirFile.getCanonicalPath();
			}
			else {
				this.outputDir = (new File(this.defaultBaseDir+File.separator+this.outputDir)).getCanonicalPath();
			}
			File inputDirFile = new File(this.inputDir);
			if (inputDirFile.isAbsolute()) {
				this.inputDir = inputDirFile.getCanonicalPath();
			}
			else {
				this.inputDir = (new File(this.defaultBaseDir+File.separator+this.inputDir)).getCanonicalPath();
			}
			*/
		} catch (IOException e) {
			System.err.println("Error accediendo a los directorios de las traducciones: "+e.getLocalizedMessage());
		}
	}
	
	/**
	 *  Creates a new ConfigOptions object, defining the config file to use.
	 *  
	 * @param configFileName The file name of the config file to use.
	 */
	public ConfigOptions(String configFileName) {
		this.configFileName = configFileName;
		
		// Now we transform all directories to absolute canonical paths, so
		// that the are easier to manage afterwards.
		// It's also done at the end of parseVars method, but we must also do
		// it here, because parseVars() might not get executed.
		try {
			this.defaultBaseDir = getAbsolutePath(".", this.defaultBaseDir);
			this.databaseDir = getAbsolutePath(this.defaultBaseDir, this.databaseDir);
			this.outputDir = getAbsolutePath(this.defaultBaseDir, this.outputDir);
			this.inputDir = getAbsolutePath(this.defaultBaseDir, this.inputDir);
			
			/*File baseDirFile = new File(this.defaultBaseDir);
			this.defaultBaseDir = baseDirFile.getCanonicalPath();
			File databaseDirFile = new File(this.databaseDir);
			if (databaseDirFile.isAbsolute()) {
				this.databaseDir = databaseDirFile.getCanonicalPath();
			}
			else {
				this.databaseDir = (new File(this.defaultBaseDir+File.separator+this.databaseDir)).getCanonicalPath();
			}
			File outputDirFile = new File(this.outputDir);
			if (outputDirFile.isAbsolute()) {
				this.outputDir = outputDirFile.getCanonicalPath();
			}
			else {
				this.outputDir = (new File(this.defaultBaseDir+File.separator+this.outputDir)).getCanonicalPath();
			}
			File inputDirFile = new File(this.inputDir);
			if (inputDirFile.isAbsolute()) {
				this.inputDir = inputDirFile.getCanonicalPath();
			}
			else {
				this.inputDir = (new File(this.defaultBaseDir+File.separator+this.inputDir)).getCanonicalPath();
			}*/
		} catch (IOException e) {
			System.err.println("Error accediendo a los directorios de las traducciones: "+e.getLocalizedMessage());
		}
	}
	
	/**
	 * Sets the name of the config file to use.
	 * 
	 * @param configFileName
	 */
	public void setConfigFile(String configFileName) {
		this.configFileName = configFileName;
	}
	
	/**
	 * Gets the name of the config file in use.
	 * 
	 * @return The name of the config file in use.
	 */
	public String getConfigFile() {
		return configFileName;
	}
	
	/**
	 *  Loads the config parameters and the projects to consider from the XML
	 * config file */
	public boolean load() {
		KXmlParser parser = new KXmlParser();
		
		// we use null encoding, in this way kxml2 tries to detect the encoding
		try {
			parser.setInput(new FileInputStream(configFileName), null);
		} catch (FileNotFoundException e1) {
			System.err.println(e1.getLocalizedMessage());
			return false;
		} catch (XmlPullParserException e1) {
			// No podemos leer el fichero de configuración. Usamos valores por defecto
			System.err.println("Aviso: no se pudo leer correctamente el fichero de configuración. Se usarán los valores por defecto.");
			return false;
		}
		
		try {
			for (parser.next(); parser.getEventType()!=KXmlParser.END_DOCUMENT; parser.next()) {
				// este bucle externo recorre las etiquetas de primer y segundo nivel
				if (parser.getEventType()==KXmlParser.START_TAG) {
					if (parser.getName().equals("config")) {
						parseVars(parser);
					}
					else if (parser.getName().equals("projects")) {
						parseProjects(parser);
					}
				}
			}	
		} catch (XmlPullParserException e1) {
			e1.getLocalizedMessage();
		} catch (IOException e1) {
			e1.getLocalizedMessage();
		}
		
		File outputDirFile = new File(outputDir);
		outputDirFile.mkdirs();
		File databaseDirFile = new File(databaseDir);
		databaseDirFile.mkdirs();
		return true;
	}

	private void parseVars(KXmlParser parser) throws XmlPullParserException, IOException {
		// recorremos todas las etiquetas 'variable' dentro de config
		int state;
		String name, value;
		
		for (state = parser.next(); state!=KXmlParser.END_TAG || !parser.getName().equals("config") ; state=parser.next()) {
			if (state==KXmlParser.START_TAG) {
				if (parser.getName().equals("variable")) {
					name = parser.getAttributeValue(null, "name");
					value = parser.getAttributeValue(null, "value");
					if (name!=null && value!=null) {
						value = parser.getAttributeValue(null, "value");
						if (parser.getAttributeValue(null, "name").equals("basename")) {
							defaultBaseName = parser.getAttributeValue(null, "value");
						}
						else if (parser.getAttributeValue(null, "name").equals("basedir")) {
							defaultBaseDir = parser.getAttributeValue(null, "value");
						}
						else if (parser.getAttributeValue(null, "name").equals("databaseDir")) {
							databaseDir = parser.getAttributeValue(null, "value");
						}
						else if (parser.getAttributeValue(null, "name").equals("defaultPropertyDir")) {
							defaultPropertyDir = parser.getAttributeValue(null, "value");
						}
						else if (parser.getAttributeValue(null, "name").equals("outputDir")) {
							outputDir = parser.getAttributeValue(null, "value");
						}
						else if (parser.getAttributeValue(null, "name").equals("inputDir")) {
							inputDir = parser.getAttributeValue(null, "value");
						}
						else if (parser.getAttributeValue(null, "name").equals("sourceKeys")) {
							sourceKeys = parser.getAttributeValue(null, "value");
						}
						else if (parser.getAttributeValue(null, "name").equals("srcDirs")) {
							String srcDirs = parser.getAttributeValue(null, "value");
							this.defaultSrcDirs = srcDirs.split(";");
						}
						else if (parser.getAttributeValue(null, "name").equals("languages")) {
							languages = parser.getAttributeValue(null, "value").split(";");
							if (languages.length==0) {
								System.err.println("Aviso: No se definieron idiomas a considerar. Se usará la lista de idiomas\n por defecto: "+defaultLangList);
								languages = defaultLangList.split(";");
							}
						}
					}
					else {
						if (name==null)
							System.err.println("Error leyendo el fichero de configuración. No se encontró el atributo 'name'\nrequerido en la etiqueta <variable>. La etiqueta será ignorada.");
						if (value==null)
							System.err.println("Error leyendo el fichero de configuración. No se encontró el atributo 'value'\nrequerido en la etiqueta <variable>. La etiqueta será ignorada.");
					}
				}
				else {
					System.err.println("Aviso: se ignoró una etiqueta desconocida o inesperada: " + parser.getName());
				}
			}
		}
		
		// Now we transform all directories to absolute canonical paths, so
		// that they are easier to manage afterwards.
		try {
			this.defaultBaseDir = getAbsolutePath(".", this.defaultBaseDir);
			this.databaseDir = getAbsolutePath(this.defaultBaseDir, this.databaseDir);
			this.outputDir = getAbsolutePath(this.defaultBaseDir, this.outputDir);
			this.inputDir = getAbsolutePath(this.defaultBaseDir, this.inputDir);
			/**
			File baseDirFile = new File(this.defaultBaseDir);
			this.defaultBaseDir = baseDirFile.getCanonicalPath();
			System.out.println(this.defaultBaseDir);
			File databaseDirFile = new File(this.databaseDir);
			if (databaseDirFile.isAbsolute()) {
				this.databaseDir = databaseDirFile.getCanonicalPath();
			}
			else {
				this.databaseDir = (new File(this.defaultBaseDir+File.separator+this.databaseDir)).getCanonicalPath();
			}
			File outputDirFile = new File(this.outputDir);
			if (outputDirFile.isAbsolute()) {
				this.outputDir = outputDirFile.getCanonicalPath();
			}
			else {
				this.outputDir = (new File(this.defaultBaseDir+File.separator+this.outputDir)).getCanonicalPath();
			}
			File inputDirFile = new File(this.inputDir);
			if (inputDirFile.isAbsolute()) {
				this.inputDir = inputDirFile.getCanonicalPath();
			}
			else {
				this.inputDir = (new File(this.defaultBaseDir+File.separator+this.inputDir)).getCanonicalPath();
			}
			*/
		} catch (IOException e) {
			System.err.println("Error accediendo a los directorios de las traducciones: "+e.getLocalizedMessage());
		}
	}
	
	/**
	 * Parse the lines containing <project /> tags (between <projects> and </projects>).
	 * 
	 * @param parser The KXmlParser, pointing to the next <project /> tag (if any)
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	private void parseProjects(KXmlParser parser) throws XmlPullParserException, IOException {
		// recorremos todos los proyectos dentro de 'projects'
		int state;
		String dir;
		File dirFile;
		Project project;
		
		for (state = parser.next(); state!=KXmlParser.END_TAG || !parser.getName().equals("projects") ; state=parser.next()) {
			if (state==KXmlParser.START_TAG) {
				if (parser.getName().equals("project")) {
					if (parser.getAttributeValue(null, "dir")!=null) {
						dir = parser.getAttributeValue(null,  "dir");
						if (dir!=null) {
							// we transform it to absolute canonical paths, so
							// that it is easier to manage afterwards.
							dirFile = new File(dir);
							try {
								if (dirFile.isAbsolute()) {
									dir = dirFile.getCanonicalPath();
								}
								else {
									dir = new File(this.defaultBaseDir+File.separator+dir).getCanonicalPath();
								}
							} catch (IOException e) {
								System.err.println("Error accediendo a los directorios de las traducciones: "+e.getLocalizedMessage());
							}
							project = new Project();
							project.dir = dir;
							project.basename = parser.getAttributeValue(null, "basename");
							if (project.basename==null)
								project.basename = this.defaultBaseName;
							
							project.propertyDir = parser.getAttributeValue(null, "propertyDir");
							if (project.propertyDir==null) {
								project.propertyDir = this.defaultPropertyDir;
							}
							// we transform it to absolute canonical paths, so
							// that it is easier to manage afterwards.
							File propDirFile = new File(project.propertyDir);
							try {
								if (propDirFile.isAbsolute()) {
									project.propertyDir = propDirFile.getCanonicalPath();
								}
								else {
									project.propertyDir = new File(dir+File.separator+project.propertyDir).getCanonicalPath();
								}
							} catch (IOException e) {
								System.err.println("Error accediendo a los directorios de las traducciones: "+e.getLocalizedMessage());
							}

							String srcDirs = parser.getAttributeValue(null, "srcDirs");
							if (srcDirs!=null) {
								project.srcDirs = srcDirs.split(";");
							}
							else {
								project.srcDirs = this.defaultSrcDirs;
							}

							project.sourceKeys = parser.getAttributeValue(null, "sourceKeys");
							if (project.sourceKeys==null)
								project.sourceKeys = this.sourceKeys;
							projects.add(project);
						}
						else
							System.err.println("Error leyendo el fichero de configuración. No se encontró el atributo 'dir'\nrequerido en la etiqueta <project>. La etiqueta será ignorada.");
					}
				}
				else {
					System.err.println("Aviso: se ignoraró una etiqueta desconocida o inesperada: " + parser.getName());
				}
			}
		}

	}
	
	public void setLanguages(String[] languages) {
		this.languages = languages; 
	}
	
	public void setProjects(ArrayList projectList) {
		this.projects = projectList;
	}
	
	/**
	 * Calculates the canonical path for the given path.
	 * If the given path is relative, it is calculated from
	 * the given baseDir.
	 * The 'path' parameter uses the '/' character to as path
	 * separator. The returned value uses the default system
	 * separator as path separator.  
	 * 
	 * @param baseDir
	 * @param path
	 * @return
	 * @throws IOException 
	 */
	public static String getAbsolutePath(String baseDir, String path) throws IOException {
		if ('/'!=File.separatorChar)
			path = path.replace('/', File.separatorChar);
		File pathFile = new File(path);
		if (pathFile.isAbsolute())
			path = pathFile.getCanonicalPath();
		else {
			File newFile = new File(baseDir+File.separator+path);
			path = newFile.getAbsolutePath();
		}
		return path;
	}
}