package org.gvsig.remoteClient.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringBufferInputStream;
import java.io.StringReader;

import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParserException;

/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2004 IVER T.I. and Generalitat Valenciana.
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
/* CVS MESSAGES:
 *
 * $Id$
 * $Log$
 *
 */
/**
 * This class is a XML pull parser that discover and manage
 * the file encoding
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public class EncodingXMLParser extends KXmlParser{

	/**
	 * This method reads the first bytes of the file and
	 * try to discover the file encoding. It parses the file
	 * with retrieved encoding.
	 * @param file
	 * @throws XmlPullParserException 
	 * @throws IOException 
	 */
	public void setInput(File file) throws XmlPullParserException, IOException{
		FileReader reader = null;
		reader = new FileReader(file);
		BufferedReader br = new BufferedReader(reader);
		String encoding = "UTF-8";

		char[] buffer = new char[(int) file.length()];
		reader.read(buffer);
		String string = new String(buffer);

		// patch for ArcIMS + WMS connector > 9.0 bug
		int a = string.toLowerCase().indexOf("<?xml");
		if (a !=-1){
			string = string.substring(a, string.length());
		}
		// end patch

		StringBuffer st = new StringBuffer(string);
		String searchText = "encoding=\"";
		int index = st.indexOf(searchText);
		if (index>-1) {
			st.delete(0, index+searchText.length());
			encoding = st.substring(0, st.indexOf("\""));
		}			
		
		if (a > 0){
			// patch for ArcIMS + WMS connector > 9.0 bug
			super.setInput(new StringReader(string));
			// end patch
		}else{
			super.setInput( new FileInputStream(file), encoding);
		}
	}
}

