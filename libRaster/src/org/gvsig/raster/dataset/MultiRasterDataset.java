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
package org.gvsig.raster.dataset;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.io.File;
import java.util.ArrayList;

import org.cresques.cts.IProjection;
import org.gvsig.raster.RasterLibrary;
import org.gvsig.raster.buffer.RasterBuffer;
import org.gvsig.raster.buffer.cache.RasterReadOnlyBuffer;
import org.gvsig.raster.dataset.properties.DatasetColorInterpretation;
import org.gvsig.raster.dataset.properties.DatasetListHistogram;
import org.gvsig.raster.dataset.properties.DatasetListStatistics;
import org.gvsig.raster.dataset.serializer.RmfSerializerException;
import org.gvsig.raster.datastruct.ColorTable;
import org.gvsig.raster.datastruct.Extent;
import org.gvsig.raster.datastruct.Histogram;
import org.gvsig.raster.datastruct.HistogramException;
import org.gvsig.raster.datastruct.Transparency;

/**
 * Clase que representa una imagen de raster georreferenciada formada por varias
 * imagenes de disco que tienen la misma extensión. Contiene funcionalidades
 * para abrir ficheros, gestionar el extent, pintar el raster sobre un DataImage
 * con su gestión de bandas correspondiente.
 * 
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class MultiRasterDataset implements IRasterDataSource {
	// File list
	private ArrayList               files         = new ArrayList();

	// Band list
	private BandList                bandList      = new BandList();
	protected DatasetListStatistics stats         = null;
	protected DatasetListHistogram  histogram     = null;
	/**
	 * Flag que fuerza al buffer de solo lectura
	 */
	private boolean                 readOnly      = false;
	/**
	 * Flag que fuerza al buffer en memoria
	 */
	private boolean                 forceToMemory = false;
	int                             percent       = 0;
			
	//TODO: FUNCIONALIDAD: Contructores igual a RasterDataset + String[] nameFiles
	public MultiRasterDataset() {
	}
	
	/**
	 * Crea un objeto MultiRasterDataset nuevo con los mismos ficheros
	 * que el actual.
	 * @return MultiRasterDataset
	 */
	public IRasterDataSource newDataset() {
		try {
			String[] fileList = getNameDatasetStringList(0, 0);
			MultiRasterDataset multiRasterDataset = MultiRasterDataset.open(getDataset(0)[0].getProjection(), fileList[0]);
			for (int j = 1; j < fileList.length; j++)
				multiRasterDataset.addDataset(new String[] { fileList[j] });
			return multiRasterDataset;
		} catch (FileNotFoundInListException e) {
			return null;
		} catch (NotSupportedExtensionException e) {
			return null;
		} catch (RasterDriverException e) {
			return null;
		}
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
	public static MultiRasterDataset open(IProjection proj, Object datasetOpenParam) throws NotSupportedExtensionException, RasterDriverException{
		MultiRasterDataset rmd = new MultiRasterDataset();
		if (datasetOpenParam instanceof String[]) {
			String[] param = (String[]) datasetOpenParam;
			for (int dataset = 0; dataset < param.length; dataset++)
				try {
					rmd.addDataset(new RasterDataset[] { RasterDataset.open(proj, param[dataset]) });
				} catch (FileNotFoundInListException e) {
					// No lo añadimos en el dataset pq ya existe
				}
		} else
			if (datasetOpenParam instanceof IBuffer[]) {
				IBuffer[] param = (IBuffer[]) datasetOpenParam;
				for (int dataset = 0; dataset < param.length; dataset++)
					try {
						rmd.addDataset(new RasterDataset[] { RasterDataset.open(proj, param[dataset]) });
					} catch (FileNotFoundInListException e) {
						// No lo añadimos en el dataset pq ya existe
					}
			} else {
				RasterDataset rd = RasterDataset.open(proj, datasetOpenParam);
				try {
					rmd.addDataset(new RasterDataset[] { rd });
				} catch (FileNotFoundInListException e) {
					// No lo añadimos en el dataset pq ya existe
				}
			}
		return rmd;
	}
	
	/**
	 * Añade un fichero a la lista que componen el multi raster. El array solo debe tener un
	 * elemento ya que solo tiene sentido para este caso con un elemento. ¡Ojo!, añadir más elementos
	 * al array no hace que se añadan varios datasets a la lista.
	 * @param f Añade el elemento 0 a la lista.
	 */
	public void addDataset(RasterDataset[] f)throws FileNotFoundInListException {
		if(f.length != 1)
			throw new FileNotFoundInListException("Error in list.");
		if(findDataset(f[0]))
			throw new FileNotFoundInListException("The file already is in list.");
		files.add(f[0]);
		addBands(f[0]);
		if(stats == null)
			stats = new DatasetListStatistics(files);
		else
			stats.addDataset(f[0]);
	}
	
	/**
	 * Añade un fichero a la lista que componen el multi raster a partir de su nombre. El array solo debe tener un
	 * elemento ya que solo tiene sentido para este caso con un elemento. ¡Ojo!, añadir más elementos
	 * al array no hace que se añadan varios datasets a la lista.
	 * @param f Añade el elemento 0 a la lista.
	 */
	public void addDataset(String[] fileName)throws FileNotFoundInListException, NotSupportedExtensionException, RasterDriverException {
		if(fileName.length != 1)
			throw new FileNotFoundInListException("Error in list.");
		if(findDataset(fileName[0]))
			throw new FileNotFoundInListException("The file already is in list.");
		RasterDataset f = RasterDataset.open(null, fileName[0]);
		files.add(f);
		addBands(f);
		if(stats == null)
			stats = new DatasetListStatistics(files);
		else
			stats.addDataset(f);
	}
	
	/**
	 * Añade el fichero a lista de georrasterfiles y sus bandas a la lista de bandas
	 * @param grf
	 */
	private void addBands(RasterDataset grf) {
		if(grf == null)
			return;
		
		for(int i = 0; i < grf.getBandCount();i++) {
			try {
				int dataType = grf.getDataType()[i];
				Band band = new Band(grf.getFName(), i, dataType);
				bandList.addBand(band, i);
			} catch(BandNotFoundInListException ex) {
				//No añadimos la banda
			}
		}
	}
	
	/**
	 * Elimina un fichero a la lista a partir de su nombre
	 * @param fileName	Nombre del fichero a eliminar.
	 */
	public void removeDataset(String fileName) {
		for(int i=0;i<files.size();i++) {
			if(((RasterDataset)files.get(i)).getFName().equals(fileName)) {
				files.remove(i);
				bandList.removeBands(fileName);
				return;
			}
		}
	}
	
	/**
	 * Elimina un fichero a la lista
	 * @param file Fichero a eliminar
	 */
	public void removeDataset(RasterDataset file) {
		for(int i=0;i<files.size();i++) {
			if(((RasterDataset)files.get(i)).getFName().equals(file.getFName())) {
				files.remove(i);
				bandList.removeBands(file.getFName());
				return;
			}
		}
	}
		
	/**
	 * Obtiene el número de ficheros en la lista
	 * @return integer.
	 */
	public int getDatasetCount() {
		return files.size();
	}
	
	/**
	 * Encuentra un fichero en la lista.
	 * @param file Fichero búscado.
	 * @return true si se ha hallado el fichero y false si no se 
	 * ha encontrado
	 */
	public boolean findDataset(RasterDataset file) {
		for(int i = 0;i < files.size(); i++) {
			RasterDataset grf = (RasterDataset)files.get(i); 
			if(	grf.getFName().equals(file.getFName()))
				return true;
		}
		return false;
	}
	
	/**
	 * Encuentra un fichero en la lista.
	 * @param file Fichero búscado.
	 * @return true si se ha hallado el fichero y false si no se 
	 * ha encontrado
	 */
	public boolean findDataset(String fileName) {
		for(int i = 0;i<files.size();i++) {
			if(((RasterDataset)files.get(i)).getFName().equals(fileName))
				return true;
		}
		return false;
	}
					
	/**
	 * Cierra la lista de datasets asociados al MultiRasterDataset
	 */
	public void close() {
		for(int i = 0; i < files.size(); i++)
			((RasterDataset)files.get(i)).close();
		files.clear();
		bandList.clear();
	}
	
	/**
	 * Obtiene en un array de String la lista de nombres de ficheros. 
	 * @param i Para un MultiRasterDataset el parámetro es ignorado
	 * @param j Para un MultiRasterDataset el parámetro es ignorado
	 * @return lista de nombres de los ficheros del GeoRasterMultiFile
	 */
	public String[] getNameDatasetStringList(int i, int j) {
		String[] list = new String[files.size()];
		for(int k = 0; k < files.size(); k++)
			list[k] = ((RasterDataset)files.get(k)).getFName();
		return list;
	}
	
	/**
	 * Inicializa el buffer a valores NoData
	 * @param raster Buffer a inicializar
	 * @param bandList Lista de bandas
	 */
	private void initBufferToNoData(IBuffer raster, BandList bandList) {
		for(int i = 0; i < bandList.getDrawableBandsCount(); i++) {
			switch(getDataType()[0]) {
			case IBuffer.TYPE_BYTE:raster.assign(i, raster.getByteNoDataValue());break;
			case IBuffer.TYPE_SHORT:raster.assign(i, raster.getShortNoDataValue());break;
			case IBuffer.TYPE_INT:raster.assign(i, raster.getIntNoDataValue());break;
			case IBuffer.TYPE_FLOAT:raster.assign(i, raster.getFloatNoDataValue());break;
			case IBuffer.TYPE_DOUBLE:raster.assign(i, raster.getNoDataValue());break;
			}
		}	
	}
	
	/**
	 * A partir de la lista de bandas que dice como cargar el buffer se crean tantos IBuffer como ficheros intervienen
	 * . Cada IBuffer corresponde a un dataset del RasterMultiDataset y en ellos se reserva memoria solo para las
	 * bandas que vayan a ser cargadas. Las otras se asignaran a la banda NotValid.
	 * @param bl Lista de bandas
	 * @param width Ancho
	 * @param height Alto
	 * @return Lista de buffers en el que cada uno corresponde a un dataset.
	 */
	private IBuffer[] mallocBuffersDatasets(BandList bl, int width, int height) {
		IBuffer[] buffers = new IBuffer[getDatasetCount()];
		for(int i = 0; i < getDatasetCount(); i++) {
			if(forceToMemory)
				buffers[i] =  RasterBuffer.getMemoryBuffer(getDataset(i)[0].getDataType()[0], width, height, getDataset(i)[0].getBandCount(), false);
			else
				buffers[i] =  RasterBuffer.getBuffer(getDataset(i)[0].getDataType()[0], width, height, getDataset(i)[0].getBandCount(), false);
			
			//Asignamos las bandas de cada fichero que no se pintan a null y las que se pintan se reserva memoria
			String name = getDataset(i)[0].getFName();
			for(int j = 0; j < getDataset(i)[0].getBandCount(); j ++) {
				if(bl.getBufferBandToDraw(name, j) == null)
					buffers[i].assignBandToNotValid(j);
				else
					buffers[i].mallocOneBand(getDataset(i)[0].getDataType()[0], width, height, j);
			}
		}
		return buffers;
	}
	
	/**
	 * Mezcla los buffers de los dataset que forman el RasterMultiDataset sobre un solo buffer
	 * con las directrices que marca la lista de bandas. Esta función es la que realiza el switch 
	 * de las bandas.
	 * @param b Buffer sobre el que se mezcla
	 * @param bDataset Buffers que corresponden a los datasets
	 * @param bandList Objeto que contiene la información de que bandas de los dataset se escriben sobre
	 * que banda del buffer.
	 */
	private void mergeBuffers(IBuffer b, IBuffer[] bDataset, BandList bandList) {
		for(int iDataset = 0; iDataset < getDatasetCount(); iDataset++){ //Ojo! Los datasets están en la misma posición que se han metido en mallocBuffersDatasets
			String name = getDataset(iDataset)[0].getFName();
						
			for(int iBand = 0; iBand < getDataset(iDataset)[0].getBandCount(); iBand ++) {
				int[] posToDraw = bandList.getBufferBandToDraw(name, iBand);
				if(posToDraw != null) {
					for(int i = 0; i < posToDraw.length; i ++) {
						switch(getDataType()[iDataset]) {
						case IBuffer.TYPE_BYTE: b.assignBand(posToDraw[i], bDataset[iDataset].getBand(iBand)); break;
						case IBuffer.TYPE_SHORT: b.assignBand(posToDraw[i], bDataset[iDataset].getBand(iBand)); break;
						case IBuffer.TYPE_INT: b.assignBand(posToDraw[i], bDataset[iDataset].getBand(iBand)); break;
						case IBuffer.TYPE_FLOAT: b.assignBand(posToDraw[i], bDataset[iDataset].getBand(iBand)); break;
						case IBuffer.TYPE_DOUBLE: b.assignBand(posToDraw[i], bDataset[iDataset].getBand(iBand)); break;
						}
					}
				}
			}
		}
	}

	/**
	 * Obtiene una ventana de datos de la imagen a partir de coordenadas reales. 
	 * No aplica supersampleo ni subsampleo sino que devuelve una matriz de igual tamaño a los
	 * pixeles de disco. 
	 * @param x Posición X superior izquierda
	 * @param y Posición Y superior izquierda
	 * @param w Ancho en coordenadas reales
	 * @param h Alto en coordenadas reales
	 * @param adjustToExtent Flag que dice si el extent solicitado debe ajustarse al extent del raster o no.
	 * @param bandList
	 * @return Buffer de datos
	 */
	public IBuffer getWindowRaster(double ulx, double uly, double lrx, double lry) 
		throws InvalidSetViewException, InterruptedException, RasterDriverException {		
		//Extent selectedExtent = new Extent(ulx, uly, lrx, lry);

		//Leemos pixels completos aunque el valor obtenido sea decimal. Esto se consigue redondeando
		//por arriba el más alto y por abajo el menor y luego restandolos
		
		Point2D p1 = ((RasterDataset)files.get(0)).worldToRaster(new Point2D.Double(ulx, uly));
		Point2D p2 = ((RasterDataset)files.get(0)).worldToRaster(new Point2D.Double(lrx, lry));

		//Para el valor mayor redondeamos por arriba. Para el valor menor redondeamos por abajo.
		double p1X = (p1.getX() > p2.getX()) ? Math.ceil(p1.getX()) : Math.floor(p1.getX());
		double p1Y = (p1.getY() > p2.getY()) ? Math.ceil(p1.getY()) : Math.floor(p1.getY());
		double p2X = (p2.getX() > p1.getX()) ? Math.ceil(p2.getX()) : Math.floor(p2.getX());
		double p2Y = (p2.getY() > p1.getY()) ? Math.ceil(p2.getY()) : Math.floor(p2.getY());
		
		int width = (int)Math.abs(p1X - p2X); 
		int height = (int)Math.abs(p1Y - p2Y);
		
		//Ajustamos por si nos hemos salido del raster
		if(((int)(Math.min(p1X, p2X) + width)) > getWidth())
			width = (int)(getWidth() - Math.min(p1X, p2X));
		if(((int)(Math.min(p1Y, p2Y) + height)) > getHeight())
			height = (int)(getHeight() - Math.min(p1Y, p2Y));
		
		if (p1X < 0)
			p1X = 0;
		if (p1Y < 0)
			p1Y = 0;
		if (p2X > getWidth())
			p2X = getWidth();
		if (p2Y > getHeight())
			p2Y = getHeight();

		int mallocNBands = 0;
		if(bandList.getDrawableBands() != null)
			mallocNBands = bandList.getDrawableBands().length;
		else
			mallocNBands = bandList.getDrawableBandsCount();
		
		//Buffer ReadOnly
		
		if(isReadOnly()) {
			RasterBuffer rb = RasterBuffer.getReadOnlyBuffer(getDataType()[0], width, height, getBandCount());
			if(rb instanceof RasterReadOnlyBuffer) {
				try {
					((RasterReadOnlyBuffer)rb).setBufferParams(this, (int)p1.getX(), (int)p1.getY(), (int)p2.getX(), (int)p2.getY(), bandList);
				} catch (FileNotExistsException e) {
					//Esto no debe darse ya que se comprueba al hacer el open.
					return null;
				} catch (NotSupportedExtensionException e) {
					//Esto no debe darse ya que se comprueba al hacer el open
					return null;
				}
				return rb;
			}
		}
		
		//Buffer RW
		
		IBuffer raster = null;
		if(forceToMemory) //Fuerza siempre buffer en memoria
			raster = RasterBuffer.getMemoryBuffer(getDataType()[0], width, height, mallocNBands, false);
		else
			raster = RasterBuffer.getBuffer(getDataType()[0], width, height, mallocNBands, false);
							
		for(int iBand = 0; iBand < raster.getBandCount(); iBand ++)
			raster.assignBandToNotValid(iBand);
		
		//Reservamos memoria para los buffers por dataset
		IBuffer[] bufferDatasets = mallocBuffersDatasets(bandList, width, height);
		
		//Si hemos redondeado los pixeles de la petición (p1 y p2) por arriba y por abajo deberemos calcular un extent mayor 
		//equivalente a los pixeles redondeados.
		Point2D wc1 = ((RasterDataset)files.get(0)).rasterToWorld(new Point2D.Double(p1X, p1Y));
		Point2D wc2 = ((RasterDataset)files.get(0)).rasterToWorld(new Point2D.Double(p2X, p2Y));
		for(int i = 0; i < getDatasetCount(); i++)
			bufferDatasets[i] = ((RasterDataset)files.get(i)).getWindowRaster(wc1.getX(), wc1.getY(), wc2.getX(), wc2.getY(), bandList, bufferDatasets[i]);
		
		//Mezclamos los buffers de cada dataset en un solo buffer
		mergeBuffers(raster, bufferDatasets, bandList);
							
		return raster;
	}
	
	/**
	 * Obtiene una ventana de datos de la imagen a partir de coordenadas reales. 
	 * No aplica supersampleo ni subsampleo sino que devuelve una matriz de igual tamaño a los
	 * pixeles de disco. 
	 * @param x Posición X superior izquierda
	 * @param y Posición Y superior izquierda
	 * @param w Ancho en coordenadas reales
	 * @param h Alto en coordenadas reales
	 * @param adjustToExtent Flag que dice si el extent solicitado debe ajustarse al extent del raster o no.
	 * @param bandList
	 * @return Buffer de datos
	 */
	public IBuffer getWindowRaster(double ulx, double uly, double w, double h, boolean adjustToExtent) 
		throws InvalidSetViewException, InterruptedException, RasterDriverException {
		//El incremento o decremento de las X e Y depende de los signos de rotación y escala en la matriz de transformación. Por esto
		//tenemos que averiguar si lrx es x + w o x -w, asi como si lry es y + h o y - h 
		Extent ext = getExtent();
		Point2D pInit = ((RasterDataset)files.get(0)).rasterToWorld(new Point2D.Double(0, 0));
		Point2D pEnd = ((RasterDataset)files.get(0)).rasterToWorld(new Point2D.Double((int)getWidth(), (int)getHeight()));
		double wRaster = Math.abs(pEnd.getX() - pInit.getX());
		double hRaster = Math.abs(pEnd.getY() - pInit.getY());
		double lrx = (((int)(ext.getULX() - wRaster)) == ((int)ext.getLRX())) ? (ulx - w) : (ulx + w);
		double lry = (((int)(ext.getULY() - hRaster)) == ((int)ext.getLRY())) ? (uly - h) : (uly + h); 
		
		//Extent selectedExtent = new Extent(ulx, uly, lrx, lry);

		//Leemos pixels completos aunque el valor obtenido sea decimal. Esto se consigue redondeando
		//por arriba el más alto y por abajo el menor y luego restandolos
		
		Point2D p1 = ((RasterDataset)files.get(0)).worldToRaster(new Point2D.Double(ulx, uly));
		Point2D p2 = ((RasterDataset)files.get(0)).worldToRaster(new Point2D.Double(lrx, lry));
		int width = (int)Math.abs(Math.ceil(p2.getX()) - Math.floor(p1.getX())); 
		int height = (int)Math.abs(Math.floor(p1.getY()) - Math.ceil(p2.getY()));
		
		//Ajustamos por si nos hemos salido del raster
		if(((int)(p1.getX() + width)) > getWidth())
			width = (int)(getWidth() - p1.getX());
		if(((int)(p1.getY() + height)) > getHeight())
			height = (int)(getHeight() - p1.getY());
					
		if (p1.getX() < 0)
			p1.setLocation(0, p1.getY());
		if (p1.getY() < 0)
			p1.setLocation(p1.getX(), 0);
		if (p2.getX() > getWidth())
			p2.setLocation(getWidth(), p2.getY());
		if (p2.getY() > getHeight())
			p2.setLocation(p2.getX(), getHeight());
		
		int mallocNBands = 0;
		if(bandList.getDrawableBands() != null)
			mallocNBands = bandList.getDrawableBands().length;
		else
			mallocNBands = bandList.getDrawableBandsCount();
		
		//Buffer ReadOnly
		
		if(isReadOnly()) {
			RasterBuffer rb = RasterBuffer.getReadOnlyBuffer(getDataType()[0], width, height, getBandCount());
			if(rb instanceof RasterReadOnlyBuffer) {
				try {
					((RasterReadOnlyBuffer)rb).setBufferParams(this, (int)p1.getX(), (int)p1.getY(), (int)p2.getX(), (int)p2.getY(), bandList);
				} catch (FileNotExistsException e) {
					//Esto no debe darse ya que se comprueba al hacer el open.
					return null;
				} catch (NotSupportedExtensionException e) {
					//Esto no debe darse ya que se comprueba al hacer el open
					return null;
				}
				return rb;
			}
		}
		
		//Buffer RW
		
		IBuffer raster = null;
		if(forceToMemory) //Fuerza siempre buffer en memoria
			raster = RasterBuffer.getBuffer(getDataType()[0], width, height, mallocNBands, false);
		else
			raster = RasterBuffer.getMemoryBuffer(getDataType()[0], width, height, mallocNBands, false);
							
		for(int iBand = 0; iBand < raster.getBandCount(); iBand ++)
			raster.assignBandToNotValid(iBand);
		
		//Si no vamos a ajustar el extent al raster inicializamos el buffer a noData ya que este puede ser
		//más grande y salirse de los límites.
		if(!adjustToExtent)
			 initBufferToNoData(raster, bandList);
		
		//Reservamos memoria para los buffers por dataset
		IBuffer[] bufferDatasets = mallocBuffersDatasets(bandList, width, height);
		
		//Si hemos redondeado los pixeles de la petición (p1 y p2) por arriba y por abajo deberemos calcular un extent mayor 
		//equivalente a los pixeles redondeados.
		Point2D wc1 = ((RasterDataset)files.get(0)).rasterToWorld(new Point2D.Double(Math.floor(p1.getX()), Math.floor(p1.getY())));
		Point2D wc2 = ((RasterDataset)files.get(0)).rasterToWorld(new Point2D.Double(Math.ceil(p2.getX()), Math.ceil(p2.getY())));
		for(int i = 0; i < getDatasetCount(); i++)
			bufferDatasets[i] = ((RasterDataset)files.get(i)).getWindowRaster(wc1.getX(), wc1.getY(), Math.abs(wc2.getX() - wc1.getX()), Math.abs(wc2.getY() - wc1.getY()), bandList, bufferDatasets[i], adjustToExtent);
			//bufferDatasets[i] = ((RasterDataset)files.get(i)).getWindowRaster(x, y, w, h, bandList, bufferDatasets[i], adjustToExtent);
		
		//Mezclamos los buffers de cada dataset en un solo buffer
		mergeBuffers(raster, bufferDatasets, bandList);
							
		return raster;
	}
		
	/**
	 * Obtiene una ventana de datos de la imagen a partir de coordenadas reales. 
	 * Aplica supersampleo o subsampleo en función del tamaño del buffer. Esta operación la gestiona
	 * el driver.
	 * @param minX Valor mínimo de la X en coordenadas reales
	 * @param minY Valor mínimo de la Y en coordenadas reales
	 * @param maxX Valor máximo de la X en coordenadas reales
	 * @param maxY Valor máximo de la Y en coordenadas reales
	 * @param bufWidth ancho del buffer lde datos
	 * @param bufHeight alto del buffer de datos
	 * @param adjustToExtent Flag que dice si el extent solicitado debe ajustarse al extent del raster o no.
	 * @param bandList
	 * @return Buffer de datos
	 */
	public IBuffer getWindowRaster(double ulx, double uly, double lrx, double lry, int bufWidth, int bufHeight, boolean adjustToExtent) 
		throws InvalidSetViewException, InterruptedException, RasterDriverException {
		
		Point2D p1 = worldToRaster(new Point2D.Double(ulx, uly));
		Point2D p2 = worldToRaster(new Point2D.Double(lrx, lry));
		if(	((int)p1.getX()) < 0 || ((int)p2.getX()) > getWidth() ||
				((int)p2.getY()) > getHeight() || ((int)p2.getY()) < 0)
				throw new InvalidSetViewException("");
		
		int mallocNBands = 0;
		if(bandList.getDrawableBands() != null)
			mallocNBands = bandList.getDrawableBands().length;
		else
			mallocNBands = bandList.getDrawableBandsCount();
		
		//Buffer ReadOnly
		
		if(isReadOnly()) {
			RasterBuffer rb = RasterBuffer.getReadOnlyBuffer(getDataType()[0], bufWidth, bufHeight, getBandCount());
			if(rb instanceof RasterReadOnlyBuffer) {
				try {
					((RasterReadOnlyBuffer)rb).setBufferParams(this, (int)p1.getX(), (int)p1.getY(), (int)p2.getX(), (int)p2.getY(), bandList);
				} catch (FileNotExistsException e) {
					//Esto no debe darse ya que se comprueba al hacer el open.
					return null;
				} catch (NotSupportedExtensionException e) {
					//Esto no debe darse ya que se comprueba al hacer el open
					return null;
				}
				return rb;
			}
		}
		
		//Buffer RW
		
		IBuffer raster = null;
		if(forceToMemory) //Fuerza siempre buffer en memoria
			raster = RasterBuffer.getMemoryBuffer(getDataType()[0], bufWidth, bufHeight, mallocNBands, false);
		else
			raster = RasterBuffer.getBuffer(getDataType()[0], bufWidth, bufHeight, mallocNBands, false);
					
		for(int iBand = 0; iBand < raster.getBandCount(); iBand ++)
			raster.assignBandToNotValid(iBand);
		
		//Si no vamos a ajustar el extent al raster inicializamos el buffer a noData ya que este puede ser
		//más grande y salirse de los límites.
		if(!adjustToExtent)
			 initBufferToNoData(raster, bandList);	
		
		//Reservamos memoria para los buffers por dataset
		IBuffer[] bufferDatasets = mallocBuffersDatasets(bandList, bufWidth, bufHeight);
		for(int i = 0; i < getDatasetCount(); i++)
			bufferDatasets[i] = ((RasterDataset)files.get(i)).getWindowRaster(ulx, uly, lrx, lry, bufWidth, bufHeight, bandList, bufferDatasets[i], adjustToExtent);
		
		//Mezclamos los buffers de cada dataset en un solo buffer
		mergeBuffers(raster, bufferDatasets, bandList);
							
		return raster;
	}
	
	/**
	 * Obtiene una ventana de datos de la imagen a partir de coordenadas reales. 
	 * No aplica supersampleo ni subsampleo sino que devuelve una matriz de igual tamaño a los
	 * pixeles de disco. 
	 * @param x Posición X superior izquierda
	 * @param y Posición Y superior izquierda
	 * @param w Ancho en coordenadas pixel
	 * @param h Alto en coordenadas pixel
	 * @param bandList
	 * @return Buffer de datos
	 */
	public IBuffer getWindowRaster(int x, int y, int w, int h) 
		throws InvalidSetViewException, InterruptedException, RasterDriverException {
		if(x < 0 || y < 0 || w > ((RasterDataset)files.get(0)).getWidth() || h > ((RasterDataset)files.get(0)).getHeight())
			throw new InvalidSetViewException("Out of image");
				
		//Buffer ReadOnly
		
		if(isReadOnly()) {
			RasterBuffer rb = RasterBuffer.getReadOnlyBuffer(getDataType()[0], w, h, getBandCount());
			if(rb instanceof RasterReadOnlyBuffer) {
				try {
					((RasterReadOnlyBuffer)rb).setBufferParams(this, x, y, x + w, y + h, bandList);
				} catch (FileNotExistsException e) {
					//Esto no debe darse ya que se comprueba al hacer el open.
					return null;
				} catch (NotSupportedExtensionException e) {
					//Esto no debe darse ya que se comprueba al hacer el open
					return null;
				}
				return rb;
			}
		}
		
		//Buffer RW
		
		IBuffer raster = null;
		if(forceToMemory) //Fuerza siempre buffer en memoria
			raster = RasterBuffer.getMemoryBuffer(getDataType()[0], w, h, bandList.getDrawableBandsCount(), false);
		else
			raster = RasterBuffer.getBuffer(getDataType()[0], w, h, bandList.getDrawableBandsCount(), false);
		
		for(int iBand = 0; iBand < raster.getBandCount(); iBand ++)
			raster.assignBandToNotValid(iBand);
		
		//Reservamos memoria para los buffers por dataset
		IBuffer[] bufferDatasets = mallocBuffersDatasets(bandList, w, h);
		for(int i = 0; i < getDatasetCount(); i++)
			bufferDatasets[i] = ((RasterDataset)files.get(i)).getWindowRaster(x, y, w, h, bandList, bufferDatasets[i]);
		
		//Mezclamos los buffers de cada dataset en un solo buffer
		mergeBuffers(raster, bufferDatasets, bandList);
							
		return raster;
	}
	
	/**
	 * Obtiene una ventana de datos de la imagen a partir de coordenadas reales. 
	 * Aplica supersampleo o subsampleo en función del tamaño del buffer
	 * @param x Posición X superior izquierda en pixels
	 * @param y Posición Y superior izquierda en pixels
	 * @param w Ancho en pixels
	 * @param h Alto en pixels
	 * @param bufWidth ancho del buffer de datos
	 * @param bufHeight alto del buffer de datos
	 * @param bandList
	 * @return Buffer de datos
	 */
	public IBuffer getWindowRaster(int x, int y, int w, int h, int bufWidth, int bufHeight)  
		throws InvalidSetViewException, InterruptedException, RasterDriverException {
		if(x < 0 || y < 0 || w > ((RasterDataset)files.get(0)).getWidth() || h > ((RasterDataset)files.get(0)).getHeight())
			throw new InvalidSetViewException("Out of image");
		
		//Buffer ReadOnly
		
		if(isReadOnly()) {
			RasterBuffer rb = RasterBuffer.getReadOnlyBuffer(getDataType()[0], bufWidth, bufHeight, getBandCount());
			if(rb instanceof RasterReadOnlyBuffer) {
				try {
					((RasterReadOnlyBuffer)rb).setBufferParams(this, x, y, x + w, y + h, bandList);
				} catch (FileNotExistsException e) {
					//Esto no debe darse ya que se comprueba al hacer el open.
					return null;
				} catch (NotSupportedExtensionException e) {
					//Esto no debe darse ya que se comprueba al hacer el open
					return null;
				}
				return rb;
			}
		}
		
		IBuffer raster = null;
		if(forceToMemory) //Fuerza siempre buffer en memoria
			raster = RasterBuffer.getMemoryBuffer(getDataType()[0], bufWidth, bufHeight, bandList.getDrawableBandsCount(), false);
		else
			raster = RasterBuffer.getBuffer(getDataType()[0], bufWidth, bufHeight, bandList.getDrawableBandsCount(), false);
			
		for(int iBand = 0; iBand < raster.getBandCount(); iBand ++)
			raster.assignBandToNotValid(iBand);
			
		//Reservamos memoria para los buffers por dataset
		IBuffer[] bufferDatasets = mallocBuffersDatasets(bandList, bufWidth, bufHeight);
					
		for(int i = 0; i < getDatasetCount(); i++)
			bufferDatasets[i] = ((RasterDataset)files.get(i)).getWindowRaster(x, y, w, h, bufWidth, bufHeight, bandList, bufferDatasets[i]);

		//Mezclamos los buffers de cada dataset en un solo buffer
		mergeBuffers(raster, bufferDatasets, bandList);
							
		return raster;
	}
	
	//******************************
	//Setters and Getters
	//******************************
	
	/**
	 * Calcula el tamaño de los ficheros en disco
	 * @return tamaño en bytes de todos los ficheros de la lista
	 */
	public long getFileSize() {
		int len = 0;
		for(int i = 0; i < files.size();i++) {
			if(((RasterDataset)files.get(i)) != null) {
				File f = new File(((RasterDataset)files.get(i)).getFName());
				len += f.length();
			}
		}
		return len;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.IRasterDataSource#getHeight()
	 */
	public double getHeight() {
		double[] lenghts = new double[getDatasetCount()];
		for(int i = 0; i < getDatasetCount(); i++)
			if(((RasterDataset)files.get(i)) != null)
				lenghts[i] = ((RasterDataset)files.get(i)).getHeight();
		return lenghts[0];
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.IRasterDataSource#getWidth()
	 */
	public double getWidth() {
		double[] lenghts = new double[getDatasetCount()];
		for(int i = 0; i < getDatasetCount(); i++)
			if(((RasterDataset)files.get(i)) != null)
				lenghts[i] = ((RasterDataset)files.get(i)).getWidth();
		return lenghts[0];	
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
	 * @see org.gvsig.raster.dataset.IRasterDataSource#getBandCount()
	 */
	public int getBandCount() {
		return bandList.getBandCount();
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.IRasterDataSource#getDataType()
	 */
	public int[] getDataType() {
		int[] dt = new int[getBandCount()];

		if (dt.length == 0)
			return null;

		int k = 0;
		for(int i = 0; i < files.size(); i++) {
			int[] types = ((RasterDataset)files.get(i)).getDataType();
			for (int j = 0; j < types.length; j++) {
				dt[k] = types[j];
				k ++;
			}
		}
				
		return dt;
	}
	
	/**
	 * Obtiene fichero de la posición i. En un MultiRasterDataset el array devuelto será de 
	 * un solo elemento por lo que solo tendrá sentido la posición 0.
	 * @param i Posición del fichero a obtener.
	 * @return GeoRasterFileDataset.
	 */
	public RasterDataset[] getDataset(int i) {
		return new RasterDataset[]{(RasterDataset)files.get(i)};
	}
	
	/**
	 * Obtiene fichero de nombre fileName.
	 * @param i Posición del fichero a obtener.
	 * @return GeoRasterFile.
	 */
	public RasterDataset getDataset(String fileName) {
		for(int i=0;i<files.size();i++){
			if(((RasterDataset)files.get(i)).getFName().equals(fileName))
				return (RasterDataset)files.get(i); 
		}
		return null;		
	}
		
	/**
	 * Obtiene la lista de bandas
	 * @return BandList
	 */
	public BandList getBands() {
		return bandList;
	}
	
	/**
	 * Obtiene la coordenada X mínima de toda la lista
	 * @return Coordenada X mínima
	 */
	public double getMinX() {
		double minx = Double.MAX_VALUE;
		for(int i = 0; i < files.size(); i++) {
			double aux = ((RasterDataset)files.get(i)).getExtent().getMin().getX();
			if(aux < minx)
				minx = aux;
		}
		return minx;
	}
	
	/**
	 * Obtiene la coordenada Y mínima de toda la lista
	 * @return Coordenada Y mínima
	 */
	public double getMinY() {
		double miny = Double.MAX_VALUE;
		for(int i = 0; i < files.size(); i++) {
			double aux = ((RasterDataset)files.get(i)).getExtent().getMin().getY();
			if(aux < miny)
				miny = aux;
		}
		return miny;
	}
	
	/**
	 * Obtiene la coordenada Y máxima de toda la lista
	 * @return Coordenada Y máxima
	 */
	public double getMaxX() {
		double maxx = Double.NEGATIVE_INFINITY;
		for(int i = 0; i < files.size(); i++) {
			double aux = ((RasterDataset)files.get(i)).getExtent().getMin().getY();
			if(aux > maxx)
				maxx = aux;
		}
		return maxx;
	}

	/**
	 * Obtiene la coordenada Y máxima de toda la lista
	 * @return Coordenada Y máxima
	 */
	public double getMaxY() {
		double maxy = Double.NEGATIVE_INFINITY;
		for(int i = 0; i < files.size(); i++) {
			double aux = ((RasterDataset)files.get(i)).getExtent().getMin().getY();
			if(aux > maxy)
				maxy = aux;
		}
		return maxy;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.IRasterDataSource#getNoDataValue()
	 */
	public double getNoDataValue() {
		if (files.isEmpty())
			return RasterLibrary.defaultNoDataValue;

		return ((RasterDataset) files.get(0)).getNoDataValue();
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.IRasterDataSource#isNoDataEnabled()
	 */
	public boolean isNoDataEnabled() {
		if (files.isEmpty())
			return false;

		return ((RasterDataset) files.get(0)).isNoDataEnabled();
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.IRasterDataSource#resetNoDataValue()
	 */
	public void resetNoDataValue() {
		for (int i = 0; i < files.size(); i++)
			((RasterDataset) files.get(i)).resetNoDataValue();
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.IRasterDataSource#setNoDataValue(double)
	 */
	public void setNoDataValue(double value) {
		for (int i = 0; i < files.size(); i++)
			((RasterDataset) files.get(i)).setNoDataValue(value);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.IRasterDataSource#setNoDataEnabled(boolean)
	 */
	public void setNoDataEnabled(boolean enabled) {
		for (int i = 0; i < files.size(); i++)
			((RasterDataset) files.get(i)).setNoDataEnabled(enabled);
	}

	/**
	 * Obtiene el extent del multi fichero. Este corresponde al primer
	 * GeoRasterFile de la lista.
	 * @return Extent
	 */
	public Extent getExtent() {
		if(files.size() == 0)
			return null;
		else
			return ((RasterDataset)files.get(0)).getExtent();
	}
	
	/**
		 * Este es el extent sobre el que se ajusta una petición para que esta no exceda el 
		 * extent máximo del raster. Para un raster sin rotar será igual al extent
		 * pero para un raster rotado será igual al extent del raster como si no 
		 * tuviera rotación. Esto ha de ser así ya que la rotación solo se hace sobre la
		 * vista y las peticiones han de hacerse en coordenadas de la imagen sin shearing
		 * aplicado.
		 * @return Extent
		 */
		public Extent getExtentForRequest() {
				return ((RasterDataset)files.get(0)).getExtentWithoutRot();
		}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.IRasterDataSource#getLastSelectedView()
	 */
	public Extent getLastSelectedView(){
		return ((RasterDataset)files.get(0)).getView();
	}
		
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.IRasterDataSource#getTransparencyFilesStatus()
	 */
	public Transparency getTransparencyFilesStatus() {
		if(files.size() <= 0)
			return null;
		Transparency t = ((RasterDataset)files.get(0)).getTransparencyDatasetStatus();
		for(int i = 1; i < files.size(); i++) {
			Transparency t1 = ((RasterDataset)files.get(i)).getTransparencyDatasetStatus();
			t.merge(t1);
		}
		return t;
	}
	
	/**
	 * Obtiene la paleta correspondiente a uno de los ficheros que forman el GeoMultiRasterFile
	 * @param i Posición del raster
	 * @return Paleta asociada a este o null si no tiene
	 */
	public ColorTable getColorTable(int i){
		if(i >= files.size())
			return null;
		return ((RasterDataset)files.get(i)).getColorTable();
	}
	
	/**
	 * Obtiene la lista de paletas correspondiente a todos los ficheros que forman el GeoMultiRasterFile
	 * @return Paleta asociada a este o null si no tiene. Una posición null en el array también indica que
	 * para ese fichero no hay paletas asociadas.
	 */
	public ColorTable[] getColorTables(){
		if(files.size() <= 0)
			return null;
		ColorTable[] list = new ColorTable[files.size()];
		for(int i = 0; i < files.size(); i++)
			list[i] = ((RasterDataset)files.get(i)).getColorTable();
		return list;
	}
	
	/**
	 * Obtiene la paleta correspondiente al nombre del fichero pasado por parámetro. 
	 * @param fileName Nombre del fichero
	 * @return Paleta o null si no la tiene
	 */
	public ColorTable getColorTable(String fileName){
		for(int i = 0; i < files.size(); i++){
			if(((RasterDataset)files.get(i)).getFName().indexOf(fileName) == 0)
				return ((RasterDataset)files.get(i)).getColorTable();
		}
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.IRasterDataSource#rasterToWorld(java.awt.geom.Point2D)
	 */
	public Point2D rasterToWorld(Point2D pt) {
		return ((RasterDataset)files.get(0)).rasterToWorld(pt);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.IRasterDataSource#worldToRaster(java.awt.geom.Point2D)
	 */
	public Point2D worldToRaster(Point2D pt) {
		return ((RasterDataset)files.get(0)).worldToRaster(pt);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.IRasterDataSource#calcSteps(double, double, double, double, double, double, int, int)
	 */
	public double[] calcSteps(double dWorldTLX, double dWorldTLY, double dWorldBRX, double dWorldBRY,
			double nWidth, double nHeight, int bufWidth, int bufHeight){
		return ((RasterDataset)files.get(0)).calcSteps(dWorldTLX, dWorldTLY, dWorldBRX, dWorldBRY, nWidth, nHeight, bufWidth, bufHeight);
	}
	
	/**
	 * Obtiene el objeto con las estadisticas
	 * @return MultiFileStatistics
	 */
	public DatasetListStatistics getStatistics(){
		return stats;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.IRasterDataSource#isGeoreferenced()
	 */
	public boolean isGeoreferenced() {
		for(int i = 0; i < files.size(); i++){
			if(((RasterDataset)files.get(i)).isGeoreferenced())
				return true;
		}
		return false;
	}
	
	/**
	 * Obtiene el tamaño de pixel en X
	 * @return tamaño de pixel en X
	 */
	public double getPixelSizeX() {
		return ((RasterDataset)files.get(0)).getPixelSizeX();
	}
	
	/**
	 * Obtiene el tamaño de pixel en Y
	 * @return tamaño de pixel en Y
	 */
	public double getPixelSizeY() {
		return ((RasterDataset)files.get(0)).getPixelSizeY();
	}

	//TODO: TEST: Probar getData para multifichero

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.IRasterDataSource#getData(int, int, int)
	 */
	public Object getData(int x, int y, int band)throws InvalidSetViewException, FileNotOpenException, RasterDriverException{
		String file = bandList.getBand(band).getFileName(); 
		int[] posList = bandList.getBandPositionList();
		for(int i = 0; i < files.size(); i++){
			if(((RasterDataset)files.get(i)).getFName().equals(file))
				return ((RasterDataset)files.get(i)).getData(x, y, posList[band]); 
		}
		return null;
	}
		
	/**
	 * Obtiene el objeto que contiene la interpretación de color del MultiRasterDataset
	 * @return DatasetColorInterpretation
	 */
	public DatasetColorInterpretation getColorInterpretation(){
		DatasetColorInterpretation ci = new DatasetColorInterpretation();
		for (int i = 0; i < files.size(); i++) {
			ci.addColorInterpretation(((RasterDataset) files.get(i)).getColorInterpretation());
		}
		return ci;
	}
	
	/**
	 * Obtiene la proyección asociada al dataset. Como todos los dataset del 
	 * multiDataset deben tener la misma proyección obtenemos esta del primer
	 * dataset.
	 * @return Proyección en formato cadena
	 * @throws RasterDriverException
	 */
	public String getWktProjection() throws RasterDriverException {
		return ((RasterDataset)files.get(0)).getWktProjection();
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.driver.datasetproperties.IHistogramable#getHistogram()
	 */
	public Histogram getHistogram() throws HistogramException, InterruptedException {
		if (histogram == null)
			histogram = new DatasetListHistogram(this);
		
		try {
			Histogram tmpHist = histogram.getHistogram();
			return tmpHist;
		} catch (FileNotOpenException e) {
			throw new HistogramException("FileNotOpenException");
		} catch (RasterDriverException e) {
			throw new HistogramException("RasterDriverException");
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.util.IHistogramable#getPercent()
	 */
	public int getPercent() {
		if (histogram != null) 
			return histogram.getPercent();
		return 0;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.util.IHistogramable#resetPercent()
	 */
	public void resetPercent() {
		if (histogram != null) histogram.resetPercent();
	}
	
	/**
	 * Metodo que obtiene si un punto cae dentro de los límites de la capa 
	 * o fuera de ellos.
	 * @param p Punto a calcular
	 * @return true si está dentro de los límites y false si está fuera
	 */
	public boolean isInside(Point2D p) {
		if(getDataset(0) != null)
			return getDataset(0)[0].isInside(p);
		return false;
	}
	
	/**
	 * Devuelve la transformación leida en la carga del raster 
	 * @return AffineTransform
	 */
	public AffineTransform getOwnAffineTransform() {
		if(getDataset(0) != null)
			return getDataset(0)[0].ownTransformation;
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.IRasterDataSource#getAffineTransform()
	 */
	public AffineTransform getAffineTransform(int band){
		if(band < 0 || band > (getBandCount() - 1))
			return null;
		int[] datasetBand = getDatasetFromBandNumber(band);
		if(datasetBand != null && getDataset(datasetBand[0]) != null)
			return getDataset(datasetBand[0])[0].getAffineTransform();
		return null;
	}
	
	/**
	 * Obtiene el número de dataset para un número de banda y la posición de la banda dentro de 
	 * ese dataset.
	 * @param bandNumber Número de banda
	 * @return Dataset que corresponde a la banda pedida y número de banda dentro del dataset (dataset, banda)
	 */
	private int[] getDatasetFromBandNumber(int bandNumber) {
		int cont = 0;
		for (int i = 0; i < getDatasetCount(); i++) {
			cont += getDataset(i)[0].getBandCount();
			if(cont > bandNumber) {
				int lastBands = (cont - getDataset(i)[0].getBandCount()); //Suma de las bandas de todos los datasets anteriores.
				return new int[]{i, (bandNumber - lastBands)};
			}
		}
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.IRasterDataSource#setAffineTransform(java.awt.geom.AffineTransform)
	 */
	public void setAffineTransform(AffineTransform transf){
		for (int i = 0; i < getDatasetCount(); i++) 
			this.getDataset(i)[0].setAffineTransform(transf);	
	}
	
	/**
	 * Obtiene la matriz de transformación del propio raster. Esta matriz es la encargada
	 * de convertir las coordenadas de la petición en coordenadas a las que se pide a la libreria.
	 * En gdal, por ejemplo, se piden las coordenadas a la libreria en coordenadas pixel por lo que
	 * esta matriz tendrá la georreferenciación asociada en el worldfile o cabecera. Otras librerias como
	 * ermapper la petición a la libreria se hace en coordenadas geograficas que son las mismas en las
	 * que pide el usuario de gvSIG por lo que esta matriz en este caso se inicializa con la identidad. 
	 * @return
	 */
	public AffineTransform getOwnTransformation() {
		if(getDataset(0) != null)
			return getDataset(0)[0].getOwnTransformation();
		return new AffineTransform();
	} 

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.IRasterDataSource#isRotated()
	 */
	public boolean isRotated() {
		if(getDataset(0) != null)
			return getDataset(0)[0].isRotated();
		return false;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.IRasterDataSource#setDrawableBands(int[])
	 */
	public void setDrawableBands(int[] db) {
		getBands().setDrawableBands(db);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.IRasterDataSource#clearDrawableBands()
	 */
	public void clearDrawableBands() {
		getBands().clearDrawableBands();
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.IRasterDataSource#addDrawableBand(int, int)
	 */
	public void addDrawableBand(int posRasterBuf, int imageBand) {
		getBands().addDrawableBand(posRasterBuf, imageBand);
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
		if(readOnly)
			this.forceToMemory = false;
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
	 * @see org.gvsig.raster.dataset.IRasterDataSource#isMemoryBuffer()
	 */
	public boolean isMemoryBuffer() {
		return forceToMemory;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.RasterDataset#getOverviewCount(int)
	 */
	public int getOverviewCount(int band) throws BandAccessException, RasterDriverException {
		if(band >= getBandCount())
			throw new BandAccessException("Wrong band");
		String fileName = getBands().getBand(band).getFileName();
		RasterDataset dataset = getDataset(fileName);
		return dataset.getOverviewCount(0);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.IRasterDataSource#overviewsSupport()
	 */
	public boolean overviewsSupport() {
		return getDataset(0)[0].overviewsSupport();
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.IRasterDataSource#saveObjectToRMF(int, java.lang.Class, java.lang.Object)
	 */
	public void saveObjectToRmf(int file, Class class1, Object value) throws RmfSerializerException {
		((RasterDataset) files.get(file)).saveObjectToRmf(class1, value);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.IRasterDataSource#loadObjectFromRMF(java.lang.Class, java.lang.Object)
	 */
	public Object loadObjectFromRmf(Class class1, Object value) throws RmfSerializerException {
		return ((RasterDataset) files.get(0)).loadObjectFromRmf(class1, value);
	}
}
