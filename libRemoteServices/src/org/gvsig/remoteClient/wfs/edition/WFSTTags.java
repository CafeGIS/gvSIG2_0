package org.gvsig.remoteClient.wfs.edition;
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
public class WFSTTags {
	public static final String XML_ROOT = "<?xml version=\"1.0\" ?>";
	public static final String XMLNS = "xmlns";
	public static final String XML_NAMESPACE_PREFIX = "xsi";
	public static final String XML_NAMESPACE = "http://www.w3.org/2001/XMLSchema-instance";
	public static final String XML_SCHEMALOCATION = "schemaLocation";
	public static final String WFS_NAMESPACE_PREFIX = "wfs";
	public static final String WFS_NAMESPACE = "http://www.opengis.net/wfs";
	public static final String OGC_NAMESPACE_PREFIX = "ogc";
	public static final String OGC_NAMESPACE = "http://www.opengis.net/ogc";
	public static final String GML_NAMESPACE_PREFIX = "gml";
	public static final String GML_NAMESPACE = "http://www.opengis.net/gml";
	
	public static final String WFST_LOCKID = "LockId";
	public static final String WFST_LOCK = "Lock";
	public static final String WFST_FEATURESLOCKED = "FeaturesLocked";
	public static final String WFST_FEATURESID = "FeatureId";	
	public static final String WFST_SERVICE = "service";
	public static final String WFST_TRANSACTIONRESULT = "TransactionResult";
	public static final String WFST_TRANSACTIONRESPONSE = "WFS_TransactionResponse";
	public static final String WFST_TRANSACTIONMESSAGE = "Message";
	public static final String WFST_STATUS = "Status";
	public static final String WFST_SUCCESS = "SUCCESS";
	public static final String WFST_FAILED = "FAILED";
	public static final String WFST_RELEASEACTION = "releaseAction";
	public static final String WFST_EXPIRYTIME = "expiry";
	
	public static final String WFS_QUERY = "Query";
	public static final String WFS_FILTER = "Filter";
}
