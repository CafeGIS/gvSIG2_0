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
package org.gvsig.raster.util;

import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.AbstractButton;
import javax.swing.JPanel;

/**
 * Clase base para los paneles gráficos. 
 * 
 * 17/07/2008
 * @author Nacho Brodin nachobrodin@gmail.com
 */
public abstract class BasePanel extends JPanel {
	private static final long    serialVersionUID         = -8877600252349334979L;
	private boolean	             enableValueChangedEvent  = true;
	
	public static final int      KEYLISTENER              = 0;
	public static final int      ACTIONLISTENER           = 1;
	public static final int      MOUSELISTENER            = 2;
	
	/**
	 * Obtiene una instancia de una clase generica que hereda de BasePanel
	 * @return BasePanel
	 */
	public static BasePanel getInstance() {
		return new GenericBasePanel();
	}
	
	/**
	 * Obtiene la traducción de la cadena de texto
	 * @param parent Ventana padre
	 * @param text Cadena a traducir
	 * @return Cadena de texto traducida
	 */
	public String getText(Object parent, String text) {
		return RasterToolsUtil.getText(parent, text);
	}
	
	/**
	 * Obtiene la traducción de la cadena de texto
	 * @param text Cadena a traducir
	 * @return Cadena de texto traducida
	 */
	public String getText(String text) {
		return RasterToolsUtil.getText(this, text);
	}
	
	/**
	 * Asigna el valor para la activación y desactivación del evento de cambio de valor en
	 * las cajas de texto.
	 * @param enableValueChangedEvent
	 */
	public void setEnableValueChangedEvent(boolean enableValueChangedEvent) {
		this.enableValueChangedEvent = enableValueChangedEvent;
	}
	
	/**
	 * Obtiene el valor para la activación y desactivación del evento de cambio de valor en
	 * las cajas de texto.
	 * @param enableValueChangedEvent
	 */
	public boolean isEnableValueChangedEvent() {
		return this.enableValueChangedEvent;
	}
	
	/**
	 * Obtiene la lista de componentes del panel. Si dentro tiene algún BasePanel
	 * consulta la lista de componentes de este y la devuelve.
	 * @return
	 */
	public ArrayList getComponentList() {
		ArrayList listComp = new ArrayList();
		for (int i = 0; i < this.getComponentCount(); i++) {
			if(getComponent(i) instanceof BasePanel) {
				ArrayList list = ((BasePanel)getComponent(i)).getComponentList();
				for (int j = 0; j < list.size(); j++) 
					listComp.add(list.get(j));
			} else
				listComp.add(getComponent(i));
		}
		return listComp;
	}
	
	/**
	 * Registra un listener en todos los componentes contenidos en este panel.
	 * @param type Tipo de listener definido por las variables estáticas en esta clase
	 * @param listener Objeto listener del tipo correcto
	 */
	public void registerListener(int type, Object listener) {
		ArrayList listComp = getComponentList();
		if(type == KEYLISTENER) {
			if(listener instanceof KeyListener) {
				for (int i = 0; i < listComp.size(); i++)
					if(listComp.get(i) instanceof Component)
						((Component)listComp.get(i)).addKeyListener((KeyListener)listener);
			}
		}
		if(type == ACTIONLISTENER) {
			if(listener instanceof ActionListener) {
				for (int i = 0; i < listComp.size(); i++)
					if(listComp.get(i) instanceof AbstractButton)
						((AbstractButton)listComp.get(i)).addActionListener((ActionListener)listener);
			}
		}
		if(type == MOUSELISTENER) {
			if(listener instanceof MouseListener) {
				for (int i = 0; i < listComp.size(); i++)
					if(listComp.get(i) instanceof Component)
						((Component)listComp.get(i)).addMouseListener((MouseListener)listener);
			}
		}
	}
	
	/**
	 * Traducción centralizada de los componentes de una panel
	 */
	protected abstract void translate();
	
	/**
	 * Acciones de inicialización
	 */
	protected abstract void init();
}
