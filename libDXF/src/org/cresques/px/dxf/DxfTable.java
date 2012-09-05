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

import java.util.Vector;


/**
 * Lista (Tabla) de capas del fichero DXF.
 * @author "Luis W. Sevilla" <sevilla_lui@gva.es>
 */
public class DxfTable {
    public Vector items = null;
    public boolean createNotFoundLayers = true;

    /**
     * Constructor de DxfTable.
     */
    public DxfTable() {
        items = new Vector();
    }

    /**
     * Añade un elemento a la tabla. En la actualidad esta table solo está implementada
     * para soportar capas.
     * @param layer, la capa que añadimos.
     */
    public void add(DxfTableItem layer) {
        items.add(layer);
    }

    /**
     * Devuelve una de las capas de la tabla de capas dada por su nombre.
     * @param name, nombre de la capa que queremos obtener.
     * @return DxfTableItem, la capa que buscamos.
     */
    public DxfTableItem getByName(String name) {
        DxfTableItem capa = null;

        for (int i = 0; i < items.size(); i++) {
            capa = (DxfTableItem) items.get(i);

            if (capa.getName().compareToIgnoreCase(name) == 0) {
                return capa;
            }
        }

        if (createNotFoundLayers) {
            capa = new DxfLayer(name);
            add(capa);
        }

        return capa;
    }

    /**
     * Permite la escritura de entidades DxfTable en un fichero DXF2000.
     * @return String, la cadena que se escribirá en el fichero con la información
     * de la DxfTable.
     */
    public String toDxfString() {
        String txt = "";

        for (int i = 0; i < items.size(); i++) {
            txt += ((DxfLayer) items.get(i)).toDxfString();
        }

        return txt;
    }
}
