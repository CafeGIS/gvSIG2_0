 /**********************************************************************
 * $Id: ogrfeature_interfaz.cpp 7765 2006-10-03 07:05:18Z nacho $
 *
 * Name:     ogrfeature_interfaz.c
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
//								dumpReadable
/******************************************************************************/

JNIEXPORT void JNICALL Java_es_gva_cit_jogr_OGRFeature_dumpReadableNat
  (JNIEnv *env, jobject obj, jlong cPtr, jstring file){
  	 
  	const char *nomfile;
  	FILE *fp;
  	OGRFeature *feature = (OGRFeature *) 0 ;
  	
  	feature = *(OGRFeature **)&cPtr;
  	if(file!=NULL){
	  	nomfile = env->GetStringUTFChars(file, 0);
  		fp = fopen(nomfile,"a+");
	  	if(!fp)return;
	  	feature->DumpReadable(fp);
	  	fclose(fp);
	  	env->ReleaseStringUTFChars(file, nomfile);
	  	return;
  	}
	
  	feature->DumpReadable(NULL);
  	
  	    	 
  }
  
/******************************************************************************/
//								~OGRFeature
/******************************************************************************/

JNIEXPORT void JNICALL Java_es_gva_cit_jogr_OGRFeature_FreeOGRFeatureNat
  (JNIEnv *env, jobject obj, jlong cPtr){
  	
  	OGRFeature *df = (OGRFeature *) 0 ;
  	
  	df = *(OGRFeature **)&cPtr;
  	if(df!=NULL){
  		delete df;
  	}
  }

