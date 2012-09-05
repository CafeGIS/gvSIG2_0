package org.gvsig.catalog.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;

import org.gvsig.catalog.schemas.Record;


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
/* CVS MESSAGES:
 *
 * $Id: ImageRetriever.java 600 2007-09-19 11:30:05 +0000 (Wed, 19 Sep 2007) jpiera $
 * $Log$
 * Revision 1.1.2.1  2007/07/24 09:45:52  jorpiell
 * Fix some interface bugs
 *
 *
 */
/**
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public class ImageRetriever {
	private static File defaultFile = new File("images/IcoRecord.png");
	
	public static BufferedImage getImageUrl(Record record) {        
		BufferedImage img = null;
		String imageURL = record.getImageURL();
		
		//Normal Procedure       
		img = getImage(imageURL);
		if (img != null){
			record.setImage(img);
			return img;
		}
		
		try {
			return ImageIO.read(defaultFile);
		} catch (IOException e2) {
			System.out.println("Default image cant be loaded");
			return null;
		}  
	}

	/**
	 * It gets an image from a URL
	 * 
	 * 
	 * @return 
	 * @param sUrl String with the image URL
	 */
	protected static BufferedImage getImage(String sUrl) {        
		try {
			URL Url = new URL(sUrl);
			return ImageIO.read(Url);
		} catch (MalformedURLException e) {
			System.out.println("The image URL is not valid: " + sUrl);
		} catch (IOException e1) {			
			System.out.println("The image cant be loaded: " + sUrl);
		}
		return null;
	} 
}
