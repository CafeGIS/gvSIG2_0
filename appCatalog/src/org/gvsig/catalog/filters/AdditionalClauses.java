
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
package org.gvsig.catalog.filters;

/**
 * This class is used like an structure. It contains the fields needed to add a new
 * operation to the query language.
 * 
 * 
 * @author Jorge Piera Llodra (piera_jor@gva.es)
 */
public class AdditionalClauses {

/**
 * 
 * 
 */
    private String property;

/**
 * 
 * 
 */
    private String value;

/**
 * 
 * 
 */
    private String concorancia;

/**
 * 
 * 
 */
    private String relationship;

/**
 * 
 * 
 */
    private String type;

/**
 * 
 * 
 * 
 * @param property 
 * @param value 
 * @param concorancia 
 * @param relationship 
 * @param type 
 */
    public  AdditionalClauses(String property, String value, String concorancia, String relationship, String type) {        
        super();
        this.property = property;
        this.value = value;
        this.concorancia = concorancia;
        this.relationship = relationship;
        this.type = type;
    } 

/**
 * 
 * 
 * 
 * @return Returns the concorancia.
 */
    public String getConcorancia() {        
        return concorancia;
    } 

/**
 * 
 * 
 * 
 * @return Returns the property.
 */
    public String getProperty() {        
        return property;
    } 

/**
 * 
 * 
 * 
 * @return Returns the relationship.
 */
    public String getRelationship() {        
        return relationship;
    } 

/**
 * 
 * 
 * 
 * @return Returns the type.
 */
    public String getType() {        
        return type;
    } 

/**
 * 
 * 
 * 
 * @return Returns the value.
 */
    public String getValue() {        
        return value;
    } 
 }
