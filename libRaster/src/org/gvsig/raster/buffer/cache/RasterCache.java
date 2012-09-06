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

package org.gvsig.raster.buffer.cache;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.gvsig.raster.buffer.IBand;
import org.gvsig.raster.buffer.RasterBand;
import org.gvsig.raster.buffer.RasterBuffer;
import org.gvsig.raster.dataset.IBuffer;
import org.gvsig.raster.util.RasterUtilities;

/*
 * TODO: OPTIMIZACION: Trabajo de optimización en la velocidad e acceso a caché.
 * TODO: FUNCIONALIDAD: Acabar de implementar los métodos de acceso a datos, intercambio de bandas, etc...
 */
/**
 * Esta clase representa un buffer de datos cacheado. El estar en cache significa que
 * solo una parte de los datos estarán en memoria y que gestiona, dependiendo de las
 * peticiones, que partes hay que cargar a memoria y en que momento. Para esto el buffer
 * no se llena de golpe sino que utiliza una clase que sirve los datos desde la fuente. 
 * Esta clase servidora de datos debe implementar el interfaz ICacheDatasetSourcepara 
 * servir datos de la forma requerida.
 *   
 * @author Nacho Brodin (nachobrodin@gmail.com)
 *
 */
public class RasterCache extends RasterBuffer {

	private Cache 			cache = null;
	private LRUAlgorithm  	lru = null;
		
	//TODO: FUNCIONALIDAD: Intercambio de bandas para el buffer cacheado
	
	 public class CacheBand extends RasterBand{
		 public ICacheDataSource[] cacheDataServer = null;
		 
		 public CacheBand(int height, int width){
			 super(height, width);
		 }
		 
		 public Object getLine(int line) {
			 return null;
		 }
		 
		 public void setLine(int line, Object value){
			
		 }
		 
		 public Object getBuf(){
			 return null;
		 }
		 
		 public void setFileName(int numBand){
			for (int i = 0; i < cacheDataServer.length; i++) 
				((CacheDataServer)cacheDataServer[i]).setName(null, numBand, i);
		 }
	}
	 
    /**
     * Constructor. Asigna las variables de inicialización y crea la estructura de 
     * la caché con los datos pasados.
     * @param dataType Tipo de dato del buffer
     * @param width Ancho del buffer
     * @param height Alto del buffer
     * @param nBands Número de bandas del buffer
     */
	public RasterCache(int dataType, int width, int height, int nBands){
		cache = new Cache(nBands, dataType, width, height);
		lru = new LRUAlgorithm(cache);
		
    	this.dataType = dataType;
        this.width = width;
        this.height = height;
        this.nBands = nBands;
	}
	
	/**
	 * Limpia los trozos de caché en disco. Después del llamar a este 
	 * método no puede volver a usarse esta caché.
	 * @throws IOException 
	 */
	public void clearCache() throws IOException {
		cache.clearCache(this.nBands);
	}
	
	/**
	 * Borra la caché cuando se elimina el objeto RasterCache ya que
	 * los trozos ya no pueden ser referenciados.
	 */
	protected void finalize() throws Throwable {
		free();
	}
	
	/*
     * (non-Javadoc)
     * @see org.gvsig.raster.driver.IBuffer#isBandSwitchable()
     */
    public boolean isBandSwitchable(){
    	return true;
    }
    
	public void malloc(int dataType, int width, int height, int bandNr) {
	}

	/**
	 * Obtiene la caché
	 * @return
	 */
	public Cache getCache() {
		return cache;
	}
	
	/**
	 * Asigna la cache
	 * @param cache
	 */
	public void setCache(Cache cache) {
		this.cache = cache;
		this.lru.setCache(cache);
	}
	
	//*********************************************************
	
	public byte[][] getLineByte(int line) {
		try {
			lru.cacheAccess(line, true);
		} catch (InvalidPageNumberException e) {return null;}
		return cache.getAccessPage().getLineByte((line & cache.getOffset()));
	}

	public short[][] getLineShort(int line) {
		try {
			lru.cacheAccess(line, true);
		} catch (InvalidPageNumberException e) {return null;}
		return cache.getAccessPage().getLineShort((line & cache.getOffset()));
	}

	public int[][] getLineInt(int line) {
		try {
			lru.cacheAccess(line, true);
		} catch (InvalidPageNumberException e) {return null;}
		return cache.getAccessPage().getLineInt((line & cache.getOffset()));
	}

	public float[][] getLineFloat(int line) {
		try {
			lru.cacheAccess(line, true);
		} catch (InvalidPageNumberException e) {return null;}
		return cache.getAccessPage().getLineFloat((line & cache.getOffset()));
	}

	public double[][] getLineDouble(int line) {
		try {
			lru.cacheAccess(line, true);
		} catch (InvalidPageNumberException e) {return null;}
		return cache.getAccessPage().getLineDouble((line & cache.getOffset()));
	}

	//*********************************************************
	
	public void setLineByte(byte[][] data, int line) {
		try {
			lru.cacheAccess(line, false);
		} catch (InvalidPageNumberException e) {return;}
		cache.getAccessPage().setLineByte(data, (line & cache.getOffset()));
	}

	public void setLineShort(short[][] data, int line) {
		try {
			lru.cacheAccess(line, false);
		} catch (InvalidPageNumberException e) {return;}
		cache.getAccessPage().setLineShort(data, (line & cache.getOffset()));
	}

	public void setLineInt(int[][] data, int line) {
		try {
			lru.cacheAccess(line, false);
		} catch (InvalidPageNumberException e) {return;}
		cache.getAccessPage().setLineInt(data, (line & cache.getOffset()));
	}

	public void setLineFloat(float[][] data, int line) {
		try {
			lru.cacheAccess(line, false);
		} catch (InvalidPageNumberException e) {return;}
		cache.getAccessPage().setLineFloat(data, (line & cache.getOffset()));
	}

	public void setLineDouble(double[][] data, int line) {
		try {
			lru.cacheAccess(line, false);
		} catch (InvalidPageNumberException e) {return;}
		cache.getAccessPage().setLineDouble(data, (line & cache.getOffset()));
	}

	//*********************************************************
	
	public byte[] getLineFromBandByte(int line, int band) {
		try {
			lru.cacheAccess(line, true);
		} catch (InvalidPageNumberException e) {return null;}
		return cache.getAccessPage().getLineFromBandByte((line & cache.getOffset()), band);
	}

	public short[] getLineFromBandShort(int line, int band) {
		try {
			lru.cacheAccess(line, true);
		} catch (InvalidPageNumberException e) {return null;}
		return cache.getAccessPage().getLineFromBandShort((line & cache.getOffset()), band);
	}

	public int[] getLineFromBandInt(int line, int band) {
		try {
			lru.cacheAccess(line, true);
		} catch (InvalidPageNumberException e) {return null;}
		return cache.getAccessPage().getLineFromBandInt((line & cache.getOffset()), band);
	}

	public float[] getLineFromBandFloat(int line, int band) {
		try {
			lru.cacheAccess(line, true);
		} catch (InvalidPageNumberException e) {return null;}
		return cache.getAccessPage().getLineFromBandFloat((line & cache.getOffset()), band);
	}

	public double[] getLineFromBandDouble(int line, int band) {
		try {
			lru.cacheAccess(line, true);
		} catch (InvalidPageNumberException e) {return null;}
		return cache.getAccessPage().getLineFromBandDouble((line & cache.getOffset()), band);
	}

	//*********************************************************
	
	public void setLineInBandByte(byte[] data, int line, int band) {
		try {
			lru.cacheAccess(line, false);
		} catch (InvalidPageNumberException e) {return;}
		cache.getAccessPage().setLineInBandByte(data, (line & cache.getOffset()), band);
	}

	public void setLineInBandShort(short[] data, int line, int band) {
		try {
			lru.cacheAccess(line, false);
		} catch (InvalidPageNumberException e) {return;}
		cache.getAccessPage().setLineInBandShort(data, (line & cache.getOffset()), band);
	}

	public void setLineInBandInt(int[] data, int line, int band) {
		try {
			lru.cacheAccess(line, false);
		} catch (InvalidPageNumberException e) {return;}
		cache.getAccessPage().setLineInBandInt(data, (line & cache.getOffset()), band);
	}

	public void setLineInBandFloat(float[] data, int line, int band) {
		try {
			lru.cacheAccess(line, false);
		} catch (InvalidPageNumberException e) {return;}
		cache.getAccessPage().setLineInBandFloat(data, (line & cache.getOffset()), band);
	}

	public void setLineInBandDouble(double[] data, int line, int band) {
		try {
			lru.cacheAccess(line, false);
		} catch (InvalidPageNumberException e) {return;}
		cache.getAccessPage().setLineInBandDouble(data, (line & cache.getOffset()), band);
	}

	//*********************************************************
	
	public byte getElemByte(int line, int col, int band) {
		try {
			lru.cacheAccess(line, true);
		} catch (InvalidPageNumberException e) {
			return (byte)getNoDataValue(); //No leemos el dato
		}
		return cache.getAccessPage().getElemByte((line & cache.getOffset()), col, band);
	}

	public short getElemShort(int line, int col, int band) {
		try {
			lru.cacheAccess(line, true);
		} catch (InvalidPageNumberException e) {
			return (short)getNoDataValue(); //No leemos el dato
		}
		return cache.getAccessPage().getElemShort((line & cache.getOffset()), col, band);
	}

	public int getElemInt(int line, int col, int band) {
		try {
			lru.cacheAccess(line, true);
		} catch (InvalidPageNumberException e) {
			return (int)getNoDataValue(); //No leemos el dato
		}
		return cache.getAccessPage().getElemInt((line & cache.getOffset()), col, band);
	}

	public float getElemFloat(int line, int col, int band) {
		try {
			lru.cacheAccess(line, true);
		} catch (InvalidPageNumberException e) {
			return (float)getNoDataValue(); //No leemos el dato
		}
		return cache.getAccessPage().getElemFloat((line & cache.getOffset()), col, band);
	}

	public double getElemDouble(int line, int col, int band) {
		try {
			lru.cacheAccess(line, true);
		} catch (InvalidPageNumberException e) {
			return (double)getNoDataValue(); //No leemos el dato
		}
		return cache.getAccessPage().getElemDouble((line & cache.getOffset()), col, band);
	}

	//*********************************************************
	
	public void setElem(int line, int col, int band, byte data) {
		try {
			lru.cacheAccess(line, false);
		} catch (InvalidPageNumberException e) { return;}
		cache.getAccessPage().setElem((line & cache.getOffset()), col, band, data);
	}

	public void setElem(int line, int col, int band, short data) {
		try {
			lru.cacheAccess(line, false);
		} catch (InvalidPageNumberException e) {return;}
		cache.getAccessPage().setElem((line & cache.getOffset()), col, band, data);
	}

	public void setElem(int line, int col, int band, int data) {
		try {
			lru.cacheAccess(line, false);
		} catch (InvalidPageNumberException e) {return;}
		cache.getAccessPage().setElem((line & cache.getOffset()), col, band, data);
	}

	public void setElem(int line, int col, int band, float data) {
		try {
			lru.cacheAccess(line, false);
		} catch (InvalidPageNumberException e) {return;}
		cache.getAccessPage().setElem((line & cache.getOffset()), col, band, data);
	}

	public void setElem(int line, int col, int band, double data) {
		try {
			lru.cacheAccess(line, false);
		} catch (InvalidPageNumberException e) {return;}
		cache.getAccessPage().setElem((line & cache.getOffset()), col, band, data);
	}
	
	//*********************************************************

	public void getElemByte(int line, int col, byte[] data) {
		try {
			lru.cacheAccess(line, true);
		} catch (InvalidPageNumberException e) {
			for (int iBand = 0; iBand < data.length; iBand++)
	            data[iBand] = (byte)getNoDataValue();
		}
		cache.getAccessPage().getElemByte((line & cache.getOffset()), col, data);
	}

	public void getElemShort(int line, int col, short[] data) {
		try {
			lru.cacheAccess(line, true);
		} catch (InvalidPageNumberException e) {
			for (int iBand = 0; iBand < data.length; iBand++)
	            data[iBand] = (short)getNoDataValue();
		}
		cache.getAccessPage().getElemShort((line & cache.getOffset()), col, data);
	}

	public void getElemInt(int line, int col, int[] data) {
		try {
			lru.cacheAccess(line, true);
		} catch (InvalidPageNumberException e) {
			for (int iBand = 0; iBand < data.length; iBand++)
	            data[iBand] = (int)getNoDataValue();
		}
		cache.getAccessPage().getElemInt((line & cache.getOffset()), col, data);
	}

	public void getElemFloat(int line, int col, float[] data) {
		try {
			lru.cacheAccess(line, true);
		} catch (InvalidPageNumberException e) {
			for (int iBand = 0; iBand < data.length; iBand++)
	            data[iBand] = (float)getNoDataValue();
		}
		cache.getAccessPage().getElemFloat((line & cache.getOffset()), col, data);
	}

	public void getElemDouble(int line, int col, double[] data) {
		try {
			lru.cacheAccess(line, true);
		} catch (InvalidPageNumberException e) {
			for (int iBand = 0; iBand < data.length; iBand++)
	            data[iBand] = (double)getNoDataValue();
		}
		cache.getAccessPage().getElemDouble((line & cache.getOffset()), col, data);
	}
	
	//*********************************************************
	
	public void setElemByte(int line, int col, byte[] data) {
		try {
			lru.cacheAccess(line, false);
		} catch (InvalidPageNumberException e) {return;}
		cache.getAccessPage().setElemByte((line & cache.getOffset()), col, data);
	}

	public void setElemShort(int line, int col, short[] data) {
		try {
			lru.cacheAccess(line, false);
		} catch (InvalidPageNumberException e) {return;}
		cache.getAccessPage().setElemShort((line & cache.getOffset()), col, data);
	}

	public void setElemInt(int line, int col, int[] data) {
		try {
			lru.cacheAccess(line, false);
		} catch (InvalidPageNumberException e) {return;}
		cache.getAccessPage().setElemInt((line & cache.getOffset()), col, data);
	}

	public void setElemFloat(int line, int col, float[] data) {
		try {
			lru.cacheAccess(line, false);
		} catch (InvalidPageNumberException e) {return;}
		cache.getAccessPage().setElemFloat((line & cache.getOffset()), col, data);
	}

	public void setElemDouble(int line, int col, double[] data) {
		try {
			lru.cacheAccess(line, false);
		} catch (InvalidPageNumberException e) {return;}
		cache.getAccessPage().setElemDouble((line & cache.getOffset()), col, data);
	}

    /*
     *  (non-Javadoc)
     * @see org.gvsig.fmap.driver.IBuffer#getBandBuffer(int)
     */
    public IBuffer getBandBuffer(int iBand){
    	RasterCache rasterCache = new RasterCache(getDataType(), getWidth(), getHeight(), 1);
    	CacheStruct cs = new CacheStruct();
    	cs.setHPag(cache.getCacheStruct().getHPag());
    	cs.setOffset(cache.getCacheStruct().getOffset());
    	cs.setNPags(cache.getCacheStruct().getNPags());
    	cs.setNBands(1);
    	cs.setNGroups(cache.getCacheStruct().getNGroups());
    	cs.setBitsPag(cache.getCacheStruct().getBitsPag());
    	cs.setNTotalPags((int)(getHeight() / cache.getCacheStruct().getHPag()));
    	cs.setDataType(cache.getCacheStruct().getDataType());
    	
    	Cache c = new Cache(cs, getWidth());
    	rasterCache.setCache(c);
    	
    	IBand band = getBand(iBand);
    	rasterCache.assignBand(0, band);
    	return rasterCache;
    }
    
    /*
     *  (non-Javadoc)
     * @see org.gvsig.fmap.driver.IBuffer#replicateBand(int, int)
     */
	public void replicateBand(int orig, int dest) {
	}
		
	/*
     *  (non-Javadoc)
     * @see org.gvsig.fmap.dataaccess.buffer.RasterBuffer#switchBands(int[])
     */
    public void switchBands(int[] bandPosition){
    	
    }

	//*********************************************************
	
	public void assign(int band, byte value) {
		for(int line = 0; line < height; line ++){
			boolean beginLine = true;  //Para acelerar solo comprobará si la página está en caché cada vez que empieza una línea
    		for(int col = 0; col < width; col ++){
    			try {
    				if(beginLine){
    					lru.cacheAccess(line, false);
    					beginLine = false;
    				}
    			} catch (InvalidPageNumberException e) {return;}
    			cache.getAccessPage().setElem((line & cache.getOffset()), col, band, value);		
    		}
		}
	}

	public void assign(int band, short value) {
		for(int line = 0; line < height; line ++){
			boolean beginLine = true;  //Para acelerar solo comprobará si la página está en caché cada vez que empieza una línea
    		for(int col = 0; col < width; col ++){
    			try {
    				if(beginLine){
    					lru.cacheAccess(line, false);
    					beginLine = false;
    				}
    			} catch (InvalidPageNumberException e) {return;}
    			cache.getAccessPage().setElem((line & cache.getOffset()), col, band, value);		
    		}
		}		
	}

	public void assign(int band, int value) {
		for(int line = 0; line < height; line ++){
			boolean beginLine = true;  //Para acelerar solo comprobará si la página está en caché cada vez que empieza una línea
    		for(int col = 0; col < width; col ++){
    			try {
    				if(beginLine){
    					lru.cacheAccess(line, false);
    					beginLine = false;
    				}
    			} catch (InvalidPageNumberException e) {return;}
    			cache.getAccessPage().setElem((line & cache.getOffset()), col, band, value);		
    		}
		}	
	}

	public void assign(int band, float value) {
		for(int line = 0; line < height; line ++){
			boolean beginLine = true;  //Para acelerar solo comprobará si la página está en caché cada vez que empieza una línea
    		for(int col = 0; col < width; col ++){
    			try {
    				if(beginLine){
    					lru.cacheAccess(line, false);
    					beginLine = false;
    				}
    			} catch (InvalidPageNumberException e) {return;}
    			cache.getAccessPage().setElem((line & cache.getOffset()), col, band, value);		
    		}
		}	
	}

	public void assign(int band, double value) {
		for(int line = 0; line < height; line ++){
			boolean beginLine = true;  //Para acelerar solo comprobará si la página está en caché cada vez que empieza una línea
    		for(int col = 0; col < width; col ++){
    			try {
    				if(beginLine){
    					lru.cacheAccess(line, false);
    					beginLine = false;
    				}
    			} catch (InvalidPageNumberException e) {return;}
    			cache.getAccessPage().setElem((line & cache.getOffset()), col, band, value);		
    		}
		}
	}
    
    /*
     *  (non-Javadoc)
     * @see org.gvsig.fmap.driver.IBuffer#cloneBuffer()
     */
    public IBuffer cloneBuffer(){
    	return null;
    }
    
    /*
     * (non-Javadoc)
     * @see org.gvsig.fmap.driver.IBuffer#interchangeBands(int, int)
     */
	public void interchangeBands(int band1, int band2) {
		IBand b1 = getBand(band1);
		IBand b2 = getBand(band2);
		
		try {
			cache.assignBand(band2, ((CacheBand)b1).cacheDataServer);
			cache.assignBand(band1, ((CacheBand)b2).cacheDataServer);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	    
    /*
     *  (non-Javadoc)
     * @see org.gvsig.fmap.driver.IBuffer#mallocOneBand(int, int, int, int)
     */
    public void mallocOneBand(int dataType, int width, int height, int band) {
			
	}

    /*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.driver.IBuffer#getBand(int)
	 */
    public IBand getBand(int band) {
    	CacheBand cb = new CacheBand(getHeight(), getWidth());
    	cb.cacheDataServer = new CacheDataServer[cache.getNTotalPags()];
    	
    	try {
			cache.resetCache();
		} catch (IOException e) {
			//TODO: EXCEPCIONES: Modificar el manejo de excepciones de RasterBuffer para q lance las apropiadas con caché
			return null;
		}
    	for (int iPage = 0; iPage < cache.getNTotalPags(); iPage++)
    		cb.cacheDataServer[iPage] = cache.getHddPage(iPage, band);	
	    	
    	return cb;
    }
    
    /*
     *  (non-Javadoc)
     * @see org.gvsig.fmap.driver.IBuffer#copyBand(int, org.gvsig.fmap.dataaccess.buffer.IBand)
     */
	public void copyBand(int nBand, IBand band) {
		if(band instanceof CacheBand){
			CacheBand cb = new CacheBand(band.getHeight(), band.getWidth());
			cb.cacheDataServer = new CacheDataServer[((CacheBand)band).cacheDataServer.length];
			
			for (int iPage = 0; iPage < cb.cacheDataServer.length; iPage++) {
				cb.cacheDataServer[iPage] = new CacheDataServer(null, nBand, iPage);
				String path = ((CacheBand)band).cacheDataServer[iPage].getPath();
				File f = new File(path);
				if(f.exists()){
					try {
						RasterUtilities.copyFile(path, cb.cacheDataServer[iPage].getPath());
					} catch (FileNotFoundException e) {
						//TODO: EXCEPCIONES: Modificar el manejo de excepciones de RasterBuffer para q lance las apropiadas con caché
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			try {
				cache.deleteBand(nBand);
				cache.assignBand(nBand, cb.cacheDataServer);
			} catch (IOException e) {
				e.printStackTrace();
			}
						
		}		
	}

	/*
	 *  (non-Javadoc)
	 * @see org.gvsig.fmap.driver.IBuffer#assignBand(int, org.gvsig.fmap.dataaccess.buffer.IBand)
	 */
	public void assignBand(int nBand, IBand band) {
		if(band instanceof CacheBand) {
			//((CacheBand)band).setFileName(nBand);
			try {
				cache.deleteBand(nBand);
				cache.assignBand(nBand, ((CacheBand)band).cacheDataServer);
			} catch (IOException e) {
				//TODO: EXCEPCIONES: Modificar el manejo de excepciones de RasterBuffer para q lance las apropiadas con caché
				return;
			}
			
		}
	}

	/*
	 *  (non-Javadoc)
	 * @see org.gvsig.fmap.driver.IBuffer#createBand(byte)
	 */
	public IBand createBand(byte defaultValue) {
		PageBandBuffer pageBuffer = new PageBandBuffer(getDataType(), getWidth(), cache.getHPag(), 1, true, 0);
		IBand band = null;
		try {
			band = createBand(pageBuffer);
		} catch (IOException e) {
			return null;
		}
		loadPage(new Byte(defaultValue), pageBuffer);
		return band;
	}
		
	/*
	 *  (non-Javadoc)
	 * @see org.gvsig.fmap.driver.IBuffer#assignBandToNotValid(int)
	 */
	public void assignBandToNotValid(int iBand) {
		PageBandBuffer pageBuffer = new PageBandBuffer(getDataType(), getWidth(), cache.getHPag(), 1, true, 0);

		switch(getDataType()){
    	case IBuffer.TYPE_BYTE: 	loadPage(new Byte((byte)getNotValidValue()), pageBuffer);break;
    	case IBuffer.TYPE_SHORT:	loadPage(new Short((short)getNotValidValue()), pageBuffer);break;
    	case IBuffer.TYPE_INT:		loadPage(new Integer((int)getNotValidValue()), pageBuffer);break;
    	case IBuffer.TYPE_FLOAT:	loadPage(new Float((float)getNotValidValue()), pageBuffer);break;
    	case IBuffer.TYPE_DOUBLE:	loadPage(new Double((double)getNotValidValue()), pageBuffer);break;
    	}	
		
		try {
			CacheBand cb = (CacheBand)createBand(pageBuffer);
			cb.setFileName(iBand);
			assignBand(iBand, cb);
		} catch (IOException e) {
			//TODO: EXCEPCIONES: Modificar el manejo de excepciones de RasterBuffer para q lance las apropiadas con caché
			e.printStackTrace();
		}
	}

	/**
	 * Creación de una banda a partir de una pagina de cache cargada con datos.
	 * @param pageBuffer Pagina de cache cargada de datos
	 * @param numBand Número de banda a la que representa
	 * @return IBand
	 * @throws IOException
	 */
	private IBand createBand(PageBandBuffer pageBuffer) throws IOException{
		CacheDataServer[] ds = new CacheDataServer[cache.getNTotalPags()];
		for (int i = 0; i < cache.getNTotalPags(); i++) {
			ds[i] = new CacheDataServer(null, 0, i);
			ds[i].savePage(pageBuffer);
		}
		CacheBand band = new CacheBand(getHeight(), getWidth());
		band.cacheDataServer = ds;
		return band;
	}
	
	/**
	 * Carga la página con el valor pasado por parámetro. El valor será del mismo tipo de dato 
	 * que el buffer actual.
	 * @param value Valor a inicializar
	 * @param pageBuffer Página
	 */
	private void loadPage(Object value, PageBandBuffer pageBuffer){
		switch(getDataType()){
    	case IBuffer.TYPE_BYTE: for(int i = 0 ; i < getWidth(); i ++)
    								for(int j = 0 ; j < cache.getHPag(); j ++)
    									pageBuffer.setElem(j, i, 0, ((Byte)value).byteValue());
    							break;
    	case IBuffer.TYPE_SHORT: for(int i = 0 ; i < getWidth(); i ++)
									for(int j = 0 ; j < cache.getHPag(); j ++)
										pageBuffer.setElem(j, i, 0, ((Short)value).shortValue());
								 break;
    	case IBuffer.TYPE_INT:	for(int i = 0 ; i < getWidth(); i ++)
									for(int j = 0 ; j < cache.getHPag(); j ++)
										pageBuffer.setElem(j, i, 0, ((Integer)value).intValue());
								break;
    	case IBuffer.TYPE_FLOAT:for(int i = 0 ; i < getWidth(); i ++)
									for(int j = 0 ; j < cache.getHPag(); j ++)
										pageBuffer.setElem(j, i, 0, ((Float)value).floatValue());
									break;
    	case IBuffer.TYPE_DOUBLE:for(int i = 0 ; i < getWidth(); i ++)
									for(int j = 0 ; j < cache.getHPag(); j ++)
										pageBuffer.setElem(j, i, 0, ((Double)value).doubleValue());
								 break;
    	}	
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.IBuffer#free()
	 */
	public void free(){
		cache.getAccessPage().free();
		for (int i = 0; i < cache.getNPags(); i++) {
			if(cache.getPageBufferFromNumberCachePage(i) != null)
				cache.getPageBufferFromNumberCachePage(i).free();
		}
		try {
			clearCache();
		} catch (IOException e) {
		}
	}
}
