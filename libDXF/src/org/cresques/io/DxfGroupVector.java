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
package org.cresques.io;

import java.util.Vector;


/**
 * Vector de DxfGroup. Auxiliar para leer ficheros .dxf
 * @author "Luis W. Sevilla" <sevilla_lui@gva.es>
 */
public class DxfGroupVector extends Vector {
    final private static long serialVersionUID = -3370601314380922368L;

    /**
     * Valor del dato del codigo de grupo especificado.
     *
     * @param code Código de grupo dxf.
     * @return Valor del data del grupo dxf, null si no existe.
     */
    public boolean hasCode(int code) {
        DxfGroup grp = null;

        for (int i = 0; i < size(); i++) {
            grp = (DxfGroup) get(i);

            if (grp.code == code) {
                return true;
            }
        }

        return false;
    }

    /**
     * Devuelve la información contenida en un DxfGroup.
     * @param code, la parte concreta del DxfGroup a la que queremos acceder
     * @return
     */
    public Object getData(int code) {
        DxfGroup grp = null;

        for (int i = 0; i < size(); i++) {
            grp = (DxfGroup) get(i);

            if (grp.code == code) {
                return grp.data;
            }
        }

        return null;
    }

    /**
     * Obtiene la información contenida en un DxfGroup en forma de String
     * @param code
     * @return
     */
    public String getDataAsString(int code) {
        return (String) getData(code);
    }

    /**
     * Obtiene la información contenida en un DxfGroup en forma de double
     * @param code
     * @return
     */
    public double getDataAsDouble(int code) {
        Double f = (Double) getData(code);

        if (f == null) {
            System.err.println(this);
            return 0;
        }

        return f.doubleValue();
    }

    /**
     * Obtiene la información contenida en un DxfGroup en forma de integer
     * @param code
     * @return
     */
    public int getDataAsInt(int code) {
        Integer i = (Integer) getData(code);

        if (i == null) {
            System.err.println(this);
        }

        return i.intValue();
    }

    /**
     * Permite obtener el contenido de un DxfGroup en forma de String
     */
    public String toString() {
        String str = "DxfGroupVector[";
        DxfGroup grp = null;

        for (int i = 0; i < size(); i++) {
            grp = (DxfGroup) get(i);
            str += ("(" + grp.code + ":" + grp.data + "),");
        }

        return str;
    }
}
