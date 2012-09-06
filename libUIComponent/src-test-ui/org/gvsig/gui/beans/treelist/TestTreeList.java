/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
*
* Copyright (C) 2007 IVER T.I. and Generalitat Valenciana.
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
*/
package org.gvsig.gui.beans.treelist;

import java.util.Hashtable;

import javax.swing.ListModel;

import org.gvsig.gui.beans.TestUI;
import org.gvsig.gui.beans.treelist.event.TreeListChangeEvent;
import org.gvsig.gui.beans.treelist.listeners.TreeListChangeListener;

public class TestTreeList implements TreeListChangeListener {
	private int               w     = 200;
	private int               h     = 380;
	private TestUI            frame = new TestUI("TestTreeList");
	private TreeListContainer tlist;

	public TestTreeList() {
		tlist = new TreeListContainer();

		tlist.addClass("uno", 0);
		tlist.addClass("dos", 1);
		tlist.addClass("tres", 2);
		tlist.addClass("cuatro", 3);
		tlist.addEntry("uno-uno","uno","1-1");
		tlist.addEntry("uno-dos","uno","1-2");
		tlist.addEntry("uno-tres","uno","1-3");
		tlist.addEntry("tres-uno","tres","3-1");
		tlist.addEntry("tres-dos","tres","3-2");
		tlist.addEntry("cuatro-uno","cuatro","4-1");
		tlist.addEntry("cuatro-dos","cuatro","4-2");
		tlist.addEntry("cuatro-tres","cuatro","4-3");
		tlist.addEntry("cuatro-cuatro","cuatro","4-4");
		tlist.addEntry("cuatro-cinco","cuatro","4-5");
		tlist.addEntry("cuatro-seis","cuatro","4-6");

		tlist.getTree().expandRow(0);
		tlist.addChangeSelectionListener(this);

		frame.getContentPane().add(tlist);
		frame.setSize(w, h);
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		new TestTreeList();
	}

	public void actionChangeSelection(TreeListChangeEvent e) {
		System.out.println("-");
		ListModel list = tlist.getListModel();
		for (int i=0; i<list.getSize(); i++) {
			Hashtable prueba = tlist.getMap();
			System.out.println(prueba.get(list.getElementAt(i)));
			System.out.println(list.getElementAt(i));
		}
	}
}