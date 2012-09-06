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

import org.gvsig.raster.dataset.properties.DatasetStatistics;
import org.gvsig.raster.dataset.rmf.ClassSerializer;
import org.gvsig.raster.dataset.rmf.ParsingException;
import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.extensionpoint.ExtensionPoint;
import org.gvsig.tools.extensionpoint.ExtensionPointManager;
import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParserException;
/**
 * <P>
 * Clase para convertir a XML las estadisticas y obtener las estadísticas desde XML.
 * Esta clase implementa el interfaz IRmfBlock con los métodos de escritura y
 * lectura. Estos serán utilizados por el gestor de ficheros RMF para escribir y
 * leer datos.
 * </P>
 * <P>
 * La estructura XML de las estadísticas es la siguiente:
 * </P>
 * <P>
 *\<Statistics\> <BR>
 *&nbsp;\<Max\>0\</Max\><BR>
 *&nbsp;\<SecondMax\>0\</SecondMax\><BR>
 *&nbsp;\<Min\>0\</Min\><BR>
 *&nbsp;\<SecondMin\>0\</SecondMin\><BR>
 *&nbsp;\<Maximun\>0\</Maximun\><BR>
 *&nbsp;\<Minimun\>0\</Minimun\><BR>
 *&nbsp;\<Mean\>0\</Mean\><BR>
 *&nbsp;\<Variance\>0\</Variance\><BR>
 *&nbsp;\<BandCount\>0\</BandCount\><BR>
 *&nbsp;\<TailTrim\>1.0:23 2.0:34 .... \</TailTrim\><BR>
 *\</Statistics\><BR>
 *</P>
 *
 * @version 23/04/2007
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class StatisticsRmfSerializer extends ClassSerializer {
	// TAGS
	public static final String MAIN_TAG  = "Statistics";
	public static final String BAND      = "Band";
	public static final String MIN       = "Min";
	public static final String MAX       = "Max";
	public static final String SNDMIN    = "SecondMin";
	public static final String SNDMAX    = "SecondMax";
	public static final String MINRGB    = "MinRGB";
	public static final String MAXRGB    = "MaxRGB";
	public static final String SNDMINRGB = "SecondMinRGB";
	public static final String SNDMAXRGB = "SecondMaxRGB";
	public static final String MAXIMUN   = "Maximun";
	public static final String MINIMUN   = "Minimun";
	public static final String MEAN      = "Mean";
	public static final String VARIANCE  = "Variance";
	public static final String BANDCOUNT = "BandCount";
	public static final String TAILTRIM  = "TailTrim";
	public static final String KEY       = "Key";
	public static final String VALUE     = "Value";

	private DatasetStatistics  stat      = null;

	/**
	 * Registra StatisticsRmfSerializer en los puntos de extension de Serializer
	 */
	public static void register() {
		ExtensionPointManager extensionPoints =ToolsLocator.getExtensionPointManager();
		ExtensionPoint point=extensionPoints.get("Serializer");
		point.append("Statistics", "", StatisticsRmfSerializer.class);
	}

	/**
	 * Constructor. Asigna la tabla a serializar.
	 * @param ColorTable tabla a convertir en XML
	 */
	public StatisticsRmfSerializer(DatasetStatistics stat) {
		if (stat != null)
			stat.setCalculated(false);

		this.stat = stat;
	}

	/**
	 * Devuelve el objeto estadisticas de un dataset, en caso de no existir, lo crea.
	 * @return
	 */
	private DatasetStatistics getDatasetStatistics() {
		if (stat == null)
			stat = new DatasetStatistics(null);

		return stat;
	}

	/**
	 * Constructor.
	 */
	public StatisticsRmfSerializer() {
	}

	/**
	 * Parsea el tag Band para extraer la lista de valores (Values) asociada a una banda.
	 * @param parser KXmlParser
	 * @return Array de long
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	private long[] parserStatBandValues(KXmlParser parser, int band, double[] max, double[] min, double[] sndmax, double[] sndmin, double[] maxRGB, double[] minRGB, double[] sndmaxRGB, double[] sndminRGB, double[] mean, double[] variance)  throws XmlPullParserException, IOException {
		boolean maxOk = false, minOk = false, sndmaxOk = false, sndminOk = false, meanOk = false, varianceOk = false;
		boolean maxRGBOk = false, minRGBOk = false, sndmaxRGBOk = false, sndminRGBOk = false;
		long[] valueList = null;
		boolean end = false;
			int tag = parser.next();
			while (!end) {
				switch(tag) {
						case KXmlParser.START_TAG:
							if(parser.getName() != null) {
						if (parser.getName().compareTo(MAX) == 0)
							maxOk = true;
						if (parser.getName().compareTo(MIN) == 0)
							minOk = true;
						if (parser.getName().compareTo(SNDMAX) == 0)
							sndmaxOk = true;
						if (parser.getName().compareTo(SNDMIN) == 0)
							sndminOk = true;
						if (parser.getName().compareTo(MAXRGB) == 0)
							maxRGBOk = true;
						if (parser.getName().compareTo(MINRGB) == 0)
							minRGBOk = true;
						if (parser.getName().compareTo(SNDMAXRGB) == 0)
							sndmaxRGBOk = true;
						if (parser.getName().compareTo(SNDMINRGB) == 0)
							sndminRGBOk = true;
						if (parser.getName().compareTo(MEAN) == 0)
							meanOk = true;
						if (parser.getName().compareTo(VARIANCE) == 0)
							varianceOk = true;
					}
					break;
						 case KXmlParser.END_TAG:
							 if (parser.getName().compareTo(BAND) == 0)
								 end = true;
							break;
						case KXmlParser.TEXT:
							if(maxOk) {
								max[band] = Double.parseDouble(parser.getText());
								maxOk = false;
							}
							if(minOk) {
								min[band] = Double.parseDouble(parser.getText());
								minOk = false;
							}
							if(sndmaxOk) {
								sndmax[band] = Double.parseDouble(parser.getText());
								sndmaxOk = false;
							}
							if(sndminOk) {
								sndmin[band] = Double.parseDouble(parser.getText());
								sndminOk = false;
							}
							if(maxRGBOk) {
								maxRGB[band] = Double.parseDouble(parser.getText());
								maxRGBOk = false;
							}
							if(minRGBOk) {
								minRGB[band] = Double.parseDouble(parser.getText());
								minRGBOk = false;
							}
							if(sndmaxRGBOk) {
								sndmaxRGB[band] = Double.parseDouble(parser.getText());
								sndmaxRGBOk = false;
							}
							if(sndminRGBOk) {
								sndminRGB[band] = Double.parseDouble(parser.getText());
								sndminRGBOk = false;
							}
							if(meanOk) {
								mean[band] = Double.parseDouble(parser.getText());
								meanOk = false;
							}
							if(varianceOk) {
								variance[band] = Double.parseDouble(parser.getText());
								varianceOk = false;
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
		int bandCount = 0;
		double[] max = null, maxRGB = null;
		double[] min = null, minRGB = null;
		double[] secondMax = null, secondMaxRGB = null;
		double[] secondMin = null, secondMinRGB = null;
		double[] mean = null;
		double[] variance = null;

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
							if(parser.getName() != null) {
								if (parser.getName().compareTo(MAIN_TAG) == 0) {
									bandCount = Integer.parseInt(parserString(parser, BANDCOUNT, null));
									if(max == null) {
										max = new double[bandCount];
										min = new double[bandCount];
										secondMax = new double[bandCount];
										secondMin = new double[bandCount];
										maxRGB = new double[bandCount];
										minRGB = new double[bandCount];
										secondMaxRGB = new double[bandCount];
										secondMinRGB = new double[bandCount];
										mean = new double[bandCount];
										variance = new double[bandCount];
									}
									for (int i = 0; i < bandCount; i++)
										parserStatBandValues(parser, i, max, min, secondMax, secondMin, maxRGB, minRGB, secondMaxRGB, secondMinRGB, mean, variance);
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
		getDatasetStatistics().setBandCount(bandCount);
		getDatasetStatistics().setMax(max);
		getDatasetStatistics().setMin(min);
		getDatasetStatistics().setSecondMax(secondMax);
		getDatasetStatistics().setSecondMin(secondMin);
		getDatasetStatistics().setMaxRGB(maxRGB);
		getDatasetStatistics().setMinRGB(minRGB);
		getDatasetStatistics().setSecondMaxRGB(secondMaxRGB);
		getDatasetStatistics().setSecondMinRGB(secondMinRGB);
		getDatasetStatistics().setMean(mean);
		getDatasetStatistics().setVariance(variance);
		getDatasetStatistics().setCalculated(true);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.io.rmf.IRmfBlock#write()
	 */
	public String write() {
		if (stat == null)
			return "";

		StringBuffer b = new StringBuffer();

		b.append("<" + MAIN_TAG + ">\n");
		putProperty(b, BANDCOUNT, getDatasetStatistics().getBandCount(), 1);
		for (int i = 0; i < getDatasetStatistics().getBandCount(); i++) {
			b.append("\t<" + BAND + ">\n");
			if (getDatasetStatistics().getMax() != null)
				putProperty(b, MAX, getDatasetStatistics().getMax()[i], 2);
			if (getDatasetStatistics().getMin() != null)
				putProperty(b, MIN, getDatasetStatistics().getMin()[i], 2);
			if (getDatasetStatistics().getSecondMax() != null)
				putProperty(b, SNDMAX, getDatasetStatistics().getSecondMax()[i], 2);
			if (getDatasetStatistics().getSecondMin() != null)
				putProperty(b, SNDMIN, getDatasetStatistics().getSecondMin()[i], 2);
			if (getDatasetStatistics().getMaxRGB() != null)
				putProperty(b, MAXRGB, getDatasetStatistics().getMaxRGB()[i], 2);
			if (getDatasetStatistics().getMinRGB() != null)
				putProperty(b, MINRGB, getDatasetStatistics().getMinRGB()[i], 2);
			if (getDatasetStatistics().getSecondMaxRGB() != null)
				putProperty(b, SNDMAXRGB, getDatasetStatistics().getSecondMaxRGB()[i], 2);
			if (getDatasetStatistics().getSecondMinRGB() != null)
				putProperty(b, SNDMINRGB, getDatasetStatistics().getSecondMinRGB()[i], 2);
			if (getDatasetStatistics().getMean() != null)
				putProperty(b, MEAN, getDatasetStatistics().getMean()[i], 2);
			if (getDatasetStatistics().getVariance() != null)
				putProperty(b, VARIANCE, getDatasetStatistics().getVariance()[i], 2);
			b.append("\t</" + BAND + ">\n");
		}
		for (int i = 0; i < getDatasetStatistics().getTailTrimCount(); i++) {
			if (getDatasetStatistics().getTailTrimValue(i) != null) {
				b.append("\t<" + TAILTRIM + ">\n");
				putProperty(b, KEY, ((Double) getDatasetStatistics().getTailTrimValue(i)[0]).doubleValue(), 2);
				putProperty(b, VALUE, ((Double) getDatasetStatistics().getTailTrimValue(i)[1]).doubleValue(), 2);
				b.append("\t</" + TAILTRIM + ">\n");
			}
		}
		b.append("</" + MAIN_TAG + ">\n");
		return b.toString();
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.io.rmf.IRmfBlock#getResult()
	 */
	public Object getResult() {
		return stat;
	}

	/*
	 *  (non-Javadoc)
	 * @see org.gvsig.raster.dataset.io.rmf.IRmfBlock#getMainTag()
	 */
	public String getMainTag() {
		return MAIN_TAG;
	}
}
