/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2007 IVER T.I. and Generalitat Valenciana.
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
package org.gvsig.fmap.raster.layers;

/**
 * Definición de acciones soportadas para una capa. Distintas capas que hereden de 
 * FLyrRasterSE pueden soportar distintos tipos de acciones.
 * @version 23/08/2007
 * @author Nacho Brodin (nachobrodin@gmail.com)
 *
 */
public interface IRasterLayerActions {
	public static int ZOOM_PIXEL_RESOLUTION = 0;
	public static int CLIPPING              = 1;
	public static int HISTOGRAM             = 2;
	public static int RASTER_PROPERTIES     = 3;
	public static int SAVEAS                = 4;
	public static int FILTER                = 5;
	public static int COLOR_TABLE           = 6;
	public static int FLYRASTER_BAR_TOOLS   = 7;
	public static int BANDS_FILE_LIST       = 8;
	public static int BANDS_RGB             = 9;
	public static int TRANSPARENCY          = 10;
	public static int OPACITY               = 11;
	public static int BRIGHTNESSCONTRAST    = 12;
	public static int ENHANCED              = 13;
	public static int PANSHARPENING         = 14;
	public static int GEOLOCATION           = 15;
	public static int CREATEOVERVIEWS       = 16;
	public static int REPROJECT             = 17;
	public static int SELECT_LAYER          = 18;
	public static int SAVE_COLORINTERP      = 19;
	
	
	/**
   * Controla si una acción está activa o no para una determinada capa. De esta
   * forma una determinada capa puede desactivar una entrada del TOC que no le
   * interese que aparezca.
   * @param action Acción definida en LayerActionSupported
   * @return true si está activa y false si no lo está.
   */
	public boolean isActionEnabled(int action);
}
