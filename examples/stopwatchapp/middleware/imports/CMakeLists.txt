# Generated from imports.pro.
set(CMAKE_INCLUDE_CURRENT_DIR ON)

set(CMAKE_AUTOMOC ON)
set(CMAKE_AUTORCC ON)
set(CMAKE_AUTOUIC ON)

find_package(Qt6 COMPONENTS Core)
find_package(Qt6 COMPONENTS InterfaceFramework)
find_package(Qt6 COMPONENTS Qml)
find_package(Qt6 COMPONENTS Quick)

qt_ifcodegen_import_variables(MIDDLEWARE IDL_FILES ${QFACE_SRC} TEMPLATE
                              qmlplugin)

if(NOT ANDROID)
  qt_add_qml_module(
    middleware_imports
    OUTPUT_DIRECTORY
    "${ROOT_BINARY_DIR}/${MIDDLEWARE_URI_PATH}"
    URI
    ${MIDDLEWARE_URI}
    VERSION
    ${MIDDLEWARE_VERSION}
    PLUGIN_TARGET
    middleware_imports
    NO_PLUGIN_OPTIONAL
    NO_GENERATE_PLUGIN_SOURCE
    SOURCES
    ${MIDDLEWARE_SOURCES})
else()
  qt_add_qml_module(
    middleware_imports
    OUTPUT_DIRECTORY
    "${CMAKE_BINARY_DIR}/qml-plugins/${MIDDLEWARE_URI_PATH}"
    URI
    ${MIDDLEWARE_URI}
    VERSION
    ${MIDDLEWARE_VERSION}
    PLUGIN_TARGET
    middleware_imports
    NO_PLUGIN_OPTIONAL
    NO_GENERATE_PLUGIN_SOURCE
    SOURCES
    ${MIDDLEWARE_SOURCES})

  set(import_path "${CMAKE_CURRENT_BINARY_DIR}/qml")

  if(NOT ${import_path} IN_LIST QML_IMPORT_PATH)
    list(APPEND QML_IMPORT_PATH "${import_path}")
    set(QML_IMPORT_PATH
        ${QML_IMPORT_PATH}
        CACHE INTERNAL "" FORCE)
  endif()

endif()

target_link_libraries(middleware_imports PUBLIC middleware Qt6::Core
                                                Qt6::InterfaceFramework)
