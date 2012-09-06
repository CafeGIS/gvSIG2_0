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
* $Id: JDnDTableModel.java 13655 2007-09-12 16:28:55Z bsanchez $
* $Log$
* Revision 1.2  2007-09-12 16:28:23  bsanchez
* *** empty log message ***
*
* Revision 1.1  2007/08/20 08:34:46  evercher
* He fusionado LibUI con LibUIComponents
*
* Revision 1.2  2006/09/27 13:34:57  jaume
* *** empty log message ***
*
* Revision 1.1  2006/09/21 16:35:12  jaume
* *** empty log message ***
*
*
*/
package org.gvsig.gui.beans.controls.dnd;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

public class JDnDTableModel extends AbstractTableModel{
  private static final long serialVersionUID = 7508610018581206163L;
		public static final short ONLY_ALLOW_MOVING_COLUMNS = JDnDTable.ONLY_ALLOW_MOVING_COLUMNS;
    public static final short ONLY_ALLOW_MOVING_ROWS = JDnDTable.ONLY_ALLOW_MOVING_ROWS;
	private static final short FREE_CELL_MOVING = JDnDTable.FREE_CELL_MOVING;

    private short mode = ONLY_ALLOW_MOVING_COLUMNS;

    private String[] colNames;
    private Object[][] values;

    public JDnDTableModel(Object[][] values, String[] colNames) {
        super();
        this.colNames = colNames;
        this.values = values;
    }

    public int getColumnCount() {
        // TODO Auto-generated method stub
        return colNames.length;
    }

    public int getRowCount() {
        // TODO Auto-generated method stub
        return values[0].length;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        // TODO Auto-generated method stub
        return values[rowIndex][columnIndex];
    }

    public void itemsMoved(CellCoordinates newCellCoord, CellCoordinates[] coordinates) {
        // TODO Auto-generated method stub
        System.out.println("Model itemsMoved");


        switch (mode) {
        case ONLY_ALLOW_MOVING_COLUMNS:

        	// last selected cell defines which column
        	int srcColumnIndex = coordinates[coordinates.length-1].i;
        	int dstColumnIndex = newCellCoord.i;
        	System.out.println("movent columna "+ srcColumnIndex + " a columna "+dstColumnIndex);
            for (int i = 0; i < getRowCount(); i++) {
            	CellCoordinates srcCoord = new CellCoordinates(i, srcColumnIndex);
            	CellCoordinates dstCoord = new CellCoordinates(i, dstColumnIndex);
            	swapCells(srcCoord, dstCoord);
            }
            swapColumnNames(dstColumnIndex, srcColumnIndex);
            break;
        case ONLY_ALLOW_MOVING_ROWS:
            // last selected cell defines which row
            int rowIndex = coordinates[coordinates.length-1].i;
            coordinates = new CellCoordinates[getColumnCount()];
            for (int i = 0; i < coordinates.length; i++) {
                coordinates[i].i = rowIndex;
                coordinates[i].j = i;
            }
            newCellCoord.j = 0;
            break;
        case FREE_CELL_MOVING:
        	// TODO implement it
        	break;
        }
        this.fireTableDataChanged();
    }

    private void swapColumnNames(int ind1, int ind2) {
        String aux = colNames[ind2];
        colNames[ind2] = colNames[ind1];
        colNames[ind1] = aux;
    }

    private void swapCells(CellCoordinates cell1, CellCoordinates cell2) {
    	Object aux = values[cell1.i][cell1.j];
    	values[cell1.i][cell1.j] = values[cell2.i][cell2.j];
    	values[cell2.i][cell2.j] = aux;
    }

    public void insertItems(CellCoordinates cellCoord, ArrayList items) {
        // TODO Auto-generated method stub
        System.out.println("Model inserItems");
    }

	public void setSelectionMode(short mode) {
		this.mode = mode;
	}

}
