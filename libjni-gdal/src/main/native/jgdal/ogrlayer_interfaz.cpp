 /**********************************************************************
 * $Id: ogrlayer_interfaz.cpp 7765 2006-10-03 07:05:18Z nacho $
 *
 * Name:     ogrlayer_interfaz.c
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
//									getLayerDefn
/******************************************************************************/

JNIEXPORT jlong JNICALL Java_es_gva_cit_jogr_OGRLayer_getLayerDefnNat
  (JNIEnv *env, jobject obj, jlong cPtr){
  	
  	OGRLayer 				*capa  = (OGRLayer *) 0 ;
  	OGRFeatureDefn 			*fd;
  	long					featuredefn=0;
  	
  	capa = *(OGRLayer **)&cPtr;
  	fd = capa->GetLayerDefn();
  	featuredefn = (long)&(*fd);
  	
  	return (jlong)featuredefn;
  }
  
/******************************************************************************/
//								getFeatureCount
/******************************************************************************/

JNIEXPORT jint JNICALL Java_es_gva_cit_jogr_JNIBase_getFeatureCountNat
  (JNIEnv *env, jobject obj, jlong cPtr){
  	
  	int 				res	= -1;
  	OGRLayer 			*capa  = (OGRLayer *) 0 ;
  	
  	capa = *(OGRLayer **)&cPtr;
  	res = capa->GetFeatureCount();
  	
  	return res;
  	
  }

/******************************************************************************/
//								  resetReading
/******************************************************************************/

JNIEXPORT void JNICALL Java_es_gva_cit_jogr_OGRLayer_resetReadingNat
  (JNIEnv *env, jobject obj, jlong cPtr){
  	
  	OGRLayer 	*capa  = (OGRLayer *) 0 ;
  	
  	capa = *(OGRLayer **)&cPtr;
  	capa->ResetReading();
  }

/******************************************************************************/
//									getExtent
/******************************************************************************/
  
JNIEXPORT jint JNICALL Java_es_gva_cit_jogr_OGRLayer_getExtentNat
  (JNIEnv *env, jobject obj, jlong cPtr, jobject extent, jboolean bForce){
  	
  	OGRLayer 				*capa  = (OGRLayer *) 0 ;
  	OGREnvelope 			oExt;
  	jclass 					clase;
  	jfieldID 				id_campo;
  	int 					res;
  	
  	capa = *(OGRLayer **)&cPtr;
  	res = capa->GetExtent(&oExt, (bool)bForce);
  	  	
  	clase = env->GetObjectClass(extent);
  	id_campo = env->GetFieldID( clase, "minX", "D");
  	env->SetDoubleField( extent, id_campo, oExt.MinX);
  	id_campo = env->GetFieldID( clase, "maxX", "D");
  	env->SetDoubleField( extent, id_campo, oExt.MaxX);
  	id_campo = env->GetFieldID( clase, "minY", "D");
  	env->SetDoubleField( extent, id_campo, oExt.MinY);
  	id_campo = env->GetFieldID( clase, "maxY", "D");
  	env->SetDoubleField( extent, id_campo, oExt.MaxX);
  	
  	oExt.~OGREnvelope();  	
  	return res;
  }
  
/******************************************************************************/
//								getNextFeature
/******************************************************************************/  

JNIEXPORT jlong JNICALL Java_es_gva_cit_jogr_OGRLayer_getNextFeatureNat
  (JNIEnv *env, jobject obj, jlong cPtr){
  	
  	OGRLayer 		*capa = (OGRLayer *) 0 ;
  	OGRFeature	 	*feature;
  	long 			ptro_feat = -1;
  	
  	capa = *(OGRLayer **)&cPtr;
  	if(capa!=NULL){
  		feature = capa->GetNextFeature();
  		if(feature !=NULL)
	  		ptro_feat = (long)&(*feature);
	  	else return -1;
  	}
  	
  	return ptro_feat;
  	
  }
    
/******************************************************************************/
//								~OGRLayer
/******************************************************************************/
 
  JNIEXPORT void JNICALL Java_es_gva_cit_jogr_OGRLayer_FreeOGRLayerNat
  (JNIEnv *env, jobject obj, jlong cPtr){
  	
  	OGRLayer *df = (OGRLayer *) 0 ;
  	
  	df = *(OGRLayer **)&cPtr;
  	if(df!=NULL){
  		delete df;
  	}
  }
  
  
/******************************************************************************/
//								getSpatialFilter
/******************************************************************************/
JNIEXPORT jlong JNICALL Java_es_gva_cit_jogr_OGRLayer_getSpatialFilterNat
  (JNIEnv *env, jobject obj, jlong cPtr){
  	
  	  	OGRLayer 		*df = (OGRLayer *) 0 ;
  	  	OGRGeometry 	*geom;
  	  	long 			ptro_geom = -1;
  	
	  	df = *(OGRLayer **)&cPtr;
		geom = df->GetSpatialFilter();
		
		if(geom!=NULL)
			ptro_geom = (long)&(*geom);
		
  	  	return ptro_geom;
  }

/******************************************************************************/
//								setSpatialFilter
/******************************************************************************/
JNIEXPORT void JNICALL Java_es_gva_cit_jogr_OGRLayer_setSpatialFilterNat
  (JNIEnv *env, jobject obj, jlong cPtr, jlong ptr_geom){
  	
  	  	OGRLayer 		*df = (OGRLayer *) 0 ;
  		OGRGeometry		*geom;
  		
	  	df = *(OGRLayer **)&cPtr;
	  	geom = *(OGRGeometry **)&ptr_geom;
	  	
	  	df->SetSpatialFilter(geom);
	  		  	
  }

/******************************************************************************/
//								setAttributeFilter
/******************************************************************************/
JNIEXPORT jint JNICALL Java_es_gva_cit_jogr_OGRLayer_setAttributeFilterNat
  (JNIEnv *env, jobject obj, jlong cPtr, jstring attr){
  		
  	  	OGRLayer *df = (OGRLayer *) 0 ;
  		const char *attrib;
  		int ogrerr;
  		
	  	df = *(OGRLayer **)&cPtr;
	  	attrib = env->GetStringUTFChars( attr, 0);
	  	ogrerr = df->SetAttributeFilter(attrib);
	  	env->ReleaseStringUTFChars( attr, attrib);
	  	
  	  	return ogrerr;
  }

/******************************************************************************/
//								getFeature
/******************************************************************************/
JNIEXPORT jlong JNICALL Java_es_gva_cit_jogr_OGRLayer_getFeatureNat
  (JNIEnv *env, jobject obj, jlong cPtr, jlong nFID){
  		
  	  	OGRLayer 		*df = (OGRLayer *) 0 ;
  		OGRFeature 		*feat;
  		long			ptro_feat=-1;
  		
	  	df = *(OGRLayer **)&cPtr;
	  	feat = df->GetFeature(nFID);
	  	if(feat!=NULL)
			ptro_feat = (long)&(*feat);
	  	
  	  	return ptro_feat;
  }

/******************************************************************************/
//								setFeature
/******************************************************************************/
JNIEXPORT jint JNICALL Java_es_gva_cit_jogr_OGRLayer_setFeatureNat
  (JNIEnv *env, jobject obj, jlong cPtr, jlong poFeature){
  	  	  		
  	  	OGRLayer 		*df = (OGRLayer *) 0 ;
  		OGRFeature		*feat;
  		int				ogrerr=-1;
  		
	  	df = *(OGRLayer **)&cPtr;
	  	feat = *(OGRFeature **)&poFeature;
	  	ogrerr = df->SetFeature(feat);
	  	
  	  	return ogrerr;
  }
  

/******************************************************************************/
//								createFeature
/******************************************************************************/
JNIEXPORT jint JNICALL Java_es_gva_cit_jogr_OGRLayer_createFeatureNat
  (JNIEnv *env, jobject obj, jlong cPtr, jlong poFeature){
  		
  	  	OGRLayer 		*df = (OGRLayer *) 0 ;
  		OGRFeature		*feat;
  		int				ogrerr=-1;
  		
	  	df = *(OGRLayer **)&cPtr;
	  	feat = *(OGRFeature **)&poFeature;
	  	ogrerr = df->CreateFeature(feat);
	  	
  	  	return ogrerr;
  }

/******************************************************************************/
//								deleteFeature
/******************************************************************************/
JNIEXPORT jint JNICALL Java_es_gva_cit_jogr_OGRLayer_deleteFeatureNat
  (JNIEnv *env, jobject obj, jlong cPtr, jlong nFID){
  		
  	  	OGRLayer 		*df = (OGRLayer *) 0 ;
  		int				ogrerr=-1;
  	
	  	df = *(OGRLayer **)&cPtr;
		ogrerr = df->DeleteFeature(nFID);
  	  	return ogrerr;
  }

/******************************************************************************/
//								getSpatialRef
/******************************************************************************/
JNIEXPORT jlong JNICALL Java_es_gva_cit_jogr_OGRLayer_getSpatialRefNat
  (JNIEnv *env, jobject obj, jlong cPtr){
  		
  	  	OGRLayer 				*df = (OGRLayer *) 0 ;
  		OGRSpatialReference		*sr;
  		long 					ptro_sr = -1;
  		
	  	df = *(OGRLayer **)&cPtr;
	  	sr = df->GetSpatialRef();
	  
	  	if(sr!=NULL)
			ptro_sr = (long)&(*sr);
	
  	  	return ptro_sr;
  }

/******************************************************************************/
//								testCapability
/******************************************************************************/
JNIEXPORT jint JNICALL Java_es_gva_cit_jogr_OGRLayer_testCapabilityNat
  (JNIEnv *env, jobject obj, jlong cPtr, jstring od){
  		
  	  	OGRLayer 	*df = (OGRLayer *) 0 ;
  		int 		res=-1;
  		const char *s;
  		
	  	df = *(OGRLayer **)&cPtr;
	  	if(df!=NULL){
		  	s = env->GetStringUTFChars( od, 0);
		  	res = df->TestCapability(s);
		  	env->ReleaseStringUTFChars( od, s);
	  	}
  	  	return res;
  }

/******************************************************************************/
//								getInfo
/******************************************************************************/
JNIEXPORT jstring JNICALL Java_es_gva_cit_jogr_OGRLayer_getInfoNat
  (JNIEnv *env, jobject obj, jlong cPtr, jstring s){
  		
  	  	OGRLayer 	*df = (OGRLayer *) 0 ;
  	  	const char 	*par;
  	  	const char 	*res;
  		jstring		cadena;
  		
	  	df = *(OGRLayer **)&cPtr;
	  	if(df!=NULL){
		  	par = env->GetStringUTFChars( s, 0);
		  	res = df->GetInfo(par);
		  	env->ReleaseStringUTFChars( s, par);
		  	cadena = env->NewStringUTF(res); 
	  	}
  		return cadena;
  }

/******************************************************************************/
//								createField
/******************************************************************************/
JNIEXPORT jint JNICALL Java_es_gva_cit_jogr_OGRLayer_createFieldNat
  (JNIEnv *env, jobject obj, jlong cPtr, jlong poField, jint bAproxOk){
  		
  	  	OGRLayer 		*df = (OGRLayer *) 0 ;
  		OGRFieldDefn	*field;
  		int				ogrerr;
  		
	  	df = *(OGRLayer **)&cPtr;
	  	if(df!=NULL){
		  	field = *(OGRFieldDefn **)&poField;
			ogrerr = df->CreateField(field, bAproxOk);
	  	}	  	
  	  	return ogrerr;
  }

/******************************************************************************/
//								getStyleTable
/******************************************************************************/
JNIEXPORT jlong JNICALL Java_es_gva_cit_jogr_OGRLayer_getStyleTableNat
  (JNIEnv *env, jobject obj, jlong cPtr){
  		
  	  	OGRLayer 		*df = (OGRLayer *) 0 ;
  		OGRStyleTable	*style;
  		long			ptro_style = -1;
  		
	  	df = *(OGRLayer **)&cPtr;
	  	style = df->GetStyleTable();
	  	if(style!=NULL)
			ptro_style = (long)&(*style);
	  	
  	  	return ptro_style;
  }

/******************************************************************************/
//								setStyleTable
/******************************************************************************/
JNIEXPORT void JNICALL Java_es_gva_cit_jogr_OGRLayer_setStyleTableNat
  (JNIEnv *env, jobject obj, jlong cPtr, jlong poStyleTable){
  		
  	  	OGRLayer 		*df = (OGRLayer *) 0 ;
  		OGRStyleTable	*styletable;
  		
	  	df = *(OGRLayer **)&cPtr;
	  	styletable = *(OGRStyleTable **)&poStyleTable;
	  	
	  	if(df!=NULL)
		  	df->SetStyleTable(styletable);
  }

/******************************************************************************/
//								initializeIndexSupport
/******************************************************************************/
JNIEXPORT jint JNICALL Java_es_gva_cit_jogr_OGRLayer_initializeIndexSupportNat
  (JNIEnv *env, jobject obj, jlong cPtr, jstring s){
  		
  	  	OGRLayer 	*df = (OGRLayer *) 0 ;
  	  	int			ogrerr;
  	  	const char	*par;
  	  		  	
	  	df = *(OGRLayer **)&cPtr;
	  	if(df!=NULL){
		  	par = env->GetStringUTFChars( s, 0);
		  	ogrerr = df->InitializeIndexSupport(par);
		  	env->ReleaseStringUTFChars( s, par);
	  	}
	  
  	  	return ogrerr;
  }

/******************************************************************************/
//								getIndex
/******************************************************************************/
JNIEXPORT jlong JNICALL Java_es_gva_cit_jogr_OGRLayer_getIndexNat
  (JNIEnv *env, jobject obj, jlong cPtr){
  		
  	  	OGRLayer 			*df = (OGRLayer *) 0 ;
  		OGRLayerAttrIndex	*layerattrindex;
  		long				ptro_layerattr = -1;
  		
	  	df = *(OGRLayer **)&cPtr;
	  	if(df!=NULL){
		  	layerattrindex = df->GetIndex();
		  	if(layerattrindex!=NULL)
				ptro_layerattr = (long)&(*layerattrindex);
	  	}
  	  	return ptro_layerattr;
  }

/******************************************************************************/
//								syncToDisk
/******************************************************************************/
JNIEXPORT jint JNICALL Java_es_gva_cit_jogr_OGRLayer_syncToDiskNat
  (JNIEnv *env, jobject obj, jlong cPtr){
  		
  	  	OGRLayer 	*df = (OGRLayer *) 0 ;
  		int 		ogrerr;
  		
		df = *(OGRLayer **)&cPtr;
		if(df!=NULL)
		  	ogrerr = df->SyncToDisk();
  	  	return ogrerr;
  }

/******************************************************************************/
//								commitTransaction
/******************************************************************************/
JNIEXPORT jint JNICALL Java_es_gva_cit_jogr_OGRLayer_commitTransactionNat
  (JNIEnv *env, jobject obj, jlong cPtr){
  		
  	  	OGRLayer *df = (OGRLayer *) 0 ;
	  	int 		ogrerr=-1;
	  	
	  	df = *(OGRLayer **)&cPtr;
	  	if(df!=NULL)
		  	ogrerr = df->CommitTransaction();
	  	
  	  	return ogrerr;
  }

/******************************************************************************/
//								rollbackTransaction
/******************************************************************************/
JNIEXPORT jint JNICALL Java_es_gva_cit_jogr_OGRLayer_rollbackTransactionNat
  (JNIEnv *env, jobject obj, jlong cPtr){
  		
  	  	OGRLayer 	*df = (OGRLayer *) 0 ;
  	  	int			ogrerr=-1;
  	
	  	df = *(OGRLayer **)&cPtr;
	  	if(df!=NULL)
		  	ogrerr = df->RollbackTransaction();
  	  	return ogrerr;
  	  	
  }

/******************************************************************************/
//								reference
/******************************************************************************/
JNIEXPORT jint JNICALL Java_es_gva_cit_jogr_OGRLayer_referenceNat
  (JNIEnv *env, jobject obj, jlong cPtr){
  		
  	  	OGRLayer 	*df = (OGRLayer *) 0 ;
  	  	int			res;
  	  	
	  	df = *(OGRLayer **)&cPtr;
	  	if(df!=NULL)
		  	res = df->Reference();
  	  	return res;
  	  	
  }

/******************************************************************************/
//								dereference
/******************************************************************************/
JNIEXPORT jint JNICALL Java_es_gva_cit_jogr_OGRLayer_dereferenceNat
  (JNIEnv *env, jobject obj, jlong cPtr){
  		
  	  	OGRLayer 	*df = (OGRLayer *) 0 ;
  		int 		res;
  		
	  	df = *(OGRLayer **)&cPtr;
	  	if(df!=NULL)
		  	res = df->Dereference();
  		return res;
  }

/******************************************************************************/
//								startTransaction
/******************************************************************************/
JNIEXPORT jint JNICALL Java_es_gva_cit_jogr_OGRLayer_startTransactionNat
  (JNIEnv *env, jobject obj, jlong cPtr){
  		
  	  	OGRLayer 	*df = (OGRLayer *) 0 ;
  		int 		ogrerr=-1;
  		
	  	df = *(OGRLayer **)&cPtr;
	  	if(df!=NULL)
			ogrerr = df->StartTransaction();
		return ogrerr;
  }

