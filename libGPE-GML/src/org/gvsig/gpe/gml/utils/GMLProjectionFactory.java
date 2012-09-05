package org.gvsig.gpe.gml.utils;

import org.gvsig.compat.CompatLocator;
import org.gvsig.gpe.GPELocator;
import org.gvsig.gpe.GPEManager;
import org.gvsig.gpe.gml.GmlProperties;
import org.gvsig.gpe.parser.GPEErrorHandler;
import org.gvsig.gpe.parser.IGPEErrorHandler;
import org.gvsig.gpe.warnings.SrsUnknownWarning;

/* gvSIG. Sistema de InformaciÛn Geogr·fica de la Generalitat Valenciana
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
 *   Av. Blasco Ib·Òez, 50
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
 * $Id: GMLProjectionFactory.java 350 2008-01-09 12:53:07Z jpiera $
 * $Log$
 * Revision 1.4  2007/05/18 10:41:01  csanchez
 * Actualizaci√≥n libGPE-GML eliminaci√≥n de clases inecesarias
 *
 * Revision 1.3  2007/05/14 11:18:51  jorpiell
 * ProjectionFactory updated
 *
 * Revision 1.2  2007/04/12 17:06:44  jorpiell
 * First GML writing tests
 *
 * Revision 1.1  2007/04/12 10:23:41  jorpiell
 * Add some writers and the GPEXml parser
 *
 *
 */
/**
 * This class is used to convert a projection written 
 * using the EPSG projection to the GML projection
 * format
 * @author Jorge Piera LLodr· (jorge.piera@iver.es)
 */
public class GMLProjectionFactory {
	private static GPEManager gpeManager = GPELocator.getGPEManager();
	
	/**
	 * This method is used to get the GML spatial reference
	 * system (srs) calculated from a GPE srs.
	 * @param srs
	 * GPE srs
	 * @param errorHandler
	 * The error handler
	 * @return
	 * GML srs
	 */
	public static String fromGPEToGML(String srs, GPEErrorHandler errorHandler){
		if (srs != null){
			//If the SRS's are not based on XML the srs 
			//will be the same
			if (!gpeManager.getBooleanProperty(GmlProperties.SRS_BASED_ON_XML)){
				return srs;
			}
			String[] parts = CompatLocator.getStringUtils().split(srs,":");
			//String[] parts = org.gvsig.gpe.utils.StringUtils.splitString(srs,":");
			//String[] parts = srs.split(":");
			if (parts.length == 2){
				if (parts[0].compareTo(GMLTags.SRS_EPSG_NAME) == 0){
					return GMLTags.SRS + GMLTags.SRS_EPSG + "#" + parts[1];
				}
			}else if(parts.length == 1){
				//If the EPSG prefix is not found 
				return GMLTags.SRS + GMLTags.SRS_EPSG + "#"  + parts[0];
				
			}
		}	
		errorHandler.addWarning(new SrsUnknownWarning(srs));
		return GMLTags.SRS_UNKNOWN;	
	}
	
	/**
	 * This method is used to get the GPE spatial reference
	 * system (srs) calculated from a GML srs.
	 * @param srs
	 * GML srs
	 * @param errorHandler
	 * The error handler
	 * @return
	 * GPE srs
	 */
	public static String fromGMLToGPE(String srs,IGPEErrorHandler errorHandler){
		if (srs != null){
			String[] parts = CompatLocator.getStringUtils().split(srs,"#");
			//String[] parts = org.gvsig.gpe.utils.StringUtils.splitString(srs,"#");
			//String[] parts = srs.split("#");
			if (parts.length == 2){
				if (parts[0].compareTo(GMLTags.SRS + GMLTags.SRS_EPSG) == 0){
					return GMLTags.SRS_EPSG_NAME + ":" + parts[1];
				}
			}
		}
		errorHandler.addWarning(new SrsUnknownWarning(srs));
		return GMLTags.SRS_UNKNOWN;	
	}
}
