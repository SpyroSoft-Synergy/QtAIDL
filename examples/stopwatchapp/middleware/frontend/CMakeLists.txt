set(CMAKE_INCLUDE_CURRENT_DIR ON)

set(CMAKE_AUTOMOC ON)
set(CMAKE_AUTORCC ON)
set(CMAKE_AUTOUIC ON)

find_package(Qt6 COMPONENTS Core)
find_package(Qt6 COMPONENTS InterfaceFramework)
find_package(Qt6 COMPONENTS Qml)
find_package(Qt6 COMPONENTS Quick)

qt_add_library(middleware)

if(NOT ANDROID)
  set_target_properties(middleware PROPERTIES OUTPUT_NAME "frontend")
  set_target_properties(middleware PROPERTIES RUNTIME_OUTPUT_DIRECTORY ../../)
endif()

qt_ifcodegen_extend_target(middleware IDL_FILES ${QFACE_SRC} TEMPLATE frontend)

target_link_libraries(
  middleware PUBLIC Qt6::Core Qt6::InterfaceFramework
                    Qt6::InterfaceFrameworkPrivate Qt6::Qml Qt6::Quick)

if(NOT ANDROID)
  set(import_path "${CMAKE_CURRENT_BINARY_DIR}/frontend/qml")
  if(NOT ${import_path} IN_LIST QML_IMPORT_PATH)
    list(APPEND QML_IMPORT_PATH "${import_path}")
    set(QML_IMPORT_PATH
        ${QML_IMPORT_PATH}
        CACHE INTERNAL "" FORCE)
  endif()
else()
  add_android_dependency(middleware)
endif()
