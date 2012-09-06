package es.gva.cit.jgdal;

import java.util.ArrayList;

/**
 * Clase que recubre la función de reproyección de gdal.
 *  
 * @author Miguel Ángel Querol Carratalá
 */
public class GdalWarp extends JNIBase {
	private int    porcentaje;

	/**
	 * Parámetros de la operación de reproyección
	 */
	private String s_srs = null;

	/**
	 * Método nativo para el warp desde gdal.
	 */
	private native int warpDataset(String s_srs, String t_srs, String source, String dest, String format);

	/**
	 * Constructor generico.
	 */
	public GdalWarp() {}

	/**
	 * Reproyecta una imagen raster, creando una imagen de salida
	 * @param proj EPSG:code o proj4
	 * @param source Ruta del fichero fuente
	 * @param dest Ruta del fichero destino
	 * @param format
	 * @return 0 si ha ocurrido algun error o 1 si la ejecución ha sido correcta.
	 * @throws GdalException
	 */
	public int warp(String t_srs, String source, String dest, String format) throws GdalException{

		int stat = warpDataset(s_srs, t_srs, source, dest, format);
		
		if (stat == 0)
			throw new GdalException("Error al reproyectar");
		
		return stat;
	}

	/**
	 * Indica la proyección del dataset origen
	 * @param s_srs
	 */
	public void setSsrs(String s_srs) {
		this.s_srs = s_srs;
	}

	/**
	 * Obtiene el porcentaje de proceso que se ha completado
	 */
	public int getPercent() {
		return porcentaje;
	}
	
	/**
	 * Devuelve la lista de drivers que usa GdalWarp para reproyectar
	 * @return
	 */
	static public ArrayList getDrivers() {
		ArrayList list = new ArrayList();
		list.add("GTiff");
		list.add("VRT");
		list.add("NITF");
		list.add("HFA");
		list.add("ELAS");
		list.add("MEM");
		list.add("BMP");
		list.add("PCIDSK");
		list.add("ILWIS");
		String os = System.getProperty("os.name");
		if (!os.toLowerCase().startsWith("windows"))
			list.add("HDF4Image");
		list.add("PNM");
		list.add("ENVI");
		list.add("EHdr");
		list.add("PAux");
		list.add("MFF");
		list.add("MFF2");
		list.add("BT");
		list.add("IDA");
		list.add("RMF");
		list.add("RST");
		list.add("Leveller");
		list.add("Terragen");
		list.add("ERS");
		list.add("INGR");
		list.add("GSAG");
		list.add("GSBG");
		list.add("ADRG");
		return list;
	}
}
