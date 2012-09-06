/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2005 IVER T.I. and Generalitat Valenciana.
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
/**
 *
 */
package org.gvsig.remoteClient.utils;

/**
 * @author jaume
 *
 */
public class DescribeCoverageTags {
	private DescribeCoverageTags() {};
    public static final String COVERAGE_DESCRIPTION = "CoverageDescription";
    public static final String COVERAGE_OFFERING = "CoverageOffering";
    public static final String NAME = "name";
    public static final String LABEL = "label";
    public static final String LONLAT_ENVELOPE = "lonLatEnvelope";
	public static final String WGS84 = "WGS84(DD)"; // Decimal Degree
	public static final String GML_ENVELOPE = "gml:Envelope";
	public static final String GML_POS = "gml:pos";
	public static final String SRSNAME = "srsName";

	public static final String DOMAINSET = "domainSet";
	public static final String SPATIALDOMAIN = "spatialDomain";
	public static final String GRID = "gml:Grid";
	public static final String RECTIFIEDGRID = "gml:RectifiedGrid";
	public static final String GML_GRIDENVELOPE = "gml:GridEnvelope";
	public static final String GML_LIMITS = "gml:limits";
	public static final String GML_AXISNAME = "gml:axisName";
	public static final String GML_ORIGIN = "gml:origin";
	public static final String DIMENSION = "dimension";
	public static final String GML_LOW = "gml:low";
	public static final String GML_HIGH = "gml:high";
	public static final String OFFSETVECTOR = "gml:offsetVector";
	public static final String TEMPORALDOMAIN = "temporalDomain";
	public static final String GML_TIMEPOSITION = "gml:timePosition";
	public static final String TIMEPERIOD = "timePeriod";
	public static final String BEGINPOSITION = "beginPosition";
	public static final String ENDPOSITION = "endPosition";
	public static final String TIMERESOLUTION = "timeResolution";

	public static final String RANGESET = "RangeSet";
	/* TODO this can cause eventually problems if in a future the "rangeSet" (not "RangeSet")
	 * has more than one sub RangeSet subelement since I'm assuming it and a "rangeSet" tag is
	 * simply ignored since it has no effect if it just contains one, and only one, RangeSet
	 * subelement.
	 */
	public static final String AXISDESCRIPTION = "AxisDescription" ;
	public static final String VALUES = "values";
	public static final String SINGLEVALUE = "singleValue";
	public static final String INTERVAL = "interval";
	public static final String DEFAULT = "default";
	public static final String NULLVALUES = "NullValues";

	public static final String SUPPORTED_CRSS = "supportedCRSs";
	public static final String REQUEST_RESPONSE_CRSS = "requestResponseCRSs";
	public static final String NATIVE_CRS = "nativeCRSs";
	public static final String REQUEST_CRSS = "requestCRSs";
	public static final String RESPONSE_CRSS = "responseCRSs";

	public static final String SUPPORTED_FORMATS = "supportedFormats";
	public static final String NATIVE_FORMAT = "nativeFormat";
	public static final String FORMATS = "formats";
	public static final String SUPPORTED_INTERPOLATIONS = "supportedInterpolations";
	public static final String INTERPOLATION_METHOD = "interpolationMethod";


}
