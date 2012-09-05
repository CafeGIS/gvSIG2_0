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
package org.gvsig.fmap.mapcontext.rendering;

import java.awt.Dimension;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Random;

import junit.framework.TestCase;

import org.gvsig.compat.CompatLocator;
import org.gvsig.fmap.AllTests;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.fmap.geom.Geometry.SUBTYPES;
import org.gvsig.fmap.geom.exception.CreateEnvelopeException;
import org.gvsig.fmap.geom.exception.CreateGeometryException;
import org.gvsig.fmap.geom.primitive.Envelope;
import org.gvsig.fmap.geom.primitive.Point;
import org.gvsig.fmap.geom.util.UtilFunctions;
import org.gvsig.fmap.mapcontext.MapContext;
import org.gvsig.fmap.mapcontext.ViewPort;
import org.gvsig.fmap.mapcontext.rendering.symbol.AbstractSymbolTestCase;
import org.gvsig.fmap.mapcontext.rendering.symbol.SimpleFillSymbolTest;
import org.gvsig.fmap.mapcontext.rendering.symbol.SimpleLineSymbolTest;
import org.gvsig.fmap.mapcontext.rendering.symbol.SimpleMarkerSymbolTest;
import org.gvsig.fmap.mapcontext.rendering.symbols.CartographicSupport;
import org.gvsig.fmap.mapcontext.rendering.symbols.IFillSymbol;
import org.gvsig.fmap.mapcontext.rendering.symbols.ILineSymbol;
import org.gvsig.fmap.mapcontext.rendering.symbols.IMarkerSymbol;
import org.gvsig.fmap.mapcontext.rendering.symbols.ISymbol;
import org.gvsig.fmap.mapcontext.rendering.symbols.SymbologyFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iver.utiles.IPersistence;
import com.iver.utiles.NotExistInXMLEntity;
import com.iver.utiles.XMLEntity;
import com.iver.utiles.XMLException;

/**
 *
 * @author jaume dominguez faus - jaume.dominguez@iver.es
 */
public class TestCartographicSupportForSymbol extends TestCase {
	private static final GeometryManager geomManager = GeometryLocator.getGeometryManager();
	private static final Logger logger = LoggerFactory.getLogger(TestCartographicSupportForSymbol.class);
	
	/**
	 * Geodesic vertex of the Micalet in Valencia (Spain) in <b>EPSG:23030</b>
	 */
	public static Point valenciaUTM30 = null;

	/**
	 * Geodesic vertex of the Micalet in Valencia (Spain) in <b>EPSG:4326</b>
	 */
	public static Point valenciaGeo = null;
	/**
	 * Extent that covers the whole Comunitat Valenciana in  <b>EPSG:23030</b>
	 */
	public static final Rectangle2D TEST_UTM30_EXTENT = new Rectangle2D.Double();
	{
		TEST_UTM30_EXTENT.setFrameFromDiagonal(
				4191037.369934333,
				626674.7454557443,

				4519266.460824658,
				797903.2656794232
		);
	}
	/**
	 * Extent that covers the whole Comunitat Valenciana in  <b>EPSG:4326</b>
	 */

	public static final Rectangle2D TEST_GEO_EXTENT = new Rectangle2D.Double();
	{
		TEST_GEO_EXTENT.setFrameFromDiagonal(

				-  (1 + (31/60) + (28.09/3600)),	// Western egde
				37 + (50/60) + (48.05/3600),		// Southern edge

				0 + (31/60) + (1.85/3600),		// Eastern edge
				40 + (47/60) + (21.36/3600)		// Northern edge

		);
	}

	public static final Rectangle2D TEST_EXTENT = new Rectangle2D.Double(
			4191037.369934333,
			626674.7454557443,

			4519266.460824658 - 4191037.369934333,
			797903.2656794232 -  626674.7454557443
	);
	private static ArrayList symbolsToTest; //<AbstractSymbolTestCase>
	private ArrayList csSymbols; //<CartographicSupport>

	/**
	 * Acceptable pixel tolerance error. 1 pixel of error is accetable since it is probably due to
	 * the java.awt.Graphics
	 */
	private static final double TOL = 1;

	double zooms[] = new double[] {
			1,
			2,
			1.2,
			10,
			0.5
	};



	public void setUp() {
		try {
			valenciaUTM30 = geomManager.createPoint(725846.080, 4373022.720, SUBTYPES.GEOM2D);
			valenciaGeo = geomManager.createPoint(- (0+ 22/60 +  27.919/3600), 39 + 28/60 + 35.4276/3600, SUBTYPES.GEOM2D);
		} catch (CreateGeometryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	
		addSymbolTest(new SimpleFillSymbolTest());
		addSymbolTest(new SimpleLineSymbolTest());
		addSymbolTest(new SimpleMarkerSymbolTest());

		init();
	}

	protected void init() {

		// take the symbols added to the TestISymbol test
		ISymbol[] symbols = getNewSymbolInstances();

		csSymbols = new ArrayList(); //<CartographicSupport>
		for (int i = 0; i < symbols.length; i++) {
			if (symbols[i] instanceof CartographicSupport) {
				csSymbols.add((CartographicSupport) symbols[i]);
			}
		}
	}

	public static void addSymbolTest(AbstractSymbolTestCase symbolTestClass) {
		if (symbolsToTest == null) symbolsToTest = new ArrayList(); //<AbstractSymbolTestCase>
		symbolsToTest.add(symbolTestClass);
	}

	public static ISymbol[] getNewSymbolInstances() {
		ISymbol[] symbols = new ISymbol[symbolsToTest.size()];
		for (int i = 0; i < symbols.length; i++) {
			symbols[i] = (ISymbol)((AbstractSymbolTestCase)symbolsToTest.get(i)).newInstance();
		}
		return symbols;
	}


	public void testSymbolUnitDefinition() {
		for (int i = 0; i < csSymbols.size(); i++) {
			CartographicSupport symbol = (CartographicSupport)csSymbols.get(i);

			int aRandomUnit = new Random().nextInt(7);


			symbol.setUnit(aRandomUnit);
			XMLEntity xml=null;
			try {
				xml = ((IPersistence) symbol).getXMLEntity();
			} catch (XMLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String name = symbol.getClass().getName().substring(
					symbol.getClass().getName().lastIndexOf('.')+1,
					symbol.getClass().getName().length());

			try {
				assertTrue( ((ISymbol) symbol).getClassName()+" does not declare units correctly",
						symbol.getUnit() == xml.getIntProperty("unit"));
			} catch (NotExistInXMLEntity neiXMLEx) {
				fail(((ISymbol) symbol).getClassName()+ " does not declare field attribute in its XMLEntity");
			}
			symbol.setUnit(5);
			try {
				assertTrue( ((ISymbol) symbol).getClassName()+" does not apply changes to symbol",symbol.getUnit()== 5);
				assertTrue( ((ISymbol) symbol).getClassName()+" does not declare units correctly",
						5 == ((IPersistence) symbol).getXMLEntity().getIntProperty("unit"));
			} catch (NotExistInXMLEntity neiXMLEx) {
				fail(((ISymbol) symbol).getClassName()+ " does not declare field attribute in its XMLEntity");
			} catch (XMLException e) {
				fail(((ISymbol) symbol).getClassName()+ " does not declare field attribute in its XMLEntity");
			}

			xml.putProperty("unit", 3);
			ISymbol ts = SymbologyFactory.createSymbolFromXML(xml, xml.getStringProperty("desc"));
			assertTrue("The application of the UNIT value to the XMLEntity didn't have any effect ("+name+")", ((CartographicSupport) ts).getUnit() == 3);
		}

	}


	public void testSymbolReferenceSystemDefinition() {
		for (int i = 0; i < csSymbols.size(); i++) {
			CartographicSupport symbol = (CartographicSupport)csSymbols.get(i);
			XMLEntity xml=null;
			try {
				xml = ((IPersistence) symbol).getXMLEntity();
			} catch (XMLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String name = symbol.getClass().getName().substring(
					symbol.getClass().getName().lastIndexOf('.')+1,
					symbol.getClass().getName().length());
			try {
				assertTrue( ((ISymbol) symbol).getClassName()+" does not declare units correctly",
						symbol.getReferenceSystem() == xml.getIntProperty("referenceSystem"));
			} catch (NotExistInXMLEntity neiXMLEx) {
				fail(((ISymbol) symbol).getClassName()+ " does not declare referenceSystem field attribute in its XMLEntity");
			}
			symbol.setReferenceSystem(CartographicSupport.PAPER);
			try {
				assertTrue( ((ISymbol) symbol).getClassName()+" does not apply changes to symbol",symbol.getReferenceSystem()== CartographicSupport.PAPER);
				assertTrue( ((ISymbol) symbol).getClassName()+" does not declare referenceSystem correctly",
						CartographicSupport.PAPER == ((IPersistence) symbol).getXMLEntity().getIntProperty("referenceSystem"));
			} catch (NotExistInXMLEntity neiXMLEx) {
				fail(((ISymbol) symbol).getClassName()+ " does not declare referenceSystem field attribute in its XMLEntity ("+name+")");
			} catch (XMLException e) {
				fail(((ISymbol) symbol).getClassName()+ " does not declare referenceSystem field attribute in its XMLEntity ("+name+")");
			}

			symbol.setReferenceSystem(CartographicSupport.WORLD);
			try {
				assertTrue( ((ISymbol) symbol).getClassName()+" does not apply changes to symbol",symbol.getReferenceSystem()== CartographicSupport.WORLD);
				assertTrue( ((ISymbol) symbol).getClassName()+" does not declare referenceSystem correctly",
						CartographicSupport.WORLD == ((IPersistence) symbol).getXMLEntity().getIntProperty("referenceSystem"));
			} catch (NotExistInXMLEntity neiXMLEx) {
				fail(((ISymbol) symbol).getClassName()+ " does not declare referenceSystem field attribute in its XMLEntity");
			} catch (XMLException e) {
				fail(((ISymbol) symbol).getClassName()+ " does not declare referenceSystem field attribute in its XMLEntity");
			}

			xml.putProperty("referenceSystem", CartographicSupport.PAPER);
			ISymbol ts = SymbologyFactory.createSymbolFromXML(xml, xml.getStringProperty("desc"));
			assertTrue("The application of the REFERENCE SYSTEM value to the XMLEntity didn't have any effect ("+name+")", ((CartographicSupport) ts).getReferenceSystem() == CartographicSupport.PAPER);
		}
	}


	public void testDegreesUnits_MapReferenceSystem() throws CreateGeometryException, CreateEnvelopeException {
		for (int j = 0; j < csSymbols.size(); j++) {
			CartographicSupport symbol = (CartographicSupport)csSymbols.get(j);

			MapContext mc = AllTests.newMapContext(AllTests.TEST_DEFAULT_PROJECTION);
			ViewPort viewPort = mc.getViewPort();

			int unit = 8;
			Dimension dim = new Dimension(600, 600);
			viewPort.setImageSize(dim);


			/*
			 * will test with several extents but in the same map image size, so
			 * the scale issue is exercised as well.
			 */
			for (int i = 0; i < zooms.length; i++) {
				Envelope extent = geomManager.createEnvelope(-30, -30,dim.getWidth()/zooms[i]-30, dim.getHeight()/zooms[i]-30, SUBTYPES.GEOM2D);

				viewPort.setMapUnits(unit);
				viewPort.setEnvelope(extent);


				String name = ((IPersistence) symbol).getClassName().substring(
						((IPersistence) symbol).getClassName().lastIndexOf('.')+1,
						((IPersistence) symbol).getClassName().length());

				CartographicSupport csSym = (CartographicSupport) symbol;
				csSym.setReferenceSystem(CartographicSupport.WORLD);
				double size = 30;

				csSym.setUnit(unit);



				if (symbol instanceof IMarkerSymbol) {

					IMarkerSymbol markerSym = (IMarkerSymbol) symbol;
					markerSym.setSize(size);
					csSym.toCartographicSize(
							viewPort,
							CompatLocator.getGraphicsUtils().getScreenDPI(),
							geomManager.createPoint(
									viewPort.getAdjustedExtent().getCenter(0),
									viewPort.getAdjustedExtent().getCenter(1),
									SUBTYPES.GEOM2D
							)
					);
					double mySize = markerSym.getSize();
					assertTrue("The Symbol '"+name+"' failed computing cartographic size. " +
							"It returned "+markerSym.getSize()+" when expecting "+
							size+"*"+zooms[i]+"="+size*zooms[i],  Math.abs(mySize-size*zooms[i]) <= TOL );
				}

				if (symbol instanceof ILineSymbol) {

					ILineSymbol lineSym = (ILineSymbol) symbol;
					lineSym.setLineWidth(size);
					csSym.toCartographicSize(
							viewPort,
							CompatLocator.getGraphicsUtils().getScreenDPI(),
							geomManager.createPoint(
									viewPort.getAdjustedExtent().getCenter(0),
									viewPort.getAdjustedExtent().getCenter(1),
									SUBTYPES.GEOM2D
							)
					);
					double mySize = lineSym.getLineStyle().getLineWidth();
					assertTrue("The Symbol '"+name+"' failed computing cartographic size. " +
							"It returned "+mySize+" when expecting "+
							size+"*"+zooms[i]+"="+size*zooms[i], Math.abs(mySize-size*zooms[i]) <= TOL );
				}

				if (symbol instanceof IFillSymbol) {
					// this tests if symbol applies the cartographic support to its
					// outline, since is the only thing that may be resized
					IFillSymbol fillSym = (IFillSymbol) symbol;
					if (fillSym.getOutline() == null) {
						fillSym.setOutline(SymbologyFactory.createDefaultLineSymbol());
					}

					fillSym.getOutline().setLineWidth(size);

					csSym.toCartographicSize(
							viewPort,
							CompatLocator.getGraphicsUtils().getScreenDPI(),
							geomManager.createPoint(
									viewPort.getAdjustedExtent().getCenter(0),
									viewPort.getAdjustedExtent().getCenter(1),
									SUBTYPES.GEOM2D
							)
					);
					double mySize = fillSym.getOutline().getLineStyle().getLineWidth();
					assertTrue("The Symbol '"+name+"' failed computing cartographic size. " +
							"It returned "+fillSym.getOutline().getLineWidth()+" when expecting "+
							size+"*"+zooms[i]+"="+size*zooms[i], Math.abs(mySize-size*zooms[i]) <= TOL );
				}

			}
		}
	}


	public void testKilometersUnits_MapReferenceSystem() throws CreateGeometryException, CreateEnvelopeException {
		for (int j = 0; j < csSymbols.size(); j++) {
			CartographicSupport symbol = (CartographicSupport)csSymbols.get(j);
			MapContext mc = AllTests.newMapContext(AllTests.TEST_DEFAULT_MERCATOR_PROJECTION);
			ViewPort viewPort = mc.getViewPort();

			int unit = 0;
			Dimension dim = new Dimension(60, 60);
			viewPort.setImageSize(dim);


			/*
			 * will test with several extents but in the same map image size, so
			 * the scale issue is exercised as well.
			 */
			for (int i = 0; i < zooms.length; i++) {
				Envelope extent = geomManager.createEnvelope(-30, -30, dim.getWidth()/zooms[i]-30, dim.getHeight()/zooms[i]-30, SUBTYPES.GEOM2D);

				viewPort.setMapUnits(unit);
				viewPort.setEnvelope(extent);


				String name = ((IPersistence) symbol).getClassName().substring(
						((IPersistence) symbol).getClassName().lastIndexOf('.')+1,
						((IPersistence) symbol).getClassName().length());

				CartographicSupport csSym = (CartographicSupport) symbol;
				csSym.setReferenceSystem(CartographicSupport.WORLD);
				double size = 30;

				csSym.setUnit(unit);




				if (symbol instanceof IMarkerSymbol) {

					IMarkerSymbol markerSym = (IMarkerSymbol) symbol;
					markerSym.setSize(size);
					csSym.toCartographicSize(
							viewPort,
							CompatLocator.getGraphicsUtils().getScreenDPI(),
							geomManager.createPoint(
									viewPort.getAdjustedExtent().getCenter(0),
									viewPort.getAdjustedExtent().getCenter(1),
									SUBTYPES.GEOM2D
							)
					);
					double mySize = markerSym.getSize();
					assertTrue("The Symbol '"+name+"' failed computing cartographic size. " +
							"It returned "+markerSym.getSize()+" when expecting "+
							size+"*"+zooms[i]+"="+size*zooms[i],  Math.abs(mySize-size*zooms[i]) <= TOL );
				}

				if (symbol instanceof ILineSymbol) {

					ILineSymbol lineSym = (ILineSymbol) symbol;
					lineSym.setLineWidth(size);
					csSym.toCartographicSize(
							viewPort,
							CompatLocator.getGraphicsUtils().getScreenDPI(),
							geomManager.createPoint(
									viewPort.getAdjustedExtent().getCenter(0),
									viewPort.getAdjustedExtent().getCenter(1),
									SUBTYPES.GEOM2D								
							)
					);
					double mySize = lineSym.getLineStyle().getLineWidth();
					assertTrue("The Symbol '"+name+"' failed computing cartographic size. " +
							"It returned "+lineSym.getLineWidth()+" when expecting "+
							size+"*"+zooms[i]+"="+size*zooms[i], Math.abs(mySize-size*zooms[i]) <= TOL );
				}

				if (symbol instanceof IFillSymbol) {
					// this tests if symbol applies the cartographic support to its
					// outline, since is the only thing that may be resized
					IFillSymbol fillSym = (IFillSymbol) symbol;
					if (fillSym.getOutline() == null) {
						fillSym.setOutline(SymbologyFactory.createDefaultLineSymbol());
					}

					fillSym.getOutline().setLineWidth(size);

					csSym.toCartographicSize(
							viewPort,
							CompatLocator.getGraphicsUtils().getScreenDPI(),
							geomManager.createPoint(
									viewPort.getAdjustedExtent().getCenter(0),
									viewPort.getAdjustedExtent().getCenter(1),
									SUBTYPES.GEOM2D
							)
					);
					double mySize = fillSym.getOutline().getLineStyle().getLineWidth();
					assertTrue("The Symbol '"+name+"' failed computing cartographic size. " +
							"It returned "+fillSym.getOutline().getLineWidth()+" when expecting "+
							size+"*"+zooms[i]+"="+size*zooms[i], Math.abs(mySize-size*zooms[i]) <= TOL );
				}

			}
		}

	}

	public void testMapInKilometersSymbolInMeters_MapReferenceSystem() throws CreateGeometryException, CreateEnvelopeException {
		for (int j = 0; j < csSymbols.size(); j++) {
			CartographicSupport symbol = (CartographicSupport)csSymbols.get(j);

			MapContext mc = AllTests.newMapContext(AllTests.TEST_DEFAULT_MERCATOR_PROJECTION);
			ViewPort viewPort = mc.getViewPort();

			int mapUnit = 0;
			int symbolUnit = 1;
			Dimension dim = new Dimension(60, 60);
			viewPort.setImageSize(dim);


			/*
			 * will test with several extents but in the same map image size, so
			 * the scale issue is exercised as well.
			 */
			for (int i = 0; i < zooms.length; i++) {
				Envelope extent = geomManager.createEnvelope(-30, -30, dim.getWidth()/zooms[i]-30, dim.getHeight()/zooms[i]-30, SUBTYPES.GEOM2D);
				viewPort.setMapUnits(mapUnit);
				viewPort.setEnvelope(extent);
				double size = 30;


				String name = ((IPersistence) symbol).getClassName().substring(
						((IPersistence) symbol).getClassName().lastIndexOf('.')+1,
						((IPersistence) symbol).getClassName().length());


				CartographicSupport csSym = (CartographicSupport) symbol;
				csSym.setReferenceSystem(CartographicSupport.WORLD);

				csSym.setUnit(symbolUnit);

				if (symbol instanceof IMarkerSymbol) {

					IMarkerSymbol markerSym = (IMarkerSymbol) symbol;
					markerSym.setSize(size*MapContext.CHANGEM[mapUnit]); // 30m*1000 = 30 km
					csSym.toCartographicSize(
							viewPort,
							CompatLocator.getGraphicsUtils().getScreenDPI(),
							geomManager.createPoint(
									viewPort.getAdjustedExtent().getCenter(0),
									viewPort.getAdjustedExtent().getCenter(1),
									SUBTYPES.GEOM2D
							)
					);
					double mySize = markerSym.getSize();
					assertTrue("The Symbol '"+name+"' failed computing cartographic size. " +
							"It returned "+markerSym.getSize()+" when expecting "+
							size+"*"+zooms[i]+"="+size*zooms[i],  Math.abs(mySize-size*zooms[i]) <= TOL );
				}

				if (symbol instanceof ILineSymbol) {

					ILineSymbol lineSym = (ILineSymbol) symbol;
					lineSym.setLineWidth(size*MapContext.CHANGEM[mapUnit]); // 30m*1000 = 30 km
					csSym.toCartographicSize(
							viewPort,
							CompatLocator.getGraphicsUtils().getScreenDPI(),
							geomManager.createPoint(
									viewPort.getAdjustedExtent().getCenter(0),
									viewPort.getAdjustedExtent().getCenter(1),
									SUBTYPES.GEOM2D
							)
					);
					double mySize = lineSym.getLineStyle().getLineWidth();
					assertTrue("The Symbol '"+name+"' failed computing cartographic size. " +
							"It returned "+lineSym.getLineWidth()+" when expecting "+
							size+"*"+zooms[i]+"="+size*zooms[i], Math.abs(mySize-size*zooms[i]) <= TOL );
				}

				if (symbol instanceof IFillSymbol) {
					// this tests if symbol applies the cartographic support to its
					// outline, since is the only thing that may be resized
					IFillSymbol fillSym = (IFillSymbol) symbol;
					if (fillSym.getOutline() == null) {
						fillSym.setOutline(SymbologyFactory.createDefaultLineSymbol());
					}

					fillSym.getOutline().setLineWidth(size*MapContext.CHANGEM[mapUnit]);

					csSym.toCartographicSize(
							viewPort,
							CompatLocator.getGraphicsUtils().getScreenDPI(),
							geomManager.createPoint(
									viewPort.getAdjustedExtent().getCenter(0),
									viewPort.getAdjustedExtent().getCenter(1),
									SUBTYPES.GEOM2D
							)
					);
					double mySize = fillSym.getOutline().getLineStyle().getLineWidth();
					assertTrue("The Symbol '"+name+"' failed computing cartographic size. " +
							"It returned "+fillSym.getOutline().getLineWidth()+" when expecting "+
							size+"*"+zooms[i]+"="+size*zooms[i], Math.abs(mySize-size*zooms[i]) <= TOL );
				}

			}
		}
	}

}
