package org.gvsig.gui.beans.swing.jComboBoxWithImageIconItems;

import javax.swing.ImageIcon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


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

/**
 * This class has an static method that is an utility to load an image icon
 *
 * @author Pablo Piqueras Bartolomé (p_queras@hotmail.com)
 */
public class ImageIconItemInfoUtils {
	// TRACE - DEBUG
	final static private Logger logger = LoggerFactory.getLogger(ImageIconItemInfoUtils.class);
	// END TRACE - DEBUG

    /**
     * Returns an ImageIcon, or null if the path from class reference was invalid.
     *
     * @param class_Reference A reference to a class which path will be the base to find the resource of the 'path' param
     * @param path Path to the image to load
     * @return javax.swing.ImageIcon
     */
    public static ImageIcon createImageIcon(Class class_Reference, String path) {
    	java.net.URL imgURL = class_Reference.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
        	logger.debug("Couldn't find file: " + path);
            return null;
        }
    }
}
