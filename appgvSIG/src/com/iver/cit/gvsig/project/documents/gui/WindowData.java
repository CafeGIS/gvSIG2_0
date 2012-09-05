/**
 * 
 */
package com.iver.cit.gvsig.project.documents.gui;

import java.util.HashMap;
import java.util.Iterator;

import com.iver.utiles.XMLEntity;

/**
 * @author cesar
 *
 */
public class WindowData {
	
	HashMap data = null;
	
	public WindowData() {
		data = new HashMap();
	}

	public void set(String key, String value) {
		data.put(key, value);
	}
	
	public String get(String key) {
		return (String) data.get(key);
	}
	
	public XMLEntity getXMLEntity() {
		XMLEntity xml = new XMLEntity();
		xml.setName("windowData");
		Iterator keyList = data.keySet().iterator();
		while (keyList.hasNext()) {
			String key = (String) keyList.next();
			xml.putProperty(key, get(key), false);
		}		
		return xml;
	}

	public void setXMLEntity(XMLEntity xml) {
		for (int i=xml.getPropertyCount()-1; i>0; i--) {
			data.put(xml.getPropertyName(i), xml.getPropertyValue(i));
		}
	}
	
}
