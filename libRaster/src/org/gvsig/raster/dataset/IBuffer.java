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

import java.awt.image.DataBuffer;

import org.gvsig.raster.buffer.IBand;
import org.gvsig.raster.hierarchy.IHistogramable;

/**
 * Interfaz que contiene las operaciones que debe soportar un buffer de datos.
 * @author Nacho Brodin (nachobrodin@gmail.com)
 *
 */
public interface IBuffer extends IHistogramable{
    public final static int TYPE_UNDEFINED = DataBuffer.TYPE_UNDEFINED;
    public final static int TYPE_BYTE = DataBuffer.TYPE_BYTE;
    public final static int TYPE_SHORT = DataBuffer.TYPE_SHORT;
    public final static int TYPE_USHORT = DataBuffer.TYPE_USHORT;
    public final static int TYPE_INT = DataBuffer.TYPE_INT;
    public final static int TYPE_FLOAT = DataBuffer.TYPE_FLOAT;
    public final static int TYPE_DOUBLE = DataBuffer.TYPE_DOUBLE;
    public final static int TYPE_IMAGE = -1;
    
	/*public static int TYPE_UNDEFINED = 32;
	public static int TYPE_Byte = 0;		//Buffer byte (8)
	public static int TYPE_UInt16 = 1;		//Buffer short (16)
	public static int TYPE_Int16 = 2;		//Buffer short (16)
	public static int TYPE_UInt32 = 6;		//Buffer int (32)
	public static int TYPE_Int32 = 3;		//Buffer int (32)
	public static int TYPE_Float32 = 4;		//Buffer float (32)
	public static int TYPE_Float64 = 5;		//Buffer double (64)
	public static int TYPE_CInt16 = 7;		//Buffer short (16)
	public static int TYPE_CInt32 = 8;		//Buffer int (32)
	public static int TYPE_CFloat32 = 9;	//Buffer float (32)
	public static int TYPE_CFloat64 = 10;*/	//Buffer double (64)
    
    /**
     * Ancho del raster
     * @return Entero con el ancho del raster
     */
    public int getWidth();

    /**
     * Alto del raster
     * @return Entero con el alto del raster
     */
    public int getHeight();

    /**
     * Número de bandas
     * @return Entero con el número de bandas
     */
    public int getBandCount();

    /**
     * Obtiene el tipo de dato. Los tipos de dato posibles están definidos en IRaster.
     * @return tipo de datos
     */
	public int getDataType();
	
	/**
	 * Asigna el tipo de dato. Los tipos de dato posibles están definidos en IRaster.
	 * @param dataType Tipo de dato del buffer
	 */
	public void setDataType(int dataType);
	
    /**
     * Obtiene el valor NoData del buffer
     * @return Valor NoData del buffer
     */
    public double getNoDataValue();
    
    /**
     * Obtiene el valor NoData del buffer
     * @return Valor NoData del buffer
     */
    public byte getByteNoDataValue();
    
    /**
     * Obtiene el valor NoData del buffer
     * @return Valor NoData del buffer
     */
    public short getShortNoDataValue();
    
    /**
     * Obtiene el valor NoData del buffer
     * @return Valor NoData del buffer
     */
    public int getIntNoDataValue();
    
    /**
     * Obtiene el valor NoData del buffer
     * @return Valor NoData del buffer
     */
    public float getFloatNoDataValue();
    
    /**
     * Obtiene true si el pixel pasado por parámetro cae dentro de los límites
     * del rater y false si cae fuera. 
     * @param x Posición X del pixel a averiguar
     * @param y Posición Y del pixel a averiguar
     * @return true si está dentro y false si cae fuera.
     */
    public boolean isInside(int x, int y);
    
		/**
		 * Calcula el mínimo y el máximo del histograma previamente.
		 * @return double[] con el mínimo y máximo y el segundo minimo y maximo.
		 * @throws InterruptedException 
		 */
		public double[] getLimits() throws InterruptedException;
    
    /**
     * Asigna el valor NoData 
     * @param nd Valor NoData
     */
    public void setNoDataValue(double nd);
    
    /**
     * Reserva de memoria para el rasterbuf solo en la banda solicitada
     * @param dataType Tipo de dato
     * @param width Ancho
     * @param height Alto
     * @param band Número de banda
     * @param orig
     */
    public void mallocOneBand(int dataType, int width, int height, int band);
    
    /**
     * Informa de si el buffer tiene la capacidad de intercambio de bandas o
     * no la tiene.
     * @return Devuelve true si tiene la capacidad de intercambio de bandas y 
     * false si no la tiene.
     */
    public boolean isBandSwitchable();
    
	/**
	 * Libera el buffer de memoria
	 */
	public void free();

    //***********************************************
    //Obtiene una linea de datos de todas las bandas
    
	/**
	 * Obtiene una línea de datos con todas las bandas para buffers con tipo
	 * de dato byte.
	 * @param line Número de línea del buffer a recuperar
	 * @return Array bidimensional conteniendo la línea de datos donde la primera
	 * dimensión representa las bandas y la segunda la posición en la línea
	 */    
    public byte[][] getLineByte(int line);

    /**
	 * Obtiene una línea de datos con todas las bandas para buffers con tipo
	 * de dato short.
	 * @param line Número de línea del buffer a recuperar
	 * @return Array bidimensional conteniendo la línea de datos donde la primera
	 * dimensión representa las bandas y la segunda la posición en la línea
	 */  
    public short[][] getLineShort(int line);

    /**
	 * Obtiene una línea de datos con todas las bandas para buffers con tipo
	 * de dato int.
	 * @param line Número de línea del buffer a recuperar
	 * @return Array bidimensional conteniendo la línea de datos donde la primera
	 * dimensión representa las bandas y la segunda la posición en la línea
	 */  
    public int[][] getLineInt(int line);
        
    /**
	 * Obtiene una línea de datos con todas las bandas para buffers con tipo
	 * de dato float.
	 * @param line Número de línea del buffer a recuperar
	 * @return Array bidimensional conteniendo la línea de datos donde la primera
	 * dimensión representa las bandas y la segunda la posición en la línea
	 */  
    public float[][] getLineFloat(int line);
    
    /**
	 * Obtiene una línea de datos con todas las bandas para buffers con tipo
	 * de dato double.
	 * @param line Número de línea del buffer a recuperar
	 * @return Array bidimensional conteniendo la línea de datos donde la primera
	 * dimensión representa las bandas y la segunda la posición en la línea
	 */  
    public double[][] getLineDouble(int line);
    
    //***********************************************
    //Obtiene una linea de datos de una banda
    
    /**
     * Obtiene una línea de datos de la banda solicitada para buffers con tipo
	 * de dato byte.
     * @param line Número de línea del buffer a recuperar
     * @param band Número de banda a recuperar
     * @return Array unidimensional que representa la línea de datos.
     */
    public byte[] getLineFromBandByte(int line, int band);

    /**
     * Obtiene una línea de datos de la banda solicitada para buffers con tipo
	 * de dato short.
     * @param line Número de línea del buffer a recuperar
     * @param band Número de banda a recuperar
     * @return Array unidimensional que representa la línea de datos.
     */
    public short[] getLineFromBandShort(int line, int band);

    /**
     * Obtiene una línea de datos de la banda solicitada para buffers con tipo
	 * de dato int.
     * @param line Número de línea del buffer a recuperar
     * @param band Número de banda a recuperar
     * @return Array unidimensional que representa la línea de datos.
     */
    public int[] getLineFromBandInt(int line, int band);
        
    /**
     * Obtiene una línea de datos de la banda solicitada para buffers con tipo
	 * de dato float.
     * @param line Número de línea del buffer a recuperar
     * @param band Número de banda a recuperar
     * @return Array unidimensional que representa la línea de datos.
     */
    public float[] getLineFromBandFloat(int line, int band);
    
    /**
     * Obtiene una línea de datos de la banda solicitada para buffers con tipo
	 * de dato double.
     * @param line Número de línea del buffer a recuperar
     * @param band Número de banda a recuperar
     * @return Array unidimensional que representa la línea de datos.
     */
    public double[] getLineFromBandDouble(int line, int band);
    
    //***********************************************    
    //Obtiene un elemento de la matriz
    
    public byte getElemByte(int line, int col, int band);
    
    public short getElemShort(int line, int col, int band);
    
    public int getElemInt(int line, int col, int band);
    
    public float getElemFloat(int line, int col, int band);
    
    public double getElemDouble(int line, int col, int band);
    
    //***********************************************
    //Copia un elemento de todas la bandas en el buffer pasado por parámetro
    
    public void getElemByte(int line, int col, byte[] data);
    
    public void getElemShort(int line, int col, short[] data);
    
    public void getElemInt(int line, int col, int[] data);
    
    public void getElemFloat(int line, int col, float[] data);
    
    public void getElemDouble(int line, int col, double[] data);
    
    //***********************************************
    
    /**
     * Sustituye una banda completa copiando los datos de la que se pasa por parámetro
     * @param nBand Número de banda a sustituir
     * @param banda a copiar
     */
    public void copyBand(int nBand, IBand band);
    
    /**
     * Sustituye una banda completa que se asigna por referencia
     * @param nBand Número de banda a sustituir
     * @param banda a asignar por referencia
     */
    public void assignBand(int nBand, IBand band);
    
    /**
     * Obtiene una banda completa del raster
     * @param nBand Número de banda 
     */
    public IBand getBand(int nBand);
    
    /**
     * Obtiene una banda completa del raster
     * @param nBand Número de banda 
     */
    public IBuffer getBandBuffer(int nBand);
        
    //***********************************************
    //Asigna una linea de datos a una banda
    
    public void setLineInBandByte(byte[] data, int line, int band);

    public void setLineInBandShort(short[] data, int line, int band);

    public void setLineInBandInt(int[] data, int line, int band);
        
    public void setLineInBandFloat(float[] data, int line, int band);
    
    public void setLineInBandDouble(double[] data, int line, int band);
    
    //***********************************************
    //Asigna una linea de datos a todas las bandas
    
    public void setLineByte(byte[][] data, int line);

    public void setLineShort(short[][] data, int line);

    public void setLineInt(int[][] data, int line);
        
    public void setLineFloat(float[][] data, int line);
    
    public void setLineDouble(double[][] data, int line);
    
    //**********************************************    
    //Asigna un elemento de la matriz
    
    public void setElem(int line, int col, int band, byte data);
    
    public void setElem(int line, int col, int band, short data);
    
    public void setElem(int line, int col, int band, int data);
    
    public void setElem(int line, int col, int band, float data);
    
    public void setElem(int line, int col, int band, double data);
    
    //***********************************************
    //Asigna un elemento a todas la bandas en el buffer pasado por parámetro
    
    public void setElemByte(int line, int col, byte[] data);
    
    public void setElemShort(int line, int col, short[] data);
    
    public void setElemInt(int line, int col, int[] data);

    public void setElemFloat(int line, int col, float[] data);
    
    public void setElemDouble(int line, int col, double[] data);
    
    //***********************************************
    //Inicializa una banda a un valor pasado por parámetro
    
    public void assign(int band, byte value);
    
    public void assign(int band, short value);
    
    public void assign(int band, int value);

    public void assign(int band, float value);
    
    public void assign(int band, double value);
    
    //***********************************************
   
    /**
     * Crea un buffer banda inicializado con el valor pasado por parámetro. Las dimensiones
     * corresponden a las del buffer existente.
     * @param defaultValue Valor con el que se inicializa la banda creada
     */
    public IBand createBand(byte defaultValue);
    
    /**
     * Replica la banda de una posición sobre otra. Si la banda de destino no existe
     * se crea nueva. Si la posición de la banda de destino está intercalada entre bandas 
     * que ya existen las otras se desplazan hacia abajo, NO se machacan los datos de ninguna.
     * Los datos se replican por referencia por lo que al modificar la banda original las
     * del resto quedarán afectadas.   
     * @param orig. Posición de la banda de origen. 
     * @param dest. Posición de la banda destino
     */   
    public void replicateBand(int orig, int dest);
    
    /**
     * Clona el buffer actual y devuelve el clone
     * @return Buffer clonado
     */
    public IBuffer cloneBuffer();
    
    /**
     * Intercambia dos bandas.
     * @param band1 Banda 1 a intercambiar 
     * @param band2 Banda 2 a intercambiar
     */
    public void interchangeBands(int band1, int band2);
    
    /**
     * Intercambia la posición de las bandas. La nueva posición viene dada por el vector
     * pasado por parámetro. Cada posición del vector es una banda del buffer y el contenido de 
     * esa posición es la banda que se dibujará sobre ese buffer.
	 * <P> 
     * Por ejemplo un array con los valores:
     * [2, 0, 1] significa que la banda que ocupa ahora la posición 2 pasará a ocupar la 0, la que 
     * tiene la posición 0 pasa a ocupar la 1 y la que tiene la posición 1 pasa a ocupar la 2.
     * </P>
     * @param bands  Array con la nueva distribución de bandas
     */
    public void switchBands(int[] bands);
    
    /**
     * Asigna el valor de no valido.
     * @param value
     */
    public void setNotValidValue(double value);
    
    /**
     * Obtiene el valor de no valido.
     * @return value
     */
    public double getNotValidValue();
    
    /**
     * Asigna una banda al valor especificado como no valido. Esta banda es común para todas las bandas
     * del buffer, es decir se asigna por referencia. No tiene el mismo resultado que asignar una banda
     * a un valor fijo.
     * @param iBand Número de banda
     */
    public void assignBandToNotValid(int iBand);
}
