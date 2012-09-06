/**********************************************************************
 * $Id: OGRLayer.java 7765 2006-10-03 07:05:18Z nacho $
 *
 * Name:     OGRLayer.java
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


/** 
 * 
 * @author Nacho Brodin <brodin_ign@gva.es>.<BR> Equipo de desarrollo gvSIG.<BR> http://www.gvsig.gva.es
 * @version 0.0
 * @link http://www.gvsig.gva.es
 */


public class OGRLayer extends JNIBase {
	
	public native long getLayerDefnNat(long cPtr);
	public native void resetReadingNat(long cPtr);
	public native int getExtentNat(long cPtr, OGREnvelope extent, boolean bForce);
	private native void FreeOGRLayerNat(long cPtr);
	private native long getNextFeatureNat(long cPtr);
	
	private native long getSpatialFilterNat(long cPtr);
	private native void setSpatialFilterNat( long cPtr, long geom );
	private native int setAttributeFilterNat( long cPtr, String att ); //Excepciones
	private native long getFeatureNat( long cPtr, long nFID );
	private native int setFeatureNat( long cPtr, long poFeature );//Excepciones
	private native int createFeatureNat( long cPtr, long poFeature );//Excepciones
	private native int deleteFeatureNat( long cPtr, long nFID );//Excepciones
	private native long getSpatialRefNat(long cPtr);
	private native int testCapabilityNat( long cPtr, String od );
	private native String getInfoNat( long cPtr, String s );
	private native int createFieldNat( long cPtr, long poField, int bApproxOK);//Excepciones
	private native long getStyleTableNat(long cPtr);
	private native void setStyleTableNat(long cPtr, long poStyleTable);
	private native int initializeIndexSupportNat( long cPtr, String s );//Excepciones
	private native long getIndexNat(long cPtr);
	private native int syncToDiskNat(long cPtr);
	private native int commitTransactionNat(long cPtr);
	private native int rollbackTransactionNat(long cPtr);
	private native int referenceNat(long cPtr);
	private native int dereferenceNat(long cPtr);
	private native int startTransactionNat(long cPtr);

	/**
	 * Constructor
	 * @param cPtr	dirección de memoria al objeto OGRLayer de C. 
	 */
	
	public OGRLayer(long cPtr){
		this.cPtr=cPtr;
	}
	
	
	 /**
	 * 
	 * @throws OGRException
	 * @return 
	 */
			
	 public OGRFeatureDefn getLayerDefn()throws OGRException{
				
	 	if(cPtr == 0)
			throw new OGRException("Error en getLayerDefn(). El constructor no tuvo exito");
		    	
		long layer = getLayerDefnNat(cPtr);
		
		if(layer == 0)
			throw new OGRException("Error en getLayerDefn(). No se ha podido obtener el objeto OGRFeatureDefn.");
						
		return new OGRFeatureDefn(layer);
			
	 }
	 
	 /**
	  * 
	  */
	 public void resetReading()throws OGRException{
	 	
	 	if(cPtr == 0)
			throw new OGRException("Error en resetReading(). El constructor no tuvo exito");
	 	
	 	resetReadingNat(cPtr);
	 }
	 
	 /**
	 * Obtiene el número de caracteristicas
	 * @throws OGRException
	 * @return Número de caracteristicas
	 */
		
	 public int getFeatureCount()throws OGRException{
				
		String msg1="Error en getFeatureCount. El constructor no tuvo exito.";
		String msg2="Error en el conteo de caracteristicas.";
		return baseSimpleFunctions(2,msg1,msg2);
	 }
	 
	 /**
	  * Obtiene el extent de la capa
	  * @throws OGRException
	  * @return objeto conteniendo el extent 
	  */
	 
	 public OGREnvelope getExtent(boolean bForce)throws OGRException{
	 	
	 	if(cPtr == 0)
			throw new OGRException("Error en getExtent(). El constructor no tuvo exito");
	 	
	 	OGREnvelope extent = new OGREnvelope();
	 	
	 	int err;
	 	err = getExtentNat(cPtr, extent, bForce);
	 		 	
	 	if(err!=0)
	 		throwException(err,"Error em getFeatureCount()");
	 	
	 	return extent;
	 	
	 }
	 
	 /**
	  * 
	  */
	 
	 public OGRFeature getNextFeature()throws OGRException{
	 	
	 	OGRFeature feature = null;
	 	if(cPtr == 0)
			throw new OGRException("Error en getNextFeature(). El constructor no tuvo exito");
	 	
	 	long ptro_feat = getNextFeatureNat(cPtr);
	 	if(ptro_feat !=0)
	 		feature = new OGRFeature(ptro_feat);
	 	return feature;
	 }
	 
	 /**
	 * Destructor 
	 */
		
	 protected void finalize() throws OGRFailureException{
		 if(cPtr == 0)
				throw new OGRFailureException("Fallo al acceder al dato.");
		 
		FreeOGRLayerNat(cPtr);
	 }
	 
	 /**
	  * 
	  */
	 
	 public OGRGeometry getSpatialFilter()throws OGRException{
	 	
	 	if(cPtr == 0)
			throw new OGRException("Error en getSpatialFilter(). El constructor no tuvo exito");
	 	
	 	long ptr_sf = getSpatialFilterNat(cPtr);
	 	
	 	if(ptr_sf == 0)
			throw new OGRException("Error en getSpatialFilter(). No se ha podido obtener un OGRGeometry valido.");
	 	
	 	OGRGeometry geom = new OGRGeometry(ptr_sf);
	 	return geom;
	 	
	 }

	 /**
	  * 
	  */
	 
	 public void setSpatialFilter( OGRGeometry geom )throws OGRException{
	 	
	 	if(cPtr == 0)
			throw new OGRException("Error en setSpatialFilter(). El constructor no tuvo exito");
	 	
	 	if(geom.getPtro() == 0)
	 		throw new OGRException("Error en setSpatialFilter(). El objeto OGRGeometry no tiene un puntero valido.");
	 	
	 	setSpatialFilterNat(cPtr,geom.getPtro());
	 }

	 /**
	  * 
	  */
	 
	 public void setAttributeFilter( String att) throws OGRException{ //Excepciones
	 	
	 	if(cPtr == 0)
			throw new OGRException("Error en setAttributeFilter(). El constructor no tuvo exito");
	 	
	 	setAttributeFilterNat(cPtr, att);
	 }

	 /**
	  * 
	  */
	 
	 public OGRFeature getFeature( long nFID )throws OGRException{
	 	
	 	if(cPtr == 0)
			throw new OGRException("Error en getFeature(). El constructor no tuvo exito");
	 	
	 	long ptr_f = getFeatureNat(cPtr, nFID);
	 	
	 	if(ptr_f == 0)
			throw new OGRException("Error en getFeature(). No se ha podido obtener un OGRFeature valido.");
	 	
	 	OGRFeature feature = new OGRFeature(ptr_f);
	 	return feature;
	 	
	 }

	 /**
	  * 
	  */
	 
	 public void setFeature( OGRFeature poFeature )throws OGRException{//Excepciones
	 	
	 	if(cPtr == 0)
			throw new OGRException("Error en setFeature(). El constructor no tuvo exito");
	 	
	 	if(poFeature.getPtro() == 0)
	 		throw new OGRException("Error en setFeature(). El objeto OGRFeature no tiene un puntero valido.");
	 	
	 	setFeatureNat(cPtr, poFeature.getPtro());
	 }

	 /**
	  * 
	  */
	 
	 public void createFeature( OGRFeature poFeature )throws OGRException{//Excepciones
	 	
	 	if(cPtr == 0)
			throw new OGRException("Error en createFeature(). El constructor no tuvo exito");
	 	
	 	if(poFeature.getPtro()==0)
	 		throw new OGRException("Error en createFeature(). El objeto OGRFeature no tiene un puntero valido.");
	 	
	 	createFeatureNat(cPtr,poFeature.getPtro());
	 	
	 }

	 /**
	  * 
	  */
	 
	 public void deleteFeature( long nFID )throws OGRException{//Excepciones
	 	
	 	if(cPtr == 0)
			throw new OGRException("Error en deleteFeature(). El constructor no tuvo exito");
	 	
	 	deleteFeatureNat(cPtr, nFID);
	 	
	 }

	 /**
	  * Obtiene el sistema de referencia espacial para esta capa o nulo si no tiene.
	  * @throws OGRException
	  * @return Sistema de referencia espacial
	  */
	 
	 public OGRSpatialReference getSpatialRef()throws OGRException{
	 	
	 	OGRSpatialReference sr=null;
	 	
	 	if(cPtr == 0)
			throw new OGRException("Error en getSpatialRef(). El constructor no tuvo exito");
	 	
	 	long ptr_sr = getSpatialRefNat(cPtr);
	 	
	 	if(ptr_sr == 0)
	 		throw new OGRException("Error en getSpatialRef().");
	 		
	 	sr = new OGRSpatialReference(ptr_sr);
	 	
	 	return sr;
	 }

	 /**
	  * 
	  */
	 
	 public int testCapability( String od )throws OGRException{
	 	
		if(cPtr == 0)
			throw new OGRException("Error en testCapability(). El constructor ha fallado.");
		 
		int res=-1;
		res = testCapabilityNat(cPtr, od);
		
		if(res==0)
			throw new OGRException("Error en testCapability(). No se ha podido obtener un valor de retorno valido.");

		return res;
		
	 }

	 /**
	  * 
	  */
	 
	 public String getInfo( String s )throws OGRException{
	 	
	 	if(cPtr == 0)
			throw new OGRException("Error en getInfo(). El constructor no tuvo exito");
	 	
	 	String info = getInfoNat(cPtr, s);
	 	
	 	if(info == null)
	 		throw new OGRException("Error en getInfo(). No se ha podido obtener información valida.");
	 	
	 	return info;
	 }

	 /**
	  * 
	  */
	 
	 public void createField( OGRFieldDefn poField, int bApproxOK)throws OGRException{//Excepciones
	 	
	 	if(cPtr == 0)
			throw new OGRException("Error en greateField(). El constructor no tuvo exito");
	 	
	 	if(poField.getPtro() == 0)
	 		throw new OGRException("Error en greateField(). El objeto OGRFieldDefn no tiene una dirección de memoria valida.");
	 	
	 	int res = createFieldNat(cPtr, poField.getPtro(), bApproxOK);
	 	throwException(res, "Error en greateField().");
	 
	 }

	 /**
	  * 
	  */
	 
	 public void syncToDisk()throws OGRException{//Excepciones
	 	
	 	if(cPtr == 0)
			throw new OGRException("Error en syncToDisk(). El constructor no tuvo exito");
		int ogrerr = syncToDiskNat(cPtr);
		throwException(ogrerr, "Error en syncToDisk()");
	 	
	 }

	 /**
	  * 
	  */
	 
	 public OGRStyleTable getStyleTable()throws OGRException{
	 	
	 	if(cPtr == 0)
			throw new OGRException("Error en getStyleTable(). El constructor no tuvo exito");
	 	
	 	long ptr_st = getStyleTableNat(cPtr);
	 	
	 	if(ptr_st == 0)
	 		throw new OGRException("Error en getStyleTable(). No se ha podido obtener un objeto OGRStyleTable valido.");
	 	
	 	OGRStyleTable st=new OGRStyleTable(ptr_st);
	 	
	 	return st;
	 }

	 /**
	  * 
	  */
	 
	 public void setStyleTable(OGRStyleTable poStyleTable)throws OGRException{
	 	
	 	if(cPtr == 0)
			throw new OGRException("Error en setStyleTable(). El constructor no tuvo exito");
	 	
	 	setStyleTableNat(cPtr, poStyleTable.getPtro());
	 	
	 }

	 /**
	  * 
	  */
	 
	 public void startTransaction()throws OGRException{//Excepciones
	 	
	 	if(cPtr == 0)
			throw new OGRException("Error en startTransaction(). El constructor no tuvo exito");
	 	
	 	int ogrerr = startTransactionNat(cPtr);
	 	throwException(ogrerr,"Error en startTransaction().");
	 }

	 /**
	  * 
	  */
	 
	 public void commitTransaction()throws OGRException{//Excepciones
	 	
	 	if(cPtr == 0)
			throw new OGRException("Error en commitTransaction(). El constructor no tuvo exito");
	 	
	 	int ogrerr = commitTransactionNat(cPtr);
	 	throwException(ogrerr,"Error en commitTransaction().");
	 	
	 }

	 /**
	  * 
	  */
	 
	 public void rollbackTransaction()throws OGRException{//Excepciones
	 	
	 	if(cPtr == 0)
			throw new OGRException("Error en rollbackTransaction(). El constructor no tuvo exito");
	 	
	 	int ogrerr = rollbackTransactionNat(cPtr);
	 	throwException(ogrerr,"Error en rollbackTransaction().");
	 }

	 /**
	  * 
	  */
	 
	 public int reference()throws OGRException{
	 	
	 	if(cPtr == 0)
			throw new OGRException("Error en reference(). El constructor no tuvo exito");
	 	
	 	return referenceNat(cPtr);
	 }

	 /**
	  * 
	  */
	 
	 public int dereference()throws OGRException{
	 	
	 	if(cPtr == 0)
			throw new OGRException("Error en dereference(). El constructor no tuvo exito");
	 	
	 	return dereferenceNat(cPtr);
	 }

	 /**
	  * 
	  */
	 
	 public int getRefCount()throws OGRException{
	 	
	 	String msg1="Error en getRefCount. El constructor no tuvo exito.";
		String msg2="Error en getRefCount. No se ha podido obtener un número de referencias valido.";
		return baseSimpleFunctions(20,msg1,msg2);
	 }

	 /**
	  * 
	  */
	 
	 public void initializeIndexSupport( String s )throws OGRException{//Excepciones
	 	
	 	if(cPtr == 0)
			throw new OGRException("Error en initializeIndexSupport(). El constructor no tuvo exito");
	 	
	 	initializeIndexSupportNat(cPtr, s);
	 }

	 /**
	  * 
	  */
	 
	 public OGRLayerAttrIndex getIndex()throws OGRException{
	 	
	 	if(cPtr == 0)
			throw new OGRException("Error en getIndex(). El constructor no tuvo exito");
	 	
	 	long ptr_lai = getIndexNat(cPtr);
	 	
	 	if(ptr_lai == 0)
	 		throw new OGRException("Error en getIndex(). No se ha podido obtener un OGRLayerAttrIndex valido.");
	 	
	 	OGRLayerAttrIndex layerattrin = new OGRLayerAttrIndex(ptr_lai);
	 	return layerattrin;
	 	
	 }	    
}