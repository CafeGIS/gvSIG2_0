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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
/**
 * This class is user to create a VRT file. It writes the file when the
 * "writeFile" method is called. If some params are not seted the deafult values
 * will bi used.
 * 
 * @author Jorge Piera Llodrá (piera_jor@gva.es)
 */
public class VRTFileCreator {
	private Writer writer             = null;
	private File   m_File             = null;
	private int    width              = 0;
	private int    height             = 0;
	private int    bands              = 0;
	private int    headerSize         = 0;
	private int    fileSize           = 0;
	private String rawFile            = null;
	private String dataType           = null;
	private int    dataSize           = 0;
	private String byteOrder          = null;
	private String interleaving       = null;
	private String outputHeaderFormat = null;
	
	public VRTFileCreator(File m_File) {
		super();		
		this.m_File = m_File;
	}
	
	public VRTFileCreator(String file) {
		super();		
		this.m_File = new File(file);
	}
	
	/**
	 * It writes the VRT file
	 * @throws IOException
	 */
	public void writeFile() throws IOException{
		if (!m_File.exists()){
			m_File.createNewFile();
		}
		writer = new FileWriter(m_File);
		createVRTFile();
		writer.close();	
	}
	
	/**
	 * It creates the VRT File
	 * @throws IOException
	 */
	private void createVRTFile() throws IOException{
		writer.write(getXMLVRTDatasetHeaderTag());
		
		for (int i=0 ; i<getBands() ; i++){
			createOneBand(i+1);
		}
		
		writer.write(getXMLVRTDatasetEndTag());
	}
	
	/**
	 * IT creates all the XML tags for one band
	 * @param band Band number
	 * @throws IOException
	 */
	private void createOneBand(int band) throws IOException {
		writer.write(getXMLVRTRasterBandHeaderTag(band));
		writer.write(getXMLSourceFilenameTag());
		writer.write(getXMLByteOrderTag());
		writer.write(getXMLImageOffsetTag(band));
		writer.write(getXMLPixelOffsetTag());
		writer.write(getXMLLineOffsetTag());
		writer.write(getXMLVRTRasterBandEndTag());
	}

	/**
	 * Creates the header of the VRTDataset XML tag. It contains the width and the
	 * height.
	 * @return The VRTDataset XML tag
	 */
	private String getXMLVRTDatasetHeaderTag() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<VRTDataset rasterXSize=\"" + getImageWidth() + "\" ");
		buffer.append("rasterYSize=\"" + getImageHeight() + "\">\n");
		return buffer.toString();
	}	
	
	/**
	 * Creates the end of the VRTDataset XML tag
	 * @return The VRTDataset XML tag
	 */
	private String getXMLVRTDatasetEndTag() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("</VRTDataset>\n");
		return buffer.toString();
	}
	
	/**
	 * Creates the header of the VRTRasterBand XML tag. It contains the image data
	 * type.
	 * @return The VRTRasterBand XML tag
	 */
	private String getXMLVRTRasterBandHeaderTag(int band) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("\t<VRTRasterBand dataType=\"" + getDataType() + "\" ");
		buffer.append("band=\"" + band + "\" ");
		buffer.append("subClass=\"VRTRawRasterBand\">\n");
		return buffer.toString();
	}

	/**
	 * Creates the end of the VRTRasterBand XML tag
	 * @return The VRTRasterBand XML tag
	 */
	private String getXMLVRTRasterBandEndTag() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("\t</VRTRasterBand>\n");
		return buffer.toString();
	}		
	
	
	/**
	 * Creates the SourceFilename XML tag: The raw file path
	 * @return The SourceFilename XML tag
	 */
	private String getXMLSourceFilenameTag() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("\t\t<SourceFilename relativetoVRT=\"1\">");
		buffer.append(getRawFile());
		buffer.append("</SourceFilename>\n");
		return buffer.toString();
	}

	/**
	 * Creates the ByteOrder XML tag. Possible values are specified in
	 * VRTFileOptions class.
	 * @return The ByteOrder XML tag
	 */
	private String getXMLByteOrderTag() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("\t\t<ByteOrder>");
		buffer.append(getByteOrder());
		buffer.append("</ByteOrder>\n");
		return buffer.toString();
	}	
	
	/**
	 * Creates the ImageOffset XML tag: The image data starts from the byte number
	 * specified by this value
	 * @return The ImageOffset XML tag
	 */
	private String getXMLImageOffsetTag(int band) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("\t\t<ImageOffset>");
		buffer.append(VRTFormatUtils.getImageOffset(this, band));
		buffer.append("</ImageOffset>\n");
		return buffer.toString();
	}

	/**
	 * Creates the PixelOffset XML tag: The byte offeset between pixels
	 * @return The PixelOffset XML tag
	 */
	private String getXMLPixelOffsetTag() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("\t\t<PixelOffset>");
		buffer.append(VRTFormatUtils.getPixelOffset(this));
		buffer.append("</PixelOffset>\n");
		return buffer.toString();
	}

	/**
	 * Creates the LineOffset XML tag.
	 * @return The LineOffset XML tag
	 */
	private String getXMLLineOffsetTag() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("\t\t<LineOffset>");
		buffer.append(VRTFormatUtils.getLineOffset(this));
		buffer.append("</LineOffset>\n");
		return buffer.toString();
	}

	/**
	 * @return Returns the bands.
	 */
	public int getBands() {
		return bands;
	}

	/**
	 * @param bands The bands to set.
	 */
	public void setBands(int bands) {
		this.bands = bands;
	}

	/**
	 * @return Returns the byteOrder.
	 */
	public String getByteOrder() {
		return byteOrder;
	}

	/**
	 * @param byteOrder The byteOrder to set.
	 */
	public void setByteOrder(String byteOrder) {
		this.byteOrder = byteOrder;
	}

	/**
	 * @return Returns the dataType.
	 */
	public String getDataType() {
		return dataType;
	}

	/**
	 * @param dataType The dataType to set.
	 */
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	/**
	 * @return Returns the fileSize.
	 */
	public int getFileSize() {
		return fileSize;
	}

	/**
	 * @param fileSize The fileSize to set.
	 */
	public void setFileSize(int fileSize) {
		this.fileSize = fileSize;
	}

	/**
	 * @return Returns the headerSize.
	 */
	public int getHeaderSize() {
		return headerSize;
	}

	/**
	 * @param headerSize The headerSize to set.
	 */
	public void setHeaderSize(int headerSize) {
		this.headerSize = headerSize;
	}

	/**
	 * @return Returns the height.
	 */
	public int getImageHeight() {
		return height;
	}

	/**
	 * @param height The height to set.
	 */
	public void setImageHeight(int height) {
		this.height = height;
	}

	/**
	 * @return Returns the interleaving.
	 */
	public String getInterleaving() {
		return interleaving;
	}

	/**
	 * @param interleaving The interleaving to set.
	 */
	public void setInterleaving(String interleaving) {
		this.interleaving = interleaving;
	}

	/**
	 * @return Returns the outputHeaderFormat.
	 */
	public String getOutputHeaderFormat() {
		return outputHeaderFormat;
	}

	/**
	 * @param outputHeaderFormat The outputHeaderFormat to set.
	 */
	public void setOutputHeaderFormat(String outputHeaderFormat) {
		this.outputHeaderFormat = outputHeaderFormat;
	}

	/**
	 * @return Returns the width.
	 */
	public int getImageWidth() {
		return width;
	}

	/**
	 * @param width The width to set.
	 */
	public void setImageWidth(int width) {
		this.width = width;
	}

	/**
	 * @return Returns the rawFile.
	 */
	public String getRawFile() {
		return rawFile;
	}

	/**
	 * @param rawFile The rawFile to set.
	 */
	public void setRawFile(String rawFile) {
		this.rawFile = rawFile;
	}

	/**
	 * @return Returns the m_File.
	 */
	public File getM_File() {
		return m_File;
	}

	/**
	 * @return Returns the dataSize.
	 */
	public int getDataSize() {
		return dataSize;
	}

	/**
	 * @param dataSize The dataSize to set.
	 */
	public void setDataSize(int dataSize) {
		this.dataSize = dataSize;
	}
}