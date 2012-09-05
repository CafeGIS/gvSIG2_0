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

/* CVS MESSAGES:
*
* $Id: ZoomConstraintsImpl.java 10671 2007-03-09 08:33:43Z jaume $
* $Log$
* Revision 1.2  2007-03-09 08:33:43  jaume
* *** empty log message ***
*
* Revision 1.1.2.2  2007/02/15 16:23:44  jaume
* *** empty log message ***
*
* Revision 1.1.2.1  2007/02/09 07:47:05  jaume
* Isymbol moved
*
* Revision 1.1.2.1  2007/01/30 18:10:45  jaume
* start commiting labeling stuff
*
*
*/
package org.gvsig.fmap.mapcontext.rendering.legend.styling;


import com.iver.utiles.XMLEntity;

/**
 * @author  jaume dominguez faus - jaume.dominguez@iver.es
 */
public class ZoomConstraintsImpl implements IZoomConstraints {
	/**
	 * @uml.property  name="mode"
	 */
	private int mode = DEFINED_BY_THE_LAYER;
	/**
	 * @uml.property  name="minScale"
	 */
	private long minScale = -1;
	/**
	 * @uml.property  name="maxScale"
	 */
	private long maxScale = -1;

	/**
	 * @param mode
	 * @uml.property  name="mode"
	 */
	public void setMode(int mode) {
		if (mode != DEFINED_BY_THE_LAYER &&
			mode != DEFINED_BY_THE_USER)
			throw new IllegalArgumentException();
		this.mode = mode;
	}

	/**
	 * @return
	 * @uml.property  name="maxScale"
	 */
	public long getMaxScale() {
		return maxScale;
	}

	/**
	 * @param maxScale
	 * @uml.property  name="maxScale"
	 */
	public void setMaxScale(long maxScale) {
		this.maxScale = maxScale;
	}

	/**
	 * @return
	 * @uml.property  name="minScale"
	 */
	public long getMinScale() {
		return minScale;
	}

	/**
	 * @param minScale
	 * @uml.property  name="minScale"
	 */
	public void setMinScale(long minScale) {
		this.minScale = minScale;
	}

	public boolean isUserDefined() {
		return mode == DEFINED_BY_THE_USER;
	}

	public boolean isLayerDefined() {
		return mode == DEFINED_BY_THE_LAYER;
	}

	public String getClassName() {
		// TODO Auto-generated method stub
		throw new Error("Not yet implemented!");
	}

	public XMLEntity getXMLEntity() {
		XMLEntity xml = new XMLEntity();
		xml.putProperty("className", getClassName());
		xml.putProperty("maxScale", maxScale);
		xml.putProperty("minScale", minScale);
		xml.putProperty("mode", mode);
		return xml;
	}

	public void setXMLEntity(XMLEntity xml) {
		setMaxScale(xml.getLongProperty("maxScale"));
		setMinScale(xml.getLongProperty("minScale"));
		setMode(xml.getIntProperty("mode"));
	}
}
