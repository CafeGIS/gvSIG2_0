/**********************************************************************
 * $Id: crsJniProj.c,v 1.9 2006/09/07 10:38:13
 *
 * Name:     crsJniProj.c
 * Project:  Proj-4.4.9. Cartographic Projections library(Gerald Evenden).
 * Purpose:  Projections Basic Funcions.
 * Author:   Miguel Garcia Jimenez, garciajimenez.miguel@gmail.com
 * Collaborator: Jose Luis Gomez Martinez, jolugomar@gmail.com
 *               David Hernandez Lopez , dhernan@agr-ab.uclm.es
 *
 **********************************************************************/
/* gvSIG. Sistema de Informacion Geografica de la Generalitat Valenciana
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
 */

#include "projects.h"
#include <jni.h>
#include <proj_api.h>

#define arraysize 300

//Realizar una operacion entre CRS, pasando como parametros tres arrays de doubles(puntos)
JNIEXPORT int JNICALL Java_org_gvsig_crs_proj_JNIBaseCrs_operation
(JNIEnv * env, jobject parent, jdoubleArray firstcoord, jdoubleArray secondcoord, jdoubleArray values, jlong src, jlong dest)
{
	int err = 0;
	int i;
	projPJ src_pj, dst_pj; // comprobar si se puede quitar
	jboolean isCopy;
	jdouble *xcoord;
	jdouble *ycoord;
	jdouble *zcoord; 
	jint sizeofdata;
	
	src_pj = (projPJ)src;
	dst_pj = (projPJ)dest;
		
	xcoord = (*env)->GetDoubleArrayElements(env, firstcoord, &isCopy);
	ycoord = (*env)->GetDoubleArrayElements(env, secondcoord, &isCopy);
	zcoord = (*env)->GetDoubleArrayElements(env, values, &isCopy);
	sizeofdata = (*env)->GetArrayLength(env, firstcoord);
	if(src_pj->is_latlong == 1)
	{
		for(i = 0;i<sizeofdata;i++)
		{
			*xcoord = *xcoord / (180/PI);
			*ycoord = *ycoord / (180/PI);

			err = pj_transform( src_pj, dst_pj,1,1, xcoord, ycoord, zcoord);
			if(dst_pj->is_latlong == 1)
			{
				*xcoord = *xcoord * (180/PI);
				*ycoord = *ycoord * (180/PI);

			}
			xcoord++;
			ycoord++;
			zcoord++;
		}
	}
	if(src_pj->is_latlong == 0)
	{
		for(i = 0;i<sizeofdata;i++)
		{
			err = pj_transform( src_pj, dst_pj,1,1, xcoord, ycoord, zcoord);

			if(dst_pj->is_latlong == 1)
			{
				*xcoord = *xcoord * (180/PI);
				*ycoord = *ycoord * (180/PI);
			}
			xcoord++;
			ycoord++;
			zcoord++;
		}
	}
	xcoord = xcoord - sizeofdata;
	ycoord = ycoord - sizeofdata;
	zcoord = zcoord - sizeofdata;
	(* env)->ReleaseDoubleArrayElements(env,firstcoord,(jdouble *) xcoord,0);
	(* env)->ReleaseDoubleArrayElements(env,secondcoord,(jdouble *) ycoord,0);
	(* env)->ReleaseDoubleArrayElements(env,values,(jdouble *) zcoord,0);
	// return 1; // pendiente de analizar error
	return err;
}

//Realizar una operacion entre CRS, pasando como parametros tres doubles(puntos)

JNIEXPORT int JNICALL Java_org_gvsig_crs_proj_JNIBaseCrs_operationSimple
(JNIEnv * env, jobject parent, jdouble firstcoord, jdouble secondcoord, jdouble values, jlong src, jlong dest)
{
	int err = 0;
	int i;
	projPJ src_pj, dst_pj; // comprobar si se puede quitar
	double x,y,z;
	double *xcoord;
	double *ycoord;
	double *zcoord;
	
	src_pj = (projPJ)src;
	dst_pj = (projPJ)dest;

	x=firstcoord;
	y=secondcoord;
	z=values;
	*xcoord = firstcoord;
	*ycoord = secondcoord;
	*zcoord = values;

	if(src_pj->is_latlong == 1)
	{
		*xcoord = *xcoord / (180/PI);
		*ycoord = *ycoord / (180/PI);
	}

	err = pj_transform( src_pj, dst_pj,1,1, xcoord,ycoord,zcoord);

	if(dst_pj->is_latlong == 1)
	{
		*xcoord = *xcoord * (180/PI);
		*ycoord = *ycoord * (180/PI);
	}

	firstcoord =*xcoord;
	secondcoord =*ycoord;
	values =*zcoord;

	// return 1; // pendiente de analizar error
	return err;
}

//Realizar una operacion entre CRS, pasando como parametro un array de tres dobles(puntos)

JNIEXPORT int JNICALL Java_org_gvsig_crs_proj_JNIBaseCrs_operationArraySimple
(JNIEnv * env, jobject parent, jdoubleArray coord, jlong src, jlong dest)
{
	int err = 0;
	projPJ src_pj, dst_pj; // comprobar si se puede quitar
	jdouble *xcoord;
	jdouble *zcoord;
	jdouble *ycoord;
	
	src_pj = (projPJ)src;
	dst_pj = (projPJ)dest;
	
	zcoord = (* env)-> GetDoubleArrayElements(env, coord, NULL);
	xcoord = zcoord++;
	ycoord = zcoord++;

	if(src_pj->is_latlong == 1)
	{
		*xcoord = *xcoord / (180/PI);
		*ycoord = *ycoord / (180/PI);
	}

	err = pj_transform( src_pj, dst_pj,1,1, xcoord, ycoord, zcoord);

	if(dst_pj->is_latlong == 1)
	{
		*xcoord = *xcoord * (180/PI);
		*ycoord = *ycoord * (180/PI);
	}

	(* env)->ReleaseDoubleArrayElements(env,coord,(jdouble *) xcoord,0);

	// return 1; // pendiente de analizar error
	return err;
}

//Crea un CRS a partir de una cadena proj4, devolviendo la direccion de memoria del CRS

JNIEXPORT projPJ JNICALL Java_org_gvsig_crs_proj_JNIBaseCrs_loadCrs
(JNIEnv * env, jobject parent, jstring strCrs)
{
	projPJ src_pj;
	char * srcproj_def = (char *) (*env)->GetStringUTFChars (env, strCrs, 0);
	if (!(src_pj = pj_init_plus(srcproj_def))) return 0;
	return src_pj;
}

//Libera la direccion de memoria del CRS creado

JNIEXPORT void JNICALL Java_org_gvsig_crs_proj_JNIBaseCrs_freeCrs
(JNIEnv * env, jobject parent, jlong crs )
{
	projPJ proj_pj;
	proj_pj = crs;
	pj_free( proj_pj );
}

//Devuelve un 1 si el CRS es latlong,  un 0 en caso contrario o un codigo de error (negativo) si el crs no está bien creado.

JNIEXPORT int JNICALL Java_org_gvsig_crs_proj_JNIBaseCrs_isLatlong
(JNIEnv * env, jobject parent, jlong crs)
{
	projPJ proj_pj = crs;

	int *err = pj_get_errno_ref();
	if(*err>=0)
	return proj_pj->is_latlong;
	else
	return *err;
}

//Devuelve el codigo de error correspondiente al ultimo crs creado.

JNIEXPORT int JNICALL Java_org_gvsig_crs_proj_JNIBaseCrs_getErrno
(JNIEnv * env, jobject parent)
{
	int *err = pj_get_errno_ref();
	return *err;
}

//Traduce un codigo de error a su correspondiente mensaje.

JNIEXPORT jstring JNICALL Java_org_gvsig_crs_proj_JNIBaseCrs_strErrno
(JNIEnv * env, jobject parent, jint errno)
{
	int err = errno;
	char * strerr = pj_strerrno(err);
	return (*env)->NewStringUTF(env,strerr);
}

/*!
 * \brief
 * retrieves projection parameters
 *
 * JNI informations:
 * Class:     org_proj4_Projections
 * Method:    getProjInfo
 * Signature: (Ljava/lang/String;)Ljava/lang/String;
 *
 *
 * \param env - parameter used by jni (see JNI specification)
 * \param parent - parameter used by jni (see JNI specification)
 * \param projdefinition - definition of the projection
 */
/*
 JNIEXPORT jstring JNICALL Java_org_proj4_Projections_getProjInfo
 (JNIEnv * env, jobject parent, jstring projdefinition)
 {
 PJ *pj;
 char * pjdesc;
 char info[arraysize];

 char * proj_def = (char *) (*env)->GetStringUTFChars (env, projdefinition, 0);

 if (!(pj = pj_init_plus(proj_def)))
 exit(1);

 // put together all the info of the projection and free the pointer to pjdesc
 pjdesc = pj_get_def(pj, 0);
 strcpy(info,pjdesc);
 pj_dalloc(pjdesc);

 return (*env)->NewStringUTF(env,info);
 }
 */

/*!
 * \brief
 * retrieves ellipsoid parameters
 *
 * JNI informations:
 * Class:     org_proj4_Projections
 * Method:    getEllipsInfo
 * Signature: (Ljava/lang/String;)Ljava/lang/String;
 *
 *
 * \param env - parameter used by jni (see JNI specification)
 * \param parent - parameter used by jni (see JNI specification)
 * \param projdefinition - definition of the projection
 */
/*
 JNIEXPORT jstring JNICALL Java_org_proj4_Projections_getEllipsInfo
 (JNIEnv * env, jobject parent, jstring projdefinition)
 {
 PJ *pj;
 char * pjdesc;
 char ellipseinfo[arraysize];
 char temp[50];

 char * proj_def = (char *) (*env)->GetStringUTFChars (env, projdefinition, 0);

 if (!(pj = pj_init_plus(proj_def)))
 exit(1);

 // put together all the info of the ellipsoid
 //  sprintf(temp,"name: %s;", pj->descr);
 sprintf(temp,"name: not available;");
 strcpy(ellipseinfo,temp);
 sprintf(temp,"a: %lf;", pj->a);
 strcat(ellipseinfo,temp);
 sprintf(temp,"e: %lf;", pj->e);
 strcat(ellipseinfo,temp);
 sprintf(temp,"es: %lf;", pj->es);
 strcat(ellipseinfo,temp);
 sprintf(temp,"ra: %lf;", pj->ra);
 strcat(ellipseinfo,temp);
 sprintf(temp,"one_es: %lf;", pj->one_es);
 strcat(ellipseinfo,temp);
 sprintf(temp,"rone_es: %lf;", pj->rone_es);
 strcat(ellipseinfo,temp);
 sprintf(temp,"lam0: %lf;", pj->lam0);
 strcat(ellipseinfo,temp);
 sprintf(temp,"phi0: %lf;", pj->phi0);
 strcat(ellipseinfo,temp);
 sprintf(temp,"x0: %lf;", pj->x0);
 strcat(ellipseinfo,temp);
 sprintf(temp,"y0: %lf;", pj->y0);
 strcat(ellipseinfo,temp);
 sprintf(temp,"k0: %lf;", pj->k0);
 strcat(ellipseinfo,temp);
 sprintf(temp,"to_meter: %lf;", pj->to_meter);
 strcat(ellipseinfo,temp);
 sprintf(temp,"fr_meter: %lf;", pj->fr_meter);
 strcat(ellipseinfo,temp);

 return (*env)->NewStringUTF(env,ellipseinfo);
 }
 */
