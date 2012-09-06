package org.gvsig.remoteClient.wms;

import java.awt.geom.Rectangle2D;
import java.util.Vector;

import junit.framework.TestCase;
/**
 * 
 * @author jaume
 *
 */
public class WMSStatusTest extends TestCase {
	WMSStatus st1, st2, st3, st4;
    protected void setUp() {
    	st1 = new WMSStatus();
    	// Layer names
    	Vector v1 = new Vector();
    	v1.add("a");
    	v1.add("b");
    	v1.add("c");
    	st1.setLayerNames(v1);
    	v1.clear();
    	// Styles
    	v1.add("s1");
    	v1.add("s2");
    	v1.add("s3");
    	st1.setStyles(v1);
    	// Transparency
    	st1.setTransparency(false);
    	v1.clear();
    	v1.add("TIME=february");
    	v1.add("WAVELENGTH=200nm");
    	st1.setDimensions(v1);
    	// Extent
    	Rectangle2D rect1 = new Rectangle2D.Double(1.0, 1.00000001, 5.000000, 6.8);
    	st1.setExtent(rect1);
    	// Exception format
    	st1.setExceptionFormat("bla");
    	// SRS
    	st1.setSrs("bla bla");
    	// Format
    	st1.setFormat("image/mpeg");
    	// Width
    	st1.setWidth(800);
    	// Height
    	st1.setHeight(600);
    	// Online resource
    	st1.setOnlineResource("alkjf");
    	
    	st2 = new WMSStatus();
    	// Layer names
        Vector v2 = new Vector();
    	v2.add("a");
    	v2.add("b");
    	v2.add("c");
    	st2.setLayerNames(v2);
    	v2.clear();
    	// Styles
    	v2.add("s1");
    	v2.add("s2");
    	v2.add("s3");
    	st2.setStyles(v2);
    	// Transparency
    	st2.setTransparency(false);
    	v2.clear();
    	// Dimensions
    	v2.add("TIME=february");
    	v2.add("WAVELENGTH=200nm");
    	st2.setDimensions(v2);
    	// Extent
    	Rectangle2D rect2 = new Rectangle2D.Double(1.0, 1.00000001, 5.000000, 6.8);
    	st2.setExtent(rect2);
    	// Exception format
    	st2.setExceptionFormat("bla");
    	// SRS
    	st2.setSrs("bla bla");
    	// Format
    	st2.setFormat("image/mpeg");
    	// Width
    	st2.setWidth(800);
    	// Height
    	st2.setHeight(600);
    	// Online resource
    	st2.setOnlineResource("alkjf");
    	
    	st3 = new WMSStatus();
    	// Layer names
        Vector v3 = new Vector();
    	v3.add("a");
    	v3.add("b");
    	v3.add("c");
    	st3.setLayerNames(v3);
    	v3.clear();
    	// Styles
    	v3.add("s1");
    	v3.add("s2");
    	v3.add("s3");
    	st3.setStyles(v3);
    	// Transparency
    	st3.setTransparency(false);
    	v3.clear();
    	// Dimensions
    	v3.add("TIME=february");
    	v3.add("WAVELENGTH=200nm");
    	st3.setDimensions(v3);
    	// Extent
    	Rectangle2D rect3 = new Rectangle2D.Double(2.0, 1.00000001, 5.000000, 6.8);
    	st3.setExtent(rect3);
    	// Exception format
    	st3.setExceptionFormat("bla");
    	// SRS
    	st3.setSrs("bla bla");
    	// Format
    	st3.setFormat("image/mpeg");
    	// Width
    	st3.setWidth(800);
    	// Height
    	st3.setHeight(600);
    	// Online resource
    	st3.setOnlineResource("alkjf");
        
    	st4 = new WMSStatus(); 
        // Layer names
        Vector v4 = new Vector();
    	v4.add("a");
    	v4.add("b");
    	v4.add("c");
    	st4.setLayerNames(v4);
    	v4.clear();
    	// Styles
    	v4.add("s1");
    	v4.add("s2");
    	v4.add("s3");
    	st4.setStyles(v4);
    	// Transparency
    	st4.setTransparency(false);
    	v4.clear();
    	// Dimensions
    	v4.add("TIME=february");
    	v4.add("WAVELENGTH=200nm");
    	st4.setDimensions(v4);
    	// Extent
    	Rectangle2D rect4 = new Rectangle2D.Double(2.0, 1.00000001, 5.000000, 6.8);
    	st4.setExtent(rect3);
    	// Exception format
    	st4.setExceptionFormat("bla");
    	// SRS
    	st4.setSrs("bla bla");
    	// Format
    	st4.setFormat("image/mpeg");
    	// Width
    	st4.setWidth(800);
    	// Height
    	st4.setHeight(600);
    	// Online resource
    	st4.setOnlineResource("alkjfa");
    }

    public void testEquality() {
    	assertTrue(st1.equals(st2));
    	assertFalse(st1.equals(st3)); // (distinct extent)
    	assertFalse(st3.equals(st4)); // (distinct online resources)
    }
}
