package org.gvsig.gpe.gml.writer.profiles;

import org.gvsig.gpe.gml.writer.v2.features.DescriptionWriter;
import org.gvsig.gpe.gml.writer.v2.features.ElementWriter;
import org.gvsig.gpe.gml.writer.v2.features.FeatureMemberWriter;
import org.gvsig.gpe.gml.writer.v2.features.NameWriter;
import org.gvsig.gpe.gml.writer.v2.geometries.BoundedByWriter;
import org.gvsig.gpe.gml.writer.v2.geometries.BoxWriter;
import org.gvsig.gpe.gml.writer.v2.geometries.CoordWriter;
import org.gvsig.gpe.gml.writer.v2.geometries.CoordinatesWriter;
import org.gvsig.gpe.gml.writer.v2.geometries.GeometryMemberWriter;
import org.gvsig.gpe.gml.writer.v2.geometries.InnerBoundaryIsWriter;
import org.gvsig.gpe.gml.writer.v2.geometries.LineStringMemberWriter;
import org.gvsig.gpe.gml.writer.v2.geometries.LineStringWriter;
import org.gvsig.gpe.gml.writer.v2.geometries.LinearRingWriter;
import org.gvsig.gpe.gml.writer.v2.geometries.MultiGeometryWriter;
import org.gvsig.gpe.gml.writer.v2.geometries.MultiLineStringWriter;
import org.gvsig.gpe.gml.writer.v2.geometries.MultiPointWriter;
import org.gvsig.gpe.gml.writer.v2.geometries.MultiPolygonWriter;
import org.gvsig.gpe.gml.writer.v2.geometries.OuterBoundaryIsWriter;
import org.gvsig.gpe.gml.writer.v2.geometries.PointMemberWriter;
import org.gvsig.gpe.gml.writer.v2.geometries.PointWriter;
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
public abstract class DefaultWriterProfile implements IWriterProfile {

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.gml.writer.profiles.IWriterProfile#getNameWriter()
	 */
	public NameWriter getNameWriter(){
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.gml.writer.profiles.IWriterProfile#getDescriptionWriter()
	 */
	public DescriptionWriter getDescriptionWriter(){
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.gml.writer.profiles.IWriterProfile#getGeometryMemberWriter()
	 */
	public GeometryMemberWriter getGeometryMemberWriter(){
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.gml.writer.profiles.IWriterProfile#getBoundedByWriter()
	 */
	public BoundedByWriter getBoundedByWriter() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.gml.writer.profiles.IWriterProfile#getBoxWriter()
	 */
	public BoxWriter getBoxWriter() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.gml.writer.profiles.IWriterProfile#getCoordWriter()
	 */
	public CoordWriter getCoordWriter() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.gml.writer.profiles.IWriterProfile#getCoordinatesWriter()
	 */
	public CoordinatesWriter getCoordinatesWriter() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.gml.writer.profiles.IWriterProfile#getElementWriter()
	 */
	public ElementWriter getElementWriter() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.gml.writer.profiles.IWriterProfile#getFeatureMemberWriter()
	 */
	public FeatureMemberWriter getFeatureMemberWriter() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.gml.writer.profiles.IWriterProfile#getInnerBoundaryIsWriter()
	 */
	public InnerBoundaryIsWriter getInnerBoundaryIsWriter() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.gml.writer.profiles.IWriterProfile#getLineStringMemeberWriter()
	 */
	public LineStringMemberWriter getLineStringMemeberWriter() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.gml.writer.profiles.IWriterProfile#getLineStringWriter()
	 */
	public LineStringWriter getLineStringWriter() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.gml.writer.profiles.IWriterProfile#getLinearRingWriter()
	 */
	public LinearRingWriter getLinearRingWriter() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.gml.writer.profiles.IWriterProfile#getMultiGeometryWriter()
	 */
	public MultiGeometryWriter getMultiGeometryWriter() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.gml.writer.profiles.IWriterProfile#getMultiLineStringWriter()
	 */
	public MultiLineStringWriter getMultiLineStringWriter() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.gml.writer.profiles.IWriterProfile#getMultiPointWriter()
	 */
	public MultiPointWriter getMultiPointWriter() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.gml.writer.profiles.IWriterProfile#getMultiPolygonWriter()
	 */
	public MultiPolygonWriter getMultiPolygonWriter() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.gml.writer.profiles.IWriterProfile#getOuterBoundaryIsWriter()
	 */
	public OuterBoundaryIsWriter getOuterBoundaryIsWriter() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.gml.writer.profiles.IWriterProfile#getPointMemberWriter()
	 */
	public PointMemberWriter getPointMemberWriter() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.gml.writer.profiles.IWriterProfile#getPointWriter()
	 */
	public PointWriter getPointWriter() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.gml.writer.profiles.IWriterProfile#getPolygonMemberWriter()
	 */
	public PolygonMemberWriter getPolygonMemberWriter() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.gml.writer.profiles.IWriterProfile#getPolygonWriter()
	 */
	public PolygonWriter getPolygonWriter() {
		return null;
	}
}
