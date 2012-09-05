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
package org.cresques.px.dxf;

import org.cresques.cts.ICoordTrans;
import org.cresques.cts.IProjection;
import org.cresques.geo.Projected;
import org.cresques.px.PxObj;


/**
 * Clase ancestro para las entidades de un fichero DXF.
 * @author "Luis W. Sevilla" <sevilla_lui@gva.es>
 * @author jmorell
 */
public abstract class DxfEntity extends PxObj implements Projected {
    IProjection proj = null;
    DxfLayer layer = null;

    //int dxfColor = 256;
    int dxfColor = 0;
    int entitiesFollow = 0;
    boolean space = false; // false --> model space, true paper space;
    private int handle;
    
    /**
     * Constructor genérico de entidades procedentes de un fichero DXF.
     * @param proj, proyección cartográfica en la que se encuentra la entidad.
     * @param layer, capa del DXF en la que se encuentra la entidad.
     */
    public DxfEntity(IProjection proj, DxfLayer layer) {
        setProjection(proj);
        this.layer = layer;
    }
    
    /**
     * Establece la proyección cartográfica en la que se encuentra la entidad.
     * @param p, Proyección cartográfica.
     */
    public void setProjection(IProjection p) {
        proj = p;
    }
    
    /**
     * Devuelve la proyección cartográfica en la que se encuentra la entidad.
     * @return IProjection, proyección cartográfica.
     */
    public IProjection getProjection() {
        return proj;
    }
    
    /**
     * Permite reproyectar una entidad dado un conjunto de coordenadas de transformación.
     * @param rp, coordenadas de transformación.
     */
    abstract public void reProject(ICoordTrans ct);
    
    /**
     * Devuelve el nombre de la capa en la que se encuentra la entidad.
     * @return String
     */
    public String getLayerName() {
        return layer.getName();
    }
    
    /**
     * Devuelve el color de la entidad según AutoCAD en forma de texto.
     * @return String con el color de la entidad según AutoCAD.
     */
    public String getColor() {
        return "" + dxfColor;
    }
    
    /**
     * Permite la escritura de entidades en un fichero DXF2000.
     * @return String, la cadena que se escribirá en el fichero con la información
     * de la correspondiente entidad.
     */
    abstract public String toDxfString();

    /**
     * @return Returns the handle.
     */
    public int getHandle() {
        return handle;
    }

    /**
     * @param handle The handle to set.
     */
    public void setHandle(int handle) {
        this.handle = handle;
    }

    /**
     * Devuelve la capa en la que se encuentra la entidad.
     * @return DxfLayer, la capa.
     */
    public DxfLayer getLayer() {
        return layer;
    }
}
