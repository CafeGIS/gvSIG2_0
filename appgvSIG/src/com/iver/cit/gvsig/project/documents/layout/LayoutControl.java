package com.iver.cit.gvsig.project.documents.layout;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.MemoryImageSource;
import java.util.HashMap;

import javax.swing.JComponent;

import org.gvsig.fmap.dal.exception.ReadException;

import com.iver.cit.gvsig.project.documents.layout.fframes.IFFrame;
import com.iver.cit.gvsig.project.documents.layout.fframes.IFFrameEditableVertex;
import com.iver.cit.gvsig.project.documents.layout.fframes.IFFrameUseFMap;
import com.iver.cit.gvsig.project.documents.layout.geometryadapters.GeometryAdapter;
import com.iver.cit.gvsig.project.documents.layout.geometryadapters.PolyLineAdapter;
import com.iver.cit.gvsig.project.documents.layout.gui.FPopupMenu;
import com.iver.cit.gvsig.project.documents.layout.gui.Layout;
import com.iver.cit.gvsig.project.documents.layout.tools.LayoutAddBoxListenerImpl;
import com.iver.cit.gvsig.project.documents.layout.tools.LayoutAddCircleListenerImpl;
import com.iver.cit.gvsig.project.documents.layout.tools.LayoutAddLegendListenerImpl;
import com.iver.cit.gvsig.project.documents.layout.tools.LayoutAddLineListenerImpl;
import com.iver.cit.gvsig.project.documents.layout.tools.LayoutAddNorthListenerImpl;
import com.iver.cit.gvsig.project.documents.layout.tools.LayoutAddOverViewListenerImpl;
import com.iver.cit.gvsig.project.documents.layout.tools.LayoutAddPictureListenerImpl;
import com.iver.cit.gvsig.project.documents.layout.tools.LayoutAddPointListenerImpl;
import com.iver.cit.gvsig.project.documents.layout.tools.LayoutAddPolygonListenerImpl;
import com.iver.cit.gvsig.project.documents.layout.tools.LayoutAddPolylineListenerImpl;
import com.iver.cit.gvsig.project.documents.layout.tools.LayoutAddRectangleListenerImpl;
import com.iver.cit.gvsig.project.documents.layout.tools.LayoutAddScaleListenerImpl;
import com.iver.cit.gvsig.project.documents.layout.tools.LayoutAddTextListenerImpl;
import com.iver.cit.gvsig.project.documents.layout.tools.LayoutAddViewListenerImpl;
import com.iver.cit.gvsig.project.documents.layout.tools.LayoutEditGraphicsListenerImpl;
import com.iver.cit.gvsig.project.documents.layout.tools.LayoutPanListenerImpl;
import com.iver.cit.gvsig.project.documents.layout.tools.LayoutSelectListenerImpl;
import com.iver.cit.gvsig.project.documents.layout.tools.LayoutViewPanListenerImpl;
import com.iver.cit.gvsig.project.documents.layout.tools.LayoutViewZoomInListenerImpl;
import com.iver.cit.gvsig.project.documents.layout.tools.LayoutViewZoomOutListenerImpl;
import com.iver.cit.gvsig.project.documents.layout.tools.LayoutZoomInListenerImpl;
import com.iver.cit.gvsig.project.documents.layout.tools.LayoutZoomOutListenerImpl;
import com.iver.cit.gvsig.project.documents.layout.tools.behavior.LayoutBehavior;
import com.iver.cit.gvsig.project.documents.layout.tools.behavior.LayoutEditBehavior;
import com.iver.cit.gvsig.project.documents.layout.tools.behavior.LayoutMoveBehavior;
import com.iver.cit.gvsig.project.documents.layout.tools.behavior.LayoutPointBehavior;
import com.iver.cit.gvsig.project.documents.layout.tools.behavior.LayoutRectangleBehavior;
import com.iver.cit.gvsig.project.documents.layout.tools.behavior.LayoutSelectBehavior;
import com.iver.cit.gvsig.project.documents.layout.tools.behavior.LayoutViewMoveBehavior;
import com.iver.cit.gvsig.project.documents.layout.tools.behavior.LayoutViewZoomBehavior;
import com.iver.cit.gvsig.project.documents.layout.tools.behavior.LayoutZoomBehavior;
import com.iver.cit.gvsig.project.documents.layout.tools.listener.ILayoutGraphicListener;


/**
 * Control of Layout.
 *
 * @author Vicente Caballero Navarro
 */
public class LayoutControl extends JComponent implements LayoutDrawListener {
    public static final int DESACTUALIZADO = 4;
    public static final int ACTUALIZADO = 5;
    private int status = DESACTUALIZADO;
    private Rectangle2D rectVisible;
    private BufferedImage img = null;
    private BufferedImage imgRuler = null;
    private boolean initial = true;
    private LayoutContext layoutContext;
    private Point origin = new Point(50, 50);
    private Point rectOrigin = new Point(origin);
    private Rectangle2D.Double rect = new Rectangle2D.Double(rectOrigin.x,
            rectOrigin.y, 400, 300);
    private FLayoutDraw layoutDraw = null;
    private FLayoutFunctions layoutFunctions = null;
    private LayoutBehavior currentLayoutTool = null;
    private Image imageCursor = null;
    private HashMap namesLayoutTools = new HashMap();
    private Point m_FirstPoint = new Point(0, 0);
    private Point m_PointAnt = new Point(0, 0);
    private Point m_LastPoint = new Point(0, 0);
    private LayoutEvents layoutEvents;
    private Point position;
    private GeometryAdapter geometryAdapter = new PolyLineAdapter();

    //private String prevTool;
    private String currentTool;
    private boolean m_bCancelDrawing = false;
    private Rectangle reSel = null;
    private boolean isReSel = true;
    private FLayoutZooms layoutZooms;

    private static Cursor transparentCursor = Toolkit.getDefaultToolkit()
    	.createCustomCursor(Toolkit.getDefaultToolkit().createImage(new MemoryImageSource(
            16, 16, new int[16 * 16], 0, 16)),
            new Point(0, 0), "invisiblecursor");


    /**
     * Create a new object of LayoutControl.
     * @param layout
     */
    public LayoutControl(Layout layout) {
        setLayoutContext(layout.getLayoutContext());
        layoutDraw = new FLayoutDraw(layout);
        layoutEvents = new LayoutEvents(layout);
        layoutZooms = new FLayoutZooms(layout);
        layoutFunctions = new FLayoutFunctions(layout);
        addComponentListener(layoutEvents);
        addMouseMotionListener(layoutEvents);
        addMouseListener(layoutEvents);

        layoutContext.updateFFrames();

        LayoutPanListenerImpl lpl = new LayoutPanListenerImpl(layout);
        addLayoutTool("layoutpan", new LayoutMoveBehavior(lpl));

        LayoutZoomInListenerImpl lzil = new LayoutZoomInListenerImpl(layout);
        addLayoutTool("layoutzoomin", new LayoutZoomBehavior(lzil));

        LayoutZoomOutListenerImpl lzol = new LayoutZoomOutListenerImpl(layout);
        addLayoutTool("layoutzoomout", new LayoutZoomBehavior(lzol));

        LayoutAddViewListenerImpl lavl = new LayoutAddViewListenerImpl(layout);
        addLayoutTool("layoutaddview", new LayoutRectangleBehavior(lavl));

        LayoutAddOverViewListenerImpl laovl = new LayoutAddOverViewListenerImpl(layout);
        addLayoutTool("layoutaddoverview", new LayoutRectangleBehavior(laovl));

        LayoutAddPictureListenerImpl lapl = new LayoutAddPictureListenerImpl(layout);
        addLayoutTool("layoutaddpicture", new LayoutRectangleBehavior(lapl));

        LayoutAddNorthListenerImpl lanorthl = new LayoutAddNorthListenerImpl(layout);
        addLayoutTool("layoutaddnorth", new LayoutRectangleBehavior(lanorthl));

        LayoutAddScaleListenerImpl lasl = new LayoutAddScaleListenerImpl(layout);
        addLayoutTool("layoutaddscale", new LayoutRectangleBehavior(lasl));

        LayoutAddLegendListenerImpl lall = new LayoutAddLegendListenerImpl(layout);
        addLayoutTool("layoutaddlegend", new LayoutRectangleBehavior(lall));

        LayoutAddTextListenerImpl latl = new LayoutAddTextListenerImpl(layout);
        addLayoutTool("layoutaddtext", new LayoutRectangleBehavior(latl));

        LayoutAddBoxListenerImpl labl = new LayoutAddBoxListenerImpl(layout);
        addLayoutTool("layoutaddbox", new LayoutRectangleBehavior(labl));

        LayoutAddPointListenerImpl lapointl = new LayoutAddPointListenerImpl(layout);
        addLayoutTool("layoutaddpoint", new LayoutPointBehavior(lapointl));

        LayoutAddLineListenerImpl lalinel = new LayoutAddLineListenerImpl(layout);
        addLayoutTool("layoutaddline", new LayoutPointBehavior(lalinel));

        LayoutAddPolygonListenerImpl lapolygonl = new LayoutAddPolygonListenerImpl(layout);
        addLayoutTool("layoutaddpolygon", new LayoutPointBehavior(lapolygonl));

        LayoutAddPolylineListenerImpl lapolylinel = new LayoutAddPolylineListenerImpl(layout);
        addLayoutTool("layoutaddpolyline", new LayoutPointBehavior(lapolylinel));

        LayoutAddCircleListenerImpl lacirclel = new LayoutAddCircleListenerImpl(layout);
        addLayoutTool("layoutaddcircle", new LayoutPointBehavior(lacirclel));

        LayoutAddRectangleListenerImpl larectanglel = new LayoutAddRectangleListenerImpl(layout);
        addLayoutTool("layoutaddrectangle",
            new LayoutPointBehavior(larectanglel));

        LayoutViewPanListenerImpl lvpl = new LayoutViewPanListenerImpl(layout);
        addLayoutTool("layoutviewpan", new LayoutViewMoveBehavior(lvpl));

        LayoutViewZoomInListenerImpl lvzil = new LayoutViewZoomInListenerImpl(layout);
        addLayoutTool("layoutviewzoomin", new LayoutViewZoomBehavior(lvzil));

        LayoutViewZoomOutListenerImpl lvzol = new LayoutViewZoomOutListenerImpl(layout);
        addLayoutTool("layoutviewzoomout", new LayoutViewZoomBehavior(lvzol));

        LayoutSelectListenerImpl lselectl = new LayoutSelectListenerImpl(layout);
        addLayoutTool("layoutselect", new LayoutSelectBehavior(lselectl));

        LayoutEditGraphicsListenerImpl leditl = new LayoutEditGraphicsListenerImpl(layout);
        addLayoutTool("layoutedit", new LayoutEditBehavior(leditl));

        setTool("layoutzoomin");
        FPopupMenu.registerExtensionPoint();
    }

    /**
     * Returns the name of the current selected tool on this Layout
     *
     * @return A tool name.
     */
    public String getCurrentTool() {
        return currentTool;
    }

    /**
     * Add a new Layout tool.
     *
     * @param name Name of tool.
     * @param tool LayoutBehavior
     */
    public void addLayoutTool(String name, LayoutBehavior tool) {
        namesLayoutTools.put(name, tool);
        tool.setLayoutControl(this);
    }

    /**
     * Inserts the LayoutContext.
     *
     * @param lc LayoutContext.
     */
    public void setLayoutContext(LayoutContext lc) {
        layoutContext = lc;
        layoutContext.addLayoutDrawListener(this);
    }

    /**
     * paintComponent of Layout.
     *
     * @param g Graphics of Layout.
     */
    protected void paintComponent(Graphics g) {
        clipVisibleRect((Graphics2D) g);

        Rectangle rClip = g.getClipBounds();

        if (rClip == null) {
            System.err.println("clip = null");
        }

        switch (status) {
        case DESACTUALIZADO:
            if (getWidth() == 0) {
                return;
            }

            img = new BufferedImage(getWidth(), getHeight(),
                    BufferedImage.TYPE_INT_ARGB);
            imgRuler = new BufferedImage(getWidth(), getHeight(),
                    BufferedImage.TYPE_INT_ARGB);

            Graphics gimag = img.getGraphics();
            Graphics gimgRuler = imgRuler.getGraphics();
            clipVisibleRect((Graphics2D) gimag);

            if (initial) {
                fullRect();
                initial = false;
            }

            try {
                layoutDraw.drawLayout((Graphics2D) gimag, img);
            } catch (ReadException e) {
                e.printStackTrace();
            }

            g.setClip(rClip);
            layoutDraw.drawRectangle((Graphics2D) g);

            g.drawImage(img, 0, 0, this);
            g.setClip(rClip);

            layoutDraw.drawGrid((Graphics2D) gimgRuler);
            layoutDraw.drawRuler((Graphics2D) gimgRuler, Color.black);
            setStatus(ACTUALIZADO);
            repaint();

            break;

        case ACTUALIZADO:
            layoutDraw.drawRectangle((Graphics2D) g);

            if (currentLayoutTool != null) {
                currentLayoutTool.paintComponent(g);
            } else {
                g.drawImage(img, 0, 0, this);

                layoutDraw.drawHandlers((Graphics2D) g, Color.black);
            }

            g.setClip(rClip);
            drawCursor(g);
        }
    }

    /**
     * Inserts the status of Layout.
     *
     * @param s Status of Layout.
     */
    public void setStatus(int s) {
        status = s;
    }

    /**
     * Clip on visible rectangle.
     *
     * @param g2d Graphics.
     */
    private void clipVisibleRect(Graphics2D g2d) {
        rectVisible = this.getVisibleRect();
        g2d.clipRect((int) rectVisible.getMinX(), (int) rectVisible.getMinY(),
            (int) rectVisible.getWidth(), (int) rectVisible.getHeight());
    }

    /**
     * Returns the image with the ruler.
     *
     * @return Ruler image.
     */
    public BufferedImage getImgRuler() {
        return imgRuler;
    }

    /**
	 * It obtains the rect that is adjusted to the size of the window,
	 * to see the full extent of layout.
	 */
    public void fullRect() {
        rect.setRect(origin.x, origin.y, getWidth() - (origin.x * 2),
            getHeight() - (origin.x * 2));

        if (layoutContext.getAttributes().isLandSpace()) {
            rect = layoutContext.getAttributes().getRectangleLandscape(rect,
                    getWidth(), getHeight());
        } else {
            rect = layoutContext.getAttributes().getRectanglePortrait(rect,
                    getWidth(), getHeight());
        }

        refresh();
    }

    /**
	 * Returns the rectangle that represents the sheet.
	 * In pixels.
	 *
	 * @return Rectangle2D.Double Rectangle that represents the sheet.
	 */
    public Rectangle2D.Double getRect() {
        return rect;
    }

    /**
	 * Inserts the rectangle that represents the sheet.
	 * In pixels.
	 *
	 */
    public void setRect(Rectangle2D r) {
        rect.setRect(r);
    }

    /**
     * Returns the current image of Layout.
     *
     * @return Current image of Layout.
     */
    public BufferedImage getImage() {
        return img;
    }

    /**
     * Draw the cursor on the Graphics.
     *
     * @param g Graphics.
     */
    private void drawCursor(Graphics g) {
        if ((imageCursor != null) && (position != null)) {
            Point pAdjusted = position;
            g.drawImage(imageCursor, (int) pAdjusted.getX() - 16,
                (int) pAdjusted.getY() - 16, this);
        }
    }

    /**
	 * Changes the pointer of the mouse by the image of parameter.
	 *
	 * @param image Image
	 */
    public void setMapCursor(Image image) {
        imageCursor = image;
    }

    /**
	 * It establishes as selected to the tool from its name of identification.
	 *
	 * @param toolName Name of identification tool.
	 */
    public void setTool(String toolName) {
        //prevTool=getCurrentTool();
        LayoutBehavior layoutTool = (LayoutBehavior) namesLayoutTools.get(toolName);
        currentLayoutTool = layoutTool;
        currentLayoutTool.setLayoutControl(this);
        currentTool = toolName;
        this.setMapCursor(layoutTool.getImageCursor());

        if (getCurrentLayoutTool().getListener() instanceof ILayoutGraphicListener) {
            geometryAdapter = ((ILayoutGraphicListener) getCurrentLayoutTool()
                                                            .getListener()).createGeometryAdapter();
        }

        if (getCurrentTool().equals("layoutedit")) {
            startEdit();
        } else {
            stopEdit();
        }
    }

    /**
	 * Start the vertex edition of graphics.
	 *
	 */
    public void startEdit() {
        IFFrame[] fframes = layoutContext.getFFrames();

        for (int i = 0; i < fframes.length; i++) {
            IFFrame frame = fframes[i];

            if (frame instanceof IFFrameEditableVertex) {
                if (frame.getSelected() != IFFrame.NOSELECT) {
                    ((IFFrameEditableVertex) frame).startEditing();
                } else {
                    ((IFFrameEditableVertex) frame).stopEditing();
                }
            }
        }

        refresh();
    }

    /**
	 * Stop the vertex edition of graphics.
	 *
	 */
    public void stopEdit() {
        boolean refresh = false;
        IFFrame[] fframes = layoutContext.getFFrames();

        for (int i = 0; i < fframes.length; i++) {
            IFFrame frame = fframes[i];

            if (frame instanceof IFFrameEditableVertex) {
                if (((IFFrameEditableVertex) frame).isEditing()) {
                    ((IFFrameEditableVertex) fframes[i]).stopEditing();
                    refresh = true;
                }
            }
        }

        if (refresh) {
            refresh();
        }
    }

    /**
	 * It returns the point that represents the northwest corner of the Layout.
	 *
	 * @return Point.
	 */
    public Point getRectOrigin() {
        return rectOrigin;
    }

    /**
     * Returns the object to draw the Layout.
     *
     * @return FLayoutDraw.
     */
    public FLayoutDraw getLayoutDraw() {
        return layoutDraw;
    }

    /**
     * Returns the current Layout tool.
     *
     * @return LayoutBehavior Current Layout Tool.
     */
    public LayoutBehavior getCurrentLayoutTool() {
        return currentLayoutTool;
    }

    /**
	 * It returns the first click point of mouse.
	 *
	 * @return Point.
	 */
    public Point getFirstPoint() {
        return m_FirstPoint;
    }

    /**
	 * Returns the previous click of mouse.
	 *
	 * @return Point.
	 */
    public Point getPointAnt() {
        return m_PointAnt;
    }

    /**
	 * Returns the last click point of mouse.
	 *
	 * @return Point.
	 */
    public Point getLastPoint() {
        return m_LastPoint;
    }

    /**
	 * Inserts the first click point of mouse.
	 *
	 * @param p
	 *            Point.
	 */
    public void setFirstPoint() {
        m_FirstPoint = position;
    }

    /**
	 * Inserts the previous click point of mouse.
	 *
	 * @param p
	 *            Point.
	 */
    public void setPointAnt() {
        m_PointAnt = position;
    }

    /**
	 * Inserts the last click point of mouse.
	 *
	 * @param p
	 *            Point.
	 */
    public void setLastPoint() {
        m_LastPoint = position;
    }

    /**
     * Insert the position point and calculate the new position if the grid is actived.
     *
     * @param point2 Position.
     */
    public void setPosition(Point point2) {
        if (layoutContext.isAdjustingToGrid() &&
                getCurrentLayoutTool().isAdjustable()) {
            position = FLayoutUtilities.getPointGrid(point2,
                    layoutContext.getAttributes().getNumUnitsX(),
                    layoutContext.getAttributes().getNumUnitsY(),
                    layoutContext.getAT());
        } else {
            position = point2;
        }
    }

    /**
     * Returns the position adjusted point.
     *
     * @return
     */
    public Point getPosition() {
        return position;
    }

    /**
	 * Returns the AffineTransform that is applying in the Layout.
	 *
	 * @return AffineTransform
	 */
    public AffineTransform getAT() {
        return layoutContext.getAT();
    }

    /**
	 * It returns the current GeometryAdapter.
	 * @return Current GeometryAdapter.
	 */
    public GeometryAdapter getGeometryAdapter() {
        return geometryAdapter;
    }

    /**
	  * Remove last point of geometryAdapter.
	  *
	  */
    public void delLastPoint() {
        getGeometryAdapter().delLastPoint();
    }

    /**
     * Add a new point to geometryAdapter.
     *
     * @return Number of points in the geometryAdapter.
     */
    public int addGeometryAdapterPoint() {
        return getGeometryAdapter().addPoint(FLayoutUtilities.toSheetPoint(
                getPosition(), getAT()));
    }

    /**
     * Change the position of mousemoved point of geometryAdapter.
     */
    public void setGeometryAdapterPoinPosition() {
        getGeometryAdapter().pointPosition(FLayoutUtilities.toSheetPoint(
                getPosition(), getAT()));
    }

    /**
     * Clear the image of pointer of mouse.
     */
    public void clearMouseImage() {
        setCursor(transparentCursor);
    }

    /**
     * Refres the Layout.
     */
    public void refresh() {
        setStatus(LayoutControl.DESACTUALIZADO);
        repaint();
    }

    /**
	 * It returns true if the drawing has been canceled.
	 *
	 * @return true if the drawn has been canceled.
	 */
    public synchronized boolean isDrawingCancelled() {
        return m_bCancelDrawing;
    }

    /**
	 * It cancels the drawing if the parameter is true.
	 *
	 * @param b true if the drawing wants to be canceled
	 */
    public synchronized void setCancelDrawing(boolean b) {
        m_bCancelDrawing = b;

        for (int i = 0; i < layoutContext.getFFrames().length; i++) {
            IFFrame fframe = layoutContext.getFFrame(i);

            if (fframe instanceof IFFrameUseFMap &&
                    (((IFFrameUseFMap) fframe).getMapContext() != null)) {
                // //TODO((FFrameView)
                // getFFrames().get(i)).getFMap().setCancelDrawing(b);
            }
        }
    }

    /**
	 * Returns the rectangle of selection.
	 *
	 * @return Rectangle of selection.
	 */
    public Rectangle getReSel() {
        return reSel;
    }

    /**
	 * It returns true if should draw the rectangle of selection and does the selection.
	 *
	 * @return true if should draw the rectangle of selection.
	 */
    public boolean isReSel() {
        return isReSel;
    }

    /**
	 * Insert true if should draw the rectangle of selection and does the selection.
	 *
	 * @param b boolean.
	 */
    public void setIsReSel(boolean b) {
        isReSel = b;
    }

    /**
	 * It does a full extent of the layers that contains the view of the
	 * FFrameView selected.
	 *
	 * @throws ReadDriverException
	 */
    public void viewFull() throws ReadException {
        IFFrame[] fframes = layoutContext.getFFrameSelected();

        for (int i = 0; i < fframes.length; i++) {
            if (fframes[i] instanceof IFFrameUseFMap) {
                IFFrameUseFMap fframe = (IFFrameUseFMap) fframes[i];

                if (fframe.getMapContext() != null) {
                    fframe.fullExtent();
                }

                fframe.refresh();
            }
        }

        refresh();
    }

    /**
     * Returns the object to make zooms.
     *
     * @return FLayoutZooms.
     */
    public FLayoutZooms getLayoutZooms() {
        return layoutZooms;
    }

    /**
     * Returns the object of FLayoutFunctions.
     *
     * @return FLayoutFunctions
     */
    public FLayoutFunctions getLayoutFunctions() {
        return layoutFunctions;
    }

    /**
     * Returns the LayoutContext.
     *
     * @return LayoutContext.
     */
    public LayoutContext getLayoutContext() {
        return layoutContext;
    }

    /**
     * Inserts the geometryAdapter.
     *
     * @param adapter GeometryAdapter.
     */
    public void setGeometryAdapter(GeometryAdapter adapter) {
        geometryAdapter = adapter;
    }
}
