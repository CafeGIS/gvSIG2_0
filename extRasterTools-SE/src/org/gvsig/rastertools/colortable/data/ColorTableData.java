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
package org.gvsig.rastertools.colortable.data;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

import org.gvsig.raster.datastruct.ColorTable;
import org.gvsig.raster.util.PropertyEvent;
import org.gvsig.raster.util.PropertyListener;
/**
 * Clase para intentar centralizar los datos del panel de Color. La idea es que
 * cada subpanel solo tenga acceso a este centro de datos y desde aqui se lancen
 * eventos
 * 
 * @version 27/09/2007
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
public class ColorTableData {
	private ArrayList  actionCommandListeners = new ArrayList();
	private Hashtable options = new Hashtable();

	/**
	 * @return the colorTable
	 */
	public ColorTable getColorTable() {
		return (ColorTable) options.get("colorTable");
	}

	/**
	 * @param colorTable the colorTable to set
	 */
	public void setColorTable(ColorTable colorTable) {
		setOption("colorTable", colorTable);
	}

	/**
	 * Devuelve el minimo valor de una tabla de color
	 * @return
	 */
	public double getMinim() {
		Double d = (Double) options.get("minim");
		if (d != null)
			return d.doubleValue();
		return 0;
	} 

	/**
	 * Devuelve el maximo valor de una tabla de color
	 * @return
	 */
	public double getMaxim() {
		Double d = (Double) options.get("maxim");
		if (d != null)
			return d.doubleValue();
		return 255;
	}
	
	/**
	 * Devuelve si esta activado el checkbox de limites
	 * @return
	 */
	public boolean isLimitsEnabled() {
		Boolean b = (Boolean) options.get("limits");
		if (b != null)
			return b.booleanValue();
		return false;
	}
	
	/**
	 * Establece el minimo de una tabla de color
	 * @param value
	 */
	public void setMinim(double value) {
		setOption("minim", new Double(value));
	}
	
	/**
	 * Establece el maximo de una tabla de color
	 * @param value
	 */
	public void setMaxim(double value) {
		setOption("maxim", new Double(value));
	}
	
	/**
	 * Establece si se ha cambiado el valor activo de los limites
	 * @param value
	 */
	public void setLimitsEnabled(boolean value) {
		setOption("limits", new Boolean(value));
	}

	/**
	 * Devuelve si esta interpolado la tabla de color
	 * @return
	 */
	public boolean isInterpolated() {
		Boolean n = (Boolean) options.get("interpolated");
		if (n != null)
			return n.booleanValue();
		return true;
	}

	/**
	 * Define si ha cambiado la interpolacion
	 * @param value
	 */
	public void setInterpolated(boolean value) {
		setOption("interpolated", new Boolean(value));
	}

	/**
	 * Devuelve si esta activo el panel
	 * @return
	 */
	public boolean isEnabled() {
		Boolean n = (Boolean) options.get("enabled");
		if (n != null)
			return n.booleanValue();
		return true;
	}

	/**
	 * Define si el panel esta activo
	 * @param value
	 */
	public void setEnabled(boolean value) {
		setOption("enabled", new Boolean(value));
	}

	/**
	 * Refresca la vista previa
	 */
	public void refreshPreview() {
		setOption("refreshPreview", new Boolean(true));
	}

	/**
	 * Especifica el valor de una variable
	 * @param name
	 * @param value
	 */
	private void setOption(String name, Object value) {
		options.put(name, value);
		callValueChangedListeners(name, value);
	}

	/**
	 * Añadir un listener a la lista de eventos
	 * @param listener
	 */
	public void addValueChangedListener(PropertyListener listener) {
		if (!actionCommandListeners.contains(listener))
			actionCommandListeners.add(listener);
	}

	/**
	 * Borrar un listener de la lista de eventos
	 * @param listener
	 */
	public void removeValueChangedListener(PropertyListener listener) {
		actionCommandListeners.remove(listener);
	}

	/**
	 * Invocar a los eventos asociados al componente
	 */
	private void callValueChangedListeners(String name, Object value) {
		Iterator acIterator = actionCommandListeners.iterator();
		while (acIterator.hasNext()) {
			PropertyListener listener = (PropertyListener) acIterator.next();
			listener.actionValueChanged(new PropertyEvent(this, name, value, null));
		}
	}
}