/******************************************************************************
 * $Id: gdalwarp.cpp,v 1.27 2006/09/27 13:06:13 dron Exp $
 *
 * Project:  High Performance Image Reprojector
 * Purpose:  Test program for high performance warper API.
 * Author:   Frank Warmerdam <warmerdam@pobox.com>
 *
 ******************************************************************************
 * Copyright (c) 2002, i3 - information integration and imaging
 *                          Fort Collin, CO
 *
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
 * OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 ******************************************************************************
 *
 * $Log: gdalwarp.cpp,v $
 * Revision 1.27.1 2007/04/02 11:35:08  maquerol
 * main() function converted into an accesible warpFunction.
 *
 * Revision 1.27  2006/09/27 13:06:13  dron
 * Memory leak fixed.
 *
 * Revision 1.26  2006/07/06 20:30:11  fwarmerdam
 * use GDALSuggestedWarpOutput2() to avoid approximation implicit in using gt
 *
 * Revision 1.25  2006/06/29 21:10:29  fwarmerdam
 * Avoid a few memory leaks.
 *
 * Revision 1.24  2006/06/02 17:31:49  fwarmerdam
 * Modified -ts to allow width or height to be zero meaning, it should
 * be computed to retain square pixels.
 *
 * Revision 1.23  2006/05/29 17:32:15  fwarmerdam
 * added preliminary support for controlling longitude wrapping on input dataset
 *
 * Revision 1.22  2006/04/25 14:28:33  fwarmerdam
 * Check for no usable sources, avoid warning.
 *
 * Revision 1.21  2006/03/21 21:34:43  fwarmerdam
 * cleanup headers
 *
 * Revision 1.20  2006/01/05 19:51:10  fwarmerdam
 * fixed checking of targetsrs
 *
 * Revision 1.19  2005/12/19 20:20:00  fwarmerdam
 * preliminary support for multiple input files
 *
 * Revision 1.18  2005/09/13 01:57:31  fwarmerdam
 * Fixed usage message.
 *
 * Revision 1.17  2005/09/13 01:24:45  fwarmerdam
 * Set UNIFIED_SRC_NODATA by default for -srcnodata switch.
 *
 * Revision 1.16  2005/09/12 18:06:25  fwarmerdam
 * Added quotes around -srcnodata and -dstnodata in usage message.
 *
 * Revision 1.15  2004/12/26 16:14:53  fwarmerdam
 * added -tps flag
 *
 * Revision 1.14  2004/11/29 15:01:55  fwarmerdam
 * Fixed typo in printf as per Bug 691.
 *
 * Revision 1.13  2004/11/14 04:57:04  fwarmerdam
 * added -srcalpha switch, and automatic alpha detection
 *
 * Revision 1.12  2004/11/05 06:15:08  fwarmerdam
 * Don't double free the warpoptions array.
 *
 * Revision 1.11  2004/11/05 05:53:43  fwarmerdam
 * Avoid various memory leaks.
 *
 * Revision 1.10  2004/10/07 15:53:42  fwarmerdam
 * added preliminary alpha band support
 */
#include <jni.h>
#include "gdalwarper.h"
#include "cpl_string.h"
#include "ogr_spatialref.h"
#include "gdal.h"
//#include "../include/gdalwarp_interfaz.h"

CPL_CVSID("$Id: gdalwarp.cpp,v 1.27 2006/09/27 13:06:13 dron Exp $");

static CPLString InsertCenterLong( GDALDatasetH hDS, CPLString osWKT );

static GDALDatasetH
GDALWarpCreateOutput( char **papszSrcFiles, const char *pszFilename,
											const char *pszFormat, const char *pszSourceSRS,
											const char *pszTargetSRS, int nOrder,
											char **papszCreateOptions, GDALDataType eDT );

int CPL_STDCALL GDALFuncTermProgress( double dfComplete, const char *pszMessage,
											void * pProgressArg );

void CPL_STDCALL statusCallBack(int nPercent);

static double	       dfMinX=0.0, dfMinY=0.0, dfMaxX=0.0, dfMaxY=0.0;
static double	       dfXRes=0.0, dfYRes=0.0;
static int             nForcePixels=0, nForceLines=0, bQuiet = FALSE;
static int             bEnableDstAlpha = FALSE, bEnableSrcAlpha = FALSE;

static int             bVRT = FALSE;


/************************************************************************/
/*                             SanitizeSRS                              */
/************************************************************************/

char *SanitizeSRS( const char *pszUserInput )

{
		OGRSpatialReferenceH hSRS;
		char *pszResult = NULL;

		CPLErrorReset();

		hSRS = OSRNewSpatialReference( NULL );
		if( OSRSetFromUserInput( hSRS, pszUserInput ) == OGRERR_NONE )
				OSRExportToWkt( hSRS, &pszResult );
		else
		{
				CPLError( CE_Failure, CPLE_AppDefined,
									"Translating source or target SRS failed:\n%s",
									pszUserInput );
				exit( 1 );
		}

		OSRDestroySpatialReference( hSRS );

		return pszResult;
}


/************************************************************************/
/*                              WarpFunction()                          */
/************************************************************************/

int launchException(JNIEnv *env, char *msg) {
	env->ExceptionDescribe();
	env->ExceptionClear();

	jclass newExcCls = env->FindClass("es/gva/cit/jgdal/GdalException");
	if (newExcCls == 0)
		return 0;

	env->ThrowNew(newExcCls, msg);
	return 0;
}

int warpFunction(JNIEnv *env, char * s_srs, char * t_srs, char *source, char * dest, char *format) {
	GDALDatasetH hDstDS;
	const char *pszFormat = "GTiff";
	char *pszTargetSRS = NULL;
	char *pszSourceSRS = NULL;
	char **papszSrcFiles = NULL;
	char *pszDstFilename = NULL;
	int bCreateOutput = FALSE, i, nOrder = 0;
	void *hTransformArg, *hGenImgProjArg = NULL, *hApproxArg = NULL;
	char **papszWarpOptions = NULL;
	double dfErrorThreshold = 0.125;
	double dfWarpMemoryLimit = 0.0;
	GDALTransformerFunc pfnTransformer = NULL;
	char **papszCreateOptions = NULL;
	GDALDataType eOutputType = GDT_Unknown, eWorkingType = GDT_Unknown;
	GDALResampleAlg eResampleAlg = GRA_NearestNeighbour;
	const char *pszSrcNodata = NULL;
	const char *pszDstNodata = NULL;
	int bMulti = FALSE;
	char error[1024];

	GDALAllRegister();

/* -------------------------------------------------------------------- */
/*      Parse arguments.                                                */
/* -------------------------------------------------------------------- */
	pszTargetSRS = SanitizeSRS(t_srs);
	if (s_srs != NULL)
		pszSourceSRS = SanitizeSRS(s_srs);

	papszSrcFiles = CSLAddString(papszSrcFiles, source);
	papszSrcFiles = CSLAddString(papszSrcFiles, dest);
	if (format != NULL)
		pszFormat = format;
	bCreateOutput = FALSE;

/* -------------------------------------------------------------------- */
/*      The last filename in the file list is really our destination    */
/*      file.                                                           */
/* -------------------------------------------------------------------- */
	if (CSLCount(papszSrcFiles) > 1) {
		pszDstFilename = papszSrcFiles[CSLCount(papszSrcFiles) - 1];
		papszSrcFiles[CSLCount(papszSrcFiles) - 1] = NULL;
	}

/* -------------------------------------------------------------------- */
/*      Does the output dataset already exist?                          */
/* -------------------------------------------------------------------- */
	CPLPushErrorHandler(CPLQuietErrorHandler);
	hDstDS = GDALOpen(pszDstFilename, GA_Update);
	CPLPopErrorHandler();

	if (hDstDS != NULL && bCreateOutput) {
		sprintf(error, "warpfunc.cpp (218): Output dataset %s exists, "
			"but some commandline options were provided indicating a new dataset "
			"should be created.  Please delete existing dataset and run again.", pszDstFilename);
		return launchException(env, error);
	}

/* -------------------------------------------------------------------- */
/*      If not, we need to create it.                                   */
/* -------------------------------------------------------------------- */
	int bInitDestSetForFirst = FALSE;

	if (hDstDS == NULL) {
		hDstDS = GDALWarpCreateOutput(papszSrcFiles, pszDstFilename, pszFormat,
				pszSourceSRS, pszTargetSRS, nOrder, papszCreateOptions, eOutputType);
		bCreateOutput = TRUE;

		if (CSLFetchNameValue(papszWarpOptions, "INIT_DEST") == NULL && pszDstNodata == NULL) {
			papszWarpOptions = CSLSetNameValue(papszWarpOptions, "INIT_DEST", "0");
			bInitDestSetForFirst = TRUE;
		} else {
			if (CSLFetchNameValue(papszWarpOptions, "INIT_DEST") == NULL) {
				papszWarpOptions = CSLSetNameValue(papszWarpOptions, "INIT_DEST", "NO_DATA");
				bInitDestSetForFirst = TRUE;
			}
		}

		CSLDestroy(papszCreateOptions);
		papszCreateOptions = NULL;
	}

	if (hDstDS == NULL)
		return launchException(env, "warpfunc.cpp (249):");

	if (pszTargetSRS == NULL)
		pszTargetSRS = CPLStrdup(GDALGetProjectionRef(hDstDS));

/* -------------------------------------------------------------------- */
/*      Loop over all source files, processing each in turn.            */
/* -------------------------------------------------------------------- */
	for (int iSrc = 0; papszSrcFiles[iSrc] != NULL; iSrc++) {
		CPLString osThisSourceSRS;
		GDALDatasetH hSrcDS;

		if (pszSourceSRS != NULL)
			osThisSourceSRS = pszSourceSRS;

/* -------------------------------------------------------------------- */
/*      Open this file.                                                 */
/* -------------------------------------------------------------------- */
		hSrcDS = GDALOpen(papszSrcFiles[iSrc], GA_ReadOnly);

		if (hSrcDS == NULL)
			return launchException(env, "warpfunc.cpp (270):");

		// Processing input file papszSrcFiles[iSrc]
		if (strlen(osThisSourceSRS) == 0) {
			if (GDALGetProjectionRef(hSrcDS) != NULL && strlen(GDALGetProjectionRef(hSrcDS)) > 0) {
				osThisSourceSRS = GDALGetProjectionRef(hSrcDS);
			} else {
				if (GDALGetGCPProjection(hSrcDS) != NULL && strlen(GDALGetGCPProjection(hSrcDS)) > 0 && GDALGetGCPCount(hSrcDS) > 1) {
					osThisSourceSRS = GDALGetGCPProjection(hSrcDS);
				} else {
					osThisSourceSRS = "";
				}
			}

			if (pszTargetSRS != NULL && strlen(pszTargetSRS) > 0 && strlen(osThisSourceSRS) == 0) {
				return launchException(env, "warpfunc.cpp (285): A target coordinate system was specified, "
					"but there is no source coordinate system.  Consider using -s_srs option to provide a "
					"source coordinate system. Operation terminated.");
			}
		}

/* -------------------------------------------------------------------- */
/*      Do we have a source alpha band?                                 */
/* -------------------------------------------------------------------- */
		if (GDALGetRasterColorInterpretation(GDALGetRasterBand(hSrcDS, GDALGetRasterCount(hSrcDS))) == GCI_AlphaBand && !bEnableSrcAlpha) {
			// Using band GDALGetRasterCount(hSrcDS) of source image as alpha
			bEnableSrcAlpha = TRUE;
		}

/* -------------------------------------------------------------------- */
/*      If the source coordinate system is geographic, try and          */
/*      insert a CENTER_LONG extension parameter on the GEOGCS to       */
/*      handle wrapping better.                                         */
/* -------------------------------------------------------------------- */
		if (EQUALN(osThisSourceSRS.c_str(), "GEOGCS[", 7))
			osThisSourceSRS = InsertCenterLong(hSrcDS, osThisSourceSRS);

/* -------------------------------------------------------------------- */
/*      Create a transformation object from the source to               */
/*      destination coordinate system.                                  */
/* -------------------------------------------------------------------- */
		hTransformArg = hGenImgProjArg = GDALCreateGenImgProjTransformer(hSrcDS, osThisSourceSRS, hDstDS,
				pszTargetSRS, TRUE, 1000.0, nOrder);

		if (hTransformArg == NULL)
			return launchException(env, "warpfunc.cpp (315):");

		pfnTransformer = GDALGenImgProjTransform;

/* -------------------------------------------------------------------- */
/*      Warp the transformer with a linear approximator unless the      */
/*      acceptable error is zero.                                       */
/* -------------------------------------------------------------------- */
		if (dfErrorThreshold != 0.0) {
			hTransformArg = hApproxArg = GDALCreateApproxTransformer(GDALGenImgProjTransform, hGenImgProjArg, dfErrorThreshold);
			pfnTransformer = GDALApproxTransform;
		}

/* -------------------------------------------------------------------- */
/*      Clear temporary INIT_DEST settings after the first image.       */
/* -------------------------------------------------------------------- */
		if (bInitDestSetForFirst && iSrc == 1)
			papszWarpOptions = CSLSetNameValue(papszWarpOptions, "INIT_DEST", NULL);

/* -------------------------------------------------------------------- */
/*      Setup warp options.                                             */
/* -------------------------------------------------------------------- */
		GDALWarpOptions *psWO = GDALCreateWarpOptions();

		psWO->papszWarpOptions = CSLDuplicate(papszWarpOptions);
		psWO->eWorkingDataType = eWorkingType;
		psWO->eResampleAlg = eResampleAlg;

		psWO->hSrcDS = hSrcDS;
		psWO->hDstDS = hDstDS;

		psWO->pfnTransformer = pfnTransformer;
		psWO->pTransformerArg = hTransformArg;

		if (!bQuiet)
			psWO->pfnProgress = GDALFuncTermProgress;

		if (dfWarpMemoryLimit != 0.0)
			psWO->dfWarpMemoryLimit = dfWarpMemoryLimit;

/* -------------------------------------------------------------------- */
/*      Setup band mapping.                                             */
/* -------------------------------------------------------------------- */
		if (bEnableSrcAlpha)
			psWO->nBandCount = GDALGetRasterCount(hSrcDS) - 1;
		else
			psWO->nBandCount = GDALGetRasterCount(hSrcDS);

		psWO->panSrcBands = (int *) CPLMalloc(psWO->nBandCount * sizeof(int));
		psWO->panDstBands = (int *) CPLMalloc(psWO->nBandCount * sizeof(int));

		for (i = 0; i < psWO->nBandCount; i++) {
			psWO->panSrcBands[i] = i + 1;
			psWO->panDstBands[i] = i + 1;
		}

/* -------------------------------------------------------------------- */
/*      Setup alpha bands used if any.                                  */
/* -------------------------------------------------------------------- */
		if (bEnableSrcAlpha)
			psWO->nSrcAlphaBand = GDALGetRasterCount(hSrcDS);

		if (!bEnableDstAlpha && GDALGetRasterCount(hDstDS) == psWO->nBandCount + 1 &&
				GDALGetRasterColorInterpretation(GDALGetRasterBand(hDstDS, GDALGetRasterCount(hDstDS))) == GCI_AlphaBand) {
			// Using band GDALGetRasterCount(hDstDS) of destination image as alpha
			bEnableDstAlpha = TRUE;
		}

		if (bEnableDstAlpha)
			psWO->nDstAlphaBand = GDALGetRasterCount(hDstDS);

/* -------------------------------------------------------------------- */
/*      Setup NODATA options.                                           */
/* -------------------------------------------------------------------- */
		if (pszSrcNodata != NULL) {
			char **papszTokens = CSLTokenizeString(pszSrcNodata);
			int nTokenCount = CSLCount(papszTokens);

			psWO->padfSrcNoDataReal = (double *) CPLMalloc(psWO->nBandCount * sizeof(double));
			psWO->padfSrcNoDataImag = (double *) CPLMalloc(psWO->nBandCount * sizeof(double));

			for (i = 0; i < psWO->nBandCount; i++) {
				if (i < nTokenCount) {
					CPLStringToComplex(papszTokens[i], psWO->padfSrcNoDataReal + i, psWO->padfSrcNoDataImag + i);
				} else {
					psWO->padfSrcNoDataReal[i] = psWO->padfSrcNoDataReal[i - 1];
					psWO->padfSrcNoDataImag[i] = psWO->padfSrcNoDataImag[i - 1];
				}
			}

			CSLDestroy(papszTokens);

			psWO->papszWarpOptions = CSLSetNameValue(psWO->papszWarpOptions, "UNIFIED_SRC_NODATA", "YES");
		}

/* -------------------------------------------------------------------- */
/*      If the output dataset was created, and we have a destination    */
/*      nodata value, go through marking the bands with the information.*/
/* -------------------------------------------------------------------- */
		if (pszDstNodata != NULL && bCreateOutput) {
			char **papszTokens = CSLTokenizeString(pszDstNodata);
			int nTokenCount = CSLCount(papszTokens);

			psWO->padfDstNoDataReal = (double *) CPLMalloc(psWO->nBandCount * sizeof(double));
			psWO->padfDstNoDataImag = (double *) CPLMalloc(psWO->nBandCount * sizeof(double));

			for (i = 0; i < psWO->nBandCount; i++) {
				if (i < nTokenCount) {
					CPLStringToComplex(papszTokens[i], psWO->padfDstNoDataReal + i, psWO->padfDstNoDataImag + i);
				} else {
					psWO->padfDstNoDataReal[i] = psWO->padfDstNoDataReal[i - 1];
					psWO->padfDstNoDataImag[i] = psWO->padfDstNoDataImag[i - 1];
				}

				if (bCreateOutput) {
					GDALSetRasterNoDataValue(GDALGetRasterBand(hDstDS, psWO->panDstBands[i]), psWO->padfDstNoDataReal[i]);
				}
			}

			CSLDestroy(papszTokens);
		}

/* -------------------------------------------------------------------- */
/*      If we are producing VRT output, then just initialize it with    */
/*      the warp options and write out now rather than proceeding       */
/*      with the operations.                                            */
/* -------------------------------------------------------------------- */
		if (bVRT) {
			if (GDALInitializeWarpedVRT(hDstDS, psWO) != CE_None)
				return launchException(env, "warpfunc.cpp (444):");

			GDALClose(hDstDS);
			GDALClose(hSrcDS);

			//GDALDumpOpenDatasets( stderr );

			GDALDestroyDriverManager();

			return 1;
		}

/* -------------------------------------------------------------------- */
/*      Initialize and execute the warp.                                */
/* -------------------------------------------------------------------- */
		GDALWarpOperation oWO;

		if (oWO.Initialize(psWO) == CE_None) {
			if (bMulti) {
				oWO.ChunkAndWarpMulti(0, 0, GDALGetRasterXSize(hDstDS), GDALGetRasterYSize(hDstDS));
			} else {
				oWO.ChunkAndWarpImage(0, 0, GDALGetRasterXSize(hDstDS), GDALGetRasterYSize(hDstDS));
			}
		}

/* -------------------------------------------------------------------- */
/*      Cleanup                                                         */
/* -------------------------------------------------------------------- */
		if (hApproxArg != NULL)
			GDALDestroyApproxTransformer(hApproxArg);

		if (hGenImgProjArg != NULL)
			GDALDestroyGenImgProjTransformer(hGenImgProjArg);

		GDALDestroyWarpOptions(psWO);

		GDALClose(hSrcDS);
	}

/* -------------------------------------------------------------------- */
/*      Final Cleanup.                                                  */
/* -------------------------------------------------------------------- */
	GDALClose(hDstDS);

	CPLFree(pszTargetSRS);
	CPLFree(pszDstFilename);
	CSLDestroy(papszSrcFiles);
	CSLDestroy(papszWarpOptions);

	GDALDestroyDriverManager();

	return 1;
}




/************************************************************************/
/*                        GDALWarpCreateOutput()                        */
/*                                                                      */
/*      Create the output file based on various commandline options,    */
/*      and the input file.                                             */
/************************************************************************/

static GDALDatasetH
GDALWarpCreateOutput( char **papszSrcFiles, const char *pszFilename,
											const char *pszFormat, const char *pszSourceSRS,
											const char *pszTargetSRS, int nOrder,
											char **papszCreateOptions, GDALDataType eDT )


{
		GDALDriverH hDriver;
		GDALDatasetH hDstDS;
		void *hTransformArg;
		GDALColorTableH hCT = NULL;
		double dfWrkMinX=0, dfWrkMaxX=0, dfWrkMinY=0, dfWrkMaxY=0;
		double dfWrkResX=0, dfWrkResY=0;
		int nDstBandCount = 0;

/* -------------------------------------------------------------------- */
/*      Find the output driver.                                         */
/* -------------------------------------------------------------------- */
		hDriver = GDALGetDriverByName( pszFormat );
		if( hDriver == NULL
				|| GDALGetMetadataItem( hDriver, GDAL_DCAP_CREATE, NULL ) == NULL )
		{
				int	iDr;

				printf( "Output driver `%s' not recognised or does not support\n",
								pszFormat );
				printf( "direct output file creation.  The following format drivers are configured\n"
								"and support direct output:\n" );

				for( iDr = 0; iDr < GDALGetDriverCount(); iDr++ )
				{
						GDALDriverH hDriver = GDALGetDriver(iDr);

						if( GDALGetMetadataItem( hDriver, GDAL_DCAP_CREATE, NULL) != NULL )
						{
								printf( "  %s: %s\n",
												GDALGetDriverShortName( hDriver  ),
												GDALGetDriverLongName( hDriver ) );
						}
				}
				printf( "\n" );
				exit( 1 );
		}

/* -------------------------------------------------------------------- */
/*      For virtual output files, we have to set a special subclass     */
/*      of dataset to create.                                           */
/* -------------------------------------------------------------------- */
		if( bVRT )
				papszCreateOptions =
						CSLSetNameValue( papszCreateOptions, "SUBCLASS",
														 "VRTWarpedDataset" );

/* -------------------------------------------------------------------- */
/*      Loop over all input files to collect extents.                   */
/* -------------------------------------------------------------------- */
		int iSrc;

		for( iSrc = 0; papszSrcFiles[iSrc] != NULL; iSrc++ )
		{
				GDALDatasetH hSrcDS;
				const char *pszThisSourceSRS = pszSourceSRS;

				hSrcDS = GDALOpen( papszSrcFiles[iSrc], GA_ReadOnly );
				if( hSrcDS == NULL )
						exit( 1 );

				if( eDT == GDT_Unknown )
						eDT = GDALGetRasterDataType(GDALGetRasterBand(hSrcDS,1));

/* -------------------------------------------------------------------- */
/*      If we are processing the first file, and it has a color         */
/*      table, then we will copy it to the destination file.            */
/* -------------------------------------------------------------------- */
				if( iSrc == 0 )
				{
						nDstBandCount = GDALGetRasterCount(hSrcDS);
						hCT = GDALGetRasterColorTable( GDALGetRasterBand(hSrcDS,1) );
						if( hCT != NULL )
						{
								hCT = GDALCloneColorTable( hCT );
								printf( "Copying color table from %s to new file.\n",
												papszSrcFiles[iSrc] );
						}
				}

/* -------------------------------------------------------------------- */
/*      Get the sourcesrs from the dataset, if not set already.         */
/* -------------------------------------------------------------------- */
				if( pszThisSourceSRS == NULL )
				{
						if( GDALGetProjectionRef( hSrcDS ) != NULL
								&& strlen(GDALGetProjectionRef( hSrcDS )) > 0 )
								pszThisSourceSRS = GDALGetProjectionRef( hSrcDS );

						else if( GDALGetGCPProjection( hSrcDS ) != NULL
										 && strlen(GDALGetGCPProjection(hSrcDS)) > 0
										 && GDALGetGCPCount( hSrcDS ) > 1 )
								pszThisSourceSRS = GDALGetGCPProjection( hSrcDS );
						else
								pszThisSourceSRS = "";
				}

				if( pszTargetSRS == NULL )
						pszTargetSRS = pszThisSourceSRS;

/* -------------------------------------------------------------------- */
/*      Create a transformation object from the source to               */
/*      destination coordinate system.                                  */
/* -------------------------------------------------------------------- */
				hTransformArg =
						GDALCreateGenImgProjTransformer( hSrcDS, pszThisSourceSRS,
																						 NULL, pszTargetSRS,
																						 TRUE, 1000.0, nOrder );

				if( hTransformArg == NULL )
						return NULL;

/* -------------------------------------------------------------------- */
/*      Get approximate output definition.                              */
/* -------------------------------------------------------------------- */
				double adfThisGeoTransform[6];
				double adfExtent[4];
				int    nThisPixels, nThisLines;

				if( GDALSuggestedWarpOutput2( hSrcDS,
																			GDALGenImgProjTransform, hTransformArg,
																			adfThisGeoTransform,
																			&nThisPixels, &nThisLines,
																			adfExtent, 0 ) != CE_None )
						return NULL;

/* -------------------------------------------------------------------- */
/*      Expand the working bounds to include this region, ensure the    */
/*      working resolution is no more than this resolution.             */
/* -------------------------------------------------------------------- */
				if( dfWrkMaxX == 0.0 && dfWrkMinX == 0.0 )
				{
						dfWrkMinX = adfExtent[0];
						dfWrkMaxX = adfExtent[2];
						dfWrkMaxY = adfExtent[3];
						dfWrkMinY = adfExtent[1];
						dfWrkResX = adfThisGeoTransform[1];
						dfWrkResY = ABS(adfThisGeoTransform[5]);
				}
				else
				{
						dfWrkMinX = MIN(dfWrkMinX,adfExtent[0]);
						dfWrkMaxX = MAX(dfWrkMaxX,adfExtent[2]);
						dfWrkMaxY = MAX(dfWrkMaxY,adfExtent[3]);
						dfWrkMinY = MIN(dfWrkMinY,adfExtent[1]);
						dfWrkResX = MIN(dfWrkResX,adfThisGeoTransform[1]);
						dfWrkResY = MIN(dfWrkResY,ABS(adfThisGeoTransform[5]));
				}

				GDALDestroyGenImgProjTransformer( hTransformArg );

				GDALClose( hSrcDS );
		}

/* -------------------------------------------------------------------- */
/*      Did we have any usable sources?                                 */
/* -------------------------------------------------------------------- */
		if( nDstBandCount == 0 )
		{
				CPLError( CE_Failure, CPLE_AppDefined,
									"No usable source images." );
				return NULL;
		}

/* -------------------------------------------------------------------- */
/*      Turn the suggested region into a geotransform and suggested     */
/*      number of pixels and lines.                                     */
/* -------------------------------------------------------------------- */
		double adfDstGeoTransform[6];
		int nPixels, nLines;

		adfDstGeoTransform[0] = dfWrkMinX;
		adfDstGeoTransform[1] = dfWrkResX;
		adfDstGeoTransform[2] = 0.0;
		adfDstGeoTransform[3] = dfWrkMaxY;
		adfDstGeoTransform[4] = 0.0;
		adfDstGeoTransform[5] = -1 * dfWrkResY;

		nPixels = (int) ((dfWrkMaxX - dfWrkMinX) / dfWrkResX + 0.5);
		nLines = (int) ((dfWrkMaxY - dfWrkMinY) / dfWrkResY + 0.5);

/* -------------------------------------------------------------------- */
/*      Did the user override some parameters?                          */
/* -------------------------------------------------------------------- */
		if( dfXRes != 0.0 && dfYRes != 0.0 )
		{
				CPLAssert( nForcePixels == 0 && nForceLines == 0 );
				if( dfMinX == 0.0 && dfMinY == 0.0 && dfMaxX == 0.0 && dfMaxY == 0.0 )
				{
						dfMinX = adfDstGeoTransform[0];
						dfMaxX = adfDstGeoTransform[0] + adfDstGeoTransform[1] * nPixels;
						dfMaxY = adfDstGeoTransform[3];
						dfMinY = adfDstGeoTransform[3] + adfDstGeoTransform[5] * nLines;
				}

				nPixels = (int) ((dfMaxX - dfMinX + (dfXRes/2.0)) / dfXRes);
				nLines = (int) ((dfMaxY - dfMinY + (dfYRes/2.0)) / dfYRes);
				adfDstGeoTransform[0] = dfMinX;
				adfDstGeoTransform[3] = dfMaxY;
				adfDstGeoTransform[1] = dfXRes;
				adfDstGeoTransform[5] = -dfYRes;
		}

		else if( nForcePixels != 0 && nForceLines != 0 )
		{
				if( dfMinX == 0.0 && dfMinY == 0.0 && dfMaxX == 0.0 && dfMaxY == 0.0 )
				{
						dfMinX = dfWrkMinX;
						dfMaxX = dfWrkMaxX;
						dfMaxY = dfWrkMaxY;
						dfMinY = dfWrkMinY;
				}

				dfXRes = (dfMaxX - dfMinX) / nForcePixels;
				dfYRes = (dfMaxY - dfMinY) / nForceLines;

				adfDstGeoTransform[0] = dfMinX;
				adfDstGeoTransform[3] = dfMaxY;
				adfDstGeoTransform[1] = dfXRes;
				adfDstGeoTransform[5] = -dfYRes;

				nPixels = nForcePixels;
				nLines = nForceLines;
		}

		else if( nForcePixels != 0 )
		{
				if( dfMinX == 0.0 && dfMinY == 0.0 && dfMaxX == 0.0 && dfMaxY == 0.0 )
				{
						dfMinX = dfWrkMinX;
						dfMaxX = dfWrkMaxX;
						dfMaxY = dfWrkMaxY;
						dfMinY = dfWrkMinY;
				}

				dfXRes = (dfMaxX - dfMinX) / nForcePixels;
				dfYRes = dfXRes;

				adfDstGeoTransform[0] = dfMinX;
				adfDstGeoTransform[3] = dfMaxY;
				adfDstGeoTransform[1] = dfXRes;
				adfDstGeoTransform[5] = -dfYRes;

				nPixels = nForcePixels;
				nLines = (int) ((dfMaxY - dfMinY + (dfYRes/2.0)) / dfYRes);
		}

		else if( nForceLines != 0 )
		{
				if( dfMinX == 0.0 && dfMinY == 0.0 && dfMaxX == 0.0 && dfMaxY == 0.0 )
				{
						dfMinX = dfWrkMinX;
						dfMaxX = dfWrkMaxX;
						dfMaxY = dfWrkMaxY;
						dfMinY = dfWrkMinY;
				}

				dfYRes = (dfMaxY - dfMinY) / nForceLines;
				dfXRes = dfYRes;

				adfDstGeoTransform[0] = dfMinX;
				adfDstGeoTransform[3] = dfMaxY;
				adfDstGeoTransform[1] = dfXRes;
				adfDstGeoTransform[5] = -dfYRes;

				nPixels = (int) ((dfMaxX - dfMinX + (dfXRes/2.0)) / dfXRes);
				nLines = nForceLines;
		}

		else if( dfMinX != 0.0 || dfMinY != 0.0 || dfMaxX != 0.0 || dfMaxY != 0.0 )
		{
				dfXRes = adfDstGeoTransform[1];
				dfYRes = fabs(adfDstGeoTransform[5]);

				nPixels = (int) ((dfMaxX - dfMinX + (dfXRes/2.0)) / dfXRes);
				nLines = (int) ((dfMaxY - dfMinY + (dfYRes/2.0)) / dfYRes);

				adfDstGeoTransform[0] = dfMinX;
				adfDstGeoTransform[3] = dfMaxY;
		}

/* -------------------------------------------------------------------- */
/*      Do we want to generate an alpha band in the output file?        */
/* -------------------------------------------------------------------- */
		if( bEnableSrcAlpha )
				nDstBandCount--;

		if( bEnableDstAlpha )
				nDstBandCount++;

/* -------------------------------------------------------------------- */
/*      Create the output file.                                         */
/* -------------------------------------------------------------------- */
		if( !bQuiet )
				printf( "Creating output file that is %dP x %dL.\n", nPixels, nLines );

		hDstDS = GDALCreate( hDriver, pszFilename, nPixels, nLines,
												 nDstBandCount, eDT, papszCreateOptions );

		if( hDstDS == NULL )
				return NULL;

/* -------------------------------------------------------------------- */
/*      Write out the projection definition.                            */
/* -------------------------------------------------------------------- */
		GDALSetProjection( hDstDS, pszTargetSRS );
		GDALSetGeoTransform( hDstDS, adfDstGeoTransform );

/* -------------------------------------------------------------------- */
/*      Try to set color interpretation of output file alpha band.      */
/*      TODO: We should likely try to copy the other bands too.         */
/* -------------------------------------------------------------------- */
		if( bEnableDstAlpha )
		{
				GDALSetRasterColorInterpretation(
						GDALGetRasterBand( hDstDS, nDstBandCount ),
						GCI_AlphaBand );
		}

/* -------------------------------------------------------------------- */
/*      Copy the color table, if required.                              */
/* -------------------------------------------------------------------- */
		if( hCT != NULL )
		{
				GDALSetRasterColorTable( GDALGetRasterBand(hDstDS,1), hCT );
				GDALDestroyColorTable( hCT );
		}

		return hDstDS;
}

/************************************************************************/
/*                          InsertCenterLong()                          */
/*                                                                      */
/*      Insert a CENTER_LONG Extension entry on a GEOGCS to indicate    */
/*      the center longitude of the dataset for wrapping purposes.      */
/************************************************************************/

static CPLString InsertCenterLong( GDALDatasetH hDS, CPLString osWKT )

{
		if( !EQUALN(osWKT.c_str(), "GEOGCS[", 7) )
				return osWKT;

		if( strstr(osWKT,"EXTENSION[\"CENTER_LONG") != NULL )
				return osWKT;

/* -------------------------------------------------------------------- */
/*      For now we only do this if we have a geotransform since         */
/*      other forms require a bunch of extra work.                      */
/* -------------------------------------------------------------------- */
		double   adfGeoTransform[6];

		if( GDALGetGeoTransform( hDS, adfGeoTransform ) != CE_None )
				return osWKT;

/* -------------------------------------------------------------------- */
/*      Compute min/max longitude based on testing the four corners.    */
/* -------------------------------------------------------------------- */
		double dfMinLong, dfMaxLong;
		int nXSize = GDALGetRasterXSize( hDS );
		int nYSize = GDALGetRasterYSize( hDS );

		dfMinLong =
				MIN(MIN(adfGeoTransform[0] + 0 * adfGeoTransform[1]
								+ 0 * adfGeoTransform[2],
								adfGeoTransform[0] + nXSize * adfGeoTransform[1]
								+ 0 * adfGeoTransform[2]),
						MIN(adfGeoTransform[0] + 0 * adfGeoTransform[1]
								+ nYSize * adfGeoTransform[2],
								adfGeoTransform[0] + nXSize * adfGeoTransform[1]
								+ nYSize * adfGeoTransform[2]));
		dfMaxLong =
				MAX(MAX(adfGeoTransform[0] + 0 * adfGeoTransform[1]
								+ 0 * adfGeoTransform[2],
								adfGeoTransform[0] + nXSize * adfGeoTransform[1]
								+ 0 * adfGeoTransform[2]),
						MAX(adfGeoTransform[0] + 0 * adfGeoTransform[1]
								+ nYSize * adfGeoTransform[2],
								adfGeoTransform[0] + nXSize * adfGeoTransform[1]
								+ nYSize * adfGeoTransform[2]));

		if( dfMaxLong - dfMinLong > 360.0 )
				return osWKT;

/* -------------------------------------------------------------------- */
/*      Insert center long.                                             */
/* -------------------------------------------------------------------- */
		OGRSpatialReference oSRS( osWKT );
		double dfCenterLong = (dfMaxLong + dfMinLong) / 2.0;
		OGR_SRSNode *poExt;

		poExt  = new OGR_SRSNode( "EXTENSION" );
		poExt->AddChild( new OGR_SRSNode( "CENTER_LONG" ) );
		poExt->AddChild( new OGR_SRSNode( CPLString().Printf("%g",dfCenterLong) ));

		oSRS.GetRoot()->AddChild( poExt );

/* -------------------------------------------------------------------- */
/*      Convert back to wkt.                                            */
/* -------------------------------------------------------------------- */
		char *pszWKT = NULL;
		oSRS.exportToWkt( &pszWKT );

		osWKT = pszWKT;
		CPLFree( pszWKT );

		return osWKT;
}


/************************************************************************/
/*                          GDALTermProgress()                          */
/************************************************************************/

/**
 * Simple progress report to terminal.
 *
 * This progress reporter prints simple progress report to the
 * terminal window.  The progress report generally looks something like
 * this:

\verbatim
0...10...20...30...40...50...60...70...80...90...100 - done.
\endverbatim

 * Every 2.5% of progress another number or period is emitted.  Note that
 * GDALTermProgress() uses internal static data to keep track of the last
 * percentage reported and will get confused if two terminal based progress
 * reportings are active at the same time.
 *
 * The GDALTermProgress() function maintains an internal memory of the
 * last percentage complete reported in a static variable, and this makes
 * it unsuitable to have multiple GDALTermProgress()'s active eithin a
 * single thread or across multiple threads.
 *
 * @param dfComplete completion ratio from 0.0 to 1.0.
 * @param pszMessage optional message.
 * @param pProgressArg ignored callback data argument.
 *
 * @return Always returns TRUE indicating the process should continue.
 */

int CPL_STDCALL GDALFuncTermProgress( double dfComplete, const char *pszMessage,
											void * pProgressArg )

{
		static double dfLastComplete = -1.0;

		(void) pProgressArg;

		 int    nPercent = (int) floor(dfComplete*100);

		 statusCallBack(nPercent);
		 fflush( stdout );

		dfLastComplete = dfComplete;

		return TRUE;
}
