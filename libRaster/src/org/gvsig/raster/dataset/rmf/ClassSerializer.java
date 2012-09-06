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
package org.gvsig.raster.dataset.rmf;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.io.IOException;

import org.cresques.impl.cts.ProjectionPool;
import org.gvsig.raster.datastruct.Extent;
import org.gvsig.raster.datastruct.ViewPortData;
import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParserException;

/**
 * Clase de la que debe heredar cualquier clase que convierta objetos a XML
 * para ser guardados en ficheros rmf.
 *
 * 23-abr-2007
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public abstract class ClassSerializer implements IRmfBlock {

	/**
	 * Parsea un tag con un String como texto de la forma \<TAG\>String\</TAG\>
	 * @param parser         KXmlParser
	 * @param parseableTag   cadena de texto con el nombre del tag
	 * @param errorTags	Lista de tags que interrumpiran el proceso si son encontrados
	 * @return	valor double recuperado
	 * @throws XmlPullParserException
	 * @throws IOException
	 * @throws ParsingException
	 */
	protected String parserString(KXmlParser parser, String parseableTag, String[] errorTags) throws XmlPullParserException, IOException, ParsingException {
		String value = null;
		boolean end = false;
		boolean tagOk = false;
		int tag = parser.next();
		while (!end) {
			switch (tag) {
				case KXmlParser.END_DOCUMENT:
					if (value != null)
						return value;
					throw new ParsingException("No se ha encontrado el parametro " + parseableTag + " en el RMF");
				case KXmlParser.START_TAG:
					if (parser.getName() != null) {
						if (errorTags != null)
							for (int i = 0; i < errorTags.length; i++)
								if (parser.getName().compareTo(errorTags[i]) == 0)
									return null;
						if (parser.getName().compareTo(parseableTag) == 0)
							tagOk = true;
					}
					break;
				case KXmlParser.END_TAG:
					if (parser.getName().compareTo(parseableTag) == 0)
						end = true;
					break;
				case KXmlParser.TEXT:
					if (tagOk) {
						value = parser.getText();
						tagOk = false;
					}
					break;
			}
			if (!end)
				tag = parser.next();
		}
		return value;
	}

	/**
	 * Parsea un bloque que representa un Extent con la forma \<Bbox\><BR> \<Px\>-245.41842857142848\</Px\><BR> \<Py\>1783.3830816326529\</Py\><BR> \<WCWidth\>500.09399999999994\</WCWidth\><BR> \<WCHeight\>324.72771428571446\</WCHeight\><BR> \</Bbox\><BR>
	 * @param gp
	 * @param parser
	 * @param parseableTag Tag principal del Extent
	 * @param bbox lista de 4 elementos que representa los 4 tags minx, maxy,
	 *          ancho y alto
	 * @throws NumberFormatException
	 * @throws XmlPullParserException
	 * @throws IOException
	 * @throws ParsingException
	 */
	public Extent parseBoundingBox(KXmlParser parser, String parseableTag, String[] bbox) throws NumberFormatException, XmlPullParserException, IOException, ParsingException {
		boolean end = false;
		boolean tagOk = false;
		int tag = parser.next();
		Extent ext = null;
		while (!end) {
			switch (tag) {
				case KXmlParser.START_TAG:
					if (parser.getName() != null)
						if (parser.getName().compareTo(parseableTag) == 0)
							tagOk = true;
					break;
				case KXmlParser.END_TAG:
					if (parser.getName().compareTo(parseableTag) == 0)
						end = true;
					break;
				case KXmlParser.TEXT:
					if (tagOk) {
						double x = Double.valueOf(parserString(parser, bbox[0], null)).doubleValue();
						double y = Double.valueOf(parserString(parser, bbox[1], null)).doubleValue();
						double w = Double.valueOf(parserString(parser, bbox[2], null)).doubleValue();
						double h = Double.valueOf(parserString(parser, bbox[3], null)).doubleValue();
						ext = new Extent(x, y, x + w, y - h);
					}
					tagOk = false;
					break;
			}
			if (!end)
				tag = parser.next();
		}
		return ext;
	}

	/**
	 * Parsea un bloque correspondiente a un punto con la forma
	 * \<Pdim\><BR>
	 * \<Width\>140.0\</Width\><BR>
	 * \<Height\>140.0\</Height\><BR>
	 * \</Pdim\><BR>
	 * @param gp
	 * @param parser
	 * @param parseableTag
	 * @throws NumberFormatException
	 * @throws XmlPullParserException
	 * @throws IOException
	 * @throws ParsingException
	 */
	public Point2D parsePoint2D(KXmlParser parser, String parseableTag, String[] points) throws NumberFormatException, XmlPullParserException, IOException, ParsingException {
		boolean end = false;
		boolean tagOk = false;
		Point2D dim = null;
		int tag = parser.next();
		while (!end) {
			switch (tag) {
				case KXmlParser.START_TAG:
					if (parser.getName() != null)
						if (parser.getName().compareTo(parseableTag) == 0)
							tagOk = true;
					break;
				case KXmlParser.END_TAG:
					if (parser.getName().compareTo(parseableTag) == 0)
						end = true;
					break;
				case KXmlParser.TEXT:
					if (tagOk) {
						double x = Double.valueOf(parserString(parser, points[0], null)).doubleValue();
						double y = Double.valueOf(parserString(parser, points[1], null)).doubleValue();
						dim = new Point2D.Double(x, y);
						tagOk = false;
					}
					break;
			}
			if (!end)
				tag = parser.next();
		}
		return dim;
	}

	/**
	 * Parsea un viewport contenido en un punto de control
	 * @param gp
	 * @param parser
	 * @param parseableTag Tag correspondiente a la cabecera del viewport
	 * @param bbox lista de tags correspondientes al extent. El primero será la cabecera y los siguientes cuatro minx, maxy, ancho y alto
	 * @param size tags del tamaño en pixeles. El primero será la cabecera y los siguientes dos ancho y alto
	 * @param proj tag de la proyección
	 * @throws XmlPullParserException
	 * @throws IOException
	 * @throws ParsingException
	 */
	public ViewPortData parseViewPort(KXmlParser parser, String parseableTag, String[] bbox, String[] size, String proj) throws XmlPullParserException, IOException, ParsingException {
		boolean end = false;
		boolean begin = true;
		ViewPortData vp = new ViewPortData();
		vp.mat = new AffineTransform();

		int tag = parser.next();
		while (!end) {
			switch (tag) {
				case KXmlParser.START_TAG:
					if (parser.getName() != null)
						if (parser.getName().compareTo(parseableTag) == 0) {
							String project = parserString(parser, proj, null);
							Extent ext = parseBoundingBox(parser, bbox[0], new String[] { bbox[1], bbox[2], bbox[3], bbox[4] });
							Point2D dim = parsePoint2D(parser, size[0], new String[] { size[1], size[2] });
							vp.setExtent(ext);
							vp.pxSize = new Point2D.Double(dim.getX(), dim.getY());
							if (project != null && project != "")
								vp.setProjection(new ProjectionPool().get(project));
							begin = false;
						} else if (begin)
							return null;
					break;
				case KXmlParser.END_TAG:
					if (parser.getName().compareTo(parseableTag) == 0)
						end = true;
					break;
				case KXmlParser.TEXT:
					break;
			}
			if (!end)
				tag = parser.next();
		}
		return vp;
	}

	/**
	 * Convierte una lista de valores en un solo String en un array de enteros. El
	 * String debe representar una lista de numeros separados por espacios.
	 * <P>
	 * String v = "4 5 65 2 1"
	 * </P>
	 * <P>
	 * Se converiría en:
	 * </P>
	 * <P>
	 * int[] i = new int[]{4, 5, 65, 2, 1};
	 * </P>
	 * @param list Cadena con la lista de valores
	 * @return Array de valores long
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	protected int[] convertStringInIntArray(String list) throws XmlPullParserException, IOException {
		String[] sValues = list.split(" ");
		int[] iValues = new int[sValues.length];
		for (int i = 0; i < sValues.length; i++)
			try {
				iValues[i] = Integer.parseInt(sValues[i]);
			} catch (NumberFormatException e) {
				// iValues[i] vale cero
			}
		return iValues;
	}

	/**
	 * Convierte una lista de valores en un solo String en un array de double. El
	 * String debe representar una lista de numeros separados por espacios.
	 * <P>
	 * String v = "4.2 5 65.5 2 1"
	 * </P>
	 * <P>
	 * Se converiría en:
	 * </P>
	 * <P>
	 * double[] i = new double[]{4.2, 5, 65.5, 2, 1};
	 * </P>
	 * @param list Cadena con la lista de valores
	 * @return Array de valores double
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	protected double[] convertStringInDoubleArray(String list) throws XmlPullParserException, IOException {
		String[] sValues = list.split(" ");
		double[] dValues = new double[sValues.length];
		for (int i = 0; i < sValues.length; i++)
			try {
				dValues[i] = Double.parseDouble(sValues[i]);
			} catch (NumberFormatException e) {
				// dValues[i] vale cero
			}
		return dValues;
	}

	/**
	 * Añade al buffer la propiedad (String) especificada en los parámetros
	 * @param b Buffer
	 * @param prop Nombre de la propiedad (Tag)
	 * @param value Valor de la propiedad
	 */
	public void putProperty(StringBuffer b, String prop, String value, int level) {
		for (int i = 0; i < level; i++)
			b.append("\t");
		b.append("<" + prop + ">");
		b.append(value);
		b.append("</" + prop + ">\n");
	}

	/**
	 * Añade al buffer la propiedad (int) especificada en los parámetros
	 * @param b Buffer
	 * @param prop Nombre de la propiedad (Tag)
	 * @param value Valor de la propiedad
	 */
	public void putProperty(StringBuffer b, String prop, int value, int level) {
		putProperty(b, prop, String.valueOf(value), level);
	}

	/**
	 * Añade al buffer la propiedad (double) especificada en los parámetros
	 * @param b Buffer
	 * @param prop Nombre de la propiedad (Tag)
	 * @param value Valor de la propiedad
	 */
	public void putProperty(StringBuffer b, String prop, double value, int level) {
		putProperty(b, prop, String.valueOf(value), level);
	}
}