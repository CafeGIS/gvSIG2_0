package org.gvsig.catalog.protocols;

import java.net.URI;
import java.util.StringTokenizer;

import org.gvsig.catalog.drivers.GetRecordsReply;
import org.jzkit.search.provider.iface.SearchException;
import org.jzkit.search.util.ResultSet.IRResultSetException;


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
 * $Id: Z3950Protocol.java 561 2007-07-27 06:49:30 +0000 (Fri, 27 Jul 2007) jpiera $
 * $Log$
 * Revision 1.21.2.2.4.3  2007/07/23 07:14:25  jorpiell
 * Catalog refactoring
 *
 * Revision 1.21.2.2.4.2  2007/07/13 12:00:35  jorpiell
 * Add the posibility to add a new panel
 *
 * Revision 1.21.2.2.4.1  2007/07/10 11:18:04  jorpiell
 * Added the registers
 *
 * Revision 1.21.2.2  2006/11/15 00:08:08  jjdelcerro
 * *** empty log message ***
 *
 * Revision 1.23  2006/10/02 08:29:07  jorpiell
 * Modificados los cambios del Branch 10 al head
 *
 * Revision 1.21.2.1  2006/09/20 12:01:18  jorpiell
 * Se ha cambiado la versión de la jzkit, se ha incorporado la funcionalidad de cargar arcims
 *
 * Revision 1.22  2006/09/20 11:20:17  jorpiell
 * Se ha cambiado la versión de la librería jzkit de la 1 a la 2.0
 *
 *
 */
/**
 * @author Jorge Piera Llodrá (piera_jor@gva.es)
 */
public class Z3950Protocol {

	/**
	   * Makes a query
	   * @param url
	   * @param object
	   * @param firstRecord
	   * @return
	 * @throws IRResultSetException 
	 * @throws SearchException 
	   */
	  public GetRecordsReply doQuery(GetRecordsReply recordsReply, URI uri, Object object, int firstRecord){    
		  Z3950Connection connection = Z3950ConnectionFactory.getConnection(uri);
		  return connection.search(recordsReply,(String)object,firstRecord);
	 }  

	  
	  /**
	   * 
	   * @param url
	   * @return
	   */
	  public String openConnection(URI uri) {      
		  Z3950Connection connection = Z3950ConnectionFactory.getConnection(uri);
		  return connection.connect();
	  }
	  
	  /**
	   * 
	   * @param url
	   * @return
	   */
	  public boolean isProtocolSupported(URI uri) {    
	     Z3950Connection connection = Z3950ConnectionFactory.getConnection(uri);
	     if (connection.connect() == null){
	    	 return false;
	     }
	     return true;
	  }

	  /**
	   * Return the database
	   * @param url
	   * @return
	   */
	  public static String getDatabase(URI uri) {
		  StringTokenizer sti = new StringTokenizer(uri.getPath(), "/");
		  if (sti.countTokens() == 0) {
			  return "geo";
		  } else {
			  return sti.nextToken();
		  }
	  }
}
