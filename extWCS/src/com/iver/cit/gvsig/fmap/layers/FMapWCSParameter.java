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
package com.iver.cit.gvsig.fmap.layers;

import java.util.ArrayList;

/**
 * Class abstracting WCS's axis descriptions into FMap
 * @author jaume dominguez faus - jaume.dominguez@iver.es
 * @TODO add interval parameters support
 */
public class FMapWCSParameter {

	public static final int VALUE_LIST = 0;
	public static final int INTERVAL = 1;
	private String name;
	private int type;
	private ArrayList valueList;
	private String interval;
	private String label;

	public void setName(String name) {
		this.name = name;
		
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getType() {
		return type;
	}

	public void setValueList(ArrayList singleValues) {
		this.valueList = singleValues;
	}

	public void setInterval(String interval) {
		this.interval = interval;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String toString() {
		return (label!=null) ? label : name;
	}

	public ArrayList getValueList() {
		return valueList;
	}

	public String getName() {
		return name;
	}
}
