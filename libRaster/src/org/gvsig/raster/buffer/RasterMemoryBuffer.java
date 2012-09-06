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

import org.gvsig.raster.dataset.IBuffer;

/**
 * Implementación del buffer de datos en memoria. Contiene las operaciones necesarias
 * para acceso a datos raster implementando IBuffer y su almacenamiento de datos está basado
 * en arrays tridimensionales. Existe un array para cada tipo de dato pero al instanciarse
 * solo se usará uno de ellos, el que corresponda al tipo de dato del raster manejado. 
 * Esto quiere decir que cada RasterMemoryBuffer solo puede contener bandas de un solo tipo de
 * dato donde la variable dataType especificará de que tipo de dato se trata.
 * 
 * @author Nacho Brodin (nachobrodin@gmail.com)
 *
 */
public class RasterMemoryBuffer extends RasterBuffer {
	private ByteBand[]      byteBuf = null;
    private ShortBand[]     shortBuf = null;
    private IntBand[]       intBuf = null;
    private FloatBand[]     floatBuf = null;
    private DoubleBand[]    doubleBuf = null;
    
    public class ByteBand extends RasterBand{
    	public byte[][] 	buf = null;
    	
    	public ByteBand(int height, int width, boolean malloc){
    		super(height, width);
    		if(malloc)
    			buf = new byte[height][width];
    	}

		public Object getLine(int line) {
			return buf[line];
		}
		
		public void setLine(int line, Object value){
			buf[line] = (byte[])value;
		}
		
		public Object getBuf(){
			return buf;
		}
    }
    
    public class ShortBand extends RasterBand{
    	public short[][] 	buf = null;
    	
    	public ShortBand(int height, int width, boolean malloc){
    		super(height, width);
    		if(malloc)
    			buf = new short[height][width];
    	}
    	
    	public Object getLine(int line) {
			return buf[line];
		}
    	
		public void setLine(int line, Object value){
			buf[line] = (short[])value;
		}
		
		public Object getBuf(){
			return buf;
		}
    }

	public class IntBand extends RasterBand{
		public int[][] 	buf = null;
		
		public IntBand(int height, int width, boolean malloc){
			super(height, width);
			if(malloc)
    			buf = new int[height][width];
    	}
		
		public Object getLine(int line) {
			return buf[line];
		}
		
		public void setLine(int line, Object value){
			buf[line] = (int[])value;
		}
		
		public Object getBuf(){
			return buf;
		}
	}

	public class FloatBand extends RasterBand{
		public float[][] 	buf = null;
		
		public FloatBand(int height, int width, boolean malloc){
			super(height, width);
			if(malloc)
    			buf = new float[height][width];
    	}
		
		public Object getLine(int line) {
			return buf[line];
		}
		
		public void setLine(int line, Object value){
			buf[line] = (float[])value;
		}
		
		public Object getBuf(){
			return buf;
		}
	}

	public class DoubleBand extends RasterBand{
		public double[][] 	buf = null;
		
		public DoubleBand(int height, int width, boolean malloc){
			super(height, width);
			if(malloc)
    			buf = new double[height][width];
    	}
		
		public Object getLine(int line) {
			return buf[line];
		}
		
		public void setLine(int line, Object value){
			buf[line] = (double[])value;
		}
		
		public Object getBuf(){
			return buf;
		}
	}
	
    /**
     * Constructor
     */
    public RasterMemoryBuffer() {
    	
    }
    
    /*
     * (non-Javadoc)
     * @see org.gvsig.raster.driver.IBuffer#isBandSwitchable()
     */
    public boolean isBandSwitchable(){
    	return true;
    }
    
    /**
     * Constructor
     * @param dataType Tipo de dato
     * @param width Ancho
     * @param height Alto
     * @param bandNr Banda
     * @param orig
     */
    public RasterMemoryBuffer(int dataType, int width, int height, int bandNr, boolean malloc) {
    	if(malloc)
    		malloc(dataType, width, height, bandNr);
    	else
    		loadVariables(dataType, width, height, bandNr);
    }

    /*
     *  (non-Javadoc)
     * @see org.gvsig.fmap.dataaccess.buffer.RasterBuffer#malloc(int, int, int, int)
     */
    public void malloc(int dataType, int width, int height, int bandNr) {
    	this.dataType = dataType;
        this.width = width;
        this.height = height;
        this.nBands = bandNr;

        if (dataType == TYPE_BYTE) {
            byteBuf = new ByteBand[bandNr];
            for(int i = 0; i < bandNr; i++)
            	byteBuf[i] = new ByteBand(height, width, true);
        } else if ((dataType == TYPE_SHORT) | (dataType == TYPE_USHORT)) {
            shortBuf = new ShortBand[bandNr];
            for(int i = 0; i < bandNr; i++)
            	shortBuf[i] = new ShortBand(height, width, true);
        } else if (dataType == TYPE_INT) {
            intBuf = new IntBand[bandNr];
            for(int i = 0; i < bandNr; i++)
            	intBuf[i] = new IntBand(height, width, true);
        } else if (dataType == TYPE_FLOAT) {
            floatBuf = new FloatBand[bandNr];
            for(int i = 0; i < bandNr; i++)
            	floatBuf[i] = new FloatBand(height, width, true);
        } else if (dataType == TYPE_DOUBLE) {
            doubleBuf = new DoubleBand[bandNr];
            for(int i = 0; i < bandNr; i++)
            	doubleBuf[i] = new DoubleBand(height, width, true);
        }

    }

    /*
     *  (non-Javadoc)
     * @see org.gvsig.fmap.dataaccess.buffer.RasterBuffer#mallocOneBand(int, int, int, int)
     */
    public void mallocOneBand(int dataType, int width, int height, int band){
    	this.dataType = dataType;
        this.width = width;
        this.height = height;
        
        if (dataType == TYPE_BYTE) {
        	if(byteBuf == null)
        		byteBuf = new ByteBand[nBands];
            byteBuf[band] = new ByteBand(height, width, true);
        } else if ((dataType == TYPE_SHORT) | (dataType == TYPE_USHORT)) {
        	if(shortBuf == null)
        		shortBuf = new ShortBand[nBands];
            shortBuf[band] = new ShortBand(height, width, true);
        } else if (dataType == TYPE_INT) {
        	if(intBuf == null)
        		intBuf = new IntBand[nBands];
            intBuf[band] = new IntBand(height, width, true);
        } else if (dataType == TYPE_FLOAT) {
        	if(floatBuf == null)
        		floatBuf = new FloatBand[nBands];
            floatBuf[band] = new FloatBand(height, width, true);
        } else if (dataType == TYPE_DOUBLE) {
        	if(doubleBuf == null)
        		doubleBuf = new DoubleBand[nBands];
            doubleBuf[band] = new DoubleBand(height, width, true);
        }
    }
    
    /**
     * Carga las variables del rasterBuf
     * @param dataType Tipo de dato
     * @param width Ancho
     * @param height Alto
     * @param bandNr Banda
     * @param orig
     */
    private void loadVariables(int dataType, int width, int height, int bandNr) {
    	this.dataType = dataType;
        this.width = width;
        this.height = height;
        this.nBands = bandNr;

        if (dataType == TYPE_BYTE) {
        	byteBuf = new ByteBand[bandNr];
        	for(int i = 0; i < bandNr; i ++)
        		byteBuf[i] = new ByteBand(height, 0, true);
        } else if ((dataType == TYPE_SHORT) | (dataType == TYPE_USHORT)) {
        	shortBuf = new ShortBand[bandNr];
        	for(int i = 0; i < bandNr; i ++)
        		shortBuf[i] = new ShortBand(height, 0, true);
        } else if (dataType == TYPE_INT) {
        	intBuf = new IntBand[bandNr];
        	for(int i = 0; i < bandNr; i ++)
        		intBuf[i] = new IntBand(height, 0, true);
        } else if (dataType == TYPE_FLOAT) {
        	floatBuf = new FloatBand[bandNr];
        	for(int i = 0; i < bandNr; i ++)
        		floatBuf[i] = new FloatBand(height, 0, true);
        } else if (dataType == TYPE_DOUBLE) {
        	doubleBuf = new DoubleBand[bandNr];
        	for(int i = 0; i < bandNr; i ++)
        		doubleBuf[i] = new DoubleBand(height, 0, true);
        }

    }

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

    //***********************************************
    //Obtiene una linea de datos con todas las bandas
    
    public byte[][] getLineByte(int line) {
    	byte[][] r = new byte[nBands][];
    	for(int iBand = 0; iBand < nBands; iBand ++)
    		r[iBand] = (byte[])byteBuf[iBand].getLine(line);
    	return r;
    }

    public short[][] getLineShort(int line) {
    	short[][] r = new short[nBands][];
    	for(int iBand = 0; iBand < nBands; iBand ++)
    		r[iBand] = (short[])shortBuf[iBand].getLine(line);
    	return r;
    }

    public int[][] getLineInt(int line) {
    	int[][] r = new int[nBands][];
    	for(int iBand = 0; iBand < nBands; iBand ++)
    		r[iBand] = (int[])intBuf[iBand].getLine(line);
    	return r;
    }
        
    public float[][] getLineFloat(int line) {
    	float[][] r = new float[nBands][];
    	for(int iBand = 0; iBand < nBands; iBand ++)
    		r[iBand] = (float[])floatBuf[iBand].getLine(line);
    	return r;
    }
    
    public double[][] getLineDouble(int line) {
    	double[][] r = new double[nBands][];
    	for(int iBand = 0; iBand < nBands; iBand ++)
    		r[iBand] = (double[])doubleBuf[iBand].getLine(line);
    	return r;
    }

    //***********************************************
    //Asigna una linea de datos a todas las bandas
    
    public void setLineByte(byte[][] data, int line) {
    	for(int iBand = 0; iBand < nBands; iBand ++)
    		byteBuf[iBand].setLine(line, data[iBand]);
    }

    public void setLineShort(short[][] data, int line) {
    	for(int iBand = 0; iBand < nBands; iBand ++)
    		shortBuf[iBand].setLine(line, data[iBand]);
    }

    public void setLineInt(int[][] data, int line) {
    	for(int iBand = 0; iBand < nBands; iBand ++)
    		intBuf[iBand].setLine(line, data[iBand]);
    }
        
    public void setLineFloat(float[][] data, int line) {
    	for(int iBand = 0; iBand < nBands; iBand ++)
    		floatBuf[iBand].setLine(line, data[iBand]);
    }
    
    public void setLineDouble(double[][] data, int line) {
    	for(int iBand = 0; iBand < nBands; iBand ++)
    		doubleBuf[iBand].setLine(line, data[iBand]);
    }
    
    //***********************************************
    //Obtiene una linea de datos de una banda
    
    public byte[] getLineFromBandByte(int line, int band) {
        return (byte[])byteBuf[band].getLine(line);
    }

    public short[] getLineFromBandShort(int line, int band) {
        return (short[])shortBuf[band].getLine(line);
    }

    public int[] getLineFromBandInt(int line, int band) {
        return (int[])intBuf[band].getLine(line);
    }
        
    public float[] getLineFromBandFloat(int line, int band) {
        return (float[])floatBuf[band].getLine(line);
    }
    
    public double[] getLineFromBandDouble(int line, int band) {
        return (double[])doubleBuf[band].getLine(line);
    }
        
    //***********************************************
    //Asigna una linea de datos a una banda
    
    public void setLineInBandByte(byte[] data, int line, int band) {
        byteBuf[band].setLine(line, data);
    }

    public void setLineInBandShort(short[] data, int line, int band) {
        shortBuf[band].setLine(line, data);
    }

    public void setLineInBandInt(int[] data, int line, int band) {
        intBuf[band].setLine(line, data);
    }
        
    public void setLineInBandFloat(float[] data, int line, int band) {
        floatBuf[band].setLine(line, data);
    }
    
    public void setLineInBandDouble(double[] data, int line, int band) {
        doubleBuf[band].setLine(line, data);
    }
    
    //***********************************************    
    //Obtiene un elemento de la matriz
    
    public byte getElemByte(int line, int col, int band) {
        return byteBuf[band].buf[line][col];
    }
    
    public short getElemShort(int line, int col, int band) {
        return shortBuf[band].buf[line][col];
    }
    
    public int getElemInt(int line, int col, int band) {
        return intBuf[band].buf[line][col];
    }
    
    public float getElemFloat(int line, int col, int band) {
        return floatBuf[band].buf[line][col];
    }
    
    public double getElemDouble(int line, int col, int band) {
        return doubleBuf[band].buf[line][col];
    }
    
    //**********************************************    
    //Asigna un elemento de la matriz
    
    public void setElem(int line, int col, int band, byte data) {
    	byteBuf[band].buf[line][col] = data;
    }
    
    public void setElem(int line, int col, int band, short data) {
    	shortBuf[band].buf[line][col] = data;
    }
    
    public void setElem(int line, int col, int band, int data) {
    	intBuf[band].buf[line][col] = data;
    }
    
    public void setElem(int line, int col, int band, float data) {
    	floatBuf[band].buf[line][col] = data;
    }
    
    public void setElem(int line, int col, int band, double data) {
    	doubleBuf[band].buf[line][col] = data;
    }
    
    //***********************************************
    //Copia un elemento de todas la bandas en el buffer pasado por parámetro
    
    public void getElemByte(int line, int col, byte[] data) {
    	for (int iBand = 0; (iBand < nBands && iBand < data.length); iBand++)
            data[iBand] = byteBuf[iBand].buf[line][col];
    }
    
    public void getElemShort(int line, int col, short[] data) {
    	for (int iBand = 0; (iBand < nBands && iBand < data.length); iBand++)
            data[iBand] = shortBuf[iBand].buf[line][col];
    }
    
    public void getElemInt(int line, int col, int[] data) {
    	for (int iBand = 0; (iBand < nBands && iBand < data.length); iBand++)
            data[iBand] = intBuf[iBand].buf[line][col];
    }  
    
    public void getElemFloat(int line, int col, float[] data) {
    	for (int iBand = 0; (iBand < nBands && iBand < data.length); iBand++)
            data[iBand] = floatBuf[iBand].buf[line][col];
    }
    
    public void getElemDouble(int line, int col, double[] data) {
    	for (int iBand = 0; (iBand < nBands && iBand < data.length); iBand++)
            data[iBand] = doubleBuf[iBand].buf[line][col];
    }

    //***********************************************
    //Asigna un elemento a todas la bandas en el buffer pasado por parámetro
    
    public void setElemByte(int line, int col, byte[] data) {
    	for (int iBand = 0; (iBand < nBands && iBand < data.length); iBand++)
            byteBuf[iBand].buf[line][col] = data[iBand];
    }
    
    public void setElemShort(int line, int col, short[] data) {
    	for (int iBand = 0; (iBand < nBands && iBand < data.length); iBand++)
            shortBuf[iBand].buf[line][col] = data[iBand];
    }
    
    public void setElemInt(int line, int col, int[] data) {
    	for (int iBand = 0; (iBand < nBands && iBand < data.length); iBand++)
            intBuf[iBand].buf[line][col] = data[iBand];
    }

    public void setElemFloat(int line, int col, float[] data) {
    	for (int iBand = 0; (iBand < nBands && iBand < data.length); iBand++)
            floatBuf[iBand].buf[line][col] = data[iBand];
    }
    
    public void setElemDouble(int line, int col, double[] data) {
    	for (int iBand = 0; (iBand < nBands && iBand < data.length); iBand++)
            doubleBuf[iBand].buf[line][col] = data[iBand];
    }
    
    //***********************************************
    //Obtiene una banda entera
    
    public IBand getBand(int band){
    	if (dataType == TYPE_BYTE) {
    		return byteBuf[band];
        } else if ((dataType == TYPE_SHORT) | (dataType == TYPE_USHORT)) {
        	return shortBuf[band];
        } else if (dataType == TYPE_INT) {
        	return intBuf[band];
        }else if (dataType == TYPE_FLOAT) {
        	return floatBuf[band];
        }else if (dataType == TYPE_DOUBLE) {
        	return doubleBuf[band];
        }
    	return null;
    }
    
    /*
     *  (non-Javadoc)
     * @see org.gvsig.fmap.driver.IBuffer#getBandBuffer(int)
     */
    public IBuffer getBandBuffer(int iBand){
    	RasterMemoryBuffer rmb = new RasterMemoryBuffer(dataType, width, height, 1, false);
    	if (dataType == TYPE_BYTE) 
    		rmb.byteBuf[0].buf = byteBuf[iBand].buf;
        else if ((dataType == TYPE_SHORT) | (dataType == TYPE_USHORT))
        	rmb.shortBuf[0].buf = shortBuf[iBand].buf;
        else if (dataType == TYPE_INT)
        	rmb.intBuf[0].buf = intBuf[iBand].buf;
        else if (dataType == TYPE_FLOAT)
        	rmb.floatBuf[0].buf = floatBuf[iBand].buf;
        else if (dataType == TYPE_DOUBLE)
        	rmb.doubleBuf[0].buf = doubleBuf[iBand].buf;
    	return rmb;
    }
    //***********************************************
    //Inicializa una banda a un valor pasado por parámetro
    
    public void assign(int band, byte value) {
    	for(int line = 0; line < height; line ++)
    		for(int col = 0; col < width; col ++)
    			byteBuf[band].buf[line][col] = value;
    }
    
    public void assign(int band, short value) {
    	for(int line = 0; line < height; line ++)
    		for(int col = 0; col < width; col ++)
    			shortBuf[band].buf[line][col] = value;
    }
    
    public void assign(int band, int value) {
    	for(int line = 0; line < height; line ++)
    		for(int col = 0; col < width; col ++)
    			intBuf[band].buf[line][col] = value;
    }

    public void assign(int band, float value) {
    	for(int line = 0; line < height; line ++)
    		for(int col = 0; col < width; col ++)
    			floatBuf[band].buf[line][col] = value;
    }
    
    public void assign(int band, double value) {
    	for(int line = 0; line < height; line ++)
    		for(int col = 0; col < width; col ++)
    			doubleBuf[band].buf[line][col] = value;
    }
    
    //***********************************************
    //Crea un buffer banda inicializado con el valor pasado por parámetro
    

    public IBand createBand(byte defaultValue){
    	switch(getDataType()){
    	case RasterBuffer.TYPE_BYTE:ByteBand bb = new ByteBand(width, height, false);
    								bb.buf = createByteBand(width, height, defaultValue);
    								return bb;
    	case RasterBuffer.TYPE_SHORT:	ShortBand sb = new ShortBand(width, height, false);
										sb.buf = createShortBand(width, height, defaultValue);
										return sb;
    	case RasterBuffer.TYPE_INT:	IntBand ib = new IntBand(width, height, false);
									ib.buf = createIntBand(width, height, defaultValue);
									return ib;
    	case RasterBuffer.TYPE_FLOAT:	FloatBand fb = new FloatBand(width, height, false);
										fb.buf = createFloatBand(width, height, defaultValue);
										return fb;
    	case RasterBuffer.TYPE_DOUBLE:	DoubleBand db = new DoubleBand(width, height, false);
										db.buf = createDoubleBand(width, height, defaultValue);
										return db;
    	}
    	return null;
    }

    public byte[][] createByteBand(int width, int height, byte defaultValue){
    	byte[][] band = new byte[height][width];
    	if(defaultValue != 0){
	    	for(int line = 0; line < height; line ++)
	    		for(int col = 0; col < width; col ++)
	    			band[line][col] = defaultValue;
    	}
    	return band;
    }
    
    public short[][] createShortBand(int width, int height, short defaultValue){
    	short[][] band = new short[height][width];
    	if(defaultValue != 0){
	    	for(int line = 0; line < height; line ++)
	    		for(int col = 0; col < width; col ++)
	    			band[line][col] = defaultValue;
    	}
    	return band;
    }
    
    public int[][] createIntBand(int width, int height, int defaultValue){
    	int[][] band = new int[height][width];
    	if(defaultValue != 0){
	    	for(int line = 0; line < height; line ++)
	    		for(int col = 0; col < width; col ++)
	    			band[line][col] = defaultValue;
    	}
    	return band;
    }
    
    public float[][] createFloatBand(int width, int height, float defaultValue){
    	float[][] band = new float[height][width];
    	if(defaultValue != 0){
	    	for(int line = 0; line < height; line ++)
	    		for(int col = 0; col < width; col ++)
	    			band[line][col] = defaultValue;
    	}
    	return band;
    }
    
    public double[][] createDoubleBand(int width, int height, double defaultValue){
    	double[][] band = new double[height][width];
    	if(defaultValue != 0){
	    	for(int line = 0; line < height; line ++)
	    		for(int col = 0; col < width; col ++)
	    			band[line][col] = defaultValue;
    	}
    	return band;
    }
    
    //***********************************************
    //Añade una banda entera. Los datos son asignados por referencia

    private void addBandByte(int pos, IBand data) {
    	if(pos < 0)
    		return;
    	ByteBand[] tmp = null;
    	if(pos >= byteBuf.length){
    		tmp = new ByteBand[pos + 1];
    		for(int iBand = 0; iBand < byteBuf.length; iBand ++)
    			tmp[iBand] = byteBuf[iBand];
    		tmp[pos] = (ByteBand)data;
    	}else{
    		tmp = new ByteBand[byteBuf.length + 1];
    		for(int iBand = 0; iBand < pos; iBand ++)
    			tmp[iBand] = byteBuf[iBand];
    		tmp[pos] = (ByteBand)data;
    		for(int iBand = pos + 1; iBand <= byteBuf.length; iBand ++)
    			tmp[iBand] = byteBuf[iBand - 1];
    	}
    	nBands = tmp.length;
    	byteBuf = tmp;
    }
    
    private void addBandShort(int pos, IBand data) {
    	if(pos < 0)
    		return;
    	ShortBand[] tmp = null;
    	if(pos >= shortBuf.length){
    		tmp = new ShortBand[pos + 1];
    		for(int iBand = 0; iBand < shortBuf.length; iBand ++)
    			tmp[iBand] = shortBuf[iBand];
    		tmp[pos] = (ShortBand)data;
    	}else{
    		tmp = new ShortBand[shortBuf.length + 1];
    		for(int iBand = 0; iBand < pos; iBand ++)
    			tmp[iBand] = shortBuf[iBand];
    		tmp[pos] = (ShortBand)data;
    		for(int iBand = pos + 1; iBand < shortBuf.length; iBand ++)
    			tmp[iBand] = shortBuf[iBand - 1];
    	}
    	nBands = tmp.length;
    	shortBuf = tmp;
    }
    
    private void addBandInt(int pos, IBand data) {
    	if(pos < 0)
    		return;
    	IntBand[] tmp = null;
    	if(pos >= intBuf.length){
    		tmp = new IntBand[pos + 1];
    		for(int iBand = 0; iBand < intBuf.length; iBand ++)
    			tmp[iBand] = intBuf[iBand];
    		tmp[pos] = (IntBand)data;
    	}else{
    		tmp = new IntBand[intBuf.length + 1];
    		for(int iBand = 0; iBand < pos; iBand ++)
    			tmp[iBand] = intBuf[iBand];
    		tmp[pos] = (IntBand)data;
    		for(int iBand = pos + 1; iBand < intBuf.length; iBand ++)
    			tmp[iBand] = intBuf[iBand - 1];
    	}
    	nBands = tmp.length;
    	intBuf = tmp;
    }
    
    private void addBandFloat(int pos, IBand data) {
    	if(pos < 0)
    		return;
    	FloatBand[] tmp = null;
    	if(pos >= floatBuf.length){
    		tmp = new FloatBand[pos + 1];
    		for(int iBand = 0; iBand < floatBuf.length; iBand ++)
    			tmp[iBand] = floatBuf[iBand];
    		tmp[pos] = (FloatBand)data;
    	}else{
    		tmp = new FloatBand[floatBuf.length + 1];
    		for(int iBand = 0; iBand < pos; iBand ++)
    			tmp[iBand] = floatBuf[iBand];
    		tmp[pos] = (FloatBand)data;
    		for(int iBand = pos + 1; iBand < floatBuf.length; iBand ++)
    			tmp[iBand] = floatBuf[iBand - 1];
    	}
    	nBands = tmp.length;
    	floatBuf = tmp;
    }
    
    private void addBandDouble(int pos, IBand data) {
    	if(pos < 0)
    		return;
    	DoubleBand[] tmp = null;
    	if(pos >= doubleBuf.length){
    		tmp = new DoubleBand[pos + 1];
    		for(int iBand = 0; iBand < doubleBuf.length; iBand ++)
    			tmp[iBand] = doubleBuf[iBand];
    		tmp[pos] = (DoubleBand)data;
    	}else{
    		tmp = new DoubleBand[doubleBuf.length + 1];
    		for(int iBand = 0; iBand < pos; iBand ++)
    			tmp[iBand] = doubleBuf[iBand];
    		tmp[pos] = (DoubleBand)data;
    		for(int iBand = pos + 1; iBand < doubleBuf.length; iBand ++)
    			tmp[iBand] = doubleBuf[iBand - 1];
    	}
    	nBands = tmp.length;
    	doubleBuf = tmp;
    }
    
    //***********************************************
    //Reemplaza una banda entera. Los datos son reemplazados por referencia
    
   /* public void replaceBandByte(int pos, byte[][] data) {
    	if(pos >= byteBuf.length)
    		return;
    	byteBuf[pos] = data;
    }
    
    public void replaceBandShort(int pos, short[][] data) {
    	if(pos >= shortBuf.length)
    		return;
    	shortBuf[pos] = data;
    }
    
    public void replaceBandInt(int pos, int[][] data) {
    	if(pos >= intBuf.length)
    		return;
    	intBuf[pos] = data;
    }
    
    public void replaceBandFloat(int pos, float[][] data) {
    	if(pos >= floatBuf.length)
    		return;
    	floatBuf[pos] = data;
    }
    
    public void replaceBandDouble(int pos, double[][] data) {
    	if(pos >= doubleBuf.length)
    		return;
    	doubleBuf[pos] = data;
    }*/
    
    /**
     * Replica la banda de una posición sobre otra. Si la banda de destino no existe
     * se crea nueva. Si la posición de la banda de destino está intercalada entre bandas 
     * que ya existen las otras se desplazan hacia abajo, NO se machacan los datos de ninguna.
     * Los datos se replican por referencia por lo que al modificar la banda original las
     * del resto quedarán afectadas.   
     * @param orig. Posición de la banda de origen. 
     * @param dest. Posición de la banda destino
     */   
    public void replicateBand(int orig, int dest){
    	switch(getDataType()){
    	case RasterBuffer.TYPE_BYTE:	if(orig >= byteBuf.length)
											return;
										addBandByte(dest, getBand(orig));
										break;
    	case RasterBuffer.TYPE_SHORT:	if(orig >= shortBuf.length)
    										return;
    									addBandShort(dest, getBand(orig));
    									break;
    	case RasterBuffer.TYPE_INT: 	if(orig >= intBuf.length)
											return;
										addBandInt(dest, getBand(orig));
										break;
    	case RasterBuffer.TYPE_FLOAT:	if(orig >= floatBuf.length)
											return;
										addBandFloat(dest, getBand(orig));
										break;
    	case RasterBuffer.TYPE_DOUBLE:	if(orig >= doubleBuf.length)
											return;
										addBandDouble(dest, getBand(orig));
										break;
    	}
    }
    
    /*
     *  (non-Javadoc)
     * @see org.gvsig.fmap.dataaccess.buffer.RasterBuffer#switchBands(int[])
     */
    public void switchBands(int[] bandPosition){
    	if(bandPosition.length != this.getBandCount())
    		return;
    	for(int i = 0; i < bandPosition.length; i++)
    		if(bandPosition[i] >=  bandPosition.length || bandPosition[i] < 0)
    			return;
    	
    	switch (getDataType()) {
    	case RasterBuffer.TYPE_BYTE:
    		ByteBand[] bufB = new ByteBand[this.getBandCount()];
    		for(int i = 0; i < bandPosition.length; i++)
    			bufB[i] = byteBuf[bandPosition[i]];
    		byteBuf = bufB;
    		break;
    	case RasterBuffer.TYPE_DOUBLE:
    		DoubleBand[] bufD = new DoubleBand[this.getBandCount()];
    		for(int i = 0; i < bandPosition.length; i++)
    			bufD[i] = doubleBuf[bandPosition[i]];
    		doubleBuf = bufD;
    		break;    		
    	case RasterBuffer.TYPE_FLOAT:
    		FloatBand[] bufF = new FloatBand[this.getBandCount()];
    		for(int i = 0; i < bandPosition.length; i++)
    			bufF[i] = floatBuf[bandPosition[i]];
    		floatBuf = bufF;
    		break;     		
    	case RasterBuffer.TYPE_INT:
    		IntBand[] bufI = new IntBand[this.getBandCount()];
    		for(int i = 0; i < bandPosition.length; i++)
    			bufI[i] = intBuf[bandPosition[i]];
    		intBuf = bufI;
    		break;     		
    	case RasterBuffer.TYPE_USHORT:
    	case RasterBuffer.TYPE_SHORT:
    		ShortBand[] bufS = new ShortBand[this.getBandCount()];
    		for(int i = 0; i < bandPosition.length; i++)
    			bufS[i] = shortBuf[bandPosition[i]];
    		shortBuf = bufS;
    		break; 
    	}
    }
    
    /*
     *  (non-Javadoc)
     * @see org.gvsig.fmap.driver.IBuffer#copyBand(int, org.gvsig.fmap.dataaccess.buffer.IBand)
     */
    public void copyBand(int nBand, IBand band) {
    	switch (band.getDataType()) {
    	case RasterBuffer.TYPE_BYTE:
    		byteBuf[nBand] = new ByteBand(band.getHeight(), band.getWidth(), true);
    		byte[][] bb = ((ByteBand)band).buf;
    		for(int i = 0; i < bb.length; i ++)
    			for(int j = 0; j < bb[i].length; j ++)
    				byteBuf[nBand].buf[i][j] = bb[i][j];
    		break;
    	case RasterBuffer.TYPE_DOUBLE:
    		doubleBuf[nBand] = new DoubleBand(band.getHeight(), band.getWidth(), true);
    		double[][] db = ((DoubleBand)band).buf;
    		for(int i = 0; i < db.length; i ++)
    			for(int j = 0; j < db[i].length; j ++)
    				doubleBuf[nBand].buf[i][j] = db[i][j];    		
    		break;    		
    	case RasterBuffer.TYPE_FLOAT:
    		floatBuf[nBand] = new FloatBand(band.getHeight(), band.getWidth(), true);
    		float[][] fb = ((FloatBand)band).buf;
    		for(int i = 0; i < fb.length; i ++)
    			for(int j = 0; j < fb[i].length; j ++)
    				floatBuf[nBand].buf[i][j] = fb[i][j];
    		break;     		
    	case RasterBuffer.TYPE_INT:
    		intBuf[nBand] = new IntBand(band.getHeight(), band.getWidth(), true);
    		int[][] ib = ((IntBand)band).buf;
    		for(int i = 0; i < ib.length; i ++)
    			for(int j = 0; j < ib[i].length; j ++)
    				intBuf[nBand].buf[i][j] = ib[i][j];
    		break;     		
    	case RasterBuffer.TYPE_USHORT:
    	case RasterBuffer.TYPE_SHORT:
    		shortBuf[nBand] = new ShortBand(band.getHeight(), band.getWidth(), true);
    		short[][] sb = ((ShortBand)band).buf;
    		for(int i = 0; i < sb.length; i ++)
    			for(int j = 0; j < sb[i].length; j ++)
    				shortBuf[nBand].buf[i][j] = sb[i][j];
    		break; 
    	}
	}

    /*
     *  (non-Javadoc)
     * @see org.gvsig.fmap.driver.IBuffer#assignBand(int, org.gvsig.fmap.dataaccess.buffer.IBand)
     */
	public void assignBand(int nBand, IBand band) {
		switch (getDataType()) {
    	case RasterBuffer.TYPE_BYTE:
    		byteBuf[nBand] = ((ByteBand)band);
    		break;
    	case RasterBuffer.TYPE_DOUBLE:
    		doubleBuf[nBand] = ((DoubleBand)band);  		
    		break;    		
    	case RasterBuffer.TYPE_FLOAT:
    		floatBuf[nBand] = ((FloatBand)band);
    		break;     		
    	case RasterBuffer.TYPE_INT:
    		intBuf[nBand] = ((IntBand)band);
    		break;     		
    	case RasterBuffer.TYPE_USHORT:
    	case RasterBuffer.TYPE_SHORT:
    		shortBuf[nBand] = ((ShortBand)band);
    		break; 
    	}
	}
    
    /*
     *  (non-Javadoc)
     * @see org.gvsig.fmap.driver.IBuffer#cloneBuffer()
     */
    public IBuffer cloneBuffer(){
    	boolean malloc = false;
    	if(byteBuf != null || shortBuf != null || intBuf != null || floatBuf != null || doubleBuf != null)
			malloc = true;
    	RasterMemoryBuffer rmb = new RasterMemoryBuffer(dataType, width, height, nBands, malloc);
    	for(int iBand = 0; iBand < nBands; iBand ++){
    		for(int row = 0; row < height; row ++){
    			for(int col = 0; col < width; col ++){
    				if(byteBuf != null)
    					rmb.setElem(row, col, iBand, getElemByte(row, col, iBand));
    				if(shortBuf != null)
    					rmb.setElem(row, col, iBand, getElemShort(row, col, iBand));
    				if(intBuf != null)
    					rmb.setElem(row, col, iBand, getElemInt(row, col, iBand));
    				if(floatBuf != null)
    					rmb.setElem(row, col, iBand, getElemFloat(row, col, iBand));
    				if(doubleBuf != null)
    					rmb.setElem(row, col, iBand, getElemDouble(row, col, iBand));
    			}
    		}
    	}
    	    	
    	return rmb;
    }
    
    /*
     * (non-Javadoc)
     * @see org.gvsig.fmap.driver.IBuffer#interchangeBands(int, int)
     */
    public void interchangeBands(int band1, int band2){
    	switch (getDataType()) {
    	case RasterBuffer.TYPE_BYTE:
    		ByteBand auxByte = byteBuf[band1];
    		byteBuf[band1] = byteBuf[band2];
    		byteBuf[band2] = auxByte;
    		break;
    	case RasterBuffer.TYPE_DOUBLE:
    		DoubleBand auxDouble = doubleBuf[band1];
    		doubleBuf[band1] = doubleBuf[band2];
    		doubleBuf[band2] = auxDouble;
    		break;    		
    	case RasterBuffer.TYPE_FLOAT:
    		FloatBand auxFloat = floatBuf[band1];
    		floatBuf[band1] = floatBuf[band2];
    		floatBuf[band2] = auxFloat;
    		break;     		
    	case RasterBuffer.TYPE_INT:
    		IntBand auxInt = intBuf[band1];
    		intBuf[band1] = intBuf[band2];
    		intBuf[band2] = auxInt;
    		break;     		
    	case RasterBuffer.TYPE_USHORT:
    	case RasterBuffer.TYPE_SHORT:
    		ShortBand auxShort = shortBuf[band1];
    		shortBuf[band1] = shortBuf[band2];
    		shortBuf[band2] = auxShort;
    		break; 
    	}
    }
        
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
    
	private ByteBand 	byteNotValid;
    private ShortBand 	shortNotValid;
    private IntBand 	intNotValid;
    private FloatBand	floatNotValid;
    private DoubleBand	doubleNotValid;
    
	/*
	 *  (non-Javadoc)
	 * @see org.gvsig.fmap.driver.IBuffer#assignBandToNotValid(int)
	 */
    public void assignBandToNotValid(int iBand) {
    	switch(getDataType()){
    	case IBuffer.TYPE_BYTE: if(byteNotValid == null) {
    								byteNotValid = new ByteBand(getHeight(), getWidth(), true);
    								for(int i = 0 ; i < getWidth(); i ++)
    									for(int j = 0 ; j < getHeight(); j ++)
    										byteNotValid.buf[j][i] = (byte)getNotValidValue();
    							}
    							byteBuf[iBand] = byteNotValid;
    							break;
    	case IBuffer.TYPE_SHORT: if(shortNotValid == null) {
					    			shortNotValid = new ShortBand(getHeight(), getWidth(), true);
					    			for(int i = 0 ; i < getWidth(); i ++)
					    				for(int j = 0 ; j < getHeight(); j ++)
					    					shortNotValid.buf[j][i] = (short)getNotValidValue();
					    			}
					    		  shortBuf[iBand] = shortNotValid;
					    		  break;
    	case IBuffer.TYPE_INT:	if(intNotValid == null) {
									intNotValid = new IntBand(getHeight(), getWidth(), true);
									for(int i = 0 ; i < getWidth(); i ++)
										for(int j = 0 ; j < getHeight(); j ++)
											intNotValid.buf[j][i] = (int)getNotValidValue();
								}
								intBuf[iBand] = intNotValid;
								break;
    	case IBuffer.TYPE_FLOAT:	if(floatNotValid == null) {
										floatNotValid = new FloatBand(getHeight(), getWidth(), true);
										for(int i = 0 ; i < getWidth(); i ++)
											for(int j = 0 ; j < getHeight(); j ++)
												floatNotValid.buf[j][i] = (float)getNotValidValue();
									}
									floatBuf[iBand] = floatNotValid;
									break;
    	case IBuffer.TYPE_DOUBLE:	if(doubleNotValid == null) {
										doubleNotValid = new DoubleBand(getHeight(), getWidth(), true);
										for(int i = 0 ; i < getWidth(); i ++)
											for(int j = 0 ; j < getHeight(); j ++)
												doubleNotValid.buf[j][i] = (double)getNotValidValue();
									}
									doubleBuf[iBand] = doubleNotValid;
									break;
    	}
    }

	/**
	 * Libera el buffer de memoria
	 */
	public void free(){
		byteBuf = null;
	    shortBuf = null;
	    intBuf = null;
	    floatBuf = null;
	    doubleBuf = null;
	}
}