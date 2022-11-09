# Fake/Empty module to create jar file dependency

qt_internal_add_module(Aidl SOURCES aidl.cpp PUBLIC_LIBRARIES
                       Qt::InterfaceFramework)

set_property(
  TARGET Aidl
  APPEND
  PROPERTY QT_ANDROID_BUNDLED_JAR_DEPENDENCIES
           jar/Qt${QtAidl_VERSION_MAJOR}Aidl.jar)

set_property(
  TARGET Aidl
  APPEND
  PROPERTY QT_ANDROID_PERMISSIONS "android.permission.QUERY_ALL_PACKAGES")
