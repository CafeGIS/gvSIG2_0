/*
 * Created on 19-may-2004
 *
 */
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
package com.iver.cit.gvsig.project.documents.layout;

import com.iver.utiles.XMLEntity;


/**
 * Clase que almacena la altura y anchura de un folio.
 *
 * @author Vicente Caballero Navarro
 */
public class Size {
	private double alto;
	private double ancho;

	/**
	 * Creates a new Size object.
	 *
	 * @param al Altura
	 * @param an Anchura
	 */
	public Size(double al, double an) {
		alto = al;
		ancho = an;
	}

	/**
	 * Devuelve el alto del folio.
	 *
	 * @return altura.
	 */
	public double getAlto() {
		return alto;
	}

	/**
	 * Devuelve la anchura del folio.
	 *
	 * @return Anchura.
	 */
	public double getAncho() {
		return ancho;
	}

	/**
	 * Devuelve un Objeto XMLEntity con la información los atributos necesarios
	 * para poder después volver a crear el objeto original.
	 *
	 * @return XMLEntity.
	 */
	public XMLEntity getXMLEntity() {
		XMLEntity xml = new XMLEntity();
		xml.putProperty("className",this.getClass().getName());
		xml.putProperty("ancho", ancho);
		xml.putProperty("alto", alto);

		return xml;
	}

	/**
	 * Crea un Objeto de esta clase a partir de la información del XMLEntity.
	 *
	 * @param xml XMLEntity
	 *
	 * @return Objeto de esta clase.
	 */
	public static Size createSize(XMLEntity xml) {
		Size size = new Size(xml.getDoubleProperty("alto"),
				xml.getDoubleProperty("ancho"));

		return size;
	}
}
