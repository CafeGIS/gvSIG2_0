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
/**
 * This class has some utils to create a VRT file. It has methods to obtaint the
 * VRT parameters that will be written in the file
 * 
 * @author Jorge Piera Llodrá (piera_jor@gva.es)
 */
public class VRTFormatUtils {

	/**
	 * It calculates the ImageOffSet parameter value.
	 * @param dataSize Type of each pixel
	 * @param headerSize Header file size
	 * @param interLeaving Type of interleaving
	 * @param band Band number
	 * @param numBands Number of bands
	 * @param width Image width
	 * @param height Image height
	 * @return ImageOffSet parameter value
	 */
	public static String getImageOffset(int dataSize, int headerSize,
			String interLeaving, int band, int numBands, int width, int height) {
		int imageOffSet = 0;
		if (interLeaving.equals(VRTFormatOptions.INTERLEAVING_PIXEL)) {
			imageOffSet = 1;

		} else
			if (interLeaving.equals(VRTFormatOptions.INTERLEAVING_BAND)) {
				imageOffSet = width * height;

			} else
				if (interLeaving.equals(VRTFormatOptions.INTERLEAVING_LINE)) {
					imageOffSet = width * numBands;
				}
		imageOffSet = imageOffSet * (dataSize / 8);
		imageOffSet = imageOffSet * (band - 1);
		imageOffSet = imageOffSet + headerSize;
		return String.valueOf(imageOffSet);
	}
	
	/**
	 * It calculates the ImageOffSet parameter value.
	 * @param fileCreator
	 * Contains all the params
	 * @param band
	 * Band to obtain the image offset
	 * @return
	 */
	public static String getImageOffset(VRTFileCreator fileCreator, int band) {
		return getImageOffset(fileCreator.getDataSize(), fileCreator.getHeaderSize(),
				fileCreator.getInterleaving(), band, fileCreator.getBands(),
				fileCreator.getImageWidth(), fileCreator.getImageHeight());
	}
	
	/**
	 * It calculates the ImageOffSet parameter value.
	 * @param dataSize Type of each pixel
	 * @param interLeaving Type of interleaving Band number
	 * @param numBands Number of bands
	 * @param width Image width
	 * @return ImageOffSet parameter value
	 */
	public static String getLineOffset(int dataSize, String interLeaving, int numBands, int width) {

		int lineOffSet = 0;
		if (interLeaving.equals(VRTFormatOptions.INTERLEAVING_PIXEL)) {
			lineOffSet = width * numBands;

		} else
			if (interLeaving.equals(VRTFormatOptions.INTERLEAVING_BAND)) {
				lineOffSet = width;

			} else
				if (interLeaving.equals(VRTFormatOptions.INTERLEAVING_LINE)) {
					lineOffSet = width * numBands;
				}

		lineOffSet = lineOffSet * (dataSize / 8);
		return String.valueOf(lineOffSet); 
	}
	
	/**
	 * It calculates the LineOffSet parameter value.
	 * @param fileCreator Contains all the params
	 * @return
	 */
	public static String getLineOffset(VRTFileCreator fileCreator) {
		return getLineOffset(fileCreator.getDataSize(), fileCreator.getInterleaving(), fileCreator.getBands(), fileCreator.getImageWidth());
	}
	
	/**
	 * It calculates the ImageOffSet parameter value.
	 * @param dataSize Type of each pixel
	 * @param interLeaving Type of interleaving
	 * @param numBands Number of bands
	 * @return ImageOffSet parameter value
	 */
	public static String getPixelOffset(int dataSize, String interLeaving, int numBands) {
		int pixelOffSet = 0;
		if (interLeaving.equals(VRTFormatOptions.INTERLEAVING_PIXEL)) {
			pixelOffSet = numBands;

		} else
			if (interLeaving.equals(VRTFormatOptions.INTERLEAVING_BAND)) {
				pixelOffSet = 1;

			} else
				if (interLeaving.equals(VRTFormatOptions.INTERLEAVING_LINE)) {
					pixelOffSet = 1;
				}

		pixelOffSet = pixelOffSet * (dataSize / 8);
		return String.valueOf(pixelOffSet);
	}
	
	/**
	 * It calculates the PixelOffSet parameter value.
	 * @param fileCreator Contains all the params
	 * @return
	 */
	public static String getPixelOffset(VRTFileCreator fileCreator) {
		return getPixelOffset(fileCreator.getDataSize(), fileCreator.getInterleaving(), fileCreator.getBands());
	}
}