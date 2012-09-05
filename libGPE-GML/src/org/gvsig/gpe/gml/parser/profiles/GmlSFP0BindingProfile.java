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
import org.gvsig.gpe.gml.parser.v2.geometries.BoundedByTypeBinding;
import org.gvsig.gpe.gml.parser.v2.geometries.GeometryBinding;
import org.gvsig.gpe.gml.parser.v2.geometries.LineStringTypeBinding;
import org.gvsig.gpe.gml.parser.v2.geometries.LinearRingTypeBinding;
import org.gvsig.gpe.gml.parser.v2.geometries.MultiPolygonTypeBinding;
import org.gvsig.gpe.gml.parser.v2.geometries.PointTypeBinding;
import org.gvsig.gpe.gml.parser.v2.geometries.PolygonMemberTypeBinding;
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
public class GmlSFP0BindingProfile extends Gml2BindingProfile{
	protected PosTypeIterator posTypeIterator = new PosTypeIterator();
	protected EnvelopeTypeBinding envelopeTypeBinding = new EnvelopeTypeBinding();
	protected LowerCornerTypeBinding lowerCornerTypeBinding = new LowerCornerTypeBinding();
	protected UpperCornerTypeBinding upperCornerTypeBinding = new UpperCornerTypeBinding();
	protected CurvePropertyTypeBinding curvePropertyTypeBinding = new CurvePropertyTypeBinding();
	protected PosListTypeIterator posListTypeIterator = new PosListTypeIterator();
	protected CurveTypeBinding curveTypeBinding = new CurveTypeBinding();
	protected MultiCurveTypeBinding multiCurveTypeBinding = new MultiCurveTypeBinding();
	protected SegmentsTypeBinding segmentsTypeBinding = new SegmentsTypeBinding();
	protected LinestringSegmentTypeBinding linestringSegmentTypeBinding = new LinestringSegmentTypeBinding();
	protected ExteriorTypeBinding exteriorTypeBinding = new ExteriorTypeBinding();
	protected InteriorTypeBinding interiorTypeBinding = new InteriorTypeBinding();
	protected org.gvsig.gpe.gml.parser.sfp0.geometries.BoundedByTypeBinding boundedByTypeBinding = new org.gvsig.gpe.gml.parser.sfp0.geometries.BoundedByTypeBinding();
	protected org.gvsig.gpe.gml.parser.sfp0.geometries.GeometryBinding geometryBinding = new org.gvsig.gpe.gml.parser.sfp0.geometries.GeometryBinding();
	protected org.gvsig.gpe.gml.parser.sfp0.geometries.PointTypeBinding pointTypeBinding = new org.gvsig.gpe.gml.parser.sfp0.geometries.PointTypeBinding();
	protected org.gvsig.gpe.gml.parser.sfp0.geometries.LinearRingTypeBinding linearRingTypeBinding = new org.gvsig.gpe.gml.parser.sfp0.geometries.LinearRingTypeBinding();
	protected org.gvsig.gpe.gml.parser.sfp0.geometries.LineStringTypeBinding lineStringTypeBinding = new org.gvsig.gpe.gml.parser.sfp0.geometries.LineStringTypeBinding();
	protected org.gvsig.gpe.gml.parser.sfp0.geometries.MultiPolygonTypeBinding multiPolygonTypeBinding = new org.gvsig.gpe.gml.parser.sfp0.geometries.MultiPolygonTypeBinding();
	protected org.gvsig.gpe.gml.parser.sfp0.geometries.PolygonMemberTypeBinding polygonMemberTypeBinding = new org.gvsig.gpe.gml.parser.sfp0.geometries.PolygonMemberTypeBinding();
	protected org.gvsig.gpe.gml.parser.sfp0.geometries.PolygonTypeBinding polygonTypeBinding = new org.gvsig.gpe.gml.parser.sfp0.geometries.PolygonTypeBinding();
	
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.gml.profiles.DefaultProfile#getPosTypeBinding()
	 */
	public PosTypeIterator getPosTypeBinding() {
		return posTypeIterator;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.gml.profiles.IProfile#getEnvelopeTypeBinding()
	 */
	public EnvelopeTypeBinding getEnvelopeTypeBinding() {
		return envelopeTypeBinding;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.gml.profiles.IProfile#getLowerCornerTypeBinding()
	 */
	public LowerCornerTypeBinding getLowerCornerTypeBinding() {
		return lowerCornerTypeBinding;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.gml.profiles.IProfile#getLowerCornerTypeBinding()
	 */
	public UpperCornerTypeBinding getUpperCornerTypeBinding() {
		return upperCornerTypeBinding;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.gml.profiles.DefaultProfile#getCurvePropertyTypeBinding()
	 */
	public CurvePropertyTypeBinding getCurvePropertyTypeBinding() {
		return curvePropertyTypeBinding;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.gml.profiles.IProfile#getPosListTypeBinding()
	 */
	public PosListTypeIterator getPosListTypeBinding() {
		return posListTypeIterator;
	}
	

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.gml.profiles.IProfile#getCurveTypeBinding()
	 */
	public CurveTypeBinding getCurveTypeBinding() {
		return curveTypeBinding;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.gml.profiles.IProfile#getSegmentsTypeBinding()
	 */
	public SegmentsTypeBinding getSegmentsTypeBinding() {
		return segmentsTypeBinding;
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
	 * @see org.gvsig.gpe.gml.profiles.IProfile#getBoundedByTypeBinding()
	 */
	public BoundedByTypeBinding getBoundedByTypeBinding() {
		return boundedByTypeBinding;
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
	 * @see org.gvsig.gpe.gml.profiles.Gml2Profile#getLinearRingTypeBinding()
	 */
	public LinearRingTypeBinding getLinearRingTypeBinding() {
		return linearRingTypeBinding;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.gml.profiles.Gml2Profile#getLineStringTypeBinding()
	*/
	public LineStringTypeBinding getLineStringTypeBinding() {
		return lineStringTypeBinding;
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
	 * @see org.gvsig.gpe.gml.profiles.Gml2Profile#getPolygonMemberTypeBinding()
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
	 * @see org.gvsig.gpe.gml.profiles.DefaultProfile#getExteriorTypeBinding()
	 */
	public ExteriorTypeBinding getExteriorTypeBinding() {
		return exteriorTypeBinding;
	}
	
	/* (non-Javadoc)
	 * @see org.gvsig.gpe.gml.profiles.IProfile#getInteriorTypeBinding()
	 */
	public InteriorTypeBinding getInteriorTypeBinding() {
		return interiorTypeBinding;
	}
	
	/* (non-Javadoc)
	 * @see org.gvsig.gpe.gml.parser.profiles.DefaultBindingProfile#getMultiCurveTypeBinding()
	 */
	public MultiCurveTypeBinding getMultiCurveTypeBinding() {
		return multiCurveTypeBinding;
	}
}
