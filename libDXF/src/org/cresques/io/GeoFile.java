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

import java.awt.geom.AffineTransform;
import java.util.Date;

import org.cresques.cts.ICoordTrans;
import org.cresques.cts.IProjection;
import org.cresques.geo.Projected;
import org.cresques.px.Extent;
import org.cresques.px.IObjList;


/**
 * Ancestro de todos los formatos geográficos
 *
 * @author "Luis W. Sevilla" <sevilla_lui@gva.es>* @author administrador
 */
public abstract class GeoFile implements Projected, Extent.Has {
    IProjection proj = null;
    /**
     * Extent completo del raster. Este contiene las coordenadas reales tanto
     * para un raster rotado como sin rotar. Este extent coincide con requestExtent
     * cuando el raster no tiene rotación.
     */
    protected Extent extent = null;
    /**
     * Este es el extent sobre el que se ajusta una petición para que esta no exceda el
     * extent máximo del raster. Para un raster sin rotar será igual al extent
     * pero para un raster rotado será igual al extent del raster como si no
     * tuviera rotación. Esto ha de ser así ya que la rotación solo se hace sobre la
     * vista y las peticiones han de hacerse en coordenadas de la imagen sin shearing
     * aplicado.
     */
    protected Extent requestExtent = null;
    /**
     * Esto corresponde a la transformación del extent de la imagen. Se calcula a partir del extent
     * guardado en el fichero .rmf asociado a la imagen.  En caso de que no exista este fichero no habrá
     * transformación
     */
    protected AffineTransform	transformRMF = null;
    /**
     * Esto corresponde a la transformación del extent de la imagen. Se calcula a partir del extent
     * guardado en el fichero .tfw asociado a la imagen o en la cabecera de la misma.
     */
    protected AffineTransform	transformTFW = null;


    protected boolean			rmfExists = false;
    long fileSize = 0;
    protected long bytesReaded = 0;
    protected long lineCnt = 0;
    String name;

    public GeoFile() {
    }

    public GeoFile(IProjection p, String n) {
        proj = p;
        name = n;

        if (name != null) {
            name = DataSource.normalize(name);
        }
        extent = new Extent();
      	transformRMF = new AffineTransform();
    	transformTFW = new AffineTransform();
    }

    public String getName() {
        return name;
    }

    public void setName(String n) {
        name = n;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long sz) {
        fileSize = sz;
    }

    public IProjection getProjection() {
        return proj;
    }

    public void setProjection(IProjection p) {
        proj = p;
    }

    abstract public void reProject(ICoordTrans rp);

    /**
     * Extent completo del raster. Este contiene las coordenadas reales tanto
     * para un raster rotado como sin rotar. Este extent coincide con requestExtent
     * cuando el raster no tiene rotación.
     * @return Extent
     */
    public Extent getExtent() {
        return extent;
    }

    /**
     * Este es el extent sobre el que se ajusta una petición para que esta no exceda el
     * extent máximo del raster. Para un raster sin rotar será igual al extent
     * pero para un raster rotado será igual al extent del raster como si no
     * tuviera rotación. Esto ha de ser así ya que la rotación solo se hace sobre la
     * vista y las peticiones han de hacerse en coordenadas de la imagen sin shearing
     * aplicado.
     * @return Extent
     */
    public Extent getExtentForRequest() {
        return requestExtent;
    }

    abstract public GeoFile load() throws Exception;

    abstract public void close();

    abstract public IObjList getObjects();

    /**
     * Filtra espacios en blanco. Deja solo uno por
     */
    public static String filterWS(String buf) {
        boolean lastCharWhite = false;
        String str = "";
        buf = buf.trim();

        for (int i = 0; i < buf.length(); i++) {
            char c = buf.charAt(i);

            if (Character.isWhitespace(c)) {
                if (lastCharWhite) {
                    continue;
                }

                lastCharWhite = true;
                c = ' ';
            } else {
                lastCharWhite = false;
            }

            str += c;
        }

        return str;
    }

    protected long getTime() {
        return (new Date()).getTime();
    }
}
