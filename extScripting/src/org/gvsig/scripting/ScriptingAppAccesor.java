package org.gvsig.scripting;

import java.io.File;
import java.util.Hashtable;
import java.util.Map;

import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.extensionpoint.ExtensionPointManager;

import com.iver.andami.PluginServices;

public class ScriptingAppAccesor {
	private static String scriptsDirectory = null;
	private static ExtensionPointManager extensionPoints =ToolsLocator.getExtensionPointManager();


	public ExtensionPointManager getExtensionPointManager() {
		return extensionPoints;
	}

	public Object getActiveDocument() {
	  return PluginServices.getMDIManager().getActiveWindow();
	}

	public String getScriptsDirectory() {
		if (scriptsDirectory == null) {
			File file = new File(PluginServices.getPluginServices("org.gvsig.scripting").getClassLoader().getBaseDir(),"scripts");
			scriptsDirectory = file.getAbsolutePath();
		}
		return scriptsDirectory;
	}


	public String translate(String str) {
		return PluginServices.getText(this,str);
	}

	public Object show(Map params) {
		return ScriptingExtension.show(params);

	}

	public Object show(String fileName,String language) {
		Hashtable params = new Hashtable();
		params.put("fileName",fileName);
		params.put("language",language);
		return ScriptingExtension.show(params);
	}

	public Object show(String fileName,String language, String title) {
		Hashtable params = new Hashtable();
		params.put("fileName",fileName);
		params.put("language",language);
		params.put("title",title);
		return ScriptingExtension.show(params);
	}


	public Object show(String fileName,String language,Map aParams) {
		Hashtable params = new Hashtable();
		params.putAll(aParams);
		params.put("fileName",fileName);
		params.put("language",language);
		return ScriptingExtension.show(params);
	}

	public Object show(String fileName,String language, String title, Map aParams) {
		Hashtable params = new Hashtable();
		params.putAll(aParams);
		params.put("fileName",fileName);
		params.put("language",language);
		params.put("title",title);
		return ScriptingExtension.show(params);
	}

	public Object show(String fileName,String language,int width, int height) {
		Hashtable params = new Hashtable();
		params.put("fileName",fileName);
		params.put("language",language);
		params.put("width",new Integer(width));
		params.put("height",new Integer(height));
		return ScriptingExtension.show(params);
	}

	public Object show(String fileName,String language, String title, int width, int height) {
		Hashtable params = new Hashtable();
		params.put("fileName",fileName);
		params.put("language",language);
		params.put("width",new Integer(width));
		params.put("height",new Integer(height));
		params.put("title",title);
		return ScriptingExtension.show(params);
	}


	public Object show(String fileName,String language,int width, int height, Map aParams) {
		Hashtable params = new Hashtable();
		params.putAll(aParams);
		params.put("fileName",fileName);
		params.put("language",language);
		params.put("width",new Integer(width));
		params.put("height",new Integer(height));
		return ScriptingExtension.show(params);
	}

	public Object show(String fileName,String language, String title, int width, int height, Map aParams) {
		Hashtable params = new Hashtable();
		params.putAll(aParams);
		params.put("fileName",fileName);
		params.put("language",language);
		params.put("width",new Integer(width));
		params.put("height",new Integer(height));
		params.put("title",title);
		return ScriptingExtension.show(params);
	}


	public void run(Map params) {
		ScriptingExtension.run(params);
	}

	public void run(String fileName, String language) {
		Hashtable params = new Hashtable();
		params.put("fileName",fileName);
		params.put("language",language);
		ScriptingExtension.run(params);
	}

	public void run(String fileName, String language,Map aParams) {
		Hashtable params = new Hashtable();
		params.putAll(aParams);
		params.put("fileName",fileName);
		params.put("language",language);
		ScriptingExtension.run(params);
	}

	public Class classForName(String className) throws ClassNotFoundException {
		return Class.forName(className);
	}
}
