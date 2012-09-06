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
* $Id: JDnDListModel.java 13655 2007-09-12 16:28:55Z bsanchez $
* $Log$
* Revision 1.2  2007-09-12 16:28:23  bsanchez
* *** empty log message ***
*
* Revision 1.1  2007/08/20 08:34:46  evercher
* He fusionado LibUI con LibUIComponents
*
* Revision 1.1  2006/03/22 11:18:29  jaume
* *** empty log message ***
*
* Revision 1.4  2006/02/28 15:25:14  jaume
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
* Revision 1.1.2.2  2006/01/17 12:55:40  jaume
* *** empty log message ***
*
* Revision 1.1.2.1  2006/01/10 13:11:38  jaume
* *** empty log message ***
*
* Revision 1.1.2.1  2005/12/29 08:26:54  jaume
* some gui issues where fixed
*
* Revision 1.1.2.2  2005/12/22 16:46:00  jaume
* puede borrar múltiples intervalos de entradas dentro de la lista
*
* Revision 1.1.2.1  2005/12/19 18:12:35  jaume
* *** empty log message ***
*
*
*/
/**
 * 
 */
package org.gvsig.gui.beans.controls.dnd;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.AbstractListModel;


/**
 * List model to use in junction the JDnDList. Contains some useful tools.
 *
 * @author jaume dominguez faus - jaume.dominguez@iver.es
 *
 */
public class JDnDListModel extends AbstractListModel {
  private static final long serialVersionUID = 8998356167841647319L;
		private ArrayList items = new ArrayList();
    /**
     * Inserts a collection of items before the specified index
     */
    public void insertItems( int index, Collection objects ) {
        // Handle the case where the items are being added to the end of the list
        if( index == -1 ) {
            // Add the items
            for( Iterator i = objects.iterator(); i.hasNext(); ) {
                String item = ( String )i.next();
                addElement(items.size(), item );
            }
        } else {
            // Insert the items
            for( Iterator i = objects.iterator(); i.hasNext(); ) {
                Object item = i.next();
                insertElement( index++, item );
            }
        }
        
        // Tell the list to update itself
        this.fireContentsChanged( this, 0, this.items.size() - 1 );
    }
    
    /**
     * Inserts a new element into the list at the position mentioned in the index param.
     * @param index
     * @param item
     */
    public boolean insertElement(int index, Object element) {
        if (element == null) {
            return false;
        }
        for (int i = 0; i < items.size(); i++) {
            
            if (items.get(i).equals(items)) {
                return false;
            }
        }
        
        this.items.add(index, element);
        return true;
    }

    /**
     * Adds a new element at the position indicated by pos of the list.
     * @param j 
     * @param pos 
     */
    public boolean addElement(int j, Object element) {
        if (element == null) {
            return false;
        }
        
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).equals(items)) {
                return false;
            }
        }
        this.items.add(j, element);
        fireContentsChanged(this, items.size() - 1, items.size() - 1);
        return true;
    }
    
    /**
     * Adds a new element at the position indicated by pos of the list.
     * @param j 
     * @param pos 
     */
    public boolean addElement(Object element) {
        return addElement(items.size(), element);
    }
    
    /**
     * Removes every elements contained in the collection from this list.
     */
    public void delElements(Collection c) {
        items.removeAll(c);
        this.fireContentsChanged(this, 0, items.size());
    }
    
    /**
     * Removes the items of the list mentioned by the index array passed as argument.
     * @param indices
     */
    public void delIndices(int[] indices){
        int removed = 0;
        for (int i = 0; i < indices.length; i++) {
            items.remove(indices[i]-removed);
            removed++;
        }
    }

    public void itemsMoved( int newIndex, int[] indicies ) {
        
        // Copy the objects to a temporary ArrayList
        ArrayList objects = new ArrayList();
        for( int i=0; i<indicies.length; i++ ) {
            objects.add( this.items.get( indicies[ i ] ) );
        }
        
        // Delete the objects from the list
        for( int i=indicies.length-1; i>=0; i-- ) {
            this.items.remove( indicies[ i ] );
        }
        
        // Insert the items at the new location
        insertItems( newIndex, objects );
    }
    
    /* (non-Javadoc)
     * @see javax.swing.ListModel#getSize()
     */
    public int getSize() {
        return items.size();
    }
    
    /* (non-Javadoc)
     * @see javax.swing.ListModel#getElementAt(int)
     */
    public Object getElementAt(int index) {
        return items.get(index);
    }
    
    /**
     * Removes any item currently contained by this list.
     *
     */
    public void clear() {
        items.clear();
        fireContentsChanged(this, 0, 0);
    }
    
    /**
     * Returns an ArrayList containing the elements of this list.
     */
    public ArrayList getElements() {
        return items;
    }

    
}
