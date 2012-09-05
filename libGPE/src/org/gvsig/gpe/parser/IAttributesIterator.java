
package org.gvsig.gpe.parser;

import java.io.IOException;

import javax.xml.namespace.QName;
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
/* CVS MESSAGES:
*
* $Id$
* $Log$
*
*/
/**
* @author Carlos Sánchez Periñán
*/
public interface IAttributesIterator {
	/**
    * Returns whether there are more attributes in this sequence
    * 
    * @return
    * @throws IOException
    */
   public boolean hasNext() throws IOException;

   /**
    * Returns the dimension of the attributes list. Consumer code should use it to
    * pass a buffer with the correct lenght.
    * 
    * @return
    */
   public int getNumAttributes();

   /**
    * Value of next attribute
    */
   public Object nextAttribute() throws IOException;
   
   /**
    * @return the name of the next attribute
    */
   public QName nextAttributeName();
   
   /**
    * Initializes the iterator
    */
   public void initialize();
}
