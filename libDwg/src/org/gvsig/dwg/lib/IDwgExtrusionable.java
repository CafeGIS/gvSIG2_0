/*
 * Created on 09-ene-2007
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
* $Id: IDwgExtrusionable.java 28969 2009-05-25 13:23:12Z jmvivo $
* $Log$
* Revision 1.3.2.2  2007-03-21 19:49:16  azabala
* implementation of dwg 12, 13, 14.
*
* Revision 1.3  2007/02/15 20:35:13  azabala
* comments
*
* Revision 1.2  2007/02/15 15:32:51  azabala
* added comments
*
* Revision 1.1  2007/01/12 19:29:58  azabala
* first version in cvs
*
*
*/
package org.gvsig.dwg.lib;
/**
 * All dwg drawing entities that need to compute
 * an extrussion before draw them must implement
 * this interface.
 * 
 * This "extrusion" transforms coordinates from the 
 * "Object Coordinate System" (OCS) to the
 * "World Coordinate System" (WCS).
 * 
 *  For some entities, the OCS is equivalent to the 
 *  WCS and all points are expressed in World coordinates. 

		Entities 						Notes

		3D entities such as 			These entities do not lie in a 
		line, point, 3dface,			particular plane. All points are  
		3D polyline,					expressed in world coordinates.  
		3D vertex, 3D mesh, 			Of these entities, only lines and points can be extruded. 
		3D mesh vertex
	



		2D entities such as circle, arc, 		These entities are planar in nature. 
		solid, trace, text, attrib, attdef, 	All points are expressed in object coordinates. 
		shape, insert, 2D polyline, 2D vertex, 	All of these entities can be extruded. 
		lwpolyline, hatch, image
	

 * 
 * 
 * */
public interface IDwgExtrusionable {
	public void applyExtrussion();
}

