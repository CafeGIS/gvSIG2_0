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
import java.awt.geom.Point2D;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import org.cresques.cts.ICoordTrans;
import org.cresques.cts.IProjection;
import org.cresques.geo.ViewPortData;
import org.cresques.px.Extent;
import org.cresques.px.IObjList;
import org.cresques.px.dxf.DxfEntityList;


/**
 * FeatureCollection de .gml y .shp
 * @author "Luis W. Sevilla" <sevilla_lui@gva.es>
 * jmorell: A�adidas a FeatureCollection las capacidades de almacenamiento propias de
 * Feature mediante herencia.
 */
public class FeatureCollection extends Feature implements IObjList.vector {
    IProjection proj = null;
    public Vector data = null;
    
    /**
     * Constructor de FeatureCollection.
     * @param proj, Proyecci�n cartogr�fica en la que se encuentra la FeatureCollection.
     */
    public FeatureCollection(IProjection proj) {
        extent = new Extent();
        data = new Vector();
        property = new Hashtable();
    }
    
    /**
     * Permite a�adir un objeto gr�fico a la lista.
     * @param obj, Objeto gr�fico que implemente el interface Extent.Has
     */
    public void add(Extent.Has feature) {
        Extent ext = feature.getExtent();

        if (extent != null) {
            extent.add(ext);
            data.add(feature);
        }
    }
    
    /**
     * Devuelve los obhjetos gr�ficos de la lista cuyos extents contengan al
     * punto que se le pasa como argumento.
     * @param pt, punto para localizar los objetos gr�ficos.
     * @return IObjList, Conjunto de objetos gr�ficos que contienen a pt.
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
     * Devuelve un iterador para recorrer los elementos de la lista de objetos gr�ficos.
     * @return Iterator, iterador.
     */
    public Iterator iterator() {
        return data.iterator();
    }
    
    /**
     * Devuelve la cantidad de elementos que contiene la lista de objetos gr�ficos.
     * @return int
     */
    public int size() {
        return data.size();
    }
    
    /**
     * Permite eliminar un elemento de la lista de objetos gr�ficos.
     * @param obj, Objeto que queremos eliminar.
     */
    public void remove(Object obj) {
        data.remove(obj);
    }
    
    /**
     * Permite vac�ar la lista de objetos gr�ficos.
     */
    public void clear() {
        extent = new Extent();
        data.clear();
    }
    
    /**
     * Devuelve uno de los elementos de la lista de objetos gr�ficos.
     * @param i, �ndice del elemento de la lista que queremos obtener.
     * @return Extent.Has, elemento gr�fico que queremos obtener.
     */
    public Extent.Has get(int i) {
        return (Extent.Has) data.get(i);
    }
    
    /**
     * Devuelve la proyecci�n cartogr�fica en la que se encuentra la FeatureCollection.
     * @return IProjection, proyecci�n cartogr�fica.
     */
    public IProjection getProjection() {
        return proj;
    }
    
    /**
     * Establece la proyecci�n cartogr�fica en la que se encuentra la FeatureCollection.
     * @param p, Proyecci�n cartogr�fica.
     */
    public void setProjection(IProjection p) {
        proj = p;
    }
    
    /**
     * Permite cambiar la proyecci�n en la que se encuentra la FeatureCollection a
     * trav�s de un conjunto de coordenadas de transformaci�n.
     * @param rp, Coordenadas de transformaci�n.
     */
    public void reProject(ICoordTrans rp) {
        extent = new Extent();

        Feature f;
        Geometry g;
        Iterator iter = iterator();

        while (iter.hasNext()) {
            f = (Feature) iter.next();
            g = f.getGeometry();
            g.reProject(rp);
            extent.add(g.getExtent());
        }

        setProjection(rp.getPDest());
    }
    
    /**
     * Permite dibujar las Features que conforman la FeatureCollection.
     */
    public void draw(Graphics2D g, ViewPortData vp) {
        Iterator iter = iterator();

        while (iter.hasNext()) {
            Feature f = (Feature) iter.next();
            f.draw(g, vp);
        }
    }
    
    /**
     * Permite obtener el extent de la FeatureCollection.
     * @return Extent, rect�ngulo en donde se ubican las Features que conforman la
     * FeatureCollection.
     */
    public Extent getExtent() {
        return extent;
    }
}
