package es.gva.cit.jgdal;

import java.io.IOException;

import junit.framework.TestCase;

/**
 * Test de acceso a datos de la imagen. 
 * @author Miguel Ángel Qierol Carratalá <miguelangel.querol@iver.es>
 *
 */
public class TestReadData extends TestCase{

	private Gdal gdal = null;
	private String baseDir = "./test-images/";
	private String file1 = baseDir + "testGdal.tif";
	private String[] metadata = null;
	
	public void start(){
		try {
			setUp();
			testStack();
		} catch (GdalException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void setUp() throws GdalException, IOException{
		gdal = new Gdal();
		gdal.open(file1, Gdal.GA_Update);
	}
	
	public void testStack() throws GdalException, IOException{
		//Llamada sin dominio
		metadata = gdal.getMetadata();
		assertNotNull("No se han devuelto metadatos", metadata);
		for (int i = 0 ; i<metadata.length ; i++){
			System.out.println("Metadato: " + metadata[i]);
		}
		
		//Llamada con dominio "Image Structure Metadata"
		metadata = gdal.getMetadata("Image Structure Metadata");
		assertNotNull("No se han devuelto metadatos", metadata);
		for (int i = 0 ; i<metadata.length ; i++){
			System.out.println("Metadato: " + metadata[i]);
		}
		
		//Comprobación del tamaño de la imagen
		assertEquals(842, gdal.getRasterXSize());
		assertEquals(1023, gdal.getRasterYSize());
		
		//Comprobación del número de bandas
		assertEquals(4, gdal.getRasterCount());
		
		//Comprobación del driver de la imagen
		assertEquals("GTiff", gdal.getDriverShortName());
		
		//Comprobación del acceso a las bandas
		for (int i = 0 ; i < gdal.getRasterCount() ; i++){
			assertNotNull(gdal.getRasterBand(i+1));
		}
		
		gdal.close();
		gdal = null;
		System.gc();
	}
}
