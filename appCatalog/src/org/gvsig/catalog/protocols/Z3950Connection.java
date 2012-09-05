package org.gvsig.catalog.protocols;

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
 * $Id: Z3950Connection.java 561 2007-07-27 06:49:30 +0000 (Fri, 27 Jul 2007) jpiera $
 * $Log$
 * Revision 1.1.2.6.4.3  2007/07/23 07:14:25  jorpiell
 * Catalog refactoring
 *
 * Revision 1.1.2.6.4.2  2007/07/13 12:00:35  jorpiell
 * Add the posibility to add a new panel
 *
 * Revision 1.1.2.6.4.1  2007/07/10 11:18:04  jorpiell
 * Added the registers
 *
 * Revision 1.1.2.6  2006/11/15 00:08:08  jjdelcerro
 * *** empty log message ***
 *
 * Revision 1.4  2006/11/13 07:52:58  jorpiell
 * Se han incluido los cambios que se hicieron para Chile: cambio de la librería jzKit, carga de ArcIms y WCS
 *
 * Revision 1.3  2006/10/02 08:29:07  jorpiell
 * Modificados los cambios del Branch 10 al head
 *
 * Revision 1.1.2.3  2006/10/02 07:13:43  jorpiell
 * Comprobada una NullPointerException que generaba una excepción
 *
 * Revision 1.1.2.2  2006/09/25 06:48:38  jorpiell
 * Añadida la funcionalidad de mostrar el documento de texto cuando no es un XML
 *
 * Revision 1.1.2.1  2006/09/20 12:01:07  jorpiell
 * Se ha cambiado la versión de la jzkit, se ha incorporado la funcionalidad de cargar arcims
 *
 * Revision 1.1  2006/09/20 11:20:17  jorpiell
 * Se ha cambiado la versión de la librería jzkit de la 1 a la 2.0
 *
 *
 */
import java.net.URI;
import java.util.StringTokenizer;
import java.util.Vector;

import org.gvsig.catalog.drivers.GetRecordsReply;
import org.gvsig.catalog.metadataxml.XMLNode;
import org.gvsig.catalog.metadataxml.XMLTree;
import org.jzkit.a2j.codec.util.OIDRegister;
import org.jzkit.search.provider.iface.IRQuery;
import org.jzkit.search.provider.iface.SearchException;
import org.jzkit.search.provider.iface.Searchable;
import org.jzkit.search.util.RecordModel.ExplicitRecordFormatSpecification;
import org.jzkit.search.util.RecordModel.InformationFragment;
import org.jzkit.search.util.RecordModel.RecordFormatSpecification;
import org.jzkit.search.util.RecordModel.SUTRS;
import org.jzkit.search.util.RecordModel.XMLRecord;
import org.jzkit.search.util.ResultSet.IRResultSet;
import org.jzkit.search.util.ResultSet.IRResultSetException;
import org.jzkit.search.util.ResultSet.IRResultSetStatus;
import org.jzkit.z3950.client.SynchronousOriginBean;
import org.jzkit.z3950.gen.v3.Z39_50_APDU_1995.InitializeResponse_type;


public class Z3950Connection extends SynchronousOriginBean
{
  public static final char ISO2709_RS = 035;
  public static final char ISO2709_FS = 036;
  public static final char ISO2709_IDFS = 037;
  private static final String PREFIX_QUERY_TYPE = "PREFIX";
  private static final String CCL_QUERY_TYPE = "CCL";

  private int auth_type = 0; // 0=none, 1=anonymous, 2=open, 3=idpass
  private String principal = null;
  private String group = null;
  private String credentials = null; 
  private InitializeResponse_type resp = null;
  private URI uri = null;
  private String host = null;
  private int port = 0;
  private String database = null;
  
  
  public Z3950Connection(URI uri){
	  super(new OIDRegister("/a2j.properties"));
	  this.uri = uri;
	  this.host = uri.getHost();
	  this.port = uri.getPort();
	  StringTokenizer sti = new StringTokenizer(uri.getPath(), "/");
      if (sti.countTokens() == 0) {
          this.database = "geo";
      } else {
          this.database = sti.nextToken();
      }
  }
  
  /**
   * 
   * @param url
   * @return
   */
  public String connect(){
	   	if (resp == null){
	    	resp = connect(host,port,auth_type,principal,group,credentials);
	    }
	    StringBuffer string = new StringBuffer();	    
	    if (resp == null){
	    	return null;
	    }
	    if (resp.result.booleanValue() == true) {
	    	if (resp.referenceId != null) {
	    		string.append("Reference ID : " + new String(resp.referenceId) +
	    		"\n");
	    	}
	    	string.append("Implementation ID : " +
	    			resp.implementationId + "\n");
	    	string.append("Implementation Name : " +
	    			resp.implementationName + "\n");
	    	string.append("Implementation Version : " +
	    			resp.implementationVersion + "\n");
	    } else {
	    	System.out.println(" Failed to establish association");
	    	return null;
	    }
	    
	    return string.toString();
  }

  public GetRecordsReply search(GetRecordsReply recordsReply, String sQuery,int firstRecord){
	  try{
		  IRQuery query = new IRQuery();
		  query.collections = new Vector();
		  query.collections.add(database);
		  query.query = new org.jzkit.search.util.QueryModel.PrefixString.PrefixString(sQuery);
		  
		  Searchable s = Z3950ConnectionFactory.getSearchable(uri);
		  IRResultSet result = s.evaluate(query);
		  result.waitForStatus(IRResultSetStatus.COMPLETE|IRResultSetStatus.FAILURE,0);
		
		  int numOfResults = result.getFragmentCount();
		  recordsReply.setNumRecords(numOfResults);
				  
		  RecordFormatSpecification rfs = new ExplicitRecordFormatSpecification(null,null,"f");
		  InformationFragment[] fragment = result.getFragment(firstRecord,10,rfs);

		  for (int i=0 ; i<fragment.length ; i++){
			  try {
				  String answer = "";				  
				  if (fragment[i] instanceof XMLRecord){				  	 
					  XMLRecord xml = (XMLRecord)fragment[i];	
					  answer = xml.toString();
				  }else {
					  SUTRS sutr = (SUTRS)fragment[i];
					  answer = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
					  "<" + XMLNode.ISNOTXML + ">" + 
					  sutr.toString() + 
					  "</" + XMLNode.ISNOTXML + ">";
				  }  				  
				  recordsReply.addRecord(uri, XMLTree.xmlToTree(answer));
			  } catch (Exception e1) {
				  // TODO Auto-generated catch block
				  e1.printStackTrace();
			  }
		  }	
	  }catch(SearchException e){
		  e.printStackTrace();
	  } catch (IRResultSetException e) {
		  // TODO Auto-generated catch block
		  e.printStackTrace();
	  } catch (Exception e) {
		  // TODO Auto-generated catch block
		  e.printStackTrace();
	  }	  
      return recordsReply;
  } 
}
