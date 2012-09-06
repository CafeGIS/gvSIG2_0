 /**********************************************************************
 * $Id: gdal_interfaz.c 15691 2007-10-31 10:49:53Z nbrodin $
 *
 * Name:     gdal_interfaz.c
 * Project:  JGDAL. Interface java to gdal (Frank Warmerdam).
 * Purpose:  dataset's Basic Funcions.
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
* 
* [01] 01-Oct-2005 nbt OpenArray function to support name parameter in char array
*/


#include <jni.h>
#include "gdal.h"
#include "cpl_string.h"
#include "signal.h"



/******************************************************************************/
//								Open
/******************************************************************************/


JNIEXPORT jlong JNICALL Java_es_gva_cit_jgdal_Gdal_openNat
  (JNIEnv *env, jobject obj, jstring pszF, jint acc){
  	
  	const char *pszFilename;
  	GDALDatasetH *dataset;
  	jlong jresult = 0 ;
  	FILE *fich;
 	
  	pszFilename = (*env)->GetStringUTFChars(env, pszF, 0);

	fich=fopen( pszFilename, "r" );
	if( fich )
		fclose(fich);
	else
   	{
      	fclose(fich);
      	return -1;
   	}
  	
  	GDALAllRegister();
  	dataset = GDALOpen((char *)pszFilename,(int)acc);
  	
  	*(GDALDatasetH **)&jresult = dataset;
  	
    (*env)->ReleaseStringUTFChars(env, pszF, pszFilename);
  	
  
  	if(dataset==NULL)return -1; 
  	else return jresult; 
  		
  }
  
/******************************************************************************/
//								OpenArray
/******************************************************************************/
void handler(int n) {
	#ifdef __linux__
	  raise(SIGALRM);
	#else
	  raise(SIGABRT);
	#endif
}

JNIEXPORT jlong JNICALL Java_es_gva_cit_jgdal_Gdal_openArrayNat
  (JNIEnv *env, jobject obj, jbyteArray pszF, jint acc){
  	
  	jbyte *pszFilename = NULL;
  	jbyte *aux = NULL;
  	GDALDatasetH *dataset;
  	jlong jresult = 0 ;
  	FILE *fich;
  	jsize longitud = 0, i;
 	  	
  	longitud = (*env)->GetArrayLength(env, pszF); 
  	pszFilename = (*env)->GetByteArrayElements(env, pszF, 0);
  	
  	#ifdef __linux__
		pszFilename = (jbyte *)realloc(pszFilename, longitud + 1);
	#else
		aux = (jbyte *)malloc(sizeof(jbyte) * (longitud + 1));
	  	memcpy(aux, pszFilename, longitud);
	  	free(pszFilename);
	  	pszFilename = aux;
	#endif
  		
	pszFilename[longitud] = '\0';
	
	fich = fopen( pszFilename, "r" );
	if( fich )
		fclose(fich);
	else		
      	return -1;
   	
   	GDALAllRegister();
  	dataset = GDALOpen((char *)pszFilename,(int)acc);
  	
  	*(GDALDatasetH **)&jresult = dataset;
  	
    (*env)->ReleaseByteArrayElements(env, pszF, pszFilename, 0);
  	
  	if(dataset == NULL)
  		return -1; 
  	else 
  		return jresult; 
  		
  }


/******************************************************************************/  
//								GetRasterXSize
/******************************************************************************/

  
  JNIEXPORT jint JNICALL Java_es_gva_cit_jgdal_JNIBase_getRasterXSizeNat
  (JNIEnv *env, jobject obj, jlong cPtr){
  	
  	int res=-1;
  	GDALDatasetH *dt  = (GDALDatasetH *) 0 ;
    	
	dt = *(GDALDatasetH **)&cPtr;
  	if(dt!=NULL)
	  	res = GDALGetRasterXSize(dt);
  
  	  	
  	return res;
  			
  }

  
/******************************************************************************/  
//								GetRasterYSize
/******************************************************************************/  
  
  
  JNIEXPORT jint JNICALL Java_es_gva_cit_jgdal_JNIBase_getRasterYSizeNat
  (JNIEnv *env, jobject obj, jlong cPtr){
  	
  	int res=-1;
  	GDALDatasetH *dt  = (GDALDatasetH *) 0 ;
  	
  	dt = *(GDALDatasetH **)&cPtr;
  	if(dt!=NULL)
  		res = GDALGetRasterYSize(dt);
  
  	  	
  	return res;
  	
  }

/******************************************************************************/   
//								GetRasterCount
/******************************************************************************/
 
 
 JNIEXPORT jint JNICALL Java_es_gva_cit_jgdal_JNIBase_getRasterCountNat
  (JNIEnv *env, jobject obj, jlong cPtr){
  	
  	int res=-1;
  	GDALDatasetH *dt  = (GDALDatasetH *) 0 ;
  	
  	dt = *(GDALDatasetH **)&cPtr;
  	if(dt!=NULL)
	  	res = GDALGetRasterCount(dt);
  
  	  	
  	return res;
  }
  
  
/******************************************************************************/ 
//								 GetMetadata
/******************************************************************************/


JNIEXPORT jobjectArray JNICALL Java_es_gva_cit_jgdal_Gdal_getMetadataNat
  (JNIEnv *env, jobject obj, jlong cPtr, jstring pszDomain){
  	
  	 char			**papszMetadata = NULL;
  	 GDALDatasetH 	*dt  = (GDALDatasetH *) 0 ;
  	 int 			i = 0;
  	 int			nmetadatos = 0;
  	 jclass 		class_string;
  	 jobjectArray 	vector_str;
  	 const char * 	domain;
  	   	  
  	 dt = *(GDALDatasetH **)&cPtr;
  	 	   	 
  	 if(dt != NULL){
  	 	if(pszDomain != NULL){
  	 		//Comprobamos el dominio de los metadatos
  	 		domain = (*env)->GetStringUTFChars(env, pszDomain, 0);
  	 		if(strcmp(domain, "") != 0){
  	 			papszMetadata = GDALGetMetadata( dt, domain );
  	 		}else{
  	 			papszMetadata = GDALGetMetadata( dt, NULL );
  	 		}
  	 		(*env)->ReleaseStringUTFChars(env, pszDomain, domain);
  	 	}else{
  	 		//Obtenemos los metadatos sobre papszMetadata
			papszMetadata = GDALGetMetadata( dt, NULL );
		}
		//Si hay metadatos devolvemos creamos el vector de String de java y los devolvemos
		 
		nmetadatos = CSLCount(papszMetadata);
		if( nmetadatos > 0 ){
			class_string = (*env)->FindClass (env, "java/lang/String");
			vector_str = (*env)->NewObjectArray(env, nmetadatos, class_string, (*env)->NewStringUTF(env,""));
		 	
		 	//Cargamos los metadatos
		 	
        	for( i = 0; papszMetadata[i] != NULL; i++ )
        		(*env)->SetObjectArrayElement(env,vector_str,i,(*env)->NewStringUTF(env,papszMetadata[i]));
               				
  	 		return vector_str;   	 
     	 }
  	 }
     
  	 return NULL;
  }





/******************************************************************************/   
//								GetRasterBand  
/******************************************************************************/


  JNIEXPORT jint JNICALL Java_es_gva_cit_jgdal_Gdal_getRasterBandNat
  (JNIEnv *env, jobject obj, jlong cPtr, jint hBand){
  	
		 	
  	GDALDatasetH *dt  = (GDALDatasetH *) 0 ;
  	GDALRasterBandH *rasterband=NULL;
  	jlong jresult = 0;

	dt = *(GDALDatasetH **)&cPtr;
    
    if(dt!=NULL){
		rasterband = GDALGetRasterBand(dt,(int)hBand);
		*(GDALDatasetH **)&jresult = rasterband;
    }else return -1;
    
   return jresult;
    
  }
  

/******************************************************************************/
//								GetGeoTransform
/******************************************************************************/


JNIEXPORT jint JNICALL Java_es_gva_cit_jgdal_Gdal_getGeoTransformNat
  (JNIEnv *env, jobject obj, jlong cPtr, jobject gt){
  	
  	jclass class_geotransform;
  	jfieldID id_campo;
  	double adfgt[6];
  	jdoubleArray doublearray;
  	GDALDatasetH *dt  = (GDALDatasetH *) 0 ;
  	

	dt = *(GDALDatasetH **)&cPtr;
  		
  	if(dt!=NULL){
  			
	  //Obtenemos el objeto java que contendr� el vector geotransform
		
  			
  		class_geotransform = (*env)->GetObjectClass(env, gt);
  		id_campo = (*env)->GetFieldID(env, class_geotransform, "adfgeotransform", "[D");


	   //Cargamos el buffer llamando a GDALRasterIO
  		 		
  		
  		if(GDALGetGeoTransform(dt,adfgt)==CE_None){
		  	doublearray = (*env)->NewDoubleArray(env,6);
		    if(doublearray!=NULL){
			  	(*env)->SetDoubleArrayRegion(env, doublearray, 0, 6,(jdouble *)adfgt); 
			  	(*env)->SetObjectField(env, gt, id_campo, doublearray);
			}
  		}else return -1;		
			
  	}else return -1;
  	
  	return 0;
  		  		
  }
  
/******************************************************************************/
//								setGeoTransformNat
/******************************************************************************/

JNIEXPORT jint JNICALL Java_es_gva_cit_jgdal_Gdal_setGeoTransformNat
  (JNIEnv *env, jobject obj, jlong cPtr, jobject gt){
  	
  	jclass class_geotransform;
  	jfieldID id_campo;
  	double adfgt[6];
  	jdoubleArray doublearray;
  	GDALDatasetH *dt  = (GDALDatasetH *) 0 ;
  	
  	dt = *(GDALDatasetH **)&cPtr;
  	
  	if(dt!=NULL){
  		
  		//Obtenemos los valores pasados en el vector gt
  		
  		class_geotransform = (*env)->GetObjectClass(env, gt);
  		id_campo = (*env)->GetFieldID(env, class_geotransform, "adfgeotransform", "[D");
  		doublearray =(jdoubleArray)(*env)->GetObjectField(env, gt, id_campo);
  		(*env)->GetDoubleArrayRegion(env,doublearray,0,6,(jdouble *)adfgt);
  		
  		GDALSetGeoTransform( dt, adfgt );
  		return 0;
  	}
  	return -1;
  	
  }  
  
/******************************************************************************/
//								GetProjectionRef
/******************************************************************************/


JNIEXPORT jstring JNICALL Java_es_gva_cit_jgdal_Gdal_getProjectionRefNat
  (JNIEnv *env, jobject obj, jlong cPtr){
  	
	char *pszProjection=NULL;
  	jstring pszP=NULL;
	GDALDatasetH *dt  = (GDALDatasetH *) 0 ;
  	
	  	
  	dt = *(GDALDatasetH **)&cPtr;
  		
  	//Llamamos a la funci�n y cargamos el jstring con el resultado 	
  		
	if(dt!=NULL){  	
	   	pszProjection = (char *) GDALGetProjectionRef(dt);
	   	if(pszProjection!=NULL)
		   	pszP = (*env)->NewStringUTF(env, pszProjection); 
	}
   	
   	return pszP;
  	
  }
  
/************************************************************************/
/*                        getDriverShortNameNat()                       */
/************************************************************************/

JNIEXPORT jstring JNICALL Java_es_gva_cit_jgdal_Gdal_getDriverShortNameNat
  (JNIEnv *env, jobject obj, jlong cPtr){
  	
  	char 			*pszShortName=NULL;
  	jstring 		pszSN=NULL;
	GDALDatasetH 	*dt  = (GDALDatasetH *) 0 ;
	GDALDriverH		hDriver;
	
	dt = *(GDALDatasetH **)&cPtr;
	
	//Llamamos a la funci�n y cargamos el jstring con el resultado 	
  		
	if(dt!=NULL){  	
		hDriver = GDALGetDatasetDriver( dt );
	   	pszShortName = (char *) GDALGetDriverShortName(hDriver);
	   	if(pszShortName!=NULL)
		   	pszSN = (*env)->NewStringUTF(env, pszShortName); 
	}
   	
   	return pszSN;
  }
  
/******************************************************************************/  
//								setProjection
/******************************************************************************/  
  
JNIEXPORT jint JNICALL Java_es_gva_cit_jgdal_Gdal_setProjectionNat
  (JNIEnv *env, jobject obj, jlong cPtr, jstring proj){
  	
  	GDALDatasetH *dt  = (GDALDatasetH *) 0 ;
  	
  	dt = *(GDALDatasetH **)&cPtr;
  	if(dt!=NULL){
  		const char *projeccion = (*env)->GetStringUTFChars(env, proj, 0);
  		GDALSetProjection( dt, projeccion );
  		(*env)->ReleaseStringUTFChars(env, proj, projeccion);
  		return 1;
  	}
  	
  	return -1;
  }
  
  
/******************************************************************************/  
//									Close
/******************************************************************************/


JNIEXPORT void JNICALL Java_es_gva_cit_jgdal_Gdal_closeNat
  (JNIEnv *env, jobject obj, jlong cPtr){

	GDALDatasetH *dt  = (GDALDatasetH *) 0 ;
  	
  	dt = *(GDALDatasetH **)&cPtr;
	GDALClose(dt);	

  }
  

/************************************************************************/
/*                        GDALGetDriverByName()                         */
/************************************************************************/

JNIEXPORT jlong JNICALL Java_es_gva_cit_jgdal_Gdal_getDriverByNameNat
  (JNIEnv *env, jclass clase, jstring name){
  	
  	const char *namedrv;
  	GDALDriverH *drv;
 
  	jlong jresult=-1;
  
    if(GDALGetDriverCount()<=0)GDALAllRegister();
    	
  	namedrv = (*env)->GetStringUTFChars(env, name, 0);
  	
  	drv=GDALGetDriverByName(namedrv);  
  		
  	*(GDALDriverH **)&jresult = drv;

  	(*env)->ReleaseStringUTFChars(env, name, namedrv);
	 
  	 return (long)jresult;
  }
  

/************************************************************************/
/*                        getGCPCountNat()                              */
/************************************************************************/

 JNIEXPORT jint JNICALL Java_es_gva_cit_jgdal_JNIBase_getGCPCountNat
  (JNIEnv *env, jobject obj, jlong cPtr){
  	
  	int res=-1;
  	GDALDatasetH *dt  = (GDALDatasetH *) 0 ;
  	
  	dt = *(GDALDatasetH **)&cPtr;
  	if(dt!=NULL)
	  	res = GDALGetGCPCount(dt);
  
  	  	
  	return res;
  }
 
 /******************************************************************************/ 
//							GetColorInterpretationName
/******************************************************************************/

JNIEXPORT jstring JNICALL Java_es_gva_cit_jgdal_Gdal_getColorInterpretationNameNat
  (JNIEnv *env, jobject obj, jlong cPtr, jint ci){
  	 GDALDatasetH 		*dt  = (GDALDatasetH *) 0 ;
   	 char 				*name = NULL;
   	 jstring 			typeName = NULL;
	 
	 dt = *(GDALDatasetH **)&cPtr;
	 name = GDALGetColorInterpretationName(ci);  	 
	 typeName = (*env)->NewStringUTF(env, name); 
	 return typeName;
  }
 
