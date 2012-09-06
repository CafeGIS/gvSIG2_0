/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2007 IVER T.I. and Generalitat Valenciana.
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
 */
package org.gvsig.raster.dataset.serializer;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.gvsig.raster.dataset.properties.DatasetColorInterpretation;
import org.gvsig.raster.dataset.rmf.ClassSerializer;
import org.gvsig.raster.dataset.rmf.ParsingException;
import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.extensionpoint.ExtensionPoint;
import org.gvsig.tools.extensionpoint.ExtensionPointManager;
import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParserException;
/**
 * <P>
 * Clase para convertir a XML la información de interpretación de color de cada
 * banda del raster.
 * </P>
 * \<ColorInterpretation\>
 * \<BandCount\>3\</BandCount\><BR>
 * \<Band\>Red\</Band\><BR>
 * \<Band\>Green\</Band\><BR>
 * \<Band\>Blue\</Band\><BR>
 * \</ColorInterpretation\><BR>
 *
 * @version 14/01/2008
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class ColorInterpretationRmfSerializer extends ClassSerializer {
	//TAGS
	public static final String         MAIN_TAG  = "ColorInterpretation";
	public static final String         BAND      = "Band";
	public static final String         BANDCOUNT = "BandCount";

	private DatasetColorInterpretation datasetCI = null;

	/**
	 * Registra ColorInterpretationRmfSerializer en los puntos de extension de Serializer
	 */
	public static void register() {
		ExtensionPointManager extensionPoints =ToolsLocator.getExtensionPointManager();
		ExtensionPoint point=extensionPoints.get("Serializer");
		point.append("ColorInterpretation", "", ColorInterpretationRmfSerializer.class);
	}

	/**
	 * Constructor. Asigna la tabla a serializar.
	 * @param ColorTable tabla a convertir en XML
	 */
	public ColorInterpretationRmfSerializer(DatasetColorInterpretation datasetCI) {
		this.datasetCI = datasetCI;
	}

	/**
	 * Constructor.
	 */
	public ColorInterpretationRmfSerializer() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.io.rmf.IRmfBlock#read(java.lang.String)
	 */
	public void read(String xml) throws ParsingException {
		String cInterp = null;
		datasetCI = new DatasetColorInterpretation();
		int band = 0;

		KXmlParser parser = new KXmlParser();
		Reader reader = new StringReader(xml);
		try {
			parser.setInput(reader);
		} catch (XmlPullParserException e) {
			throw new ParsingException(xml);
		}
		try {
			int tag = parser.nextTag();

			if ( parser.getEventType() != KXmlParser.END_DOCUMENT ){
				parser.require(KXmlParser.START_TAG, null, MAIN_TAG);
				while(tag != KXmlParser.END_DOCUMENT) {
					switch(tag) {
						case KXmlParser.START_TAG:
							if (parser.getName().compareTo(MAIN_TAG) == 0) {
								int nBands = Integer.valueOf(parserString(parser, BANDCOUNT, null)).intValue();
								datasetCI.initColorInterpretation(nBands);
								for (int i = 0; i < nBands; i++) {
									cInterp = parserString(parser, BAND, null);
									datasetCI.setColorInterpValue(i, cInterp);
									band ++;
								}
							}
							break;
						case KXmlParser.END_TAG:
							break;
						case KXmlParser.TEXT:
							break;
					}
					tag = parser.next();
				}
				parser.require(KXmlParser.END_DOCUMENT, null, null);
			}

		} catch (XmlPullParserException e) {
			throw new ParsingException(xml);
		} catch (IOException e) {
			throw new ParsingException(xml);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.io.rmf.IRmfBlock#write()
	 */
	public String write() {
		StringBuffer b = new StringBuffer();
		if (datasetCI == null)
			return null;

		b.append("<" + MAIN_TAG + ">\n");
		putProperty(b, BANDCOUNT, datasetCI.length(), 1);
		for (int i = 0; i < datasetCI.length(); i++) {
			String ci = datasetCI.get(i);
			if (ci != null)
				putProperty(b, BAND, ci, 1);
		}
		b.append("</" + MAIN_TAG + ">\n");
		return b.toString();
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.io.rmf.IRmfBlock#getResult()
	 */
	public Object getResult() {
		return datasetCI;
	}

	/*
	 *  (non-Javadoc)
	 * @see org.gvsig.raster.dataset.io.rmf.IRmfBlock#getMainTag()
	 */
	public String getMainTag() {
		return MAIN_TAG;
	}
}
