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

package org.gvsig.gpe.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Properties;

import org.gvsig.gpe.GPEManager;
import org.gvsig.gpe.exceptions.ParserCreationException;
import org.gvsig.gpe.exceptions.ParserFileNotSupportedException;
import org.gvsig.gpe.exceptions.ParserMimetypeNotSupportedException;
import org.gvsig.gpe.exceptions.ParserNotRegisteredException;
import org.gvsig.gpe.exceptions.WriterHandlerCreationException;
import org.gvsig.gpe.exceptions.WriterHandlerMimeTypeNotSupportedException;
import org.gvsig.gpe.exceptions.WriterHandlerNotRegisteredException;
import org.gvsig.gpe.parser.GPEParser;
import org.gvsig.gpe.writer.GPEWriterHandler;
import org.gvsig.gpe.writer.GPEWriterHandlerImplementor;
import org.gvsig.gpe.writer.IGPEWriterHandlerImplementor;

/**
 * @author <a href="mailto:jpiera@gvsig.org">Jorge Piera</a>
 */
public class DefaultGPEManager implements GPEManager {
	private Hashtable parsers = new Hashtable();
	private Hashtable writers = new Hashtable();
	private Properties properties = new Properties();

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.GPEManager#addGpeParser(java.lang.String, java.lang.String, java.lang.Class)
	 */
	public void addGpeParser(String name, String description,Class clazz) throws ParserNotRegisteredException { 
		try{
			if (clazz != null){
				GPEParser parser = (GPEParser)clazz.getConstructor(null).newInstance(null);
				parsers.put(name, parser);
			}	
		}catch (Exception e){
			throw new ParserNotRegisteredException(clazz.getName());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.GPEManager#addGpeParser(java.lang.Class)
	 */	
	public void addGpeParser(Class clazz) throws ParserNotRegisteredException { 
		try{
			if (clazz != null){
				GPEParser parser = (GPEParser)clazz.getConstructor(null).newInstance(null);
				parsers.put(parser.getName(), parser);
			}	
		}catch (Exception e){
			throw new ParserNotRegisteredException(clazz.getName());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.GPEManager#addParsersFile(java.io.File)
	 */
	public void addParsersFile(File file) throws FileNotFoundException, IOException{
		if (!file.exists()){
			return;
		}
		Properties properties = new Properties();
		properties.load(new FileInputStream(file));
		for (Enumeration e = properties.keys(); e.hasMoreElements() ; ) {
			String key = e.nextElement().toString();
			Class clazz;
			try {
				clazz = Class.forName(properties.getProperty(key).toString());
				addGpeParser(clazz);
			} catch (ClassNotFoundException ex) {
				//Next class
			} catch (ParserNotRegisteredException ex) {
				//Next class
			} 	    
		}
	}	

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.GPEManager#addWritersFile(java.io.File)
	 */
	public void addWritersFile(File file) throws FileNotFoundException, IOException{
		if (!file.exists()){
			return;
		}
		Properties properties = new Properties();
		properties.load(new FileInputStream(file));
		for (Enumeration e = properties.keys(); e.hasMoreElements() ; ) {
			String key = e.nextElement().toString();
			Class clazz;
			try {
				clazz = Class.forName(properties.getProperty(key).toString());
				addGpeWriterHandler(clazz);
			} catch (ClassNotFoundException ex){
				//Next class
			} catch (WriterHandlerNotRegisteredException ex){
				//Next class
			}		    
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.GPEManager#getAllParsers()
	 */
	public GPEParser[] getAllParsers(){
		GPEParser[] auxParsers = new GPEParser[parsers.size()];
		Iterator it = parsers.keySet().iterator();
		int i=0;
		while (it.hasNext()){
			String key = (String)it.next();
			auxParsers[i] = (GPEParser)parsers.get(key);
			i++;
		}
		return auxParsers;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.GPEManager#addGpeWriterHandler(java.lang.String, java.lang.String, java.lang.Class)
	 */
	public void addGpeWriterHandler(String name, String description,Class clazz) throws WriterHandlerNotRegisteredException {
		try{
			if (clazz != null){
				GPEWriterHandlerImplementor writerImplementor = (GPEWriterHandlerImplementor)clazz.getConstructor(null).newInstance(null);
				writers.put(name, writerImplementor);
			}
		}catch (Exception e){
			throw new WriterHandlerNotRegisteredException(clazz.getName());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.GPEManager#addGpeWriterHandler(java.lang.Class)
	 */
	public void addGpeWriterHandler(Class clazz) throws WriterHandlerNotRegisteredException {
		try{
			if (clazz != null){
				GPEWriterHandlerImplementor writerImplementor = (GPEWriterHandlerImplementor)clazz.getConstructor(null).newInstance(null);
				writers.put(writerImplementor.getName(), writerImplementor);
			}
		}catch (Exception e){
			throw new WriterHandlerNotRegisteredException(clazz.getName());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.GPEManager#createParser(java.lang.String)
	 */
	public GPEParser createParser(String name) throws ParserCreationException  {
		Object parser =  parsers.get(name);
		if (parser != null){
			return createNewParser((GPEParser)parser);
		}else{
			Exception e = new ParserNotRegisteredException(name);
			throw new ParserCreationException(e);
		}
	}

	private GPEParser createNewParser(GPEParser parser) throws ParserCreationException{
		try{
			return (GPEParser)parser.getClass().getConstructor(null).newInstance(null);
		}catch (Exception e) {
			throw new ParserCreationException(e);
		}		
	}

	/* (non-Javadoc)
	 * @see org.gvsig.gpe.GPEManager#createParserByClass(java.lang.String)
	 */
	public GPEParser createParserByClass(String prefferredImplClassName)
	throws ParserCreationException {
		Iterator it = parsers.keySet().iterator();
		int i=0;
		while (it.hasNext()){
			String key = (String)it.next();
			GPEParser parser = (GPEParser)parsers.get(key);
			if (parser.getClass().getName().equals(prefferredImplClassName)){
				return createNewParser(parser);
			}
		}
		throw new ParserNotRegisteredException(prefferredImplClassName) ;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.GPEManager#createParser(java.net.URI)
	 */
	public GPEParser createParser(URI uri) throws ParserCreationException {
		Iterator keys = parsers.keySet().iterator();
		while (keys.hasNext()){
			String key = (String)keys.next();
			GPEParser parser = (GPEParser)parsers.get(key);
			if (parser.accept(uri)){
				return createNewParser(parser);
			}
		}
		throw new ParserFileNotSupportedException(uri);
	}	

	/* (non-Javadoc)
	 * @see org.gvsig.gpe.GPEManager#createParserByMimeType(java.lang.String)
	 */
	public GPEParser createParserByMimeType(String mimeType)
	throws ParserCreationException {
		Iterator it = parsers.keySet().iterator();
		int i=0;
		while (it.hasNext()){
			String key = (String)it.next();
			GPEParser parser = (GPEParser)parsers.get(key);
			if (parser.getFormat().equals(mimeType)){
				return createNewParser(parser);
			}
		}
		throw new ParserMimetypeNotSupportedException(mimeType);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.GPEManager#createWriter(java.lang.String)
	 */
	public GPEWriterHandler createWriter(String name) throws WriterHandlerCreationException{
		Object writer =  writers.get(name);
		if (writer != null){
			return createNewWriterHandler((IGPEWriterHandlerImplementor)writer);
		}else{
			Exception e = new WriterHandlerNotRegisteredException(name);
			throw new WriterHandlerCreationException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.gvsig.gpe.GPEManager#createWriterByClass(java.lang.String)
	 */
	public GPEWriterHandler createWriterByClass(String prefferredImplClassName)
	throws WriterHandlerCreationException {
		Iterator it = writers.keySet().iterator();
		ArrayList possibleWriters = new ArrayList();
		while (it.hasNext()){
			String key = (String)it.next();
			IGPEWriterHandlerImplementor implementor = (IGPEWriterHandlerImplementor)writers.get(key);
			if (implementor.getClass().getName().equals(prefferredImplClassName)){
				return createNewWriterHandler(implementor);
			}
		}
		throw new WriterHandlerNotRegisteredException(prefferredImplClassName);
	}

	/* (non-Javadoc)
	 * @see org.gvsig.gpe.GPEManager#createWriterByMimeType(java.lang.String)
	 */
	public GPEWriterHandler createWriterByMimeType(String mimeType)
	throws WriterHandlerCreationException {
		Iterator it = writers.keySet().iterator();
		ArrayList possibleWriters = new ArrayList();
		while (it.hasNext()){
			String key = (String)it.next();
			IGPEWriterHandlerImplementor implementor = (IGPEWriterHandlerImplementor)writers.get(key);
			if (implementor.getFormat().equals(mimeType)){
				return createNewWriterHandler(implementor);
			}
		}
		throw new WriterHandlerMimeTypeNotSupportedException(mimeType);
	}

	private GPEWriterHandler createNewWriterHandler(IGPEWriterHandlerImplementor gpeWriterHandlerImplementor) throws WriterHandlerCreationException{
		try{
			GPEWriterHandlerImplementor writerImplementor = (GPEWriterHandlerImplementor)gpeWriterHandlerImplementor.getClass().getConstructor(null).newInstance(null);
			return new GPEWriterHandler(writerImplementor);			
		}catch (Exception e) {
			throw new WriterHandlerCreationException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.GPEManager#getWriterHandlerByFormat(java.lang.String)
	 */
	public ArrayList getWriterHandlerByFormat(String format){
		Iterator it = writers.keySet().iterator();
		ArrayList possibleWriters = new ArrayList();
		while (it.hasNext()){
			String key = (String)it.next();
			IGPEWriterHandlerImplementor implementor = (IGPEWriterHandlerImplementor)writers.get(key);
			if (format.toLowerCase().compareTo(format.toLowerCase()) == 0){
				possibleWriters.add(new GPEWriterHandler(implementor));
			}
		}
		return possibleWriters;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.GPEManager#accept(java.net.URI)
	 */
	public boolean accept(URI uri){
		Iterator keys = parsers.keySet().iterator();
		while (keys.hasNext()){
			GPEParser parser = (GPEParser)parsers.get(keys.next());
			if (parser.accept(uri)){
				return true;
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.GPEManager#getKeys()
	 */
	public Iterator getKeys(){
		return properties.keySet().iterator();
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.GPEManager#getStringProperty(java.lang.String)
	 */
	public String getStringProperty(String key){
		Object obj = getProperty(key);
		if (obj == null){
			return null;
		}
		return (String)obj;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.GPEManager#getIntPropertyProperty(java.lang.String)
	 */
	public int getIntPropertyProperty(String key){
		Object obj = getProperty(key);
		if (obj == null){
			return -1;
		}
		if (obj instanceof Integer){
			return ((Integer)obj).intValue();
		}return -1;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.GPEManager#getBooleanProperty(java.lang.String)
	 */
	public boolean getBooleanProperty(String key){
		Object obj = getProperty(key);
		if (obj == null){
			return false;
		}
		if (obj instanceof Boolean){
			return ((Boolean)obj).booleanValue();
		}return false;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.GPEManager#getProperty(java.lang.String)
	 */
	public Object getProperty(String key){
		return properties.get(key);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.GPEManager#setProperty(java.lang.String, java.lang.Object)
	 */
	public void setProperty(String key, Object value){
		properties.put(key, value);
	}	
}

