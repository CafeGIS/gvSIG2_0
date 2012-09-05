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
package org.gvsig.fmap.mapcontext.rendering.strategies;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;



/**
 * Estructura de datos con la información relativa a las geometrías de una
 * fuente de datos necesaria para acelerar el procesado de la capa en memoria.
 *
 * @author Vicente Caballero Navarro
 */
public class MemoryShapeInfo implements ShapeInfo {
	ArrayList infos = new ArrayList();

	/**
	 * @see org.gvsig.fmap.mapcontext.rendering.strategies.ShapeInfo#addShapeInfo(java.awt.geom.Rectangle2D,
	 * 		int)
	 */
	public void addShapeInfo(Rectangle2D boundingBox, int type) {
		infos.add(new Info(boundingBox, type));
	}

	/**
	 * @see org.gvsig.fmap.mapcontext.rendering.strategies.ShapeInfo#addShapeInfo(int,
	 * 		java.awt.geom.Rectangle2D, int)
	 */
	public void setShapeInfo(int index, Rectangle2D boundingBox, int type)
		throws ArrayIndexOutOfBoundsException {
		infos.set(index, new Info(boundingBox, type));
	}

	/**
	 * @see org.gvsig.fmap.mapcontext.rendering.strategies.ShapeInfo#getBoundingBox(int)
	 */
	public Rectangle2D getBoundingBox(int index) {
		return ((Info) infos.get(index)).getRect();
	}

	/**
	 * @see org.gvsig.fmap.mapcontext.rendering.strategies.ShapeInfo#getType(int)
	 */
	public int getType(int index) {
		return ((Info) infos.get(index)).getType();
	}

	/**
	 * Clase con el rectángulo y el tipo de shape.
	 *
	 * @author Vicente Caballero Navarro
	 */
	class Info {
		private Rectangle2D rect;
		private int type;

		/**
		 * Crea un nuevo Info.
		 *
		 * @param rect Extent del shape.
		 * @param type Tipo de shape.
		 */
		public Info(Rectangle2D rect, int type) {
			this.rect = rect;
			this.type = type;
		}

		/**
		 * Devuelve el extent del shape.
		 *
		 * @return Extent del shape.
		 */
		public Rectangle2D getRect() {
			return rect;
		}

		/**
		 * Devuelve el tipo de shape.
		 *
		 * @return Tipo de shape.
		 */
		public int getType() {
			return type;
		}
	}
}
