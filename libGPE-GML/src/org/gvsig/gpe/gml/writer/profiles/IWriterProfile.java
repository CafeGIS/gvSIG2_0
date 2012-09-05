package org.gvsig.gpe.gml.writer.profiles;

import org.gvsig.gpe.gml.writer.v2.features.DescriptionWriter;
import org.gvsig.gpe.gml.writer.v2.features.ElementWriter;
import org.gvsig.gpe.gml.writer.v2.features.FeatureCollectionWriter;
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
public interface IWriterProfile {
	
	public FeatureCollectionWriter getFeatureCollectionWriter();
	
	public NameWriter getNameWriter();
	
	public DescriptionWriter getDescriptionWriter();
	
	public ElementWriter getElementWriter();
	
	public FeatureMemberWriter getFeatureMemberWriter();
	
	public BoundedByWriter getBoundedByWriter();
	
	public BoxWriter getBoxWriter();
	
	public CoordinatesWriter getCoordinatesWriter();
	
	public CoordWriter getCoordWriter();	
	
	public GeometryMemberWriter getGeometryMemberWriter();
	
	public InnerBoundaryIsWriter getInnerBoundaryIsWriter();
	
	public LinearRingWriter getLinearRingWriter();
	
	public LineStringMemberWriter getLineStringMemeberWriter();
	
	public LineStringWriter getLineStringWriter();
	
	public MultiGeometryWriter getMultiGeometryWriter();
	
	public MultiLineStringWriter getMultiLineStringWriter();
	
	public MultiPointWriter getMultiPointWriter();
	
	public MultiPolygonWriter getMultiPolygonWriter();
	
	public OuterBoundaryIsWriter getOuterBoundaryIsWriter();
	
	public PointMemberWriter getPointMemberWriter();
	
	public PointWriter getPointWriter();
	
	public PolygonMemberWriter  getPolygonMemberWriter();
	
	public PolygonWriter getPolygonWriter();	
}
