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

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;


/**
 * @author "Luis W. Sevilla" <sevilla_lui@gva.es>* @author administrador
 */
public class ZipFileFolder extends FileFolder {
    String zName = null;
    public ZipFile file = null;

    public ZipFileFolder() {
        super();
    }

    /**
     * Constructor.
     *
     * @param fname
     */
    public ZipFileFolder(String fName) throws IOException {
        fName = DataSource.normalize(fName);

        if (isUrl(fName)) {
            zName = getZName(fName);
        } else {
            zName = fName;
        }

        file = new ZipFile(zName);
    }

    /**
     * Analiza un nombre de fichero en formato zip://zipname.zip?file.ext
     * @param urlName
     */
    public static boolean isUrl(String name) {
        String str = name.substring(0, 3);
        str.toLowerCase();

        if (str.compareTo("zip") == 0) {
            return true;
        }

        return false;
    }

    private String getZName(String urlName) {
        return urlName.substring(6, urlName.indexOf("?"));
    }

    private String getFName(String urlName) {
        return urlName.substring(urlName.indexOf("?") + 1);
    }

    public ZipEntry getZipEntry(String fName) throws IOException {
        InputStream is = null;

        if (isUrl(fName)) {
            fName = getFName(fName);
        }

        return file.getEntry(fName);
    }

    public InputStream getInputStream(String fName) throws IOException {
        return file.getInputStream(getZipEntry(fName));
    }

    public InputStream getInputStream(ZipEntry ze) throws IOException {
        return file.getInputStream(ze);
    }

    public int count() {
        return file.size();
    }

    public Enumeration entries() {
        return file.entries();
    }
}
