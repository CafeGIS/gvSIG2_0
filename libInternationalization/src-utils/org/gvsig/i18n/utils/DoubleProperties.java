/**
 * 
 */
package org.gvsig.i18n.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * The DoubleProperties class represents a set of properties. It provides the
 * same functionality as its parent class, Properties. Besides that, it also
 * provides an efficient method to get the key associated with a value.  
 * 
 * @author cesar
 *
 */
public class DoubleProperties extends OrderedProperties {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1738114064256800193L;
	HashMap reverseMap = new HashMap();

	public DoubleProperties() {
		super();
	}
	

	public DoubleProperties(OrderedProperties defaults) {
		super(defaults);
		Iterator keysIterator = this.keySet().iterator();
		ArrayList keySet;
		
		String key, value;
		while (keysIterator.hasNext()) {
			key = (String) keysIterator.next();
			value = this.getProperty(key);
			if (reverseMap.containsKey(value)) {
				keySet = (ArrayList) reverseMap.get(value);
				keySet.add(key);
			}
			else {
				keySet = new ArrayList();
				keySet.add(key);
				reverseMap.put(value, keySet);
			}
		}
	}
	
	
	public void load(InputStream stream) throws IOException {
		super.load(stream);

		Iterator keysIterator = this.keySet().iterator();
		ArrayList keySet;
		
		String key, value;
		while (keysIterator.hasNext()) {
			key = (String) keysIterator.next();
			value = this.getProperty(key);
			if (reverseMap.containsKey(value)) {
				keySet = (ArrayList) reverseMap.get(value);
				keySet.add(key);
			}
			else {
				keySet = new ArrayList();
				keySet.add(key);
				reverseMap.put(value, keySet);
			}
		}
	}
	
	public Object setProperty(String key, String value) {
		ArrayList keySet;
		
		Object returnValue = super.setProperty(key, value);
		if (reverseMap.containsKey(value)) {
			keySet = (ArrayList) reverseMap.get(value);
			keySet.add(key);
		}
		else {
			keySet = new ArrayList();
			keySet.add(key);
			reverseMap.put(value, keySet);
		}
		
		return returnValue;
	}
	
	/**
	 * Gets the key associated with the provided value. If there
	 * are several associated keys, returns one of them.
	 * 
	 * @param value
	 * @return The key associated with the provided value, or null
	 * if the value is not present in the dictionary. If there
	 * are several associated keys, returns one of them.
	 */
	public String getAssociatedKey(String value) {
		ArrayList keySet = (ArrayList) reverseMap.get(value);
		if (keySet==null) return null;
		return (String) keySet.get(0);
	}
	
	/**
	 * Returns the keys associated with the provided value. If there
	 * are several associated keys, returns one of them.
	 * 
	 * @param value
	 * @return An ArrayList containing the keys associated with the
	 * provided value, or null if the value is not present in the 
	 * dictionary.
	 */
	public ArrayList getAssociatedKeys(String value) {
		return (ArrayList) reverseMap.get(value);
	}
	
	public Object remove(Object key) {
		Object value = super.remove(key);
		if (value==null) return null;
		ArrayList keySet = (ArrayList) reverseMap.get(value);
		if (keySet==null) return null;
		if (keySet.size()<=1) {
			//if it's the last key associated wit the value, remove the
			// value from the reverseDictionary			
			reverseMap.remove(value);
		}
		else {
			// otherwise, remove the key from the list of associated keys
			keySet.remove(key);
		}
		return value;
	}	
}
