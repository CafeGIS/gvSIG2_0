/**********************************************************************
 * $Id: GdalRasterBand.java 15691 2007-10-31 10:49:53Z nbrodin $
 *
 * Name:     GdalRasterBand.java
 * Project:  JGDAL. Interface java to gdal (Frank Warmerdam).
 * Purpose:  Basic Funcions about raster bands. 
 * Author:   Nacho Brodin, brodin_ign@gva.es
 *
 **********************************************************************/
/* gvSIG. Sistema de Informaciï¿½n Geogrï¿½fica de la Generalitat Valenciana
 *
 * Copyright (C) 2004 IVER T.I. and Generalitat Valenciana.
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
 * For more information, contact:
 *
 *  Generalitat Valenciana
 *   Conselleria d'Infraestructures i Transport
 *   Av. Blasco Ibï¿½ï¿½ez, 50
 *   46010 VALENCIA
 *   SPAIN
 *
 *      +34 963862235
 *   gvsig@gva.es
 *      www.gvsig.gva.es
 *
 *    or
 *
 *   IVER T.I. S.A
 *   Salamanca 50
 *   46005 Valencia
 *   Spain
 *
 *   +34 963163400
 *   dac@iver.es
 */

package es.gva.cit.jgdal;

/**
 * Representa a una banda simple de la imï¿½gen o canal.
 * 
 * @author Nacho Brodin <brodin_ign@gva.es>.<BR> Equipo de desarrollo gvSIG.<BR> http://www.gvsig.gva.es
 * @version 0.0
 * @link http://www.gvsig.gva.es
 */

public class GdalRasterBand extends JNIBase{


	private native long getOverviewNat(long cPtr,int i);
	private native long getRasterColorTableNat(long cPtr);
	private native GdalBuffer readRasterNat(long cPtr, 
			int nXOff, int nYOff, int nXSize, int nYSize,
			int BufXSize, int BufYSize,
			int eBufType);
	private native GdalBuffer readRasterWithPaletteNat(long cPtr, 
			int nXOff, int nYOff, int nXSize, int nYSize,
			int BufXSize, int BufYSize,
			int eBufType);
	private native void writeRasterNat(	long cPtr, 
			int nXOff, int nYOff, int nXSize, int nYSize,
			GdalBuffer buffer,
			int eBufType);
	private native double getRasterNoDataValueNat(long cPtr);
	private native int existsNoDataValueNat(long cPtr);
	private native String[] getMetadataNat(long cPtr,String pszDomain);
	private native int getRasterColorInterpretationNat(long cPtr);
	private native int setRasterColorInterpretationNat(long cPtr, int bandType);


	/**
	 * Asigna el identificador de la banda
	 */

	 public GdalRasterBand(long cPtr) {
		 this.cPtr=cPtr;
	 }


	 /**
	  * Lee datos de la banda de la imï¿½gen
	  * 
	  * @return	Devuelve un vector de bytes con el trozo de raster leï¿½do.
	  * @param nXOff	El desplazamiento del pixel desde la esquina superior derecha
	  * de la banda accedida.  
	  * @param nYOff	El desplazamiento de lï¿½nea desde la esquina superior derecha
	  * de la banda accedida. 	
	  * @param nXSize	Ancho de la regiï¿½n en pixels de la banda que serï¿½ accedida
	  * @param nYSize	Altura de la regiï¿½n en lï¿½neas de la banda que serï¿½ accedida
	  * @param bufXSize	Ancho del buffer donde la regiï¿½n de la imï¿½gen serï¿½ guardada
	  * @param bufYSize	Altura del buffer donde la regiï¿½n de la imï¿½gen serï¿½ guardada
	  * En caso de que bufXSize o bufYSize sean menores que 1, pasan a tener el mismo valor que
	  * nXSize y nYSize respectivamente para evitar buffers con tamaño 0. 
	  * @param eBufType		
	  */

	 public GdalBuffer readRaster(int nXOff, int nYOff, int nXSize, int nYSize,
			 int bufXSize, int bufYSize,
			 int eBufType)throws GdalException {
		 
		 if (cPtr == 0)
			 throw new GdalException("No se ha podido acceder al archivo.");

		 if ((nXOff<0) || (nXOff > baseSimpleFunctions(5, "", "")) || (nYOff < 0) || (nYOff > baseSimpleFunctions(6, "", "")))
			 throw new GdalException("Desplazamiento de la ventana fuera de rango.");

		 if ((nXSize < 1) || (nXSize > baseSimpleFunctions(5, "", "")) || (nYSize<1) || (nYSize > baseSimpleFunctions(6, "", "")))
			 throw new GdalException("Tamaño de ventana incorrecto.");

		 if (((nXSize + nXOff) > (baseSimpleFunctions(5, "", ""))) || ((nYSize + nYOff) > (baseSimpleFunctions(6, "", ""))))
			 throw new GdalException("Posicion de la ventana incorrecta.");
		 
		 if ((eBufType < 1) || (eBufType > 11))
			 throw new GdalException("Tipo de datos incorrecto.");
		 
		 if (bufXSize < 1)
			 bufXSize = nXSize;
		 
		 if (bufYSize < 1)
			 bufYSize = nYSize;
		 
		 GdalBuffer buffer = readRasterNat(cPtr, nXOff, nYOff, nXSize, nYSize, bufXSize, bufYSize, eBufType);
		 if(buffer!=null)
			 return buffer;
		 else 
			 return null;
	 }

	 /**
	  * Escribe datos en la banda de la imï¿½gen
	  * 
	  * @param nXOff	El desplazamiento del pixel desde la esquina superior derecha
	  * de la banda accedida.  
	  * @param nYOff	El desplazamiento de lï¿½nea desde la esquina superior derecha
	  * de la banda accedida. 	
	  * @param nXSize	Ancho de la regiï¿½n en pixels de la banda que serï¿½ accedida
	  * @param nYSize	Altura de la regiï¿½n en lï¿½neas de la banda que serï¿½ accedida
	  * @param BufXSize	Ancho del buffer donde la regiï¿½n de la imï¿½gen serï¿½ guardada
	  * @param BufYSize	Altura del buffer donde la regiï¿½n de la imï¿½gen serï¿½ guardada
	  * @param eBufType
	  */

	 public void writeRaster(int nXOff, int nYOff, int nXSize, int nYSize, GdalBuffer buf, int eBufType)throws GdalException{
		 GdalBuffer buffer=new GdalBuffer();
		 
		 if ((nXOff<0) || (nXOff > baseSimpleFunctions(5, "", "")) || (nYOff < 0) || (nYOff > baseSimpleFunctions(6, "", "")))
			 throw new GdalException("Desplazamiento de la ventana fuera de rango.");

		 if ((nXSize < 1) || (nXSize > baseSimpleFunctions(5, "", "")) || (nYSize<1) || (nYSize > baseSimpleFunctions(6, "", "")))
			 throw new GdalException("Tamaño de ventana incorrecto.");

		 if (((nXSize + nXOff) > (baseSimpleFunctions(5, "", ""))) || ((nYSize + nYOff) > (baseSimpleFunctions(6, "", ""))))
			 throw new GdalException("Posicion de la ventana incorrecta.");
		 
		 if ((eBufType < 1) || (eBufType > 11))
			 throw new GdalException("Tipo de datos incorrecto.");
		 
		 if (buf == null)
			 throw new GdalException("Buffer incorrecto");
		 
		 switch(eBufType){
		 case 0:
			 return;
		 case 1:
			 buffer.buffByte=buf.buffByte;
			 break;
		 case 2:
		 case 3:
		 case 8:
			 buffer.buffShort=buf.buffShort;
			 break;
		 case 4:
		 case 5:
		 case 9:
			 buffer.buffInt=buf.buffInt;
			 break;
		 case 6:
		 case 10:
			 buffer.buffFloat=buf.buffFloat;
			 break;
		 case 7:
		 case 11:
			 buffer.buffDouble=buf.buffDouble;
			 break; 		
		 case 12:
			 return;
		 }

		 writeRasterNat(cPtr, nXOff, nYOff, nXSize, nYSize, buffer, eBufType); 	 	
	 }

	 /**
	  *Obtiene el tamaï¿½o en pixeles de la imï¿½gen en el eje de las X
	  *@return Tamaï¿½o en pixeles del eje X
	  *@throws GdalException 
	  */

	 public int getRasterBandXSize()throws GdalException {
		 String msg1="Error en getRasterBandXSize(). La llamada getRasterBand no tuvo exito";
		 String msg2="Tamaï¿½o de banda erroneo devuelto por GetRasterBandXSize";

		 return baseSimpleFunctions(0,msg1,msg2);
	 }

	 /**
	  *Obtiene el tamaï¿½o en pixeles de la imï¿½gen en el eje de las Y
	  *@return Tamaï¿½o en pixeles del eje Y
	  *@throws GdalException 
	  */

	 public int getRasterBandYSize()throws GdalException {
		 String msg1="Error en getRasterBandYSize(). La llamada getRasterBand no tuvo exito";
		 String msg2="Tamaï¿½o de banda erroneo devuelto por GetRasterBandYSize";

		 return baseSimpleFunctions(1,msg1,msg2);
	 }


	 /**
	  * Devuelve el nï¿½mero de overviews que contiene la banda.
	  * @return Nï¿½mero de overviews
	  * @throws GdalException 
	  */

	 public int getOverviewCount()throws GdalException {
		 String msg1="Error en getOverviewCount(). La llamada getRasterBand no tuvo exito";		
		 String msg2="Error al obtener el nï¿½mero de overviews";

		 return baseSimpleFunctions(2,msg1,msg2);
	 }


	 /**
	  * Obtiene el overview indicado por el ï¿½ndice "i".
	  * 
	  * @param i	indice del overview que se quiere recuperar.
	  * @return GdalRasterBand	Banda correspondiente al overview selecccionado
	  * @throws GdalException 
	  */

	 public GdalRasterBand getOverview(int i)throws GdalException {
		 long cPtr_ov;

		 if((i < 0) || (i >= getOverviewCount()))
			 throw new GdalException("El overview seleccionado no existe");

		 cPtr_ov = getOverviewNat(cPtr,i);
		 
		 if (cPtr_ov == 0)
			 throw new GdalException("No se ha podido obtener el overview");

		 return new GdalRasterBand(cPtr_ov);
	 }


	 /**
	  * Devuelve el tamaï¿½o en X del bloque para esa banda
	  * @return Tamaï¿½o en pixeles del bloque en el eje X
	  * @throws GdalException 
	  */

	 public int getBlockXSize()throws GdalException {
		 String msg1="Error en getBlockXSize(). La llamada getRasterBand no tuvo exito";
		 String msg2="Tamaï¿½o de bloque erroneo devuelto por GetBlockXSize";

		 return baseSimpleFunctions(3,msg1,msg2);
	 }


	 /**
	  * Devuelve el tamaï¿½o en Y del bloque para esa banda
	  * @return Tamaï¿½o en pixeles del bloque en el eje Y
	  * @throws GdalException 
	  */

	 public int getBlockYSize()throws GdalException {
		 String msg1="Error en getBlockXSize(). La llamada getRasterBand no tuvo exito";
		 String msg2="Tamaï¿½o de bloque erroneo devuelto por GetBlockYSize";

		 return baseSimpleFunctions(4,msg1,msg2);
	 }

	 /**
	  * Devuelve el tipo de datos de la banda
	  * @return Tamaï¿½o en pixeles del bloque en el eje Y
	  * @throws GdalException 
	  */

	 public int getRasterDataType()throws GdalException {
		 String msg1="Error en getRasterDataType(). La llamada getRasterBand no tuvo exito";
		 String msg2="Tipo de dato devuelto por GetRasterDataType erroneo";

		 return baseSimpleFunctions(9,msg1,msg2);
	 }

	 /**
	  * Obtiene la tabla de color asociada a la imagen
	  */
	 public GdalColorTable getRasterColorTable()throws GdalException {
		 GdalColorTable gct = null;
		 
		 if (cPtr == 0)
			 throw new GdalException("No se ha podido acceder al archivo.");
		 
		 long cPtr_ct = getRasterColorTableNat(cPtr);
		 
		 if ((cPtr_ct == 0) || (cPtr_ct == -1))
			 return null;
		 
		 gct = new GdalColorTable(cPtr_ct);

		 return gct;
	 }

	 /**
	  * Lee datos de la banda de la imï¿½gen con una paleta asociada
	  * 
	  * @return	Devuelve un vector de bytes con el trozo de raster leï¿½do.
	  * @param nXOff	El desplazamiento del pixel desde la esquina superior derecha
	  * de la banda accedida.  
	  * @param nYOff	El desplazamiento de lï¿½nea desde la esquina superior derecha
	  * de la banda accedida. 	
	  * @param nXSize	Ancho de la regiï¿½n en pixels de la banda que serï¿½ accedida
	  * @param nYSize	Altura de la regiï¿½n en lï¿½neas de la banda que serï¿½ accedida
	  * @param BufXSize	Ancho del buffer donde la regiï¿½n de la imï¿½gen serï¿½ guardada
	  * @param BufYSize	Altura del buffer donde la regiï¿½n de la imï¿½gen serï¿½ guardada
	  * En caso de que bufXSize o bufYSize sean menores que 1, pasan a tener el mismo valor que
	  * nXSize y nYSize respectivamente para evitar buffers con tamaño 0. 
	  * @param eBufType		
	  */

	 public GdalBuffer readRasterWithPalette(int nXOff, int nYOff, int nXSize, int nYSize,
			 int bufXSize, int bufYSize,
			 int eBufType)throws GdalException {
		 
		 if (cPtr == 0)
			 throw new GdalException("No se ha podido acceder al archivo.");
		 
		 if ((nXOff<0) || (nXOff > baseSimpleFunctions(5, "", "")) || (nYOff < 0) || (nYOff > baseSimpleFunctions(6, "", "")))
			 throw new GdalException("Desplazamiento de la ventana fuera de rango.");

		 if ((nXSize < 1) || (nXSize > baseSimpleFunctions(5, "", "")) || (nYSize<1) || (nYSize > baseSimpleFunctions(6, "", "")))
			 throw new GdalException("Tamaño de ventana incorrecto.");

		 if (((nXSize + nXOff) > (baseSimpleFunctions(5, "", ""))) || ((nYSize + nYOff) > (baseSimpleFunctions(6, "", ""))))
			 throw new GdalException("Posicion de la ventana incorrecta.");
		 
		 if ((eBufType < 1) || (eBufType > 11))
			 throw new GdalException("Tipo de datos incorrecto.");
		 
		 if (bufXSize < 1)
			 bufXSize = nXSize;
		 
		 if (bufYSize < 1)
			 bufYSize = nYSize;
		 
		 
		 GdalBuffer buffer = readRasterWithPaletteNat(cPtr, nXOff, nYOff, nXSize, nYSize, bufXSize, bufYSize, eBufType);


		 if(buffer!=null)
			 return buffer;
		 else 
			 return null;
	 }

	 /**
	  *Devuelve el valor de NoData
	  */
	 public double getRasterNoDataValue()throws GdalException {
		 if (cPtr == 0)
			 throw new GdalException("No se ha podido acceder al archivo.");
		 
		 return getRasterNoDataValueNat(cPtr);
	 }
	 
	 /**
	  * Obtiene el valorDevuelve el valor de NoData
	  */
	 public boolean existsNoDataValue()throws GdalException {
		 if (cPtr == 0)
			 throw new GdalException("No se ha podido acceder al archivo.");
		 
		 int result = existsNoDataValueNat(cPtr);
		 
		 if(result == 1)
			 return true;
		 else 
			 return false;
	 }

	 /**
	  * Obtiene un array de Strings con los metadatos
	  * 
	  * @throws GdalException
	  * @return Array de Strings que corresponden a los metadatos que ofrece la imï¿½gen
	  */

	 public String[] getMetadata()throws GdalException {
		 if (cPtr == 0)
			 throw new GdalException("No se ha podido acceder al archivo.");
		 
		 String[] res = getMetadataNat(cPtr,null);
		 if(res == null)
			 return new String[0];
		 else return res;
	 }

	 /**
	  * Obtiene identificador que representa el tipo de banda de color. 
	  * @return	identificador del tipo de banda de color
	  * @throws GdalException
	  */

	 public int getRasterColorInterpretation()throws GdalException {
		 if (cPtr == 0)
			 throw new GdalException("No se ha podido acceder al archivo.");

		 int bandType = getRasterColorInterpretationNat(cPtr);
		 return bandType;		
	 }


	 /**
	  * Asigna la interpretación de color de la banda.
	  * Con algunos formatos no es posible modificar la interpretación de color, 
	  * tales como tiff y jpg. En el caso de tif, no hay error pero tampoco se
	  * produce el cambio en la interpretación. En el caso de jpg, gdal lanza un error.
	  * 0 = "Undefined"
	  * 1 = "Gray";
	  * 2 = "Palette";
	  * 3 = "Red";
	  * 4 = "Green";
	  * 5 = "Blue";
	  * 6 = "Alpha";
	  * 7 = "Hue";
	  * 8 = "Saturation";
	  * 9 = "Lightness";
	  * 10 = "Cyan";
	  * 11 = "Magenta";
	  * 12 = "Yellow";
	  * 13 = "Black";
	  * 14 = "YCbCr_Y";
	  * 15 = "YCbCr_Cb";
	  * 16 = "YCbCr_Cr";
	  * @param bandType
	  * @throws GdalException
	  */
	 public void setRasterColorInterpretation(int bandType) throws GdalException{
		 if (cPtr == 0)
			 throw new GdalException("No se ha podido acceder al archivo.");
		 
		 if ((bandType < 0) || (bandType > 16)){
			 throw new GdalException("Tipo de banda incorrecto");
		 }
		 
		 int err = setRasterColorInterpretationNat(cPtr, bandType);
		 
	 }

}