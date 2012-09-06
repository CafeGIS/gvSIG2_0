/* gvSIG. Geographic Information System of the Valencian Government
 *
 * Copyright (C) 2007-2008 Infrastructures and Transports Department
 * of the Valencian Government (CIT)
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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, 
 * MA  02110-1301, USA.
 * 
 */

/*
 * AUTHORS (In addition to CIT):
 * 2009 Iver T.I.  {{Task}}
 */

package org.gvsig.remoteClient.ogc.request;

import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

import org.gvsig.remoteClient.RemoteClientStatus;
import org.gvsig.remoteClient.ogc.OGCClientOperation;
import org.gvsig.remoteClient.ogc.OGCProtocolHandler;
import org.gvsig.remoteClient.utils.Utilities;
import org.gvsig.remoteClient.wfs.WFSOperation;

public abstract class OGCRequest {
	protected RemoteClientStatus status = null;
	protected OGCProtocolHandler protocolHandler = null;
	protected boolean isDeleted = false;

	public OGCRequest(RemoteClientStatus status, OGCProtocolHandler protocolHandler) {
		super();
		this.status = status;
		this.protocolHandler = protocolHandler;
	}	

	/**
	 * Send a request to the server.
	 * @return
	 * The server reply
	 * @throws IOException 
	 * @throws UnknownHostException 
	 * @throws ConnectException 
	 */
	public File sendRequest() throws ConnectException, UnknownHostException, IOException{
		if (status.getProtocol() != OGCClientOperation.PROTOCOL_UNDEFINED){
			if (status.getProtocol() == OGCClientOperation.PROTOCOL_GET){
				String onlineResource = protocolHandler.getHost();
				String symbol = getSymbol(onlineResource);
				onlineResource = onlineResource + symbol;
				return sendHttpGetRequest(onlineResource);
			}else{
				String onlineResource = protocolHandler.getHost();
				return sendHttpPostRequest(onlineResource);
			}
		}
		//if exists an online resource for the GET operation
		String onlineResource = protocolHandler.getServiceInformation().getOnlineResource(getOperationName(), WFSOperation.PROTOCOL_GET);
		if (onlineResource != null){
			String symbol = getSymbol(onlineResource);
			onlineResource = onlineResource + symbol;
			return sendHttpGetRequest(onlineResource);
		}
		//if exists an online resource for the POST operation
		onlineResource =  protocolHandler.getServiceInformation().getOnlineResource(getOperationName(), WFSOperation.PROTOCOL_POST);
		if (onlineResource != null){
			return sendHttpPostRequest(onlineResource);
		}
		//If the online resource doesn't exist, it tries with the server URL and GET
		onlineResource = protocolHandler.getHost();
		String symbol = getSymbol(onlineResource);
		onlineResource = onlineResource + symbol;
		return sendHttpGetRequest(onlineResource);
	}

	protected abstract String getHttpGetRequest(String onlineResource);

	protected abstract String getHttpPostRequest(String onlineResource);

	protected abstract String getTempFilePrefix();

	protected abstract String getOperationName();

	protected abstract String getSchemaLocation();	

	/**
	 * @return the URL used in the HTTP get operation
	 * @throws MalformedURLException
	 */
	public URL getURL() throws MalformedURLException{
		String onlineResource = protocolHandler.getServiceInformation().getOnlineResource(getOperationName(), WFSOperation.PROTOCOL_GET);
		if (onlineResource != null){
			String symbol = getSymbol(onlineResource);
			onlineResource = onlineResource + symbol;
			return new URL(getHttpGetRequest(onlineResource));
		}
		return null;
	}

	/**
	 * Send a Http request using the get protocol
	 * @param onlineResource
	 * @return
	 * @throws ConnectException
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	private File sendHttpGetRequest(String onlineResource) throws ConnectException, UnknownHostException, IOException{
		URL url = new URL(getHttpGetRequest(onlineResource));
		if (isDeleted()){
			Utilities.removeURL(url);
		}
		return Utilities.downloadFile(url, getTempFilePrefix(), null);		
	}

	/**
	 * Send a Http request using the post protocol
	 * @param onlineResource
	 * @return
	 * @throws ConnectException
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	private File sendHttpPostRequest(String onlineResource) throws ConnectException, UnknownHostException, IOException{
		URL url = new URL(onlineResource);
		String data = getHttpPostRequest(onlineResource);
		System.out.println(data);
		if (isDeleted()){
			Utilities.removeURL(url+data);
		}
		return Utilities.downloadFile(url, data, getTempFilePrefix(), null);		
	}

	/**
	 * Just for not repeat code. Gets the correct separator according 
	 * to the server URL
	 * @param h
	 * @return
	 */
	protected static String getSymbol(String h) {
		String symbol;
		if (h.indexOf("?")==-1) 
			symbol = "?";
		else if (h.indexOf("?")!=h.length()-1)
			symbol = "&";
		else
			symbol = "";
		return symbol;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}  

}

