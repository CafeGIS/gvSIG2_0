# --- Locate ImageMagick ---
#
# Variable definitions
# --------------------
# IMAGEMAGICK_FOUND
# IMAGEMAGICK_INCLUDE_DIR
# IMAGEMAGICK_DEFINITIONS
# IMAGEMAGICK_LIBRARIES

SET(IMAGEMAGICK_FOUND FALSE)

IF(WIN32)
	FIND_PATH(IMAGEMAGICK_INCLUDE_DIR magick/magick.h ${DEPMAN_PATH}/include)
	IF(IMAGEMAGICK_INCLUDE_DIR)
		SET(IMAGEMAGICK_FOUND TRUE)
		SET(IMAGEMAGICK_DEFINITIONS -D_VISUALC_ -DNeedFunctionPrototypes -D_DLL -D_MAGICKMOD_)
		LINK_DIRECTORIES(${DEPMAN_PATH}/lib)
		SET(WXWIDGETS_LIBRARIES
				CORE_RL_Magick++_.lib
				CORE_RL_magick_.lib
				CORE_RL_wand_.lib
				X11.lib)
		MARK_AS_ADVANCED(IMAGEMAGICK_INCLUDE_DIR)
	ENDIF(IMAGEMAGICK_INCLUDE_DIR)
ENDIF(WIN32)
