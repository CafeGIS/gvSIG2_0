/* gvSIG. Sistema de Informaci�n Geogr�fica de la Generalitat Valenciana
 *
 * Copyright (C) 2004 IVER T.I. and Generalitat Valenciana.
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
 *   Av. Blasco Ib��ez, 50
 *   46010 VALENCIA
 *   SPAIN
 *
 *      +34 963862235
 *   gvsig@gva.es
 *      www.gvsig.gva.es
 *
 *    or
 *
 *   IVER T.I. S.A
 *   Salamanca 50
 *   46005 Valencia
 *   Spain
 *
 *   +34 963163400
 *   dac@iver.es
 */
/* CVS MESSAGES:
 *
 * $Id: FFrameTable.java 28370 2009-05-04 16:14:34Z vcaballero $
 * $Log$
 * Revision 1.5  2007-03-08 11:33:01  caballero
 * Exceptions
 *
 * Revision 1.4  2007/03/06 16:36:19  caballero
 * Exceptions
 *
 * Revision 1.3  2007/01/23 13:10:17  caballero
 * valor no num�rico
 *
 * Revision 1.2  2007/01/03 12:02:54  caballero
 * Extensibilidad FFrames
 *
 * Revision 1.1  2006/12/20 14:42:06  caballero
 * Remodelado Layout
 *
 * Revision 1.6  2006/12/11 17:40:35  caballero
 * ajustar a grid en el Layout
 *
 * Revision 1.5  2006/09/15 10:41:30  caballero
 * extensibilidad de documentos
 *
 * Revision 1.4  2006/08/01 09:16:24  caballero
 * optimizar c�digo
 *
 * Revision 1.3  2006/05/26 06:51:08  caballero
 * Proyectos de la versi�n 0.6
 *
 * Revision 1.2  2006/04/18 06:43:11  caballero
 * Editar v�rtice
 *
 * Revision 1.1  2006/04/10 06:37:07  caballero
 * FFrameTable
 *
 * Revision 1.1  2006/01/12 12:32:04  caballero
 * box
 *
 *
 */
package com.iver.cit.gvsig.project.documents.layout.fframes;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import org.gvsig.fmap.dal.exception.ReadException;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.ProjectExtension;
import com.iver.cit.gvsig.project.Project;
import com.iver.cit.gvsig.project.documents.exceptions.OpenException;
import com.iver.cit.gvsig.project.documents.exceptions.SaveException;
import com.iver.cit.gvsig.project.documents.layout.fframes.gui.dialogs.FFrameBoxDialog;
import com.iver.cit.gvsig.project.documents.layout.fframes.gui.dialogs.IFFrameDialog;
import com.iver.cit.gvsig.project.documents.layout.gui.Layout;
import com.iver.utiles.XMLEntity;


/**
 * DOCUMENT ME!
 *
 * @author Vicente Caballero Navarro
 */
public class FFrameTable extends FFrameGroup implements IFFrameGroupSelectable{
    private int numColumns = 2;
    private int numRows = 2;
    private static AffineTransform identity=new AffineTransform();
    private boolean selectFFrameBasic=false;
    private static final Image iMove = PluginServices.getIconTheme().
    	get("move-icon").getImage();


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
        Rectangle2D.Double r = getBoundingBox(at);
        g.setColor(Color.black);
        g.rotate(Math.toRadians(getRotation()), r.x + (r.width / 2),
                r.y + (r.height / 2));
        drawRectangles(g,at,rv,imgBase);
        g.rotate(Math.toRadians(-getRotation()), r.x + (r.width / 2),
                r.y + (r.height / 2));
        super.draw(g,at,rv,imgBase);
    }

    private void drawRectangles(Graphics2D g, AffineTransform at,Rectangle2D rv,BufferedImage imgBase) {
        IFFrame[] fframes=getFFrames();
        for (int i =0;i<fframes.length;i++){
            FFrameBasic basic=(FFrameBasic)fframes[i];
            basic.draw(g,at,rv,imgBase);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param r DOCUMENT ME!
     * @param g DOCUMENT ME!
     */
    public void drawBox(Rectangle2D r, Graphics2D g) {
        calculateTable(r);
        IFFrame[] fframes=getFFrames();
        for (int i =0;i<fframes.length;i++){
            FFrameBasic basic=(FFrameBasic)fframes[i];
            basic.draw(g,identity,null,null);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     * @param at DOCUMENT ME!
     *
     * @throws ReadDriverException
     */
    public void print(Graphics2D g, AffineTransform at)
        throws ReadException {
        draw(g, at, null, null);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws SaveException DOCUMENT ME!
     */
    public XMLEntity getXMLEntity() throws SaveException {
        XMLEntity xml = super.getXMLEntity();
        xml.putProperty("numColumns",numColumns);
        xml.putProperty("numRows",numRows);
        return xml;
    }

    /**
     * DOCUMENT ME!
     *
     * @param xml DOCUMENT ME!
     */
    public void setXMLEntity(XMLEntity xml) {
        super.setXMLEntity(xml);
        numColumns=xml.getIntProperty("numColumns");
        numRows=xml.getIntProperty("numRows");
    }

    /**
     * DOCUMENT ME!
     *
     * @param xml DOCUMENT ME!
     * @param l DOCUMENT ME!
     */
    public void setXMLEntity03(XMLEntity xml, Layout l) {
        // TODO Auto-generated method stub
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getNameFFrame() {
        return PluginServices.getText(this, "box") + num;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getNumColumns() {
        return numColumns;
    }

    /**
     * DOCUMENT ME!
     *
     * @param numColumns DOCUMENT ME!
     */
    public void setNumColumns(int numColumns) {
        this.numColumns = numColumns;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getNumRows() {
        return numRows;
    }

    /**
     * DOCUMENT ME!
     *
     * @param numRows DOCUMENT ME!
     */
    public void setNumRows(int numRows) {
        this.numRows = numRows;
    }

    public void calculateTable(Rectangle2D r) {
            double wC = r.getWidth() / numColumns;
            double hR = r.getHeight() / numRows;
            int rows = numRows;
            clearFFrames();
            for (int i = 0; i < numColumns; i++) {
                 for (int j = 0; j < rows; j++) {
                     double x=r.getX() + (wC * i);
                     double y=r.getY() + (hR * j);
                     double w=wC;
                     double h=hR;
                     Rectangle2D rBasic=new Rectangle2D.Double(x,y,w,h);
                     FFrameBasic basic=(FFrameBasic)FrameFactory.createFrameFromName(FFrameBasicFactory.registerName);
                     basic.setRectangle(rBasic);
                     basic.setLayout(getLayout());
                     addFFrame(basic);
                 }
            }
    }

    public void selectFFrame(boolean b) {
        selectFFrameBasic=b;
    }

    public IFFrame joinFFrame() {
    	IFFrame[] fframes=this.getFFrames();
    	Rectangle2D r=null;

    	for (int i=fframes.length-1;i>=0;i--){
    		if (fframes[i].getSelected()!=IFFrame.NOSELECT){
    			if (r==null){
    				r=(Rectangle2D)fframes[i].getBoundBox().clone();
    			}else{
    				r.add(fframes[i].getBoundBox());
    			}
    			this.removeFFrame(i);
    		}

    		//removeFFrame(fframes[i]);
    	}
    	if (r!=null){
    		//Layout layout=(Layout)PluginServices.getMDIManager().getActiveView();
    		FFrameBasic basic=(FFrameBasic)FrameFactory.createFrameFromName(FFrameBasicFactory.registerName);
            basic.setRectangle(r);
    		basic.setLayout(getLayout());
    		this.addFFrame(basic);
    		return this;
    	}

    	return null;
    }

/*
    public int getSelected() {
//		if (!selectFFrameBasic)
            return super.getSelected();
//		IFFrame[] fframes=getFFrames();
//		for (int i = 0;i<fframes.length;i++){
//			int selection=fframes[i].getSelected();
//			if (selection>0){
//				return selection;
//			}
//		}
//		return 0;

    }
*/
    public void setSelected(Point2D p,MouseEvent e) {
        if (!selectFFrameBasic) {
			super.setSelected(p,e);
		} else{
            setSelectedGroup(p,e);
        }
    }

    public boolean contains(Point2D p) {
        if (!selectFFrameBasic) {
			return super.contains(p);
		}
        return contains(p);
    }

    public void drawHandlers(Graphics2D g) {
        if (!selectFFrameBasic) {
			super.drawHandlers(g);
		} else{
            drawHandlersGroup(g);

        }
    }

    public int getContains(Point2D p) {
        if (!selectFFrameBasic) {
			return super.getContains(p);
		}
        return getContainsGroup(p);
    }

    public Rectangle2D getMovieRect(int difx, int dify) {
        //TODO Esto de momento lo dejo que no se pueda mover
    	//cuando se tiene seleccionada la herramienta de selecci�n de un FFrameBasic.
    	Rectangle2D r= super.getMovieRect(difx,dify);
        if (!selectFFrameBasic) {
			return r;
		}
        r= super.getMovieRect(0,0);
        return r;
    }

    public Rectangle2D getMovieRectGroup(int difX, int difY) {
        Rectangle2D r=null;
        IFFrame[] fframes = getFFrames();
        ArrayList selected=new ArrayList();
        for (int i = 0; i < fframes.length; i++) {
            IFFrame fframe = fframes[i];
            if (fframe.getSelected()!=IFFrame.NOSELECT){
                selected.add(fframe);
            }
        }

        for (int i = 0; i < selected.size(); i++) {

            if (i==0){
                r=((IFFrame)selected.get(i)).getMovieRect(difX,difY);
            } else {
				r.add(((IFFrame)selected.get(i)).getMovieRect(difX,difY));
			}
        }
        return r;
    }

    public int getContainsGroup(Point2D p) {
        ArrayList selected=new ArrayList();
        IFFrame[] fframes = getFFrames();
        for (int i = 0; i < fframes.length; i++) {
            IFFrame fframe = fframes[i];
            int contains=fframe.getContains(p);
            if (contains != IFFrame.NOSELECT) {
                if (contains == IFFrame.RECT){
                    selected.add(fframe);
                }else{
                    return contains;
                }
            }
        }
        if (selected.size()>0){
            return ((IFFrame)selected.get(0)).getContains(p);
        }
        return 0;
    }

    /**
     * Dibuja los handlers sobre el boundingBox en el graphics que se pasa como
     * par�metro.
     *
     * @param g Graphics sobre el que dibujar.
     */
    public void drawHandlersGroup(Graphics2D g) {
        int size = 8;
        g.setColor(Color.gray);
        IFFrame[] fframes=getFFrames();
         for (int i = 0; i < fframes.length; i++) {
             IFFrame fframe = fframes[i];
             if (fframe.getSelected()!=IFFrame.NOSELECT){
            	 Rectangle2D r = fframe.getBoundingBox(null);
                 Point2D p = new Point2D.Double();
                 g.rotate(Math.toRadians(getRotation()), r.getX() + (r.getWidth() / 2),
                     r.getY() + (r.getHeight() / 2));

                 AffineTransform atRotate = new AffineTransform();
                 atRotate.rotate(Math.toRadians(getRotation()),
                     r.getX() + (r.getWidth() / 2), r.getY() + (r.getHeight() / 2));

                 g.fillRect((int) r.getX() - size/2, (int) r.getY() - size/2, size, size);
                 atRotate.transform(new Point2D.Double(r.getX() - size/2, r.getY() - size/2),
                     p);
                 no.setRect((int) p.getX(), (int) p.getY(), size, size);

                 g.fillRect((int) r.getMaxX()- size/2, (int) r.getY() - size/2, size, size);
                 atRotate.transform(new Point2D.Double(r.getMaxX()-size/2, r.getY() - size/2), p);
                 ne.setRect((int) p.getX(), (int) p.getY(), size, size);

                 g.fillRect((int) r.getX() - size/2, (int) r.getMaxY()-size/2, size, size);
                 atRotate.transform(new Point2D.Double(r.getX() - size/2, r.getMaxY()-size/2), p);
                 so.setRect((int) p.getX(), (int) p.getY(), size, size);

                 g.fillRect((int) r.getMaxX()-size/2, (int) r.getMaxY()-size/2, size, size);
                 atRotate.transform(new Point2D.Double(r.getMaxX()-size/2, r.getMaxY()-size/2), p);
                 se.setRect((int) p.getX(), (int) p.getY(), size, size);

                 g.fillRect((int) r.getCenterX() - (size / 2), (int) r.getY() - size/2,
                     size, size);
                 atRotate.transform(new Point2D.Double(r.getCenterX() - (size / 2),
                         r.getY() - size/2), p);
                 n.setRect((int) p.getX(), (int) p.getY(), size, size);

                 g.fillRect((int) r.getCenterX() - (size / 2), (int) r.getMaxY()-size/2, size,
                     size);
                 atRotate.transform(new Point2D.Double(r.getCenterX() - (size / 2),
                         r.getMaxY()-size/2), p);
                 s.setRect((int) p.getX(), (int) p.getY(), size, size);

                 g.fillRect((int) r.getX() - size/2, (int) r.getCenterY() - (size / 2),
                     size, size);
                 atRotate.transform(new Point2D.Double(r.getX() - size/2,
                         r.getCenterY() - (size / 2)), p);
                 o.setRect((int) p.getX(), (int) p.getY(), size, size);

                 g.fillRect((int) r.getMaxX()-size/2, (int) r.getCenterY() - (size / 2), size,
                     size);
                 atRotate.transform(new Point2D.Double(r.getMaxX()-size/2,
                         r.getCenterY() - (size / 2)), p);
                 e.setRect((int) p.getX()-size/2, (int) p.getY()-size/2, size, size);
                 g.rotate(Math.toRadians(-getRotation()), r.getX() + (r.getWidth() / 2),
                     r.getY() + (r.getHeight() / 2));
             }
        }

    }

  /*  public void drawHandlersGroup(Graphics2D g) {
        g.setColor(Color.gray);
        IFFrame[] fframes=getFFrames();
         for (int i = 0; i < fframes.length; i++) {
             IFFrame fframe = fframes[i];
             if (fframe.getSelected()!=IFFrame.NOSELECT){
                 fframe.drawHandlers(g);
             }
        }
    }
*/
    public boolean containsGroup(Point2D p) {
        IFFrame[] fframes=getFFrames();
         for (int i = 0; i < fframes.length; i++) {
            IFFrame fframe = fframes[i];
            if (fframe.contains(p)){
                return true;
            }
       }
     return false;
    }

    public void setSelectedGroup(Point2D p, MouseEvent e) {
        IFFrame[] fframes = getFFrames();
        if (!e.isShiftDown()) {
            for (int j = 0; j < fframes.length; j++) {
                fframes[j].setSelected(false);
            }
            for (int i = 0; i < fframes.length; i++) {
                IFFrame fframe = fframes[i];
                //if ((fframe.getSelected() == IFFrame.NOSELECT)) {
                    fframe.setSelected(p, e);
                //}
            }
        }else{
        	 for (int i = 0; i < fframes.length; i++) {
                 IFFrame fframe = fframes[i];
                 if (fframe.contains(p)) {
					if ((fframe.getSelected() == IFFrame.NOSELECT)) {
						fframe.setSelected(p, e);
					} else {
						fframe.setSelected(false);
					}
				}
             }
        }
    }

    public int getSelectedGroup() {
        return 0;
    }

	public Image getMapCursor(Point2D p) {
		if (!selectFFrameBasic) {
			return super.getMapCursor(p);
		}
       	return getMapCursorGroup(p);
    }
	public Image getMapCursorGroup(Point2D p){
		 int select = getContains(p);
	        switch (select) {
	            case (RECT):
	                return iMove;
	        }
	        return null;
	}

	public IFFrameDialog getPropertyDialog() {
		return new FFrameBoxDialog(getLayout(),this);
	}
	public IFFrame cloneFFrame(Layout layout) {
        Project p = ((ProjectExtension) PluginServices.getExtension(ProjectExtension.class)).getProject();
        IFFrame frame = null;

        try {
            frame = createFromXML(this.getXMLEntity(), p,layout);
        } catch (OpenException e) {
            e.showError();
        } catch (SaveException e) {
            e.showError();
        }
        frame.setLayout(layout);

        if (frame instanceof IFFrameViewDependence) {
            ((IFFrameViewDependence) frame).initDependence(layout.getLayoutContext().getAllFFrames());
        }
        frame.setFrameLayoutFactory(factory);
        cloneActions(frame);
        return frame;
    }
}
