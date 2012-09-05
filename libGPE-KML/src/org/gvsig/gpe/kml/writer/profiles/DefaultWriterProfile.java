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
public class DefaultWriterProfile implements IWriterProfile {

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.kml.writer.profiles.IWriterProfile#getNameWriter()
	 */
	public NameWriter getNameWriter(){
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.kml.writer.profiles.IWriterProfile#getDescriptionWriter()
	 */
	public DescriptionWriter getDescriptionWriter(){
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.kml.writer.profiles.IWriterProfile#getCoordinatesWriter()
	 */
	public CoordinatesWriter getCoordinatesWriter() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.kml.writer.profiles.IWriterProfile#getDocumentWriter()
	 */
	public DocumentWriter getDocumentWriter() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.kml.writer.profiles.IWriterProfile#getElementWriter()
	 */
	public ElementWriter getElementWriter() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.kml.writer.profiles.IWriterProfile#getFeatureWriter()
	 */
	public FeatureWriter getFeatureWriter() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.kml.writer.profiles.IWriterProfile#getFolderWriter()
	 */
	public FolderWriter getFolderWriter() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.kml.writer.profiles.IWriterProfile#getGeometryWriter()
	 */
	public GeometriesWriter getGeometryWriter() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.kml.writer.profiles.IWriterProfile#getInnerBoundaryIsWriter()
	 */
	public InnerBoundaryIsWriter getInnerBoundaryIsWriter() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.kml.writer.profiles.IWriterProfile#getLatLonAltBoxWriter()
	 */
	public LatLonAltBoxWriter getLatLonAltBoxWriter() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.kml.writer.profiles.IWriterProfile#getLineStringTypeWriter()
	 */
	public LineStringWriter getLineStringTypeWriter() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.kml.writer.profiles.IWriterProfile#getLinearRingWriter()
	 */
	public LinearRingWriter getLinearRingWriter() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.kml.writer.profiles.IWriterProfile#getMetadataWriter()
	 */
	public MetadataWriter getMetadataWriter() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.kml.writer.profiles.IWriterProfile#getMultiGeometryWriter()
	 */
	public MultiGeometryWriter getMultiGeometryWriter() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.kml.writer.profiles.IWriterProfile#getOuterBoundaryIsWriter()
	 */
	public OuterBoundaryIsWriter getOuterBoundaryIsWriter() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.kml.writer.profiles.IWriterProfile#getPlaceMarkWriter()
	 */
	public PlaceMarkWriter getPlaceMarkWriter() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.kml.writer.profiles.IWriterProfile#getPointTypeWriter()
	 */
	public PointWriter getPointTypeWriter() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.kml.writer.profiles.IWriterProfile#getPolygonTypeWriter()
	 */
	public PolygonWriter getPolygonTypeWriter() {
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.kml.writer.profiles.IWriterProfile#getRegionWriter()
	 */
	public RegionWriter getRegionWriter() {
		return null;
	}

}
