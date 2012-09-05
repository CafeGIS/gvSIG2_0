
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
package org.gvsig.gazetteer.utils.thesaurusjtree;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;

import org.gvsig.gazetteer.querys.FeatureType;


/**
 * This class is a kind of Jtree that loads a Thesaurus in it
 * 
 * 
 * @author Jorge Piera Llodra (piera_jor@gva.es)
 */
public class ThesaurusJTree extends JTree {

/**
 * @param features Features to load
 * @param rootName Name to the root left
 */
    public  ThesaurusJTree(FeatureType[] features, String rootName) {        
        super();
        creteTreeNodes(features,rootName);
        setStyle();        
    } 

/**
 * 
 * 
 */
    private void setStyle() {        
        DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer)this.getCellRenderer();
        renderer.setLeafIcon(null);
        renderer.setClosedIcon(null);
        renderer.setOpenIcon(null);
    } 

/**
 * 
 * 
 * 
 * @param features 
 * @param rootName 
 */
    private void creteTreeNodes(FeatureType[] features, String rootName) {        
        FeatureType tes = new FeatureType("ThesaurusRoot");
        tes.setTitle(rootName);
        DefaultMutableTreeNode top = new DefaultMutableTreeNode(tes);
        createNodes(top,features);
        DefaultTreeModel model = new DefaultTreeModel(top);
		this.setModel(model);
    } 

/**
 * 
 * 
 * 
 * @param top 
 * @param features 
 */
    private void createNodes(DefaultMutableTreeNode top, FeatureType[] features) {        
        if (features == null)
   		    return;
   	  		  	
   		for (int i=0 ; i<features.length ; i++){
   		    top.add(new DefaultMutableTreeNode(features[i]));
   		    createNodes(top.getLastLeaf(),features[i].getFeatures());
   		}    
	
    } 
 }
