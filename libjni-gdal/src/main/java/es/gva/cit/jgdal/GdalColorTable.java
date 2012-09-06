package es.gva.cit.jgdal;

public class GdalColorTable extends JNIBase{
	
	private native int getColorEntryCountNat(long cPtr);
	private native short[] getColorEntryAsRGBNat(long cPtr, int pos);
	
	
	/**
	 * Constructor de ColorTable pasandole como parámetro la referencia al objeto 
	 * GdalColorTable en C
	 * 
	 * @param cPtr	dirección de memoria del objeto 
	 */
	
	public GdalColorTable(long cPtr){
		this.cPtr=cPtr;
	}
	
	/**
	 * Obtiene
	 * @return
	 * @throws GdalException
	 */
	public int getColorEntryCount() throws GdalException{
		if (cPtr == 0)
	    	throw new GdalException("No se ha podido acceder al archivo.");
		return getColorEntryCountNat(cPtr);
	}
	
	/**
	 * Obtiene la entrada de la tabla de color de la posición pasada por parámetro
	 * y la devuelve en forma de objeto GdalColorEntry.
	 * @param pos Posición de la entrada de la tabla
	 * @return Objeto GdalColorEntry correspondiente a pos
	 * @throws GdalException
	 */
	public GdalColorEntry getColorEntryAsRGB(int pos) throws GdalException{
		if (cPtr == 0)
	    	throw new GdalException("No se ha podido acceder al archivo.");
		
		if ((pos < 0) || (pos >= getColorEntryCount()))
			throw new GdalException("Entrada de la tabla de color fuera de rango");
		
		GdalColorEntry entry = new GdalColorEntry();
		short[] values =  getColorEntryAsRGBNat(cPtr, pos);
		entry.c1 = values[0];
		entry.c2 = values[1];
		entry.c3 = values[2];
		entry.c4 = values[3];
		if(values == null)
			throw new GdalException("Error en getColorEntryAsRGB(). Posición de la tabla de color inexistente.");
		return entry;
	}
}