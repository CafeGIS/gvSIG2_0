
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
package org.gvsig.catalog.protocols;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;

/**
 * This class is used lo load an image from a URL. It saves the image
 * in a file
 * 
 * 
 * @author Jorge Piera Llodra (piera_jor@gva.es)
 */
public class GetImageFromWeb {

/**
 * It saves an image in a file
 * 
 * 
 * @return 
 * @param sUrl URL that contains the image
 * @param sfile File to save the image
 */
    public boolean getImageUrl(String sUrl, String sfile) {        
        URL url;
        try {
            url = new URL(sUrl);
            BufferedImage img = ImageIO.read(url);
            
            if (img == null)
                return false;            
            
            File outFile = new File(sfile);
                                   
            //We must to change this value: getCapabalilities request
            //has the supported formats
            return ImageIO.write(img, "jpeg", outFile);
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            System.out.println("La URL de la imagen no es correcta");
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            System.out.println("No he podido leer la imagen desde la URL");
        }
        return false;
    } 
 }
