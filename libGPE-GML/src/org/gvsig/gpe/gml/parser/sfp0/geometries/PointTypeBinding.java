package org.gvsig.gpe.gml.parser.sfp0.geometries;

import java.io.IOException;

import javax.xml.namespace.QName;

import org.gvsig.gpe.gml.parser.GPEDefaultGmlParser;
import org.gvsig.gpe.gml.parser.sfp0.coordinates.PosTypeIterator;
import org.gvsig.gpe.gml.utils.GMLTags;
import org.gvsig.gpe.xml.stream.IXmlStreamReader;
import org.gvsig.gpe.xml.stream.XmlStreamException;
import org.gvsig.gpe.xml.utils.CompareUtils;

/* gvSIG. Sistema de Informaci�n Geogr�fica de la Generalitat Valenciana
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
 *   Av. Blasco Ib��ez, 50
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
 * @author Jorge Piera LLodr� (jorge.piera@iver.es)
 */
public class PointTypeBinding extends org.gvsig.gpe.gml.parser.v2.geometries.PointTypeBinding {

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.gml.bindings.v2.geometries.PointTypeBinding#parseTag(org.xmlpull.v1.XmlPullParser, org.gvsig.gpe.gml.GPEDefaultGmlParser, java.lang.String, java.lang.String, java.lang.String)
	 */
	protected Object parseTag(IXmlStreamReader parser,GPEDefaultGmlParser handler, QName tag, String id, String srsName) throws XmlStreamException, IOException{
		Object point = super.parseTag(parser, handler, tag, id, srsName);
		if (point != null){
			return point;
		}
		if (CompareUtils.compareWithNamespace(tag, GMLTags.GML_POS)){
			PosTypeIterator coordinatesIteartor = handler.getProfile().getPosTypeBinding();
			coordinatesIteartor.initialize(parser, handler,GMLTags.GML_POINT);
			point = handler.getContentHandler().startPoint(id,
					coordinatesIteartor,
					srsName);
		}
		return point;
	}
}
