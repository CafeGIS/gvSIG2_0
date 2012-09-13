# Locate CoreVideo
# This module defines
# COREVIDEO_LIBRARY
# COREVIDEO_FOUND, if false, do not try to link to gdal 
#

IF(APPLE)
	FIND_LIBRARY(COREVIDEO_LIBRARY CoreVideo)
ENDIF(APPLE)

SET(COREVIDEO_FOUND "NO")
IF(COREVIDEO_LIBRARY)
	SET(COREVIDEO_FOUND "YES")
	MARK_AS_ADVANCED(COREVIDEO_LIBRARY)
ENDIF(COREVIDEO_LIBRARY)

