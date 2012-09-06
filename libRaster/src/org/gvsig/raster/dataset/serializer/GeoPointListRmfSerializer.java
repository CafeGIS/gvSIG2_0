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

import java.awt.geom.Point2D;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;

import org.gvsig.raster.dataset.rmf.ClassSerializer;
import org.gvsig.raster.dataset.rmf.ParsingException;
import org.gvsig.raster.datastruct.GeoPoint;
import org.gvsig.raster.datastruct.GeoPointList;
import org.gvsig.raster.datastruct.ViewPortData;
import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.extensionpoint.ExtensionPoint;
import org.gvsig.tools.extensionpoint.ExtensionPointManager;
import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParserException;
/**
 * <P>
 * Clase para convertir a XML la información de una lista de puntos de control y obtener esta información desde XML.
 * Esta clase implementa el interfaz IRmfBlock con los métodos de escritura y
 * lectura. Estos serán utilizados por el gestor de ficheros RMF para escribir y
 * leer datos.
 * </P>
 * <P>
 * La estructura XML es la siguiente:
 * </P>
 * <P>
 * \<GeoPoint n="0"\><BR>
 * \<PixelX\>17.85306122448992\</PixelX\><BR>
 * \<PixelY\>15.632653061225255\</PixelY\><BR>
 * \<MapX\>4.62857142857149\</MapX\><BR>
 * \<MapY\>1945.74693877551\</MapY\><BR>
 * \<Active\>true\</Active\><BR>
 * \<LeftCenterPoint\><BR>
 * \<X\>5.104234119122292\</X\><BR>
 * \<Y\>1945.2980070494339\</Y\><BR>
 * \</LeftCenterPoint\><BR>
 * \<RightCenterPoint\><BR>
 * \<X\>4.62857142857149\</X\><BR>
 * \<Y\>1945.74693877551\</Y\><BR>
 * \</RightCenterPoint\><BR>
 * \<LeftViewPort\><BR>
 * \<Proj\>EPSG:23030\</Proj\><BR>
 * \<Bbox\><BR>
 * \<Px\>-244.94276588087763\</Px\><BR>
 * \<Py\>1782.9341499065767\</Py\><BR>
 * \<WCWidth\>500.094\</WCWidth\><BR>
 * \<WCHeight\>324.7277142857142\</WCHeight\><BR>
 * \</Bbox\><BR>
 * \<Pdim\><BR>
 * \<Width\>140\</Width\><BR>
 * \<Height\>140</Height\><BR>
 * \</Pdim\><BR>
 * \<Zoom\>1.0\</Zoom\><BR>
 * \</LeftViewPort\><BR>
 * \<RightViewPort\><BR>
 * \<Proj\>EPSG:23030\</Proj\><BR>
 * \<Bbox\><BR>
 * \<Px\>-245.41842857142848\</Px\><BR>
 * \<Py\>1783.3830816326529\</Py\><BR>
 * \<WCWidth\>500.09399999999994\</WCWidth\><BR>
 * \<WCHeight\>324.72771428571446\</WCHeight\><BR>
 * \</Bbox\><BR>
 * \<Pdim\><BR>
 * \<Width\>140\</Width\><BR>
 * \<Height\>140\</Height\><BR>
 * \</Pdim\><BR>
 * \<Zoom\>1.0\</Zoom\><BR>
 * \</RightViewPort\><BR>
 * \</GeoPoint\><BR>
 * </P>
 *
 * @version 23/04/2007
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class GeoPointListRmfSerializer extends ClassSerializer {
	//TAGS
	public static final String MAIN_TAG = "GeoPoints";
	public static final String GEOPOINT = "GeoPoint";
	public static final String PIXELX   = "PixelX";
	public static final String PIXELY   = "PixelY";
	public static final String MAPX     = "MapX";
	public static final String MAPY     = "MapY";
	public static final String ACTIVE   = "Active";
	public static final String LEFTCP   = "LeftCenterPoint";
	public static final String RIGHTCP  = "RightCenterPoint";
	public static final String X        = "X";
	public static final String Y        = "Y";
	public static final String LEFTVP   = "LeftViewPort";
	public static final String RIGHTVP  = "RightViewPort";
	public static final String PROJ     = "Proj";
	public static final String BBOX     = "Bbox";
	public static final String PX       = "Px";
	public static final String PY       = "Py";
	public static final String WCWIDTH  = "WCWidth";
	public static final String WCHEIGHT = "WCHeight";
	public static final String PDIM     = "Pdim";
	public static final String WIDTH    = "Width";
	public static final String HEIGHT   = "Height";
	public static final String ZOOM     = "Zoom";
	public static final String LYR      = "Layer";

	private GeoPointList  gpList = null;
	private ViewPortData viewPort = null;

	/**
	 * Registra GeoPointRmfSerializer en los puntos de extension de Serializer
	 */
	public static void register() {
		ExtensionPointManager extensionPoints =ToolsLocator.getExtensionPointManager();
		ExtensionPoint point=extensionPoints.get("Serializer");
		point.append("GeoPoint", "", GeoPointListRmfSerializer.class);
	}

	/**
	 * Constructor. Asigna la lista de puntos a serializar.
	 * @param ColorTable tabla a convertir en XML
	 */
	public GeoPointListRmfSerializer(GeoPointList gp) {
		this.gpList = gp;
	}

	/**
	 * Constructor. Asigna la lista de puntos a serializar y el viewport.
	 * @param ColorTable tabla a convertir en XML
	 */
	public GeoPointListRmfSerializer(GeoPointList gp, ViewPortData vp) {
		this.gpList = gp;
		this.viewPort = vp;
	}

	/**
	 * Constructor.
	 */
	public GeoPointListRmfSerializer() {
	}

	/**
	 * Parsea un bloque correspondiente a un punto central
	 * @param gp
	 * @param parser
	 * @param parseableTag
	 * @throws NumberFormatException
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	/*private void parserCenterPoint(GeoPoint gp, KXmlParser parser, String parseableTag) throws NumberFormatException, XmlPullParserException, IOException {
		boolean end = false;
		boolean begin = true;
		boolean tagOk = false;
		int tag = parser.next();
		while (!end) {
			switch(tag) {
			case KXmlParser.START_TAG:
				if(parser.getName() != null){
					if (parser.getName().compareTo(parseableTag) == 0) {
						tagOk = true;
						begin = false;
					} else {
						if(begin)//Condicion de salida
							return;
					}
				}
				break;
			case KXmlParser.END_TAG:
				if (parser.getName().compareTo(parseableTag) == 0)
					end = true;
				break;
			case KXmlParser.TEXT:
				if(tagOk) {
					if(parseableTag.compareTo("LeftCenterPoint") == 0) {
						double x = Double.valueOf(parserString(parser, X, null)).doubleValue();
						double y = Double.valueOf(parserString(parser, Y, null)).doubleValue();
						gp.leftCenterPoint = new Point2D.Double(x, y);
					}
					if(parseableTag.compareTo("RightCenterPoint") == 0) {
						double x = Double.valueOf(parserString(parser, X, null)).doubleValue();
						double y = Double.valueOf(parserString(parser, Y, null)).doubleValue();
						gp.rightCenterPoint = new Point2D.Double(x, y);
					}
					tagOk = false;
				}
				break;
			}
			if (!end)
				tag = parser.next();
		}
	}*/

	/**
	 * Parsea un bloque correspondiente a un punto central
	 * @param gp
	 * @param parser
	 * @param parseableTag
	 * @throws NumberFormatException
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	/*private void parserBoundingBox(GeoPoint gp, KXmlParser parser, String parseableTag) throws NumberFormatException, XmlPullParserException, IOException {
		boolean end = false;
		boolean tagOk = false;
		int tag = parser.next();
		while (!end) {
			switch(tag) {
			case KXmlParser.START_TAG:
				if(parser.getName() != null){
					if (parser.getName().compareTo(BBOX) == 0)
						tagOk = true;
				}
				break;
			case KXmlParser.END_TAG:
				if (parser.getName().compareTo(BBOX) == 0)
					end = true;
				break;
			case KXmlParser.TEXT:
				if(tagOk) {
					if(parseableTag.compareTo("LeftViewPort") == 0) {
						double x = Double.valueOf(parserString(parser, PX, null)).doubleValue();
						double y = Double.valueOf(parserString(parser, PY, null)).doubleValue();
						double w = Double.valueOf(parserString(parser, WCWIDTH, null)).doubleValue();
						double h = Double.valueOf(parserString(parser, WCHEIGHT, null)).doubleValue();
						gp.leftViewPort.setExtent(new Extent(x, y, x + w, y - h));
					}
					if(parseableTag.compareTo("RightViewPort") == 0) {
						double x = Double.valueOf(parserString(parser, PX, null)).doubleValue();
						double y = Double.valueOf(parserString(parser, PY, null)).doubleValue();
						double w = Double.valueOf(parserString(parser, WCWIDTH, null)).doubleValue();
						double h = Double.valueOf(parserString(parser, WCHEIGHT, null)).doubleValue();
						gp.rightViewPort.setExtent(new Extent(x, y, x + w, y - h));
					}
					tagOk = false;
				}
				break;
			}
			if (!end)
				tag = parser.next();
		}
	}*/

	/**
	 * Parsea un bloque correspondiente a un punto central
	 * @param gp
	 * @param parser
	 * @param parseableTag
	 * @throws NumberFormatException
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	/*private void parserDimension(GeoPoint gp, KXmlParser parser, String parseableTag) throws NumberFormatException, XmlPullParserException, IOException {
		boolean end = false;
		boolean tagOk = false;
		int tag = parser.next();
		while (!end) {
			switch(tag) {
			case KXmlParser.START_TAG:
				if(parser.getName() != null){
					if (parser.getName().compareTo(PDIM) == 0)
						tagOk = true;
				}
				break;
			case KXmlParser.END_TAG:
				if (parser.getName().compareTo(PDIM) == 0)
					end = true;
				break;
			case KXmlParser.TEXT:
				if(tagOk) {
					if(parseableTag.compareTo("LeftViewPort") == 0) {
						double x = Double.valueOf(parserString(parser, WIDTH, null)).doubleValue();
						double y = Double.valueOf(parserString(parser, HEIGHT, null)).doubleValue();
						//gp.leftViewPort.setSize(x, y);
					}
					if(parseableTag.compareTo("RightViewPort") == 0) {
						double x = Double.valueOf(parserString(parser, WIDTH, null)).doubleValue();
						double y = Double.valueOf(parserString(parser, HEIGHT, null)).doubleValue();
						//gp.rightViewPort.setSize(x, y);
					}
					tagOk = false;
				}
				break;
			}
			if (!end)
				tag = parser.next();
		}
	}*/

	/**
	 * Parsea un viewport contenido en un punto de control
	 * @param gp
	 * @param parser
	 * @param parseableTag
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	/*private void parserViewPort(GeoPoint gp, KXmlParser parser, String parseableTag) throws XmlPullParserException, IOException {
		boolean end = false;
		if(parseableTag.compareTo("LeftViewPort") == 0) {
			gp.leftViewPort = new ViewPortData();
			gp.leftViewPort.mat = new AffineTransform();
		}
		if(parseableTag.compareTo("RightViewPort") == 0) {
			gp.rightViewPort = new ViewPortData();
			gp.rightViewPort.mat = new AffineTransform();
		}
		int tag = parser.next();
		while (!end) {
			switch(tag) {
			case KXmlParser.START_TAG:
				if(parser.getName() != null){
					if (parser.getName().compareTo(parseableTag) == 0) {
						String proj = parserString(parser, PROJ, null);
						parserBoundingBox(gp, parser, parseableTag);
						parserDimension(gp, parser, parseableTag);
						if(parseableTag.compareTo("LeftViewPort") == 0)
							gp.zoomLeft = Double.valueOf(parserString(parser, ZOOM, null)).doubleValue();
						if(parseableTag.compareTo("RightViewPort") == 0)
							gp.zoomRight = Double.valueOf(parserString(parser, ZOOM, null)).doubleValue();
					}
				}
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
	}*/

	/**
	 * Parsea un punto de control completo
	 * @param gp
	 * @param parser
	 * @param xml
	 * @param tag
	 * @throws XmlPullParserException
	 * @throws IOException
	 * @throws ParsingException
	 * @throws NumberFormatException
	 */
	public void parserGeoPoint(GeoPoint gp, KXmlParser parser, String xml, int tag) throws XmlPullParserException, IOException, NumberFormatException, ParsingException  {
		double px = Double.parseDouble(parserString(parser, PIXELX, null));
		double py = Double.parseDouble(parserString(parser, PIXELY, null));
		gp.pixelPoint = new Point2D.Double(px, py);
		double mx = Double.parseDouble(parserString(parser, MAPX, null));
		double my = Double.parseDouble(parserString(parser, MAPY, null));
		gp.mapPoint = new Point2D.Double(mx, my);
		gp.active = Boolean.valueOf(parserString(parser, ACTIVE, null)).booleanValue();
		/*parserCenterPoint(gp, parser, "LeftCenterPoint");
		parserCenterPoint(gp, parser, "RightCenterPoint");
		parserViewPort(gp, parser, "LeftViewPort");
		parserViewPort(gp, parser, "RightViewPort");*/
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.io.rmf.IRmfBlock#read(java.lang.String)
	 */
	public void read(String xml) throws ParsingException {
		ArrayList list = new ArrayList();
		GeoPoint gp = null;
		boolean init = false;
		int nPoint = -1;
		boolean tagOk = false;
		boolean vpRead = false;

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
							if (parser.getName().compareTo(MAIN_TAG) == 0)
								init = true;
							if(init) {
								if(!vpRead) {
									viewPort = parseViewPort(parser, LYR, new String[]{BBOX, PX, PY, WCWIDTH, WCHEIGHT}, new String[]{PDIM, WIDTH, HEIGHT}, PROJ);
									vpRead = true;
								}
								if (parser.getName().compareTo(GEOPOINT) == 0) {
									gp = new GeoPoint();
									tagOk = true;
									nPoint++;
								}
							}
							break;
						case KXmlParser.END_TAG:
							break;
						case KXmlParser.TEXT:
							if(tagOk) {
								parserGeoPoint(gp, parser, xml, tag);
								tagOk = false;
								list.add(gp);
							}
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

		gpList = new GeoPointList();
		for (int i = 0; i < list.size(); i++)
			gpList.add((GeoPoint) list.get(i));
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.io.rmf.IRmfBlock#write()
	 */
	public String write() {
		StringBuffer b = new StringBuffer();

		b.append("<" + MAIN_TAG + ">\n");
		/*b.append("\t<" + LYR + ">\n");
		if(viewPort != null && viewPort.pxSize != null) {
			String proj = "";
			if(viewPort.getProjection() != null)
				proj = viewPort.getProjection().getAbrev();
			putProperty(b, PROJ, proj, 3);
			b.append("\t\t\t<" + BBOX + ">\n");
			putProperty(b, PX, viewPort.getExtent().getMin().getX(), 4);
			putProperty(b, PY, viewPort.getExtent().getMax().getY(), 4);
			putProperty(b, WCWIDTH, viewPort.getExtent().width(), 4);
			putProperty(b, WCHEIGHT, viewPort.getExtent().height(), 4);
			b.append("\t\t\t</" + BBOX + ">\n");
			b.append("\t\t\t<" + PDIM + ">\n");
			putProperty(b, WIDTH, viewPort.pxSize.getX(), 4);
			putProperty(b, HEIGHT, viewPort.pxSize.getY(), 4);
			b.append("\t\t\t</" + PDIM + ">\n");
		} else {
			putProperty(b, PROJ, "", 3);
			b.append("\t\t\t<" + BBOX + ">\n");
			putProperty(b, PX, 0.0, 4);
			putProperty(b, PY, 0.0, 4);
			putProperty(b, WCWIDTH, 0.0, 4);
			putProperty(b, WCHEIGHT, 0.0, 4);
			b.append("\t\t\t</" + BBOX + ">\n");
			b.append("\t\t\t<" + PDIM + ">\n");
			putProperty(b, WIDTH, 0.0, 4);
			putProperty(b, HEIGHT, 0.0, 4);
			b.append("\t\t\t</" + PDIM + ">\n");
		}
		b.append("\t</" + LYR + ">\n");*/
		for (int i = 0; i < gpList.size(); i++) {
			GeoPoint point = gpList.get(i);
			b.append("\t<" + GEOPOINT + " n=\"" + i + "\">\n");
			putProperty(b, PIXELX, point.pixelPoint.getX(), 2);
			putProperty(b, PIXELY, point.pixelPoint.getY(), 2);
			putProperty(b, MAPX, point.mapPoint.getX(), 2);
			putProperty(b, MAPY, point.mapPoint.getY(), 2);
			putProperty(b, ACTIVE, String.valueOf(point.active), 2);

			/*if(point.leftCenterPoint != null) {
				b.append("\t\t<" + LEFTCP + ">\n");
				putProperty(b, X, point.leftCenterPoint.getX(), 3);
				putProperty(b, Y, point.leftCenterPoint.getY(), 3);
				b.append("\t\t</" + LEFTCP + ">\n");
			}

			if(point.rightCenterPoint != null) {
				b.append("\t\t<" + RIGHTCP + ">\n");
				putProperty(b, X, point.rightCenterPoint.getX(), 3);
				putProperty(b, Y, point.rightCenterPoint.getY(), 3);
				b.append("\t\t</" + RIGHTCP + ">\n");
			}

			String proj = "";

			if(point.leftViewPort != null) {
				b.append("\t\t<" + LEFTVP + ">\n");

				if(point.leftViewPort.getProjection() != null)
					proj = point.leftViewPort.getProjection().getAbrev();
				putProperty(b, PROJ, proj, 3);
				b.append("\t\t\t<" + BBOX + ">\n");
				putProperty(b, PX, point.leftViewPort.getExtent().getMin().getX(), 4);
				putProperty(b, PY, point.leftViewPort.getExtent().getMax().getY(), 4);
				putProperty(b, WCWIDTH, point.leftViewPort.getExtent().width(), 4);
				putProperty(b, WCHEIGHT, point.leftViewPort.getExtent().height(), 4);
				b.append("\t\t\t</" + BBOX + ">\n");

				b.append("\t\t\t<" + PDIM + ">\n");
				putProperty(b, WIDTH, GeoPoint.WIDTH_WINDOW, 4);
				putProperty(b, HEIGHT, GeoPoint.HEIGHT_WINDOW, 4);
				b.append("\t\t\t</" + PDIM + ">\n");
				putProperty(b, ZOOM, point.zoomLeft, 3);

				b.append("\t\t</" + LEFTVP + ">\n");
			}

			if(point.rightViewPort != null) {
				b.append("\t\t<" + RIGHTVP + ">\n");

				proj = "";
				if(point.rightViewPort.getProjection() != null)
					proj = point.rightViewPort.getProjection().getAbrev();
				putProperty(b, PROJ, proj, 3);
				b.append("\t\t\t<" + BBOX + ">\n");
				putProperty(b, PX, point.rightViewPort.getExtent().getMin().getX(), 4);
				putProperty(b, PY, point.rightViewPort.getExtent().getMax().getY(), 4);
				putProperty(b, WCWIDTH, point.rightViewPort.getExtent().width(), 4);
				putProperty(b, WCHEIGHT, point.rightViewPort.getExtent().height(), 4);
				b.append("\t\t\t</" + BBOX + ">\n");

				b.append("\t\t\t<" + PDIM + ">\n");
				putProperty(b, WIDTH, GeoPoint.WIDTH_WINDOW, 4);
				putProperty(b, HEIGHT, GeoPoint.HEIGHT_WINDOW, 4);
				b.append("\t\t\t</" + PDIM + ">\n");
				putProperty(b, ZOOM, point.zoomRight, 3);

				b.append("\t\t</" + RIGHTVP + ">\n");
			}*/

			b.append("\t</" + GEOPOINT + ">\n");
		}
		b.append("</" + MAIN_TAG + ">\n");
		return b.toString();
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.io.rmf.IRmfBlock#getResult()
	 */
	public Object getResult() {
		return gpList;
	}

	/**
	 * Obtiene el viewPortData actual asignado o cargado desde el fichero XML
	 * @return ViewPortData
	 */
	public ViewPortData getViewPort() {
		return viewPort;
	}

	/*
	 *  (non-Javadoc)
	 * @see org.gvsig.raster.dataset.io.rmf.IRmfBlock#getMainTag()
	 */
	public String getMainTag() {
		return MAIN_TAG;
	}
}
