package org.gvsig.i18n.utils;

import java.io.File;
import java.io.IOException;

import junit.framework.TestCase;

/**
 * @author cesar
 *
 */
public class TestUtils extends TestCase {
	
	public ConfigOptions loadConfig() {
		ConfigOptions config = new ConfigOptions();
		config.setConfigFile("src-test"+File.separator+"org"+File.separator+"gvsig"+File.separator+"i18n"+File.separator+"utils"+File.separator+"config.xml");
		config.load();
		return config;
	}
	
	public void testConfigOptions() {
		ConfigOptions config = loadConfig();
		
		String databaseDir=null, basedir=null;
		try {
			databaseDir = new File("test-data/database").getCanonicalPath();
			basedir =  new File("test-data").getCanonicalPath();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// the values of the projects. They are pairs (dir, basename).
		String projectValues[]={"src/appgvSIG", "otro", "sources", "src/_fwAndami", "text", "sources"};
		for (int i=0; i<projectValues.length; i=i+3) {
			try {
				// set the canonical path
				projectValues[i]= new File(basedir+File.separator+projectValues[i]).getCanonicalPath();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		TestCase.assertEquals(databaseDir, config.databaseDir);
		TestCase.assertEquals(basedir, config.defaultBaseDir);
		TestCase.assertEquals("text", config.defaultBaseName);
		
		String languages = config.languages[0];
		for (int i=1; i<config.languages.length; i++) {
			languages=languages+";"+config.languages[i];
		}
		TestCase.assertEquals("ca;cs;de;en;es;eu;fr;gl;it;pt", languages);
		
		for (int i=0; i<config.projects.size(); i=i+3) {
			Project project = (Project) config.projects.get(i);
			TestCase.assertEquals(projectValues[i], project.dir);
			TestCase.assertEquals(projectValues[i+1], project.basename);
			TestCase.assertEquals(projectValues[i+2], project.sourceKeys);
		}
	}
	
	public void testTranslationDatabase() {
		ConfigOptions config = loadConfig();
		TranslationDatabase database = new TranslationDatabase(config);
		database.load();
		TestCase.assertEquals("Añadir Capa" , database.getTranslation("es", "Anadir_Capa"));
		TestCase.assertEquals("Add Layer" , database.getTranslation("en", "Anadir_Capa"));
		TestCase.assertEquals("Afegir Capa" , database.getTranslation("ca", "Anadir_Capa"));
		TestCase.assertEquals("Layer hinzufügen" , database.getTranslation("de", "Anadir_Capa"));
		TestCase.assertEquals("Ayuda" , database.getTranslation("es", "Ayuda"));
		TestCase.assertEquals(null , database.getTranslation("es", "test_case.clave_que_no_existe"));
		
		// add a translation
		database.setTranslation("es", "test_case.clave_que_no_existe", "Clave que no existe");
		// is it present now?
		TestCase.assertEquals("Clave que no existe" , database.getTranslation("es", "test_case.clave_que_no_existe"));
		
		// remove the translation
		database.removeTranslation("es", "test_case.clave_que_no_existe");
		// was really removed?
		TestCase.assertEquals(null , database.getTranslation("es", "test_case.clave_que_no_existe"));
		
		// add the translation again
		database.setTranslation("es", "test_case.clave_que_no_existe", "Clave que no existe");
		// save to disk
		database.save();
		// load from disk
		database.load();
		// is the translation still there?
		TestCase.assertEquals("Clave que no existe" , database.getTranslation("es", "test_case.clave_que_no_existe"));
		
		// remove the translation again and save to disk
		database.removeTranslation("es", "test_case.clave_que_no_existe");
		TestCase.assertEquals(null , database.getTranslation("es", "test_case.clave_que_no_existe"));
		TestCase.assertEquals("", database.getTranslation("es", "prueba"));
		database.save();
	}

	public void testKeys() {
		ConfigOptions config = loadConfig();
		Keys keys = new Keys(config);
		keys.load();
	}

}
