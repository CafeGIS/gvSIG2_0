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
public class Kml21WriterProfile extends DefaultWriterProfile{
	protected NameWriter nameWriter = null;
	protected DescriptionWriter descriptionWriter = null;
	protected CoordinatesWriter coordinatesWriter = null;
	protected DocumentWriter documentWriter = null;
	protected ElementWriter elementWriter = null;
	protected FeatureWriter featureWriter = null;
	protected FolderWriter folderWriter = null;
	protected GeometriesWriter geometriesWriter = null;
	protected InnerBoundaryIsWriter innerBoundaryIsWriter = null;
	protected LatLonAltBoxWriter latLonAltBoxWriter = null;
	protected LineStringWriter lineStringWriter = null;
	protected LinearRingWriter linearRingWriter = null;
	protected MetadataWriter metadataWriter = null;
	protected MultiGeometryWriter multiGeometryWriter= null;
	protected OuterBoundaryIsWriter outerBoundaryIsWriter = null;
	protected PlaceMarkWriter placeMarkWriter = null;
	protected PointWriter pointWriter = null;
	protected PolygonWriter polygonWriter = null;
	protected RegionWriter regionWriter = null;
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.kml.writer.profiles.IWriterProfile#getNameWriter()
	 */
	public NameWriter getNameWriter(){
		if (nameWriter == null){
			nameWriter = new NameWriter();
		}
		return nameWriter;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.kml.writer.profiles.IWriterProfile#getDescriptionWriter()
	 */
	public DescriptionWriter getDescriptionWriter(){
		if (descriptionWriter == null){
			descriptionWriter = new DescriptionWriter();
		}
		return descriptionWriter;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.kml.writer.profiles.DefaultWriterProfile#getCoordinatesWriter()
	 */
	public CoordinatesWriter getCoordinatesWriter() {
		if (coordinatesWriter == null){
			coordinatesWriter = new CoordinatesWriter();
		}
		return coordinatesWriter;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.kml.reader.bindings.profiles.IWriterProfile#getDocumentWriter()
	 */
	public DocumentWriter getDocumentWriter() {
		if (documentWriter == null){
			documentWriter = new DocumentWriter();
		}
		return documentWriter;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.kml.reader.bindings.profiles.IWriterProfile#getElementWriter()
	 */
	public ElementWriter getElementWriter() {
		if (elementWriter == null){
			elementWriter = new ElementWriter();
		}
		return elementWriter;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.kml.reader.bindings.profiles.IWriterProfile#getFeatureWriter()
	 */
	public FeatureWriter getFeatureWriter() {
		if (featureWriter == null){
			featureWriter = new FeatureWriter();
		}
		return featureWriter;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.kml.reader.bindings.profiles.IWriterProfile#getFolderWriter()
	 */
	public FolderWriter getFolderWriter() {
		if (folderWriter == null){
			folderWriter = new FolderWriter();
		}
		return folderWriter;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.kml.writer.profiles.DefaultWriterProfile#getGeometryWriter()
	 */
	public GeometriesWriter getGeometryWriter() {
		if (geometriesWriter == null){
			geometriesWriter = new GeometriesWriter();
		}
		return geometriesWriter;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.kml.writer.profiles.DefaultWriterProfile#getInnerBoundaryIsWriter()
	 */
	public InnerBoundaryIsWriter getInnerBoundaryIsWriter() {
		if (innerBoundaryIsWriter == null){
			innerBoundaryIsWriter = new InnerBoundaryIsWriter();
		}
		return innerBoundaryIsWriter;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.kml.writer.profiles.DefaultWriterProfile#getLatLonAltBoxWriter()
	 */
	public LatLonAltBoxWriter getLatLonAltBoxWriter() {
		if (latLonAltBoxWriter == null){
			latLonAltBoxWriter = new LatLonAltBoxWriter();
		}
		return latLonAltBoxWriter;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.kml.writer.profiles.DefaultWriterProfile#getLineStringTypeWriter()
	 */
	public LineStringWriter getLineStringTypeWriter() {
		if (lineStringWriter == null){
			lineStringWriter = new LineStringWriter();
		}
		return lineStringWriter;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.kml.writer.profiles.DefaultWriterProfile#getLinearRingWriter()
	 */
	public LinearRingWriter getLinearRingWriter() {
		if (linearRingWriter == null){
			linearRingWriter = new LinearRingWriter();
		}
		return linearRingWriter;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.kml.writer.profiles.DefaultWriterProfile#getMetadataWriter()
	 */
	public MetadataWriter getMetadataWriter() {
		if (metadataWriter == null){
			metadataWriter = new MetadataWriter();
		}
		return metadataWriter;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.kml.writer.profiles.DefaultWriterProfile#getMultiGeometryWriter()
	 */
	public MultiGeometryWriter getMultiGeometryWriter() {
		if (multiGeometryWriter == null){
			multiGeometryWriter = new MultiGeometryWriter();
		}
		return multiGeometryWriter;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.kml.writer.profiles.DefaultWriterProfile#getOuterBoundaryIsWriter()
	 */
	public OuterBoundaryIsWriter getOuterBoundaryIsWriter() {
		if (outerBoundaryIsWriter == null){
			outerBoundaryIsWriter = new OuterBoundaryIsWriter();
		}
		return outerBoundaryIsWriter;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.kml.writer.profiles.DefaultWriterProfile#getPlaceMarkWriter()
	 */
	public PlaceMarkWriter getPlaceMarkWriter() {
		if (placeMarkWriter == null){
			placeMarkWriter = new PlaceMarkWriter();
		}
		return placeMarkWriter;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.kml.writer.profiles.DefaultWriterProfile#getPointTypeWriter()
	 */
	public PointWriter getPointTypeWriter() {
		if (pointWriter == null){
			pointWriter = new PointWriter();
		}
		return pointWriter;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.kml.writer.profiles.DefaultWriterProfile#getPolygonTypeWriter()
	 */
	public PolygonWriter getPolygonTypeWriter() {
		if (polygonWriter == null){
			polygonWriter = new PolygonWriter();
		}
		return polygonWriter;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.kml.writer.profiles.DefaultWriterProfile#getRegionWriter()
	 */
	public RegionWriter getRegionWriter() {
		if (regionWriter == null){
			regionWriter = new RegionWriter();
		}
		return regionWriter;
	}
}
