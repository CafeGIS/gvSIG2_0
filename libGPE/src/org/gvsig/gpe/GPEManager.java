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
* 2009 {Iver T.I.}   {Task}
*/
 
package org.gvsig.gpe;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Properties;

import org.gvsig.gpe.exceptions.ParserCreationException;
import org.gvsig.gpe.exceptions.ParserMimetypeNotSupportedException;
import org.gvsig.gpe.exceptions.ParserNotRegisteredException;
import org.gvsig.gpe.exceptions.WriterHandlerCreationException;
import org.gvsig.gpe.exceptions.WriterHandlerNotRegisteredException;
import org.gvsig.gpe.parser.GPEParser;
import org.gvsig.gpe.writer.GPEWriterHandler;
import org.gvsig.gpe.writer.GPEWriterHandlerImplementor;
import org.gvsig.gpe.writer.IGPEWriterHandlerImplementor;

/**
 * @author <a href="mailto:jpiera@gvsig.org">Jorge Piera</a>
 */
public interface GPEManager {
	
	/**
	 * Adds a new GPE parser
	 * @param name
	 * Driver name. It must be written like FORMAT VERSION
	 * @param description
	 * Driver description. Just a descriptive text
	 * @param clazz
	 * The parser class	
	 * @throws ParserNotRegisteredException 
	 * @throws GPEParserRegisterException
	 */
	public void addGpeParser(String name, String description,Class clazz) throws ParserNotRegisteredException; 
			
	/**
	 * Adds a new GPE parser
	 * @param clazz
	 * The parser class	
	 * @throws ParserNotRegisteredException 
	 * @throws GPEParserRegisterException
	 */
	public void addGpeParser(Class clazz) throws ParserNotRegisteredException; 
			
	/**
	 * It loads the parsers of a parsers file. The file is
	 * a properties file. Every line has the structure: 
	 * Parser=Parser class
	 * @param file
	 * File that contains the parsers list
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 * @throws ParserNotRegisteredException 
	 */
	public void addParsersFile(File file) throws FileNotFoundException, IOException;
		
	/**
	 * It loads the writers of a writers file. The file is
	 * a properties file. Every line has the structure: 
	 * Writer=Parser class
	 * @param file
	 * File that contains the writers list
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public void addWritersFile(File file) throws FileNotFoundException, IOException;
			
	/**
	 * @return all the registered parsers
	 */
	public GPEParser[] getAllParsers();
				
	/**
	 * Adds a new GPEWriterHandlerImplementor
	 * @param name
	 * Driver name. It must be written like FORMAT VERSION
	 * @param description
	 * Driver description. Just a descriptive text
	 * @param clazz
	 * The parser class	
	 * @throws WriterHandlerNotRegisteredException 
	 * @throws GPEWriterHandlerRegisterException 
	 */
	public void addGpeWriterHandler(String name, String description,Class clazz) throws WriterHandlerNotRegisteredException;
	
	/**
	 * Adds a new GPEWriterHandlerImplementor
	 * @param clazz
	 * The parser class	
	 * @throws WriterHandlerNotRegisteredException 
	 * @throws GPEWriterHandlerRegisterException 
	 */
	public void addGpeWriterHandler(Class clazz) throws WriterHandlerNotRegisteredException;
	
	/**
	 * Create a new parser from a name
	 * @param name
	 * GPEParser name
	 * @param contenHandler
	 * Application contenHandler usett to throw the parsing events
	 * @param errorHandler
	 * Application errror handler used to put errors and warnings
	 * @throws ParserCreationException 
	 * @throws GPEParserCreationException 
	 */
	public GPEParser createParser(String name) throws ParserCreationException;
		
	/**
	 * Create a new parser from a name
	 * @param name
	 * GPEParser name
	 * @param contenHandler
	 * Application contenHandler usett to throw the parsing events
	 * @param errorHandler
	 * Application errror handler used to put errors and warnings
	 * @throws ParserCreationException 
	 * @throws GPEParserCreationException 
	 */
	public GPEParser createParserByClass(String prefferredImplClassName) throws ParserCreationException;
		
	/**
	 * Create a new parser from a mime type. Each parser has a method
	 * named {@link GPEParser#getFormat()} that returns the mimetype
	 * that the parser can read. One parser only supports one mimetype.
	 * <p>
	 * This method retrieve all the parsers and returns the first
	 * parser that is able to read the mimetype. If there are more
	 * parsers that can open the file will not be used.
	 * </p>
	 * @param mimeType
	 * The mimetype of the file to open
	 * @return
	 * A parser that can parse the mimetype.
	 * @throws ParserCreationException
	 * If it is not possible to create a parser
	 * @see 
	 * <a href="http://www.iana.org/assignments/media-types/">http://www.iana.org/assignments/media-types/</a>
	 */
	public GPEParser createParserByMimeType(String mimeType) throws ParserCreationException;
		
	/**
	 * Gets the parser that can open the file (if it exists)
	 * @param uri
	 * File to open
	 * @return
	 * Null if the driver doesn't exist
	 * @throws GPEParserCreationException 
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws SecurityException 
	 * @throws IllegalArgumentException 
	 */
	public GPEParser createParser(URI uri) throws ParserCreationException;
			
	/**
	 * Create a new content writer from a name
	 * @param name
	 * GPEWriterHandler name
	 * GPEParser name
	 * @param contenHandler
	 * Application contenHandler usett to throw the parsing events
	 * @param errorHandler
	 * Application errror handler used to put errors and warnings
	 * @throws GPEWriterHandlerCreationException 	
	 */
	public GPEWriterHandler createWriter(String name) throws WriterHandlerCreationException;
	
	/**
	 * Create a new writer from a class name. This method can be used 
	 * if the class name is known but, the common method to get a writer
	 * is using the mime type.     * 
	 * @param prefferredImplClassName
	 * The name of the class that implements {@link GPEWriterHandler}
	 * @return
	 * A writer for a concrete format.
	 * @throws WriterHandlerCreationException
	 * If it is not possible to create a writer
	 */
	public GPEWriterHandler createWriterByClass(String prefferredImplClassName) throws WriterHandlerCreationException; 
		
	/**
	 * Create a new writer from a mime type. Each writer has a method
	 * named {@link GPEWriterHandler#getFormat()} that returns the mimetype
	 * that the writer can write. One writer only supports one mimetype.
	 * <p>
	 * This method retrieve all the writers and returns the first
	 * one that is able to write the mimetype. If there are more
	 * writer that can write the format will not be used.
	 * </p>
	 * @param mimeType
	 * The mimetype of the file to write
	 * @return
	 * A writer that can write the mimetype.
	 * @throws WriterHandlerCreationException
	 * If it is not possible to create a writer
	 * @see 
	 * <a href="http://www.iana.org/assignments/media-types/">http://www.iana.org/assignments/media-types/</a>
	 */
	public GPEWriterHandler createWriterByMimeType(String mimeType) throws WriterHandlerCreationException;
	
	/**
	 * Gets all the writers that can 
	 * @param format
	 * @return
	 */
	public ArrayList getWriterHandlerByFormat(String format);
		
	/**
	 * Return true if exists a driver that can open the file
	 * @param uri
	 * File to open
	 * @return
	 * true if the driver exists
	 */
	public boolean accept(URI uri);	
	
	/**
	 * Returns an iterator with the name of all the properties that 
	 * has been established.
	 */
	public Iterator getKeys();
		
	
	/**
	 * Gets a String property
	 * @param key
	 * Property name
	 * @return
	 */
	public String getStringProperty(String key);
	
	/**
	 * Gets a int property
	 * @param key
	 * Property name
	 * @return
	 * The int property or -1
	 */
	public int getIntPropertyProperty(String key);
	
	/**
	 * Gets a boolean property. If the property doesn't exist
	 * it returns false.
	 * @param key
	 * Property name
	 * @return
	 * The boolean property or false
	 */
	public boolean getBooleanProperty(String key);
	
	/**
	 * Gets a property
	 * @param key
	 * Property name
	 * @return
	 */
	public Object getProperty(String key);
	
	/**
	 * Sets a property
	 * @param key
	 * @param value
	 */
	public void setProperty(String key, Object value);
}

