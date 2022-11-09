set(CMAKE_INCLUDE_CURRENT_DIR ON)

set(CMAKE_AUTOMOC ON)
set(CMAKE_AUTORCC ON)
set(CMAKE_AUTOUIC ON)

find_package(Qt6 COMPONENTS Core)
find_package(Qt6 COMPONENTS InterfaceFramework)
find_package(Qt6 COMPONENTS Qml)
find_package(Qt6 COMPONENTS Quick)

qt_add_plugin(middleware_backend)
set_target_properties(middleware_backend PROPERTIES LIBRARY_OUTPUT_DIRECTORY
                                                    ../../interfaceframework)
qt_ifcodegen_extend_target(middleware_backend IDL_FILES ${QFACE_SRC} TEMPLATE
                           backend_simulator)

target_link_libraries(middleware_backend PUBLIC middleware Qt6::Core Qt6::Gui
                                                Qt6::InterfaceFramework)

qt_add_resources(middleware_backend "simulation" PREFIX "/" FILES simulation.qml)

set(import_path "${CMAKE_CURRENT_BINARY_DIR}/backend_simulator/qml")
if(NOT ${import_path} IN_LIST QML_IMPORT_PATH)
  list(APPEND QML_IMPORT_PATH "${import_path}")
  set(QML_IMPORT_PATH
      ${QML_IMPORT_PATH}
      CACHE INTERNAL "" FORCE)
endif()
