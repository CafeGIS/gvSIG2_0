/* gvSIG. Sistema de Informaci�n Geogr�fica de la Generalitat Valenciana
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
package org.gvsig.fmap.mapcontext.rendering.symbol;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import junit.framework.TestCase;

import org.gvsig.compat.CompatLocator;
import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.fmap.geom.Geometry.SUBTYPES;
import org.gvsig.fmap.geom.util.UtilFunctions;
import org.gvsig.fmap.mapcontext.rendering.symbols.IMarkerSymbol;
import org.gvsig.fmap.mapcontext.rendering.symbols.ISymbol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Integration test to test that Marker symbols always draw in the same
 * place respecting size constraints.
 *
 * @author jaume dominguez faus - jaume.dominguez@iver.es
 */
public class TestDrawMarkers extends TestCase {
	private static final GeometryManager geomManager = GeometryLocator.getGeometryManager();
	private static final Logger logger = LoggerFactory.getLogger(TestDrawMarkers.class);
	private IMarkerSymbol[] symbols;
	private final Dimension sz = new Dimension(400, 400);
	private org.gvsig.fmap.geom.primitive.Point centerP = null;
	private static final int OUTTER_TOLERANCE = 1;
	private static final int INNER_TOLERANCE = 1;

	private static final double sizes[] = new double[] {
		200,
		100,
		50,
		30,
		16,
		5,
		3,
		2,
		// smaller sizes don't make any sense

	};



	protected void setUp() throws Exception {
		// get all the symbols in the Test Suite
		super.setUp();

		centerP = geomManager.createPoint(sz.width/2, sz.height/2, SUBTYPES.GEOM2D);
		
		ISymbol[] allSymbols = TestISymbol.getNewSymbolInstances();
		// Filter the marker ones
		ArrayList symbols = new ArrayList();

		for (int i = 0; i < allSymbols.length; i++) {
			if (allSymbols[i] instanceof IMarkerSymbol) {
				IMarkerSymbol sym = (IMarkerSymbol) allSymbols[i];
				symbols.add(sym);

			}
		}
		this.symbols = (IMarkerSymbol[]) symbols.toArray(new IMarkerSymbol[symbols.size()]);
	}

	public void testDrawingSize() {
		for (int i = 0; i < symbols.length; i++) {
			for (int j = 0; j < sizes.length; j++) {
				// new blank buffered image
				BufferedImage bi = CompatLocator.getGraphicsUtils().createBufferedImage(sz.width, sz.height, BufferedImage.TYPE_INT_ARGB);

				// the graphics for the image, so we can draw onto the buffered image
				Graphics2D g = bi.createGraphics();

				IMarkerSymbol testSymbol = symbols[i];
				testSymbol.setColor(Color.YELLOW);
				testSymbol.setSize(sizes[j]);
				String name = testSymbol.getClassName().substring(
						testSymbol.getClassName().lastIndexOf('.')+1,
						testSymbol.getClassName().length());

				testSymbol.draw(g, new AffineTransform(), centerP, null);

				Rectangle wrappingRect = new Rectangle(
						(int) (centerP.getX()-sizes[j]/2),
						(int) (centerP.getY()-sizes[j]/2),
						(int) (sizes[j]),
						(int) (sizes[j]));

//				if (testSymbol instanceof CharacterMarkerSymbol)
//					continue;




				assertFalse("fails sizing marker, too big ("+name+", "+sizes[j]+"px)", isOutsideRect(bi, wrappingRect, OUTTER_TOLERANCE ));
				assertTrue("fails sizing marker, too small ("+name+", "+sizes[j]+"px) \n" +
						"\t - forgot to enable ANTIALIASING?", fitsInsideRect(bi, wrappingRect, INNER_TOLERANCE));


			}
		}
	}

	public void testDrawingOffset() {
		Random random = new Random(System.currentTimeMillis());
		for (int i = 0; i < symbols.length; i++) {
			for (int j = 0; j < sizes.length; j++) {
				// new blank buffered image
				BufferedImage bi = CompatLocator.getGraphicsUtils().createBufferedImage(sz.width, sz.height, BufferedImage.TYPE_INT_ARGB);


				// the graphics for the image, so we can draw onto the buffered image
				Graphics2D g = bi.createGraphics();

				IMarkerSymbol testSymbol = symbols[i];
				testSymbol.setColor(Color.YELLOW);
				testSymbol.setSize(sizes[j]);

				String name = testSymbol.getClassName().substring(
						testSymbol.getClassName().lastIndexOf('.')+1,
						testSymbol.getClassName().length());

				double xOffset = (random.nextDouble()-0.5)*sz.width;
				double yOffset = (random.nextDouble()-0.5)*sz.height;

				testSymbol.setOffset(new Point2D.Double(xOffset, yOffset));

				Rectangle wrappingRect = new Rectangle(
						(int) ((centerP.getX()-sizes[j]/2) + xOffset),
						(int) ((centerP.getY()-sizes[j]/2) + yOffset),
						(int) (sizes[j]),
						(int) (sizes[j]));

//				if (testSymbol instanceof CharacterMarkerSymbol)
//					continue;



				testSymbol.draw(g, new AffineTransform(), centerP, null);
				assertFalse("fails sizing marker, too big ("+name+", "+sizes[j]+"px)", isOutsideRect(bi, wrappingRect, OUTTER_TOLERANCE ));
				assertTrue("fails sizing marker, too small ("+name+", "+sizes[j]+"px) \n" +
						"\t - forgot to enable ANTIALIASING?", fitsInsideRect(bi, wrappingRect, INNER_TOLERANCE));

			}
		}
	}

	public void testDrawingRotation() {
		// TODO implement it

	}

	private boolean fitsInsideRect(BufferedImage bi, Rectangle wrappingRect, int tolerance) {
		int myTolerance = Math.min(tolerance, wrappingRect.width);
		Rectangle myRect;
		if (myTolerance >= INNER_TOLERANCE) {
			myRect = new Rectangle(
					wrappingRect.x+myTolerance,
					wrappingRect.y+myTolerance,
					wrappingRect.width-myTolerance-myTolerance,
					wrappingRect.height-myTolerance-myTolerance);
		} else {
			myRect = new Rectangle(-1, -1, 0, 0); // just ensure it does not disturb the check
		}

		for (int i = 0; i < bi.getWidth(); i++) {
			for (int j = 0; j < bi.getHeight(); j++) {
				if (wrappingRect.contains(i,j)
						&& !myRect.contains(i,j)) {

					if (bi.getRGB(i,j) != 0)
						return true;
				}
			}
		}
		return false;
	}

	private boolean isOutsideRect(BufferedImage bi, Rectangle wrappingRect, int tolerance) {
		Rectangle myRect = new Rectangle(
				wrappingRect.x-tolerance,
				wrappingRect.y-tolerance,
				wrappingRect.width+tolerance+tolerance,
				wrappingRect.height+tolerance+tolerance);
		for (int i = 0; i < bi.getWidth(); i++) {
			for (int j = 0; j < bi.getHeight(); j++) {
				if (!myRect.contains(i,j))
					continue;

				if (bi.getRGB(i,j) != 0) return false;
			}
		}
		return true;
	}
}

/// paste this piece of code to produce some png files to check visually what is happening
//g.setColor(Color.BLUE);
//Rectangle myRect = new Rectangle(
//		wrappingRect.x+INNER_TOLERANCE,
//		wrappingRect.y+INNER_TOLERANCE,
//		wrappingRect.width-INNER_TOLERANCE-INNER_TOLERANCE,
//		wrappingRect.height-INNER_TOLERANCE-INNER_TOLERANCE);
//
//g.draw(myRect);
//g.setColor(Color.RED);
//g.draw(wrappingRect);
//try {
//
//	File dstDir = new File (System.getProperty("java.io.tmpdir")+"/prova-imatges/");
//	if (!dstDir.exists()) dstDir.mkdir();
//	ImageIO.write(bi, "png",
//			new File(dstDir.getAbsoluteFile()+File.separator+
//					name+"_size_"+sizes[j]
//					                    +".png"));
//} catch (IOException e) {
//	e.printStackTrace();
//	fail();
//}
///
