/*
 * Created on 01-oct-2005
 */
package org.gvsig.remoteClient.taskplanning.retrieving;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Luis W. Sevilla (sevilla_lui@gva.es)
 */
public class URLRequest{
	public static final String HTTP = "http";
	private volatile int hashCode = 0;
	public static final int GET = 1;
	public static final int POST = 2;
	
	private int requestType = GET;
	private String protocol;
	private String host;
	private int port = -1;
	private String file;
	private String fileName;
	
	public URL getUrl() throws MalformedURLException {
		String u = protocol;
		u += "://"+host;
		if (port != -1)
			u += ":"+port;
		u += "/"+file;
		return new URL(u);
	}
	/**
	 * @return Returns the fileName.
	 */
	public String getFileName() {
		return fileName;
	}
	/**
	 * @param fileName The fileName to set.
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	/**
	 * @return Returns the host.
	 */
	public String getHost() {
		return host;
	}
	/**
	 * @param host The host to set.
	 */
	public void setHost(String host) {
		this.host = host;
	}
	/**
	 * @return Returns the file.
	 */
	public String getFile() {
		return file;
	}
	/**
	 * @param page The file to set.
	 */
	public void setFile(String page) {
		this.file = page;
	}
	/**
	 * @return Returns the protocol.
	 */
	public String getProtocol() {
		return protocol;
	}
	/**
	 * @param protocol The protocol to set.
	 */
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	/**
	 * @return Returns the requestType.
	 */
	public int getRequestType() {
		return requestType;
	}
	/**
	 * @param requestType The requestType to set.
	 */
	public void setRequestType(int requestType) {
		this.requestType = requestType;
	}
	/**
	 * @return Returns the port.
	 */
	public int getPort() {
		return port;
	}
	/**
	 * @param port The port to set.
	 */
	public void setPort(int port) {
		this.port = port;
	}

	public int hashCode() {
		if (hashCode == 0) {
			int result = 17;
			String[] stringFields = new String[] {
					fileName,
					host,
					file,
					protocol
			};
			for (int i = 0; i < stringFields.length; i++) {
				if (stringFields[i] != null)
					for (int j = 0; j < stringFields[i].length(); j++) 
						result = 37*result + (int) stringFields[i].charAt(j);
				
			}
			result = 37*result + port;
			result = 37*result + requestType;
		}
		return hashCode;
	}
	
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof URLRequest))
			return false;

		URLRequest other = (URLRequest) o;
		
		String[] stringFields = new String[] {
				// this.fileName, NO!!! Do not check file name
				this.host,
				this.file,
				this.protocol
		};
		String[] othersStringField = new String[] {
				// other.fileName, NO!!! Do not check file name
				other.host,
				other.file,
				other.protocol
		};
		for (int i = 0; i < stringFields.length; i++) {
			if (stringFields[i] == null && othersStringField[i]!=null) return false;
			if (stringFields[i] == null && othersStringField[i]!=null) return false;
			if (stringFields[i] != null && othersStringField[i]!=null) 
				if (!stringFields[i].equals(othersStringField[i])) return false;
		}
		
		if (this.port != other.port) return false;
		if (this.requestType != other.requestType) return false;
		return true;
	}
}
