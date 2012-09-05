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
import java.util.Vector;

import org.cresques.cts.ICoordTrans;
import org.cresques.cts.IProjection;
import org.cresques.geo.ViewPortData;
import org.cresques.px.Extent;


/**
 * Entidad INSERT de un fichero DXF.
 * @author jmorell
 */
public class DxfInsert extends DxfEntity {
    Point2D pt;
    String blockName;
    Point2D scaleFactor;
    double rotAngle;
    DxfBlock block = new DxfBlock(proj);
    Vector blkList;
    boolean blockFound = false;

    /**
     * Constructor de DxfInsert.
     * @param proj, proyección cartográfica en la que se encuentra el DxfInsert.
     * @param layer, capa del DXF en la que se encuentra el DxfInsert.
     */
    public DxfInsert(IProjection proj, DxfLayer layer) {
        super(proj, layer);
        extent = new Extent();
    }

    /**
     * @return Returns the pt.
     */
    public Point2D getPt() {
        return pt;
    }

    /**
     * Devuelve el ángulo de rotación que se aplica al bloque.
     * @return double. Ángulo de rotación.
     */
    public double getRotAngle() {
        return rotAngle;
    }

    /**
     * Devuelve el factor de escala que se aplica al bloque.
     * @return Point3D. Contiene el factor de escala en X, en Y y en Z.
     */
    public Point2D getScaleFactor() {
        return scaleFactor;
    }

    /**
     * Informa sobre si el bloque ya ha sido encontrado.
     * @return boolean, true si el bloque ya ha sido encontrado.
     */
    public boolean getBlockFound() {
        return blockFound;
    }

    /**
     * Establece si el bloque ya ha sido encontrado.
     * @param found, true si el bloque ya ha sido encontrado.
     */
    public void setBlockFound(boolean found) {
        blockFound = found;
    }

    /**
     * Método para cargar el bloque al que hace referencia el punto de inserción, en el
     * propio DxfInsert.
     * @param blockName, nombre del bloque que estamos buscando.
     * @return boolean, true si hemos encontrado el bloque que buscabamos y ha sido
     * cargado.
     */
    public boolean encuentraBloque(String blockName) {
        int i = 0;

        while ((i < blkList.size()) && (blockFound == false)) {
            //System.out.println("encuentraBloque: ((DxfBlock)blkList.get(i)).getBlkName() = " + ((DxfBlock)blkList.get(i)).getBlkName());
            //System.out.println("encuentraBloque: blockName = " + blockName);
            if (((DxfBlock) blkList.get(i)).getBlkName().equals(blockName)) {
                //System.out.println("encuentraBloque: Bloque " + i + " encontrado.");
                block = (DxfBlock) blkList.get(i);
                blockFound = true;
            } else {
                //System.out.println("encuentraBloque: Bloque " + blockName + " no encontrado");
                blockFound = false;
            }

            i++;
        }

        return blockFound;
    }

    /**
     * Devuelve la capa en la que se encuentra el DxfInsert.
     * @return DxfLayer, la capa.
     */
    public DxfLayer getDxfLayer() {
        return layer;
    }

    /**
     * Establece la lista de bloques.
     * @param blkList, Vector con la lista de bloques.
     */
    public void setBlkList(Vector blkList) {
        this.blkList = blkList;
    }

    /**
     * Devuelve el bloque al que hace referencia el punto de inserción.
     * @return DxfBlock, el bloque.
     */
    public DxfBlock getDxfBlock() {
        return block;
    }

    /**
     * Establece el punto de inserción del DxfInsert.
     * @param pt, punto de inserción.
     */
    public void setPt(Point2D pt) {
        this.pt = pt;
        extent.add(pt);
    }

    /**
     * Establece el nombre del bloque.
     * @param blockName, nombre del bloque.
     */
    public void setBlockName(String blockName) {
        this.blockName = blockName;
    }

    /**
     * Devuelve el nombre del bloque al que hace referencia el punto de inserción.
     * @return String, nombre del bloque.
     */
    public String getBlockName() {
        return blockName;
    }

    /**
     * Establece el factor de escala para el bloque.
     * @param scaleFactor, factor de escala en X, en Y y en Z.
     */
    public void setScaleFactor(Point2D scaleFactor) {
        this.scaleFactor = scaleFactor;
    }

    /**
     * Establece el ángulo de rotación para el bloque.
     * @param rotAngle, ángulo de rotación.
     */
    public void setRotAngle(double rotAngle) {
        this.rotAngle = rotAngle;
    }

    /* (non-Javadoc)
     * @see org.cresques.px.dxf.DxfEntity#toDxfFileString()
     */
    public String toDxfString() {
        // TODO Auto-generated method stub
        return "";
    }

    /* (non-Javadoc)
     * @see org.cresques.px.dxf.DxfEntity#reProject(org.cresques.geo.ReProjection)
     */
    public void reProject(ICoordTrans rp) {
        // TODO Auto-generated method stub
    }

    /* (non-Javadoc)
     * @see org.cresques.px.Drawable#draw(java.awt.Graphics2D, org.cresques.geo.ViewPort)
     */
    public void draw(Graphics2D g, ViewPortData vp) {
        // TODO Auto-generated method stub
    }
}
