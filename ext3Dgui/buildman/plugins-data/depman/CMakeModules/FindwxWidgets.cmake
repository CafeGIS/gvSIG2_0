# --- Locate wxWidgets ---
#
# Variable definitions
# --------------------
# WXWIDGETS_FOUND
# WXWIDGETS_INCLUDE_DIRS
# WXWIDGETS_DEFINITIONS
# WXWIDGETS_LIBRARIES    # only for non-win32 platforms
#
# Libraries are linked using #pragma directives on VS2005

SET(WXWIDGETS_FOUND FALSE)

IF(WIN32)
	FIND_PATH(WXWIDGETS_INCLUDE_DIRS wx/wx.h ${DEPMAN_PATH}/include)
	IF(WXWIDGETS_INCLUDE_DIRS)
		SET(WXWIDGETS_FOUND TRUE)
		SET(WXWIDGETS_INCLUDE_DIRS ${WXWIDGETS_INCLUDE_DIRS} ${DEPMAN_PATH}/include/msvc)
		SET(WXWIDGETS_DEFINITIONS -D__WXMSW__ -DWXUSINGDLL -DNOPCH)
		LINK_DIRECTORIES(${DEPMAN_PATH}/lib)
		MARK_AS_ADVANCED(WXWIDGETS_INCLUDE_DIRS)
	ENDIF(WXWIDGETS_INCLUDE_DIRS)
ENDIF(WIN32)

IF(APPLE)
	FIND_PATH(WXWIDGETS_INCLUDE_DIRS wx-2.8/wx/wx.h ${DEPMAN_PATH}/include)
	IF(WXWIDGETS_INCLUDE_DIRS)
		SET(WXWIDGETS_FOUND TRUE)
		SET(WXWIDGETS_INCLUDE_DIRS
				${DEPMAN_PATH}/lib/wx/include/mac-ansi-release-static-2.8
				${DEPMAN_PATH}/include/wx-2.8)
		SET(WXWIDGETS_DEFINITIONS -D_FILE_OFFSET_BITS=64 -D_LARGE_FILES -D__WXMAC__)
		LINK_DIRECTORIES(${DEPMAN_PATH}/lib)
		SET(WXWIDGETS_LIBRARIES
				"-framework IOKit"
				"-framework Carbon"
				"-framework Cocoa"
				"-framework System"
				"-framework QuickTime"
				wx_mac_aui-2.8
				wx_mac_xrc-2.8
				wx_mac_qa-2.8
				wx_mac_html-2.8
				wx_mac_adv-2.8
				wx_mac_core-2.8
				wx_base_carbon_xml-2.8
				wx_base_carbon_net-2.8
				wx_base_carbon-2.8
				"-framework WebKit"
				wxexpat-2.8
				wxtiff-2.8
				wxjpeg-2.8
				wxpng-2.8
				z
				pthread
				iconv)
		MARK_AS_ADVANCED(WXWIDGETS_INCLUDE_DIRS)
	ENDIF(WXWIDGETS_INCLUDE_DIRS)
ENDIF(APPLE)

