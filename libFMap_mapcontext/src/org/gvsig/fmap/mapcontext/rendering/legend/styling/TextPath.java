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

/* CVS MESSAGES:
 *
 * $Id: TextPath.java 25636 2008-12-01 08:42:11Z vcaballero $
 * $Log$
 * Revision 1.2  2007-03-09 11:20:57  jaume
 * Advanced symbology (start committing)
 *
 * Revision 1.1.2.3  2007/02/21 07:34:09  jaume
 * labeling starts working
 *
 * Revision 1.1.2.2  2007/02/09 07:47:05  jaume
 * Isymbol moved
 *
 * Revision 1.1.2.1  2007/02/06 17:01:04  jaume
 * first version (only lines)
 *
 *
 */
package org.gvsig.fmap.mapcontext.rendering.legend.styling;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.Point2D;

import org.apache.batik.ext.awt.geom.PathLength;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.fmap.geom.Geometry.SUBTYPES;
import org.gvsig.fmap.geom.exception.CreateGeometryException;
import org.gvsig.fmap.geom.primitive.GeneralPathX;
import org.gvsig.fmap.geom.util.UtilFunctions;
import org.gvsig.fmap.mapcontext.Messages;
import org.gvsig.fmap.mapcontext.rendering.symbols.ITextSymbol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.algorithm.Angle;
/**
 * <p>Class that represents baseline of a string and allows the baseline to
 * be composed as contiguous segments with distinct slope each.<br></p>
 *
 * <p>Once a TextPath is created for a string it is possible to know where
 * the character at a determined position in the string is placed and
 * rotated.<br></p>
 *
 * @author jaume dominguez faus - jaume.dominguez@iver.es
 *
 */
public class TextPath {
	private static final GeometryManager geomManager = GeometryLocator.getGeometryManager();
	private static final Logger logger = LoggerFactory.getLogger(GeometryManager.class);

	public static final int NO_POS = Integer.MIN_VALUE;
	/**
	 * Don't set a concrete word spacing. The word is separated using the normal
	 * width of the separator glyph.
	 */
	public static final int DEFAULT_WORD_SPACING = Integer.MIN_VALUE;

	private char[] text;
	/**
	 * An array which contains the calculated positions for the glyphs
	 * Each row represents a glyph, and it contains the X coord, the Y coord, and the rotation angle
	 */
	private double[][] posList;
	private int alignment;
	private float characterSpacing;
	private boolean kerning;
	private float wordSpacing;
	private float margin;
	private boolean rightToLeft;
	private int numGlyphs = 0;
	private float characterWidth;
	private char[] wordSeparators = {' '}; // in the future, separators might be provided as parameter

	/**
	 * <p>Creates a new instance of TextPath with the current graphics
	 * context.<br></p>
	 *
	 * <p>Given a <b>Graphics2D</b>, TextPath can know which Font and FontRenderContext
	 * is in use. So, it can calculate the position and rotation of each
	 * character in <b>char[] text</b> based in the path defined by the
	 * <b>FShape path</b> argument.</p>
	 * @param g, Graphics2D
	 * @param path, FShape
	 * @param text, char[]
	 */
	public TextPath(Graphics2D g, Geometry path, char[] text, Font font,
			float characterSpacing, float characterWidth, boolean kerning,
			float leading, int alignment, float wordSpacing, float margin,
			boolean rightToLeft) {
		this.text = text;
		if (alignment == ITextSymbol.SYMBOL_STYLE_ALIGNMENT_LEFT ||
				alignment == ITextSymbol.SYMBOL_STYLE_ALIGNMENT_RIGHT
				||
				alignment == ITextSymbol.SYMBOL_STYLE_ALIGNMENT_CENTERED ||
				alignment == ITextSymbol.SYMBOL_STYLE_ALIGNMENT_JUSTIFY) {
			this.alignment = alignment;
		} else {
			throw new IllegalArgumentException(
					Messages.getString("invalid_value_for") + ": " +
					Messages.getString("alignment")+" ( "+alignment+")");
		}
		this.characterWidth = characterWidth;
		this.characterSpacing = characterSpacing;
		this.kerning = kerning;
		this.wordSpacing = wordSpacing;
		this.margin = margin;
		this.rightToLeft = rightToLeft;

		FontRenderContext frc = g.getFontRenderContext();
		/* java 6 code
		 * TODO keep this!!
		if (kerning) {
			HashMap<TextAttribute, Object> attrs = new HashMap<TextAttribute, Object>();
			attrs.put(TextAttribute.KERNING , TextAttribute.KERNING_ON);
		}
		 */
		GlyphVector gv = font.createGlyphVector(frc, text);

		PathLength pl;
		try {
			pl = new PathLength(softenShape(path, gv));
			if (alignment==ITextSymbol.SYMBOL_STYLE_ALIGNMENT_RIGHT) {
				posList = computeAtRight(gv, pl, text);
			}
			else if (alignment==ITextSymbol.SYMBOL_STYLE_ALIGNMENT_CENTERED) {
				computeAtMiddle(frc, text, font, pl);
			}
			else {
				posList = computeAtLeft(gv, pl, text);
			}
		} catch (CreateGeometryException e) {
			logger.error("Error creating a curve", e);
		}		
	}

	protected Geometry softenShape(Geometry shape, GlyphVector gv) throws CreateGeometryException {

		float interval = (float) gv.getVisualBounds().getWidth()/(gv.getNumGlyphs()*3);

		PathLength pl = new PathLength(shape);

		GeneralPathX correctedPath = new GeneralPathX();
		int controlPoints = 16;
		double[][] points = new double[controlPoints][2];
		double prevX, prevY;
		double xsum=0, ysum=0;
		int nextPos = 0;
		boolean bufferComplete = false;
		boolean movedTo = false;
		for (float curPos = 0; curPos<pl.lengthOfPath(); curPos = curPos+interval) {
			prevX = points[nextPos][0];
			prevY = points[nextPos][1];
			Point2D point =pl.pointAtLength(curPos);
			if (!movedTo) {
				correctedPath.moveTo(point.getX(), point.getY());
				movedTo = true;
			}

			points[nextPos][0] = point.getX();
			points[nextPos][1] = point.getY();

			if (!bufferComplete) {
				xsum += points[nextPos][0];
				ysum += points[nextPos][1];
				nextPos++;
				if (nextPos==controlPoints) {
					nextPos = 0;
					bufferComplete = true;


					/**
					 * calculate the beginning of the line
					 */
					// this will be the first interpolated point
					double auxX2 = xsum/controlPoints;
					double auxY2 = ysum/controlPoints;

					for (int i=1; i<controlPoints/2-1; i++) {
						// calculate the points from the origin of the geometry to the first interpolated point
						double auxX = (points[0][0]+points[i][0]+auxX2)/3;
						double auxY = (points[0][1]+points[i][1]+auxY2)/3;
						correctedPath.lineTo(auxX, auxY);
					}
					correctedPath.lineTo(auxX2, auxY2);
				}
			}
			else {

				xsum = xsum - prevX + points[nextPos][0];
				ysum = ysum - prevY + points[nextPos][1];
				if (!movedTo) {
					correctedPath.moveTo(xsum/controlPoints, ysum/controlPoints);
					movedTo = true;
				}
				else {
					correctedPath.lineTo(xsum/controlPoints, ysum/controlPoints);
				}

				nextPos = (nextPos+1)%controlPoints;
			}
		}
		Point2D endPoint = pl.pointAtLength(pl.lengthOfPath());
		// last point in the geom
		double endPointX = endPoint.getX();
		double endPointY = endPoint.getY();

		if (bufferComplete) {
			/**
			 * calculate the points from the last interpolated point to the end of the geometry
			 */

			// last interpolated point
			double auxX2 = xsum/controlPoints;
			double auxY2 = ysum/controlPoints;
			nextPos = (nextPos+(controlPoints/2))%controlPoints;
			for (int i=0; i<controlPoints/2-1; i++) {
				// calculate the points from the last interpolated point to the end of the geometry
				double auxX = (auxX2+points[nextPos][0]+endPointX)/3;
				double auxY = (auxY2+points[nextPos][1]+endPointY)/3;
				correctedPath.lineTo(auxX, auxY);
				nextPos = (nextPos+1)%controlPoints;
			}
		}
		correctedPath.lineTo(endPointX, endPointY);

		return geomManager.createCurve(new GeneralPathX(
				correctedPath), SUBTYPES.GEOM2D);
	}

	/**
	 * Initializes the position vector.
	 * @param g
	 * @param path
	 */
	private double[][] computeAtRight(GlyphVector gv, PathLength pl, char[] text) {
		numGlyphs = gv.getNumGlyphs();
		double[][] pos = new double[numGlyphs][3];
		float[] charAnchors = new float[numGlyphs];

		/**
		 * Compute glyph positions using linear distances
		 */
		float lengthOfPath = pl.lengthOfPath();
		// char distance from the right side
		float charDistance = lengthOfPath-margin;
		int glyphsConsumed = numGlyphs-1;
		float previousAngle = 0.0f;
		float angle = 0.0f;
		boolean correction = true;
		float charWidth = characterWidth;
		for (int i = numGlyphs-1; i>=0; i--) {
			if (correction && charDistance>=0) {
				previousAngle = angle;
				angle = pl.angleAtLength(charDistance);
				if (i<numGlyphs-1) {
					// correct distance according to angle between current and previous glyph
					int turn = Angle.getTurn(previousAngle, angle);
					if (turn==1) {  // if turn is positive => increase distance
						float auxDistance = charDistance - (float)(charWidth*2.5f*Angle.diff(previousAngle, angle)/Math.PI);
						float auxAngle = pl.angleAtLength(auxDistance);
						if (Angle.getTurn(previousAngle, auxAngle)==1) { // ensure new position also has positive turn
							charDistance = auxDistance;
							angle = auxAngle;
						}
					}
					else if (turn==-1) { // if turn is negative => decrease distance
						float auxDistance = charDistance + (float)(charWidth*0.9f*Angle.diff(previousAngle, angle)/Math.PI);
						float auxAngle = pl.angleAtLength(auxDistance);
						if (Angle.getTurn(previousAngle, auxAngle)==-1) { // ensure new position also has negative turn
							charDistance = auxDistance;
							angle = auxAngle;
						}
					}
				}
			}

			if (wordSpacing!=DEFAULT_WORD_SPACING
					&& isWordSeparator(text[gv.getGlyphCharIndex(glyphsConsumed)], wordSeparators)) {
				charWidth = wordSpacing;
			}
			else {
				charWidth = Math.max(gv.getGlyphMetrics(glyphsConsumed).getAdvance(), characterWidth);

			}
			charDistance -= charWidth;
			charAnchors[glyphsConsumed] = charDistance;
			charDistance -= characterSpacing;
			glyphsConsumed--;
		}

		/**
		 * Calculate 2D positions for the glyphs from the calculated linear distances
		 */
		for (int i = numGlyphs-1; i>=0; i--) {
			float anchor = (rightToLeft) ? charAnchors[charAnchors.length-1-i] : charAnchors[i];
			Point2D p = pl.pointAtLength( anchor );
			if (p == null) {
				if (i<numGlyphs-1) { // place in a straight line the glyphs that don't fit in the shape
					pos[i][0] = pos[i+1][0] + (charAnchors[i]-charAnchors[i+1])*Math.cos(pos[i+1][2]);
					pos[i][1] = pos[i+1][1] + (charAnchors[i]-charAnchors[i+1])*Math.sin(pos[i+1][2]);
					pos[i][2] = pos[i+1][2];
				} else {
					pos[i][0] = NO_POS;
					pos[i][1] = NO_POS;
				}
				continue;
			}
			pos[i][0] = p.getX();
			pos[i][1] = p.getY();
			pos[i][2] = pl.angleAtLength( anchor );
		}
		return pos;
	}

	/**
	 * Initializes the position vector.
	 * @param g
	 * @param path
	 */
	private double[][] computeAtLeft(GlyphVector gv, PathLength pl, char[] text) {
		numGlyphs = gv.getNumGlyphs();
		double[][] pos = new double[numGlyphs][3];
		float[] charAnchors = new float[numGlyphs];
		float[] charWidths = new float[numGlyphs];

		/**
		 * Compute glyph positions using linear distances
		 */
		float lengthOfPath = pl.lengthOfPath();
		float charDistance = margin;
		int glyphsConsumed = 0;
		float previousAngle = 0.0f;
		float angle = 0.0f;
		boolean correction = true;
		float charWidth = characterWidth;
		for (int i = 0; i < gv.getNumGlyphs(); i++) {

			if (correction && charDistance<=lengthOfPath) {
				previousAngle = angle;
				angle = pl.angleAtLength(charDistance);
				if (i>0) {
					// correct distance according to angle between current and previous glyph
					int turn = Angle.getTurn(previousAngle, angle);
					if (turn==1) {  // if turn is positive => decrease distance
						float auxDistance = charDistance - (float)(charWidth*0.9*Angle.diff(previousAngle, angle)/Math.PI);
						float auxAngle = pl.angleAtLength(auxDistance);
						if (Angle.getTurn(previousAngle, auxAngle)==1) { // ensure new position also has positive turn
							charDistance = auxDistance;
							angle = auxAngle;
						}
					}
					else if (turn == -1){ // if turn is negative => increase distance

						float auxDistance = charDistance + (float)(charWidth*2.5*Angle.diff(previousAngle, angle)/Math.PI);
						float auxAngle = pl.angleAtLength(auxDistance);
						if (Angle.getTurn(previousAngle, auxAngle)==-1) { // ensure new position also has negative turn
							charDistance = auxDistance;
							angle = auxAngle;
						}
					}
				}
			}
			if (wordSpacing!=DEFAULT_WORD_SPACING
					&& isWordSeparator(text[gv.getGlyphCharIndex(glyphsConsumed)], wordSeparators)) {
				// use defined wordspacing
				charWidth = wordSpacing;
			}
			else {
				charWidth = Math.max(gv.getGlyphMetrics(glyphsConsumed).getAdvance(), characterWidth);

			}
			charWidths[glyphsConsumed] = charWidth;
			charAnchors[glyphsConsumed] = charDistance;
			charDistance += charWidth;
			charDistance += characterSpacing;
			glyphsConsumed++;
		}

		/**
		 * Calculate 2D positions for the glyphs from the calculated linear distances
		 */
		for (int i = 0; i < charAnchors.length; i++) {
			float anchor = (rightToLeft) ? charAnchors[charAnchors.length-1-i] : charAnchors[i];
			Point2D p = pl.pointAtLength( anchor );
			if (p == null) {
				if (i>0) { // place in a straight line the glyphs that don't fit in the shape
					pos[i][0] = pos[i-1][0] + (charAnchors[i]-charAnchors[i-1])*Math.cos(pos[i-1][2]);
					pos[i][1] = pos[i-1][1] + (charAnchors[i]-charAnchors[i-1])*Math.sin(pos[i-1][2]);
					pos[i][2] = pos[i-1][2];
				} else {
					pos[i][0] = NO_POS;
					pos[i][1] = NO_POS;
				}
				continue;
			}
			pos[i][2] = pl.angleAtLength( anchor );
			//			pos[i][0] = p.getX() - charWidths[i]*Math.cos(pos[i][2]);
			//			pos[i][1] = p.getY() - charWidths[i]*Math.sin(pos[i][2]);
			pos[i][0] = p.getX();
			pos[i][1] = p.getY();
		}
		return pos;
	}


	/**
	 * Initializes the position vector.
	 * @param g
	 * @param path
	 */
	private void computeAtMiddle(FontRenderContext frc, char[] text, Font font, PathLength pl) {
		if (text.length==0) {
			return; // nothing to compute if text length is 0
		}
		int middleChar = (text.length-1)/2;
		char[] text1 = new char[middleChar+1];
		char[] text2 = new char[text.length-text1.length];
		System.arraycopy(text, 0, text1, 0, text1.length);
		System.arraycopy(text, text1.length,  text2, 0, text2.length);

		float halfLength = pl.lengthOfPath()/2.0f;
		margin = halfLength;
		GlyphVector gv = font.createGlyphVector(frc, text1);
		double[][] pos1 = computeAtRight(gv, pl, text1);
		int glyphCount = numGlyphs;
		gv = font.createGlyphVector(frc, text2);
		margin = halfLength + characterSpacing;
		double[][] pos2 = computeAtLeft(gv, pl, text2);
		numGlyphs += glyphCount;
		posList = new double[pos1.length+pos2.length][3];
		System.arraycopy(pos1, 0, posList, 0, pos1.length);
		System.arraycopy(pos2, 0, posList, pos1.length, pos2.length);
	}


	/**
	 * <p>Returns the placement of the next character to draw and the corresponding
	 * rotation in a double array of three elements with this order:</p><br>
	 *
	 * <p><b>double[0]</b> Position in X in the screen</p>
	 * <p><b>double[1]</b> Position in Y in the screen</p>
	 * <p><b>double[2]</b> Angle of the character.</p>
	 * @return
	 */
	public double[] nextPosForGlyph(int glyphIndex) {
		return posList[glyphIndex];
	}

	public int getGlyphCount() {
		return numGlyphs;
	}

	protected static boolean isWordSeparator(char c, char[] wordSeparators) {
		char separator;
		for (int i = 0; i < wordSeparators.length; i++) {
			separator = wordSeparators[i];
			if (c==separator) {
				return true;
			}
		}
		return false;
	}

}
