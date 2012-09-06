/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2006 IVER T.I. and Generalitat Valenciana.
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


import java.awt.Dimension;
import java.awt.geom.Point2D;

import org.gvsig.raster.dataset.FileNotFoundInListException;
import org.gvsig.raster.dataset.IBuffer;
import org.gvsig.raster.dataset.IRasterDataSource;
import org.gvsig.raster.dataset.InvalidSetViewException;
import org.gvsig.raster.dataset.MultiRasterDataset;
import org.gvsig.raster.dataset.RasterDataset;
import org.gvsig.raster.dataset.RasterDriverException;
import org.gvsig.raster.datastruct.ColorTable;
import org.gvsig.raster.datastruct.Extent;
import org.gvsig.raster.util.RasterUtilities;

/**
 * <P>
 * Factoria de buffers de datos. Esta clase devuelve al usuario buffers cargados con datos
 * a partir de uno o varios datasets y unos parámetros de selección de área.
 * </P>
 * <P>
 * Para la carga del buffer de datos habrá que asigna las bandas que queremos cargar
 * de todas las disponibles con addDrawableBands. Después se seleccionará el área
 * que queremos cargar con setAreaOfInterest. Este área se define con coordenadas
 * pixel o reales. Finalmente podemos recuperar el buffer con los datos usando la
 * función getRasterBuf.
 * </P>
 * @author Nacho Brodin (nachobrodin@gmail.com)
 *
 */
public class BufferFactory {
	private IRasterDataSource 		mDataset = null;
	private IBuffer					rasterBuf = null;
	private boolean					adjustToExtent = true;

	/**
	 * Extensión de los datos del buffer
	 */
	private Extent					dataExtent = null;
	/**
	 * Lista de paletas asociadas a las bandas cargadas en el DataSource. Estas son calculadas
	 * en las funciones que asignan las bandas a dibujar (addDrawableBands)
	 */
	private ColorTable[]			palette = null;
	/**
	 * Activa o desactiva el supersampleo en la carga del buffer.
	 */
	private boolean					supersamplingLoadingBuffer = true;

	/**
	 * Ancho y alto en pixeles del último buffer asignado
	 */
	private double 					nWidth = 0;
	private double 					nHeight = 0;
	/**
	 * Valor NoData con el que se rellenan las celdas cuando adjustToExtent es false
	 */
	private double                  noDataValueToFill = -99999;

	/**
	 * Constructor
	 */
	public BufferFactory() {}

	/**
	 * Constructor
	 * @param MultiRasterDataset
	 */
	public BufferFactory(IRasterDataSource rmd) {
		mDataset = rmd;
	}

	/**
	 * Constructor
	 * @param grf Lista de geoRasterFile
	 */
	public BufferFactory(RasterDataset[] grf) {
		for(int i = 0; i< grf.length; i++)
			addFile(grf[i]);
	}

	/**
	 * Constructor
	 * @param grf GeoRasterFile
	 */
	public BufferFactory(RasterDataset grf) {
		addFile(grf);
	}

	/**
	 * Añade un MultiRasterDataset al Grid
	 * @param grf MultiRasterDataset a añadir
	 */
	public void addFile(RasterDataset grf) {
		if(mDataset == null)
			mDataset = new MultiRasterDataset();
		try{
			mDataset.addDataset(new RasterDataset[]{grf});
		}catch(FileNotFoundInListException e) {
			//El fichero está en la lista por lo que no lo añadimos
		}
	}

	/**
	 * Elimina un GeoRasterFile del Grid
	 * @param grf GeoRasterFile a eliminar
	 */
	public void removeFile(RasterDataset grf) {
		mDataset.removeDataset(grf);
	}

	/**
	 * Elimina un GeoRasterFile del Grid
	 * @param fileName Nombre del fichero a eliminar su GeoRasterFile
	 */
	public void removeFile(String fileName) {
		mDataset.removeDataset(fileName);
	}

	/**
	 * Obtiene el número de ficheros del que está compuesta la fuente de datos.
	 * @return
	 */
	public int getFileCount() {
		return  mDataset.getDatasetCount();
	}

	/**
	 * Obtiene la estructura que contiene la lista de ficheros del Grid
	 * @return GeoRasterMultiFile
	 */
	public IRasterDataSource getDataSource() {
		return mDataset;
	}

	/**
	 * Libera el buffer de memoria
	 */
	public void free() {
		if(rasterBuf != null) {
			rasterBuf.free();
			rasterBuf = null;
		}
		mDataset = null;
	}

	/**
	 * Resetea la asignación de dibujado de las bandas de la imagen
	 * sobre el DataImage cuando se hace un update para esta banda.
	 */
	public void clearDrawableBand() {
		for(int i = 0; i < mDataset.getDatasetCount(); i++)
			mDataset.getBands().clearDrawableBands();
		palette = null;
	}

	/**
	 * Para este GeoRasterFile asigna que bandas se pintaran
	 * sobre el RasterBuf cuando se haga un update. Cada posición del vector es una banda
	 * del rasterBuf y el contenido de esa posición es la banda de la imagen que se dibujará
	 * sobre ese RasterBuf.
	 * @param drawableBands	Array con las bandas a dibujar.
	 * @return array con tantos elementos como bandas a dibujar. El valor contenido es el fichero del
	 * dataset multifichero al que corresponde la banda.
	 */
	public int[] setDrawableBands(int[] drawableBands) {
		clearDrawableBand();
		mDataset.setDrawableBands(drawableBands);

		int[] files = new int[drawableBands.length];
		palette = new ColorTable[drawableBands.length];

		for(int i = 0; i< drawableBands.length; i++) {
			if(drawableBands[i] < 0 || drawableBands[i] >= mDataset.getBandCount())
				continue;
			mDataset.addDrawableBand(i, drawableBands[i]);
			String fileName = mDataset.getBands().getBand(drawableBands[i]).getFileName();
			files[i] = mDataset.getBands().getFileNumber(fileName);
			palette[i] = mDataset.getColorTable(fileName);
		}
		return files;
	}

	/**
	 * Para este GeoRasterFile asigna que bandas se pintaran
	 * sobre el RasterBuf cuando se haga un update. Cada posición del vector es una banda
	 * del rasterBuf y el contenido de esa posición es la banda de la imagen que se dibujará
	 * sobre ese RasterBuf. Esta llamada asigna todas las bandas dibujables en su orden natural.
	 * @return array con tantos elementos como bandas a dibujar. El valor contenido es el fichero del
	 * dataset multifichero al que corresponde la banda.
	 */
	public int[] setAllDrawableBands() {
		clearDrawableBand();
		
		int[] list = new int[mDataset.getBandCount()];
		for(int i = 0; i< mDataset.getBandCount(); i++)
			list[i] = i;
		mDataset.setDrawableBands(list);
		
		int[] files = new int[mDataset.getBandCount()];
		palette = new ColorTable[mDataset.getBandCount()];

		for(int i = 0; i< mDataset.getBandCount(); i++) {
			mDataset.addDrawableBand(i, i);
			String fileName = mDataset.getBands().getBand(i).getFileName();
			files[i] = mDataset.getBands().getFileNumber(fileName);
			palette[i] = mDataset.getColorTable(fileName);
		}
		return files;
	}

	/**
	 * Obtiene el array que contiene el orden de bandas. Cada posición del vector es una banda
	 * del rasterBuf y el contenido de esa posición es la banda de la imagen que se dibujará
	 * sobre ese RasterBuf.
	 * @return Array de enteros con el orden de las badas
	 */
	public int[] getDrawableBands() {
		return mDataset.getBands().getDrawableBands();
	}

	/**
	 * Asigna el área de interes en coordenadas del mundo real. Si las coordenadas exceden del tamaño de la imagen
	 * estas coordenadas son ajustadas el extent.
	 * @param x Coordenada X, esquina superior izquierda
	 * @param y Coordenada Y, esquina superior izquierda
	 * @param w Ancho del área
	 * @param h Alto del área
	 * @throws ArrayIndexOutOfBoundsException
	 * @throws InvalidSetViewException 
	 */
	public void setAreaOfInterest(double x, double y, double w, double h)
		throws RasterDriverException, InvalidSetViewException, InterruptedException {
		dataExtent = new Extent(x, y, x + w, y - h);

		Extent adjustedDataExtent = RasterUtilities.calculateAdjustedView(dataExtent, mDataset.getAffineTransform(0), new Dimension((int)mDataset.getWidth(), (int)mDataset.getHeight()));
		
		rasterBuf = mDataset.getWindowRaster(adjustedDataExtent.getMin().getX(), adjustedDataExtent.getMax().getY(), adjustedDataExtent.width(), adjustedDataExtent.height(), adjustToExtent);
	}

	/**
	 * Asigna el área de interes en coordenadas del mundo real. Si las coordenadas exceden del tamaño de la imagen
	 * estas coordenadas son ajustadas el extent.
	 * @param x Coordenada X, esquina superior izquierda
	 * @param y Coordenada Y, esquina superior izquierda
	 * @param w Ancho del área
	 * @param h Alto del área
	 * @param bufWidth Ancho del buffer
	 * @param bufHeight Alto del buffer
	 * @return En caso de que el buffer sea mayor que el tamaño seleccionado de raster se produce supersampleo. La función devuelve
	 * un array de dos elementos que representan el desplazamiento en pixels de X e Y de la esquina superior izquierda.
	 * @throws ArrayIndexOutOfBoundsException
	 * @throws InvalidSetViewException 
	 */
	public double[] setAreaOfInterest(double ulx, double uly, double lrx, double lry, int bufWidth, int bufHeight)
		throws RasterDriverException, InvalidSetViewException, InterruptedException {
		dataExtent = new Extent(ulx, uly, lrx, lry);
		Extent adjustedDataExtent = RasterUtilities.calculateAdjustedView(dataExtent, mDataset.getAffineTransform(0), new Dimension((int)mDataset.getWidth(), (int)mDataset.getHeight()));

		//Caso 3D: La petición no se ajusta al área y se rellena el exterior con NoData
		if(!adjustToExtent && !RasterUtilities.isInside(dataExtent, mDataset.getExtent())) 
			return requestFillingWithNoData(dataExtent, adjustedDataExtent, bufWidth, bufHeight);
		
		//Esta sección es para que no supersamplee el driver y pueda hacerse en el cliente
		if(!isSupersamplingLoadingBuffer()) {
			//nWidth = ((adjustedDataExtent.width() * mDataset.getDataset(0).getWidth()) / mDataset.getExtentForRequest().width());
			//nHeight = ((adjustedDataExtent.height() * mDataset.getDataset(0).getHeight()) / mDataset.getExtentForRequest().height());
			Point2D p1 = mDataset.worldToRaster(new Point2D.Double(adjustedDataExtent.getULX(), adjustedDataExtent.getULY()));
			Point2D p2 = mDataset.worldToRaster(new Point2D.Double(adjustedDataExtent.getLRX(), adjustedDataExtent.getLRY()));
			nWidth = Math.abs(p1.getX() - p2.getX());
			nHeight = Math.abs(p1.getY() - p2.getY());

			if(bufWidth > Math.ceil(nWidth) && bufHeight > Math.ceil(nHeight)) {
				rasterBuf = mDataset.getWindowRaster(adjustedDataExtent.getULX(), adjustedDataExtent.getULY(), adjustedDataExtent.getLRX(), adjustedDataExtent.getLRY());
				double[] step = mDataset.calcSteps(adjustedDataExtent.getULX(), adjustedDataExtent.getULY(), adjustedDataExtent.getLRX(), adjustedDataExtent.getLRY(), nWidth, nHeight, bufWidth, bufHeight);
				return step;
			}
		}
		rasterBuf = mDataset.getWindowRaster(adjustedDataExtent.getULX(), adjustedDataExtent.getULY(), adjustedDataExtent.getLRX(), adjustedDataExtent.getLRY(), bufWidth, bufHeight, true /*Siempre ajustado*/);
		return null;
	}
	
	/**
	 * Método que crea un buffer con la extensión que se ha pedido completa y sin ajustar
	 * a la extensión del raster. La zona que tenga información del raster se rellenara con 
	 * esta y la que quede vacia se rellenará con valores NoData. El ancho y alto del buffer corresponden
	 * a toda la zona solicitada, tanto la que cae dentro como la rellena con Nodata
	 * 
	 * @param rasterBuf Buffer de salida.
	 * @throws InvalidSetViewException 
	 */
	private double[] requestFillingWithNoData(Extent requestExtent, Extent fitExtent, int bufWidth, int bufHeight) 
		throws InvalidSetViewException, InterruptedException, RasterDriverException {
		double error = 0.01;
		//Upper Left
		double distWcX = Math.abs(fitExtent.getMin().getX() - dataExtent.getMin().getX());
		distWcX = (distWcX > error) ? distWcX : 0;
		double distWcY = Math.abs(fitExtent.getMax().getY() - dataExtent.getMax().getY());
		distWcY = (distWcY > error) ? distWcY : 0;
		//Pixel inicial del buffer donde se empieza a dibujar. Redondeamos por arriba pq lo que sobra se pone NoData
		double initPxX = Math.ceil((distWcX * bufWidth) / requestExtent.width()); 
		double initPxY = Math.ceil((distWcY * bufHeight) / requestExtent.height());

		//Lower Right
		distWcX = Math.abs(fitExtent.getMax().getX() - dataExtent.getMin().getX());
		distWcX = (distWcX > error) ? distWcX : 0;
		distWcY = Math.abs(fitExtent.getMin().getY() - dataExtent.getMax().getY());
		distWcY = (distWcY > error) ? distWcY : 0;
		//Pixel final del buffer donde se dibuja. Redondeamos por abajo pq lo que sobra se pone NoData
		double endPxX = Math.floor((distWcX * bufWidth) / requestExtent.width()); 
		double endPxY = Math.floor((distWcY * bufHeight) / requestExtent.height());

		int copyX = (int)Math.abs(endPxX - initPxX);
		int copyY = (int)Math.abs(endPxY - initPxY);

		rasterBuf = mDataset.getWindowRaster(fitExtent.getULX(), fitExtent.getULY(), fitExtent.getLRX(), fitExtent.getLRY(), copyX, copyY, true);
		IBuffer buf = RasterBuffer.getBuffer(mDataset.getDataType()[0], bufWidth, bufHeight, rasterBuf.getBandCount(), true);
		buf.setNoDataValue(noDataValueToFill);
		for(int i = 0; i < buf.getBandCount(); i++) {
			switch(buf.getDataType()) {
			case IBuffer.TYPE_BYTE:buf.assign(i, rasterBuf.getByteNoDataValue());break;
			case IBuffer.TYPE_SHORT:buf.assign(i, rasterBuf.getShortNoDataValue());break;
			case IBuffer.TYPE_INT:buf.assign(i, rasterBuf.getIntNoDataValue());break;
			case IBuffer.TYPE_FLOAT:buf.assign(i, rasterBuf.getFloatNoDataValue());break;
			case IBuffer.TYPE_DOUBLE:buf.assign(i, rasterBuf.getNoDataValue());break;
			}
		}	

		switch(rasterBuf.getDataType()) {
		case IBuffer.TYPE_BYTE:
			for (int iBand = 0; iBand < rasterBuf.getBandCount(); iBand++)	
				for (int row = 0; row < copyY; row++) 
					for (int col = 0; col < copyX; col++) 
						buf.setElem((int)(row + initPxY), (int)(col + initPxX), 
								iBand, 
								rasterBuf.getElemByte(row, col, iBand));
			break;
		case IBuffer.TYPE_SHORT:
		case IBuffer.TYPE_USHORT:
			for (int iBand = 0; iBand < rasterBuf.getBandCount(); iBand++)	
				for (int row = 0; row < copyY; row++) 
					for (int col = 0; col < copyX; col++) 
						buf.setElem((int)(row + initPxY), (int)(col + initPxX), 
								iBand, 
								rasterBuf.getElemShort(row, col, iBand));
			break;
		case IBuffer.TYPE_INT:
			for (int iBand = 0; iBand < rasterBuf.getBandCount(); iBand++)	
				for (int row = 0; row < copyY; row++) 
					for (int col = 0; col < copyX; col++) 
						buf.setElem((int)(row + initPxY), (int)(col + initPxX), 
								iBand, 
								rasterBuf.getElemInt(row, col, iBand));
			break;
		case IBuffer.TYPE_FLOAT:
			for (int iBand = 0; iBand < rasterBuf.getBandCount(); iBand++)	
				for (int row = 0; row < copyY; row++) 
					for (int col = 0; col < copyX; col++) 
						buf.setElem((int)(row + initPxY), (int)(col + initPxX), 
								iBand, 
								rasterBuf.getElemFloat(row, col, iBand));
			break;
		case IBuffer.TYPE_DOUBLE:
			for (int iBand = 0; iBand < rasterBuf.getBandCount(); iBand++)	
				for (int row = 0; row < copyY; row++) 
					for (int col = 0; col < copyX; col++) 
						buf.setElem((int)(row + initPxY), (int)(col + initPxX), 
								iBand, 
								rasterBuf.getElemDouble(row, col, iBand));
			break;
		}
		rasterBuf = buf;
		return null;
	}
	
	/**
	 * Dado unas coordenadas reales, un tamaño de buffer y un tamaño de raster. 
	 * Si el buffer es de mayor tamaño que el raster (supersampleo) quiere decir que 
	 * por cada pixel de buffer se repiten varios del raster. Esta función calcula el 
	 * número de pixels de desplazamiento en X e Y que corresponden al primer pixel del
	 * buffer en la esquina superior izquierda. Esto es necesario porque la coordenada
	 * solicitada es real y puede no caer sobre un pixel completo. Este calculo es
	 * util cuando un cliente quiere supersamplear sobre un buffer y que no se lo haga
	 * el driver automáticamente.
	 * @param dWorldTLX Coordenada real X superior izquierda
	 * @param dWorldTLY Coordenada real Y superior izquierda
	 * @param dWorldBRX Coordenada real X inferior derecha
	 * @param dWorldBRY Coordenada real Y inferior derecha
	 * @param nWidth Ancho del raster
	 * @param nHeight Alto del raster
	 * @param bufWidth Ancho del buffer
	 * @param bufHeight Alto del buffer
	 * @return Array de dos elementos con el desplazamiento en X e Y. 
	 */
	public double[] calcSteps(double ulx, double uly, double lrx, double lry, 
			double nWidth, double nHeight, int bufWidth, int bufHeight) {
		Point2D p1 = mDataset.worldToRaster(new Point2D.Double(ulx, uly));
		Point2D p2 = mDataset.worldToRaster(new Point2D.Double(lrx, lry));
		int width = (int)Math.abs(Math.ceil(p2.getX()) - Math.floor(p1.getX())); 
		int height = (int)Math.abs(Math.floor(p1.getY()) - Math.ceil(p2.getY()));
		Point2D wc1 = mDataset.rasterToWorld(new Point2D.Double(Math.floor(p1.getX()), Math.floor(p1.getY())));
		Point2D wc2 = mDataset.rasterToWorld(new Point2D.Double(Math.ceil(p2.getX()), Math.ceil(p2.getY())));
		
		return mDataset.calcSteps(wc1.getX(), wc1.getY(), wc2.getX(), wc2.getY(), width, height, bufWidth, bufHeight);
	}

	/**
	 * Asigna el área de interes en coordenadas pixel. Si las coordenadas exceden del tamaño de la imagen
	 * lanza una excepción.
	 * @param x Coordenada X, esquina superior izquierda
	 * @param y Coordenada Y, esquina superior izquierda
	 * @param w Ancho del área
	 * @param h Alto del área
	 * @throws InvalidSetViewException  
	 * @throws ArrayIndexOutOfBoundsException
	 */
	public void setAreaOfInterest(int x, int y, int w, int h) 
		throws InvalidSetViewException, InterruptedException, RasterDriverException {
		if(x > getSourceWidth() || y > getSourceHeight())
			throw new InvalidSetViewException("Parámetros incorrectos en setAreaOfInterest");
		x = (x < 0) ? 0 : x;
		y = (y < 0) ? 0 : y;
		w = (w > getSourceWidth()) ? getSourceWidth() : w;
		h = (h > getSourceHeight()) ? getSourceHeight() : h;

		dataExtent = new Extent(mDataset.rasterToWorld(new Point2D.Double(x, y)),
								mDataset.rasterToWorld(new Point2D.Double(x + w, y + h)));
		rasterBuf = mDataset.getWindowRaster(x, y, w, h);
	}

	/**
	 * Asigna el área de interés a toda la extensión del raster.
	 */
	public void setAreaOfInterest() 
		throws InterruptedException, RasterDriverException {
		dataExtent = mDataset.getExtent();
		try {
			rasterBuf = mDataset.getWindowRaster(0, 0, (int)mDataset.getWidth(), (int)mDataset.getHeight());
		} catch (InvalidSetViewException e) {
			//Esta excepción no debería darse ya que las coordenadas se asignan automáticamente por lo que no
			//tiene sentido lanzarla hacia arriba
			e.printStackTrace();
		}
	}

	/**
	 * Asigna el área de interes en coordenadas pixel. Esta operación cargará un RasterBuffer con los datos solicitados por
	 * lo que, si al acabar hacemos getRasterBuf obtendremos la matriz de datos. Si las coordenadas exceden del tamaño
	 * de la imagen lanza una excepción.
	 * @param x Coordenada X, esquina superior izquierda
	 * @param y Coordenada Y, esquina superior izquierda
	 * @param w Ancho del área
	 * @param h Alto del área
	 * @param bufWidth Ancho del buffer
	 * @param bufHeight Alto del buffer
	 * @throws ArrayIndexOutOfBoundsException
	 */
	public void setAreaOfInterest(int x, int y, int w, int h, int bufWidth, int bufHeight)
		throws InvalidSetViewException, InterruptedException, RasterDriverException {
		if(x > getSourceWidth() || y > getSourceHeight())
			throw new InvalidSetViewException("Parámetros incorrectos en setAreaOfInterest");
		
		x = (x < 0) ? 0 : x;
		y = (y < 0) ? 0 : y;
		w = (w > getSourceWidth()) ? getSourceWidth() : w;
		h = (h > getSourceHeight()) ? getSourceHeight() : h;

		dataExtent = new Extent(mDataset.rasterToWorld(new Point2D.Double(x, y)),
								mDataset.rasterToWorld(new Point2D.Double(x + w, y + h)));

		rasterBuf = mDataset.getWindowRaster(x, y, w, h, bufWidth, bufHeight);
	}

	/**
	 * Obtiene la altura de la fuente de datos.
	 * @return altura en celdas del grid.
	 */
	public int getSourceHeight() {
		return (mDataset != null) ? (int)mDataset.getHeight() : 0;
	}

	/**
	 * Obtiene la anchura de la fuente de datos.
	 * @return anchura en celdas del grid.
	 */
	public int getSourceWidth() {
		return (mDataset != null) ? (int)mDataset.getWidth() : 0;
	}

	/**
	 * Obtiene el número de bandas
	 * @return Número de bandas
	 */
	public int getBandCount() {
		if(rasterBuf != null)
			return rasterBuf.getBandCount();
		else
			return this.getDataSource().getBandCount();
	}

	/**
	 * Obtiene el buffer de datos del grid
	 * @return RasterBuf
	 */
	public IBuffer getRasterBuf() {
		if (rasterBuf != null && mDataset != null)
			rasterBuf.setNoDataValue(mDataset.getNoDataValue());
		return rasterBuf;
	}

	/**
	 * Obtiene un buffer desde la posición x,y en pixeles de ancho w y alto h
	 * @param x Coordenada x de la esquina superior izquierda
	 * @param y Coordenada y de la esquina superior izquierda
	 * @param w Ancho del grid
	 * @param h Alto del grid
	 * @return Buffer de datos
	 */
	public RasterBuffer getData(int x, int y, int w, int h) {
		return null;
	}

	/**
	 * Obtiene el extent de la última selección hecha con alguna de las llamadas
	 * setAreaOfInterest. Este extent es devuelto en coordenadas reales con las transformaciones
	 * que se hayan aplicado sobre el/los dataset.
	 * @return Extent Coordenadas reales que representan el último área de datos
	 * solicitada.
	 */
	public Extent getLastSelectedView() {
		return mDataset.getLastSelectedView();
	}

	/**
	 * Obtiene el extent correspondiente a los datos cargados en el buffer
	 * @return Extent
	 */
	public Extent getDataExtent() {
		return dataExtent;
	}

	/**
	 * Obtiene la lista de paletas asociadas a las bandas cargadas en el DataSource
	 * @return Lista con las paletas o null si no hay ninguna asocida. Un nulo en una
	 * posición del array también indicará que para esa banda no hay paletas asociadas.
	 */
	public ColorTable[] getColorTables() {
		return palette;
	}

	/**
	 * Consulta el flag de supersampleo en la carga del buffer.
	 * <P>
	 * Si este flag es false
	 * y pasamos un buffer de tamaño mayor que el número de pixels del área requerida en la
	 * llamada setAreaOfInterest entonces se ajustará este buffer al número de pixeles contenidos
	 * en el área.
	 * </P>
	 * <P>
	 * Por ejemplo, si solicitamos un área de 5x4 pixels de un raster y pedimos que nos los grabe
	 * en un buffer de 500x400, si esta variable es false el buffer lo generará de 5x4. Si esta
	 * variable es true el buffer lo generará de 500x400.
	 * </P>
	 *
	 * @return true si el supersampleo en la carga del buffer está activado y false si no lo está.
	 */
	public boolean isSupersamplingLoadingBuffer() {
		return supersamplingLoadingBuffer;
	}

	/**
	 * Activa o desactiva el supersampling en la carga del buffer.
	 * <P>
	 * Si este flag es false
	 * y pasamos un buffer de tamaño mayor que el número de pixels del área requerida en la
	 * llamada setAreaOfInterest entonces se ajustará este buffer al número de pixeles contenidos
	 * en el área.
	 * </P>
	 * <P>
	 * Por ejemplo, si solicitamos un área de 5x4 pixels de un raster y pedimos que nos los grabe
	 * en un buffer de 500x400, si esta variable es false el buffer lo generará de 5x4. Si esta
	 * variable es true el buffer lo generará de 500x400.
	 * </P>
	 *
	 * @param supersamplingLoadingBuffer true o false para activar o desactivar el supersampling en la
	 * carga del buffer.
	 */
	public void setSupersamplingLoadingBuffer(boolean supersamplingLoadingBuffer) {
		this.supersamplingLoadingBuffer = supersamplingLoadingBuffer;
	}

	/**
	 * Obtiene el flag que ajusta el extent de la petición al del raster. Si está a
	 * true en caso de que el extent de la petición sea mayor lo ajustará a los limites
	 * de este. Si está a false no lo ajustará rellenando los valores con NoData. Por defecto
	 * estará a true.
	 * @return true si ajusta y false si no lo hace
	 */
	public boolean isAdjustToExtent() {
		return adjustToExtent;
	}

	/**
	 * Asigna el flag que ajusta el extent de la petición al del raster. Si está a
	 * true en caso de que el extent de la petición sea mayor lo ajustará a los limites
	 * de este. Si está a false no lo ajustará rellenando los valores con NoData
	 * @param adjustToExtent true para ajustar y false si no queremos que lo haga. Por defecto
	 * estará a true.
	 */
	public void setAdjustToExtent(boolean adjustToExtent) {
		this.adjustToExtent = adjustToExtent;
	}
	
	/**
	 * Asigna el valor noData con el que se rellenan las celdas cuando se hace una petición
	 * en la que no se quiere que se ajuste al área del raster. Por defecto noData tendrá el valor
	 * que aparece en IBuffer.
	 * @param noData
	 */
	public void setNoDataToFill(double noData) {
		this.noDataValueToFill = noData;
	}

	/**
	 * Consulta si el buffer siguiente a pedir es de solo lectura o lectura y escritura.
	 * La asignación del flag de solo lectura a true debe hacerse para cada consulta.
	 * @return true si la siguiente carga de buffer se hace de solo lectura y false si es de lectura/escritura 
	 */
	public boolean isReadOnly() {
		if(mDataset != null)
			return mDataset.isReadOnly();
		return false;
	}
	
	/**
	 * Asigna el flag que dice si la carga del siguiente buffer es de solo lectura o lectura/escritura.
	 * La asignación del flag de solo lectura a true debe hacerse para cada consulta.
	 * @param readOnly true si la siguiente carga de buffer se hace de solo lectura y false si es de lectura/escritura
	 */
	public void setReadOnly(boolean readOnly) {
		if(mDataset != null)
			mDataset.setReadOnly(readOnly);
	}
	
	/**
	 * Asigna el flag que dice si la carga del siguiente buffer es en memoria
	 * @param memory true si la siguiente carga de buffer se hace en memoria y false se deja decidir al dataset 
	 * el tipo de buffer
	 */
	public void setMemoryBuffer(boolean readOnly) {
		if(mDataset != null)
			mDataset.setMemoryBuffer(readOnly);
	}
}