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

import java.io.FileNotFoundException;

import org.gvsig.raster.RasterLibrary;
import org.gvsig.raster.buffer.cache.RasterCache;
import org.gvsig.raster.buffer.cache.RasterReadOnlyBuffer;
import org.gvsig.raster.dataset.IBuffer;
import org.gvsig.raster.dataset.IRasterDataSource;
import org.gvsig.raster.dataset.NotSupportedExtensionException;
import org.gvsig.raster.dataset.RasterDriverException;
import org.gvsig.raster.datastruct.Histogram;
import org.gvsig.raster.process.RasterTask;
import org.gvsig.raster.process.RasterTaskQueue;
import org.gvsig.raster.util.RasterUtilities;

/**
 * Rectangulo de pixeles. Para cada tipo de datos java hay un buffer distinto donde cada elemento es
 * accedido de la siguiente forma: [banda][fila][columna]
 * m[1][2][0] = cte;-> Sustituye el elemento de la fila 2 de la banda 1 columna 0
 * m[1][0] = array; -> Sustituye la fila 0 de la banda 1 
 * m[0] = matriz cuadrada; -> Sustituye la banda entera.
 * 
 */
public abstract class RasterBuffer implements IBuffer {
	public static final int      INTERPOLATION_PROCESS = 0;
	public static final int      HISTOGRAM_PROCESS = 1;
	
	protected boolean[]		     cancel = new boolean[1];

	public double			     noDataValue = -99999;

	protected int			     progressHistogram = 0;
	protected int			     progressInterpolation = 0;
	protected boolean		     canceled = false;

	protected int			     width;
	protected int			     height;
	protected int			     nBands;
	protected int			     dataType;
	
	/**
	 * Variable estática que si está a false desactiva el uso de caché. Puede ser usada por un cliente
	 * para cargar siempre los datos en memoria. independientemente de su tamaño. 
	 */
	public static boolean		      cacheOn = true;
	/**
	 * Fuerza la carga de los datos en caché independientemente de su tamaño. Su
	 * uso suele ser util solo para depuración. Su valor por defecto y recomendado
	 * es siempre false.
	 */
	public static boolean 	      forceToLoadInCache = false;
	/**
	 * Fuerza la carga de los datos en caché de solo lectura independientemente de su tamaño. Su
	 * uso suele ser util solo para depuración. Su valor por defecto y recomendado
	 * es siempre false.
	 */
	public static boolean 	     forceToLoadInReadOnlyCache = false;
	/**
	 * Valor con el que se rellena una banda no valida del buffer. Una banda no valida es la que 
	 * no tiene datos asignados y tampoco puede ser null. Todas las bandas no validas de un buffer
	 * apuntan por referencia a la misma banda.
	 */
	protected double			     notValidValue = 0D;
	
	private BufferInterpolation	 interp = null;
	
	/**
	 * Proceso del cual se devuelve el porcentaje cuando este es solicitado
	 */
	private int                    process = HISTOGRAM_PROCESS;

	/**
	 * Genera instancias del buffer de datos adecuado al tamaño del raster. Si no hay muchos datos
	 * (menos de cacheMemorySize) creará un buffer en memoria. Si hay más de esta cantidad
	 * entonces crearemos un buffer cacheado (RasterCache). A partir de la cantidad señalada
	 * por multicacheMemorySize haremos un buffer cacheado donde cada página no ocupa todo
	 * el ancho del raster ya que este será muy grande. La gestión de una cache donde cada
	 * pagina ha de partir una línea lleva una complejidad añadida.
	 *  
	 * @param dataType Tipo de dato
	 * @param width Ancho
	 * @param height Alto
	 * @param bandNr Banda
	 * @param flag En caso de buffers de memoria este flag a true significa que se reserva la memoria
	 * para el buffer de forma normal y si está a false no se reserva por lo que la reserva deberá ser
	 * posterior. 
	 * @return Objeto RasterBuffer
	 * @throws RasterDriverException 
	 * @throws NotSupportedExtensionException 
	 * @throws FileNotFoundException 
	 */
	public static RasterBuffer getBuffer(int dataType, int width, int height, int bandNr, boolean malloc) 
		/*throws FileNotFoundException, NotSupportedExtensionException, RasterDriverException*/{
		//Opción de cachear siempre activada (Solo DEBUG)
		if(forceToLoadInCache)
			return new RasterCache(dataType, width, height, bandNr);
		if(forceToLoadInReadOnlyCache){
		return new RasterReadOnlyBuffer(dataType, width, height, bandNr);
		}
			
		if(cacheOn){
			long size = (RasterUtilities.getBytesFromRasterBufType(dataType) * width * height * bandNr) / 1024;
			long ms1 = RasterLibrary.cacheSize * 1024;
			if(size <= ms1)
				return new RasterMemoryBuffer(dataType, width, height, bandNr, malloc);
			else
				return new RasterCache(dataType, width, height, bandNr);
		}else
			return new RasterMemoryBuffer(dataType, width, height, bandNr, malloc);
	}
		
	/**
	 * Genera una instancia del buffer de solo lectura. Este buffer consta de una cache y unos apuntadores
	 * a las páginas en disco. Cuando se accede a los datos se carga en memoria la página pedida.
	 *  
	 * @param dataType Tipo de dato
	 * @param width Ancho
	 * @param height Alto
	 * @param bandNr Banda
	 */
	public static RasterBuffer getReadOnlyBuffer(int dataType, int width, int height, int bandNr) {
		return new RasterReadOnlyBuffer(dataType, width, height, bandNr);
	}
	
	/**
	 * Genera una instancia del buffer de solo lectura. Este buffer consta de una cache y unos apuntadores
	 * a las páginas en disco. Cuando se accede a los datos se carga en memoria la página pedida.
	 *  
	 * @param dataType Tipo de dato
	 * @param width Ancho
	 * @param height Alto
	 * @param bandNr Banda
	 * @param flag En caso de buffers de memoria este flag a true significa que se reserva la memoria
	 * para el buffer de forma normal y si está a false no se reserva por lo que la reserva deberá ser
	 * posterior. 
	 */
	public static RasterBuffer getMemoryBuffer(int dataType, int width, int height, int bandNr, boolean malloc) {
		return new RasterMemoryBuffer(dataType, width, height, bandNr, malloc);
	}
	/**
	 * Devuelve true si el tamaño del dataset es menor que el de la caché y false 
	 * si no lo es.
	 * @param datasource Fuente de datos
	 * @return true si podemos cargar en memoria el raster
	 */
	public static boolean loadInMemory(IRasterDataSource datasource) {
		return (datasource.getFileSize() < (RasterLibrary.cacheSize * 1048576));
	}
	
	/**
	 * Dadas unas coordenadas pixel y un número de bandas, esta función comprueba si 
	 * el tamaño de ventana que va a generarse supera el tamaño de la caché 
	 * @param coords Coordenadas pixel del raster
	 * @param bands Número de bandas
	 * @param ds
	 * @return true si la ventana supera el tamaño de la caché o false si no lo supera.
	 */
	public static boolean isBufferTooBig(double[] coords, int bands) {		
		int w = (int)Math.abs(coords[2] - coords[0]);
		int h = (int)Math.abs(coords[3] - coords[1]);

		long windowSize = w * h * bands; 
		return (windowSize > (RasterLibrary.cacheSize * 1048576));
	}  
	
	/**
	 * Dadas unas coordenadas pixel y un número de bandas, esta función comprueba si 
	 * el tamaño de ventana que va a generarse supera el tamaño de la caché 
	 * @param coords Coordenadas pixel del raster
	 * @param bands Número de bandas
	 * @param ds
	 * @return true si la ventana supera el tamaño de la caché o false si no lo supera.
	 */
	public static boolean isBufferTooBig(double[] coords, double resolution, int bands) {
		double wPx = (coords[0] - coords[2]) / resolution;
		double hPx = (coords[1] - coords[3]) / resolution;
		return isBufferTooBig(new double[]{0, 0, wPx, hPx}, bands);
	}  
	
	/**
	 * Reserva de memoria para el rasterbuf
	 * @param dataType Tipo de dato
	 * @param width Ancho
	 * @param height Alto
	 * @param bandNr Numero de bandas
	 * @param orig
	 */
	public abstract void malloc(int dataType, int width, int height, int bandNr);
 
	/*
	 *  (non-Javadoc)
	 * @see org.gvsig.fmap.driver.IBuffer#getWidth()
	 */
	public int getWidth() {
			return width;
	}

 /*
	*  (non-Javadoc)
	* @see org.gvsig.fmap.driver.IBuffer#getHeight()
	*/
	public int getHeight() {
			return height;
	}

	/*
	 *  (non-Javadoc)
	 * @see org.gvsig.fmap.driver.IBuffer#getBandCount()
	 */
	public int getBandCount() {
			return nBands;
	}

	/**
	 * Obtiene el tipo de dato. Los tipos de dato posibles están definidos en IRaster.
	 * @return tipo de datos
	 */
	public int getDataType() {
		return dataType;
	}
	
	/**
	 * Asigna el tipo de dato. Los tipos de dato posibles están definidos en IRaster.
	 * @param dataType Tipo de dato del buffer
	 */
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
	public long sizeof() {
			return getDataSize() * width * height * nBands;
	}
	
	/**
	 * Replica la banda de una posición sobre otra. Si la banda de destino no existe
	 * se crea nueva. Si la posición de la banda de destino está intercalada entre bandas 
	 * que ya existen las otras se desplazan hacia abajo, NO se machacan los datos de ninguna.
	 * Los datos se replican por referencia por lo que al modificar la banda original las
	 * del resto quedarán afectadas.
	 * @param orig. Posición de la banda de origen. 
	 * @param dest. Posición de la banda destino
	 */   
	public abstract void replicateBand(int orig, int dest);
	
	/**
	 * Cambia bandas de posición. Las posiciones deben existir como bandas del raster. 
	 * Cada elemento del array representa una banda existente en el buffer (de longitud
	 * rasterBuf.length) y el valor contenido dentro la banda que le corresponde. Por ejemplo
	 * si pasamos un array {1, 0, 3, 2} significa que el buffer tiene cuatro bandas y que 
	 * cambiamos la 0 por la 1 y la 2 por la 3. Un array {0, 1, 2, 3} en el mismo 
	 * caso no produciría nigún cambio.
	 * 
	 * Si quisieramos asignar en un buffer monobanda su banda a la segunda posición habria
	 * que insertar una vacia, por ejemplo con addBandFloat(0, null) se insertaria una 
	 * banda nula en la posición 0 y la banda que estaba en la 0 pasaría a la segunda.
	 * 
	 */
	public abstract void switchBands(int[] bandPosition);
		
	/**
	 * Convierte un tipo de dato a cadena
	 * @param type Tipo de dato
	 * @return cadena  que representa el tipo de dato
	 */
	public static String typesToString(int type) {
			switch (type) {
			case RasterBuffer.TYPE_IMAGE:
					return new String("Image");

			case RasterBuffer.TYPE_BYTE:
					return new String("Byte");

			case RasterBuffer.TYPE_DOUBLE:
					return new String("Double");

			case RasterBuffer.TYPE_FLOAT:
					return new String("Float");

			case RasterBuffer.TYPE_INT:
				return new String("Integer");
				
			case RasterBuffer.TYPE_USHORT:
			case RasterBuffer.TYPE_SHORT:
					return new String("Short");
			}

			return null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.IBuffer#isInside(int, int)
	 */
	public boolean isInside(int x, int y) {
		if (x < 0 || y < 0 || x >= getWidth() || y >= getHeight())
			return false;
		return true;
	}
			
	/*
	 *  (non-Javadoc)
	 * @see org.gvsig.fmap.driver.IBuffer#getNoDataValue()
	 */
	public double getNoDataValue() {
		return noDataValue;
	}
	
	/*
	 *  (non-Javadoc)
	 * @see org.gvsig.fmap.driver.IBuffer#getByteNoDataValue()
	 */
	public byte getByteNoDataValue() {
		return (byte)noDataValue;
	}
	
	/*
	 *  (non-Javadoc)
	 * @see org.gvsig.fmap.driver.IBuffer#getShortNoDataValue()
	 */
	public short getShortNoDataValue(){
		return (short)noDataValue;
	}
	
	/*
	 *  (non-Javadoc)
	 * @see org.gvsig.fmap.driver.IBuffer#getIntNoDataValue()
	 */
	public int getIntNoDataValue(){
		return (int)noDataValue;
	}
	
	/*
	 *  (non-Javadoc)
	 * @see org.gvsig.fmap.driver.IBuffer#getFloatNoDataValue()
	 */
	public float getFloatNoDataValue(){
		return (float)noDataValue;
	}
	
	/*
	 *  (non-Javadoc)
	 * @see org.gvsig.fmap.driver.IBuffer#setNoDataValue(double)
	 */
	public void setNoDataValue(double nd){
		noDataValue = nd;
	}
	
	/*
	 *  (non-Javadoc)
	 * @see org.gvsig.fmap.driver.IBuffer#getNotValidValue()
	 */
	public double getNotValidValue(){
		return notValidValue;
	}
	
	/*
	 *  (non-Javadoc)
	 * @see org.gvsig.fmap.driver.IBuffer#setNotValidValue(java.lang.Object)
	 */
	public void setNotValidValue(double value){
		this.notValidValue = value;
	}
	
	/*
	 *  (non-Javadoc)
	 * @see org.gvsig.fmap.driver.IBuffer#cloneBuffer()
	 */
	public abstract IBuffer cloneBuffer();

	/**
	 * Ajusta el área del grid a un ancho y un alto dado en pixeles. Este ajuste se hará 
	 * en relación a un método de interpolación definido en el parámetro.
	 * @param w Ancho de la nueva imagen.
	 * @param h Alto de la nueva imagen.
	 * @param interpolation Método de interpolación que se usará en el ajuste.
	 */
	public RasterBuffer getAdjustedWindow(int w, int h, int interpolationMethod) throws InterruptedException {
		if (interp == null)
			interp = new BufferInterpolation(this);

		if (w == getWidth() && h == getHeight())
			return this;
		RasterBuffer rasterBuf = null;
		switch (interpolationMethod) {
			case BufferInterpolation.INTERPOLATION_NearestNeighbour:
				rasterBuf = interp.adjustRasterNearestNeighbourInterpolation(w, h);
				break;
			case BufferInterpolation.INTERPOLATION_Bilinear:
				rasterBuf = interp.adjustRasterBilinearInterpolation(w, h);
				break;
			case BufferInterpolation.INTERPOLATION_InverseDistance:
				rasterBuf = interp.adjustRasterInverseDistanceInterpolation(w, h);
				break;
			case BufferInterpolation.INTERPOLATION_BicubicSpline:
				rasterBuf = interp.adjustRasterBicubicSplineInterpolation(w, h);
				break;
			case BufferInterpolation.INTERPOLATION_BSpline:
				rasterBuf = interp.adjustRasterBSplineInterpolation(w, h);
				break;
		}
		if (rasterBuf != null)
			return rasterBuf;
		else
			return this;
	}
	
	public BufferInterpolation getLastInterpolation() {
		return interp;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.IBuffer#getLimits()
	 */
	public double[] getLimits() throws InterruptedException {
		RasterTask task = RasterTaskQueue.get(Thread.currentThread().toString());
		double max = Double.NEGATIVE_INFINITY;
		double secondMax = max;
		double min = Double.MAX_VALUE;
		double secondMin = min;
		double value = 0;

		switch (getDataType()) {
			case IBuffer.TYPE_BYTE:
				for (int i = 0; i < getBandCount(); i++)
					for (int r = 0; r < getHeight(); r++) {
						for (int c = 0; c < getWidth(); c++) {
							value = (double) ((getElemByte(r, c, i)));
							if (value > max) {
								if (max != value) secondMax = max;
								max = value;
							}
							if (value < min) {
								if (min != value) secondMin = min;
								min = value;
							}
						}
						if (task.getEvent() != null)
							task.manageEvent(task.getEvent());
					}
				break;
			case IBuffer.TYPE_SHORT:
				for (int i = 0; i < getBandCount(); i++)
					for (int r = 0; r < getHeight(); r++) {
						for (int c = 0; c < getWidth(); c++) {
							value = (double) getElemShort(r, c, i);
							if (value > max) {
								if (max != value) secondMax = max;
								max = value;
							}
							if (value < min) {
								if (min != value) secondMin = min;
								min = value;
							}
						}
						if (task.getEvent() != null)
							task.manageEvent(task.getEvent());
					}
				break;
			case IBuffer.TYPE_INT:
				for (int i = 0; i < getBandCount(); i++)
					for (int r = 0; r < getHeight(); r++) {
						for (int c = 0; c < getWidth(); c++) {
							value = (double) getElemInt(r, c, i);
							if (value > max) {
								if (max != value) secondMax = max;
								max = value;
							}
							if (value < min) {
								if (min != value) secondMin = min;
								min = value;
							}
						}
						if (task.getEvent() != null)
							task.manageEvent(task.getEvent());
					}
				break;
			case IBuffer.TYPE_FLOAT:
				for (int i = 0; i < getBandCount(); i++)
					for (int r = 0; r < getHeight(); r++) {
						for (int c = 0; c < getWidth(); c++) {
							value =  (double) getElemFloat(r, c, i);
							if (value > max) {
								if (max != value) secondMax = max;
								max = value;
							}
							if (value < min) {
								if (min != value) secondMin = min;
								min = value;
							}
						}
						if (task.getEvent() != null)
							task.manageEvent(task.getEvent());
					}
				break;
			case IBuffer.TYPE_DOUBLE:
				for (int i = 0; i < getBandCount(); i++)
					for (int r = 0; r < getHeight(); r++) {
						for (int c = 0; c < getWidth(); c++) {
							value = getElemDouble(r, c, i);
							if (value > max) {
								if (max != value) secondMax = max;
								max = value;
							}
							if (value < min) {
								if (min != value) secondMin = min;
								min = value;
							}
						}
						if (task.getEvent() != null)
							task.manageEvent(task.getEvent());
					}
				break;
		}
		// Si no existe un secondMax lo igualo al maximo existente
		if (secondMax == Double.NEGATIVE_INFINITY)
			secondMax = max;
		// Si no existe un secondMin lo igualo al minimo existente
		if (secondMin == Double.MAX_VALUE)
			secondMin = min;
		
		double[] values = new double[4];
		values[0] = min;
		values[1] = max;
		values[2] = secondMin;
		values[3] = secondMax;
		return values;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.IBuffer#getLimits()
	 */
	private double[][] getAllBandsLimits() throws InterruptedException {
		RasterTask task = RasterTaskQueue.get(Thread.currentThread().toString());
		double max[] = new double[getBandCount()];
		double min[] = new double[getBandCount()];
		double value = 0;

		for (int i = 0; i < getBandCount(); i++) {
			max[i] = Double.NEGATIVE_INFINITY;
			min[i] = Double.MAX_VALUE;
		}
		
		switch (getDataType()) {
			case IBuffer.TYPE_BYTE:
				for (int i = 0; i < getBandCount(); i++) {
					for (int r = 0; r < getHeight(); r++) {
						for (int c = 0; c < getWidth(); c++) {
							value = (double) ((getElemByte(r, c, i)));
							if (value > max[i])
								max[i] = value;
							if (value < min[i])
								min[i] = value;
						}
						if (task.getEvent() != null)
							task.manageEvent(task.getEvent());
					}
				}
				break;
			case IBuffer.TYPE_SHORT:
				for (int i = 0; i < getBandCount(); i++) {
					for (int r = 0; r < getHeight(); r++) {
						for (int c = 0; c < getWidth(); c++) {
							value = (double) getElemShort(r, c, i);
							if (value > max[i])
								max[i] = value;
							if (value < min[i])
								min[i] = value;
						}
						if (task.getEvent() != null)
							task.manageEvent(task.getEvent());
					}
				}
				break;
			case IBuffer.TYPE_INT:
				for (int i = 0; i < getBandCount(); i++) {
					for (int r = 0; r < getHeight(); r++) {
						for (int c = 0; c < getWidth(); c++) {
							value = (double) getElemInt(r, c, i);
							if (value > max[i])
								max[i] = value;
							if (value < min[i])
								min[i] = value;
						}
						if (task.getEvent() != null)
							task.manageEvent(task.getEvent());
					}
				}
				break;
			case IBuffer.TYPE_FLOAT:
				for (int i = 0; i < getBandCount(); i++) {
					for (int r = 0; r < getHeight(); r++) {
						for (int c = 0; c < getWidth(); c++) {
							value = (double) getElemFloat(r, c, i);
							if (value > max[i])
								max[i] = value;
							if (value < min[i])
								min[i] = value;
						}
						if (task.getEvent() != null)
							task.manageEvent(task.getEvent());
					}
				}
				break;
			case IBuffer.TYPE_DOUBLE:
				for (int i = 0; i < getBandCount(); i++) {
					for (int r = 0; r < getHeight(); r++) {
						for (int c = 0; c < getWidth(); c++) {
							value = getElemDouble(r, c, i);
							if (value > max[i])
								max[i] = value;
							if (value < min[i])
								min[i] = value;
						}
						if (task.getEvent() != null)
							task.manageEvent(task.getEvent());
					}
				}
				break;
		}
		double[][] values = new double[2][getBandCount()];

		for (int i = 0; i < getBandCount(); i++) {
			values[0][i] = min[i];
			values[1][i] = max[i];
		}
		return values;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.driver.datasetproperties.IHistogramable#getHistogram()
	 */
	public Histogram getHistogram() throws InterruptedException {
		RasterTask task = RasterTaskQueue.get(Thread.currentThread().toString());
		progressHistogram = 0;
		Histogram hist = null;
		double[][] limits = getAllBandsLimits();

		hist = new Histogram(getBandCount(), limits[0], limits[1], getDataType());
				
		for (int iBand = 0; iBand < getBandCount(); iBand++) {
			for (int row = 0; row < getHeight(); row++) {
				switch(getDataType()) {
				case IBuffer.TYPE_BYTE:
					for (int col = 0; col < getWidth(); col++) 
						hist.incrementPxValue(iBand, (double)(getElemByte(row, col, iBand)));
					break;
				case IBuffer.TYPE_SHORT:
					for (int col = 0; col < getWidth(); col++) 
						hist.incrementPxValue(iBand, (double)(getElemShort(row, col, iBand)));
					break;
				case IBuffer.TYPE_INT:
					for (int col = 0; col < getWidth(); col++) 
						hist.incrementPxValue(iBand, (double)(getElemInt(row, col, iBand)));
					break;
				case IBuffer.TYPE_FLOAT:
					for (int col = 0; col < getWidth(); col++) 
						hist.incrementPxValue(iBand, (double)getElemFloat(row, col, iBand));
					break;
				case IBuffer.TYPE_DOUBLE:
					for (int col = 0; col < getWidth(); col++) 
						hist.incrementPxValue(iBand, getElemDouble(row, col, iBand));
					break;
				}
				
				if (task.getEvent() != null)
					task.manageEvent(task.getEvent());

				progressHistogram = ((iBand*getHeight() + row) * 100) /(getHeight() * getBandCount());
			}
		}
		progressHistogram = 100;
		return hist;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.util.IHistogramable#resetPercent()
	 */
	public void resetPercent() {
		switch(process) {
		case HISTOGRAM_PROCESS: progressHistogram = 0;
		case INTERPOLATION_PROCESS: progressInterpolation = 0;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.util.IHistogramable#getPercent()
	 */
	public int getPercent() {
		switch(process) {
		case HISTOGRAM_PROCESS: return progressHistogram;
		case INTERPOLATION_PROCESS: return progressInterpolation;
		}
		return 0;
	}
	
	/**
	 * Asigna el proceso del cual se desea obtener información. Los procesos
	 * disponibles se definen como constantes en esta clase.
	 * @param process
	 */
	public void setProcess(int process) {
		this.process = process;
	}
}
