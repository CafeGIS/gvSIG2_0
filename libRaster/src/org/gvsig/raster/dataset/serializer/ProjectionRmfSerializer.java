/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2005 IVER T.I. and Generalitat Valenciana.
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

import org.cresques.cts.IProjection;
import org.gvsig.raster.dataset.rmf.ClassSerializer;
import org.gvsig.raster.dataset.rmf.ParsingException;
import org.gvsig.raster.projection.CRS;
import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.extensionpoint.ExtensionPoint;
import org.gvsig.tools.extensionpoint.ExtensionPointManager;
import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParserException;
/**
 * <P>
 * Clase para convertir a XML una proyeccion y obtener el valor desde un XML.
 * Esta clase implementa el interfaz IRmfBlock con los métodos de escritura y
 * lectura. Estos serán utilizados por el gestor de ficheros RMF para escribir y
 * leer datos.
 * </P>
 * <P>
 * La estructura XML de una proyeccion es la siguiente:
 * </P>
 * <P>
 *
 * @version 20/05/2008
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
public class ProjectionRmfSerializer extends ClassSerializer {
	private final String MAIN_TAG   = "Projection";
	private IProjection  projection = null;

	/**
	 * Registra ProjectionRmfSerializer en los puntos de extension de Serializer
	 */
	public static void register() {
		ExtensionPointManager extensionPoints =ToolsLocator.getExtensionPointManager();
		ExtensionPoint point=extensionPoints.get("Serializer");
		point.append("Projection", "", ProjectionRmfSerializer.class);
	}

	public ProjectionRmfSerializer(IProjection projection) {
		this.projection = projection;
	}

	/**
	 * Constructor.
	 */
	public ProjectionRmfSerializer() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.io.rmf.IRmfBlock#getMainTag()
	 */
	public String getMainTag() {
		return MAIN_TAG;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.io.rmf.IRmfBlock#getResult()
	 */
	public Object getResult() {
		return projection;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.io.rmf.IRmfBlock#read(java.lang.String)
	 */
	public void read(String xml) throws ParsingException {
		KXmlParser parser = new KXmlParser();
		Reader reader = new StringReader(xml);
		try {
			parser.setInput(reader);
		} catch (XmlPullParserException e) {
			throw new ParsingException(xml);
		}

		try {
			int tag = parser.nextTag();

			if (parser.getEventType() != KXmlParser.END_DOCUMENT) {
				parser.require(KXmlParser.START_TAG, null, MAIN_TAG);

				while (tag != KXmlParser.END_DOCUMENT) {
					switch (tag) {
						case KXmlParser.START_TAG:
							if (parser.getName().equals("WktProjection")) {
								for (int i = 0; i < parser.getAttributeCount(); i++) {
									if (parser.getAttributeName(i).equals("value")) {
										projection = CRS.convertWktToIProjection((String) parser.getAttributeValue(i));
									}
								}
							}
					}
					tag = parser.next();
				}
			}
			reader.close();
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
	public String write() throws IOException {
		StringBuffer b = new StringBuffer();

		if (projection == null)
			return null;

		b.append("<" + MAIN_TAG + ">\n");
		b.append("\t<WktProjection");
		b.append(" value=\"" + CRS.convertIProjectionToWkt(projection) + "\"/>\n");
		b.append("</" + MAIN_TAG + ">\n");

		return b.toString();
	}
}