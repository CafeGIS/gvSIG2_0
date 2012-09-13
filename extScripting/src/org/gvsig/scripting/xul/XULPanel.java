package org.gvsig.scripting.xul;

import java.awt.BorderLayout;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.swing.JPanel;

import thinlet.Thinlet;

import com.iver.andami.PluginServices;

public class XULPanel extends  Thinlet implements IXULPanel  {

	private static final long serialVersionUID = -7549083932449351947L;
	
	private XULJPanel panel =null;
	
	private String xulFile = null;
	
	private Map data = new HashMap();
		
	
	public XULPanel() {
		MyResources r = new MyResources(this.getClass());
		setResourceBundle(r);
		panel = new XULJPanel(this);
	}
	
	public XULPanel(XULJPanel panel) {
		MyResources r = new MyResources(this.getClass());
		setResourceBundle(r);
		this.panel = panel;
	}
	
	public String translate(String msg) {
		return PluginServices.getText(this,msg);
	}
	

    public Object parse(String path, Object handler) throws IOException {
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
            return super.parse(inputstream,handler);
        } finally{ 
            if(inputstream != null) try{inputstream.close();}catch(IOException ignore){}
        }
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
	
	public Object createComponent(String name) {
		return create(name);
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
	
	public int getSize(Object component) {
		return getCount(component);
	}
	
}

class MyResources extends ResourceBundle {
	
	Class theClass = null;
	
	public MyResources(Class theClass) {
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