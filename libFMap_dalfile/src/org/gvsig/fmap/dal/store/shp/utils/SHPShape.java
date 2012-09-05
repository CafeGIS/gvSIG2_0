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
package org.gvsig.fmap.dal.store.shp.utils;

import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;

import org.gvsig.fmap.geom.Geometry;

/**
 * Interfaz de todos los tipos de formato shape.
 *
 * @author Vicente Caballero Navarro
 */
public interface SHPShape {
	/**
	 * Devuelve el tipo de shape de que se trata.
	 *
	 * @return Tipo de shape.
	 */
	public int getShapeType();

	/**
	 * Lee del buffer el shape y crea una nueva geometría.
	 *
	 * @param buffer Buffer de donde se lee.
	 * @param type Tipo de shape en concreto.
	 *
	 * @return Nueva geometría.
	 */
	public Geometry read(MappedByteBuffer buffer, int type);

	/**
	 * Escribe en el buffer la geometría que se pasa como parámetro.
	 *
	 * @param buffer Buffer donde escribir.
	 * @param geometry Geometría a escribir.
	 */
	public void write(ByteBuffer buffer, Geometry geometry);

	/**
	 * Devuelve el tamaño de la geometría.
	 *
	 * @param fgeometry Geometría a medir.
	 *
	 * @return Tamaño de la geometría.
	 */
	public int getLength(Geometry fgeometry);

	/**
	 * Obtiene los puntos y partes del GeneralPathXIterator del shape.
	 *
	 * @param iter
	 */
	public void obtainsPoints(Geometry g);

//	public void setFlatness(double flatness);
}
