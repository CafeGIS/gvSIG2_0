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
* $Id: JDnDTable.java 13655 2007-09-12 16:28:55Z bsanchez $
* $Log$
* Revision 1.3  2007-09-12 16:28:23  bsanchez
* *** empty log message ***
*
* Revision 1.2  2007/08/21 09:56:44  bsanchez
* - Variable no usada
*
* Revision 1.1  2007/08/20 08:34:46  evercher
* He fusionado LibUI con LibUIComponents
*
* Revision 1.1  2006/09/21 16:35:12  jaume
* *** empty log message ***
*
*
*/
package org.gvsig.gui.beans.controls.dnd;

import java.awt.Component;
import java.awt.Point;
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
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
/**
 * Preten ser una taula que accepte arrossegar i soltar. Ja mou coses i això
 * però encara està en proves.
 *
 * La intenció és que pugues moure columnes, però una volta posats, que moga
 * línies i cel·les lliurement.
 * @author jaume dominguez faus - jaume.dominguez@iver.es
 *
 */
public class JDnDTable extends JTable implements DragSourceListener,
								DragGestureListener, DropTargetListener{
  private static final long serialVersionUID = -5479369256188567414L;
	private CellCoordinates	overCellCoordinates = null;
	private DragSource	dragSource;
	private CellCoordinates[]	selectedCells;
	private boolean	dragging;
	private CellCoordinates currSelectedCell;
	static final short FREE_CELL_MOVING = -4;
	public static final short ONLY_ALLOW_MOVING_ROWS = -2;
	public static final short ONLY_ALLOW_MOVING_COLUMNS = -1;



	public JDnDTable() {
		// Configure ourselves to be a drag source
		dragSource = new DragSource();
		dragSource.createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_MOVE, this);

		// Configure ourselves to be a drop target
		new DropTarget(this, this);
	}

	public JDnDTable( JDnDTableModel model ) {
        super( model );
        // Configure ourselves to be a drag source
        dragSource = new DragSource();
        dragSource.createDefaultDragGestureRecognizer( this, DnDConstants.ACTION_MOVE, this);

        // Configure ourselves to be a drop target
        new DropTarget( this, this );
    }

	public void dragEnter(DragSourceDragEvent dsde) {
		Point thisLocation = this.getLocationOnScreen();
		Point clickLocation = dsde.getLocation();

		Point2D relativeLocation = new Point2D.Double(
				(clickLocation.getX() - thisLocation.getX()),
				(clickLocation.getY() - thisLocation.getY()));

		this.overCellCoordinates = locationToCellCoord(relativeLocation);
	}

	public void setSelectionMode(short mode) {
		if (mode == ONLY_ALLOW_MOVING_COLUMNS) {
			setColumnSelectionAllowed(true);
			setRowSelectionAllowed(false);
		} else if ( mode == ONLY_ALLOW_MOVING_ROWS) {
			setColumnSelectionAllowed(false);
			setRowSelectionAllowed(true);
		}
		((JDnDTableModel) this.getModel()).setSelectionMode(mode);
	}
	private CellCoordinates locationToCellCoord(Point2D location) {

		int[] ij = new int[2];
		int width = (int) location.getX();
		int height = (int) location.getY();

		for (int i = 0; i < this.getColumnCount(); i++) {
			int iColumnWidth = this.getColumnModel().getColumn(i).getWidth();
			// it seems to be right, but just in case consideer to use:
			// ? int iColumnWidth = this.getColumnModel().getColumn(i).getPreferredWidth();
			if ((width - iColumnWidth) <= 0) {
				ij[1] = i;
				break;
			}
			width = width - iColumnWidth;
		}

		// Get the current default height for all rows
        int jRowHeight = this.getRowHeight();
		for (int j = 0; j < this.getRowCount(); j++) {
	        // Determine highest cell in the row
	        for (int c=0; c<this.getColumnCount(); c++) {
	            TableCellRenderer renderer = this.getCellRenderer(j, c);
	            Component comp = this.prepareRenderer(renderer, j, c);
	            int h = comp.getPreferredSize().height - 2*rowMargin;
	            jRowHeight = Math.max(jRowHeight, h);
	        }

	        if ((height - jRowHeight) <= 0) {
	        	ij[0] = j;
	        	break;
	        }
	        height = height - jRowHeight;
		}

		return new CellCoordinates(ij[0], ij[1]);
	}


	public void dragOver(DragSourceDragEvent dsde) {
		// TODO Auto-generated method stub
	}

	public void dropActionChanged(DragSourceDragEvent dsde) {
		// TODO Auto-generated method stub
	}

	public void dragDropEnd(DragSourceDropEvent dsde) {
		System.out.println("dragDropEnd");
		this.dragging = false;
	}

	public void dragExit(DragSourceEvent dse) {
		this.overCellCoordinates = null;
	}

	public void dragGestureRecognized(DragGestureEvent dge) {
		// TODO Auto-generated method stub
		this.selectedCells = this.getSelectedCells();
		Object[] selectedObjects = this.getSelectedValues();
		if (selectedObjects.length>0) {
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
            System.err.println("Selected objects\n"+sb.toString());
		}
	}

	private Object[] getSelectedValues() {
		ArrayList values = new ArrayList(selectedCells.length);
		for (int i = 0; i < selectedCells.length; i++) {
			values.add(this.getValueAt(selectedCells[i].i, selectedCells[i].j));
		}
		return values.toArray();
	}

	private CellCoordinates[] getSelectedCells() {
		ArrayList cells = new ArrayList();
		for (int i = 0; i < this.getColumnCount(); i++) {
			for (int j = 0; j < this.getRowCount(); j++) {
				if (this.isCellSelected(i, j))
					cells.add( new CellCoordinates(i,j) );
			}
		}
		return (CellCoordinates[]) cells.toArray(new CellCoordinates[0]);
	}

	public void dragEnter(DropTargetDragEvent dtde) {
		// TODO
		System.out.println("dragEnter");
	}

	public void dragOver(DropTargetDragEvent dtde) {
		 // See who we are over...
		CellCoordinates overCellCoordinates = this.locationToCellCoord( dtde.getLocation());
        if( overCellCoordinates != null && !overCellCoordinates.equals(this.overCellCoordinates) ) {
            // If the value has changed from what we were previously over
            // then change the selected object to the one we are over; this
            // is a visual representation that this is where the drop will occur
            this.overCellCoordinates = overCellCoordinates;

            currSelectedCell = this.overCellCoordinates;
            System.out.println("Current cell  ["+currSelectedCell.i+","+currSelectedCell.j+"]");

        }
	}

	public void dropActionChanged(DropTargetDragEvent dtde) {
		// TODO Auto-generated method stub
	}

	public void drop(DropTargetDropEvent dtde) {
		try {
			Transferable transferable = dtde.getTransferable();
			if( transferable.isDataFlavorSupported( DataFlavor.stringFlavor ) ) {
				dtde.acceptDrop( DnDConstants.ACTION_MOVE );

				// Find out where the item was dropped
				CellCoordinates newCellCoord = locationToCellCoord(dtde.getLocation());

				// Get the items out of the transferable object and build an
				// array out of them...
				String s = ( String ) transferable.getTransferData( DataFlavor.stringFlavor );
				StringTokenizer st = new StringTokenizer( s );
				ArrayList items = new ArrayList();
				while( st.hasMoreTokens() ) {
					items.add( st.nextToken() );
				}

				JDnDTableModel model = (JDnDTableModel) this.getModel();

                // If we are dragging from our this to our list them move the items,
                // otherwise just add them...
                if( this.dragging ) {
                    //model.itemsMoved( newIndex, items );
                    model.itemsMoved( newCellCoord, this.selectedCells );
                } else {
                    model.insertItems( newCellCoord, items );
                }

                // Update the selected cells
                /* TODO
                int[] newIndicies = new int[ items.size() ];
                for( int i=0; i<items.size(); i++ ) {
                    newIndicies[ i ] = newIndex + i;
                }
                this.setSelectedIndices( newIndicies );
                */
                // Reset the over index
                this.overCellCoordinates = null;

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

	public void dragExit(DropTargetEvent dte) {
		// TODO Auto-generated method stub
		System.out.println("dragExit");
	}

}

class CellCoordinates {
	int i, j;

	public CellCoordinates(int i, int j) {
		this.i = i;
		this.j = j;
	}

	public boolean equals(Object obj) {
		if (!(obj instanceof CellCoordinates))
			return false;

		CellCoordinates other = (CellCoordinates) obj;
		return (this.i == other.i) && (this.j == other.j);
	}
}
