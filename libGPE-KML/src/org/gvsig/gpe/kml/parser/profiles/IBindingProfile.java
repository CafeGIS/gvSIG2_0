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
public interface IBindingProfile {
	
	public DocumentBinding getDocumentBinding();
	
	public ElementBinding getElementBinding();
	
	public FeatureBinding getFeatureBinding();
	
	public FolderBinding getFolderBinding();
	
	public LookAtBinding getLookAtBinding();
	
	public MetadataBinding getMetadataBinding();
	
	public PlaceMarketBinding getPlaceMarketBinding();
	
	public StyleBinding getStyleBinding();
	
	public CoordinatesTypeIterator getCoordinatesTypeBinding();
	
	public DoubleBinding getDoubleBinding();
	
	public GeometryBinding getGeometryBinding();
	
	public InnerBoundaryIsBinding getInnerBoundaryIsBinding();
	
	public LatLonAltBoxIterator getLatLonAltBoxBinding();
	
	public LinearRingBinding getLinearRingBinding();
	
	public LineStringTypeBinding getLineStringTypeBinding();
	
	public MultiGeometryBinding getMultiGeometryBinding();
	
	public OuterBoundaryIsBinding getOuterBoundaryIsBinding();
	
	public PointTypeBinding getPointTypeBinding();
	
	public PolygonTypeBinding getPolygonTypeBinding();
	
	public RegionBinding getRegionBinding();
	
	public HeaderBinding getHeaderBinding();	
}
