/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
*
* Copyright (C) 2007 Instituto de Desarrollo Regional and Generalitat Valenciana.
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
*   Instituto de Desarrollo Regional (Universidad de Castilla La-Mancha)
*   Campus Universitario s/n
*   02071 Alabacete
*   Spain
*
*   +34 967 599 200
*/

package org.gvsig.remotesensing.scatterplot.chart;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Iterator;
import java.util.ResourceBundle;

import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.event.EventListenerList;

import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.raster.IProcessActions;
import org.gvsig.remotesensing.scatterplot.gui.ManagerROIChartPanel;
import org.gvsig.remotesensing.scatterplot.listener.RoiFromChartProcess;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.event.ChartChangeEvent;
import org.jfree.chart.event.ChartChangeListener;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.Zoomable;
import org.jfree.data.Range;
import org.jfree.ui.ExtensionFileFilter;

import com.iver.andami.PluginServices;

/**
 * 
 *  Reescritura de la clase ChartPanel. Se reescribe para personalizar el grafico con
 *  opciones de dibujado de rectangulos sobre el area del gráfico.
 *  
 *  @see CharPanel JfreeChart
 *  @author  Alejandro Muñoz Sanchez (alejandro.munoz@uclm.es)
 *  @version 20/12/2007
 *  
 * 
 * */
public class ScatterPlotDiagram extends JPanel implements ChartChangeListener,
         ActionListener, MouseListener, 
        MouseMotionListener, Printable, Serializable,IProcessActions {


	private static final long serialVersionUID = 1L;
	public static final boolean DEFAULT_BUFFER_USED = false;
    public static final int DEFAULT_WIDTH = 680;
    public static final int DEFAULT_HEIGHT = 420;
    public static final int DEFAULT_MINIMUM_DRAW_WIDTH = 300;
    public static final int DEFAULT_MINIMUM_DRAW_HEIGHT = 200;
    public static final int DEFAULT_MAXIMUM_DRAW_WIDTH = 800;
    public static final int DEFAULT_MAXIMUM_DRAW_HEIGHT = 600;
    public static final int DEFAULT_ZOOM_TRIGGER_DISTANCE = 1;
    public static final String PROPERTIES_COMMAND = "PROPERTIES";
    public static final String SAVE_COMMAND = "SAVE";
    public static final String PRINT_COMMAND = "PRINT";
    public static final String NEW_CLASS_COMMAND ="NEW_CLASS";
    
   
    
    private JFreeChart chart;
    private EventListenerList chartMouseListeners;
    private boolean useBuffer;
    private boolean refreshBuffer;
    private Image chartBuffer;
    private int chartBufferHeight;
    private int chartBufferWidth;
    private int minimumDrawWidth;
    private int minimumDrawHeight;
    private int maximumDrawWidth;
    private int maximumDrawHeight;

 
    private JPopupMenu popup;
    private ChartRenderingInfo info;
    private Point2D anchor;
    private double scaleX;
    private double scaleY;
    private PlotOrientation orientation = PlotOrientation.VERTICAL;
    private boolean domainZoomable = false;
    private boolean rangeZoomable = false;
    private Point zoomPoint = null;

   
    private transient Rectangle2D zoomRectangle = null;
    private boolean fillZoomRectangle = true;
    private int zoomTriggerDistance;
    private File defaultDirectoryForSaveAs;
    
  
    private boolean enforceFileExtensions;
    private boolean zoomAroundAnchor;
    protected static ResourceBundle localizationResources  = ResourceBundle.getBundle("org.jfree.chart.LocalizationBundle");
    private int[] bands= null;
   
    private ROIChart activeROI= null;
    private ROIChartList roiList= new ROIChartList();
    Graphics2D g2= null;
    private ManagerROIChartPanel gestorRois= null;
    private FLyrRasterSE rasterSE= null;
   
    
    /**
     * Constructs a panel that displays the specified chart.
     *
     * @param chart  the chart.
     */
    public ScatterPlotDiagram(JFreeChart chart, FLyrRasterSE raster, int bands[]) {

        this(
            chart,
            DEFAULT_WIDTH,
            DEFAULT_HEIGHT,
            DEFAULT_MINIMUM_DRAW_WIDTH,
            DEFAULT_MINIMUM_DRAW_HEIGHT,
            DEFAULT_MAXIMUM_DRAW_WIDTH,
            DEFAULT_MAXIMUM_DRAW_HEIGHT,
            false,
            true,  // properties
            true,  // save
            true,  // print
            true,  // zoom
            true   // tooltips
        );
        this.bands= new int[2];
        updateBands(bands);
        this.rasterSE= raster;
    }

   

    /**
     * Constructs a JFreeChart panel.
     *
     * @param chart  the chart.
     * @param width  the preferred width of the panel.
     * @param height  the preferred height of the panel.
     * @param minimumDrawWidth  the minimum drawing width.
     * @param minimumDrawHeight  the minimum drawing height.
     * @param maximumDrawWidth  the maximum drawing width.
     * @param maximumDrawHeight  the maximum drawing height.
     * @param useBuffer  a flag that indicates whether to use the off-screen
     *                   buffer to improve performance (at the expense of 
     *                   memory).
     * @param properties  a flag indicating whether or not the chart property
     *                    editor should be available via the popup menu.
     * @param save  a flag indicating whether or not save options should be
     *              available via the popup menu.
     * @param print  a flag indicating whether or not the print option
     *               should be available via the popup menu.
     * @param zoom  a flag indicating whether or not zoom options should be 
     *              added to the popup menu.
     * @param tooltips  a flag indicating whether or not tooltips should be 
     *                  enabled for the chart.
     */
    public ScatterPlotDiagram(JFreeChart chart,
                      int width,
                      int height,
                      int minimumDrawWidth,
                      int minimumDrawHeight,
                      int maximumDrawWidth,
                      int maximumDrawHeight,
                      boolean useBuffer,
                      boolean properties,
                      boolean save,
                      boolean print,
                      boolean zoom,
                      boolean tooltips) {

        this.setChart(chart);
        this.chartMouseListeners = new EventListenerList();
        this.info = new ChartRenderingInfo();
        setPreferredSize(new Dimension(width, height));
        this.useBuffer = useBuffer;
        this.refreshBuffer = false;
        this.minimumDrawWidth = minimumDrawWidth;
        this.minimumDrawHeight = minimumDrawHeight;
        this.maximumDrawWidth = maximumDrawWidth;
        this.maximumDrawHeight = maximumDrawHeight;
        this.zoomTriggerDistance = DEFAULT_ZOOM_TRIGGER_DISTANCE;

        this.popup = null;
        if (properties || save || print || zoom) {
            this.popup = createPopupMenu(properties, save, print, zoom);
        }

        enableEvents(AWTEvent.MOUSE_EVENT_MASK);
        enableEvents(AWTEvent.MOUSE_MOTION_EVENT_MASK);
        setDisplayToolTips(tooltips);
        addMouseListener(this);
        addMouseMotionListener(this);

        this.defaultDirectoryForSaveAs = null;
        this.enforceFileExtensions = true;

    }

    /**
     * Returns the chart contained in the panel.
     *
     * @return The chart (possibly <code>null</code>).
     */
    public JFreeChart getChart() {
        return this.chart;
    }

    /**
     * Sets the chart that is displayed in the panel.
     *
     * @param chart  the chart (<code>null</code> permitted).
     */
    public void setChart(JFreeChart chart) {
        if (this.chart != null) {
            this.chart.removeChangeListener(this);
        }
        
        this.chart = chart;
        if (chart != null) {
            this.chart.addChangeListener(this);
            Plot plot = chart.getPlot();
            this.domainZoomable = false;
            this.rangeZoomable = false;
            if (plot instanceof Zoomable) {
                Zoomable z = (Zoomable) plot;
                this.domainZoomable = z.isDomainZoomable();
                this.rangeZoomable = z.isRangeZoomable();
                this.orientation = z.getOrientation();
            }
        }
        else {
            this.domainZoomable = false;
            this.rangeZoomable = false;
        }
        if (this.useBuffer) {
            this.refreshBuffer = true;
        }
        repaint();
    }


    /**
     * Returns the X scale factor for the chart.  This will be 1.0 if no 
     * scaling has been used.
     * 
     * @return The scale factor.
     */
    public double getScaleX() {
        return this.scaleX;
    }
    
    /**
     * Returns the Y scale factory for the chart.  This will be 1.0 if no 
     * scaling has been used.
     * 
     * @return The scale factor.
     */
    public double getScaleY() {
        return this.scaleY;
    }
    
    /**
     * Returns the anchor point.
     * 
     * @return The anchor point (possibly <code>null</code>).
     */
    public Point2D getAnchor() {
        return this.anchor;   
    }
    
    /**
     * Sets the anchor point.  This method is provided for the use of 
     * subclasses, not end users.
     * 
     * @param anchor  the anchor point (<code>null</code> permitted).
     */
    protected void setAnchor(Point2D anchor) {
        this.anchor = anchor;   
    }
    
    /**
     * Returns the popup menu.
     *
     * @return The popup menu.
     */
    public JPopupMenu getPopupMenu() {
        return this.popup;
    }

    /**
     * Sets the popup menu for the panel.
     *
     * @param popup  the popup menu (<code>null</code> permitted).
     */
    public void setPopupMenu(JPopupMenu popup) {
        this.popup = popup;
    }

    /**
     * Returns the chart rendering info from the most recent chart redraw.
     *
     * @return The chart rendering info.
     */
    public ChartRenderingInfo getChartRenderingInfo() {
        return this.info;
    }
    
    /**
     * Returns the default directory for the "save as" option.
     * 
     * @return The default directory (possibly <code>null</code>).
     * 
     * @since 1.0.7
     */
    public File getDefaultDirectoryForSaveAs() {
        return this.defaultDirectoryForSaveAs;
    }

    /**
     * Sets the default directory for the "save as" option.  If you set this
     * to <code>null</code>, the user's default directory will be used.
     * 
     * @param directory  the directory (<code>null</code> permitted).
     * 
     * @since 1.0.7
     */
    public void setDefaultDirectoryForSaveAs(File directory) {
        if (directory != null) {
            if (!directory.isDirectory()) {
                throw new IllegalArgumentException(
                        "The 'directory' argument is not a directory.");
            }
        }
        this.defaultDirectoryForSaveAs = directory;
    }
    
    /**
     * Returns <code>true</code> if file extensions should be enforced, and 
     * <code>false</code> otherwise.
     *
     * @return The flag.
     * 
     * @see #setEnforceFileExtensions(boolean)
     */
    public boolean isEnforceFileExtensions() {
        return this.enforceFileExtensions;
    }

    /**
     * Sets a flag that controls whether or not file extensions are enforced.
     *
     * @param enforce  the new flag value.
     * 
     * @see #isEnforceFileExtensions()
     */
    public void setEnforceFileExtensions(boolean enforce) {
        this.enforceFileExtensions = enforce;
    }
    
    /**
     * Returns the flag that controls whether or not zoom operations are 
     * centered around the current anchor point.
     * 
     * @return A boolean.
     * 
     * @since 1.0.7
     * 
     * @see #setZoomAroundAnchor(boolean)
     */
    public boolean getZoomAroundAnchor() {
        return this.zoomAroundAnchor;
    }
    
    /**
     * Sets the flag that controls whether or not zoom operations are
     * centered around the current anchor point.
     * 
     * @param zoomAroundAnchor  the new flag value.
     * 
     * @since 1.0.7
     * 
     * @see #getZoomAroundAnchor()
     */
    public void setZoomAroundAnchor(boolean zoomAroundAnchor) {
        this.zoomAroundAnchor = zoomAroundAnchor;
    }

    /**
     * Switches the display of tooltips for the panel on or off.  Note that 
     * tooltips can only be displayed if the chart has been configured to
     * generate tooltip items.
     *
     * @param flag  <code>true</code> to enable tooltips, <code>false</code> to
     *              disable tooltips.
     */
    public void setDisplayToolTips(boolean flag) {
        if (flag) {
            ToolTipManager.sharedInstance().registerComponent(this);
        }
        else {
            ToolTipManager.sharedInstance().unregisterComponent(this);
        }
    }

    
    /**
     * Translates a panel (component) location to a Java2D point.
     *
     * @param screenPoint  the screen location (<code>null</code> not 
     *                     permitted).
     *
     * @return The Java2D coordinates.
     */
    public Point2D translateScreenToJava2D(Point screenPoint) {
        Insets insets = getInsets();
        double x = (screenPoint.getX() - insets.left) / this.scaleX;
        double y = (screenPoint.getY() - insets.top) / this.scaleY;
        return new Point2D.Double(x, y);
    }

    /**
     * Applies any scaling that is in effect for the chart drawing to the
     * given rectangle.
     *  
     * @param rect  the rectangle.
     * 
     * @return A new scaled rectangle.
     */
    public Rectangle2D scale(Rectangle2D rect) {
        Insets insets = getInsets();
        double x = rect.getX() * getScaleX() + insets.left;
        double y = rect.getY() * this.getScaleY() + insets.top;
        double w = rect.getWidth() * this.getScaleX();
        double h = rect.getHeight() * this.getScaleY();
        return new Rectangle2D.Double(x, y, w, h);
    }


    /**
     * Paints the component by drawing the chart to fill the entire component,
     * but allowing for the insets (which will be non-zero if a border has been
     * set for this component).  To increase performance (at the expense of
     * memory), an off-screen buffer image can be used.
     *
     * @param g  the graphics device for drawing on.
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (this.chart == null) {
            return;
        }
        Graphics2D g2 = (Graphics2D) g.create();

        // first determine the size of the chart rendering area...
        Dimension size = getSize();
        Insets insets = getInsets();
        Rectangle2D available = new Rectangle2D.Double(insets.left, insets.top,
                size.getWidth() - insets.left - insets.right,
                size.getHeight() - insets.top - insets.bottom);

        // work out if scaling is required...
        boolean scale = false;
        double drawWidth = available.getWidth();
        double drawHeight = available.getHeight();
        this.scaleX = 1.0;
        this.scaleY = 1.0;

        if (drawWidth < this.minimumDrawWidth) {
            this.scaleX = drawWidth / this.minimumDrawWidth;
            drawWidth = this.minimumDrawWidth;
            scale = true;
        }
        else if (drawWidth > this.maximumDrawWidth) {
            this.scaleX = drawWidth / this.maximumDrawWidth;
            drawWidth = this.maximumDrawWidth;
            scale = true;
        }
        if (drawHeight < this.minimumDrawHeight) {
            this.scaleY = drawHeight / this.minimumDrawHeight;
            drawHeight = this.minimumDrawHeight;
            scale = true;
        }
        else if (drawHeight > this.maximumDrawHeight) {
            this.scaleY = drawHeight / this.maximumDrawHeight;
            drawHeight = this.maximumDrawHeight;
            scale = true;
        }

        Rectangle2D chartArea = new Rectangle2D.Double(0.0, 0.0, drawWidth, 
                drawHeight);

        // are we using the chart buffer?
        if (this.useBuffer) {

            // if buffer is being refreshed, it needs clearing unless it is
            // new - use the following flag to track this...
            boolean clearBuffer = true;
            
            // do we need to resize the buffer?
            if ((this.chartBuffer == null) 
                    || (this.chartBufferWidth != available.getWidth())
                    || (this.chartBufferHeight != available.getHeight())) {
                this.chartBufferWidth = (int) available.getWidth();
                this.chartBufferHeight = (int) available.getHeight();
                this.chartBuffer = createImage(this.chartBufferWidth, 
                        this.chartBufferHeight);
//                GraphicsConfiguration gc = g2.getDeviceConfiguration();
//                this.chartBuffer = gc.createCompatibleImage(
//                        this.chartBufferWidth, this.chartBufferHeight, 
//                        Transparency.TRANSLUCENT);
                this.refreshBuffer = true;
                clearBuffer = false;  // buffer is new, no clearing required
            }

            // do we need to redraw the buffer?
            if (this.refreshBuffer) {

                this.refreshBuffer = false; // clear the flag

                Rectangle2D bufferArea = new Rectangle2D.Double(
                        0, 0, this.chartBufferWidth, this.chartBufferHeight);

                Graphics2D bufferG2 = (Graphics2D) 
                        this.chartBuffer.getGraphics();
                if (clearBuffer) {
                    bufferG2.clearRect(0, 0, this.chartBufferWidth, 
                            this.chartBufferHeight);
                }
                if (scale) {
                    AffineTransform saved = bufferG2.getTransform();
                    AffineTransform st = AffineTransform.getScaleInstance(
                            this.scaleX, this.scaleY);
                    bufferG2.transform(st);
                    this.chart.draw(bufferG2, chartArea, this.anchor, 
                            this.info);
                    bufferG2.setTransform(saved);
                }
                else {
                    this.chart.draw(bufferG2, bufferArea, this.anchor, 
                            this.info);
                }
            }
            g2.drawImage(this.chartBuffer, insets.left, insets.top, this);
            g2.draw((Shape)activeROI.getShapesList().get(1));
          // Dibujar las rois que se encuentren activas
        
        }

        // or redrawing the chart every time...
        else {

            AffineTransform saved = g2.getTransform();
            g2.translate(insets.left, insets.top);
            if (scale) {
                AffineTransform st = AffineTransform.getScaleInstance(
                        this.scaleX, this.scaleY);
                g2.transform(st);
            }
            this.chart.draw(g2, chartArea, this.anchor, this.info);
            g2.drawImage(this.chartBuffer, insets.left, insets.top, this);
         
            // Se pintan las Roi activa
            drawsROIs(g2);
            g2.setTransform(saved);
        }

        g2.dispose();
        this.anchor = null; 
    }

  
	/**
     * Receives notification of changes to the chart, and redraws the chart.
     *
     * @param event  details of the chart change event.
     */
    public void chartChanged(ChartChangeEvent event) {
        this.refreshBuffer = true;
        Plot plot = this.chart.getPlot();
        if (plot instanceof Zoomable) {
            Zoomable z = (Zoomable) plot;
            this.orientation = z.getOrientation();
        }
        repaint();
    }

    /**
     * Receives notification of a chart progress event.
     *
     * @param event  the event.
     */


    /**
     * Handles action events generated by the popup menu.
     *
     * @param event  the event.
     */
    public void actionPerformed(ActionEvent event) {

        String command = event.getActionCommand();

        if (command.equals(SAVE_COMMAND)) {
            try {
                doSaveAs();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (command.equals(PRINT_COMMAND)) {
            createChartPrintJob();
        }
       
        else if (command.equals(NEW_CLASS_COMMAND)) {
            // Definicion de nueba clase
        	newRoiChart();
        	if(gestorRois!= null){
        		gestorRois.addNewRoiEntry(activeROI);
        	}
        	updateList();
        	
        }
      
    }
    
    
    public void  updateList(){
    	// Actualizo la roi activa en la lista
    	roiList.getListRois().remove(activeROI.getName());
    	roiList.add(activeROI);
    	updateUI();
    }
    
    
    public void mouseEntered(MouseEvent e) {
    
    }

   
    public void mouseExited(MouseEvent e) {
       
    }

    /**
     * Handles a 'mouse pressed' event.
     * <P>
     * This event is the popup trigger on Unix/Linux.  For Windows, the popup
     * trigger is the 'mouse released' event.
     *
     * @param e  The mouse event.
     */
    public void mousePressed(MouseEvent e) {
        if (this.zoomRectangle == null) {
            Rectangle2D screenDataArea = getScreenDataArea(e.getX(), e.getY());
            if (screenDataArea != null) {
                this.zoomPoint = getPointInRectangle(e.getX(), e.getY(), 
                        screenDataArea);
            }
            else {
                this.zoomPoint = null;
            }
            if (e.isPopupTrigger()) {
                if (this.popup != null) {
                    displayPopupMenu(e.getX(), e.getY());
                }
            }
        }
    }
    
    /**
     * Returns a point based on (x, y) but constrained to be within the bounds
     * of the given rectangle.  This method could be moved to JCommon.
     * 
     * @param x  the x-coordinate.
     * @param y  the y-coordinate.
     * @param area  the rectangle (<code>null</code> not permitted).
     * 
     * @return A point within the rectangle.
     */
    private Point getPointInRectangle(int x, int y, Rectangle2D area) {
        x = (int) Math.max(Math.ceil(area.getMinX()), Math.min(x, 
                Math.floor(area.getMaxX())));   
        y = (int) Math.max(Math.ceil(area.getMinY()), Math.min(y, 
                Math.floor(area.getMaxY())));
        return new Point(x, y);
    }

    /**
     * Handles a 'mouse dragged' event.
     *
     * @param e  the mouse event.
     */
    public void mouseDragged(MouseEvent e) {

        // if the popup menu has already been triggered, then ignore dragging...
        if (this.popup != null && this.popup.isShowing()) {
            return;
        }
        // if no initial zoom point was set, ignore dragging...
        if (this.zoomPoint == null) {
            return;
        }
        Graphics2D g2 = (Graphics2D) getGraphics();

        // Erase the previous zoom rectangle (if any)...
        drawRectangle(g2);

        boolean hZoom = false;
        boolean vZoom = false;
        if (this.orientation == PlotOrientation.HORIZONTAL) {
            hZoom = this.rangeZoomable;
            vZoom = this.domainZoomable;
        }
        else {
            hZoom = this.domainZoomable;              
            vZoom = this.rangeZoomable;
        }
        Rectangle2D scaledDataArea = getScreenDataArea(
                (int) this.zoomPoint.getX(), (int) this.zoomPoint.getY());
        if (hZoom && vZoom) {
            // selected rectangle shouldn't extend outside the data area...
            double xmax = Math.min(e.getX(), scaledDataArea.getMaxX());
            double ymax = Math.min(e.getY(), scaledDataArea.getMaxY());
            this.zoomRectangle = new Rectangle2D.Double(
                    this.zoomPoint.getX(), this.zoomPoint.getY(),
                    xmax - this.zoomPoint.getX(), ymax - this.zoomPoint.getY());
        }
        else if (hZoom) {
            double xmax = Math.min(e.getX(), scaledDataArea.getMaxX());
            this.zoomRectangle = new Rectangle2D.Double(
                    this.zoomPoint.getX(), scaledDataArea.getMinY(),
                    xmax - this.zoomPoint.getX(), scaledDataArea.getHeight());
        }
        else if (vZoom) {
            double ymax = Math.min(e.getY(), scaledDataArea.getMaxY());
            this.zoomRectangle = new Rectangle2D.Double(
                    scaledDataArea.getMinX(), this.zoomPoint.getY(),
                    scaledDataArea.getWidth(), ymax - this.zoomPoint.getY());
        }

        // Draw the new zoom rectangle...
        drawRectangle(g2);
        g2.dispose();
    }

    /**
     * Handles a 'mouse released' event.  On Windows, we need to check if this 
     * is a popup trigger, but only if we haven't already been tracking a zoom
     * rectangle.
     *
     * @param e  information about the event.
     */
    public void mouseReleased(MouseEvent e) {

        if (this.zoomRectangle != null) {
            boolean hZoom = false;
            boolean vZoom = false;
            if (this.orientation == PlotOrientation.HORIZONTAL) {
                hZoom = this.rangeZoomable;
                vZoom = this.domainZoomable;
            }
            else {
                hZoom = this.domainZoomable;              
                vZoom = this.rangeZoomable;
            }
            
            boolean zoomTrigger1 = hZoom && Math.abs(e.getX() 
                - this.zoomPoint.getX()) >= this.zoomTriggerDistance;
            boolean zoomTrigger2 = vZoom && Math.abs(e.getY() 
                - this.zoomPoint.getY()) >= this.zoomTriggerDistance;
            if (zoomTrigger1 || zoomTrigger2) {
                if ((hZoom && (e.getX() < this.zoomPoint.getX())) 
                    || (vZoom && (e.getY() < this.zoomPoint.getY()))) {
                   // restoreAutoBounds();
                }
                else {
                    double x, y, w, h;
                    Rectangle2D screenDataArea = getScreenDataArea(
                            (int) this.zoomPoint.getX(), 
                            (int) this.zoomPoint.getY());
                    // for mouseReleased event, (horizontalZoom || verticalZoom)
                    // will be true, so we can just test for either being false;
                    // otherwise both are true
                    if (!vZoom) {
                        x = this.zoomPoint.getX();
                        y = screenDataArea.getMinY();
                        w = Math.min(this.zoomRectangle.getWidth(),
                                screenDataArea.getMaxX() 
                                - this.zoomPoint.getX());
                        h = screenDataArea.getHeight();
                    }
                    else if (!hZoom) {
                        x = screenDataArea.getMinX();
                        y = this.zoomPoint.getY();
                        w = screenDataArea.getWidth();
                        h = Math.min(this.zoomRectangle.getHeight(),
                                screenDataArea.getMaxY() 
                                - this.zoomPoint.getY());
                    }
                    else {
                        x = this.zoomPoint.getX();
                        y = this.zoomPoint.getY();
                        w = Math.min(this.zoomRectangle.getWidth(),
                                screenDataArea.getMaxX() 
                                - this.zoomPoint.getX());
                        h = Math.min(this.zoomRectangle.getHeight(),
                                screenDataArea.getMaxY() 
                                - this.zoomPoint.getY());
                    }
                    if (activeROI!= null)
                    {
                    	Rectangle2D rectangleArea = new Rectangle2D.Double(x, y, w, h);
                    	g2 = (Graphics2D)getGraphics();
                    	g2.setPaint(activeROI.getColor());
                    	g2.draw(rectangleArea);
                    	activeROI.add(rectangleArea, getRange(rectangleArea));
                    	RoiFromChartProcess process= new RoiFromChartProcess();
        				process.addParam("roi",activeROI);
        				process.addParam("raster",(FLyrRasterSE)rasterSE);
        				process.setActions(this);
        				process.start();
                    	if(gestorRois!= null){
                    		// Cargar la nueva ROI en el destor
                    		gestorRois.clearRoiGraphics();
                    	}
                    }
                  
                }
                this.zoomPoint = null;
                this.zoomRectangle = null;
                updateUI();
            }
            else {
                // Erase the zoom rectangle
                Graphics2D g2 = (Graphics2D) getGraphics();
                drawRectangle(g2);
                g2.dispose();
                this.zoomPoint = null;
                this.zoomRectangle = null;
            }

        }

        else if (e.isPopupTrigger()) {
            if (this.popup != null) {
                displayPopupMenu(e.getX(), e.getY());
            }
        }
    }
    
    
  
    /**
     * @param selection .
     * @return range[] rangos del rectangulo
     */
    public Range[] getRange(Rectangle2D selection) {

    	Range[] rangos= new Range[2];
    	
        // get the origin of the zoom selection in the Java2D space used for
        // drawing the chart (that is, before any scaling to fit the panel)
        Point2D selectOrigin = translateScreenToJava2D(new Point(
                (int) Math.ceil(selection.getX()), 
                (int) Math.ceil(selection.getY())));
        PlotRenderingInfo plotInfo = this.info.getPlotInfo();
        Rectangle2D scaledDataArea = getScreenDataArea(
                (int) selection.getCenterX(), (int) selection.getCenterY());
        if ((selection.getHeight() > 0) && (selection.getWidth() > 0)) {

            double hLower = (selection.getMinX() - scaledDataArea.getMinX()) 
                / scaledDataArea.getWidth();
            double hUpper = (selection.getMaxX() - scaledDataArea.getMinX()) 
                / scaledDataArea.getWidth();
            double vLower = (scaledDataArea.getMaxY() - selection.getMaxY()) 
                / scaledDataArea.getHeight();
            double vUpper = (scaledDataArea.getMaxY() - selection.getMinY()) 
                / scaledDataArea.getHeight();

            Plot p = this.chart.getPlot();
            if (p instanceof ScatterPlotChart) {
              ScatterPlotChart z = (ScatterPlotChart) p;
                if (z.getOrientation() == PlotOrientation.HORIZONTAL) {
                   rangos[0]= z.getRangeX(vLower, vUpper, plotInfo, selectOrigin);
                    rangos[1]=z.getRangeY(hLower, hUpper, plotInfo, selectOrigin);
                }
                else {
                	// devolver las coordenadas del rectangulo.
                	rangos[0]=z.getRangeX(hLower, hUpper, plotInfo, selectOrigin);
                	rangos[1]=z.getRangeY(vLower, vUpper, plotInfo, selectOrigin);
                }
            }

        }

        return rangos;
    }


    /**
     * Returns the data area for the chart (the area inside the axes) with the
     * current scaling applied (that is, the area as it appears on screen).
     *
     * @return The scaled data area.
     */
    public Rectangle2D getScreenDataArea() {
        Rectangle2D dataArea = this.info.getPlotInfo().getDataArea();
        Insets insets = getInsets();
        double x = dataArea.getX() * this.scaleX + insets.left;
        double y = dataArea.getY() * this.scaleY + insets.top;
        double w = dataArea.getWidth() * this.scaleX;
        double h = dataArea.getHeight() * this.scaleY;
        return new Rectangle2D.Double(x, y, w, h);
    }
    
    /**
     * Returns the data area (the area inside the axes) for the plot or subplot,
     * with the current scaling applied.
     *
     * @param x  the x-coordinate (for subplot selection).
     * @param y  the y-coordinate (for subplot selection).
     * 
     * @return The scaled data area.
     */
    public Rectangle2D getScreenDataArea(int x, int y) {
        PlotRenderingInfo plotInfo = this.info.getPlotInfo();
        Rectangle2D result;
        if (plotInfo.getSubplotCount() == 0) {
            result = getScreenDataArea();
        } 
        else {
            Point2D selectOrigin = translateScreenToJava2D(new Point(x, y));
            int subplotIndex = plotInfo.getSubplotIndex(selectOrigin);
            if (subplotIndex == -1) {
                return null;
            }
            result = scale(plotInfo.getSubplotInfo(subplotIndex).getDataArea());
        }
        return result;
    }

    
    /**
     *
     * Pinta el rectangulo sombreado mientras se realiza la selección 
     * @param g2 the graphics device. 
     */
    private void drawRectangle(Graphics2D g2) {
        // Set XOR mode to draw the zoom rectangle
        g2.setXORMode(Color.gray);
        if (this.zoomRectangle != null) {
            if (this.fillZoomRectangle) {
                g2.fill(this.zoomRectangle);
            }
            else {
                g2.draw(this.zoomRectangle);
            }
        }
        // Reset to the default 'overwrite' mode
        g2.setPaintMode();
    }
    
   
    /**
     * Displays a dialog that allows the user to edit the properties for the
     * current chart.
     * 
     * @since 1.0.3
     */
    public void doEditChartProperties() {
    	
    	   /*ChartEditorManager.getChartEditor(this.chart);
        int result = JOptionPane.showConfirmDialog(this, editor, 
                localizationResources.getString("Chart_Properties"),
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            editor.updateChart(this.chart);
        }
*/
    }

    /**
     * Opens a file chooser and gives the user an opportunity to save the chart
     * in PNG format.
     *
     * @throws IOException if there is an I/O error.
     */
    public void doSaveAs() throws IOException {

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(this.defaultDirectoryForSaveAs);
        ExtensionFileFilter filter = new ExtensionFileFilter(
                localizationResources.getString("PNG_Image_Files"), ".png");
        fileChooser.addChoosableFileFilter(filter);

        int option = fileChooser.showSaveDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            String filename = fileChooser.getSelectedFile().getPath();
            if (isEnforceFileExtensions()) {
                if (!filename.endsWith(".png")) {
                    filename = filename + ".png";
                }
            }
            ChartUtilities.saveChartAsPNG(new File(filename), this.chart, 
                    getWidth(), getHeight());
        }

    }

    /**
     * Creates a print job for the chart.
     */
    public void createChartPrintJob() {

        PrinterJob job = PrinterJob.getPrinterJob();
        PageFormat pf = job.defaultPage();
        PageFormat pf2 = job.pageDialog(pf);
        if (pf2 != pf) {
            job.setPrintable(this, pf2);
            if (job.printDialog()) {
                try {
                    job.print();
                }
                catch (PrinterException e) {
                    JOptionPane.showMessageDialog(this, e);
                }
            }
        }

    }

    /**
     * Prints the chart on a single page.
     *
     * @param g  the graphics context.
     * @param pf  the page format to use.
     * @param pageIndex  the index of the page. If not <code>0</code>, nothing 
     *                   gets print.
     *
     * @return The result of printing.
     */
    public int print(Graphics g, PageFormat pf, int pageIndex) {

        if (pageIndex != 0) {
            return NO_SUCH_PAGE;
        }
        Graphics2D g2 = (Graphics2D) g;
        double x = pf.getImageableX();
        double y = pf.getImageableY();
        double w = pf.getImageableWidth();
        double h = pf.getImageableHeight();
        this.chart.draw(g2, new Rectangle2D.Double(x, y, w, h), this.anchor, 
                null);
        return PAGE_EXISTS;

    }

    /**
     * Adds a listener to the list of objects listening for chart mouse events.
     *
     * @param listener  the listener (<code>null</code> not permitted).
     */
    public void addChartMouseListener(ChartMouseListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("Null 'listener' argument.");
        }
        this.chartMouseListeners.add(ChartMouseListener.class, listener);
    }

    /**
     * Removes a listener from the list of objects listening for chart mouse 
     * events.
     *
     * @param listener  the listener.
     */
    public void removeChartMouseListener(ChartMouseListener listener) {
        this.chartMouseListeners.remove(ChartMouseListener.class, listener);
    }


    /**
     * Creates a popup menu for the panel.
     *
     * @param properties  include a menu item for the chart property editor.
     * @param save  include a menu item for saving the chart.
     * @param print  include a menu item for printing the chart.
     * @param zoom  include menu items for zooming.
     *
     * @return The popup menu.
     */
    protected JPopupMenu createPopupMenu(boolean properties, 
                                         boolean save, 
                                         boolean print,
                                         boolean zoom) {

        JPopupMenu result = new JPopupMenu("Chart:");
        boolean separator = false;

        JMenuItem newClassItem = new JMenuItem( PluginServices.getText(this,"new_class"));
        newClassItem.setActionCommand(NEW_CLASS_COMMAND);
        newClassItem.addActionListener(this);
        result.add(newClassItem);
        separator = true;
         
        if (properties) {
        }

        if (save) {
            if (separator) {
                result.addSeparator();
                separator = false;
            }
            JMenuItem saveItem = new JMenuItem(
                    localizationResources.getString("Save_as..."));
            saveItem.setActionCommand(SAVE_COMMAND);
            saveItem.addActionListener(this);
            result.add(saveItem);
            separator = true;
        }

        if (print) {
            if (separator) {
                result.addSeparator();
                separator = false;
            }
            JMenuItem printItem = new JMenuItem(
                    localizationResources.getString("Print..."));
            printItem.setActionCommand(PRINT_COMMAND);
            printItem.addActionListener(this);
            result.add(printItem);
            separator = true;
        }

        if (zoom) {
            if (separator) {
                result.addSeparator();
                separator = false;
            }
        }

        return result;

    }

    /**
     * The idea is to modify the zooming options depending on the type of chart 
     * being displayed by the panel.
     *
     * @param x  horizontal position of the popup.
     * @param y  vertical position of the popup.
     */
    protected void displayPopupMenu(int x, int y) {

        if (this.popup != null){
			updateUI();
            this.popup.show(this, x, y);
          
        }

    }
    
    
    /* (non-Javadoc)
     * @see javax.swing.JPanel#updateUI()
     */
    public void updateUI() {
        // here we need to update the UI for the popup menu, if the panel
        // has one...
        if (this.popup != null) {
            SwingUtilities.updateComponentTreeUI(this.popup);
        }
        super.updateUI();
      
          
    }
    
    
    public void updateBands(int [] bandas){
    	bands= bandas;
    }
    
    // Devuelve la lista de ROIs definidas sobre el grafico.
    public ROIChartList getROIChartList(){
    	return roiList;
    }
    
    
    // Borra una ROI del grafico (con todas las geometrias asociadas)
    public void clearROIChart(int index){
    	roiList.deleteROI(new Integer(index));
    }
    
    
    /**Pinta la lista de ROis Visibles sobre el grafico*/
    public void drawsROIs(Graphics2D g2){
    	
    	Iterator iterator= roiList.getList().iterator();
    	ROIChart roiDraw=null;
    	
    	while(iterator.hasNext()){
    		
    		roiDraw= (ROIChart)iterator.next();
    		if(activeROI.equals(roiDraw)){
    			g2.setPaint(roiDraw.getColor());
    			for(int i=0; i<roiDraw.getShapesList().size(); i++){
    				g2.fill((Shape)roiDraw.getShapesList().get(i));
    			}
    		
    		}
    			
    		else if(roiDraw.getShapesList().size()>0){		
    			g2.setPaint(roiDraw.getColor());
    			for(int i=0; i<roiDraw.getShapesList().size(); i++){
    				g2.setPaint(roiDraw.getColor());
    				g2.draw((Shape)roiDraw.getShapesList().get(i));
    				}	
    		}
    		
    	}
    }

    	
    /**
     * Crea una nueva roiChart 
     * 
     * */
    public void newRoiChart(){
    	activeROI= new ROIChart(roiList.getNexColor(),roiList.getdefaultName(),bands);
    	updateList();
    }
    
    
    /**
     *  @return devuelve la ROI activa del diagrama. 
     * */
    public ROIChart getActiveRoiChart(){
    	return activeROI;
    }
    
    
    /**
     *  Establece la roi activa en el diagrama
     * */
    public void setActiveRoi(ROIChart newActiveRoiChart){
    	activeROI= newActiveRoiChart;	
    }
    
    /**
     * Asignacion del panel de gestion de Rois
     * */
    public void setManagerROi(ManagerROIChartPanel gestorRois){
    	this.gestorRois =gestorRois;
    }
    
    
    public void cleanChart(){
    }

	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
	}



	public void interrupted() {
		// TODO Auto-generated method stub
		
	}



	public void end(Object param) {
		// TODO Auto-generated method stub
		
	}

}
