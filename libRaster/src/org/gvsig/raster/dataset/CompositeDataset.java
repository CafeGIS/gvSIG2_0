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
package org.gvsig.raster.dataset;

import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.io.IOException;

import org.cresques.cts.IProjection;
import org.gvsig.raster.RasterLibrary;
import org.gvsig.raster.buffer.RasterBuffer;
import org.gvsig.raster.dataset.properties.DatasetColorInterpretation;
import org.gvsig.raster.dataset.properties.DatasetListHistogram;
import org.gvsig.raster.dataset.properties.DatasetListStatistics;
import org.gvsig.raster.dataset.serializer.RmfSerializerException;
import org.gvsig.raster.datastruct.ColorTable;
import org.gvsig.raster.datastruct.Extent;
import org.gvsig.raster.datastruct.GeoPointList;
import org.gvsig.raster.datastruct.Histogram;
import org.gvsig.raster.datastruct.HistogramException;
import org.gvsig.raster.datastruct.Transparency;
import org.gvsig.raster.util.MathUtils;
import org.gvsig.raster.util.RasterUtilities;
/**
 * Esta clase está compuestas de multiples datasets formando una rejilla de NxM
 * rasters. Un cliente de esta clase debe tener una visión de la rejilla como si
 * fuese un solo raster, gestionando esta el acceso la imagen que corresponda en
 * cada petición de usuario.
 * 
 * @version 29/08/2007
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class CompositeDataset implements IRasterDataSource {
	private MultiRasterDataset[][] mosaic        = null;
	private DatasetListStatistics  stats         = null;
	private BandList               bandList      = new BandList();
	private boolean                readOnly      = false;

	/**
	 * Flag que fuerza al buffer en memoria
	 */
	private boolean                forceToMemory = false;
		
	/**
	 * Constructor. Genera la estructura de n filas por n columnas de rasters.
	 * @param n Número de filas
	 * @param m Número de columnas
	 */
	public CompositeDataset(int n, int m) {
		mosaic = new MultiRasterDataset[n][m];
	}

	/**
	 * Constructor. Genera la estructura de n filas por n columnas de rasters y
	 * las asigna a los raster que se le pasan por parámetro.
	 * @param n Número de filas
	 * @param m Número de columnas
	 */
	public CompositeDataset(MultiRasterDataset[][] mos) throws MosaicNotValidException {
		this.mosaic = deleteNullValues(mos);
		//this.mosaic = mos;
		
		/*if(!validateMosaic()) {
			this.mosaic = null;
			throw new MosaicNotValidException("Extends no validos para montar un mosaico");
		}*/
		init();
	}
	
	/**
	 * Método que elimina crea un array bidimensional eliminando los nulos del que tiene
	 * el que se le pasa por parámetro
	 * @param values
	 * @return MultiRasterDataset
	 */
	private MultiRasterDataset[][] deleteNullValues(MultiRasterDataset[][] values) {
		int n = values.length;
		int m = values[0].length;
		int posInitX = 0;
		int posInitY = 0;
		
		int nRows = n, nCols = m;
		//Contador de filas
		boolean first = true;
		for (int row = 0; row < n; row++) {
			boolean isNull = true;
			for (int col = 0; col < m; col++) {
				if(values[row][col] != null) {
					isNull = false;
					if(first) {
						posInitX = col;
						first = false;
					}
				}
			}			
			if(isNull)
				nRows --;
		}
		
		//Contador de columnas
		first = true;
		for (int col = 0; col < m; col++) {
			boolean isNull = true;
			for (int row = 0; row < n; row++) {
				if(values[row][col] != null) {
					isNull = false;
					if(first) {
						posInitY = row;
						first = false;
					}
				}
			}			
			if(isNull)
				nCols --;
		}
		//Copia de datos
		MultiRasterDataset[][] result = new MultiRasterDataset[nRows][nCols];
		
		for (int row = 0; row < result.length; row++) 
			for (int col = 0; col < result[row].length; col++) 
				result[row][col] = values[row + posInitY][col + posInitX];
		return result;
	}
		
	/**
	 * Abre un dataset pasando como parámetros la proyección y un objeto identificador del dataset. Este
	 * objeto puede ser una ruta a un fichero en disco. En este caso la extensión del fichero servirá para 
	 * buscar el driver que lo gestiona. Si proporcionamos un array de cadenas se tratarán como la ruta a N ficheros
	 * de disco. También puede ser un buffer de datos en memoria o cualquier otro objeto
	 * que pueda aceptar un driver.  
	 * @param proj PRoyección
	 * @param datasetOpenParam Parámetros al driver
	 * @return RasterMultiDatset
	 * @throws NotSupportedExtensionException
	 * @throws RasterDriverException
	 */
	public static CompositeDataset open(IProjection proj, Object datasetOpenParam) throws NotSupportedExtensionException, RasterDriverException {
		if(datasetOpenParam instanceof String[][]) {
			String[][] param = (String[][])datasetOpenParam;
			MultiRasterDataset[][] mosaic = new MultiRasterDataset[param.length][param[0].length];
			for (int i = 0; i < param.length; i++) {
				for (int j = 0; j < param[i].length; j++) 
					mosaic[i][j] = MultiRasterDataset.open(proj, param[i][j]);
			}
			CompositeDataset cd;
			try {
				cd = new CompositeDataset(mosaic);
			} catch (MosaicNotValidException e) {
				return null;
			}
			return cd;
		}
		return null;
	}
	
	/**
	 * Valida que los extends del mosaico sean validos, es decir, que sean correlativos
	 * formando la matriz. Además también valida que el tamaño de pixel coincida en todos los
	 * raster que forman el mosaico. 
	 * 
	 * @param mos 
	 * @throws MosaicNotValidException
	 */
	public boolean validateMosaic() {
		int n = mosaic.length;
		int m = mosaic[0].length;
		//Comprobamos en Horizontal
		if(m > 1) {
			for (int row = 0; row < n; row++) {
				for (int col = 0; col < m; col++) {
					if(col < (m - 1) && mosaic[row][col] != null) {
						Extent a = mosaic[row][col].getExtent();
						Extent b = mosaic[row][col + 1].getExtent();
						if(((int)a.maxX()) != ((int)b.minX()))
							return false;
						double psx = MathUtils.clipDecimals(mosaic[row][col].getPixelSizeX(), 2);
						double psx1 = MathUtils.clipDecimals(mosaic[row][col + 1].getPixelSizeX(), 2);
						double psy = MathUtils.clipDecimals(mosaic[row][col].getPixelSizeY(), 2);
						double psy1 = MathUtils.clipDecimals(mosaic[row][col + 1].getPixelSizeY(), 2);
						if(psx != psx1 || psy != psy1)
							return false;
						if(mosaic[row][col].getBandCount() != mosaic[row][col + 1].getBandCount())
							return false;
					}
				}
			}
		}

		//Comprobamos en Vertical
		if(n > 1) {
			for (int col = 0; col < m; col++) {
				for (int row = 0; row < n; row++) {
					if(row < (n - 1) && mosaic[row][col] != null) {
						Extent a = mosaic[row][col].getExtent();
						Extent b = mosaic[row + 1][col].getExtent();
						if(((int)a.minY()) != ((int)b.maxY()))
							return false;
						double psx = MathUtils.clipDecimals(mosaic[row][col].getPixelSizeX(), 2);
						double psx1 = MathUtils.clipDecimals(mosaic[row + 1][col].getPixelSizeX(), 2);
						double psy = MathUtils.clipDecimals(mosaic[row][col].getPixelSizeY(), 2);
						double psy1 = MathUtils.clipDecimals(mosaic[row + 1][col].getPixelSizeY(), 2);
						if(psx != psx1 || psy != psy1)
							return false;
						if(mosaic[row][col].getBandCount() != mosaic[row + 1][col].getBandCount())
							return false;
					}
				}
			}
		}
		
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.IRasterDataSource#addDataset(org.gvsig.raster.dataset.RasterDataset[])
	 */
	public void addDataset(RasterDataset[] f) throws FileNotFoundInListException {
		if(mosaic != null) {
			int n = mosaic.length;
			int m = mosaic[0].length;
			if(f.length == (n * m)) {
				for (int i = 0; i < n; i++) {
					for (int j = 0; j < m; j++) {
						MultiRasterDataset mrd = new MultiRasterDataset();
						mrd.addDataset(new RasterDataset[]{f[i * n + j]});		
					}
				}
			}
			init();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.IRasterDataSource#addDataset(java.lang.String[])
	 */
	public void addDataset(String[] fileName) throws FileNotFoundInListException, NotSupportedExtensionException, RasterDriverException {
		if(mosaic != null) {
			int n = mosaic.length;
			int m = mosaic[0].length;
			if(fileName.length == (n * m)) {
				for (int i = 0; i < n; i++) {
					for (int j = 0; j < m; j++) {
						MultiRasterDataset mrd = new MultiRasterDataset();
						mrd.addDataset(new RasterDataset[]{RasterDataset.open(null, fileName[i * n + j])});		
					}
				}
			}
			init();
		}
	}

	/**
	 * Acciones de inicialización cuando se crea el objeto o se
	 * añaden nuevos dataset a este
	 */
	private void init() {
		stats = new DatasetListStatistics(mosaic);
		
		//Creamos la lista de bandas
		bandList = (BandList)mosaic[0][0].getBands().clone();
		int n = mosaic.length;
		int m = mosaic[0].length;
		for (int row = 0; row < n; row++) { 
			for (int col = 0; col < m; col++) {
				if(row != 0 && col != 0) { //El primero ya está añadido
					for (int i = 0; i < mosaic[0][0].getBandCount(); i++) {
						if(mosaic[row][col] != null)
							bandList.getBand(i).setAdditionalName(mosaic[row][col].getBands().getBand(i).getFileName());
					}
				}
			}
		}
	
	}
	
	/**
	 * Obtiene la lista de nombres de los dataset 
	 * @return
	 */
	public String[][] getFileNames() {
		String[][] s = new String[mosaic.length][mosaic[0].length];
		for (int i = 0; i < s.length; i++) {
			for (int j = 0; j < s[i].length; j++) {
				if(mosaic[i][j] != null)
					s[i][j] = mosaic[i][j].getDataset(0)[0].getFName();
			}
		}
		return s;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.IRasterDataSource#calcSteps(double, double, double, double, double, double, int, int)
	 */
	public double[] calcSteps(double dWorldTLX, double dWorldTLY, double dWorldBRX, double dWorldBRY, double nWidth, double nHeight, int bufWidth, int bufHeight) {
		if(mosaic != null)
			return mosaic[0][0].calcSteps(dWorldTLX, dWorldTLY, dWorldBRX, dWorldBRY, nWidth, nHeight, bufWidth, bufHeight);
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.IRasterDataSource#close()
	 */
	public void close() {
		int n = mosaic.length;
		int m = mosaic[0].length;
		for (int row = 0; row < n; row++) {
			for (int col = 0; col < m; col++) {
				if(mosaic[row][col] != null)
					mosaic[row][col].close();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.IRasterDataSource#copy()
	 */
	public IRasterDataSource newDataset() {
		int n = mosaic.length;
		int m = mosaic[0].length;
		MultiRasterDataset[][] mrd = new MultiRasterDataset[n][m];
		for (int row = 0; row < n; row++) 
			for (int col = 0; col < m; col++) 
				if(mosaic[row][col] != null)
					mrd[row][col] = (MultiRasterDataset)mosaic[row][col].newDataset();
					
		try {
			return new CompositeDataset(mrd);
		} catch (MosaicNotValidException e) {
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.IRasterDataSource#getOwnAffineTransform()
	 */
	public AffineTransform getOwnAffineTransform() {
		if(mosaic != null && mosaic[0][0] != null)
			return mosaic[0][0].getOwnAffineTransform();
		return new AffineTransform();
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.IRasterDataSource#getAffineTransform()
	 */
	public AffineTransform getAffineTransform(int band) {
		if(mosaic != null && mosaic[0][0] != null)
			return mosaic[0][0].getAffineTransform(band);
		return new AffineTransform();
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.IRasterDataSource#getExtent()
	 */
	public Extent getExtent() {
		int n = mosaic.length;
		int m = mosaic[0].length;
		if(mosaic != null && mosaic[0][0] != null) {
			double ulx = mosaic[0][0].getExtent().getULX();
			double uly = mosaic[0][0].getExtent().getULY();
			
			double urx = mosaic[n - 1][0].getExtent().getURX();
			double ury = mosaic[n - 1][0].getExtent().getURY();
			
			double llx = mosaic[0][m - 1].getExtent().getLLX();
			double lly = mosaic[0][m - 1].getExtent().getLLY();
			
			double lrx = mosaic[n - 1][m - 1].getExtent().getLRX();
			double lry = mosaic[n - 1][m - 1].getExtent().getLRY();
			
			return new Extent(	new Point2D.Double(ulx, uly), 
									new Point2D.Double(lrx, lry), 
									new Point2D.Double(urx, ury), 
									new Point2D.Double(llx, lly));
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.IRasterDataSource#getBandCount()
	 */
	public int getBandCount() {
		if(mosaic != null && mosaic[0][0] != null)
			return mosaic[0][0].getBandCount();
		return 0;
	}

	/**
	 * Obtiene el ancho del mosaico completo en píxeles, esto es la
	 * suma de todos los raster que componen la extensión.
	 */
	public double getWidth() {
		double w = 0;
		if(mosaic != null && mosaic[0][0] != null) {
			int m = mosaic[0].length;
			for (int col = 0; col < m; col++)
				w += mosaic[0][col].getWidth();
		}
		return w;
	}
	
	/**
	 * Obtiene el alto del mosaico completo en píxeles, esto es la
	 * suma de todos los raster que componen la extensión.
	 */
	public double getHeight() {
		double h = 0;
		if(mosaic != null && mosaic[0][0] != null) {
			int n = mosaic.length;
			for (int row = 0; row < n; row++)
				h += mosaic[row][0].getHeight();
		}
		return h;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.IRasterDataSource#getCellSize()
	 */
	public double getCellSize() {
		try {
			Extent e = getExtent();
			double dCellsize = (e.getMax().getX() - e.getMin().getX() ) / getWidth();
			return dCellsize;
		} catch (NullPointerException e) {
			return 1;
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.IRasterDataSource#getDataType()
	 */
	public int[] getDataType() {
		if(mosaic != null && mosaic[0][0] != null)
			return mosaic[0][0].getDataType();
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.IRasterDataSource#getDatasetCount()
	 */
	public int getDatasetCount() {
		if(mosaic != null && mosaic[0][0] != null)
			return mosaic[0][0].getDatasetCount();
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.IRasterDataSource#getFileSize()
	 */
	public long getFileSize() {
		long size = 0;
		int n = mosaic.length;
		int m = mosaic[0].length;
		for (int row = 0; row < n; row++) { 
			for (int col = 0; col < m; col++) {
				if(mosaic[row][col] != null)
					size += mosaic[row][col].getFileSize();
			}
		}
		return size;
	}
	
	/**
	 * Obtiene el dataset cuyas coordenadas contienen el punto pasado por parámeto
	 * @param x Coordenada X a comprobar
	 * @param y Coordenada Y a comprobar
	 * @return Point2D Posición del MultiRasterDataset dentro del mosaico
	 */
	public Point2D getDatasetByCoords(double x, double y) {
		int n = mosaic.length;
		int m = mosaic[0].length;
		for (int row = 0; row < n; row++) {
			for (int col = 0; col < m; col++) {
				if(mosaic[row][col] != null && 
					 RasterUtilities.isInside(new Point2D.Double(x, y), mosaic[row][col].getExtent(), mosaic[row][col].getAffineTransform(0))) 
					return new Point2D.Double(row, col);
			}
		}

		return null;
	}
	
	/**
	 * Obtiene la lista de datasets del mosaico que intersectan con el extent proporcionado
	 * @param ulx Coordenada X superior izquierda
	 * @param uly Coordenada Y superior izquierda
	 * @param lrx Coordenada X inferior derecha
	 * @param lry Coordenada Y inferior derecha
	 * @return MultiRasterDataset[][][]
	 * @throws NoninvertibleTransformException
	 */
	private MultiRasterDataset[][] getDatasetListInArea(double ulx, double uly, double lrx, double lry) throws NoninvertibleTransformException {
		int n = mosaic.length;
		int m = mosaic[0].length;
		
		MultiRasterDataset[][] result = new MultiRasterDataset[n][m];
		
		for (int row = 0; row < n; row++) 
			for (int col = 0; col < m; col++) 
				if(mosaic[row][col] != null && 
					 RasterUtilities.intersects(new Extent(ulx, uly, lrx, lry), mosaic[row][col].getExtent(), mosaic[row][col].getAffineTransform(0))) {
					for (int k = 0; k < mosaic.length; k++) 
						result[row][col] = mosaic[row][col];	
				}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.IRasterDataSource#isInside(java.awt.geom.Point2D)
	 */
	public boolean isInside(Point2D p) {
		return RasterUtilities.isInside(p, getExtent(), getAffineTransform(0));
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.IRasterDataSource#rasterToWorld(java.awt.geom.Point2D)
	 */
	public Point2D rasterToWorld(Point2D pt) {
		Point2D p = new Point2D.Double();
		getAffineTransform(0).transform(pt, p);
		return p;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.IRasterDataSource#worldToRaster(java.awt.geom.Point2D)
	 */
	public Point2D worldToRaster(Point2D pt) {
		Point2D p = new Point2D.Double();
		try {
			getAffineTransform(0).inverseTransform(pt, p);
		} catch (NoninvertibleTransformException e) {
			return pt;
		}
		return p;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.IRasterDataSource#isRotated()
	 */
	public boolean isRotated() {
		if(getAffineTransform(0).getShearX() != 0 || getAffineTransform(0).getShearY() != 0)
			return true;
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.IRasterDataSource#isGeoreferenced()
	 */
	public boolean isGeoreferenced() {
		//Este tipo de datasets siempre está georreferenciado
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.IRasterDataSource#getDataset(int)
	 */
	public RasterDataset[] getDataset(int i) {
		RasterDataset[] d = new RasterDataset[mosaic.length * mosaic[0].length];
		int count = 0;
		for (int row = 0; row < mosaic.length; row++) { 
			for (int col = 0; col < mosaic[row].length; col++) {
				if(mosaic[row][col] != null)
					d[count] = mosaic[row][col].getDataset(i)[0];
			}
		}
		return d;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.IRasterDataSource#getStatistics()
	 */
	public DatasetListStatistics getStatistics() {
		return stats;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.IRasterDataSource#getBands()
	 */
	public BandList getBands() {
		return bandList;
	}
	
	/**
	 * Genera un buffer de datos único a partir de una matriz de buffers donde puede haber
	 * elementos con valor nulo. 
	 * @return
	 */
	public IBuffer generateBuffer(IBuffer[][] bufList, int drawableBands) {
		int n = mosaic.length;
		int m = mosaic[0].length;
		int nCols = 0, nRows = 0;
		//Contamos el número de filas y columnas del buffer nuevo
		for (int row = 0; row < n; row++) { 
			for (int col = 0; col < m; col++) {
				if(bufList[row][col] != null)
					nCols += bufList[row][col].getWidth();

			}
			if(nCols != 0) break;
		}
		for (int col = 0; col < m; col++) { 
			for (int row = 0; row < n; row++) {
				if(bufList[row][col] != null)
					nRows += bufList[row][col].getHeight();
			}
			if(nRows != 0) break;
		}
		
		//Creamos el buffer
		IBuffer raster = RasterBuffer.getBuffer(bufList[0][0].getDataType(), nCols, nRows, drawableBands, true);
		
		//Hacemos la copia
		int[] pos = new int[2];
		int validCol = 0;
		for (int row = 0; row < n; row++) { 
			for (int col = 0; col < m; col++) {
				pos[1] = (col == 0) ? 0 : pos[1];
				if(bufList[row][col] == null)
					continue;
				validCol = col;
				copyTile(bufList[row][col], raster, pos[0], pos[1]);
				pos[1] +=  bufList[row][col].getWidth();
			}
			if(bufList[row][validCol] != null)
				pos[0] +=  bufList[row][validCol].getHeight();
		}
		return raster;
	}
	
	/**
	 * Copia un tile en el buffer que contendrá todos los tiles
	 * @param origin Buffer de origen
	 * @param dest Buffer de destino
	 * @param col Columna del buffer de destino donde se empieza a escribir
	 * @param row Fila del buffer de destino donde se empieza a escribir
	 * @return array con los valores que representan la última fila y 
	 * última columna que se escribieron 
	 */
	private void copyTile(IBuffer origin, IBuffer dest, int r, int c) {
		switch(origin.getDataType()) {
		case IBuffer.TYPE_BYTE :
			for (int band = 0; band < origin.getBandCount(); band++) 
				for (int row = 0; row < origin.getHeight(); row++) 
					for (int col = 0; col < origin.getWidth(); col++) 
						try {
								dest.setElem(row + r, col + c, band, origin.getElemByte(row, col, band));
							} catch (ArrayIndexOutOfBoundsException e) {break;}
			break;
		case IBuffer.TYPE_SHORT :
			for (int band = 0; band < origin.getBandCount(); band++) 
				for (int row = 0; row < origin.getHeight(); row++) 
					for (int col = 0; col < origin.getWidth(); col++) 
						try {
								dest.setElem(row + r, col + c, band, origin.getElemShort(row, col, band));
							} catch (ArrayIndexOutOfBoundsException e) {break;}
			break;
		case IBuffer.TYPE_FLOAT :
			for (int band = 0; band < origin.getBandCount(); band++) 
				for (int row = 0; row < origin.getHeight(); row++) 
					for (int col = 0; col < origin.getWidth(); col++) 
						try {
								dest.setElem(row + r, col + c, band, origin.getElemFloat(row, col, band));
							} catch (ArrayIndexOutOfBoundsException e) {break;}
			break;
		case IBuffer.TYPE_DOUBLE:
			for (int band = 0; band < origin.getBandCount(); band++) 
				for (int row = 0; row < origin.getHeight(); row++) 
					for (int col = 0; col < origin.getWidth(); col++) 
						try {
								dest.setElem(row + r, col + c, band, origin.getElemDouble(row, col, band));
							} catch (ArrayIndexOutOfBoundsException e) {break;}
			break;
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.IRasterDataSource#getWindowRaster(double, double, double, double)
	 */
	public IBuffer getWindowRaster(double ulx, double uly, double lrx, double lry) 
		throws InvalidSetViewException, InterruptedException, RasterDriverException {
		try {
			MultiRasterDataset[][] datasetList = getDatasetListInArea(ulx, uly, lrx, lry);
			int n = mosaic.length;
			int m = mosaic[0].length;
			IBuffer[][] bufferList = new IBuffer[n][m];
			for (int row = 0; row < n; row++) 
				for (int col = 0; col < m; col++) 
					if(datasetList[row][col] != null)
						bufferList[row][col] = datasetList[row][col].getWindowRaster(ulx, uly, lrx, lry);
			return generateBuffer(bufferList, mosaic[0][0].getBands().getDrawableBandsCount());
		} catch (NoninvertibleTransformException e) {
			throw new InvalidSetViewException("No se ha podido aplicar la transformación inversa para esa vista.");
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.IRasterDataSource#getWindowRaster(double, double, double, double, boolean)
	 */
	public IBuffer getWindowRaster(double ulx, double uly, double w, double h, boolean adjustToExtent) 
		throws InvalidSetViewException, RasterDriverException {
		//TODO: FUNCIONALIDAD: getWindowRaster en CompositeDataset sin implementar
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.IRasterDataSource#getWindowRaster(double, double, double, double, int, int, boolean)
	 */
	public IBuffer getWindowRaster(double ulx, double uly, double lrx, double lry, int bufWidth, int bufHeight, boolean adjustToExtent) 
		throws InvalidSetViewException, InterruptedException, RasterDriverException {
		try {
			Point2D p1 = new Point2D.Double(ulx, uly);
			Point2D p2 = new Point2D.Double(lrx, lry);
			MultiRasterDataset[][] datasetList = getDatasetListInArea(p1.getX(), p1.getY(), p2.getX(), p2.getY());
			Point2D px1 = mosaic[0][0].worldToRaster(p1);
			Point2D px2 = mosaic[0][0].worldToRaster(p2);
			int n = mosaic.length;
			int m = mosaic[0].length;
			IBuffer[][] bufferList = new IBuffer[n][m];
			for (int row = 0; row < n; row++) 
				for (int col = 0; col < m; col++) 
					if(datasetList[row][col] != null) {
						int[] values = getLocalRequest((int)px1.getX(), (int)px1.getY(), (int)Math.abs(px2.getX() - px1.getX()), (int)Math.abs(px2.getY() - px1.getY()), row, col);
						if(values != null) {
							int bW = (int)Math.round((Math.abs(values[2] - values[0]) * bufWidth) / Math.abs(px2.getX() - px1.getX()));
							int bH = (int)Math.round((Math.abs(values[3] - values[1]) * bufHeight) / Math.abs(px2.getY() - px1.getY()));
							int wTile = ((int)Math.abs(values[2] - values[0]) > (int)datasetList[row][col].getWidth()) ? (int)datasetList[row][col].getWidth() : (int)Math.abs(values[2] - values[0]);  
							int hTile = ((int)Math.abs(values[3] - values[1]) > (int)datasetList[row][col].getHeight()) ? (int)datasetList[row][col].getHeight() : (int)Math.abs(values[3] - values[1]);
							bufferList[row][col] = datasetList[row][col].getWindowRaster(values[0], values[1], wTile, hTile, bW, bH);
						}
					}
			return generateBuffer(bufferList, mosaic[0][0].getBands().getDrawableBandsCount());
		} catch (NoninvertibleTransformException e) {
			throw new InvalidSetViewException("No se ha podido aplicar la transformación inversa para esa vista.");
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.IRasterDataSource#getWindowRaster(int, int, int, int)
	 */
	public IBuffer getWindowRaster(int x, int y, int w, int h) 
		throws InvalidSetViewException, InterruptedException, RasterDriverException {
		try {
			Point2D p1 = mosaic[0][0].rasterToWorld(new Point2D.Double(x, y));
			Point2D p2 = mosaic[0][0].rasterToWorld(new Point2D.Double(x + w, y + h));
			MultiRasterDataset[][] datasetList = getDatasetListInArea(p1.getX(), p1.getY(), p2.getX(), p2.getY());
			int n = mosaic.length;
			int m = mosaic[0].length;
			IBuffer[][] bufferList = new IBuffer[n][m];
			for (int row = 0; row < n; row++) 
				for (int col = 0; col < m; col++) 
					if(datasetList[row][col] != null) {
						int[] values = getLocalRequest(x, y, w, h, row, col);
						int wTile = ((int)Math.abs(values[2] - values[0]) > (int)datasetList[row][col].getWidth()) ? (int)datasetList[row][col].getWidth() : (int)Math.abs(values[2] - values[0]);  
						int hTile = ((int)Math.abs(values[3] - values[1]) > (int)datasetList[row][col].getHeight()) ? (int)datasetList[row][col].getHeight() : (int)Math.abs(values[3] - values[1]);
						if(values != null) 
							bufferList[row][col] = datasetList[row][col].getWindowRaster(values[0], values[1], wTile, hTile);
					}
			return generateBuffer(bufferList, mosaic[0][0].getBands().getDrawableBandsCount());
		} catch (NoninvertibleTransformException e) {
			throw new InvalidSetViewException("No se ha podido aplicar la transformación inversa para esa vista.");
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.IRasterDataSource#getWindowRaster(int, int, int, int, int, int)
	 */
	public IBuffer getWindowRaster(int x, int y, int w, int h, int bufWidth, int bufHeight) 
		throws InvalidSetViewException, InterruptedException, RasterDriverException {
		try {
			Point2D p1 = mosaic[0][0].rasterToWorld(new Point2D.Double(x, y));
			Point2D p2 = mosaic[0][0].rasterToWorld(new Point2D.Double(x + w, y + h));
			MultiRasterDataset[][] datasetList = getDatasetListInArea(p1.getX(), p1.getY(), p2.getX(), p2.getY());
			int n = mosaic.length;
			int m = mosaic[0].length;
			IBuffer[][] bufferList = new IBuffer[n][m];
			for (int row = 0; row < n; row++) { 
				for (int col = 0; col < m; col++) {
					if(datasetList[row][col] != null) {
						int[] values = getLocalRequest(x, y, w, h, row, col);
						if(values != null) {
							int bW = Math.round((Math.abs(values[2] - values[0]) * bufWidth) / w);
							int bH = Math.round((Math.abs(values[3] - values[1]) * bufHeight) / h);
							int wTile = ((int)Math.abs(values[2] - values[0]) > (int)datasetList[row][col].getWidth()) ? (int)datasetList[row][col].getWidth() : (int)Math.abs(values[2] - values[0]);  
							int hTile = ((int)Math.abs(values[3] - values[1]) > (int)datasetList[row][col].getHeight()) ? (int)datasetList[row][col].getHeight() : (int)Math.abs(values[3] - values[1]);
							bufferList[row][col] = datasetList[row][col].getWindowRaster(values[0], values[1], wTile, hTile, bW, bH);
						}
					}
				}
			}
			return generateBuffer(bufferList, mosaic[0][0].getBands().getDrawableBandsCount());
		} catch (NoninvertibleTransformException e) {
			throw new InvalidSetViewException("No se ha podido aplicar la transformación inversa para esa vista.");
		}
	}
	
	/**
	 * Convierte una petición global del mosaico en coordenadas pixel a una local al tile
	 * concreto que se está tratando
	 * @param x Posición X de la petición a comprobar
	 * @param y Posición Y de la petición a comprobar
	 * @param w Ancho de la petición a comprobar
	 * @param h Alto de la petición a comprobar
	 * @param r File del tile
	 * @param c Columna del tile
	 * @return cuatro valores correspondientes a la x1, y1, x2, y2 de la petición referente al tile.
	 */
	private int[] getLocalRequest(int x, int y, int w, int h, int r, int c) {
		Point2D p1 = null, p2 = null;
		
		if(!requestGoThroughTile(x, y, w, h, r, c))
			return null;
		
		if(isInside(x, y, r, c))
			p1 = getLocalPixel(x, y);
		else {
			if(getTileFromPixelPoint(x, y)[0] == r)  //Está en la misma fila
				p1 = new Point2D.Double(0, getLocalPixel(x, y).getY());
			else if(getTileFromPixelPoint(x, y)[1] == c) //Está en la misma columna
				p1 = new Point2D.Double(getLocalPixel(x, y).getX(), 0);
			else 
				p1 = new Point2D.Double(0, 0);
		}
		int fx = x + w;
		int fy = y + h;
		if(isInside(fx, fy, r, c)) 
			p2 = getLocalPixel(fx, fy);
		else {
			if(getTileFromPixelPoint(fx, fy)[0] == r)
				p2 = new Point2D.Double(mosaic[r][c].getWidth(), getLocalPixel(fx, fy).getY());
			else if(getTileFromPixelPoint(fx, fy)[1] == c)
				p2 = new Point2D.Double(getLocalPixel(fx, fy).getX(), mosaic[r][c].getHeight());
			else
				p2 = new Point2D.Double(mosaic[r][c].getWidth(), mosaic[r][c].getHeight());
		}
		if(p1 != null && p2 != null)
			return new int[]{(int)p1.getX(), (int)p1.getY(), (int)p2.getX(), (int)p2.getY()};
		return null;
	}
	
	/**
	 * Consulta si una petición en coordenadas pixel de la imagen completa pasa a través de un
	 * tile de posición r, c
	 * @param x Posición X de la petición a comprobar
	 * @param y Posición Y de la petición a comprobar
	 * @param w Ancho de la petición a comprobar
	 * @param h Alto de la petición a comprobar
	 * @param r File del tile
	 * @param c Columna del tile
	 * @return true si la petición pasa a través del tile y false si no pasa.
	 */
	private boolean requestGoThroughTile(int x, int y, int w, int h, int r, int c) {
		if(isInside(x, y, r, c) || isInside(w, h, r, c))
			return true;
		Point2D p1 = getGlobalPixel(x, y, r, c);
		Point2D p2 = getGlobalPixel(w, h, r, c);
		
		//Intersección entre el área pedida y el tile. Si intersectan se devuelve true 
		if(((x <= p1.getX() && w >= p1.getX()) || (x <= p2.getX() && w >= p2.getX())) && 
			 ((y <= p1.getY() && h >= p1.getY()) || (y <= p2.getY() && h >= p2.getY()))) 
			return true;
		return false;
	}
	
	/**
	 * Obtiene el tile correspondiente a un pixel dado
	 * @param x Posición X del punto a comprobar
	 * @param y Posición Y del punto a comprobar
	 * @return Posición en fila-columna
	 */
	public int[] getTileFromPixelPoint(int x, int y) {
		int n = mosaic.length;
		int m = mosaic[0].length;
		for (int row = 0; row < n; row++)
			for (int col = 0; col < m; col++)
				if(isInside(x, y, row, col))
					return new int[]{row, col};
		return null;
	}
	
	/**
	 * Comprueba si un punto (x, y) cae dentro de un Tile (r, c)
	 * @param x Posición X del punto a comprobar
	 * @param y Posición Y del punto a comprobar
	 * @param r Fila del tile a comprobar
	 * @param c Columna del tile a comprobar
	 * @return true si el punto cae dentro del tile y false si no cae
	 */
	private boolean isInside(int x, int y, int r, int c) {
		int posR = -1, posC = -1;
		int sum = 0;
		for (int row = 0; row < mosaic.length; row++) {
			sum += mosaic[row][0].getHeight();
			if(sum >= y) {
				posR = row;
				break;
			}
		}
		sum = 0;
		for (int col = 0; col < mosaic[0].length; col++) {
			sum += mosaic[0][col].getWidth();
			if(sum >= x) {
				posC = col;
				break;
			}
		}
		return (posR == r && posC == c) ? true : false;
	}
	
	
	/**
	 * Obtiene la coordenada pixel local para un raster dada la coordenada pixel global
	 * @param x Coordenada X global
	 * @param y Coordenada Y global
	 * @return Coordenada local
	 */
	private Point2D getLocalPixel(int x, int y) {
		int w = 0, h = 0;
		int sum = 0;
		for (int row = 0; row < mosaic.length; row++) {
			sum += mosaic[row][0].getHeight();
			if(sum >= y) {
				h = y - ((int)(sum - mosaic[row][0].getHeight()));
				break;
			}
		}
		sum = 0;
		for (int col = 0; col < mosaic[0].length; col++) {
			sum += mosaic[0][col].getWidth();
			if(sum >= x) {
				w = x - ((int)(sum - mosaic[0][col].getWidth()));
				break;
			}
		}
		return new Point2D.Double(w, h);
	}
	
	/**
	 * Obtiene la coordenada pixel global para un raster dada la coordenada pixel local
	 * @param x Coordenada X local
	 * @param y Coordenada Y local
	 * @param r Fila del tile
	 * @param c Columna del tile
	 * @return Coordenada global
	 */
	private Point2D getGlobalPixel(int x, int y, int r, int c) {
		int sumX = 0, sumY = 0;
		for (int row = 0; row < (r - 1); row++)
			sumY += mosaic[row][0].getHeight();
		sumY += y;
		for (int col = 0; col < (c - 1); col++) 
			sumX += mosaic[0][col].getWidth();
		sumX += x;
		return new Point2D.Double(sumX, sumY);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.IRasterDataSource#setDrawableBands(int[])
	 */
	public void setDrawableBands(int[] db) {
		int n = mosaic.length;
		int m = mosaic[0].length;
		for (int row = 0; row < n; row++) 
			for (int col = 0; col < m; col++) 
				mosaic[row][col].setDrawableBands(db);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.IRasterDataSource#clearDrawableBands()
	 */
	public void clearDrawableBands() {
		int n = mosaic.length;
		int m = mosaic[0].length;
		for (int row = 0; row < n; row++) 
			for (int col = 0; col < m; col++) 
				mosaic[row][col].clearDrawableBands();
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.IRasterDataSource#addDrawableBand(int, int)
	 */
	public void addDrawableBand(int posRasterBuf, int imageBand){
		int n = mosaic.length;
		int m = mosaic[0].length;
		for (int row = 0; row < n; row++) 
			for (int col = 0; col < m; col++) 
				mosaic[row][col].addDrawableBand(posRasterBuf, imageBand);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.IRasterDataSource#getTransparencyFilesStatus()
	 */
	public Transparency getTransparencyFilesStatus() {
		if(mosaic != null && mosaic[0][0] != null)
			return mosaic[0][0].getTransparencyFilesStatus();
		return new Transparency();
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.IRasterDataSource#getColorTables()
	 */
	public ColorTable[] getColorTables() {
		if(mosaic != null && mosaic[0][0] != null)
			return mosaic[0][0].getColorTables();
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.hierarchy.IHistogramable#getHistogram()
	 */
	public Histogram getHistogram() throws InterruptedException, HistogramException {
		Histogram[][] histogram = new Histogram[mosaic.length][mosaic[0].length];
	
		try {
			for (int i = 0; i < histogram.length; i++) { 
				for (int j = 0; j < histogram[i].length; j++) { 
					histogram[i][j] = new DatasetListHistogram(mosaic[i][j]).getHistogram();
					if(i != 0 || j != 0) 
						histogram[0][0].union(histogram[i][j]);
				}
			}
			return histogram[0][0];
		} catch (FileNotOpenException e) {
			throw new HistogramException("FileNotOpenException");
		} catch (RasterDriverException e) {
			throw new HistogramException("RasterDriverException");
		}
	}
	
	/**
	 * Obtiene la paleta correspondiente a uno de los ficheros que forman el GeoMultiRasterFile
	 * @param i Posición del raster
	 * @return Paleta asociada a este o null si no tiene
	 */
	public ColorTable getColorTable(int i){
		return (mosaic != null) ? mosaic[0][0].getColorTable(i) : null;
	}
	
	/**
	 * Obtiene la paleta correspondiente al nombre del fichero pasado por parámetro. 
	 * @param fileName Nombre del fichero
	 * @return Paleta o null si no la tiene
	 */
	public ColorTable getColorTable(String fileName){
		return (mosaic != null) ? mosaic[0][0].getColorTable(fileName) : null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.IRasterDataSource#getData(int, int, int)
	 */
	public Object getData(int x, int y, int band) throws InvalidSetViewException, FileNotOpenException, RasterDriverException {
		int[] posTile = getTileFromPixelPoint(x, y);
		if(posTile != null) {
			RasterDataset dataset = mosaic[posTile[0]][posTile[1]].getDataset(0)[0];
			Point2D localPixel = getLocalPixel(x, y);
			return dataset.getData((int)localPixel.getX(), (int)localPixel.getY(), band);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.IRasterDataSource#isReadOnly()
	 */
	public boolean isReadOnly() {
		return readOnly;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.IRasterDataSource#setReadOnly(boolean)
	 */
	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.IRasterDataSource#isMemoryBuffer()
	 */
	public boolean isMemoryBuffer() {
		return forceToMemory;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.IRasterDataSource#setMemoryBuffer(boolean)
	 */
	public void setMemoryBuffer(boolean memory) {
		this.forceToMemory = memory;
		if(memory)
			this.readOnly = false;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.RasterDataset#getOverviewCount(int)
	 */
	public int getOverviewCount(int band) throws BandAccessException, RasterDriverException {
		if(band >= getBandCount())
			throw new BandAccessException("Wrong band");
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.IRasterDataSource#overviewsSupport()
	 */
	public boolean overviewsSupport() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.IRasterDataSource#getNoDataValue()
	 */
	public double getNoDataValue() {
		int n = mosaic.length;
		int m = mosaic[0].length;
		if ((n == 0) || (m == 0))
			return RasterLibrary.defaultNoDataValue;
		return mosaic[0][0].getNoDataValue();
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.IRasterDataSource#resetNoDataValue()
	 */
	public void resetNoDataValue() {
		int n = mosaic.length;
		int m = mosaic[0].length;
		for (int row = 0; row < n; row++)
			for (int col = 0; col < m; col++)
				mosaic[row][col].resetNoDataValue();
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.IRasterDataSource#setNoDataValue(double)
	 */
	public void setNoDataValue(double value) {
		int n = mosaic.length;
		int m = mosaic[0].length;
		for (int row = 0; row < n; row++)
			for (int col = 0; col < m; col++)
				mosaic[row][col].setNoDataValue(value);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.IRasterDataSource#isNoDataEnabled()
	 */
	public boolean isNoDataEnabled() {
		int n = mosaic.length;
		int m = mosaic[0].length;
		if ((n == 0) || (m == 0))
			return false;
		return mosaic[0][0].isNoDataEnabled();
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.IRasterDataSource#setNoDataEnabled(boolean)
	 */
	public void setNoDataEnabled(boolean enabled) {
		int n = mosaic.length;
		int m = mosaic[0].length;
		for (int row = 0; row < n; row++)
			for (int col = 0; col < m; col++)
				mosaic[row][col].setNoDataEnabled(enabled);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.IRasterDataSource#loadGeoPointsFromRmf()
	 */
	public GeoPointList loadGeoPointsFromRmf() throws IOException {
		throw new IOException("Not implemented yet");
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.IRasterDataSource#saveGeoPointsToRmf(org.gvsig.raster.datastruct.GeoPointList)
	 */
	public void saveGeoPointsToRmf(GeoPointList geoPoints) throws IOException {
		throw new IOException("Not implemented yet");	
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.IRasterDataSource#getDataset(java.lang.String)
	 */
	public RasterDataset getDataset(String fileName) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.IRasterDataSource#getLastSelectedView()
	 */
	public Extent getLastSelectedView() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.IRasterDataSource#getNameDatasetStringList(int, int)
	 */
	public String[] getNameDatasetStringList(int i, int j) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.IRasterDataSource#getWktProjection()
	 */
	public String getWktProjection() throws RasterDriverException {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.hierarchy.IHistogramable#getPercent()
	 */
	public int getPercent() {
		return 0;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.IRasterDataSource#getColorInterpretation()
	 */
	public DatasetColorInterpretation getColorInterpretation() {
		return null;
	}

	public void removeDataset(String fileName) {}
	public void removeDataset(RasterDataset file) {}
	public void saveGeoToRmf() throws IOException {}
	public void saveRmfModification() throws IOException {}
	public void setAffineTransform(AffineTransform transf) {}
	public void resetPercent() {}
	public void saveObjectToRmf(int file, Class class1, Object value) throws RmfSerializerException {}
	public Object loadObjectFromRmf(Class class1, Object value) throws RmfSerializerException {
		return null;
	}
}