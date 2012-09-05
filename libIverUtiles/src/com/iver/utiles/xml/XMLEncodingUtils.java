/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * This class is based on KXML2's KXmlParser.java
 * Copyright (c) From KXML2: 2002,2003, Stefan Haustein, Oberhausen, Rhld., Germany
 * See http://kxml.sourceforge.net to get more info.
 * 
 * Copyright (C) From this file: 2006 IVER T.I. and Generalitat Valenciana.
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

package com.iver.utiles.xml;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * A set of methods to detect XML encoding. The class is able to autodetect
 * certain encodings, and it reads the XML header for the rest of encodings.
 * 
 * @author César Martínez Izquierdo <cesar.martinez@iver.es>
 *
 */
public class XMLEncodingUtils {
	InputStream _is;
	
	/**
	 * Creates a new XMLEncodingUtils object.
	 * 
	 * @param is An InputStream connected to the XML file to process.
	 */
	public XMLEncodingUtils(InputStream is) {
		if (is == null)
			throw new IllegalArgumentException();
		_is = is;
	}
	
	/**
	 * Gets the encoding of the XML file.
	 * 
	 * The following encodings can be detected: UTF-32BE, UTF-32LE,
	 * UTF-16BE, UTF-16-LE, UTF-8. The rest of the encodings are
	 * read from the XML header.
	 * 
	 * @return Returns the encoding of the XML file, or null if the
	 * encoding couldn't be correctly detected or read from the XML
	 * header.
	 */
	public String getEncoding() {
		int srcCount = 0;
		String enc=null;
		char[] srcBuf = new char[128];
		
		// read four bytes 
		int chk = 0;
		try {
			while (srcCount < 4) {
				int i = _is.read();
				if (i == -1)
					break;
				chk = (chk << 8) | i;
				srcBuf[srcCount++] = (char) i;
			}
			
			if (srcCount == 4) {
				switch (chk) {
				case 0x00000FEFF :
					enc = "UTF-32BE";
					srcCount = 0;
					break;
					
				case 0x0FFFE0000 :
					enc = "UTF-32LE";
					srcCount = 0;
					break;
					
				case 0x03c :
					enc = "UTF-32BE";
					srcBuf[0] = '<';
					srcCount = 1;
					break;
					
				case 0x03c000000 :
					enc = "UTF-32LE";
					srcBuf[0] = '<';
					srcCount = 1;
					break;
					
				case 0x0003c003f :
					enc = "UTF-16BE";
					srcBuf[0] = '<';
					srcBuf[1] = '?';
					srcCount = 2;
					break;
					
				case 0x03c003f00 :
					enc = "UTF-16LE";
					srcBuf[0] = '<';
					srcBuf[1] = '?';
					srcCount = 2;
					break;
					
				case 0x03c3f786d :
					while (true) {
						int i = _is.read();
						if (i == -1)
							break;
						srcBuf[srcCount++] = (char) i;
						if (i == '>') {
							String s = new String(srcBuf, 0, srcCount);
							int i0 = s.indexOf("encoding");
							if (i0 != -1) {
								while (s.charAt(i0) != '"'
									&& s.charAt(i0) != '\'')
									i0++;
								char deli = s.charAt(i0++);
								int i1 = s.indexOf(deli, i0);
								enc = s.substring(i0, i1);
							}
							break;
						}
					}
					
				default :
					if ((chk & 0x0ffff0000) == 0x0FEFF0000) {
						enc = "UTF-16BE";
						srcBuf[0] =
							(char) ((srcBuf[2] << 8) | srcBuf[3]);
						srcCount = 1;
					}
					else if ((chk & 0x0ffff0000) == 0x0fffe0000) {
						enc = "UTF-16LE";
						srcBuf[0] =
							(char) ((srcBuf[3] << 8) | srcBuf[2]);
						srcCount = 1;
					}
					else if ((chk & 0x0ffffff00) == 0x0EFBBBF00) {
						enc = "UTF-8";
						srcBuf[0] = srcBuf[3];
						srcCount = 1;
					}
				}
			}
		}
		catch (IOException ex) {
			return null;
		}
		return enc;
	}
	
	/**
	 * Gets an InputStreamReader for the provided XML file.
	 * The reader uses the right encoding, as specified in
	 * the XML header (or autodetected). 
	 * 
	 * @return A reader which uses the right encoding, or null
	 * if the encoding couldn't be correctly detected or read
	 * from the XML header.
	 */
	public InputStreamReader getReader() {
		String encoding = getEncoding();
		if (encoding==null)
			return null;
		try {
			return new InputStreamReader(_is, encoding);
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}
	
	/**
	 * Gets an InputStreamReader for the provided XML file.
	 * The reader uses the right encoding, as specified in
	 * the XML header (or autodetected).
	 * 
	 * @param is An InputStream connected to the XML file to process
	 * @return A reader for the provided XML file.
	 * @see getReader()
	 */
	public static InputStreamReader getReader(InputStream is) {
		XMLEncodingUtils util = new XMLEncodingUtils(is);
		return util.getReader();
	}

	/**
	 * Gets the character encoding of the XML file.
	 * 
	 * @param is An InputStream connected to the XML file to process
	 * @see getEncoding()
	 * @return The encoding of the file
	 */
	public static String getEncoding(InputStream is) {
		XMLEncodingUtils util = new XMLEncodingUtils(is);
		return util.getEncoding();
	}
	
	/**
	 * Gets an InputStreamReader for the provided XML file.
	 * The reader uses the right encoding, as specified in
	 * the XML header (or autodetected).
	 * 
	 * @param file The XML file to process
	 * @return A reader for the provided XML file.
	 * @see getReader()
	 */
	public static InputStreamReader getReader(File file) throws FileNotFoundException {
		BufferedInputStream bs = new BufferedInputStream(new FileInputStream(file));
		XMLEncodingUtils util = new XMLEncodingUtils(bs);
		return util.getReader();
	}
	
	/**
	 * Gets the character encoding of the XML file.
	 * 
	 * @param File The XML file to process
	 * @see getEncoding()
	 * @return The encoding of the file
	 * @throws FileNotFoundException 
	 */
	public static String getEncoding(File file) throws FileNotFoundException {
		BufferedInputStream bs = new BufferedInputStream(new FileInputStream(file));
		XMLEncodingUtils util = new XMLEncodingUtils(bs);
		return util.getEncoding();
	}
}
