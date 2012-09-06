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

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.gvsig.raster.dataset.RasterDataset;
import org.gvsig.raster.dataset.rmf.ClassSerializer;
import org.gvsig.raster.dataset.rmf.ParsingException;
import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.extensionpoint.ExtensionPoint;
import org.gvsig.tools.extensionpoint.ExtensionPointManager;
import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParserException;
/**
 * <P>
 * Clase para convertir a XML la información geo del raster y obtener esta información desde XML.
 * Esta clase implementa el interfaz IRmfBlock con los métodos de escritura y
 * lectura. Estos serán utilizados por el gestor de ficheros RMF para escribir y
 * leer datos.
 * </P>
 * <P>
 * La estructura XML es la siguiente:
 * </P>
 * <P>
 * \<FLyrGeoRaster xmlns="http://www.gvsig.org"\><BR>
 * \<Extent\><BR>
 * \<X\>4.325548573549895\</X\><BR>
 * \<Y\>1952.062652448396\</Y\><BR>
 * \<RotationX\>-0.23070178571428576\</RotationX\><BR>
 * \<RotationY\>-0.1655108150921638\</RotationY\><BR>
 * \<PixelSizeX\>0.04363559968817176\</PixelSizeX\><BR>
 * \<PixelSizeY\>-0.43297650488377315\</PixelSizeY\><BR>
 * \<Width\>98.92190449308538\</Width\><BR>
 * \<Height\>-746.0185179147411\</Height\><BR>
 * \</Extent\><BR>
 * \<Dimension\><BR>
 * \<ImagePxWidth\>2268\</ImagePxWidth\><BR>
 * \<ImagePxHeight\>1724\</ImagePxHeight\><BR>
 * \</Dimension\><BR>
 * \</FLyrGeoRaster\><BR>
 * </P>
 *
 * @version 23/04/2007
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class GeoInfoRmfSerializer extends ClassSerializer {

	//TAGS
	public static final String MAIN_TAG = "FLyrGeoRaster";
	public static final String EXTENT   = "Extent";
	public static final String X        = "X";
	public static final String Y        = "Y";
	public static final String ROTX     = "RotationX";
	public static final String ROTY     = "RotationY";
	public static final String PSX      = "PixelSizeX";
	public static final String PSY      = "PixelSizeY";
	public static final String W        = "Width";
	public static final String H        = "Height";
	public static final String DIM      = "Dimension";
	public static final String IMGW     = "ImagePxWidth";
	public static final String IMGH     = "ImagePxHeight";

	private RasterDataset      dataset  = null;
	private AffineTransform    at       = null;
	private Point2D            dim      = null;

	/**
	 * Registra GeoInfoRmfSerializer en los puntos de extension de Serializer
	 */
	public static void register() {
		ExtensionPointManager extensionPoints =ToolsLocator.getExtensionPointManager();
		ExtensionPoint point=extensionPoints.get("Serializer");
		point.append("GeoInfo", "", GeoInfoRmfSerializer.class);
	}

	/**
	 * Constructor. Asigna la tabla a serializar.
	 * @param ColorTable tabla a convertir en XML
	 */
	public GeoInfoRmfSerializer(RasterDataset dataset) {
		this.dataset = dataset;
	}

	/**
	 * Constructor.
	 */
	public GeoInfoRmfSerializer(AffineTransform at, Point2D dim) {
		this.at = at;
		this.dim = dim;
	}

	/**
	 * Constructor.
	 */
	public GeoInfoRmfSerializer() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.io.rmf.IRmfBlock#read(java.lang.String)
	 */
	public void read(String xml) throws ParsingException {
		double x = 0, y = 0, rotX = 0, rotY = 0, psX = 0, psY = 0;
		double imgW = 0, imgH = 0;

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
								x = Double.parseDouble(parserString(parser, X, null));
								y = Double.parseDouble(parserString(parser, Y, null));
								rotX = Double.parseDouble(parserString(parser, ROTX, null));
								rotY = Double.parseDouble(parserString(parser, ROTY, null));
								psX = Double.parseDouble(parserString(parser, PSX, null));
								psY = Double.parseDouble(parserString(parser, PSY, null));
								Double.parseDouble(parserString(parser, W, null));
								Double.parseDouble(parserString(parser, H, null));
								imgW = Double.parseDouble(parserString(parser, IMGW, null));
								imgH = Double.parseDouble(parserString(parser, IMGH, null));
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

		dim = new Point2D.Double(imgW, imgH);
		at = new AffineTransform(psX, rotY, rotX, psY, x, y);
		if (dataset != null)
			dataset.setAffineTransform(at);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.io.rmf.IRmfBlock#write()
	 */
	public String write() {
		StringBuffer b = new StringBuffer();

		if(dataset != null) {
			at = dataset.getAffineTransform();
			dim = new Point2D.Double(dataset.getWidth(), dataset.getHeight());
		}

		b.append("<" + MAIN_TAG + ">\n");
		b.append("\t<" + EXTENT + ">\n");
		putProperty(b, X, at.getTranslateX(), 3);
		putProperty(b, Y, at.getTranslateY(), 3);
		putProperty(b, ROTX, at.getShearX(), 3);
		putProperty(b, ROTY, at.getShearY(), 3);
		double wd = 0;
		double hd = 0;
		if(dataset != null) {
			wd = (dataset.getExtent().getMax().getX() - dataset.getExtent().getMin().getX());
			hd = (dataset.getExtent().getMax().getY() - dataset.getExtent().getMin().getY());
		}
		putProperty(b, PSX, at.getScaleX(), 3);
		putProperty(b, PSY, at.getScaleY(), 3);
		putProperty(b, W, wd, 3);
		putProperty(b, H, hd, 3);
		b.append("\t</" + EXTENT + ">\n");
		b.append("\t<" + DIM + ">\n");
		putProperty(b, IMGW, dim.getX(), 3);
		putProperty(b, IMGH, dim.getY(), 3);
		b.append("\t</" + DIM + ">\n");
		b.append("</" + MAIN_TAG + ">\n");
		return b.toString();
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.io.rmf.IRmfBlock#getResult()
	 */
	public Object getResult() {
		return dataset;
	}

	/*
	 *  (non-Javadoc)
	 * @see org.gvsig.raster.dataset.io.rmf.IRmfBlock#getMainTag()
	 */
	public String getMainTag() {
		return MAIN_TAG;
	}
}
