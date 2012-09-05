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

import org.cresques.io.DxfGroup;


/**
 * La clase DxfTableItem representa una entrada de una tabla en un fichero DXF. 
 * Classe représentant une table fichier DXF.
 * @author Michael Michaud
 * @documentation jmorell
 */
public class DxfTableItem {
    public final static DxfGroup ENDTAB = new DxfGroup(0, "ENDTAB");
    public final static DxfGroup APPID = new DxfGroup(0, "APPID");
    public final static DxfGroup DIMSTYLE = new DxfGroup(0, "DIMSTYLE");
    public final static DxfGroup LAYER = new DxfGroup(0, "LAYER");
    public final static DxfGroup LTYPE = new DxfGroup(0, "LTYPE");
    public final static DxfGroup STYLE = new DxfGroup(0, "STYLE");
    public final static DxfGroup UCS = new DxfGroup(0, "UCS");
    public final static DxfGroup VIEW = new DxfGroup(0, "VIEW");
    public final static DxfGroup VPORT = new DxfGroup(0, "VPORT");
    private String name;
    public int flags = 0;

    /**
     * Constructor de DxfTableItem.
     * @param name, nombre de la entrada de la tabla.
     * @param flags, estado.
     */
    public DxfTableItem(String name, int flags) {
        this.name = name;
        this.flags = flags;
    }

    /**
     * Devuelve el nombre de la entrada de la tabla.
     * @return String
     */
    public String getName() {
        return name;
    }

    /**
     * Establece el nombre de la entrada de la tabla.
     * @param name, nombre.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Devuelve el estado.
     * @return int
     */
    public int getFlags() {
        return flags;
    }

    /**
     * Establece el estado.
     * @param flags
     */
    public void setFlags(int flags) {
        this.flags = flags;
    }

    /**
     * Devuelve true si el estado es 1.
     * @return boolean
     */
    public boolean getFlag1() {
        return ((flags & 1) == 1);
    }

    /**
     * Devuelve true si el estado es 2.
     * @return boolean
     */
    public boolean getFlag2() {
        return ((flags & 2) == 2);
    }

    /**
     * Devuelve true si el estado es 4.
     * @return boolean
     */
    public boolean getFlag4() {
        return ((flags & 4) == 4);
    }

    /**
     * Devuelve true si el estado es 8.
     * @return boolean
     */
    public boolean getFlag8() {
        return ((flags & 8) == 8);
    }

    /**
     * Devuelve true si el estado es 16.
     * @return boolean
     */
    public boolean getFlag16() {
        return ((flags & 16) == 16);
    }

    /**
     * Devuelve true si el estado es 32.
     * @return boolean
     */
    public boolean getFlag32() {
        return ((flags & 32) == 32);
    }

    /**
     * Devuelve true si el estado es 64.
     * @return boolean
     */
    public boolean getFlag64() {
        return ((flags & 64) == 64);
    }

    /**
     * Devuelve true si el estado es 128.
     * @return boolean
     */
    public boolean getFlag128() {
        return ((flags & 128) == 128);
    }

    /**
     * Permite la escritura de entidades DxfTableItem en un fichero DXF2000.
     * @return String, la cadena que se escribirá en el fichero con la información
     * del DxfTableItem.
     */
    public String toDxfString() {
        return DxfGroup.toString(2, name) + DxfGroup.toString(70, flags);
    }
}
