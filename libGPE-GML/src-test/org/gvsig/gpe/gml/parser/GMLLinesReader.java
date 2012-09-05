package org.gvsig.gpe.gml.parser;

import java.util.ArrayList;

import org.gvsig.gpe.containers.Layer;

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
 * $Id: GMLLinesReader.java 73 2007-04-20 08:33:37Z jorpiell $
 * $Log$
 * Revision 1.1  2007/04/20 08:33:37  jorpiell
 * Test updated
 *
 *
 */
/**
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public class GMLLinesReader extends GMLReaderBaseTest {
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.parser.GPEReaderBaseTest#getFile()
	 */
	public String getFile() {
		return "testdata/GML-lines.gml";
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.gml.parser.GMLReaderBaseTest#hasSchema()
	 */
	public boolean hasSchema() {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.parser.GPEReaderBaseTest#makeAsserts()
	 */
	public void makeAsserts() {
		assertEquals(1, getLayers().length);
		Layer layer = getLayers()[0];
		ArrayList features = layer.getFeatures();
		assertEquals(10, features.size());		
	}
}
