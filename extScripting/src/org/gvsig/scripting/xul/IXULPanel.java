package org.gvsig.scripting.xul;

import java.io.IOException;

import javax.swing.JPanel;

public interface IXULPanel {

	public void setXULFile (String xulFile) throws IOException;
	
	public String getXULFile();

	public JPanel getJPanel();

	public Object getEngine();
	
	public void close();

	public void showWindow();

	public void setTitle(String title);
	
	public void setPalette(boolean state);

	public Object get(Object key);

	public Object put(Object key, Object value);
	
	public Object find(String name);
	
	public Object createComponent(String name);
	
	public String getString(Object component, String propertyName);
	
	public int getInteger(Object component, String propertyName);
	
	public boolean getBoolean(Object component, String propertyName);
	
	public Object getItem(Object component, int index);
	
	public int getSize(Object component);
	
	public void setString(Object component, String propertyName, String value);
	
	public void setInteger(Object component, String propertyName, int value);
	
	public void setBoolean(Object component, String propertyName, boolean value);
		
}
