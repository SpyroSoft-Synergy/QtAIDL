cmake_minimum_required(VERSION 3.21)

project(
  StopwatchApp
  VERSION 0.1
  LANGUAGES CXX)

if(ANDROID)
  set(CMAKE_MODULE_PATH ${CMAKE_MODULE_PATH} ${CMAKE_CURRENT_SOURCE_DIR}/cmake)
  include(AndroidAPKHelpers)
else()
  set(ROOT_BINARY_DIR
      ${CMAKE_CURRENT_BINARY_DIR}
      CACHE INTERNAL "")
endif()

set(QFACE_SRC ${CMAKE_CURRENT_SOURCE_DIR}/../common/stopwatch.qface)

add_subdirectory(middleware)
add_subdirectory(app)
