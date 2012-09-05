/**
 * 
 */
package org.gvsig.i18n;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Locale;

import junit.framework.TestCase;

/**
 * @author cesar
 *
 */
public class TestMessages extends TestCase {

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
	}

	/*
	 * @see TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testSetPreferredLocales() {
		setPreferredLocales();
		removeLocales();
	}
	
	private void setPreferredLocales() {
		ArrayList preferred = new ArrayList();
		preferred.add(new Locale("en"));
		preferred.add(new Locale("es"));
		Messages.setPreferredLocales(preferred);
		ArrayList resultPref = Messages.getPreferredLocales();
		
		TestCase.assertEquals(resultPref.size(), 2);
		
		Locale lang1 = (Locale) resultPref.get(0);		
		TestCase.assertEquals(lang1.getLanguage(), "en");
		Locale lang2 = (Locale) resultPref.get(1);		
		TestCase.assertEquals(lang2.getLanguage(), "es");
	}

	private void removeLocales() {	
		Locale lang1 = new Locale("en");
		Locale lang2 = new Locale("es");	
		ArrayList resultPref = Messages.getPreferredLocales();
		Messages.removeLocale(lang1);
		TestCase.assertEquals(resultPref.size(), 1);
		Messages.removeLocale(lang2);		
		TestCase.assertEquals(resultPref.size(), 0);
	}
	
	public void testAddResourceFamily() {
		setPreferredLocales();
		//this.getClass().getClassLoader().getResource("text_en.properties");

		try {
			Messages.addResourceFamily("text", new File("src-test/org/gvsig/i18n/dataset1/"));
		} catch (MalformedURLException e) {
			TestCase.fail("Fichero de recursos no encontrado");
		}
		TestCase.assertEquals(5, Messages.size(new Locale("en")));
		TestCase.assertEquals(4,Messages.size(new Locale("es")));
		TestCase.assertEquals(0,Messages.size(new Locale("fr")));
		
		TestCase.assertEquals("OK", Messages.getText("aceptar"));
		TestCase.assertEquals("Cancel", Messages.getText("cancelar"));
		TestCase.assertEquals("Cascade", Messages.getText("Cascada"));
		TestCase.assertEquals("Window", Messages.getText("ventana"));
		TestCase.assertEquals("Debe haber al menos una ventana abierta", Messages.getText("cascada_enable"));
		TestCase.assertEquals("Configurar", Messages.getText("Configurar"));
		TestCase.assertEquals(null, Messages.getText("Configurar", false));

		// load another file now
		try {
			Messages.addResourceFamily("text", new File(
                    "src-test/org/gvsig/i18n/dataset2/"));
		} catch (MalformedURLException e) {
			TestCase.fail("Fichero de recursos no encontrado");
		}
		// check that the right amount of translations was loaded
		TestCase.assertEquals(7, Messages.size(new Locale("en")));
		TestCase.assertEquals(6, Messages.size(new Locale("es")));
		TestCase.assertEquals(0, Messages.size(new Locale("fr")));

		// test that keys didn't get overwritten by the new files
		// (only new keys should have been added for each language)
		TestCase.assertEquals("OK", Messages.getText("aceptar"));
		TestCase.assertEquals("Cancel", Messages.getText("cancelar"));
		TestCase.assertEquals("Cascade", Messages.getText("Cascada"));
		// check the new keys
		TestCase.assertEquals("At least one window should be open", Messages.getText("cascada_enable"));
		TestCase.assertEquals("Insert first corner point", Messages.getText("insert_first_point_corner"));
		TestCase.assertEquals("Capa exportada", Messages.getText("capa_exportada"));
		TestCase.assertEquals("Circunscrito", Messages.getText("circumscribed"));
		
		Messages.removeResources();
		TestCase.assertEquals(0, Messages.size(new Locale("en")));
		TestCase.assertEquals(0, Messages.size(new Locale("es")));
		TestCase.assertEquals(0, Messages.size(new Locale("fr")));
		
		removeLocales();
	}
}
