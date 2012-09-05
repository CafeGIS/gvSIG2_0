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
 * Entidad BLOCK de un fichero DXF.
 * @author jmorell
 */
public class DxfBlock extends PxObj implements IObjList {
    IProjection proj = null;
    Vector data = null;
    int flags = 0;
    String blkName = "";
    Point2D bPoint = new Point2D.Double();
    
    /**
     * Constructor de DxfBlock.
     * @param proj, proyección cartográfica en la que se encuentra el DxfBlock.
     */
    public DxfBlock(IProjection proj) {
        extent = new Extent();
        data = new Vector();
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
     * Devuelve uno de los elementos de la lista de objetos gráficos.
     * @param i, Índice del elemento de la lista que queremos obtener.
     * @return Extent.Has, elemento gráfico que queremos obtener.
     */
    public Extent.Has get(int i) {
        return (Extent.Has) data.get(i);
    }
    
    /**
     * Devuelve la proyección cartográfica en la que se encuentra el DxfBlock.
     * @return IProjection, proyección cartográfica.
     */
    public IProjection getProjection() {
        return proj;
    }
    
    /**
     * Establece la proyección cartográfica en la que se encuentra el DxfBlock.
     * @param p, Proyección cartográfica.
     */
    public void setProjection(IProjection p) {
        proj = p;
    }
    
    /**
     * Permite reproyectar un DxfBlock dado un conjunto de coordenadas de transformación.
     * @param rp, coordenadas de transformación.
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
     * Permite dibujar un DxfBlock.
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

            if (!entity.layer.frozen) {
                entity.draw(g, vp);
            }
        }
    }
    
    /**
     * Permite la escritura de entidades DxfBlock en un fichero DXF2000.
     * @return String, la cadena que se escribirá en el fichero con la información
     * del DxfBlock.
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
    
    /**
     * Devuelve el estado de flags.
     * @return int
     */
    public int getFlags() {
        return flags;
    }
    
    /**
     * Devuelve el nombre del bloque.
     * @return String
     */
    public String getBlkName() {
        return blkName;
    }
    
    /**
     * Devuelve un Vector con los elementos que conforman el bloque.
     * @return Vector
     */
    public Vector getBlkElements() {
        return data;
    }
    
    /**
     * Establece los elementos que conforman el bloque.
     * @param blkElements
     */
    public void setBlkElements(Vector blkElements) {
        data = blkElements;
    }
    
    /**
     * Establece el nombre del bloque.
     * @param blkName
     */
    public void setBlkName(String blkName) {
        this.blkName = blkName;
        System.out.println("setBlkName: this.blkName = " + this.blkName);
    }
    
    /**
     * Establece el punto base del bloque.
     * @param basePoint
     */
    public void setBPoint(Point2D basePoint) {
        this.bPoint = basePoint;
        System.out.println("setBPoint: this.bPoint = " + this.bPoint);
    }
    
    /**
     * Devuelve el punto base del bloque.
     * @return
     */
    public Point2D getBPoint() {
        return bPoint;
    }
    
    /**
     * Establece el estado de la variable flags.
     * @param flags
     */
    public void setFlags(int flags) {
        this.flags = flags;
    }
}
