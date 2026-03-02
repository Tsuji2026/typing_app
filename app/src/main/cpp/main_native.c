#include <jni.h>
#include <stdio.h>

static int count = 0;

JNIEXPORT jstring JNICALL
Java_com_example_myapplication_JniActivity_getMessageFromJNI_1no_1input(JNIEnv *env, jobject thiz) {
    return (*env)->NewStringUTF(env, "Hello from C");
}

JNIEXPORT jstring JNICALL
Java_com_example_myapplication_JniActivity_getMessageFromJNI_1input(JNIEnv *env, jobject thiz, jint num, jstring str) {
    const char* text = (*env)->GetStringUTFChars(env, str, 0);
    char buffer[256];

    count++;

    sprintf(buffer, "input data num:%d num + count:%d str:%s", num, num + count, text);

    return (*env)->NewStringUTF(env, buffer);
}