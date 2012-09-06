 /**********************************************************************
 * $Id: ogrspatialreference_interfaz.c,v 1.9 2006/06/10 12:50:13
 *
 * Name:     OGRSpatialReference_interfaz.c
 * Project:  JGDAL. Interface java to gdal (Frank Warmerdam).
 * Purpose:  dataset's Basic Funcions.
 * Author:   Miguel Garcia Jiemenez, garciajimenez.miguel@gmail.com
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
#include "ogr_api.h"
#include "ogr_srs_api.h"



/******************************************************************************/
//							OGRSpatialReference
/******************************************************************************/

JNIEXPORT jlong JNICALL Java_org_gvsig_jogr_OGRSpatialReference_OGRSpatialReferenceNat
  (JNIEnv * env, jobject obj){

  	OGRSpatialReferenceH *hSRS;
  	jlong jresult=0;
  	hSRS=(OGRSpatialReferenceH*)malloc(sizeof(OGRSpatialReferenceH));

 	*hSRS = OSRNewSpatialReference( NULL );
  	*(OGRSpatialReferenceH **)&jresult = hSRS;

  	//printf("-------->%ld\n",(long)jresult);
  	return (long)jresult;
  }

/******************************************************************************/
//								exportToWkt
/******************************************************************************/

JNIEXPORT jstring JNICALL Java_org_gvsig_jogr_OGRSpatialReference_exportToWktNat
  (JNIEnv *env, jobject obj, jlong cPtr){

  	OGRSpatialReferenceH *hSRS = (OGRSpatialReferenceH *) 0 ;
  	jstring wkt;
  	char *pszSRS_WKT = NULL;

  	hSRS = *(OGRSpatialReferenceH **)&cPtr;

  	if(hSRS!=NULL){
  		 OSRExportToWkt( *hSRS, &pszSRS_WKT );
  		 if(pszSRS_WKT!=NULL){
			wkt = (*env)->NewStringUTF(env, pszSRS_WKT);
			return wkt;
  		 }
  	}

  	return NULL;
  }
  
/******************************************************************************/
//							OSRDestroySpatialReference
/******************************************************************************/

JNIEXPORT void JNICALL Java_org_gvsig_jogr_OGRSpatialReference_OGRDestroySpatialReferenceNat
  (JNIEnv *env, jobject obj, jlong cPtr){

  	OGRSpatialReferenceH *hSRS = (OGRSpatialReferenceH *) 0 ;

  	hSRS = *(OGRSpatialReferenceH **)&cPtr;

  	if(hSRS!=NULL){
  		OSRDestroySpatialReference(*hSRS);
  	}
  }

/******************************************************************************/
//								importFromWkt
/******************************************************************************/

JNIEXPORT jint JNICALL Java_org_gvsig_jogr_OGRSpatialReference_importFromWktNat
  (JNIEnv *env, jobject obj, jlong cPtr, jstring cs){

    OGRSpatialReferenceH *hSRS = (OGRSpatialReferenceH *) 0 ;
    jint err;

    hSRS = *(OGRSpatialReferenceH **)&cPtr;
    if(hSRS!=NULL){
		char *cad = (*env)->GetStringUTFChars(env, cs, 0);
    	err = OSRImportFromWkt( *hSRS,&cad);
    	return err;
    }

    return err;
 }

/******************************************************************************/
//								setUTM
/******************************************************************************/

JNIEXPORT jint JNICALL Java_org_gvsig_jogr_OGRSpatialReference_setUTMNat
  (JNIEnv *env, jobject obj, jlong cPtr, jint zona, jint norte_sur){

  	OGRSpatialReferenceH *hSRS = (OGRSpatialReferenceH *) 0 ;
  	jint err;

  	hSRS = *(OGRSpatialReferenceH **)&cPtr;
  	if(hSRS!=NULL){
  		err = OSRSetUTM( *hSRS, zona, norte_sur );
 		return err;
  	}
  	return err;
  }

/******************************************************************************/
//							setWellKnownGeogCS
/******************************************************************************/

JNIEXPORT jint JNICALL Java_org_gvsig_jogr_OGRSpatialReference_setWellKnownGeogCSNat
  (JNIEnv *env, jobject obj, jlong cPtr, jstring cs){

  	OGRSpatialReferenceH *hSRS = (OGRSpatialReferenceH *) 0;
  	jint err;

  	hSRS = *(OGRSpatialReferenceH **)&cPtr;
  	if(hSRS!=NULL){
  		const char *coord_sys = (*env)->GetStringUTFChars(env, cs, 0);
  		err = OSRSetWellKnownGeogCS( *hSRS, coord_sys );
  		(*env)->ReleaseStringUTFChars(env, cs, coord_sys);
  		return err;
  	}

  	return err;
  }


/******************************************************************************/
//								importFromEPSG
/******************************************************************************/

JNIEXPORT jint JNICALL Java_org_gvsig_jogr_OGRSpatialReference_importFromEPSGNat
  (JNIEnv *env, jobject obj, jlong cPtr, jint cod){

	OGRSpatialReferenceH *hSRS = (OGRSpatialReferenceH *) 0 ;
	jint err;
	char *pszSRS_WKT = NULL;

  	hSRS = *(OGRSpatialReferenceH **)&cPtr;

  	if(hSRS!=NULL){
  		 err = OSRImportFromEPSG(*hSRS, cod );
  		  OSRExportToWkt(*hSRS, &pszSRS_WKT);
  	}

  	return err;
  }


/******************************************************************************/
//								importFromProj4
/******************************************************************************/

JNIEXPORT jint JNICALL Java_org_gvsig_jogr_OGRSpatialReference_importFromProj4Nat
  (JNIEnv *env, jobject obj, jlong cPtr, jstring cs){

  	OGRSpatialReferenceH *hSRS = (OGRSpatialReferenceH *) 0 ;
  	jint err;

  	hSRS = *(OGRSpatialReferenceH **)&cPtr;
  	if(hSRS!=NULL){
  		const char *coord_sys = (*env)->GetStringUTFChars(env, cs, 0);
  		err = OSRImportFromProj4( *hSRS, coord_sys );
  		(*env)->ReleaseStringUTFChars(env, cs, coord_sys);
  		return err;
  	}

  	return err;
  }

/******************************************************************************/
//								exportToProj4
/******************************************************************************/

JNIEXPORT jstring JNICALL Java_org_gvsig_jogr_OGRSpatialReference_exportToProj4Nat
  (JNIEnv *env, jobject obj, jlong cPtr){

  	OGRSpatialReferenceH *hSRS = (OGRSpatialReferenceH *) 0 ;
  	jstring wkt;
  	char *pszSRS_WKT = NULL;

  	hSRS = *(OGRSpatialReferenceH **)&cPtr;

  	if(hSRS!=NULL){
  		 OSRExportToProj4( *hSRS, &pszSRS_WKT );
  		 if(pszSRS_WKT!=NULL){
			wkt = (*env)->NewStringUTF(env, pszSRS_WKT);
			return wkt;
  		 }
  	}

  	return NULL;
  }

/******************************************************************************/
//								importFromPCI
/******************************************************************************/

JNIEXPORT jint JNICALL Java_org_gvsig_jogr_OGRSpatialReference_importFromPCINat
  (JNIEnv *env, jobject obj, jlong cPtr,jstring cod, jstring cs, jdoubleArray coord){

  	OGRSpatialReferenceH *hSRS = (OGRSpatialReferenceH *) 0 ;
  	jint err;
  	double *xcoord = (* env)-> GetDoubleArrayElements(env, coord, NULL);

  	hSRS = *(OGRSpatialReferenceH **)&cPtr;
  	if(hSRS!=NULL){
  		const char *coord_sys = (*env)->GetStringUTFChars(env, cs, 0);
  		const char *cod_sys = (*env)->GetStringUTFChars(env, cod, 0);
  		err = OSRImportFromPCI( *hSRS, cod_sys, coord_sys, xcoord );
  		(*env)->ReleaseStringUTFChars(env, cs, coord_sys);
  		return err;
  	}
  	(* env)->ReleaseDoubleArrayElements(env,coord,(jdouble *) xcoord,JNI_COMMIT);

  	return err;
  }

/******************************************************************************/
//								importFromESRI
/******************************************************************************/

JNIEXPORT jint JNICALL Java_org_gvsig_jogr_OGRSpatialReference_importFromESRINat
  (JNIEnv *env, jobject obj, jlong cPtr, jstring cs){

    OGRSpatialReferenceH *hSRS = (OGRSpatialReferenceH *) 0 ;
    jint err;

    hSRS = *(OGRSpatialReferenceH **)&cPtr;
    if(hSRS!=NULL){
		char *cad = (*env)->GetStringUTFChars(env, cs, 0);
    	err = OSRImportFromWkt( *hSRS,&cad);
    	return err;
    }

    return err;
 }


/******************************************************************************/
//								importFromUSGS
/******************************************************************************/

JNIEXPORT jint JNICALL Java_org_gvsig_jogr_OGRSpatialReference_importFromUSGSNat
  (JNIEnv *env, jobject obj, jlong cPtr,jlong iProjSys ,jlong iZone, jdoubleArray padfPrjParams, jlong iDatum){

	OGRSpatialReferenceH *hSRS = (OGRSpatialReferenceH *) 0;
	jint err;

	double *params = (* env)-> GetDoubleArrayElements(env, padfPrjParams, NULL);
	jint sizeofdata = (* env)-> GetArrayLength(env,padfPrjParams);

	if(sizeofdata != 15){
		printf("El tama�o del array de dobles debe ser 15");
	 	return 5;
	}

	hSRS = *(OGRSpatialReferenceH **)&cPtr;
	if(hSRS!=NULL){
		err = OSRImportFromUSGS(*hSRS, iProjSys, iZone, params, iDatum);
		return err;
	}

	return err;
}

/******************************************************************************/
//								isGeographic
/******************************************************************************/

JNIEXPORT jboolean JNICALL Java_org_gvsig_jogr_OGRSpatialReference_isGeographicNat
  (JNIEnv *env, jobject obj, jlong cPtr){

    OGRSpatialReferenceH *hSRS = (OGRSpatialReferenceH *) 0 ;
    jint err;

    hSRS = *(OGRSpatialReferenceH **)&cPtr;
    if(hSRS!=NULL) {
    	err = OSRIsGeographic( *hSRS);
    	return err;
    }

    return err;
 }
 
/******************************************************************************/
//								getAuthorityCode
/******************************************************************************/

JNIEXPORT jstring JNICALL Java_org_gvsig_jogr_OGRSpatialReference_getAuthorityCodeNat
  (JNIEnv *env, jobject obj, jlong cPtr, jstring cs){

    OGRSpatialReferenceH *hSRS = (OGRSpatialReferenceH *) 0 ;
    jint err;
    jstring authority_codeUTF;
    char *cad = NULL;
    char *authority_code = NULL;

    hSRS = *(OGRSpatialReferenceH **)&cPtr;
    if(hSRS != NULL) {
    	if(cs != NULL) {
			cad = (*env)->GetStringUTFChars(env, cs, 0);
		}
		authority_code = OSRGetAuthorityCode( *hSRS, cad);
    	authority_codeUTF = (*env)->NewStringUTF(env, authority_code);
    	return authority_codeUTF;
    }

    return NULL;
 }

/******************************************************************************/
//								getAuthorityName
/******************************************************************************/

JNIEXPORT jstring JNICALL Java_org_gvsig_jogr_OGRSpatialReference_getAuthorityNameNat
  (JNIEnv *env, jobject obj, jlong cPtr, jstring cs){

    OGRSpatialReferenceH *hSRS = (OGRSpatialReferenceH *) 0 ;
    jint err;
    jstring authority_nameUTF;
    char *cad = NULL;
    char *authority_name = NULL;
    
    hSRS = *(OGRSpatialReferenceH **)&cPtr;
    if(hSRS != NULL) {
	    if(cs != NULL) {
			cad = (*env)->GetStringUTFChars(env, cs, 0);
		}
    	authority_name = OSRGetAuthorityName( *hSRS, cad);
    	authority_nameUTF = (*env)->NewStringUTF(env, authority_name);
    	return authority_nameUTF;
    }

    return NULL;
 }
 

///////////////////////////////////////////////////////////// 
//////////////LLAMADAS TEMPORALES PARA JCRS//////////////////
/////////////////////////////////////////////////////////////



/******************************************************************************/
//							OGRSpatialReference
/******************************************************************************/

JNIEXPORT jlong JNICALL Java_org_gvsig_crs_ogr_JNIBase_OGRSpatialReferenceNat
  (JNIEnv * env, jobject obj){

  	OGRSpatialReferenceH *hSRS;
  	jlong jresult=0;
  	hSRS=(OGRSpatialReferenceH*)malloc(sizeof(OGRSpatialReferenceH));

 	*hSRS = OSRNewSpatialReference( NULL );
  	*(OGRSpatialReferenceH **)&jresult = hSRS;

  	//printf("-------->%ld\n",(long)jresult);
  	return (long)jresult;
  }

/******************************************************************************/
//								exportToWkt
/******************************************************************************/

JNIEXPORT jstring JNICALL Java_org_gvsig_crs_ogr_JNIBaseCRS_exportToWktNat
  (JNIEnv *env, jobject obj, jlong cPtr){

  	OGRSpatialReferenceH *hSRS = (OGRSpatialReferenceH *) 0 ;
  	jstring wkt;
  	char *pszSRS_WKT = NULL;

  	hSRS = *(OGRSpatialReferenceH **)&cPtr;

  	if(hSRS!=NULL){
  		 OSRExportToWkt( *hSRS, &pszSRS_WKT );
  		 if(pszSRS_WKT!=NULL){
			wkt = (*env)->NewStringUTF(env, pszSRS_WKT);
			return wkt;
  		 }
  	}

  	return NULL;
  }
/******************************************************************************/
//							OSRDestroySpatialReference
/******************************************************************************/

JNIEXPORT void JNICALL Java_org_gvsig_crs_ogr_JNIBase_OGRDestroySpatialReferenceNat
  (JNIEnv *env, jobject obj, jlong cPtr){

  	OGRSpatialReferenceH *hSRS = (OGRSpatialReferenceH *) 0 ;

  	hSRS = *(OGRSpatialReferenceH **)&cPtr;

  	if(hSRS!=NULL){
  		OSRDestroySpatialReference(*hSRS);
  	}
  }

/******************************************************************************/
//								importFromWkt
/******************************************************************************/
JNIEXPORT jint JNICALL Java_org_gvsig_crs_ogr_JNIBaseCRS_importFromWktNat
  (JNIEnv *env, jobject obj, jlong cPtr, jstring cs){

    OGRSpatialReferenceH *hSRS = (OGRSpatialReferenceH *) 0 ;
    jint err;

    hSRS = *(OGRSpatialReferenceH **)&cPtr;
    if(hSRS!=NULL){
		char *cad = (*env)->GetStringUTFChars(env, cs, 0);
    	err = OSRImportFromWkt( *hSRS,&cad);
    	return err;
    }

    return err;
 }

/******************************************************************************/
//								setUTM
/******************************************************************************/

JNIEXPORT jint JNICALL Java_org_gvsig_crs_ogr_JNIBaseCRS_setUTMNat
  (JNIEnv *env, jobject obj, jlong cPtr, jint zona, jint norte_sur){

  	OGRSpatialReferenceH *hSRS = (OGRSpatialReferenceH *) 0 ;
  	jint err;

  	hSRS = *(OGRSpatialReferenceH **)&cPtr;
  	if(hSRS!=NULL){
  		err = OSRSetUTM( *hSRS, zona, norte_sur );
 		return err;
  	}
  	return err;
  }

/******************************************************************************/
//							setWellKnownGeogCS
/******************************************************************************/

JNIEXPORT jint JNICALL Java_org_gvsig_crs_ogr_JNIBaseCRS_setWellKnownGeogCSNat
  (JNIEnv *env, jobject obj, jlong cPtr, jstring cs){

  	OGRSpatialReferenceH *hSRS = (OGRSpatialReferenceH *) 0;
  	jint err;

  	hSRS = *(OGRSpatialReferenceH **)&cPtr;
  	if(hSRS!=NULL){
  		const char *coord_sys = (*env)->GetStringUTFChars(env, cs, 0);
  		err = OSRSetWellKnownGeogCS( *hSRS, coord_sys );
  		(*env)->ReleaseStringUTFChars(env, cs, coord_sys);
  		return err;
  	}

  	return err;
  }


/******************************************************************************/
//								importFromEPSG
/******************************************************************************/

JNIEXPORT jint JNICALL Java_org_gvsig_crs_ogr_JNIBaseCRS_importFromEPSGNat
  (JNIEnv *env, jobject obj, jlong cPtr, jint cod){

	OGRSpatialReferenceH *hSRS = (OGRSpatialReferenceH *) 0 ;
	jint err;
	char *pszSRS_WKT = NULL;

  	hSRS = *(OGRSpatialReferenceH **)&cPtr;

  	if(hSRS!=NULL){
  		 err = OSRImportFromEPSG(*hSRS, cod );
  		  OSRExportToWkt(*hSRS, &pszSRS_WKT);
  	}

  	return err;
  }


/******************************************************************************/
//								importFromProj4
/******************************************************************************/

JNIEXPORT jint JNICALL Java_org_gvsig_crs_ogr_JNIBaseCRS_importFromProj4Nat
  (JNIEnv *env, jobject obj, jlong cPtr, jstring cs){

  	OGRSpatialReferenceH *hSRS = (OGRSpatialReferenceH *) 0 ;
  	jint err;

  	hSRS = *(OGRSpatialReferenceH **)&cPtr;
  	if(hSRS!=NULL){
  		const char *coord_sys = (*env)->GetStringUTFChars(env, cs, 0);
  		err = OSRImportFromProj4( *hSRS, coord_sys );
  		(*env)->ReleaseStringUTFChars(env, cs, coord_sys);
  		return err;
  	}

  	return err;
  }

/******************************************************************************/
//								exportToProj4
/******************************************************************************/

JNIEXPORT jstring JNICALL Java_org_gvsig_crs_ogr_JNIBaseCRS_exportToProj4Nat
  (JNIEnv *env, jobject obj, jlong cPtr){

  	OGRSpatialReferenceH *hSRS = (OGRSpatialReferenceH *) 0 ;
  	jstring wkt;
  	char *pszSRS_WKT = NULL;

  	hSRS = *(OGRSpatialReferenceH **)&cPtr;

  	if(hSRS!=NULL){
  		 OSRExportToProj4( *hSRS, &pszSRS_WKT );
  		 if(pszSRS_WKT!=NULL){
			wkt = (*env)->NewStringUTF(env, pszSRS_WKT);
			return wkt;
  		 }
  	}

  	return NULL;
  }

/******************************************************************************/
//								importFromPCI
/******************************************************************************/

JNIEXPORT jint JNICALL Java_org_gvsig_crs_ogr_JNIBaseCRS_importFromPCINat
  (JNIEnv *env, jobject obj, jlong cPtr,jstring cod, jstring cs, jdoubleArray coord){

  	OGRSpatialReferenceH *hSRS = (OGRSpatialReferenceH *) 0 ;
  	jint err;
  	double *xcoord = (* env)-> GetDoubleArrayElements(env, coord, NULL);

  	hSRS = *(OGRSpatialReferenceH **)&cPtr;
  	if(hSRS!=NULL){
  		const char *coord_sys = (*env)->GetStringUTFChars(env, cs, 0);
  		const char *cod_sys = (*env)->GetStringUTFChars(env, cod, 0);
  		err = OSRImportFromPCI( *hSRS, cod_sys, coord_sys, xcoord );
  		(*env)->ReleaseStringUTFChars(env, cs, coord_sys);
  		return err;
  	}
  	(* env)->ReleaseDoubleArrayElements(env,coord,(jdouble *) xcoord,JNI_COMMIT);

  	return err;
  }

/******************************************************************************/
//								importFromESRI
/******************************************************************************/
JNIEXPORT jint JNICALL Java_org_gvsig_crs_ogr_JNIBaseCRS_importFromESRINat
  (JNIEnv *env, jobject obj, jlong cPtr, jstring cs){

    OGRSpatialReferenceH *hSRS = (OGRSpatialReferenceH *) 0 ;
    jint err;

    hSRS = *(OGRSpatialReferenceH **)&cPtr;
    if(hSRS!=NULL){
		char *cad = (*env)->GetStringUTFChars(env, cs, 0);
    	err = OSRImportFromWkt( *hSRS,&cad);
    	return err;
    }

    return err;
 }


/******************************************************************************/
//								importFromUSGS
/******************************************************************************/
JNIEXPORT jint JNICALL Java_org_gvsig_crs_ogr_JNIBaseCRS_importFromUSGSNat
  (JNIEnv *env, jobject obj, jlong cPtr,jlong iProjSys ,jlong iZone, jdoubleArray padfPrjParams, jlong iDatum){

	OGRSpatialReferenceH *hSRS = (OGRSpatialReferenceH *) 0;
	jint err;

	double *params = (* env)-> GetDoubleArrayElements(env, padfPrjParams, NULL);
	jint sizeofdata = (* env)-> GetArrayLength(env,padfPrjParams);

	if(sizeofdata != 15){
		printf("El tama�o del array de dobles debe ser 15");
	 	return 5;
	}

	hSRS = *(OGRSpatialReferenceH **)&cPtr;
	if(hSRS!=NULL){
		err = OSRImportFromUSGS(*hSRS, iProjSys, iZone, params, iDatum);
		return err;
	}

	return err;
}

