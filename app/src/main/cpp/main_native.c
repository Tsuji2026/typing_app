#include <jni.h>
#include <stdio.h>
#include <android/log.h>
#include <string.h>

static int count = 0;

JNIEXPORT jstring JNICALL
Java_com_example_myapplication_JniActivity_getMessageFromJNINoInput(JNIEnv *env, jobject thiz) {
    // お試し
    int i ;
    char str[512];
    int str_size = 0;
    int index = 0;

    for(i = 0; i <= 99; i++){
        if(i % 10 == 9){
            str_size = snprintf(&str[index], sizeof(str) - index, "%d\n", i);
        } else {
            str_size = snprintf(&str[index], sizeof(str) - index, "%d ", i);
        }

        index += str_size;
    }

    // printf("%s", str);
    __android_log_print(ANDROID_LOG_DEBUG, "JniActivity_native", "%s", str);

    return (*env)->NewStringUTF(env, "Hello from C");
}

JNIEXPORT jstring JNICALL
Java_com_example_myapplication_JniActivity_getMessageFromJNIIinput(JNIEnv *env, jobject thiz, jint num, jstring str) {
    const char* text = (*env)->GetStringUTFChars(env, str, 0);
    char buffer[256];

    count++;

    sprintf(buffer, "input data num:%d num + count:%d str:%s", num, num + count, text);

    return (*env)->NewStringUTF(env, buffer);
}

JNIEXPORT jboolean JNICALL
Java_com_example_myapplication_TypingActivityJni_checkInputStrRed(JNIEnv *env, jobject thiz,
                                                                   jchar input_char, jint input_char_pos,
                                                                   jstring question_str) {
    const char *question_str_utf8 = (*env)->GetStringUTFChars(env, question_str, NULL);
    __android_log_print(ANDROID_LOG_DEBUG, "JniActivity_native", "input_char:%c, input_char_pos:%d question_str_utf8:%s",
                        input_char, input_char_pos, question_str_utf8);

    if(question_str_utf8[input_char_pos - 1] != input_char){
        return JNI_FALSE;
    }
    return JNI_TRUE;
}

JNIEXPORT jboolean JNICALL
Java_com_example_myapplication_TypingActivityJni_checkMatch(JNIEnv *env, jobject thiz,
                                                              jstring input_str, jstring question_str) {
    const char *input_str_utf8 = (*env)->GetStringUTFChars(env, input_str, NULL);
    const char *question_str_utf8 = (*env)->GetStringUTFChars(env, question_str, NULL);

    __android_log_print(ANDROID_LOG_DEBUG, "JniActivity_native", "input_str_utf8:%s question_str_utf8:%s", input_str_utf8, question_str_utf8);

    if(strcmp(input_str_utf8, question_str_utf8) == 0){
        return JNI_TRUE;
    }

    return JNI_FALSE;
}