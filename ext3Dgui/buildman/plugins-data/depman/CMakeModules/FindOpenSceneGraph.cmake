# Locate OpenSceneGraph
# This module defines
# OSG*_LIBRARY (where * matches <empty>, DB, UTIL, ...)
# OSG*_LIBRARY_DEBUG (where * matches <empty>, DB, UTIL, ...)
# OSG_LIBRARIES (All core libraries)
# OSG_LIBRARIES_DEBUG (All debug core libraries)
# OSG_FOUND, if false, do not try to link to OpenSceneGraph
# OSG_INCLUDE_DIR, where to find the headers
#
# $OSG_DIR is an environment variable that would
# correspond to the ./configure --prefix=$OSG_DIR
# used in building osg.
#
# Created by Eric Wing.

# Header files are presumed to be included like
# #include <osg/Version>

# Try the user's environment request before anything else.


FIND_PACKAGE(OpenThreads REQUIRED)

# OSG Core
FIND_PATH(OSG_INCLUDE_DIR osg/Version
	$ENV{OSG_DIR}/include
	$ENV{OSG_DIR}
	~/Library/Frameworks
	/Library/Frameworks
	/usr/local/include
	/usr/include
	/sw/include # Fink
	/opt/local/include # DarwinPorts
	/opt/csw/include # Blastwave
	/opt/include
)

MARK_AS_ADVANCED(OSG_INCLUDE_DIR)

FIND_LIBRARY(OSG_LIBRARY 
	NAMES osg
	PATHS 
	$ENV{OSG_DIR}/lib64
	$ENV{OSG_DIR}/lib
	$ENV{OSG_DIR}
	~/Library/Frameworks
	/Library/Frameworks
	/usr/local/lib64
	/usr/local/lib
	/usr/lib64
	/usr/lib
	/sw/lib
	/opt/local/lib
	/opt/csw/lib
	/opt/lib
)

MARK_AS_ADVANCED(OSG_LIBRARY)
SET(OSG_LIBRARIES ${OSG_LIBRARY} ${OPENTHREADS_LIBRARY})

FIND_LIBRARY(OSG_LIBRARY_DEBUG 
	NAMES osgd
	PATHS
	$ENV{OSG_DIR}/lib64
	$ENV{OSG_DIR}/lib
	$ENV{OSG_DIR}
	~/Library/Frameworks
	/Library/Frameworks
	/usr/local/lib64
	/usr/local/lib
	/usr/lib64
	/usr/lib
	/sw/lib
	/opt/local/lib
	/opt/csw/lib
	/opt/lib
)

MARK_AS_ADVANCED(OSG_LIBRARY_DEBUG)

IF(OSG_LIBRARY)
  IF(NOT OSG_LIBRARY_DEBUG)
	  SET(OSG_LIBRARY_DEBUG "${OSG_LIBRARY}" CACHE FILEPATH "Debug version of osg Library (use regular version if not available)" FORCE)
  ENDIF(NOT OSG_LIBRARY_DEBUG)
ENDIF(OSG_LIBRARY)

SET(OSG_LIBRARIES_DEBUG ${OSG_LIBRARY_DEBUG} ${OPENTHREADS_LIBRARY_DEBUG})	

SET(OSG_FOUND "NO")
IF(OSG_INCLUDE_DIR AND OSG_LIBRARY)
  SET(OSG_FOUND "YES")
ENDIF(OSG_INCLUDE_DIR AND OSG_LIBRARY)


# osgUtil

FIND_LIBRARY(OSGUTIL_LIBRARY 
	NAMES osgUtil
	PATHS 
	$ENV{OSG_DIR}/lib64
	$ENV{OSG_DIR}/lib
	$ENV{OSG_DIR}
	~/Library/Frameworks
	/Library/Frameworks
	/usr/local/lib64
	/usr/local/lib
	/usr/lib64
	/usr/lib
	/sw/lib
	/opt/local/lib
	/opt/csw/lib
	/opt/lib
)

MARK_AS_ADVANCED(OSGUTIL_LIBRARY)
SET(OSG_LIBRARIES ${OSG_LIBRARIES} ${OSGUTIL_LIBRARY})

FIND_LIBRARY(OSGUTIL_LIBRARY_DEBUG 
	NAMES osgUtild
	PATHS
	$ENV{OSG_DIR}/lib64
	$ENV{OSG_DIR}/lib
	$ENV{OSG_DIR}
	~/Library/Frameworks
	/Library/Frameworks
	/usr/local/lib64
	/usr/local/lib
	/usr/lib64
	/usr/lib
	/sw/lib
	/opt/local/lib
	/opt/csw/lib
	/opt/lib
)

MARK_AS_ADVANCED(OSGUTIL_LIBRARY_DEBUG)

IF(OSGUTIL_LIBRARY)
  IF(NOT OSGUTIL_LIBRARY_DEBUG)
	  SET(OSGUTIL_LIBRARY_DEBUG "${OSGUTIL_LIBRARY}" CACHE FILEPATH "Debug version of osgUtil Library (use regular version if not available)" FORCE)
  ENDIF(NOT OSGUTIL_LIBRARY_DEBUG)
ENDIF(OSGUTIL_LIBRARY)
	
SET(OSG_LIBRARIES_DEBUG ${OSG_LIBRARIES_DEBUG} ${OSGUTIL_LIBRARY_DEBUG})

SET(OSGUTIL_FOUND "NO")
IF(OSGUTIL_INCLUDE_DIR AND OSGUTIL_LIBRARY)
  SET(OSGUTIL_FOUND "YES")
ENDIF(OSGUTIL_INCLUDE_DIR AND OSGUTIL_LIBRARY)

# osgViewer

FIND_LIBRARY(OSGVIEWER_LIBRARY 
	NAMES osgViewer
	PATHS 
	$ENV{OSG_DIR}/lib64
	$ENV{OSG_DIR}/lib
	$ENV{OSG_DIR}
	~/Library/Frameworks
	/Library/Frameworks
	/usr/local/lib64
	/usr/local/lib
	/usr/lib64
	/usr/lib
	/sw/lib
	/opt/local/lib
	/opt/csw/lib
	/opt/lib
)

MARK_AS_ADVANCED(OSGVIEWER_LIBRARY)
SET(OSG_LIBRARIES ${OSG_LIBRARIES} ${OSGVIEWER_LIBRARY})

FIND_LIBRARY(OSGVIEWER_LIBRARY_DEBUG 
	NAMES osgViewerd
	PATHS
	$ENV{OSG_DIR}/lib64
	$ENV{OSG_DIR}/lib
	$ENV{OSG_DIR}
	~/Library/Frameworks
	/Library/Frameworks
	/usr/local/lib64
	/usr/local/lib
	/usr/lib64
	/usr/lib
	/sw/lib
	/opt/local/lib
	/opt/csw/lib
	/opt/lib
)

MARK_AS_ADVANCED(OSGVIEWER_LIBRARY_DEBUG)

IF(OSGVIEWER_LIBRARY)
  IF(NOT OSGVIEWER_LIBRARY_DEBUG)
	  SET(OSGVIEWER_LIBRARY_DEBUG "${OSGVIEWER_LIBRARY}" CACHE FILEPATH "Debug version of osgViewer Library (use regular version if not available)" FORCE)
  ENDIF(NOT OSGVIEWER_LIBRARY_DEBUG)
ENDIF(OSGVIEWER_LIBRARY)

SET(OSG_LIBRARIES_DEBUG ${OSG_LIBRARIES_DEBUG} ${OSGVIEWER_LIBRARY_DEBUG})

SET(OSGVIEWER_FOUND "NO")
IF(OSGVIEWER_INCLUDE_DIR AND OSGVIEWER_LIBRARY)
  SET(OSGVIEWER_FOUND "YES")
ENDIF(OSGVIEWER_INCLUDE_DIR AND OSGVIEWER_LIBRARY)


# osgDB

FIND_LIBRARY(OSGDB_LIBRARY 
	NAMES osgDB
	PATHS 
	$ENV{OSG_DIR}/lib64
	$ENV{OSG_DIR}/lib
	$ENV{OSG_DIR}
	~/Library/Frameworks
	/Library/Frameworks
	/usr/local/lib64
	/usr/local/lib
	/usr/lib64
	/usr/lib
	/sw/lib
	/opt/local/lib
	/opt/csw/lib
	/opt/lib
)

SET(OSG_LIBRARIES ${OSG_LIBRARIES} ${OSGDB_LIBRARY})

MARK_AS_ADVANCED(OSGDB_LIBRARY)

FIND_LIBRARY(OSGDB_LIBRARY_DEBUG 
	NAMES osgDBd
	PATHS
	$ENV{OSG_DIR}/lib64
	$ENV{OSG_DIR}/lib
	$ENV{OSG_DIR}
	~/Library/Frameworks
	/Library/Frameworks
	/usr/local/lib64
	/usr/local/lib
	/usr/lib64
	/usr/lib
	/sw/lib
	/opt/local/lib
	/opt/csw/lib
	/opt/lib
)

MARK_AS_ADVANCED(OSGDB_LIBRARY_DEBUG)

IF(OSGDB_LIBRARY)
  IF(NOT OSGDB_LIBRARY_DEBUG)
	  SET(OSGDB_LIBRARY_DEBUG "${OSGDB_LIBRARY}" CACHE FILEPATH "Debug version of osgDB Library (use regular version if not available)" FORCE)
  ENDIF(NOT OSGDB_LIBRARY_DEBUG)
ENDIF(OSGDB_LIBRARY)

SET(OSG_LIBRARIES_DEBUG ${OSG_LIBRARIES_DEBUG} ${OSGDB_LIBRARY_DEBUG})
	
SET(OSGDB_FOUND "NO")
IF(OSGDB_INCLUDE_DIR AND OSGDB_LIBRARY)
  SET(OSGDB_FOUND "YES")
ENDIF(OSGDB_INCLUDE_DIR AND OSGDB_LIBRARY)


# osgGA

FIND_LIBRARY(OSGGA_LIBRARY 
	NAMES osgGA
	PATHS 
	$ENV{OSG_DIR}/lib64
	$ENV{OSG_DIR}/lib
	$ENV{OSG_DIR}
	~/Library/Frameworks
	/Library/Frameworks
	/usr/local/lib64
	/usr/local/lib
	/usr/lib64
	/usr/lib
	/sw/lib
	/opt/local/lib
	/opt/csw/lib
	/opt/lib
)

MARK_AS_ADVANCED(OSGGA_LIBRARY)

FIND_LIBRARY(OSGGA_LIBRARY_DEBUG 
	NAMES osgGAd
	PATHS
	$ENV{OSG_DIR}/lib64
	$ENV{OSG_DIR}/lib
	$ENV{OSG_DIR}
	~/Library/Frameworks
	/Library/Frameworks
	/usr/local/lib64
	/usr/local/lib
	/usr/lib64
	/usr/lib
	/sw/lib
	/opt/local/lib
	/opt/csw/lib
	/opt/lib
)

SET(OSG_LIBRARIES ${OSG_LIBRARIES} ${OSGGA_LIBRARY})

MARK_AS_ADVANCED(OSGGA_LIBRARY_DEBUG)

IF(OSGGA_LIBRARY)
  IF(NOT OSGGA_LIBRARY_DEBUG)
	  SET(OSGGA_LIBRARY_DEBUG "${OSGGA_LIBRARY}" CACHE FILEPATH "Debug version of osgGA Library (use regular version if not available)" FORCE)
  ENDIF(NOT OSGGA_LIBRARY_DEBUG)
ENDIF(OSGGA_LIBRARY)

SET(OSG_LIBRARIES_DEBUG ${OSG_LIBRARIES_DEBUG} ${OSGGA_LIBRARY_DEBUG})
	
SET(OSGGA_FOUND "NO")
IF(OSGGA_INCLUDE_DIR AND OSGGA_LIBRARY)
  SET(OSGGA_FOUND "YES")
ENDIF(OSGGA_INCLUDE_DIR AND OSGGA_LIBRARY)

# osgText

FIND_LIBRARY(OSGTEXT_LIBRARY 
	NAMES osgText
	PATHS 
	$ENV{OSG_DIR}/lib64
	$ENV{OSG_DIR}/lib
	$ENV{OSG_DIR}
	~/Library/Frameworks
	/Library/Frameworks
	/usr/local/lib64
	/usr/local/lib
	/usr/lib64
	/usr/lib
	/sw/lib
	/opt/local/lib
	/opt/csw/lib
	/opt/lib
)

MARK_AS_ADVANCED(OSGTEXT_LIBRARY)
SET(OSG_LIBRARIES ${OSG_LIBRARIES} ${OSGTEXT_LIBRARY})

FIND_LIBRARY(OSGTEXT_LIBRARY_DEBUG 
	NAMES osgTextd
	PATHS
	$ENV{OSG_DIR}/lib64
	$ENV{OSG_DIR}/lib
	$ENV{OSG_DIR}
	~/Library/Frameworks
	/Library/Frameworks
	/usr/local/lib64
	/usr/local/lib
	/usr/lib64
	/usr/lib
	/sw/lib
	/opt/local/lib
	/opt/csw/lib
	/opt/lib
)

MARK_AS_ADVANCED(OSGTEXT_LIBRARY_DEBUG)

IF(OSGTEXT_LIBRARY)
  IF(NOT OSGTEXT_LIBRARY_DEBUG)
	  SET(OSGTEXT_LIBRARY_DEBUG "${OSGTEXT_LIBRARY}" CACHE FILEPATH "Debug version of osgText Library (use regular version if not available)" FORCE)
  ENDIF(NOT OSGTEXT_LIBRARY_DEBUG)
ENDIF(OSGTEXT_LIBRARY)

SET(OSG_LIBRARIES_DEBUG ${OSG_LIBRARIES_DEBUG} ${OSGTEXT_LIBRARY_DEBUG})
	
SET(OSGTEXT_FOUND "NO")
IF(OSGTEXT_INCLUDE_DIR AND OSGTEXT_LIBRARY)
  SET(OSGTEXT_FOUND "YES")
ENDIF(OSGTEXT_INCLUDE_DIR AND OSGTEXT_LIBRARY)

# osgParticle

FIND_LIBRARY(OSGPARTICLE_LIBRARY 
	NAMES osgParticle
	PATHS 
	$ENV{OSG_DIR}/lib64
	$ENV{OSG_DIR}/lib
	$ENV{OSG_DIR}
	~/Library/Frameworks
	/Library/Frameworks
	/usr/local/lib64
	/usr/local/lib
	/usr/lib64
	/usr/lib
	/sw/lib
	/opt/local/lib
	/opt/csw/lib
	/opt/lib
)

MARK_AS_ADVANCED(OSGPARTICLE_LIBRARY)
SET(OSG_LIBRARIES ${OSG_LIBRARIES} ${OSGPARTICLE_LIBRARY})

FIND_LIBRARY(OSGPARTICLE_LIBRARY_DEBUG 
	NAMES osgParticled
	PATHS
	$ENV{OSG_DIR}/lib64
	$ENV{OSG_DIR}/lib
	$ENV{OSG_DIR}
	~/Library/Frameworks
	/Library/Frameworks
	/usr/local/lib64
	/usr/local/lib
	/usr/lib64
	/usr/lib
	/sw/lib
	/opt/local/lib
	/opt/csw/lib
	/opt/lib
)

MARK_AS_ADVANCED(OSGPARTICLE_LIBRARY_DEBUG)

IF(OSGPARTICLE_LIBRARY)
  IF(NOT OSGPARTICLE_LIBRARY_DEBUG)
	  SET(OSGPARTICLE_LIBRARY_DEBUG "${OSGPARTICLE_LIBRARY}" CACHE FILEPATH "Debug version of osgParticle Library (use regular version if not available)" FORCE)
  ENDIF(NOT OSGPARTICLE_LIBRARY_DEBUG)
ENDIF(OSGPARTICLE_LIBRARY)

SET(OSG_LIBRARIES_DEBUG ${OSG_LIBRARIES_DEBUG} ${OSGPARTICLE_LIBRARY_DEBUG})
	
SET(OSGPARTICLE_FOUND "NO")
IF(OSGPARTICLE_INCLUDE_DIR AND OSGPARTICLE_LIBRARY)
  SET(OSGPARTICLE_FOUND "YES")
ENDIF(OSGPARTICLE_INCLUDE_DIR AND OSGPARTICLE_LIBRARY)

# osgFX

FIND_LIBRARY(OSGFX_LIBRARY 
	NAMES osgFX
	PATHS 
	$ENV{OSG_DIR}/lib64
	$ENV{OSG_DIR}/lib
	$ENV{OSG_DIR}
	~/Library/Frameworks
	/Library/Frameworks
	/usr/local/lib64
	/usr/local/lib
	/usr/lib64
	/usr/lib
	/sw/lib
	/opt/local/lib
	/opt/csw/lib
	/opt/lib
)

MARK_AS_ADVANCED(OSGFX_LIBRARY)
SET(OSG_LIBRARIES ${OSG_LIBRARIES} ${OSGFX_LIBRARY})

FIND_LIBRARY(OSGFX_LIBRARY_DEBUG 
	NAMES osgFXd
	PATHS
	$ENV{OSG_DIR}/lib64
	$ENV{OSG_DIR}/lib
	$ENV{OSG_DIR}
	~/Library/Frameworks
	/Library/Frameworks
	/usr/local/lib64
	/usr/local/lib
	/usr/lib64
	/usr/lib
	/sw/lib
	/opt/local/lib
	/opt/csw/lib
	/opt/lib
)

MARK_AS_ADVANCED(OSGFX_LIBRARY_DEBUG)

IF(OSGFX_LIBRARY)
  IF(NOT OSGFX_LIBRARY_DEBUG)
	  SET(OSGFX_LIBRARY_DEBUG "${OSGFX_LIBRARY}" CACHE FILEPATH "Debug version of osgParticle Library (use regular version if not available)" FORCE)
  ENDIF(NOT OSGFX_LIBRARY_DEBUG)
ENDIF(OSGFX_LIBRARY)

SET(OSG_LIBRARIES_DEBUG ${OSG_LIBRARIES_DEBUG} ${OSGFX_LIBRARY_DEBUG})
	
SET(OSGFX_FOUND "NO")
IF(OSGFX_INCLUDE_DIR AND OSGFX_LIBRARY)
  SET(OSGFX_FOUND "YES")
ENDIF(OSGFX_INCLUDE_DIR AND OSGFX_LIBRARY)

# osgTerrain

FIND_LIBRARY(OSGTERRAIN_LIBRARY 
	NAMES osgTerrain
	PATHS
	$ENV{OSG_DIR}/lib64
	$ENV{OSG_DIR}/lib
	$ENV{OSG_DIR}
	~/Library/Frameworks
	/Library/Frameworks
	/usr/local/lib64
	/usr/local/lib
	/usr/lib64
	/usr/lib
	/sw/lib
	/opt/local/lib
	/opt/csw/lib
	/opt/lib
)

MARK_AS_ADVANCED(OSGTERRAIN_LIBRARY)
SET(OSG_LIBRARIES ${OSG_LIBRARIES} ${OSGTERRAIN_LIBRARY})

FIND_LIBRARY(OSGTERRAIN_LIBRARY_DEBUG 
	NAMES osgTerraind
	PATHS
	$ENV{OSG_DIR}/lib64
	$ENV{OSG_DIR}/lib
	$ENV{OSG_DIR}
	~/Library/Frameworks
	/Library/Frameworks
	/usr/local/lib64
	/usr/local/lib
	/usr/lib64
	/usr/lib
	/sw/lib
	/opt/local/lib
	/opt/csw/lib
	/opt/lib
)

MARK_AS_ADVANCED(OSGTERRAIN_LIBRARY_DEBUG)

IF(OSGTERRAIN_LIBRARY)
  IF(NOT OSGTERRAIN_LIBRARY_DEBUG)
	  SET(OSGTERRAIN_LIBRARY_DEBUG "${OSGTERRAIN_LIBRARY}" CACHE FILEPATH "Debug version of osgTerrain Library (use regular version if not available)" FORCE)
  ENDIF(NOT OSGTERRAIN_LIBRARY_DEBUG)
ENDIF(OSGTERRAIN_LIBRARY)

SET(OSG_LIBRARIES_DEBUG ${OSG_LIBRARIES_DEBUG} ${OSGTERRAIN_LIBRARY_DEBUG})
	
SET(OSGTERRAIN_FOUND "NO")
IF(OSGTERRAIN_INCLUDE_DIR AND OSGTERRAIN_LIBRARY)
  SET(OSGTERRAIN_FOUND "YES")
ENDIF(OSGTERRAIN_INCLUDE_DIR AND OSGTERRAIN_LIBRARY)

# osgSim

FIND_LIBRARY(OSGSIM_LIBRARY 
	NAMES osgSim
	PATHS 
	$ENV{OSG_DIR}/lib64
	$ENV{OSG_DIR}/lib
	$ENV{OSG_DIR}
	~/Library/Frameworks
	/Library/Frameworks
	/usr/local/lib64
	/usr/local/lib
	/usr/lib64
	/usr/lib
	/sw/lib
	/opt/local/lib
	/opt/csw/lib
	/opt/lib
)

MARK_AS_ADVANCED(OSGSIM_LIBRARY)
SET(OSG_LIBRARIES ${OSG_LIBRARIES} ${OSGSIM_LIBRARY})

FIND_LIBRARY(OSGSIM_LIBRARY_DEBUG 
	NAMES osgSimd
	PATHS
	$ENV{OSG_DIR}/lib64
	$ENV{OSG_DIR}/lib
	$ENV{OSG_DIR}
	~/Library/Frameworks
	/Library/Frameworks
	/usr/local/lib64
	/usr/local/lib
	/usr/lib64
	/usr/lib
	/sw/lib
	/opt/local/lib
	/opt/csw/lib
	/opt/lib
)

MARK_AS_ADVANCED(OSGSIM_LIBRARY_DEBUG)

IF(OSGSIM_LIBRARY)
  IF(NOT OSGSIM_LIBRARY_DEBUG)
	  SET(OSGSIM_LIBRARY_DEBUG "${OSGSIM_LIBRARY}" CACHE FILEPATH "Debug version of osgSim Library (use regular version if not available)" FORCE)
  ENDIF(NOT OSGSIM_LIBRARY_DEBUG)
ENDIF(OSGSIM_LIBRARY)
	
SET(OSG_LIBRARIES_DEBUG ${OSG_LIBRARIES_DEBUG} ${OSGSIM_LIBRARY_DEBUG})

SET(OSGSIM_FOUND "NO")
IF(OSGSIM_INCLUDE_DIR AND OSGSIM_LIBRARY)
  SET(OSGSIM_FOUND "YES")
ENDIF(OSGSIM_INCLUDE_DIR AND OSGSIM_LIBRARY)

# osgShadow

FIND_LIBRARY(OSGSHADOW_LIBRARY 
	NAMES osgShadow
	PATHS 
	$ENV{OSG_DIR}/lib64
	$ENV{OSG_DIR}/lib
	$ENV{OSG_DIR}
	~/Library/Frameworks
	/Library/Frameworks
	/usr/local/lib64
	/usr/local/lib
	/usr/lib64
	/usr/lib
	/sw/lib
	/opt/local/lib
	/opt/csw/lib
	/opt/lib
)

MARK_AS_ADVANCED(OSGSHADOW_LIBRARY)
SET(OSG_LIBRARIES ${OSG_LIBRARIES} ${OSGSHADOW_LIBRARY})

FIND_LIBRARY(OSGSHADOW_LIBRARY_DEBUG 
	NAMES osgShadowd
	PATHS
	$ENV{OSG_DIR}/lib64
	$ENV{OSG_DIR}/lib
	$ENV{OSG_DIR}
	~/Library/Frameworks
	/Library/Frameworks
	/usr/local/lib64
	/usr/local/lib
	/usr/lib64
	/usr/lib
	/sw/lib
	/opt/local/lib
	/opt/csw/lib
	/opt/lib
)

MARK_AS_ADVANCED(OSGSHADOW_LIBRARY_DEBUG)

IF(OSGSHADOW_LIBRARY)
  IF(NOT OSGSHADOW_LIBRARY_DEBUG)
	  SET(OSGSHADOW_LIBRARY_DEBUG "${OSGSHADOW_LIBRARY}" CACHE FILEPATH "Debug version of osgShadow Library (use regular version if not available)" FORCE)
  ENDIF(NOT OSGSHADOW_LIBRARY_DEBUG)
ENDIF(OSGSHADOW_LIBRARY)

SET(OSG_LIBRARIES_DEBUG ${OSG_LIBRARIES_DEBUG} ${OSGSHADOW_LIBRARY_DEBUG})
	
SET(OSGSHADOW_FOUND "NO")
IF(OSGSHADOW_INCLUDE_DIR AND OSGSHADOW_LIBRARY)
  SET(OSGSHADOW_FOUND "YES")
ENDIF(OSGSHADOW_INCLUDE_DIR AND OSGSHADOW_LIBRARY)

# osgManipulator

FIND_LIBRARY(OSGMANIPULATOR_LIBRARY 
	NAMES osgManipulator
	PATHS 
	$ENV{OSG_DIR}/lib64
	$ENV{OSG_DIR}/lib
	$ENV{OSG_DIR}
	~/Library/Frameworks
	/Library/Frameworks
	/usr/local/lib64
	/usr/local/lib
	/usr/lib64
	/usr/lib
	/sw/lib
	/opt/local/lib
	/opt/csw/lib
	/opt/lib
)

MARK_AS_ADVANCED(OSGMANIPULATOR_LIBRARY)
SET(OSG_LIBRARIES ${OSG_LIBRARIES} ${OSGMANIPULATOR_LIBRARY})

FIND_LIBRARY(OSGMANIPULATOR_LIBRARY_DEBUG 
	NAMES osgManipulatord
	PATHS
	$ENV{OSG_DIR}/lib64
	$ENV{OSG_DIR}/lib
	$ENV{OSG_DIR}
	~/Library/Frameworks
	/Library/Frameworks
	/usr/local/lib64
	/usr/local/lib
	/usr/lib64
	/usr/lib
	/sw/lib
	/opt/local/lib
	/opt/csw/lib
	/opt/lib
)

IF(OSGMANIPULATOR_LIBRARY)
  IF(NOT OSGMANIPULATOR_LIBRARY_DEBUG)
	  SET(OSGMANIPULATOR_LIBRARY_DEBUG "${OSGMANIPULATOR_LIBRARY}" CACHE FILEPATH "Debug version of osgManipulator Library (use regular version if not available)" FORCE)
  ENDIF(NOT OSGMANIPULATOR_LIBRARY_DEBUG)
ENDIF(OSGMANIPULATOR_LIBRARY)
	
MARK_AS_ADVANCED(OSGMANIPULATOR_LIBRARY_DEBUG)

SET(OSG_LIBRARIES_DEBUG ${OSG_LIBRARIES_DEBUG} ${OSGMANIPULATOR_LIBRARY_DEBUG})

SET(OSGMANIPULATOR_FOUND "NO")
IF(OSGMANIPULATOR_INCLUDE_DIR AND OSGMANIPULATOR_LIBRARY)
  SET(OSGMANIPULATOR_FOUND "YES")
ENDIF(OSGMANIPULATOR_INCLUDE_DIR AND OSGMANIPULATOR_LIBRARY)

# osgIntrospection

FIND_LIBRARY(OSGINTROSPECTION_LIBRARY 
	NAMES osgIntrospection
	PATHS 
	$ENV{OSG_DIR}/lib64
	$ENV{OSG_DIR}/lib
	$ENV{OSG_DIR}
	~/Library/Frameworks
	/Library/Frameworks
	/usr/local/lib64
	/usr/local/lib
	/usr/lib64
	/usr/lib
	/sw/lib
	/opt/local/lib
	/opt/csw/lib
	/opt/lib
)

MARK_AS_ADVANCED(OSGINTROSPECTION_LIBRARY)
IF(OSGINTROSPECTION_LIBRARY)
	SET(OSG_LIBRARIES ${OSG_LIBRARIES} ${OSGINTROSPECTION_LIBRARY})
ENDIF(OSGINTROSPECTION_LIBRARY)

FIND_LIBRARY(OSGINTROSPECTION_LIBRARY_DEBUG 
	NAMES osgIntrospectiond
	PATHS
	$ENV{OSG_DIR}/lib64
	$ENV{OSG_DIR}/lib
	$ENV{OSG_DIR}
	~/Library/Frameworks
	/Library/Frameworks
	/usr/local/lib64
	/usr/local/lib
	/usr/lib64
	/usr/lib
	/sw/lib
	/opt/local/lib
	/opt/csw/lib
	/opt/lib
)

IF(OSGINTROSPECTION_LIBRARY)
  IF(NOT OSGINTROSPECTION_LIBRARY_DEBUG)
	  SET(OSGINTROSPECTION_LIBRARY_DEBUG "${OSGINTROSPECTION_LIBRARY}" CACHE FILEPATH "Debug version of osgIntrospection Library (use regular version if not available)" FORCE)
  ENDIF(NOT OSGINTROSPECTION_LIBRARY_DEBUG)
ENDIF(OSGINTROSPECTION_LIBRARY)
	
MARK_AS_ADVANCED(OSGINTROSPECTION_LIBRARY_DEBUG)
IF(OSGINTROSPECTION_LIBRARY_DEBUG)
	SET(OSG_LIBRARIES_DEBUG ${OSG_LIBRARIES_DEBUG} ${OSGINTROSPECTION_LIBRARY_DEBUG})
ENDIF(OSGINTROSPECTION_LIBRARY_DEBUG)

SET(OSGINTROSPECTION_FOUND "NO")
IF(OSGINTROSPECTION_INCLUDE_DIR AND OSGINTROSPECTION_LIBRARY)
  SET(OSGINTROSPECTION_FOUND "YES")
ENDIF(OSGINTROSPECTION_INCLUDE_DIR AND OSGINTROSPECTION_LIBRARY)

