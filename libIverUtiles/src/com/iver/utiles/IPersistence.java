/*
 * Created on 10-nov-2005
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
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
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
package com.iver.utiles;

/**
 * <p>Any object that needs store its data to restore it after, should implement <code>IPersistence</code>.</p>
 *
 * <p>The process of persisting (for instance in a file) the information of an object using XML is named <i>Marshall</i>,
 *  whereas the inverse process in named <i>Unmarshall</i>.</p>
 *
 * <p>It's necessary specify the name of the class that will be persisted or restored.</p>
 *
 * @author fjp
 */
public interface IPersistence {
	/**
	 * <p>Gets the class name of the object.</p>
	 *
	 * @return the class name of the object
	 */
    String getClassName();

    /**
     * <p>Returns an XML entity with all necessary information of the object to <i>marshall</i>.</p>
     *
     * @return the XML entity with all necessary information of the object
     */
    XMLEntity getXMLEntity() throws XMLException;

    /**
     * <p>Sets an XML entity with all necessary information of the object.</p>
     *
     * @param xml the XML entity with all necessary information of the object
     */
    void setXMLEntity(XMLEntity xml) throws XMLException;
}
