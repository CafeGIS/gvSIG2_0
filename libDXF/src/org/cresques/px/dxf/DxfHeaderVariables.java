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



/**
 * Definición de las variables que definen un DXF. Estas variables están
 * establecidas en la sección HEADER del propio fichero DXF.
 * @author jmorell (jose.morell@gmail.com)
 * @version 15-dic-2004
 */
public class DxfHeaderVariables {
    private String acadVersion;
    private String acadMaintVer;
    private boolean writedDxf3D;
    private double minZFromHeader;
    private double maxZFromHeader;

    /**
     * Constructor de DxfHeaderVariables.
     */
    public DxfHeaderVariables() {
    }

    /**
     * Devuelve la versión del fichero DXF.
     * @return String
     */
    public String getAcadVersion() {
        return acadVersion;
    }

    /**
     * Establece la versión del fichero DXF.
     * @param acadVersion
     */
    public void setAcadVersion(String acadVersion) {
        //System.out.println(acadVersion);
        this.acadVersion = acadVersion;
    }

    /**
     * Transforma la nomenclatura interna de versiones de ficheros DXF en la
     * nomenclatura habitual de versiones de AutoCAD.
     * @param codedVersion, versión según la nomenclatura interna.
     * @return String con la versión en la nomenclatura más habitual.
     */
    public String decodeAcadVersion(String codedVersion) {
        if (codedVersion.equals("AC1006")) {
            return new String("R10");
        } else if (codedVersion.equals("AC1009")) {
            return new String("R11&R12");
        } else if (codedVersion.equals("AC1012")) {
            return new String("R13");
        } else if (codedVersion.equals("AC1014")) {
            return new String("R14");
        } else if (codedVersion.equals("AC1015")) {
            return new String("ACAD2000");
        } else if (codedVersion.equals("AC1018")) {
            return new String("ACAD2004");
        } else {
            return new String("Unknown codedVersion");
        }
    }
    
    public boolean isWritedDxf3D() {
        // si los ext son como los escribimos devolver true
        writedDxf3D = false;
        double z1 = minZFromHeader;
        double z2 = maxZFromHeader;
        if (z1==999999999.0 && z2==-999999999.0) writedDxf3D = true;
        return writedDxf3D;
    }
    
    public void loadMinZFromHeader(double d) {
        minZFromHeader = d;
    }
    
    public void loadMaxZFromHeader(double d) {
        maxZFromHeader = d;
    }
}
