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
* $Id: LayerTree.java 4602 2006-03-27 15:10:06Z jaume $
* $Log$
* Revision 1.6  2006-03-27 15:10:06  jaume
* images moved to libUI, and some code clean up
*
* Revision 1.5  2006/03/21 17:49:04  jaume
* *** empty log message ***
*
* Revision 1.4  2006/02/28 15:25:14  jaume
* *** empty log message ***
*
* Revision 1.2.2.4  2006/02/17 12:57:34  jaume
* oculta/eXconde los nombres de las capas y además corrige el error de selección de varios styles si hay alguna capa seleccionada repetida
*
* Revision 1.2.2.3  2006/01/31 16:25:24  jaume
* correcciones de bugs
*
* Revision 1.3  2006/01/26 16:07:14  jaume
* *** empty log message ***
*
* Revision 1.2.2.1  2006/01/26 12:59:32  jaume
* 0.5
*
* Revision 1.2  2006/01/24 14:36:33  jaume
* This is the new version
*
* Revision 1.1.2.1  2006/01/17 12:56:03  jaume
* *** empty log message ***
*
*
*/
/**
 * 
 */
package com.iver.cit.gvsig.gui.panels;

import java.awt.Component;

import javax.swing.JToolTip;
import javax.swing.JTree;
import javax.swing.ToolTipManager;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.gvsig.gui.beans.controls.MultiLineToolTip;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.fmap.layers.WMSLayerNode;

/**
 * LayerTree extends the standard JTree class to allow use custom tooltips.
 * It is just JTree containing WMSLayerNode at its nodes.
 * 
 * @author jaume
 *
 */
public class LayerTree extends JTree {
	public boolean showLayerNames = false;
	private int count = 0;
	public LayerTree(){
        super();
        ToolTipManager.sharedInstance().registerComponent(this);
        ToolTipManager.sharedInstance().setDismissDelay(60*1000);
        setCellRenderer(new MyRenderer());
    }

    /**
     * Layer tree specific renderer allowing to show multiple line
     * tooltips 
     * @author jaume
     *
     */
    private class MyRenderer extends DefaultTreeCellRenderer {
       
        
       public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
            if (value instanceof WMSLayerNode){
                WMSLayerNode layer = (WMSLayerNode) value;
                
                String myLatLonTxt = layer.getLatLonBox();
                if (myLatLonTxt == null)
                    myLatLonTxt = "-";
                
                String myAbstract = layer.getAbstract();
                
                if (myAbstract == null)
                    myAbstract = "-";
                else 
                    myAbstract = format(myAbstract.trim(), 100);
                
                String text =
                    PluginServices.getText(this, "abstract") + ":\n" + myAbstract +
                    "\n\n" +
                    PluginServices.getText(this, "covered_extension") + ":\n" + myLatLonTxt;
                
                setToolTipText(text);
                
                if (!showLayerNames) {
                	if (layer.getName() != null || layer.getName() == "") {
                		text = layer.toString();
                		text = text.substring(text.indexOf(']')+2, text.length());
                		setText(text);
                	}
                }
                
//                Dimension sz  = getPreferredSize();
//                sz.setSize((sz.getWidth()+50) - (3*count), sz.getHeight());
//                setPreferredSize(sz);
//                count++;
                
            } else {
                setToolTipText(null);
            }
            return this;
        }
    }
    
    
    /**
     * Cuts the message text to force its lines to be shorter or equal to 
     * lineLength.
     * @param message, the message.
     * @param lineLength, the max line length in number of characters.
     * @return the formated message.
     */
    public static String format(String message, int lineLength){
        if (message.length() <= lineLength) return message;
        String[] lines = message.split("\n");
        String theMessage = "";
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i].trim();
            if (line.length()<lineLength)
                theMessage += line+"\n";
            else {
                String[] chunks = line.split(" ");
                String newLine = "";
                for (int j = 0; j < chunks.length; j++) {
                    int currentLength = newLine.length();
                    chunks[j] = chunks[j].trim();
                    if (chunks[j].length()==0)
                        continue;
                    if ((currentLength + chunks[j].length() + " ".length()) <= lineLength)
                        newLine += chunks[j] + " ";
                    else {
                        newLine += "\n"+chunks[j]+" ";
                        theMessage += newLine;
                        newLine = "";
                    }
                }
            }
        }
        return theMessage;
    }
    
    public JToolTip createToolTip() {
    	MultiLineToolTip tip = new MultiLineToolTip();
    	tip.setComponent(this);
    	return tip;
    }
}
