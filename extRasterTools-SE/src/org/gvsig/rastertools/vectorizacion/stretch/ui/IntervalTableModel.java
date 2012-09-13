/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2007 IVER T.I. and Generalitat Valenciana.
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
package org.gvsig.rastertools.vectorizacion.stretch.ui;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import org.gvsig.raster.beans.canvas.layers.StretchLayerDataModel;

/**
 * Modelo de datos para la tabla con la lista de valores. Utiliza el modelo de 
 * datos de la gráfica. El modelo de datos utiliza el rango 0-1 para almacenar los 
 * tramos. Es necesario un desplazamiento y una distancia para visualizarlo en el 
 * rango deseado.
 * 
 * 07/08/2008
 * @author Nacho Brodin nachobrodin@gmail.com
 */
public class IntervalTableModel extends AbstractTableModel {
	private static final long   serialVersionUID  = 1L;
	
	private StretchLayerDataModel    data              = new StretchLayerDataModel();
	private double                   shift             = 0;
	private double                   distance          = 255;
	
	/**
	 * Asigna el modelo de datos
	 * @param data
	 */
	public IntervalTableModel(StretchLayerDataModel data) {
		this.data = data;
	}
	
	/**
	 * Asigna la distancia y desplazamiento para que los valores del array que
	 * son porcentuales sean mostrados en el rango requerido
	 * @param shift Desplazamiento
	 * @param distance Distancia
	 */
	public void setShiftAndDistance(double shift, double distance) {
		this.shift = shift;
		this.distance = distance;
	}
	
	/**
	 * Obtiene el modelo de datos
	 * @return
	 */
	public StretchLayerDataModel getStretchDataModel() {
		return this.data;
	}

	/**
	 * Añade un elemento a la lista
	 * @param element
	 */
	public void setElement(String element) {
		try {
			Double value = new Double((String)element);
			data.add(value);
			super.fireTableDataChanged();
		}catch(NumberFormatException e) {
			//No se hace ninguna asignación
		}
	}

	/**
	 * Obtiene el tamaño de la lista
	 */
	public int getSize() {
		return data.size();
	}
	

	/*
	 * (non-Javadoc)
	 * @see javax.swing.table.TableModel#getColumnClass(int)
	 */
	public Class getColumnClass(int columnIndex) {
		return String.class;
	}

	/*
	 * (non-Javadoc)
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
	public int getColumnCount() {
		return 1;
	}

	/*
	 * (non-Javadoc)
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	public int getRowCount() {
		return data.size();
	}

	/*
	 * (non-Javadoc)
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	public Object getValueAt(int rowIndex, int columnIndex) {
		return new Double((((Double)data.get(rowIndex)).doubleValue() * distance) + shift);
	}
	
	/**
	 * Obtiene la lista de valores contenida en el modelo
	 * @return ArrayList
	 */
	public ArrayList getValueList() {
		ArrayList list = new ArrayList();
		for (int i = 0; i < getRowCount(); i++) {
			list.add(getValueAt(i, 0));
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * @see javax.swing.table.TableModel#isCellEditable(int, int)
	 */
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see javax.swing.table.TableModel#setValueAt(java.lang.Object, int, int)
	 */
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		try {
			if(aValue instanceof String) {
				Double value = new Double((String)aValue);
				//Devuelve el valor al rango 0-1 antes de salvar
				value = new Double((value.doubleValue() - shift) / distance);
				data.set(rowIndex, value);
				data.sort();
				super.fireTableDataChanged();
			}
		}catch(NumberFormatException e) {
			//No se hace ninguna asignación
		}
	}
}
