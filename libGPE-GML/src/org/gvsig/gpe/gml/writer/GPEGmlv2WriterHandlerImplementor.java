package org.gvsig.gpe.gml.writer;

import org.gvsig.gpe.gml.writer.profiles.Gml2WriterProfile;

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
public class GPEGmlv2WriterHandlerImplementor extends GPEGmlWriterHandlerImplementor {

	public GPEGmlv2WriterHandlerImplementor() {
		super();
		setProfile(new Gml2WriterProfile());
	}	
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writer.IGPEWriterHandlerImplementor#getDescription()
	 */
	public String getDescription() {
		return "This writer writes GML 2.1.2";
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writer.IGPEWriterHandlerImplementor#getName()
	 */
	public String getName() {
		return "GML v2.1.2";
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writer.GPEWriterHandlerImplementor#getDefaultFormat()
	 */
	public String getFormat() {
		return "text/xml; subtype=gml/2.1.2";		
	}
}
