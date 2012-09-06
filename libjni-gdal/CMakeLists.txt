set(CMAKE_ALLOW_LOOSE_LOOP_CONSTRUCTS TRUE)

IF(WIN32)
    CMAKE_MINIMUM_REQUIRED(VERSION 2.4.6 FATAL_ERROR)
ELSE(WIN32)
    IF(APPLE)
        CMAKE_MINIMUM_REQUIRED(VERSION 2.6.0 FATAL_ERROR)
    ELSE(APPLE)
        CMAKE_MINIMUM_REQUIRED(VERSION 2.4.4 FATAL_ERROR)
    ENDIF(APPLE)
ENDIF(WIN32)

if(COMMAND cmake_policy)
    # Works around warnings libraries linked against that don't
    # have absolute paths (e.g. -lpthreads)
    cmake_policy(SET CMP0003 NEW)

    # Works around warnings about escaped quotes in ADD_DEFINITIONS
    # statements.
    cmake_policy(SET CMP0005 NEW)

    # cmake-2.6.1 introduces policy cmp0008 decide how to treat full path libraries that do not appear to be valid library file names
    # quote from cvslog "Such libraries worked by accident in the VS IDE and Xcode generators in CMake 2.4 and below."
    if(${CMAKE_MAJOR_VERSION} EQUAL 2 AND ${CMAKE_MINOR_VERSION} GREATER 4 AND ${CMAKE_PATCH_VERSION} GREATER 0)
        cmake_policy(SET CMP0008 OLD)
    endif()
endif()

PROJECT(jgdal)

SET(JGDAL_VERSION_MAJOR "2")
SET(JGDAL_VERSION_MINOR "0")
SET(JGDAL_VERSION_PATCH "0")
SET(VERSION "${JGDAL_VERSION_MAJOR}.${JGDAL_VERSION_MINOR}.${JGDAL_VERSION_PATCH}")

SET(CMAKE_MODULE_PATH "${CMAKE_SOURCE_DIR}/../build/CMakeModules;${CMAKE_SOURCE_DIR}/CMakeModules;${CMAKE_MODULE_PATH}")

FIND_PACKAGE(DepMan REQUIRED) 
INCLUDE(GeneralMacros) 

CONFIGURE_DEFAULTS()

IF(CMAKE_INSTALL_PREFIX_INITIALIZED_TO_DEFAULT)
  SET(CMAKE_INSTALL_PREFIX
    ${DEPMAN_PATH} CACHE PATH "depman path install prefix" FORCE
    )
ENDIF(CMAKE_INSTALL_PREFIX_INITIALIZED_TO_DEFAULT)

FIND_PACKAGE(JNI REQUIRED) 
FIND_PACKAGE(GDAL REQUIRED) 

ADD_SUBDIRECTORY(src/main/native)

CONFIGURE_END()


