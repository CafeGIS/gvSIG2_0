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

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.Iterator;
import java.util.Vector;

import org.cresques.cts.ICoordTrans;
import org.cresques.cts.IProjection;
import org.cresques.geo.Projected;
import org.cresques.geo.ViewPortData;
import org.cresques.px.Extent;
import org.cresques.px.IObjList;
import org.cresques.px.PxObj;


/**
 * La clase DxfEntityList almacena un conjunto de objetos gráficos. Esta basada en
 * FeatureCollection de .gml y .shp
 * @author "Luis W. Sevilla" <sevilla_lui@gva.es>
 */
public class DxfEntityList extends PxObj implements IObjList.vector {
    IProjection proj = null;
    private Vector data = null;
    
    /**
     * Constructor de DxfEntityList.
     * @param proj, Proyección cartográfica en la que se encuentra la DxfEntityList.
     */
    public DxfEntityList(IProjection proj) {
        extent = new Extent();
        data = new Vector();
    }
    
    /**
     * Permite añadir un objeto gráfico a la lista.
     * @param obj, Objeto gráfico que implemente el interface Extent.Has
     */
    public void add(Extent.Has obj) {
        if (obj != null) {
            extent.add(obj.getExtent());
            data.add(obj);
        }
    }
    
    /**
     * Devuelve uno de los elementos de la lista de objetos gráficos.
     * @param cnt, Índice del elemento de la lista que queremos obtener.
     * @return Extent.Has, elemento gráfico que queremos obtener.
     */
    public Extent.Has get(int cnt) {
        return (Extent.Has) data.get(cnt);
    }
    
    /**
     * Devuelve los obhjetos gráficos de la lista cuyos extents contengan al
     * punto que se le pasa como argumento.
     * @param pt, punto para localizar los objetos gráficos.
     * @return IObjList, Conjunto de objetos gráficos que contienen a pt.
     */
    public IObjList getAt(Point2D pt) {
        IObjList oList = new DxfEntityList(proj);
        Iterator iter = iterator();

        while (iter.hasNext()) {
            Extent.Has o = (Extent.Has) iter.next();

            if (o.getExtent().isAt(pt)) {
                oList.add(o);
            }
        }

        return oList;
    }
    
    /**
     * Devuelve un iterador para recorrer los elementos de la lista de objetos gráficos.
     * @return Iterator, iterador.
     */
    public Iterator iterator() {
        return data.iterator();
    }
    
    /**
     * Devuelve la cantidad de elementos que contiene la lista de objetos gráficos.
     * @return int
     */
    public int size() {
        return data.size();
    }
    
    /**
     * Permite eliminar un elemento de la lista de objetos gráficos.
     * @param obj, Objeto que queremos eliminar.
     */
    public void remove(Object obj) {
        data.remove(obj);
    }
    
    /**
     * Permite vacíar la lista de objetos gráficos.
     */
    public void clear() {
        extent = new Extent();
        data.clear();
    }
    
    /**
     * Devuelve la proyección cartográfica en la que se encuentra la DxfEntityList.
     * @return IProjection, proyección cartográfica.
     */
    public IProjection getProjection() {
        return proj;
    }
    
    /**
     * Establece la proyección cartográfica en la que se encuentra la DxfEntityList.
     * @param p, Proyección cartográfica.
     */
    public void setProjection(IProjection p) {
        proj = p;
    }
    
    /**
     * Permite cambiar la proyección en la que se encuentra la DxfEntityList a
     * través de un conjunto de coordenadas de transformación.
     * @param rp, Coordenadas de transformación.
     */
    public void reProject(ICoordTrans rp) {
        extent = new Extent();

        PxObj o;
        Iterator iter = iterator();

        while (iter.hasNext()) {
            o = (PxObj) iter.next();
            ((Projected) o).reProject(rp);
            extent.add(o.getExtent());
        }

        setProjection(rp.getPDest());
    }
    
    /**
     * Permite dibujar las entidades que conforman la DxfEntityList.
     */
    public void draw(Graphics2D g, ViewPortData vp) {
        Iterator iter = iterator();
        Extent extent;

        while (iter.hasNext()) {
            DxfEntity entity = (DxfEntity) iter.next();
            extent = entity.getExtent();

            if (vp.getExtent().minX() > extent.maxX()) {
                continue;
            }

            if (vp.getExtent().minY() > extent.maxY()) {
                continue;
            }

            if (vp.getExtent().maxX() < extent.minX()) {
                continue;
            }

            if (vp.getExtent().maxY() < extent.minY()) {
                continue;
            }

            if (!entity.layer.frozen && !entity.layer.isOff) {
                entity.draw(g, vp);
            }
        }
    }
    
    /**
     * Permite la escritura de entidades en un fichero DXF2000.
     * @return String, la cadena que se escribirá en el fichero con la información
     * de la correspondiente entidad.
     */
    public String toDxfString() {
        StringBuffer sb = new StringBuffer("");

        Iterator iter = iterator();
        Extent extent;

        while (iter.hasNext()) {
            sb.append(((DxfEntity) iter.next()).toDxfString());
        }

        return sb.toString();
    }
}
