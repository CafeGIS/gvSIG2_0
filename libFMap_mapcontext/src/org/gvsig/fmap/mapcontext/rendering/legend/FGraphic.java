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
package org.gvsig.fmap.mapcontext.rendering.legend;

import java.awt.Graphics2D;

import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.operation.Draw;
import org.gvsig.fmap.geom.operation.DrawOperationContext;
import org.gvsig.fmap.geom.operation.GeometryOperationException;
import org.gvsig.fmap.geom.operation.GeometryOperationNotSupportedException;
import org.gvsig.fmap.mapcontext.ViewPort;
import org.gvsig.fmap.mapcontext.rendering.symbols.ISymbol;


/**
 * <p><code>FGraphic</code> defines a graphic symbol.</p>
 *
 * <p>That symbol represents a geometry capable to be painted, with an identifier, and can, optionally,
 *  have attached a tag that can be a text or an object.</p>
 */
public class FGraphic {
	/**
	 * <p>Identifies this graphic symbol.</p>
	 *
	 * @see #getIdSymbol()
	 * @see #setIdSymbol(int)
	 */
	int idSymbol;

    /**
     * <p>Geometry of this graphic symbol.</p>
     *
     * @see #getGeom()
     * @see #setGeom(IGeometry)
     */
	Geometry geom;

    /**
     * <p>Optional text associated to this graphic symbol as a tag.</p>
     *
     * @see #getTag()
     * @see #setTag(String)
     */
	String tag;

    /**
     * <p>Optional object <i>(example: an icon)</i> associated to this graphic symbol as a tag.</p>
     *
     * @see #getObjectTag()
     * @see #setObjectTag(Object)
     */
	Object objectTag;

    /**
     * <p>Creates a <code>geom</code> type <code>FGraphic</code> instance, identified by <code>idSymbol</code>.</p>
     *
     * @param geom the geometry that this symbol will represent
     * @param idSymbol this geometry identifier
     */
	public FGraphic(Geometry geom, int idSymbol)
	{
		this.idSymbol = idSymbol;
		this.geom = geom;
	}

    /**
     * <p>Gets the text tag associated to this symbol.</p>
     *
     * @return the text tag associated to this symbol, or <code>null</code> if undefined
     *
     * @see #setTag(String)
     */
	public String getTag() {
		return tag;
	}

    /**
     * <p>Sets a text tag associated to this symbol.</p>
     *
     * @param tag a text tag associated to this symbol
     *
     * @see #getTag()
     */
	public void setTag(String tag) {
		this.tag = tag;
	}

    /**
     * <p>Gets the geometry of this symbol.</p>
     *
     * @return an object indicating this component's geometry
     *
     * @see #setGeom(IGeometry)
     */
	public Geometry getGeom() {
		return geom;
	}

    /**
     * <p>Replaces the geometry of this symbol by <code>geom</code></p>.
     *
     * @param geom an object indicating this symbol geometry
     *
     * @see #getGeom()
     */
	public void setGeom(Geometry geom) {
		this.geom = geom;
	}

    /**
     * <p>Gets the identifier of this symbol.</p>
     *
     * @return the identifier of this symbol
     *
     * @see #setIdSymbol(int)
     */
	public int getIdSymbol() {
		return idSymbol;
	}

    /**
     * <p>Replaces the identifier of this symbol by <code>idSymbol</code></p>
     *
     * @param idSymbol the new identifier
     *
     * @see #getIdSymbol()
     */
	public void setIdSymbol(int idSymbol) {
		this.idSymbol = idSymbol;
	}

	/**
     * <p>Defines the default logic of drawing a graphic symbol.</p>
     *
     * <p>Using the rendering defined in <code>g</code>, if <code>theSymbol</code> is visible,
     *  that symbol will be drawn in <code>viewPort</code>.</p>
     *
     * @param g for rendering 2-dimensional shapes, text and images on the Java(tm) platform
     * @param viewPort information for drawing this graphic item
     * @param theSymbol the symbol that will be drawn
     */
	public void draw(Graphics2D g, ViewPort viewPort, ISymbol theSymbol )
	{
		if (theSymbol.isShapeVisible())
		{
			Geometry cloneGeom = geom.cloneGeometry();
			DrawOperationContext doc=new DrawOperationContext();
			doc.setGraphics(g);
			doc.setViewPort(viewPort);
			doc.setSymbol(theSymbol);
			try {
				cloneGeom.invokeOperation(Draw.CODE,doc);
			} catch (GeometryOperationNotSupportedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (GeometryOperationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			cloneGeom.draw(g,viewPort,theSymbol, null);
		}
	}

    /**
     * <p>Gets the object tag associated to this symbol.</p>
     *
     * @return the object tag associated to this symbol, or <code>null</code> if  undefined
     *
     * @see #setObjectTag(Object)
     */
	public Object getObjectTag() {
		return objectTag;
	}

	/**
	 * <p>Sets an object tag associated to this symbol.</p>
	 *
	 * @param objectTag the object tag associated to this symbol
	 *
	 * @see #getObjectTag()
	 */
	public void setObjectTag(Object objectTag) {
		this.objectTag = objectTag;
	}
}
