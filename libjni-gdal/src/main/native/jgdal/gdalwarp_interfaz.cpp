/**********************************************************************
 * $Id: gdalwarp_interfaz.cpp,v 1.0 2007/04/10 00:08:29 maquerol Exp $
 *
 * Name:     gdalwarp_interfaz.c
 * Project:  JGDAL. Interface java to gdalwarp (Frank Warmerdam).
 * Purpose:  dataset's Basic Funcions.
 * Author:   Miguel Ángel Querol <miguelangel.querol@iver.es>
 *
 **********************************************************************/
/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
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
* [01] 01-Oct-2005 nbt OpenArray function to support name parameter in char array
*/
#include <jni.h>
#include "gdal.h"
#include "cpl_string.h"

int warpFunction(JNIEnv *env, char *s_srs, char *t_srs, char *source, char *dest, char *format);

JNIEnv *gEnv;
jobject gObj;
jclass gClass;

/******************************************************************************/
//								Warp
/******************************************************************************/

extern "C" JNIEXPORT jint JNICALL Java_es_gva_cit_jgdal_GdalWarp_warpDataset(JNIEnv *env, jobject obj, jstring srcProj, jstring newProj, jstring source, jstring dest, jstring format) {
	gEnv = env;
	gObj = obj;
	gClass = (env)->GetObjectClass(obj);

	const char * oldProj;
	const char * proj;
	const char * src;
	const char * dst;
	const char * form;
	int stat = 0;

	proj = (env)->GetStringUTFChars(newProj, 0);
	src = (env)->GetStringUTFChars(source, 0);
	dst = (env)->GetStringUTFChars(dest, 0);

	if (format != NULL) {
		form = (env)->GetStringUTFChars(format, 0);
		if (srcProj != NULL) {
			oldProj = (env)->GetStringUTFChars(srcProj, 0);
			stat = warpFunction(env, (char *)oldProj, (char *)proj, (char *)src, (char *)dst, (char *)form);
			(env)->ReleaseStringUTFChars(srcProj, oldProj);
		} else {
			stat = warpFunction(env, NULL, (char *)proj, (char *)src, (char *)dst, (char *)form);
		}
		(env)->ReleaseStringUTFChars(format, form);
	} else {
		if (srcProj != NULL) {
			oldProj = (env)->GetStringUTFChars(srcProj, 0);
			stat = warpFunction(env, (char *)oldProj, (char *)proj, (char *)src, (char *)dst, NULL);
			(env)->ReleaseStringUTFChars(srcProj,oldProj);
		} else {
			stat = warpFunction(env, NULL, (char *)proj, (char *)src, (char *)dst, NULL);
		}
	}

	(env)->ReleaseStringUTFChars(newProj,proj);
	(env)->ReleaseStringUTFChars(source,src);
	(env)->ReleaseStringUTFChars(dest,dst);

	return stat;
}

/******************************************************************************/
//								StatusCallback
/******************************************************************************/
//Asigna el tanto por cien de imagen que lleva comprimido en una variable de java
//Ejecuta la función updatePercent de java despues de actualizar la varible.

void CPL_STDCALL statusCallBack(int nPercent) {
	jclass 		GdalWarp = gClass;
	JNIEnv 		*env=gEnv;
	jobject		obj = gObj;
	jmethodID 	metodo;
	jfieldID	fid;
	jint		jpercent = nPercent;

	fid = (env)->GetFieldID(gClass, "porcentaje", "I");
	(env)->SetIntField(obj, fid, jpercent);
}
