/*
 * Created on 02-mar-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2004 IVER T.I. and Generalitat Valenciana.
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
 *
 * For more information, contact:
 *
 *  Generalitat Valenciana
 *   Conselleria d'Infraestructures i Transport
 *   Av. Blasco Ibáñez, 50
 *   46010 VALENCIA
 *   SPAIN
 *
 *      +34 963862235
 *   gvsig@gva.es
 *      www.gvsig.gva.es
 *
 *    or
 *
 *   IVER T.I. S.A
 *   Salamanca 50
 *   46005 Valencia
 *   Spain
 *
 *   +34 963163400
 *   dac@iver.es
 */
package org.gvsig.fmap.geom.utils;


/**
  * Clase utilizada para almacenar las variables estáticas.
 *
 * @author Vicente Caballero Navarro
 */
public class FConstant {
	/** Nombre de las unidades de medida, utilizadas. */
//	public static String[] NAMES = null;
	public final static int SHAPE_TYPE_NULL = 0;
	public final static int SHAPE_TYPE_POINT = 1;
	public final static int SHAPE_TYPE_POLYLINE = 3;
	public final static int SHAPE_TYPE_POLYGON = 5;
	public final static int SHAPE_TYPE_MULTIPOINT = 8;
	public final static int SHAPE_TYPE_POINTZ = 11;
	public final static int SHAPE_TYPE_POLYLINEZ = 13;
	public final static int SHAPE_TYPE_POLYGONZ = 15;
	public final static int SHAPE_TYPE_MULTIPOINTZ = 18;
	public final static int SHAPE_TYPE_POINTM = 21;
	public final static int SHAPE_TYPE_POLYLINEM = 23;
	public final static int SHAPE_TYPE_POLYGONM = 25;
	public final static int SHAPE_TYPE_MULTIPOINTM = 28;
	public final static int LAYER_TYPE_DGN = 1;
	public final static int LAYER_TYPE_DWG = 2;
	public final static int LAYER_TYPE_DXF = 3;
	public final static int LAYER_TYPE_JPEG2000 = 4;
	public final static int LAYER_TYPE_SHP = 5;
	public final static int LAYER_TYPE_WMS = 6;
	public final static int LEGEND_TYPE_BREAK = 3;
	public final static int LEGEND_TYPE_DEFAULT = 1;
	public final static int LEGEND_TYPE_VALUE = 2;
	public final static int SYMBOL_TYPE_DEFAULT = 5;
	public final static int SYMBOL_TYPE_POINT = 1;
	public final static int SYMBOL_TYPE_LINE = 2;
	public final static int SYMBOL_TYPE_FILL = 4;
	public final static int SYMBOL_TYPE_TEXT = 7;
	public final static int SYMBOL_TYPE_ICON = 9;
	public final static int SYMBOL_TYPE_POINTZ = 11;
	public final static int SYMBOL_TYPE_MULTIPOINT = 8;
	public final static int SYMBOL_STYLE_POINTZ = 0;
	public final static int SYMBOL_TYPE_POLYLINEZ = 13;
	public final static int SYMBOL_TYPE_POLYGONZ = 15;




	/**
	 * If you want to obtain a 12-pixel height font you have to apply this
	 * scale factor. <br>
	 * So, it would be: <br>
	 * 	<b> font.setSize(FONT_SCALE_FACTOR*size)</b>
	 */
//	public static final double FONT_HEIGHT_SCALE_FACTOR = 1.4;
	/*
	 * Esto parece un error, tal vez se pensó sólo en el tamaño de las
	 * mayúsculas, sin tener en cuenta que alguna letras (j, g, q) tienen
	 * descendente y que si medimos un texto en vertical desde la parte
	 * superior de una mayúscula a la parte inferior de una minúscula con
	 * descendente no es necesario aplicar este factor de corrección.
	 */



	public static final double DEGREE_TO_RADIANS = Math.PI/180D;


	static {
		new FConstant();
	}

	/**
	 * 050211, jmorell: Se han añadido los Grados.
	 * Crea un nuevo FConstant.
	 */
//	public FConstant() {
//		if (NAMES == null) {
//			int i = 0;
//			NAMES = new String[10];
//			NAMES[i++] = "Kilometros";
//			NAMES[i++] = "Metros";
//			NAMES[i++] = "Centimetros";
//			NAMES[i++] = "Milimetros";
//			NAMES[i++] = "Millas";
//			NAMES[i++] = "Yardas";
//			NAMES[i++] = "Pies";
//			NAMES[i++] = "Pulgadas";
//			NAMES[i++] = "Grados";
//			NAMES[i++] = "Coordenadas";
//		}
//	}
}
