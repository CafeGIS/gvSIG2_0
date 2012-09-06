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
package org.gvsig.raster.datastruct.serializer;

import java.awt.Color;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;

import org.gvsig.raster.dataset.rmf.ClassSerializer;
import org.gvsig.raster.dataset.rmf.ParsingException;
import org.gvsig.raster.datastruct.ColorItem;
import org.gvsig.raster.datastruct.ColorTable;
import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.extensionpoint.ExtensionPoint;
import org.gvsig.tools.extensionpoint.ExtensionPointManager;
import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParserException;
/**
 * <P>
 * Clase para convertir a XML una tabla de color y obtener la tabla desde XML.
 * Esta clase implementa el interfaz IRmfBlock con los métodos de escritura y
 * lectura. Estos serán utilizados por el gestor de ficheros RMF para escribir y
 * leer datos.
 * </P>
 * <P>
 * La estructura XML de una tabla de color es la siguiente:
 * </P>
 * <P>
 * &lt;ColorTable name="nombre" version="1.1"><BR>
 * &nbsp;&lt;Color value="0.0" name="rojo" rgb="#FF34FF" interpolated="50"/><BR>
 * &nbsp;&lt;Color value="1.0" name="rojo" rgb="#FF34FF" interpolated="50"/><BR>
 * &nbsp;&lt;Alpha value="0.0" number="255" interpolated="50"/><BR>
 * &nbsp;&lt;NoData rgba="#"><BR>
 * &lt;/ColorTable><BR>
 *
 * @version 23/04/2007
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class ColorTableRmfSerializer extends ClassSerializer {
	private final String MAIN_TAG = "ColorTable";

	private ColorTable  colorTable = null;

	/**
	 * Registra ColorTableRmfSerializer en los puntos de extension de Serializer
	 */
	public static void register() {
		ExtensionPointManager extensionPoints =ToolsLocator.getExtensionPointManager();
		ExtensionPoint point=extensionPoints.get("Serializer");
		point.append("ColorTable", "", ColorTableRmfSerializer.class);
	}

	/**
	 * Constructor. Asigna la tabla a serializar.
	 * @param ColorTable tabla a convertir en XML
	 */
	public ColorTableRmfSerializer(ColorTable colorTable) {
		this.colorTable = colorTable;
	}

	/**
	 * Constructor.
	 */
	public ColorTableRmfSerializer() {
	}

	/**
	 * Devuelve el color si lo encuentra en el arraylist y lo elimina, en caso
	 * contrario devuelve null
	 * @param list
	 * @param value
	 * @return
	 */
	private static ColorItem getColorItem(ArrayList list, double value) {
		for (int i = 0; i < list.size(); i++) {
			if (((ColorItem) list.get(i)).getValue() == value) {
				return (ColorItem) list.remove(i);
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.io.rmf.IRmfBlock#read(java.lang.String)
	 */
	public void read(String xml) throws ParsingException {
		String paletteName = "";
		boolean interpolated = true;

		KXmlParser parser = new KXmlParser();
		Reader reader = new StringReader(xml);
		try {
			parser.setInput(reader);
		} catch (XmlPullParserException e) {
			throw new ParsingException(xml);
		}

		try {
			ArrayList rows = new ArrayList();

			int tag = parser.nextTag();

			if (parser.getEventType() != KXmlParser.END_DOCUMENT) {
				parser.require(KXmlParser.START_TAG, null, MAIN_TAG);

				for (int i = 0; i < parser.getAttributeCount(); i++) {
					if (parser.getAttributeName(i).equals("name"))
						paletteName = parser.getAttributeValue(i);
					if (parser.getAttributeName(i).equals("interpolated"))
						interpolated = parser.getAttributeValue(i).equals("1");
				}

				while (tag != KXmlParser.END_DOCUMENT) {
					switch (tag) {
						case KXmlParser.START_TAG:
							if (parser.getName().equals("Color")) {
								ColorItem colorItem = new ColorItem();
								int a = 255;
								for (int i = 0; i < parser.getAttributeCount(); i++) {
									if (parser.getAttributeName(i).equals("value")) {
										colorItem.setValue(Double.parseDouble((String) parser.getAttributeValue(i)));
										ColorItem aux = getColorItem(rows, Double.parseDouble((String) parser.getAttributeValue(i)));
										if (aux != null)
											a = aux.getColor().getAlpha();
									}
									if (parser.getAttributeName(i).equals("name")) {
										colorItem.setNameClass((String) parser.getAttributeValue(i));
									}
									if (parser.getAttributeName(i).equals("rgb")) {
										String rgb = parser.getAttributeValue(i);
										int r = Integer.valueOf(rgb.substring(0, rgb.indexOf(","))).intValue();
										int g = Integer.valueOf(rgb.substring(rgb.indexOf(",") + 1, rgb.lastIndexOf(","))).intValue();
										int b = Integer.valueOf(rgb.substring(rgb.lastIndexOf(",") + 1, rgb.length())).intValue();

										colorItem.setColor(new Color(r, g, b, a));
									}
									if (parser.getAttributeName(i).equals("interpolated")) {
										colorItem.setInterpolated(Double.parseDouble((String) parser.getAttributeValue(i)));
									}
								}

								rows.add(colorItem);
								break;
							}
							if (parser.getName().equals("Alpha")) {
								ColorItem colorItem = new ColorItem();
								for (int i = 0; i < parser.getAttributeCount(); i++) {
									if (parser.getAttributeName(i).equals("value")) {
										colorItem.setValue(Double.parseDouble((String) parser.getAttributeValue(i)));
										ColorItem aux = getColorItem(rows, Double.parseDouble((String) parser.getAttributeValue(i)));
										if (aux != null) {
											colorItem.setNameClass(aux.getNameClass());
											colorItem.setInterpolated(aux.getInterpolated());
											colorItem.setColor(new Color(aux.getColor().getRed(), aux.getColor().getGreen(), aux.getColor().getBlue(), colorItem.getColor().getAlpha()));
										}
									}
									if (parser.getAttributeName(i).equals("alpha")) {
										int a = Integer.parseInt(parser.getAttributeValue(i));

										colorItem.setColor(new Color(colorItem.getColor().getRed(), colorItem.getColor().getGreen(), colorItem.getColor().getBlue(), a));
									}
									if (parser.getAttributeName(i).equals("interpolated")) {
										colorItem.setInterpolated(Double.parseDouble((String) parser.getAttributeValue(i)));
									}
								}

								rows.add(colorItem);
								break;
							}
							break;
					}

					tag = parser.next();
				}
				parser.require(KXmlParser.END_DOCUMENT, null, null);
			}

			colorTable = new ColorTable();
			colorTable.setName(paletteName);
			colorTable.createPaletteFromColorItems(rows, false);
			colorTable.setInterpolated(interpolated);
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

		if (colorTable == null)
			return "";

		b.append("<" + MAIN_TAG + " name=\"" + colorTable.getName() + "\" interpolated=\"" + (colorTable.isInterpolated()?"1":"0") + "\" version=\"1.1\">\n");

		for (int i = 0; i < colorTable.getColorItems().size(); i++) {
			b.append("\t<Color");
			ColorItem colorItem = (ColorItem) colorTable.getColorItems().get(i);
			b.append(" value=\"" + colorItem.getValue() + "\"");
			b.append(" name=\"" + colorItem.getNameClass() + "\"");
			Color c = colorItem.getColor();
			b.append(" rgb=\"" + c.getRed() + "," + c.getGreen() + "," + c.getBlue() + "\"");
			b.append(" interpolated=\"" + colorItem.getInterpolated() + "\"");
			b.append("/>\n");
		}
		for (int i = 0; i < colorTable.getColorItems().size(); i++) {
			b.append("\t<Alpha");
			ColorItem colorItem = (ColorItem) colorTable.getColorItems().get(i);
			b.append(" value=\"" + colorItem.getValue() + "\"");
			Color c = colorItem.getColor();
			b.append(" alpha=\"" + c.getAlpha() + "\"");
			b.append(" interpolated=\"" + colorItem.getInterpolated() + "\"");
			b.append("/>\n");
		}

		b.append("</" + MAIN_TAG + ">\n");

		return b.toString();
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.io.rmf.IRmfBlock#getResult()
	 */
	public Object getResult() {
		return colorTable;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.io.rmf.IRmfBlock#getMainTag()
	 */
	public String getMainTag() {
		return MAIN_TAG;
	}
}