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
package org.gvsig.fmap.mapcontext.rendering.symbol;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import junit.framework.TestCase;

import org.gvsig.compat.CompatLocator;
import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.fmap.geom.Geometry.SUBTYPES;
import org.gvsig.fmap.geom.exception.CreateGeometryException;
import org.gvsig.fmap.geom.primitive.Curve;
import org.gvsig.fmap.geom.primitive.GeneralPathX;
import org.gvsig.fmap.geom.util.UtilFunctions;
import org.gvsig.fmap.mapcontext.rendering.symbols.ILineSymbol;
import org.gvsig.fmap.mapcontext.rendering.symbols.ISymbol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Integration test to test that Line symbols always draw in the same
 * place respecting size constraints.
 *
 * @author jaume dominguez faus - jaume.dominguez@iver.es
 */
public class TestDrawLines extends TestCase {
	private static final GeometryManager geomManager = GeometryLocator.getGeometryManager();
	private static final Logger logger = LoggerFactory.getLogger(TestDrawLines.class);
	private ILineSymbol[] symbols;
	private final Dimension sz = new Dimension(401, 401);
	private Curve centerL;

	{
		GeneralPathX gp = new GeneralPathX();
		gp.moveTo(0, sz.height / 2 );
		gp.lineTo(sz.width, sz.height /2 );

		try {
			centerL = geomManager.createCurve(gp, SUBTYPES.GEOM2D);
		} catch (CreateGeometryException e) {
			e.printStackTrace();
		}
	}
	private static final double sizes[] = new double[] {
		300,
		250,
		225,
		200,
		100,
		50,
		30,
		15,
		5,
		3,
		2,
		1,
		// smaller sizes don't make any sense
	};

	private static final float INNER_TOLERANCE = 1F;
	private static final float OUTTER_TOLERANCE = 1F;

	protected void setUp() throws Exception {
		// get all the symbols in the Test Suite
		super.setUp();

		ISymbol[] allSymbols = TestISymbol.getNewSymbolInstances();
		// Filter the marker ones
		ArrayList symbols = new ArrayList();

		for (int i = 0; i < allSymbols.length; i++) {
			if (allSymbols[i] instanceof ILineSymbol) {
				ILineSymbol sym = (ILineSymbol) allSymbols[i];
				symbols.add(sym);

			}
		}
		this.symbols = (ILineSymbol[]) symbols.toArray(new ILineSymbol[symbols.size()]);
	}

	public void testDrawingSize() {
		for (int i = 0; i < symbols.length; i++) {
			for (int j = 0; j < sizes.length; j++) {
				// new blank buffered image
				
				BufferedImage bi = CompatLocator.getGraphicsUtils().createBufferedImage(sz.width, sz.height, BufferedImage.TYPE_INT_ARGB);

				// the graphics for the image, so we can draw onto the buffered image
				Graphics2D g = bi.createGraphics();

				ILineSymbol testSymbol = symbols[i];
				testSymbol.setLineColor(Color.YELLOW);
				testSymbol.setLineWidth(sizes[j]);
				String name = testSymbol.getClassName().substring(
						testSymbol.getClassName().lastIndexOf('.')+1,
						testSymbol.getClassName().length());

				Point2D upperP1 = new Point2D.Double(0, centerL.getBounds().getY() - sizes[j]*0.5);
				Point2D upperP2 = new Point2D.Double(centerL.getBounds().getWidth(), centerL.getBounds().getY() - sizes[j]*0.5);
				GeneralPathX gpUp = new GeneralPathX();
				gpUp.moveTo(upperP1.getX(), upperP1.getY());
				gpUp.lineTo(upperP2.getX(), upperP2.getY());


				GeneralPathX gpDown = new GeneralPathX();
				Point2D lowerP1 = new Point2D.Double(0, centerL.getBounds().getY() + sizes[j]*0.5);
				Point2D lowerP2 = new Point2D.Double(centerL.getBounds().getWidth(), centerL.getBounds().getY() + sizes[j]*0.5);
				gpDown.moveTo(lowerP1.getX(), lowerP1.getY());
				gpDown.lineTo(lowerP2.getX(), lowerP2.getY());


				testSymbol.draw(g, new AffineTransform(), centerL, null);

				/// per a borrar
//				g.setStroke(new BasicStroke());
//				g.setColor(Color.RED);
//
//				g.draw(gpUp);
//
//				g.setColor(Color.BLUE);
//				g.draw(gpDown);
//
//				try {
//
//					File dstDir = new File (System.getProperty("java.io.tmpdir")+"/prova-imatges/");
//					if (!dstDir.exists()) dstDir.mkdir();
//				ImageIO.write(bi, "png",
//						new File(dstDir.getAbsoluteFile()+File.separator+
//									name+"_size_"+sizes[j]
//									                    +".png"));
//				} catch (IOException e) {
//					e.printStackTrace();
//					fail();
//				}
				/// fi per a borrar
				assertFalse("fails sizing line, too big ("+name+", "+sizes[j]+"px)", isOutsideRect(bi, upperP1, lowerP1, OUTTER_TOLERANCE ));
				assertTrue("fails sizing line, too small ("+name+", "+sizes[j]+"px) \n" +
						"\t - forgot to enable ANTIALIASING?", fitsInsideRect(bi, upperP1, lowerP1, INNER_TOLERANCE));

			}
		}
	}

	private boolean isOutsideRect(BufferedImage bi, Point2D upper, Point2D lower, float outterTolerance) {
		for (int i = 0; i < bi.getWidth(); i++) {
			for (int j = 0; j < bi.getHeight(); j++) {
				if (j<upper.getY()-outterTolerance && j>lower.getY()+outterTolerance)
					if (bi.getRGB(i,j) != 0) {
						System.out.println("too big In pixel ("+i+", "+j+")");
						return true;
					}

			}
		}
		return false;
	}

	private boolean fitsInsideRect(BufferedImage bi,Point2D upper, Point2D lower , float innerTolerance) {
		for (int i = 0; i < bi.getWidth(); i++) {
			for (int j = 0; j < bi.getHeight(); j++) {
				if (j<upper.getY()+innerTolerance && j>lower.getY()-innerTolerance)
					if (bi.getRGB(i,j) == 0) {
						System.out.println("does not fit big In pixel ("+i+", "+j+")");
						return false;
					}

			}
		}
		return true;
	}

}
