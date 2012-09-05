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
package org.cresques.px;

import java.awt.Stroke;


public abstract class PxObj implements Drawable, Extent.Has {
    public Stroke stroke = null;
    /**
     * Extent completo del raster. Este contiene las coordenadas reales tanto
     * para un raster rotado como sin rotar. Este extent coincide con requestExtent
     * cuando el raster no tiene rotaci�n. 
     */
    protected Extent extent = null;
    /**
     * Este es el extent sobre el que se ajusta una petici�n para que esta no exceda el 
     * extent m�ximo del raster. Para un raster sin rotar ser� igual al extent
     * pero para un raster rotado ser� igual al extent del raster como si no 
     * tuviera rotaci�n. Esto ha de ser as� ya que la rotaci�n solo se hace sobre la
     * vista y las peticiones han de hacerse en coordenadas de la imagen sin shearing
     * aplicado.
     */
    protected Extent requestExtent = null;

    public Extent getExtent() {
        return extent;
    }
}
