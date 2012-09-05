/*
 * Created on 20-feb-2004
 *
 */
/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
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
 *   IVER T.I. S.A
 *   Salamanca 50
 *   46005 Valencia
 *   Spain
 *
 *   +34 963163400
 *   dac@iver.es
 */
package com.iver.cit.gvsig.project.documents.layout.fframes;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Point2D.Double;
import java.awt.image.BufferedImage;

import org.gvsig.compat.print.PrintAttributes;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.mapcontext.rendering.symbols.IFillSymbol;
import org.gvsig.fmap.mapcontext.rendering.symbols.SimpleLineSymbol;
import org.gvsig.fmap.mapcontext.rendering.symbols.SymbologyFactory;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.ProjectExtension;
import com.iver.cit.gvsig.project.Project;
import com.iver.cit.gvsig.project.documents.exceptions.OpenException;
import com.iver.cit.gvsig.project.documents.exceptions.SaveException;
import com.iver.cit.gvsig.project.documents.layout.fframes.gui.dialogs.IFFrameDialog;
import com.iver.cit.gvsig.project.documents.layout.geometryadapters.PolygonAdapter;
import com.iver.cit.gvsig.project.documents.layout.gui.Layout;
import com.iver.utiles.XMLEntity;


/**
 * FFrame básica que contiene una FFrame de cualquier tipo.
 *
 * @author Vicente Caballero Navarro
 */
public class FFrameBasic extends FFrame {

	private FFrameGraphics fframeGraphics;
	private IFFrame fframe;


	public void setRectangle(Rectangle2D r){
		FFrameGraphics graphics =(FFrameGraphics)FrameFactory.createFrameFromName(FFrameGraphicsFactory.registerName);
       graphics.setLayout(getLayout());
        PolygonAdapter pa=new PolygonAdapter();
        pa.addPoint(new Point2D.Double(r.getX(),r.getY()));
        pa.addPoint(new Point2D.Double(r.getMaxX(),r.getY()));
        pa.addPoint(new Point2D.Double(r.getMaxX(),r.getMaxY()));
        pa.addPoint(new Point2D.Double(r.getX(),r.getMaxY()));
        pa.addPoint(new Point2D.Double(r.getX(),r.getY()));
        pa.end();
        graphics.setGeometryAdapter(pa);
        graphics.setBoundBox(r);

        /*
         * jaume; moved to ISymbol
         * FSymbol symbol=new FSymbol(FShape.POLYGON,Color.black);
         * symbol.setColor(new Color(255,255,255,0));
         * symbol.setOutlineColor(Color.black);
         */

        IFillSymbol symbol= SymbologyFactory.createDefaultFillSymbol();
        symbol.setFillColor(new Color(255,255,255,0));
        SimpleLineSymbol blackOutline = new SimpleLineSymbol();
        blackOutline.setLineColor(Color.BLACK);
        symbol.setOutline(blackOutline);
        graphics.setSymbol(symbol);
        graphics.setType(Geometry.TYPES.CURVE);
        setFframeGraphics(graphics);
	}
	public FFrameBasic(){

	}
	/**
     * Método que dibuja sobre el graphics que se le pasa como parámetro, según
     * la transformada afin que se debe de aplicar y el rectángulo que se debe
     * de dibujar.
     *
     * @param g Graphics
     * @param at Transformada afin.
     * @param rv rectángulo sobre el que hacer un clip.
     * @param imgBase Imagen para acelerar el dibujado.
     */
    public void draw(Graphics2D g, AffineTransform at, Rectangle2D rv,
        BufferedImage imgBase) {
    	fframeGraphics.draw(g,at,rv,imgBase);
    	if (fframe!=null) {
			fframe.draw(g,at,rv,imgBase);
		}
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws SaveException
     *
     * @see com.iver.cit.gvsig.project.documents.layout.fframes.IFFrame#getXMLEntity()
     */
    public XMLEntity getXMLEntity() throws SaveException {
        XMLEntity xml = super.getXMLEntity();
        xml.addChild(fframeGraphics.getXMLEntity());
        if (fframe!=null) {
			xml.addChild(fframe.getXMLEntity());
		}
        return xml;
    }

    /**
     * Incorpora los atributos del XMLEntity en el objeto actual.
     *
     * @param xml XMLEntity
     * @param l Referencia al Layout.
     */
    public void setXMLEntity03(XMLEntity xml, Layout l) {
    }

    /**
     * Incorpora los atributos del XMLEntity en el objeto actual.
     *
     * @param xml XMLEntity
     */
    public void setXMLEntity(XMLEntity xml) {
    	ProjectExtension pe=(ProjectExtension)PluginServices.getExtension(ProjectExtension.class);
    	Project project=pe.getProject();
    	try {
			FFrameGraphics fframeGraphics = (FFrameGraphics)createFromXML(xml.getChild(0),project,null);
			this.setFframeGraphics(fframeGraphics);
		} catch (OpenException e1) {
			e1.printStackTrace();
		}
		if (xml.getChildrenCount()>1){
    		try {
				IFFrame fframe=FFrame.createFromXML(xml.getChild(1),project,null);
				this.setFframe(fframe);
    		} catch (OpenException e) {
				e.printStackTrace();
			}
    	}
    }

    /**
     * @see com.iver.cit.gvsig.project.documents.layout.fframes.IFFrame#getNameFFrame()
     */
    public String getNameFFrame() {
        return PluginServices.getText(this, "base") + num;
    }

    /**
     * @see com.iver.cit.gvsig.project.documents.layout.fframes.IFFrame#print(java.awt.Graphics2D,
     *      java.awt.geom.AffineTransform)
     */
    public void print(Graphics2D g, AffineTransform at, Geometry geom,
			PrintAttributes printingProperties) {
//    	fframeGraphics.setPrintingProperties(printingProperties);
    	fframeGraphics.print(g,at,geom, printingProperties);
//    	fframeGraphics.setPrintingProperties(null);
    	if (fframe!=null) {
    		fframe.print(g,at,geom, printingProperties);
    	}
    }

	public IFFrame getFframe() {
		return fframe;
	}

	public void setFframe(IFFrame fframe) {
		this.fframe = fframe;
	}

	public FFrameGraphics getFframeGraphics() {
		return fframeGraphics;
	}

	public void setFframeGraphics(FFrameGraphics fframeGraphics) {
		this.fframeGraphics = fframeGraphics;
	}

	public void drawHandlers(Graphics2D g) {
		fframeGraphics.drawHandlers(g);
	}

	public IFFrame cloneFFrame(Layout layout) {
		FFrameBasic basic=new FFrameBasic();
		basic.setFframeGraphics((FFrameGraphics)fframeGraphics.cloneFFrame(layout));
		if (fframe!=null) {
			basic.setFframe(fframe.cloneFFrame(layout));
		}
		return basic;
	}

	public boolean contains(Double p) {
		return getFframeGraphics().contains(p);
	}

	public void drawDraft(Graphics2D g) {
		super.drawDraft(g);
	}

	public void drawEmpty(Graphics2D g) {
		super.drawEmpty(g);
	}

	public void drawSymbolTag(Graphics2D g) {
		super.drawSymbolTag(g);
	}

	public java.awt.geom.Rectangle2D.Double getBoundBox() {
		return getFframeGraphics().getBoundBox();
	}

	public java.awt.geom.Rectangle2D.Double getBoundingBox(AffineTransform at) {
		return getFframeGraphics().getBoundingBox(at);
	}

	public int getContains(Double p) {
		return getFframeGraphics().getContains(p);
	}

	public int getLevel() {
		return super.getLevel();
	}

	public Rectangle2D getMovieRect(int difx, int dify) {
		return fframeGraphics.getMovieRect(difx, dify);
	}

	public String getName() {
		return super.getName();
	}

	public double getRotation() {
		return super.getRotation();
	}

	public int getSelected() {
		return fframeGraphics.getSelected();
	}

	public String getTag() {
		return super.getTag();
	}

	public boolean intersects(Rectangle2D rv, Rectangle2D r) {
		return super.intersects(rv, r);
	}

	public void openTag() {
		super.openTag();
	}

	public void setBoundBox(Rectangle2D r) {
		if (getFframeGraphics()!=null) {
			getFframeGraphics().setBoundBox(r);
		}
		if (getFframe() != null) {
			getFframe().setBoundBox(r);
		//super.setBoundBox(r);
		}
	}

	public void setLevel(int l) {
		super.setLevel(l);
	}

	public void setName(String n) {
		super.setName(n);
	}

	public void setNum(int i) {
		super.setNum(i);
	}

	public void setRotation(double rotation) {
		super.setRotation(rotation);
	}

	public void setSelected(boolean b) {
		fframeGraphics.setSelected(b);
	}

	public void setSelected(Point2D p,MouseEvent e) {
		fframeGraphics.setSelected(p,e);
	}

	public void setTag(String s) {
		super.setTag(s);
	}

	public void updateRect(Rectangle2D r, AffineTransform at) {
		getFframeGraphics().updateRect(r, at);
	}
	public void cloneActions(IFFrame frame) {
		// TODO Auto-generated method stub

	}
	public IFFrameDialog getPropertyDialog() {
		// TODO Auto-generated method stub
		return null;
	}
}
