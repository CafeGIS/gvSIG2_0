package org.gvsig.fmap;

import java.io.File;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.cresques.cts.IProjection;
import org.gvsig.fmap.crs.CRSFactory;
import org.gvsig.fmap.mapcontext.MapContext;
import org.gvsig.fmap.mapcontext.ViewPort;
import org.gvsig.fmap.mapcontext.exceptions.LoadLayerException;
import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.mapcontext.rendering.TestAbstractIntervalLegend;
import org.gvsig.fmap.mapcontext.rendering.TestCartographicSupportForSymbol;
import org.gvsig.fmap.mapcontext.rendering.TestIClassifiedLegend;
import org.gvsig.fmap.mapcontext.rendering.TestILegend;
import org.gvsig.fmap.mapcontext.rendering.symbol.TestDrawFills;
import org.gvsig.fmap.mapcontext.rendering.symbol.TestDrawLines;
import org.gvsig.fmap.mapcontext.rendering.symbol.TestDrawMarkers;
import org.gvsig.fmap.mapcontext.rendering.symbol.TestISymbol;
import org.gvsig.fmap.mapcontext.rendering.symbol.TestMultiLayerSymbol;

public class AllTests extends TestCase{
	/**
	 * The EPSG:4326 projection
	 */
	public static IProjection TEST_DEFAULT_PROJECTION =
		CRSFactory.getCRS("EPSG:4326");

	/**
	 * The EPSG:23030 projection
	 */
	public static IProjection TEST_DEFAULT_MERCATOR_PROJECTION =
		CRSFactory.getCRS("EPSG:23030");

	/**
	 * The EPSG:23029 projection
	 */
	public static IProjection test_newProjection =
		CRSFactory.getCRS("EPSG:23029");

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for com.iver.cit.gvsig.fmap");
		//$JUnit-BEGIN$



		/* Symbols (jaume) */
			// integration tests
				// ISymbol
				suite.addTestSuite(TestISymbol.class);
				suite.addTestSuite(TestMultiLayerSymbol.class);

				// CartographicSupport
				suite.addTestSuite(TestCartographicSupportForSymbol.class);

		//
		/*
		 * Feature iterators
		 * */
//		suite.addTestSuite(FeatureIteratorTest.class);

		/*
		 * Other Tests present in FMap (cesar)
		 * Remove them from here and the src-test dir if they are not
		 * useful anymore.
		 */

		suite.addTestSuite(TestAbstractIntervalLegend.class);
		suite.addTestSuite(TestCartographicSupportForSymbol.class);
		suite.addTestSuite(TestDrawFills.class);
		suite.addTestSuite(TestDrawLines.class);
		suite.addTestSuite(TestDrawMarkers.class);
		suite.addTestSuite(TestIClassifiedLegend.class);
		suite.addTestSuite(TestILegend.class);
		suite.addTestSuite(TestMultiLayerSymbol.class);

		//$JUnit-END$
		return suite;
	}

//// jaume
//// PASTED FROM FeatureIteratorTest.java to be globally accessible
	static final String fwAndamiDriverPath = "../_fwAndami/gvSIG/extensiones/com.iver.cit.gvsig/drivers";
	private static File baseDataPath;
	private static File baseDriversPath;


	public static void setUpDrivers() {
//		try {
//			URL url = AllTests.class.getResource("testdata");
//			if (url == null)
//				throw new Exception("No se encuentra el directorio con datos de prueba");
//
//			baseDataPath = new File(url.getFile());
//			if (!baseDataPath.exists())
//				throw new Exception("No se encuentra el directorio con datos de prueba");
//
//			baseDriversPath = new File(fwAndamiDriverPath);
//			if (!baseDriversPath.exists())
//				throw new Exception("Can't find drivers path: " + fwAndamiDriverPath);
//
//			LayerFactory.setDriversPath(baseDriversPath.getAbsolutePath());
//			if (LayerFactory.getDM().getDriverNames().length < 1)
//				throw new Exception("Can't find drivers in path: " + fwAndamiDriverPath);
//		} catch (Exception e) {
//			throw new RuntimeException(e);
//		}
	}

	public static FLayer newLayer(String fileName,
			String driverName)
	throws LoadLayerException {
//		File file = new File(baseDataPath, fileName);
//		return LayerFactory.createLayer(fileName,
//				driverName,
//				file, TEST_DEFAULT_MERCATOR_PROJECTION);
	return null;
	}

	public static MapContext newMapContext(IProjection projection) {
		ViewPort vp = new ViewPort(projection);
		return new MapContext(vp);
	}

//// end past
}