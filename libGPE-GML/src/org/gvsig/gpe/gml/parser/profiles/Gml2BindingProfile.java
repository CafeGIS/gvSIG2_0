package org.gvsig.gpe.gml.parser.profiles;


import org.gvsig.gpe.gml.parser.v2.coordinates.CoordTypeIterator;
import org.gvsig.gpe.gml.parser.v2.coordinates.CoordinatesTypeIterator;
import org.gvsig.gpe.gml.parser.v2.features.ElementTypeBinding;
import org.gvsig.gpe.gml.parser.v2.features.FeatureCollectionBinding;
import org.gvsig.gpe.gml.parser.v2.features.FeatureMemberTypeBinding;
import org.gvsig.gpe.gml.parser.v2.features.FeatureMembersTypeBinding;
import org.gvsig.gpe.gml.parser.v2.features.FeatureTypeBinding;
import org.gvsig.gpe.gml.parser.v2.geometries.BoundedByTypeBinding;
import org.gvsig.gpe.gml.parser.v2.geometries.BoxTypeBinding;
import org.gvsig.gpe.gml.parser.v2.geometries.GeometryBinding;
import org.gvsig.gpe.gml.parser.v2.geometries.GeometryMemberTypeBinding;
import org.gvsig.gpe.gml.parser.v2.geometries.GeometryPropertyTypeBinding;
import org.gvsig.gpe.gml.parser.v2.geometries.InnerBoundaryIsTypeBinding;
import org.gvsig.gpe.gml.parser.v2.geometries.LineStringMemberTypeBinding;
import org.gvsig.gpe.gml.parser.v2.geometries.LineStringPropertyTypeBinding;
import org.gvsig.gpe.gml.parser.v2.geometries.LineStringTypeBinding;
import org.gvsig.gpe.gml.parser.v2.geometries.LinearRingTypeBinding;
import org.gvsig.gpe.gml.parser.v2.geometries.MultiGeometryPropertyTypeBinding;
import org.gvsig.gpe.gml.parser.v2.geometries.MultiGeometryTypeBinding;
import org.gvsig.gpe.gml.parser.v2.geometries.MultiLineStringPropertyTypeBinding;
import org.gvsig.gpe.gml.parser.v2.geometries.MultiLineStringTypeBinding;
import org.gvsig.gpe.gml.parser.v2.geometries.MultiPointPropertyTypeBinding;
import org.gvsig.gpe.gml.parser.v2.geometries.MultiPointTypeBinding;
import org.gvsig.gpe.gml.parser.v2.geometries.MultiPolygonPropertyTypeBinding;
import org.gvsig.gpe.gml.parser.v2.geometries.MultiPolygonTypeBinding;
import org.gvsig.gpe.gml.parser.v2.geometries.OuterBoundaryIsTypeBinding;
import org.gvsig.gpe.gml.parser.v2.geometries.PointMemberTypeBinding;
import org.gvsig.gpe.gml.parser.v2.geometries.PointPropertyTypeBinding;
import org.gvsig.gpe.gml.parser.v2.geometries.PointTypeBinding;
import org.gvsig.gpe.gml.parser.v2.geometries.PolygonMemberTypeBinding;
import org.gvsig.gpe.gml.parser.v2.geometries.PolygonPropertyTypeBinding;
import org.gvsig.gpe.gml.parser.v2.geometries.PolygonTypeBinding;

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
public class Gml2BindingProfile extends DefaultBindingProfile {
	protected FeatureCollectionBinding featureCollectionBinding = new FeatureCollectionBinding();
	protected ElementTypeBinding elementTypeBinding = new ElementTypeBinding();
	protected GeometryBinding geometryBinding = new GeometryBinding();
	protected GeometryPropertyTypeBinding geometryPropertyTypeBinding = new GeometryPropertyTypeBinding();
	protected PointPropertyTypeBinding pointPropertyTypeBinding = new PointPropertyTypeBinding();
	protected LineStringPropertyTypeBinding lineStringPropertyTypeBinding = new LineStringPropertyTypeBinding();
	protected PolygonPropertyTypeBinding polygonPropertyTypeBinding = new PolygonPropertyTypeBinding();
	protected MultiPointPropertyTypeBinding multiPointPropertyTypeBinding = new MultiPointPropertyTypeBinding();
	protected MultiLineStringPropertyTypeBinding multiLineStringPropertyTypeBinding = new MultiLineStringPropertyTypeBinding();
	protected MultiPolygonPropertyTypeBinding multiPolygonPropertyTypeBinding = new MultiPolygonPropertyTypeBinding();
	protected MultiGeometryPropertyTypeBinding multiGeometryPropertyTypeBinding = new MultiGeometryPropertyTypeBinding();
	protected BoundedByTypeBinding boundedByTypeBinding = new BoundedByTypeBinding();
	protected FeatureMemberTypeBinding featureMemberTypeBinding = new FeatureMemberTypeBinding();
	protected FeatureMembersTypeBinding featureMembersTypeBinding = new FeatureMembersTypeBinding();
	protected FeatureTypeBinding featureTypeBinding = new FeatureTypeBinding();
	protected BoxTypeBinding boxTypeBinding = new BoxTypeBinding();
	protected CoordinatesTypeIterator coordinatesTypeIterator = new CoordinatesTypeIterator();
	protected CoordTypeIterator coordTypeIterator = new CoordTypeIterator();
	protected PointMemberTypeBinding pointMemberTypeBinding = new PointMemberTypeBinding();
	protected PointTypeBinding pointTypeBinding = new PointTypeBinding();
	protected LineStringMemberTypeBinding lineStringMemberTypeBinding = new LineStringMemberTypeBinding();
	protected LineStringTypeBinding lineStringTypeBinding = new LineStringTypeBinding();
	protected LinearRingTypeBinding linearRingTypeBinding = new LinearRingTypeBinding();
	protected PolygonMemberTypeBinding polygonMemberTypeBinding = new PolygonMemberTypeBinding();
	protected PolygonTypeBinding polygonTypeBinding = new PolygonTypeBinding();
	protected GeometryMemberTypeBinding geometryMemberTypeBinding = new GeometryMemberTypeBinding();
	protected MultiPointTypeBinding multiPointTypeBinding = new MultiPointTypeBinding();
	protected MultiLineStringTypeBinding multiLineStringTypeBinding = new MultiLineStringTypeBinding();
	protected MultiPolygonTypeBinding multiPolygonTypeBinding = new MultiPolygonTypeBinding();
	protected MultiGeometryTypeBinding multiGeometryTypeBinding = new MultiGeometryTypeBinding();
	protected OuterBoundaryIsTypeBinding outerBoundaryIsTypeBinding = new OuterBoundaryIsTypeBinding();
	protected InnerBoundaryIsTypeBinding innerBoundaryIsTypeBinding = new InnerBoundaryIsTypeBinding();
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.gml.profiles.IProfile#getFeatureCollectionBinding()
	 */
	public FeatureCollectionBinding getFeatureCollectionBinding(){
		return featureCollectionBinding;
	}
		
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.gml.profiles.IProfile#getElementTypeBinding()
	 */
	public ElementTypeBinding getElementTypeBinding(){
		return elementTypeBinding;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.gml.profiles.IProfile#getGeometryBinding()
	 */
	public GeometryBinding getGeometryBinding() {
		return geometryBinding;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.gml.profiles.IProfile#getGeometryPropertyTypeBinding()
	 */
	public GeometryPropertyTypeBinding getGeometryPropertyTypeBinding() {
		return geometryPropertyTypeBinding;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.gml.profiles.IProfile#getLineStringPropertyTypeBinding()
	 */
	public LineStringPropertyTypeBinding getLineStringPropertyTypeBinding() {
		return lineStringPropertyTypeBinding;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.gml.profiles.IProfile#getMultiGeometryPropertyTypeBinding()
	 */
	public MultiGeometryPropertyTypeBinding getMultiGeometryPropertyTypeBinding() {
		return multiGeometryPropertyTypeBinding;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.gml.profiles.IProfile#getMultiLineStringPropertyTypeBinding()
	 */
	public MultiLineStringPropertyTypeBinding getMultiLineStringPropertyTypeBinding() {
		return multiLineStringPropertyTypeBinding;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.gml.profiles.IProfile#getMultiPointPropertyTypeBinding()
	 */
	public MultiPointPropertyTypeBinding getMultiPointPropertyTypeBinding() {
		return multiPointPropertyTypeBinding ;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.gml.profiles.IProfile#getMultiPolygonPropertyTypeBinding()
	 */
	public MultiPolygonPropertyTypeBinding getMultiPolygonPropertyTypeBinding() {
		return multiPolygonPropertyTypeBinding;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.gml.profiles.IProfile#getPointPropertyTypeBinding()
	 */
	public PointPropertyTypeBinding getPointPropertyTypeBinding() {
		return pointPropertyTypeBinding;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.gml.profiles.IProfile#getPolygonPropertyTypeBinding()
	 */
	public PolygonPropertyTypeBinding getPolygonPropertyTypeBinding() {
		return polygonPropertyTypeBinding;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.gml.profiles.IProfile#getBoundedByTypeBinding()
	 */
	public BoundedByTypeBinding getBoundedByTypeBinding() {
		return boundedByTypeBinding;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.gml.profiles.IProfile#getFeatureMemberTypeBinding()
	 */
	public FeatureMemberTypeBinding getFeatureMemberTypeBinding() {
		return featureMemberTypeBinding;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.gml.profiles.IProfile#getFeatureTypeBinding()
	 */
	public FeatureTypeBinding getFeatureTypeBinding() {
		return featureTypeBinding;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.gml.profiles.IProfile#getBoxTypeBinding()
	 */
	public BoxTypeBinding getBoxTypeBinding() {
		return boxTypeBinding;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.gml.profiles.IProfile#getCoordinatesTypeBinding()
	 */
	public CoordinatesTypeIterator getCoordinatesTypeBinding() {
		return coordinatesTypeIterator;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.gml.profiles.IProfile#getCoordTypeBinding()
	 */
	public CoordTypeIterator getCoordTypeBinding() {
		return coordTypeIterator;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.gml.profiles.IProfile#getGeometryMemberTypeBinding()
	 */
	public GeometryMemberTypeBinding getGeometryMemberTypeBinding() {
		return geometryMemberTypeBinding;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.gml.profiles.IProfile#getLineStringMemberTypeBinding()
	 */
	public LineStringMemberTypeBinding getLineStringMemberTypeBinding() {
		return lineStringMemberTypeBinding;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.gml.profiles.IProfile#getLineStringTypeBinding()
	 */
	public LineStringTypeBinding getLineStringTypeBinding() {
		return lineStringTypeBinding;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.gml.profiles.IProfile#getLinearRingTypeBinding()
	 */
	public LinearRingTypeBinding getLinearRingTypeBinding() {
		return linearRingTypeBinding;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.gml.profiles.IProfile#getMultiGeometryTypeBinding()
	 */
	public MultiGeometryTypeBinding getMultiGeometryTypeBinding() {
		return multiGeometryTypeBinding;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.gml.profiles.IProfile#getMultiLineStringTypeBinding()
	 */
	public MultiLineStringTypeBinding getMultiLineStringTypeBinding() {
		return multiLineStringTypeBinding;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.gml.profiles.IProfile#getMultiPointTypeBinding()
	 */
	public MultiPointTypeBinding getMultiPointTypeBinding() {
		return multiPointTypeBinding;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.gml.profiles.IProfile#getMultiPolygonTypeBinding()
	 */
	public MultiPolygonTypeBinding getMultiPolygonTypeBinding() {
		return multiPolygonTypeBinding;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.gml.profiles.IProfile#getPointMemberTypeBinding()
	 */
	public PointMemberTypeBinding getPointMemberTypeBinding() {
		return pointMemberTypeBinding;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.gml.profiles.IProfile#getPointTypeBinding()
	 */
	public PointTypeBinding getPointTypeBinding() {
		return pointTypeBinding;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.gml.profiles.IProfile#getPolygonMemberTypeBinding()
	 */
	public PolygonMemberTypeBinding getPolygonMemberTypeBinding() {
		return polygonMemberTypeBinding;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.gml.profiles.IProfile#getPolygonTypeBinding()
	 */
	public PolygonTypeBinding getPolygonTypeBinding() {
		return polygonTypeBinding;
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.gml.profiles.IProfile#getOuterBoundaryIsTypeBinding()
	 */
	public OuterBoundaryIsTypeBinding getOuterBoundaryIsTypeBinding() {
		return outerBoundaryIsTypeBinding;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.gml.profiles.IProfile#getInnerBoundaryIsTypeBinding()
	 */
	public InnerBoundaryIsTypeBinding getInnerBoundaryIsTypeBinding() {
		return innerBoundaryIsTypeBinding;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.gml.parser.profiles.IBindingProfile#getFeatureMembersTypeBinding()
	 */
	public FeatureMembersTypeBinding getFeatureMembersTypeBinding() {
		return featureMembersTypeBinding;
	}	
}

