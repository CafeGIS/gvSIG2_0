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
package org.gvsig.gui.util;

import java.util.ArrayList;

import javax.swing.JComponent;
/**
 * Clase para poder cambiar el estado de un componente y sus componentes hijos.
 *
 * Tiene dos formas de uso:
 *
 * 1.- Desactivar un componente y todos sus hijos sin necesidad de guardar su
 *     estado. Para este caso solo es necesario usar el método estático
 *     setDisabled(componente). El hecho de que no exista un activar es que para
 *     desactivar esta claro que queremos desactivar un componente y sus hijos,
 *     pero a la hora de activar no todos los hijos deben estar activos, para
 *     estos casos es necesario ver la segunda opción.
 *
 * 2.- Desactivar un componente guardando todos sus estados y volver a recuperar
 *     sus estados como estaba inicialmente. Ejemplo:
 *
 *     // Creamos el StatusComponent asociándolo al componente en cuestión
 *     StatusComponent statusComponent = new StatusComponent(miControl);
 *
 *     // Desactivamos el componente y sus hijos guardando todos los estados.
 *     statusComponent.setEnabled(false);
 *
 *     ......
 *     // Activamos el componente recuperando su estado inicial
 *     statusComponent.setEnabled(true);
 *
 * @version 07/09/2007
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
public class StatusComponent {
	private ArrayList<StatusComponentStruct> statusList = new ArrayList<StatusComponentStruct>();
	private boolean    enabled   = true;
	private JComponent component = null;

	/**
	 * Estructura de datos para poder tener el estado de un componente
	 * @version 07/09/2007
	 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
	 */
	public class StatusComponentStruct {
		private JComponent object;
		private boolean enabled;

		public boolean isEnabled() {
			return enabled;
		}

		public void setEnabled(boolean enabled) {
			this.enabled = enabled;
		}

		public JComponent getObject() {
			return object;
		}

		public void setObject(JComponent object) {
			this.object = object;
		}
	}

	/**
	 * Construye un StatusComponent. Es necesario pasarle el componente que
	 * queremos tratar.
	 * @param component
	 */
	public StatusComponent(JComponent component) {
		this.component = component;
	}

	/**
	 * Recupera el estado de un componente y todos sus hijos, vaciando la pila de
	 * estados. Eso quiere decir que no se podra volver a recuperar su estado sin
	 * haberlo guardado previamente.
	 * @param component
	 */
	private void restoreStatus(JComponent component) {
		boolean auxEnabled = false;
		boolean finded = false;
		// Buscar estado de dicho componente
		for (int i = 0; i < statusList.size(); i++) {
			StatusComponentStruct auxStatus = (StatusComponentStruct) statusList.get(i);
			if (auxStatus.getObject() == component) {
				auxEnabled = auxStatus.isEnabled();
				statusList.remove(i);
				finded = true;
				break;
			}
		}

		// Asignar su estado
		if (finded)
			component.setEnabled(auxEnabled);

		for (int i = 0; i < component.getComponentCount(); i++)
			if (component.getComponent(i) instanceof JComponent)
				restoreStatus((JComponent) component.getComponent(i));
	}

	/**
	 * Desactivar el componente y todos sus hijos sin guardar los estados. Hay que
	 * tener cuidado con no confundirlo con setEnabled(false). Este metodo nunca
	 * guardara el estado, asi que no se podra recuperar despues dicho estado.
	 * @param component
	 */
	static public void setDisabled(JComponent component) {
		component.setEnabled(false);
		for (int i = 0; i < component.getComponentCount(); i++)
			if (component.getComponent(i) instanceof JComponent)
				setDisabled((JComponent) component.getComponent(i));
	}

	/**
	 * Guarda el estado de un componente. Este proceso es recursivo. El estado
	 * se guarda en un array y este array no es vaciado inicialmente. La idea es
	 * guardar en un disabled y recuperar en un enabled y asegurarse que no puede
	 * ocurrir un disabled o un enabled dos veces.
	 * @param component
	 */
	private void saveComponentsStatus(JComponent component) {
		// Guardar estado
		StatusComponentStruct auxStatus = new StatusComponentStruct();
		auxStatus.setEnabled(component.isEnabled());
		auxStatus.setObject(component);
		statusList.add(auxStatus);

		for (int i = 0; i < component.getComponentCount(); i++)
			if (component.getComponent(i) instanceof JComponent)
				saveComponentsStatus((JComponent) component.getComponent(i));
	}

	/**
	 * Activa o desactiva un componente y todos sus componentes hijos. No se puede
	 * activar o desactivar dos veces seguidas. Siendo ignoradas las peticiones
	 * repetitivas.
	 * @param enabled
	 */
	public void setEnabled(boolean enabled) {
		// Si el estado ha cambiado, hacemos algo
		if (this.enabled != enabled) {
			if (enabled) {
				restoreStatus(component);
			} else {
				saveComponentsStatus(component);
				setDisabled(component);
			}
			this.enabled = enabled;
		}
	}

	/**
	 * @return the enabled
	 */
	public boolean isEnabled() {
		return enabled;
	}
}
