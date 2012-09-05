package org.gvsig.gpe.gml.parser.sfp0.geometries;

import java.io.IOException;

import javax.xml.namespace.QName;

import org.gvsig.gpe.gml.parser.GPEDefaultGmlParser;
import org.gvsig.gpe.gml.parser.sfp0.coordinates.PosListTypeIterator;
import org.gvsig.gpe.gml.utils.GMLTags;
import org.gvsig.gpe.parser.ICoordinateIterator;
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
 * $Id$
 * $Log$
 *
 */
/**
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public class LinearRingTypeBinding extends org.gvsig.gpe.gml.parser.v2.geometries.LinearRingTypeBinding{

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.gml.bindings.v2.geometries.LinearRingTypeBinding#parseTag(org.xmlpull.v1.XmlPullParser, org.gvsig.gpe.gml.GPEDefaultGmlParser, java.lang.String, org.gvsig.gpe.gml.utils.CoordsContainer)
	 */
	protected ICoordinateIterator parseTag_(IXmlStreamReader parser,GPEDefaultGmlParser handler, QName tag) throws XmlStreamException, IOException{
		ICoordinateIterator coordinatesIterator = super.parseTag_(parser, handler, tag);
		if (coordinatesIterator != null){
			return coordinatesIterator;
		}
		if (CompareUtils.compareWithNamespace(tag,GMLTags.GML_POSLIST)){
			PosListTypeIterator postListBinding = handler.getProfile().getPosListTypeBinding();
			postListBinding.initialize(parser, handler,GMLTags.GML_LINEARRING);
			coordinatesIterator = postListBinding; 
		}
		return coordinatesIterator;
	}
}

