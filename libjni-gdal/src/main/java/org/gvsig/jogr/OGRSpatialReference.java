/**********************************************************************
 * $Id: OGRSpatialReference.java 15690 2007-10-31 10:28:41Z nbrodin $
 *
 * Name:     OGRSpatialReference.java
 * Project:  JGDAL. Interface java to gdal (Frank Warmerdam).
 * Purpose:   
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

package org.gvsig.jogr;



//import es.gva.cit.wrappergdal.OGRException;

/** 
 * Representa un sistema de referencia espacial OpenGIS.
 * 
 * @author Nacho Brodin <brodin_ign@gva.es>.<BR> Equipo de desarrollo gvSIG.<BR> http://www.gvsig.gva.es
 * @version 0.0
 * @link http://www.gvsig.gva.es
 */

public class OGRSpatialReference extends JNIBase {
	
	//private native int setUTMNat(long cPtr, int zona, int norte_sur);
	//private native int setWellKnownGeogCSNat(long cPtr, String cs);
	//private native long OGRSpatialReferenceNat();
	//private native String exportToWktNat(long cPtr);
	private native void OSRDestroySpatialReferenceNat(long cPtr);
	private native int getUTMZoneNat(long cPtr);
	private native int getHemisphereNat(long cPtr);
	private native int setFromUserInputNat(long cPtr, String a_srs);
	
	private native long OGRSpatialReferenceNat() throws CrsGdalException;
	private native void OGRDestroySpatialReferenceNat(long crs);
	
	protected static native String exportToProj4Nat(long cPtr);
	protected static native String exportToWktNat(long cPtr);
	protected static native int importFromWktNat(long cPtr, String cadenas);
	protected static native int setUTMNat(long cPtr, int zona, int norte_sur);
	protected static native int setWellKnownGeogCSNat(long cPtr, String cs);
	protected static native int importFromEPSGNat(long cPtr, int cod);
	protected static native int importFromProj4Nat(long cPtr, String cs);
	protected static native int importFromPCINat(long cPtr,String cod, String cs,double[] coord);
	protected static native int importFromUSGSNat(long cPtr, long code, long zone, double[] params, long datum);
	protected static native int importFromESRINat(long cPtr, String cadenas);
	protected static native boolean isGeographicNat(long cPtr);
	protected static native String getAuthorityCodeNat(long cPtr, String targetKey);
	protected static native String getAuthorityNameNat(long cPtr, String targetKey);
	
	/**
	 *Constructor a partir de la dirección de memoria 
	 */
	
	public OGRSpatialReference(long cPtr){
		this.cPtr=cPtr;
	}
	
	/**
	 * Constructor generico 
	 */
	
	public OGRSpatialReference(){
		try{
			cPtr = OGRSpatialReferenceNat();
		}catch(CrsGdalException exp){
			System.out.println(exp.getStrError());
		}
	}
	
	/**
	 * Escribe la zona UTM pasada por parámetro
	 * @param zona	Zona UTM
	 * @param norte_sur	TRUE hemisferio norte y FALSE hemisferio sur
	 * @throws OGRException
	 */
	
	public void setUTM(int zona, boolean norte_sur)throws OGRException {
		int ns=-1;
		if(norte_sur)ns=1;
		else ns=0;
		
		if(cPtr == 0)
			throw new OGRException("Fallo al acceder al dato.");
		
		if ((zona <= 0) || (zona > 60))
			throw new OGRException("Zona incorrecta");
		
		int res = setUTMNat(cPtr, zona, ns);
		
		if(res < 0)
			throw new OGRException("Error en setUTM(). No se ha podido asignar la zona especificada.");
	}
	
	/**
	 * Lee la zona UTM 
	 * @return Zona UTM
	 * @throws OGRException
	 */
	
	public int getUTMZone()throws OGRException {
		if(cPtr == 0)
			throw new OGRException("Fallo al acceder al dato.");
		
		int res = getUTMZoneNat(cPtr);
		
		if(res < 0)
			throw new OGRException("Error en getUTMZone(). Valor de zona devuelto erroneo.");
		
		return res;	
	}
	
	/**
	 * Obtiene el hemisferio
	 * @return TRUE hemisferio norte y FALSE hemisferio sur
	 * @throws OGRException
	 */
	
	public boolean getHemisphere()throws OGRException {
		if(cPtr == 0)
			throw new OGRException("Fallo al acceder al dato.");
		
		int hemis = getHemisphereNat(cPtr);
		 			
		if(hemis < 0)
			throw new OGRException("Error en getHemisphere(). Valor de hemisferio devuelto erroneo.");
		
		if(hemis == 1)return true;
		else return false;
	}
	
	/**
	 *Asigna las coordenadas geográficas
	 *@param cs	Coordenadas geograficas soportadas:<BR>
	 *<UL>
	 *<LI>WGS84</LI>
	 *<LI>WGS72</LI>
	 *<LI>NAD27</LI>
	 *<LI>NAD83</LI>
	 *<LI>EPSG:n</LI>
	 *</UL>
	 *@throws OGRException
	 */
	
	public void setWellKnownGeogCS(String cs)throws OGRException {
		if(cPtr == 0)
			throw new OGRException("Fallo al acceder al dato.");
		
		if ((cs == null) || (cs.equals("")))
			throw new OGRException("Parametro incorrecto en OGRSpatialReference.setWllKnownGeoCS");
		
		int res = setWellKnownGeogCSNat(cPtr, cs);
		
		if(res < 0)
			throw new OGRException("Error en setWellKnownGeoCS(). No se ha podido asignar el sistema de coordenadas especificado.");
	}
	
	/**
	 * Devuelve la codificación WKT
	 * @return Codificación WKT
	 * @throws OGRException
	 */
	public String exportToWkt()throws OGRException {
		if(cPtr == 0)
			throw new OGRException("Fallo al acceder al dato.");
		
		String wkt = exportToWktNat(cPtr);
		
		if(wkt == null)
			throw new OGRException("Error en exportToWkt(). No se ha podido obtener la proyección.");
		
		return wkt;
	}
	
	/**
	 * @throws OGRException
	 * @param a_srs	
	 */
	public void setFromUserInput(String a_srs)throws OGRException {
		if(cPtr == 0)
			throw new OGRException("Fallo al acceder al dato.");
		
		if ((a_srs == null) || (a_srs.equals("")))
			throw new OGRException("Parametro incorrecto en OGRSpatialReference.setFromUserInput");
		
		int ogrerr = setFromUserInputNat(cPtr,a_srs);
		throwException(ogrerr,"Error en setFromUserInput().");
	}
	
	/**
	 * Destructor 
	 */
	protected void finalize() throws OGRFailureException{
		if(cPtr == 0)
			throw new OGRFailureException("Fallo al acceder al dato.");
		
		OSRDestroySpatialReferenceNat(cPtr);
	}
	
	/**
	 * Consulta si la fuente de datos está en coordenas geograficas o no
	 * @return true si está en coordenadas geográficas
	 */
	public boolean isGeographic() throws CrsGdalException {
		if(cPtr == 0)
			throw new CrsGdalException();
		return isGeographicNat(cPtr);
	}
	
	/**
	 * Obtiene el código EPSG
	 * @param targetKey
	 * @return "PROJCS", "GEOGCS", "GEOGCS|UNIT" or NULL
	 * @throws CrsGdalException
	 */
	public String getAuthorityCode(String targetKey) throws CrsGdalException {
		if(cPtr == 0)
			throw new CrsGdalException();
		
		if ((targetKey == null) || (targetKey.equals("")))
			throw new CrsGdalException();
		
		return getAuthorityCodeNat(cPtr, targetKey);
	}
	
	/**
	 * Obtiene el autority name. Tipicamente EPSG
	 * @param targetKey
	 * @return "PROJCS", "GEOGCS", "GEOGCS|UNIT" or NULL
	 * @throws CrsGdalException
	 */
	public String getAuthorityName(String targetKey) throws CrsGdalException {
		if(cPtr == 0)
			throw new CrsGdalException();
		
		if ((targetKey == null) || (targetKey.equals("")))
			throw new CrsGdalException();
		
		return getAuthorityNameNat(cPtr, targetKey);
	}
	
	
	
	/**
	 * 
	 * @param ORGSpace
	 * @return
	 * @throws CrsGdalException
	 */
	public static String exportToWkt(OGRSpatialReference ORGSpace) throws CrsGdalException{
		if (ORGSpace.cPtr == 0)
			throw new CrsGdalException();
		
		return exportToWktNat(ORGSpace.cPtr);
	}
	
	
	/**
	 * 
	 * @param ORGSpace
	 * @param cadenas
	 * @return
	 * @throws OGRException
	 */
	public static int importFromWkt(OGRSpatialReference ORGSpace, String cadenas) throws OGRException {
		if ((cadenas == null) || (cadenas.equals("")))
			throw new OGRException("Parametro incorrecto");
		
		if (ORGSpace.cPtr == 0)
			throw new OGRException("Parametro incorrecto");
		
		int result = importFromWktNat(ORGSpace.cPtr, cadenas);
		if(result != 0) 
			throw new OGRException(result, "Error en Wkt().La creacion del objeto no tuvo exito. ", ORGSpace);
		return result;
	}
	
	
	/**
	 * 
	 * @param ORGSpace
	 * @param zona
	 * @param norte_sur
	 * @return
	 * @throws OGRException
	 */
	public static int setUTM(OGRSpatialReference ORGSpace ,int zona, boolean norte_sur)throws OGRException {
		if ((zona <= 0) || (zona > 60))
			throw new OGRException("Zona incorrecta");
		
		if(ORGSpace.cPtr == 0)
			throw new OGRException("Error en setUTM(). La llamada de creación de objeto no tuvo exito.");
		
		int ns=-1;
		if(norte_sur)ns=1;
		else ns=0;
		
		int res = setUTMNat(ORGSpace.cPtr, zona, ns);
		
		if(res != 0)
			throw new OGRException(res,"Error en setUTM(). No se ha podido asignar la zona especificada. ",ORGSpace);
		return res;
	}
	
	
	/**
	 * 
	 * @param ORGSpace
	 * @param cs
	 * @return
	 * @throws OGRException
	 */
	public static int setWellKnownGeogCS(OGRSpatialReference ORGSpace, String cs)throws OGRException {
		if ((cs == null) || (cs.equals("")))
			throw new OGRException("Parametro incorrecto");
		
		if(ORGSpace.cPtr == 0)
			throw new OGRException("Error en setWellKnownGeogCS(). La llamada de creación de objeto no tuvo exito.");
		
		int res = setWellKnownGeogCSNat(ORGSpace.cPtr, cs);
		
		if(res != 0)
			throw new OGRException(res,"Error en setWellKnownGeoCS(). No se ha podido asignar el sistema de coordenadas especificado. ",ORGSpace);
		return res;
	}
	
	
	/**
	 * 
	 * @param ORGSpace
	 * @param cod
	 * @return
	 * @throws OGRException
	 */
	public static int importFromEPSG(OGRSpatialReference ORGSpace, int cod) throws OGRException {
		if(ORGSpace.cPtr == 0)
			throw new OGRException("Error importFromEPSG. La llamada de creación de objeto no tuvo exito.");
		
		int result = importFromEPSGNat(ORGSpace.cPtr, cod);
		
		if(result != 0) 
			throw new OGRException(result,"Error en EPSG().La creacion del objeto no tuvo exito. ",ORGSpace);

		return result;
	}
	
	
	/**
	 * 
	 * @param ORGSpace
	 * @param cs
	 * @return
	 * @throws OGRException
	 */
	public static int importFromProj4(OGRSpatialReference ORGSpace, String cs)throws OGRException {
		
		if ((cs == null) || (cs.equals("")))
			throw new OGRException("Parametro incorrecto");
		
		if(ORGSpace.cPtr == 0)
			throw new OGRException("Error en Proj4(). La llamada de creación de objeto no tuvo exito.");
		
		int res = importFromProj4Nat(ORGSpace.cPtr, cs);
		
		if(res != 0)
			throw new OGRException(res, "Error en Proj4(). No se ha podido asignar el sistema de coordenadas especificado. ", ORGSpace);
		
		return res;
		
	}
	
	
	/**
	 * 
	 * @param ORGSpace
	 * @return
	 * @throws CrsOgrException
	 */
	public static String exportToProj4(OGRSpatialReference ORGSpace) throws OGRException {
		if(ORGSpace.cPtr == 0)
			throw new OGRException("Error en exportToProj4(). La llamada de creación de objeto no tuvo exito.");
		
		String result = exportToProj4Nat(ORGSpace.cPtr);
		
		if(result.length() == 0) 
			throw new OGRException("No se ha podido hacer exportToProj4");
		return result;
	}
	
	
	/**
	 * 
	 * @param ORGSpace
	 * @param cod
	 * @param cs
	 * @param coord
	 * @return
	 * @throws OGRException
	 */
	public static int importFromPCI(OGRSpatialReference ORGSpace,String cod, String cs, double[] coord)throws OGRException {
		if ((cs == null) || (cs.equals("")))
			throw new OGRException("Parametro incorrecto");
		
		if ((cod == null) || (cod.equals("")))
			throw new OGRException("Parametro incorrecto");
		
		if(ORGSpace.cPtr == 0)
			throw new OGRException("Error en importFromPCI. La llamada de creación de objeto no tuvo exito.");
		
		if (coord == null)
			throw new OGRException("Parametro incorrecto");
		
		int result = importFromPCINat(ORGSpace.cPtr,cod, cs, coord);
		
		if(result != 0) 
			throw new OGRException(result, "Error en PCI().La creacion del objeto no tuvo exito. ", ORGSpace);
		
		return result;
	}
	
	
	/**
	 * 
	 * @param ORGSpace
	 * @param code
	 * @param zone
	 * @param params
	 * @param datum
	 * @return
	 * @throws OGRException
	 */
	public static int importFromUSGS(OGRSpatialReference ORGSpace, long code, long zone, double[] params, long datum) throws OGRException {
		if(ORGSpace.cPtr == 0)
			throw new OGRException("Error en importFromUSGS. La llamada de creación de objeto no tuvo exito.");
		
		if ((zone <= 0) || (zone > 60))
			throw new OGRException("Zona incorrecta");
		
		if (params == null)
			throw new OGRException("Parametro incorrecto");
		
		int result = importFromUSGSNat(ORGSpace.cPtr,code, zone, params, datum);
		
		if(result != 0) 
			throw new OGRException(result, "Error en USGS().La creacion del objeto no tuvo exito. ", ORGSpace);
		
		return result;	
		
	}
	
	
	/**
	 * 
	 * @param ORGSpace
	 * @param cadenas
	 * @return
	 * @throws OGRException
	 */
	public static int importFromESRI(OGRSpatialReference ORGSpace, String cadenas) throws OGRException {
		if (cadenas == null)
			throw new OGRException("Parametro incorrecto");
		
		if(ORGSpace.cPtr == 0)
			throw new OGRException("Error en importFromEsri. La llamada de creación de objeto no tuvo exito.");
		
		
		int result = importFromESRINat(ORGSpace.cPtr, cadenas);
		if(result != 0) 
			throw new OGRException(result, "Error en ESRI().La creacion del objeto no tuvo exito. ", ORGSpace);
		return result;
	}
}