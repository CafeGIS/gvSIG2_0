package org.gvsig.raster.grid;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import org.gvsig.raster.buffer.RasterBuffer;
import org.gvsig.raster.buffer.RasterBufferInvalidAccessException;
import org.gvsig.raster.dataset.IBuffer;

/**
 * Esta clase representa un grid para escritura 
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class GridWriter{

	private RasterBuffer	rasterBuf = null;
	private GridExtent		extent = null;
	private int 			dataType = IBuffer.TYPE_UNDEFINED;
	private int				bandToOperate = 0;
	
	/**
	 * Asignación del buffer de datos e inicialización de variables. 
	 * @param ge Extent del grid
	 * @param dataType Tipo de dato del grid
	 * @param rasterBuf Buffer de datos del grid
	 */
	public GridWriter(GridExtent ge, int dataType, RasterBuffer rasterBuf){
		this.rasterBuf = rasterBuf;
		this.extent = ge;	
	}
	
	/**
	 * Creación del escritor de grid a través de los parámetros de su extent. Con
	 * ellos se creará el GridExtent y el buffer de datos para escribir.
	 * @param iNX Ancho en pixeles
	 * @param iNY Alto en pixeles
	 * @param dCellSize Tamaño de celda
	 * @param dMinX Coordenada mínima en X
	 * @param dMinY Coordenada mínima en Y
	 * @param dataType Tipo de dato
	 * @param bands Bandas a escribir
	 */
	public GridWriter(int iNX, int iNY, double dCellSize,
						double dMinX, double dMinY, int dataType, 
						int[] bands) {
		extent = new GridExtent();
		extent.setCellSize(dCellSize);
		extent.setXRange(dMinX, dMinX + iNX * dCellSize);
		extent.setYRange(dMinY, dMinY + iNY * dCellSize);
	
		rasterBuf = RasterBuffer.getBuffer(dataType, iNX, iNY, bands.length, true);
	}
	
	public void assign(byte value) throws RasterBufferInvalidAccessException {
		try{
			rasterBuf.assign(bandToOperate, value);
		}catch(ArrayIndexOutOfBoundsException e){
			throw new RasterBufferInvalidAccessException("Access to data type " + dataType + "invalid");
		}catch(NullPointerException e){
			throw new RasterBufferInvalidAccessException("Access to data type " + dataType + "invalid");
		}
	}
	
	public void assign(short value) throws RasterBufferInvalidAccessException {
		try{
			rasterBuf.assign(bandToOperate, value);
		}catch(ArrayIndexOutOfBoundsException e){
			throw new RasterBufferInvalidAccessException("Access to data type " + dataType + "invalid");
		}catch(NullPointerException e){
			throw new RasterBufferInvalidAccessException("Access to data type " + dataType + "invalid");
		}	
	}
	
	public void assign(int value) throws RasterBufferInvalidAccessException {
		try{
			rasterBuf.assign(bandToOperate, value);
		}catch(ArrayIndexOutOfBoundsException e){
			throw new RasterBufferInvalidAccessException("Access to data type " + dataType + "invalid");
		}catch(NullPointerException e){
			throw new RasterBufferInvalidAccessException("Access to data type " + dataType + "invalid");
		}	
	}
	
	public void assign(float value) throws RasterBufferInvalidAccessException {
		try{
			rasterBuf.assign(bandToOperate, value);
		}catch(ArrayIndexOutOfBoundsException e){
			throw new RasterBufferInvalidAccessException("Access to data type " + dataType + "invalid");
		}catch(NullPointerException e){
			throw new RasterBufferInvalidAccessException("Access to data type " + dataType + "invalid");
		}	
	}
	
	public void assign(double value) throws RasterBufferInvalidAccessException {
		try{
			rasterBuf.assign(bandToOperate, value);
		}catch(ArrayIndexOutOfBoundsException e){
			throw new RasterBufferInvalidAccessException("Access to data type " + dataType + "invalid");
		}catch(NullPointerException e){
			throw new RasterBufferInvalidAccessException("Access to data type " + dataType + "invalid");
		}	
	}
	
	public GridExtent getGridExtent() {		
		return extent;
	}
	
	public void setCellValue( int iX, int iY, byte value) throws OutOfGridException {
		if (isCellInGrid(iX, iY))
			rasterBuf.setElem(iY, iX, bandToOperate, value);	
		else
			throw new OutOfGridException("Point:(" + iX + "," + iY + ") out of grid.");
	}
	
	public void setCellValue( int iX, int iY, short value) throws OutOfGridException {
		if (isCellInGrid(iX, iY))
			rasterBuf.setElem(iY, iX, bandToOperate, value);	
		else
			throw new OutOfGridException("Point:(" + iX + "," + iY + ") out of grid.");
	}
	
	public void setCellValue( int iX, int iY, int value) throws OutOfGridException {
		if (isCellInGrid(iX, iY))
			rasterBuf.setElem(iY, iX, bandToOperate, value);	
		else
			throw new OutOfGridException("Point:(" + iX + "," + iY + ") out of grid.");
	}
	
	public void setCellValue( int iX, int iY, float value) throws OutOfGridException {
		if (isCellInGrid(iX, iY))
			rasterBuf.setElem(iY, iX, bandToOperate, value);	
		else
			throw new OutOfGridException("Point:(" + iX + "," + iY + ") out of grid.");
	}
	
	public void setCellValue( int iX, int iY, double value) throws OutOfGridException {
		if (isCellInGrid(iX, iY))
			rasterBuf.setElem(iY, iX, bandToOperate, value);	
		else
			throw new OutOfGridException("Point:(" + iX + "," + iY + ") out of grid.");
	}
	
	public void setNoData( int iX, int iY) {
		if (isCellInGrid(iX,iY)) {
			switch(rasterBuf.getDataType()) {
			case IBuffer.TYPE_BYTE: rasterBuf.setElem(iY, iX, bandToOperate, (byte)rasterBuf.getNoDataValue());break;
			case IBuffer.TYPE_SHORT: rasterBuf.setElem(iY, iX, bandToOperate, (short)rasterBuf.getNoDataValue());break;
			case IBuffer.TYPE_INT: rasterBuf.setElem(iY, iX, bandToOperate, (int)rasterBuf.getNoDataValue());break;
			case IBuffer.TYPE_FLOAT: rasterBuf.setElem(iY, iX, bandToOperate, (float)rasterBuf.getNoDataValue());break;
			case IBuffer.TYPE_DOUBLE: rasterBuf.setElem(iY, iX, bandToOperate, (double)rasterBuf.getNoDataValue());break;
			}
		}
	}
	
	/**
	 * Exporta a fichero ArcView ASCII pasandole el nombre del fichero a salvar. Esta
	 * llamada afecta solo a la banda 0 del raster y es especial para MDT's y para conservar
	 * la compatibilidad con código que la usa de esta forma.
	 * 
	 * @param sFile Nombre del fichero
	 * @throws IOException 
	 * @throws NumberFormatException
	 */
	public void ExportToArcViewASCIIFile(String sFile)
				throws IOException, NumberFormatException {
		ExportToArcViewASCIIFile(sFile, bandToOperate);
	}
	
	/**
	 * Exporta a fichero ArcView ASCII pasandole el nombre del fichero a salvar.
	 * @param sFile Nombre del fichero
	 * @param band 
	 * @throws IOException
	 * @throws NumberFormatException
	 */
	public void ExportToArcViewASCIIFile(String sFile, int band)
				throws IOException, NumberFormatException {
		
		BufferedWriter fout = new BufferedWriter(new FileWriter(sFile));
	
		fout.write("ncols " + Integer.toString(extent.getNX()));
		fout.newLine();
		fout.write("nrows " + Integer.toString(extent.getNY()));
		fout.newLine();
		fout.write("xllcorner " + Double.toString(extent.minX()));
		fout.newLine();	
		fout.write("yllcorner " + Double.toString(extent.minY()));
		fout.newLine();
		fout.write("cellsize " + Double.toString(extent.getCellSize()));
		fout.newLine();
		fout.write("nodata_value " + Float.toString((float)rasterBuf.getNoDataValue()));
		fout.newLine();
		
		switch(rasterBuf.getDataType()) {
		case IBuffer.TYPE_BYTE: for (int i = 0; i < extent.getNY(); i++) {
									for (int j = 0; j < extent.getNX(); j++)
										fout.write(Byte.toString(rasterBuf.getElemByte(i, j, band)/*[j][i]*/) + " ");
									fout.newLine();
								}
								break;
		case IBuffer.TYPE_SHORT:for (int i = 0; i < extent.getNY(); i++) {
									for (int j = 0; j < extent.getNX(); j++)
										fout.write(Short.toString(rasterBuf.getElemShort(i, j, band)/*[j][i]*/) + " ");
									fout.newLine();
								}
								break;
		case IBuffer.TYPE_INT: 	for (int i = 0; i < extent.getNY(); i++) {
									for (int j = 0; j < extent.getNX(); j++)
										fout.write(Integer.toString(rasterBuf.getElemInt(i, j, band)/*[j][i]*/) + " ");
									fout.newLine();
								}
								break;
		case IBuffer.TYPE_FLOAT:for (int i = 0; i < extent.getNY(); i++) {
									for (int j = 0; j < extent.getNX(); j++)
										fout.write(Float.toString(rasterBuf.getElemFloat(i, j, band)/*[j][i]*/) + " ");
									fout.newLine();
								}
								break;
		case IBuffer.TYPE_DOUBLE:for (int i = 0; i < extent.getNY(); i++) {
									for (int j = 0; j < extent.getNX(); j++)
										fout.write(Double.toString(rasterBuf.getElemDouble(i, j, band)/*[j][i]*/) + " ");
									fout.newLine();
								}
								break;
		}
		fout.close();
	}
		
	public void setNoDataValue(double noDataValue) {
		rasterBuf.setNoDataValue(noDataValue);
	}

	/**
	 * Consulta si el grid tiene la posición indicada por parámetro o si esta cae
	 * fuera del área.
	 * @param iX Posición X
	 * @param iY Posición Y
	 * @return true si está incluido en el Grid y false si no lo está
	 */
	public boolean isCellInGrid(int iX, int iY) {
		return (iX >= 0 && iX < extent.getNX() && iY >= 0 && iY < extent.getNY());
	}
	
	/**
	 * Asigna la banda sobre la que se realizan las operaciones. Por defecto es la banda 0
	 * con lo que para el uso de MDTs no habrá que modificar este valor.
	 * @param band Banda sobre la que se realizan las operaciones.
	 */
	public void setBandToOperate(int band) {
		this.bandToOperate = band;
	}
}
