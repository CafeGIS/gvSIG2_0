/**********************************************************************
 * $Id: colortable_interfaz.c 7764 2006-10-03 07:04:43Z nacho $
 *
 * Name:     colortable_interfaz.c
 * Project:  JGDAL. Interface java to gdal (Frank Warmerdam).
 * Purpose:  Color table
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
*/
	
#include <jni.h>
#include "gdal.h"
#include "cpl_string.h"
  
/******************************************************************************/
//							GetColorEntryCount
/******************************************************************************/

JNIEXPORT jint JNICALL Java_es_gva_cit_jgdal_GdalColorTable_getColorEntryCountNat
  (JNIEnv *env, jobject obj, jlong cPtr){
  	GDALColorTableH *hTable  = (GDALColorTableH *) 0 ;
  	int res = -1;
  	  	
    hTable = *(GDALColorTableH **)&cPtr;
    
    if(hTable != NULL)
		res = GDALGetColorEntryCount(hTable);	

    return res;
  }
  
/******************************************************************************/
//							GetColorEntryAsRGB
/******************************************************************************/

JNIEXPORT jshortArray JNICALL Java_es_gva_cit_jgdal_GdalColorTable_getColorEntryAsRGBNat
  (JNIEnv *env, jobject obj, jlong cPtr, jint pos){
  	short     			entryValues[4];
  	GDALColorEntry		sEntry;
  	jshortArray			shortarray;
	GDALColorTableH 	*hTable  = (GDALColorTableH *) 0 ;
  	
    hTable = *(GDALColorTableH **)&cPtr;
        
    if(hTable != NULL){
		GDALGetColorEntryAsRGB( hTable, (int)pos, &sEntry );	
		entryValues[0] = sEntry.c1;
	    entryValues[1] = sEntry.c2;
        entryValues[2] = sEntry.c3;
        entryValues[3] = sEntry.c4;
        
        shortarray = (*env)->NewShortArray(env, 4);
        (*env)->SetShortArrayRegion(env, shortarray, 0, 4,(jshort *)entryValues); 
	  	return shortarray;
    }
    return NULL;
  }


