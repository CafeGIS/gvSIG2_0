/*
 * Cresques Mapping Suite. Graphic Library for constructing mapping applications.
 *
 * Copyright (C) 2004-5.
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
 * cresques@gmail.com
 */
package org.cresques.px.gml;

import org.cresques.geo.Point3D;


/**
 * Extensión 3D de InsPoint.
 * @author jmorell
 */
public class InsPoint3D extends InsPoint {
    
    /**
     * Constructor de InsPoint3D.
     */
    public InsPoint3D() {
        super();
    }
    
    /**
     * Permite añadir un punto en 3D.
     * @param pt, punto en 3D.
     */
    public void add(Point3D pt) {
        pointNr++;
        super.add(pt);
    }
    
    /**
     * Devuelve un punto 3D dado por un índice.
     * @param i, Índice.
     * @return Point3D, punto 3D.
     */
    public Point3D getPoint3D(int i) {
        return (Point3D) data.get(i);
    }
}
