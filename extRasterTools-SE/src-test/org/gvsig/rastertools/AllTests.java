package org.gvsig.rastertools;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.gvsig.fmap.raster.grid.roi.TestRoiStatistics;
import org.gvsig.rastertools.clipping.EcwClippingProcessTest;
import org.gvsig.rastertools.clipping.InterpolateClippingProcessTest;
import org.gvsig.rastertools.clipping.MrsidClippingProcessTest;
import org.gvsig.rastertools.clipping.TifByteClippingProcessTest;
import org.gvsig.rastertools.clipping.TifFloatClippingProcessTest;
import org.gvsig.rastertools.filters.FilterProcessTest;
import org.gvsig.rastertools.histogram.HistogramProcessTest;
/**
 * Parámetro para la máquina virtual para la ejecución de los test
 * -Xss1024k -Xmn200M -Xmx500M -Djava.library.path=ruta
 */
public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.gvsig.rastertools");
		//$JUnit-BEGIN$
		suite.addTestSuite(TestRoiStatistics.class);
//		suite.addTestSuite(TestRoiStatisticAdvanced.class);
		suite.addTestSuite(EcwClippingProcessTest.class);
		suite.addTestSuite(MrsidClippingProcessTest.class);
		suite.addTestSuite(TifByteClippingProcessTest.class);
		suite.addTestSuite(TifFloatClippingProcessTest.class);
		suite.addTestSuite(InterpolateClippingProcessTest.class);
		suite.addTestSuite(HistogramProcessTest.class);
		suite.addTestSuite(FilterProcessTest.class);
		//$JUnit-END$
		return suite;
	}
}