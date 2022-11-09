cmake_minimum_required(VERSION 3.16)

project(
  QtAidl
  VERSION "6.0.1"
  DESCRIPTION
    "Qt Interface Framework Libraries Extensions for AIDL based services support"
  HOMEPAGE_URL "https://www.spyro-soft.com/"
  LANGUAGES CXX C)

if(NOT ANDROID)
  message(NOTICE "Skipping the build as only supported platform is ANDROID")
endif()

# TODO: Fix warnings and remove this
set(QT_REPO_NOT_WARNINGS_CLEAN TRUE)

# Make sure we only use latest private CMake API, aka no compatibility wrappers.
set(QT_NO_INTERNAL_COMPATIBILITY_FUNCTIONS TRUE)

find_package(Qt6 CONFIG REQUIRED COMPONENTS BuildInternals Core
                                            InterfaceFramework)

qt_build_repo()
