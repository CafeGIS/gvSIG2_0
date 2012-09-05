package org.gvsig.gpe.kml.writer.profiles;

import org.gvsig.gpe.kml.writer.v21.features.DescriptionWriter;
import org.gvsig.gpe.kml.writer.v21.features.DocumentWriter;
import org.gvsig.gpe.kml.writer.v21.features.ElementWriter;
import org.gvsig.gpe.kml.writer.v21.features.FeatureWriter;
import org.gvsig.gpe.kml.writer.v21.features.FolderWriter;
import org.gvsig.gpe.kml.writer.v21.features.MetadataWriter;
import org.gvsig.gpe.kml.writer.v21.features.NameWriter;
import org.gvsig.gpe.kml.writer.v21.features.PlaceMarkWriter;
import org.gvsig.gpe.kml.writer.v21.geometries.CoordinatesWriter;
import org.gvsig.gpe.kml.writer.v21.geometries.GeometriesWriter;
import org.gvsig.gpe.kml.writer.v21.geometries.InnerBoundaryIsWriter;
import org.gvsig.gpe.kml.writer.v21.geometries.LatLonAltBoxWriter;
import org.gvsig.gpe.kml.writer.v21.geometries.LineStringWriter;
import org.gvsig.gpe.kml.writer.v21.geometries.LinearRingWriter;
import org.gvsig.gpe.kml.writer.v21.geometries.MultiGeometryWriter;
import org.gvsig.gpe.kml.writer.v21.geometries.OuterBoundaryIsWriter;
import org.gvsig.gpe.kml.writer.v21.geometries.PointWriter;
import org.gvsig.gpe.kml.writer.v21.geometries.PolygonWriter;
import org.gvsig.gpe.kml.writer.v21.geometries.RegionWriter;

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
	
	public NameWriter getNameWriter();
	
	public DescriptionWriter getDescriptionWriter();
	
	public DocumentWriter getDocumentWriter();
	
	public ElementWriter getElementWriter();
	
	public FeatureWriter getFeatureWriter();
	
	public FolderWriter getFolderWriter();
		
	public MetadataWriter getMetadataWriter();
	
	public PlaceMarkWriter getPlaceMarkWriter();
	
	public CoordinatesWriter getCoordinatesWriter();
	
	public GeometriesWriter getGeometryWriter();
	
	public InnerBoundaryIsWriter getInnerBoundaryIsWriter();
	
	public LatLonAltBoxWriter getLatLonAltBoxWriter();
	
	public LinearRingWriter getLinearRingWriter();
	
	public LineStringWriter getLineStringTypeWriter();
	
	public MultiGeometryWriter getMultiGeometryWriter();
	
	public OuterBoundaryIsWriter getOuterBoundaryIsWriter();
	
	public PointWriter getPointTypeWriter();
	
	public PolygonWriter getPolygonTypeWriter();
	
	public RegionWriter getRegionWriter();
	
}
