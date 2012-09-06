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
 * 
 * Project:  JGDAL. Interface java to gdal (Frank Warmerdam).
 */
package es.gva.cit.jgdal;

import java.io.File;
import java.io.IOException;
/**
 * Contiene las funcionalidades necesarias para el acceso a los elementos de un
 * dataset de gdal correspondiente a una imagen
 * 
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class Gdal extends JNIBase {
	// CONSTANTES

	// GDALAccess
	public static int GA_ReadOnly   = 0;
	public static int GA_Update     = 1;

	// GDALDataType
	public static int GDT_Unknown   = 0;
	public static int GDT_Byte      = 1; // Buffer byte (8)
	public static int GDT_UInt16    = 2; // Buffer short (16)
	public static int GDT_Int16     = 3; // Buffer short (16)
	public static int GDT_UInt32    = 4; // Buffer int (32)
	public static int GDT_Int32     = 5; // Buffer int (32)
	public static int GDT_Float32   = 6; // Buffer float (32)
	public static int GDT_Float64   = 7; // Buffer double (64)
	public static int GDT_CInt16    = 8; // Buffer short (16)
	public static int GDT_CInt32    = 9; // Buffer int (32)
	public static int GDT_CFloat32  = 10; // Buffer float (32)
	public static int GDT_CFloat64  = 11; // Buffer double (64)
	public static int GDT_TypeCount = 12;

	private String    pszFilename   = "";

	private native long openNat(String pszFilename, int access);
	private native long openArrayNat(byte[] pszFilename, int access);
	private native int getGeoTransformNat(long cPtr, GeoTransform adfgeotransform);
	private native int setGeoTransformNat(long cPtr, GeoTransform adfgeotransform);
	private native String[] getMetadataNat(long cPtr, String pszDomain);
	private native String getProjectionRefNat(long cPtr);
	private native void closeNat(long cPtr);
	private native int getRasterBandNat(long cPtr, int hBand);
	private native int setProjectionNat(long cPtr, String proj);
	private native String getDriverShortNameNat(long cPtr);
	
	//private static native long getGDALDriverManagerNat(); 
	private static native long getDriverByNameNat(String name);
	private native String getColorInterpretationNameNat(long cPtr, int colorInterp);
	
	/**
	 *Constructor a partir de la dirección de memoria 
	 */
	public Gdal(long cPtr){
		this.cPtr=cPtr;
	}
	
	/**
	 *Constructor generico
	 */
	public Gdal(){}
	
	/**
	 * Devuelve la dirección de memoria del objeto dataset en C.
	 */
	public long getPtro(){return cPtr;}
	
	/**
	 * Abre el fichero de imagen.
	 *
	 * @param pszFilename	Nombre del fichero.
	 * @param access	Apertura en solo lectura o escritura.
	 * @throws GdalException
	 */
	public void open(String pszFilename, int access)throws GdalException, IOException {
		File f = new File( pszFilename );
		if(!f.exists())
			throw new GdalException("El archivo "+pszFilename+" no existe");
			
			if(!f.canRead())
				throw new GdalException("El archivo no puede leerse");
			
			if ((access < 0) || (access > 1))
				throw new GdalException("Tipo de acceso al dataset incorrecto");
	
			cPtr=openArrayNat(pszFilename.getBytes(), access);
			
			if (cPtr == 0)
				throw new GdalException("No se ha podido acceder al archivo.");
	}
	
	/**
	 * Obtiene un array de Strings con los metadatos
	 * 
	 * @throws GdalException
	 * @return Array de Strings que corresponden a los metadatos que ofrece la imagen
	 */
	public String[] getMetadata()throws GdalException {
		return getMetadata(null);
		
	}
	
	
	/**
	 * Obtiene un array de Strings con los metadatos
	 * 
	 * @throws GdalException
	 * @return Array de Strings que corresponden a los metadatos que ofrece la imagen
	 */
	public String[] getMetadata(String domain)throws GdalException {
		String[] res = getMetadataNat(cPtr, domain);
		if(res == null)
			return new String[0];
		else return res;
		
	}
	
	/**
	 * Obtiene el número de bandas de la imagen
	 * 
	 * @throws GdalException
	 * @param hBand	Entero que corresponde al número de banda que se quiere recuperar
	 * @return Objeto GdalRasterBand que representa la banda recuperada	
	 */
	
	public GdalRasterBand getRasterBand(int hBand)throws GdalException {
		long cPtr_rb;
			
		if ((hBand < 1) || (hBand > getRasterCount()))
			throw new GdalException("Banda seleccionada incorrecta");
		
		if (cPtr == 0)
				throw new GdalException("No se ha podido acceder al archivo.");
		
		cPtr_rb = getRasterBandNat(cPtr,hBand);
		
		return new GdalRasterBand(cPtr_rb);	
	}
	
		
	
	/**
	 * Obtiene la dimensión del raster en el eje X.
	 * 
	 * @return	Devuelve un entero con la longitud de la imagen en el eje X en pixels.
	 * @throws GdalException 
	 */
	public int getRasterXSize()throws GdalException {
		String msg1="Error en GDALGetRasterXSize. La llamada GDALOpen no tuvo éxito";
		String msg2="Error en tamaño X";
		return baseSimpleFunctions(5,msg1,msg2);
	}
		
	
	/**
	 * Obtiene la dimensión del raster en el eje Y.
	 * 
	 * @return	Devuelve un entero con la longitud de la imagen en el eje Y en pixels.
	 * @throws GdalException 
	 */
	public int getRasterYSize()throws GdalException {
		String msg1="Error en GDALGetRasterYSize. La llamada GDALOpen no tuvo éxito";
		String msg2="Error en tamaño Y";
		return baseSimpleFunctions(6,msg1,msg2);
	}
	
	
	/**
	 * Obtiene el número de bandas de la imagen
	 * 
	 * @return	Devuelve un entero con el número de bandas que contiene la imagen.
	 * @throws GdalException
	 */
	public int getRasterCount()throws GdalException {
		String msg1="Error en GDALGetRasterCount. . La llamada GDALOpen no tuvo éxito";
		String msg2="Error en el conteo de número de bandas";
		return baseSimpleFunctions(7,msg1,msg2);
	}
	
	
	/**
	 * Obtiene el vector geoTransform de la imagen que contiene los valores Origen y pixelSize
	 * 
	 * @return	Devuelve un vector de doubles que contiene los valores de coordenadas de origen y pixelSize.
	 * @throws GdalException
	 */
	public GeoTransform getGeoTransform()throws GdalException {
		GeoTransform gt=new GeoTransform();
				
		if (cPtr == 0)
				throw new GdalException("No se ha podido acceder al archivo.");
		
		if(getGeoTransformNat(cPtr,gt) < 0)
			throw new GdalException("Error en getGeoTransform(). No se han obtenido valores para geoTransform.");
		else{
			return gt;
		}
	}
	
	/**
	 * Obtiene el Nombre corto asociado al driver del dataset
	 * 
	 * @return	Cadena con el nombre del driver
	 * @throws GdalException
	 */
	public String getDriverShortName()throws GdalException {
		if (cPtr == 0)
				throw new GdalException("No se ha podido acceder al archivo.");
		
		String shortName = getDriverShortNameNat(cPtr);
		
		if(shortName == null)
			throw new GdalException("Error en getDriverShortName(). No ha podido obtenerse el driver");
		else 
			return shortName;
	}
	
	/**
	 * Añade el vector geoTransform a la imagen que contiene los valores Origen y pixelSize
	 * 
	 * @return	Devuelve un vector de doubles que contiene los valores de coordenadas de origen y pixelSize.
	 * @throws GdalException
	 */
	public void setGeoTransform(GeoTransform gt)throws GdalException {
		if (cPtr == 0)
				throw new GdalException("No se ha podido acceder al archivo.");
		if (gt == null)
			throw new GdalException("el objeto " + gt.getClass().getName() + " es null");
		
		int res = setGeoTransformNat(cPtr,gt);
	}
	
	/**
	 * Obtiene el sistema de coordenadas de referencia de la imagen.
	 * 
	 * @return	Devuelve un String con los datos del sistema de coordenadas de referencia.
	 * @throws GdalException
	 */
	public String getProjectionRef()throws GdalException {
		if (cPtr == 0)
				throw new GdalException("No se ha podido acceder al archivo.");
		
		String res = getProjectionRefNat(cPtr);
		
		if(res == null)return new String("");
		else return res;
	}
	
	/**
	 * Cierra el fichero de imagen.
	 *
	 * @throws GdalException 
	 */
	public void close()throws GdalException {
		if (cPtr == 0)
				throw new GdalException("No se ha podido acceder al archivo.");
		
		closeNat(cPtr);	
	}
	
	/**
	 * Obtiene un driver a través de su nombre
	 * 
	 * @param name	Nombre del driver
	 */
	public static GdalDriver getDriverByName(String name)throws GdalException {
		long ptrdrv = -1;
		
		if (name == null)
			throw new GdalException("El nombre del driver es null");
		
		ptrdrv = getDriverByNameNat(name);
		
		return (new GdalDriver(ptrdrv));
	}
	
	
	/**
	 * Obtiene el numero de bandas de la imagen
	 * 
	 * @return	Devuelve un entero con el numero de bandas que contiene la imagen.
	 * @throws GdalException
	 */
	public int getGCPCount()throws GdalException {
		String msg1="Error en GDALGetRasterCount. . La llamada GDALOpen no tuvo éxito";
		String msg2="Error en el conteo de número de bandas";
		return baseSimpleFunctions(8,msg1,msg2);
	}
	
	/**
	 *Asigna la proyección especificada en la cadena que se le pasa por parámetro.
	 *@param proj	proyección
	 *@throws GdalException 
	 */
	public void setProjection(String proj)throws GdalException {
		if (cPtr == 0)
				throw new GdalException("No se ha podido acceder al archivo.");
		if (proj == null)
				throw new GdalException("La proyeccion es null");
		
		int res = setProjectionNat(cPtr, proj);
		
		if(res < 0)
			throw new GdalException("Error en setProjection(). No se ha podido asignar la proyección.");
	}
	
	/**
	 * Obtiene la cadena que representa el tipo de banda de color. Los tipos posibles son:
	 * <UL>
	 *  <LI>0 = "Undefined" </LI>
	 *  <LI>1 = "Gray";</LI>
	 *  <LI>2 = "Palette";</LI>
	 *  <LI>3 = "Red";</LI>
	 *  <LI>4 = "Green";</LI>
	 *  <LI>5 = "Blue";</LI>
	 *  <LI>6 = "Alpha";</LI>
	 *  <LI>7 = "Hue";</LI>
	 *  <LI>8 = "Saturation";</LI>
	 *  <LI>9 = "Lightness";</LI>
	 *  <LI>10 = "Cyan";</LI>
	 *  <LI>11 = "Magenta";</LI>
	 *  <LI>12 = "Yellow";</LI>
	 *  <LI>13 = "Black";</LI>
	 *  <LI>14 = "YCbCr_Y";</LI>
	 *  <LI>15 = "YCbCr_Cb";</LI>
	 *  <LI>16 = "YCbCr_Cr";</LI>
	 * </UL>
	 * @return	Cadena con el nombre del tipo de banda de color
	 * @throws GdalException
	 */
	public String getColorInterpretationName(int colorInterp)throws GdalException {
		if ((colorInterp < 0) || (colorInterp > 16))
			throw new GdalException("Valor de interpretacion de color fuera de rango");
		
		String bandTypeName = getColorInterpretationNameNat(cPtr, colorInterp);
		
		if(bandTypeName == null)
			throw new GdalException("Error en getColorInterpretationName(). No ha podido obtenerse el tipo de banda de color");
		else 
			return bandTypeName;
	}	
	
}
