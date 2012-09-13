FIND_PATH(JOSG_INCLUDE_DIR josgcore/Export.hpp
    ${JOSG_INCLUDE_DIR}/include
    NO_DEFAULT_PATH
)

IF(NOT JOSG_INCLUDE_DIR)
    FIND_PATH(JOSG_INCLUDE_DIR josgcore/Export.hpp
        PATHS ${CMAKE_PREFIX_PATH}  
        PATH_SUFFIXES include
    )
ENDIF(NOT JOSG_INCLUDE_DIR)

IF(NOT JOSG_INCLUDE_DIR)
    FIND_PATH(JOSG_INCLUDE_DIR josgcore/Export.hpp
        ~/Library/Frameworks
        /Library/Frameworks
        /usr/local/include
        /usr/include
        /sw/include # Fink
        /opt/local/include # DarwinPorts
        /opt/csw/include # Blastwave
        /opt/include
    )
ENDIF(NOT JOSG_INCLUDE_DIR)

MARK_AS_ADVANCED(JOSG_INCLUDE_DIR)

FIND_LIBRARY(JOSGCORE_LIBRARY 
	NAMES josgcore
	PATHS 
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

MARK_AS_ADVANCED(JOSGCORE_LIBRARY)

SET(OSG_LIBRARIES_DEBUG ${OSG_LIBRARY_DEBUG} ${OPENTHREADS_LIBRARY_DEBUG})	

SET(JOSGCORE_FOUND "NO")
IF(JOSGCORE_LIBRARY)
  SET(JOSGCORE_FOUND "YES")
ENDIF(JOSGCORE_LIBRARY)

