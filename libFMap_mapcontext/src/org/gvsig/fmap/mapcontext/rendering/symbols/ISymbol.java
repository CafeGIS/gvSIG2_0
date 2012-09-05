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
package org.gvsig.fmap.mapcontext.rendering.symbols;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;

import org.gvsig.compat.print.PrintAttributes;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.mapcontext.ViewPort;
import org.gvsig.tools.task.Cancellable;

import com.iver.utiles.IPersistence;
import com.iver.utiles.XMLEntity;
import com.iver.utiles.XMLException;

/**
 *
 * Interface for symbols.It is the most general one which is implemented by other
 * specific symbols.For this reason this interface has a method to obtain the derived
 * version of a common symbol(apart from others). The main purpose is to offer a set of
 * symbols that will be part of the FMap kernel and allow the developer to add new symbols
 * without changes in the initial implementation.
 *
 *
 * @author   jaume dominguez faus - jaume.dominguez@iver.es
 */
public interface ISymbol extends IPersistence, IPrintable {

	public static Color SELECTION_COLOR = Color.YELLOW;

	/**
	 * Returns the derived version of the symbol that will be used to draw the
	 * feature when it is selected.
	 * @return <b>ISymbol</b> applied to a feature when it has been selected.
	 */
	public ISymbol getSymbolForSelection();

	/**
	 * Used when a symbol is going to be drawn.The method to do
	 * it will depend on the derived version of the symbol.
	 * @param g
	 * @param affineTransform
	 * @param shp
	 * @param cancel TODO
	 */
	public void draw(Graphics2D g, AffineTransform affineTransform, Geometry geom, Cancellable cancel);

	/**
	 * <p>
	 * Returns <b>the distance between the shape's bounding box and the
	 * symbol-that-represents-this-shape's bounding box</b> in a two-length-float
	 * array passed as parameter.<br>
	 * </p>
	 * <p>
	 * After this method returns, the float array passed will contain two values
	 * representing <b>the amount of pixels</b> separating each of the X (first element)
	 * and Y (second element) axes.<br>
	 * </p>
	 * <p>
	 * This distance maybe dependent on:
	 * 	<ol>
	 * 		<li>
	 * 			<b>The ViewPort</b>: if the symbol is an instance of CartographicSupport
	 * 			the units it uses are not pixels and the reference system is
	 * 			CarthographicSupport.WORLD.
	 * 		</li>
	 * 		<li>
	 * 			<b>The target rendering context's dpi (dots-per-inch)</b>: if the symbol
	 * 			is an instance of CartographicSupport the units it uses are not pixels and
	 * 			CarthographicSupport.PAPER.
	 * 		</li>
	 * 	</ol>
	 * <br>
	 * </p>
	 * <p>
	 * And in any other case, if the unit of the symbol is pixels or the symbol is not
	 * even an instance of CartographicSupport, the returning values are only
	 * defined by the symbol and are not calculated.
	 * </p>
	 * @param ViewPort viewPort
	 * @param Shape shp
	 * @param int dpi
	 * @param float[] distances, the array of floats where to store the distances in x and y axis
	 */
	public void getPixExtentPlus(Geometry geom, float[] distances, ViewPort viewPort, int dpi);

	/**
	 * Returns the rgb of the symbol when it is drawn like a point.
	 *
	 * @return rgb of the symbol.
	 */
	public int getOnePointRgb();

	/**
	 * Allows the symbol to be persisted during sessions.With this method, the symbol is
	 * automatically described to be integrated as a part of the project file.Use it to
	 * specify how do you want to write the symbol in .gvp project
	 * @return
	 * @throws XMLException
	 */
	public XMLEntity getXMLEntity() throws XMLException;

	/**
	 * Called by the factory, this method will set up the symbol and after call it, the
	 * symbol must be fully functional and ready to be used.
	 */
	public void setXMLEntity(XMLEntity xml);


	/**
	 * The description is a human-readable text used to label it when show in a symbol menu or something like that.
	 * @return   description of this symbol.
	 * @uml.property  name="description"
	 */
	public String getDescription();

	/**
	 * Tells whether the shape of the symbol will be drawn or not.
	 *
	 * @return <b>true</b> if Shape must be drawn. Useful if you are labelling
	 */
	public boolean isShapeVisible();

	/**
	 * Sets the description of this symbol
	 * @param   desc, a string with the description
	 * @see   ISymbol.getDescription();
	 * @uml.property  name="description"
	 */
	public void setDescription(String desc);

	/**
	 * The use of this method -and its mechanism- is being valorated. It probably
	 * will be <b>deprecated</b>.
	 * @return FSymbol constants. I think it is better to use isSuitableFor
	 *
	 */
	public int getSymbolType();

	/**
	 * True if this symbol is ok for the geometry. For example, a FillSymbol will
	 * be suitable for a Polygon.
	 * @param geom
	 * @return
	 */
	public boolean isSuitableFor(Geometry geom);

	/**
	 * Useful to render the symbol inside the TOC, or inside little
	 * rectangles. For example, think about rendering a Label with size
	 * in meters => You will need to specify a size in pixels.
	 * Of course, you can also to choose to render a prepared image, etc.
	 * @param scaleInstance
	 * @param r
	 * @param properties TODO
	 * @param g2
	 * @throws SymbolDrawingException TODO
	 */
	public void drawInsideRectangle(Graphics2D g, AffineTransform scaleInstance, Rectangle r, PrintAttributes properties) throws SymbolDrawingException;

}


