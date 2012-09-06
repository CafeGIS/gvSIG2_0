/**********************************************************************
 * $Id: rasterband_interfaz.c 15691 2007-10-31 10:49:53Z nbrodin $
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
  
/******************************************************************************/
//							GetRasterBandXSize
/******************************************************************************/


JNIEXPORT jint JNICALL Java_es_gva_cit_jgdal_JNIBase_getRasterBandXSizeNat
  (JNIEnv *env, jobject obj, jlong cPtr){
  	
  	GDALRasterBandH *rb  = (GDALRasterBandH *) 0 ;
  	int res = -1;
  	  	
    rb = *(GDALRasterBandH **)&cPtr;
    
    if(rb!=NULL)
    	res = GDALGetRasterBandXSize(rb);	


    return res;
  }
  
  
/******************************************************************************/ 
//							GetRasterBandYSize
/******************************************************************************/


JNIEXPORT jint JNICALL Java_es_gva_cit_jgdal_JNIBase_getRasterBandYSizeNat
  (JNIEnv *env, jobject obj, jlong cPtr){
  	
  	GDALRasterBandH *rb  = (GDALRasterBandH *) 0 ;
  	int res = -1;
  	  	
    rb = *(GDALRasterBandH **)&cPtr;
    
    if(rb!=NULL)
    	res = GDALGetRasterBandYSize(rb);	

    return res;
  	
  }
  
 /****************************************************************************/
 //							 GetOverviewCount
 /******************************************************************************/
  
  
  JNIEXPORT jint JNICALL Java_es_gva_cit_jgdal_JNIBase_getOverviewCountNat
  (JNIEnv *env, jobject obj, jlong cPtr){
  	
  	GDALRasterBandH *rb  = (GDALRasterBandH *) 0 ;
  	int res = -1;
  	  	
    rb = *(GDALRasterBandH **)&cPtr;
    
    if(rb!=NULL)
		res = GDALGetOverviewCount(rb);	

    return res;
    
  }
  
/******************************************************************************/   
//								GetOverview
/******************************************************************************/


JNIEXPORT jlong JNICALL Java_es_gva_cit_jgdal_GdalRasterBand_getOverviewNat
  (JNIEnv *env, jobject obj, jlong cPtr, jint noverview){
  	
	GDALRasterBandH *rb  = (GDALRasterBandH *) 0 ;
	GDALRasterBandH *res = NULL;
	jlong jresult = 0;
  	
	rb = *(GDALRasterBandH **)&cPtr;
    
    if(rb!=NULL)
    	res = GDALGetOverview(rb,(int)noverview);
    
    *(GDALDatasetH **)&jresult = res;
        
    if(res==NULL)return -1;
    else return jresult;
	  	
  }
  
/******************************************************************************/     
//								min
/******************************************************************************/    
  
/*int min(int x, int y){
	if(x < y)
		return x;
	else
		return y;	
}*/
  
/******************************************************************************/     
//								ReadRaster
/******************************************************************************/  
  
  
JNIEXPORT jobject JNICALL Java_es_gva_cit_jgdal_GdalRasterBand_readRasterNat
  (JNIEnv *env, jobject obj, jlong cPtr, jint nXOff, jint nYOff, jint nXSize, jint nYSize, jint BufXSize, jint BufYSize, jint eBufType){

  		jclass class_gdalbuffer;
  		jmethodID metodo;
  		jobject obj_gdalbuffer = NULL;
  		jfieldID id_campo;
  		
  		GDALRasterBandH *rb  = (GDALRasterBandH *) 0 ;
  		
  		jbyteArray bytearray;
  		jshortArray shortarray;
  		jintArray intarray;
  		jfloatArray floatarray;
  		jdoubleArray doublearray;
  		
  		unsigned char *pabyScanLineBuf;
  		int *buffInt=NULL;
  		unsigned int *buffUInt=NULL;
  		long *buffLong=NULL;
  		unsigned long *buffULong=NULL;
  		float *buffFloat=NULL;
  		double *buffDouble=NULL;
  		 		
	    rb = *(GDALRasterBandH **)&cPtr;
  		
  		if(rb!=NULL){
  			
		  //Creamos el objeto java que contendr� la l�nea
  		
	  		class_gdalbuffer = (*env)->FindClass (env, "es/gva/cit/jgdal/GdalBuffer");
	  		
	  		if(eBufType==1){
	  			
	  			metodo = (*env)->GetMethodID(env, class_gdalbuffer, "reservaByte", "(I)V");
		  			
		  		obj_gdalbuffer = (*env)->NewObject (env,class_gdalbuffer,metodo,(int)(BufXSize*BufYSize));
			  	id_campo = (*env)->GetFieldID(env, class_gdalbuffer, "buffByte", "[B");
		  		  		
				//Reservamos memoria para el buffer y lo cargamos llamando a GDALRasterIO
		  		  		
		  		pabyScanLineBuf = (unsigned char*) CPLMalloc(sizeof(unsigned char) *(int)BufXSize * (int)BufYSize);
		  		GDALRasterIO(rb, GF_Read,  (int)nXOff, (int)nYOff, (int)nXSize, (int)nYSize, pabyScanLineBuf, (int)BufXSize, (int)BufYSize, GDT_Byte, 0, 0);  	
				bytearray = (*env)->NewByteArray(env,(int)(BufXSize*BufYSize));
				if(bytearray!=NULL){
				  	(*env)->SetByteArrayRegion(env, bytearray, 0, (int)(BufXSize*BufYSize),(jbyte *)pabyScanLineBuf); 
				  	(*env)->SetObjectField(env, obj_gdalbuffer, id_campo, bytearray);
				}
				if(pabyScanLineBuf != NULL)CPLFree(pabyScanLineBuf);
				
	  		}else
	  		
	  		//------------------------------------------------------------------
	  		
	  		if(eBufType==2 || eBufType==3 || eBufType==8){
	  			
	  			metodo = (*env)->GetMethodID(env, class_gdalbuffer, "reservaShort", "(I)V");
	  			obj_gdalbuffer = (*env)->NewObject (env,class_gdalbuffer,metodo,(int)(BufXSize*BufYSize));
		  		id_campo = (*env)->GetFieldID(env, class_gdalbuffer, "buffShort", "[S");
	  		  		
			   //Reservamos memoria para el buffer y lo cargamos llamando a GDALRasterIO
	  		  			  			
	  			if(eBufType==2){
	  				buffUInt = (unsigned int*) CPLMalloc(sizeof(unsigned int) *(int)BufXSize * (int)BufYSize);
	  				GDALRasterIO(rb, GF_Read,  (int)nXOff, (int)nYOff, (int)nXSize, (int)nYSize, buffUInt, (int)BufXSize, (int)BufYSize, GDT_UInt16, 0, 0);
	  			  	shortarray = (*env)->NewShortArray(env,(int)(BufXSize*BufYSize));
			    	if(shortarray!=NULL){
					  	(*env)->SetShortArrayRegion(env, shortarray, 0, (int)(BufXSize*BufYSize),(jshort *)buffUInt); 
					  	(*env)->SetObjectField(env, obj_gdalbuffer, id_campo, shortarray);
					}
					if(buffUInt != NULL)CPLFree(buffUInt);
					return obj_gdalbuffer;		
	  			}
	  			
	  			buffInt = (int*) CPLMalloc(sizeof(int) *(int)BufXSize * (int)BufYSize);
	  			GDALRasterIO(rb, GF_Read,  (int)nXOff, (int)nYOff, (int)nXSize, (int)nYSize, buffInt, (int)BufXSize, (int)BufYSize, (int)eBufType, 0, 0);
	  			shortarray = (*env)->NewShortArray(env,(int)(BufXSize*BufYSize));
			    if(shortarray!=NULL){
				  	(*env)->SetShortArrayRegion(env, shortarray, 0, (int)(BufXSize*BufYSize),(jshort *)buffInt); 
				 	(*env)->SetObjectField(env, obj_gdalbuffer, id_campo, shortarray);
				}

				if(buffInt != NULL)CPLFree(buffInt);
				
	  		}else
	  		
	  		//------------------------------------------------------------------
	  		
	  		if(eBufType==4 || eBufType==5 || eBufType==9){
	  			
	  			metodo = (*env)->GetMethodID(env, class_gdalbuffer, "reservaInt", "(I)V");
	  			obj_gdalbuffer = (*env)->NewObject (env,class_gdalbuffer,metodo,(int)(BufXSize*BufYSize));
		  		id_campo = (*env)->GetFieldID(env, class_gdalbuffer, "buffInt", "[I");
	  		  		
			   //Reservamos memoria para el buffer y lo cargamos llamando a GDALRasterIO
	  		  			  			
	  			if(eBufType==4){
	  				buffULong = (unsigned long*) CPLMalloc(sizeof(unsigned long) *(int)BufXSize * (int)BufYSize);
	  				GDALRasterIO(rb, GF_Read,  (int)nXOff, (int)nYOff, (int)nXSize, (int)nYSize, buffULong, (int)BufXSize, (int)BufYSize, GDT_UInt32, 0, 0);
	  				intarray = (*env)->NewIntArray(env,(int)(BufXSize*BufYSize));
			    	if(intarray!=NULL){
					  	(*env)->SetIntArrayRegion(env, intarray, 0, (int)(BufXSize*BufYSize),(jint *)buffULong); 
					  	(*env)->SetObjectField(env, obj_gdalbuffer, id_campo, intarray);
					}
					if(buffULong != NULL)CPLFree(buffULong);
					return obj_gdalbuffer;
	  			}
	  			
	  			buffLong = (long*) CPLMalloc(sizeof(long) *(int)BufXSize * (int)BufYSize);
	  			GDALRasterIO(rb, GF_Read,  (int)nXOff, (int)nYOff, (int)nXSize, (int)nYSize, buffLong, (int)BufXSize, (int)BufYSize, (int)eBufType, 0, 0);
	  			intarray = (*env)->NewIntArray(env,(int)(BufXSize*BufYSize));
			    if(intarray!=NULL){
				  	(*env)->SetIntArrayRegion(env, intarray, 0, (int)(BufXSize*BufYSize),(jint *)buffLong); 
				 	(*env)->SetObjectField(env, obj_gdalbuffer, id_campo, intarray);
				}

				if(buffLong != NULL)CPLFree(buffLong);
				
	  		}else
	  		
	  		//------------------------------------------------------------------
	  		
	  		if(eBufType==6 || eBufType==10){
	  			
	  			metodo = (*env)->GetMethodID(env, class_gdalbuffer, "reservaFloat", "(I)V");
	  			obj_gdalbuffer = (*env)->NewObject (env,class_gdalbuffer,metodo,(int)(BufXSize*BufYSize));
		  		id_campo = (*env)->GetFieldID(env, class_gdalbuffer, "buffFloat", "[F");
	  		  		
			   	//Reservamos memoria para el buffer y lo cargamos llamando a GDALRasterIO
			   
			   	buffFloat = (float*) CPLMalloc(sizeof(float) *(int)BufXSize * (int)BufYSize);
	  			GDALRasterIO(rb, GF_Read,  (int)nXOff, (int)nYOff, (int)nXSize, (int)nYSize, buffFloat, (int)BufXSize, (int)BufYSize, (int)eBufType, 0, 0);
	  			floatarray = (*env)->NewFloatArray(env,(int)(BufXSize*BufYSize));
			    if(floatarray!=NULL){
				  	(*env)->SetFloatArrayRegion(env, floatarray, 0, (int)(BufXSize*BufYSize),(jfloat *)buffFloat); 
				 	(*env)->SetObjectField(env, obj_gdalbuffer, id_campo, floatarray);
				}

				if(buffFloat != NULL)CPLFree(buffFloat);
	  		}else
	  		
	  		//------------------------------------------------------------------
	  		
	  		if(eBufType==7 || eBufType==11){
	  			
	  			metodo = (*env)->GetMethodID(env, class_gdalbuffer, "reservaDouble", "(I)V");
	  			obj_gdalbuffer = (*env)->NewObject (env,class_gdalbuffer,metodo,(int)(BufXSize*BufYSize));
		  		id_campo = (*env)->GetFieldID(env, class_gdalbuffer, "buffDouble", "[D");
	  		  		
			   	//Reservamos memoria para el buffer y lo cargamos llamando a GDALRasterIO
			   
			   	buffDouble = (double*) CPLMalloc(sizeof(double) *(int)BufXSize * (int)BufYSize);
	  			GDALRasterIO(rb, GF_Read,  (int)nXOff, (int)nYOff, (int)nXSize, (int)nYSize, buffDouble, (int)BufXSize, (int)BufYSize, (int)eBufType, 0, 0);
	  			doublearray = (*env)->NewDoubleArray(env,(int)(BufXSize*BufYSize));
			    if(doublearray!=NULL){
				  	(*env)->SetDoubleArrayRegion(env, doublearray, 0, (int)(BufXSize*BufYSize),(jdouble *)buffDouble); 
				 	(*env)->SetObjectField(env, obj_gdalbuffer, id_campo, doublearray);
				}

				if(buffDouble != NULL)CPLFree(buffDouble);
	  		}
	  		
  		}//if(rb!=NULL)
  		  		
  		return obj_gdalbuffer;
  		
  } 
  
/******************************************************************************/     
//								ReadRaster
/******************************************************************************/  
  
  
JNIEXPORT jobject JNICALL Java_es_gva_cit_jgdal_GdalRasterBand_readRasterWithPaletteNat
  (JNIEnv *env, jobject obj, jlong cPtr, jint nXOff, jint nYOff, jint nXSize, jint nYSize, jint BufXSize, jint BufYSize, jint eBufType){

  		jclass class_gdalbuffer;
  		jmethodID metodo;
  		jobject obj_gdalbuffer = NULL;
  		jfieldID id_campoR, id_campoG, id_campoB, id_campoA;
  		int dfNoData, bGotNodata;
  		
  		GDALRasterBandH *rb  = (GDALRasterBandH *) 0 ;
  		
  		jbyteArray bytearrayR, bytearrayG, bytearrayB, bytearrayA;
  		
  		unsigned char *scanLineBufR, *scanLineBufG, *scanLineBufB, *scanLineBufA;
  		
  		GDALColorTableH hTable;
        GByte      abyPCT[256][4];
		int i = 0;  		
  		 		
	    rb = *(GDALRasterBandH **)&cPtr;
  		
  		if(rb!=NULL){
  			
		  //Creamos el objeto java que contendr� la l�nea
  		
	  		class_gdalbuffer = (*env)->FindClass (env, "es/gva/cit/jgdal/GdalBuffer");
	  		
	  		if(eBufType==1){
	  			
	  			metodo = (*env)->GetMethodID(env, class_gdalbuffer, "reservaPalette", "(I)V");
		  			
		  		obj_gdalbuffer = (*env)->NewObject (env,class_gdalbuffer,metodo,(int)(BufXSize*BufYSize));
			  	id_campoR = (*env)->GetFieldID(env, class_gdalbuffer, "buffRPalette", "[B");
			  	id_campoG = (*env)->GetFieldID(env, class_gdalbuffer, "buffGPalette", "[B");
			  	id_campoB = (*env)->GetFieldID(env, class_gdalbuffer, "buffBPalette", "[B");
			  	id_campoA = (*env)->GetFieldID(env, class_gdalbuffer, "buffAPalette", "[B");
		  		  		
				//Reservamos memoria para el buffer y lo cargamos llamando a GDALRasterIO
		  		  		
		  		scanLineBufR = (unsigned char*) CPLMalloc(sizeof(unsigned char) *(int)BufXSize * (int)BufYSize);
		  		scanLineBufG = (unsigned char*) CPLMalloc(sizeof(unsigned char) *(int)BufXSize * (int)BufYSize);
		  		scanLineBufB = (unsigned char*) CPLMalloc(sizeof(unsigned char) *(int)BufXSize * (int)BufYSize);
		  		scanLineBufA = (unsigned char*) CPLMalloc(sizeof(unsigned char) *(int)BufXSize * (int)BufYSize);
		  		
		  		GDALRasterIO(rb, GF_Read,  (int)nXOff, (int)nYOff, (int)nXSize, (int)nYSize, scanLineBufR, (int)BufXSize, (int)BufYSize, GDT_Byte, 0, 0);
		  			  			
   			 	hTable = GDALGetRasterColorTable( rb );	  
   			 	 
	  			if( hTable != NULL ){
	  			int min = GDALGetColorEntryCount( hTable );
	  			dfNoData = GDALGetRasterNoDataValue( rb, &bGotNodata );
	  			if(256 < min)
	  			 min = 256;
				 	for( i = 0; i < min; i++ ){
		            	GDALColorEntry sEntry;

	        		    GDALGetColorEntryAsRGB( hTable, i, &sEntry );
			            abyPCT[i][0] = sEntry.c1;
        			    abyPCT[i][1] = sEntry.c2;
		    	        abyPCT[i][2] = sEntry.c3;
		    	        abyPCT[i][3] = sEntry.c4;
		    	        if(sEntry.c4 == 0){
		    	        	abyPCT[i][0] = 255;
	        			    abyPCT[i][1] = 255;
			    	        abyPCT[i][2] = 255;
			    	        abyPCT[i][3] = 0;
		    	        } 	
		    	        //printf("%d %d %d %d\n", sEntry.c1,sEntry.c2, sEntry.c3,sEntry.c4);
        			}
        			
        			for( i = GDALGetColorEntryCount( hTable ); i < 256; i++ ){
			            abyPCT[i][0] = 0;
            			abyPCT[i][1] = 0;
			            abyPCT[i][2] = 0;
            			abyPCT[i][3] = 255;
        			}
        			
        			for( i = nXSize * nYSize - 1; i >= 0; i-- ){
			            scanLineBufG[i] = abyPCT[scanLineBufR[i]][1];
			            scanLineBufB[i] = abyPCT[scanLineBufR[i]][2];
           				scanLineBufA[i] = abyPCT[scanLineBufR[i]][3];
           				scanLineBufR[i] = abyPCT[scanLineBufR[i]][0];
        			}
        			//printf("-%d %d %d %d\n", scanLineBufR[0], scanLineBufG[0], scanLineBufB[0],scanLineBufA[0]);
	  			}
	  					  	
				bytearrayR = (*env)->NewByteArray(env,(int)(BufXSize*BufYSize));
				bytearrayG = (*env)->NewByteArray(env,(int)(BufXSize*BufYSize));
				bytearrayB = (*env)->NewByteArray(env,(int)(BufXSize*BufYSize));
				bytearrayA = (*env)->NewByteArray(env,(int)(BufXSize*BufYSize));
				if(bytearrayR!=NULL){
				  	(*env)->SetByteArrayRegion(env, bytearrayR, 0, (int)(BufXSize*BufYSize),(jbyte *)scanLineBufR); 
				  	(*env)->SetObjectField(env, obj_gdalbuffer, id_campoR, bytearrayR);
				}
				if(scanLineBufR != NULL)
					CPLFree(scanLineBufR);
					
				if(bytearrayG!=NULL){
				  	(*env)->SetByteArrayRegion(env, bytearrayG, 0, (int)(BufXSize*BufYSize),(jbyte *)scanLineBufG); 
				  	(*env)->SetObjectField(env, obj_gdalbuffer, id_campoG, bytearrayG);
				}
				if(scanLineBufG != NULL)
					CPLFree(scanLineBufG);
					
				if(bytearrayB!=NULL){
				  	(*env)->SetByteArrayRegion(env, bytearrayB, 0, (int)(BufXSize*BufYSize),(jbyte *)scanLineBufB); 
				  	(*env)->SetObjectField(env, obj_gdalbuffer, id_campoB, bytearrayB);
				}
				if(scanLineBufB != NULL)
					CPLFree(scanLineBufB);
					
				if(bytearrayA!=NULL){
				  	(*env)->SetByteArrayRegion(env, bytearrayA, 0, (int)(BufXSize*BufYSize),(jbyte *)scanLineBufA); 
				  	(*env)->SetObjectField(env, obj_gdalbuffer, id_campoA, bytearrayA);
				}
				if(scanLineBufA != NULL)
					CPLFree(scanLineBufA);
				
	  		}
  		}
  		//CPLFree(hTable);

  		return obj_gdalbuffer;
  		
  } 
  
/******************************************************************************/
//								GetRasterColorTable
/******************************************************************************/

JNIEXPORT jlong JNICALL Java_es_gva_cit_jgdal_GdalRasterBand_getRasterColorTableNat
  (JNIEnv *env, jobject obj, jlong cPtr){

	GDALColorTableH hTable = NULL;
	jlong jresult = 0;
  	GDALRasterBandH *rb  = (GDALRasterBandH *) 0 ;
  	
  	rb = *(GDALRasterBandH **)&cPtr;
	hTable = GDALGetRasterColorTable( rb );
    
    *(GDALColorTableH **)&jresult = hTable;
        
    if(hTable == NULL)
    	return -1;
    else 
	    return jresult;
  }
  
/******************************************************************************/
//								GetBlockXSize
/******************************************************************************/


JNIEXPORT jint JNICALL Java_es_gva_cit_jgdal_JNIBase_getBlockXSizeNat
  (JNIEnv *env, jobject obj, jlong cPtr){
  	
  	GDALRasterBandH *rb  = (GDALRasterBandH *) 0 ;
  	int pnXSize=-1,pnYSize=-1;
  	  	
    rb = *(GDALRasterBandH **)&cPtr;
    
    if(rb!=NULL)
    	GDALGetBlockSize( rb,&pnXSize, &pnYSize );

    return pnXSize;
  }


/******************************************************************************/     
//								WriteRaster
/******************************************************************************/  
  
JNIEXPORT void JNICALL Java_es_gva_cit_jgdal_GdalRasterBand_writeRasterNat
  (JNIEnv *env, jobject obj, jlong cPtr, jint nXOff, jint nYOff, jint nXSize, jint nYSize, jobject buffer, jint eBufType){
  	
	  	GDALRasterBandH *rb  = (GDALRasterBandH *) 0 ;
	  	jclass clase;
	  	jfieldID id_campo;
	  	jmethodID metodo;
	  	jbyteArray bytearray;
	  	jshortArray shortarray;
	  	jintArray intarray;
	  	jfloatArray floatarray;
	  	jdoubleArray doublearray;
	  	
	  	int tam;
	  	unsigned char *byteBuf;
	  	unsigned short *shortBuf;
	  	unsigned int *intBuf;
	  	float *floatBuf;
	  	double *doubleBuf;
	  	
		char funcion[11];
		char signature[3];
		
		switch(eBufType){
			case 1:	
					strncpy(funcion,"buffByte\0",9);
					strncpy(signature,"[B\0",3);
					break;
			case 2:	
			case 3:	
			case 8:
					strncpy(funcion,"buffShort\0",10);
					strncpy(signature,"[S\0",3);
					break;
			case 4:	
			case 5:	
			case 9:	
					strncpy(funcion,"buffInt\0",8);
					strncpy(signature,"[I\0",3);
					break;
			case 6:	
			case 10:
					strncpy(funcion,"buffFloat\0",10);
					strncpy(signature,"[F\0",3);
					break;	
			case 7:	
			case 11:
					strncpy(funcion,"buffDouble\0",11);
					strncpy(signature,"[D\0",3);
					break;	
		}
	  	
	  	
	  	clase = (*env)->GetObjectClass(env, buffer);
	  	id_campo = (*env)->GetFieldID(env, clase, funcion, signature);
	  	metodo = (*env)->GetMethodID(env, clase, "getSize","()I");
	  	tam = (*env)->CallIntMethod(env,buffer,metodo);
	  	
  		rb = *(GDALRasterBandH **)&cPtr;
  		
	  	switch(eBufType){
			case 1:	
				bytearray =(jbyteArray)(*env)->GetObjectField(env, buffer, id_campo); 
		  		byteBuf = (unsigned char *)malloc(sizeof(unsigned char)*tam);
		  		if(bytearray!=NULL)
		  		    (*env)->GetByteArrayRegion(env, bytearray, 0, tam, (jbyte *)byteBuf); 
	  		    if(rb != NULL)
					GDALRasterIO( rb, GF_Write, nXOff, nYOff, nXSize, nYSize, byteBuf, nXSize, nYSize, GDT_Byte, 0, 0 );          
        		free(byteBuf);
				break;
			case 2:	
			case 3:	
			case 8:
				shortarray = (jshortArray)(*env)->GetObjectField(env, buffer, id_campo); 
		  		shortBuf = (unsigned short *)malloc(sizeof(unsigned short) * tam);
		  		if(shortarray!=NULL)
		  		    (*env)->GetShortArrayRegion(env, shortarray, 0, tam, (jshort *)shortBuf); 
				 if(rb!=NULL)
					GDALRasterIO( rb, GF_Write, nXOff, nYOff, nXSize, nYSize, shortBuf, nXSize, nYSize, GDT_Int16, 0, 0 );          
        		free(shortBuf); 	
				break;
			case 4:	
			case 5:	
			case 9:	
				intarray = (jintArray)(*env)->GetObjectField(env, buffer, id_campo); 
		  		intBuf = (unsigned int *)malloc(sizeof(unsigned int) * tam);
		  		if(intarray!=NULL)
		  		    (*env)->GetIntArrayRegion(env, intarray, 0, tam, (jint *)intBuf); 
		  		if(rb!=NULL)
					GDALRasterIO( rb, GF_Write, nXOff, nYOff, nXSize, nYSize, intBuf, nXSize, nYSize, GDT_Int32, 0, 0 );          
		        free(intBuf);
				break;
			case 6:	
			case 10:
				floatarray = (jfloatArray)(*env)->GetObjectField(env, buffer, id_campo); 
		  		floatBuf = (float *)malloc(sizeof(float) * tam);
		  		if(floatarray!=NULL)
		  		    (*env)->GetFloatArrayRegion(env, floatarray, 0, tam, (jfloat *)floatBuf); 
		  		if(rb!=NULL)
					GDALRasterIO( rb, GF_Write, nXOff, nYOff, nXSize, nYSize, floatBuf, nXSize, nYSize, GDT_Float32, 0, 0 );          
		        free(floatBuf);
				break;
			case 7:	
			case 11:
				doublearray = (jdoubleArray)(*env)->GetObjectField(env, buffer, id_campo); 
		  		doubleBuf = (double *)malloc(sizeof(double) * tam);
		  		if(doublearray!=NULL)
		  		    (*env)->GetDoubleArrayRegion(env, doublearray, 0, tam, (jdouble *)doubleBuf); 
		  		if(rb!=NULL)
					GDALRasterIO( rb, GF_Write, nXOff, nYOff, nXSize, nYSize, doubleBuf, nXSize, nYSize, GDT_Float64, 0, 0 );          
		        free(doubleBuf);
				break;
				
		}
  	  	
  }
    
/******************************************************************************/
//								GetBlockYSize
/******************************************************************************/


JNIEXPORT jint JNICALL Java_es_gva_cit_jgdal_JNIBase_getBlockYSizeNat
  (JNIEnv *env, jobject obj, jlong cPtr){
  	
  	GDALRasterBandH *rb  = (GDALRasterBandH *) 0 ;
  	int pnXSize=-1,pnYSize=-1;
  	  	
    rb = *(GDALRasterBandH **)&cPtr;
    
    if(rb!=NULL)
    	GDALGetBlockSize( rb,&pnXSize, &pnYSize );

    return pnYSize;
  }
  
/******************************************************************************/
//								GetRasterDataType
/******************************************************************************/  
  
  JNIEXPORT jint JNICALL Java_es_gva_cit_jgdal_JNIBase_getRasterDataTypeNat
  (JNIEnv *env, jobject obj, jlong cPtr){
  	
  	int datatype = -1;
  	GDALRasterBandH *rb  = (GDALRasterBandH *) 0 ;
  	
  	rb = *(GDALRasterBandH **)&cPtr;
  	
  	if(rb!=NULL)
    	datatype = GDALGetRasterDataType( rb );
 
    return datatype;
  }
  
/******************************************************************************/
//								GetRasterNoDataValue
/******************************************************************************/  
  
JNIEXPORT jdouble JNICALL Java_es_gva_cit_jgdal_GdalRasterBand_getRasterNoDataValueNat
  (JNIEnv *env, jobject obj, jlong cPtr){
  	
  	double nodata = -1;
  	int bGotNodata = 0;
  	GDALRasterBandH *rb  = (GDALRasterBandH *) 0 ;
  	
  	rb = *(GDALRasterBandH **)&cPtr;
  	
  	if(rb!=NULL)
    	nodata = GDALGetRasterNoDataValue( rb, &bGotNodata );
 	
    return nodata;
  }
  
/******************************************************************************/
//								ExistsNoDataValue
/******************************************************************************/  
  
JNIEXPORT jint JNICALL Java_es_gva_cit_jgdal_GdalRasterBand_existsNoDataValueNat
  (JNIEnv *env, jobject obj, jlong cPtr){

  	int bGotNodata = 0;
  	GDALRasterBandH *rb  = (GDALRasterBandH *) 0 ;
  	
  	rb = *(GDALRasterBandH **)&cPtr;
  	
  	if(rb!=NULL)
    	GDALGetRasterNoDataValue( rb, &bGotNodata );
 	
    return bGotNodata;
  }
  
/******************************************************************************/ 
//								 GetMetadata
/******************************************************************************/

JNIEXPORT jobjectArray JNICALL Java_es_gva_cit_jgdal_GdalRasterBand_getMetadataNat
  (JNIEnv *env, jobject obj, jlong cPtr, jstring pszDomain){
  	
  	 char		**papszMetadata;
  	 GDALRasterBandH *rb  = (GDALRasterBandH *) 0 ;
  	 int i, nmetadatos;
  	 
  	 jclass class_string;
  	 jobjectArray vector_str;
  	 
  	 //Obtenemos los metadatos sobre papszMetadata
  	  
  	 rb = *(GDALRasterBandH **)&cPtr;
  	 	 
  	 
  	 if(rb!=NULL){
		 papszMetadata = GDALGetMetadata( rb, NULL );
		 
		 
		 //Si hay metadatos devolvemos creamos el vector de String de java y los devolvemos
		 
		 nmetadatos = CSLCount(papszMetadata);
	     if( nmetadatos > 0 )
     	 {
    	    class_string = (*env)->FindClass (env, "java/lang/String");
		 	vector_str=(*env)->NewObjectArray(env, nmetadatos, class_string, (*env)->NewStringUTF(env,""));
		 	
		 	//Cargamos los metadatos
		 	
        	for( i = 0; papszMetadata[i] != NULL; i++ )
        		(*env)->SetObjectArrayElement(env,vector_str,i,(*env)->NewStringUTF(env,papszMetadata[i]));
        				
  	 		return vector_str;   	 
     	 }
  	 }
     
  	 return NULL;
  }
  
/******************************************************************************/ 
//							GetRasterColorInterpretation
/******************************************************************************/

JNIEXPORT jint JNICALL Java_es_gva_cit_jgdal_GdalRasterBand_getRasterColorInterpretationNat
  (JNIEnv *env, jobject obj, jlong cPtr){
  	 GDALRasterBandH *rb  = (GDALRasterBandH *) 0 ;
 	 int interp;
 	 
 	 rb = *(GDALRasterBandH **)&cPtr;
	 interp = GDALGetRasterColorInterpretation(rb); 
	 
	 return (jint)interp;
  }


/******************************************************************************/ 
//							SetRasterColorInterpretation
/******************************************************************************/

JNIEXPORT jint JNICALL Java_es_gva_cit_jgdal_GdalRasterBand_setRasterColorInterpretationNat
	(JNIEnv *env, jobject obj, jlong cPtr, jint bandType){
	GDALRasterBandH *rb = (GDALRasterBandH *) 0;
	jint err;
	GDALColorInterp interp = (GDALColorInterp)bandType;
	
	rb = *(GDALRasterBandH **)&cPtr;
	
	if (rb != NULL){
		err = GDALSetRasterColorInterpretation(rb, GCI_CyanBand);
		}
		
	return err;
	
	}

