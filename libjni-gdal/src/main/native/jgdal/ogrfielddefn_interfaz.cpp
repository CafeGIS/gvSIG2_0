 /**********************************************************************
 * $Id: ogrfielddefn_interfaz.cpp 7765 2006-10-03 07:05:18Z nacho $
 *
 * Name:     ogrfielddefn_interfaz.c
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
//								~OGRFieldDefn
/******************************************************************************/
 
  JNIEXPORT void JNICALL Java_es_gva_cit_jogr_OGRFieldDefn_FreeOGRFieldDefnNat
  (JNIEnv *env, jobject obj, jlong cPtr){
  	
  	OGRFieldDefn 	*fd = (OGRFieldDefn *) 0 ;
  	
  	fd = *(OGRFieldDefn **)&cPtr;
  	if(fd!=NULL){
  		delete fd;
  	}
  }
 
/******************************************************************************/
//								getType
/******************************************************************************/
 
 JNIEXPORT jint JNICALL Java_es_gva_cit_jogr_OGRFieldDefn_getTypeNat
  (JNIEnv *env, jobject obj, jlong cPtr){
  	
  	OGRFieldDefn 	*fd = (OGRFieldDefn *) 0 ;
  	OGRFieldType	ft;
  	
  	fd = *(OGRFieldDefn **)&cPtr;
  	if(fd!=NULL)
  		ft = fd->GetType();
  	
  	return (int)ft;
  }

/******************************************************************************/
//								getNameRef
/******************************************************************************/

JNIEXPORT jstring JNICALL Java_es_gva_cit_jogr_OGRFieldDefn_getNameRefNat
  (JNIEnv *env, jobject obj, jlong cPtr){
  	
  	OGRFieldDefn 	*fd = (OGRFieldDefn *) 0 ;
  	jstring			jstipo;
  	
  	fd = *(OGRFieldDefn **)&cPtr;
  	if(fd!=NULL){
  	  const char *tipo = fd->GetNameRef();
  	  jstipo = env->NewStringUTF(tipo); 
  	}
  	return jstipo;
  }

/******************************************************************************/
//								getFieldTypeName
/******************************************************************************/

JNIEXPORT jstring JNICALL Java_es_gva_cit_jogr_OGRFieldDefn_getFieldTypeNameNat
  (JNIEnv *env, jobject obj, jlong cPtr, jint tipo){
  	
  	OGRFieldDefn	*fd = (OGRFieldDefn *) 0 ;
  	jstring			jsnombretipo;
  	
  	fd = *(OGRFieldDefn **)&cPtr;
  	if(fd!=NULL){
  		OGRFieldType ft;
  		switch(tipo){
  			case 0:ft=OFTInteger;break;
			case 1:ft=OFTIntegerList;break;
			case 2:ft=OFTReal;break;
			case 3:ft=OFTRealList;break;
			case 4:ft=OFTString;break;
			case 5:ft=OFTStringList;break;
			case 6:ft=OFTWideString;break;
			case 7:ft=OFTWideStringList;break;
			case 8:ft=OFTBinary;break;
  		}
  		const char  *nombretipo = fd->GetFieldTypeName(ft);
  		jsnombretipo = env->NewStringUTF(nombretipo);
  	}
  	return jsnombretipo;
  }

/******************************************************************************/
//								getWidth
/******************************************************************************/

JNIEXPORT jint JNICALL Java_es_gva_cit_jogr_JNIBase_getWidthNat
 (JNIEnv *env, jobject obj, jlong cPtr){
  	
  	OGRFieldDefn 	*fd = (OGRFieldDefn *) 0 ;
  	int 			width = -1;
  	
  	fd = *(OGRFieldDefn **)&cPtr;
  	if(fd!=NULL){
  		width = fd->GetWidth();
  	}
  	return width;
  }

/******************************************************************************/
//								getPrecision
/******************************************************************************/

JNIEXPORT jint JNICALL Java_es_gva_cit_jogr_JNIBase_getPrecisionNat
  (JNIEnv *env, jobject obj, jlong cPtr){
  	
  	OGRFieldDefn 	*fd = (OGRFieldDefn *) 0 ;
  	int 			precision = -1;
  	
  	fd = *(OGRFieldDefn **)&cPtr;
  	if(fd!=NULL){
  		precision = fd->GetPrecision();
  	}
  	return precision;
  }

