/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class br_unb_unbiquitous_jni_MarkerDetectionJni */

#ifndef _Included_br_unb_unbiquitous_jni_MarkerDetectionJni
#define _Included_br_unb_unbiquitous_jni_MarkerDetectionJni
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     br_unb_unbiquitous_jni_MarkerDetectionJni
 * Method:    detectMarkers
 * Signature: ([B[FIIZI)I
 */
JNIEXPORT jint JNICALL Java_br_unb_unbiquitous_jni_MarkerDetectionJni_detectMarkers
  (JNIEnv *, jobject, jbyteArray, jfloatArray, jint, jint, jboolean, jint);

/*
 * Class:     br_unb_unbiquitous_jni_MarkerDetectionJni
 * Method:    calibrate
 * Signature: (II)Z
 */
JNIEXPORT jboolean JNICALL Java_br_unb_unbiquitous_jni_MarkerDetectionJni_calibrate
  (JNIEnv *, jobject, jint, jint);

/*
 * Class:     br_unb_unbiquitous_jni_MarkerDetectionJni
 * Method:    getCalibration
 * Signature: ([D[D)Z
 */
JNIEXPORT jboolean JNICALL Java_br_unb_unbiquitous_jni_MarkerDetectionJni_getCalibration
  (JNIEnv *, jobject, jdoubleArray, jdoubleArray);

/*
 * Class:     br_unb_unbiquitous_jni_MarkerDetectionJni
 * Method:    setCalibration
 * Signature: ([D[D)Z
 */
JNIEXPORT jboolean JNICALL Java_br_unb_unbiquitous_jni_MarkerDetectionJni_setCalibration
  (JNIEnv *, jobject, jdoubleArray, jdoubleArray);

/*
 * Class:     br_unb_unbiquitous_jni_MarkerDetectionJni
 * Method:    initThread
 * Signature: ([I[D[D)Z
 */
JNIEXPORT jboolean JNICALL Java_br_unb_unbiquitous_jni_MarkerDetectionJni_initThread
  (JNIEnv *, jobject, jintArray, jdoubleArray, jdoubleArray);

#ifdef __cplusplus
}
#endif
#endif