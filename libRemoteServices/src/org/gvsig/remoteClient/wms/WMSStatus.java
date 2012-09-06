
package org.gvsig.remoteClient.wms;

import java.awt.geom.Rectangle2D;
import java.util.Vector;

/**
 * Describes the status of a WMSclient, so it adds to the Remote client status
 * a list of layers, a list of layer styles, the extent of the map.
 * Provides the functionality to modify these lists.
 * 
 */
public class WMSStatus extends org.gvsig.remoteClient.RemoteClientStatus {
	// list of layer to be retrieved by the WMS
    private Vector layers;
    // List of layer styles
    private Vector styles;
    private Vector dimensions;
    // extent required by the WMS client
    private Rectangle2D extent;
    private boolean transparency;
	private String onlineResource;
    public WMSStatus()
    {
    	layers = new Vector();
    	styles = new Vector();    	    	
    }
    
    // sets the list of layers required by the WMS client
    public void setLayerNames(Vector _layers) {        
        layers = _layers;
    } 

/**
 * <p>Retrieves the layer list required by the WMS client</p>
 * 
 * 
 * @return Vector, the list of layers
 */
    public Vector getLayerNames() {        
        return layers;
    } 

/**
 * <p> Adds a layer to the list of layers required by the WMS client</p>
 * 
 * 
 * @param _layerName, name of the layer to be added. 
 */
    public void addLayerName(String _layerName) {        
        layers.add(_layerName);
    } 

/**
 * <p>removes a layer from the layer list</p>
 * 
 * 
 * @param _layerName 
 * @return true if the layer name has been deleted from the list
 */
    public boolean removeLayerName(String _layerName) {
    	return layers.remove(_layerName);       
    } 

/**
 * <p>gets the styles list required by the WMS client</p>
 * 
 * 
 * @return Vector with the list of layer styles
 */
    public Vector getStyles() {        
        return styles;
    } 

/**
 * <p>sets the styles list required by the WMS client</p>
 * 
 * 
 * @param _styles, list to be set as the required styles.
 */
    public void setStyles(Vector _styles) {        
        styles = _styles;
    }
    
    /**
     * <p>sets the styles list required by the WMS client</p>
     * 
     * 
     * @param _styles, list to be set as the required styles.
     */
    public void setDimensions(Vector _dimensions) {        
        
        dimensions = _dimensions;
    } 

/**
 * <p>Adds a style name to the styles list required by the WMS client</p>
 * 
 * 
 * @param _name, style name to be added
 */
    public void addStyleName(String _name) {        
        styles.add( _name);
    } 

/**
 * <p>Removes a style from the list of styles required by the WMS client</p>
 * 
 * 
 * @param _name, style name to be removed
 */
    public boolean removeStyleName(String _name) {        
        return styles.remove(_name);
    } 
/**
 * <p>Gets the extent defined by the map</p>
 */
    public Rectangle2D getExtent() {        
        return extent;
    } 
/**
 * <p>Sets the extent defined by the map</p>
 */
    public void setExtent(Rectangle2D extent) {        
        this.extent = extent;
    } 
 
   

    /**
     * @return
     */
    public boolean getTransparency() {
        return transparency;
    }

    /**
     * @param wmsTransparency
     */
    public void setTransparency(boolean wmsTransparency) {
        transparency = wmsTransparency;
    }

    /**
     * @return
     */
    public Vector getDimensions() {
        return dimensions;
    }
    
    public boolean equals(Object obj){
        if (!(obj instanceof WMSStatus))
            return false;
        WMSStatus s = (WMSStatus) obj;
        
        // Compare layer names
        if (!(( s.getLayerNames()==null && this.getLayerNames()==null) ||
                s.getLayerNames().equals(this.getLayerNames())))
        	return false;
        
        // Compare extent
        if (!(( s.getExtent()==null && this.getExtent()==null) ||
                s.getExtent().equals(this.getExtent())))
        	return false;
        
        // Compare height
        if ( s.getHeight() != this.getHeight())
        	return false;
        	
        // Compare width
        if ( s.getWidth()  != this.getWidth())
        	return false;
        
        // Compare styles
        if (!(( s.getStyles()==null && this.getStyles()==null) ||
                s.getStyles().equals(this.getStyles())))
        	return false;
        
        // Compare dimensions
        if (!(( s.getDimensions()==null && this.getDimensions()==null) ||
                s.getDimensions().equals(this.getDimensions())))
        	return false;
        
        // Compare transparencies
        if ( s.getTransparency() != this.getTransparency())
        	return false;

		// Compare srs
        if (!(( s.getSrs()==null && this.getSrs()==null) || 
        		s.getSrs().equals(this.getSrs())))
        	return false;
        
        // Compare exception formats
        if (!(( s.getExceptionFormat()==null && this.getExceptionFormat()==null) ||
        		s.getExceptionFormat().equals(this.getExceptionFormat())))
        	return false;
        
        // Compare formats
        if (!(( s.getFormat()==null && this.getFormat()==null) ||
        		s.getFormat().equals(this.getFormat())))
        	return false;
        
        // Compare online resources
        if (!(( s.getOnlineResource()==null && this.getOnlineResource()==null) ||
        		s.getOnlineResource().equals(this.getOnlineResource())))
        	return false;
        
        return true;
    }
    
    public Object clone() {
    	WMSStatus newObject = new WMSStatus();
    	Vector v = this.getLayerNames();
    	if (v != null)
    		newObject.setLayerNames((Vector)v.clone());
    	Rectangle2D r = this.getExtent();
    	if (r != null)
    		newObject.setExtent((Rectangle2D)r.clone());
        newObject.setHeight(this.getHeight());
        newObject.setWidth(this.getWidth());
        v = this.getStyles();
        if (v != null)
        	newObject.setStyles((Vector)v.clone());
        v = this.getDimensions();
        if (v != null)
        	newObject.setDimensions((Vector)v.clone());
        newObject.setTransparency(this.getTransparency());
        newObject.setSrs(this.getSrs());
        newObject.setExceptionFormat(this.getExceptionFormat());
        newObject.setFormat(this.getFormat());
        newObject.setOnlineResource(this.getOnlineResource());
    	return newObject;
    }

    /**
     * Returns the URL that the server specified for a WMS request if any was described in
     * its capabilities document. 
     * @param operationName, a String containing the name of the operation (case-independent)
     * @return <b>String</b> containing the URL for this operationName or <B>null</B> if none was
     *                specified.
     */
	public String getOnlineResource() {
		return onlineResource;
	}
	
	/**
	 * Sets the string literal containing the URL of an online resource for a specific
	 * WMS request.
	 * @param operationName, String telling to which request correspond the address
	 * @param url, String containing the URL for the given WMS request
	 */
	public void setOnlineResource(String url) {
		onlineResource = url;
	}

}
