
LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE_TAGS := optional
LOCAL_MODULE := jabberwocky
LOCAL_SDK_VERSION := 19
LOCAL_SRC_FILES := $(call all-java-files-under, java)

include $(BUILD_STATIC_JAVA_LIBRARY)
