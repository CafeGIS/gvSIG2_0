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

import org.cresques.io.DxfFile;
import org.cresques.io.DxfGroupVector;


/**
 * La clase DxfHeaderManager gestiona las variables definidas en la sección HEADER de
 * un fichero DXF.
 * @author jmorell (jose.morell@gmail.com)
 * @version 15-dic-2004
 */
public class DxfHeaderManager implements DxfFile.VarSettings {
    private DxfHeaderVariables dxfHeadVars;

    /**
     * Constructor sin parámetros. Inicializa el vector de variables que
     * caracterizan un DXF.
     */
    public DxfHeaderManager() {
        dxfHeadVars = new DxfHeaderVariables();
    }

    /* (non-Javadoc)
     * @see org.cresques.io.DxfFile.VarSettings#getDxfHeaderVars()
     */
    public DxfHeaderVariables getDxfHeaderVars() {
        return dxfHeadVars;
    }

    /* (non-Javadoc)
     * @see org.cresques.io.DxfFile.VarSettings#setAcadVersion(org.cresques.io.DxfGroupVector)
     */
    public void setAcadVersion(DxfGroupVector v) throws Exception {
        if (v.hasCode(1)) {
            String codedVersion = new String(v.getDataAsString(1));
            dxfHeadVars.setAcadVersion(dxfHeadVars.decodeAcadVersion(codedVersion));
        }
    }

    /* (non-Javadoc)
     * @see org.cresques.io.DxfFile.VarSettings#getAcadVersion()
     */
    public String getAcadVersion() {
        return dxfHeadVars.getAcadVersion();
    }

    public boolean isWritedDxf3D() {
        return dxfHeadVars.isWritedDxf3D();
    }

    public void loadMinZFromHeader(double d) {
        dxfHeadVars.loadMinZFromHeader(d);
    }

    public void loadMaxZFromHeader(double d) {
        dxfHeadVars.loadMaxZFromHeader(d);
    }
}
