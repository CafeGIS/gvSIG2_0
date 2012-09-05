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

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import junit.framework.TestCase;

import org.gvsig.compat.CompatLocator;
import org.gvsig.fmap.AllTests;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.fmap.geom.Geometry.SUBTYPES;
import org.gvsig.fmap.geom.exception.CreateGeometryException;
import org.gvsig.fmap.geom.primitive.GeneralPathX;
import org.gvsig.fmap.geom.util.UtilFunctions;
import org.gvsig.fmap.mapcontext.MapContext;
import org.gvsig.fmap.mapcontext.rendering.symbols.IFillSymbol;
import org.gvsig.fmap.mapcontext.rendering.symbols.ISymbol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Integration test to test that Fill symbols always draw in the same
 * place respecting size constraints.
 *
 * @author jaume dominguez faus - jaume.dominguez@iver.es
 */
public class TestDrawFills extends TestCase {
	private static final GeometryManager geomManager = GeometryLocator.getGeometryManager();
	private static final Logger logger = LoggerFactory.getLogger(TestDrawFills.class);
	private final Dimension sz = new Dimension(400, 400);
	private org.gvsig.fmap.geom.primitive.Point centerP = null;
	private static final int OUTTER_TOLERANCE = 1;
	private static final int INNER_TOLERANCE = 1;
	private IFillSymbol[] symbols;
	private IDrawFillSymbol[] drawFillSymbols;
	private static ArrayList classesToTest;

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


	private static ArrayList getClassesToTest() {
		if (classesToTest == null) {
			classesToTest = new ArrayList();
//			TestDrawFills.addSymbolToTest(DrawLineFillSymbol.class);
//			TestDrawFills.addSymbolToTest(DrawPictureFillSymbol.class);
		}

		return classesToTest;
	}

	public static void addSymbolToTest(Class symbolClass) {
        try {
            IDrawFillSymbol sym = (IDrawFillSymbol) symbolClass.newInstance();
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            fail("Instantiating class, cannot test a non-instantiable symbol");
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            fail("Class not instantiable");
        } catch (ClassCastException ccEx) {
            fail("Cannot test a non symbol class");
        }
        getClassesToTest().add(symbolClass);
    }


	protected void setUp() throws Exception {
		// get all the symbols in the Test Suite
		super.setUp();
		 centerP = geomManager.createPoint(sz.width/2, sz.height/2, SUBTYPES.GEOM2D);
		
		this.drawFillSymbols = new IDrawFillSymbol[getClassesToTest().size()];

		for (int i = 0; i < drawFillSymbols.length; i++) {
			drawFillSymbols[i] = (IDrawFillSymbol) ((Class) getClassesToTest().get(i)).newInstance();
		}


		ISymbol[] allSymbols = TestISymbol.getNewSymbolInstances();
		// Filter the marker ones
		ArrayList symbols = new ArrayList();

		for (int i = 0; i < allSymbols.length; i++) {
			if (allSymbols[i] instanceof IFillSymbol) {
				IFillSymbol sym = (IFillSymbol) allSymbols[i];
				symbols.add(sym);

			}
		}
		this.symbols = (IFillSymbol[]) symbols.toArray(new IFillSymbol[symbols.size()]);



	}

	public void testDraw() {
		MapContext mc = AllTests.newMapContext(AllTests.TEST_DEFAULT_MERCATOR_PROJECTION);

	}


	public final void testNullColorAvoidsFilling() throws CreateGeometryException {

		for (int i = 0; i < symbols.length; i++) {

			for (int s = 0; s < sizes.length; s++) {

				BufferedImage bi = CompatLocator.getGraphicsUtils().createBufferedImage(sz.width,sz.height, BufferedImage.TYPE_INT_ARGB);
				// the graphics for the image, so we can draw onto the buffered image
				Graphics2D g = bi.createGraphics();

				IFillSymbol newSymbol = symbols[i];
				newSymbol.setFillColor(null);
				newSymbol.setOutline(null);




				String name = newSymbol.getClassName().substring(
						newSymbol.getClassName().lastIndexOf('.')+1,
						newSymbol.getClassName().length());


				Point2D firstPoint = new Point2D.Float((float)(centerP.getX()-sizes[s]/2-3),(float)(centerP.getY()-sizes[s]/2-3));
				Point2D lastPoint = new Point2D.Float((float)(centerP.getX()+sizes[s]/2+3),(float)(centerP.getY()+sizes[s]/2+3));



				GeneralPathX gpx = new GeneralPathX();

				gpx.moveTo((float)firstPoint.getX(),(float) firstPoint.getY());
				gpx.lineTo((float)lastPoint.getX(),(float) firstPoint.getY());
				gpx.lineTo((float)lastPoint.getX(),(float) lastPoint.getY());
				gpx.lineTo((float)firstPoint.getX(),(float) lastPoint.getY());
				gpx.lineTo((float)firstPoint.getX(), (float)firstPoint.getY());
				gpx.closePath();


				for (int j = 0; j < drawFillSymbols.length; j++) {
					if (drawFillSymbols[j].isSuitableFor(newSymbol)) {
						newSymbol = drawFillSymbols[j].makeSymbolTransparent(newSymbol);
						break;
					}
				}


				newSymbol.draw(g, new AffineTransform(), geomManager.createSurface(gpx, SUBTYPES.GEOM2D), null);

				try {

					File dstDir = new File (System.getProperty("java.io.tmpdir")+"/prova-imatges/");
					if (!dstDir.exists()) dstDir.mkdir();
					ImageIO.write(bi, "png",
							new File(dstDir.getAbsoluteFile()+File.separator+
									name+"_size_"+sizes[s]
									                    +".png"));
				} catch (IOException e) {
					e.printStackTrace();
					fail();
				}

				for (int j = 0; j < bi.getWidth(); j++) {
					for (int k = 0; k < bi.getHeight(); k++) {
						if (isInsideShape(geomManager.createSurface(gpx, SUBTYPES.GEOM2D), j, k)) {
							assertEquals("Failed, the fill is being painted when the color is null",bi.getRGB(j, k),0);

						}
					}

				}
			}
		}
	}


	private boolean isInsideShape(Geometry geom, int x, int y) {
		Point2D p = new Point2D.Float(x,y);
		if(geom.contains(p))return true;
		return false;
	}


}
