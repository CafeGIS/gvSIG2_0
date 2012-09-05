package org.gvsig.gpe.gml.parser.profiles;

import org.gvsig.gpe.gml.parser.sfp0.coordinates.PosListTypeIterator;
import org.gvsig.gpe.gml.parser.sfp0.coordinates.PosTypeIterator;
import org.gvsig.gpe.gml.parser.sfp0.geometries.CurvePropertyTypeBinding;
import org.gvsig.gpe.gml.parser.sfp0.geometries.CurveTypeBinding;
import org.gvsig.gpe.gml.parser.sfp0.geometries.EnvelopeTypeBinding;
import org.gvsig.gpe.gml.parser.sfp0.geometries.ExteriorTypeBinding;
import org.gvsig.gpe.gml.parser.sfp0.geometries.InteriorTypeBinding;
import org.gvsig.gpe.gml.parser.sfp0.geometries.LinestringSegmentTypeBinding;
import org.gvsig.gpe.gml.parser.sfp0.geometries.LowerCornerTypeBinding;
import org.gvsig.gpe.gml.parser.sfp0.geometries.MultiCurveTypeBinding;
import org.gvsig.gpe.gml.parser.sfp0.geometries.SegmentsTypeBinding;
import org.gvsig.gpe.gml.parser.sfp0.geometries.UpperCornerTypeBinding;
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
public interface IBindingProfile{

	public FeatureCollectionBinding getFeatureCollectionBinding();

	public ElementTypeBinding getElementTypeBinding();

	public GeometryBinding getGeometryBinding();

	public GeometryPropertyTypeBinding getGeometryPropertyTypeBinding();

	public PointPropertyTypeBinding getPointPropertyTypeBinding();

	public LineStringPropertyTypeBinding getLineStringPropertyTypeBinding();

	public PolygonPropertyTypeBinding getPolygonPropertyTypeBinding();

	public MultiPointPropertyTypeBinding getMultiPointPropertyTypeBinding();

	public MultiLineStringPropertyTypeBinding getMultiLineStringPropertyTypeBinding();

	public MultiPolygonPropertyTypeBinding getMultiPolygonPropertyTypeBinding();

	public MultiGeometryPropertyTypeBinding getMultiGeometryPropertyTypeBinding();

	public BoundedByTypeBinding getBoundedByTypeBinding();

	public FeatureMemberTypeBinding getFeatureMemberTypeBinding();
	
	public FeatureMembersTypeBinding getFeatureMembersTypeBinding();

	public FeatureTypeBinding getFeatureTypeBinding();

	public BoxTypeBinding getBoxTypeBinding();

	public EnvelopeTypeBinding getEnvelopeTypeBinding();

	public CoordinatesTypeIterator getCoordinatesTypeBinding();

	public CoordTypeIterator getCoordTypeBinding();

	public PosTypeIterator getPosTypeBinding();

	public LowerCornerTypeBinding getLowerCornerTypeBinding();

	public UpperCornerTypeBinding getUpperCornerTypeBinding();

	public CurvePropertyTypeBinding getCurvePropertyTypeBinding();

	public PointMemberTypeBinding getPointMemberTypeBinding();

	public PointTypeBinding getPointTypeBinding();

	public LineStringMemberTypeBinding getLineStringMemberTypeBinding();

	public LineStringTypeBinding getLineStringTypeBinding();

	public LinearRingTypeBinding getLinearRingTypeBinding();

	public PolygonMemberTypeBinding getPolygonMemberTypeBinding();

	public PolygonTypeBinding getPolygonTypeBinding();

	public GeometryMemberTypeBinding getGeometryMemberTypeBinding();

	public MultiPointTypeBinding getMultiPointTypeBinding();

	public MultiLineStringTypeBinding getMultiLineStringTypeBinding();

	public MultiPolygonTypeBinding getMultiPolygonTypeBinding();
	
	public MultiCurveTypeBinding getMultiCurveTypeBinding();

	public MultiGeometryTypeBinding getMultiGeometryTypeBinding();

	public PosListTypeIterator getPosListTypeBinding();

	public CurveTypeBinding getCurveTypeBinding();

	public SegmentsTypeBinding getSegmentsTypeBinding();

	public OuterBoundaryIsTypeBinding getOuterBoundaryIsTypeBinding();

	public InnerBoundaryIsTypeBinding getInnerBoundaryIsTypeBinding();

	public LinestringSegmentTypeBinding getLinestringSegmentTypeBinding();

	public ExteriorTypeBinding getExteriorTypeBinding();

	public InteriorTypeBinding getInteriorTypeBinding();
}
