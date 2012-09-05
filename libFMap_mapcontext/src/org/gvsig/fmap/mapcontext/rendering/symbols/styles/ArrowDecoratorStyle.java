/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2005 IVER T.I. and Generalitat Valenciana.
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
package org.gvsig.fmap.mapcontext.rendering.symbols.styles;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import org.apache.batik.ext.awt.geom.PathLength;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.fmap.geom.Geometry.SUBTYPES;
import org.gvsig.fmap.geom.exception.CreateGeometryException;
import org.gvsig.fmap.geom.primitive.Point;
import org.gvsig.fmap.geom.util.UtilFunctions;
import org.gvsig.fmap.mapcontext.rendering.symbols.ArrowMarkerSymbol;
import org.gvsig.fmap.mapcontext.rendering.symbols.ILineSymbol;
import org.gvsig.fmap.mapcontext.rendering.symbols.IMarkerSymbol;
import org.gvsig.fmap.mapcontext.rendering.symbols.ISymbol;
import org.gvsig.fmap.mapcontext.rendering.symbols.SymbologyFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iver.utiles.XMLEntity;
import com.iver.utiles.XMLException;

/**
 * Class ArrowDecoratorStyle. It is used to store the information about the
 * different options to draw an arrow in a line (and draw it too). This
 * information is taken from the panel.
 *
 * @author jaume dominguez faus - jaume.dominguez@iver.es
 */
public class ArrowDecoratorStyle implements IStyle {
	private static final GeometryManager geomManager = GeometryLocator.getGeometryManager();
	private static final Logger logger = LoggerFactory.getLogger(ArrowDecoratorStyle.class);
	private boolean flipAll = false;
	private boolean flipFirst = false;
	private int arrowMarkerCount = 2;
	private boolean followLineAngle = true;
	private IMarkerSymbol marker = new ArrowMarkerSymbol();
	private String desc;

	{
		marker.setSize(10);
		((ArrowMarkerSymbol) marker).setSharpness(30);
	}

	/**
	 * Obtains the number of arrows that the user wants to draw in the same line.
	 * @return
	 */
	public int getArrowMarkerCount() {
		return arrowMarkerCount;
	}

	/**
	 * Defines the number of arrows that the user wants to draw in the same line.
	 * @return
	 */
	public void setArrowMarkerCount(int arrowMarkerCount) {
		this.arrowMarkerCount = arrowMarkerCount;
	}

	/**
	 * Defines the flipAll attribute.If the value of this attribute is true all the
	 * arrows that we had drawn in the same line will be flipped.
	 * @return
	 */
	public boolean isFlipAll() {
		return flipAll;
	}

	/**
	 * Obtains the flipAll attribute.If the value of this attribute is true all the
	 * arrows that we had drawn in the same line will be flipped.
	 * @return
	 */
	public void setFlipAll(boolean flipAll) {
		this.flipAll = flipAll;
	}

	/**
	 * Obtains the flipFirst attribute.If it is true only the first arrow of the line
	 * will be flipped.The rest will keep the same orientation.
	 * @return
	 */
	public boolean isFlipFirst() {
		return flipFirst;
	}

	/**
	 * Sets the flipFirst attribute.If it is true only the first arrow of the line
	 * will be flipped.The rest will keep the same orientation.
	 * @return
	 */
	public void setFlipFirst(boolean flipFirst) {
		this.flipFirst = flipFirst;
	}

	/**
	 * Gets the followLineAngle attribute.This attribute allows the arrow that we are
	 * going to draw to be more or less aligned with the line where it will be included (depending on the angle) .
	 * @return
	 */
	public boolean isFollowLineAngle() {
		return followLineAngle;
	}

	/**
	 * Sets the followLineAngle attribute.This attribute allows the arrow that we are
	 * going to draw to be more or less aligned with the line where it will be included.
	 * (depending on the angle).
	 * @param followingLineAngle
	 * @return
	 */
	public void setFollowLineAngle(boolean followLineAngle) {
		this.followLineAngle = followLineAngle;
	}
	/**
	 * Draws an arrow(or other symbol that substitutes an arrow selected by the user)
	 * in a line.When the line is drawn, the symbol is added and takes care of the different
	 * options of the user(for example if he wants to flip the first symbol or all and
	 * the number of symbols per line to be drawn)
	 * @param g
	 * @param affineTransform
	 * @param shp
	 * @throws CreateGeometryException 
	 */
	public void draw(Graphics2D g, AffineTransform affineTransform, Geometry geom) throws CreateGeometryException {
		if (arrowMarkerCount <= 0) return;

		PathLength pl = new PathLength(geom);
		float size = (float) marker.getSize();
		float myLineLength = pl.lengthOfPath()-size; // length without the first and last arrow
		float step = arrowMarkerCount>2 ? myLineLength/(arrowMarkerCount-1) : pl.lengthOfPath();
		float rotation1; // rotation at the arrow's vertex
		float rotation2; // rotation at the arrow's back;

		Point startP;

		// the first arrow at the end of the line
		{
			float theLength = pl.lengthOfPath();

			if ((flipFirst || flipAll) && (flipFirst != flipAll)) { // logical XOR
				Point2D p = pl.pointAtLength(theLength-size);
				startP = geomManager.createPoint(p.getX(), p.getY(), SUBTYPES.GEOM2D);
				rotation1 = pl.angleAtLength(theLength-size);
				rotation2 = pl.angleAtLength(theLength);
			} else {
				Point2D p = pl.pointAtLength(theLength);
				startP = geomManager.createPoint(p.getX(), p.getY(), SUBTYPES.GEOM2D);
				rotation1 = pl.angleAtLength(theLength-size)+(float) Math.PI;
				rotation2 = pl.angleAtLength(theLength)+(float) Math.PI;

			}

			if (rotation1 == rotation2)	{
				marker.setRotation(rotation1);
				marker.draw(g, affineTransform, startP, null);
			}
		}
		// the other arrows but the first and the last
		float aLength;
		for (int i = 1; i < arrowMarkerCount-1; i++) {
			aLength = (float) (step*i);

			rotation1 = (float) pl.angleAtLength(aLength);
			rotation2 = (float) pl.angleAtLength((float)(aLength+size));

			if (flipAll) {
				Point2D p = pl.pointAtLength(aLength);
				startP = geomManager.createPoint(p.getX(), p.getY(), SUBTYPES.GEOM2D);

			} else {
				Point2D p = pl.pointAtLength(aLength+size);
				startP = geomManager.createPoint(p.getX(), p.getY(), SUBTYPES.GEOM2D);
				rotation1 += Math.PI;
				rotation2 += Math.PI;
			}
			/*
			 *  the following are just visualization improvements,
			 *  being rigurous it just be only these two lines
			 *
			 *  marker.setRotation(rotation1);
			 *  marker.draw(g, affineTransform, startP);
			 *
			 *  but it produces ugly results at the line edges
			 */
			if (rotation1 == rotation2)	{
				marker.setRotation(rotation1);
				marker.draw(g, affineTransform, startP, null);
			} else {
				rotation1 = (float) pl.angleAtLength(aLength+1);
				rotation2 = (float) pl.angleAtLength((float)(aLength+size+1));
				if (flipAll) {
					Point2D p = pl.pointAtLength(aLength+1);
					startP = geomManager.createPoint(p.getX(), p.getY(), SUBTYPES.GEOM2D);

				} else {
					Point2D p = pl.pointAtLength(aLength+size+1);
					startP = geomManager.createPoint(p.getX(), p.getY(), SUBTYPES.GEOM2D);
					rotation1 += Math.PI;
					rotation2 += Math.PI;
				}

				if (rotation1 == rotation2)	{
					marker.setRotation(rotation1);
					marker.draw(g, affineTransform, startP, null);
				}
			}

		}

		// and the last arrow at the begining of the line
		if (arrowMarkerCount>1) {
			rotation1 = (float) pl.angleAtLength(size);
			rotation2 = (float) pl.angleAtLength(0);

			if (flipAll) {
				Point2D p = pl.pointAtLength(0);
				startP = geomManager.createPoint(p.getX(), p.getY(), SUBTYPES.GEOM2D);
				
			} else {
				Point2D p = pl.pointAtLength(size);
				startP = geomManager.createPoint(p.getX(), p.getY(), SUBTYPES.GEOM2D);
				rotation1 += Math.PI;
				rotation2 += Math.PI;
			}

			if (rotation1 == rotation2)	{
				marker.setRotation(rotation1);
				marker.draw(g, affineTransform, startP, null);
			}
		}
	}

	public void drawInsideRectangle(Graphics2D g, Rectangle r) {
		// TODO Auto-generated method stub
		throw new Error("Not yet implemented!");
	}

	public void drawOutline(Graphics2D g, Rectangle r) {
		// TODO Auto-generated method stub
		throw new Error("Not yet implemented!");
	}

	public String getDescription() {
		return desc;
	}

	public boolean isSuitableFor(ISymbol symbol) {
		return symbol instanceof ILineSymbol;
	}

	public void setDescription(String desc) {
		this.desc = desc;
	}

	public String getClassName() {
		return getClass().getName();
	}

	public XMLEntity getXMLEntity() throws XMLException {
		XMLEntity xml = new XMLEntity();
		xml.putProperty("className", getClassName());
		xml.putProperty("flipAll", flipAll);
		xml.putProperty("flipFirst", flipFirst);
		xml.putProperty("arrowMarkerCount", arrowMarkerCount);
		xml.putProperty("desc", desc);
		xml.addChild(marker.getXMLEntity());
		return xml;
	}

	public void setXMLEntity(XMLEntity xml) {
		setFlipAll(xml.getBooleanProperty("flipAll"));
		setFlipFirst(xml.getBooleanProperty("flipFirst"));
		setArrowMarkerCount(xml.getIntProperty("arrowMarkerCount"));
		setDescription(xml.getStringProperty("desc"));
		marker = (IMarkerSymbol) SymbologyFactory.
		createSymbolFromXML(
				xml.getChild(0), "ArrowDecorator marker symbol");
	}

	public IMarkerSymbol getMarker() {
		return marker;
	}

	public void setMarker(IMarkerSymbol marker) {
		this.marker = marker;
	}

}
