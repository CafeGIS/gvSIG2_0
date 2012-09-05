package org.gvsig.gpe.kml.parser.v21.kml;

import org.gvsig.gpe.kml.impl.DefaultKmlLibrary;
import org.gvsig.gpe.parser.GPEReaderBaseTest;
import org.gvsig.gpe.xml.impl.DefaultXmlLibrary;

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
 * $Id:KMLBaseTest.java 359 2008-01-09 17:51:16Z jpiera $
 * $Log$
 * Revision 1.1  2007/05/16 09:57:10  jorpiell
 * Tests refactoring
 *
 * Revision 1.2  2007/04/20 08:38:59  jorpiell
 * Tests updating
 *
 * Revision 1.1  2007/04/13 13:16:21  jorpiell
 * Add KML reading support
 *
 *
 */
/**
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public abstract class KMLBaseTest extends GPEReaderBaseTest{
		
	/* (non-Javadoc)
	 * @see org.gvsig.gpe.parser.GPEReaderBaseTest#setUp()
	 */
	public void setUp() throws Exception {
		super.setUp();
		
		DefaultKmlLibrary lib = new DefaultKmlLibrary();
		lib.initialize();
		lib.postInitialize();
		
		DefaultXmlLibrary xmlLibrary = new DefaultXmlLibrary();
		xmlLibrary.initialize();
		xmlLibrary.postInitialize();
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writers.GPEWriterBaseTest#getGPEParserClass()
	 */
	public Class getGPEParserClass() {
		return org.gvsig.gpe.kml.parser.GPEKml2_1_Parser.class;
	}

}
