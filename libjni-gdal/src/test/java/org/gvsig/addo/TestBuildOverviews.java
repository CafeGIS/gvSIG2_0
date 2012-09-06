package org.gvsig.addo;

import java.io.IOException;

import junit.framework.TestCase;
import es.gva.cit.jgdal.Gdal;
import es.gva.cit.jgdal.GdalException;

/**
 * Test para la generacion de overviews sobre una imagen raster.
 * Registra un listener para mostrar el incremento de la tarea.
 *
 * 18-nov-2007
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class TestBuildOverviews extends TestCase implements IOverviewIncrement {
	private int value = 0;

	private Jaddo addo = null;
	private Gdal gdal = null;
	private String path = "./test-images/testGdalWarp.tif";
	
	public void start(){
		setUp();
		testStack();
	}
	
	
	public void setUp(){
		addo = new Jaddo();
		addo.setIncrementListener(this);
		gdal = new Gdal();
	}
	
	
	public void testStack(){
		try {
			addo.buildOverviews(Jaddo.AVERAGE, path, new int[]{2, 4, 8, 16});
			gdal.open(path, Gdal.GA_ReadOnly);
			assertTrue("No hay overviews!!", gdal.getRasterBand(1).getOverviewCount()>0);
		} catch (BuildingOverviewsException e) {
			System.err.println(e);
		} catch (WritingException e) {
			System.err.println(e);
		} catch (GdalException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public int getPercent() {
		return value;
	}

	public void setPercent(int value) {
		this.value = value;
		System.out.println("Increment:" + value);
	}
}