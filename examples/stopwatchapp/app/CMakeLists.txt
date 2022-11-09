cmake_minimum_required(VERSION 3.14)

set(CMAKE_AUTOMOC ON)
set(CMAKE_AUTORCC ON)
set(CMAKE_CXX_STANDARD_REQUIRED ON)
set(CMAKE_VERBOSE_MAKEFILE ON)
set(CMAKE_CXX_STANDARD 11)

find_package(
  Qt6
  COMPONENTS Core Quick
  REQUIRED)

qt_add_executable(StopwatchApp ${CMAKE_CURRENT_LIST_DIR}/src/main.cpp
                  ${CMAKE_CURRENT_LIST_DIR}/res/qml.qrc)

target_link_libraries(StopwatchApp PUBLIC Qt6::Core Qt6::Quick)

if(ANDROID)
  set_property(
    TARGET StopwatchApp
    APPEND
    PROPERTY QT_ANDROID_PACKAGE_SOURCE_DIR ${CMAKE_CURRENT_LIST_DIR}/android)
  set_target_properties(
    StopwatchApp PROPERTIES QT_QML_IMPORT_PATH ${QT_QML_IMPORT_PATH}
                            ${CMAKE_BINARY_DIR}/qml-plugins)
  finalize_android_dependencies(StopwatchApp)
else()
  set_target_properties(StopwatchApp PROPERTIES OUTPUT_NAME "StopwatchApp")
  set_target_properties(StopwatchApp PROPERTIES RUNTIME_OUTPUT_DIRECTORY ../)

  install(
    TARGETS StopwatchApp
    RUNTIME DESTINATION ${ROOT_BINARY_DIR}
    BUNDLE DESTINATION ${ROOT_BINARY_DIR}
    LIBRARY DESTINATION ${ROOT_BINARY_DIR})
endif()
