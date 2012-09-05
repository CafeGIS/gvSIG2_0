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


/**
 *
 * @author "Luis W. Sevilla" <sevilla_lui@gva.es>* @author administrador
 */
abstract public class FileFolder {
    static String[] supportedFolders = { "zip://" };

    abstract public int count();

    /**
     * Analiza un nombre de fichero en formato zip://zipname.zip?file.ext
     * @param urlName
     */
    public static boolean isUrl(String name) {
        String str = name.substring(0, 6);
        str.toLowerCase();

        for (int i = 0; i < supportedFolders.length; i++)
            if (str.compareTo(supportedFolders[i]) == 0) {
                return true;
            }

        return false;
    }
}
