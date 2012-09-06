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
package org.gvsig.raster.dataset.rmf;

import java.util.ArrayList;
/**
 * Clase base para el gestor de ficheros rmf. Esta contiene el mecanismo de registro de objetos
 * serializadores. Mantiene una lista de IRmfBlock.
 * 
 * 21-abr-2007
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public abstract class ClientRegister {

	protected ArrayList clients = new ArrayList();
	
	/**
	 * Añade un objeto IRmfBlock
	 * @param block
	 */
	public void addClient(IRmfBlock block) {
		removeClient(block.getClass());
		clients.add(block);
	}
	
	/**
	 * Elimina un objeto IRmfBlock de la lista a través de la clase del serializador.
	 * 
	 * @param id
	 */
	public void removeClient(Class class1) {
		for (int i = clients.size() - 1; i >= 0; i--)
			if (clients.get(i).getClass().equals(class1))
				clients.remove(i);
	}
	
	/**
	 * Obtiene el objeto serializador a través de su nombre de clase. El nombre
	 * de este coincide con el nombre del objeto que se está serializando seguido de la palabra 
	 * Serializer. Asi pues, el serializador del objeto Histogram se llamará HistogramSerializer.
	 * @param id
	 * @return IRmfBlock
	 */
	public IRmfBlock getClient(String id) {
		for (int i = 0; i < clients.size(); i++) {
			try {
				if (Class.forName(id).isInstance(clients.get(i)))
					return (IRmfBlock) clients.get(i);
			} catch (ClassNotFoundException e) {
				return null; // No devuelve ninguno
			}
		}
		return null;
	}
	
	/**
	 * Obtiene el objeto serializador a través de su posición. 
	 * @param pos Posición
	 * @return IRmfBlock
	 */
	public IRmfBlock getClient(int pos) {
		return (IRmfBlock) clients.get(pos);
	}
	
	/**
	 * Elimina todos los objetos serializadores de la lista.
	 */
	public void removeAllClients() {
		clients.clear();
	}
	
	/**
	 * Obtiene el número de clientes registrado (Objetos IRmfBlock).
	 * @return Entero con el número de clientes registrado
	 */
	public int getClientsCount() {
		return clients.size();
	}
}
