package es.unex.sextante.gvsig.core;

import java.awt.geom.AffineTransform;
import java.awt.image.DataBuffer;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import org.cresques.cts.IProjection;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.raster.buffer.RasterBuffer;
import org.gvsig.raster.buffer.WriterBufferServer;
import org.gvsig.raster.dataset.GeoRasterWriter;
import org.gvsig.raster.dataset.IBuffer;
import org.gvsig.raster.dataset.Params;

import com.iver.andami.Utilities;

import es.unex.sextante.rasterWrappers.GridExtent;

public class RasterMemoryDriver {

	public static final int RASTER_DATA_TYPE_FLOAT = DataBuffer.TYPE_FLOAT;
	public static final int RASTER_DATA_TYPE_DOUBLE = DataBuffer.TYPE_DOUBLE;
	public static final int RASTER_DATA_TYPE_INT = DataBuffer.TYPE_INT;
	public static final int RASTER_DATA_TYPE_SHORT = DataBuffer.TYPE_SHORT;
	public static final int RASTER_DATA_TYPE_BYTE = DataBuffer.TYPE_BYTE;

	private GridExtent m_GridExtent;
	private IBuffer buf = null;
	private String name = null;
	
	public RasterMemoryDriver(GridExtent ge, int iDataType, int iNumBands) {

		super();

		buf = RasterBuffer.getBuffer(iDataType, ge.getNX(), ge.getNY(), iNumBands, true);
		m_GridExtent = ge;

	}

	public RasterMemoryDriver(GridExtent ge, int iDataType) {

		this(ge, iDataType, 1);

	}

	public GridExtent getGridExtent(){

		return m_GridExtent;

	}

	public IBuffer getRasterBuf(){

		return this.buf;

	}

	public void reset(){

		this.buf = null;

	}

	public void setCellValue(int x, int y, double dValue) {

		setCellValue(x, y, 0, dValue);

	}

	public void setNoData(int x, int y) {

		setNoData(x, y, 0);

	}

	private void setNoData(int x, int y, int iBand) {

		setCellValue(x, y, iBand, getNoDataValue());

	}

	public void setCellValue(int x, int y, int iBand, double dValue){

		if (isInGrid(x,y) && iBand < buf.getBandCount()){
			if(buf.getDataType() == RasterBuffer.TYPE_BYTE){
				buf.setElem(y, x, iBand, (byte)dValue);
			}else if(buf.getDataType() == RasterBuffer.TYPE_SHORT){
				buf.setElem(y, x, iBand, (short)dValue);
			}else if(buf.getDataType() == RasterBuffer.TYPE_INT){
				buf.setElem(y, x, iBand, (int)dValue);
			}else if(buf.getDataType() == RasterBuffer.TYPE_FLOAT){
				buf.setElem(y, x, iBand, (float)dValue);
			}else if(buf.getDataType() == RasterBuffer.TYPE_DOUBLE){
				buf.setElem(y, x, iBand, (double)dValue);
			}
		}

	}

	public double getNoDataValue(){

		return buf.getNoDataValue();

	}

	public void setNoDataValue(double dNoDataValue){

		buf.setNoDataValue(dNoDataValue);

	}


	public double getCellValue(int x, int y) {

		return getCellValue(x,y,0);

	}

	public double getCellValue(int x, int y, int iBand){

		if (isInGrid(x,y)){
			int iDataType = buf.getDataType();
			if (iDataType == RasterBuffer.TYPE_DOUBLE) {
	        	return buf.getElemDouble(y, x, iBand);
	        } else if (iDataType == RasterBuffer.TYPE_INT) {
	        	return (double) buf.getElemInt(y, x, iBand);
	        } else if (iDataType == RasterBuffer.TYPE_FLOAT) {
	        	return (double) buf.getElemFloat(y, x, iBand);
	        } else if (iDataType == RasterBuffer.TYPE_BYTE) {
	        	return (double) buf.getElemByte(y, x, iBand);
	        } else if ((iDataType == RasterBuffer.TYPE_SHORT) | (iDataType == RasterBuffer.TYPE_USHORT)) {
	        	return (double) buf.getElemShort(y, x, iBand);
	        }
		}

		return getNoDataValue();

	}

	public boolean isNoDataValue(double dNoDataValue){

		return (getNoDataValue() == dNoDataValue);

	}

	public boolean isInGrid(int x, int y){

		if (x < 0 || y < 0)
			return false;

		if (x >= m_GridExtent.getNX() || y >= m_GridExtent.getNY())
			return false;

		return true;

	}

	public double getCellSize() {

		return m_GridExtent.getCellSize();

	}

	public FLyrRasterSE getRasterLayer(String sDescription, IProjection projection) {

		FLyrRasterSE layer;
		String sFilename = getFilename(sDescription, "tif");
		try {
			exportToGeoTiff(sFilename, projection);
			layer = FLyrRasterSE.createLayer(sDescription, new File(sFilename), projection);
			layer.setNoDataValue(this.getNoDataValue());
			return layer;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	public static String getFilename (String sRoot, String sExtension){

		String sFilename;
		int i = 1;

		sRoot = sRoot.toLowerCase();
        sRoot = sRoot.replaceAll(" ","_");
        sRoot = sRoot.replaceAll("\\)","");
        sRoot = sRoot.replaceAll("\\(","_");
        sRoot = sRoot.replaceAll("\\[","_");
        sRoot = sRoot.replaceAll("\\]","");
        sRoot = sRoot.replaceAll("<","_");
        sRoot = sRoot.replaceAll(">","_");
        sRoot = sRoot.replaceAll("__","_");

		while(true){
			sFilename = Utilities.createTempDirectory() +  File.separator + sRoot
					+ Integer.toString(i) + "." + sExtension;
			File file = new File(sFilename);
			if (file.exists()){
				i++;
			}
			else{
				return sFilename;
			}
		}

	}

	public void fitToGridExtent(GridExtent gridExtent, IProjection projection) {



	}

	private boolean exportToArcInfoASCIIFile(String sFilename){

		try{
			FileWriter f = new FileWriter(sFilename);
			BufferedWriter fout = new BufferedWriter(f);
			DecimalFormat df = new DecimalFormat("##.###");
			df.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.US));
			df.setDecimalSeparatorAlwaysShown(true);

			fout.write("ncols " + Integer.toString(m_GridExtent.getNX()));
			fout.newLine();
			fout.write("nrows " + Integer.toString(m_GridExtent.getNY()));
			fout.newLine();
			fout.write("xllcorner " + Double.toString(m_GridExtent.getXMin()));
			fout.newLine();
			fout.write("yllcorner " + Double.toString(m_GridExtent.getYMin()));
			fout.newLine();
			fout.write("cellsize " + Double.toString(m_GridExtent.getCellSize()));
			fout.newLine();
			fout.write("nodata_value " + Double.toString(getNoDataValue()));
			fout.newLine();

			for (int i = 0; i < m_GridExtent.getNY(); i++) {
				for (int j = 0; j < m_GridExtent.getNX(); j++) {
					fout.write(df.format(getCellValue(j,i)) + " ");
				}
				fout.newLine();
			}
			fout.close();
			f.close();
		}catch(Exception e){
			return false;
		}

		return true;

	}

	private boolean exportToGeoTiff(String sFilename, IProjection projection){

		try{
			WriterBufferServer writerBufferServer = new WriterBufferServer();
			writerBufferServer.setBuffer(buf, -1);
			/*Extent ext = new Extent(m_GridExtent.getAsRectangle2D());
			ViewPortData vpData = new ViewPortData(projection, ext,
									new Dimension(m_GridExtent.getNX(),m_GridExtent.getNY()));*/
			Params params = GeoRasterWriter.getWriter(sFilename).getParams();
			AffineTransform affineTransform = new AffineTransform(m_GridExtent.getCellSize(), 0, 0, -m_GridExtent.getCellSize(), m_GridExtent.getXMin(), m_GridExtent.getYMax());
			GeoRasterWriter writer = GeoRasterWriter.getWriter(writerBufferServer, 
																sFilename, 
																buf.getBandCount(),
																affineTransform,
																m_GridExtent.getNX(),
																m_GridExtent.getNY(),
																buf.getDataType(),
																params,
																projection);
			writer.dataWrite();
			writer.writeClose();
		}catch(Exception e){
			return false;
		}

		return true;


	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}

	public boolean export (String sFilename, IProjection projection){

		if (sFilename.endsWith("asc")){
			return exportToArcInfoASCIIFile(sFilename);
		}
		else{
			return exportToGeoTiff(sFilename, projection);
		}

	}



}
