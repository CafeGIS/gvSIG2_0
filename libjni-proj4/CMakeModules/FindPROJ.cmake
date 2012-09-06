# Locate proj
# This module defines
# PROJ_LIBRARY
# PROJ_FOUND, if false, do not try to link to proj 
# PROJ_INCLUDE_DIR, where to find the headers
#
# $PROJDIR is an environment variable that would
# correspond to the ./configure --prefix=$PROJ_DIR
# used in building proj.
#
# Created by Eric Wing. I'm not a proj user, but OpenSceneGraph uses it 
# for osgTerrain so I whipped this module together for completeness.
# I actually don't know the conventions or where files are typically
# placed in distros.
# Any real proj users are encouraged to correct this (but please don't
# break the OS X framework stuff when doing so which is what usually seems 
# to happen).

# This makes the presumption that you are include proj.h like
# #include "proj.h"

FIND_PATH(PROJ_INCLUDE_DIR proj_api.h
  PATHS
  $ENV{PROJ_DIR}
  NO_DEFAULT_PATH
    PATH_SUFFIXES include
)

FIND_PATH(PROJ_INCLUDE_DIR proj_api.h
    PATHS ${CMAKE_PREFIX_PATH} # Unofficial: We are proposing this.
    NO_DEFAULT_PATH
    PATH_SUFFIXES include
)

FIND_PATH(PROJ_INCLUDE_DIR proj_api.h
  PATHS
  ~/Library/Frameworks/proj.framework/Headers
  /Library/Frameworks/proj.framework/Headers
  /usr/local/include/proj
  /usr/local/include/PROJ
  /usr/local/include
  /usr/include/proj
  /usr/include/PROJ
  /usr/include
  /sw/include/proj 
  /sw/include/PROJ 
  /sw/include # Fink
  /opt/local/include/proj
  /opt/local/include/PROJ
  /opt/local/include # DarwinPorts
  /opt/csw/include/proj
  /opt/csw/include/PROJ
  /opt/csw/include # Blastwave
  /opt/include/proj
  /opt/include/PROJ
  /opt/include
)

MARK_AS_ADVANCED(PROJ_INCLUDE_DIR)

FIND_LIBRARY(PROJ_LIBRARY 
  NAMES proj PROJ
  PATHS
  $ENV{PROJ_DIR}
  NO_DEFAULT_PATH
  PATH_SUFFIXES lib64 lib
)
FIND_LIBRARY(PROJ_LIBRARY 
  NAMES proj PROJ
  PATHS ${CMAKE_PREFIX_PATH} # Unofficial: We are proposing this.
    NO_DEFAULT_PATH
    PATH_SUFFIXES lib64 lib
)
FIND_LIBRARY(PROJ_LIBRARY 
  NAMES proj PROJ
  PATHS
    ~/Library/Frameworks
    /Library/Frameworks
    /usr/local
    /usr
    /sw
    /opt/local
    /opt/csw
    /opt
    /usr/freeware
    [HKEY_LOCAL_MACHINE\\SYSTEM\\CurrentControlSet\\Control\\Session\ Manager\\Environment;PROJ_ROOT]/lib
  PATH_SUFFIXES lib64 lib
)

MARK_AS_ADVANCED(PROJ_LIBRARY)

SET(PROJ_FOUND "NO")
IF(PROJ_LIBRARY AND PROJ_INCLUDE_DIR)
  SET(PROJ_FOUND "YES")
  SET(PROJ_LIBRARY_DEBUG ${PROJ_LIBRARY})
ENDIF(PROJ_LIBRARY AND PROJ_INCLUDE_DIR)



