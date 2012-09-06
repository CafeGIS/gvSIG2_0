package org.gvsig.gui.beans.controls.dnd;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.swing.JList;


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
 * $Id: JDnDList.java 13655 2007-09-12 16:28:55Z bsanchez $
 * $Log$
 * Revision 1.3  2007-09-12 16:28:23  bsanchez
 * *** empty log message ***
 *
 * Revision 1.2  2007/08/21 09:57:17  bsanchez
 * - Variable no usada
 *
 * Revision 1.1  2007/08/20 08:34:46  evercher
 * He fusionado LibUI con LibUIComponents
 *
 * Revision 1.2  2006/03/23 10:37:11  jaume
 * *** empty log message ***
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
 * Revision 1.1.2.1  2006/01/10 13:11:38  jaume
 * *** empty log message ***
 *
 * Revision 1.1.2.1  2005/12/29 08:26:54  jaume
 * some gui issues where fixed
 *
 * Revision 1.1.2.1  2005/12/19 18:12:35  jaume
 * *** empty log message ***
 *
 *
 */
/**
 * <p>
 * A JList that allows drag'n'drop elements. It accepts changing position of
 * one or more elements within the list, adding other from other JDnDList's,
 * removing sets of selected items, and other features.
 * </p>
 * <p>
 * In order to use this features you have to use a JDnDListModel as the list's
 * model.
 * </p>
 * jaume dominguez faus - jaume.dominguez@iver.es
 */
public class JDnDList extends JList implements DragSourceListener, DragGestureListener, DropTargetListener{
  private static final long serialVersionUID = -9071985815215584362L;
		private DragSource dragSource;
    private boolean    dragging;
    private int overIndex;
    private int[] selectedIndices;
    
    
    public JDnDList() {
        new DropTarget (this, this);
        dragSource = new DragSource();
        dragSource.createDefaultDragGestureRecognizer( this, DnDConstants.ACTION_MOVE, this);
      }
    
    public JDnDList( JDnDListModel model ) {
        super( model );
        // Configure ourselves to be a drag source
        dragSource = new DragSource();
        dragSource.createDefaultDragGestureRecognizer( this, DnDConstants.ACTION_MOVE, this);
        
        // Configure ourselves to be a drop target
        new DropTarget( this, this );
    }
    
    public void dragGestureRecognized(DragGestureEvent dge)  {
        this.selectedIndices = this.getSelectedIndices();
        Object[] selectedObjects = this.getSelectedValues();
        if( selectedObjects.length > 0 ) {
            StringBuffer sb = new StringBuffer();
            for( int i=0; i<selectedObjects.length; i++ ) {
                sb.append( selectedObjects[ i ].toString() + "\n" );
            }
            
            // Build a StringSelection object that the Drag Source
            // can use to transport a string to the Drop Target
            StringSelection text = new StringSelection( sb.toString() ); 
            
            // Start dragging the object 
            this.dragging = true;
            dragSource.startDrag( dge, DragSource.DefaultMoveDrop, text, this );
        }
    }
    
    public void dragDropEnd(DragSourceDropEvent dsde) {
        this.dragging = false;
    }
    
    public void dragExit(DropTargetEvent dte) {
        this.overIndex = -1;
    }
    public void dragEnter(DropTargetDragEvent dtde) {
        this.overIndex = this.locationToIndex( dtde.getLocation() );
        this.setSelectedIndex( this.overIndex );
    }
    public void dragOver(DropTargetDragEvent dtde) {
        // See who we are over...
        int overIndex = this.locationToIndex( dtde.getLocation() );
        if( overIndex != -1 && overIndex != this.overIndex ) {
            // If the value has changed from what we were previously over
            // then change the selected object to the one we are over; this 
            // is a visual representation that this is where the drop will occur
            this.overIndex = overIndex;
            this.setSelectedIndex( this.overIndex );
        }
    }
    
    public void drop(DropTargetDropEvent dtde) {
        try {
            Transferable transferable = dtde.getTransferable();
            if( transferable.isDataFlavorSupported( DataFlavor.stringFlavor ) ) {
                dtde.acceptDrop( DnDConstants.ACTION_MOVE );
                
                // Find out where the item was dropped
                int newIndex = this.locationToIndex( dtde.getLocation() );
                
                // Get the items out of the transferable object and build an
                // array out of them...
                String s = ( String )transferable.getTransferData( DataFlavor.stringFlavor );
                StringTokenizer st = new StringTokenizer( s );
                ArrayList items = new ArrayList();
                while( st.hasMoreTokens() ) {
                    items.add( st.nextToken() );
                }
                JDnDListModel model = ( JDnDListModel )this.getModel();
                
                // If we are dragging from our this to our list them move the items,
                // otherwise just add them...
                if( this.dragging ) {
                    //model.itemsMoved( newIndex, items );
                    model.itemsMoved( newIndex, this.selectedIndices );
                } else {
                    model.insertItems( newIndex, items );
                }
                
                // Update the selected indicies
                int[] newIndicies = new int[ items.size() ];
                for( int i=0; i<items.size(); i++ ) {
                    newIndicies[ i ] = newIndex + i;
                }
                this.setSelectedIndices( newIndicies );
                
                // Reset the over index
                this.overIndex = -1;
                
                dtde.getDropTargetContext().dropComplete( true );
            } else {
                dtde.rejectDrop();
            }
        } catch( IOException exception ) {
            exception.printStackTrace();
            System.err.println( "Exception" + exception.getMessage());
            dtde.rejectDrop();
        } catch( UnsupportedFlavorException ufException ) {
            ufException.printStackTrace();
            System.err.println( "Exception" + ufException.getMessage());
            dtde.rejectDrop();
        }
    }

    /* (non-Javadoc)
     * @see java.awt.dnd.DragSourceListener#dragEnter(java.awt.dnd.DragSourceDragEvent)
     */
    public void dragEnter(DragSourceDragEvent dsde) {
        
    }

    /* (non-Javadoc)
     * @see java.awt.dnd.DragSourceListener#dragOver(java.awt.dnd.DragSourceDragEvent)
     */
    public void dragOver(DragSourceDragEvent dsde) {
        
    }

    /* (non-Javadoc)
     * @see java.awt.dnd.DragSourceListener#dropActionChanged(java.awt.dnd.DragSourceDragEvent)
     */
    public void dropActionChanged(DragSourceDragEvent dsde) {
        
    }

    /* (non-Javadoc)
     * @see java.awt.dnd.DragSourceListener#dragExit(java.awt.dnd.DragSourceEvent)
     */
    public void dragExit(DragSourceEvent dse) {
        
    }

    /* (non-Javadoc)
     * @see java.awt.dnd.DropTargetListener#dropActionChanged(java.awt.dnd.DropTargetDragEvent)
     */
    public void dropActionChanged(DropTargetDragEvent dtde) {
        
    }
    
    /**
     * Constructs a <b>JDnDListModel</b> from an array of objects and then applies setModel to it. 
     * @param listData an array of Objects containing the items to display in the list
     * @see	setModel
     * @author jaume
     */
    public void setListData(Object[] obj) {
    	JDnDListModel model = new JDnDListModel();
		for (int i = 0; i < obj.length; i++) {
			model.addElement(obj[i]);
		}
		this.setModel(model);
    }
}