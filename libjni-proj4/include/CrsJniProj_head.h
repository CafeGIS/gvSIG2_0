#include "projects.h"
#include <jni.h>

#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT int JNICALL Java_org_gvsig_crs_epsg_JNIBaseOperation_operationSimple
  (JNIEnv *, jobject, jdouble, jdouble, jdouble, jlong, jlong);

JNIEXPORT int JNICALL Java_org_gvsig_crs_epsg_JNIBaseOperation_operation
  (JNIEnv *, jobject, jdoubleArray, jdoubleArray, jdoubleArray, jlong, jlong);
  
JNIEXPORT int JNICALL Java_org_gvsig_crs_epsg_JNIBaseOperation_operationArraySimple
  (JNIEnv *, jobject, jdoubleArray, jlong, jlong);
  
JNIEXPORT projPJ JNICALL Java_org_gvsig_crs_epsg_JNIBaseCrs_loadCrs
  (JNIEnv * env, jobject parent, jstring);
  
JNIEXPORT void JNICALL Java_org_gvsig_crs_epsg_JNIBaseCrs_freeCrs
  (JNIEnv * env, jobject parent, jlong);
  
JNIEXPORT int JNICALL Java_org_gvsig_crs_epsg_JNIBaseCrs_isLatlong
  (JNIEnv * env, jobject parent, jlong);


#ifdef __cplusplus
}
#endif
