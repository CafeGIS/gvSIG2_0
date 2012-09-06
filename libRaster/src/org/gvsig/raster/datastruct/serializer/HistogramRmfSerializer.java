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

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.gvsig.raster.dataset.rmf.ClassSerializer;
import org.gvsig.raster.dataset.rmf.ParsingException;
import org.gvsig.raster.datastruct.Histogram;
import org.gvsig.raster.datastruct.HistogramClass;
import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.extensionpoint.ExtensionPoint;
import org.gvsig.tools.extensionpoint.ExtensionPointManager;
import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParserException;
/**
 * <P>
 * Clase para convertir a XML un histograma y obtener el histograma desde XML.
 * Esta clase implementa el interfaz IRmfBlock con los métodos de escritura y
 * lectura. Estos serán utilizados por el gestor de ficheros RMF para escribir y
 * leer datos.
 * </P>
 * <P>
 * La estructura XML de un histograma es la siguiente:
 * </P>
 * <P>
 *\<Histogram\> <BR>
 *&nbsp;\<ClassInterval\>1.0\</ClassInterval\><BR>
 *&nbsp;\<Min\>0.0\</Min\><BR>
 *&nbsp;\<Max\>255.0\</Max\><BR>
 *&nbsp;\<BandCount\>3\</BandCount\><BR>
 *&nbsp;\<Band\><BR>
 *&nbsp;&nbsp;\<Values\>13 2 0 ... 2 1 6\</Values\><BR>
 *&nbsp;\</Band\><BR>
 *&nbsp;\<Band\><BR>
 *&nbsp;&nbsp;\<Values\>6 2 2 ... 3 2 8\</Values\><BR>
 *&nbsp;\</Band\><BR>
 *&nbsp;\<Band\><BR>
 *&nbsp;&nbsp;\<Values\>16 1 1 ... 1 1 9\</Values\><BR>
 *&nbsp;\</Band\><BR\>
 *\</Histogram\><BR>
 *\</P\>
 *
 * @version 23/04/2007
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class HistogramRmfSerializer extends ClassSerializer {
	//TAGS
	public static final String MAIN_TAG   = "Histogram";
	public static final String MIN        = "Min";
	public static final String MAX        = "Max";
	public static final String BAND       = "Band";
	public static final String VALUES     = "Values";
	public static final String BAND_COUNT = "BandCount";
	public static final String DATA_TYPE  = "DataType";

	private Histogram          histogram  = null;

	/**
	 * Registra HistogramRmfSerializer en los puntos de extension de Serializer
	 */
	public static void register() {
		ExtensionPointManager extensionPoints =ToolsLocator.getExtensionPointManager();
		ExtensionPoint point=extensionPoints.get("Serializer");
		point.append("Histogram", "", HistogramRmfSerializer.class);
	}

	/**
	 * Constructor. Asigna el histograma a serializar.
	 * @param Histogram histograma a convertir en XML
	 */
	public HistogramRmfSerializer(Histogram hist) {
		this.histogram = hist;
	}

	/**
	 * Constructor.
	 */
	public HistogramRmfSerializer() {}

	/**
	 * Convierte una lista de valores en un solo String en un array de long
	 * @param list Cadena con la lista de valores
	 * @return Array de valores long
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	private long[] parseLongList(String list) throws XmlPullParserException, IOException {
		String[] sValues = list.split(" ");
		long[] dValues = new long[sValues.length];
		for (int i = 0; i < sValues.length; i++)
			dValues[i] = Long.parseLong(sValues[i]);
		return dValues;
	}

	/**
	 * Parsea el tag Band para extraer la lista de valores (Values) asociada a una banda.
	 * @param parser KXmlParser
	 * @return Array de long
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	private long[] parserHistogramListValues(KXmlParser parser)  throws XmlPullParserException, IOException {
		long[] valueList = null;
		boolean end = false;
		boolean tagOk = false;
		int tag = parser.next();
		while (!end) {
			switch (tag) {
				case KXmlParser.START_TAG:
					if (parser.getName() != null) {
						if (parser.getName().compareTo(VALUES) == 0)
							tagOk = true;
					}
					break;
				case KXmlParser.END_TAG:
					if (parser.getName().compareTo(BAND) == 0)
						end = true;
					break;
				case KXmlParser.TEXT:
					if (tagOk) {
						valueList = parseLongList(parser.getText());
						tagOk = false;
					}
					break;
			}
			if (!end)
				tag = parser.next();
		}
		return valueList;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.io.rmf.IRmfBlock#read(java.lang.String)
	 */
	public void read(String xml) throws ParsingException {
//		double interval = 0D;
		double min = 0D;
		double max = 0D;
		int numBands = 0;
		int dataType = 0;
		long[][] values = null;

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
							if (parser.getName() != null) {
								if (parser.getName().compareTo(MAIN_TAG) == 0) {
									numBands = (int) Double.parseDouble(parserString(parser, BAND_COUNT, null));
									dataType = Integer.parseInt(parserString(parser, DATA_TYPE, null));
									values = new long[numBands][];
									for (int iBand = 0; iBand < numBands; iBand++) {
										min = Double.parseDouble(parserString(parser, MIN, null));
										max = Double.parseDouble(parserString(parser, MAX, null));
										values[iBand] = parserHistogramListValues(parser);
									}
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

		double[] mins = new double[numBands];
		double[] maxs = new double[numBands];
		for (int i = 0; i < numBands; i++) {
			mins[i] = min;
			maxs[i] = max;
		}
		histogram = new Histogram(numBands, mins, maxs, dataType);
		histogram.setTable(values);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.io.rmf.IRmfBlock#write()
	 */
	public String write() {
		StringBuffer b = new StringBuffer();
		HistogramClass[][] c = histogram.getHistogram();

		b.append("<" + MAIN_TAG + ">\n");

		putProperty(b, BAND_COUNT, histogram.getNumBands(), 1);
		putProperty(b, DATA_TYPE, histogram.getDataType(), 1);

		for (int iBand = 0; iBand < histogram.getNumBands(); iBand++) {
			b.append("\t<" + BAND + ">\n");
			putProperty(b, MIN, c[iBand][0].getMin(), 2);
			putProperty(b, MAX, c[iBand][c[iBand].length - 1].getMin(), 2);
			b.append("\t\t<" + VALUES + ">");
			for (int iValues = 0; iValues < histogram.getNumValues(); iValues++) {
				if (iValues != 0)
					b.append(" ");
				b.append((long) histogram.getHistogramValueByPos(iBand, iValues));
			}
			b.append("</" + VALUES + ">\n");
			b.append("\t</" + BAND + ">\n");
		}
		b.append("</" + MAIN_TAG + ">\n");
		return b.toString();
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.io.rmf.IRmfBlock#getResult()
	 */
	public Object getResult() {
		return (Histogram) histogram;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.io.rmf.IRmfBlock#getMainTag()
	 */
	public String getMainTag() {
		return MAIN_TAG;
	}
}