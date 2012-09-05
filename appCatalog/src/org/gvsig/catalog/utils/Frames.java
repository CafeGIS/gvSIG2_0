
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
package org.gvsig.catalog.utils;
import java.awt.GraphicsEnvironment;
import java.awt.Point;

import javax.swing.JDialog;
import javax.swing.JFrame;

/**
 * 
 * 
 * 
 * @author Jorge Piera Llodra (piera_jor@gva.es)
 */
public class Frames {

/**
 * Sets a JFrame on the screen center
 * 
 * 
 * @param frame JFrams
 * @param width Frame width
 * @param height Frame height
 */
    public static void centerFrame(JFrame frame, int width, int height) {        
        Point p = getWindowCenter(width,height);
        frame.setBounds(p.x, p.y, width, height);
        frame.validate();
    } 

/**
 * Sets a JDialog on the screen center
 * 
 * 
 * @param dialog JDilog
 * @param width Dilaog width
 * @param height Dialog height
 */
    public static void centerFrame(JDialog dialog, int width, int height) {        
        Point p = getWindowCenter(width,height);
        dialog.setBounds(p.x, p.y, width, height);
        dialog.validate();
    } 

/**
 * Sets a JDialog on the upper rigth corner
 * 
 * 
 * @param dialog JDilog
 * @param width Dilaog width
 * @param height Dialog height
 */
    public static void searchDialogPosition(JDialog dialog, int width, int height) {        
        Point p = getWindowCenter(width,height);
        int x = new Double(p.x * 2).intValue();
        int y = new Double(p.y *         0.25).intValue();
        dialog.setBounds(x, y, width, height);
        dialog.validate();
    } 

/**
 * It returns the screen center
 * 
 * 
 * @return 
 */
    private static Point getCenter() {        
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        return ge.getCenterPoint();
    } 

/**
 * It returns the point to center a frame
 * 
 * 
 * @return 
 * @param width Frame width
 * @param height Frame height
 */
    private static Point getWindowCenter(int width, int height) {        
        Point center = getCenter();
        center.x = center.x - (width / 2);
        center.y = center.y - (height / 2);
        return center;
    } 
 }
