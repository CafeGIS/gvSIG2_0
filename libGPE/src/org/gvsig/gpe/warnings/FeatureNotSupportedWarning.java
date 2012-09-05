package org.gvsig.gpe.warnings;

import java.util.Hashtable;
import java.util.Map;

import org.gvsig.tools.exception.BaseException;

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
 * $Id: FeatureNotSupportedWarning.java 190 2007-11-21 12:53:42Z csanchez $
 * $Log$
 * Revision 1.2  2007/06/19 10:34:51  jorpiell
 * Add some comments and creates a warning when an operation is not supported
 *
 * Revision 1.1  2007/05/16 12:07:07  jorpiell
 * New exceptions added
 *
 *
 */
/**
 * This warning is throwed when an application try to write
 * a feature that the current format doesn't supports it.
 * A feature in this case is not a "geographic feature":
 * is a detail: A layer with bbox, a leyer name... * 
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public class FeatureNotSupportedWarning extends BaseException{
	private static final long serialVersionUID = 8532995170438868404L;
	private String feature = null;
	private String parserName = null;
	public static final String LAYERWITHNAME = "Layer with name";
	public static final String LAYERWITHDESCRIPTION = "Layer with description";
	public static final String LAYERWITHBBOX = "Layer with bbox";
	public static final String LAYERWITHSRS = "Layer with srs";
	public static final String FEATUEWITHBBOX = "Feature with bbox";
	public static final String FEATUREWITHELEMENTS = "Feature with elements";
	public static final String FEATUREWITHNAME = "Feature with name";
	public static final String FEATURECREATION = "Feature creation";
	public static final String FEATUREWITHGEOMETRY = "Feature with geometry";
	public static final String LAYERCREATION = "Layer creation";
	public static final String LAYERWITHCHILDREN = "Layer with children";
	public static final String LAYERWITHFEATURES = "Layer with features";
	public static final String ELEMENTCREATION = "Create an element";
	public static final String ELEMENTWITHCHILDREN = "Element with child elements";
	public static final String BBOXCREATION = "Create a bbox";
	public static final String POINTCREATION = "Create a point";
	public static final String LINESTRINGCREATION = "Create a lineString";
	public static final String LINEARRINGCREATION = "Create a linearRing";
	public static final String POLYGONCREATION = "Create a polygon";
	public static final String POLYGONWITHINNERPOLYGON = "Polygon with inner polygon";
	public static final String INNERBOUNDARYCREATION = "Create a polygon innerboundary";
	public static final String MULTIPOINTCREATION = "Create a multiPoint";
	public static final String MULTILINESTRINGCREATION = "Create a multiLineString";
	public static final String MULTIPOLYGONCREATION = "Create a multiPolygon";
	public static final String MULTIGEOMETRYCREATION = "Create a multiGeometry";
	/************** CURVE CREATIONS FOR GML 3 ***************/
	public static final String MULTICURVECREATION = "Create a multiCurve";
	public static final String CURVECREATION = "Create a Curve";	
	/**METADATA**/
	public static final String METADATACREATION = "Create metadata";
	public static final String FEATUREWITHMETADATA = "Feature with metadata";
	public static final String METADATAWITHCHILDREN = "Metadata with children";
	/**TIME**/
	public static final String TIMECREATION = "Create time";	
	public static final String FEATUREWITHTIME = "Feature with time";
	
	public FeatureNotSupportedWarning(String feature, String parserName){
		this.feature = feature;
		this.parserName = parserName;
		initialize();
	}

	public FeatureNotSupportedWarning(String feature){
		this.feature = feature;	
		initialize();
	}

	/**
	 * Initialize the properties
	 */
	private void initialize() {
		messageKey = "gpe_feature_not_supported_warning";
		if (parserName != null){
			formatString = "The feature '%(feature)' is not supported" +
			" by the %(parserName) parser. It could be by two" +
			" reasons: 1) The parser doesn't support this feature. " + 
			" 2) The format doesn't support the feature.";
		}else{
			formatString = "The feature '%(feature)' is not supported" +
			" by the consumer application."; 
		}
		code = serialVersionUID;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.tools.exception.BaseException#values()
	 */
	protected Map values() {
		Hashtable hash = new Hashtable();
		hash.put("feature", feature);
		if (parserName != null){
			hash.put("parserName", parserName);
		}
		return hash;
	}


}
