set(CMAKE_INCLUDE_CURRENT_DIR ON)

set(CMAKE_AUTOMOC ON)
set(CMAKE_AUTORCC ON)
set(CMAKE_AUTOUIC ON)

find_package(Qt6 COMPONENTS Core)
find_package(Qt6 COMPONENTS Qml)
find_package(Qt6 COMPONENTS Quick)
find_package(Qt6 COMPONENTS Aidl) #[1]

#[2]
qt_add_plugin(middleware_backend PLUGIN_TYPE interfaceframework)

#[3]
qt_ifcodegen_extend_target(
  middleware_backend
  IDL_FILES
  ${QFACE_SRC}
  TEMPLATE
  aidl #[4]
  PREFIX
  MIDDLEWARE)

target_link_libraries(middleware_backend PRIVATE middleware Qt6::Aidl Qt6::Core) #[5]

add_android_dependency(middleware_backend)
