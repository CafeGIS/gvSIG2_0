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
package org.gvsig.rastertools.raw.tools;

import java.util.Vector;
/**
 * This class contains some information to manage the VRT file formats. It has
 * the possible values for the params.
 * 
 * @author Jorge Piera Llodrá (piera_jor@gva.es)
 */
public class VRTFormatOptions {
	public static final String INTERLEAVING_PIXEL = "Pixel";
	public static final String INTERLEAVING_BAND  = "Band";
	public static final String INTERLEAVING_LINE  = "Line";

	public static final String DATA_TYPE_BYTE     = "Byte";
	public static final String DATA_TYPE_UINT16   = "Uint16";
	public static final String DATA_TYPE_INT16    = "Int16";
	public static final String DATA_TYPE_UINT32   = "UInt32";
	public static final String DATA_TYPE_INT32    = "Int32";
	public static final String DATA_TYPE_FLOAT32  = "Float32";
	public static final String DATA_TYPE_FLOAT64  = "Float64";
	public static final String DATA_TYPE_CINT16   = "CInt16";
	public static final String DATA_TYPE_CINT32   = "CInt32";
	public static final String DATA_TYPE_CFLOAT32 = "CFloat32";
	public static final String DATA_TYPE_CFLOAT64 = "CFloat64";

	public static final String BYTE_ORDER_LSB     = "LSB";
	public static final String BYTE_ORDER_MSB     = "MSB";

	public static final String OUTPUT_FORMAT_VRT  = "VRT";
	
	/**
	 * Return the supported image data types 
	 * @return
	 * One vector of data types
	 */
	public static Vector getDataTypes() {
		Vector vector = new Vector();
		vector.add(new UIOption(DATA_TYPE_BYTE, 8));
		vector.add(new UIOption(DATA_TYPE_UINT32, 16));
		vector.add(new UIOption(DATA_TYPE_INT16, 16));
		vector.add(new UIOption(DATA_TYPE_UINT32, 32));
		vector.add(new UIOption(DATA_TYPE_INT32, 32));
		vector.add(new UIOption(DATA_TYPE_FLOAT32, 32));
		vector.add(new UIOption(DATA_TYPE_FLOAT64, 64));
		vector.add(new UIOption(DATA_TYPE_CINT16, 16));
		vector.add(new UIOption(DATA_TYPE_CINT32, 32));
		vector.add(new UIOption(DATA_TYPE_CFLOAT32, 32));
		vector.add(new UIOption(DATA_TYPE_CFLOAT64, 64));
		return vector;
	}

	/**
	 * Return the supported byte order
	 * @return
	 * One vector of byte order
	 */
	public static Vector getByteOrder() {
		Vector vector = new Vector();
		vector.add(new UIOption(BYTE_ORDER_LSB, "LSB (Swapped)"));
		vector.add(new UIOption(BYTE_ORDER_MSB, "MSB (Unswapped)"));
		return vector;
	}

	/**
	 * Return the types of interleavings
	 * @return
	 *One vector  of interleavings
	 */
	public static Vector getInterleaving() {
		Vector vector = new Vector();
		vector.add(new UIOption(INTERLEAVING_PIXEL));
		vector.add(new UIOption(INTERLEAVING_BAND));
		vector.add(new UIOption(INTERLEAVING_LINE));
		return vector;
	}

	/**
	 * Return the supported output header formats
	 * @return
	 * One vector of header formats
	 */
	public static Vector getOutputHeaderFormats() {
		Vector vector = new Vector();
		vector.add(new UIOption(OUTPUT_FORMAT_VRT));
		return vector;
	}

	/**
	 * This class is used to separate the string that is showes in the user
	 * interface of the other string used in the vrt file format.
	 * 
	 * @author Jorge Piera Llodrá (piera_jor@gva.es)
	 */
	public static class UIOption {
		private String uiName        = null;
		private String vrtOptionName = null;
		private int    dataSize      = 0;

		public UIOption(String vrtOptionName, String uiName) {
			super();
			this.uiName = uiName;
			this.vrtOptionName = vrtOptionName;
		}

		public UIOption(String vrtOptionName) {
			super();
			this.uiName = vrtOptionName;
			this.vrtOptionName = vrtOptionName;
		}

		public UIOption(String vrtOptionName, int dataSize) {
			super();
			this.uiName = vrtOptionName;
			this.vrtOptionName = vrtOptionName;
			this.dataSize = dataSize;
		}

		/**
		 * @return Returns the uiName.
		 */
		public String getUiName() {
			return uiName;
		}

		/**
		 * @return Returns the vrtOptionName.
		 */
		public String getVrtOptionName() {
			return vrtOptionName;
		}

		/**
		 * @return Returns the uiName.
		 */
		public String toString() {
			return uiName;
		}

		/**
		 * @return Returns the dataSize.
		 */
		public int getDataSize() {
			return dataSize;
		}
	}
}