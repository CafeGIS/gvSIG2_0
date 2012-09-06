
package org.gvsig.remoteClient.utils;

/**
 * <p></p>
 * 
 */
public class BoundaryBox {
    
/**
 * <p>Represents ...</p>
 * 
 */
    private double xmin;

/**
 * <p>Represents ...</p>
 * 
 */
    private double xmax;

/**
 * <p>Represents ...</p>
 * 
 */
    private double ymin;

/**
 * <p>Represents ...</p>
 * 
 */
    private double ymax;

/**
 * <p>Represents ...</p>
 * 
 */
    private String srs;

/**
 * <p>Represents ...</p>
 * 
 * 
 * @return 
 */
    public double getXmin() {        
        return xmin;
    } 

/**
 * <p>Represents ...</p>
 * 
 * 
 * @param _xmin 
 */
    public void setXmin(double _xmin) {        
        xmin = _xmin;
    } 

/**
 * <p>Represents ...</p>
 * 
 * 
 * @return 
 */
    public double getXmax() {        
        return xmax;
    } 

/**
 * <p>Represents ...</p>
 * 
 * 
 * @param _xmax 
 */
    public void setXmax(double _xmax) {        
        xmax = _xmax;
    } 

/**
 * <p>Represents ...</p>
 * 
 * 
 * @return 
 */
    public double getYmin() {        
        return ymin;
    } 

/**
 * <p>Represents ...</p>
 * 
 * 
 * @param _ymin 
 */
    public void setYmin(double _ymin) {        
        ymin = _ymin;
    } 

/**
 * <p>Represents ...</p>
 * 
 * 
 * @return 
 */
    public double getYmax() {        
        return ymax;
    } 

/**
 * <p>Represents ...</p>
 * 
 * 
 * @param _ymax 
 */
    public void setYmax(double _ymax) {        
        ymax = _ymax;
    } 

/**
 * <p>Represents ...</p>
 * 
 * 
 * @return 
 */
    public String getSrs() {        
        return srs;
    } 

/**
 * <p>Represents ...</p>
 * 
 * 
 * @param _srs 
 */
    public void setSrs(String _srs) {        
        srs = _srs;
    }
    
    public String toString(){
        String s = srs + " (" + xmin + ", " + ymin + ", " + xmax + ", " + ymax + ")";
        
        return s;
    }
 }
