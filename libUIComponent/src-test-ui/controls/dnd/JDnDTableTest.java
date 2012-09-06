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
* $Id: JDnDTableTest.java 15818 2007-11-05 07:28:30Z bsanchez $
* $Log$
* Revision 1.2  2007-08-21 09:58:04  bsanchez
* - Quitados imports innecesarios
* - Quitado codigo no usado
* - Cambio de deprecated show por setVisible(true)
*
* Revision 1.1  2007/08/20 08:34:46  evercher
* He fusionado LibUI con LibUIComponents
*
* Revision 1.1  2006/09/27 13:34:57  jaume
* *** empty log message ***
*
*
*/
package controls.dnd;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.gvsig.gui.beans.TestUI;
import org.gvsig.gui.beans.controls.dnd.JDnDList;
import org.gvsig.gui.beans.controls.dnd.JDnDListModel;
import org.gvsig.gui.beans.controls.dnd.JDnDTable;
import org.gvsig.gui.beans.controls.dnd.JDnDTableModel;
/**
 * El model que ha de suportar les funcions d'una JDnDTable. Encara està en
 * proves.
 *
 * @author jaume
 */
public class JDnDTableTest {
	static String[] colNames = new String[] {"1234", "abcd", "qwerty"};
	static String[][] values = new String[][] {new String[] {"a1", "a2", "a3"},
										new String[] {"b1", "b2", "b3"},
										new String[] {"c1", "c2", "c3"}};

	public static void main(String args[]) {
		JFrame f = new TestUI("JDnDTableTest");
		JPanel content = new JPanel();
		content.setPreferredSize(new Dimension(500,400));

		JDnDTableModel model = new JDnDTableModel(values, colNames);
		JDnDListModel listModel = new JDnDListModel();
		listModel.addElement("a");
		listModel.addElement("b");
		listModel.addElement("c");

		JDnDTable tbl = new JDnDTable();

		tbl.setModel(model);
		tbl.setSize(new Dimension(450, 350));
		JDnDList list = new JDnDList();
		list.setModel(listModel);
		content.add(tbl);
		content.add(list);
		f.setContentPane(content);
		f.pack();
		f.setVisible(true);
	}
}