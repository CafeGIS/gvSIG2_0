package org.gvsig.gpe.gml.writer.profiles;

import org.gvsig.gpe.gml.writer.v2.geometries.InnerBoundaryIsWriter;
import org.gvsig.gpe.gml.writer.v2.geometries.MultiPolygonWriter;
import org.gvsig.gpe.gml.writer.v2.geometries.OuterBoundaryIsWriter;
import org.gvsig.gpe.gml.writer.v2.geometries.PolygonMemberWriter;
import org.gvsig.gpe.gml.writer.v2.geometries.PolygonWriter;

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
public class GmlSFP0WriterProfile extends Gml2WriterProfile{
	

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.gml.writer.profiles.IWriterProfile#getPolygonMemberWriter()
	 */
	public PolygonMemberWriter getPolygonMemberWriter() {
		if (polygonMemberWriter == null){
			polygonMemberWriter = new org.gvsig.gpe.gml.writer.sfp0.geometries.PolygonMemberWriter();
		}
		return polygonMemberWriter;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.gml.writer.profiles.IWriterProfile#getPolygonWriter()
	 */
	public PolygonWriter getPolygonWriter() {
		if (polygonWriter == null){
			polygonWriter = new org.gvsig.gpe.gml.writer.sfp0.geometries.PolygonWriter();
		}
		return polygonWriter;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.gpe.gml.writer.profiles.Gml2WriterProfile#getInnerBoundaryIsWriter()
	 */
	public InnerBoundaryIsWriter getInnerBoundaryIsWriter() {
		if (innerBoundaryIsWriter == null){
			innerBoundaryIsWriter = new org.gvsig.gpe.gml.writer.sfp0.geometries.InnerBoundaryIsWriter();
		}
		return innerBoundaryIsWriter;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.gpe.gml.writer.profiles.Gml2WriterProfile#getOuterBoundaryIsWriter()
	 */
	public OuterBoundaryIsWriter getOuterBoundaryIsWriter() {
		if (outerBoundaryIsWriter == null){
			outerBoundaryIsWriter = new org.gvsig.gpe.gml.writer.sfp0.geometries.OuterBoundaryIsWriter();
		}
		return outerBoundaryIsWriter;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.gpe.gml.writer.profiles.Gml2WriterProfile#getMultiPolygonWriter()
	 */
	public MultiPolygonWriter getMultiPolygonWriter() {
		if (multiPolygonWriter == null){
			multiPolygonWriter = new org.gvsig.gpe.gml.writer.sfp0.geometries.MultiPolygonWriter();
		}
		return multiPolygonWriter;
	}
	
	
	
	
}
