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

import java.awt.Graphics2D;
import java.util.Hashtable;

import org.cresques.geo.ViewPortData;
import org.cresques.px.Extent;
import org.cresques.px.PxObj;


/**
 * Feature de .gml y .shp
 * @author "Luis W. Sevilla" <sevilla_lui@gva.es>
 */
public class Feature extends PxObj {
    Hashtable property = null;
    Geometry geometry = null;
    
    /**
     * Constructor de la clase Feature.
     */
    public Feature() {
        property = new Hashtable();
    }
    
    /**
     * Establece los atributos de las Features.
     * @param prop, Nombre del atributo.
     * @param value, Valor que toma este atributo para esta Feature.
     */
    public void setProp(String prop, String value) {
        property.put(prop, value);
    }
    
    /**
     * Devuelve el valor de un determinado atributo del Feature.
     * @param prop, Nombre del atributo del cual queremos conocer el valor.
     * @return String, Valor del atributo para esta Feature.
     */
    public String getProp(String prop) {
        return (String) property.get(prop);
    }
    
    /**
     * Establece la geometría a la que está asociada este Feature.
     * @param geom, la geometría que caracteriza a este Feature.
     */
    public void setGeometry(Geometry geom) {
        geometry = geom;
    }
    
    /**
     * Devuelve la geometría asociada a este Feature.
     * @return Geometry, la geometría que caracteriza a este Feature.
     */
    public Geometry getGeometry() {
        return geometry;
    }
    
    /**
     * Devuelve el extent de la geometría que caracteriza al Feature.
     * @return Extent, rectángulo en el que está ubicado la geometría característica
     * del Feature.
     */
    public Extent getExtent() {
        if (geometry != null) {
            return geometry.getExtent();
        }

        return null;
    }
    
    /**
     * Permite dibujar le geometría característica del Feature.
     */
    public void draw(Graphics2D g, ViewPortData vp) {
        Geometry geo = getGeometry();

        if (geo != null) {
            if (geo instanceof Point) {
                ((Point) geo).text = getProp("NOMBRE");
            }

            geo.draw(g, vp);
        }
    }
}
