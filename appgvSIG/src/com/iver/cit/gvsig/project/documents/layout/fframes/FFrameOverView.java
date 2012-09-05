package com.iver.cit.gvsig.project.documents.layout.fframes;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.geom.primitive.Envelope;
import org.gvsig.fmap.mapcontext.MapContext;
import org.gvsig.fmap.mapcontext.ViewPort;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.project.documents.exceptions.SaveException;
import com.iver.cit.gvsig.project.documents.layout.fframes.gui.dialogs.FFrameOverViewDialog;
import com.iver.cit.gvsig.project.documents.layout.fframes.gui.dialogs.IFFrameDialog;
import com.iver.cit.gvsig.project.documents.layout.gui.Layout;
import com.iver.cit.gvsig.project.documents.view.ProjectView;
import com.iver.utiles.XMLEntity;


/**
 * FFrame para introducir el localizador de una vista en el Layout.
 *
 * @author Vicente Caballero Navarro
 */
public class FFrameOverView extends FFrameView implements IFFrameViewDependence{
    private MapContext assoc_map;
    private Envelope extent;
	private FFrameView fframeview;
	private int dependenceIndex = -1;
    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     * @param at DOCUMENT ME!
     * @param rv DOCUMENT ME!
     * @param imgBase DOCUMENT ME!
     */
	 public void draw(Graphics2D g, AffineTransform at, Rectangle2D rv,
			BufferedImage imgBase) {
		 try {
			if (getMapContext()!=null && getMapContext().getFullEnvelope()!=null) {
				getMapContext().getViewPort()
					.setEnvelope(getMapContext().getFullEnvelope());
			}
		} catch (ReadException e) {
			e.printStackTrace();
		}
		refresh();
		super.draw(g, at, rv, imgBase);
		Rectangle2D r = getBoundingBox(at);

		if ((assoc_map != null)
				&& (assoc_map.getViewPort().getExtent() != null)
				&& (getMapContext().getViewPort().getExtent() != null)) {

			ViewPort vp = getMapContext().getViewPort();
			ViewPort vpOrig = assoc_map.getViewPort();

			if (extent == null) {
				extent = vpOrig.getAdjustedExtent();
			}
			vp.refreshExtent();
			Rectangle2D aux=new Rectangle2D.Double(extent.getMinimum(0),extent.getMinimum(1),extent.getLength(0),extent.getLength(1));
			Rectangle2D extentPixels = vp.fromMapRectangle(aux);

			extentPixels.setFrame(extentPixels.getX(), extentPixels.getY()
					, extentPixels.getWidth(),
					extentPixels.getHeight());
			Rectangle2D clip = g.getClipBounds();

			g.setClip((int) r.getX(), (int) r.getY(), (int) r.getWidth(),
					(int) r.getHeight());
			g.setColor(Color.red);
			g.draw(extentPixels);
			// System.err.println("extentPixels =
			// "+extentPixels.getX()+","+extentPixels.getY()+",
			// "+extentPixels.getWidth()+","+extentPixels.getHeight());
			g.setColor(new Color(100, 100, 100, 100));
			g.fill(extentPixels);
			// dibujamos las líneas vertical y horizontal
			double pRightUp = (int) (r.getWidth() + r.getX());

			Line2D.Double linVert = new Line2D.Double(
					extentPixels.getCenterX(), r.getY(), extentPixels
							.getCenterX(), r.getMaxY());
			Line2D.Double linHoriz = new Line2D.Double(r.getX(), extentPixels
					.getCenterY(), pRightUp, extentPixels.getCenterY());

			g.setColor(Color.darkGray);
			g.draw(linVert);
			g.draw(linHoriz);

			g.setClip((int) clip.getX(), (int) clip.getY(), (int) clip
					.getWidth(), (int) clip.getHeight());
			extent = null;
			// System.err.println("Dibujando FFrameOverView ...");
		}

	}
    /**
	 * DOCUMENT ME!
	 *
	 * @param g
	 *            DOCUMENT ME!
	 * @param at
	 *            DOCUMENT ME!
	 * @param rv
	 *            DOCUMENT ME!
	 * @param imgBase
	 *            DOCUMENT ME!
	 *
	 * @throws ReadDriverException
	 */
    public void print(Graphics2D g, AffineTransform at)
    throws ReadException {
    	draw(g, at, null, null);
//    	Rectangle2D rect=getBoundingBox(at);
//    	Point2D mp1 = FLayoutFunctions.toMapPoint(new Point2D.Double(rect.getX(),rect.getY()), getATMap());
//		Point2D mp2 = FLayoutFunctions.toMapPoint(new Point2D.Double(rect.getMaxX(),rect.getMaxY()), getATMap());

//    	 ViewPort vpOrig = assoc_map.getViewPort();
//
//         if (extent == null) {
//             extent = vpOrig.getAdjustedExtent();
//         }
//         g.setColor(Color.blue);
//         g.draw(extent);
//        Rectangle2D r = getBoundingBox(at);
//
//        if ((assoc_map != null) &&
//                (assoc_map.getViewPort().getExtent() != null) &&
//                (getMapContext().getViewPort().getExtent() != null)) {
//            ViewPort vp = getMapContext().getViewPort();
//            Rectangle2D extentView = vp.getAdjustedExtent();
//            ViewPort vpOrig = assoc_map.getViewPort();
//
//            if (extent == null) {
//                extent = vpOrig.getAdjustedExtent();
//            }
//            Rectangle2D clip=g.getClipBounds();
//            g.setClip((int)r.getX(),(int)r.getY(),(int)r.getWidth(),(int)r.getHeight());
//
//            AffineTransform affineTransform = vp.getAffineTransform();
//            Rectangle2D extentBounding=vp.toMapRectangle(r);
//            //Point2D p = vp.toMapPoint((int) r.getX(), (int) r.getY());
//            g.setTransform(affineTransform);
//            g.setColor(Color.red);
//            //Rectangle2D.intersect(extent,extentBounding,extent);
//            g.setStroke(new BasicStroke(5));
//            g.drawRect((int)extent.getX(),(int)extent.getY(),(int)extent.getWidth(),(int)extent.getHeight());
//            g.setStroke(new BasicStroke(0));
//            g.setColor(new Color(100, 100, 100, 100));
//            g.fill(extent);
//
//            // dibujamos las líneas vertical y horizontal
//            Point2D pRightUp = vp.toMapPoint((int) (r.getWidth() + r.getX()), 0);
//
//            Line2D.Double linVert = new Line2D.Double(extent.getCenterX(),
//                    extentView.getMinY(), extent.getCenterX(), extentBounding.getY());
//            Line2D.Double linHoriz = new Line2D.Double(extentBounding.getX(),
//                    extent.getCenterY(), pRightUp.getX(), extent.getCenterY());
//
//            g.setColor(Color.darkGray);
//            g.draw(linVert);
//            g.draw(linHoriz);
//            g.setTransform(new AffineTransform());
//            g.setClip((int)clip.getX(),(int)clip.getY(),(int)clip.getWidth(),(int)clip.getHeight());
//
//
//            extent = null;
//        }
    }

    /**
     * Inserta el ProjectView de donde obtener las propiedades de la vista a
     * mostrar.
     *
     * @param v Modelo de la vista.
     */
    public void setView(ProjectView v) {
        view = v;

        ViewPort vp = null;

        if (getMapContext() != null) {
            vp = getMapContext().getViewPort();
        } else {
            vp = v.getMapContext().getViewPort().cloneViewPort();
        }

        vp.setImageSize(new Dimension((int) getBoundingBox(null).width,
                (int) getBoundingBox(null).height));
        assoc_map = v.getMapContext();

        if (m_bLinked) {
            if (getTypeScale() == AUTOMATICO) {
                m_fmap = v.getMapOverViewContext().createNewFMap(v.getMapOverViewContext()
                                                                  .getViewPort()
                                                                  .cloneViewPort());
                m_fmap.getViewPort().setImageSize(new Dimension(
                        (int) getBoundingBox(null).width,
                        (int) getBoundingBox(null).height));
                v.getMapOverViewContext().getViewPort().addViewPortListener(this);
                v.getMapOverViewContext().addLayerListener(this);
            }
        } else if (!m_bLinked) {
//            try {
                if (getTypeScale() == AUTOMATICO) {
                    m_fmap = v.getMapOverViewContext().cloneFMap(); //(v.getMapContext().getViewPort().cloneViewPort());
                    m_fmap.setViewPort(v.getMapOverViewContext().getViewPort()
                                        .cloneViewPort());
                    m_fmap.getViewPort().setImageSize(new Dimension(
                            (int) getBoundingBox(null).width,
                            (int) getBoundingBox(null).height));
                    v.getMapOverViewContext().getViewPort().addViewPortListener(this);
                }
//            } catch (XMLException e1) {
//                NotificationManager.addError("Cuando se añade una vista al Layout",
//                    e1);
//            }
        }

	}

    /**
     * @see com.iver.cit.gvsig.project.documents.layout.fframes.IFFrame#getNameFFrame()
     */
    public String getNameFFrame() {
        return PluginServices.getText(this, "Localizador") + num;
    }
    public IFFrame cloneFFrame(Layout layout) {
        FFrameOverView frame = new FFrameOverView();
        frame.setLevel(this.getLevel());
        frame.setNum(this.num);
        frame.setName(this.getName());
        frame.setBoundBox(this.getBoundBox());
        frame.setTag(this.getTag());
        frame.m_Mode = this.m_Mode;
        frame.m_typeScale = this.m_typeScale;
        frame.m_extension = this.m_extension;
        frame.m_quality = this.m_quality;
        frame.m_viewing = this.m_viewing;
        frame.m_bLinked = this.m_bLinked;
        frame.m_mapUnits = this.m_mapUnits;
        frame.setRotation(this.getRotation());

        frame.m_Scale = this.m_Scale;
        frame.view=this.getView();
        frame.m_fmap = this.getMapContext();
        frame.setSelected(this.getSelected()!=IFFrame.NOSELECT);
        frame.setLayout(layout);
        frame.assoc_map=this.assoc_map;
        frame.extent=this.extent;
        frame.dependenceIndex=dependenceIndex;
        frame.fframeview=fframeview;
        frame.initDependence(layout.getLayoutContext().getAllFFrames());
        frame.setFrameLayoutFactory(factory);
        cloneActions(frame);
        return frame;
    }
	public IFFrameDialog getPropertyDialog() {
		return new FFrameOverViewDialog(getLayout(),this);
	}
	public void setFFrameDependence(IFFrame f) {
		fframeview=(FFrameView)f;

	}
	public IFFrame[] getFFrameDependence() {
		return new IFFrame[] {fframeview};
	}
	public void initDependence(IFFrame[] fframes) {
		  if ((dependenceIndex != -1) &&
	                fframes[dependenceIndex] instanceof FFrameView) {
	            fframeview = (FFrameView) fframes[dependenceIndex];
	        }
	}
	 /**
     * @see com.iver.cit.gvsig.project.documents.layout.fframes.IFFrame#getXMLEntity()
     */
    public XMLEntity getXMLEntity() throws SaveException {
        XMLEntity xml = super.getXMLEntity();
        try {
            if (fframeview != null) {
                Layout layout = fframeview.getLayout();
                IFFrame[] fframes = layout.getLayoutContext().getAllFFrames();

                for (int i = 0; i < fframes.length; i++) {
                    if (fframeview.equals(fframes[i])) {
                        xml.putProperty("index", i);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            throw new SaveException(e, this.getClass().getName());
        }
        return xml;
    }

    public void setXMLEntity(XMLEntity xml) {
    	super.setXMLEntity(xml);
        if (xml.contains("index")) {
            dependenceIndex = xml.getIntProperty("index");
        }
    }
	public void refreshDependence(IFFrame fant, IFFrame fnew) {
		if ((fframeview != null) &&
                fframeview.equals(fant)) {
            fframeview=(FFrameView)fnew;
    	}

	}

}
