
package org.gvsig.remoteClient;

import org.gvsig.remoteClient.ogc.OGCClientOperation;

/**
 * This class represents the client status at a certain moment
 * it describes the "graphic" situation or the requirements of the data
 * to be retrieved.
 * 
 */
public abstract class RemoteClientStatus {

	// width and heigh of the map
    private int width;
    private int height;
    
    //format of the image to be retrieved
    private String format;
    // spatial reference system of the image to be retrieved
    private String srs;
    // exception format, to be retrieved in case of error
    private String exceptionFormat;
    
    //To set if the client has to use GET or POST
	private int protocol = OGCClientOperation.PROTOCOL_UNDEFINED;

	public int getWidth() {        
        return width;
    }
    
    public void setWidth(int _width) {        
    	width = _width;
    } 

    public int getHeight() {                
        return height;
    } 
    public void setHeight(int _height) {        
        height = _height;
    } 

    public String getFormat() {        
        return format;
    } 
    public void setFormat(String _format) {        
        format = _format;
    } 

    public String getSrs() {        
        return srs;
    } 

    public void setSrs(String _srs) {        
        srs = _srs;
    } 

    public void setExceptionFormat(String _format) {        
        exceptionFormat = _format;
    } 
    
    public String getExceptionFormat() {        
        return exceptionFormat;
    }
    
    /**
	 * @return the protocol
	 */
	public int getProtocol() {
		return protocol;
	}

	/**
	 * @param protocol the protocol to set
	 */
	public void setProtocol(int protocol) {
		this.protocol = protocol;
	}
 
 }
