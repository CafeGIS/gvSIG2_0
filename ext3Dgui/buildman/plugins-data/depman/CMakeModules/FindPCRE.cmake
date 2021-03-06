# Locate PCRE
# This module defines
# PCRE_LIBRARY
# PCRE_FOUND, if false, do not try to link to PCRE
# PCRE_INCLUDE_DIRS, where to find the headers

FIND_PATH(PCRE_INCLUDE_DIR pcre.h)
MARK_AS_ADVANCED(PCRE_INCLUDE_DIR)

FIND_LIBRARY(PCRE_LIBRARY pcre)
MARK_AS_ADVANCED(PCRE_LIBRARY)

SET(PCRE_FOUND "NO")
IF(PCRE_INCLUDE_DIR AND PCRE_LIBRARY)
	SET(PCRE_FOUND "YES")
	IF(WIN32)
		ADD_DEFINITIONS(-DPCRE_STATIC)
	ENDIF(WIN32)
ENDIF(PCRE_INCLUDE_DIR AND PCRE_LIBRARY)

