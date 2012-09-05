/*
 * Created on 09-ene-2007
 *
 * gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
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

package org.gvsig.dwg.lib.readers;

import org.gvsig.dwg.lib.CorruptedDwgEntityException;
import org.gvsig.dwg.lib.DwgObject;

/**
 * Each dwg entity in the dwg section OBJECTS has a common part
 * and an specific part.
 * This interface has the responsability of reading the specif part.
 * 
 * In previous versions of libDwg DwgObject has this responsability, but
 * with the inclusion of new versions of DWG (12, 13, 14, 2004) implementation
 * is function of the dwg entity and the version.
 * 
 * 
 * 
 * @author azabala

 */
public interface IDwgObjectReader {
	
	
	/**
	 * Reads the specific part of the dwg object
	 * @throws Exception 
	 * */
	public void readSpecificObj(int[] data, int offset, DwgObject dwgObj) throws RuntimeException, CorruptedDwgEntityException;
	/**
	 * Sets the fileReader (wich has the responsability of read
	 * head and tail of a dwg object)
	 * */
	public void setFileReader(IDwgFileReader headTailReader);
}
