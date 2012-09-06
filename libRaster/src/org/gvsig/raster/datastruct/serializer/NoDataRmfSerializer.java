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
package org.gvsig.raster.datastruct.serializer;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.gvsig.raster.RasterLibrary;
import org.gvsig.raster.dataset.rmf.ClassSerializer;
import org.gvsig.raster.dataset.rmf.ParsingException;
import org.gvsig.raster.datastruct.NoData;
import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.extensionpoint.ExtensionPoint;
import org.gvsig.tools.extensionpoint.ExtensionPointManager;
import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParserException;
/**
 * <P>
 * Clase para convertir a XML un valor NoData y obtener el valor desde un XML.
 * Esta clase implementa el interfaz IRmfBlock con los métodos de escritura y
 * lectura. Estos serán utilizados por el gestor de ficheros RMF para escribir y
 * leer datos.
 * </P>
 * <P>
 * La estructura XML de un valor NoData es la siguiente:
 * </P>
 * <P>
 * &lt;NoData value="-99.999"><BR>
 * &lt;/NoData><BR>
 *
 * @version 18/12/2007
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
public class NoDataRmfSerializer extends ClassSerializer {
	private final String MAIN_TAG = "NoData";
	private NoData noData = null;

	/**
	 * Registra NoDataRmfSerializer en los puntos de extension de Serializer
	 */
	public static void register() {
		ExtensionPointManager extensionPoints =ToolsLocator.getExtensionPointManager();
		ExtensionPoint point=extensionPoints.get("Serializer");
		point.append("NoData", "", NoDataRmfSerializer.class);
	}

	/**
	 * Constructor. Asigna el valor NoData a serializar
	 * @param noData   Valor NoData
	 * @param type     Tipo de NoData
	 * @param dataType Tipo de datos de la capa
	 */
	public NoDataRmfSerializer(NoData noData) {
		this.noData = noData;
	}

	/**
	 * Constructor.
	 */
	public NoDataRmfSerializer() {}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.io.rmf.IRmfBlock#getMainTag()
	 */
	public String getMainTag() {
		return MAIN_TAG;
	}

	/**
	 * Devuelve el valor noData, en caso de no existir lo crea.
	 * @return
	 */
	private NoData getNoData() {
		if (noData == null)
			noData = new NoData();
		return noData;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.io.rmf.IRmfBlock#getResult()
	 */
	public Object getResult() {
		if (noData == null)
			return null;
		if (getNoData().getType() == -1)
			return null;
		if (getNoData().getType() == RasterLibrary.NODATATYPE_LAYER)
			return null;
		return noData;
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
							if (parser.getName().equals("Data")) {
								for (int i = 0; i < parser.getAttributeCount(); i++) {
									if (parser.getAttributeName(i).equals("value")) {
										getNoData().setValue(Double.parseDouble((String) parser.getAttributeValue(i)));
									}
									if (parser.getAttributeName(i).equals("type")) {
										getNoData().setType(Integer.parseInt((String) parser.getAttributeValue(i)));
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
		if (noData == null)
			return "";

		if (getNoData().getType() == RasterLibrary.NODATATYPE_LAYER)
			return "";

		StringBuffer b = new StringBuffer();

		b.append("<" + MAIN_TAG + ">\n");
		b.append("\t<Data");
		if (getNoData().getType() == RasterLibrary.NODATATYPE_USER)
			b.append(" value=\"" + getNoData().getValue() + "\"");
		b.append(" type=\"" + getNoData().getType() + "\"/>\n");
		b.append("</" + MAIN_TAG + ">\n");

		return b.toString();
	}
}