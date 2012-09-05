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

import java.awt.Color;

import org.cresques.io.DxfGroup;


/**
 * La clase DxfLayer representa las capas de AutoCAD que aparecen en un fichero
 * DXF. 
 * @author "Luis W. Sevilla" <sevilla_lui@gva.es>
 * @author jmorell
 */
public class DxfLayer extends DxfTableItem {
    public int colorNumber = 9;
    public String lType = "CONTINUOUS";
    public boolean frozen = false;
    public boolean isOff = false;

    /**
     * Constructor de DxfLayer.
     * @param name, nombre de la capa.
     */
    public DxfLayer(String name) {
        super(name, 0);
        System.out.println("Dxf: Capa '" + name + "'.");
        colorNumber = 255;
        lType = "CONTINUOUS";
    }

    /**
     * Sobrecarga del constructor de DxfLayer. Mediante este constructor también es
     * posible especificar el color de la capa.
     * @param name, nombre de la capa.
     * @param clr, color de la capa.
     */
    public DxfLayer(String name, int clr) {
        super(name, 0);
        colorNumber = clr;
        lType = "CONTINUOUS";
        System.out.println("DxfLayer name=" + name + ", color=" + colorNumber);
    }

    /**
     * Sobrecarga del constructor de DxfLayer. Mediante este constructor es
     * posible especificar el nombre, el color y el tipo de línea asociado a la capa.
     * @param name, nombre de la capa.
     * @param clr, color de la capa.
     * @param lType, tipo de línea asociado a la capa.
     */
    public DxfLayer(String name, int clr, String lType) {
        super(name, 0);
        colorNumber = clr;
        this.lType = lType;
    }

    /**
     * Devuelve el color asociado a la capa.
     * @return Color
     */
    public Color getColor() {
        return AcadColor.getColor(colorNumber);
    }

    /**
     * Permite la escritura de capas en un fichero DXF2000.
     * @return String, la cadena que se escribirá en el fichero con la información
     * de la capa.
     */
    public String toDxfString() {
        /*5
        10
        330
        2
        100
        AcDbSymbolTableRecord
        100
        AcDbLayerTableRecord
          2
        0
         70
             0
         62
             7
          6
        Continuous
        370
            -3
        390
        F*/
        StringBuffer sb = new StringBuffer(LAYER.toString() +
                                           super.toDxfString());
        sb.append(DxfGroup.toString(5, 10));
        sb.append(DxfGroup.toString(100, "AcDbSymbolTableRecord"));
        sb.append(DxfGroup.toString(100, "AcDbLayerTableRecord"));
        sb.append(DxfGroup.toString(62, colorNumber));
        sb.append(DxfGroup.toString(6, lType));

        return sb.toString();
    }
}
