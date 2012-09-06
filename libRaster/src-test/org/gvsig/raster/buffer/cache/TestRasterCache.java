package org.gvsig.raster.buffer.cache;

import junit.framework.TestCase;

import org.gvsig.raster.RasterLibrary;
import org.gvsig.raster.buffer.BufferFactory;
import org.gvsig.raster.dataset.IBuffer;
import org.gvsig.raster.dataset.InvalidSetViewException;
import org.gvsig.raster.dataset.NotSupportedExtensionException;
import org.gvsig.raster.dataset.RasterDataset;
import org.gvsig.raster.dataset.RasterDriverException;
/**
 * Test de comparativa de datos cacheados con datos sin cachear. 
 * Leemos un raster 1..n veces cacheandolo por páginas. Una vez cacheado recorreremos
 * la cache recuperando los datos y comparandolos con los originales para comprobar que no
 * ha habido perdida y que lo que se obtiene es igual a lo inicial.
 * 
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class TestRasterCache extends TestCase{
	private String        baseDir = "./test-images/";
	private String        path    = baseDir + "03AUG23153350-M2AS-000000122423_01_P001-BROWSE.jpg";
	private RasterDataset f       = null;

	static {
		RasterLibrary.wakeUp();
	}

	public void start() {
		this.setUp();
		this.testStack();
	}

	public void setUp() {
		System.err.println("TestRasterCache running...");
		try {
			f = RasterDataset.open(null, path);
		} catch (NotSupportedExtensionException e) {
			e.printStackTrace();
		} catch (RasterDriverException e) {
			e.printStackTrace();
		}
	}
	
	public void testStack() {
		int[] drawableBands = { 0, 1, 2 };
		//Número de veces que cacheamos la imagen. Esto hace que el número de líneas totales se multiplique por este número
		int nTimes = 1;
		BufferFactory ds = new BufferFactory(f);
		ds.setDrawableBands(drawableBands);
		try {
			ds.setAreaOfInterest(0, 0, f.getWidth(), f.getHeight());
		} catch (InvalidSetViewException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (RasterDriverException e) {
			e.printStackTrace();
		}

		// Salvamos los PageBuffer
		IBuffer buf = ds.getRasterBuf();
		
		//Reducimos el tamaño de la caché para poder trabajar con un volumen de datos reducido
		long cacheSize = RasterLibrary.cacheSize;
		double pageSize = RasterLibrary.pageSize;
		RasterLibrary.cacheSize = 1;
		RasterLibrary.pageSize = 0.2;

		RasterCache rc = new RasterCache(f.getDataType()[0], f.getWidth(), f.getHeight() * nTimes, 3);
//		rc.getCache().getCacheStruct().show();

		// Cargamos la caché
		byte[] data = new byte[3];
		for (int i = 0; i < nTimes; i++) {
			for (int line = 0; line < f.getHeight(); line++) {
				for (int col = 0; col < f.getWidth(); col++) {
					buf.getElemByte(line, col, data);
					rc.setElemByte(line + (f.getHeight() * i), col, data);
				}
			}
		}

		// Obtenemos los datos y comparamos
		byte[] dataA = new byte[3];
		byte[] dataB = new byte[3];
		for (int i = 0; i < nTimes; i++) {
			for (int line = 0; line < f.getHeight(); line++) {
				for (int col = 0; col < f.getWidth(); col++) {
					buf.getElemByte(line, col, dataA);
					rc.getElemByte(line + (f.getHeight() * i), col, dataB);
					assertEquals(dataA[0], dataB[0]);
					assertEquals(dataA[1], dataB[1]);
					assertEquals(dataA[2], dataB[2]);
				}
			}
		}
		RasterLibrary.cacheSize = cacheSize;
		RasterLibrary.pageSize = pageSize;
	}
}
