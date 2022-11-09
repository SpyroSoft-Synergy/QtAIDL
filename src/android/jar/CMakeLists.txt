set(java_sources src/com/spyro_soft/aidl/ServiceBinder.java)

qt_internal_add_jar(Qt${QtAidl_VERSION_MAJOR}Aidl SOURCES ${java_sources}
                    OUTPUT_DIR "${CMAKE_CURRENT_BINARY_DIR}/jar")

install_jar(
  Qt${QtAidl_VERSION_MAJOR}Aidl
  DESTINATION jar
  COMPONENT Devel)
