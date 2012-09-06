 /**********************************************************************
 * $Id: ogrsfdriver_interfaz.cpp 7765 2006-10-03 07:05:18Z nacho $
 *
 * Name:     ogrsfdriver_interfaz.c
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
//									getName
/******************************************************************************/
  
 JNIEXPORT jstring JNICALL Java_es_gva_cit_jogr_OGRSFDriver_getNameNat
  (JNIEnv *env, jobject obj, jlong cPtr){
  	
  	OGRSFDriver 			*drv = (OGRSFDriver *) 0 ;
  	jstring					nom_drv;
  	
  	drv = *(OGRSFDriver **)&cPtr;
  	const char *name = drv->GetName();
  	
  	if(name!=NULL)
	  	nom_drv = env->NewStringUTF(name);
  	else return NULL;
  	  	
  	return nom_drv;
  }
  
 

/******************************************************************************/
//									open										+
/******************************************************************************/

JNIEXPORT jlong JNICALL Java_es_gva_cit_jogr_OGRSFDriver_openNat
  (JNIEnv *env, jobject obj, jlong cPtr, jstring pszName, jboolean bUpdate){
  	
  	OGRSFDriver 			*drv = (OGRSFDriver *) 0 ;
  	OGRDataSource 			*ds;
  	long 					ptr_ds;
  	drv = *(OGRSFDriver **)&cPtr;
	
  	const char *name = env->GetStringUTFChars( pszName, 0);
  	if(bUpdate==true)
  		ds=drv->Open(name,1);
  	else
	  	ds=drv->Open(name,0);
  	env->ReleaseStringUTFChars( pszName, name);
  	
  	if(ds==NULL)return -1;
  	
  	ptr_ds = (long)&(*ds);
  	return (jlong)ptr_ds;
  	
  }

/******************************************************************************/
//									testCapability								+
/******************************************************************************/

JNIEXPORT jint JNICALL Java_es_gva_cit_jogr_OGRSFDriver_testCapabilityNat
  (JNIEnv *env, jobject obj, jlong cPtr, jstring cap){
  	
  	OGRSFDriver 			*drv = (OGRSFDriver *) 0 ;
  	drv = *(OGRSFDriver **)&cPtr;
  	int res=-1;
  	
  	const char *testcap = env->GetStringUTFChars( cap, 0);
  	res = drv->TestCapability(testcap);
  	env->ReleaseStringUTFChars( cap, testcap);
  	return res;
  	
  }

/******************************************************************************/
//									createDataSource
/******************************************************************************/

JNIEXPORT jlong JNICALL Java_es_gva_cit_jogr_OGRSFDriver_createDataSourceNat
  (JNIEnv *env, jobject obj, jlong cPtr, jstring pszName){
  	
  	OGRSFDriver 			*drv = (OGRSFDriver *) 0 ;
  	drv = *(OGRSFDriver **)&cPtr;
  	OGRDataSource 			*ds;
  	long 					ptr_ds;
  	
  	const char *name = env->GetStringUTFChars( pszName, 0);
  	ds = drv->CreateDataSource(name);
  	env->ReleaseStringUTFChars( pszName, name);
  	if(ds==NULL)return -1;
  	
  	ptr_ds = (long)&(*ds);
  	return (jlong)ptr_ds;
  }


/******************************************************************************/
//									deleteDataSource							+
/******************************************************************************/

JNIEXPORT jint JNICALL Java_es_gva_cit_jogr_OGRSFDriver_deleteDataSourceNat
  (JNIEnv *env, jobject obj, jlong cPtr, jstring pszName){
  	
  	OGRSFDriver 			*drv = (OGRSFDriver *) 0 ;
  	drv = *(OGRSFDriver **)&cPtr;
  	int						ogrerr;
  	
  	const char *name = env->GetStringUTFChars( pszName, 0);
	ogrerr = drv->DeleteDataSource(name);
  	env->ReleaseStringUTFChars( pszName, name);
  	return ogrerr;
  }

/******************************************************************************/
//									copyDataSource								+
/******************************************************************************/

JNIEXPORT jlong JNICALL Java_es_gva_cit_jogr_OGRSFDriver_copyDataSourceNat
  (JNIEnv *env, jobject obj, jlong cPtr, jlong dsSrc, jstring pszNewName, jobjectArray joptions){
  	
  	OGRSFDriver 			*drv = (OGRSFDriver *) 0 ;
  	char 					**options;
  	int						longitud;
  	OGRDataSource			*ds_fte;
  	OGRDataSource			*ds_dstno;
  	long					ptr_dtno;
  	
  	drv = *(OGRSFDriver **)&cPtr;
  	ds_fte = *(OGRDataSource **)&dsSrc;
  	
  	longitud = env->GetArrayLength(joptions);
  	options = (char **)malloc(sizeof(char *)*longitud);
  	for(int i=0;i<longitud;i++){
  		jstring el = (jstring)env->GetObjectArrayElement(joptions,i);
  		const char *simple_option = env->GetStringUTFChars( el, 0);
  		options[i]=(char *)malloc(strlen(simple_option));
  		strcpy(options[i],simple_option);
  		env->ReleaseStringUTFChars( el, simple_option);
  	}
  	
  	const char *name = env->GetStringUTFChars( pszNewName, 0);
  	ds_dstno = drv->CopyDataSource(ds_fte, name, options);
  	env->ReleaseStringUTFChars( pszNewName, name);
  	
  	for(int i=0;i<longitud;i++)free(options[i]);
  	free(options);
  	
  	if(ds_dstno==NULL)return -1;
  	
  	ptr_dtno = (long)&(*ds_dstno);
  	return (jlong)ptr_dtno;
  	
  }

