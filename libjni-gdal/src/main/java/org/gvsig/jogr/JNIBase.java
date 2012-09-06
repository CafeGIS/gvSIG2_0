/**********************************************************************
 * $Id: JNIBase.java 7765 2006-10-03 07:05:18Z nacho $
 *
 * Name:     JNIBase.java
 * Project:  JGDAL. Interfaz java to gdal (Frank Warmerdam).
 * Purpose:  Base class for classes that use JNI.
 * Author:   Nacho Brodin, brodin_ign@gva.es
 *
 **********************************************************************/
/*Copyright (C) 2004  Nacho Brodin <brodin_ign@gva.es>

 This program is free software; you can redistribute it and/or
 modify it under the terms of the GNU General Public License
 as published by the Free Software Foundation; either version 2
 of the License, or (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */


/**
 * @author Nacho Brodin <brodin_ign@gva.es>.<BR> Equipo de desarrollo gvSIG.<BR> http://www.gvsig.gva.es
 * @version 0.0
 * @link http://www.gvsig.gva.es
 */

package org.gvsig.jogr;



public class JNIBase{
	
	protected long cPtr;
	

	private native int getDriverCountNat(long cPtr);
	private native int getLayerCountNat(long cPtr);
	private native int getFeatureCountNat(long cPtr);
	private native int getFieldCountNat(long cPtr);
	private native int getWidthNat(long cPtr);
	private native int getPrecisionNat(long cPtr);
	
	private native int referenceNat(long cPtr);
	private native int dereferenceNat(long cPtr);
	private native int getRefCountNat(long cPtr);
	private native int getSummaryRefCountNat(long cPtr);
	
	private native int referenceFeatureDefnNat(long cPtr);
	private native int dereferenceFeatureDefnNat(long cPtr);
	private native int getReferenceCountNat(long cPtr);
	
	private native int getFieldCountFeatureNat(long cPtr);
	
	//private native int syncToDiskNat(long cPtr);//Excepciones
	//private native int startTransactionNat(long cPtr);//Excepciones
	//private native int commitTransactionNat(long cPtr);//Excepciones
	//private native int rollbackTransactionNat(long cPtr);//Excepciones
	private native int referenceLayerNat(long cPtr);
	private native int dereferenceLayerNat(long cPtr);
	private native int getRefCountLayerNat(long cPtr);
	
	private native int getOpenDSCountNat(long cPtr);
	
	//Geometry
	private native int getDimensionNat( long cPtr);
	private native int isSimpleNat( long cPtr );
	private native int wkbSizeNat( long cPtr );
	
	//Point
	private native int isEmptyPointNat( long cPtr ); //return OGRBoolean
	private native int getCoordinateDimensionPointNat( long cPtr );
	
	//Line String
	private native int isEmptyLineStringNat( long cPtr ); //return OGRBoolean
	private native int getCoordinateDimensionLineStringNat( long cPtr );
	private native int getNumPointsNat( long cPtr );
	
	//Linear Ring
	private native int isClockwiseNat(long cPtr);
	private native int WkbSizeNat(long cPtr);
	
	//Polygon
	private native int getNumInteriorRingsNat( long cPtr );
	private native int wkbSizePolygonNat( long cPtr );
	private native int getDimensionPolygonNat( long cPtr );
	private native int getCoordinateDimensionNat( long cPtr );
	
	//GeometryCollection
	private native int getDimensionGeometryCollectionNat( long cPtr );
	private native int getCoordinateDimensionGeometryCollectionNat( long cPtr );
	
	 /**
	 * Función que sirve como base para funcionalidades de ogr que admiten como parámetro un long y devuelven un entero.
	 * 
	 * @throws OGRException.
	 * @param msg1	Mensaje de error que se muestra cuando el puntero a objeto pasado es vacio.
	 * @param msg2	Mensaje de error que se muestra cuando el resultado de la llamada a la función de gdal es menor o igual que 0.
	 */
	 
	 
	protected int baseSimpleFunctions(int n,String msg1,String msg2)throws OGRException{
			
		int res = 0;
		if(cPtr <= 0)
			throw new OGRException(msg1);
			
		switch(n){
			case 0: res = getDriverCountNat(cPtr);break;
			case 1: res = getLayerCountNat(cPtr);break;
			case 2: res = getFeatureCountNat(cPtr);break;
			case 3: res = getFieldCountNat(cPtr);break;
			case 4: res = getWidthNat(cPtr);break;
			case 5: res = getPrecisionNat(cPtr);break;
			case 6: res = referenceNat(cPtr);break;
			case 7: res = dereferenceNat(cPtr);break;
			case 8: res = getRefCountNat(cPtr);break;
			case 9: res = getSummaryRefCountNat(cPtr);break;
			case 10: res = referenceFeatureDefnNat(cPtr);break;
			case 11: res = dereferenceFeatureDefnNat(cPtr);break;
			case 12: res = getReferenceCountNat(cPtr);break;
			case 13: res = getFieldCountFeatureNat(cPtr);break;
			//case 14: res = syncToDiskNat(cPtr);break;//Excepciones
			//case 15: res = startTransactionNat( cPtr);break;//Excepciones
			//case 16: res = commitTransactionNat( cPtr);break;//Excepciones
			//case 17: res = rollbackTransactionNat( cPtr);break;//Excepciones
			case 18: res = referenceLayerNat( cPtr);break;
			case 19: res = dereferenceLayerNat( cPtr);break;
			case 20: res = getRefCountLayerNat( cPtr);break;
			case 21: res = getOpenDSCountNat(cPtr);break;
			case 22: res = getDimensionNat(cPtr);break;
			case 23: res = isSimpleNat(cPtr );break;
			case 24: res = wkbSizeNat(cPtr );break;
			case 25: res = isEmptyPointNat( cPtr );break; //return OGRBoolean
			case 26: res = getCoordinateDimensionPointNat( cPtr );break;
			case 27: res = getNumPointsNat( cPtr );break;
			case 28: res = isEmptyLineStringNat( cPtr );break; //return OGRBoolean
			case 29: res = getCoordinateDimensionLineStringNat( cPtr );break;
			case 30: res = isClockwiseNat(cPtr);break;
			case 31: res = WkbSizeNat(cPtr);break;
			case 32: res = getNumInteriorRingsNat( cPtr );break;
			case 33: res = wkbSizeNat( cPtr );break;
			case 34: res = getDimensionNat( cPtr );break;
			case 35: res = getCoordinateDimensionNat( cPtr );break;
		}
			
		if(res<0)
		 	throw new OGRException(msg2);
		else return res;
	}
	
	/**
	 * Lanza una excepción dependiendo del tipo de error pasado
	 * @param err
	 * @param mensaje
	 * @throws OGRCorruptDataException
	 * @throws OGRFailureException
	 * @throws OGRNotEnoughDataException
	 * @throws OGRNotEnoughMemoryException
	 * @throws OGRUnsupportedGeometryTypeException
	 * @throws OGRUnsupportedOperationException
	 * @throws OGRUnsupportedSRSException
	 */
	protected void throwException(int err, String mensaje)
		throws OGRCorruptDataException, OGRFailureException, OGRNotEnoughDataException, OGRNotEnoughMemoryException, OGRUnsupportedGeometryTypeException, OGRUnsupportedOperationException, OGRUnsupportedSRSException{
		
		switch(err){
			case 1:throw new OGRNotEnoughDataException(mensaje);
			case 2:throw new OGRNotEnoughMemoryException(mensaje);
			case 3:throw new OGRUnsupportedGeometryTypeException(mensaje);
			case 4:throw new OGRUnsupportedOperationException(mensaje);
			case 5:throw new OGRCorruptDataException(mensaje);
			case 6:throw new OGRFailureException(mensaje);
			case 7:throw new OGRUnsupportedSRSException(mensaje);
			default : return;
		}
	}
	
	/**
	 * Obtiene el puntero a C del objeto
	 * @param cPtr long con la dirección de memoria del objeto
	 */
	public long getPtro() {
		return cPtr;
	}

	static {
		System.loadLibrary("jgdal2.0.0");
	}
}
