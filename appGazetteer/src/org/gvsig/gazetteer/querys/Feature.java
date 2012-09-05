
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
package org.gvsig.gazetteer.querys;
import java.awt.geom.Point2D;

/**
 * This class represents a Feature into the WFS context
 * 
 * 
 * @author Jorge Piera Llodra (piera_jor@gva.es)
 */
public class Feature {
    private String Id;
    private String Name;
    private String Description;
    private Point2D coordinates;

/**
 * @param id 
 * @param name 
 * @param description 
 * @param coordinates 
 */
    public  Feature(String id, String name, String description, Point2D coordinates) {        
        super();
        Id = id;
        Name = name;
        Description = description;
        this.coordinates = coordinates;
    } 

/**
 * 
 * 
 * 
 * @return Returns the coordinates.
 */
    public Point2D getCoordinates() {        
        return coordinates;
    } 

/**
 * 
 * 
 * 
 * @param coordinates The coordinates to set.
 */
    public void setCoordinates(Point2D coordinates) {        
        this.coordinates = coordinates;
    } 

/**
 * 
 * 
 * 
 * @return Returns the description.
 */
    public String getDescription() {        
        return Description;
    } 

/**
 * 
 * 
 * 
 * @param description The description to set.
 */
    public void setDescription(String description) {        
        Description = description;
    } 

/**
 * 
 * 
 * 
 * @return Returns the id.
 */
    public String getId() {        
        return Id;
    } 

/**
 * 
 * 
 * 
 * @param id The id to set.
 */
    public void setId(String id) {        
        Id = id;
    } 

/**
 * 
 * 
 * 
 * @return Returns the name.
 */
    public String getName() {        
        return Name;
    } 

/**
 * 
 * 
 * 
 * @param name The name to set.
 */
    public void setName(String name) {        
        Name = name;
    } 

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {        
        return getDescription();
    } 
 }
