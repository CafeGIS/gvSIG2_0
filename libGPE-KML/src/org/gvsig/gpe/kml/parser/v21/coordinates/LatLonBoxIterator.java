package org.gvsig.gpe.kml.parser.v21.coordinates;

import java.io.IOException;

import javax.xml.namespace.QName;

import org.gvsig.gpe.kml.parser.GPEDeafultKmlParser;
import org.gvsig.gpe.kml.utils.Kml2_1_Tags;
import org.gvsig.gpe.xml.stream.IXmlStreamReader;
import org.gvsig.gpe.xml.stream.XmlStreamException;
import org.gvsig.gpe.xml.utils.CompareUtils;

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
 * $Id:LatLonBoxBinding.java 357 2008-01-09 17:50:08Z jpiera $
 * $Log$
 * Revision 1.1  2007/05/11 07:06:29  jorpiell
 * Refactoring of some package names
 *
 * Revision 1.1  2007/05/09 08:36:24  jorpiell
 * Add the bbox to the layer
 *
 *
 */
/**
 * This class parses a LatLonBox tag. Example:
 * <p>
 * <pre>
 * <code> 
 * &lt;LatLonAltBox&gt;
 * &lt;north&gt;43.374&lt;/north&gt;
 * &lt;south&gt;42.983&lt;/south&gt;
 * &lt;east&gt;-0.335&lt;/east&gt;
 * &lt;west&gt;-1.423&lt;/west&gt;
 * &lt;rotation&gt;39.37878630116985&lt;/rotation&gt;
 * &lt;/LatLonAltBox&gt;
 * </code>
 * </pre>
 * </p> 
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 * @see http://code.google.com/apis/kml/documentation/kml_tags_21.html#latlonbox
 */
public class LatLonBoxIterator extends KmlCoodinatesIterator{
	double[] min = null;
	double[] max = null;
	int iterations = 0;
	
	/**
	 * It parses the LatLonAltBox tag
	 * @param parser
	 * The XML parser
	 * @param handler
	 * The GPE parser that contains the content handler and
	 * the error handler
	 * @return
	 * A Bounding box
	 * @throws IOException 
	 * @throws XmlStreamException 
	 */
	public  Object parse(IXmlStreamReader parser,GPEDeafultKmlParser handler) throws XmlStreamException, IOException {
		boolean endFeature = false;
		int currentTag;
		Object bbox = null;
		min = new double[3];
		max = new double[3];
		double rotation;
		iterations = 0;
		dimension = 2;
		
		String id = handler.getProfile().getGeometryBinding().getID(parser, handler);

		QName tag = parser.getName();
		currentTag = parser.getEventType();

		while (!endFeature){
			switch(currentTag){
			case IXmlStreamReader.START_ELEMENT:
				if (CompareUtils.compareWithOutNamespace(tag,Kml2_1_Tags.NORTH)){
					parser.next();
					max[1] = handler.getProfile().getDoubleBinding().parse(parser.getText());
				}else if (CompareUtils.compareWithOutNamespace(tag,Kml2_1_Tags.SOUTH)){
					parser.next();
					min[1] = handler.getProfile().getDoubleBinding().parse(parser.getText());
				}else if (CompareUtils.compareWithOutNamespace(tag,Kml2_1_Tags.EAST)){
					parser.next();
					max[0] = handler.getProfile().getDoubleBinding().parse(parser.getText());
				}else if (CompareUtils.compareWithOutNamespace(tag,Kml2_1_Tags.WEST)){
					parser.next();
					min[0] = handler.getProfile().getDoubleBinding().parse(parser.getText());
				}else if (CompareUtils.compareWithOutNamespace(tag,Kml2_1_Tags.ROTATION)){
					parser.next();
					rotation = handler.getProfile().getDoubleBinding().parse(parser.getText());
				}
				break;
			case IXmlStreamReader.END_ELEMENT:
				if (CompareUtils.compareWithOutNamespace(tag,Kml2_1_Tags.LATLONBOX)){						
					endFeature = true;
					bbox = handler.getContentHandler().startBbox(id, this, Kml2_1_Tags.DEFAULT_SRS);
					handler.getContentHandler().endBbox(bbox);
				}
				break;
			case IXmlStreamReader.CHARACTERS:					

				break;
			}
			if (!endFeature){					
				currentTag = parser.next();
				tag = parser.getName();
			}
		}			
		return bbox;	
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.parser.ICoordinateIterator#hasNext()
	 */
	public boolean hasNext() throws IOException {
		if (iterations < 2){
			return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.parser.ICoordinateIterator#next(double[])
	 */
	public void next(double[] buffer) throws IOException {
		if (iterations == 0){
			for (int i=0 ; i<buffer.length ; i++){
				buffer[i] = min[i];
			}
			iterations = 1;
		}
		if (iterations == 1){
			for (int i=0 ; i<buffer.length ; i++){
				buffer[i] = max[i];
			}
			iterations = 2;
		}
	}
}
