 /**********************************************************************
 * $Id: ogrdatasource_interfaz.cpp 7765 2006-10-03 07:05:18Z nacho $
 *
 * Name:     ogrdatasource_interfaz.c
 * Project:  JGDAL. Interface java to gdal (Frank Warmerdam).
 * Purpose:  
 * Author:   Nacho Brodin, brodin_ign@gva.es
 *
 **********************************************************************/
/* gvSIG. Sistema de Informaci�n Geogr�fica de la Generalitat Valenciana
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
*   Av. Blasco Ib��ez, 50
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


#include <jni.h>
#include "ogr_api.h"
#include "ogrsf_frmts.h"



/******************************************************************************/
//								getName
/******************************************************************************/

JNIEXPORT jstring JNICALL Java_es_gva_cit_jogr_OGRDataSource_getNameNat
  (JNIEnv *env, jobject obj, jlong cPtr){
  	
  	OGRDataSource 			*ds = (OGRDataSource *) 0 ;
  	jstring					nom_ds;
  	
  	ds = *(OGRDataSource **)&cPtr;
  	const char *name = ds->GetName();
  	
  	if(name!=NULL)
	  	nom_ds = env->NewStringUTF(name);
  	else return NULL;
  	
  	return nom_ds;
  	
  }
  
/******************************************************************************/
//								getLayerCount
/******************************************************************************/
 
 JNIEXPORT jint JNICALL Java_es_gva_cit_jogr_JNIBase_getLayerCountNat
  (JNIEnv *env, jobject obj, jlong cPtr){
  	
  	int res=-1;
  	OGRDataSource *ds  = (OGRDataSource *) 0 ;
  
  	ds = *(OGRDataSource **)&cPtr;
  	if(ds!=NULL)
	  	res = ds->GetLayerCount();
	  	  
  	return res;
  }
  
/******************************************************************************/
//									getLayer
/******************************************************************************/

JNIEXPORT jlong JNICALL Java_es_gva_cit_jogr_OGRDataSource_getLayerNat
  (JNIEnv *env, jobject obj, jlong cPtr, jint iLayer){
  	
  	OGRDataSource		 	*ds  = (OGRDataSource *) 0 ;
  	OGRLayer 				*capa;
  	long					layer=-1;
  	
  	ds = *(OGRDataSource **)&cPtr;
  	if(ds!=NULL){
  		capa = ds->GetLayer(iLayer);
  		if(capa!=NULL)layer = (long)&(*capa);
  	}
  	
  	return (jlong)layer;
  	
  }
  
/******************************************************************************/
//								~OGRDataSource
/******************************************************************************/
  
JNIEXPORT void JNICALL Java_es_gva_cit_jogr_OGRDataSource_FreeOGRDataSource
  (JNIEnv *env, jobject obj, jlong cPtr){
  	
  	OGRDataSource *df = (OGRDataSource *) 0 ;
  	
  	df = *(OGRDataSource **)&cPtr;
  	if(df!=NULL){
  		delete df;
  	}
  }
  
/******************************************************************************/
//								reference
/******************************************************************************/

JNIEXPORT jint JNICALL Java_es_gva_cit_jogr_JNIBase_referenceNat
  (JNIEnv *env, jobject obj, jlong cPtr){
  	
  	OGRDataSource 	*ds = (OGRDataSource *) 0 ;
  	int 			res=-1;
  	
  	ds = *(OGRDataSource **)&cPtr;
  	if(ds!=NULL){
	  	res=ds->Reference();
  	}
  	return res;
  	  	
  }

/******************************************************************************/
//								dereference
/******************************************************************************/

JNIEXPORT jint JNICALL Java_es_gva_cit_jogr_JNIBase_dereferenceNat
  (JNIEnv *env, jobject obj, jlong cPtr){
  	
  	OGRDataSource 	*ds = (OGRDataSource *) 0 ;
  	int 			res=-1;
  	
  	ds = *(OGRDataSource **)&cPtr;
  	if(ds!=NULL){
	  	res = ds->Dereference();
  	}
  	return res;
  	
  }

/******************************************************************************/
//								getRefCount
/******************************************************************************/

JNIEXPORT jint JNICALL Java_es_gva_cit_jogr_JNIBase_getRefCountNat
  (JNIEnv *env, jobject obj, jlong cPtr){
  	  	
  	OGRDataSource 	*ds = (OGRDataSource *) 0 ;
  	int 			res = -1;
  	
  	ds = *(OGRDataSource **)&cPtr;
  	if(ds!=NULL){
	  	res = ds->GetRefCount();
  	}
  	return res;
  	
  	
  }

/******************************************************************************/
//								getSummaryRefCount
/******************************************************************************/

JNIEXPORT jint JNICALL Java_es_gva_cit_jogr_JNIBase_getSummaryRefCountNat
  (JNIEnv *env, jobject obj, jlong cPtr){
  	  	
  	OGRDataSource   *ds = (OGRDataSource *) 0 ;
  	int 			res = -1;
  	
  	ds = *(OGRDataSource **)&cPtr;
  	if(ds!=NULL){
	  	res = ds->GetSummaryRefCount();
  	}
  	return res;
  	
  }

/******************************************************************************/
//								OGRDataSource
/******************************************************************************/

JNIEXPORT jlong JNICALL Java_es_gva_cit_jogr_OGRDataSource_getLayerByNameNat
  (JNIEnv *env, jobject obj, jlong cPtr, jstring name){
  	  	
  	OGRDataSource 	*ds = (OGRDataSource *) 0 ;
  	const char 		*layername;
  	OGRLayer		*layer;
  	long			ptro_layer=-1;
  	
  	ds = *(OGRDataSource **)&cPtr;
  	layername = env->GetStringUTFChars( name, 0 );
  	if(ds!=NULL){
	  	layer=ds->GetLayerByName(layername);
  		if(layer!=NULL)ptro_layer = (long)&(*layer);
  	}
  	env->ReleaseStringUTFChars( name, layername );
  	return (jlong)ptro_layer;
  }

/******************************************************************************/
//								deleteLayer
/******************************************************************************/

JNIEXPORT jint JNICALL Java_es_gva_cit_jogr_OGRDataSource_deleteLayerNat
  (JNIEnv *env, jobject obj, jlong cPtr, jint layer){
  	  	
  	OGRDataSource 	*ds = (OGRDataSource *) 0 ;
  	int 			ogrerr;
  	
  	ds = *(OGRDataSource **)&cPtr;
  	if(ds!=NULL){
  		ogrerr = ds->DeleteLayer(layer);
  	}
  	return ogrerr;
  }

/******************************************************************************/
//								testCapability
/******************************************************************************/

JNIEXPORT jint JNICALL Java_es_gva_cit_jogr_OGRDataSource_testCapabilityNat
  (JNIEnv *env, jobject obj, jlong cPtr, jstring cap){
  	  	
  	OGRDataSource 	*ds = (OGRDataSource *) 0 ;
  	int 			res=-1;
  	const char 		*capability;
  	
  	ds = *(OGRDataSource **)&cPtr;
  	if(ds!=NULL){
  		capability = env->GetStringUTFChars( cap, 0 );
  		res = ds->TestCapability(capability);
  		env->ReleaseStringUTFChars( cap, capability );
  	}
  	return res;
  	
  }

/******************************************************************************/
//								createLayer
/******************************************************************************/

JNIEXPORT jlong JNICALL Java_es_gva_cit_jogr_OGRDataSource_createLayerNat
  (JNIEnv *env, jobject obj, jlong cPtr, jstring pszName, jlong poSpatialRef, jstring eGType, jobjectArray papszOptions){
  	  	
  	OGRDataSource 		*ds = (OGRDataSource *) 0 ;
  	int 				longitud;
  	char				**opciones;
  	OGRLayer			*layer_dstno;
	OGRSpatialReference	*spatialRef;
  	long 				ptr_dtno=-1;
  	OGRwkbGeometryType	geomtype;
  	
  	ds = *(OGRDataSource **)&cPtr;
  	
	spatialRef = *(OGRSpatialReference **)&poSpatialRef;
			
  	if(ds!=NULL){
  		longitud = env->GetArrayLength( papszOptions); 
  		opciones = (char **)malloc(sizeof(char *)*longitud);
  		for(int i=0;i<longitud;i++){
	  		jstring el = (jstring)env->GetObjectArrayElement(papszOptions,i);
	  		const char *simple_option = env->GetStringUTFChars( el, 0);
	  		opciones[i]=(char *)malloc(strlen(simple_option));
	  		strcpy(opciones[i],simple_option);
	  		env->ReleaseStringUTFChars( el, simple_option);
  		}
  		
  		const char *type = env->GetStringUTFChars( eGType, 0);
  		const char *name = env->GetStringUTFChars( pszName, 0);
  		if(strcmp(type,"wkbUnknown")==0)geomtype = wkbUnknown;
  		else if(strcmp(type,"wkbPoint")==0)geomtype = wkbPoint;
  		else if(strcmp(type,"wkbLineString")==0)geomtype = wkbLineString;
  		else if(strcmp(type,"wkbPolygon")==0)geomtype = wkbPolygon;
  		else if(strcmp(type,"wkbMultiPoint")==0)geomtype = wkbMultiPoint;
  		else if(strcmp(type,"wkbMultiLineString")==0)geomtype = wkbMultiLineString;
  		else if(strcmp(type,"wkbMultiPolygon")==0)geomtype = wkbMultiPolygon;
  		else if(strcmp(type,"wkbGeometryCollection")==0)geomtype = wkbGeometryCollection;
  		else if(strcmp(type,"wkbNone")==0)geomtype = wkbNone;
  		else if(strcmp(type,"wkbLinearRing")==0)geomtype = wkbLinearRing;
		else if(strcmp(type,"wkbPoint25D")==0)geomtype = wkbPoint25D;
  		else if(strcmp(type,"wkbLineString25D")==0)geomtype = wkbLineString25D;
  		else if(strcmp(type,"wkbPolygon25D")==0)geomtype = wkbPolygon25D;
  		else if(strcmp(type,"wkbMultiPoint25D")==0)geomtype = wkbMultiPoint25D;
  		else if(strcmp(type,"wkbMultiLineString25D")==0)geomtype = wkbMultiLineString25D;
  		else if(strcmp(type,"wkbMultiPolygon25D")==0)geomtype = wkbMultiPolygon25D;
  		else if(strcmp(type,"wkbGeometryCollection25D")==0)geomtype = wkbGeometryCollection25D;
  		
  		layer_dstno = ds->CreateLayer(name, spatialRef, geomtype, opciones);
	  	env->ReleaseStringUTFChars( eGType, type);
	  	env->ReleaseStringUTFChars( pszName, name);
  	}
  	
  	for(int i=0;i<longitud;i++)free(opciones[i]);
  	free(opciones);
  	
  	if(layer_dstno==NULL)return -1;
  	
  	ptr_dtno = (long)&(*layer_dstno);
  	return (jlong)ptr_dtno;
  }

/******************************************************************************/
//								copyLayer
/******************************************************************************/

JNIEXPORT jlong JNICALL Java_es_gva_cit_jogr_OGRDataSource_copyLayerNat
  (JNIEnv *env, jobject obj, jlong cPtr, jlong poSrcLayer, jstring pszNewName, jobjectArray papszOptions){
  	  	
  	OGRDataSource 		*ds = (OGRDataSource *) 0 ;
  	int 				longitud;
  	char				**opciones;
  	OGRLayer			*layer_dstno;
  	OGRLayer			*layer;
  	long 				ptr_dtno=-1;
  	
  	ds = *(OGRDataSource **)&cPtr;
  	layer = *(OGRLayer **)&poSrcLayer;
  	if(ds!=NULL && layer!=NULL){
  		longitud = env->GetArrayLength( papszOptions); 
  		opciones = (char **)malloc(sizeof(char *)*longitud);
  		for(int i=0;i<longitud;i++){
	  		jstring el = (jstring)env->GetObjectArrayElement(papszOptions,i);
	  		const char *simple_option = env->GetStringUTFChars( el, 0);
	  		opciones[i]=(char *)malloc(strlen(simple_option));
	  		strcpy(opciones[i],simple_option);
	  		env->ReleaseStringUTFChars( el, simple_option);
  		}
  		
  		const char *name = env->GetStringUTFChars( pszNewName, 0);
  		layer_dstno = ds->CopyLayer(layer, name, opciones);
	  	env->ReleaseStringUTFChars( pszNewName, name);
  		
  	}
  	
  	for(int i=0;i<longitud;i++)free(opciones[i]);
  	free(opciones);
  	
  	if(layer_dstno==NULL)return -1;
  	
  	ptr_dtno = (long)&(*layer_dstno);
  	return (jlong)ptr_dtno;
  }

/******************************************************************************/
//								getStyleTable
/******************************************************************************/

JNIEXPORT jlong JNICALL Java_es_gva_cit_jogr_OGRDataSource_getStyleTableNat
  (JNIEnv *env, jobject obj, jlong cPtr){
  	  	
  	OGRDataSource 	*ds = (OGRDataSource *) 0 ;
  	OGRStyleTable	*styletable;
  	long			ptro_styletable=-1;
  	
  	ds = *(OGRDataSource **)&cPtr;
  	if(ds!=NULL){
  		styletable = ds->GetStyleTable();
  		if(styletable!=NULL)
	  		ptro_styletable = (long)&(*styletable);
  	}
  	return ptro_styletable;
  }

/******************************************************************************/
//								executeSQL
/******************************************************************************/

JNIEXPORT jlong JNICALL Java_es_gva_cit_jogr_OGRDataSource_executeSQLNat
  (JNIEnv *env, jobject obj, jlong cPtr, jstring pszStatement, jlong ptr_spatialFilter, jstring pszDialect){
  	  	
  	OGRDataSource 		*ds = (OGRDataSource *) 0 ;
  	OGRGeometry			*geom = (OGRGeometry *) 0 ;
   	OGRLayer			*layer;
   	long				ptro_layer=-1;
   	
  	ds = *(OGRDataSource **)&cPtr;
  	geom = *(OGRGeometry **)&ptr_spatialFilter;
  	if(ds!=NULL ){
  		const char *stat = env->GetStringUTFChars( pszStatement, 0);
  		const char *dialect = env->GetStringUTFChars( pszDialect, 0);
  		layer = ds->ExecuteSQL(stat, geom, dialect);
  		env->ReleaseStringUTFChars( pszStatement, stat);
  		env->ReleaseStringUTFChars( pszDialect, dialect);
  		if(layer!=NULL)
	  		ptro_layer = (long)&(*layer);
  	}
  	return ptro_layer;
  }

/******************************************************************************/
//							  releaseResultSet
/******************************************************************************/

JNIEXPORT void JNICALL Java_es_gva_cit_jogr_OGRDataSource_releaseResultSetNat
  (JNIEnv *env, jobject obj, jlong cPtr, jlong ptr_poResultsSet){
  	  	
  	OGRDataSource 	*ds = (OGRDataSource *) 0 ;
  	OGRLayer		*layer = (OGRLayer *) 0 ;
  	
  	ds = *(OGRDataSource **)&cPtr;
  	layer = *(OGRLayer **)&ptr_poResultsSet;
  	if(ds!=NULL ){
  		ds->ReleaseResultSet(layer);
  	}
  }

/******************************************************************************/
//								syncToDisk
/******************************************************************************/

JNIEXPORT jint JNICALL Java_es_gva_cit_jogr_OGRDataSource_syncToDiskNat
  (JNIEnv *env, jobject obj, jlong cPtr){
  	  	
  	OGRDataSource 	*ds = (OGRDataSource *) 0 ;
  	int 			ogrerr;
  	
  	ds = *(OGRDataSource **)&cPtr;
  	if(ds!=NULL ){
  		ogrerr = ds->SyncToDisk();
  	}
  	return ogrerr;
  }
 
