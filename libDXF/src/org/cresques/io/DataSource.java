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

import java.util.Hashtable;


/**
 * Origen de datos. Unidad, volumen de red, etc.
 *
 * @author "Luis W. Sevilla" <sevilla_lui@gva.es>
 */
public class DataSource {
    private static int counter = 0;
    private static Hashtable units = new Hashtable();
    String path = null;
    String name = null;

    public DataSource(String path, String name) {
        this.path = path;
        this.name = name;
        units.put(name, this);
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public static DataSource getDSFromName(String name) {
        if (name.indexOf("[") >= 0) {
            name = name.substring(name.indexOf("[") + 1);
        }

        if (name.indexOf("]") >= 0) {
            name = name.substring(0, name.indexOf("]"));
        }

        DataSource ds = (DataSource) units.get(name);

        return ds;
    }

    /**
     * Sustituye en el path el nombre de la unidad por su path real.
     *
     * @param path
     * @return
     */
    public static String normalize(String path) {
        if (path.indexOf("[") >= 0) {
            DataSource ds = DataSource.getDSFromName(path);

            if (ds == null) {
                return null;
            }

            path = path.substring(0, path.indexOf("[")) + ds.getPath() +
                   path.substring(path.indexOf("]") + 1);

            //System.out.println(path);
        }

        return path;
    }

    public String toString() {
        return "[" + counter + "]";
    }
}
