package es.unex.sextante.gvsig.core;

import java.awt.geom.Rectangle2D;

import org.cresques.cts.IProjection;
import org.gvsig.fmap.geom.primitive.Envelope;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.raster.buffer.RasterBuffer;
import org.gvsig.raster.grid.Grid;
import org.gvsig.raster.grid.GridException;

import es.unex.sextante.dataObjects.AbstractRasterLayer;
import es.unex.sextante.dataObjects.IRasterLayer;
import es.unex.sextante.rasterWrappers.GridExtent;

public class gvRasterLayer extends AbstractRasterLayer {

	private String m_sFilename;
	private IProjection m_Projection;
	private Grid layerGrid = null;

	private double DEFAULT_NO_DATA_VALUE = -99999.;

	public void create(String sName, String sFilename,
						GridExtent ge, int iDataType,
						int iNumBands, Object crs) {

		RasterMemoryDriver rmd = new RasterMemoryDriver(ge, iDataType, iNumBands);
		rmd.setName(sName);
		//rmd.setProjection((IProjection) crs);
		rmd.setNoDataValue(DEFAULT_NO_DATA_VALUE);

		m_BaseDataObject = rmd;

		m_sFilename = sFilename;

		m_Projection = (IProjection) crs;

	}

	public void create(String sName, String sFilename,
			GridExtent ge, int iDataType, Object crs) {

		create(sName, sFilename, ge, iDataType, 1, crs);

	}

	public void create(Object obj) {

		if (obj instanceof FLyrRasterSE){
			m_BaseDataObject = obj;
			m_Projection = ((FLyrRasterSE)obj).getProjection();
		}

	}

	public int getDataType() {

		if (m_BaseDataObject instanceof FLyrRasterSE) {
			FLyrRasterSE layer = (FLyrRasterSE) m_BaseDataObject;
			return layer.getDataType()[0];
		}
		else if (m_BaseDataObject instanceof RasterMemoryDriver) {
			RasterMemoryDriver layer = (RasterMemoryDriver) m_BaseDataObject;
			return layer.getRasterBuf().getDataType();
		}

		return 0;
	}

	public void assign(double dValue) {

		if (m_BaseDataObject instanceof RasterMemoryDriver) {
			super.assign(dValue);
		}

	}

	public void assign(IRasterLayer layer) {

		if (m_BaseDataObject instanceof RasterMemoryDriver) {
			super.assign(layer);
		}

	}

	public void setCellValue(int x, int y, int iBand, double dValue) {

		if (m_BaseDataObject instanceof RasterMemoryDriver) {
			RasterMemoryDriver layer = (RasterMemoryDriver) m_BaseDataObject;
			layer.setCellValue(x,y, iBand, dValue);
		}

	}

	public void setNoDataValue(double dNoDataValue) {

		if (m_BaseDataObject instanceof RasterMemoryDriver) {
			RasterMemoryDriver layer = (RasterMemoryDriver) m_BaseDataObject;
			layer.setNoDataValue(dNoDataValue);
		}
		else if (m_BaseDataObject instanceof FLyrRasterSE) {
			FLyrRasterSE layer = (FLyrRasterSE) m_BaseDataObject;
			layer.setNoDataValue(dNoDataValue);
		}

	}

	public void setNoData(int x, int y) {

		if (m_BaseDataObject instanceof RasterMemoryDriver) {
			RasterMemoryDriver layer = (RasterMemoryDriver) m_BaseDataObject;
			layer.setNoData(x, y);
		}

	}

	public double getNoDataValue(){

		if (m_BaseDataObject instanceof RasterMemoryDriver) {
			RasterMemoryDriver layer = (RasterMemoryDriver) m_BaseDataObject;
			return layer.getNoDataValue();
		}
		else if (m_BaseDataObject instanceof FLyrRasterSE) {
			FLyrRasterSE layer = (FLyrRasterSE) m_BaseDataObject;
			return layer.getNoDataValue();
		}

		return 0;

	}

	public double getCellValueInLayerCoords(int x, int y, int band) {

		if (m_BaseDataObject instanceof FLyrRasterSE) {
			FLyrRasterSE layer = (FLyrRasterSE) m_BaseDataObject;
			int iType = layerGrid.getDataType();

			if (iType == RasterBuffer.TYPE_DOUBLE) {
				return  layerGrid.getRasterBuf().getElemDouble(y, x, band);
			} else if (iType == RasterBuffer.TYPE_INT) {
				return (double)  layerGrid.getRasterBuf().getElemInt(y, x, band);
			} else if (iType == RasterBuffer.TYPE_FLOAT) {
				return (double)  layerGrid.getRasterBuf().getElemFloat(y, x, band);
			} else if (iType == RasterBuffer.TYPE_BYTE) {
				return (double) ( layerGrid.getRasterBuf().getElemByte(y, x, band) & 0xff);
			} else if ((iType == RasterBuffer.TYPE_SHORT) | (iType == RasterBuffer.TYPE_USHORT)) {
				return (double)  layerGrid.getRasterBuf().getElemShort(y, x, band);
			}

			return layerGrid.getNoDataValue();
		}
		else if (m_BaseDataObject instanceof RasterMemoryDriver) {
			RasterMemoryDriver layer = (RasterMemoryDriver) m_BaseDataObject;
			return layer.getCellValue(x, y, band);
		}

		return getNoDataValue();

	}

	public int getBandsCount() {

		if (m_BaseDataObject instanceof FLyrRasterSE) {
			FLyrRasterSE layer = (FLyrRasterSE) m_BaseDataObject;
			return layer.getBandCount();
		}
		else if (m_BaseDataObject instanceof RasterMemoryDriver) {
			RasterMemoryDriver layer = (RasterMemoryDriver) m_BaseDataObject;
			return layer.getRasterBuf().getBandCount();
		}

		return 0;

	}

	public void fitToGridExtent(GridExtent ge){

		int iNX, iNY;
		int x, y;
		int iBand;

		if (ge != null){
			if (!ge.equals(getWindowGridExtent()) ){
				if (m_BaseDataObject instanceof RasterMemoryDriver) {
					RasterMemoryDriver gvSIGDriver = (RasterMemoryDriver) m_BaseDataObject;

					FLyrRasterSE gvSIGLayer = gvSIGDriver.getRasterLayer(Long.toString(System.currentTimeMillis()).toString(),
							m_Projection);
					gvRasterLayer orgLayer = new gvRasterLayer();
					orgLayer.create(gvSIGLayer);

					iNX = ge.getNX();
					iNY = ge.getNY();

					m_BaseDataObject = null;

					create(this.getName(), m_sFilename, ge, this.getDataType(), this.getBandsCount());
					for (iBand = 0; iBand < this.getBandsCount(); iBand++){
						for (y = 0; y < iNY; y++){
							for (x = 0; x < iNX; x++){
								setCellValue(x, y, iBand, orgLayer.getCellValueAsDouble(x, y, iBand));
							}
						}
					}
				}
			}
		}
	}

	public String getName() {

		if (m_BaseDataObject instanceof FLyrRasterSE) {
			FLyrRasterSE layer = (FLyrRasterSE) m_BaseDataObject;
			return layer.getName();
		}
		else if (m_BaseDataObject instanceof RasterMemoryDriver) {
			RasterMemoryDriver layer = (RasterMemoryDriver) m_BaseDataObject;
			return layer.getName();
		}

		return null;

	}

	public void postProcess() {

		if (m_BaseDataObject instanceof RasterMemoryDriver) {
			RasterMemoryDriver layer = (RasterMemoryDriver) m_BaseDataObject;
			if (layer.export(m_sFilename, m_Projection)){
				FLyrRasterSE rasterLayer = (FLyrRasterSE) FileTools.openLayer(m_sFilename,
											layer.getName(), null);
				create(rasterLayer);
				rasterLayer.setNoDataValue(layer.getNoDataValue());
				System.gc();
			}
		}

	}

	public void open() {

		if (m_BaseDataObject instanceof FLyrRasterSE) {
			FLyrRasterSE layer = (FLyrRasterSE) m_BaseDataObject;
			try {
				layerGrid = layer.getReadOnlyFullGrid(false);
			} catch (GridException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	public void close() {

		if (m_BaseDataObject instanceof FLyrRasterSE) {
			FLyrRasterSE layer = (FLyrRasterSE) m_BaseDataObject;
			if(layerGrid != null)
				layerGrid.getRasterBuf().free();
			layerGrid = null;
		}

	}

	public Rectangle2D getFullExtent() {

		if (m_BaseDataObject instanceof FLyrRasterSE) {
			FLyrRasterSE layer = (FLyrRasterSE) m_BaseDataObject;
			try{
				Envelope env=layer.getFullEnvelope();
				return new Rectangle2D.Double(env.getMinimum(0),env.getMinimum(1),env.getLength(0),env.getLength(1));
			}catch(Exception e){
				return null;
			}
		}
		else if (m_BaseDataObject instanceof RasterMemoryDriver) {
			RasterMemoryDriver layer = (RasterMemoryDriver) m_BaseDataObject;
			return layer.getGridExtent().getAsRectangle2D();
		}

		return null;

	}

	public GridExtent getLayerGridExtent() {

		if (m_BaseDataObject instanceof FLyrRasterSE) {
			FLyrRasterSE layer = (FLyrRasterSE) m_BaseDataObject;
			try{
				GridExtent extent = new GridExtent();
				extent.setXRange(layer.getMinX(), layer.getMaxX());
				extent.setYRange(layer.getMinY(), layer.getMaxY());
				extent.setCellSize(layer.getCellSize());
				return extent;
			}catch(Exception e){
				return null;
			}
		}
		else if (m_BaseDataObject instanceof RasterMemoryDriver) {
			RasterMemoryDriver layer = (RasterMemoryDriver) m_BaseDataObject;
			return layer.getGridExtent();
		}

		return null;

	}

	public double getLayerCellSize() {

		if (m_BaseDataObject instanceof FLyrRasterSE) {
			FLyrRasterSE layer = (FLyrRasterSE) m_BaseDataObject;
			return layer.getCellSize();
		}
		else if (m_BaseDataObject instanceof RasterMemoryDriver) {
			RasterMemoryDriver layer = (RasterMemoryDriver) m_BaseDataObject;
			return layer.getGridExtent().getCellSize();
		}

		return 0;

	}

	public String getFilename() {

		if (m_BaseDataObject instanceof FLyrRasterSE) {
			FLyrRasterSE layer = (FLyrRasterSE) m_BaseDataObject;
			return layer.getDataSource().getDataset(0)[0].getFName();
		}
		else if (m_BaseDataObject instanceof RasterMemoryDriver) {
			return m_sFilename;
		}

		return "";

	}


	public Object getCRS() {

		return m_Projection;

	}


	public void setName(String name) {

		if (m_BaseDataObject instanceof FLyrRasterSE) {
			FLyrRasterSE layer = (FLyrRasterSE) m_BaseDataObject;
			layer.setName(name);
		}
		else if (m_BaseDataObject instanceof RasterMemoryDriver) {
			RasterMemoryDriver layer = (RasterMemoryDriver) m_BaseDataObject;
			layer.setName(name);
		};

	}

}
