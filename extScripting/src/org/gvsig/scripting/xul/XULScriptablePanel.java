package org.gvsig.scripting.xul;


import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.swing.JPanel;

import org.apache.bsf.BSFEngine;
import org.apache.bsf.BSFException;
import org.apache.bsf.BSFManager;
import org.gvsig.scripting.ScriptingExtension;

import thinlet.script.ScriptableThinlet;

import com.iver.andami.PluginServices;

public class XULScriptablePanel extends  ScriptableThinlet implements IXULScriptablePanel  {
	
	private static final long serialVersionUID = -8034625039942158574L;
	
	public static final String JAVASCRIPT = ScriptableThinlet.JAVASCRIPT;
    public static final String BEANSHELL = ScriptableThinlet.BEANSHELL;
    public static final String JYTHON = ScriptableThinlet.JYTHON;
    public static final String GROOVY = ScriptableThinlet.GROOVY;

	private XULScriptableJPanel panel =null;
	private String xulFile = null;
	private Map data = new HashMap();
	
	
	public static void setBSFManager(BSFManager bsfManager) {
		try {
			fbsfManager = bsfManager;
			XULScriptablePanel thinlet;
			thinlet = new XULScriptablePanel();
			thinlet.runStartupScripJython();
		} catch (BSFException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public XULScriptablePanel() throws BSFException{
		super(JYTHON);
		MyResources2 r = new MyResources2(this.getClass());
		setResourceBundle(r);
        panel = new XULScriptableJPanel(this);
	}
	
	public XULScriptablePanel(XULScriptableJPanel panel)throws BSFException {
		super(JYTHON);
		MyResources2 r = new MyResources2(this.getClass());
		setResourceBundle(r);
		this.panel = panel;
	}
	
    public Object parse(String path, Object handler) throws IOException {
        ffilename = path;
        InputStream inputstream = null;
        try {
            inputstream = getClass().getResourceAsStream(path);
            if (inputstream == null) {
            	
                try {
                    inputstream = new URL(path).openStream();
                } catch (MalformedURLException mfe) {
                	try {
	                	File file = new File(path);
	                	inputstream = new FileInputStream(file);
                	} catch (Exception e) {
                		/* thows nullpointerexception*/
            			e.printStackTrace();
                	}                	 
                }
                
            }
            fin = new LineNumberReader(new BufferedReader(new InputStreamReader(inputstream)));
            fin.setLineNumber(1);
            Object ret = parse(fin, true, false, handler);
            return ret;
        } finally{ 
            fin = null;
            if(inputstream != null) try{inputstream.close();}catch(IOException ignore){}
        }
    }

	private void runStartupScripJython() {
		BSFEngine bsfEngine;
		try {
			bsfEngine = fbsfManager.loadScriptingEngine(JYTHON);
			String startUpFileName = ScriptingExtension.getScriptingAppAccesor().getScriptsDirectory();
			startUpFileName = startUpFileName + File.separatorChar+ "jython"+ File.separatorChar+"startup.py";
			File startupFile = new File(startUpFileName);
			
			if (!startupFile.exists()) {
				//throw new IOException("Startup File "+startUpFileName+" not found");
				return;
			}
			String startupCode = readFile(startupFile);
			bsfEngine.exec(startUpFileName, -1, -1, startupCode);
		} catch (BSFException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private String readFile(File aFile) throws IOException {
		StringBuffer fileContents = new StringBuffer();
		FileReader fileReader = new FileReader(aFile);
		int c;
		while ((c = fileReader.read()) > -1) {
			fileContents.append((char)c);
		}
		fileReader.close();
		return fileContents.toString();
	}

	//==============================================================
	// Implementacion del interface IXULScriptablePanel
	//
	public void invokeMethod(Object component, String method)  {
		invoke(component,null,method);
	}
	
	public XULScriptablePanel createPanel() throws BSFException {
		XULScriptablePanel thinlet = new XULScriptablePanel();
		return thinlet;
	}
	
	//==============================================================
	// Implementacion del interface IXULPanel
	//
	public void setXULFile (String xulFile) throws IOException {
		try {
			this.xulFile = xulFile;
			add(parse(xulFile));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
		panel.setLayout(new BorderLayout());
		panel.add(this,BorderLayout.CENTER);
	}
	
	public String getXULFile() {
		return xulFile;
	}

	public JPanel getJPanel() {
		return panel;
	}

	public Object getEngine() {
		return this;
	}
	
	public void close(){
		panel.close();
	}

	public void showWindow(){
		panel.showWindow();
	}

	public void setTitle(String title) {
		panel.setTitle(title);
	}
	
	public void setPalette(boolean state) {
		panel.setPalette(state);
	}

	public Object get(Object key) {
		return data.get(key);
	}

	public Object put(Object key, Object value) {
		return data.put(key,value);
	}

	public Object createComponent(String name) {
		return create(name);
	}

	public int getSize(Object component) {
		return getCount(component);
	}	

	public String translate(String msg) {
		return PluginServices.getText(this,msg);
	}
	

}

class MyResources2 extends ResourceBundle {
	
	Class theClass = null;
	
	public MyResources2(Class theClass) {
		this.theClass = theClass;
	}
	
    public Object handleGetObject(String key) {
    	String msg = PluginServices.getText(theClass,key);
        return msg;
    }

	public Enumeration getKeys() {
		return null;
	}
}