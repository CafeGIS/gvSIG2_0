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
package es.gva.cit.jgdal;

import java.io.File;
import java.util.StringTokenizer;
/**
 * Representa un driver de un tipo de imágen 
 * 
 * @author Nacho Brodin <brodin_ign@gva.es>.<BR> Equipo de desarrollo gvSIG.<BR> http://www.gvsig.gva.es
 * @version 0.0
 * @link http://www.gvsig.gva.es
 */
public class GdalDriver extends JNIBase {
	private native long createCopyNat(long cPtr, String file, long src, int bstrict);
	private native long createCopyParamsNat(long cPtr, String file, long src, int bstrict, Options opc);
	private native long createNat(long cPtr, String filename, int nXSize, int nYSize, int nBands, int nType, Options opc);
	
	/**
	 * Constructor de Driver pasandole como parámetro la referencia al objeto 
	 * GdalDriver en C
	 * 
	 * @param cPtr	dirección de memoria del objeto 
	 */
	public GdalDriver(long cPtr) {
		this.cPtr=cPtr;
	}

	/**
	 * Crea una copia de una imágen a partir de un dataset de origen especificado.
	 * @param file	Nombre del fichero sobre el cual se guardará la copia
	 * @param src	Dataset fuente a copiar
	 * @param bstrict	TRUE si la copia debe ser estrictamente equivalente y FALSE indica que la copia puede
	 * adaptarse a las necesidades del formato de salida
	 * @return Gdal	Dataset de la imágen de salida
	 * @throws GdalException
	 */
	public Gdal createCopy(String file, Gdal src, boolean bstrict) throws GdalException {
		String path = file.substring(0, file.lastIndexOf(File.separator));
		File f = new File(path);
		if (!f.canWrite())
			throw new GdalException("Ruta de archivo incorrecta.");
		f = null;

		if (src == null)
			throw new GdalException("El objeto Gdal es null");

		if (cPtr == 0)
			throw new GdalException("No se ha podido acceder al archivo.");

		long ptr = createCopyNat(cPtr, file, src.getPtro(), bstrict ? 1 : 0);
	
//		if (ptr == 0)
//			throw new GdalException("No se ha podido crear la copia");

		return (new Gdal(ptr));
	}
	
	
	/**
	 * A partir de las opciones en forma de vector de Strings pasadas por el usuario donde cada
	 * elemento del vector tiene la forma VARIABLE=VALOR crea el objeto Options para que sea accesible
	 * a las funciones JNI desde C.
	 * @param params	Vector de strigs con las opciones
	 * @return Options	Objeto de opciones
	 */
	private Options selectOptions(String[] params) {
		if (params == null)
			return null;

		Options opc = new Options(params.length);
		StringTokenizer st;
		for (int i = 0; i < params.length; i++) {
			st = new StringTokenizer(params[i], "=");
			String var = st.nextToken();
			String dato = st.nextToken();
			opc.addOption(var, dato);
		}
		return opc;
	}
	
	/**
	 * Crea una copia de una imágen a partir de un dataset de origen especificado y unos parámetros dados.
	 * @param file	Nombre del fichero sobre el cual se guardará la copia
	 * @param src	Dataset fuente a copiar
	 * @param bstrict	TRUE si la copia debe ser estrictamente equivalente y FALSE indica que la copia puede
	 * adaptarse a las necesidades del formato de salida
	 * @param params	Vector de strigs con las opciones de la copia
	 * @return Gdal	Dataset de la imágen de salida
	 * @throws GdalException
	 */
	public Gdal createCopy(String file, Gdal src, boolean bstrict, String[] params) throws GdalException {
		String path = file.substring(0, file.lastIndexOf(File.separator));
		File f = new File(path);
		if (!f.canWrite())
			throw new GdalException("Ruta de archivo incorrecta.");
		f = null;

		if (cPtr == 0)
			throw new GdalException("No se ha podido acceder al archivo.");

		if (src == null)
			throw new GdalException("El objeto Gdal es null");

		long ptr = createCopyParamsNat(cPtr, file, src.getPtro(), bstrict ? 1 : 0, selectOptions(params));

		if (ptr == 0)
			throw new GdalException("No se ha podido crear la copia");

		return (new Gdal(ptr));
	}
	
	
	/**
	 * Crea un nuevo dataset con el driver actual
	 * 
	 * @param filename	Nombre del dataset a crear
	 * @param nXSize	Ancho en pixels
	 * @param nYSize	Alto en pixels
	 * @param nBands	Número de bandas
	 * @param nType	Tipo de raster
	 * @param params	lista de parámetros especificos del driver
	 */
	public Gdal create(String filename, int nXSize, int nYSize, int nBands, int nType, String[] params) throws GdalException {
		if (cPtr == 0)
			throw new GdalException("No se ha podido acceder al archivo.");

		long ptr = createNat(cPtr, filename, nXSize, nYSize, nBands, nType, selectOptions(params));

		return (new Gdal(ptr));
	}
}