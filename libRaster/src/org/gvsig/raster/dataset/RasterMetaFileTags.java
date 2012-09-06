/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2006 IVER T.I. and Generalitat Valenciana.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,USA.
 */
package org.gvsig.raster.dataset;

/**
 * Tags que corresponden al fichero XML .rmf (Raster Metafile) 
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class RasterMetaFileTags {
	public static final String NAMESPACE = "http://www.gvsig.org";
	public static final String ENCODING = "ISO-8859-15";
	public static final String MAIN_TAG = "RasterMetaFile";
	public static final String LAYER = "FLyrGeoRaster";
	public static final String VIEWPORT = "ViewPort";
	public static final String PROJ = "Projection";
	public static final String BBOX = "Extent";
	public static final String POSX = "X";
	public static final String POSY = "Y";
	public static final String ROTX = "RotationX";
	public static final String ROTY = "RotationY";
	public static final String PX_SIZE_X = "PixelSizeX";
	public static final String PX_SIZE_Y = "PixelSizeY";
	public static final String WIDTH = "Width";
	public static final String HEIGHT = "Height";
	public static final String PX_WIDTH = "ImagePxWidth";
	public static final String PX_HEIGHT = "ImagePxHeight";
	public static final String DIM = "Dimension";
	
	public static final String GEOPOINTS = "GeoPoints";
	public static final String GEOPOINT = "GeoPoint";
	public static final String GEOPOINT_PX = "PixelX";
	public static final String GEOPOINT_PY = "PixelY";
	public static final String GEOPOINT_MAPX = "MapX";
	public static final String GEOPOINT_MAPY = "MapY";
	public static final String GEOPOINT_ACTIVE = "Active";
	public static final String GEOPOINT_LCENTER = "LeftCenterPoint";
	public static final String GEOPOINT_RCENTER = "RightCenterPoint";
	public static final String GEOPOINT_LVP = "LeftViewPort";
	public static final String GEOPOINT_RVP = "RightViewPort";
		
	public static final String POINTS_LYR = "Layer";
	public static final String POINTS_VP = "ViewPortData";
	public static final String POINTS_PROJ = "Proj";
	public static final String POINTS_ZOOM = "Zoom";
	public static final String POINTS_BBOX = "Bbox";
	public static final String POINTS_PX = "Px";
	public static final String POINTS_PY = "Py";
	public static final String POINTS_W = "WCWidth";
	public static final String POINTS_H = "WCHeight";
	public static final String POINTS_DIM = "Pdim";
}