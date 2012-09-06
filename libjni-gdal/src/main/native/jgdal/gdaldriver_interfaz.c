/**********************************************************************
 * $Id: gdaldriver_interfaz.c 7765 2006-10-03 07:05:18Z nacho $
 *
 * Name:     rasterband_interfaz.c
 * Project:  JGDAL. Interface java to gdal (Frank Warmerdam).
 * Purpose:  Raster band's Basic Funcions.
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
#include "gdal.h"
#include "cpl_string.h"


/* ******************************************************************** */
/*                              createCopy                              */
/* ******************************************************************** */

JNIEXPORT jlong JNICALL Java_es_gva_cit_jgdal_GdalDriver_createCopyNat
  (JNIEnv *env, jobject obj, jlong ptr_driver, jstring file, jlong ptr_gdal, jint bstrict){
  	
  	GDALDriverH *driver  = (GDALDriverH *) 0 ;
  	GDALDatasetH *dt_fte  = (GDALDatasetH *) 0 ;
  	GDALDatasetH dt_dstno;
  	jlong jresult=0;
	const char *filename;
  	
  	dt_fte = *(GDALDatasetH **)&ptr_gdal;
  	driver = *(GDALDriverH **)&ptr_driver;
  	
 	filename = (*env)->GetStringUTFChars(env, file, 0);
 	
  	dt_dstno = GDALCreateCopy(driver, filename, dt_fte, FALSE, NULL, NULL, NULL);
  	*(GDALDatasetH **)&jresult = dt_dstno;
  	
  	(*env)->ReleaseStringUTFChars(env, file, filename);
  	
    return jresult;
  	
  }
 
/* ******************************************************************** */
/*                              createCopyParams                        */
/* ******************************************************************** */ 
  
JNIEXPORT jlong JNICALL Java_es_gva_cit_jgdal_GdalDriver_createCopyParamsNat
  (JNIEnv *env, jobject obj, jlong ptr_driver, jstring file, jlong ptr_gdal, jint bstrict, jobject options){
  	
  	GDALDriverH *driver  = (GDALDriverH *) 0 ;
  	GDALDatasetH *dt_fte  = (GDALDatasetH *) 0 ;
  	GDALDatasetH dt_dstno;
  	jlong jresult=0;
	const char *filename;
	char **papszOptions = NULL;
	int i;
	
	//Variables para la obtenci�n de opciones
	
	jfieldID fid_vars, fid_datos;
	jobjectArray array_vars, array_datos;
	jmethodID metodo;
	jobject objeto_vars,objeto_datos;
	int tam;
	const char *str_vars,*str_datos;
  	
   	
  	dt_fte = *(GDALDatasetH **)&ptr_gdal;
  	driver = *(GDALDriverH **)&ptr_driver;
  	
 	filename = (*env)->GetStringUTFChars(env, file, 0);
 	
 	//Obtenemos los par�metros
 	
 	if(options!=NULL){
	 	jclass clase = (*env)->GetObjectClass(env, options);
	 	fid_vars = (*env)->GetFieldID(env, clase, "vars", "[Ljava/lang/String;");
	 	fid_datos = (*env)->GetFieldID(env, clase, "datos", "[Ljava/lang/String;");
	 	array_vars =(jobjectArray)(*env)->GetObjectField(env, options, fid_vars);
	 	array_datos =(jobjectArray)(*env)->GetObjectField(env, options, fid_datos);
	 	
	 	metodo = (*env)->GetMethodID(env, clase, "getSize","()I");
	    tam = (*env)->CallIntMethod(env,options,metodo);
	  
	    for(i=0;i<tam;i++){
	    	objeto_vars = (*env)->GetObjectArrayElement(env, array_vars, i);
	    	objeto_datos = (*env)->GetObjectArrayElement(env, array_datos, i);
	    	str_vars = (*env)->GetStringUTFChars(env,objeto_vars,0);
	    	str_datos = (*env)->GetStringUTFChars(env,objeto_datos,0);
	   	    
	   	    //printf("L105gdaldriver_interfaz.c var=%s param=%s\n",str_vars,str_datos);
	  	    papszOptions = CSLSetNameValue( papszOptions, str_vars, str_datos );	    
	   	    
	       	(*env)->ReleaseStringUTFChars(env, objeto_vars, str_vars);     
	       	(*env)->ReleaseStringUTFChars(env, objeto_datos, str_datos); 
	       	   
	    }    
 	}
    
 	
  	dt_dstno = GDALCreateCopy(driver, filename, dt_fte, FALSE, papszOptions, NULL, NULL);
  	*(GDALDatasetH **)&jresult = dt_dstno;
  	
  	(*env)->ReleaseStringUTFChars(env, file, filename);
  	//printf("L76createCopy jresult=%ld",(long)jresult);
    return jresult;
  	
  }
  
/* ******************************************************************** */
/*                              create                                  */
/* ******************************************************************** */
  
  JNIEXPORT jlong JNICALL Java_es_gva_cit_jgdal_GdalDriver_createNat
  (JNIEnv *env, jobject obj, jlong cPtr, jstring file, jint nXSize, jint nYSize, jint nBands, jint nType, jobject options){

	GDALDriverH *driver  = (GDALDriverH *) 0 ;
  	GDALDatasetH *dt;
  	jlong jresult=0;
	const char *filename;
	char **papszOptions = NULL;
	int i;
	
	//Variables para la obtenci�n de opciones
	
	jfieldID fid_vars, fid_datos;
	jobjectArray array_vars, array_datos;
	jmethodID metodo;
	jobject objeto_vars,objeto_datos;
	int tam;
	const char *str_vars,*str_datos;
  	
   	
  	driver = *(GDALDriverH **)&cPtr;
  	
 	filename = (*env)->GetStringUTFChars(env, file, 0);
 	
 	//Obtenemos los par�metros
 	
 	if(options!=NULL){
	 	jclass clase = (*env)->GetObjectClass(env, options);
	 	fid_vars = (*env)->GetFieldID(env, clase, "vars", "[Ljava/lang/String;");
	 	fid_datos = (*env)->GetFieldID(env, clase, "datos", "[Ljava/lang/String;");
	 	array_vars =(jobjectArray)(*env)->GetObjectField(env, options, fid_vars);
	 	array_datos =(jobjectArray)(*env)->GetObjectField(env, options, fid_datos);
	 	
	 	metodo = (*env)->GetMethodID(env, clase, "getSize","()I");
	    tam = (*env)->CallIntMethod(env,options,metodo);
	  
	    for(i=0;i<tam;i++){
	    	objeto_vars = (*env)->GetObjectArrayElement(env, array_vars, i);
	    	objeto_datos = (*env)->GetObjectArrayElement(env, array_datos, i);
	    	str_vars = (*env)->GetStringUTFChars(env,objeto_vars,0);
	    	str_datos = (*env)->GetStringUTFChars(env,objeto_datos,0);
	   	    
	   	    //printf("L105gdaldriver_interfaz.c var=%s param=%s\n",str_vars,str_datos);
	  	    papszOptions = CSLSetNameValue( papszOptions, str_vars, str_datos );	    
	   	    
	       	(*env)->ReleaseStringUTFChars(env, objeto_vars, str_vars);     
	       	(*env)->ReleaseStringUTFChars(env, objeto_datos, str_datos); 
	       	   
	    }    
 	}
 	
  	dt = GDALCreate(driver, filename, nXSize, nYSize, nBands, nType, papszOptions);  	
  	*(GDALDatasetH **)&jresult = dt;
  	
  	(*env)->ReleaseStringUTFChars(env, file, filename);
  	//printf("L76createCopy jresult=%ld",(long)jresult);
    return jresult;
 	  	
  }
 
