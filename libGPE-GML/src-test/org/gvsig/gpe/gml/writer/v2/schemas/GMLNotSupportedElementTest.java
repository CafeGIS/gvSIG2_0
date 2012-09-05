package org.gvsig.gpe.gml.writer.v2.schemas;

import org.gvsig.gpe.writer.schemas.GPENotSupportedElementTest;

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
 * $Id:GMLNotSupportedElementTest.java 350 2008-01-09 12:53:07Z jpiera $
 * $Log$
 * Revision 1.1  2007/06/29 12:19:34  jorpiell
 * The schema validation is made independently of the concrete writer
 *
 * Revision 1.1  2007/06/28 13:05:09  jorpiell
 * The Qname has been updated to the 1.5 JVM machine. The schema validation is made in the GPEWriterHandlerImplementor class
 *
 * Revision 1.1  2007/06/22 12:22:53  jorpiell
 * The typeNotFoundException has been deleted. It never was thrown
 *
 *
 */
/**
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public class GMLNotSupportedElementTest extends
		GPENotSupportedElementTest {

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writers.GPEWriterBaseTest#getGPEParserClass()
	 */
	public Class getGPEParserClass() {
		return org.gvsig.gpe.gml.parser.GPEGml2_1_2_Parser.class;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writers.GPEWriterBaseTest#getGPEWriterHandlerClass()
	 */
	public Class getGPEWriterHandlerClass() {
		return org.gvsig.gpe.gml.writer.GPEGmlv2WriterHandlerImplementor.class;
	}

}
