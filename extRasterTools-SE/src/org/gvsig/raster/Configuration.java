/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2005 IVER T.I. and Generalitat Valenciana.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,USA.
 */
package org.gvsig.raster;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.gvsig.fmap.raster.layers.IConfiguration;

import com.iver.andami.PluginServices;
import com.iver.utiles.XMLEntity;
/**
 * La clase <code>Configuration</code> sirve para poder leer y escribir valores en el entorno
 * de raster a nivel de configuración. Para leer o escribir hay que usar los
 * metodos getValue y setValue, estos metodos lanzan eventos en el caso de
 * cambiar el valor que habia establecido. Forma de uso:<p>
 *
 * En la lectura es recomendable pasar un valor por defecto en el get, para que
 * si no existe o si existe pero no corresponde el tipo de datos devolvera el
 * valor por defecto<p>
 *
 * <code>Boolean valor = Configuration.getValue("valorBooleano", Boolean.valueOf(true));</code><p>
 *
 * <code>Configuration.setValue("valorBooleano", Boolean.valueOf(false));</code><p>
 *
 * Solo se pueden usar los siguientes tipos de datos:<br>
 *  - <b>Boolean</b>, <b>Double</b>, <b>Float</b>, <b>Integer</b>, <b>Long</b>
 *  y <b>String</b>.<p>
 *
 * Otra funcionalidad que tiene, es que puedes agregar un manejador de eventos
 * para controlar los cambios de las variables y actuar en consecuencia si cambia
 * la que deseas.
 *
 * @version 07/12/2007
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
public class Configuration implements IConfiguration {
	static private Configuration singleton              = new Configuration();
	private ArrayList            actionCommandListeners = new ArrayList();
	private XMLEntity            xml                    = null;
	private HashMap              hashMap                = new HashMap();

	/**
	 * Constructor privado. Nos aseguramos de que nadie pueda crear una instancia
	 * desde fuera, la configuración es única para todos.
	 */
	private Configuration() {
		try {
			PluginServices ps = PluginServices.getPluginServices("org.gvsig.rastertools");
			xml = ps.getPersistentXML();
		} catch (NullPointerException e) {
			//No está inicializado Configuration
			xml = new XMLEntity();
		}
	}

	/**
	 * Devuelve un valor Boolean para el key especificado
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	static public Boolean getValue(String key, Boolean defaultValue) {
		singleton.saveDefaultValue(key, defaultValue);
		try {
			return Boolean.valueOf(getXMLEntity().getStringProperty(key));
		} catch (Exception e) {
		}
		try {
			getXMLEntity().putProperty(key, defaultValue.booleanValue());
		} catch(NullPointerException e) {
			//No está inicializada la configuración. Devuelve el default
		}
		return defaultValue;
	}

	/**
	 * Devuelve un valor Double para el key especificado
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	static public Double getValue(String key, Double defaultValue) {
		singleton.saveDefaultValue(key, defaultValue);
		try {
			return Double.valueOf(getXMLEntity().getStringProperty(key));
		} catch (Exception e) {
		}
		getXMLEntity().putProperty(key, defaultValue.doubleValue());
		return defaultValue;
	}

	/**
	 * Devuelve un valor Float para el key especificado
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	static public Float getValue(String key, Float defaultValue) {
		singleton.saveDefaultValue(key, defaultValue);
		try {
			return Float.valueOf(getXMLEntity().getStringProperty(key));
		} catch (Exception e) {
		}
		getXMLEntity().putProperty(key, defaultValue.floatValue());
		return defaultValue;
	}

	/**
	 * Devuelve un valor Integer para el key especificado
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	static public Integer getValue(String key, Integer defaultValue) {
		singleton.saveDefaultValue(key, defaultValue);
		try {
			return Integer.valueOf(getXMLEntity().getStringProperty(key));
		} catch (Exception e) {
		}
		getXMLEntity().putProperty(key, defaultValue.intValue());
		return defaultValue;
	}

	/**
	 * Devuelve un valor Long para el key especificado
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	static public Long getValue(String key, Long defaultValue) {
		singleton.saveDefaultValue(key, defaultValue);
		try {
			return Long.valueOf(getXMLEntity().getStringProperty(key));
		} catch (Exception e) {
		}
		getXMLEntity().putProperty(key, defaultValue.longValue());
		return defaultValue;
	}

	/**
	 * Devuelve un valor String para el key especificado
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	static public String getValue(String key, String defaultValue) {
		singleton.saveDefaultValue(key, defaultValue);
		try {
			return getXMLEntity().getStringProperty(key);
		} catch (Exception e) {
		}
		getXMLEntity().putProperty(key, defaultValue);
		return defaultValue;
	}

	/**
	 * Guarda el valor por defecto en caso de que no exista
	 * @param key
	 * @param defaultValue
	 */
	private void saveDefaultValue(String key, Object defaultValue) {
		if (hashMap.get(key) == null)
			hashMap.put(key, defaultValue);
	}

	/**
	 * Devuelve el valor por defecto de un key
	 * @param key
	 * @return
	 */
	static public Object getDefaultValue(String key) {
		return singleton.hashMap.get(key);
	}

	/**
	 * Guarda en la configuracion el Objeto pasado por parametro asociado a dicho
	 * key
	 * @param key
	 * @param value
	 */
	private void putProperty(String key, Object value) {
		if (Integer.class.isInstance(value)) {
			getXMLEntity().putProperty(key, ((Integer) value).intValue());
			return;
		}
		if (Double.class.isInstance(value)) {
			getXMLEntity().putProperty(key, ((Double) value).doubleValue());
			return;
		}
		if (Float.class.isInstance(value)) {
			getXMLEntity().putProperty(key, ((Float) value).floatValue());
			return;
		}
		if (Boolean.class.isInstance(value)) {
			getXMLEntity().putProperty(key, ((Boolean) value).booleanValue());
			return;
		}
		if (Long.class.isInstance(value)) {
			getXMLEntity().putProperty(key, ((Long) value).longValue());
			return;
		}
		if (String.class.isInstance(value)) {
			getXMLEntity().putProperty(key, (String) value);
			return;
		}
		getXMLEntity().putProperty(key, value);
	}

	/**
	 * Establece un valor en la configuracion
	 * @param name
	 * @param value
	 */
	static public void setValue(String key, Object value) {
		if (value == null) {
			getXMLEntity().remove(key);
			singleton.callConfigurationChanged(key, value);
			return;
		}

		String oldValue = getValue(key, value.toString());

		singleton.putProperty(key, value);

		if (!oldValue.equals(value.toString()))
			singleton.callConfigurationChanged(key, value);
	}

	/**
	 * Añadir un listener a la lista de eventos
	 * @param listener
	 */
	static public void addValueChangedListener(ConfigurationListener listener) {
		if (!singleton.actionCommandListeners.contains(listener))
			singleton.actionCommandListeners.add(listener);
	}

	/**
	 * Borrar un listener de la lista de eventos
	 * @param listener
	 */
	static public void removeValueChangedListener(ConfigurationListener listener) {
		singleton.actionCommandListeners.remove(listener);
	}

	/**
	 * Invocar a los eventos asociados al componente
	 */
	private void callConfigurationChanged(String key, Object value) {
		Iterator iterator = actionCommandListeners.iterator();
		while (iterator.hasNext()) {
			ConfigurationListener listener = (ConfigurationListener) iterator.next();
			listener.actionConfigurationChanged(new ConfigurationEvent(this, key, value));
		}
	}

	/**
	 * Devuelve una instancia unica al XMLEntity de Configuration
	 * @return
	 */
	static private XMLEntity getXMLEntity() {
		return singleton.xml;
	}
	
	/**
	 * Devuelve una instancia al unico objeto de configuración que puede existir.
	 * @return
	 */
	static public Configuration getSingleton() {
		return singleton;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.fmap.raster.conf.IConfiguration#getValueBoolean(java.lang.String, java.lang.Boolean)
	 */
	public Boolean getValueBoolean(String name, Boolean defaultValue) {
		return Configuration.getValue(name, defaultValue);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.fmap.raster.conf.IConfiguration#getValueString(java.lang.String, java.lang.String)
	 */
	public String getValueString(String name, String defaultValue) {
		return Configuration.getValue(name, defaultValue);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.fmap.raster.conf.IConfiguration#getValueDouble(java.lang.String, java.lang.Double)
	 */
	public Double getValueDouble(String name, Double defaultValue) {
		return Configuration.getValue(name, defaultValue);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.fmap.raster.conf.IConfiguration#getValueFloat(java.lang.String, java.lang.Float)
	 */
	public Float getValueFloat(String name, Float defaultValue) {
		return Configuration.getValue(name, defaultValue);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.fmap.raster.conf.IConfiguration#getValueInteger(java.lang.String, java.lang.Integer)
	 */
	public Integer getValueInteger(String name, Integer defaultValue) {
		return Configuration.getValue(name, defaultValue);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.fmap.raster.conf.IConfiguration#getValueLong(java.lang.String, java.lang.Long)
	 */
	public Long getValueLong(String name, Long defaultValue) {
		return Configuration.getValue(name, defaultValue);
	}
}