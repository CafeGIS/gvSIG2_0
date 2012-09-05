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

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.Vector;

import org.cresques.cts.ICoordTrans;
import org.cresques.cts.IProjection;
import org.cresques.geo.Point3D;
import org.cresques.geo.ViewPortData;

/**
 * Punto de inserción. Se trata de un Point al que se le han añadido los atributos
 * necesarios para hacer referencia a un conjunto de objetos gráficos o bloque.
 * Las funciones de gestión de bloques estan basadas en la clase DxfInsert.
 * @author jmorell
 */
public class InsPoint extends Geometry {
    public static int pointNr = 0;
    public String text = null;
    String blockName;
    Point3D scaleFactor;
    double rotAngle;
    private FeatureCollection block = new FeatureCollection(proj);
    Vector blkList;
    boolean blockFound = false;
    private Color fColor = null; //new Color(255,222,165,64);
    private Color color = new Color(255, 0, 0); //Color(255,214,132,255);
    
    /**
     * Constructor de la clase.
     */
    public InsPoint() {
        super();
    }
    
    /**
     * Permite añadir un punto.
     * @param pt, Punto.
     */
    public void add(Point2D pt) {
        pointNr++;
        super.add(pt);
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
    public Point3D getScaleFactor() {
        return scaleFactor;
    }
    
    /**
     * Devuelve la lista de bloques.
     * @return Vector, la lista de bloques.
     */
    public Vector getBlkList() {
        return blkList;
    }
    
    /**
     * Informa sobre si el bloque ya ha sido encontrado.
     * @return boolean, true si el bloque ya ha sido encontrado.
     */
    public boolean getBlockFound() {
        return blockFound;
    }
    
    /**
     * Establece el bloque al que hace referencia este punto de inserción.
     * @param block, bloque.
     */
    public void setBlock(FeatureCollection block) {
        this.block = block;
    }
    
    /**
     * Establece si el bloque ya ha sido encontrado.
     * @param found, true si el bloque ya ha sido encontrado.
     */
    public void setBlockFound(boolean found) {
        blockFound = found;
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
     * @return FeatureCollection, el bloque.
     */
    public FeatureCollection getBlock() {
        return block;
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
    public void setScaleFactor(Point3D scaleFactor) {
        this.scaleFactor = scaleFactor;
    }
    
    /**
     * Establece el ángulo de rotación para el bloque.
     * @param rotAngle, ángulo de rotación.
     */
    public void setRotAngle(double rotAngle) {
        this.rotAngle = rotAngle;
    }
    
    /**
     * Método para cargar el bloque al que hace referencia el punto de inserción, en el
     * propio InsPoint.
     * @param blockName, nombre del bloque que estamos buscando.
     * @return boolean, true si hemos encontrado el bloque que buscabamos y ha sido
     * cargado.
     */
    public boolean encuentraBloque(String blockName) {
        int i = 0;

        while ((i < blkList.size()) && (blockFound == false)) {
            //System.out.println("encuentraBloque: ((DxfBlock)blkList.get(i)).getBlkName() = " + ((FeatureCollection)blkList.get(i)).getProp("blockName"));
            //System.out.println("encuentraBloque: blockName = " + blockName);
            if (((FeatureCollection) blkList.get(i)).getProp("blockName")
                     .equals(blockName)) {
                //System.out.println("encuentraBloque: Bloque " + i + " encontrado.");
                block = (FeatureCollection) blkList.get(i);
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
     * Devuelve el color del punto de inserción.
     * @return Color
     */
    public Color c() {
        return color;
    }
    
    /**
     * Establece el color del punto de inserción.
     * @param color
     * @return Color
     */
    public Color c(Color color) {
        this.color = color;

        return color;
    }
    
    /**
     * Devuelve el color del relleno.
     * @return Color
     */
    public Color fillColor() {
        return fColor;
    }
    
    /**
     * Establece el color de relleno.
     * @param c, color.
     * @return Color
     */
    public Color fillColor(Color c) {
        fColor = c;

        return fColor;
    }
    
    /**
     * Devuelve la proyección cartográfica en la que se encuentra el InsPoint.
     * @return IProjection, proyección cartográfica.
     */
    public IProjection getProjection() {
        return proj;
    }
    
    /**
     * Establece la proyección cartográfica en la que se encuentra el InsPoint.
     * @param p, proyección cartográfica.
     */
    public void setProjection(IProjection p) {
        proj = p;
    }
    
    /**
     * Permite cambiar la proyección cartográfica del InsPoint a través de unas
     * coordenadas de transformación.
     * @param rp, coordenadas de transformación.
     */
    public void reProject(ICoordTrans rp) {
        // TODO metodo reProject pendiente de implementar
    }
    
    /**
     * Permite dibujar el InsPoint.
     */
    public void draw(Graphics2D g, ViewPortData vp) {
        g.setColor(c());

        Point2D pt = new Point2D.Double(0D, 0D);
        vp.mat.transform((Point2D) data.get(0), pt);
        g.draw(new Line2D.Double(pt, pt));

        if (text != null) {
            g.drawString(text, (int) pt.getX(), (int) pt.getY());
        }
    }
}
