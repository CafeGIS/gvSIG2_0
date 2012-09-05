package org.gvsig.fmap.mapcontext.rendering.symbols;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.TextHitInfo;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.gvsig.fmap.geom.utils.FLabel;
import org.gvsig.fmap.mapcontext.ViewPort;



/**
 * DOCUMENT ME!
 *
 * @author Vicente Caballero Navarro
 */
public class FTextLayout {
    private java.awt.font.TextLayout tl;
    private String s;
	private boolean isSelected=false;

    /**
     * Crea un nuevo TextLayout.
     *
     * @param s DOCUMENT ME!
     * @param f DOCUMENT ME!
     * @param frc DOCUMENT ME!
     */
    public FTextLayout(String s, Font f, FontRenderContext frc) {
        this.s = s;
        tl = new java.awt.font.TextLayout(s, f, frc);

    }

    /**
     * Construcción del rectángulo
     *
     * @param p
     * @param g DOCUMENT ME!
     * @param justification DOCUMENT ME!
     * @param vp DOCUMENT ME!
     *
     * @return
     */
    public Rectangle2D getBoundBox(Point2D p, Graphics2D g,
        int justification, ViewPort vp) {
        Rectangle2D bound = tl.getBounds();
        Rectangle2D bounding = vp.toMapRectangle(bound);

        FontMetrics metrics = g.getFontMetrics();

        int w = metrics.stringWidth(s);
        double width = vp.toMapDistance(w);
        int h = metrics.getMaxAscent();
        double height = vp.toMapDistance(h);
        double dist = vp.toMapDistance(3);

        switch (justification) {
            case FLabel.LEFT_BOTTOM:
                bounding=justification(p, bounding, 0, -dist);

                break;

            case FLabel.LEFT_CENTER:
            	 bounding=justification(p, bounding, 0, -(height / 2));

                break;

            case FLabel.LEFT_TOP:
            	 bounding=justification(p, bounding, 0, -height);

                break;

            case FLabel.CENTER_BOTTOM:
            	 bounding=justification(p, bounding, -(width / 2), -dist);

                break;

            case FLabel.CENTER_CENTER:
            	 bounding=justification(p, bounding, -(width / 2), -(height / 2));

                break;

            case FLabel.CENTER_TOP:
            	 bounding=justification(p, bounding, -(width / 2), -height);

                break;

            case FLabel.RIGHT_BOTTOM:
            	 bounding=justification(p, bounding, -width, -dist);

                break;

            case FLabel.RIGHT_CENTER:
            	 bounding=justification(p, bounding, -width, -(height / 2));

                break;

            case FLabel.RIGHT_TOP:
            	 bounding=justification(p, bounding, -width, -height);

                break;
        }

        return bounding;
        /*
    	Point2D pOrigen=vp.fromMapPoint(p);
    	return vp.toMapRectangle(getBoundingBox(pOrigen,justification,g));
    */
    }

    /**
     * DOCUMENT ME!
     *
     * @param p DOCUMENT ME!
     * @param r DOCUMENT ME!
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     */
    private Rectangle2D justification(Point2D p, Rectangle2D r, double x, double y) {
        r.setRect(p.getX() + x, p.getY() - y, r.getWidth(), r.getHeight());
        return r;
    }

    /**
     * DOCUMENT ME!
     *
     * @param g2 DOCUMENT ME!
     * @param justification DOCUMENT ME!
     */
    public void draw(Graphics2D g2, int justification) {
        FontMetrics metrics = g2.getFontMetrics();
        float width = metrics.stringWidth(s);
        float height = metrics.getMaxAscent();

        switch (justification) {
        /*
            case FLabel.LEFT_BOTTOM:
                tl.draw(g2, 0, -3);

                break;

            case FLabel.LEFT_CENTER:
                tl.draw(g2, 0, -(height / 2));

                break;

            case FLabel.LEFT_TOP:
                tl.draw(g2, 0, -height);

                break;

            case FLabel.CENTER_BOTTOM:
                tl.draw(g2, -(width / 2), -3);

                break;

            case FLabel.CENTER_CENTER:
                tl.draw(g2, -(width / 2), -(height / 2));

                break;

            case FLabel.CENTER_TOP:
                tl.draw(g2, -(width / 2), -height);

                break;

            case FLabel.RIGHT_BOTTOM:
                tl.draw(g2, -width, -3);

                break;

            case FLabel.RIGHT_CENTER:
                tl.draw(g2, -width, -(height / 2));

                break;

            case FLabel.RIGHT_TOP:
                tl.draw(g2, -width, -height);

                break;
                */
            case FLabel.LEFT_BOTTOM:
				g2.drawString(s, 0, 0 - 3);

				break;

			case FLabel.LEFT_CENTER:
				g2.drawString(s, 0, 0 - (height / 2));

				break;

			case FLabel.LEFT_TOP:
				g2.drawString(s, 0, 0 - height);

				break;

			case FLabel.CENTER_BOTTOM:
				g2.drawString(s, 0 - (width / 2), 0 - 3);

				break;

			case FLabel.CENTER_CENTER:
				g2.drawString(s, 0 - (width / 2),
					0 - (height / 2));

				break;

			case FLabel.CENTER_TOP:
				g2.drawString(s, 0 - (width / 2), 0 -
					height);

				break;

			case FLabel.RIGHT_BOTTOM:
				g2.drawString(s, 0 - width, 0 - 3);

				break;

			case FLabel.RIGHT_CENTER:
				g2.drawString(s, 0 - width, 0 -
					(height / 2));

				break;

			case FLabel.RIGHT_TOP:
				g2.drawString(s, 0 - width, 0 - height);

				break;

        }
    }

	public TextHitInfo getNextRightHit(int insertionIndex) {
		return tl.getNextRightHit(insertionIndex);
	}

	public TextHitInfo getNextLeftHit(int insertionIndex) {
		return tl.getNextLeftHit(insertionIndex);
	}

	public void setSelected(boolean b) {
		isSelected=b;
	}

	public Rectangle2D getBoundingBox(Point2D p1,int justification,Graphics2D g,ViewPort vp){
		Rectangle2D bounding=tl.getBounds();

	    FontMetrics metrics = g.getFontMetrics();
	    double dist = -12;
	    int width = metrics.stringWidth(s);
	    int height = metrics.getMaxAscent();
	    //Point2D p=vp.fromMapPoint(p1);
	    Point p=new Point(0,0);
	    switch (justification) {
        case FLabel.LEFT_BOTTOM:
        	bounding =justification(p, bounding, 0, -dist);
        	break;

        case FLabel.LEFT_CENTER:
        	bounding=justification(p, bounding, 0, -(height / 2));

            break;

        case FLabel.LEFT_TOP:
            bounding=justification(p, bounding, 0, -height);

            break;

        case FLabel.CENTER_BOTTOM:
            bounding=justification(p, bounding, -(width / 2), -dist);

            break;

        case FLabel.CENTER_CENTER:
            bounding=justification(p, bounding, -(width / 2), -(height / 2));

            break;

        case FLabel.CENTER_TOP:
            bounding=justification(p, bounding, -(width / 2), -height);

            break;

        case FLabel.RIGHT_BOTTOM:
            bounding=justification(p, bounding, -width, -dist);

            break;

        case FLabel.RIGHT_CENTER:
            bounding=justification(p, bounding, -width, -(height / 2));

            break;

        case FLabel.RIGHT_TOP:
            bounding=justification(p, bounding, -width, -height);

            break;
    }
	   /* Rectangle2D rtl=tl.getBounds();
	    Rectangle2D r=new Rectangle2D.Double(rtl.getX(),rtl.getY()-rtl.getHeight()/2,rtl.getWidth(),rtl.getHeight());
		*/
		return bounding;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void addChar(char keyChar) {
		s.concat(String.valueOf(keyChar));

	}

	public Shape[] getCaretShapes(int insertionIndex) {
		return tl.getCaretShapes(insertionIndex);
	}

}
