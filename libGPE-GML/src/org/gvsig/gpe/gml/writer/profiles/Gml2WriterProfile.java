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
public class Gml2WriterProfile extends DefaultWriterProfile{
	protected FeatureCollectionWriter featureCollectionWriter = null;
	protected NameWriter nameWriter = null;	
	protected DescriptionWriter descriptionWriter = null;
	protected GeometryMemberWriter geometryMemberWriter = null;
	protected BoundedByWriter boundedByWriter = null;
	protected BoxWriter boxWriter = null;
	protected CoordWriter coordWriter = null;
	protected CoordinatesWriter coordinatesWriter = null;
	protected ElementWriter elementWriter = null;
	protected FeatureMemberWriter featureMemberWriter = null;
	protected InnerBoundaryIsWriter innerBoundaryIsWriter = null;
	protected LineStringMemberWriter lineStringMemberWriter = null;
	protected LineStringWriter lineStringWriter = null;
	protected LinearRingWriter linearRingWriter = null;
	protected MultiGeometryWriter multiGeometryWriter = null;
	protected MultiLineStringWriter multiLineStringWriter = null;
	protected MultiPointWriter multiPointWriter = null;
	protected MultiPolygonWriter multiPolygonWriter = null;
	protected OuterBoundaryIsWriter outerBoundaryIsWriter = null;
	protected PointMemberWriter pointMemberWriter = null;
	protected PointWriter pointWriter = null;
	protected PolygonMemberWriter polygonMemberWriter = null;
	protected PolygonWriter polygonWriter = null;

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.gml.writer.profiles.IWriterProfile#getNameWriter()
	 */
	public NameWriter getNameWriter(){
		if (nameWriter == null){
			nameWriter = new NameWriter();
		}
		return nameWriter;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.gml.writer.profiles.IWriterProfile#getDescriptionWriter()
	 */
	public DescriptionWriter getDescriptionWriter(){
		if (descriptionWriter == null){
			descriptionWriter = new DescriptionWriter();
		}
		return descriptionWriter;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.gml.writer.profiles.IWriterProfile#getGeometryMemberWriter()
	 */
	public GeometryMemberWriter getGeometryMemberWriter(){
		if (geometryMemberWriter == null){
			geometryMemberWriter = new GeometryMemberWriter();
		}
		return geometryMemberWriter;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.gml.writer.profiles.IWriterProfile#getBoundedByWriter()
	 */
	public BoundedByWriter getBoundedByWriter() {
		if (boundedByWriter == null){
			boundedByWriter = new BoundedByWriter();
		}
		return boundedByWriter;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.gml.writer.profiles.IWriterProfile#getBoxWriter()
	 */
	public BoxWriter getBoxWriter() {
		if (boxWriter == null){
			boxWriter = new BoxWriter();
		}
		return boxWriter;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.gml.writer.profiles.IWriterProfile#getCoordWriter()
	 */
	public CoordWriter getCoordWriter() {
		if (coordWriter == null){
			coordWriter = new CoordWriter();
		}
		return coordWriter;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.gml.writer.profiles.IWriterProfile#getCoordinatesWriter()
	 */
	public CoordinatesWriter getCoordinatesWriter() {
		if (coordinatesWriter == null){
			coordinatesWriter = new CoordinatesWriter();
		}
		return coordinatesWriter;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.gml.writer.profiles.IWriterProfile#getElementWriter()
	 */
	public ElementWriter getElementWriter() {
		if (elementWriter == null){
			elementWriter = new ElementWriter();
		}
		return elementWriter;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.gml.writer.profiles.IWriterProfile#getFeatureMemberWriter()
	 */
	public FeatureMemberWriter getFeatureMemberWriter() {
		if (featureMemberWriter == null){
			featureMemberWriter = new FeatureMemberWriter();
		}
		return featureMemberWriter;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.gml.writer.profiles.IWriterProfile#getInnerBoundaryIsWriter()
	 */
	public InnerBoundaryIsWriter getInnerBoundaryIsWriter() {
		if (innerBoundaryIsWriter == null){
			innerBoundaryIsWriter = new InnerBoundaryIsWriter();
		}
		return innerBoundaryIsWriter;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.gml.writer.profiles.IWriterProfile#getLineStringMemeberWriter()
	 */
	public LineStringMemberWriter getLineStringMemeberWriter() {
		if (lineStringMemberWriter == null){
			lineStringMemberWriter = new LineStringMemberWriter();
		}
		return lineStringMemberWriter;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.gml.writer.profiles.IWriterProfile#getLineStringWriter()
	 */
	public LineStringWriter getLineStringWriter() {
		if (lineStringWriter == null){
			lineStringWriter = new LineStringWriter();
		}
		return lineStringWriter;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.gml.writer.profiles.IWriterProfile#getLinearRingWriter()
	 */
	public LinearRingWriter getLinearRingWriter() {
		if (linearRingWriter == null){
			linearRingWriter = new LinearRingWriter();
		}
		return linearRingWriter;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.gml.writer.profiles.IWriterProfile#getMultiGeometryWriter()
	 */
	public MultiGeometryWriter getMultiGeometryWriter() {
		if (multiGeometryWriter == null){
			multiGeometryWriter = new MultiGeometryWriter();
		}
		return multiGeometryWriter;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.gml.writer.profiles.IWriterProfile#getMultiLineStringWriter()
	 */
	public MultiLineStringWriter getMultiLineStringWriter() {
		if (multiLineStringWriter == null){
			multiLineStringWriter = new MultiLineStringWriter();
		}
		return multiLineStringWriter;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.gml.writer.profiles.IWriterProfile#getMultiPointWriter()
	 */
	public MultiPointWriter getMultiPointWriter() {
		if (multiPointWriter == null){
			multiPointWriter = new MultiPointWriter();
		}
		return multiPointWriter;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.gml.writer.profiles.IWriterProfile#getMultiPolygonWriter()
	 */
	public MultiPolygonWriter getMultiPolygonWriter() {
		if (multiPolygonWriter == null){
			multiPolygonWriter = new MultiPolygonWriter();
		}
		return multiPolygonWriter;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.gml.writer.profiles.IWriterProfile#getOuterBoundaryIsWriter()
	 */
	public OuterBoundaryIsWriter getOuterBoundaryIsWriter() {
		if (outerBoundaryIsWriter == null){
			outerBoundaryIsWriter = new OuterBoundaryIsWriter();
		}
		return outerBoundaryIsWriter;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.gml.writer.profiles.IWriterProfile#getPointMemberWriter()
	 */
	public PointMemberWriter getPointMemberWriter() {
		if (pointMemberWriter == null){
			pointMemberWriter = new PointMemberWriter();
		}
		return pointMemberWriter;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.gml.writer.profiles.IWriterProfile#getPointWriter()
	 */
	public PointWriter getPointWriter() {
		if (pointWriter == null){
			pointWriter = new PointWriter();
		}
		return pointWriter;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.gml.writer.profiles.IWriterProfile#getPolygonMemberWriter()
	 */
	public PolygonMemberWriter getPolygonMemberWriter() {
		if (polygonMemberWriter == null){
			polygonMemberWriter = new PolygonMemberWriter();
		}
		return polygonMemberWriter;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.gml.writer.profiles.IWriterProfile#getPolygonWriter()
	 */
	public PolygonWriter getPolygonWriter() {
		if (polygonWriter == null){
			polygonWriter = new PolygonWriter();
		}
		return polygonWriter;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.gml.writer.profiles.IWriterProfile#getFeatureCollectionWriter()
	 */
	public FeatureCollectionWriter getFeatureCollectionWriter() {
		if (featureCollectionWriter == null){
			featureCollectionWriter = new FeatureCollectionWriter();
		}
		return featureCollectionWriter;
	}	
}
