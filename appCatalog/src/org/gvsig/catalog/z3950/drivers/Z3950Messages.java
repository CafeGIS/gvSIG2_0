
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
* $Id: Z3950Messages.java 585 2007-09-03 10:21:55 +0000 (Mon, 03 Sep 2007) jpiera $
* $Log$
* Revision 1.4.10.2  2006/11/15 00:08:08  jjdelcerro
* *** empty log message ***
*
* Revision 1.6  2006/10/02 08:29:07  jorpiell
* Modificados los cambios del Branch 10 al head
*
* Revision 1.4.10.1  2006/09/20 12:01:18  jorpiell
* Se ha cambiado la versión de la jzkit, se ha incorporado la funcionalidad de cargar arcims
*
* Revision 1.5  2006/09/20 11:22:43  jorpiell
* Se ha cambiado la versión de la librería jzkit de la 1 a la 2.0
*
* Revision 1.4  2006/01/12 13:52:24  jorpiell
* Se ha añadido un boton close en las dos pantallas de connect. Además se ha cambiado el comportamiento de las ventanas para adaptarlo a la nueva forma de buscar multihilo
*
* Revision 1.3  2006/01/10 17:23:23  jorpiell
* Se ha hecho una pequeña modificación en el gazeteer: ahora funcionan las búsquedas restringiendo el área en el WFSG. Hay muchos cambios porque se ha hecho un CONTROL+SHIFT+O sobre todo el proyecto para eliminar los imports y para ordenarlos
*
* Revision 1.2  2006/01/10 09:32:49  jorpiell
* Se ha echo un commit de las versiones modificadas del catalogo y del gazetteer usando el Poseidon. Se han renombrado algunas clases por considerar que tenian un nombre confuso y se han cambiado algunas relaciones entre clases (con la intención de separar GUI de la parte de control). Han habido clases que no han sido tocadas, pero como han sido formateadas usando el Poseidon TODAS las CLASES del proyecto han cambiado de versión.
*
* Revision 1.1  2005/12/22 08:31:43  jorpiell
* Aqui tambien se han producido muchos cambis, porque hemos acabado de cambiar la estructura del catálogo: Se han creado todas las clases "XXXMessages", que sacan toda la parte de los mensajes de los drivers. Ademas se ha incluido en "CatalogClient" la operación "getCapabilities", que libera a la interfaz de algunas operaciones que hacía anteriormente.
*
*
*/
package org.gvsig.catalog.z3950.drivers;
import org.gvsig.catalog.querys.CatalogQuery;
import org.gvsig.catalog.z3950.filters.Z3950Filter;

/**
 * This class is used to create all the Z3950 protocol
 * messages
 * 
 * 
 * @author Jorge Piera Llodra (piera_jor@gva.es)
 */
public class Z3950Messages {
/**
 * 
 * 
 */
    private Z3950CatalogServiceDriver driver = null;

/**
 * 
 * 
 * 
 * @param driver 
 */
    public  Z3950Messages(Z3950CatalogServiceDriver driver) {        
        this.driver = driver;
    } 

/**
 * 
 * 
 * 
 * @return 
 * @param message 
 */
    public String getCapabilities(String message) {        
        return "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>" + "<Z3950> " +
        "<Servidor>" + message + "</Servidor>" + "</Z3950>";
       
    } 

/**
 * 
 * 
 * 
 * @return 
 * @param query 
 */
    public String getRecords(CatalogQuery query,String database) {        
        return new Z3950Filter(database).getQuery(query);
    } 
 }
