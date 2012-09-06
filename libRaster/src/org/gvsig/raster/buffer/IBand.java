package org.gvsig.raster.buffer;

import org.gvsig.raster.dataset.IBuffer;

/**
 * 
 * @author Nacho Brodin (nachobrodin@gmail.com)
 *
 */
public interface IBand{
    public final static int TYPE_UNDEFINED = IBuffer.TYPE_UNDEFINED;
    public final static int TYPE_BYTE = IBuffer.TYPE_BYTE;
    public final static int TYPE_SHORT = IBuffer.TYPE_SHORT;
    public final static int TYPE_USHORT = IBuffer.TYPE_USHORT;
    public final static int TYPE_INT = IBuffer.TYPE_INT;
    public final static int TYPE_FLOAT = IBuffer.TYPE_FLOAT;
    public final static int TYPE_DOUBLE = IBuffer.TYPE_DOUBLE;
    public final static int TYPE_IMAGE = -1;
    
    /**
     * Ancho de la banda
     * @return Entero con el ancho de la banda
     */
    public int getWidth();

    /**
     * Alto de la banda
     * @return Entero con el alto de la banda
     */
    public int getHeight();
    
    /**
     * Obtiene el tipo de dato. Los tipos de dato posibles están definidos en IRaster.
     * @return tipo de datos
     */
	public int getDataType();
	
	/**
	 * Obtiene una linea de la banda
	 * @return
	 */
	public Object getLine(int line);
	
	/**
	 * Asigna una linea de la banda
	 * @param line Número de línea
	 * @param value Valor representado por un array del tipo de dato correspondiente
	 */
	public void setLine(int line, Object value);
	
	/**
	 * Obtiene el buffer de datos de la banda
	 * @return Array bidimensional del tipo de dato correspondiente si es un buffer en memoria
	 * 
	 */
	public Object getBuf();
	
}