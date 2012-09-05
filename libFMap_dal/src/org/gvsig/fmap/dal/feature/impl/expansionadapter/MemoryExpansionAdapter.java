/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2004 IVER T.I. and Generalitat Valenciana.
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
 *
 * For more information, contact:
 *
 *  Generalitat Valenciana
 *   Conselleria d'Infraestructures i Transport
 *   Av. Blasco Ibáñez, 50
 *   46010 VALENCIA
 *   SPAIN
 *
 *      +34 963862235
 *   gvsig@gva.es
 *      www.gvsig.gva.es
 *
 *    or
 *
 *   IVER T.I. S.A
 *   Salamanca 50
 *   46005 Valencia
 *   Spain
 *
 *   +34 963163400
 *   dac@iver.es
 */
package org.gvsig.fmap.dal.feature.impl.expansionadapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


/**
 * Implementación en memoria de ExpansionAdapter.
 *
 * @author Vicente Caballero Navarro
 */
public class MemoryExpansionAdapter implements ExpansionAdapter {
	private ArrayList objects = new ArrayList();

	public MemoryExpansionAdapter(){
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.feature.expansionadapter.ExpansionAdapter#addFeature(org.gvsig.fmap.dal.feature.Feature)
	 */
	public int addObject(Object obj) {
//		Feature feature=(Feature)obj;
		int newIndex = objects.size();
		objects.add(obj);
		return newIndex;
	}

//	/* (non-Javadoc)
//	 * @see org.gvsig.fmap.dal.feature.expansionadapter.ExpansionAdapter#modifyFeature(int, org.gvsig.fmap.dal.feature.Feature)
//	 */
//	public int updateObject(Object obj) {
////		Feature feature=(Feature)obj;
//		objects.add(obj);
//		return objects.size() - 1;
//	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.feature.expansionadapter.ExpansionAdapter#getFeature(int)
	 */
	public Object getObject(int index){
		return objects.get(index);
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.feature.expansionadapter.ExpansionAdapter#compact(java.util.HashMap)
	 */
	public void compact(HashMap relations) {

	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.feature.expansionadapter.ExpansionAdapter#deleteLastFeature()
	 */
	public void deleteLastObject() {
		objects.remove(objects.size()-1);

	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.feature.expansionadapter.ExpansionAdapter#open()
	 */
	public void open() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.feature.expansionadapter.ExpansionAdapter#close()
	 */
	public void close() {
		objects.clear();
		System.gc();
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.feature.expansionadapter.ExpansionAdapter#getSize()
	 */
	public int getSize() {
		return objects.size();
	}

	public Iterator iterator() {
		return objects.iterator();
	}
}
