package org.gvsig.gpe.gml.parser;

import org.gvsig.gpe.gml.parser.profiles.Gml2BindingProfile;

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
 * $Id: GPEGmlParser.java 162 2007-06-29 12:19:48Z jorpiell $
 * $Log$
 * Revision 1.15  2007/06/29 12:19:34  jorpiell
 * The schema validation is made independently of the concrete writer
 *
 * Revision 1.14  2007/06/07 14:53:30  jorpiell
 * Add the schema support
 *
 * Revision 1.13  2007/05/24 07:29:47  csanchez
 * AÃ±adidos Alias GML2
 *
 * Revision 1.12  2007/05/17 11:20:51  jorpiell
 * Add the layer methods
 *
 * Revision 1.11  2007/05/16 12:34:55  csanchez
 * GPEParser Prototipo final de lectura
 *
 * Revision 1.10  2007/05/14 11:23:12  jorpiell
 * ProjectionFactory updated
 *
 * Revision 1.9  2007/05/14 09:52:26  jorpiell
 * Tupes separator tag updated
 *
 * Revision 1.8  2007/05/09 06:54:25  jorpiell
 * Change the File by URI
 *
 * Revision 1.7  2007/05/07 07:06:46  jorpiell
 * Add a constructor with the name and the description fields
 *
 * Revision 1.6  2007/04/25 11:08:38  csanchez
 * Parseo correcto con XSOM de esquemas, EntityResolver para los imports
 *
 * Revision 1.5  2007/04/20 12:04:10  csanchez
 * Actualizacion protoripo libGPE, AÃ±adidos test para el parser, parseo con XSOM
 *
 * Revision 1.4  2007/04/19 11:51:43  csanchez
 * Actualizacion protoripo libGPE
 *
 * Revision 1.1  2007/04/18 12:54:45  csanchez
 * Actualizacion protoripo libGPE
 *
 *
 */
/**
 * @author Carlos Sánchez Periñán (sanchez_carper@gva.es)
 **/

public class GPEGml2_1_2_Parser extends GPEDefaultGmlParser{
	
	public GPEGml2_1_2_Parser() {
		super();
		setProfile(new Gml2BindingProfile());
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.parser.GPEParser#getDescription()
	 */
	public String getDescription() {
		return "This parser parses GML 2.1.2";
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.parser.GPEParser#getName()
	 */
	public String getName() {
		return "GML v2.1.2 parser";
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.parser.GPEParser#getFormat()
	 */
	public String getFormat() {
		return "text/xml; subtype=gml/2.1.2";		
	}
}
