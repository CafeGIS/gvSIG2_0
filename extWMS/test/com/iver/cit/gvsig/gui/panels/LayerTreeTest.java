package com.iver.cit.gvsig.gui.panels;

/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2005 IVER T.I. and Generalitat Valenciana.
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

/* CVS MESSAGES:
 *
 * $Id: LayerTreeTest.java 5158 2006-05-12 07:47:39Z jaume $
 * $Log$
 * Revision 1.5  2006-05-12 07:47:39  jaume
 * removed unnecessary imports
 *
 * Revision 1.4  2006/02/28 15:25:15  jaume
 * *** empty log message ***
 *
 * Revision 1.2.2.3  2006/01/31 16:25:24  jaume
 * correcciones de bugs
 *
 * Revision 1.3  2006/01/26 16:07:14  jaume
 * *** empty log message ***
 *
 * Revision 1.2.2.1  2006/01/26 12:59:33  jaume
 * 0.5
 *
 * Revision 1.2  2006/01/24 14:36:33  jaume
 * This is the new version
 *
 * Revision 1.1.2.1  2006/01/19 16:09:30  jaume
 * *** empty log message ***
 *
 *
 */
/**
 * 
 */


/**
 * LayerTree test.
 * 
 * @author jaume
 *
 */
public class LayerTreeTest {
    static String[] texts = new String[] {
    "Release 2 of the WMS Global Mosaic, a seamless mosaic of Landsat7 scenes.\n"+
    "Spatial resolution is 0.5 second for the pan band, 1 second for the visual and near-IR bands and 2 second for the thermal bands\n"+
    "Use this layer to request individual grayscale bands. The default styles may have gamma, sharpening and saturation filters applied.\n"+
    "The grayscale styles have no extra processing applied, and will return the image data as stored on the server.\n"+
    "The source dataset is part of the NASA Scientific Data Purchase, and contains scenes acquired in 1999-2003.\n"+
    "    This layer provides pan-sharpened images, where the pan band is used for the image brightness regardless of the color combination requested."      
    
    ,

    "Release 2 of the WMS Global Mosaic, a seamless mosaic of Landsat7 scenes. Spatial resolution is 0.5 second for the pan band, 1 second for the visual and near-IR bands and 2 second for the thermal bands Use this layer to request individual grayscale bands. The default styles may have gamma, sharpening and saturation filters applied.     The grayscale styles have no extra processing applied, and will return the image data as stored on the server.    The source dataset is part of the NASA Scientific Data Purchase, and contains scenes acquired in 1999-2003.        This layer provides pan-sharpened images, where the pan band is used for the image brightness regardless of the color combination requested."
    };
    
    public static void main(String args[]){       
        for (int i = 0; i < texts.length; i++) {
            System.out.println(LayerTree.format(texts[i], 80));
        }
    }
    
}
