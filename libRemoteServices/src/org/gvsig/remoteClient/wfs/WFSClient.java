package org.gvsig.remoteClient.wfs;

import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
import java.util.Hashtable;

import org.gvsig.remoteClient.RemoteClient;
import org.gvsig.remoteClient.wfs.exceptions.WFSException;
import org.gvsig.remoteClient.wms.ICancellable;

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
 * $Id: WFSClient.java 28430 2009-05-06 08:56:02Z jpiera $
 * $Log$
 * Revision 1.10  2007-09-20 09:30:12  jaume
 * removed unnecessary imports
 *
 * Revision 1.9  2007/02/09 14:11:01  jorpiell
 * Primer piloto del soporte para WFS 1.1 y para WFS-T
 *
 * Revision 1.8  2006/06/14 08:46:07  jorpiell
 * Se tiene en cuanta la opcion para refrescar las capabilities
 *
 * Revision 1.7  2006/05/25 10:28:25  jorpiell
 * Se ha añadido un atributo al método connect
 *
 * Revision 1.6  2006/05/23 13:23:22  jorpiell
 * Se ha cambiado el final del bucle de parseado y se tiene en cuenta el online resource
 *
 * Revision 1.4  2006/04/20 16:39:16  jorpiell
 * Añadida la operacion de describeFeatureType y el parser correspondiente.
 *
 * Revision 1.3  2006/04/19 12:51:35  jorpiell
 * Añadidas algunas de las clases del servicio WFS
 *
 *
 */
/**
 * Represents the class the with the necessary logic to connect to a 
 * OGCWFS and interpretate the data 
 * @author Jorge Piera Llodrá (piera_jor@gva.es)
 */
public class WFSClient extends RemoteClient{
	private WFSProtocolHandler handler;
		
	/**
	 * Constructor.
	 * the parameter host, indicates the WFS host to connect.
	 * @throws ConnectException,IOException 
	 *
	 */
	public WFSClient(String host) throws ConnectException,IOException {
		setHost(host);
		
		try {        	
			handler = WFSProtocolHandlerFactory.negotiate(host);
			handler.setHost(host);        
		} catch(ConnectException conE) {
			conE.printStackTrace();
			throw conE; 
		} catch(IOException ioE) {
			ioE.printStackTrace();
			throw ioE; 
		} catch(Exception e) {
			e.printStackTrace();       	
		} 
	}
	
	public WFSClient(String host, String version) throws IOException {
		setHost(host);
		
		if (version == null){
			try {        	
				handler = WFSProtocolHandlerFactory.negotiate(host);				  
			} catch(ConnectException conE) {
				conE.printStackTrace();
				throw conE; 
			} catch(IOException ioE) {
				ioE.printStackTrace();
				throw ioE; 
			} catch(Exception e) {
				e.printStackTrace();       	
			} 
		}else{
			handler = WFSProtocolHandlerFactory.createVersionDriver(version);
		}
		
		if (handler == null){
			throw new UnsupportedOperationException("Unsupported version");
		}
		handler.setHost(host);        		
	}
	
	/**
	 * Every OGC Web Service (OWS), including a Web Feature Service,
	 * must have the ability to describe its capabilities by returning
	 * service metadata in response to a GetCapabilities request.
	 * @param status
	 * WFS client status. Contains all the information to create
	 * the query
	 */
	public void getCapabilities(WFSStatus status, boolean override, ICancellable cancel)throws WFSException {
		handler.getCapabilities(status,override,cancel);		
	}
	
	/**
	 * The function of the DescribeFeatureType operation is to 
	 * generate a schema description of feature types serviced 
	 * by a WFS implementation. The schema descriptions define 
	 * how a WFS implementation expects feature instances to 
	 * be encoded on input (via Insert and Update requests) 
	 * and how feature instances will be generated on output 
	 * (in response to GetFeature and GetGmlObject requests). 
	 * @param status
	 * WFS client status. Contains all the information to create
	 * the query
	 */
	public void describeFeatureType(WFSStatus status, boolean override, ICancellable cancel)throws WFSException {
		handler.describeFeatureType(status,override,cancel);		
	}
	
	/**
	 * The GetFeature operation allows retrieval of features from a 
	 * web feature service. A GetFeature request is processed by
	 * a WFS and when the value of the outputFormat attribute is 
	 * set to text/gml a GML instance document, containing the 
	 * result set, is returned to the client.
	 * @param status
	 * WFS client status. Contains all the information to create
	 * the query
	 * @return File
	 * GML File
	 */
	public File getFeature(WFSStatus status, boolean override, ICancellable cancel) throws WFSException{
		return handler.getFeature(status,override,cancel);
	}
	
	/**
	 * The Transaction operation is used to describe data transformation 
	 * operations that are to be applied to web accessible feature 
	 * instances. A web feature service may process a Transaction 
	 * operation directly or possibly translate it into the language 
	 * of a target datastore to which it is connected and then have the
	 * datastore execute the transaction. When the transaction has been 
	 * completed, a web feature service will generate an XML response 
	 * document indicating the completion status of the transaction.
	 * @param status
	 * WFS client status. Contains all the information to create
	 * the query
	 */
	public void transaction(WFSStatus status, boolean override, ICancellable cancel) throws WFSException{
		handler.transaction(status,override,cancel);
	}
	
	/**
	 * Web connections are inherently stateless. As a consequence 
	 * of this, the semantics of serializable transactions are not 
	 * preserved. To understand the issue, consider an update operation.
	 * The client fetches a feature instance. The feature is then 
	 * modified on the client side, and submitted back to the database 
	 * via a Transaction request for update. Serializability is lost 
	 * since there is nothing to guarantee that while the feature was 
	 * being modified on the client side, another client did not come 
	 * along and update that same feature in the database.
	 * One way to ensure serializability is to require that access to
	 * data be done in a mutually exclusive manner; that is while one 
	 * transaction accesses a data item, no other transaction can modify 
	 * the same data item. This can be accomplished by using locks that 
	 * control access to the data.
	 * The purpose of the LockFeature operation is to expose a long 
	 * term feature locking mechanism to ensure consistency. The lock
	 * is considered long term because network latency would make 
	 * feature locks last relatively longer than native commercial 
	 * database locks.
	 * The LockFeature operation is optional and does not need to be 
	 * implemented for a WFS implementation to conform to this 
	 * specification. If a WFS implements the LockFeature operation, 
	 * this fact must be advertised in the capabilities document
	 * @param status
	 */
	public void lockFeature(WFSStatus status, boolean override, ICancellable cancel)throws WFSException {
		handler.lockFeature(status,override,cancel);
	}
	
	
	/**
	 * <p>Checks the connection to de remote WFS and requests its 
	 * capabilities.</p>
	 * 
	 */
	public boolean connect(boolean override,ICancellable cancel) 
	{
		try {
			if (handler == null) {
				if (getHost().trim().length() > 0) {                   
					handler = WFSProtocolHandlerFactory.negotiate(getHost());
					handler.setHost(getHost());
				} else {
					// must to specify host first!!!!
					return false;
				}
			}
			getCapabilities(null,override,cancel);			
			
			return true;
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/*
	 *  (non-Javadoc)
	 * @see org.gvsig.remoteClient.RemoteClient#connect(org.gvsig.remoteClient.wms.ICancellable)
	 */
	public boolean connect(ICancellable cancel) {
		return connect(false, cancel);
	}
	
	/*
	 *  (non-Javadoc)
	 * @see org.gvsig.remoteClient.RemoteClient#close()
	 */
	public void close() {
		// TODO Auto-generated method stub
		
	}
	
	 public String getVersion()
	 {
		 return handler.getVersion();
	 }	 
	 
	 /**
     * Gets the Service information included in the Capabilities
     */
    
    public WFSServiceInformation getServiceInformation(){
        return (WFSServiceInformation)handler.getServiceInformation();  
    }
    
    /**
     * Returns the features list
     * @return
     */
    public Hashtable getFeatures()
    {
        return handler.getFeatures();  
    }	
	
}
