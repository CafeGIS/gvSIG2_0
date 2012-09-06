package org.gvsig.raster.buffer.cache;

import junit.framework.TestCase;

import org.gvsig.raster.RasterLibrary;
import org.gvsig.raster.dataset.NotSupportedExtensionException;
import org.gvsig.raster.dataset.RasterDataset;
import org.gvsig.raster.dataset.RasterDriverException;


/**
 * Calculo que las dimensiones de página para la cache de solo lectura. 
 * Se creará un objeto caché y se comprobará que los trozos calculados con la llamada 
 * calcExtentPages son del tamaño correcto.
 * 
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class TestWorldCoordHDDPages extends TestCase {
	private String baseDir = "./test-images/";
	private String path = baseDir + "03AUG23153350-M2AS-000000122423_01_P001-BROWSE.jpg";
	private RasterDataset f = null;
			
	static{
		RasterLibrary.wakeUp();
	}
	
	public void start() {
		this.setUp();
		this.testStack();
	}
	
	public void setUp() {
		System.err.println("TestWorldCoordHDDPages running...");
		try {
			f = RasterDataset.open(null, path);
		} catch (NotSupportedExtensionException e) {
			e.printStackTrace();
		} catch (RasterDriverException e) {
			e.printStackTrace();
		}
		f.getWidth();
	}
	
	public void testStack() {
		//TODO: TEST: Acabar TestPagesWC
	}
}
