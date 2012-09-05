package org.gvsig.gpe.gml.writer.v2;

import org.gvsig.gpe.writer.GPEMultiLineStringLayerTest;

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
 * $Id: GMLMultiLineStringLayerTest.java 352 2008-01-09 13:51:51Z jpiera $
 * $Log$
 * Revision 1.6  2007/06/29 12:19:34  jorpiell
 * The schema validation is made independently of the concrete writer
 *
 * Revision 1.5  2007/06/07 14:53:30  jorpiell
 * Add the schema support
 *
 * Revision 1.4  2007/04/26 14:42:42  jorpiell
 * Some writer handler methods updated
 *
 * Revision 1.3  2007/04/19 11:51:43  csanchez
 * Actualizacion protoripo libGPE
 *
 * Revision 1.2  2007/04/14 16:07:30  jorpiell
 * The writer has been created
 *
 * Revision 1.1  2007/04/13 13:16:00  jorpiell
 * Add the multiple geometries
 *
 *
 */
/**
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public class GMLMultiLineStringLayerTest extends GPEMultiLineStringLayerTest{
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

