/*
 * Created on 02-mar-2004
 *
 * gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
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
package com.iver.cit.gvsig.project.documents.layout.gui;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Array;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.extensionpoint.ExtensionPoint;
import org.gvsig.tools.extensionpoint.ExtensionPoint.Extension;

import com.iver.cit.gvsig.project.documents.IContextMenuAction;
import com.iver.cit.gvsig.project.documents.layout.contextmenu.gui.AbstractLayoutContextMenuAction;
import com.iver.cit.gvsig.project.documents.layout.contextmenu.gui.BeforeLayoutMenuEntry;
import com.iver.cit.gvsig.project.documents.layout.contextmenu.gui.BehindLayoutMenuEntry;
import com.iver.cit.gvsig.project.documents.layout.contextmenu.gui.CancelLayoutMenuEntry;
import com.iver.cit.gvsig.project.documents.layout.contextmenu.gui.CopyLayoutMenuEntry;
import com.iver.cit.gvsig.project.documents.layout.contextmenu.gui.CutLayoutMenuEntry;
import com.iver.cit.gvsig.project.documents.layout.contextmenu.gui.PasteLayoutMenuEntry;
import com.iver.cit.gvsig.project.documents.layout.contextmenu.gui.PositionLayoutMenuEntry;
import com.iver.cit.gvsig.project.documents.layout.contextmenu.gui.PropertyLayoutMenuEntry;
import com.iver.cit.gvsig.project.documents.layout.contextmenu.gui.RefreshLayoutMenuEntry;
import com.iver.cit.gvsig.project.documents.layout.contextmenu.gui.SelectAllLayoutMenuEntry;
import com.iver.cit.gvsig.project.documents.layout.contextmenu.gui.SimplifyLayoutMenuEntry;
import com.iver.cit.gvsig.project.documents.layout.contextmenu.gui.TerminateLayoutMenuEntry;
import com.iver.cit.gvsig.project.documents.layout.fframes.IFFrame;


/**
 * Menu de botón derecho para el Layout.
 * Se pueden añadir entradas facilmente desde una extensión,
 * creando una clase derivando de LayoutMenuEntry, y añadiendola en
 * estático (o en tiempo de carga de la extensión) a FPopupMenu.
 * (Las entradas actuales están hechas de esa manera).
 *
 * @author Vicente Caballero Navarro
 *
 */

public class FPopupMenu extends JPopupMenu {
	/**
	 *
	 */
	private static final long serialVersionUID = 5340224239252101704L;
	//private static ArrayList menuEntrys = new ArrayList();
    protected Layout layout;
    private IFFrame[] selecteds;
    private static String extPoint="Layout_PopupActions";
    //private JMenuItem capa;
    // Lo de fijar la fuente es porque en linux se veía mal si no se fija.
    // TODO: Esto no funcionará para idiomas como el chino. Hay que cambiarlo.
    public final static Font theFont = new Font("SansSerif", Font.PLAIN, 10);

    public static void registerExtensionPoint() {

    	ExtensionPoint extensionPoint = ToolsLocator.getExtensionPointManager()
				.add(extPoint);
		extensionPoint.append("Terminate", "", new TerminateLayoutMenuEntry());
		extensionPoint.append("Cancel", "", new CancelLayoutMenuEntry());
		extensionPoint.append("Copy", "", new CopyLayoutMenuEntry());
		extensionPoint.append("Cut", "", new CutLayoutMenuEntry());
		extensionPoint.append("Paste", "", new PasteLayoutMenuEntry());
		extensionPoint.append("Simplify", "", new SimplifyLayoutMenuEntry());
		extensionPoint.append("Property", "", new PropertyLayoutMenuEntry());
		extensionPoint.append("SelectAll", "", new SelectAllLayoutMenuEntry());
		extensionPoint.append("Behind", "", new BehindLayoutMenuEntry());
		extensionPoint.append("Before", "", new BeforeLayoutMenuEntry());
		extensionPoint.append("Position", "", new PositionLayoutMenuEntry());
		extensionPoint.append("Refresh", "", new RefreshLayoutMenuEntry());

    }
    /**
     * Creates a new FPopupMenu object.
     *
     * @param nodo DOCUMENT ME!
     * @param vista DOCUMENT ME!
     */
    public FPopupMenu(Layout layout) {
        super();
        this.initialize(layout);
    }

    private void initialize(Layout layout) {
        this.layout = layout;
		this.selecteds = this.layout.getLayoutContext().getFFrameSelected();
		IContextMenuAction[] actions = this.getActionList();
		this.createMenuElements(actions);
    }

    private IContextMenuAction[] getActionList() {
		ArrayList<AbstractLayoutContextMenuAction> actionArrayList = new ArrayList<AbstractLayoutContextMenuAction>();
		Iterator<Extension> iter = ToolsLocator.getExtensionPointManager().get(
				extPoint).iterator();
		AbstractLayoutContextMenuAction action;
		while (iter.hasNext()) {
			action = null;
			try {
				action = (AbstractLayoutContextMenuAction) iter.next().create();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			if (action != null) {
				action.setLayout(layout);
				if (action.isVisible(null,this.selecteds)) {
					actionArrayList.add(action);
				}
			}
		}
		IContextMenuAction[] result = (IContextMenuAction[])Array.newInstance(IContextMenuAction.class,actionArrayList.size());
		System.arraycopy(actionArrayList.toArray(),0,result,0,actionArrayList.size());
		Arrays.sort(result,new CompareAction());

		return result;
    }

	public class CompareAction implements
			Comparator<IContextMenuAction> {

		public int compare(IContextMenuAction o1, IContextMenuAction o2) {
			NumberFormat formater = NumberFormat.getInstance();
			formater.setMinimumIntegerDigits(3);
			String key1= ""+formater.format(o1.getGroupOrder())+o1.getGroup()+formater.format(o1.getOrder());
			String key2= ""+formater.format(o2.getGroupOrder())+o2.getGroup()+formater.format(o2.getOrder());
			return key1.compareTo(key2);
		}
	}

	private void createMenuElements(IContextMenuAction[] actions) {
		String group = null;
		for (int i=0;i < actions.length;i++) {
			IContextMenuAction action = actions[i];
			MenuItem item = new MenuItem(action.getText(),action);
			item.setFont(theFont);
			item.setEnabled(action.isEnabled(null,this.selecteds));
			if (!action.getGroup().equals(group)) {
				if (group != null) {
					this.addSeparator();
				}
				group = action.getGroup();
			}
			this.add(item);
		}

	}


	public class MenuItem extends JMenuItem implements ActionListener{
		/**
		 *
		 */
		private static final long serialVersionUID = 2518112362194914446L;
		private IContextMenuAction action;
		public MenuItem(String text,IContextMenuAction documentAction) {
			super(text);
			this.action = documentAction;
			String tip = this.action.getDescription();
			if (tip != null && tip.length() > 0) {
				this.setToolTipText(tip);
			}
			this.addActionListener(this);
		}

		public void actionPerformed(ActionEvent e) {
			this.action.execute(layout.getLayoutContext(), FPopupMenu.this.selecteds);
		}
	}
}
