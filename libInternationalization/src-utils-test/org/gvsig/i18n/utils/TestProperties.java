package org.gvsig.i18n.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import junit.framework.TestCase;

/**
 * @author cesar
 *
 */
public class TestProperties extends TestCase {
	static final String fileName = "TestProperties-file";
	InputStream is;
	OutputStream os;
	
	public void initOutputStream() {
		try {
			os = new FileOutputStream(fileName);
		} catch (FileNotFoundException e) {
			TestCase.fail(e.getLocalizedMessage());
		}
	}
	
	public void discardOutputStream() {
		try {
			os.close();
			File file = new File(fileName);
			file.delete();
		} catch (IOException e) {
			TestCase.fail(e.getLocalizedMessage());
		}
	}
	
	public void initInputStream() {
		try {
			is = new FileInputStream(fileName);
		} catch (FileNotFoundException e) {
			TestCase.fail(e.getLocalizedMessage());
		}
	}
	
	public void discardInputStream() {
		try {
			is.close();
		} catch (IOException e) {
			TestCase.fail(e.getLocalizedMessage());
		}
	}
	
	public void testOrderedPropertiesBasic() {
		// write file
		OrderedProperties props1 = new OrderedProperties();
		props1.setProperty("prueba", "hola");
		props1.setProperty("prueba2", "holas");
		props1.setProperty("abrir", "cerrar");
		props1.setProperty("puerta", "hembrilla");
		props1.setProperty("12libros", "quijote");
		props1.setProperty("cañuto", "\u20acuropeo");
		props1.setProperty("confli=cto", "amigo");
		props1.setProperty("  confli =  :cto mayor ", " pequeño conflicto");
		initOutputStream();
		try {
			props1.store(os, "header");
		} catch (IOException e) {
			TestCase.fail(e.getLocalizedMessage());
		}
		
		// read file
		OrderedProperties props2 = new OrderedProperties();
		initInputStream();
		try {
			props2.load(is);
		} catch (IOException e) {
			TestCase.fail(e.getLocalizedMessage());
		}
		TestCase.assertEquals("\u20acuropeo", props2.getProperty("cañuto"));
		TestCase.assertEquals("amigo", props2.getProperty("confli=cto"));
		TestCase.assertEquals(" pequeño conflicto", props2.getProperty("  confli =  :cto mayor "));
		TestCase.assertEquals("hola", props2.getProperty("prueba"));
		TestCase.assertEquals("holas", props2.getProperty("prueba2"));
		TestCase.assertEquals("cerrar", props2.getProperty("abrir"));
		TestCase.assertEquals("hembrilla", props2.getProperty("puerta"));
		TestCase.assertEquals("quijote", props2.getProperty("12libros"));
		discardInputStream();
		discardOutputStream();
	}

	
	public void testOrderedProperties() {
		// write file
		OrderedProperties props1 = new OrderedProperties();
		props1.setProperty("prueba", "hola");
		props1.setProperty("prueba2", "holas");
		props1.setProperty("cañuto", "\u20acuropeo");
		props1.setProperty("confli=cto", "amigo");
		props1.setProperty("  conflis =  :cto mayor ", " pequeño conflicto");
		props1.setProperty("abrir", "cerrar");
		props1.setProperty("puerta", "hembrilla");
		props1.setProperty("12libros", "quijote");
		initOutputStream();
		try {
			props1.store(os, "header", "8859_15");
		} catch (IOException e) {
			TestCase.fail(e.getLocalizedMessage());
		}
		
		// read file
		OrderedProperties props2 = new OrderedProperties();
		initInputStream();
		try {
			props2.load(is, "8859_15");
		} catch (IOException e) {
			TestCase.fail(e.getLocalizedMessage());
		}
		// estos deberían salir igual
		TestCase.assertEquals("hola", props2.getProperty("prueba"));
		TestCase.assertEquals("holas", props2.getProperty("prueba2"));
		TestCase.assertEquals("cerrar", props2.getProperty("abrir"));
		TestCase.assertEquals("hembrilla", props2.getProperty("puerta"));
		TestCase.assertEquals("quijote", props2.getProperty("12libros"));
		// con estos en cambio no obtenemos los mismos valores que escribimos anteriormente. Es normal, porque incluimos
		// caracteres especiales como ":" y "=". Para manejarlos correctamente necesitamos usas los métodos load/store
		// sin encoding, los cuales sí escapan los caracteres especiales
		TestCase.assertEquals("\u20acuropeo", props2.getProperty("cañuto"));
		TestCase.assertEquals("cto=amigo", props2.getProperty("confli"));
		TestCase.assertEquals(":cto mayor = pequeño conflicto", props2.getProperty("conflis"));

		discardInputStream();
		discardOutputStream();
		
		// write file
		props1 = new OrderedProperties();
		props1.setProperty("Prueba", "hola");
		props1.setProperty("prueba2", "holas");
		props1.setProperty("cañuto", "\u20acuropeo");
		props1.setProperty("confli=cto", "amigo");
		props1.setProperty("  conflis =  :cto mayor ", " pequeño conflicto");
		props1.setProperty("abrir", "cerrar");
		props1.setProperty("puerta", "hembrilla");
		props1.setProperty("12libros", "quijote");
		initOutputStream();
		try {
			props1.store(os, "header", "UTF8");
		} catch (IOException e) {
			TestCase.fail(e.getLocalizedMessage());
		}
		
		// read file
		props2 = new OrderedProperties();
		initInputStream();
		try {
			props2.load(is, "UTF8");
		} catch (IOException e) {
			TestCase.fail(e.getLocalizedMessage());
		}
		// estos deberían salir igual
		TestCase.assertEquals("hola", props2.getProperty("Prueba"));
		TestCase.assertEquals("holas", props2.getProperty("prueba2"));
		TestCase.assertEquals("cerrar", props2.getProperty("abrir"));
		TestCase.assertEquals("hembrilla", props2.getProperty("puerta"));
		TestCase.assertEquals("quijote", props2.getProperty("12libros"));
		// con estos en cambio no obtenemos los mismos valores que escribimos anteriormente. Es normal, porque incluimos
		// caracteres especiales como ":" y "=". Para manejarlos correctamente necesitamos usas los métodos load/store
		// sin encoding, los cuales sí escapan los caracteres especiales
		TestCase.assertEquals("\u20acuropeo", props2.getProperty("cañuto"));
		TestCase.assertEquals("cto=amigo", props2.getProperty("confli"));
		TestCase.assertEquals(":cto mayor = pequeño conflicto", props2.getProperty("conflis"));

		discardInputStream();
		discardOutputStream();
	}
	
}
