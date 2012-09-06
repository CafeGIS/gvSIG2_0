
package org.gvsig.remoteClient.wms;

import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
import java.util.TreeMap;
import java.util.Vector;

import org.gvsig.remoteClient.exceptions.ServerErrorException;
import org.gvsig.remoteClient.exceptions.WMSException;
import org.gvsig.remoteClient.utils.BoundaryBox;


/**
 * <p>Represents the class the with the necessary logic to connect to a OGCWMS and interpretate the data </p>
 * 
 */
public class WMSClient extends org.gvsig.remoteClient.RasterClient {
    
    private org.gvsig.remoteClient.wms.WMSProtocolHandler handler;
//    private TreeMap layers = new TreeMap();
//    private WMSLayer rootLayer;
    
    /**
     * @return Returns the rootLayer.
     */
    public WMSLayer getRootLayer() {
        return handler.rootLayer;
    }

    /**
     * Constructor.
     * the parameter host, indicates the WMS host to connect.
     * */
    public WMSClient(String host) throws ConnectException, IOException 
    {
    	setHost(host);
        try {        	
        	handler = WMSProtocolHandlerFactory.negotiate(host);
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
    
    public String getVersion()
    {
        return handler.getVersion();
    }
    /**
     * <p>One of the three interfaces that OGC WMS defines. Request a map.</p> 
     * @throws ServerErrorException 
     */
    public File getMap(WMSStatus status, ICancellable cancel) throws WMSException, ServerErrorException{   
        return handler.getMap(status, cancel);
    } 
    
    /**
     * <p>One of the three interfaces defined by OGC WMS, it gets the service capabilities</p>
     * @param override, if true the previous downloaded data will be overridden
     */
    public void getCapabilities(WMSStatus status, boolean override, ICancellable cancel) {        
        handler.getCapabilities(status, override, cancel);
    } 
    
    /**
     * <p>One of the three interfaces defined by the OGC WMS, it gets the information about a feature requested</p>
     * @return 
     */
    public String getFeatureInfo(WMSStatus status, int x, int y, int featureCount, ICancellable cancel) throws WMSException{        
        return handler.getFeatureInfo(status, x, y, featureCount, cancel);
    } 
    
    /**
     * <p>One of the three interfaces defined by the OGC WMS, it gets legend of a layer</p>
     * @return 
     */
    public File getLegendGraphic(WMSStatus status, String layerName, ICancellable cancel) throws WMSException, ServerErrorException{        
        return handler.getLegendGraphic(status, layerName, cancel);
    } 
    
    /**
     * <p> Reads from the WMS Capabilities, the layers available in the service</p>
     * @return a TreeMap with the available layers in the WMS 
     */
    public TreeMap getLayers() {        
        return handler.layers;
    } 
    
    /**
     * <p>Reads from the WMS Capabilities the number if layers available in the service</p>
     * @return, number of layers available
     */
    public int getNumberOfLayers() {        
        if (handler.layers != null)
        {
            return handler.layers.size();
        }
        return 0;
    } 
    
    /**
     * <p>Gets the WMSLayer with this name</p>
     * 
     * @param _name, layer name
     * @return the layer with this name
     */
    public WMSLayer getLayer(String _name) {        
        if (handler.layers.get(_name) != null)
        {
            return (WMSLayer)handler.layers.get(_name);
        }
        
        return null;
    } 
    
    public String[] getLayerNames()
    {    	
        WMSLayer[] lyrs;
        
        lyrs = (WMSLayer[])handler.layers.values().toArray(new WMSLayer[0]);
        
        String[] names = new String[lyrs.length];
        
        for(int i = 0; i<lyrs.length; i++)
        {
            names[i] = ((WMSLayer)lyrs[i]).getName();
        }
        return names;
    }
    
    public String[] getLayerTitles()
    {    	
        WMSLayer[] lyrs;
        
        lyrs = (WMSLayer[])handler.layers.values().toArray(new WMSLayer[0]);
        
        String[] titles = new String[lyrs.length];
        
        for(int i = 0; i<lyrs.length; i++)
        {
            titles[i] = ((WMSLayer)lyrs[i]).getTitle();
        }
        return titles;
    }
    /**
     * <p>Gets the image formats available in the Service to retrieve the maps</p>
     * @return a vector with all the available formats
     */
    public Vector getFormats() {        
        return ((WMSServiceInformation)handler.getServiceInformation()).formats;         
    } 
    
    public boolean isQueryable()
    {
    	return ((WMSServiceInformation)handler.getServiceInformation()).isQueryable();  
    }
    public boolean hasLegendGraphic()
    {
    	return ((WMSServiceInformation)handler.getServiceInformation()).hasLegendGraphic();  
    }
    
    public void close() {        
        // your code here
    } 
    
    
    /**
     * Returns the max extent that envolves the requested layers
     * */
    public Rectangle2D getLayersExtent(String[]layerNames, String srs)
    {
        try
        {
        	if (layerNames == null) return null;
            BoundaryBox bbox;
            WMSLayer layer = getLayer(layerNames[0]);
            
            bbox = layer.getBbox(srs);
            if (bbox == null) return null;
            double xmin = bbox.getXmin();
            double xmax = bbox.getXmax();
            double ymin = bbox.getYmin();
            double ymax = bbox.getYmax();
            
            for(int i=1; i<layerNames.length; i++)
            {
                layer = getLayer(layerNames[i]);
                bbox = layer.getBbox(srs);
                if (bbox == null) return null;
                if (bbox.getXmin() < xmin)
                {
                    xmin = bbox.getXmin();
                }
                if (bbox.getYmin() < ymin)
                {
                    ymin = bbox.getYmin();
                }
                if (bbox.getXmax() > xmax)
                {
                    xmax = bbox.getXmax();
                }
                if (bbox.getYmax() > ymax)
                {
                    ymax = bbox.getYmax();
                }
            }	
            
            Rectangle2D extent = new Rectangle2D.Double(xmin,ymin,Math.abs(xmax-xmin),Math.abs(ymax-ymin));
            return extent;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
    
    
    /**
     * Gets the Service information included in the Capabilities
     * */    
    public WMSServiceInformation getServiceInformation()
    {
        return ((WMSServiceInformation)handler.getServiceInformation());
    }
    
    
    /**
     * <p>Checks the connection to de remote WMS and requests its capabilities.</p>
     * @param override, if true the previous downloaded data will be overridden
     */
    public boolean connect(boolean override, ICancellable cancel) 
    {
        try {            
            if (handler == null)
            {
                if (getHost().trim().length() > 0)
                {					
                    //TODO: Implement correctly the negotiate algorithm
                    handler = WMSProtocolHandlerFactory.negotiate(getHost());
                    //handler = new WMSProtocolHandler1_1_1();
                    handler.setHost(getHost());
                }
                else
                {
                    //must to specify host first!!!!
                    return false;
                }                
            }
            getCapabilities(null, override, cancel);
            return true;
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    //TODO Check this out: Always 1 layer at first level...
    public WMSLayer getLayersRoot() {
        return handler.rootLayer;
    }

	public boolean connect(ICancellable cancel) {
		return connect(false, cancel);
	}
}
