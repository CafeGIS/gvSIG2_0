#include <jni.h>
#include "gdal_priv.h"
#include "cpl_string.h"

jobject *jni_obj;
JNIEnv *jni_env;


int CPL_STDCALL incrementFnc( double dfComplete, const char *pszMessage, void * pProgressArg ) {
	jclass class_incr;
	jmethodID method;
	int value = 0;

	value = (int)(dfComplete * 100);
	class_incr = jni_env->FindClass ("org/gvsig/addo/IOverviewIncrement");

	method = jni_env->GetMethodID( class_incr, "setPercent", "(I)V");
	jni_env->CallVoidMethod( (*jni_obj), method, value);
  return 1;
}

extern "C" JNIEXPORT jint JNICALL Java_org_gvsig_addo_Jaddo_buildOverviewsNative
  (JNIEnv *env, jobject obj, jint method, jstring file, jintArray values) {
  	int err = 0;
  	jsize lon = 0;
	int i = 0;
  	jint *listValues = NULL;
  	GDALDataset *poDataset;
  	int anLevels[4] = {2, 4, 8, 16};
  	const char * pszResampling = "nearest";
	const char *filename = env->GetStringUTFChars( file, 0);
	lon = env->GetArrayLength( values);

	jni_obj = &obj;
  	jni_env = env;

  	listValues = env->GetIntArrayElements( values, 0);

  	for (i = 0; i < lon; i++) {
		anLevels[i] = listValues[i];
	}

  	switch(method) {
  		case 0: pszResampling = "nearest"; break;
  		case 1: pszResampling = "average"; break;
  		case 2: pszResampling = "average_mp"; break;
  		case 3: pszResampling = "average_magphase"; break;
  		case 4: pszResampling = "mode"; break;
  	}

  	GDALAllRegister();
    poDataset = (GDALDataset *) GDALOpen( filename, GA_Update );

    if( poDataset == NULL )
        poDataset = (GDALDataset *) GDALOpen( filename, GA_ReadOnly );

    if( poDataset == NULL ) {
    	env->ReleaseIntArrayElements( values, listValues, 0);
    	env->ReleaseStringUTFChars( file, filename);
        return CE_Fatal;
    }

	err = poDataset->BuildOverviews(  pszResampling,
                            lon, anLevels, 0, NULL,
                            incrementFnc, NULL );

    if( err != CE_None ) {
		env->ReleaseIntArrayElements( values, listValues, 0);
		env->ReleaseStringUTFChars( file, filename);
        return err;
    }

	delete poDataset;
    GDALDestroyDriverManager();

    env->ReleaseIntArrayElements( values, listValues, 0);
    env->ReleaseStringUTFChars( file, filename);

    return -1;
}


