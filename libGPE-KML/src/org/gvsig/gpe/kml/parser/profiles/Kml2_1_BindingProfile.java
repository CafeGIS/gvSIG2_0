package org.gvsig.gpe.kml.parser.profiles;

import org.gvsig.gpe.kml.parser.v21.coordinates.CoordinatesTypeIterator;
import org.gvsig.gpe.kml.parser.v21.coordinates.LatLonAltBoxIterator;
import org.gvsig.gpe.kml.parser.v21.features.DocumentBinding;
import org.gvsig.gpe.kml.parser.v21.features.ElementBinding;
import org.gvsig.gpe.kml.parser.v21.features.FeatureBinding;
import org.gvsig.gpe.kml.parser.v21.features.FolderBinding;
import org.gvsig.gpe.kml.parser.v21.features.LookAtBinding;
import org.gvsig.gpe.kml.parser.v21.features.MetadataBinding;
import org.gvsig.gpe.kml.parser.v21.features.PlaceMarketBinding;
import org.gvsig.gpe.kml.parser.v21.features.StyleBinding;
import org.gvsig.gpe.kml.parser.v21.geometries.DoubleBinding;
import org.gvsig.gpe.kml.parser.v21.geometries.GeometryBinding;
import org.gvsig.gpe.kml.parser.v21.geometries.InnerBoundaryIsBinding;
import org.gvsig.gpe.kml.parser.v21.geometries.LineStringTypeBinding;
import org.gvsig.gpe.kml.parser.v21.geometries.LinearRingBinding;
import org.gvsig.gpe.kml.parser.v21.geometries.MultiGeometryBinding;
import org.gvsig.gpe.kml.parser.v21.geometries.OuterBoundaryIsBinding;
import org.gvsig.gpe.kml.parser.v21.geometries.PointTypeBinding;
import org.gvsig.gpe.kml.parser.v21.geometries.PolygonTypeBinding;
import org.gvsig.gpe.kml.parser.v21.geometries.RegionBinding;
import org.gvsig.gpe.kml.parser.v21.header.HeaderBinding;


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
public class Kml2_1_BindingProfile extends DefaultBindingProfile{
	protected CoordinatesTypeIterator coordinatesTypeBinding = null;
	protected DocumentBinding documentBinding = null;
	protected DoubleBinding doubleBinding = null;
	protected ElementBinding elementBinding = null;
	protected FeatureBinding featureBinding = null;
	protected FolderBinding folderBinding = null;
	protected GeometryBinding geometryBinding = null;
	protected HeaderBinding headerBinding = null;
	protected InnerBoundaryIsBinding innerBoundaryIsBinding = null;
	protected LatLonAltBoxIterator latLonAltBoxBinding = null;
	protected LineStringTypeBinding lineStringTypeBinding = null;
	protected LinearRingBinding linearRingBinding = null;
	protected LookAtBinding lookAtBinding = null;
	protected MetadataBinding metadataBinding = null;
	protected MultiGeometryBinding multiGeometryBinding= null;
	protected OuterBoundaryIsBinding outerBoundaryIsBinding = null;
	protected PlaceMarketBinding placeMarketBinding = null;
	protected PointTypeBinding pointTypeBinding = null;
	protected PolygonTypeBinding polygonTypeBinding = null;
	protected RegionBinding regionBinding = null;
	protected StyleBinding styleBinding = null;
	

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.kml.reader.bindings.profiles.IBindingProfile#getCoordinatesTypeBinding()
	 */
	public CoordinatesTypeIterator getCoordinatesTypeBinding() {
		if (coordinatesTypeBinding == null){
			coordinatesTypeBinding = new CoordinatesTypeIterator();
		}
		return coordinatesTypeBinding;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.kml.reader.bindings.profiles.IBindingProfile#getDocumentBinding()
	 */
	public DocumentBinding getDocumentBinding() {
		if (documentBinding == null){
			documentBinding = new DocumentBinding();
		}
		return documentBinding;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.kml.reader.bindings.profiles.IBindingProfile#getDoubleBinding()
	 */
	public DoubleBinding getDoubleBinding() {
		if (doubleBinding == null){
			doubleBinding = new DoubleBinding();
		}
		return doubleBinding;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.kml.reader.bindings.profiles.IBindingProfile#getElementBinding()
	 */
	public ElementBinding getElementBinding() {
		if (elementBinding == null){
			elementBinding = new ElementBinding();
		}
		return elementBinding;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.kml.reader.bindings.profiles.IBindingProfile#getFeatureBinding()
	 */
	public FeatureBinding getFeatureBinding() {
		if (featureBinding == null){
			featureBinding = new FeatureBinding();
		}
		return featureBinding;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.kml.reader.bindings.profiles.IBindingProfile#getFolderBinding()
	 */
	public FolderBinding getFolderBinding() {
		if (folderBinding == null){
			folderBinding = new FolderBinding();
		}
		return folderBinding;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.kml.reader.bindings.profiles.IBindingProfile#getGeometryBinding()
	 */
	public GeometryBinding getGeometryBinding() {
		if (geometryBinding == null){
			geometryBinding = new GeometryBinding();
		}
		return geometryBinding;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.kml.reader.bindings.profiles.IBindingProfile#getHeaderBinding()
	 */
	public HeaderBinding getHeaderBinding() {
		if (headerBinding == null){
			headerBinding = new HeaderBinding();
		}
		return headerBinding;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.kml.reader.bindings.profiles.IBindingProfile#getInnerBoundaryIsBinding()
	 */
	public InnerBoundaryIsBinding getInnerBoundaryIsBinding() {
		if (innerBoundaryIsBinding == null){
			innerBoundaryIsBinding = new InnerBoundaryIsBinding();
		}
		return innerBoundaryIsBinding;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.kml.reader.bindings.profiles.IBindingProfile#getLatLonAltBoxBinding()
	 */
	public LatLonAltBoxIterator getLatLonAltBoxBinding() {
		if (latLonAltBoxBinding == null){
			latLonAltBoxBinding = new LatLonAltBoxIterator();
		}
		return latLonAltBoxBinding;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.kml.reader.bindings.profiles.IBindingProfile#getLineStringTypeBinding()
	 */
	public LineStringTypeBinding getLineStringTypeBinding() {
		if (lineStringTypeBinding == null){
			lineStringTypeBinding = new LineStringTypeBinding();
		}
		return lineStringTypeBinding;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.kml.reader.bindings.profiles.IBindingProfile#getLinearRingBinding()
	 */
	public LinearRingBinding getLinearRingBinding() {
		if (linearRingBinding == null){
			linearRingBinding = new LinearRingBinding();
		}
		return linearRingBinding;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.kml.reader.bindings.profiles.IBindingProfile#getLookAtBinding()
	 */
	public LookAtBinding getLookAtBinding() {
		if (lookAtBinding == null){
			lookAtBinding = new LookAtBinding();
		}
		return lookAtBinding;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.kml.reader.bindings.profiles.IBindingProfile#getMetadataBinding()
	 */
	public MetadataBinding getMetadataBinding() {
		if (metadataBinding == null){
			metadataBinding = new MetadataBinding();
		}
		return metadataBinding;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.kml.reader.bindings.profiles.IBindingProfile#getMultiGeometryBinding()
	 */
	public MultiGeometryBinding getMultiGeometryBinding() {
		if (multiGeometryBinding == null){
			multiGeometryBinding = new MultiGeometryBinding();
		}
		return multiGeometryBinding;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.kml.reader.bindings.profiles.IBindingProfile#getOuterBoundaryIsBinding()
	 */
	public OuterBoundaryIsBinding getOuterBoundaryIsBinding() {
		if (outerBoundaryIsBinding == null){
			outerBoundaryIsBinding = new OuterBoundaryIsBinding();
		}
		return outerBoundaryIsBinding;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.kml.reader.bindings.profiles.IBindingProfile#getPlaceMarketBinding()
	 */
	public PlaceMarketBinding getPlaceMarketBinding() {
		if (placeMarketBinding == null){
			placeMarketBinding = new PlaceMarketBinding();
		}
		return placeMarketBinding;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.kml.reader.bindings.profiles.IBindingProfile#getPointTypeBinding()
	 */
	public PointTypeBinding getPointTypeBinding() {
		if (pointTypeBinding == null){
			pointTypeBinding = new PointTypeBinding();
		}
		return pointTypeBinding;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.kml.reader.bindings.profiles.IBindingProfile#getPolygonTypeBinding()
	 */
	public PolygonTypeBinding getPolygonTypeBinding() {
		if (polygonTypeBinding == null){
			polygonTypeBinding = new PolygonTypeBinding();
		}
		return polygonTypeBinding;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.kml.reader.bindings.profiles.IBindingProfile#getRegionBinding()
	 */
	public RegionBinding getRegionBinding() {
		if (regionBinding == null){
			regionBinding = new RegionBinding();
		}
		return regionBinding;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.kml.reader.bindings.profiles.IBindingProfile#getStyleBinding()
	 */
	public StyleBinding getStyleBinding() {
		if (styleBinding == null){
			styleBinding = new StyleBinding();
		}
		return styleBinding;
	}
}
