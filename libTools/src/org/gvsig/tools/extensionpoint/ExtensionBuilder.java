/* gvSIG. Geographic Information System of the Valencian Government
 *
 * Copyright (C) 2007-2008 Infrastructures and Transports Department
 * of the Valencian Gobernment (CIT)
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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, 
 * MA  02110-1301, USA.
 * 
 */

/*
 * AUTHORS (In addition to CIT):
 */
package org.gvsig.tools.extensionpoint;

import java.util.Map;

/**
 * Interface utilizado para indicar al registro de extensiones
 * que no se trata de una clase lo que hey registrado, si no
 * una instancia de un objeto a usar para crear la extension.
 * 
 * 
 * @author jjdelcerro
 *
 */
public interface ExtensionBuilder {
	/**
	 * Crea una instancia de la extension y la retorna.
	 * <br>
	 * @return
	 */
	public Object create();
	
	/**
	 * Crea una instancia de la extension y la retorna.
	 * <br>
	 * En <i>args</i> recibira la lista de argumeentos a utilizar
	 * para crear la extension.
	 * <br>
	 * @param args
	 * @return
	 */
	public Object create(Object [] args);
	
	public Object create(Map args);
}
