package org.gvsig.fmap.mapcontext.rendering.symbol;

import java.awt.Color;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Random;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.fmap.geom.Geometry.SUBTYPES;
import org.gvsig.fmap.geom.Geometry.TYPES;
import org.gvsig.fmap.geom.exception.CreateGeometryException;
import org.gvsig.fmap.geom.impl.DefaultGeometryLibrary;
import org.gvsig.fmap.geom.primitive.Curve;
import org.gvsig.fmap.geom.primitive.GeneralPathX;
import org.gvsig.fmap.geom.primitive.Point;
import org.gvsig.fmap.geom.primitive.Surface;
import org.gvsig.fmap.mapcontext.rendering.symbols.IFillSymbol;
import org.gvsig.fmap.mapcontext.rendering.symbols.ILineSymbol;
import org.gvsig.fmap.mapcontext.rendering.symbols.IMarkerSymbol;
import org.gvsig.fmap.mapcontext.rendering.symbols.ISymbol;
import org.gvsig.fmap.mapcontext.rendering.symbols.ITextSymbol;
import org.gvsig.fmap.mapcontext.rendering.symbols.SymbologyFactory;

import com.iver.utiles.XMLException;
/**
 * Integration test to ensure that the symbols follow the rules that follow the
 * managing of them by the application.
 *
 * @author jaume dominguez faus - jaume.dominguez@iver.es
 */


public class TestISymbol extends TestCase {
    private static ArrayList classesToTest;//<AbstractSymbolTestCase>
    transient private ISymbol[] symbols;


    public static TestSuite suite() {
    	TestSuite suite = new TestSuite("Integration test for com.iver.cit.gvsig.fmap.core.ISymbol");
        suite.addTestSuite(TestISymbol.class);
        suite.addTestSuite(TestDrawMarkers.class);
        suite.addTestSuite(TestDrawLines.class);
        suite.addTestSuite(TestDrawFills.class);

        addSymbolTest(new SimpleFillSymbolTest());
        addSymbolTest(new SimpleLineSymbolTest());
        addSymbolTest(new SimpleMarkerSymbolTest());
        addSymbolTest(new SimpleTextSymbolTest());
        return suite;
    }

    public static void addSymbolTest(AbstractSymbolTestCase symbolTestClass) {
    	if (classesToTest == null) classesToTest = new ArrayList(); //<AbstractSymbolTestCase>

    	classesToTest.add(symbolTestClass);
    }

    public static ISymbol[] getNewSymbolInstances() {
        ISymbol[] symbols = new ISymbol[classesToTest.size()];
        for (int i = 0; i < symbols.length; i++) {
        	symbols[i] = (ISymbol)((AbstractSymbolTestCase)classesToTest.get(i)).newInstance();
        }
        return symbols;
    }

    protected void setUp() throws Exception {
        symbols = getNewSymbolInstances();
        
        DefaultGeometryLibrary lib = new DefaultGeometryLibrary();		
		lib.initialize();
		lib.postInitialize();
    }

    public void testPointSuitability() throws InstantiationException, IllegalAccessException, CreateGeometryException {
    	GeometryManager geomManager = GeometryLocator.getGeometryManager();
    	Point point = (Point)geomManager.create(TYPES.CURVE, SUBTYPES.GEOM2D);
    	final Geometry dummyPointGeom = point;
        for (int i = 0; i < symbols.length; i++) {
            // text symbols are suitable for everything
            if (symbols[i] instanceof ITextSymbol) {
                assertTrue(symbols[i].getClassName()+" should be suitable for "+dummyPointGeom,
                        symbols[i].isSuitableFor(dummyPointGeom));
            } else

            // marker symbols are suitable for points
            if (symbols[i] instanceof IMarkerSymbol) {
                assertTrue(symbols[i].getClassName()+" should be suitable for "+dummyPointGeom,
                        symbols[i].isSuitableFor(dummyPointGeom));
            } else {
                assertFalse(symbols[i].getClassName()+" should NOT be suitable for "+dummyPointGeom,
                        symbols[i].isSuitableFor(dummyPointGeom));
            }
        }
    }

    public void testLineSuitability() throws InstantiationException, IllegalAccessException, CreateGeometryException {
    	GeometryManager geomManager = GeometryLocator.getGeometryManager();
    	Curve curve = (Curve)geomManager.create(TYPES.CURVE, SUBTYPES.GEOM2D);
		curve.setGeneralPath(new GeneralPathX());
    	final Geometry dummyLineGeom = curve;
        for (int i = 0; i < symbols.length; i++) {
            // text symbols are suitable for everything
            if (symbols[i] instanceof ITextSymbol) {
                assertTrue(symbols[i].getClassName()+" should be suitable for "+dummyLineGeom,
                        symbols[i].isSuitableFor(dummyLineGeom));
            } else

            // line symbols are suitable for line
            if (symbols[i] instanceof ILineSymbol) {
                assertTrue(symbols[i].getClassName()+" should be suitable for "+dummyLineGeom,
                        symbols[i].isSuitableFor(dummyLineGeom));
            } else {
                assertFalse(symbols[i].getClassName()+" should NOT be suitable for "+dummyLineGeom,
                        symbols[i].isSuitableFor(dummyLineGeom));
            }
        }
    }

    public void testPolygonSuitability() throws InstantiationException, IllegalAccessException, CreateGeometryException {
    	GeometryManager geomManager = GeometryLocator.getGeometryManager();
    	Surface surface = (Surface)geomManager.create(TYPES.CURVE, SUBTYPES.GEOM2D);
    	surface.setGeneralPath(new GeneralPathX());
        final Geometry dummyPolygonGeom = surface;
        for (int i = 0; i < symbols.length; i++) {

            // text symbols are suitable for everything
            if (symbols[i] instanceof ITextSymbol) {
                assertTrue(symbols[i].getClassName()+" should be suitable for "+dummyPolygonGeom,
                        symbols[i].isSuitableFor(dummyPolygonGeom));
            } else

            // fill symbols are suitable for polygons
            if (symbols[i] instanceof IFillSymbol) {
                assertTrue(symbols[i].getClassName()+" should be suitable for "+dummyPolygonGeom,
                        symbols[i].isSuitableFor(dummyPolygonGeom));
            } else {
                assertFalse(symbols[i].getClassName()+" should NOT be suitable for "+dummyPolygonGeom,
                        symbols[i].isSuitableFor(dummyPolygonGeom));
            }
        }
    }

    /**
     * tests whether the symbols were correctly configured to work with
     * different kinds of shapes.
     */
    public void testGeneralSuitability() {

        for (int i = 0; i < symbols.length; i++) {
            // text symbols are suitable for everything
            if (symbols[i] instanceof ITextSymbol) {
                assertTrue(symbols[i].getClassName()+" should be suitable for "+null,
                        symbols[i].isSuitableFor(null));
            } else {
                try {
                    symbols[i].isSuitableFor(null);
                    fail("Exception was not thrown");

                } catch (NullPointerException npEx) {
                    // this is correct!
                }
            }
        }
    }

    /**
     * ensures that symbols defined which can of FShape is the symbol
     */
    public void testSymbolTypeDefinition() {
        for (int i = 0; i < symbols.length; i++) {
            assertFalse("Symbol no. "+i+" '"+symbols[i].getClassName()+"'",
                        symbols[i].getSymbolType() == 0);

        }
    }

    /**
     * ensures that any symbol that is suitable for markers declares its type
     * as FShape.POINT or FShape.MULTI
     *
     */
    public void testMarkerSymbolTypeDefinition() {
        for (int i = 0; i < symbols.length; i++) {
            if (symbols[i] instanceof IMarkerSymbol) {
                assertTrue("Symbol no. "+i+" '"+symbols[i].getClassName()+"'",
                        symbols[i].getSymbolType() == Geometry.TYPES.POINT
                        || symbols[i].getSymbolType() == Geometry.TYPES.GEOMETRY);
            }
        }
    }

    /**
     * ensures that any symbol that is suitable for lines declares its type
     * as FShape.LINE or FShape.MULTI
     *
     */
    public void testLineSymbolTypeDefinition() {
        for (int i = 0; i < symbols.length; i++) {
            if (symbols[i] instanceof ILineSymbol) {
                assertTrue("Symbol no. "+i+" '"+symbols[i].getClassName()+"'",
                        symbols[i].getSymbolType() == Geometry.TYPES.CURVE
                        || symbols[i].getSymbolType() == Geometry.TYPES.GEOMETRY);
            }
        }
    }

    /**
     * ensures that any symbol that is suitable for fills declares its type
     * as POLYGON or Geometry.TYPES.GEOMETRY
     *
     */
    public void testFillSymbolTypeDefinition() {
        for (int i = 0; i < symbols.length; i++) {
            if (symbols[i] instanceof IFillSymbol) {
                assertTrue("Symbol no. "+i+" '"+symbols[i].getClassName()+"'",
                        symbols[i].getSymbolType() == Geometry.TYPES.SURFACE
                        || symbols[i].getSymbolType() == Geometry.TYPES.GEOMETRY);
            }
        }
    }

    /**
     * ensures that any symbol has a description in its persistence
     * (very important)
     */
    public void testDescription() {
        for (int i = 0; i < symbols.length; i++) {
            try {
				assertTrue(symbols[i].getClassName() + " does not declare a description in its XMLEntity",
				        symbols[i].getXMLEntity().contains("desc"));
			} catch (XMLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    }

    /**
     * ensures that any symbol has an isShapeVisible field in its persistence
     * (very important)
     */
    public void testIsShapeVisible() {
        for (int i = 0; i < symbols.length; i++) {
            try {
				assertTrue(symbols[i].getClassName() + " does not declare the isShapeVisible field in its XMLEntity",
				        symbols[i].getXMLEntity().contains("isShapeVisible"));
			} catch (XMLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    }

    /**
     * ensures that the symbol is self-defining
     */
    public void testSymbolSelfDefinition() {
        for (int i = 0; i < symbols.length; i++) {
            final ISymbol theSymbol = symbols[i];
            ISymbol cloneSymbol=null;
			try {
				cloneSymbol = SymbologyFactory.createSymbolFromXML(theSymbol.getXMLEntity(), null);
			} catch (XMLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
            assertTrue(theSymbol.getClassName()+ " wrong class name declaration in getXMLEntity() ",
                    cloneSymbol.getClass().equals(theSymbol.getClass()));
            final Field[] theSymbolFields = theSymbol.getClass().getFields();
            for (int j = 0; j < theSymbolFields.length; j++) {
                final Field fi = theSymbolFields[j];
                final boolean wasAccessible = fi.isAccessible();
                fi.setAccessible(true);

                try {
                    assertTrue(theSymbol.getClassName() + " fails or misses clonning the field " +fi.getName(),
                            fi.get(theSymbol).equals(fi.get(cloneSymbol)));
                } catch (IllegalArgumentException e) {
                    fail();
                } catch (IllegalAccessException e) {
                    fail();
                }
                fi.setAccessible(wasAccessible);
            }
        }
    }

    /**
     * Check one pixel acceleration consistency. Checks that the color returned
     * in RGB matches the color of the symbol set.
     *
     */
    public void testOnePointRGB() {
    	Random random = new Random(System.currentTimeMillis());

        for (int i = 0; i < symbols.length; i++) {
            if (symbols[i] instanceof IMarkerSymbol) {
                IMarkerSymbol marker = (IMarkerSymbol) symbols[i];
                marker.setColor(new Color(random.nextInt()>>8));
                assertTrue("Symbol no. "+i+" '"+symbols[i].getClassName()+"' RGB value mismatch for getOnePointRBG() and getColor().getRGB()",
                        symbols[i].getOnePointRgb() == marker.getColor().getRGB());

            }

            if (symbols[i] instanceof ILineSymbol) {
                ILineSymbol line = (ILineSymbol) symbols[i];
                line.setLineColor(new Color(random.nextInt()>>8));
                assertTrue("Symbol no. "+i+" '"+symbols[i].getClassName()+"' RGB value mismatch for getOnePointRBG() and getColor().getRGB()",
                        symbols[i].getOnePointRgb() == line.getColor().getRGB());
            }

            if (symbols[i] instanceof IFillSymbol) {
                IFillSymbol fill = (IFillSymbol) symbols[i];
                boolean outlined = fill.getOutline() != null;
                if (!outlined)
                	fill.setFillColor(new Color(random.nextInt()>>8));
                else
                	fill.getOutline().setLineColor(new Color(random.nextInt()>>8));
                assertTrue("Symbol no. "+i+" '"+symbols[i].getClassName()+"' RGB value mismatch for getOnePointRBG() and getColor().getRGB()",
                        symbols[i].getOnePointRgb() == ((outlined) ? fill.getOutline().getColor().getRGB() : fill.getFillColor().getRGB()));
            }

            if (symbols[i] instanceof ITextSymbol) {
            	ITextSymbol text = (ITextSymbol) symbols[i];
            	text.setTextColor(new Color(random.nextInt()>>8));
                assertTrue("Symbol no. "+i+" '"+symbols[i].getClassName()+"' RGB value mismatch for getOnePointRBG() and getColor().getRGB()",
                        symbols[i].getOnePointRgb() == text.getTextColor().getRGB());
            }

        }
    }

    /**
     * ensures that any symbol provides a version of itself to use when the
     * feature is selected
     *
     */
    public void testSymbolForSelection() {
    	for (int i = 0; i < symbols.length; i++) {
			assertNotNull("Symbol no. "+i+" '"+symbols[i].getClassName()+" does not define any derived symbol for selection", symbols[i].getSymbolForSelection());
		}
    }
}
