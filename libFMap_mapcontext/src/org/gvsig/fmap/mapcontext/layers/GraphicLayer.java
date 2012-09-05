/*
 * Created on 19-sep-2005
 *
 * gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
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
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
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
package org.gvsig.fmap.mapcontext.layers;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import org.cresques.cts.ICoordTrans;
import org.gvsig.compat.print.PrintAttributes;
import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.primitive.Envelope;
import org.gvsig.fmap.mapcontext.ViewPort;
import org.gvsig.fmap.mapcontext.rendering.legend.FGraphic;
import org.gvsig.fmap.mapcontext.rendering.symbols.FGraphicUtilities;
import org.gvsig.fmap.mapcontext.rendering.symbols.ISymbol;
import org.gvsig.tools.task.Cancellable;

import com.vividsolutions.jts.index.ItemVisitor;
import com.vividsolutions.jts.index.SpatialIndex;
import com.vividsolutions.jts.index.quadtree.Quadtree;

/**
 * <p><code>GraphicLayer</code> represents a layer with graphical items, that are geometries with
 *  a symbol and handlers associated.</p>
 *
 * <p>The internal graphical items are independent to each other and can be selected separately. There is a <i>bit set</i> to
 * define which item are selected. They, will be drawn with handlers, that according to the particular
 * implementation of each one, can allow user to move, center, deform, ... each graphical item.</p>
 *
 * @see FLyrDefault
 */
public class GraphicLayer extends FLyrDefault {
	/**
	 * Internal list with all graphic items of this layer.
	 *
	 * @see #addGraphic(FGraphic)
	 * @see #clearAllGraphics()
	 * @see #getGraphic(int)
	 * @see #getGraphicByObjectTag(Object)
	 * @see #inserGraphics(int, Collection)
	 * @see #insertGraphic(int, FGraphic)
	 * @see #removeGraphic(FGraphic)
	 * @see #removeGraphic(int)
	 * @see #getNumGraphics()
	 */
    private ArrayList graphics = new ArrayList();

	/**
	 * Internal list with all symbols of this layer.
	 *
	 * @see #addSymbol(ISymbol)
	 * @see #clearSymbolsGraphics()
	 */
    private ArrayList symbols = new ArrayList();

	/**
	 * Describes a rectangle defined by a location (x, y) and dimension, that represents this layer position
	 *  and dimension.
	 *
	 * @see #getFullEnvelope()
	 * @see #reCalculateFullExtent()
	 */
    private Envelope fullExtent;

	/**
	 * An optimal index according a quadtree for fast access to spatial data.
	 */
    private SpatialIndex spatialIndex = new Quadtree();

	/**
	 * A set of boolean items that specifies which graphic items of this layer are selected.
	 *
	 * @see #getSelection()
	 * @see #setSelection(FBitSet)
	 */
    private FBitSet selection = new FBitSet();

	/**
	 * <p>Creates a new <code>GraphicLayer</code> instance.</p>
	 *
	 * @see FLyrDefault#FLyrDefault()
	 */
    public GraphicLayer() {
        super();
    }

    /*
     * @see com.iver.cit.gvsig.fmap.layers.FLayer#getFullExtent()
     */
    public Envelope getFullEnvelope() {
        return fullExtent;
    }

    /*
     * @see com.iver.cit.gvsig.fmap.layers.FLayer#draw(java.awt.image.BufferedImage, java.awt.Graphics2D, com.iver.cit.gvsig.fmap.ViewPort, com.iver.utiles.swing.threads.Cancellable, double)
     */
    public void draw(BufferedImage image, Graphics2D g, ViewPort viewPort, Cancellable cancel, double scale) throws ReadException {
        if (isVisible() && isWithinScale(scale)){
            drawGraphics(image, g, viewPort, cancel);
            }
    }

    /**
     * <p>Processes all graphic items of this layer according to the <i>visitor pattern</i>. This
     *  operation can be canceled at any time.</p>
     *
     * @param visitor object that allows visit each graphic item
     * @param cancel shared object that determines if this layer can continue being drawn
     */
    public void process(ItemVisitor visitor, Cancellable cancel)
    {
        int numReg;

        for (numReg = 0; numReg < graphics.size(); numReg++) {
           if(cancel != null){
	        	if (cancel.isCanceled()) {
	                break;
	            }
            }
            FGraphic theGraphic = (FGraphic) graphics.get(numReg);
            visitor.visitItem(theGraphic);
        }

    }

    public void print(Graphics2D g, ViewPort viewPort, Cancellable cancel, double scale, PrintAttributes properties) throws ReadException {
        if (isVisible() && isWithinScale(scale)){
            drawGraphics(null, g, viewPort, cancel);
            }
    }

    /**
     * <p>Draws each graphic item of this layer that it's associated symbol is also in this layer, and its
     *  geometry intersects with the extent currently covered by the view (of the {@link ViewPort ViewPort}). If
     *  the graphic item is selected, will also draw its selection handlers.</p>
     *
     * @param image buffer used sometimes instead <code>g</code> to accelerate the draw. For example, if two points are as closed that can't be distinguished, draws only one.
	 * @param g for rendering 2-dimensional shapes, text and images on the Java(tm) platform
	 * @param viewPort the information for drawing the layers
	 * @param cancel an object thread that implements the {@link Cancellable Cancellable} interface, and will allow to cancel the draw
	 *
     * @see #draw(BufferedImage, Graphics2D, ViewPort, Cancellable, double)
     * @see #print(Graphics2D, ViewPort, Cancellable, double, PrintAttributes)
     */
    private void drawGraphics(BufferedImage image, Graphics2D g,
            ViewPort viewPort, Cancellable cancel)
    {
        int numReg;
        Envelope elExtent = viewPort.getAdjustedExtent();
        if (elExtent == null) {
			return;
		}

        //int anchoMapa;
        //int altoMapa;
        //double anchoReal;
        //double altoReal;
        //double escala;
        ISymbol theSymbol = null;

        ICoordTrans ct = getCoordTrans();

        for (numReg = 0; numReg < graphics.size(); numReg++) {
            if (cancel.isCanceled()) {
                break;
            }

            FGraphic theGraphic = (FGraphic) graphics.get(numReg);
            Geometry geom = theGraphic.getGeom();

            // Modificación para Jorge, para que le reproyecte los gráficos.
			if (ct != null) {
		        geom = geom.cloneGeometry();
				geom.reProject(ct);
			}

            if (geom.fastIntersects(elExtent.getMinimum(0),
                        elExtent.getMinimum(1), elExtent.getLength(0), elExtent.getLength(1)))
             {
                theSymbol = (ISymbol) symbols.get(theGraphic.getIdSymbol());
                if (theSymbol == null) {
					continue;
				}
                if (selection.get(numReg)) // Si está seleccinado
                {
                	FGraphicUtilities.DrawHandlers(g, viewPort.getAffineTransform(), geom.getHandlers(Geometry.SELECTHANDLER), theSymbol);
                }
                else
                {
                	theGraphic.draw(g, viewPort, theSymbol);
                }
            }
        }
    }

    /**
     * <p>Adds a new symbol to the graphic layer, and, if success, returns the last index of the internal
     *  list of symbols.</p>
     *
     * @param newSymbol the new symbol
     * @return last index of the internal list of symbols if success, -1 otherwise
     *
     * @see #clearSymbolsGraphics()
     */
    public int addSymbol(ISymbol newSymbol)
    {
        if (symbols.add(newSymbol)) {
			return symbols.size()-1;
		}
        return -1;

    }

    /**
     * <p>Adds a new graphic item to the graphic layer, and, if success, sets as new extent the graphic's
     *  one if this layer hadn't, otherwise sets as new extent the union of both.</p>
     *
     * @param g the new graphic item
     *
     * @see #insertGraphic(int, FGraphic)
     * @see #inserGraphics(int, Collection)
     */
    public void addGraphic(FGraphic g)
    {
        if (graphics.add(g))
        {

//        	spatialIndex.insert(g.getGeom().getBounds2D())
            if (fullExtent == null) {
                fullExtent = g.getGeom().getEnvelope();
            } else {
                fullExtent.add(g.getGeom().getEnvelope());
            }

//            return graphics.size()-1;
        }
//        return -1;

    }

    /**
     * <p>Adds a new graphic item to the graphic layer at the position specified of the internal list, and, if success,
     *  sets as new extent the graphic's one if this layer hadn't, otherwise sets as new extent the union of both.</p>
     *
     * @param position the index of the element to insert
     * @param g the new graphic item
     *
     * @see #inserGraphics(int, Collection)
     * @see #addGraphic(FGraphic)
     */
    public void insertGraphic(int position, FGraphic g) {
    	graphics.add(position, g);
        if (fullExtent == null) {
            fullExtent = g.getGeom().getEnvelope();
        } else {
            fullExtent.add(g.getGeom().getEnvelope());
        }
    }

    /**
     * Removes all graphic items from this layer. The internal list of graphic items will be empty
     *  after this call returns, and layer won't have extent.
     *
     * @see #removeGraphic(FGraphic)
     * @see #removeGraphic(int)
     */
    public void clearAllGraphics()
    {
        graphics.clear();
        fullExtent = null;
    }

    /**
     * Removes all symbols from this layer. The internal list of symbols will be empty
     *  after this call returns.
     *
     * @see #addSymbol(ISymbol)
     */
    public void clearSymbolsGraphics()
    {
        symbols.clear();
    }

    /**
     * <p>Gets a set of boolean values that specifies which graphic items of this layer are selected.</p>
     *
     * @return a set of boolean values. Each value is equal to <code>true</code> if is selected the graphic item
     *  that has the same position in the internal list of graphic items of this layer, and to <code>false</code> if isn't selected
     *
     * @see #setSelection(FBitSet)
     */
	public FBitSet getSelection() {
		return selection;
	}

	/**
     * <p>Sets a set of boolean values that specifies which graphic items of this layer are selected.</p>
     *
     * @param selection a set of boolean values. Each value is equal to <code>true</code> if is selected the graphic item
     *  that has the same position in the internal list of graphic items of this layer, and to <code>false</code> if isn't selected
     *
     * @see #getSelection()
	 */
	public void setSelection(FBitSet selection) {
		this.selection = selection;
	}

	/**
	 * Returns a bit set that reports which graphic items <i>(geometries)</i>, intersect with
	 *  the rectangle <code>r</code>.
	 *
	 * @param r the <code>Rectangle2D</code> to be intersected with some geometries of this layer
	 * @return a <code>FBitSet</code> which <code>true</code> bits represent the geometries of this layer
	 *  that intersect with the rectangle
	 *
	 * @see IGeometry#intersects(Rectangle2D)
	 */
	public FBitSet queryByRect(Rectangle2D r) {
		FBitSet newSel = new FBitSet();
        for (int numReg = 0; numReg < graphics.size(); numReg++) {

            FGraphic theGraphic = (FGraphic) graphics.get(numReg);
            Geometry geom = theGraphic.getGeom();
            if (geom.intersects(r))
            {
            	newSel.set(numReg);
            }
        }
        return newSel;
	}

	/**
	 * Removes a graphic item and recalculates the extent of this layer.
	 *
	 * @param graphic the graphic item to be removed
	 *
	 * @see #removeGraphic(int)
	 * @see #clearAllGraphics()
	 */
	public void removeGraphic(FGraphic graphic) {
		graphics.remove(graphic);
		reCalculateFullExtent();

	}

	/**
	 * Returns the item at the specified position in the internal list of graphics.
	 *
	 * @param idGraphic index of item to return
	 * @return the item at the specified position in the internal list of graphics
	 *
	 * @see #getGraphicByObjectTag(Object)
	 * @see #getNumGraphics()
	 */
	public FGraphic getGraphic(int idGraphic) {
		return (FGraphic) graphics.get(idGraphic);
	}

	/**
	 * Returns the graphic item of this layer, that has the specified object tag.
	 *
	 * @param objectTag the tag of the item to return
	 * @return the item that has the specified object tag, or <code>null</code> if there was no item that had it
	 *
	 * @see #getGraphic(int)
	 * @see #getNumGraphics()
	 */
	public FGraphic getGraphicByObjectTag(Object objectTag) {
        for (int i = 0; i < graphics.size(); i++) {
             FGraphic theGraphic = (FGraphic) graphics.get(i);
             if (theGraphic.getObjectTag() == objectTag) {
				return theGraphic;
			}
         }
        return null;
	}

	/**
	 * Returns the number of graphic items in this layer.
	 *
	 * @return the number of graphic items in this list
	 *
	 * @see #addGraphic(FGraphic)
	 * @see #inserGraphics(int, Collection)
	 * @see #insertGraphic(int, FGraphic)
	 * @see #clearAllGraphics()
	 * @see #removeGraphic(FGraphic)
	 * @see #removeGraphic(int)
	 * @see #getGraphic(int)
	 */
	public int getNumGraphics() {
		return graphics.size();
	}

	/**
	 * Recalculates the full extent of this layer as the union of the bounds 2D
	 *  of all internal graphic items.
	 */
	private void reCalculateFullExtent(){
		if (graphics.size() > 0) {
			FGraphic g = (FGraphic) graphics.get(0);
			fullExtent = g.getGeom().getEnvelope();
			for (int i = 1; i < graphics.size(); i++) {
				g = (FGraphic) graphics.get(i);
				Envelope rAux = g.getGeom().getEnvelope();

				fullExtent.add(rAux);
			}
		}
	}

	/**
	 * <p>Inserts all of the elements in the specified {@link Collection Collection} into the internal list of graphic items,
	 *  starting at the specified position. Shifts the element currently at that position (if there was any) and any
	 *  subsequent elements to the right (increasing their indices). The new elements will appear in the list
	 *  in the order that they are returned by the specified collection's iterator.</p>
	 *
	 * @param index index where starting to insert the elements from the specified collection
	 * @param c elements to be inserted into this list
	 *
	 * @see #insertGraphic(int, FGraphic)
	 * @see #addGraphic(FGraphic)
	 */
	public void inserGraphics(int index, Collection c) {
		graphics.addAll(index, c);

	}

	/**
	 * <p>Fast remove: removes only a graphic item of this layer.</p>
	 * <p>Remember to call {@linkplain #reCalculateFullExtent()} when you finish removing graphics.</p>
	 *
	 * @param graphicIndex the graphic item to be removed
	 *
	 * @see #removeGraphic(FGraphic)
	 * @see #clearAllGraphics()
	 */
	public void removeGraphic(int graphicIndex) {
		graphics.remove(graphicIndex);
	}

	/**
	  * <p>Searches for the first symbol <code>sym</code>, testing for equality using the equals method.</p>
	  *
	  * @param sym a symbol
	  * @return the index of the first occurrence of <code>sym</code>; returns -1 if the object is not found.
	  */
	public int getSymbol(ISymbol sym) {
		if (symbols.contains(sym)) {
			return symbols.indexOf(sym);
		}
		return -1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.gvsig.metadata.Metadata#getMetadataChildren()
	 */
	public Set getMetadataChildren() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.gvsig.metadata.Metadata#getMetadataID()
	 */
	public Object getMetadataID() {
		if (this.getName() != null) {
			return "GraphicLayer(" + this.getName() + ")";
		} else {
			return "GraphicLayer";
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.gvsig.metadata.Metadata#getMetadataName()
	 */
	public String getMetadataName() {
		if (this.getName() != null) {
			return "Graphic Layer '" + this.getName() + "'";
		} else {
			return "Graphic Layer";
		}
	}
}
