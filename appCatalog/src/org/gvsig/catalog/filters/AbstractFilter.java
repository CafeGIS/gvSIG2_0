
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
/*
* CVS MESSAGES:
*
* $Id: AbstractFilter.java 561 2007-07-27 06:49:30 +0000 (Fri, 27 Jul 2007) jpiera $
* $Log$
* Revision 1.4  2006/01/18 09:57:01  jorpiell
* Eliminados algunos ficheros innecesarios
*
* Revision 1.3  2006/01/12 13:52:24  jorpiell
* Se ha añadido un boton close en las dos pantallas de connect. Además se ha cambiado el comportamiento de las ventanas para adaptarlo a la nueva forma de buscar multihilo
*
* Revision 1.2  2006/01/10 09:32:48  jorpiell
* Se ha echo un commit de las versiones modificadas del catalogo y del gazetteer usando el Poseidon. Se han renombrado algunas clases por considerar que tenian un nombre confuso y se han cambiado algunas relaciones entre clases (con la intención de separar GUI de la parte de control). Han habido clases que no han sido tocadas, pero como han sido formateadas usando el Poseidon TODAS las CLASES del proyecto han cambiado de versión.
*
* Revision 1.1  2005/12/22 08:31:43  jorpiell
* Aqui tambien se han producido muchos cambis, porque hemos acabado de cambiar la estructura del catálogo: Se han creado todas las clases "XXXMessages", que sacan toda la parte de los mensajes de los drivers. Ademas se ha incluido en "CatalogClient" la operación "getCapabilities", que libera a la interfaz de algunas operaciones que hacía anteriormente.
*
*
*/
package org.gvsig.catalog.filters;

/**
 * 
 * 
 */
public abstract class AbstractFilter implements IFilter {
 }
