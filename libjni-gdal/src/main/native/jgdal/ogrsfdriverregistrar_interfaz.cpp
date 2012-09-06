 /**********************************************************************
 * $Id: ogrsfdriverregistrar_interfaz.cpp 7765 2006-10-03 07:05:18Z nacho $
 *
 * Name:     ogrsfdriverregistrar_interfaz.c
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
//									open
/******************************************************************************/

JNIEXPORT jlong JNICALL Java_es_gva_cit_jogr_OGRSFDriverRegistrar_openNat
  (JNIEnv *env, jobject obj, jstring fte, jint bUpdate){
  	
  	OGRDataSource       *poDS;
    OGRSFDriver         *poDriver;
    long				driver=0;
    long				dataset=0;
    const char 			*pszFilename;
    jclass 				clase;
  	jfieldID 			id_campo;
    
    pszFilename = env->GetStringUTFChars( fte, 0);
        
	poDS = OGRSFDriverRegistrar::Open( pszFilename, bUpdate, &poDriver );
	
    if( poDS == NULL )return -1;
    if(poDriver!=NULL)
	    driver = (long)&(*poDriver);

    clase = env->GetObjectClass(obj);
    id_campo = env->GetFieldID( clase, "driver", "J");
    env->SetLongField( obj, id_campo, (jlong)driver);    
    dataset = (long)&(*poDS);
    env->ReleaseStringUTFChars( fte, pszFilename);
   
    return (jlong)dataset;
  	
  }
  
/******************************************************************************/
//									getDriverCount
/******************************************************************************/

JNIEXPORT jint JNICALL Java_es_gva_cit_jogr_JNIBase_getDriverCountNat
  (JNIEnv *env, jobject obj, jlong cPtr){
  	
  	int res=-1;
  	OGRSFDriverRegistrar *drvreg  = (OGRSFDriverRegistrar *) 0 ;
  
  	drvreg = *(OGRSFDriverRegistrar **)&cPtr;
  	if(drvreg!=NULL)
	  	res = drvreg->GetDriverCount();
	  	  
  	return res;
  	
  }
  
/******************************************************************************/
//									getRegistrar
/******************************************************************************/

  JNIEXPORT jlong JNICALL Java_es_gva_cit_jogr_OGRSFDriverRegistrar_getRegistrarNat
  (JNIEnv *env, jobject obj){
  	
  	long ptro;
  	OGRSFDriverRegistrar    *poR = OGRSFDriverRegistrar::GetRegistrar();
  	ptro = (long)&(*poR);
  	
  	return (jlong)ptro;
  }	
  
/******************************************************************************/
//									getDriver
/******************************************************************************/
  
  
  JNIEXPORT jlong JNICALL Java_es_gva_cit_jogr_OGRSFDriverRegistrar_getDriverNat
  (JNIEnv *env, jobject obj, jlong cPtr, jint i){
  	
  	OGRSFDriverRegistrar 	*drvreg  = (OGRSFDriverRegistrar *) 0 ;
  	OGRSFDriver 			*drv;
  	long					driver=0;
  	
  	drvreg = *(OGRSFDriverRegistrar **)&cPtr;
  	if(drvreg!=NULL){
	  	drv = drvreg->GetDriver(i);
	  	driver = (long)&(*drv);
  	}
  	return (jlong)driver;
  	
  	
  }
  
  
/******************************************************************************/
//								~OGRSFDriverRegistrar
/******************************************************************************/
 
  JNIEXPORT void JNICALL Java_es_gva_cit_jogr_OGRSFDriverRegistrar_FreeOGRSFDriverRegistrarNat
  (JNIEnv *env, jobject obj, jlong cPtr){
  	
  	OGRSFDriverRegistrar *df = (OGRSFDriverRegistrar *) 0 ;
  	
  	df = *(OGRSFDriverRegistrar **)&cPtr;
  	if(df!=NULL){
  		delete df;
  	}
  }

