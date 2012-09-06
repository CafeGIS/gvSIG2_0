 /**********************************************************************
 * $Id: ogrfeaturedefn_interfaz.cpp 7765 2006-10-03 07:05:18Z nacho $
 *
 * Name:     ogrfeaturedefn_interfaz.c
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


#include <jni.h>h"
#include "ogr_api.h"
#include "ogrsf_frmts.h"

/******************************************************************************/
//								getName
/******************************************************************************/

JNIEXPORT jstring JNICALL Java_es_gva_cit_jogr_OGRFeatureDefn_getNameNat
  (JNIEnv *env, jobject obj, jlong cPtr){
  	
  	OGRFeatureDefn 			*fd = (OGRFeatureDefn *) 0 ;
  	jstring					nom_fd;
  	
  	fd = *(OGRFeatureDefn **)&cPtr;
  	const char *name = fd->GetName();
  	
  	if(name!=NULL)
	  	nom_fd = env->NewStringUTF(name);
  	else return NULL;
  	
  	return nom_fd;
  	
  }
  
/******************************************************************************/
//								getGeomType
/******************************************************************************/

JNIEXPORT jint JNICALL Java_es_gva_cit_jogr_OGRFeatureDefn_getGeomTypeNat
  (JNIEnv *env, jobject obj, jlong cPtr){
  	
  	OGRFeatureDefn 			*fd = (OGRFeatureDefn *) 0 ;
	
  	fd = *(OGRFeatureDefn **)&cPtr;
  	OGRwkbGeometryType tipo = fd->GetGeomType();  
  	
  	if(tipo==wkbUnknown)return 0;	
  	if(tipo==wkbPoint)return 1;
  	if(tipo==wkbLineString)return 2;
  	if(tipo==wkbPolygon)return 3;
  	if(tipo==wkbMultiPoint)return 4;
  	if(tipo==wkbMultiLineString)return 5;
  	if(tipo==wkbMultiPolygon)return 6;
  	if(tipo==wkbGeometryCollection)return 7;
  	if(tipo==wkbNone)return 8;
  	if(tipo==wkbLinearRing)return 9;
  	if(tipo==wkbPoint25D)return 10;
  	if(tipo==wkbLineString25D)return 11;
  	if(tipo==wkbPolygon25D)return 12;
  	if(tipo==wkbMultiPoint25D)return 13;
  	if(tipo==wkbMultiLineString25D)return 14;
  	if(tipo==wkbMultiPolygon25D)return 15;
  	if(tipo==wkbGeometryCollection25D)return 16;
  	
  	return -1;  	
  }
  
/******************************************************************************/
//								~OGRFeatureDefn
/******************************************************************************/
 
  JNIEXPORT void JNICALL Java_es_gva_cit_jogr_OGRFeatureDefn_FreeOGRFeatureDefnNat
  (JNIEnv *env, jobject obj, jlong cPtr){
  	
  	OGRFeatureDefn *df = (OGRFeatureDefn *) 0 ;
  	
  	df = *(OGRFeatureDefn **)&cPtr;
  	if(df!=NULL){
  		delete df;
  	}
  }
  
  /******************************************************************************/
//								getFieldCount
/******************************************************************************/   
 
JNIEXPORT jint JNICALL Java_es_gva_cit_jogr_JNIBase_getFieldCountNat
  (JNIEnv *env, jobject obj, jlong cPtr){
  	
  	OGRFeatureDefn 	*df = (OGRFeatureDefn *) 0 ;
  	int 			nfields=-1;
  	
  	df = *(OGRFeatureDefn **)&cPtr;
  	if(df!=NULL){
  		nfields = df->GetFieldCount();
  	}
  	return nfields;
  }
   
/******************************************************************************/
//								getFieldDefn
/******************************************************************************/  
  
JNIEXPORT jlong JNICALL Java_es_gva_cit_jogr_OGRFeatureDefn_getFieldDefnNat
  (JNIEnv *env, jobject obj, jlong cPtr, jint i){
  	
  	OGRFeatureDefn 	*df = (OGRFeatureDefn *) 0 ;
  	OGRFieldDefn 	*field;
  	long			ptro_field;  	
  	df = *(OGRFeatureDefn **)&cPtr;
  	if(df!=NULL){
  		field = df->GetFieldDefn(i);
  		ptro_field = (long)&(*field);
  	}
  	return (jlong)ptro_field;
  	
  }
  
  
/******************************************************************************/
//								getFieldIndex
/******************************************************************************/

JNIEXPORT jint JNICALL Java_es_gva_cit_jogr_OGRFeatureDefn_getFieldIndexNat
  (JNIEnv *env, jobject obj, jlong cPtr, jstring field){
  
  	  OGRFeatureDefn 	*fd = (OGRFeatureDefn *) 0 ;
  	  const char		*campo;
  	  int 				index=-1;
  	  
	  fd = *(OGRFeatureDefn **)&cPtr;
	  if(fd!=NULL){
	  	campo = env->GetStringUTFChars(field, 0);
		index =	fd->GetFieldIndex(campo);
	  	env->ReleaseStringUTFChars(field, campo);
	  }
	  return index;
  }

/******************************************************************************/
//								addFieldDefn
/******************************************************************************/

JNIEXPORT void JNICALL Java_es_gva_cit_jogr_OGRFeatureDefn_addFieldDefnNat
  (JNIEnv *env, jobject obj, jlong cPtr, jlong fdefn){
  
  	  OGRFeatureDefn 	*fd = (OGRFeatureDefn *) 0 ;
	  OGRFieldDefn		*fielddefn;
	  
	  fd = *(OGRFeatureDefn **)&cPtr;
	  fielddefn = *(OGRFieldDefn **)&fdefn;
	  if(fd!=NULL){
	  	fd->AddFieldDefn(fielddefn);
	  }
  }

/******************************************************************************/
//								setGeomType
/******************************************************************************/

JNIEXPORT void JNICALL Java_es_gva_cit_jogr_OGRFeatureDefn_setGeomTypeNat
  (JNIEnv *env, jobject obj, jlong cPtr, jstring eGType){
  
  	  OGRFeatureDefn 	*fd = (OGRFeatureDefn *) 0 ;
  	  OGRwkbGeometryType 	geomtype;
  	  
	  fd = *(OGRFeatureDefn **)&cPtr;
	  if(fd!=NULL){
	  	const char *type = env->GetStringUTFChars( eGType, 0);
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
  		
  		fd->SetGeomType(geomtype);
	  	env->ReleaseStringUTFChars( eGType, type );

	  	
	  }
  }

/******************************************************************************/
//								cloneFeatureDefn
/******************************************************************************/

JNIEXPORT jlong JNICALL Java_es_gva_cit_jogr_OGRFeatureDefn_cloneFeatureDefnNat
  (JNIEnv *env, jobject obj, jlong cPtr){
  
  	  OGRFeatureDefn 	*fd = (OGRFeatureDefn *) 0 ;
   	  OGRFeatureDefn 	*return_fd;
   	  long				ptr_fd=-1;
   	  
	  fd = *(OGRFeatureDefn **)&cPtr;
	  if(fd!=NULL){
	  	return_fd = fd->Clone();
	  	if(return_fd!=NULL)
	  	  	ptr_fd = (long)&(*return_fd);
	  }
	  
      return (jlong)ptr_fd;
  }

/******************************************************************************/
//								createFeatureDefn
/******************************************************************************/

JNIEXPORT jlong JNICALL Java_es_gva_cit_jogr_OGRFeatureDefn_createFeatureDefnNat
  (JNIEnv *env, jclass clase, jstring pszName){
  
  	  OGRFeatureDefn	*return_fd;
  	  long				ptr_fd=-1;
  	  const	char		*name;
  	  
	  name = env->GetStringUTFChars(pszName, 0);
	  return_fd = OGRFeatureDefn::CreateFeatureDefn(name);
	  if(return_fd!=NULL)
	  	  ptr_fd = (long)&(*return_fd);
	  env->ReleaseStringUTFChars( pszName, name );
	
	  return (jlong)ptr_fd;
  }

/******************************************************************************/
//								destroyFeatureDefn
/******************************************************************************/

JNIEXPORT void JNICALL Java_es_gva_cit_jogr_OGRFeatureDefn_destroyFeatureDefnNat
  (JNIEnv *env, jclass clase, jlong ptr_fd){
  
  	  OGRFeatureDefn 	*fd = (OGRFeatureDefn *) 0 ;
  
	  fd = *(OGRFeatureDefn **)&ptr_fd;
	  if(fd!=NULL){
	  	OGRFeatureDefn::DestroyFeatureDefn(fd);
	  }
  }
  
/******************************************************************************/
//								referenceFeatureDefn
/******************************************************************************/

JNIEXPORT jint JNICALL Java_es_gva_cit_jogr_JNIBase_referenceFeatureDefnNat
  (JNIEnv *env, jobject obj, jlong cPtr){
  
  	  OGRFeatureDefn 	*fd = (OGRFeatureDefn *) 0 ;
  	  int 				res=-1;
  	  
	  fd = *(OGRFeatureDefn **)&cPtr;
	  if(fd!=NULL){
	  	res = fd->Reference();
	  }
	  return res;
  }

/******************************************************************************/
//								dereferenceFeatureDefn
/******************************************************************************/

JNIEXPORT jint JNICALL Java_es_gva_cit_jogr_JNIBase_dereferenceFeatureDefnNat
  (JNIEnv *env, jobject obj, jlong cPtr){
  
  	  OGRFeatureDefn 	*fd = (OGRFeatureDefn *) 0 ;
  	  int				res=-1;
  	  
	  fd = *(OGRFeatureDefn **)&cPtr;
	  if(fd!=NULL){
	  	res = fd->Dereference();
	  }
	  
	  return res;
  }

/******************************************************************************/
//								getReferenceCount
/******************************************************************************/

JNIEXPORT jint JNICALL Java_es_gva_cit_jogr_JNIBase_getReferenceCountNat
  (JNIEnv *env, jobject obj, jlong cPtr){
  
  	  OGRFeatureDefn 	*fd = (OGRFeatureDefn *) 0 ;
  	  int 				count=-1;
  	  
	  fd = *(OGRFeatureDefn **)&cPtr;
	  if(fd!=NULL){
	  	count = fd->GetReferenceCount();
	  }
	  return count;
  }

