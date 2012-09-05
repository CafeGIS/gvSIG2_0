FIND_PATH(DEPMAN_PATH NAMES .cache Frameworks bin include lib share
	PATHS
	$ENV{DEPMAN_REPO}
	$ENV{DEPMAN_PATH}
	~/.depman
	/Developer/DepMan
	/DepMan
	$ENV{USERPROFILE}/.depman
    NO_DEFAULT_PATH
)

SET(DEPMAN_FOUND "NO")
IF(DEPMAN_PATH)
  SET(DEPMAN_FOUND "YES")
  SET(CMAKE_INCLUDE_PATH ${DEPMAN_PATH}/include ${DEPMAN_PATH}/Frameworks ${CMAKE_INCLUDE_PATH})
  SET(CMAKE_LIBRARY_PATH ${DEPMAN_PATH}/lib ${DEPMAN_PATH}/Frameworks ${CMAKE_LIBRARY_PATH})
ENDIF(DEPMAN_PATH)

MARK_AS_ADVANCED(DEPMAN_PATH)
