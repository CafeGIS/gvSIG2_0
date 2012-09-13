package org.gvsig.remotesensing.featherprocess;

import java.io.File;

import org.gvsig.fmap.mapcontext.exceptions.LoadLayerException;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.remotesensing.BaseTestCase;
import org.gvsig.raster.Configuration;
import org.gvsig.raster.RasterLibrary;
import org.gvsig.remotesensing.mosaic.process.FeatherProcess;

public class FeatherProcessTest extends BaseTestCase {

	private String baseDir = "./test-images/";

	static{
		RasterLibrary.wakeUp();
	}

	protected void setUp(){
	}

	public void start() {
		this.setUp();
		this.testStack();
	}

	public void testStack() {
		try {
			FLyrRasterSE.setConfiguration(Configuration.getSingleton());
			FLyrRasterSE lyr1 = FLyrRasterSE.createLayer("m1", baseDir + "mosaic_test1.tif", null);
			FLyrRasterSE lyr2 = FLyrRasterSE.createLayer("m2", baseDir + "mosaic_test2.tif", null);
			//FLyrRasterSE lyr1 = FLyrRasterSE.createLayer("m1", "/home/dguerrero/datos/Raster/mosaico/Cn_ccolor_l5_20033_20070624/cn_ccolor_i5_20033_20070624_subset.img", null);
			//FLyrRasterSE lyr2 = FLyrRasterSE.createLayer("m2", "/home/dguerrero/datos/Raster/mosaico/Cn_ccolor_l5_19933_20070703/cn_ccolor_i5_19933_20070703_subset.img", null);
			FLyrRasterSE[] layers = new FLyrRasterSE[2];
			layers[0]=lyr1;
			layers[1]=lyr2;
			FeatherProcess featherProcess = new FeatherProcess();
			featherProcess.addParam("inputRasterLayers", layers);
			featherProcess.addParam("filename", tempDir + File.separator+"mosaicResult.tif");
			featherProcess.init();
			featherProcess.process();

		} catch (LoadLayerException e) {
			e.printStackTrace();
			assertFalse(true);
		} catch (InterruptedException e) {
			e.printStackTrace();
			assertFalse(true);
		}
	}

}
