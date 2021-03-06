# DirectX - DirectX SDK
#
# DIRECTX_FOUND
# DIRECTX_INCLUDE_DIR

IF(WIN32)

	FIND_PATH(DIRECTX_INCLUDE_DIR d3d.h ${DEPMAN_PATH}/include)
	MARK_AS_ADVANCED(DIRECTX_INCLUDE_DIR)
	IF(DIRECTX_INCLUDE_DIR)
		SET(DIRECTX_FOUND 1)
	ENDIF(DIRECTX_INCLUDE_DIR)

ENDIF(WIN32)

