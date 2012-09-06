/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2007 IVER T.I. and Generalitat Valenciana.
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
 */
package org.gvsig.raster.buffer;



public abstract class RasterBand implements IBand{	
	public double 			noDataValue = -99999;
    protected int 			width;
    protected int 			height;
    protected int 			dataType;
    
    /**
     * Constructor a llamar desde las clases hijas
     * @param width
     * @param height
     */
    public RasterBand(int height, int width){
    	this.width = width;
		this.height = height;
    }
    
    /*
     *  (non-Javadoc)
     * @see org.gvsig.fmap.dataaccess.buffer.IBand#getWidth()
     */
    public int getWidth() {
        return width;
    }

   /*
    *  (non-Javadoc)
    * @see org.gvsig.fmap.dataaccess.buffer.IBand#getHeight()
    */
    public int getHeight() {
        return height;
    }

    /*
     *  (non-Javadoc)
     * @see org.gvsig.fmap.dataaccess.buffer.IBand#getDataType()
     */
	public int getDataType() {
		return dataType;
	}
	
	
	public void setDataType(int dataType) {
		this.dataType = dataType;
	}

    /**
     * Obtiene el tamaño del tipo de dato en bytes
     * @return Tipo de dato
     */
    public int getDataSize() {
        if (dataType == TYPE_BYTE) {
            return 1;
        } else if ((dataType == TYPE_SHORT) | (dataType == TYPE_USHORT)) {
            return 2;
        } else if (dataType == TYPE_INT) {
            return 4;
        }else if (dataType == TYPE_FLOAT) {
            return 8;
        }else if (dataType == TYPE_DOUBLE) {
            return 16;
        }

        return 0;
    }

    /**
     * Obtiene el tamaño del buffer
     * @return tamaño del buffer
     */
    public int sizeof() {
        return getDataSize() * width * height;
    }

}
